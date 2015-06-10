/*    */ package com.emt.proteus.xserver.display;
/*    */ 
/*    */ import java.awt.Color;
/*    */ import java.awt.image.RGBImageFilter;
/*    */ 
/*    */ 
/*    */ class AlphaFilter
/*    */   extends RGBImageFilter
/*    */ {
/* 10 */   int color = -1;
/* 11 */   int alphacolor = this.color & 0xFFFFFF;
/* 12 */   int alpha = 0;
/*    */   
/* 14 */   AlphaFilter(int alpha) { this.canFilterIndexColorModel = true;
/* 15 */     this.alpha = (alpha << 24);
/*    */   }
/*    */   
/* 18 */   void setColor(Color c, int alpha) { this.alpha = (alpha << 24);
/* 19 */     setColor(c);
/*    */   }
/*    */   
/* 22 */   void setColor(Color c) { this.color = (c.getRGB() | 0xFF000000);
/* 23 */     this.alphacolor = (this.color & 0xFFFFFF | this.alpha);
/*    */   }
/*    */   
/*    */   public int filterRGB(int x, int y, int rgb) {
/* 27 */     if (rgb == this.color) return this.alphacolor;
/* 28 */     return rgb;
/*    */   }
/*    */ }


/* Location:              C:\Users\Joey\Downloads\launch.jar!\com\emt\proteus\xserver\display\AlphaFilter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */