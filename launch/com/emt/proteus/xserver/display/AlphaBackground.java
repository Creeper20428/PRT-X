/*    */ package com.emt.proteus.xserver.display;
/*    */ 
/*    */ import java.awt.Color;
/*    */ import java.awt.Graphics;
/*    */ import java.awt.Image;
/*    */ import java.awt.Toolkit;
/*    */ import java.awt.image.ImageObserver;
/*    */ 
/*    */ class AlphaBackground
/*    */ {
/*    */   Image offi;
/* 12 */   Image filteredi = null;
/* 13 */   AlphaFilter filter = null;
/* 14 */   Color background = null;
/*    */   int x;
/*    */   int y;
/*    */   
/* 18 */   AlphaBackground(Image offi, int alpha) { this.offi = offi;
/* 19 */     this.alpha = alpha;
/* 20 */     this.filter = new AlphaFilter(alpha);
/*    */   }
/*    */   
/* 23 */   void setImage(Image img) { this.offi = img;
/* 24 */     freeImage();
/* 25 */     this.filteredi = null; }
/*    */   
/*    */   int width;
/* 28 */   void setCrop(int x, int y, int width, int height) { if ((x == this.x) && (y == this.y) && (width == this.width) && (height == this.height))
/* 29 */       return;
/* 30 */     this.x = x;this.y = y;this.width = width;this.height = height;
/* 31 */     freeImage();
/* 32 */     this.filteredi = null;
/*    */   }
/*    */   
/* 35 */   void setColor(Color c, int alpha) { if ((this.background != null) && (this.background.equals(c))) return;
/* 36 */     this.background = c;
/* 37 */     this.alpha = alpha;
/* 38 */     this.filter.setColor(c, alpha);
/* 39 */     freeImage();
/* 40 */     this.filteredi = null;
/*    */   }
/*    */   
/* 43 */   synchronized void freeImage() { if (this.filteredi != null) this.filteredi.flush();
/*    */   }
/*    */   
/* 46 */   Image getImage() { if (this.filteredi == null) {
/* 47 */       if (this.filter == null) this.filter = new AlphaFilter(this.alpha);
/* 48 */       this.filteredi = Toolkit.getDefaultToolkit().createImage(new java.awt.image.FilteredImageSource(this.offi.getSource(), this.filter));
/*    */     }
/*    */     
/*    */ 
/* 52 */     return this.filteredi;
/*    */   }
/*    */   
/* 55 */   void dispose() { finalize(); }
/*    */   
/*    */   synchronized void drawImage(Graphics g, int xx, int yy, ImageObserver io) {
/* 58 */     java.awt.Rectangle r = g.getClipBounds();
/* 59 */     Image i = getImage();
/* 60 */     g.drawImage(i, xx, yy, io);
/*    */   }
/*    */   
/* 63 */   public void finalize() { this.offi = null;
/* 64 */     freeImage();
/* 65 */     this.filteredi = null;
/*    */   }
/*    */   
/*    */   int height;
/*    */   int alpha;
/*    */ }


/* Location:              C:\Users\Joey\Downloads\launch.jar!\com\emt\proteus\xserver\display\AlphaBackground.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */