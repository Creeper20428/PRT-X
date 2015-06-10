/*    */ package com.emt.proteus.xserver.display;
/*    */ 
/*    */ import java.awt.image.MemoryImageSource;
/*    */ 
/*    */ class ResizablePixmap1
/*    */   extends Pixmap1
/*    */   implements Resizable
/*    */ {
/*  9 */   int real_width = 0;
/* 10 */   int real_height = 0;
/*    */   
/*    */   ResizablePixmap1(int id, Drawable d, int width, int height) {
/* 13 */     super(id, d, width, height);
/* 14 */     getData();
/* 15 */     this.real_width = width;
/* 16 */     this.real_height = height;
/*    */   }
/*    */   
/*    */   public void setColormap(Colormap colormap) {}
/*    */   
/*    */   public void setSize(int w, int h)
/*    */   {
/* 23 */     if ((w <= this.real_width) && (h <= this.real_height)) {
/* 24 */       if ((2 * w < this.real_width) && (2 * h < this.real_height)) {
/* 25 */         this.real_width /= 2;this.real_height /= 2;
/* 26 */         this.data = new byte[this.real_width * this.real_height];
/* 27 */         this.mis = new MemoryImageSource(this.real_width, this.real_height, Colormap.bwicm, this.data, 0, this.real_width);
/*    */         
/*    */ 
/* 30 */         this.mis.setAnimated(true);
/*    */       }
/*    */     }
/*    */     else {
/* 34 */       if (this.real_width < w) this.real_width = w;
/* 35 */       if (this.real_height < h) this.real_height = h;
/* 36 */       this.data = new byte[this.real_width * this.real_height];
/* 37 */       this.mis = new MemoryImageSource(this.real_width, this.real_height, Colormap.bwicm, this.data, 0, this.real_width);
/*    */       
/*    */ 
/* 40 */       this.mis.setAnimated(true);
/*    */     }
/* 42 */     this.width = w;this.height = h;
/*    */   }
/*    */   
/* 45 */   public int getRealWidth() { return this.real_width; }
/*    */   
/*    */   public int getRealHeight() {
/* 48 */     return this.real_height;
/*    */   }
/*    */ }


/* Location:              C:\Users\Joey\Downloads\launch.jar!\com\emt\proteus\xserver\display\ResizablePixmap1.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */