/*     */ package com.emt.proteus.runtime32;
/*     */ 
/*     */ import com.emt.proteus.runtime32.api.IoSystem;
/*     */ import com.emt.proteus.runtime32.io.IoLib;
/*     */ import com.emt.proteus.runtime32.memory.MainMemory;
/*     */ import com.emt.proteus.runtime32.rtld.DynamicLinker;
/*     */ import com.emt.proteus.runtime32.rtld.ElfModule;
/*     */ import com.emt.proteus.utils.ILog;
/*     */ import com.emt.proteus.utils.Utils;
/*     */ import java.io.PrintStream;
/*     */ import java.net.URL;
/*     */ import java.util.Formatter;
/*     */ import java.util.Map;
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
/*     */ public class ProteusRuntime
/*     */ {
/*  26 */   private static final boolean STACK_WATCH = Option.stackWatch.value();
/*     */   private RuntimeArgs runtimeArgs;
/*     */   
/*     */   public static class DefaultEnvironment extends BasicBlockExecutionEnvironment {
/*  30 */     private static final boolean LOG_DECODE = Option.decode.value();
/*  31 */     private int[] callStack = new int[''];
/*  32 */     private int[] retStack = new int[''];
/*  33 */     private int[] values = new int[''];
/*     */     private boolean trace;
/*     */     
/*     */     public DefaultEnvironment() {
/*  37 */       super();
/*     */     }
/*     */     
/*     */     public void call(Processor cpu, int eip, int esp)
/*     */     {
/*  42 */       if (ProteusRuntime.STACK_WATCH) {
/*     */         try {
/*  44 */           this.callStack[this.depth] = eip;
/*     */         } catch (ArrayIndexOutOfBoundsException aie) {
/*  46 */           this.callStack = Utils.grow(this.callStack);
/*  47 */           this.retStack = Utils.grow(this.retStack);
/*  48 */           this.values = Utils.grow(this.retStack);
/*  49 */           this.callStack[this.depth] = eip;
/*     */         }
/*  51 */         this.retStack[this.depth] = esp;
/*  52 */         this.values[this.depth] = cpu.ctx.getMemory().getDoubleWord(esp);
/*  53 */         this.depth += 1;
/*     */       } else {
/*  55 */         super.call(cpu, eip, esp);
/*     */       }
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */     public void ret(Processor cpu, int eip, int esp)
/*     */     {
/*  63 */       if (ProteusRuntime.STACK_WATCH) {
/*  64 */         int actualEsp = esp - 4;
/*  65 */         int expectedEsp = this.retStack[(--this.depth)];
/*  66 */         int function = this.callStack[this.depth];
/*  67 */         int ret = this.values[this.depth];
/*  68 */         if (ret != eip) {
/*  69 */           MainMemory memory = cpu.ctx.getMemory();
/*  70 */           cpu.ctx.log.warn("STACK_WATCH", "Return from %08X()-> %08X actual=%08X  expected=%08X  -> %08X ", new Object[] { Integer.valueOf(function), Integer.valueOf(eip), Integer.valueOf(actualEsp), Integer.valueOf(expectedEsp), Integer.valueOf(memory.getDoubleWord(expectedEsp)) });
/*     */         }
/*     */       } else {
/*  73 */         super.ret(cpu, eip, esp);
/*     */       }
/*     */     }
/*     */     
/*     */ 
/*     */     public int[] extractUCodes(Processor cpu, MainMemory memory, int address)
/*     */     {
/*  80 */       if (LOG_DECODE) cpu.ctx.trace("%08X %08X -> Decode ", new Object[] { Integer.valueOf(address), Integer.valueOf(address - getBase()) });
/*  81 */       return super.extractUCodes(cpu, memory, address);
/*     */     }
/*     */     
/*     */ 
/*     */     public int getStatus(int currentAddress)
/*     */     {
/*  87 */       return -1;
/*     */     }
/*     */     
/*     */     public String toString()
/*     */     {
/*  92 */       StringBuilder builder = new StringBuilder(8192);
/*  93 */       Formatter formatter = new Formatter(builder);
/*  94 */       for (int i = 0; i < this.depth; i++) {
/*  95 */         formatter.format("%2d) %08X   [%08X]=%08X\n", new Object[] { Integer.valueOf(i), Integer.valueOf(this.callStack[i]), Integer.valueOf(this.retStack[i]), Integer.valueOf(this.values[i]) });
/*     */       }
/*  97 */       return builder.toString();
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   private String copyLibrary;
/*     */   
/*     */   private ThreadContext mainThread;
/*     */   public ProteusRuntime(MainMemory memory, String[] args, Map<String, String> env, ILog log)
/*     */   {
/* 107 */     this(memory, null, args, env, new DefaultEnvironment(), log);
/*     */   }
/*     */   
/*     */   public ProteusRuntime(MainMemory memory, String program, String[] args, Map<String, String> env, ExecutionEnvironment ee, ILog log) {
/* 111 */     this.mainThread = new ThreadContext(ee, memory, log);
/* 112 */     this.runtimeArgs = new RuntimeArgs(program, args, env);
/*     */   }
/*     */   
/*     */   public ProteusRuntime(RuntimeArgs runtimeArgs, ThreadContext mainThread) {
/* 116 */     this.runtimeArgs = runtimeArgs;
/* 117 */     this.mainThread = mainThread;
/*     */   }
/*     */   
/*     */   public int exec()
/*     */   {
/* 122 */     DynamicLinker linker = new DynamicLinker(this.mainThread);
/* 123 */     linker.setCopyLibrary(this.copyLibrary);
/* 124 */     String[] env = this.runtimeArgs.getEnv();
/* 125 */     System.out.flush();
/*     */     try {
/* 127 */       IoLib ioLib = this.mainThread.getIoLib();
/* 128 */       program = ioLib.getIoSystem().getUrl(this.runtimeArgs.getProgram());
/* 129 */       ElfModule module = new ElfModule(program);
/* 130 */       linker.dl_start(module, this.runtimeArgs, -1073741824);
/* 131 */       return this.mainThread.execute();
/*     */     } catch (Exception e) { URL program;
/* 133 */       e.printStackTrace();
/* 134 */       return -1;
/*     */     } finally {
/* 136 */       linker.dispose();
/* 137 */       this.mainThread.iolib.stop();
/*     */     }
/*     */   }
/*     */   
/*     */   public void setCopyLibrary(String copyLibrary) {
/* 142 */     this.copyLibrary = copyLibrary;
/*     */   }
/*     */   
/* 145 */   public void kill() { this.mainThread.killAll(); }
/*     */ }


/* Location:              C:\Users\Joey\Downloads\codejar.jar!\com\emt\proteus\runtime32\ProteusRuntime.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */