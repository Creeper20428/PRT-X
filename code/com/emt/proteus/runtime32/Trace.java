/*    */ package com.emt.proteus.runtime32;
/*    */ 
/*    */ import java.io.PrintStream;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public abstract class Trace
/*    */ {
/* 11 */   public static final boolean compareToInt = Option.compareIntToCompByBlock.value();
/* 12 */   public static ProcessEmulator interpreted = null;
/* 13 */   public static ProcessEmulator compiled = null;
/*    */   
/*    */ 
/*    */ 
/* 17 */   public static BreakPointException stop = new BreakPointException();
/*    */   
/*    */   public abstract void executeEntry(ThreadContext paramThreadContext, ExecutionEnvironment paramExecutionEnvironment);
/*    */   
/* 21 */   public int[] getEntryPoints() { return new int[0]; }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public static void beginBlock(Processor cpu)
/*    */   {
/* 30 */     if (compareToInt)
/*    */     {
/*    */ 
/* 33 */       if (cpu == compiled.context.cpu) {
/* 34 */         executeBlockAndCompare();
/*    */       }
/*    */     }
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/* 45 */   private static int lastEIP = 0;
/* 46 */   private static int secondLastEIP = 0;
/*    */   
/*    */   public static void endBlock(Processor cpu) {}
/*    */   
/* 50 */   public static void executeBlockAndCompare() { if (interpreted == null) {
/* 51 */       return;
/*    */     }
/*    */     
/*    */ 
/*    */ 
/*    */     try
/*    */     {
/* 58 */       ProcessEmulator.compareState(interpreted, compiled);
/*    */     }
/*    */     catch (RuntimeException e) {
/* 61 */       System.out.println("Previous EIP: 0x" + Integer.toHexString(lastEIP));
/* 62 */       System.out.println("2nd Previous EIP: 0x" + Integer.toHexString(secondLastEIP));
/* 63 */       throw e;
/*    */     }
/*    */     
/* 66 */     interpreted.executeInterpreted(new BasicBlockExecutionEnvironment(true));
/*    */     
/* 68 */     secondLastEIP = lastEIP;
/* 69 */     lastEIP = compiled.context.cpu.eip;
/*    */   }
/*    */ }


/* Location:              C:\Users\Joey\Downloads\codejar.jar!\com\emt\proteus\runtime32\Trace.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */