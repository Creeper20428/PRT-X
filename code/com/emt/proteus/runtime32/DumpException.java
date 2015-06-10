/*   */ package com.emt.proteus.runtime32;
/*   */ 
/*   */ import java.io.PrintStream;
/*   */ 
/*   */ public class DumpException extends FlowException
/*   */ {
/*   */   public DumpException(String message) {
/* 8 */     System.err.println(message + "ABORT ");
/* 9 */     System.exit(-1);
/*   */   }
/*   */ }


/* Location:              C:\Users\Joey\Downloads\codejar.jar!\com\emt\proteus\runtime32\DumpException.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */