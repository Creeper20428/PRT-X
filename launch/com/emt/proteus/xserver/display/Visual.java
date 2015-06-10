/*     */ package com.emt.proteus.xserver.display;
/*     */ 
/*     */ import com.emt.proteus.xserver.client.XClient;
/*     */ import com.emt.proteus.xserver.io.ComChannel;
/*     */ import java.io.IOException;
/*     */ import java.util.Vector;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class Visual
/*     */ {
/*     */   public Depth depth;
/*     */   public int id;
/*     */   public int clss;
/*     */   private int bitsPerRGB;
/*     */   public int colormapEntries;
/*     */   public int nplanes;
/*     */   private int redMask;
/*     */   private int greenMask;
/*     */   private int blueMask;
/*     */   
/*  38 */   public Depth getDepth() { return this.depth; }
/*     */   
/*  40 */   public void setDepth(Depth d) { this.depth = d;
/*  41 */     this.nplanes = d.depth;
/*     */   }
/*     */   
/*  44 */   public int getVisualClass() { return this.clss; }
/*  45 */   public void setVisualClass(int v) { this.clss = v; }
/*     */   
/*  47 */   public int getBitsPerRGB() { return this.bitsPerRGB; }
/*  48 */   public void setBitsPerRGB(int b) { this.bitsPerRGB = b; }
/*     */   
/*  50 */   public int getColormapEntries() { return this.colormapEntries; }
/*  51 */   public void setColormapEntries(int c) { this.colormapEntries = c; }
/*     */   
/*  53 */   public int getRedMask() { return this.redMask; }
/*  54 */   public void setRedMask(int r) { this.redMask = r; }
/*     */   
/*  56 */   public int getGreenMask() { return this.greenMask; }
/*  57 */   public void setGreenMask(int g) { this.greenMask = g; }
/*  58 */   public int getBlueMask() { return this.blueMask; }
/*  59 */   public void setBlueMask(int b) { this.blueMask = b; }
/*     */   
/*     */   public Visual(int id, int clss, int bitsPerRGB, int colormapEntries, int redMask, int greenMask, int blueMask)
/*     */   {
/*  63 */     this.id = id;
/*  64 */     this.clss = clss;
/*  65 */     this.bitsPerRGB = bitsPerRGB;
/*  66 */     this.colormapEntries = colormapEntries;
/*  67 */     this.redMask = redMask;
/*  68 */     this.greenMask = greenMask;
/*  69 */     this.blueMask = blueMask;
/*     */   }
/*     */   
/*     */   public void writeByte(ComChannel out) throws IOException {
/*  73 */     out.writeInt(this.id);
/*  74 */     out.writeByte(this.clss);
/*  75 */     out.writeByte(this.bitsPerRGB);
/*  76 */     out.writeShort(this.colormapEntries);
/*  77 */     out.writeInt(this.redMask);
/*  78 */     out.writeInt(this.greenMask);
/*  79 */     out.writeInt(this.blueMask);
/*  80 */     out.writePad(4);
/*     */   }
/*     */   
/*  83 */   public static Visual[] getStaticGray1(XClient c) { Visual[] v = new Visual[1];
/*  84 */     v[0] = new Visual(Resource.fakeClientId(c), 0, 1, 2, 0, 0, 0);
/*  85 */     return v;
/*     */   }
/*     */   
/*     */   public static Visual[] getStaticGray8(XClient c) {
/*  89 */     Visual[] v = new Visual[1];
/*  90 */     v[0] = new Visual(Resource.fakeClientId(c), 0, 8, 256, 0, 0, 0);
/*  91 */     return v;
/*     */   }
/*     */   
/*     */   public static Visual[] getPseudoColor8(XClient c) {
/*  95 */     Vector vec = new Vector();
/*  96 */     vec.addElement(new Visual(Resource.fakeClientId(c), 3, 6, 256, 0, 0, 0));
/*  97 */     Visual[] v = new Visual[vec.size()];
/*  98 */     for (int i = 0; i < vec.size(); i++) {
/*  99 */       v[i] = ((Visual)vec.elementAt(i));
/*     */     }
/* 101 */     vec.removeAllElements();
/* 102 */     return v;
/*     */   }
/*     */   
/* 105 */   public static Visual[] getTrueColor16(XClient c) { Vector vec = new Vector();
/* 106 */     vec.addElement(new Visual(Resource.fakeClientId(c), 4, 6, 64, 63488, 2016, 31));
/*     */     
/*     */ 
/* 109 */     Visual[] v = new Visual[vec.size()];
/* 110 */     for (int i = 0; i < vec.size(); i++) {
/* 111 */       v[i] = ((Visual)vec.elementAt(i));
/*     */     }
/* 113 */     vec.removeAllElements();
/* 114 */     return v;
/*     */   }
/*     */ }


/* Location:              C:\Users\Joey\Downloads\launch.jar!\com\emt\proteus\xserver\display\Visual.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */