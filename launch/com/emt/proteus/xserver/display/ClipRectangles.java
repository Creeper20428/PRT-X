/*    */ package com.emt.proteus.xserver.display;
/*    */ 
/*    */ import java.awt.Rectangle;
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
/*    */ final class ClipRectangles
/*    */   implements Clip
/*    */ {
/*    */   private static final int Unsorted = 0;
/*    */   private static final int YSorted = 1;
/*    */   private static final int YXSorted = 2;
/*    */   private static final int YXBanded = 3;
/*    */   int ordering;
/* 30 */   Rectangle[] masks = null;
/* 31 */   Rectangle mask = null;
/*    */   
/*    */   ClipRectangles(int ordering, int size) {
/* 34 */     this.ordering = ordering;
/* 35 */     if (size > 0) {
/* 36 */       this.masks = new Rectangle[size];
/*    */     }
/*    */   }
/*    */   
/*    */   void add(int x, int y, int w, int h) {
/* 41 */     int i = 0;
/* 42 */     while ((i < this.masks.length) && 
/* 43 */       (this.masks[i] != null)) {
/* 44 */       i++;
/*    */     }
/* 46 */     this.masks[i] = new Rectangle(x, y, w, h);
/* 47 */     if (i == this.masks.length - 1) {
/* 48 */       if (this.masks.length == 1) {
/* 49 */         this.mask = this.masks[0];
/* 50 */         return;
/*    */       }
/* 52 */       this.mask = new Rectangle(this.masks[0]);
/* 53 */       for (int j = 1; j < this.masks.length; j++) {
/* 54 */         this.mask.add(this.masks[j]);
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
/*    */   boolean validate()
/*    */   {
/* 67 */     return true;
/*    */   }
/*    */   
/*    */   public Object getMask() {
/* 71 */     return this.mask;
/*    */   }
/*    */ }


/* Location:              C:\Users\Joey\Downloads\launch.jar!\com\emt\proteus\xserver\display\ClipRectangles.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */