/*    */ package com.emt.proteus.runtime32;
/*    */ 
/*    */ 
/*    */ public class FatalException
/*    */   extends RuntimeException
/*    */ {
/*    */   public FatalException() {}
/*    */   
/*    */   public FatalException(String message)
/*    */   {
/* 11 */     super(message);
/*    */   }
/*    */   
/*    */   public FatalException(String message, Throwable cause) {
/* 15 */     super(message, cause);
/*    */   }
/*    */   
/*    */   public FatalException(Throwable cause) {
/* 19 */     super(cause);
/*    */   }
/*    */ }


/* Location:              C:\Users\Joey\Downloads\codejar.jar!\com\emt\proteus\runtime32\FatalException.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */