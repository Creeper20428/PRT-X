/*     */ package com.emt.proteus.runtime32.demo;
/*     */ 
/*     */ import com.emt.proteus.decoder.Disassembler;
/*     */ import com.emt.proteus.runtime32.Option;
/*     */ import com.emt.proteus.runtime32.Option.Switch;
/*     */ import com.emt.proteus.runtime32.ProteusRuntime;
/*     */ import com.emt.proteus.runtime32.ProteusRuntime.DefaultEnvironment;
/*     */ import com.emt.proteus.runtime32.RuntimeArgs;
/*     */ import com.emt.proteus.runtime32.SystemClock;
/*     */ import com.emt.proteus.runtime32.ThreadContext;
/*     */ import com.emt.proteus.runtime32.api.IoSystem;
/*     */ import com.emt.proteus.runtime32.api.ProteusProcess;
/*     */ import com.emt.proteus.runtime32.memory.MainMemory;
/*     */ import com.emt.proteus.utils.ILog.Stream;
/*     */ import java.io.IOException;
/*     */ import java.util.HashMap;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ProcessImpl
/*     */   implements ProteusProcess
/*     */ {
/*     */   private IoSystem ioSystem;
/*     */   private ProteusRuntime pRtLd;
/*     */   
/*     */   public void init(IoSystem ioSystem)
/*     */   {
/*  73 */     this.ioSystem = ioSystem;
/*  74 */     Option.strace.update(null, 0);
/*     */     try {
/*  76 */       Disassembler.init();
/*     */     } catch (Exception e) {
/*  78 */       e.printStackTrace();
/*     */     }
/*     */   }
/*     */   
/*     */   public void exec(String[] args)
/*     */     throws IOException
/*     */   {
/*  85 */     if (this.pRtLd != null) {
/*  86 */       return;
/*     */     }
/*  88 */     ThreadContext main = new ThreadContext(new ProteusRuntime.DefaultEnvironment(), this.ioSystem, new MainMemory(), new SystemClock(), new ILog.Stream(this.ioSystem.getStdErr()));
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  95 */     HashMap<String, String> env = null;
/*  96 */     RuntimeArgs rtargs = new RuntimeArgs(args, env);
/*  97 */     this.pRtLd = new ProteusRuntime(rtargs, main);
/*     */     
/*  99 */     this.pRtLd.setCopyLibrary(null);
/* 100 */     this.pRtLd.exec();
/*     */   }
/*     */   
/*     */   public void kill()
/*     */   {
/* 105 */     if (this.pRtLd == null) {
/* 106 */       return;
/*     */     }
/*     */     try {
/* 109 */       ProteusRuntime pRtLd1 = this.pRtLd;
/* 110 */       this.pRtLd = null;
/* 111 */       pRtLd1.kill();
/*     */     } finally {
/* 113 */       this.ioSystem.programExit();
/*     */     }
/*     */   }
/*     */   
/*     */   public void dispose()
/*     */   {
/* 119 */     kill();
/*     */   }
/*     */ }


/* Location:              C:\Users\Joey\Downloads\codejar.jar!\com\emt\proteus\runtime32\demo\ProcessImpl.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */