/*    */ package com.emt.proteus.xserver.display;
/*    */ 
/*    */ import java.awt.image.MemoryImageSource;
/*    */ 
/*    */ class ResizablePixmap
/*    */   extends Pixmap
/*    */   implements Resizable
/*    */ {
/*  9 */   int real_width = 0;
/* 10 */   int real_height = 0;
/*    */   
/*    */   ResizablePixmap(int id, Drawable d, int width, int height, byte depth) {
/* 13 */     super(id, d, width, height, depth);
/* 14 */     getData();
/* 15 */     this.real_width = width;
/* 16 */     this.real_height = height;
/*    */   }
/*    */   
/*    */   public void setColormap(Colormap colormap) {
/* 20 */     this.colormap = colormap;
/*    */   }
/*    */   
/*    */   static Pixmap createPixmap(int id, Drawable d, int width, int height, byte depth)
/*    */   {
/* 25 */     Pixmap p = null;
/* 26 */     if (depth == 1) { p = new ResizablePixmap1(id, d, width, height);
/* 27 */     } else if (depth == 16) p = new ResizablePixmap16(id, d, width, height); else
/* 28 */       p = new ResizablePixmap(id, d, width, height, depth);
/* 29 */     add(p);
/* 30 */     return p;
/*    */   }
/*    */   
/*    */   public void setSize(int w, int h) {
/* 34 */     if ((w <= this.real_width) && (h <= this.real_height)) {
/* 35 */       if ((2 * w < this.real_width) && (2 * h < this.real_height)) {
/* 36 */         this.real_width /= 2;this.real_height /= 2;
/* 37 */         this.data = new byte[this.real_width * this.real_height];
/* 38 */         this.mis = new MemoryImageSource(this.real_width, this.real_height, this.colormap.cm, this.data, 0, this.real_width);
/*    */         
/*    */ 
/* 41 */         this.mis.setAnimated(true);
/*    */       }
/*    */     }
/*    */     else {
/* 45 */       if (this.real_width < w) this.real_width = w;
/* 46 */       if (this.real_height < h) this.real_height = h;
/* 47 */       this.data = new byte[this.real_width * this.real_height];
/* 48 */       this.mis = new MemoryImageSource(this.real_width, this.real_height, this.colormap.cm, this.data, 0, this.real_width);
/*    */       
/*    */ 
/* 51 */       this.mis.setAnimated(true);
/*    */     }
/* 53 */     this.width = w;this.height = h;
/*    */   }
/*    */   
/*    */   public int getRealWidth() {
/* 57 */     return this.real_width;
/*    */   }
/*    */   
/* 60 */   public int getRealHeight() { return this.real_height; }
/*    */ }


/* Location:              C:\Users\Joey\Downloads\launch.jar!\com\emt\proteus\xserver\display\ResizablePixmap.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */