/*      */ package com.emt.proteus.runtime32;
/*      */ 
/*      */ import com.emt.proteus.debugger.RemoteGDB;
/*      */ import com.emt.proteus.decoder.Disassembler;
/*      */ import com.emt.proteus.decoder.X86Opcode;
/*      */ import com.emt.proteus.elf.Elf;
/*      */ import com.emt.proteus.elf.ElfHeader;
/*      */ import com.emt.proteus.elf.ProgramHeader;
/*      */ import com.emt.proteus.runtime32.memory.AbstractMemory;
/*      */ import com.emt.proteus.runtime32.memory.MainMemory;
/*      */ import com.emt.proteus.runtime32.memory.MemoryInputStream;
/*      */ import java.io.BufferedReader;
/*      */ import java.io.File;
/*      */ import java.io.PrintStream;
/*      */ import java.util.Iterator;
/*      */ import java.util.List;
/*      */ import java.util.Set;
/*      */ 
/*      */ public class ProcessEmulator
/*      */ {
/*      */   public static final int X86_STACK_SIZE = 8388608;
/*      */   public static final int MAX_ARG_PAGES = 33;
/*      */   public static final int TARGET_PAGE_SIZE = 4096;
/*   24 */   public static long STACK_BASE = 3221090304L;
/*      */   
/*      */   public static final int DLINFO_ITEMS = 13;
/*      */   
/*      */   public static final int AT_NULL = 0;
/*      */   
/*      */   public static final int AT_IGNORE = 1;
/*      */   
/*      */   public static final int AT_EXECFD = 2;
/*      */   
/*      */   public static final int AT_PHDR = 3;
/*      */   public static final int AT_PHENT = 4;
/*      */   public static final int AT_PHNUM = 5;
/*      */   public static final int AT_PAGESZ = 6;
/*      */   public static final int AT_BASE = 7;
/*      */   public static final int AT_FLAGS = 8;
/*      */   public static final int AT_ENTRY = 9;
/*      */   public static final int AT_NOTELF = 10;
/*      */   public static final int AT_UID = 11;
/*      */   public static final int AT_EUID = 12;
/*      */   public static final int AT_GID = 13;
/*      */   public static final int AT_EGID = 14;
/*      */   public static final int AT_PLATFORM = 15;
/*      */   public static final int AT_HWCAP = 16;
/*      */   public static final int AT_CLKTCK = 17;
/*      */   public static final int AT_RANDOM = 25;
/*      */   public Elf elf;
/*      */   public final ThreadContext context;
/*      */   public final MainMemory memory;
/*   53 */   public static boolean randomise = false;
/*      */   public static final int COMPARE_SKIP = 100000;
/*      */   
/*      */   public ProcessEmulator(Elf elf, String[] args, MainMemory addrSpace) {
/*   57 */     this(elf, args, getEnv(), addrSpace);
/*      */   }
/*      */   
/*      */   public ProcessEmulator(Elf elf, String[] args, String[] env, MainMemory addrSpace)
/*      */   {
/*   62 */     this.elf = elf;
/*   63 */     this.memory = addrSpace;
/*   64 */     this.context = new ThreadContext(new BasicBlockExecutionEnvironment(true), this.memory, new com.emt.proteus.utils.ILog.Err());
/*      */     
/*      */ 
/*      */ 
/*   68 */     int maxAddr = 0;
/*   69 */     for (int i = 0; i < elf.getHeader().getProgramHeaderCount(); i++)
/*      */     {
/*   71 */       ProgramHeader phdr = elf.getProgramHeader(i);
/*   72 */       if (phdr.getType() == 1)
/*      */       {
/*      */ 
/*   75 */         long vaddr = 0xFFFFFFFF & phdr.getVirtualAddress();
/*   76 */         int size = phdr.getMemorySize();
/*      */         
/*   78 */         byte[] data = elf.getProgramSegment(i).getData();
/*   79 */         if (data == null) {
/*   80 */           data = new byte[0];
/*      */         }
/*   82 */         long v = vaddr;
/*   83 */         for (int j = 0; j < size; j++)
/*      */         {
/*   85 */           byte val = 0;
/*   86 */           if (j >= data.length) break;
/*   87 */           val = data[j];
/*      */           
/*      */ 
/*      */ 
/*   91 */           this.memory.setByte((int)v, val);
/*   92 */           v += 1L;
/*      */         }
/*      */         
/*   95 */         if ((phdr.getFlags() & 0x2) == 0)
/*      */         {
/*   97 */           for (long addr = vaddr; addr < vaddr + size; addr += 4096L) {
/*   98 */             this.memory.setPageReadOnly((int)addr);
/*      */           }
/*      */         }
/*  101 */         boolean execute = (phdr.getFlags() & 0x1) != 0;
/*      */         
/*  103 */         for (long addr = vaddr; addr < vaddr + size; addr += 4096L) {
/*  104 */           this.memory.setPageExecutePermission((int)addr, execute);
/*      */         }
/*  106 */         maxAddr = Math.max(maxAddr, (int)((addr & 0xFFFFFFFFFFFFF000) + 4096L));
/*      */       } }
/*  108 */     this.memory.setBreakAddress(maxAddr);
/*      */     
/*  110 */     this.context.cpu.eip = elf.getHeader().getEntryPoint();
/*  111 */     setupStack(elf, this.context.cpu, this.memory, args, env);
/*      */   }
/*      */   
/*      */   public ProcessEmulator(Elf elf, MainMemory mem)
/*      */   {
/*  116 */     this.memory = mem;
/*  117 */     this.context = new ThreadContext(new BasicBlockExecutionEnvironment(true), this.memory, new com.emt.proteus.utils.ILog.Err());
/*  118 */     this.elf = elf;
/*      */   }
/*      */   
/*      */ 
/*      */   public static String[] getEnv()
/*      */   {
/*  124 */     java.util.Map<String, String> envmap = System.getenv();
/*  125 */     String[] env = new String[envmap.size()];
/*  126 */     int i = 0;
/*  127 */     for (String key : envmap.keySet())
/*  128 */       env[(i++)] = (key + "=" + (String)envmap.get(key));
/*  129 */     java.util.Arrays.sort(env);
/*  130 */     return env;
/*      */   }
/*      */   
/*      */   private static byte[] getBytes(String src)
/*      */   {
/*      */     try
/*      */     {
/*  137 */       return src.getBytes("UTF-8");
/*      */     }
/*      */     catch (java.io.UnsupportedEncodingException e) {}
/*  140 */     return null;
/*      */   }
/*      */   
/*      */   private static void setupStack(Elf elf, Processor cpu, MainMemory mem, String[] args, String[] env)
/*      */   {
/*  145 */     boolean pushPointer = false;
/*      */     
/*      */ 
/*  148 */     int size = 8388608;
/*  149 */     long stackBase = STACK_BASE;
/*      */     
/*  151 */     byte[] buf = new byte[135168];
/*  152 */     int index = buf.length - 4;
/*      */     
/*  154 */     int[] argSizes = new int[args.length];
/*  155 */     int[] envSizes = new int[env.length];
/*      */     
/*      */ 
/*  158 */     byte[] name = getBytes(args[0]);
/*  159 */     index -= name.length + 1;
/*  160 */     System.arraycopy(name, 0, buf, index, name.length);
/*      */     
/*      */ 
/*  163 */     for (int i = env.length - 1; i >= 0; i--)
/*      */     {
/*  165 */       name = getBytes(env[i]);
/*  166 */       System.arraycopy(name, 0, buf, index - name.length - 1, name.length);
/*  167 */       envSizes[i] = name.length;
/*  168 */       index -= name.length + 1;
/*      */     }
/*      */     
/*      */ 
/*  172 */     for (int i = args.length - 1; i >= 0; i--)
/*      */     {
/*  174 */       name = getBytes(args[i]);
/*  175 */       System.arraycopy(name, 0, buf, index - name.length - 1, name.length);
/*  176 */       argSizes[i] = name.length;
/*  177 */       index -= name.length + 1;
/*      */     }
/*      */     
/*      */ 
/*  181 */     mem.copyArrayIntoContents((int)stackBase, buf, 0, buf.length);
/*      */     
/*      */ 
/*  184 */     long p = index + stackBase;
/*  185 */     int stringAddr = (int)p;
/*      */     
/*  187 */     byte[] plat = getBytes("i386");
/*  188 */     p -= (plat.length + 4 & 0xFFFFFFFC);
/*  189 */     int platformPointer = (int)p;
/*  190 */     mem.copyArrayIntoContents((int)p, plat, 0, 4);
/*  191 */     int platform = mem.getDoubleWord((int)p);
/*      */     
/*  193 */     p -= 16L;
/*  194 */     byte[] ran = new byte[16];
/*  195 */     if (randomise)
/*  196 */       new java.util.Random().nextBytes(ran);
/*  197 */     int randomBytesAddr = (int)p;
/*  198 */     mem.copyArrayIntoContents((int)p, ran, 0, 16);
/*      */     
/*  200 */     p &= 0xFFFFFFFFFFFFFFF0;
/*      */     
/*  202 */     int dsize = 30;
/*  203 */     dsize += args.length + env.length + 2;
/*  204 */     dsize += (pushPointer ? 3 : 1);
/*  205 */     dsize *= 4;
/*  206 */     if ((dsize & 0xF) != 0) {
/*  207 */       p -= 16 - (dsize & 0xF);
/*      */     }
/*  209 */     if (!SystemConstants.UNAME_RELEASE.startsWith("2"))
/*  210 */       p -= 8L;
/*  211 */     p -= 4L;
/*  212 */     mem.setDoubleWord((int)p, elf.getProgramHeader(0).getVirtualAddress() + elf.getHeader().getProgramHeaderOffset());
/*  213 */     p -= 4L;
/*  214 */     mem.setDoubleWord((int)p, 3);
/*  215 */     p -= 4L;
/*  216 */     mem.setDoubleWord((int)p, 32);
/*  217 */     p -= 4L;
/*  218 */     mem.setDoubleWord((int)p, 4);
/*  219 */     p -= 4L;
/*  220 */     mem.setDoubleWord((int)p, elf.getHeader().getProgramHeaderCount());
/*  221 */     p -= 4L;
/*  222 */     mem.setDoubleWord((int)p, 5);
/*  223 */     p -= 4L;
/*  224 */     mem.setDoubleWord((int)p, 4096);
/*  225 */     p -= 4L;
/*  226 */     mem.setDoubleWord((int)p, 6);
/*  227 */     p -= 4L;
/*  228 */     mem.setDoubleWord((int)p, 0);
/*  229 */     p -= 4L;
/*  230 */     mem.setDoubleWord((int)p, 7);
/*  231 */     p -= 4L;
/*  232 */     mem.setDoubleWord((int)p, 0);
/*  233 */     p -= 4L;
/*  234 */     mem.setDoubleWord((int)p, 8);
/*  235 */     p -= 4L;
/*  236 */     mem.setDoubleWord((int)p, elf.getHeader().getEntryPoint());
/*  237 */     p -= 4L;
/*  238 */     mem.setDoubleWord((int)p, 9);
/*  239 */     p -= 4L;
/*  240 */     mem.setDoubleWord((int)p, 1000);
/*  241 */     p -= 4L;
/*  242 */     mem.setDoubleWord((int)p, 11);
/*  243 */     p -= 4L;
/*  244 */     mem.setDoubleWord((int)p, 1000);
/*  245 */     p -= 4L;
/*  246 */     mem.setDoubleWord((int)p, 12);
/*  247 */     p -= 4L;
/*  248 */     mem.setDoubleWord((int)p, 1000);
/*  249 */     p -= 4L;
/*  250 */     mem.setDoubleWord((int)p, 13);
/*  251 */     p -= 4L;
/*  252 */     mem.setDoubleWord((int)p, 1000);
/*  253 */     p -= 4L;
/*  254 */     mem.setDoubleWord((int)p, 14);
/*  255 */     p -= 4L;
/*  256 */     mem.setDoubleWord((int)p, 0);
/*  257 */     p -= 4L;
/*  258 */     mem.setDoubleWord((int)p, 16);
/*  259 */     p -= 4L;
/*  260 */     mem.setDoubleWord((int)p, 100);
/*  261 */     p -= 4L;
/*  262 */     mem.setDoubleWord((int)p, 17);
/*  263 */     if (!SystemConstants.UNAME_RELEASE.startsWith("2"))
/*      */     {
/*  265 */       p -= 4L;
/*  266 */       mem.setDoubleWord((int)p, randomBytesAddr);
/*  267 */       p -= 4L;
/*  268 */       mem.setDoubleWord((int)p, 25);
/*      */     }
/*  270 */     p -= 4L;
/*  271 */     mem.setDoubleWord((int)p, platformPointer);
/*  272 */     p -= 4L;
/*  273 */     mem.setDoubleWord((int)p, 15);
/*      */     
/*      */ 
/*  276 */     int envp = (int)(p - (env.length + 1) * 4);
/*  277 */     int argp = (int)(p - (env.length + 1) * 4 - (args.length + 1) * 4);
/*  278 */     if (pushPointer)
/*      */     {
/*  280 */       mem.setDoubleWord(argp - 4, envp);
/*  281 */       mem.setDoubleWord(argp - 8, argp);
/*  282 */       mem.setDoubleWord(argp - 12, args.length);
/*  283 */       cpu.esp = (argp - 12);
/*      */     }
/*      */     else {
/*  286 */       mem.setDoubleWord(argp - 4, args.length);
/*  287 */       cpu.esp = (argp - 4);
/*      */     }
/*      */     
/*  290 */     int tmp = stringAddr;
/*  291 */     int address = argp;
/*  292 */     for (int i = 0; i < argSizes.length; i++)
/*      */     {
/*  294 */       mem.setDoubleWord(address, tmp);
/*  295 */       address += 4;
/*  296 */       tmp += argSizes[i] + 1;
/*      */     }
/*  298 */     address += 4;
/*  299 */     for (int i = 0; i < envSizes.length; i++)
/*      */     {
/*  301 */       mem.setDoubleWord(address, tmp);
/*  302 */       address += 4;
/*  303 */       tmp += envSizes[i] + 1;
/*      */     }
/*      */   }
/*      */   
/*      */   public int getCurrentInstruction()
/*      */   {
/*  309 */     return this.context.cpu.eip;
/*      */   }
/*      */   
/*      */   public int getProgramEntry()
/*      */   {
/*  314 */     return this.elf.getHeader().getEntryPoint();
/*      */   }
/*      */   
/*      */   public void execute(ExecutionEnvironment env)
/*      */   {
/*      */     try
/*      */     {
/*  321 */       this.context.executor.execute(this.context, env);
/*      */     }
/*      */     catch (IllegalStateException e) {
/*  324 */       System.out.println("ERROR at: " + Integer.toHexString(this.context.cpu.eip));
/*  325 */       throw e;
/*      */     }
/*      */   }
/*      */   
/*      */   public void executeInterpreted(ExecutionEnvironment env)
/*      */   {
/*      */     try
/*      */     {
/*  333 */       Interpreter.executeInterpreted(this.context, env);
/*      */     }
/*      */     catch (IllegalStateException e) {
/*  336 */       System.out.println("ERROR at: " + Integer.toHexString(this.context.cpu.eip));
/*  337 */       throw e;
/*      */     }
/*      */   }
/*      */   
/*      */   public static ProcessEmulator load(String fileName) throws Exception
/*      */   {
/*  343 */     return load(fileName, new String[] { fileName });
/*      */   }
/*      */   
/*      */   public static ProcessEmulator load(String fileName, String[] args) throws Exception
/*      */   {
/*  348 */     return load(fileName, args, new MainMemory());
/*      */   }
/*      */   
/*      */   public static ProcessEmulator load(String fileName, String[] args, MainMemory addressSpace) throws Exception
/*      */   {
/*  353 */     return load(fileName, args, getEnv(), addressSpace);
/*      */   }
/*      */   
/*      */   public static ProcessEmulator load(String fileName, String[] args, String[] env) throws Exception
/*      */   {
/*  358 */     return load(fileName, args, env, new MainMemory());
/*      */   }
/*      */   
/*      */   public static ProcessEmulator load(String fileName, String[] args, String[] env, MainMemory addressSpace) throws Exception
/*      */   {
/*  363 */     Elf elf = com.emt.proteus.elf.SeekableFile.readElf(fileName);
/*  364 */     ProcessEmulator proc = new ProcessEmulator(elf, args, env, addressSpace);
/*  365 */     if (IO_FILES != null)
/*  366 */       proc.context.iolib.load(IO_FILES);
/*  367 */     return proc;
/*      */   }
/*      */   
/*      */   static class DebugEnv extends ExecutionEnvironment
/*      */   {
/*      */     int count;
/*      */     boolean print;
/*      */     
/*      */     DebugEnv(int limit, boolean printEIP)
/*      */     {
/*  377 */       this.count = limit;
/*  378 */       this.print = printEIP;
/*      */     }
/*      */     
/*      */     DebugEnv(int limit)
/*      */     {
/*  383 */       this(limit, false);
/*      */     }
/*      */     
/*      */     public boolean isFinished()
/*      */     {
/*  388 */       return this.count <= 0;
/*      */     }
/*      */     
/*      */     public int getStatus(int currentAddress)
/*      */     {
/*  393 */       this.count -= 1;
/*  394 */       if (this.count <= 0)
/*  395 */         return -1;
/*  396 */       return 1;
/*      */     }
/*      */     
/*      */     public void cpuStateUpdate(Processor cpu)
/*      */     {
/*  401 */       if (this.print) {
/*  402 */         System.out.println("CPU EIP: " + Integer.toHexString(cpu.eip));
/*      */       }
/*      */     }
/*      */     
/*      */     public int[] extractUCodes(Processor cpu, MainMemory memory, int address) {
/*  407 */       int[] result = (int[])memory.getAnnotation(address);
/*  408 */       if (result != null) {
/*  409 */         return result;
/*      */       }
/*      */       try
/*      */       {
/*  413 */         MemoryInputStream min = new MemoryInputStream(memory, address);
/*  414 */         X86Opcode decoded = Disassembler.getDisassembler().disassemble(min);
/*  415 */         result = decoded.getUCodes();
/*  416 */         memory.setAnnotation(address, result);
/*      */ 
/*      */       }
/*      */       catch (java.io.IOException e) {}catch (IllegalStateException e)
/*      */       {
/*  421 */         System.out.println("Address at exception: " + Integer.toHexString(address));
/*  422 */         throw e;
/*      */       }
/*      */       catch (NullPointerException e) {
/*  425 */         System.out.println("Address at exception: " + Integer.toHexString(address));
/*  426 */         throw e;
/*      */       }
/*      */       
/*  429 */       return result;
/*      */     }
/*      */   }
/*      */   
/*      */   public static class RunEnv extends ProcessEmulator.DebugEnv
/*      */   {
/*      */     public RunEnv(int limit)
/*      */     {
/*  437 */       super();
/*      */     }
/*      */     
/*      */     public boolean isFinished()
/*      */     {
/*  442 */       return this.count <= 0;
/*      */     }
/*      */     
/*      */     public int getStatus(int currentAddress)
/*      */     {
/*  447 */       this.count -= 1;
/*  448 */       if (this.count <= 0)
/*  449 */         return -1;
/*  450 */       return 0;
/*      */     }
/*      */   }
/*      */   
/*      */   public static void saveState(File f, ProcessEmulator em) throws java.io.IOException
/*      */   {
/*  456 */     java.io.DataOutputStream out = new java.io.DataOutputStream(new java.io.BufferedOutputStream(new java.io.FileOutputStream(f)));
/*  457 */     em.context.cpu.saveState(out);
/*  458 */     em.memory.saveState(out);
/*  459 */     out.flush();
/*  460 */     out.close();
/*      */   }
/*      */   
/*      */   public static ProcessEmulator load(String fileName, File f, Processor cpu, MainMemory mem) throws Exception
/*      */   {
/*  465 */     Elf elf = com.emt.proteus.elf.SeekableFile.readElf(fileName);
/*  466 */     java.io.DataInputStream in = new java.io.DataInputStream(new java.io.FileInputStream(f));
/*  467 */     ProcessEmulator processEmulator = new ProcessEmulator(elf, mem);
/*  468 */     cpu.loadState(in);
/*  469 */     mem.loadState(in);
/*  470 */     return processEmulator;
/*      */   }
/*      */   
/*      */   public void loadState(File f) throws Exception
/*      */   {
/*  475 */     java.io.DataInputStream in = new java.io.DataInputStream(new java.io.FileInputStream(f));
/*  476 */     this.context.cpu.loadState(in);
/*  477 */     this.memory.loadState(in);
/*      */   }
/*      */   
/*      */   public static void compareCPU(Processor cpu1, Processor cpu2)
/*      */   {
/*  482 */     boolean same = true;
/*  483 */     if (cpu1.eax != cpu2.eax)
/*      */     {
/*  485 */       System.out.println("cpu1.EAX: " + Integer.toHexString(cpu1.eax) + ", cpu2.EAX: " + Integer.toHexString(cpu2.eax));
/*  486 */       same = false;
/*      */     }
/*  488 */     if (cpu1.ebx != cpu2.ebx)
/*      */     {
/*  490 */       System.out.println("cpu1.EBX: " + Integer.toHexString(cpu1.ebx) + ", cpu2.EBX: " + Integer.toHexString(cpu2.ebx));
/*  491 */       same = false;
/*      */     }
/*  493 */     if (cpu1.ecx != cpu2.ecx)
/*      */     {
/*  495 */       System.out.println("cpu1.ECX: " + Integer.toHexString(cpu1.ecx) + ", cpu2.ECX: " + Integer.toHexString(cpu2.ecx));
/*  496 */       same = false;
/*      */     }
/*  498 */     if (cpu1.edx != cpu2.edx)
/*      */     {
/*  500 */       System.out.println("cpu1.EDX: " + Integer.toHexString(cpu1.edx) + ", cpu2.EDX: " + Integer.toHexString(cpu2.edx));
/*  501 */       same = false;
/*      */     }
/*  503 */     if (cpu1.esi != cpu2.esi)
/*      */     {
/*  505 */       System.out.println("cpu1.ESI: " + Integer.toHexString(cpu1.esi) + ", cpu2.ESI: " + Integer.toHexString(cpu2.esi));
/*  506 */       same = false;
/*      */     }
/*  508 */     if (cpu1.edi != cpu2.edi)
/*      */     {
/*  510 */       System.out.println("cpu1.EDI: " + Integer.toHexString(cpu1.edi) + ", cpu2.EDI: " + Integer.toHexString(cpu2.edi));
/*  511 */       same = false;
/*      */     }
/*  513 */     if (cpu1.esp != cpu2.esp)
/*      */     {
/*  515 */       System.out.println("cpu1.ESP: " + Integer.toHexString(cpu1.esp) + ", cpu2.ESP: " + Integer.toHexString(cpu2.esp));
/*  516 */       same = false;
/*      */     }
/*  518 */     if (cpu1.ebp != cpu2.ebp)
/*      */     {
/*  520 */       System.out.println("cpu1.EBP: " + Integer.toHexString(cpu1.ebp) + ", cpu2.EBP: " + Integer.toHexString(cpu2.ebp));
/*  521 */       same = false;
/*      */     }
/*  523 */     if (cpu1.eip != cpu2.eip)
/*      */     {
/*  525 */       System.out.println("cpu1.EIP: " + Integer.toHexString(cpu1.eip) + ", cpu2.EIP: " + Integer.toHexString(cpu2.eip));
/*  526 */       same = false;
/*      */     }
/*  528 */     if (cpu1.getEflags() != cpu2.getEflags())
/*      */     {
/*  530 */       System.out.println("cpu1.EFLAGS: " + Integer.toHexString(cpu1.getEflags()) + ", cpu2.EFLAGS: " + Integer.toHexString(cpu2.getEflags()));
/*  531 */       same = false;
/*      */     }
/*      */     
/*      */ 
/*  535 */     if (!same)
/*      */     {
/*  537 */       System.out.println("cpu1.EIP: " + Integer.toHexString(cpu1.eip) + ", cpu2.EIP: " + Integer.toHexString(cpu2.eip));
/*  538 */       throw new IllegalStateException("Different state in CPUs");
/*      */     }
/*      */   }
/*      */   
/*      */   public static void comparePage(AbstractMemory page1, AbstractMemory page2, int address)
/*      */   {
/*  544 */     boolean same = true;
/*  545 */     for (int i = 0; i < 4096; i++)
/*      */     {
/*  547 */       byte b1 = page1.getByte(i);
/*  548 */       byte b2 = page2.getByte(i);
/*  549 */       if (b1 != b2)
/*      */       {
/*  551 */         System.out.println("Memory difference at 0x" + Integer.toHexString(address + i) + ", values: " + Integer.toHexString(b1 & 0xFF) + " " + Integer.toHexString(b2 & 0xFF));
/*  552 */         same = false;
/*      */       }
/*      */     }
/*      */     
/*  556 */     if (!same)
/*      */     {
/*  558 */       System.out.println("**********************Pages");
/*  559 */       printPages(page1, page2, address);
/*  560 */       throw new IllegalStateException("Different memory state");
/*      */     }
/*      */   }
/*      */   
/*      */   public static void printPages(AbstractMemory p, AbstractMemory p2, int address)
/*      */   {
/*  566 */     for (int i = 0; i < 256; i++)
/*      */     {
/*  568 */       int v1 = p.getDoubleWord(16 * i);
/*  569 */       int v2 = p.getDoubleWord(16 * i + 4);
/*  570 */       int v3 = p.getDoubleWord(16 * i + 8);
/*  571 */       int v4 = p.getDoubleWord(16 * i + 12);
/*  572 */       int r1 = p2.getDoubleWord(16 * i);
/*  573 */       int r2 = p2.getDoubleWord(16 * i + 4);
/*  574 */       int r3 = p2.getDoubleWord(16 * i + 8);
/*  575 */       int r4 = p2.getDoubleWord(16 * i + 12);
/*      */       
/*  577 */       System.out.printf("\033[1;49m0x%8x:  %8x %8x %8x %8x -- %8x %8x %8x %8x ==== ", new Object[] { Integer.valueOf(address + 16 * i), Integer.valueOf(v1), Integer.valueOf(v2), Integer.valueOf(v3), Integer.valueOf(v4), Integer.valueOf(r1), Integer.valueOf(r2), Integer.valueOf(r3), Integer.valueOf(r4) });
/*  578 */       printIntChars(v1, r1);
/*  579 */       printIntChars(v2, r2);
/*  580 */       printIntChars(v3, r3);
/*  581 */       printIntChars(v4, r4);
/*  582 */       System.out.print(" -- ");
/*  583 */       printIntChars(r1, v1);
/*  584 */       printIntChars(r2, v2);
/*  585 */       printIntChars(r3, v3);
/*  586 */       printIntChars(r4, v4);
/*  587 */       System.out.println();
/*      */     }
/*      */   }
/*      */   
/*      */   public static void printIntChars(int i, int c)
/*      */   {
/*  593 */     int[] ia = { i & 0xFF, i >> 8 & 0xFF, i >> 16 & 0xFF, i >> 24 & 0xFF };
/*  594 */     int[] ca = { c & 0xFF, c >> 8 & 0xFF, c >> 16 & 0xFF, c >> 24 & 0xFF };
/*      */     
/*  596 */     for (int a = 0; a < 4; a++)
/*  597 */       if (ia[a] == ca[a]) {
/*  598 */         System.out.printf("%c", new Object[] { Character.valueOf(ia[a] == 0 ? 32 : (char)ia[a]) });
/*      */       } else
/*  600 */         System.out.printf("\033[1;44m%c\033[1;49m", new Object[] { Character.valueOf(ia[a] == 0 ? 32 : (char)ia[a]) });
/*  601 */     System.out.printf(" ", new Object[0]);
/*      */   }
/*      */   
/*      */   public static void compareMemory(MainMemory memory1, MainMemory memory2)
/*      */   {
/*  606 */     if (Trace.compareToInt)
/*      */     {
/*  608 */       Set<Integer> dirtyPages1 = memory1.getDirtyPageSet();
/*  609 */       Set<Integer> dirtyPages2 = memory2.getDirtyPageSet();
/*      */       
/*  611 */       for (Iterator i$ = dirtyPages1.iterator(); i$.hasNext();) { int page = ((Integer)i$.next()).intValue();
/*      */         
/*  613 */         AbstractMemory page1 = memory1.getPageFor(page << 12);
/*  614 */         AbstractMemory page2 = memory2.getPageFor(page << 12);
/*  615 */         if ((page1 != null) || (page2 != null))
/*      */         {
/*  617 */           if ((page1 == null) || (page2 == null))
/*  618 */             throw new IllegalStateException("Unallocated memory page at address: " + Integer.toHexString(page << 12));
/*  619 */           comparePage(page1, page2, page << 12);
/*  620 */           dirtyPages2.remove(Integer.valueOf(page));
/*      */         } }
/*  622 */       for (Iterator i$ = dirtyPages2.iterator(); i$.hasNext();) { int page = ((Integer)i$.next()).intValue();
/*      */         
/*  624 */         AbstractMemory page1 = memory1.getPageFor(page << 12);
/*  625 */         AbstractMemory page2 = memory2.getPageFor(page << 12);
/*  626 */         if ((page1 != null) || (page2 != null))
/*      */         {
/*  628 */           if ((page1 == null) || (page2 == null))
/*  629 */             throw new IllegalStateException("Unallocated memory page at address: " + Integer.toHexString(page << 12));
/*  630 */           comparePage(page1, page2, page << 12);
/*      */         } }
/*  632 */       dirtyPages1.clear();
/*  633 */       dirtyPages2.clear();
/*  634 */       return;
/*      */     }
/*      */     
/*  637 */     boolean[] dirty1 = memory1.dirtyPages;
/*  638 */     boolean[] dirty2 = memory2.dirtyPages;
/*      */     
/*  640 */     for (int page = 0; page < 1048576; page++)
/*      */     {
/*  642 */       if ((dirty1[page] != 0) || (dirty2[page] != 0))
/*      */       {
/*  644 */         AbstractMemory page1 = memory1.getPageFor(page << 12);
/*  645 */         AbstractMemory page2 = memory2.getPageFor(page << 12);
/*  646 */         if ((page1 != null) || (page2 != null))
/*      */         {
/*  648 */           if ((page1 == null) || (page2 == null))
/*  649 */             throw new IllegalStateException("Unallocated memory page at address: " + Integer.toHexString(page << 12));
/*  650 */           comparePage(page1, page2, page << 12);
/*      */         } } }
/*  652 */     memory1.clearDirtyPages();
/*  653 */     memory2.clearDirtyPages();
/*      */   }
/*      */   
/*      */   public static void compareState(ProcessEmulator proc1, ProcessEmulator proc2)
/*      */   {
/*  658 */     compareCPU(proc1.context.cpu, proc2.context.cpu);
/*      */     try {
/*  660 */       compareMemory(proc1.memory, proc2.memory);
/*      */     }
/*      */     catch (RuntimeException e) {
/*  663 */       System.out.println("EIP = 0x" + Integer.toHexString(proc1.context.cpu.eip));
/*  664 */       throw e;
/*      */     }
/*      */   }
/*      */   
/*      */   public static void printInstruction(int eip, MainMemory mem) throws java.io.IOException
/*      */   {
/*  670 */     MemoryInputStream min = new MemoryInputStream(mem, eip);
/*  671 */     X86Opcode decoded = Disassembler.getDisassembler().disassemble(min);
/*  672 */     System.out.println("Instruction pattern: " + decoded.getPattern().toUpperCase().replace(" ", "_").replace(",", "_"));
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*  677 */   public static final List<String> ignore = new java.util.ArrayList();
/*  678 */   public static final List<String> adopt = new java.util.ArrayList();
/*      */   
/*      */   private static void printGdbState(RemoteGDB gdb, ProcessEmulator proc, int ins, java.io.Writer w) throws Exception
/*      */   {
/*  682 */     if (ignore.contains(gdb.previ[1]))
/*  683 */       return;
/*  684 */     if (adopt.contains(gdb.previ[1]))
/*      */     {
/*  686 */       gdb.adoptRegisters();
/*  687 */       return;
/*      */     }
/*  689 */     java.awt.Toolkit.getDefaultToolkit().beep();
/*  690 */     gdb.printLastRegisters();
/*  691 */     System.out.println("Remote previous: " + gdb.previ[1]);
/*  692 */     System.out.println("Remote next: " + gdb.nexti[1]);
/*  693 */     System.out.println(gdb.getDifferences());
/*  694 */     MemoryInputStream min = new MemoryInputStream(proc.memory, gdb.getPreviousEIP());
/*  695 */     X86Opcode decoded = Disassembler.getDisassembler().disassemble(min);
/*  696 */     System.out.println("Previous instruction pattern at 0x" + Integer.toHexString(gdb.getPreviousEIP()) + ": " + decoded.getPattern().toUpperCase().replace(" ", "_").replace(",", "_"));
/*  697 */     min = new MemoryInputStream(proc.memory, proc.context.cpu.eip);
/*  698 */     decoded = Disassembler.getDisassembler().disassemble(min);
/*  699 */     System.out.println("Current instruction pattern at 0x" + Integer.toHexString(proc.context.cpu.eip) + " : " + decoded.getPattern().toUpperCase().replace(" ", "_").replace(",", "_"));
/*  700 */     System.out.println("Executed " + ins + " instructions.");
/*  701 */     System.out.println("Adopt remote registers, ignore this instruction henceforth, adopt registers henceforth? (y/n/i/a)");
/*  702 */     String line = new BufferedReader(new java.io.InputStreamReader(System.in)).readLine();
/*  703 */     if (line.equals("y"))
/*      */     {
/*  705 */       gdb.adoptRegisters();
/*      */     }
/*  707 */     else if (line.equals("i"))
/*      */     {
/*  709 */       ignore.add(gdb.previ[1]);
/*      */     }
/*  711 */     else if (line.equals("a"))
/*      */     {
/*  713 */       w.write(gdb.previ[1] + "\n");
/*  714 */       w.flush();
/*  715 */       gdb.adoptRegisters();
/*  716 */       adopt.add(gdb.previ[1]);
/*      */     }
/*      */     else {
/*  719 */       throw new IllegalStateException("Different state");
/*      */     }
/*      */   }
/*      */   
/*      */   public static void compareQemu(String[] args, String initialBreak) throws Exception {
/*  724 */     System.out.println("Comparing to Native");
/*  725 */     ProcessEmulator proc = load(args[0], args);
/*  726 */     RemoteGDB gdb = new RemoteGDB(proc.context.cpu, proc.memory, args, "0x" + initialBreak);
/*      */     
/*      */ 
/*  729 */     File adopts = new File("adopts");
/*  730 */     if (adopts.exists())
/*      */     {
/*  732 */       BufferedReader r = new BufferedReader(new java.io.FileReader(adopts));
/*      */       String ad;
/*  734 */       while ((ad = r.readLine()) != null)
/*      */       {
/*  736 */         System.out.println("Ignoring: " + ad);
/*  737 */         adopt.add(ad);
/*      */       }
/*      */     }
/*  740 */     java.io.BufferedWriter w = new java.io.BufferedWriter(new java.io.FileWriter("adopts", true));
/*      */     
/*      */ 
/*  743 */     proc.continueToBreak(Integer.parseInt(initialBreak, 16));
/*      */     
/*      */ 
/*  746 */     gdb.updateRegHistory();
/*  747 */     gdb.adoptRegisters();
/*  748 */     System.out.println("Beginning EIP: 0x" + Integer.toHexString(proc.context.cpu.eip));
/*      */     
/*  750 */     for (int j = 0; j < 100000; j++)
/*      */     {
/*  752 */       System.out.println("Executed " + j * 100000 + " instructions...");
/*      */       
/*  754 */       saveState(new File(j * 100000 + ""), proc);
/*  755 */       System.out.println("Saved state. EIP=0x" + Integer.toHexString(proc.context.cpu.eip));
/*  756 */       for (int i = 0; i < 100000; i++)
/*      */       {
/*  758 */         if ((i % 10000 == 0) && (i != 0))
/*  759 */           System.out.println("Executed " + (j * 100000 + i) + " instructions...");
/*  760 */         if (!gdb.compareRegisters(false))
/*      */         {
/*  762 */           printGdbState(gdb, proc, j * 100000 + i, w);
/*      */         }
/*      */         
/*      */         try
/*      */         {
/*  767 */           proc.execute(new DebugEnv(1));
/*      */         }
/*      */         catch (Exception e)
/*      */         {
/*  771 */           printGdbState(gdb, proc, j * 100000 + i, w);
/*  772 */           throw e;
/*      */         }
/*      */         
/*      */         try
/*      */         {
/*  777 */           gdb.executeRemoteInstructions(1);
/*      */         }
/*      */         catch (Exception e) {
/*  780 */           e.printStackTrace();
/*  781 */           printGdbState(gdb, proc, j * 100000 + i, w);
/*  782 */           throw e;
/*      */         }
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */   public static void recompareQemu(File f, String[] args, int postskip, String initialBreak) throws Exception
/*      */   {
/*  790 */     System.out.println("Recomparing to Qemu");
/*  791 */     ProcessEmulator proc = load(args[0], args);
/*  792 */     proc.loadState(f);
/*  793 */     proc.skip(postskip);
/*  794 */     RemoteGDB gdb = new RemoteGDB(proc.context.cpu, proc.memory, args, initialBreak);
/*  795 */     int skip = Integer.parseInt(f.getName());
/*  796 */     System.out.println("Skipping remote instructions");
/*  797 */     gdb.skipRemoteInstructions(skip + postskip);
/*  798 */     System.out.println("Skipped " + (skip + postskip) + " remote instructions.");
/*  799 */     gdb.updateRegHistory();
/*      */     
/*      */ 
/*  802 */     BufferedReader r = new BufferedReader(new java.io.FileReader("adopts"));
/*      */     String ad;
/*  804 */     while ((ad = r.readLine()) != null)
/*  805 */       adopt.add(ad);
/*  806 */     java.io.BufferedWriter w = new java.io.BufferedWriter(new java.io.FileWriter("adopts", true));
/*      */     
/*      */ 
/*      */ 
/*  810 */     int loadaddr = proc.context.cpu.gs.getBase();
/*  811 */     System.out.println("Getting remote GS bytes from: 0x" + Integer.toHexString(loadaddr) + " to 0x" + Integer.toHexString(loadaddr + 512));
/*  812 */     byte[] qstack = gdb.getRemoteBytes(loadaddr, 512);
/*  813 */     proc.memory.copyArrayIntoContents(loadaddr, qstack, 0, qstack.length);
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*  818 */     int loadaddr = proc.context.cpu.esp & 0xF000;
/*  819 */     System.out.println("Getting remote bytes from: 0x" + Integer.toHexString(loadaddr) + " to 0x" + Integer.toHexString(1082134528));
/*  820 */     byte[] qstack = gdb.getRemoteBytes(loadaddr, 1082134528 - loadaddr);
/*  821 */     proc.memory.copyArrayIntoContents(loadaddr, qstack, 0, qstack.length);
/*      */     
/*      */ 
/*  824 */     System.out.println("Initial ESP: 0x" + Integer.toHexString(proc.context.cpu.esp));
/*  825 */     for (int j = 0; j < 10000; j++)
/*      */     {
/*  827 */       System.out.println("Executed " + j * 100000 + " instructions, saving state...");
/*  828 */       for (int i = 0; i < 100000; i++)
/*      */       {
/*  830 */         if ((i % 10000 == 0) && (i != 0))
/*  831 */           System.out.println("Executed " + (j * 100000 + i) + " instructions...");
/*  832 */         if (!gdb.compareRegisters(false))
/*      */         {
/*  834 */           printGdbState(gdb, proc, j * 100000 + i, w);
/*      */         }
/*      */         
/*      */         try
/*      */         {
/*  839 */           proc.execute(new DebugEnv(1));
/*      */         }
/*      */         catch (Exception e) {
/*  842 */           printGdbState(gdb, proc, j * 100000 + i, w);
/*  843 */           throw e;
/*      */         }
/*      */         
/*      */         try
/*      */         {
/*  848 */           gdb.executeRemoteInstructions(1);
/*      */         }
/*      */         catch (Exception e) {
/*  851 */           printGdbState(gdb, proc, j * 100000 + i, w);
/*  852 */           throw e;
/*      */         }
/*      */       }
/*      */       
/*  856 */       saveState(new File((j + 1) * 100000 + skip + ""), proc);
/*      */     }
/*      */   }
/*      */   
/*      */   public void skip(int skip)
/*      */   {
/*  862 */     RunEnv env = new RunEnv(skip);
/*  863 */     while (!env.isFinished()) {
/*  864 */       execute(env);
/*      */     }
/*      */   }
/*      */   
/*      */   public void continueToBreak(int brk) {
/*  869 */     System.out.println("Continuing to 0x" + Integer.toHexString(brk));
/*  870 */     long skipped = 0L;
/*  871 */     while (this.context.cpu.eip != brk)
/*      */     {
/*  873 */       execute(new DebugEnv(1));
/*  874 */       skipped += 1L;
/*      */     }
/*  876 */     System.out.println("Skipped " + skipped);
/*      */   }
/*      */   
/*      */   public static void newCompareLoopToSingle(String[] args) throws Exception
/*      */   {
/*  881 */     String[] env = getEnv();
/*  882 */     ProcessEmulator proc1 = load(args[0], args, env);
/*  883 */     ProcessEmulator proc2 = load(args[0], args, env);
/*  884 */     compareState(proc1, proc2);
/*      */     
/*      */     for (;;)
/*      */     {
/*  888 */       proc1.execute(new BasicBlockExecutionEnvironment(true));
/*      */       
/*      */ 
/*      */ 
/*      */ 
/*      */       for (;;)
/*      */       {
/*  895 */         MemoryInputStream min = new MemoryInputStream(proc2.memory, proc2.context.cpu.eip);
/*  896 */         X86Opcode decoded = Disassembler.getDisassembler().disassemble(min);
/*  897 */         DebugEnv d = new DebugEnv(1);
/*  898 */         while (!d.isFinished())
/*  899 */           proc2.execute(d);
/*  900 */         if (decoded.isBranch())
/*      */           break;
/*      */       }
/*  903 */       compareState(proc1, proc2);
/*      */     }
/*      */   }
/*      */   
/*      */   public static void compareInterpretedToCompiledByBlock(String[] args) throws Exception
/*      */   {
/*  909 */     System.out.println("Comparing compiled execution to interpreted...");
/*  910 */     String[] env = getEnv();
/*  911 */     ProcessEmulator procInt = load(args[0], args, env);
/*  912 */     ProcessEmulator procComp = load(args[0], args, env);
/*  913 */     Trace.compiled = procComp;
/*  914 */     Trace.interpreted = procInt;
/*  915 */     run(procComp);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static void compareInterpretedToCompiledByFunction(String[] args)
/*      */     throws Exception
/*      */   {}
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static void run(ProcessEmulator proc)
/*      */   {
/*      */     for (;;)
/*      */     {
/*  939 */       proc.execute(new BasicBlockExecutionEnvironment(true));
/*  940 */       if (Interpreter.frequencies) {
/*  941 */         Interpreter.printFrequencies();
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */   public static void run(String[] args) throws Exception {
/*      */     try {
/*  948 */       ProcessEmulator proc = load(args[0], args);
/*  949 */       run(proc);
/*      */     }
/*      */     catch (ProgramEndException e) {}
/*      */   }
/*      */   
/*      */   public static void runInterpreted(ProcessEmulator proc) throws Exception
/*      */   {
/*      */     for (;;) {
/*  957 */       proc.executeInterpreted(new BasicBlockExecutionEnvironment(true));
/*  958 */       if (Interpreter.frequencies) {
/*  959 */         Interpreter.printFrequencies();
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */   public static void runInterpreted(String[] args) throws Exception {
/*  965 */     ProcessEmulator proc = load(args[0], args);
/*  966 */     runInterpreted(proc);
/*      */   }
/*      */   
/*      */   public static void runSingle(String[] args) throws Exception
/*      */   {
/*  971 */     ProcessEmulator proc = load(args[0], args);
/*      */     for (;;)
/*      */     {
/*  974 */       System.out.println("EIP: " + Integer.toHexString(proc.context.cpu.eip));
/*  975 */       proc.executeInterpreted(new RunEnv(1));
/*      */     }
/*      */   }
/*      */   
/*      */   public static void compareInitialMemory(String[] args, File snapshot) throws Exception
/*      */   {
/*  981 */     ProcessEmulator proc1 = load(args[0], args);
/*  982 */     ProcessEmulator proc2 = load(args[0], args);
/*  983 */     proc2.loadState(snapshot);
/*  984 */     compareMemory(proc1.memory, proc2.memory);
/*      */   }
/*      */   
/*  987 */   public static String IO_FILES = null;
/*      */   
/*      */   public static void main(String[] args) throws Exception
/*      */   {
/*  991 */     Disassembler.init();
/*  992 */     args = Option.parse(args);
/*      */     
/*  994 */     int loops = 1;
/*  995 */     if (Option.benchmark.isSet())
/*      */     {
/*  997 */       loops = Integer.parseInt(Option.benchmark.value());
/*      */     }
/*  999 */     boolean time = Option.time.isSet();
/*      */     
/* 1001 */     long start = 0L;long end = 0L;
/* 1002 */     for (int i = 0; i < loops; i++)
/*      */     {
/* 1004 */       if (time) {
/* 1005 */         start = System.nanoTime();
/*      */       }
/*      */       try {
/* 1008 */         if (Option.dynamic.value())
/*      */         {
/* 1010 */           com.emt.proteus.utils.ILog.Base log = new com.emt.proteus.utils.ILog.Err();
/* 1011 */           args = configureLogging(args, log);
/*      */           
/* 1013 */           String libDir = Option.copylib.value(null);
/*      */           
/* 1015 */           ProteusRuntime pRtLd = new ProteusRuntime(new MainMemory(), args, null, log);
/* 1016 */           pRtLd.setCopyLibrary(libDir);
/* 1017 */           pRtLd.exec();
/* 1018 */           System.exit(0);
/*      */         }
/* 1020 */         if (Option.stackbase.isSet())
/*      */         {
/* 1022 */           STACK_BASE = Long.parseLong(Option.stackbase.value(), 16);
/*      */         }
/* 1024 */         IO_FILES = Option.IO_FILES.value();
/* 1025 */         if (IO_FILES != null)
/*      */         {
/* 1027 */           System.out.println("Loading open files from: " + IO_FILES);
/*      */         }
/* 1029 */         int postskip = 0;
/* 1030 */         if (Option.postskip.isSet())
/*      */         {
/* 1032 */           postskip = Integer.parseInt(Option.postskip.value());
/* 1033 */           System.out.println("Skipping " + postskip + " instructions after snapshot.");
/*      */         }
/*      */         
/* 1036 */         if (Option.recompare.isSet())
/*      */         {
/* 1038 */           File state = new File(Option.recompare.value());
/* 1039 */           recompareQemu(state, args, postskip, Option.compare.value());
/* 1040 */         } else { if (Option.compare.isSet())
/*      */           {
/* 1042 */             String initialBreak = Option.compare.value();
/* 1043 */             compareQemu(args, initialBreak);
/* 1044 */             return; }
/* 1045 */           if (Option.loopcompare.isSet())
/*      */           {
/* 1047 */             newCompareLoopToSingle(args);
/* 1048 */             return; }
/* 1049 */           if (Option.compareIntToCompByFunction.value())
/*      */           {
/* 1051 */             compareInterpretedToCompiledByFunction(args);
/* 1052 */             return; }
/* 1053 */           if (Option.compareIntToCompByBlock.value())
/*      */           {
/* 1055 */             compareInterpretedToCompiledByBlock(args);
/* 1056 */             return; }
/* 1057 */           if (Option.ss.isSet())
/*      */           {
/* 1059 */             File ss = new File(Option.ss.value());
/* 1060 */             ProcessEmulator proc = load(args[0], args);
/* 1061 */             proc.loadState(ss);
/* 1062 */             run(proc);
/* 1063 */           } else if (Option.run1.isSet())
/*      */           {
/* 1065 */             runSingle(args);
/* 1066 */           } else if (Option.interp.isSet())
/*      */           {
/* 1068 */             runInterpreted(args);
/*      */           }
/*      */           else {
/* 1071 */             run(args);
/*      */           }
/*      */         }
/*      */       } catch (ProgramEndException e) {}
/* 1075 */       if (time)
/*      */       {
/* 1077 */         end = System.nanoTime();
/* 1078 */         System.out.println("Run took: " + (end - start) / 1.0E9D + "S");
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */   private static String[] configureLogging(String[] args, com.emt.proteus.utils.ILog.Base log) {
/*      */     java.util.logging.Level l;
/*      */     try {
/* 1086 */       l = java.util.logging.Level.parse(Option.log.value("WARNING"));
/* 1087 */       log.setLevel(l);
/* 1088 */       System.out.println("Logging at " + l);
/*      */     } catch (IllegalArgumentException iae) {
/* 1090 */       l = java.util.logging.Level.WARNING;
/*      */     }
/* 1092 */     log.info("BOOT", "Logging at %s", new Object[] { l });
/* 1093 */     return args;
/*      */   }
/*      */ }


/* Location:              C:\Users\Joey\Downloads\codejar.jar!\com\emt\proteus\runtime32\ProcessEmulator.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */