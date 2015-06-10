/*    */ package com.emt.proteus.xserver.display;
/*    */ 
/*    */ import java.awt.Color;
/*    */ import java.awt.image.RGBImageFilter;
/*    */ 
/*    */ 
/*    */ class FBFilter
/*    */   extends RGBImageFilter
/*    */ {
/* 10 */   int foreGround = Color.white.getRGB();
/* 11 */   int backGround = Color.black.getRGB();
/* 12 */   boolean through = true;
/*    */   
/*    */   void setFgBg(Color f, Color b) {
/* 15 */     this.foreGround = (f.getRGB() | 0xFF000000);
/* 16 */     this.backGround = (b.getRGB() | 0xFF000000);
/* 17 */     if (((this.foreGround & 0xFFFFFF) != 0) && ((this.backGround & 0xFFFFFF) == 0))
/*    */     {
/* 19 */       this.through = true;
/*    */     }
/*    */     else {
/* 22 */       this.through = false;
/*    */     }
/*    */   }
/*    */   
/*    */   public int filterRGB(int x, int y, int rgb) {
/* 27 */     rgb &= 0xFFFFFF;
/* 28 */     if (rgb == 0) return this.backGround;
/* 29 */     return this.foreGround;
/*    */   }
/*    */ }


/* Location:              C:\Users\Joey\Downloads\launch.jar!\com\emt\proteus\xserver\display\FBFilter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */