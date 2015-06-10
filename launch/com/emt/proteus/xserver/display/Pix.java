/*    */ package com.emt.proteus.xserver.display;
/*    */ 
/*    */ import java.awt.Image;
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
/*    */ final class Pix
/*    */ {
/*    */   int pixel;
/*    */   Pixmap pixmap;
/*    */   Image img;
/*    */   
/*    */   Pix dup()
/*    */   {
/* 30 */     Pix p = new Pix();
/* 31 */     p.pixel = this.pixel;
/* 32 */     p.pixmap = this.pixmap;
/* 33 */     p.img = this.img;
/* 34 */     return p;
/*    */   }
/*    */ }


/* Location:              C:\Users\Joey\Downloads\launch.jar!\com\emt\proteus\xserver\display\Pix.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */