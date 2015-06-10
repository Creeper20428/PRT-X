/*    */ package com.emt.proteus.runtime32;
/*    */ 
/*    */ import com.emt.proteus.runtime32.memory.MainMemory;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public final class CallArgs
/*    */ {
/*    */   public static int internal(ThreadContext ctx, int arg)
/*    */   {
/* 17 */     return internal(ctx.getCpu(), ctx.getMemory(), arg);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public static int internal(Processor cpu, MainMemory memory, int arg)
/*    */   {
/* 28 */     switch (arg) {
/*    */     case 0: 
/* 30 */       return cpu.eax;
/*    */     case 1: 
/* 32 */       return cpu.edx;
/*    */     case 2: 
/* 34 */       return cpu.ecx;
/*    */     }
/* 36 */     return memory.getDoubleWord(cpu.esp + (arg - 2 << 2));
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public static int cdecl(ThreadContext ctx, int arg)
/*    */   {
/* 49 */     return cdecl(ctx.getCpu(), ctx.getMemory(), arg);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public static int cdecl(Processor cpu, MainMemory memory, int arg)
/*    */   {
/* 61 */     return memory.getDoubleWord(cpu.esp + (arg + 1 << 2));
/*    */   }
/*    */ }


/* Location:              C:\Users\Joey\Downloads\codejar.jar!\com\emt\proteus\runtime32\CallArgs.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */