/*     */ package com.emt.proteus.xserver.display;
/*     */ 
/*     */ import com.emt.proteus.xserver.client.XClient;
/*     */ import com.emt.proteus.xserver.io.ComChannel;
/*     */ import java.awt.Color;
/*     */ import java.awt.Graphics;
/*     */ import java.awt.Image;
/*     */ import java.awt.Rectangle;
/*     */ import java.awt.Shape;
/*     */ import java.awt.Toolkit;
/*     */ import java.awt.image.ColorModel;
/*     */ import java.awt.image.FilteredImageSource;
/*     */ import java.awt.image.MemoryImageSource;
/*     */ import java.awt.image.PixelGrabber;
/*     */ import java.io.IOException;
/*     */ import java.io.PrintStream;
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
/*     */ public class Pixmap
/*     */   extends Drawable
/*     */ {
/*     */   ColorModel filter;
/*     */   
/*     */   public static void init(Screen[] screen)
/*     */   {
/*  36 */     ImageFactory.init(screen[0]);
/*  37 */     screen[0].pixmaps = new Pixmap[Format.format.length];
/*  38 */     for (int i = 0; i < Format.format.length; i++) {
/*  39 */       screen[0].pixmaps[i] = ResizablePixmap.createPixmap(fakeClientId(XClient.X_CLIENTs[0]), screen[0].root, 1, 1, Format.format[i].depth);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  47 */   long time = 0L;
/*     */   
/*  49 */   int fg = 1;
/*  50 */   int bg = 0;
/*     */   
/*     */   byte lpad;
/*     */   
/*     */   Colormap colormap;
/*  55 */   boolean dirty = true;
/*     */   
/*  57 */   MemoryImageSource mis = null;
/*  58 */   Drawable drawable = null;
/*     */   int refcnt;
/*     */   int datasize;
/*  61 */   Image img = null;
/*  62 */   Graphics imgg = null;
/*  63 */   byte[] data = null;
/*     */   
/*  65 */   boolean imageDirty = false;
/*     */   int imageDirtyX;
/*     */   int imageDirtyY;
/*     */   
/*     */   public Pixmap(int id, Drawable d, int width, int height, byte depth) {
/*  70 */     super(id, -1073741822);
/*     */     
/*  72 */     this.type = 1;
/*  73 */     this.clss = 0;
/*  74 */     this.screen = d.screen;
/*  75 */     this.depth = depth;
/*  76 */     this.bitsPerPixel = depth;
/*  77 */     this.drawable = d;
/*  78 */     this.x = 0;this.y = 0;
/*  79 */     this.width = width;this.height = height;
/*  80 */     this.refcnt = 0;
/*     */     
/*  82 */     this.img = ImageFactory.createImage(width, height);
/*  83 */     this.img.flush();
/*  84 */     this.imgg = this.img.getGraphics();
/*  85 */     this.colormap = d.getColormap();
/*  86 */     this.filter = this.colormap.cm;
/*  87 */     this.time = System.currentTimeMillis(); }
/*     */   
/*     */   int imageDirtyWidth;
/*     */   int imageDirtyHeight;
/*  91 */   public void ref() { this.refcnt += 1; }
/*     */   
/*     */ 
/*     */   public void mkMIS() {
/*  95 */     this.mis = new MemoryImageSource(this.width, this.height, this.colormap.cm, this.data, 0, this.width);
/*  96 */     this.mis.setAnimated(true);
/*     */   }
/*     */   
/*     */   public byte[] getData() {
/* 100 */     if (this.data == null) {
/* 101 */       this.data = new byte[this.width * this.height];
/* 102 */       mkMIS();
/*     */     }
/* 104 */     return this.data;
/*     */   }
/*     */   
/*     */   public Image getImage() {
/* 108 */     return this.img;
/*     */   }
/*     */   
/*     */   public Image getImage(XWindow win) {
/* 112 */     return getImage(win, this.fg, this.bg);
/*     */   }
/*     */   
/*     */   public Image getImage(XWindow win, GC gc) {
/* 116 */     return getImage(win, gc.fgPixel, gc.bgPixel);
/*     */   }
/*     */   
/*     */   public Image getImage(XWindow win, int fgPixel, int bgPixel) {
/* 120 */     if (this.img == null) return null;
/* 121 */     if (win == null) return getImage();
/* 122 */     if ((this.imageDirty) && (this.colormap != win.getColormap())) {
/* 123 */       this.data = getData();
/*     */     }
/* 125 */     if (this.data != null) {
/* 126 */       if ((this.depth != 1) && (this.filter != win.getColormap().cm)) {
/* 127 */         if (this.imageDirty) {
/* 128 */           image2data();
/*     */         }
/* 130 */         this.filter = win.getColormap().cm;
/* 131 */         this.dirty = true;
/*     */       }
/*     */       
/*     */ 
/* 135 */       if (this.time < win.getColormap().icmtime) {
/* 136 */         this.dirty = true;
/*     */       }
/*     */       
/* 139 */       if (this.dirty) {
/* 140 */         this.dirty = false;
/* 141 */         mkMIS();
/* 142 */         Image dataImg = Toolkit.getDefaultToolkit().createImage(this.mis);
/* 143 */         this.time = System.currentTimeMillis();
/* 144 */         this.imgg.drawImage(dataImg, 0, 0, Screen.screen[0].root.ddxwindow);
/*     */         
/* 146 */         dataImg.flush();
/*     */       }
/*     */     }
/* 149 */     return getImage();
/*     */   }
/*     */   
/*     */   public Image getImage(XWindow win, GC gc, int x, int y, int w, int h) {
/* 153 */     getImage(win, gc);
/* 154 */     return getImage(gc, x, y, w, h);
/*     */   }
/*     */   
/*     */   public Image getImage(GC gc, int x, int y, int w, int h) {
/* 158 */     if ((this.data != null) && (this.time < this.colormap.icmtime)) {
/* 159 */       mkMIS();
/* 160 */       Image dataImg = Toolkit.getDefaultToolkit().createImage(this.mis);
/* 161 */       this.time = System.currentTimeMillis();
/* 162 */       this.imgg.drawImage(dataImg, 0, 0, Screen.screen[0].root.ddxwindow);
/*     */       
/* 164 */       dataImg.flush();
/*     */     }
/*     */     
/* 167 */     Image i = getImage();
/* 168 */     if ((gc != null) && (gc.clip_mask != null) && ((gc.clip_mask instanceof ClipPixmap))) {
/* 169 */       TransparentFilter tf = new TransparentFilter(0, 0, (Pixmap)gc.clip_mask.getMask());
/* 170 */       i = Toolkit.getDefaultToolkit().createImage(new FilteredImageSource(i.getSource(), tf));
/*     */     }
/*     */     
/* 173 */     return i;
/*     */   }
/*     */   
/*     */   public void draw(int x, int y, int w, int h) {}
/*     */   
/*     */   public void image2data()
/*     */   {
/* 180 */     image2data(this.imageDirtyX, this.imageDirtyY, this.imageDirtyWidth, this.imageDirtyHeight);
/* 181 */     this.imageDirty = false; }
/*     */   
/* 183 */   public int[] pixels = new int[10];
/*     */   
/* 185 */   public void image2data(int x, int y, int w, int h) { if (this.pixels.length < w * h) {
/* 186 */       this.pixels = new int[w * h];
/*     */     }
/* 188 */     PixelGrabber pg = new PixelGrabber(this.img, x, y, w, h, this.pixels, 0, w);
/*     */     try {
/* 190 */       pg.grabPixels();
/*     */     } catch (InterruptedException e) {
/* 192 */       System.err.println("interrupted waiting for pixels!");
/* 193 */       return;
/*     */     }
/* 195 */     if ((pg.getStatus() & 0x80) != 0) {
/* 196 */       System.err.println("image fetch aborted or errored");
/* 197 */       return;
/*     */     }
/* 199 */     byte[] dt = getData();
/* 200 */     for (int i = 0; i < h; i++) {
/* 201 */       for (int j = 0; j < w; j++) {
/* 202 */         dt[((y + i) * this.width + x + j)] = ((byte)this.colormap.rgb2pixel(this.pixels[(i * w + j)]));
/*     */       }
/*     */     }
/* 205 */     this.time = 0L;
/*     */   }
/*     */   
/*     */   public void reset() {}
/*     */   
/*     */   public static void reqFreePixmap(XClient c) throws IOException
/*     */   {
/* 212 */     ComChannel comChannel = c.channel;
/* 213 */     int foo = c.length;
/* 214 */     foo = comChannel.readInt();
/* 215 */     Resource o = lookupIDByType(foo, -1073741822);
/* 216 */     c.length -= 2;
/* 217 */     if ((o == null) || (!(o instanceof Pixmap))) {
/* 218 */       c.errorValue = foo;
/* 219 */       c.errorReason = 4;
/* 220 */       return;
/*     */     }
/* 222 */     freeResource(foo, 0);
/*     */   }
/*     */   
/*     */   public static void reqGetImage(XClient c) throws IOException
/*     */   {
/* 227 */     ComChannel comChannel = c.channel;
/*     */     
/* 229 */     int format = c.data;
/* 230 */     int foo = comChannel.readInt();
/* 231 */     Drawable d = c.lookupDrawable(foo);
/* 232 */     c.length -= 2;
/* 233 */     if (d == null) {
/* 234 */       c.errorValue = foo;
/* 235 */       c.errorReason = 9;
/* 236 */       return;
/*     */     }
/*     */     
/*     */ 
/* 240 */     int x = (short)comChannel.readShort();
/* 241 */     int y = (short)comChannel.readShort();
/* 242 */     int width = (short)comChannel.readShort();
/* 243 */     int height = (short)comChannel.readShort();
/* 244 */     foo = comChannel.readInt();
/* 245 */     c.length = 0;
/* 246 */     Image img = null;
/* 247 */     Colormap colormap = d.getColormap();
/* 248 */     img = d.getImage(null, x, y, width, height);
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
/* 259 */     int[] pixels = new int[width * height];
/* 260 */     PixelGrabber pg = new PixelGrabber(img, x, y, width, height, pixels, 0, width);
/*     */     int i;
/*     */     try
/*     */     {
/* 264 */       pg.grabPixels();
/*     */     }
/*     */     catch (InterruptedException e)
/*     */     {
/* 268 */       i = 0; } for (; i < pixels.length; i++) { pixels[i] = 0;
/*     */     }
/* 270 */     if ((pg.getStatus() & 0x80) != 0) {
/* 271 */       System.err.println("image fetch aborted or errored");
/* 272 */       for (int i = 0; i < pixels.length; i++) { pixels[i] = 0;
/*     */       }
/*     */     }
/* 275 */     if ((d instanceof XWindow)) {
/* 276 */       if (((XWindow)d != ((XWindow)d).screen.root) && (img != ((XWindow)d).getImage()))
/*     */       {
/* 278 */         img.flush();
/*     */       }
/*     */       
/*     */     }
/* 282 */     else if (img != ((Pixmap)d).getImage()) {
/* 283 */       img.flush();
/*     */     }
/*     */     
/*     */ 
/*     */     int i;
/*     */     
/* 289 */     if (d.depth == 1) {
/* 290 */       int www = width % 32 / 8;
/* 291 */       int wwww = width % 32 % 8;
/*     */       
/* 293 */       synchronized (comChannel) {
/* 294 */         comChannel.writeByte((byte)1);
/* 295 */         comChannel.writeByte((byte)1);
/* 296 */         comChannel.writeShort(c.getSequence());
/* 297 */         comChannel.writeInt((width + 31) / 32 * height);
/* 298 */         comChannel.writeInt(0);
/* 299 */         comChannel.writePad(20);
/* 300 */         i = 0;
/* 301 */         if (format == 1) {
/* 302 */           for (int hh = 0; hh < height; hh++) {
/* 303 */             int ww = width;
/*     */             for (;;) {
/* 305 */               foo = 0;
/* 306 */               if (32 >= ww) break;
/* 307 */               for (int ii = 0; ii < 4; ii++) {
/* 308 */                 foo = 0;
/* 309 */                 i += 8;
/* 310 */                 for (int iii = 0; iii < 8; iii++) {
/* 311 */                   i--;
/* 312 */                   foo = foo << 1 | ((pixels[i] & 0xFFFFFF) != 0 ? 1 : 0);
/*     */                 }
/* 314 */                 i += 8;
/* 315 */                 comChannel.writeByte((byte)(foo & 0xFF));
/*     */               }
/* 317 */               ww -= 32;
/*     */             }
/*     */             
/* 320 */             if (ww != 0) {
/* 321 */               for (int ii = 0; ii < www; ii++) {
/* 322 */                 foo = 0;
/* 323 */                 i += 8;
/* 324 */                 for (int iii = 0; iii < 8; iii++) {
/* 325 */                   i--;
/* 326 */                   foo = foo << 1 | ((pixels[i] & 0xFFFFFF) != 0 ? 1 : 0);
/*     */                 }
/* 328 */                 i += 8;
/* 329 */                 comChannel.writeByte((byte)(foo & 0xFF));
/*     */               }
/* 331 */               if (wwww != 0) {
/* 332 */                 foo = 0;
/* 333 */                 i += wwww;
/* 334 */                 for (int iii = 0; iii < wwww; iii++) {
/* 335 */                   i--;
/* 336 */                   foo = foo << 1 | ((pixels[i] & 0xFFFFFF) != 0 ? 1 : 0);
/*     */                 }
/* 338 */                 i += wwww;
/* 339 */                 comChannel.writeByte((byte)foo);
/* 340 */                 for (int ii = www + 1; ii < 4; ii++) {
/* 341 */                   comChannel.writeByte((byte)0);
/*     */                 }
/*     */               }
/*     */               else {
/* 345 */                 for (int ii = www; ii < 4; ii++) {
/* 346 */                   comChannel.writeByte((byte)0);
/*     */                 }
/*     */                 
/*     */               }
/*     */               
/*     */             }
/*     */             
/*     */           }
/*     */           
/*     */         } else {
/* 356 */           for (int hh = 0; hh < height; hh++) {
/* 357 */             int ww = width;
/*     */             for (;;) {
/* 359 */               foo = 0;
/* 360 */               if (32 >= ww) break;
/* 361 */               for (int ii = 0; ii < 32; ii++) {
/* 362 */                 foo = foo << 1 | ((pixels[i] & 0xFFFFFF) != 0 ? 1 : 0);
/* 363 */                 i++;
/* 364 */                 if ((ii == 7) || (ii == 15) || (ii == 23) || (ii == 31)) {
/* 365 */                   comChannel.writeByte((byte)bi_reverse(foo));
/* 366 */                   foo = 0;
/*     */                 }
/*     */               }
/* 369 */               ww -= 32;
/*     */             }
/*     */             
/* 372 */             if (ww != 0) {
/* 373 */               for (int ii = 0; ii < ww; ii++) {
/* 374 */                 foo = foo << 1 | ((pixels[i] & 0xFFFFFF) != 0 ? 1 : 0);
/* 375 */                 i++;
/* 376 */                 if ((ii == 7) || (ii == 15) || (ii == 23) || (ii == 31)) {
/* 377 */                   comChannel.writeByte((byte)bi_reverse(foo));
/* 378 */                   foo = 0;
/*     */                 }
/*     */               }
/* 381 */               for (int ii = ww; ii < 32; ii++) {
/* 382 */                 foo = foo << 1 | 0x0;
/* 383 */                 if ((ii == 7) || (ii == 15) || (ii == 23) || (ii == 31)) {
/* 384 */                   comChannel.writeByte((byte)bi_reverse(foo));
/* 385 */                   foo = 0;
/*     */                 }
/*     */               }
/*     */             }
/*     */           }
/*     */         }
/*     */         
/*     */ 
/* 393 */         comChannel.flush(); return;
/*     */       }
/*     */     }
/*     */     int n;
/* 397 */     if (d.depth == 8) {
/* 398 */       if (format == 1)
/*     */       {
/* 400 */         synchronized (comChannel) {
/* 401 */           comChannel.writeByte((byte)1);
/* 402 */           comChannel.writeByte(d.depth);
/* 403 */           comChannel.writeShort(c.getSequence());
/* 404 */           n = (width + 3) / 4;
/* 405 */           comChannel.writeInt(n * height);
/* 406 */           comChannel.writeInt(0);
/* 407 */           comChannel.writePad(20);
/*     */           
/* 409 */           i = 0;
/* 410 */           for (int hh = 0; hh < height; hh++) {
/* 411 */             int ww = width;
/*     */             for (;;) {
/* 413 */               foo = 0;
/* 414 */               if (4 >= ww) break;
/* 415 */               for (int ii = 0; ii < 4; ii++) {
/* 416 */                 comChannel.writeByte(colormap.rgb2pixel(pixels[i]) & 0xFF);
/* 417 */                 i++;
/*     */               }
/* 419 */               ww -= 4;
/*     */             }
/*     */             
/* 422 */             if (ww != 0) {
/* 423 */               for (int ii = 0; ii < ww; ii++) {
/* 424 */                 comChannel.writeByte(colormap.rgb2pixel(pixels[i]) & 0xFF);
/* 425 */                 i++;
/*     */               }
/* 427 */               ww = 4 - ww;
/* 428 */               while (ww != 0) {
/* 429 */                 comChannel.writeByte(0);
/* 430 */                 ww--;
/*     */               }
/*     */             }
/*     */           }
/*     */           
/*     */ 
/* 436 */           comChannel.flush();
/* 437 */           return;
/*     */         }
/*     */       }
/*     */       
/*     */ 
/* 442 */       synchronized (comChannel) {
/* 443 */         comChannel.writeByte((byte)1);
/* 444 */         comChannel.writeByte(d.depth);
/* 445 */         comChannel.writeShort(c.getSequence());
/* 446 */         n = (width + 3) / 4;
/* 447 */         comChannel.writeInt(n * height);
/* 448 */         comChannel.writeInt(0);
/* 449 */         comChannel.writePad(20);
/*     */         
/* 451 */         i = 0;
/* 452 */         for (int hh = 0; hh < height; hh++) {
/* 453 */           int ww = width;
/*     */           for (;;) {
/* 455 */             foo = 0;
/* 456 */             if (4 >= ww) break;
/* 457 */             for (int ii = 0; ii < 4; ii++) {
/* 458 */               comChannel.writeByte(colormap.rgb2pixel(pixels[i]) & 0xFF);
/* 459 */               i++;
/*     */             }
/* 461 */             ww -= 4;
/*     */           }
/*     */           
/* 464 */           if (ww != 0) {
/* 465 */             for (int ii = 0; ii < ww; ii++) {
/* 466 */               comChannel.writeByte(colormap.rgb2pixel(pixels[i]) & 0xFF);
/* 467 */               i++;
/*     */             }
/* 469 */             ww = 4 - ww;
/* 470 */             while (ww != 0) {
/* 471 */               comChannel.writeByte(0);
/* 472 */               ww--;
/*     */             }
/*     */           }
/*     */         }
/*     */         
/*     */ 
/* 478 */         comChannel.flush();
/* 479 */         return;
/*     */       }
/*     */     }
/*     */     
/* 483 */     if ((d.depth == 16) && 
/* 484 */       (format == 2)) {
/* 485 */       synchronized (comChannel) {
/* 486 */         comChannel.writeByte((byte)1);
/* 487 */         comChannel.writeByte(d.depth);
/* 488 */         comChannel.writeShort(c.getSequence());
/* 489 */         n = (width / 2 + width % 2) * 4;
/* 490 */         comChannel.writeInt(n * height / 4);
/* 491 */         comChannel.writeInt(0);
/* 492 */         comChannel.writePad(20);
/* 493 */         i = 0;
/*     */         
/* 495 */         for (int hh = 0; hh < height; hh++) {
/* 496 */           for (int ii = 0; ii < width; ii++) {
/* 497 */             int iii = pixels[i];
/* 498 */             iii = (iii >> 16 & 0xFF) / 8 << 11 | (iii >> 8 & 0xFF) / 4 << 5 | (iii & 0xFF) / 8;
/*     */             
/*     */ 
/* 501 */             comChannel.writeByte(iii & 0xFF);
/* 502 */             comChannel.writeByte(iii >> 8 & 0xFF);
/* 503 */             i++;
/*     */           }
/* 505 */           if (width % 2 != 0) comChannel.writePad(2);
/*     */         }
/* 507 */         comChannel.flush();
/* 508 */         return;
/*     */       }
/*     */     }
/*     */     
/*     */ 
/* 513 */     synchronized (comChannel) {
/* 514 */       comChannel.writeByte((byte)0);
/* 515 */       comChannel.writeByte((byte)17);
/* 516 */       comChannel.writeShort(c.getSequence());
/* 517 */       comChannel.writePad(4);
/* 518 */       comChannel.writeShort(0);
/* 519 */       comChannel.writeByte((byte)73);
/* 520 */       comChannel.writePad(21);
/* 521 */       comChannel.flush();
/*     */     }
/*     */   }
/*     */   
/*     */   public static void reqPutImage(XClient c) throws IOException
/*     */   {
/* 527 */     ComChannel comChannel = c.channel;
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 532 */     Pixmap pixmap = null;
/* 533 */     byte format = (byte)c.data;
/* 534 */     int n = c.length;
/* 535 */     int foo = comChannel.readInt();
/* 536 */     Drawable d = c.lookupDrawable(foo);
/* 537 */     if (d == null) {
/* 538 */       c.errorValue = foo;
/* 539 */       c.errorReason = 9;
/*     */     }
/* 541 */     foo = comChannel.readInt();
/* 542 */     GC gc = c.lookupGC(foo);
/* 543 */     if ((gc == null) && (c.errorReason == 0)) {
/* 544 */       c.errorValue = foo;
/* 545 */       c.errorReason = 13;
/*     */     }
/* 547 */     short width = (short)comChannel.readShort();
/* 548 */     short height = (short)comChannel.readShort();
/* 549 */     short dstx = (short)comChannel.readShort();
/* 550 */     short dsty = (short)comChannel.readShort();
/* 551 */     byte lpad = (byte)comChannel.readByte();
/* 552 */     byte depth = (byte)comChannel.readByte();
/* 553 */     comChannel.readPad(2);
/* 554 */     c.length -= 6;
/* 555 */     n -= 6;
/* 556 */     if (c.errorReason != 0) {
/* 557 */       return;
/*     */     }
/* 559 */     if (dsty < 0)
/*     */     {
/* 561 */       dsty = 0;
/*     */     }
/* 563 */     if (dstx < 0)
/*     */     {
/* 565 */       dstx = 0;
/*     */     }
/*     */     
/* 568 */     int ddstx = dstx;
/* 569 */     int ddsty = dsty;
/*     */     
/* 571 */     synchronized (Pixmap.class) {
/* 572 */       if ((d instanceof Pixmap)) {
/* 573 */         pixmap = (Pixmap)d;
/* 574 */         if (pixmap.imageDirty) {
/* 575 */           pixmap.image2data();
/*     */         }
/*     */       }
/*     */       else {
/* 579 */         if (!((XWindow)d).ddxwindow.isVisible()) {
/* 580 */           comChannel.readPad(n * 4);
/* 581 */           return;
/*     */         }
/* 583 */         pixmap = null;
/* 584 */         Pixmap[] pixmaps = ((XWindow)d).screen.pixmaps;
/* 585 */         for (int i = 0; i < pixmaps.length; i++) {
/* 586 */           if (pixmaps[i].depth == d.depth) {
/* 587 */             pixmap = pixmaps[i];
/* 588 */             break;
/*     */           }
/*     */         }
/* 591 */         if (pixmap == null) {}
/*     */         
/* 593 */         ((Resizable)pixmap).setSize(width, height);
/* 594 */         ((Resizable)pixmap).setColormap(d.getColormap());
/* 595 */         pixmap.lpad = lpad;
/* 596 */         dstx = 0;
/* 597 */         dsty = 0;
/*     */       }
/*     */       
/* 600 */       byte[] data = null;
/*     */       
/* 602 */       data = pixmap.getData();
/*     */       
/* 604 */       int ww = 0;
/* 605 */       int j = 0;
/* 606 */       int i = 0;
/*     */       
/* 608 */       j = dsty;
/*     */       
/* 610 */       if ((depth == 1) && ((pixmap.depth == 1) || (pixmap.depth == 8))) {
/* 611 */         int www = 0;
/* 612 */         if ((d instanceof XWindow)) www = ((Resizable)pixmap).getRealWidth(); else {
/* 613 */           www = pixmap.width;
/*     */         }
/* 615 */         if (XDisplay.imageByteOrder == 1) {
/* 616 */           Pixmap1.putData(c, data, www, dstx, dsty, width, lpad);
/*     */         }
/*     */         else {
/* 619 */           Pixmap1.putData_LSB(c, data, www, dstx, dsty, width, lpad);
/*     */         }
/*     */         
/* 622 */         if (d.depth != 1) {
/* 623 */           byte f = (byte)gc.fgPixel;
/* 624 */           byte b = (byte)gc.bgPixel;
/* 625 */           for (i = dsty; i < height + dsty; i++) {
/* 626 */             for (j = dstx; j < width + dstx; j++) {
/* 627 */               if (data[(i * www + j)] == 0) {
/* 628 */                 data[(i * www + j)] = b;
/*     */               }
/*     */               else {
/* 631 */                 data[(i * www + j)] = f;
/*     */               }
/*     */               
/*     */             }
/*     */           }
/*     */         }
/*     */       }
/* 638 */       else if (depth == 8) {
/* 639 */         if (format == 2) {
/* 640 */           int restw = width;
/* 641 */           int restww = width;
/* 642 */           int resth = height;
/* 643 */           if (d.width < dstx + width) {
/* 644 */             restw = (short)(d.width - dstx);
/*     */           }
/* 646 */           if (d.height < dsty + height) {
/* 647 */             resth = (short)(d.height - dsty);
/*     */           }
/*     */           
/* 650 */           int www = 0;
/* 651 */           if ((d instanceof XWindow)) www = ((Resizable)pixmap).getRealWidth(); else
/* 652 */             www = pixmap.width;
/* 653 */           j *= www;
/*     */           
/* 655 */           if (width < c.bbuffer.length) {
/* 656 */             int paddedwidthi = (width + 3) / 4;
/* 657 */             int paddedwidth = paddedwidthi * 4;
/* 658 */             int padding = paddedwidth - restw;
/* 659 */             while (n != 0) {
/* 660 */               if (resth > 0) {
/* 661 */                 comChannel.readByte(data, j + dstx, restw);
/* 662 */                 if (padding != 0) {
/* 663 */                   comChannel.readPad(padding);
/*     */                 }
/*     */               }
/*     */               else {
/* 667 */                 comChannel.readPad(paddedwidth);
/*     */               }
/* 669 */               n -= paddedwidthi;
/* 670 */               j += www;
/* 671 */               resth--;
/*     */             }
/*     */           }
/*     */           else {
/* 675 */             while (n != 0) {
/* 676 */               restww = restw;
/* 677 */               ww = width;
/* 678 */               i = dstx;
/* 679 */               while (0 < ww) {
/* 680 */                 comChannel.readByte(c.bbuffer, 0, 4);n--;
/* 681 */                 if (4 <= ww) {
/* 682 */                   if (resth > 0) {
/* 683 */                     if (restww >= 4) {
/* 684 */                       System.arraycopy(c.bbuffer, 0, data, j + i, 4);
/* 685 */                       restww -= 4;
/*     */                     }
/* 687 */                     else if (restww > 0) {
/* 688 */                       System.arraycopy(c.bbuffer, 0, data, j + i, restww);
/* 689 */                       restww = 0;
/*     */                     }
/*     */                   }
/* 692 */                   i += 4;ww -= 4;
/*     */                 }
/*     */                 else {
/* 695 */                   if (resth > 0) {
/* 696 */                     if (restww >= ww) {
/* 697 */                       System.arraycopy(c.bbuffer, 0, data, j + i, ww);
/* 698 */                       restww -= ww;
/*     */                     }
/* 700 */                     else if (restww > 0) {
/* 701 */                       System.arraycopy(c.bbuffer, 0, data, j + i, restww);
/* 702 */                       restww = 0;
/*     */                     }
/*     */                   }
/* 705 */                   i += ww;
/*     */                 }
/*     */               }
/*     */               
/* 709 */               j += www;
/* 710 */               resth--;
/*     */             }
/*     */           }
/*     */         }
/*     */         else {
/* 715 */           int www = 0;
/* 716 */           if ((d instanceof XWindow)) www = ((Resizable)pixmap).getRealWidth(); else
/* 717 */             www = pixmap.width;
/* 718 */           n *= 4;
/* 719 */           while (n != 0) {
/* 720 */             ww = width;
/* 721 */             i = dstx;
/* 722 */             while (4 < ww) {
/* 723 */               foo = comChannel.readInt();n -= 4;
/* 724 */               data[(j * www + i)] = ((byte)(foo & 0xFF));i++;
/* 725 */               foo >>= 8;
/* 726 */               data[(j * www + i)] = ((byte)(foo & 0xFF));i++;
/* 727 */               foo >>= 8;
/* 728 */               data[(j * www + i)] = ((byte)(foo & 0xFF));i++;
/* 729 */               foo >>= 8;
/* 730 */               data[(j * www + i)] = ((byte)(foo & 0xFF));i++;
/* 731 */               ww -= 4;
/*     */             }
/* 733 */             if (ww != 0) {
/* 734 */               foo = comChannel.readInt();n -= 4;
/* 735 */               while (0 < ww) {
/* 736 */                 data[(j * www + i)] = ((byte)(foo & 0xFF));i++;
/* 737 */                 foo >>= 8;
/* 738 */                 ww--;
/*     */               }
/*     */             }
/* 741 */             j++;
/*     */           }
/*     */         }
/*     */       }
/* 745 */       else if (pixmap.depth == 16) {
/* 746 */         ((Pixmap16)pixmap).putImage(c, gc, dstx, dsty, width, height, lpad, format, depth);
/*     */       }
/*     */       else
/*     */       {
/* 750 */         comChannel.readPad(n * 4);
/*     */       }
/*     */       
/* 753 */       if ((d instanceof XWindow)) {
/* 754 */         Graphics g = ((XWindow)d).getGraphics();
/* 755 */         pixmap.mis.newPixels(dstx, dsty, width, height);
/* 756 */         Image dataImg = Toolkit.getDefaultToolkit().createImage(pixmap.mis);
/* 757 */         Image ii = dataImg;
/*     */         
/* 759 */         Shape tmp = g.getClip();
/* 760 */         g.clipRect(ddstx, ddsty, width, height);
/* 761 */         g.drawImage(ii, ddstx, ddsty, Screen.screen[0].root.ddxwindow);
/* 762 */         if (tmp == null) g.setClip(0, 0, d.width, d.height); else
/* 763 */           g.setClip(tmp);
/* 764 */         ((XWindow)d).draw(ddstx, ddsty, width, height);
/* 765 */         dataImg.flush();
/*     */       }
/*     */       else {
/* 768 */         if (pixmap.time < pixmap.colormap.icmtime) {
/* 769 */           pixmap.mkMIS();
/*     */         }
/* 771 */         pixmap.mis.newPixels(dstx, dsty, width, height);
/* 772 */         Image dataImg = Toolkit.getDefaultToolkit().createImage(pixmap.mis);
/* 773 */         if ((dstx != 0) || (dsty != 0) || (width != d.width) || (height != d.height)) {
/* 774 */           Shape tmp = pixmap.imgg.getClip();
/*     */           
/* 776 */           pixmap.imgg.clipRect(dstx, dsty, width, height);
/* 777 */           pixmap.imgg.drawImage(dataImg, 0, 0, Screen.screen[0].root.ddxwindow);
/*     */           
/* 779 */           if (tmp == null) pixmap.imgg.setClip(0, 0, d.width, d.height); else {
/* 780 */             pixmap.imgg.setClip(tmp);
/*     */           }
/*     */         } else {
/* 783 */           pixmap.imgg.drawImage(dataImg, 0, 0, Screen.screen[0].root.ddxwindow);
/*     */         }
/*     */         
/* 786 */         dataImg.flush();
/* 787 */         pixmap.reset();
/* 788 */         pixmap.time = System.currentTimeMillis();
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public static void reqCreatePixmap(XClient c)
/*     */     throws IOException
/*     */   {
/* 798 */     ComChannel comChannel = c.channel;
/* 799 */     byte depth = (byte)c.data;
/* 800 */     int pid = comChannel.readInt();
/* 801 */     int foo = comChannel.readInt();
/* 802 */     c.length -= 3;
/*     */     
/* 804 */     Drawable d = c.lookupDrawable(foo);
/* 805 */     if (d == null) {
/* 806 */       c.errorValue = foo;
/* 807 */       c.errorReason = 9;
/* 808 */       return;
/*     */     }
/*     */     
/* 811 */     short width = (short)comChannel.readShort();
/* 812 */     short height = (short)comChannel.readShort();
/*     */     
/* 814 */     c.length -= 1;
/*     */     
/* 816 */     if ((width == 0) || (height == 0)) {
/* 817 */       c.errorValue = 0;
/* 818 */       c.errorReason = 4;
/* 819 */       return;
/*     */     }
/*     */     
/* 822 */     if (depth != 1) {
/* 823 */       Depth[] dd = d.screen.depth;
/* 824 */       int i = 0;
/* 825 */       for (i = 0; i < dd.length; i++)
/* 826 */         if (dd[i].depth == depth)
/*     */           break;
/* 828 */       if (i == dd.length) {
/* 829 */         c.errorValue = depth;
/* 830 */         c.errorReason = 8;
/* 831 */         return;
/*     */       }
/*     */     }
/* 834 */     createPixmap(pid, d, width, height, depth);
/*     */   }
/*     */   
/*     */   static Pixmap createPixmap(int id, Drawable d, int width, int height, byte depth)
/*     */   {
/* 839 */     Pixmap p = null;
/* 840 */     if (depth == 1) { p = new Pixmap1(id, d, width, height);
/* 841 */     } else if (depth == 16) p = new Pixmap16(id, d, width, height); else
/* 842 */       p = new Pixmap(id, d, width, height, depth);
/* 843 */     add(p);
/* 844 */     return p;
/*     */   }
/*     */   
/*     */   static int bi_reverse(int code) {
/* 848 */     int len = 8;
/* 849 */     int res = 0;
/*     */     do {
/* 851 */       res |= code & 0x1;
/* 852 */       code >>>= 1;
/* 853 */       res <<= 1;
/*     */       
/* 855 */       len--; } while (len > 0);
/* 856 */     return res >>> 1;
/*     */   }
/*     */   
/*     */   Graphics getGraphics() {
/* 860 */     return this.imgg;
/*     */   }
/*     */   
/*     */   Graphics getGraphics(GC gc, int mask) {
/* 864 */     Graphics graphics = this.imgg;
/* 865 */     if ((gc == this.currentGC) && (gc.time == this.gctime) && ((mask & (this.gmask ^ 0xFFFFFFFF)) == 0))
/*     */     {
/*     */ 
/* 868 */       return graphics;
/*     */     }
/* 870 */     this.gctime = gc.time;
/* 871 */     this.currentGC = gc;
/* 872 */     this.gmask = mask;
/* 873 */     if ((gc.clip_mask != null) && ((gc.clip_mask instanceof ClipRectangles))) {
/* 874 */       Rectangle rec = (Rectangle)gc.clip_mask.getMask();
/* 875 */       if (rec == null) {
/* 876 */         return graphics;
/*     */       }
/* 878 */       graphics.setClip(rec.x, rec.y, rec.width, rec.height);
/*     */     }
/* 880 */     if ((mask & 0x1) != 0) {
/* 881 */       Color color = getColormap().getColor(gc.fgPixel);
/* 882 */       if (gc.function == 6) {
/* 883 */         this.gmask &= 0xFFFFFFFE;
/* 884 */         graphics.setXORMode(new Color((color.getRGB() ^ graphics.getColor().getRGB()) & 0xFFFFFF));
/*     */       }
/* 886 */       else if (gc.function != 10)
/*     */       {
/*     */ 
/*     */ 
/* 890 */         graphics.setColor(color);
/*     */       }
/*     */     }
/* 893 */     if ((mask & 0x4000) != 0) {
/* 894 */       XFont XFont = gc.XFont;
/* 895 */       graphics.setFont(XFont.getFont());
/*     */     }
/* 897 */     if (((mask & 0x10) == 0) && ((mask & 0x20) == 0) && ((mask & 0x40) == 0) && ((mask & 0x80) != 0)) {}
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 902 */     return graphics;
/*     */   }
/*     */   
/*     */   void copyPlane(Pixmap dst, GC gc, int sx, int sy, int dx, int dy, int w, int h)
/*     */   {
/* 907 */     copyArea(dst, gc, sx, sy, dx, dy, w, h);
/*     */   }
/*     */   
/*     */   void copyArea(Pixmap dst, GC gc, int sx, int sy, int dx, int dy, int w, int h)
/*     */   {
/* 912 */     if (this.width - sx < w) w = this.width - sx;
/* 913 */     if (dst.width - dx < w) { w = dst.width - dx;
/*     */     }
/* 915 */     if (this.height - sy < h) h = this.height - sy;
/* 916 */     if (dst.height - dy < h) { h = dst.height - dy;
/*     */     }
/* 918 */     int s = sy * this.width + sx;
/* 919 */     int d = dy * dst.width + dx;
/*     */     
/* 921 */     Graphics g = dst.getGraphics();
/* 922 */     Image ii = getImage(gc, sx, sy, w, h);
/*     */     
/* 924 */     if ((sx == 0) && (sy == 0) && (w == this.width) && (h == this.height)) {
/* 925 */       g.drawImage(ii, dx, dy, this.width, this.height, Screen.screen[0].root.ddxwindow);
/*     */     }
/*     */     else
/*     */     {
/* 929 */       Shape tmp = g.getClip();
/* 930 */       g.setClip(dx, dy, w, h);
/* 931 */       g.drawImage(ii, dx - sx, dy - sy, this.width, this.height, Screen.screen[0].root.ddxwindow);
/*     */       
/*     */ 
/* 934 */       if (tmp == null) {
/* 935 */         g.setClip(0, 0, dst.width, dst.height);
/*     */       }
/*     */       else {
/* 938 */         g.setClip(tmp);
/*     */       }
/*     */     }
/* 941 */     dst.draw(dx, dy, w, h);
/*     */     
/* 943 */     if (ii != getImage()) {
/* 944 */       ii.flush();
/*     */     }
/*     */   }
/*     */   
/*     */   Colormap getColormap() {
/* 949 */     return this.colormap;
/*     */   }
/*     */   
/*     */   void delete() throws IOException {
/* 953 */     if (this.refcnt > 0) {
/* 954 */       this.refcnt -= 1;
/* 955 */       return;
/*     */     }
/* 957 */     if (this.img != null) {
/* 958 */       this.img.flush();
/* 959 */       this.img = null;
/* 960 */       this.imgg.dispose();
/* 961 */       this.imgg = null;
/*     */     }
/* 963 */     this.data = null;
/* 964 */     this.filter = null;
/* 965 */     this.colormap = null;
/* 966 */     this.drawable = null;
/*     */   }
/*     */   
/* 969 */   void restoreClip() { this.imgg.setClip(0, 0, this.width, this.height); }
/*     */ }


/* Location:              C:\Users\Joey\Downloads\launch.jar!\com\emt\proteus\xserver\display\Pixmap.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */