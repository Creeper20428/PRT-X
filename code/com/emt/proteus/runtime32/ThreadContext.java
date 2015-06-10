/*     */ package com.emt.proteus.runtime32;
/*     */ 
/*     */ import com.emt.proteus.runtime32.api.IoSystem;
/*     */ import com.emt.proteus.runtime32.io.DefaultIoSystem;
/*     */ import com.emt.proteus.runtime32.io.IoLib;
/*     */ import com.emt.proteus.runtime32.memory.MainMemory;
/*     */ import com.emt.proteus.runtime32.memory.ManagedMemory;
/*     */ import com.emt.proteus.runtime32.syscall.Clone;
/*     */ import com.emt.proteus.runtime32.syscall.Futex;
/*     */ import com.emt.proteus.utils.ILog;
/*     */ import java.io.PrintStream;
/*     */ import java.io.UnsupportedEncodingException;
/*     */ import java.util.Collection;
/*     */ import java.util.Hashtable;
/*     */ import java.util.concurrent.atomic.AtomicInteger;
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
/*     */ public final class ThreadContext
/*     */ {
/*  36 */   private static final boolean TRACE = Option.trace.value();
/*  37 */   private static final boolean INTERPRET = Option.interp.value();
/*     */   
/*     */   public static final String PADDING = "                                                                       ";
/*     */   
/*  41 */   private static final AtomicInteger TID_SEED = new AtomicInteger(0);
/*     */   
/*     */ 
/*     */ 
/*  45 */   private static final Hashtable<Integer, ThreadContext> _allThreads = new Hashtable();
/*     */   
/*  47 */   private static ThreadLocal<ThreadContext> _current = new ThreadLocal();
/*     */   
/*     */   public final ILog log;
/*     */   
/*     */   public final Processor cpu;
/*     */   
/*     */   public final MainMemory memory;
/*     */   
/*     */   public final IoLib iolib;
/*     */   
/*     */   public final Interpreter executor;
/*     */   
/*     */   public final ExecutionEnvironment env;
/*     */   
/*     */   private final ThreadContext parent;
/*     */   
/*     */   private final int flags;
/*     */   
/*     */   private int threadPointer;
/*     */   
/*     */   private int clearChildTid;
/*     */   
/*     */   private int setChildTid;
/*     */   
/*     */   private int robustlistHead$;
/*     */   
/*     */   private int robustlistLen;
/*     */   
/*     */   private int schedulePolicy;
/*     */   
/*     */   private int schedulePriority;
/*     */   private String threadId;
/*     */   private final Signals signals;
/*     */   private final SystemClock clock;
/*     */   public int retAddr;
/*     */   private int depth;
/*     */   private int tid;
/*     */   private int pid;
/*     */   public boolean step;
/*     */   private boolean silent;
/*     */   private boolean killed;
/*     */   private ManagedMemory systemAllocator;
/*     */   
/*     */   public ThreadContext(ExecutionEnvironment env, MainMemory memory, ILog log)
/*     */   {
/*  92 */     this(env, new DefaultIoSystem(), memory, new SystemClock(), log);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public ThreadContext(ExecutionEnvironment env, IoSystem io, MainMemory memory, SystemClock clock, ILog log)
/*     */   {
/* 104 */     _current.set(this);
/* 105 */     this.systemAllocator = new ManagedMemory(memory, -805306368, 20, 6);
/* 106 */     this.clock = clock;
/* 107 */     this.env = env;
/* 108 */     this.flags = 0;
/* 109 */     this.parent = null;
/* 110 */     this.signals = new Signals();
/* 111 */     this.cpu = new Processor(this);
/* 112 */     this.memory = memory;
/* 113 */     this.iolib = new IoLib(io, memory);
/* 114 */     this.executor = new Interpreter();
/* 115 */     this.log = log;
/* 116 */     this.tid = register(this);
/* 117 */     this.threadId = String.format("%2d:%8s", new Object[] { Integer.valueOf(this.tid), "MAIN" });
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public ThreadContext(ThreadContext parent, int flags)
/*     */   {
/* 126 */     this(parent, flags, null);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public ThreadContext(ThreadContext parent, int flags, IoLib ioLib)
/*     */   {
/* 136 */     this.cpu = new Processor(this);
/* 137 */     this.signals = new Signals(parent.signals);
/* 138 */     this.clock = parent.clock;
/* 139 */     this.flags = flags;
/* 140 */     this.parent = parent;
/* 141 */     this.systemAllocator = parent.systemAllocator;
/* 142 */     this.log = parent.log;
/* 143 */     this.memory = parent.memory;
/* 144 */     this.iolib = (ioLib == null ? parent.getIoLib() : ioLib);
/* 145 */     this.executor = parent.executor;
/* 146 */     this.env = parent.env.newThread();
/* 147 */     this.tid = register(this);
/* 148 */     this.threadId = String.format("%2d:%8s", new Object[] { Integer.valueOf(this.tid), "MAIN" });
/*     */   }
/*     */   
/*     */   public ILog getLog()
/*     */   {
/* 153 */     return this.log;
/*     */   }
/*     */   
/*     */   public Processor getCpu() {
/* 157 */     return this.cpu;
/*     */   }
/*     */   
/*     */   public MainMemory getMemory() {
/* 161 */     return this.memory;
/*     */   }
/*     */   
/*     */   public IoLib getIoLib() {
/* 165 */     return this.iolib;
/*     */   }
/*     */   
/*     */   public ThreadContext getParent()
/*     */   {
/* 170 */     return this.parent;
/*     */   }
/*     */   
/*     */   public Interpreter getExecutor() {
/* 174 */     return this.executor;
/*     */   }
/*     */   
/*     */   public int getThreadPointer() {
/* 178 */     return this.cpu.gs.getBase();
/*     */   }
/*     */   
/*     */   public int getSchedulePolicy()
/*     */   {
/* 183 */     return this.schedulePolicy;
/*     */   }
/*     */   
/*     */   public void setSchedulePolicy(int schedulePolicy) {
/* 187 */     this.schedulePolicy = schedulePolicy;
/*     */   }
/*     */   
/*     */   public int getSchedulePriority() {
/* 191 */     return this.schedulePriority;
/*     */   }
/*     */   
/*     */   public void setSchedulePriority(int schedulePriority) {
/* 195 */     this.schedulePriority = schedulePriority;
/*     */   }
/*     */   
/*     */   public int execute() {
/*     */     try {
/*     */       for (;;) {
/* 201 */         executeImpl();
/*     */       }
/*     */     } catch (FlowException e) {}
/* 204 */     return 0;
/*     */   }
/*     */   
/*     */ 
/*     */   public void executeImpl()
/*     */   {
/* 210 */     this.executor.execute(this, this.env);
/*     */   }
/*     */   
/*     */   public SystemClock getClock() {
/* 214 */     return this.clock;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void safeExecute(Object label)
/*     */   {
/* 221 */     int mark = this.cpu.esp;
/* 222 */     int fn_ptr = this.cpu.eip;
/* 223 */     int eip = fn_ptr;
/* 224 */     int base = this.env.getBase();
/* 225 */     this.env.call(this.cpu, fn_ptr, mark);
/*     */     try
/*     */     {
/* 228 */       Interpreter.executeInterpreted(this, this.env);
/* 229 */       while (mark >= this.cpu.esp) {
/* 230 */         eip = this.cpu.eip == 0 ? eip : this.cpu.eip;
/* 231 */         this.executor.execute(this.cpu.ctx, this.env);
/*     */       }
/* 233 */       this.env.ret(this.cpu, this.cpu.eip, this.cpu.esp);
/*     */     }
/*     */     catch (RuntimeException re) {
/* 236 */       throw exception(re, "@%08X:%08X  %s ", new Object[] { Integer.valueOf(eip), Integer.valueOf(eip - base), label });
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   public void interpret(Object label)
/*     */   {
/* 243 */     int mark = this.cpu.esp;
/* 244 */     int fn_ptr = this.cpu.eip;
/* 245 */     int eip = fn_ptr;
/* 246 */     int base = this.env.getBase();
/*     */     try {
/* 248 */       this.executor.interpret(this, this.env);
/*     */     } catch (RuntimeException re) {
/* 250 */       throw exception(re, "@%08X:%08X  %s ", new Object[] { Integer.valueOf(eip), Integer.valueOf(eip - base), label });
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   public void execute(Object label)
/*     */   {
/* 257 */     int mark = this.cpu.esp;
/* 258 */     int fn_ptr = this.cpu.eip;
/* 259 */     int eip = fn_ptr;
/* 260 */     int base = this.env.getBase();
/* 261 */     this.env.call(this.cpu, fn_ptr, mark);
/*     */     try {
/* 263 */       while (mark >= this.cpu.esp) {
/* 264 */         this.executor.execute(this.cpu.ctx, this.env);
/*     */       }
/* 266 */       this.env.ret(this.cpu, this.cpu.eip, this.cpu.esp);
/*     */     }
/*     */     catch (RuntimeException re) {
/* 269 */       throw exception(re, "@%08X:%08X  %s ", new Object[] { Integer.valueOf(eip), Integer.valueOf(eip - base), label });
/*     */     }
/*     */   }
/*     */   
/*     */   public void call(int fn_ptr, Object label)
/*     */   {
/* 275 */     int stack = push(-268431857);
/*     */     
/* 277 */     int eip = fn_ptr;
/* 278 */     this.env.call(this.cpu, fn_ptr, stack);
/* 279 */     int base = this.env.getBase();
/*     */     try {
/* 281 */       this.cpu.eip = fn_ptr;
/* 282 */       call();
/*     */ 
/*     */     }
/*     */     catch (FunctionEndException re)
/*     */     {
/*     */ 
/* 288 */       warn("Maybe LongJump in %08X : %s", new Object[] { Integer.valueOf(fn_ptr), label });
/*     */     } catch (RuntimeException re) {
/* 290 */       throw exception(re, "@%08X:%08X  %s ", new Object[] { Integer.valueOf(eip), Integer.valueOf(eip - base), label });
/*     */     } finally {
/* 292 */       this.env.setBase(base);
/*     */     }
/*     */   }
/*     */   
/*     */   public int getDepth() {
/* 297 */     return this.env.getDepth();
/*     */   }
/*     */   
/*     */   public void setDepth(int d) {
/* 301 */     this.env.setDepth(d);
/*     */   }
/*     */   
/*     */   public int pop() {
/* 305 */     int ret = this.memory.getDoubleWord(this.cpu.esp);
/* 306 */     this.cpu.esp += 4;
/* 307 */     return ret;
/*     */   }
/*     */   
/*     */   public int peek() {
/* 311 */     return this.memory.getDoubleWord(this.cpu.esp);
/*     */   }
/*     */   
/*     */   public int push(int val) {
/* 315 */     this.cpu.esp -= 4;
/* 316 */     this.memory.setDoubleWord(this.cpu.esp, val);
/* 317 */     return this.cpu.esp;
/*     */   }
/*     */   
/*     */   public ThreadContext pushl(int val) {
/* 321 */     this.cpu.esp -= 4;
/* 322 */     this.memory.setDoubleWord(this.cpu.esp, val);
/* 323 */     return this;
/*     */   }
/*     */   
/*     */   public int push(int[] array) {
/* 327 */     for (int i = array.length - 1; i >= 0; i--) {
/* 328 */       push(array[i]);
/*     */     }
/* 330 */     return this.cpu.esp;
/*     */   }
/*     */   
/*     */   public int pushs(String str) {
/*     */     try {
/* 335 */       byte[] bytes = str.getBytes("UTF-8");
/* 336 */       this.cpu.esp -= bytes.length + 2;
/* 337 */       this.memory.copyArrayIntoContents(this.cpu.esp, bytes, 0, bytes.length);
/*     */     } catch (UnsupportedEncodingException e) {
/* 339 */       e.printStackTrace();
/*     */     }
/* 341 */     return this.cpu.esp;
/*     */   }
/*     */   
/*     */   public int ret() {
/* 345 */     int addr = pop();
/* 346 */     this.cpu.eip = addr;
/* 347 */     this.env.ret(this.cpu, this.cpu.eip, this.cpu.esp);
/* 348 */     return addr;
/*     */   }
/*     */   
/*     */   public void ret(int amt)
/*     */   {
/* 353 */     this.cpu.eip = pop();
/* 354 */     this.cpu.esp += (amt << 2);
/* 355 */     this.env.ret(this.cpu, this.cpu.eip, this.cpu.esp);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void spawn(int toAddr)
/*     */   {
/* 364 */     push(-268431857);
/* 365 */     this.cpu.eip = toAddr;
/* 366 */     this.env.call(this.cpu, toAddr, this.cpu.esp);
/*     */   }
/*     */   
/*     */   public int getESP() {
/* 370 */     return this.cpu.esp;
/*     */   }
/*     */   
/*     */   public void setESP(int newEsp) {
/* 374 */     this.cpu.esp = newEsp;
/*     */   }
/*     */   
/*     */   public void stackAlign(int amount) {
/* 378 */     int current = this.cpu.esp;
/* 379 */     int _new = current & (amount - 1 ^ 0xFFFFFFFF);
/* 380 */     this.cpu.esp = _new;
/*     */   }
/*     */   
/*     */   public void setBase(int base)
/*     */   {
/* 385 */     this.env.setBase(base);
/*     */   }
/*     */   
/*     */   public RuntimeException exception(RuntimeException ex, String fmt, Object... args)
/*     */   {
/* 390 */     if (!(ex instanceof FlowException)) {
/* 391 */       this.log.error(this.threadId, fmt, args);
/*     */     }
/* 393 */     return ex;
/*     */   }
/*     */   
/*     */   private ILog log()
/*     */   {
/* 398 */     if ((this.log == null) || (this.silent)) {
/* 399 */       return ILog.NULL;
/*     */     }
/* 401 */     return this.log;
/*     */   }
/*     */   
/*     */   public void callIndirect(int fn_ptr_ptr, Object label) {
/* 405 */     int fn_ptr = this.memory.getDoubleWord(fn_ptr_ptr);
/* 406 */     call(fn_ptr, label);
/*     */   }
/*     */   
/*     */   public void begin(int entryPoint) {
/* 410 */     clearCpu();
/* 411 */     this.cpu.eip = entryPoint;
/*     */   }
/*     */   
/*     */   public void clearCpu()
/*     */   {
/* 416 */     this.cpu.eax = 0;
/* 417 */     this.cpu.ebx = 0;
/* 418 */     this.cpu.ecx = 0;
/* 419 */     this.cpu.edx = 0;
/* 420 */     this.cpu.esi = 0;
/* 421 */     this.cpu.edi = 0;
/*     */   }
/*     */   
/*     */   public int getTlsAddress(int $tls_index) {
/* 425 */     int moduleIndex = this.memory.getDoubleWord($tls_index);
/* 426 */     int offset = this.memory.getDoubleWord($tls_index + 4);
/* 427 */     return getTlsAddress(moduleIndex, offset);
/*     */   }
/*     */   
/*     */   public int getTlsAddress(int moduleIndex, int offset) {
/* 431 */     return Tls.getAddress(this.memory, this.threadPointer, moduleIndex, offset);
/*     */   }
/*     */   
/*     */   public void setTlsAddress(int moduleIndex, int addr) {
/* 435 */     Tls.setAddress(this.memory, this.threadPointer, moduleIndex, addr);
/*     */   }
/*     */   
/*     */   public void setTCB(int field, int value) {
/* 439 */     this.memory.setDoubleWord(this.threadPointer + field, value);
/*     */   }
/*     */   
/*     */   public int getTCB(int field)
/*     */   {
/* 444 */     return this.memory.getDoubleWord(this.threadPointer + field);
/*     */   }
/*     */   
/*     */   public void setThreadPointer(int segbase) {
/* 448 */     this.memory.setDoubleWord(segbase, segbase);
/* 449 */     this.threadPointer = segbase;
/* 450 */     this.cpu.gs = new Segment(segbase, 0);
/* 451 */     this.threadId = String.format("%2d:%08X", new Object[] { Integer.valueOf(this.tid), Integer.valueOf(this.threadPointer) });
/*     */   }
/*     */   
/*     */   public int getFlags()
/*     */   {
/* 456 */     return this.flags;
/*     */   }
/*     */   
/*     */   public void setClearChildTid(int address) {
/* 460 */     this.clearChildTid = address;
/* 461 */     this.memory.setDoubleWord(address, this.tid);
/*     */   }
/*     */   
/*     */   public void setSetChildTid(int address) {
/* 465 */     this.setChildTid = address;
/* 466 */     this.memory.setDoubleWord(address, this.tid);
/*     */   }
/*     */   
/*     */   public void setChildTid(int value) {
/* 470 */     this.memory.setDoubleWord(this.setChildTid, value);
/*     */   }
/*     */   
/*     */   public int getTid() {
/* 474 */     return this.tid;
/*     */   }
/*     */   
/*     */   public void setRobustList(int head$, int len) {
/* 478 */     this.robustlistHead$ = head$;
/* 479 */     this.robustlistLen = len;
/*     */   }
/*     */   
/*     */   public int getRobustlistHead$() {
/* 483 */     return this.robustlistHead$;
/*     */   }
/*     */   
/*     */   public int getRobustlistLen() {
/* 487 */     return this.robustlistLen;
/*     */   }
/*     */   
/*     */   public int getBase() {
/* 491 */     return this.env.getBase();
/*     */   }
/*     */   
/*     */   public ThreadContext dump() {
/* 495 */     Dump.dump(this.cpu, this.memory, this.iolib);
/* 496 */     return this;
/*     */   }
/*     */   
/*     */   public ThreadContext dumpAll() {
/* 500 */     Dump.dump(this.cpu, this.memory, this.iolib);
/* 501 */     System.err.println(this.env);
/* 502 */     return this;
/*     */   }
/*     */   
/*     */   public ThreadContext registers() {
/* 506 */     Dump.registers(this.cpu, this.memory, this.iolib);
/* 507 */     return this;
/*     */   }
/*     */   
/*     */   public void eax(int value) {
/* 511 */     this.cpu.eax = value;
/*     */   }
/*     */   
/*     */   public int eax() {
/* 515 */     return this.cpu.eax;
/*     */   }
/*     */   
/*     */   public void esp(int esp) {
/* 519 */     this.cpu.esp = esp;
/*     */   }
/*     */   
/*     */   public int esp() {
/* 523 */     return this.cpu.esp;
/*     */   }
/*     */   
/*     */   public void trace(String fmt, Object... args)
/*     */   {
/* 528 */     log().info(this.threadId, fmt, args);
/*     */   }
/*     */   
/*     */   public void enter(Object label) {
/* 532 */     log().warn(this.threadId, "Enter  :[%08X]=%08X %s ", new Object[] { Integer.valueOf(this.cpu.esp), Integer.valueOf(this.memory.getDoubleWord(this.cpu.esp)), label });
/*     */   }
/*     */   
/*     */   public void exit(Object label)
/*     */   {
/* 537 */     log().warn(this.threadId, "Exit   :[%08X]=%08X %s -> %08X ", new Object[] { Integer.valueOf(this.cpu.esp - 4), Integer.valueOf(this.memory.getDoubleWord(this.cpu.esp - 4)), label, Integer.valueOf(this.cpu.eip) });
/*     */   }
/*     */   
/*     */   public void exception(Object label) {
/* 541 */     log().error(this.threadId, "In %s return to  %08X", new Object[] { label, Integer.valueOf(this.cpu.eip) });
/*     */   }
/*     */   
/*     */   public void warn(String fmt, Object... args) {
/* 545 */     log().warn(this.threadId, fmt, args);
/*     */   }
/*     */   
/*     */   public void info(String fmt, Object... args) {
/* 549 */     log().info(this.threadId, fmt, args);
/*     */   }
/*     */   
/*     */   public void debug(String fmt, Object... args) {
/* 553 */     log().debug(this.threadId, fmt, args);
/*     */   }
/*     */   
/*     */   public void start() {
/* 557 */     ProteusThread realThread = new ProteusThread();
/* 558 */     realThread.start();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void yield() {}
/*     */   
/*     */ 
/*     */ 
/*     */   public Signals getSignals()
/*     */   {
/* 570 */     return this.signals;
/*     */   }
/*     */   
/*     */   public String getThreadId() {
/* 574 */     return this.threadId;
/*     */   }
/*     */   
/*     */   public static ThreadContext currentCtx() {
/* 578 */     return (ThreadContext)_current.get();
/*     */   }
/*     */   
/*     */   public void call() {
/* 582 */     if ((Trace.compareToInt) && 
/* 583 */       (this.cpu == Trace.interpreted.context.cpu))
/* 584 */       return;
/* 585 */     this.depth += 1;
/* 586 */     int eip = this.cpu.eip;
/* 587 */     if (TRACE) { this.log.enter(this.threadId, this.depth, eip, null);
/*     */     }
/* 589 */     if (INTERPRET) {
/* 590 */       this.executor.interpret(this, this.env);
/*     */     } else
/* 592 */       this.executor.execute(this, this.env);
/* 593 */     if (TRACE) { this.log.exit(this.threadId, this.depth, eip, this.cpu.eip, Integer.valueOf(this.cpu.eip));
/*     */     }
/* 595 */     this.depth -= 1;
/*     */   }
/*     */   
/*     */   public boolean interpret() {
/* 599 */     return this.executor.interpret(this, this.env);
/*     */   }
/*     */   
/*     */   public void lock(int address) {
/* 603 */     this.memory.lock(address);
/*     */   }
/*     */   
/*     */   public void unlock(int address)
/*     */   {
/* 608 */     this.memory.unlock(address);
/*     */   }
/*     */   
/*     */   public void step() {
/* 612 */     this.step = true;
/*     */   }
/*     */   
/*     */   public void resume() {
/* 616 */     this.step = false;
/*     */   }
/*     */   
/*     */   public void hang() {
/* 620 */     warn("Suspending Thread until Program Exit", new Object[0]);
/*     */     try {
/*     */       for (;;) {
/* 623 */         Thread.sleep(1000L);
/*     */       }
/* 625 */     } catch (InterruptedException e) { e.printStackTrace();
/*     */     }
/*     */   }
/*     */   
/*     */   public int getClearChildTid()
/*     */   {
/* 631 */     return this.clearChildTid;
/*     */   }
/*     */   
/*     */   public int getSetChildTid() {
/* 635 */     return this.setChildTid;
/*     */   }
/*     */   
/*     */   public void sleep(int timeMs) {
/* 639 */     warn("Sleeping Thread ", new Object[0]);
/*     */     try {
/* 641 */       Thread.sleep(timeMs);
/*     */     } catch (InterruptedException e) {
/* 643 */       e.printStackTrace();
/*     */     }
/*     */     
/* 646 */     warn("Resumed Thread ", new Object[0]);
/*     */   }
/*     */   
/*     */   public boolean isSilent()
/*     */   {
/* 651 */     return this.silent;
/*     */   }
/*     */   
/*     */   public void exit()
/*     */   {
/* 656 */     if (Clone.isChildClearTID(this.flags)) {
/* 657 */       this.memory.setDoubleWord(this.clearChildTid, 0);
/* 658 */       Futex.wake(this, this.clearChildTid, Integer.MAX_VALUE);
/*     */     }
/* 660 */     int signal = Clone.getSignal(this.flags);
/* 661 */     if ((signal != 0) && (this.parent != null)) {
/* 662 */       this.parent.getSignals().send(signal);
/*     */     }
/* 664 */     deregister(this);
/*     */     
/* 666 */     waitForNoThreads();
/* 667 */     throw new ProgramEndException();
/*     */   }
/*     */   
/*     */   public boolean isKilled() {
/* 671 */     return this.killed;
/*     */   }
/*     */   
/*     */   public ManagedMemory getSystemAllocator() {
/* 675 */     return this.systemAllocator;
/*     */   }
/*     */   
/*     */   public void out(String strace, String fmt, Object... args) {
/* 679 */     log().out(this.threadId, strace, fmt, args);
/*     */   }
/*     */   
/*     */   public void killAll() {
/* 683 */     getSignals().send(9);
/*     */   }
/*     */   
/*     */   public final class ProteusThread
/*     */     extends Thread
/*     */   {
/*     */     public ProteusThread() {}
/*     */     
/*     */     public void run()
/*     */     {
/* 693 */       ThreadContext._current.set(ThreadContext.this);
/* 694 */       ThreadContext.this.log().info(ThreadContext.this.threadId, "Start", new Object[0]);
/*     */       try {
/* 696 */         int start = ThreadContext.this.cpu.esp;
/*     */         
/* 698 */         while (ThreadContext.this.cpu.esp <= start)
/* 699 */           ThreadContext.this.executeImpl();
/*     */       } catch (ThreadEndException ise) {
/* 701 */         ise = 
/*     */         
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 710 */           ise;
/*     */       }
/*     */       catch (ProgramEndException ise)
/*     */       {
/* 703 */         ise = 
/*     */         
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 710 */           ise;
/*     */       }
/*     */       catch (Exception ise)
/*     */       {
/* 705 */         ise = 
/*     */         
/*     */ 
/*     */ 
/*     */ 
/* 710 */           ise;ThreadContext.this.dump();System.err.println(ThreadContext.this.env);System.exit(0);
/*     */       } finally {}
/*     */     }
/*     */   }
/*     */   
/*     */   public static String getPadding(int depth) {
/* 716 */     StringBuilder b = new StringBuilder();
/* 717 */     for (int i = 0; i < depth + 1; i++)
/* 718 */       b.append("   ");
/* 719 */     return b.toString();
/*     */   }
/*     */   
/*     */ 
/*     */   public int getLiveThreadCount()
/*     */   {
/* 725 */     return _allThreads.size();
/*     */   }
/*     */   
/*     */   public void waitForNoThreads() {
/* 729 */     while (getLiveThreadCount() != 0) {
/* 730 */       synchronized (_allThreads) {
/*     */         try {
/* 732 */           _allThreads.wait(1000L);
/*     */         } catch (InterruptedException e) {}
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public static ThreadContext getThreadCtx(int tid) {
/* 739 */     return (ThreadContext)_allThreads.get(Integer.valueOf(tid));
/*     */   }
/*     */   
/*     */   public void exitGroup()
/*     */   {
/* 744 */     ThreadContext[] threadContexts = (ThreadContext[])_allThreads.values().toArray(new ThreadContext[_allThreads.size()]);
/* 745 */     for (int i = 0; i < threadContexts.length; i++) {
/* 746 */       ThreadContext threadContext = threadContexts[i];
/* 747 */       threadContext.kill();
/*     */     }
/* 749 */     Futex.wakeAll();
/* 750 */     exit();
/*     */   }
/*     */   
/*     */   private void kill() {
/* 754 */     this.killed = true;
/*     */   }
/*     */   
/*     */   private static int register(ThreadContext ctx) {
/* 758 */     int tid = TID_SEED.incrementAndGet();
/* 759 */     synchronized (_allThreads) {
/* 760 */       _allThreads.put(Integer.valueOf(tid), ctx);
/* 761 */       _allThreads.notifyAll();
/*     */     }
/* 763 */     return tid;
/*     */   }
/*     */   
/*     */   private static int deregister(ThreadContext ctx) {
/* 767 */     int tid = ctx.tid;
/* 768 */     synchronized (_allThreads) {
/* 769 */       _allThreads.remove(Integer.valueOf(tid));
/* 770 */       _allThreads.notifyAll();
/*     */     }
/* 772 */     return tid;
/*     */   }
/*     */ }


/* Location:              C:\Users\Joey\Downloads\codejar.jar!\com\emt\proteus\runtime32\ThreadContext.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */