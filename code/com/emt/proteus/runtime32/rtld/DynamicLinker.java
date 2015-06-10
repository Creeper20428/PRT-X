/*     */ package com.emt.proteus.runtime32.rtld;
/*     */ 
/*     */ import com.emt.proteus.runtime32.CallArgs;
/*     */ import com.emt.proteus.runtime32.ExecutionEnvironment;
/*     */ import com.emt.proteus.runtime32.FunctionEndException;
/*     */ import com.emt.proteus.runtime32.Interpreter;
/*     */ import com.emt.proteus.runtime32.Option;
/*     */ import com.emt.proteus.runtime32.Option.Opt;
/*     */ import com.emt.proteus.runtime32.Processor;
/*     */ import com.emt.proteus.runtime32.RuntimeArgs;
/*     */ import com.emt.proteus.runtime32.RuntimeArgs.Aux;
/*     */ import com.emt.proteus.runtime32.ThreadContext;
/*     */ import com.emt.proteus.runtime32.Tls;
/*     */ import com.emt.proteus.runtime32.Trace;
/*     */ import com.emt.proteus.runtime32.api.IoSystem;
/*     */ import com.emt.proteus.runtime32.io.IoLib;
/*     */ import com.emt.proteus.runtime32.memory.MainMemory;
/*     */ import com.emt.proteus.runtime32.memory.ManagedMemory;
/*     */ import com.emt.proteus.utils.ILog;
/*     */ import java.io.File;
/*     */ import java.io.FileInputStream;
/*     */ import java.io.FileOutputStream;
/*     */ import java.io.IOException;
/*     */ import java.net.URL;
/*     */ import java.util.LinkedHashMap;
/*     */ import java.util.Map;
/*     */ import java.util.NoSuchElementException;
/*     */ import java.util.Random;
/*     */ import java.util.concurrent.locks.ReentrantLock;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class DynamicLinker
/*     */ {
/*     */   public static final String LINKER = "LINKER";
/*     */   public static final int LOWER_MEMORY_LIMIT = 16384;
/*     */   public static final int RTLD_LAZY = 1;
/*     */   public static final int RTLD_NOW = 2;
/*     */   public static final int RTLD_BINDING_MASK = 3;
/*     */   public static final int RTLD_NOLOAD = 4;
/*     */   public static final int RTLD_DEEPBIND = 8;
/*     */   public static final int __RTLD_DLOPEN = Integer.MIN_VALUE;
/*     */   public static final int __RTLD_SPROF = 1073741824;
/*     */   public static final int __RTLD_OPENEXEC = 536870912;
/*     */   public static final int __RTLD_CALLMAP = 268435456;
/*     */   public static final int __RTLD_SECURE = 67108864;
/*     */   public static final int __RTLD_AUDIT = 134217728;
/*     */   public static final int END_OF_FUNCTION = -1;
/*     */   public static final int TCB_SIZE = 4096;
/*     */   public static final int INITIAL_STATIC_TLS = 16384;
/*     */   public static final int GS_SYSTEM_CALL = -1073741820;
/*  59 */   private static final String[] CP_PREF = { "", "/", "lib/", "/lib/" };
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  66 */   private static final String[] DEFAULT_PATHS = { "/lib32", "/lib/i386-linux-gnu", "/usr/lib32", "/usr/lib/i386-linux-gnu", "/lib/i686-linux-gnu", "/usr/lib/i686-linux-gnu", "/usr/lib", "/usr/local/lib", "tests" };
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  79 */   private boolean useSymbolCache = true;
/*     */   
/*     */   private int pointerGuard;
/*     */   
/*     */   private int stackGuard;
/*     */   
/*     */   private String copyLibrary;
/*     */   private ThreadContext processRoot;
/*     */   private SearchPath searchPath;
/*     */   private MainMemory memory;
/*     */   private LinkerMap.Entry root;
/*     */   private LinkerMap.Entry tail;
/*     */   private LinkerModule linker;
/*     */   private int pltInterceptorAddress;
/*     */   private final ILog log;
/*     */   private final Map<String, Symbols.Symbol> exported;
/*     */   private int tlsDtvGeneration;
/*     */   private int tlsMaxIndex;
/*     */   private int lastTlsOffset;
/*     */   private int staticTlsSize;
/*  99 */   private int staticTlsAlign = 16;
/*     */   private ManagedMemory dtvStorage;
/*     */   private static final int LD_STRUCT_SIZE = 256;
/*     */   private CLinkEntry tmpMap;
/*     */   private RtLdState rtldState;
/* 104 */   private ReentrantLock lock = new ReentrantLock();
/*     */   private IoSystem ioSystem;
/*     */   
/*     */   public DynamicLinker(ThreadContext root) {
/* 108 */     this.ioSystem = root.getIoLib().getIoSystem();
/* 109 */     this.processRoot = root;
/* 110 */     this.memory = root.getMemory();
/* 111 */     this.log = root.getLog();
/* 112 */     Random rand = new Random();
/* 113 */     this.pointerGuard = 0;
/* 114 */     this.stackGuard = 0;
/*     */     
/* 116 */     Interpreter executor = this.processRoot.getExecutor();
/* 117 */     executor.addTrace(-1073741820, new Function.SystemCall());
/* 118 */     executor.addTrace(0, new Function.ZeroExec());
/* 119 */     executor.addTrace(-268431857, new ExitFunction());
/*     */     
/* 121 */     this.searchPath = SearchPath.compile(Option.libpath.arrayValue(DEFAULT_PATHS));
/* 122 */     this.exported = new LinkedHashMap();
/*     */     
/*     */ 
/* 125 */     this.tmpMap = new CLinkEntry();
/*     */   }
/*     */   
/*     */   public int getLinkerBase()
/*     */   {
/* 130 */     return this.linker.getBaseAddress();
/*     */   }
/*     */   
/*     */ 
/*     */   public void dispose() {}
/*     */   
/*     */   public void setCopyLibrary(String copyLibrary)
/*     */   {
/* 138 */     this.copyLibrary = copyLibrary;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void dl_start(Module program, RuntimeArgs args, int stack$)
/*     */     throws IOException
/*     */   {
/* 149 */     this.rtldState = new RtLdState();
/* 150 */     LinkerModule linker = new LinkerModule(this.rtldState);
/* 151 */     LinkerMap map = this.rtldState.getMap();
/* 152 */     CLinkEntry linkEntry = map.getLinker();
/* 153 */     this.tail = new LinkerMap.Entry(linker, linkEntry);
/* 154 */     this.root = new LinkerMap.Entry(program, map.getRoot());
/* 155 */     loadIntoMemory(linker, linkEntry);
/* 156 */     loadIntoMemory(program, map.getRoot());
/* 157 */     this.rtldState.syncMap();
/*     */     
/* 159 */     Module executable = this.root.getModule();
/* 160 */     executable.markAsExecutable();
/*     */     
/* 162 */     this.tail = this.root.append(this.tail);
/*     */     
/* 164 */     this.linker = linker;
/* 165 */     args.auxv(7, linker.getBaseAddress());
/* 166 */     args.auxv(31, executable.getFilePath());
/* 167 */     args.auxv(9, executable.getEntryPoint());
/* 168 */     args.auxv(3, executable.getPhdr());
/* 169 */     args.auxv(5, executable.getPhnum());
/* 170 */     args.auxv(4, executable.getPhent());
/* 171 */     args.auxv(32, -1073741820);
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 176 */     doBootStrap(linker, stack$);
/* 177 */     this.dtvStorage = new ManagedMemory(this.memory);
/*     */     
/* 179 */     ThreadContext mainThread = this.processRoot;
/* 180 */     this.staticTlsSize = calculateTlsSize();
/*     */     
/* 182 */     stack$ = initTCB(mainThread, stack$);
/* 183 */     int stack_end = stack$;
/* 184 */     initMainStack(mainThread, stack$, args);
/*     */     
/*     */ 
/*     */ 
/* 188 */     setInt(linker._dl_argv, stack$ - args.getArgs().length * 4);
/* 189 */     setInt(linker.__libc_stack_end, stack_end);
/* 190 */     setInt(linker.__libc_enable_secure, 0);
/* 191 */     LinkerMap.Entry current = this.tail.previous();
/*     */     
/* 193 */     while (current != null) {
/* 194 */       init(current, mainThread);
/* 195 */       current = current.previous();
/*     */     }
/*     */     
/* 198 */     int entryPoint = executable.getEntryPoint();
/* 199 */     info("Linker Started : Beginning execution @%08X", new Object[] { Integer.valueOf(entryPoint) });
/* 200 */     mainThread.begin(entryPoint);
/* 201 */     this.rtldState.getMap().printMap();
/*     */   }
/*     */   
/*     */   public void setInt(Symbols.Symbol symbol, int value) {
/* 205 */     this.memory.setDoubleWord(symbol.getRelocatedAddress(), value);
/*     */   }
/*     */   
/*     */   private void initMainStack(ThreadContext mainThread, int stack$, RuntimeArgs rtArgs) {
/* 209 */     mainThread.setESP(stack$);
/* 210 */     String[] env = rtArgs.getEnv();
/* 211 */     String[] args = rtArgs.getArgs();
/* 212 */     RuntimeArgs.Aux[] auvx = rtArgs.getAuxv();
/* 213 */     int[] envp = new int[env.length];
/* 214 */     int[] argp = new int[args.length];
/*     */     
/*     */ 
/* 217 */     for (int i = 0; i < auvx.length; i++) {
/* 218 */       String txt = auvx[i].getText();
/* 219 */       if (txt != null) {
/* 220 */         int ptr$ = mainThread.pushs(txt);
/* 221 */         auvx[i].setValue(ptr$);
/*     */       }
/*     */     }
/* 224 */     for (int i = 0; i < env.length; i++) {
/* 225 */       envp[i] = mainThread.pushs(env[i]);
/*     */     }
/* 227 */     for (int i = 0; i < args.length; i++) {
/* 228 */       argp[i] = mainThread.pushs(args[i]);
/*     */     }
/* 230 */     mainThread.stackAlign(4);
/* 231 */     mainThread.pushl(this.stackGuard).pushl(this.stackGuard).pushl(this.pointerGuard).pushl(this.pointerGuard);
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 236 */     int rand$ = mainThread.getESP();
/* 237 */     mainThread.stackAlign(16);
/*     */     
/* 239 */     mainThread.pushl(0);
/* 240 */     mainThread.pushl(0);
/* 241 */     for (int i = auvx.length - 1; i >= 0; i--) {
/* 242 */       mainThread.pushl(auvx[i].getValue()).pushl(auvx[i].getKey());
/*     */     }
/*     */     
/*     */ 
/* 246 */     mainThread.pushl(25).pushl(rand$);
/* 247 */     int auvx$ = mainThread.getESP();
/*     */     
/* 249 */     mainThread.pushl(0);
/* 250 */     for (int i = envp.length - 1; i >= 0; i--) {
/* 251 */       mainThread.pushl(envp[i]);
/*     */     }
/* 253 */     int envp$ = mainThread.esp();
/*     */     
/* 255 */     mainThread.push(envp$);
/* 256 */     int argv$ = mainThread.push(argp);
/* 257 */     mainThread.push(argp.length);
/* 258 */     mainThread.getLog().info("LINKER", "Stack initialized at %08X", new Object[] { Integer.valueOf(mainThread.getESP()) });
/*     */   }
/*     */   
/*     */   private void doBootStrap(LinkerModule linker, int stack$)
/*     */     throws IOException
/*     */   {
/* 264 */     this.pltInterceptorAddress = linker.pltInteceptor.getRelocatedAddress();
/*     */     
/* 266 */     this.lastTlsOffset = 0;
/* 267 */     this.tlsMaxIndex = 0;
/*     */     
/* 269 */     loadDependencies(this.root, 1000, true);
/* 270 */     assignTlsOffsets(this.root);
/* 271 */     LinkerMap.Entry current = this.tail.previous();
/*     */     
/*     */ 
/* 274 */     relocateReverse(current, null);
/*     */     
/* 276 */     relocateSingle(this.tail);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public int getStaticTlsSize()
/*     */   {
/* 285 */     return this.staticTlsSize;
/*     */   }
/*     */   
/*     */   public int getStaticTlsAlign() {
/* 289 */     return this.staticTlsAlign;
/*     */   }
/*     */   
/*     */   public synchronized int dlopen(ThreadContext ctx, String library, int flags) {
/* 293 */     LinkerMap.Entry newEntry = findModule(library);
/* 294 */     if (newEntry != null) {
/* 295 */       newEntry.open();
/*     */     }
/*     */     else {
/* 298 */       LinkerMap.Entry last = this.tail;
/*     */       try {
/* 300 */         ctx.warn("Open %s", new Object[] { library });
/* 301 */         newEntry = loadModule(library, this.searchPath, last, false, last);
/* 302 */         loadDependencies(newEntry, 1000, true);
/* 303 */         this.tail = this.tail.tail();
/* 304 */         assignTlsOffsets(newEntry);
/* 305 */         relocateReverse(this.tail, null);
/* 306 */         init(newEntry, ctx);
/*     */       } catch (IOException e) {
/* 308 */         e.printStackTrace();
/* 309 */         return 0;
/*     */       }
/*     */     }
/* 312 */     return newEntry.getMap().addressOf();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public int initTCB(ThreadContext ctx, int memory$)
/*     */   {
/* 325 */     int tlsSize = this.staticTlsSize;
/* 326 */     int tcbSize = 4096;
/* 327 */     memory$ -= tcbSize;
/* 328 */     int segbase = memory$;
/* 329 */     ctx.setThreadPointer(segbase);
/* 330 */     int dtv$ = allocateDtv(segbase);
/* 331 */     MainMemory memory = ctx.getMemory();
/*     */     
/* 333 */     ctx.setTCB(Tls.TCB_THR_DESC_PTR, segbase);
/* 334 */     ctx.setTCB(Tls.TCB_MULTI_THREAD, 0);
/* 335 */     ctx.setTCB(Tls.TCB_SYS_INFO, -1073741820);
/* 336 */     ctx.setTCB(Tls.TCB_STACK_GUARD, this.stackGuard);
/* 337 */     ctx.setTCB(Tls.TCB_PTR_GUARD, this.pointerGuard);
/* 338 */     ctx.setTCB(Tls.TCB_GSCOPE, 0);
/* 339 */     ctx.setTCB(Tls.TCB_PRIVATE_FUTEX, 128);
/* 340 */     ctx.setTCB(Tls.TCB_PRIVATE_TM, 0);
/*     */     
/* 342 */     allocateTlsInit(segbase, dtv$);
/* 343 */     memory$ -= tlsSize;
/* 344 */     return memory$;
/*     */   }
/*     */   
/*     */   public int allocateTlsInit(int threadPointer, int dtv$) {
/* 348 */     assert (threadPointer != dtv$);
/* 349 */     int tlsSize = this.staticTlsSize;
/* 350 */     this.memory.setDoubleWord(dtv$, this.tlsDtvGeneration);
/* 351 */     this.memory.setDoubleWord(dtv$ + 4, this.tlsMaxIndex);
/* 352 */     LinkerMap.Entry e = this.root;
/* 353 */     this.memory.memset(threadPointer - tlsSize, tlsSize, (byte)0);
/* 354 */     while (e != null) {
/* 355 */       Module module = e.getModule();
/* 356 */       if (module.hasTls()) {
/* 357 */         int offset = module.getTlsOffset();
/* 358 */         int init$ = module.getTlsInit();
/* 359 */         int tlsIndex = module.getTlsIndex();
/* 360 */         int len = module.getTlsInitSize();
/* 361 */         int ptr$ = initTLS(this.memory, threadPointer, tlsIndex, offset, init$, len);
/* 362 */         info("Initialized TLS %08X %08X %s ", new Object[] { Integer.valueOf(ptr$), Integer.valueOf(ptr$ + module.getTlsSize()), module });
/*     */       }
/* 364 */       e = e.next();
/*     */     }
/* 366 */     return threadPointer;
/*     */   }
/*     */   
/*     */   private static int initTLS(MainMemory memory, int threadPointer, int tlsIndex, int offset, int init$, int len) {
/* 370 */     int ptr$ = threadPointer - offset;
/*     */     
/* 372 */     memory.memcopy(init$, len, ptr$);
/* 373 */     Tls.setAddress(memory, threadPointer, tlsIndex, ptr$);
/* 374 */     return ptr$;
/*     */   }
/*     */   
/*     */   private int loadDependencies(LinkerMap.Entry current, int limit, boolean reorder) throws IOException
/*     */   {
/* 379 */     while (current != null) { limit--; if (limit == 0) break;
/* 380 */       load(current, reorder);
/* 381 */       current = current.next();
/*     */     }
/* 383 */     if (limit == 0) {
/* 384 */       throw new UnsupportedOperationException("Complex dependencies, may be circular references. Unsupported at this time");
/*     */     }
/*     */     
/* 387 */     return limit;
/*     */   }
/*     */   
/*     */   private void assignTlsOffsets(LinkerMap.Entry current)
/*     */   {
/* 392 */     while (current != null) {
/* 393 */       Module module = current.getModule();
/* 394 */       current = current.next();
/* 395 */       if (module.hasTls()) {
/* 396 */         int tlsIndex = module.getTlsIndex();
/* 397 */         if (tlsIndex == 0) {
/* 398 */           tlsIndex = ++this.tlsMaxIndex;
/*     */           
/* 400 */           if (tlsIndex == 1) {
/* 401 */             this.lastTlsOffset = round(module.getTlsSize(), module.getTlsAlign());
/*     */           } else {
/* 403 */             this.lastTlsOffset = round(this.lastTlsOffset + module.getTlsSize(), module.getTlsAlign());
/*     */           }
/* 405 */           module.setTlsIndex(tlsIndex);
/* 406 */           module.setTlsOffset(this.lastTlsOffset);
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */   
/* 412 */   private int calculateTlsSize() { return Math.max(this.lastTlsOffset, 16384); }
/*     */   
/*     */   private int round(int size, int align)
/*     */   {
/* 416 */     align = Math.max(this.staticTlsAlign, align);
/* 417 */     return size + align - 1 & (align - 1 ^ 0xFFFFFFFF);
/*     */   }
/*     */   
/*     */ 
/*     */   private void relocateReverse(LinkerMap.Entry entry, LinkerMap.Entry terminate)
/*     */   {
/* 423 */     while ((entry != terminate) && (entry != null)) {
/* 424 */       relocateSingle(entry);
/* 425 */       entry = entry.previous();
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   private void relocateSingle(LinkerMap.Entry current)
/*     */   {
/* 432 */     if (!current.isRelocated()) {
/* 433 */       Module module = current.getModule();
/* 434 */       int linkerInstallAddress = module.getGotPlt();
/* 435 */       if (linkerInstallAddress > 0) {
/* 436 */         int dynamicAddress = module.getDynamicAddress();
/* 437 */         int moduleKey = module.getMap().addressOf();
/* 438 */         this.memory.setDoubleWord(linkerInstallAddress, dynamicAddress);
/* 439 */         this.memory.setDoubleWord(linkerInstallAddress + 4, moduleKey);
/* 440 */         this.memory.setDoubleWord(linkerInstallAddress + 8, this.pltInterceptorAddress);
/*     */       }
/* 442 */       info("Relocating %08X %s", new Object[] { Integer.valueOf(module.getBaseAddress()), module.getFilePath(), current });
/* 443 */       module.relocate(this, this.memory);
/* 444 */       current.relocated();
/*     */     }
/*     */   }
/*     */   
/*     */   private void load(LinkerMap.Entry parent, boolean reorder) throws IOException {
/* 449 */     ensureInMemory(parent);
/* 450 */     Module module = parent.getModule();
/* 451 */     String[] dependencies = module.getDependencies();
/* 452 */     String[] rpath = module.getSearchPaths();
/* 453 */     SearchPath searchPath = this.searchPath.append(rpath);
/* 454 */     LinkerMap.Entry appendTo = parent;
/* 455 */     for (int i = 0; i < dependencies.length; i++) {
/* 456 */       String dependency = dependencies[i];
/* 457 */       appendTo = loadModule(dependency, searchPath, appendTo, reorder, parent);
/*     */     }
/*     */   }
/*     */   
/*     */   private LinkerMap.Entry loadModule(String name, SearchPath searchPath, LinkerMap.Entry appendTo, boolean reorder, LinkerMap.Entry parent) throws IOException
/*     */   {
/* 463 */     LinkerMap.Entry depend = findModule(name);
/* 464 */     if (depend == null)
/*     */     {
/*     */ 
/* 467 */       Module dep = createModule(searchPath, name);
/* 468 */       if (dep == null) {
/* 469 */         fatal(" Unable to locate dependency %s\n Aborting...", new Object[] { name });
/*     */       }
/* 471 */       debug("file=%s;  needed by %s", new Object[] { dep.getName(), parent.getFilePath() });
/* 472 */       LinkerMap.Entry entry = this.rtldState.newEntry(appendTo, dep);
/* 473 */       return ensureInMemory(entry);
/*     */     }
/* 475 */     if ((reorder) && (!depend.isInitCalled()))
/*     */     {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 482 */       if (depend.isBefore(parent)) {
/* 483 */         info("%s depends on %s - inserting %s before  %s", new Object[] { parent.getName(), depend.getName(), parent.getName(), depend.getName() });
/* 484 */         this.rtldState.getMap().insertBefore(parent, depend);
/*     */       }
/*     */     }
/*     */     
/*     */ 
/*     */ 
/* 490 */     return appendTo;
/*     */   }
/*     */   
/*     */   private LinkerMap.Entry ensureInMemory(LinkerMap.Entry entry) throws IOException
/*     */   {
/* 495 */     if (entry.isInMemory()) return entry;
/* 496 */     Module module = entry.getModule();
/* 497 */     CLinkEntry map = entry.getMap();
/* 498 */     loadIntoMemory(module, map);
/* 499 */     return entry;
/*     */   }
/*     */   
/*     */   private void loadIntoMemory(Module module, CLinkEntry map) throws IOException
/*     */   {
/* 504 */     int minimumBaseAddress = Math.max(this.processRoot.getMemory().getBreakAddress(), 16384);
/*     */     
/* 506 */     module.load(this.memory, minimumBaseAddress, map);
/* 507 */     int baseAddress = map.getBaseAddress();
/* 508 */     int mapStart = map.getMapStart();
/* 509 */     int mapEnd = map.getMapEnd();
/* 510 */     int gotPLT = map.info.getGotPLT();
/* 511 */     String filePath = module.getFilePath();
/* 512 */     info("Loaded %08X %08X (+%08X) GOT=%08X MAP=%08X %s", new Object[] { Integer.valueOf(mapStart), Integer.valueOf(mapEnd), Integer.valueOf(baseAddress), Integer.valueOf(gotPLT), Integer.valueOf(map.addressOf()), filePath });
/* 513 */     updateBreakAddress(map);
/* 514 */     this.memory.addFormat(mapStart, mapEnd, baseAddress, "[%08X:%06X] " + filePath);
/*     */   }
/*     */   
/*     */   private void updateBreakAddress(CLinkEntry map)
/*     */   {
/* 519 */     int newBreakAddress = Math.max(this.processRoot.getMemory().getBreakAddress(), map.getMapEnd());
/*     */     
/* 521 */     this.memory.setBreakAddress(newBreakAddress);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private Module createModule(SearchPath search, String dependency)
/*     */     throws IOException
/*     */   {
/* 537 */     Module module = findJavaModule(search, dependency);
/* 538 */     if (module == null)
/*     */     {
/* 540 */       module = findElfModule(search, dependency);
/*     */     }
/* 542 */     return module;
/*     */   }
/*     */   
/*     */   private LinkerMap.Entry init(LinkerMap.Entry entry, ThreadContext thread)
/*     */   {
/* 547 */     if (!entry.isInitCalled())
/*     */     {
/* 549 */       int depth = thread.getDepth();
/*     */       
/* 551 */       warn("calling init: %s", new Object[] { entry.getFilePath() });
/* 552 */       entry.getModule().init(thread);
/* 553 */       entry.initCalled();
/* 554 */       warn("exit init: %s", new Object[] { entry.getFilePath() });
/* 555 */       int newDepth = thread.getDepth();
/* 556 */       if (depth != newDepth) {
/* 557 */         warn("Stack depth changed following init %2d <> %2d : Resetting to %2d", new Object[] { Integer.valueOf(depth), Integer.valueOf(newDepth), Integer.valueOf(depth) });
/* 558 */         thread.setDepth(depth);
/*     */       }
/*     */     }
/* 561 */     return entry;
/*     */   }
/*     */   
/*     */   private LinkerMap.Entry fini(LinkerMap.Entry entry, ThreadContext ctx) {
/* 565 */     if (!entry.isFiniCalled()) {
/* 566 */       info("calling fini: %s", new Object[] { entry.getFilePath() });
/* 567 */       entry.getModule().fini(ctx);
/* 568 */       entry.finiCalled();
/*     */     }
/* 570 */     return entry;
/*     */   }
/*     */   
/*     */   public LinkerMap.Entry findModule(String name) {
/* 574 */     LinkerMap.Entry e = this.root;
/* 575 */     while (e != null) {
/* 576 */       debug("%s %s", new Object[] { e.getName(), name });
/* 577 */       if (e.getName().equals(name)) return e;
/* 578 */       e = e.next();
/*     */     }
/* 580 */     return null;
/*     */   }
/*     */   
/*     */   public LinkerMap.Entry findModule(int address) {
/* 584 */     LinkerMap.Entry e = this.root;
/* 585 */     while (e != null) {
/* 586 */       if (e.getBaseAddress() == address) return e;
/* 587 */       e = e.next();
/*     */     }
/*     */     
/* 590 */     return null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Symbols.Symbol findSymbol(Module src, String name, boolean weak, boolean searchSrc)
/*     */   {
/* 602 */     if (name == null) { throw new NoSuchElementException("Cannot search for symbol with null name");
/*     */     }
/*     */     
/* 605 */     LinkerMap.Entry e = this.root;
/* 606 */     while (e != null) {
/* 607 */       Module module = e.getModule();
/* 608 */       if ((searchSrc) || (src != module)) {
/* 609 */         Symbols.Symbol symbol = module.find(name);
/* 610 */         debug("symbol=%s;  lookup in file=%s", new Object[] { name, e.getFilePath() });
/* 611 */         if (symbol != null) {
/* 612 */           debug("binding file %s to %s : normal symbol `%s' ", new Object[] { src.getFilePath(), symbol.getModule().getFilePath(), symbol });
/* 613 */           return symbol;
/*     */         }
/*     */       }
/* 616 */       e = e.next();
/*     */     }
/* 618 */     throw new NoSuchElementException("Unable to locate " + name);
/*     */   }
/*     */   
/*     */   public Symbols.Symbol dlSymbol(int mapAddress, String symbolName)
/*     */   {
/* 623 */     int loadedAt = this.memory.getDoubleWord(mapAddress);
/* 624 */     LinkerMap.Entry entry = findModule(loadedAt);
/* 625 */     Module module = entry.getModule();
/* 626 */     Symbols.Symbol symbol = module.find(symbolName);
/*     */     
/* 628 */     return symbol;
/*     */   }
/*     */   
/*     */   public int getTlsIndex(Symbols.Symbol dynSymbol) {
/* 632 */     return dynSymbol.getModule().getTlsIndex();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public int symbol(int handle, String name)
/*     */   {
/* 643 */     LinkerMap.Entry entry = findModule(handle);
/*     */     
/* 645 */     if (entry == null) throw new RuntimeException("Invalid Handle " + Integer.toHexString(handle));
/* 646 */     Symbols.Symbol symbol = entry.getModule().find(name);
/* 647 */     return symbol != null ? symbol.getRelocatedAddress() : -1;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private JavaModule findJavaModule(SearchPath paths, String dependency)
/*     */   {
/* 659 */     return null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private ElfModule findElfModule(SearchPath paths, String dependency)
/*     */     throws IOException
/*     */   {
/* 670 */     info("Searching for:%s", new Object[] { dependency });
/* 671 */     URL url = searchClassPath(dependency);
/* 672 */     if (url == null) {
/* 673 */       SearchPath current = paths;
/* 674 */       while (current != null)
/*     */       {
/* 676 */         File f = new File(current.getPath(), dependency);
/* 677 */         if (f.exists()) {
/* 678 */           String absolutePath = f.getAbsolutePath();
/* 679 */           info("Using File library %s", new Object[] { absolutePath });
/* 680 */           if ((this.copyLibrary != null) && (!this.copyLibrary.isEmpty())) {
/* 681 */             copy(f, this.copyLibrary);
/*     */           }
/* 683 */           return new ElfModule(absolutePath);
/*     */         }
/* 685 */         current = current.getNext();
/*     */       }
/* 687 */       return null;
/*     */     }
/* 689 */     info("Using resource library %s", new Object[] { url });
/* 690 */     return new ElfModule(url);
/*     */   }
/*     */   
/*     */   private URL searchClassPath(String dependency)
/*     */   {
/* 695 */     ClassLoader classLoader = DynamicLinker.class.getClassLoader();
/* 696 */     if (dependency.startsWith("/")) {
/* 697 */       dependency = dependency.substring(1);
/*     */     }
/* 699 */     URL resource = null;
/* 700 */     for (int i = 0; i < CP_PREF.length; i++) {
/* 701 */       String s = CP_PREF[i];
/* 702 */       String res = s + dependency;
/* 703 */       debug("Search Class Path for %s", new Object[] { res });
/* 704 */       resource = classLoader.getResource(res);
/* 705 */       if (resource != null) return resource;
/*     */     }
/* 707 */     return resource;
/*     */   }
/*     */   
/*     */   public void addGnuIndirectionCall(int callAddress, Symbols.Symbol symbol, int gotEntry) {
/* 711 */     Function indirection = new GnuIndirection(callAddress, symbol, gotEntry);
/* 712 */     addMapping(callAddress, indirection);
/*     */   }
/*     */   
/*     */   public Function ensureSymbolFunction(Symbols.Symbol symbol) {
/* 716 */     int executionAddress = symbol.getRelocatedAddress();
/*     */     
/* 718 */     Object object = symbol.getObject();
/*     */     Function t;
/* 720 */     Function t; if ((object instanceof Trace)) {
/* 721 */       t = (Function)object;
/*     */     } else {
/* 723 */       t = Function.create(symbol);
/*     */     }
/* 725 */     addMapping(executionAddress, t);
/* 726 */     return t;
/*     */   }
/*     */   
/* 729 */   public void addTrampoline(int callAddress, Symbols.Symbol symbol) { Function function = ensureSymbolFunction(symbol);
/* 730 */     Function trampoline = Function.trampoline(callAddress, symbol.getRelocatedAddress(), function);
/* 731 */     addMapping(callAddress, trampoline);
/*     */   }
/*     */   
/* 734 */   public void addMapping(int callAddress, Trace trace) { this.processRoot.getExecutor().addTrace(callAddress, trace); }
/*     */   
/*     */ 
/*     */ 
/*     */   public ILog getLog()
/*     */   {
/* 740 */     return this.log;
/*     */   }
/*     */   
/*     */   public void debug(String fmt, Object... args) {
/* 744 */     this.log.debug("LINKER", fmt, args);
/*     */   }
/*     */   
/*     */   public void info(String fmt, Object... args)
/*     */   {
/* 749 */     this.log.info("LINKER", fmt, args);
/*     */   }
/*     */   
/*     */   public void warn(String fmt, Object... args)
/*     */   {
/* 754 */     this.log.warn("LINKER", fmt, args);
/*     */   }
/*     */   
/*     */   public void fatal(String fmt, Object... args) {
/* 758 */     this.log.fatal("LINKER", fmt, args);
/*     */   }
/*     */   
/*     */   public int allocateDtv(int threadPointer) {
/* 762 */     return Tls.allocateDTV(this.dtvStorage, threadPointer, this.tlsMaxIndex);
/*     */   }
/*     */   
/*     */   public int deallocateDtv(int dtv$) {
/* 766 */     this.dtvStorage.free(dtv$);
/* 767 */     return dtv$;
/*     */   }
/*     */   
/*     */   private static void copy(File f, String copyLibrary)
/*     */   {
/* 772 */     File toFile = new File(copyLibrary, f.getName());
/* 773 */     byte[] buffer = new byte[81920];
/* 774 */     FileInputStream input = null;
/* 775 */     FileOutputStream output = null;
/*     */     try {
/* 777 */       output = new FileOutputStream(toFile);
/* 778 */       input = new FileInputStream(f);
/*     */       for (;;) {
/* 780 */         int n = input.read(buffer);
/* 781 */         if (n < 0) return;
/* 782 */         if (n > 0)
/* 783 */           output.write(buffer, 0, n);
/*     */       }
/*     */       return;
/*     */     } catch (IOException e) {
/* 787 */       e.fillInStackTrace();
/*     */     } finally {
/* 789 */       if (output != null) {
/*     */         try {
/* 791 */           output.close();
/*     */         } catch (IOException e) {
/* 793 */           e.printStackTrace();
/*     */         }
/*     */       }
/* 796 */       if (input != null) {
/*     */         try {
/* 798 */           input.close();
/*     */         } catch (IOException e) {
/* 800 */           e.printStackTrace();
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void unlock()
/*     */   {
/* 827 */     this.lock.unlock();
/*     */   }
/*     */   
/*     */   public void lock() {
/* 831 */     this.lock.lock();
/*     */   }
/*     */   
/*     */   public class ExitFunction
/*     */     extends Function
/*     */   {
/*     */     public ExitFunction() {}
/*     */     
/*     */     public void executeImpl(ThreadContext ctx, ExecutionEnvironment env)
/*     */     {
/* 841 */       throw new FunctionEndException();
/*     */     }
/*     */   }
/*     */   
/*     */   public class GnuIndirection extends Function {
/*     */     private final int callAddress;
/*     */     private final Symbols.Symbol symbol;
/*     */     private final int gotEntry;
/* 849 */     private int count = 0;
/*     */     
/*     */     public GnuIndirection(int callAddress, Symbols.Symbol symbol, int gotEntry) {
/* 852 */       super(true);
/* 853 */       this.callAddress = callAddress;
/* 854 */       this.symbol = symbol;
/* 855 */       this.gotEntry = gotEntry;
/*     */     }
/*     */     
/*     */     public void executeImpl(ThreadContext ctx, ExecutionEnvironment env)
/*     */     {
/* 860 */       if (this.count != 0) throw new IllegalStateException(this.symbol + " should only be called once");
/* 861 */       int ret = ctx.peek();
/* 862 */       int string = CallArgs.cdecl(ctx, 0);
/* 863 */       ctx.cpu.eip = this.symbol.getRelocatedAddress();
/* 864 */       ctx.execute(this.symbol);
/* 865 */       int realFunction = ctx.cpu.eax;
/* 866 */       ctx.memory.setDoubleWord(this.gotEntry, realFunction);
/*     */       
/* 868 */       if (ctx.cpu.eip != ret) {
/* 869 */         ctx.warn("Expected return to be %08X not %08X", new Object[] { Integer.valueOf(ret), Integer.valueOf(ctx.cpu.eip) });
/*     */       }
/* 871 */       ctx.push(ret);
/* 872 */       Function indirection = Function.indirection(this.symbol, realFunction);
/* 873 */       int string2 = CallArgs.cdecl(ctx, 0);
/* 874 */       if (string2 != string) {
/* 875 */         ctx.warn("Argument changed %08X to %08X", new Object[] { Integer.valueOf(string), Integer.valueOf(string2) });
/*     */       }
/* 877 */       DynamicLinker.this.addMapping(this.callAddress, indirection);
/* 878 */       indirection.execute(ctx, env);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Joey\Downloads\codejar.jar!\com\emt\proteus\runtime32\rtld\DynamicLinker.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */