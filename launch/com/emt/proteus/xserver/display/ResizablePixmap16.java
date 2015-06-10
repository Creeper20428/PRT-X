/*    */ package com.emt.proteus.xserver.display;
/*    */ 
/*    */ import java.awt.image.MemoryImageSource;
/*    */ 
/*    */ class ResizablePixmap16
/*    */   extends Pixmap16
/*    */   implements Resizable
/*    */ {
/*  9 */   int real_width = 0;
/* 10 */   int real_height = 0;
/*    */   
/*    */   ResizablePixmap16(int id, Drawable d, int width, int height) {
/* 13 */     super(id, d, width, height);
/* 14 */     getData();
/* 15 */     this.real_width = width;
/* 16 */     this.real_height = height;
/*    */   }
/*    */   
/*    */ 
/*    */   public void setColormap(Colormap colormap) {}
/*    */   
/*    */   public void setSize(int w, int h)
/*    */   {
/* 24 */     if ((w <= this.real_width) && (h <= this.real_height)) {
/* 25 */       if ((2 * w < this.real_width) && (2 * h < this.real_height)) {
/* 26 */         this.real_width /= 2;this.real_height /= 2;
/* 27 */         this.data = new byte[this.real_width * this.real_height * 2];
/* 28 */         this.idata = new int[this.real_width * this.real_height];
/* 29 */         this.mis = new MemoryImageSource(this.real_width, this.real_height, this.idata, 0, this.real_width);
/*    */         
/* 31 */         this.mis.setAnimated(true);
/*    */       }
/*    */     }
/*    */     else {
/* 35 */       if (this.real_width < w) this.real_width = w;
/* 36 */       if (this.real_height < h) this.real_height = h;
/* 37 */       this.data = new byte[this.real_width * this.real_height * 2];
/* 38 */       this.idata = new int[this.real_width * this.real_height];
/* 39 */       this.mis = new MemoryImageSource(this.real_width, this.real_height, this.idata, 0, this.real_width);
/*    */       
/* 41 */       this.mis.setAnimated(true);
/*    */     }
/* 43 */     this.width = w;this.height = h;
/*    */   }
/*    */   
/* 46 */   public int getRealWidth() { return this.real_width; }
/*    */   
/*    */   public int getRealHeight() {
/* 49 */     return this.real_height;
/*    */   }
/*    */   
/* 52 */   int getScanWidth() { return this.real_width * 2; }
/*    */ }


/* Location:              C:\Users\Joey\Downloads\launch.jar!\com\emt\proteus\xserver\display\ResizablePixmap16.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */