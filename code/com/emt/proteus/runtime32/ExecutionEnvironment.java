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
/*    */ public class ExecutionEnvironment
/*    */ {
/*    */   public static final int CONTINUE = 0;
/*    */   public static final int SUPPLY_CPU_STATE = 1;
/*    */   public static final int HALT_EXECUTION = -1;
/*    */   private int base;
/*    */   protected int depth;
/*    */   
/*    */   public int getStatus(int currentAddress)
/*    */   {
/* 22 */     return 0;
/*    */   }
/*    */   
/*    */ 
/*    */   public void cpuStateUpdate(Processor cpu) {}
/*    */   
/*    */ 
/*    */   public int[] extractUCodes(Processor cpu, MainMemory memory, int address)
/*    */   {
/* 31 */     return new int[] { 1, 1 };
/*    */   }
/*    */   
/*    */   public int getBase() {
/* 35 */     return this.base;
/*    */   }
/*    */   
/*    */   public void setBase(int base) {
/* 39 */     this.base = base;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public void call(Processor cpu, int eip, int esp)
/*    */   {
/* 49 */     this.depth += 1;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public void ret(Processor cpu, int eip, int esp)
/*    */   {
/* 59 */     this.depth -= 1;
/*    */   }
/*    */   
/*    */   public final int getDepth() {
/* 63 */     return this.depth;
/*    */   }
/*    */   
/* 66 */   public final void setDepth(int d) { this.depth = d; }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public ExecutionEnvironment newThread()
/*    */   {
/*    */     try
/*    */     {
/* 76 */       return (ExecutionEnvironment)getClass().newInstance();
/*    */     } catch (InstantiationException e) {
/* 78 */       e.printStackTrace();
/*    */     } catch (IllegalAccessException e) {
/* 80 */       e.printStackTrace();
/*    */     }
/* 82 */     return null;
/*    */   }
/*    */ }


/* Location:              C:\Users\Joey\Downloads\codejar.jar!\com\emt\proteus\runtime32\ExecutionEnvironment.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */