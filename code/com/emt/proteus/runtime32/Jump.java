/*    */ package com.emt.proteus.runtime32;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class Jump
/*    */ {
/*    */   public static void go_to(int label)
/*    */   {
/* 34 */     throw new IllegalStateException("Unpatched jump to 0x" + Integer.toHexString(label));
/*    */   }
/*    */   
/*    */   public static void label(int label) {}
/*    */ }


/* Location:              C:\Users\Joey\Downloads\codejar.jar!\com\emt\proteus\runtime32\Jump.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */