/*    */ package com.emt.proteus.xserver.display;
/*    */ 
/*    */ import com.emt.proteus.xserver.client.XClient;
/*    */ import java.awt.Color;
/*    */ import java.io.IOException;
/*    */ 
/*    */ 
/*    */ class Colormap16
/*    */   extends Colormap
/*    */ {
/*    */   Colormap16(int id, Screen s, Visual v, int alloc, XClient XClient)
/*    */   {
/* 13 */     super(id, s, v, alloc, XClient);
/*    */   }
/*    */   
/*    */   public int allocColor(XClient XClient, int red, int green, int blue) throws IOException {
/* 17 */     return red / 8 << 11 | green / 4 << 5 | blue / 8;
/*    */   }
/*    */   
/*    */   public Color getColor(int pixel) {
/* 21 */     if (pixel == 1) { return Color.white;
/*    */     }
/* 23 */     int r = (pixel >> 11 & 0x1F) * 8;
/* 24 */     if (r == 248) r = 255;
/* 25 */     int g = (pixel >> 5 & 0x3F) * 4;
/* 26 */     if (g == 252) g = 255;
/* 27 */     int b = (pixel & 0x1F) * 8;
/* 28 */     if (b == 248) { b = 255;
/*    */     }
/* 30 */     return new Color(r << 16 | g << 8 | b);
/*    */   }
/*    */   
/*    */   public void mkIcm() {}
/*    */   
/*    */   public void freePixels(int client) {}
/*    */   
/*    */   public void freeAll() {}
/*    */   
/* 39 */   public int rgb2pixel(int rgb) { return ((rgb & 0xFF0000) >> 16) / 8 << 11 | ((rgb & 0xFF00) >> 8) / 4 << 5 | (rgb & 0xFF) / 8; }
/*    */   
/*    */   public void delete()
/*    */     throws IOException
/*    */   {}
/*    */ }


/* Location:              C:\Users\Joey\Downloads\launch.jar!\com\emt\proteus\xserver\display\Colormap16.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */