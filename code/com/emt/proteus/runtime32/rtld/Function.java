/*     */ package com.emt.proteus.runtime32.rtld;
/*     */ 
/*     */ import com.emt.proteus.runtime32.CallArgs;
/*     */ import com.emt.proteus.runtime32.ExecutionEnvironment;
/*     */ import com.emt.proteus.runtime32.FatalException;
/*     */ import com.emt.proteus.runtime32.FlowException;
/*     */ import com.emt.proteus.runtime32.Option;
/*     */ import com.emt.proteus.runtime32.Option.Opt;
/*     */ import com.emt.proteus.runtime32.Processor;
/*     */ import com.emt.proteus.runtime32.ThreadContext;
/*     */ import com.emt.proteus.runtime32.memory.MainMemory;
/*     */ import com.emt.proteus.runtime32.sockets.SocketAPI;
/*     */ import java.lang.reflect.Constructor;
/*     */ import java.util.Hashtable;
/*     */ 
/*     */ public class Function extends com.emt.proteus.runtime32.Trace
/*     */ {
/*     */   public static final int END_OF_FUNCTION = -268431857;
/*  19 */   private static final boolean LTRACE = Option.ltrace.value();
/*  20 */   public static final boolean WATCHED_FUNCTIONS_SET = Option.watch_function.isSet();
/*  21 */   public static final String WATCHED_FUNCTIONS = Option.watch_function.value();
/*  22 */   public static final boolean SUSPEND_FUNCTIONS_SET = Option.suspend_function.isSet();
/*  23 */   public static final String SUSPEND_FUNCTIONS = Option.suspend_function.value();
/*     */   
/*     */   public static final int OBS_START = 0;
/*     */   
/*     */   public static final int OBS_END = 0;
/*  28 */   private static final FunctionMap overrides = new FunctionMap(null).register("getaddrinfo", GetAddrInfo.class).register("freeaddrinfo", FreeAddrInfo.class);
/*     */   
/*     */ 
/*     */ 
/*     */   private Object label;
/*     */   
/*     */ 
/*     */ 
/*     */   private final boolean output;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public Function()
/*     */   {
/*  43 */     this(null, false);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*  48 */   public Function(Object label) { this(label, label != null); }
/*     */   
/*     */   public Function(Object label, boolean output) {
/*  51 */     this.label = label;
/*  52 */     this.output = output;
/*     */   }
/*     */   
/*     */   public final void executeEntry(ThreadContext threadContext, ExecutionEnvironment env)
/*     */   {
/*  57 */     execute(threadContext, env);
/*     */   }
/*     */   
/*     */   public final void execute(ThreadContext ctx, ExecutionEnvironment env) {
/*  61 */     boolean success = false;
/*  62 */     if ((LTRACE) && (this.output)) ctx.enter(getLabel());
/*     */     try {
/*  64 */       executeImpl(ctx, env);
/*  65 */       if ((LTRACE) && (this.output)) {
/*  66 */         success = true;
/*     */       }
/*     */     } catch (NullPointerException npe) {
/*  69 */       throw new FatalException(npe);
/*     */     } catch (FlowException fe) {
/*  71 */       success = true;
/*  72 */       throw fe;
/*     */     } finally {
/*  74 */       if ((LTRACE) && (this.output)) {
/*  75 */         if (success) {
/*  76 */           ctx.exit(getLabel());
/*     */         } else {
/*  78 */           ctx.exception(getLabel());
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public final Object getLabel() {
/*  85 */     return this.label;
/*     */   }
/*     */   
/*     */   public final void setLabel(Object label) {
/*  89 */     this.label = label;
/*     */   }
/*     */   
/*  92 */   public String toString() { return this.label != null ? this.label.toString() : getClass().getSimpleName(); }
/*     */   
/*     */   public void executeImpl(ThreadContext ctx, ExecutionEnvironment env) {
/*  95 */     throw new UnsupportedOperationException("ANON");
/*     */   }
/*     */   
/*     */   public static Function trampoline(int callAddress, int executionAddress, Function target) {
/*  99 */     return new Trampoline(executionAddress);
/*     */   }
/*     */   
/*     */   public static class Jump extends Function {
/*     */     private final int real;
/*     */     
/*     */     public Jump(int real) {
/* 106 */       super();
/* 107 */       this.real = real;
/*     */     }
/*     */     
/* 110 */     public Jump(Object label, int real) { super();
/* 111 */       this.real = real;
/*     */     }
/*     */     
/*     */     public void executeImpl(ThreadContext ctx, ExecutionEnvironment env)
/*     */     {
/* 116 */       ctx.cpu.eip = this.real;
/* 117 */       ctx.execute(getLabel());
/*     */     }
/*     */   }
/*     */   
/*     */   public static class Default extends Function
/*     */   {
/*     */     public Default(Object label) {
/* 124 */       super();
/*     */     }
/*     */     
/*     */     public void executeImpl(ThreadContext ctx, ExecutionEnvironment env)
/*     */     {
/* 129 */       ctx.interpret(getLabel());
/*     */     }
/*     */   }
/*     */   
/*     */   public static class Interpret extends Function {
/*     */     public Interpret(Object label) {
/* 135 */       super();
/*     */     }
/*     */     
/*     */     public void executeImpl(ThreadContext ctx, ExecutionEnvironment env)
/*     */     {
/* 140 */       ctx.interpret(getLabel());
/*     */     }
/*     */   }
/*     */   
/*     */   public static class Watch extends Function {
/* 145 */     public Watch(Object label) { super(); }
/*     */     
/*     */ 
/*     */     public void executeImpl(ThreadContext ctx, ExecutionEnvironment env)
/*     */     {
/* 150 */       int a0 = CallArgs.cdecl(ctx, 0);
/* 151 */       int a1 = CallArgs.cdecl(ctx, 1);
/* 152 */       int a2 = CallArgs.cdecl(ctx, 2);
/*     */       
/* 154 */       ctx.warn(" >>> %s(%08X:%s)", new Object[] { getLabel(), Integer.valueOf(a0), ctx.memory.string(a0) });
/*     */       
/*     */ 
/* 157 */       ctx.interpret(getLabel());
/* 158 */       int result = ctx.cpu.eax;
/*     */       
/*     */ 
/* 161 */       ctx.warn(" <<< %s(%08X:%s)=%08X:%s ->%08X", new Object[] { getLabel(), Integer.valueOf(a0), ctx.memory.string(a0), Integer.valueOf(result), ctx.memory.string(result), Integer.valueOf(ctx.cpu.eip) });
/*     */     }
/*     */   }
/*     */   
/*     */   public static class SuspendFunction
/*     */     extends Function
/*     */   {
/*     */     public SuspendFunction(Object label)
/*     */     {
/* 170 */       super();
/*     */     }
/*     */     
/*     */     public void executeImpl(ThreadContext ctx, ExecutionEnvironment env) {
/* 174 */       if (ctx.getTid() != 2) {
/* 175 */         ctx.hang();
/*     */       } else {
/* 177 */         ctx.sleep(5000);
/*     */       }
/* 179 */       ctx.safeExecute(getLabel());
/*     */     }
/*     */   }
/*     */   
/*     */   public static class SystemCall extends Function
/*     */   {
/*     */     public SystemCall()
/*     */     {
/* 187 */       super(false);
/*     */     }
/*     */     
/*     */     public void executeImpl(ThreadContext ctx, ExecutionEnvironment env) {
/* 191 */       if (Function.LTRACE) ctx.warn("GS:Syscall eax=0x%X", new Object[] { Integer.valueOf(ctx.cpu.eax) });
/* 192 */       Processor cpu = ctx.getCpu();
/* 193 */       cpu.eax = com.emt.proteus.runtime32.syscall.SystemCalls.syscall(ctx.getMemory(), cpu, ctx.getIoLib(), cpu.eax, cpu.ebx, cpu.ecx, cpu.edx, cpu.esi, cpu.edi, cpu.esp, cpu.ebp);
/* 194 */       ctx.ret();
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public static class ZeroExec
/*     */     extends Function
/*     */   {
/*     */     public void executeImpl(ThreadContext ctx, ExecutionEnvironment env)
/*     */     {
/* 205 */       int ret = ctx.peek();
/* 206 */       ctx.dump();
/* 207 */       throw new FatalException("Attempting to execute instructions at 0 returning to " + Integer.toHexString(ret));
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/* 213 */   public static Function indirection(Object label, int pointerAddress) { return new Jump(label, pointerAddress); }
/*     */   
/*     */   public static Function create(Symbols.Symbol symbol) {
/* 216 */     Function function = create(symbol.getName(), symbol);
/* 217 */     symbol.setObject(function);
/* 218 */     return function;
/*     */   }
/*     */   
/* 221 */   public static Function create(String name, Object label) { Function override = overrides.instance(name, label);
/* 222 */     if (override != null) return override;
/* 223 */     if ((SUSPEND_FUNCTIONS_SET) && (SUSPEND_FUNCTIONS.contains(name)))
/* 224 */       return new SuspendFunction(label);
/* 225 */     if ((WATCHED_FUNCTIONS_SET) && (WATCHED_FUNCTIONS.contains(name))) {
/* 226 */       return new Watch(label);
/*     */     }
/* 228 */     return new Default(label);
/*     */   }
/*     */   
/*     */   public static Function create(ThreadContext context, int eip)
/*     */   {
/* 233 */     int codeWord = context.memory.getDoubleWord(eip);
/* 234 */     int rel = eip - 0;
/* 235 */     if (codeWord == -1021043573)
/* 236 */       return new BxPcThunk(eip);
/* 237 */     if ((eip >= 0) && (eip <= 0)) {
/* 238 */       return new Watch(String.format("anon:[%08X:%08X]", new Object[] { Integer.valueOf(eip), Integer.valueOf(rel) }));
/*     */     }
/* 240 */     return new Default(String.format("anon:%08X", new Object[] { Integer.valueOf(eip) }));
/*     */   }
/*     */   
/*     */   private static class FunctionMap
/*     */   {
/* 245 */     private final Hashtable<String, Class> entries = new Hashtable();
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 251 */     public Class get(String name) { return (Class)this.entries.get(name); }
/*     */     
/*     */     public FunctionMap register(Class... function) {
/* 254 */       for (int i = 0; i < function.length; i++) {
/* 255 */         Class aClass = function[i];
/* 256 */         String name = aClass.getSimpleName();
/* 257 */         this.entries.put(name, aClass);
/*     */       }
/* 259 */       return this;
/*     */     }
/*     */     
/* 262 */     public FunctionMap register(String name, Class function) { this.entries.put(name, function);
/* 263 */       return this;
/*     */     }
/*     */     
/*     */     public Function instance(String name, Object arg) {
/* 267 */       Class aClass = get(name);
/*     */       try
/*     */       {
/* 270 */         if (aClass == null) return null;
/* 271 */         Constructor constructor = aClass.getConstructor(new Class[] { Object.class });
/* 272 */         return (Function)constructor.newInstance(new Object[] { arg });
/*     */       } catch (Exception e) {
/* 274 */         e.printStackTrace(); }
/* 275 */       return null;
/*     */     }
/*     */   }
/*     */   
/*     */   private static class BxPcThunk extends Function
/*     */   {
/*     */     private final int eip;
/*     */     
/*     */     public BxPcThunk(int eip) {
/* 284 */       super();
/* 285 */       this.eip = eip;
/*     */     }
/*     */     
/*     */     public void executeImpl(ThreadContext ctx, ExecutionEnvironment env)
/*     */     {
/* 290 */       int address = ctx.ret();
/* 291 */       ctx.cpu.ebx = address;
/* 292 */       if (Function.LTRACE) ctx.warn("__i686.get_pc_thunk.bx@%08X ebx=%08X", new Object[] { Integer.valueOf(this.eip), Integer.valueOf(address) });
/*     */     }
/*     */   }
/*     */   
/*     */   private static class Trampoline extends Function {
/*     */     private final int targetEip;
/*     */     
/* 299 */     public Trampoline(int eip) { super();
/* 300 */       this.targetEip = eip;
/*     */     }
/*     */     
/*     */     public void executeImpl(ThreadContext ctx, ExecutionEnvironment env)
/*     */     {
/* 305 */       ctx.cpu.eip = this.targetEip;
/* 306 */       ctx.call();
/*     */     }
/*     */   }
/*     */   
/*     */   public static class GetAddrInfo
/*     */     extends Function
/*     */   {
/*     */     public GetAddrInfo(Object label)
/*     */     {
/* 315 */       super();
/*     */     }
/*     */     
/*     */     public void executeImpl(ThreadContext ctx, ExecutionEnvironment env)
/*     */     {
/* 320 */       int node$ = CallArgs.cdecl(ctx, 0);
/* 321 */       int service$ = CallArgs.cdecl(ctx, 1);
/* 322 */       int hint$ = CallArgs.cdecl(ctx, 2);
/* 323 */       int res$$ = CallArgs.cdecl(ctx, 3);
/* 324 */       int result = SocketAPI.getAddrInfo(ctx, node$, service$, hint$, res$$);
/* 325 */       ctx.eax(result);
/* 326 */       ctx.ret();
/*     */     }
/*     */   }
/*     */   
/*     */   public static class FreeAddrInfo extends Function {
/* 331 */     public FreeAddrInfo(Object label) { super(); }
/*     */     
/*     */ 
/*     */     public void executeImpl(ThreadContext ctx, ExecutionEnvironment env)
/*     */     {
/* 336 */       int res$$ = CallArgs.cdecl(ctx, 0);
/* 337 */       SocketAPI.freeAddrInfo(ctx, res$$);
/* 338 */       ctx.ret();
/*     */     }
/*     */   }
/*     */   
/*     */   public static class GetEnv extends Function {
/* 343 */     public GetEnv(Object label) { super(); }
/*     */     
/*     */ 
/*     */     public void executeImpl(ThreadContext ctx, ExecutionEnvironment env)
/*     */     {
/* 348 */       String key = ctx.memory.string(CallArgs.cdecl(ctx, 0));
/* 349 */       ctx.interpret(getLabel());
/* 350 */       String val = ctx.memory.string(ctx.eax());
/*     */     }
/*     */   }
/*     */   
/*     */   public static class __IO_file_overflow extends Function {
/*     */     public __IO_file_overflow(Object label) {
/* 356 */       super();
/*     */     }
/*     */     
/*     */     public void executeImpl(ThreadContext ctx, ExecutionEnvironment env) {
/* 360 */       int dpy$ = CallArgs.cdecl(ctx, 0);
/* 361 */       String type = ctx.memory.string(CallArgs.cdecl(ctx, 1));
/* 362 */       String filename = ctx.memory.string(CallArgs.cdecl(ctx, 2));
/* 363 */       String suffix = ctx.memory.string(CallArgs.cdecl(ctx, 3));
/* 364 */       String path = ctx.memory.string(CallArgs.cdecl(ctx, 4));
/* 365 */       int sub$ = CallArgs.cdecl(ctx, 5);
/* 366 */       int nsub = CallArgs.cdecl(ctx, 6);
/* 367 */       int pred = CallArgs.cdecl(ctx, 7);
/* 368 */       ctx.out("CALL   ", "XtResolvePath:( display:0x%08X,%s,%s, %s, %s,0x%08X ,%d,0x%08X )", new Object[] { Integer.valueOf(dpy$), type, filename, suffix, path, Integer.valueOf(sub$), Integer.valueOf(nsub), Integer.valueOf(pred) });
/* 369 */       ctx.interpret(getLabel());
/* 370 */       String val = ctx.memory.string(ctx.eax());
/* 371 */       ctx.out("RETURN ", "XtResolve:( display:0x%08X,%s,%s, %s, %s,0x%08X ,%d,0x%08X )=%s", new Object[] { Integer.valueOf(dpy$), type, filename, suffix, path, Integer.valueOf(sub$), Integer.valueOf(nsub), Integer.valueOf(pred), val });
/*     */     }
/*     */   }
/*     */   
/*     */   public static class SetLocale extends Function {
/* 376 */     public SetLocale(Object label) { super(); }
/*     */     
/*     */ 
/*     */ 
/*     */     public void executeImpl(ThreadContext ctx, ExecutionEnvironment env)
/*     */     {
/* 382 */       int type = CallArgs.cdecl(ctx, 0);
/* 383 */       String locale = ctx.memory.string(CallArgs.cdecl(ctx, 1));
/* 384 */       ctx.out("CALL   ", "setlocale:( %d, %s)", new Object[] { Integer.valueOf(type), locale });
/* 385 */       ctx.interpret(getLabel());
/* 386 */       String val = ctx.memory.string(ctx.eax());
/* 387 */       ctx.out("RETURN ", "setlocale:( %d, %s)=%s", new Object[] { Integer.valueOf(type), locale, val });
/*     */     }
/*     */   }
/*     */   
/*     */   public static class XtResolvePathname extends Function {
/* 392 */     public XtResolvePathname(Object label) { super(); }
/*     */     
/*     */ 
/*     */     public void executeImpl(ThreadContext ctx, ExecutionEnvironment env)
/*     */     {
/* 397 */       int dpy$ = CallArgs.cdecl(ctx, 0);
/* 398 */       String type = ctx.memory.string(CallArgs.cdecl(ctx, 1));
/* 399 */       String filename = ctx.memory.string(CallArgs.cdecl(ctx, 2));
/* 400 */       String suffix = ctx.memory.string(CallArgs.cdecl(ctx, 3));
/* 401 */       String path = ctx.memory.string(CallArgs.cdecl(ctx, 4));
/* 402 */       int sub$ = CallArgs.cdecl(ctx, 5);
/* 403 */       int nsub = CallArgs.cdecl(ctx, 6);
/* 404 */       int pred = CallArgs.cdecl(ctx, 7);
/* 405 */       ctx.out("CALL   ", "XtResolvePath:( display:0x%08X,%s,%s, %s, %s,0x%08X ,%d,0x%08X )", new Object[] { Integer.valueOf(dpy$), type, filename, suffix, path, Integer.valueOf(sub$), Integer.valueOf(nsub), Integer.valueOf(pred) });
/* 406 */       ctx.interpret(getLabel());
/* 407 */       String val = ctx.memory.string(ctx.eax());
/* 408 */       ctx.out("RETURN ", "XtResolve:( display:0x%08X,%s,%s, %s, %s,0x%08X ,%d,0x%08X )=%s", new Object[] { Integer.valueOf(dpy$), type, filename, suffix, path, Integer.valueOf(sub$), Integer.valueOf(nsub), Integer.valueOf(pred), val });
/*     */     }
/*     */   }
/*     */   
/*     */   public static class XtFindFile extends Function {
/* 413 */     public XtFindFile(Object label) { super(); }
/*     */     
/*     */ 
/*     */ 
/*     */     public void executeImpl(ThreadContext ctx, ExecutionEnvironment env)
/*     */     {
/* 419 */       String path = ctx.memory.string(CallArgs.cdecl(ctx, 0));
/* 420 */       int sub$ = CallArgs.cdecl(ctx, 1);
/* 421 */       int nsub = CallArgs.cdecl(ctx, 2);
/* 422 */       int pred = CallArgs.cdecl(ctx, 3);
/* 423 */       ctx.out("CALL   ", "XtFindFile:( %s,0x%08X ,%d,0x%08X )", new Object[] { path, Integer.valueOf(sub$), Integer.valueOf(nsub), Integer.valueOf(pred) });
/*     */       
/* 425 */       ctx.interpret(getLabel());
/* 426 */       String val = ctx.memory.string(ctx.eax());
/* 427 */       ctx.out("RETURN ", "XtFindFile:( %s,0x%08X ,%d,0x%08X )=%s", new Object[] { path, Integer.valueOf(sub$), Integer.valueOf(nsub), Integer.valueOf(pred), val });
/*     */     }
/*     */   }
/*     */   
/*     */   public static class _Xstat64 extends Function {
/* 432 */     public _Xstat64(Object label) { super(); }
/*     */     
/*     */ 
/*     */     public void executeImpl(ThreadContext ctx, ExecutionEnvironment env)
/*     */     {
/* 437 */       String path = ctx.memory.string(CallArgs.cdecl(ctx, 0));
/* 438 */       int struct$ = CallArgs.cdecl(ctx, 1);
/* 439 */       ctx.out("CALL   ", "%s:( %s,0x%08X )", new Object[] { getLabel(), path, Integer.valueOf(struct$) });
/* 440 */       ctx.interpret(getLabel());
/* 441 */       ctx.out("RETURN ", "%s:( %s,0x%08X )=%d", new Object[] { getLabel(), path, Integer.valueOf(struct$), Integer.valueOf(ctx.eax()) });
/*     */     }
/*     */   }
/*     */   
/*     */   public static class Internal extends Function
/*     */   {
/*     */     public Internal(Object label) {
/* 448 */       super();
/*     */     }
/*     */     
/*     */     public void executeImpl(ThreadContext ctx, ExecutionEnvironment env)
/*     */     {
/* 453 */       String path = ctx.memory.string(CallArgs.internal(ctx, 0));
/* 454 */       int len = CallArgs.internal(ctx, 1);
/* 455 */       ctx.out("CALL   ", "%s:( %s,%d )", new Object[] { getLabel(), path, Integer.valueOf(len) });
/* 456 */       ctx.interpret(getLabel());
/* 457 */       ctx.out("RETURN ", "%s:( %s,%d )=0x%08", new Object[] { getLabel(), path, Integer.valueOf(len), Integer.valueOf(ctx.eax()) });
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Joey\Downloads\codejar.jar!\com\emt\proteus\runtime32\rtld\Function.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */