/*    */ package com.emt.proteus.runtime32;
/*    */ 
/*    */ public class Segment {
/*    */   public int base;
/*    */   public int selector;
/*    */   public int limit;
/*    */   public long descriptor;
/*    */   
/*    */   public Segment(int selector, long descriptor) {
/* 10 */     this.selector = selector;
/* 11 */     this.descriptor = descriptor;
/* 12 */     this.base = ((int)(0xFFFFFF & descriptor >> 16 | descriptor >> 32 & 0xFFFFFFFFFF000000));
/*    */   }
/*    */   
/*    */   public Segment(int base, int limit)
/*    */   {
/* 17 */     this.base = base;
/* 18 */     this.limit = limit;
/*    */   }
/*    */   
/*    */   public int getBase()
/*    */   {
/* 23 */     return this.base;
/*    */   }
/*    */   
/*    */   public int getLimit()
/*    */   {
/* 28 */     return this.limit;
/*    */   }
/*    */ }


/* Location:              C:\Users\Joey\Downloads\codejar.jar!\com\emt\proteus\runtime32\Segment.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */