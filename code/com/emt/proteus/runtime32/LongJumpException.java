/*    */ package com.emt.proteus.runtime32;
/*    */ 
/*    */ public class LongJumpException extends FlowException
/*    */ {
/*    */   private final int status;
/*    */   
/*    */   public LongJumpException(int status) {
/*  8 */     this.status = status;
/*    */   }
/*    */   
/*    */   public int getStatus() {
/* 12 */     return this.status;
/*    */   }
/*    */ }


/* Location:              C:\Users\Joey\Downloads\codejar.jar!\com\emt\proteus\runtime32\LongJumpException.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */