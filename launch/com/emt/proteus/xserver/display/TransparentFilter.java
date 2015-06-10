/*    */ package com.emt.proteus.xserver.display;
/*    */ 
/*    */ import java.awt.Image;
/*    */ import java.awt.image.PixelGrabber;
/*    */ import java.awt.image.RGBImageFilter;
/*    */ import java.io.PrintStream;
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
/*    */ final class TransparentFilter
/*    */   extends RGBImageFilter
/*    */ {
/*    */   int width;
/*    */   int height;
/*    */   byte[] pixels;
/*    */   int cx;
/*    */   int cy;
/*    */   
/*    */   TransparentFilter(int cx, int cy, Pixmap pixmap)
/*    */   {
/* 33 */     this.cx = cx;
/* 34 */     this.cy = cy;
/*    */     
/* 36 */     this.width = pixmap.width;
/* 37 */     this.height = pixmap.height;
/*    */     
/* 39 */     this.pixels = new byte[pixmap.width * pixmap.height];
/* 40 */     if (pixmap.data != null) {
/* 41 */       for (int i = 0; i < pixmap.height; i++) {
/* 42 */         for (int j = 0; j < pixmap.width; j++) {
/* 43 */           if (pixmap.data[(i * pixmap.width + j)] == 0) {
/* 44 */             this.pixels[(i * pixmap.width + j)] = 0;
/*    */           } else {
/* 46 */             this.pixels[(i * pixmap.width + j)] = 1;
/*    */           }
/*    */         }
/*    */       }
/*    */     } else {
/* 51 */       int[] ipixels = new int[pixmap.width * pixmap.height];
/* 52 */       Image img = pixmap.img;
/* 53 */       PixelGrabber pg = null;
/* 54 */       pg = new PixelGrabber(img, 0, 0, pixmap.width, pixmap.height, ipixels, 0, pixmap.width);
/*    */       
/*    */       try
/*    */       {
/* 58 */         pg.grabPixels();
/*    */       }
/*    */       catch (InterruptedException e)
/*    */       {
/* 62 */         return;
/*    */       }
/*    */       
/* 65 */       if ((pg.getStatus() & 0x80) != 0) {
/* 66 */         System.err.println("image fetch aborted or errored");
/* 67 */         return;
/*    */       }
/*    */       
/* 70 */       for (int i = 0; i < pixmap.height; i++) {
/* 71 */         for (int j = 0; j < pixmap.width; j++) {
/* 72 */           if (ipixels[(i * pixmap.width + j)] == -16777216) {
/* 73 */             this.pixels[(i * pixmap.width + j)] = 0;
/*    */           } else
/* 75 */             this.pixels[(i * pixmap.width + j)] = 1;
/*    */         }
/*    */       }
/* 78 */       ipixels = null;
/*    */     }
/*    */   }
/*    */   
/*    */   public int filterRGB(int x, int y, int rgb) {
/* 83 */     if ((this.cx <= x) && (x < this.cx + this.width) && (this.cy <= y) && (y < this.cy + this.height) && 
/* 84 */       (this.pixels[((y - this.cy) * this.width + (x - this.cx))] == 0)) { rgb = 16777215;
/*    */     }
/* 86 */     return rgb;
/*    */   }
/*    */ }


/* Location:              C:\Users\Joey\Downloads\launch.jar!\com\emt\proteus\xserver\display\TransparentFilter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */