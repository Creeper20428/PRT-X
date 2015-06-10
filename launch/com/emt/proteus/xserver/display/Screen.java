/*     */ package com.emt.proteus.xserver.display;
/*     */ 
/*     */ import com.emt.proteus.xserver.client.XClient;
/*     */ import com.emt.proteus.xserver.io.ComChannel;
/*     */ import java.io.IOException;
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
/*     */ public final class Screen
/*     */ {
/*     */   public static Screen[] screen;
/*     */   public XWindow root;
/*     */   public int rootId;
/*     */   public Colormap defaultColormap;
/*     */   public int defaultColormapId;
/*     */   public int white;
/*     */   public int black;
/*     */   public int currentInputMasks;
/*     */   public short width;
/*     */   public short height;
/*     */   public short width_mm;
/*     */   public short height_mm;
/*     */   public int minInstalledMaps;
/*     */   public int maxInstalledMaps;
/*     */   public int rootVisual;
/*     */   public int backingStores;
/*     */   public int saveUnders;
/*     */   public byte rootDepth;
/*  45 */   public int windowmode = 0;
/*     */   
/*     */   public Depth[] depth;
/*     */   
/*     */   public Visual[] visual;
/*     */   public Pixmap[] pixmaps;
/*     */   private static final int visibilityOffset = 17;
/*     */   private static final int visibility = 393216;
/*     */   private static final int VisibilityUnobscured = 0;
/*     */   private static final int VisibilityPartiallyObscured = 1;
/*     */   private static final int VisibilityFullyObscured = 2;
/*     */   private static final int VisibilityNotViewable = 3;
/*     */   
/*     */   public static Screen[] init()
/*     */   {
/*  60 */     screen = new Screen[1];
/*  61 */     return screen;
/*     */   }
/*     */   
/*  64 */   public int getRootId() { return this.rootId; }
/*  65 */   public void setRootId(int r) { this.rootId = r; }
/*  66 */   public byte getRootDepth() { return this.rootDepth; }
/*  67 */   public void setRootDepth(byte r) { this.rootDepth = r; }
/*     */   
/*  69 */   public int getRootVisual() { return this.rootVisual; }
/*  70 */   public void setRootVisual(int r) { this.rootVisual = r; }
/*     */   
/*  72 */   public XWindow getRoot() { return this.root; }
/*  73 */   public void setRoot(XWindow root) { this.root = root; }
/*     */   
/*  75 */   public Depth[] getDepth() { return this.depth; }
/*  76 */   public void setDepth(Depth[] d) { this.depth = d; }
/*     */   
/*  78 */   public int depths() { return this.depth.length; }
/*     */   
/*  80 */   public Colormap defaultColormap() { return this.defaultColormap; }
/*  81 */   public int defaultColormapId() { return this.defaultColormapId; }
/*     */   
/*  83 */   public int getWhite() { return this.white; }
/*  84 */   public void setWhite(int w) { this.white = w; }
/*     */   
/*  86 */   public int getBlack() { return this.black; }
/*  87 */   public void setBlack(int b) { this.black = b; }
/*     */   
/*  89 */   public int getCurrentInputMasks() { return this.currentInputMasks; }
/*  90 */   public void setCurrentInputMasks(int c) { this.currentInputMasks = c; }
/*     */   
/*  92 */   public int getMinInstalledMaps() { return this.minInstalledMaps; }
/*  93 */   public void setMinInstalledMaps(int m) { this.minInstalledMaps = m; }
/*     */   
/*  95 */   public int getMaxInstalledMaps() { return this.maxInstalledMaps; }
/*  96 */   public void setMaxInstalledMaps(int m) { this.maxInstalledMaps = m; }
/*     */   
/*  98 */   public int getBackingStores() { return this.backingStores; }
/*  99 */   public void setBackingStores(int b) { this.backingStores = b; }
/*     */   
/* 101 */   public int getSaveUnders() { return this.saveUnders; }
/* 102 */   public void setSaveUnders(int s) { this.saveUnders = s; }
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
/*     */   public Screen(int rootId, int dColormap, int white, int black, int cInputMasks, int width, int height, int width_mm, int height_mm, int minInstalledMaps, int maxInstalledMaps, int rootVisual, int bStores, int sUnders, int rootDepth, Depth[] depth)
/*     */   {
/* 120 */     this.rootId = rootId;
/* 121 */     this.defaultColormapId = dColormap;
/* 122 */     this.white = white;
/* 123 */     this.black = black;
/* 124 */     this.currentInputMasks = cInputMasks;
/* 125 */     this.width = ((short)width);
/* 126 */     this.height = ((short)height);
/* 127 */     this.width_mm = ((short)width_mm);
/* 128 */     this.height_mm = ((short)height_mm);
/* 129 */     this.minInstalledMaps = minInstalledMaps;
/* 130 */     this.maxInstalledMaps = maxInstalledMaps;
/* 131 */     this.rootVisual = rootVisual;
/* 132 */     this.backingStores = bStores;
/* 133 */     this.saveUnders = sUnders;
/* 134 */     this.rootDepth = ((byte)rootDepth);
/* 135 */     this.depth = depth;
/*     */   }
/*     */   
/*     */   public void writeByte(ComChannel out) throws IOException {
/* 139 */     out.writeInt(this.rootId);
/* 140 */     out.writeInt(this.defaultColormapId);
/* 141 */     out.writeInt(this.white);
/* 142 */     out.writeInt(this.black);
/* 143 */     this.currentInputMasks = (this.root.eventMask | this.root.getOtherEventMask());
/* 144 */     out.writeInt(this.currentInputMasks);
/* 145 */     out.writeShort(this.width);
/* 146 */     out.writeShort(this.height);
/* 147 */     out.writeShort(this.width_mm);
/* 148 */     out.writeShort(this.height_mm);
/* 149 */     out.writeShort(this.minInstalledMaps);
/* 150 */     out.writeShort(this.maxInstalledMaps);
/* 151 */     out.writeInt(this.rootVisual);
/* 152 */     out.writeByte(this.backingStores);
/* 153 */     out.writeByte(this.saveUnders);
/* 154 */     out.writeByte(this.rootDepth);
/* 155 */     out.writeByte(this.depth.length);
/* 156 */     for (int i = 0; i < this.depth.length; i++) {
/* 157 */       this.depth[i].writeByte(out);
/*     */     }
/*     */   }
/*     */   
/*     */   public int getLength() {
/* 162 */     int i = 10;
/* 163 */     if (this.depth != null) {
/* 164 */       for (int j = 0; j < this.depth.length; j++) {
/* 165 */         i += this.depth[j].getLength();
/*     */       }
/*     */     }
/* 168 */     return i;
/*     */   }
/*     */   
/*     */   public static void resetScreen(int scrn) {
/* 172 */     XClient.closeDownAll();
/* 173 */     Colormap cmap = screen[0].defaultColormap;
/* 174 */     if ((cmap.visual.clss & 0x1) != 0) {
/* 175 */       cmap.flags |= 0x4;
/* 176 */       cmap.freeAll();
/*     */       try {
/* 178 */         cmap.allocColor(XClient.X_CLIENTs[0], 0, 0, 0);
/* 179 */         cmap.allocColor(XClient.X_CLIENTs[0], 255, 255, 255);
/*     */       }
/*     */       catch (Exception e) {}
/* 182 */       cmap.mkIcm();
/* 183 */       cmap.flags &= 0xFFFFFFFB;
/*     */     }
/* 185 */     screen[0].root.initAttr();
/*     */   }
/*     */ }


/* Location:              C:\Users\Joey\Downloads\launch.jar!\com\emt\proteus\xserver\display\Screen.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */