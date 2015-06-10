/*      */ package com.emt.proteus.xserver.display;
/*      */ 
/*      */ import com.emt.proteus.xserver.client.XClient;
/*      */ import com.emt.proteus.xserver.io.ComChannel;
/*      */ import java.awt.Color;
/*      */ import java.awt.image.ColorModel;
/*      */ import java.awt.image.IndexColorModel;
/*      */ import java.io.IOException;
/*      */ import java.io.PrintStream;
/*      */ import java.util.Hashtable;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public class Colormap
/*      */   extends Resource
/*      */ {
/*      */   private static final int REDMAP = 0;
/*      */   private static final int GREENMAP = 1;
/*      */   private static final int BLUEMAP = 2;
/*      */   private static final int PSEUDOMAP = 3;
/*      */   private static final int AllocPrivate = -1;
/*      */   private static final int AllocTemporary = -2;
/*      */   private static final int DynamicClass = 1;
/*      */   private static final int AllocNone = 0;
/*      */   private static final int AllocAll = 1;
/*      */   private static final int IsDefault = 1;
/*      */   private static final int AllAllocated = 2;
/*      */   private static final int BeingCreated = 4;
/*      */   private static final int StaticGray = 0;
/*      */   private static final int GrayScale = 1;
/*      */   private static final int StaticColor = 2;
/*      */   private static final int PseudoColor = 3;
/*      */   private static final int TrueColor = 4;
/*      */   private static final int DirectColor = 5;
/*      */   private static final int DoRed = 1;
/*      */   private static final int DoGreen = 2;
/*      */   private static final int DoBlue = 4;
/*   59 */   public static IndexColorModel bwicm = null;
/*      */   
/*   61 */   static { byte[] foo = { 0, -1 };
/*   62 */     bwicm = new IndexColorModel(1, 2, foo, foo, foo);
/*      */   }
/*      */   
/*      */ 
/*      */   Color[] colors;
/*      */   
/*      */   byte[] r;
/*      */   byte[] g;
/*      */   byte[] b;
/*      */   ColorModel cm;
/*      */   long icmtime;
/*      */   Visual visual;
/*      */   Screen screen;
/*      */   int[][] pixels;
/*      */   int freeRed;
/*      */   Entry[] entries;
/*      */   public int flags;
/*   79 */   public static Colormap[] installed = null;
/*   80 */   public static Colormap defaultColormap = null;
/*      */   
/*      */   Colormap(int id, Screen s, Visual v, int alloc, XClient XClient) {
/*   83 */     super(id, 6);
/*      */     
/*   85 */     add(this);
/*      */     
/*   87 */     this.screen = s;
/*   88 */     this.visual = v;
/*      */     
/*   90 */     if (v.depth.depth == 16) { return;
/*      */     }
/*   92 */     int n = v.getColormapEntries();
/*      */     
/*   94 */     this.r = new byte[n];
/*   95 */     this.g = new byte[n];
/*   96 */     this.b = new byte[n];
/*      */     
/*   98 */     for (int i = 0; i < n; i++) { this.r[i] = -1;this.g[i] = -1;this.b[i] = -1;
/*      */     }
/*  100 */     this.colors = new Color[n];
/*      */     
/*  102 */     this.pixels = new int[''][];
/*  103 */     this.entries = new Entry[n];
/*  104 */     for (int i = 0; i < this.entries.length; i++) {
/*  105 */       this.entries[i] = new LocalEntry();
/*      */     }
/*  107 */     this.freeRed = n;
/*  108 */     this.flags = 0;
/*      */     
/*  110 */     if (id == this.screen.defaultColormapId) {
/*  111 */       this.flags |= 0x1;
/*      */     }
/*      */     
/*  114 */     if (alloc == 1) {
/*  115 */       if ((this.visual.clss & 0x1) != 0) {
/*  116 */         this.flags |= 0x2;
/*      */       }
/*  118 */       for (int i = 0; i < this.entries.length; i++) {
/*  119 */         this.entries[i].refcnt = -1;
/*      */       }
/*  121 */       this.freeRed = 0;
/*  122 */       this.pixels[XClient.index] = new int[this.entries.length];
/*  123 */       for (int i = 0; i < this.pixels[XClient.index].length; i++) {
/*  124 */         this.pixels[XClient.index][i] = i;
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */   public static Colormap getColormap(int id, Screen s, Visual v, int alloc, XClient XClient) {
/*  130 */     if (v.depth.depth == 16) {
/*  131 */       return new Colormap16(id, s, v, alloc, XClient);
/*      */     }
/*  133 */     return new Colormap(id, s, v, alloc, XClient);
/*      */   }
/*      */   
/*      */   private int alloc() {
/*  137 */     for (int i = 0; 
/*  138 */         i < this.colors.length; i++) {
/*  139 */       if (this.colors[i] == null) {
/*  140 */         this.colors[i] = Color.black;
/*  141 */         break;
/*      */       }
/*      */     }
/*  144 */     if (i == this.colors.length) i = 2;
/*  145 */     return i;
/*      */   }
/*      */   
/*      */   private void alloc(String s) {
/*  149 */     for (int i = 0; 
/*  150 */         i < this.colors.length; i++)
/*  151 */       if (this.colors[i] == null)
/*      */         break;
/*  153 */     if (i == this.colors.length) return;
/*  154 */     alloc(i, (Color)rgbTable.get(s));
/*  155 */     mkIcm();
/*      */   }
/*      */   
/*      */   private void alloc(int i, Color color) {
/*  159 */     this.colors[i] = color;
/*  160 */     this.r[i] = ((byte)color.getRed());
/*  161 */     this.g[i] = ((byte)color.getGreen());
/*  162 */     this.b[i] = ((byte)color.getBlue());
/*      */   }
/*      */   
/*      */   private void free(int i) {
/*  166 */     if (i < this.colors.length) {
/*  167 */       this.colors[i] = null;
/*  168 */       this.r[i] = -1;
/*  169 */       this.g[i] = -1;
/*  170 */       this.b[i] = -1;
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   void freeAll()
/*      */   {
/*  178 */     for (int i = 0; i < this.entries.length; i++) {
/*  179 */       this.entries[i].refcnt = 0;
/*      */     }
/*  181 */     for (int i = 0; i < this.colors.length; i++) {
/*  182 */       free(i);
/*      */     }
/*      */   }
/*      */   
/*      */   int rgb2pixel(int rgb) {
/*  187 */     byte red = (byte)(rgb >> 16 & 0xFF);
/*  188 */     byte green = (byte)(rgb >> 8 & 0xFF);
/*  189 */     byte blue = (byte)(rgb & 0xFF);
/*  190 */     for (int i = 0; i < this.r.length; i++) {
/*  191 */       if ((this.r[i] == red) && (this.g[i] == green) && (this.b[i] == blue)) {
/*  192 */         return i;
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*  197 */     int distance = 1000;
/*  198 */     int j = 0;int k = 0;int l = 0;
/*  199 */     for (int i = 0; i < this.r.length; i++) {
/*  200 */       k = 0;
/*  201 */       l = this.r[i] - red; if (l < 0) l *= -1; k += l;
/*  202 */       l = this.g[i] - green; if (l < 0) l *= -1; k += l;
/*  203 */       l = this.b[i] - blue; if (l < 0) l *= -1; k += l;
/*  204 */       if (k < distance) { j = i;distance = k;
/*      */       } }
/*  206 */     return j;
/*      */   }
/*      */   
/*      */   public static void reqCreateColormap(XClient c) throws IOException
/*      */   {
/*  211 */     ComChannel comChannel = c.channel;
/*  212 */     int alloc = c.data;
/*  213 */     if ((alloc != 0) && (alloc != 1)) {
/*  214 */       c.errorValue = alloc;
/*  215 */       c.errorReason = 2;
/*      */     }
/*  217 */     int mid = comChannel.readInt();
/*  218 */     int foo = comChannel.readInt();
/*  219 */     XWindow w = c.lookupWindow(foo);
/*  220 */     if ((c.errorReason == 0) && (w == null)) {
/*  221 */       c.errorValue = foo;
/*  222 */       c.errorReason = 3;
/*      */     }
/*  224 */     foo = comChannel.readInt();
/*  225 */     c.length -= 4;
/*  226 */     if (c.errorReason != 0) {
/*  227 */       return;
/*      */     }
/*      */     
/*  230 */     Visual v = null;
/*  231 */     Visual[] vv = w.screen.visual;
/*  232 */     for (int i = 0; i < vv.length; i++) {
/*  233 */       if (vv[i].id == foo) {
/*  234 */         v = vv[i];
/*  235 */         break;
/*      */       }
/*      */     }
/*  238 */     if (v == null) {
/*  239 */       c.errorValue = foo;
/*  240 */       c.errorReason = 2;
/*  241 */       return;
/*      */     }
/*      */     
/*  244 */     if (((v.clss & 0x1) == 0) && (alloc != 0) && (c != XClient.X_CLIENTs[0]))
/*      */     {
/*      */ 
/*  247 */       c.errorReason = 9;
/*  248 */       return;
/*      */     }
/*  250 */     getColormap(mid, w.screen, v, alloc, c);
/*      */   }
/*      */   
/*      */   public static void reqStoreColors(XClient c) throws IOException
/*      */   {
/*  255 */     ComChannel comChannel = c.channel;
/*  256 */     int n = c.length;
/*  257 */     int foo = comChannel.readInt();
/*  258 */     Colormap cmap = (Colormap)lookupIDByType(foo, 6);
/*  259 */     c.length -= 2;
/*  260 */     if (cmap == null) {
/*  261 */       c.errorValue = foo;
/*  262 */       c.errorReason = 12;
/*  263 */       return;
/*      */     }
/*  265 */     if ((cmap.visual.clss & 0x1) == 0) {
/*  266 */       c.errorReason = 11;
/*  267 */       return;
/*      */     }
/*  269 */     n -= 2;
/*  270 */     cmap.storeColors(c, n);
/*  271 */     if (c.errorReason != 0) {
/*  272 */       return;
/*      */     }
/*  274 */     cmap.mkIcm();
/*      */   }
/*      */   
/*      */   public static void reqUninstallColormap(XClient c) throws IOException
/*      */   {
/*  279 */     ComChannel comChannel = c.channel;
/*  280 */     int foo = comChannel.readInt();
/*  281 */     c.length -= 2;
/*  282 */     Colormap cmap = (Colormap)lookupIDByType(foo, 6);
/*  283 */     if (cmap == null) {
/*  284 */       c.errorValue = foo;
/*  285 */       c.errorReason = 12;
/*  286 */       return;
/*      */     }
/*  288 */     if (installed.length == 1) return;
/*  289 */     boolean notfound = true;
/*  290 */     for (int i = 0; i < installed.length; i++)
/*  291 */       if (installed[i] == cmap) { notfound = false; break;
/*      */       }
/*  293 */     if (notfound) return;
/*  294 */     synchronized (installed) {
/*  295 */       Colormap[] tmp = new Colormap[installed.length - 1];
/*  296 */       tmp[0] = installed[0];
/*  297 */       for (int i = 1; i < installed.length; i++) {
/*  298 */         if (installed[i] == cmap) {
/*  299 */           for (int j = i + 1; j < installed.length; j++) {
/*  300 */             tmp[(j - 1)] = installed[j];
/*      */           }
/*  302 */           break;
/*      */         }
/*  304 */         tmp[i] = installed[i];
/*      */       }
/*  306 */       installed = tmp;
/*      */     }
/*      */   }
/*      */   
/*      */   public static void reqInstallColormap(XClient c) throws IOException {
/*  311 */     ComChannel comChannel = c.channel;
/*  312 */     int foo = comChannel.readInt();
/*  313 */     c.length -= 2;
/*  314 */     Colormap cmap = (Colormap)lookupIDByType(foo, 6);
/*  315 */     if (cmap == null) {
/*  316 */       c.errorValue = foo;
/*  317 */       c.errorReason = 12;
/*  318 */       return;
/*      */     }
/*      */     
/*  321 */     synchronized (installed) {
/*  322 */       for (int i = 0; i < installed.length; i++) {
/*  323 */         if (installed[i] == cmap) return;
/*      */       }
/*  325 */       Colormap[] tmp = new Colormap[installed.length + 1];
/*  326 */       System.arraycopy(installed, 0, tmp, 0, installed.length);
/*  327 */       tmp[installed.length] = cmap;
/*  328 */       installed = tmp;
/*      */     }
/*      */   }
/*      */   
/*      */   public static void reqListInstalledColormaps(XClient c) throws IOException
/*      */   {
/*  334 */     ComChannel comChannel = c.channel;
/*  335 */     int foo = comChannel.readInt();
/*      */     
/*  337 */     synchronized (comChannel) {
/*  338 */       comChannel.writeByte(1);
/*  339 */       comChannel.writePad(1);
/*  340 */       comChannel.writeShort(c.getSequence());
/*  341 */       comChannel.writeInt(1);
/*  342 */       comChannel.writeShort(1);
/*  343 */       comChannel.writePad(22);
/*  344 */       int n = 1;
/*  345 */       while (n != 0) {
/*  346 */         comChannel.writeInt(Screen.screen[0].defaultColormapId);
/*  347 */         n--;
/*      */       }
/*  349 */       comChannel.flush();
/*      */     }
/*      */   }
/*      */   
/*      */   public static void reqFreeColors(XClient c) throws IOException
/*      */   {
/*  355 */     ComChannel comChannel = c.channel;
/*  356 */     int n = c.length;
/*  357 */     int foo = comChannel.readInt();
/*  358 */     Colormap cmap = (Colormap)lookupIDByType(foo, 6);
/*  359 */     c.length -= 2;
/*  360 */     if (cmap == null) {
/*  361 */       c.errorValue = foo;
/*  362 */       c.errorReason = 12;
/*  363 */       return;
/*      */     }
/*  365 */     foo = comChannel.readInt();
/*  366 */     c.length -= 1;
/*  367 */     n -= 3;
/*  368 */     cmap.freeColors(c, n, foo);
/*      */   }
/*      */   
/*      */   public static void reqAllocColorPlanes(XClient c) throws IOException {
/*  372 */     ComChannel comChannel = c.channel;
/*  373 */     int cont = c.data;
/*  374 */     int foo = comChannel.readInt();
/*  375 */     Colormap cmap = (Colormap)lookupIDByType(foo, 6);
/*  376 */     c.length -= 2;
/*  377 */     if (cmap == null) {
/*  378 */       c.errorValue = foo;
/*  379 */       c.errorReason = 12;
/*  380 */       return;
/*      */     }
/*      */     
/*  383 */     int n = comChannel.readShort();
/*      */     
/*  385 */     int reds = comChannel.readShort();
/*  386 */     int greens = comChannel.readShort();
/*  387 */     int blues = comChannel.readShort();
/*  388 */     c.length = 0;
/*      */     
/*  390 */     if (n == 0) {
/*  391 */       c.errorValue = 0;
/*  392 */       c.errorReason = 2;
/*  393 */       return;
/*      */     }
/*  395 */     cmap.allocColorPlanes(c, n, cont == 1, reds, greens, blues);
/*  396 */     synchronized (comChannel) {
/*  397 */       comChannel.writeByte(1);
/*  398 */       comChannel.writePad(1);
/*  399 */       comChannel.writeShort(c.getSequence());
/*  400 */       comChannel.writeInt(n);
/*  401 */       comChannel.writeShort(n);
/*  402 */       comChannel.writePad(2);
/*  403 */       comChannel.writeInt(16711680);
/*  404 */       comChannel.writeInt(65280);
/*  405 */       comChannel.writeInt(255);
/*  406 */       comChannel.writePad(8);
/*  407 */       while (n != 0) {
/*  408 */         comChannel.writeInt(cmap.alloc());
/*  409 */         n--;
/*      */       }
/*  411 */       comChannel.flush();
/*      */     }
/*      */   }
/*      */   
/*      */   public static void reqAllocColorCells(XClient c) throws IOException {
/*  416 */     ComChannel comChannel = c.channel;
/*  417 */     int cont = c.data;
/*  418 */     int foo = comChannel.readInt();
/*  419 */     Colormap cmap = (Colormap)lookupIDByType(foo, 6);
/*  420 */     c.length -= 2;
/*  421 */     if (cmap == null) {
/*  422 */       c.errorValue = foo;
/*  423 */       c.errorReason = 12;
/*  424 */       return;
/*      */     }
/*  426 */     int n = (short)comChannel.readShort();
/*  427 */     int m = (short)comChannel.readShort();
/*  428 */     c.length -= 1;
/*  429 */     if (n == 0) {
/*  430 */       c.errorValue = 0;
/*  431 */       c.errorReason = 2;
/*  432 */       return;
/*      */     }
/*  434 */     if ((cont != 0) && (cont != 1)) {
/*  435 */       c.errorValue = cont;
/*  436 */       c.errorReason = 2;
/*  437 */       return;
/*      */     }
/*      */     
/*  440 */     int[] pix = null;int[] msk = null;
/*  441 */     pix = new int[n];
/*  442 */     if (m != 0) {
/*  443 */       msk = new int[m];
/*      */     }
/*  445 */     cmap.allocColorCells(c, n, m, cont == 1, pix, msk);
/*  446 */     if (c.errorReason != 0) {
/*  447 */       return;
/*      */     }
/*  449 */     synchronized (comChannel) {
/*  450 */       comChannel.writeByte(1);
/*  451 */       comChannel.writePad(1);
/*  452 */       comChannel.writeShort(c.getSequence());
/*  453 */       comChannel.writeInt(n + m);
/*  454 */       comChannel.writeShort(n);
/*  455 */       comChannel.writeShort(m);
/*  456 */       comChannel.writePad(20);
/*      */       
/*  458 */       for (int i = 0; i < pix.length; i++) {
/*  459 */         comChannel.writeInt(pix[i]);
/*      */       }
/*      */       
/*  462 */       if (m != 0) {
/*  463 */         for (int i = 0; i < msk.length; i++) {
/*  464 */           comChannel.writeInt(msk[i]);
/*      */         }
/*      */       }
/*  467 */       comChannel.flush();
/*      */     }
/*      */   }
/*      */   
/*      */   public static void reqLookupColor(XClient c) throws IOException {
/*  472 */     ComChannel comChannel = c.channel;
/*      */     
/*  474 */     int n = c.length;
/*  475 */     int foo = comChannel.readInt();
/*  476 */     Colormap cmap = (Colormap)lookupIDByType(foo, 6);
/*  477 */     c.length -= 2;
/*  478 */     if (cmap == null) {
/*  479 */       c.errorValue = foo;
/*  480 */       c.errorReason = 12;
/*  481 */       return;
/*      */     }
/*      */     
/*  484 */     foo = comChannel.readShort();
/*  485 */     comChannel.readPad(2);
/*  486 */     comChannel.readByte(c.bbuffer, 0, foo);
/*  487 */     comChannel.readPad(-foo & 0x3);
/*  488 */     c.length = 0;
/*  489 */     foo = chopspace(c.bbuffer, foo);
/*  490 */     if (foo == 0) {
/*  491 */       c.errorReason = 2;
/*  492 */       return;
/*      */     }
/*  494 */     String s = new String(c.bbuffer, 0, foo);
/*  495 */     Color color = (Color)rgbTable.get(s);
/*  496 */     if (color != null) {
/*  497 */       synchronized (comChannel) {
/*  498 */         comChannel.writeByte(1);
/*  499 */         comChannel.writePad(1);
/*  500 */         comChannel.writeShort(c.getSequence());
/*  501 */         comChannel.writeInt(0);
/*      */         
/*  503 */         foo = color.getRed();comChannel.writeShort(foo | foo << 8);
/*  504 */         foo = color.getGreen();comChannel.writeShort(foo | foo << 8);
/*  505 */         foo = color.getBlue();comChannel.writeShort(foo | foo << 8);
/*  506 */         foo = color.getRed();comChannel.writeShort(foo | foo << 8);
/*  507 */         foo = color.getGreen();comChannel.writeShort(foo | foo << 8);
/*  508 */         foo = color.getBlue();comChannel.writeShort(foo | foo << 8);
/*  509 */         comChannel.writePad(12);
/*  510 */         comChannel.flush();
/*  511 */         return;
/*      */       }
/*      */     }
/*      */     
/*  515 */     synchronized (comChannel) {
/*  516 */       comChannel.writeByte((byte)0);
/*  517 */       comChannel.writeByte((byte)15);
/*  518 */       comChannel.writeShort(c.getSequence());
/*  519 */       comChannel.writePad(4);
/*  520 */       comChannel.writeShort(0);
/*  521 */       comChannel.writeByte((byte)92);
/*  522 */       comChannel.writePad(21);
/*  523 */       comChannel.flush();
/*      */     }
/*      */   }
/*      */   
/*      */   public static void reqAllocColor(XClient c) throws IOException
/*      */   {
/*  529 */     ComChannel comChannel = c.channel;
/*  530 */     int foo = comChannel.readInt();
/*  531 */     Colormap cmap = (Colormap)lookupIDByType(foo, 6);
/*  532 */     c.length -= 2;
/*  533 */     if (cmap == null) {
/*  534 */       c.errorValue = foo;
/*  535 */       c.errorReason = 12;
/*  536 */       return;
/*      */     }
/*      */     
/*  539 */     int red = (short)comChannel.readShort();
/*  540 */     int green = (short)comChannel.readShort();
/*  541 */     int blue = (short)comChannel.readShort();
/*      */     
/*  543 */     comChannel.readPad(2);
/*      */     
/*  545 */     c.length -= 2;
/*      */     
/*  547 */     if (((red >> 8 & 0xFF) != 0) || ((green >> 8 & 0xFF) != 0) || ((blue >> 8 & 0xFF) != 0)) {
/*  548 */       red = red >> 8 & 0xFF;
/*  549 */       green = green >> 8 & 0xFF;
/*  550 */       blue = blue >> 8 & 0xFF;
/*      */     }
/*      */     else {
/*  553 */       red &= 0xFF;
/*  554 */       green &= 0xFF;
/*  555 */       blue &= 0xFF;
/*      */     }
/*      */     
/*  558 */     int pixel = cmap.allocColor(c, red, green, blue);
/*  559 */     if (c.errorReason != 0) {
/*  560 */       return;
/*      */     }
/*      */     
/*  563 */     if (cmap.visual.depth.depth != 16) {
/*  564 */       LocalEntry ent = (LocalEntry)cmap.entries[pixel];
/*  565 */       red = ent.r;
/*  566 */       green = ent.g;
/*  567 */       blue = ent.b;
/*  568 */       if (ent.refcnt == 1) {
/*  569 */         cmap.mkIcm();
/*      */       }
/*      */     }
/*      */     
/*  573 */     synchronized (comChannel) {
/*  574 */       comChannel.writeByte(1);
/*  575 */       comChannel.writePad(1);
/*  576 */       comChannel.writeShort(c.getSequence());
/*  577 */       comChannel.writeInt(0);
/*      */       
/*  579 */       comChannel.writeShort(red | red << 8);
/*  580 */       comChannel.writeShort(green | green << 8);
/*  581 */       comChannel.writeShort(blue | blue << 8);
/*      */       
/*  583 */       comChannel.writePad(2);
/*  584 */       comChannel.writeInt(pixel);
/*  585 */       comChannel.writePad(12);
/*  586 */       comChannel.flush();
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */   public static void reqQueryColors(XClient c)
/*      */     throws IOException
/*      */   {
/*  594 */     ComChannel comChannel = c.channel;
/*  595 */     int n = c.length;
/*  596 */     int foo = comChannel.readInt();
/*  597 */     Colormap cmap = (Colormap)lookupIDByType(foo, 6);
/*  598 */     c.length -= 2;
/*  599 */     if (cmap == null) {
/*  600 */       c.errorValue = foo;
/*  601 */       c.errorReason = 12;
/*  602 */       return;
/*      */     }
/*  604 */     n -= 2;
/*  605 */     int[] nn = new int[n];
/*  606 */     for (int i = 0; i < n; i++) {
/*  607 */       nn[i] = comChannel.readInt();
/*      */     }
/*  609 */     c.length = 0;
/*  610 */     cmap.queryColors(c, nn);
/*      */   }
/*      */   
/*      */   public static void reqStoreNamedColor(XClient c) throws IOException
/*      */   {
/*  615 */     ComChannel comChannel = c.channel;
/*  616 */     int doc = c.data;
/*  617 */     int n = c.length;
/*  618 */     int foo = comChannel.readInt();
/*  619 */     Colormap cmap = (Colormap)lookupIDByType(foo, 6);
/*  620 */     c.length -= 2;
/*  621 */     if (cmap == null) {
/*  622 */       c.errorValue = foo;
/*  623 */       c.errorReason = 12;
/*  624 */       return;
/*      */     }
/*      */     
/*  627 */     int pixel = comChannel.readInt();
/*  628 */     int len = comChannel.readShort();
/*  629 */     comChannel.readPad(2);
/*  630 */     n -= 4;
/*  631 */     n *= 4;
/*  632 */     n -= len;
/*  633 */     comChannel.readByte(c.bbuffer, 0, len);
/*  634 */     comChannel.readPad(n);
/*  635 */     c.length = 0;
/*  636 */     len = chopspace(c.bbuffer, len);
/*  637 */     if (len == 0) {
/*  638 */       c.errorReason = 2;
/*  639 */       return;
/*      */     }
/*      */     
/*  642 */     String s = new String(c.bbuffer, 0, len);
/*  643 */     Color color = (Color)rgbTable.get(s);
/*  644 */     if (pixel == -1) {
/*  645 */       System.out.println("?? pixel=" + pixel);
/*  646 */       pixel = 25;
/*      */     }
/*      */     
/*  649 */     int red = color.getRed();int green = color.getGreen();int blue = color.getBlue();
/*  650 */     Color cp = cmap.colors[pixel];
/*  651 */     if ((cp != null) && 
/*  652 */       (doc != 0)) {
/*  653 */       if ((doc & 0x1) == 0) red = cp.getRed();
/*  654 */       if ((doc & 0x2) == 0) green = cp.getGreen();
/*  655 */       if ((doc & 0x4) == 0) { blue = cp.getBlue();
/*      */       }
/*      */     }
/*  658 */     c.errorReason = cmap.storeColor(c, pixel, red, green, blue, doc);
/*  659 */     if (c.errorReason == 0) {
/*  660 */       cmap.mkIcm();
/*      */     }
/*      */   }
/*      */   
/*      */   public static void reqAllocNamedColor(XClient c)
/*      */     throws IOException
/*      */   {
/*  667 */     ComChannel comChannel = c.channel;
/*  668 */     int n = c.length;
/*  669 */     int foo = comChannel.readInt();
/*  670 */     Colormap cmap = (Colormap)lookupIDByType(foo, 6);
/*  671 */     c.length -= 2;
/*  672 */     if (cmap == null) {
/*  673 */       c.errorValue = foo;
/*  674 */       c.errorReason = 12;
/*  675 */       return;
/*      */     }
/*  677 */     int len = comChannel.readShort();
/*  678 */     comChannel.readPad(2);
/*  679 */     n -= 3;
/*  680 */     n *= 4;
/*  681 */     n -= len;
/*  682 */     comChannel.readByte(c.bbuffer, 0, len);
/*  683 */     comChannel.readPad(n);
/*  684 */     c.length = 0;
/*  685 */     len = chopspace(c.bbuffer, len);
/*  686 */     if (len == 0) {
/*  687 */       c.errorReason = 2;
/*  688 */       return;
/*      */     }
/*      */     
/*  691 */     String s = new String(c.bbuffer, 0, len);
/*  692 */     Color color = (Color)rgbTable.get(s);
/*  693 */     if (color != null)
/*      */     {
/*  695 */       int red = color.getRed();
/*  696 */       int green = color.getGreen();
/*  697 */       int blue = color.getBlue();
/*      */       
/*  699 */       int i = cmap.allocColor(c, color.getRed(), color.getGreen(), color.getBlue());
/*  700 */       if (c.errorReason != 0) {
/*  701 */         return;
/*      */       }
/*      */       
/*  704 */       if (cmap.visual.depth.depth != 16) {
/*  705 */         LocalEntry ent = (LocalEntry)cmap.entries[i];
/*  706 */         red = ent.r;
/*  707 */         green = ent.g;
/*  708 */         blue = ent.b;
/*  709 */         if (ent.refcnt == 1) {
/*  710 */           cmap.mkIcm();
/*      */         }
/*      */       }
/*      */       
/*  714 */       synchronized (comChannel) {
/*  715 */         comChannel.writeByte(1);
/*  716 */         comChannel.writePad(1);
/*  717 */         comChannel.writeShort(c.getSequence());
/*  718 */         comChannel.writeInt(0);
/*  719 */         comChannel.writeInt(i);
/*  720 */         comChannel.writeShort(red | red << 8);
/*  721 */         comChannel.writeShort(green | green << 8);
/*  722 */         comChannel.writeShort(blue | blue << 8);
/*  723 */         comChannel.writeShort(red | red << 8);
/*  724 */         comChannel.writeShort(green | green << 8);
/*  725 */         comChannel.writeShort(blue | blue << 8);
/*  726 */         comChannel.writePad(8);
/*  727 */         comChannel.flush();
/*  728 */         return;
/*      */       }
/*      */     }
/*      */     
/*  732 */     synchronized (comChannel) {
/*  733 */       comChannel.writeByte((byte)0);
/*  734 */       comChannel.writeByte((byte)15);
/*  735 */       comChannel.writeShort(c.getSequence());
/*  736 */       comChannel.writePad(4);
/*  737 */       comChannel.writeShort(0);
/*  738 */       comChannel.writeByte((byte)85);
/*  739 */       comChannel.writePad(21);
/*  740 */       comChannel.flush();
/*      */     }
/*      */   }
/*      */   
/*      */   Color getColor(int pixel) {
/*  745 */     if (pixel < 0) pixel = 255;
/*  746 */     if (this.colors.length <= pixel) {
/*  747 */       pixel = 1;
/*      */     }
/*  749 */     Color c = this.colors[pixel];
/*  750 */     if (c == null) {
/*  751 */       c = this.colors[1];
/*      */     }
/*  753 */     return c;
/*      */   }
/*      */   
/*      */   private static int chopspace(byte[] buf, int len) {
/*  757 */     for (int i = 0; i < len; i++) {
/*  758 */       if ((65 <= buf[i]) && (buf[i] <= 90)) {
/*  759 */         buf[i] = ((byte)(97 + buf[i] - 65));
/*      */       }
/*  761 */       else if (buf[i] == 32) {
/*  762 */         len--;
/*  763 */         for (int j = i; j < len; j++) {
/*  764 */           buf[j] = buf[(j + 1)];
/*      */         }
/*  766 */         i--;
/*      */       }
/*      */     }
/*  769 */     return len;
/*      */   }
/*      */   
/*      */   public static int isMapInstalled(int map, XWindow w) {
/*  773 */     return 1;
/*      */   }
/*      */   
/*  776 */   public static Hashtable rgbTable = null;
/*      */   
/*      */   public static void init() {
/*  779 */     if (rgbTable == null) {
/*  780 */       rgbTable = new Hashtable();
/*  781 */       RGBTXT.init(rgbTable);
/*      */     }
/*  783 */     if (installed == null) {
/*  784 */       installed = new Colormap[1];
/*      */     }
/*      */   }
/*      */   
/*      */   public void delete() throws IOException {
/*  789 */     for (int i = 0; i < this.pixels.length; i++) {
/*  790 */       if (this.pixels != null) this.pixels[i] = null;
/*      */     }
/*  792 */     this.pixels = ((int[][])null);
/*  793 */     this.entries = null;
/*      */   }
/*      */   
/*      */   private void freeCell(int pix, int channel)
/*      */   {
/*  798 */     switch (channel)
/*      */     {
/*      */     }
/*      */     
/*  802 */     Entry ent = this.entries[pix];
/*      */     
/*      */ 
/*      */ 
/*  806 */     if (ent.refcnt > 1) {
/*  807 */       ent.refcnt -= 1;
/*      */     }
/*      */     else {
/*  810 */       ent.refcnt = 0;
/*  811 */       switch (channel)
/*      */       {
/*      */       }
/*      */       
/*  815 */       this.freeRed += 1;
/*      */       
/*      */ 
/*  818 */       free(pix);
/*      */     }
/*      */   }
/*      */   
/*      */   public void freePixels(int client) {
/*  823 */     int[] pixStart = this.pixels[client];
/*  824 */     if (((this.visual.clss & 0x1) != 0) && 
/*  825 */       (pixStart != null)) {
/*  826 */       for (int i = 0; i < pixStart.length; i++) {
/*  827 */         freeCell(pixStart[i], 0);
/*      */       }
/*      */     }
/*      */     
/*  831 */     this.pixels[client] = null;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public int allocColor(XClient XClient, int red, int green, int blue)
/*      */     throws IOException
/*      */   {
/*  839 */     int pixel = 0;
/*      */     
/*  841 */     if (this.visual.depth.depth == 16) { return 0;
/*      */     }
/*  843 */     int clss = this.visual.clss;
/*  844 */     if ((this.flags & 0x4) != 0) {
/*  845 */       clss |= 0x1;
/*      */     }
/*  847 */     switch (clss) {
/*      */     case 0: 
/*      */     case 2: 
/*  850 */       if (this.visual.getDepth().depth == 1) {
/*  851 */         pixel = (red + green + blue) / 3 == 0 ? 0 : 1;
/*      */       }
/*      */       else {
/*  854 */         pixel = (red + green + blue) / 3;
/*      */       }
/*  856 */       break;
/*      */     case 1: 
/*      */     case 3: 
/*  859 */       pixel = findColor(XClient, this.entries, red, green, blue, 3);
/*      */       
/*  861 */       if (XClient.errorReason != 0) {
/*  862 */         return pixel;
/*      */       }
/*      */       break;
/*      */     }
/*      */     
/*  867 */     if ((this.pixels[XClient.index] != null) && (this.pixels[XClient.index].length == 1) && ((this.id & 0x1FC00000) >> 22 != XClient.index) && ((this.flags & 0x4) == 0))
/*      */     {
/*      */ 
/*      */ 
/*  871 */       ClientColormap cc = new ClientColormap(fakeClientId(XClient), XClient.index, this.id);
/*      */       
/*  873 */       add(cc);
/*      */     }
/*  875 */     return pixel;
/*      */   }
/*      */   
/*      */   private int findBestPixel(Entry[] entry, int red, int green, int blue, int channel) {
/*  879 */     return 0;
/*      */   }
/*      */   
/*      */ 
/*      */   private int findColor(XClient XClient, Entry[] entries, int red, int green, int blue, int channel)
/*      */   {
/*  885 */     boolean foundFree = false;
/*  886 */     int Free = 0;
/*      */     
/*  888 */     int pPixel = -1;
/*      */     
/*  890 */     for (int i = 0; i < entries.length; i++) { Entry ent;
/*  891 */       if ((ent = entries[i]).refcnt > 0) {
/*  892 */         if (ent.eq(red, green, blue)) {
/*  893 */           if (XClient.index >= 0) {
/*  894 */             ent.refcnt += 1;
/*      */           }
/*  896 */           pPixel = i;
/*  897 */           break;
/*      */         }
/*      */       }
/*  900 */       else if ((!foundFree) && (ent.refcnt == 0)) {
/*  901 */         Free = i;
/*  902 */         foundFree = true;
/*  903 */         if ((this.flags & 0x4) != 0) {
/*      */           break;
/*      */         }
/*      */       }
/*      */     }
/*  908 */     if (pPixel == -1) {
/*  909 */       if (!foundFree) {
/*  910 */         XClient.errorReason = 11;
/*  911 */         return pPixel;
/*      */       }
/*      */       
/*  914 */       chkLocalEntry(Free);
/*  915 */       Entry ent = entries[Free];
/*  916 */       ent.refcnt = (XClient.index >= 0 ? 1 : -2);
/*      */       
/*  918 */       switch (channel) {
/*      */       case 3: 
/*  920 */         ((LocalEntry)ent).r = red;
/*  921 */         ((LocalEntry)ent).g = green;
/*  922 */         ((LocalEntry)ent).b = blue;
/*  923 */         if (XClient.index >= 0)
/*  924 */           this.freeRed -= 1;
/*      */         break;
/*      */       }
/*  927 */       pPixel = Free;
/*  928 */       alloc(pPixel, new Color(red, green, blue));
/*      */     }
/*      */     
/*  931 */     if (((this.flags & 0x4) != 0) || (XClient.index == -1)) {
/*  932 */       return pPixel;
/*      */     }
/*      */     
/*  935 */     switch (channel) {
/*      */     case 0: 
/*      */     case 3: 
/*  938 */       int[] foo = expand(this.pixels[XClient.index], 1);
/*  939 */       foo[(foo.length - 1)] = pPixel;
/*      */       
/*  941 */       this.pixels[XClient.index] = foo;
/*      */     }
/*      */     
/*  944 */     return pPixel;
/*      */   }
/*      */   
/*      */   private void update() {
/*  948 */     if (this.visual.clss != 5)
/*      */     {
/*      */ 
/*      */ 
/*      */ 
/*  953 */       for (int i = 0; i < this.entries.length; i++) {
/*  954 */         Entry ent = this.entries[i];
/*  955 */         if (ent.refcnt == 0)
/*  956 */           free(i);
/*      */         int blue;
/*  958 */         int red; int green; int blue; if (ent.shared) {
/*  959 */           int red = ((SharedEntry)ent).r.color;
/*  960 */           int green = ((SharedEntry)ent).g.color;
/*  961 */           blue = ((SharedEntry)ent).b.color;
/*      */         }
/*      */         else {
/*  964 */           red = ((LocalEntry)ent).r;
/*  965 */           green = ((LocalEntry)ent).g;
/*  966 */           blue = ((LocalEntry)ent).b;
/*      */         }
/*  968 */         alloc(i, new Color(red, green, blue));
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   private void allocColorPlanes(XClient c, int colors, boolean contig, int r, int g, int b)
/*      */     throws IOException
/*      */   {
/*  978 */     ComChannel comChannel = c.channel;
/*      */     
/*  980 */     if ((this.visual.clss & 0x1) == 0) {
/*  981 */       c.errorReason = 11;
/*  982 */       return;
/*      */     }
/*      */     
/*  985 */     ClientColormap cc = null;
/*      */     
/*  987 */     int oldcount = this.pixels[c.index] == null ? 0 : this.pixels[c.index].length;
/*  988 */     int[] ppix = new int[colors];
/*      */     
/*  990 */     if ((this.visual.clss != 5) || (
/*      */     
/*  992 */       (oldcount == 0) && ((this.id & 0x1FC00000) >> 22 != c.index)))
/*      */     {
/*  994 */       cc = new ClientColormap(fakeClientId(c), c.index, this.id);
/*      */     }
/*      */     
/*  997 */     if (this.visual.clss != 5)
/*      */     {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1004 */       int[] pMask = new int[1];
/* 1005 */       boolean ok = allocPseudo(c, colors, r + g + b, contig, ppix, pMask);
/* 1006 */       if (ok) { int bmask;
/*      */         int gmask;
/* 1008 */         int rmask = gmask = bmask = 0;
/* 1009 */         int shift = 1;
/* 1010 */         for (int i = r;; shift += shift) { i--; if (i < 0) break;
/* 1011 */           while ((pMask[0] & shift) == 0) {
/* 1012 */             shift += shift;
/*      */           }
/* 1014 */           rmask |= shift;
/*      */         }
/* 1016 */         for (int i = g;; shift += shift) { i--; if (i < 0) break;
/* 1017 */           while ((pMask[0] & shift) == 0) {
/* 1018 */             shift += shift;
/*      */           }
/* 1020 */           gmask |= shift;
/*      */         }
/* 1022 */         for (int i = b;; shift += shift) { i--; if (i < 0) break;
/* 1023 */           while ((pMask[0] & shift) == 0) {
/* 1024 */             shift += shift;
/*      */           }
/* 1026 */           bmask |= shift;
/*      */         }
/* 1028 */         if (!allocShared(c, ppix, colors, r, g, b, rmask, gmask, bmask, oldcount))
/*      */         {
/* 1030 */           freeColors(c, ppix, pMask[0]);
/* 1031 */           c.errorReason = 11;
/*      */         }
/*      */         else {
/* 1034 */           synchronized (comChannel) {
/* 1035 */             comChannel.writeByte(1);
/* 1036 */             comChannel.writePad(1);
/* 1037 */             comChannel.writeShort(c.getSequence());
/* 1038 */             comChannel.writeInt(colors);
/* 1039 */             comChannel.writeShort(colors);
/* 1040 */             comChannel.writePad(2);
/* 1041 */             comChannel.writeInt(rmask);
/* 1042 */             comChannel.writeInt(gmask);
/* 1043 */             comChannel.writeInt(bmask);
/* 1044 */             comChannel.writePad(8);
/* 1045 */             for (int i = 0; i < this.pixels.length; i++) {
/* 1046 */               comChannel.writeInt(ppix[i]);
/*      */             }
/* 1048 */             comChannel.flush();
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/*      */     
/* 1054 */     if ((c.errorReason == 0) && (cc != null)) {
/* 1055 */       add(cc);
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private boolean allocShared(XClient XClient, int[] ppix, int c, int r, int g, int b, int rmask, int gmask, int bmask, int start)
/*      */   {
/* 1068 */     int[] pptr = this.pixels[XClient.index];
/*      */     
/* 1070 */     int npixClientNew = c << r + g + b;
/* 1071 */     int npixShared = (c << r) + (c << g) + (c << b);
/* 1072 */     SharedColor[] sharedList = new SharedColor[npixShared];
/*      */     
/* 1074 */     for (int i = 0; i < sharedList.length; i++) {
/* 1075 */       sharedList[i] = new SharedColor();
/*      */     }
/* 1077 */     int sindex = 0;
/* 1078 */     for (int i = 0; i < c; i++) {
/* 1079 */       int basemask = (gmask | bmask) ^ 0xFFFFFFFF;
/* 1080 */       int common = ppix[i] & basemask;
/* 1081 */       if (rmask != 0) {
/* 1082 */         int bits = 0;
/* 1083 */         int base = lowbit(rmask);
/*      */         for (;;) {
/* 1085 */           SharedColor shared = sharedList[sindex];sindex++;
/* 1086 */           for (int j = 0; j < npixClientNew; j++) { int pix;
/* 1087 */             if (((pix = pptr[(start + j)]) & basemask) == (common | bits)) {
/* 1088 */               chkShareEntry(pix);
/* 1089 */               ((SharedEntry)this.entries[pix]).r = shared;
/*      */             }
/*      */           }
/*      */           
/* 1093 */           if (bits == rmask) break;
/* 1094 */           bits += base;
/* 1095 */           while ((bits & (rmask ^ 0xFFFFFFFF)) != 0) {
/* 1096 */             bits += (bits & (rmask ^ 0xFFFFFFFF));
/*      */           }
/*      */         }
/*      */       }
/*      */       
/* 1101 */       SharedColor shared = sharedList[sindex];sindex++;
/* 1102 */       for (int j = 0; j < npixClientNew; j++) { int pix;
/* 1103 */         if (((pix = pptr[(start + j)]) & basemask) == common) {
/* 1104 */           chkShareEntry(pix);
/* 1105 */           ((SharedEntry)this.entries[pix]).r = shared;
/*      */         }
/*      */       }
/*      */       
/* 1109 */       basemask = (rmask | bmask) ^ 0xFFFFFFFF;
/* 1110 */       common = ppix[i] & basemask;
/* 1111 */       if (gmask != 0) {
/* 1112 */         int bits = 0;
/* 1113 */         int base = lowbit(gmask);
/*      */         for (;;) {
/* 1115 */           shared = sharedList[sindex];sindex++;
/* 1116 */           for (int j = 0; j < npixClientNew; j++) { int pix;
/* 1117 */             if (((pix = pptr[(start + j)]) & basemask) == (common | bits)) {
/* 1118 */               chkShareEntry(pix);
/* 1119 */               ((SharedEntry)this.entries[pix]).g = shared;
/*      */             }
/*      */           }
/*      */           
/* 1123 */           if (bits == gmask) break;
/* 1124 */           bits += base;
/* 1125 */           while ((bits & (gmask ^ 0xFFFFFFFF)) != 0) {
/* 1126 */             bits += (bits & (gmask ^ 0xFFFFFFFF));
/*      */           }
/*      */         }
/*      */       }
/*      */       
/* 1131 */       shared = sharedList[sindex];sindex++;
/* 1132 */       for (int j = 0; j < npixClientNew; j++) { int pix;
/* 1133 */         if (((pix = pptr[(start + j)]) & basemask) == common) {
/* 1134 */           chkShareEntry(pix);
/* 1135 */           ((SharedEntry)this.entries[pix]).g = shared;
/*      */         }
/*      */       }
/*      */       
/* 1139 */       basemask = (rmask | gmask) ^ 0xFFFFFFFF;
/* 1140 */       common = ppix[i] & basemask;
/* 1141 */       if (bmask != 0) {
/* 1142 */         int bits = 0;
/* 1143 */         int base = lowbit(bmask);
/*      */         for (;;) {
/* 1145 */           shared = sharedList[sindex];sindex++;
/* 1146 */           for (int j = 0; j < npixClientNew; j++) { int pix;
/* 1147 */             if (((pix = pptr[(start + j)]) & basemask) == (common | bits)) {
/* 1148 */               chkShareEntry(pix);
/* 1149 */               ((SharedEntry)this.entries[pix]).b = shared;
/*      */             }
/*      */           }
/* 1152 */           if (bits == bmask) break;
/* 1153 */           bits += base;
/* 1154 */           while ((bits & (bmask ^ 0xFFFFFFFF)) != 0) {
/* 1155 */             bits += (bits & (bmask ^ 0xFFFFFFFF));
/*      */           }
/*      */         }
/*      */       }
/*      */       
/* 1160 */       shared = sharedList[sindex];sindex++;
/* 1161 */       for (int j = 0; j < npixClientNew; j++) { int pix;
/* 1162 */         if (((pix = pptr[(start + j)]) & basemask) == common) {
/* 1163 */           chkShareEntry(pix);
/* 1164 */           ((SharedEntry)this.entries[pix]).b = shared;
/*      */         }
/*      */       }
/*      */     }
/*      */     
/* 1169 */     return true;
/*      */   }
/*      */   
/*      */   private void chkShareEntry(int i) {
/* 1173 */     if (!this.entries[i].shared) {
/* 1174 */       this.entries[i] = new SharedEntry();
/*      */     }
/*      */   }
/*      */   
/*      */   private void chkLocalEntry(int i) {
/* 1179 */     if (this.entries[i].shared) {
/* 1180 */       this.entries[i] = new LocalEntry();
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   private void allocColorCells(XClient XClient, int colors, int planes, boolean contig, int[] ppix, int[] masks)
/*      */   {
/* 1189 */     ClientColormap cc = null;
/*      */     
/* 1191 */     if ((this.visual.clss & 0x1) == 0) {
/* 1192 */       XClient.errorReason = 11;
/* 1193 */       return;
/*      */     }
/* 1195 */     if ((this.pixels[XClient.index] == null) && ((this.id & 0x1FC00000) >> 22 != XClient.index))
/*      */     {
/* 1197 */       cc = new ClientColormap(fakeClientId(XClient), XClient.index, this.id);
/*      */     }
/*      */     
/*      */ 
/* 1201 */     if (this.visual.clss != 5)
/*      */     {
/*      */ 
/*      */ 
/* 1205 */       int[] pMask = new int[1];
/* 1206 */       boolean ok = allocPseudo(XClient, colors, planes, contig, ppix, pMask);
/* 1207 */       if (ok) {
/* 1208 */         int i = 0;
/* 1209 */         int r = 1; for (int n = planes;; r += r) { n--; if (n < 0) break;
/* 1210 */           while ((pMask[0] & r) == 0) {
/* 1211 */             r += r;
/*      */           }
/* 1213 */           masks[i] = r;i++;
/*      */         }
/*      */       }
/*      */     }
/* 1217 */     if ((XClient.errorReason == 0) && (cc != null)) {
/* 1218 */       add(cc);
/*      */     }
/*      */   }
/*      */   
/*      */   private boolean allocPseudo(XClient XClient, int c, int r, boolean contig, int[] pDst, int[] pMask)
/*      */   {
/* 1224 */     int npix = c << r;
/* 1225 */     if ((r >= 32) || (npix > this.freeRed) || (npix < c)) {
/* 1226 */       XClient.errorReason = 11;
/* 1227 */       return false;
/*      */     }
/*      */     
/* 1230 */     int[] ppixTemp = new int[npix];
/* 1231 */     boolean ok = allocCP(XClient, this.entries, c, r, contig, ppixTemp, pMask);
/* 1232 */     if (ok) {
/* 1233 */       int[] foo = expand(this.pixels[XClient.index], npix);
/* 1234 */       System.arraycopy(ppixTemp, 0, foo, foo.length - npix, npix);
/* 1235 */       this.pixels[XClient.index] = foo;
/* 1236 */       this.freeRed -= npix;
/* 1237 */       System.arraycopy(ppixTemp, 0, pDst, 0, c);
/*      */     }
/* 1239 */     return ok;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private boolean allocCP(XClient XClient, Entry[] entries, int count, int planes, boolean contig, int[] pixels, int[] pmask)
/*      */   {
/* 1252 */     int dplanes = this.visual.nplanes;
/*      */     
/* 1254 */     if (planes == 0) {
/* 1255 */       int j = 0;
/* 1256 */       for (int i = 0; i < count; i++) { Entry ent;
/* 1257 */         while ((j < entries.length) && ((ent = entries[j]).refcnt != 0)) j++;
/* 1258 */         if (j == entries.length) {
/* 1259 */           j = -1;
/* 1260 */           break;
/*      */         }
/*      */       }
/* 1263 */       if (j == -1)
/*      */       {
/* 1265 */         XClient.errorReason = 11;
/* 1266 */         return false;
/*      */       }
/* 1268 */       j = 0;
/* 1269 */       for (int i = 0; i < count; i++) { Entry ent;
/* 1270 */         while ((ent = entries[j]).refcnt != 0) j++;
/* 1271 */         chkLocalEntry(j);
/* 1272 */         ent.refcnt = -1;
/* 1273 */         pixels[i] = j;
/*      */       }
/* 1275 */       pmask[0] = 0;
/* 1276 */       return true;
/*      */     }
/* 1278 */     if (planes > dplanes) {
/* 1279 */       XClient.errorReason = 11;
/* 1280 */       return false;
/*      */     }
/* 1282 */     int mask = (1 << planes) - 1;
/* 1283 */     int base = 1;
/* 1284 */     dplanes -= planes - 1;
/* 1285 */     for (;;) { dplanes--; if (dplanes < 0) {
/*      */         break;
/*      */       }
/* 1288 */       int ppixi = 0;
/* 1289 */       int[] ppix = pixels;
/*      */       
/* 1291 */       int found = 0;
/* 1292 */       int pixel = 0;
/*      */       
/* 1294 */       int entcount = this.visual.colormapEntries - mask;
/*      */       
/* 1296 */       while (pixel < entcount) {
/* 1297 */         int save = pixel;
/* 1298 */         int maxp = pixel + mask + base;
/*      */         
/* 1300 */         while ((pixel != maxp) && (entries[pixel].refcnt == 0)) {
/* 1301 */           pixel += base;
/*      */         }
/* 1303 */         if (pixel == maxp) {
/* 1304 */           ppix[ppixi] = save;ppixi++;
/* 1305 */           found++;
/* 1306 */           if (found == count) {
/* 1307 */             count--; if (count >= 0) {
/* 1308 */               pixel = pixels[count];
/* 1309 */               maxp = pixel + mask;
/*      */               for (;;)
/*      */               {
/* 1312 */                 chkLocalEntry(pixel);
/*      */                 
/* 1314 */                 entries[pixel].refcnt = -1;
/* 1315 */                 entries[pixel].shared = false;
/* 1316 */                 if (pixel == maxp) {
/*      */                   break;
/*      */                 }
/* 1319 */                 pixel += base;
/* 1320 */                 ppix[ppixi] = pixel;ppixi++;
/*      */               }
/*      */             }
/* 1323 */             pmask[0] = mask;
/* 1324 */             return true;
/*      */           }
/*      */         }
/* 1327 */         pixel = save + 1;
/* 1328 */         if ((pixel & mask) != 0) {
/* 1329 */           pixel += mask;
/*      */         }
/*      */       }
/* 1286 */       mask += mask;base += base;
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1333 */     dplanes = this.visual.nplanes;
/* 1334 */     if ((contig) || (planes == 1) || (dplanes < 3)) {
/* 1335 */       return false;
/*      */     }
/*      */     
/* 1338 */     int finalmask = ((1 << planes - 1) - 1 << dplanes - planes + 1) + (1 << dplanes - planes - 1);
/*      */     
/*      */ 
/*      */ 
/* 1342 */     for (mask = (3 << planes - 1) - 1; mask <= finalmask; mask++) {
/* 1343 */       int pixel = mask >> 1 & 0xDB6DB6DB;
/* 1344 */       pixel = mask - pixel - (pixel >> 1 & 0xDB6DB6DB);
/* 1345 */       if ((pixel + (pixel >> 3) & 0xC71C71C7) % 63 == planes)
/*      */       {
/* 1347 */         int[] ppix = pixels;
/* 1348 */         int ppixi = 0;
/*      */         
/* 1350 */         int found = 0;
/* 1351 */         int entcount = this.visual.colormapEntries - mask;
/* 1352 */         base = lowbit(mask);
/* 1353 */         for (pixel = 0; pixel < entcount; pixel++)
/* 1354 */           if ((pixel & mask) == 0)
/*      */           {
/* 1356 */             int maxp = 0;
/* 1357 */             while ((entries[(pixel + maxp)].refcnt == 0) && 
/* 1358 */               (maxp != mask)) {
/* 1359 */               maxp += base;
/* 1360 */               while ((maxp & (mask ^ 0xFFFFFFFF)) != 0) {
/* 1361 */                 maxp += (maxp & (mask ^ 0xFFFFFFFF));
/*      */               }
/*      */             }
/* 1364 */             if ((maxp >= mask) && (entries[(pixel + mask)].refcnt == 0))
/*      */             {
/*      */ 
/* 1367 */               ppix[ppixi] = pixel;ppixi++;
/* 1368 */               found++;
/* 1369 */               if (found >= count)
/*      */               {
/*      */ 
/* 1372 */                 count--; if (count >= 0) {
/* 1373 */                   pixel = pixels[count];
/* 1374 */                   maxp = 0;
/*      */                   for (;;) {
/* 1376 */                     chkLocalEntry(pixel + maxp);
/* 1377 */                     entries[(pixel + maxp)].refcnt = -1;
/* 1378 */                     entries[(pixel + maxp)].shared = false;
/* 1379 */                     if (maxp == mask) break;
/* 1380 */                     maxp += base;
/* 1381 */                     while ((maxp & (mask ^ 0xFFFFFFFF)) != 0) {
/* 1382 */                       maxp += (maxp & (mask ^ 0xFFFFFFFF));
/*      */                     }
/* 1384 */                     ppix[ppixi] = (pixel + maxp);ppixi++;
/*      */                   }
/*      */                 }
/* 1387 */                 pmask[0] = mask;
/* 1388 */                 return true;
/*      */               }
/*      */             } } } }
/* 1391 */     return false;
/*      */   }
/*      */   
/*      */   private void freeColors(XClient c, int rest, int mask) throws IOException {
/* 1395 */     ComChannel comChannel = c.channel;
/* 1396 */     if ((this.flags & 0x2) != 0) {
/* 1397 */       c.errorReason = 10;
/* 1398 */       return;
/*      */     }
/* 1400 */     if (rest == 0) return;
/* 1401 */     int[] foo = new int[rest];
/* 1402 */     for (int i = 0; i < foo.length; i++) {
/* 1403 */       foo[i] = comChannel.readInt();
/*      */     }
/* 1405 */     c.length = 0;
/* 1406 */     freeColors(c, foo, mask);
/*      */   }
/*      */   
/*      */   void freeColors(XClient c, int[] foo, int mask) throws IOException {
/* 1410 */     int rmask = 0;
/* 1411 */     if ((this.visual.clss | 0x1) == 5) {
/* 1412 */       rmask = 0;
/*      */     }
/*      */     else {
/* 1415 */       rmask = mask & (1 << this.visual.nplanes) - 1;
/* 1416 */       freeCo(c, 3, foo, rmask);
/*      */     }
/* 1418 */     if (mask != rmask) {
/* 1419 */       c.errorValue = (foo[0] | mask);
/* 1420 */       c.errorReason = 2;
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private void freeCo(XClient XClient, int channel, int[] pixIn, int mask)
/*      */   {
/* 1431 */     int errVal = 0;
/*      */     
/*      */ 
/* 1434 */     int cmask = 255;
/* 1435 */     int offset = 0;
/* 1436 */     int[] pixClient = null;
/*      */     
/* 1438 */     int bits = 0;
/* 1439 */     int zapped = 0;
/* 1440 */     int base = lowbit(mask);
/* 1441 */     switch (channel)
/*      */     {
/*      */     }
/* 1444 */     cmask = -1;
/* 1445 */     int rgbbad = 0;
/* 1446 */     offset = 0;
/* 1447 */     int numents = this.visual.colormapEntries;
/* 1448 */     pixClient = this.pixels[XClient.index];
/*      */     
/*      */ 
/*      */     for (;;)
/*      */     {
/* 1453 */       for (int i = 0; i < pixIn.length; i++) {
/* 1454 */         int pixTest = ((pixIn[i] | bits) & cmask) >> offset;
/* 1455 */         if ((pixTest >= numents) || ((pixIn[i] & rgbbad) != 0)) {
/* 1456 */           XClient.errorValue = (pixIn[i] | bits);
/* 1457 */           errVal = 2;
/*      */ 
/*      */         }
/* 1460 */         else if (pixClient == null) {
/* 1461 */           errVal = 10;
/*      */         }
/*      */         else {
/* 1464 */           int j = 0;
/* 1465 */           while ((j < pixClient.length) && (pixClient[j] != pixTest)) j++;
/* 1466 */           if (j != pixClient.length) {
/* 1467 */             if ((this.visual.clss & 0x1) != 0) {
/* 1468 */               freeCell(pixTest, channel);
/* 1469 */               pixClient[j] = -1;
/* 1470 */               zapped++;
/*      */             }
/*      */             else {
/* 1473 */               errVal = 10;
/*      */             }
/*      */           }
/*      */         }
/*      */       }
/*      */       
/* 1479 */       if (bits == mask) break;
/* 1480 */       bits += base;
/* 1481 */       while ((bits & (mask ^ 0xFFFFFFFF)) != 0) {
/* 1482 */         bits += (bits & (mask ^ 0xFFFFFFFF));
/*      */       }
/*      */     }
/*      */     
/* 1486 */     if (zapped != 0) {
/* 1487 */       int npixNew = pixClient.length - zapped;
/* 1488 */       if (npixNew != 0) { int[] cptr;
/* 1489 */         int[] pptr = cptr = pixClient;
/* 1490 */         int i = 0; for (int j = 0; (i < pixClient.length) && (j < npixNew); i++) {
/* 1491 */           if (cptr[i] != -1) {
/* 1492 */             pptr[j] = cptr[i];
/* 1493 */             j++;
/*      */           }
/*      */         }
/* 1496 */         int[] foo = new int[npixNew];
/* 1497 */         System.arraycopy(pixClient, 0, foo, 0, npixNew);
/* 1498 */         pixClient = foo;
/*      */       }
/*      */       else {
/* 1501 */         pixClient = null;
/*      */       }
/* 1503 */       switch (channel) {
/*      */       case 0: 
/*      */       case 3: 
/* 1506 */         this.pixels[XClient.index] = pixClient;
/*      */       }
/*      */       
/*      */     }
/* 1510 */     if (errVal != 0) {
/* 1511 */       XClient.errorReason = errVal;
/*      */     }
/*      */   }
/*      */   
/*      */   private void storeColors(XClient c, int rest) throws IOException
/*      */   {
/* 1517 */     int errVal = 0;
/* 1518 */     ComChannel comChannel = c.channel;
/*      */     
/* 1520 */     if (((this.visual.clss & 0x1) == 0) && ((this.flags & 0x4) == 0))
/*      */     {
/* 1522 */       c.errorReason = 11;
/* 1523 */       return;
/*      */     }
/*      */     
/* 1526 */     int idef = 0;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/* 1531 */     while (rest != 0) {
/* 1532 */       int pixel = comChannel.readInt();rest--;
/* 1533 */       int red = (short)comChannel.readShort();
/* 1534 */       int green = (short)comChannel.readShort();rest--;
/* 1535 */       int blue = (short)comChannel.readShort();
/* 1536 */       int doc = comChannel.readByte();
/* 1537 */       comChannel.readPad(1);rest--;
/*      */       
/* 1539 */       if (((red >> 8 & 0xFF) != 0) || ((green >> 8 & 0xFF) != 0) || ((blue >> 8 & 0xFF) != 0)) {
/* 1540 */         red = red >> 8 & 0xFF;
/* 1541 */         green = green >> 8 & 0xFF;
/* 1542 */         blue = blue >> 8 & 0xFF;
/*      */       }
/*      */       else {
/* 1545 */         red &= 0xFF;
/* 1546 */         green &= 0xFF;
/* 1547 */         blue &= 0xFF;
/*      */       }
/* 1549 */       errVal = storeColor(c, pixel, red, green, blue, doc);
/*      */     }
/* 1551 */     c.errorReason = errVal;
/*      */   }
/*      */   
/*      */ 
/*      */   private int storeColor(XClient c, int pixel, int red, int green, int blue, int doc)
/*      */     throws IOException
/*      */   {
/* 1558 */     int errVal = 0;
/*      */     
/* 1560 */     if ((this.visual.clss | 0x1) != 5)
/*      */     {
/*      */ 
/* 1563 */       boolean ok = true;
/* 1564 */       if (pixel >= this.visual.colormapEntries) {
/* 1565 */         c.errorValue = pixel;
/* 1566 */         errVal = 2;
/* 1567 */         ok = false;
/*      */       }
/* 1569 */       else if (this.entries[pixel].refcnt != -1) {
/* 1570 */         errVal = 11;
/* 1571 */         ok = false;
/*      */       }
/* 1573 */       if (!ok)
/*      */       {
/*      */ 
/* 1576 */         return errVal;
/*      */       }
/*      */       
/* 1579 */       Entry pent = this.entries[pixel];
/*      */       
/* 1581 */       if ((doc & 0x1) != 0) {
/* 1582 */         if (!pent.shared)
/*      */         {
/* 1584 */           ((LocalEntry)pent).r = red;
/*      */         }
/*      */       }
/* 1587 */       else if (!pent.shared)
/*      */       {
/*      */ 
/* 1590 */         red = ((LocalEntry)pent).r;
/*      */       }
/*      */       
/* 1593 */       if ((doc & 0x2) != 0) {
/* 1594 */         if (!pent.shared)
/*      */         {
/* 1596 */           ((LocalEntry)pent).g = green;
/*      */         }
/*      */       }
/* 1599 */       else if (!pent.shared)
/*      */       {
/*      */ 
/* 1602 */         green = ((LocalEntry)pent).g;
/*      */       }
/*      */       
/* 1605 */       if ((doc & 0x4) != 0) {
/* 1606 */         if (!pent.shared)
/*      */         {
/*      */ 
/* 1609 */           ((LocalEntry)pent).b = blue;
/*      */         }
/*      */         
/*      */       }
/* 1613 */       else if (!pent.shared)
/*      */       {
/*      */ 
/* 1616 */         blue = ((LocalEntry)pent).b;
/*      */       }
/*      */       
/* 1619 */       alloc(pixel, new Color(red, green, blue));
/*      */     }
/* 1621 */     return errVal;
/*      */   }
/*      */   
/*      */   private void queryColors(XClient c, int[] ppixel) throws IOException {
/* 1625 */     ComChannel comChannel = c.channel;
/* 1626 */     if (((this.visual.clss & 0x1) == 0) && (this.visual.depth.depth == 16))
/*      */     {
/* 1628 */       int n = ppixel.length;
/*      */       
/* 1630 */       synchronized (comChannel) {
/* 1631 */         comChannel.writeByte(1);
/* 1632 */         comChannel.writePad(1);
/* 1633 */         comChannel.writeShort(c.getSequence());
/* 1634 */         comChannel.writeInt(n * 2);
/* 1635 */         comChannel.writeShort(n);
/* 1636 */         comChannel.writePad(22);
/*      */         
/* 1638 */         for (int i = 0; i < n; i++) {
/* 1639 */           int ii = ppixel[i];
/* 1640 */           int foo = (ii >> 11 & 0x1F) * 8;
/* 1641 */           comChannel.writeShort(foo | foo << 8);
/* 1642 */           foo = (ii >> 5 & 0x3F) * 4;
/* 1643 */           comChannel.writeShort(foo | foo << 8);
/* 1644 */           foo = (ii & 0x1F) * 8;
/* 1645 */           comChannel.writeShort(foo | foo << 8);
/* 1646 */           comChannel.writePad(2);
/*      */         }
/* 1648 */         comChannel.flush();
/* 1649 */         return;
/*      */       }
/*      */     }
/*      */     
/* 1653 */     if ((this.visual.clss | 0x1) != 5)
/*      */     {
/*      */ 
/* 1656 */       for (int i = 0; i < ppixel.length; i++) {
/* 1657 */         if (ppixel[i] >= this.visual.colormapEntries) {
/* 1658 */           c.errorValue = ppixel[i];
/* 1659 */           c.errorReason = 2;
/* 1660 */           return;
/*      */         }
/*      */       }
/*      */       
/* 1664 */       synchronized (comChannel) {
/* 1665 */         comChannel.writeByte(1);
/* 1666 */         comChannel.writePad(1);
/* 1667 */         comChannel.writeShort(c.getSequence());
/* 1668 */         comChannel.writeInt(ppixel.length * 2);
/* 1669 */         comChannel.writeShort(ppixel.length);
/* 1670 */         comChannel.writePad(22);
/*      */         
/* 1672 */         for (int i = 0; i < ppixel.length; i++) {
/* 1673 */           Entry pent = this.entries[ppixel[i]];
/* 1674 */           if (!pent.shared)
/*      */           {
/*      */ 
/*      */ 
/* 1678 */             LocalEntry le = (LocalEntry)pent;
/* 1679 */             comChannel.writeShort(le.r | le.r << 8);
/* 1680 */             comChannel.writeShort(le.g | le.g << 8);
/* 1681 */             comChannel.writeShort(le.b | le.b << 8);
/* 1682 */             comChannel.writePad(2);
/*      */           }
/*      */         }
/* 1685 */         comChannel.flush();
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */   private static int lowbit(int mask)
/*      */   {
/* 1692 */     int result = 1;
/* 1693 */     for (int i = 0; i < 32; i++) {
/* 1694 */       if ((mask & 0x1) != 0) { result <<= i; break; }
/* 1695 */       mask >>= 1;
/*      */     }
/* 1697 */     return result;
/*      */   }
/*      */   
/*      */   private int[] expand(int[] foo, int i) {
/* 1701 */     int[] bar = new int[i + (foo == null ? 0 : foo.length)];
/* 1702 */     if (foo != null) System.arraycopy(foo, 0, bar, 0, foo.length);
/* 1703 */     return bar;
/*      */   }
/*      */   
/*      */   public void mkIcm() {
/* 1707 */     if (((this.visual.clss & 0x1) != 0) || ((this.flags & 0x4) != 0))
/*      */     {
/* 1709 */       if (this.visual.depth.depth == 16) return;
/* 1710 */       this.cm = new IndexColorModel(this.r.length == 256 ? 8 : 1, this.r.length, this.r, this.g, this.b);
/*      */     }
/* 1712 */     this.icmtime = System.currentTimeMillis();
/*      */   }
/*      */ }


/* Location:              C:\Users\Joey\Downloads\launch.jar!\com\emt\proteus\xserver\display\Colormap.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */