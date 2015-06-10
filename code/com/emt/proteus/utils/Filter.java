/*    */ package com.emt.proteus.utils;
/*    */ 
/*    */ import java.util.HashSet;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ public abstract interface Filter
/*    */ {
/* 37 */   public static final Filter ALL = new Filter() {
/*    */     public boolean accept(Object value) {
/* 39 */       return true;
/*    */     }
/*    */   };
/* 42 */   public static final Filter NONE = new Filter() {
/*    */     public boolean accept(Object value) {
/* 44 */       return false;
/*    */     }
/*    */   };
/*    */   
/*    */   public abstract boolean accept(Object paramObject);
/*    */   
/*    */   public static class Single implements Filter {
/* 51 */     private final HashSet set = new HashSet();
/*    */     
/*    */     public boolean accept(Object value) {
/* 54 */       return this.set.add(value);
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\Joey\Downloads\codejar.jar!\com\emt\proteus\utils\Filter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */