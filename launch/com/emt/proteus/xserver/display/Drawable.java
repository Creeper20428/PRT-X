/*    */ package com.emt.proteus.xserver.display;
/*    */ 
/*    */ import java.awt.Graphics;
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
/*    */ public abstract class Drawable
/*    */   extends Resource
/*    */ {
/*    */   public static final byte DRAWABLE_WINDOW = 0;
/*    */   public static final byte DRAWABLE_PIXMAP = 1;
/*    */   public static final byte UNDRAWABLE_WINDOW = 2;
/*    */   public static final byte DRAWABLE_BUFFER = 3;
/*    */   public byte type;
/*    */   public int clss;
/*    */   public byte depth;
/*    */   public byte bitsPerPixel;
/*    */   public int x;
/*    */   public int y;
/*    */   public int width;
/*    */   public int height;
/*    */   public Screen screen;
/*    */   public long serialNumber;
/* 38 */   private static int serial = 0;
/*    */   
/*    */   private static final int max = 268435456;
/* 41 */   int gctime = 0;
/* 42 */   int gmask = 0;
/* 43 */   GC currentGC = null;
/*    */   
/*    */   Drawable(int id, int type) {
/* 46 */     super(id, type);
/* 47 */     if (++serial > 268435456) serial = 1;
/* 48 */     this.serialNumber = serial;
/*    */   }
/*    */   
/*    */   abstract Graphics getGraphics();
/*    */   
/*    */   abstract void restoreClip();
/*    */   
/*    */   abstract Graphics getGraphics(GC paramGC, int paramInt);
/*    */   
/*    */   abstract Colormap getColormap();
/*    */   
/*    */   abstract Image getImage(GC paramGC, int paramInt1, int paramInt2, int paramInt3, int paramInt4);
/*    */ }


/* Location:              C:\Users\Joey\Downloads\launch.jar!\com\emt\proteus\xserver\display\Drawable.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */