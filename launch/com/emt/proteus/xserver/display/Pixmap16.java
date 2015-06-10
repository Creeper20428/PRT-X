/*     */ package com.emt.proteus.xserver.display;
/*     */ 
/*     */ import com.emt.proteus.xserver.client.XClient;
/*     */ import com.emt.proteus.xserver.io.ComChannel;
/*     */ import java.awt.Graphics;
/*     */ import java.awt.Image;
/*     */ import java.awt.Toolkit;
/*     */ import java.awt.image.FilteredImageSource;
/*     */ import java.awt.image.MemoryImageSource;
/*     */ import java.awt.image.PixelGrabber;
/*     */ import java.io.IOException;
/*     */ import java.io.PrintStream;
/*     */ 
/*     */ class Pixmap16 extends Pixmap
/*     */ {
/*     */   int[] idata;
/*     */   
/*     */   Pixmap16(int id, Drawable d, int width, int height)
/*     */   {
/*  20 */     super(id, d, width, height, (byte)16);
/*  21 */     this.img = ImageFactory.createImage(width, height);
/*  22 */     this.img.flush();
/*  23 */     this.imgg = this.img.getGraphics();
/*     */   }
/*     */   
/*  26 */   int getScanWidth() { return this.width * 2; }
/*     */   
/*     */   public byte[] getData() {
/*  29 */     if (this.data == null) {
/*  30 */       this.data = new byte[this.width * this.height * 2];
/*  31 */       this.idata = new int[this.width * this.height];
/*  32 */       mkMIS();
/*     */     }
/*  34 */     return this.data;
/*     */   }
/*     */   
/*     */ 
/*  38 */   public Image getImage(XWindow win) { return getImage(win, null); }
/*     */   
/*     */   public Image getImage(XWindow win, GC gc) {
/*  41 */     if (this.img == null) return null;
/*  42 */     if (win == null) { return getImage();
/*     */     }
/*  44 */     if ((this.imageDirty) && (this.colormap != win.getColormap())) {
/*  45 */       this.data = getData();
/*     */     }
/*     */     
/*  48 */     if (this.data != null) {
/*  49 */       if (this.depth != 1) {
/*  50 */         if (this.imageDirty) {
/*  51 */           image2data();
/*     */         }
/*  53 */         this.dirty = true;
/*     */       }
/*     */       
/*     */ 
/*     */ 
/*  58 */       if (this.time < win.getColormap().icmtime) {
/*  59 */         this.dirty = true;
/*     */       }
/*     */       
/*  62 */       if (this.dirty) {
/*  63 */         this.dirty = false;
/*  64 */         mkMIS();
/*  65 */         Image dataImg = Toolkit.getDefaultToolkit().createImage(this.mis);
/*  66 */         this.time = System.currentTimeMillis();
/*  67 */         this.imgg.drawImage(dataImg, 0, 0, Screen.screen[0].root.ddxwindow);
/*     */         
/*  69 */         dataImg.flush();
/*     */       }
/*     */     }
/*  72 */     return getImage();
/*     */   }
/*     */   
/*     */   public Image getImage(XWindow win, GC gc, int x, int y, int w, int h) {
/*  76 */     getImage(win, gc);
/*  77 */     return getImage(gc, x, y, w, h);
/*     */   }
/*     */   
/*  80 */   public Image getImage(GC gc, int x, int y, int w, int h) { if ((this.data != null) && (this.time < this.colormap.icmtime)) {
/*  81 */       mkMIS();
/*  82 */       Image dataImg = Toolkit.getDefaultToolkit().createImage(this.mis);
/*  83 */       this.time = System.currentTimeMillis();
/*  84 */       this.imgg.drawImage(dataImg, 0, 0, Screen.screen[0].root.ddxwindow);
/*     */       
/*  86 */       dataImg.flush();
/*     */     }
/*     */     
/*  89 */     Image i = getImage();
/*     */     
/*  91 */     if ((gc != null) && (gc.clip_mask != null) && ((gc.clip_mask instanceof ClipPixmap))) {
/*  92 */       TransparentFilter tf = new TransparentFilter(0, 0, (Pixmap)gc.clip_mask.getMask());
/*  93 */       i = Toolkit.getDefaultToolkit().createImage(new FilteredImageSource(i.getSource(), tf));
/*     */     }
/*     */     
/*  96 */     return i;
/*     */   }
/*     */   
/*     */ 
/*     */   void putImage(XClient c, GC gc, int dstx, int dsty, int dwidth, int dheight, int lpad, int format, int depth)
/*     */     throws IOException
/*     */   {
/* 103 */     ComChannel comChannel = c.channel;
/* 104 */     int scanWidth = getScanWidth();
/* 105 */     byte[] d = getData();
/*     */     
/* 107 */     if (depth == 1) {
/* 108 */       int j = dsty * scanWidth;
/* 109 */       int n = c.length;
/*     */       
/* 111 */       int f = gc.fgPixel;
/*     */       
/*     */ 
/* 114 */       byte fhi = (byte)(f & 0xFF);
/* 115 */       byte flo = (byte)(f >> 8 & 0xFF);
/* 116 */       int b = gc.bgPixel;
/*     */       
/*     */ 
/* 119 */       byte bhi = (byte)(b & 0xFF);
/* 120 */       byte blo = (byte)(b >> 8 & 0xFF);
/*     */       
/* 122 */       if (n * 4 < c.cbuffer.length) {
/* 123 */         comChannel.readByte(c.bbuffer, 0, n * 4);
/* 124 */         int start = 0;
/* 125 */         while (n != 0) {
/* 126 */           int ww = dwidth;
/* 127 */           int i = dstx;
/* 128 */           if (lpad != 0) {
/* 129 */             n--;
/*     */             
/*     */ 
/* 132 */             int ii = lpad % 8;
/* 133 */             int iii = lpad / 8;
/* 134 */             if (ii != 0) {
/* 135 */               int foo = c.bbuffer[(start + iii)];
/* 136 */               foo >>= ii;
/* 137 */               for (; ii < 8; ii++) {
/* 138 */                 if ((foo & 0x1) != 0) { d[(j + i)] = fhi;d[(j + i + 1)] = flo;
/* 139 */                 } else { d[(j + i)] = bhi;d[(j + i + 1)] = blo; }
/* 140 */                 i += 2;
/* 141 */                 foo >>= 1;
/* 142 */                 ww--; if (ww == 0) break label382;
/*     */               }
/* 144 */               iii++;
/*     */             }
/* 146 */             for (; iii < 4; iii++) {
/* 147 */               int foo = c.bbuffer[(start + iii)];
/* 148 */               for (ii = 0; ii < 8; ii++) {
/* 149 */                 if ((foo & 0x1) != 0) { d[(j + i)] = fhi;d[(j + i + 1)] = flo;
/* 150 */                 } else { d[(j + i)] = bhi;d[(j + i + 1)] = blo; }
/* 151 */                 i += 2;
/* 152 */                 foo >>= 1;
/* 153 */                 ww--; if (ww == 0) break label382;
/*     */               }
/*     */             }
/*     */             label382:
/* 157 */             start += 4;
/*     */           }
/* 159 */           while (32 < ww) {
/* 160 */             n--;
/* 161 */             for (int iii = 0; iii < 4; iii++) {
/* 162 */               int foo = c.bbuffer[start];start++;
/* 163 */               for (int ii = 0; ii < 8; ii++) {
/* 164 */                 if ((foo & 0x1) != 0) { d[(j + i)] = fhi;d[(j + i + 1)] = flo;
/* 165 */                 } else { d[(j + i)] = bhi;d[(j + i + 1)] = blo; }
/* 166 */                 i += 2;
/* 167 */                 foo >>= 1;
/*     */               }
/*     */             }
/* 170 */             ww -= 32;
/*     */           }
/*     */           
/* 173 */           if (ww != 0) {
/* 174 */             n--;
/* 175 */             for (int iii = 0; iii < 4; iii++) {
/* 176 */               int foo = c.bbuffer[start];start++;
/* 177 */               for (int ii = 0; ii < 8; ii++) {
/* 178 */                 if (0 < ww) {
/* 179 */                   if ((foo & 0x1) != 0) { d[(j + i)] = fhi;d[(j + i + 1)] = flo;
/* 180 */                   } else { d[(j + i)] = bhi;d[(j + i + 1)] = blo; }
/* 181 */                   i += 2;
/* 182 */                   foo >>= 1;
/* 183 */                   ww--;
/*     */                 }
/*     */               }
/*     */             }
/*     */           }
/* 188 */           j += scanWidth;
/*     */         }
/*     */       }
/*     */       else {
/* 192 */         while (n != 0) {
/* 193 */           int ww = dwidth;
/* 194 */           int i = dstx;
/* 195 */           if (lpad != 0) {
/* 196 */             comChannel.readByte(c.bbuffer, 0, 4);n--;
/*     */             
/*     */ 
/* 199 */             int ii = lpad % 8;
/* 200 */             int iii = lpad / 8;
/* 201 */             if (ii != 0) {
/* 202 */               int foo = c.bbuffer[iii];
/* 203 */               foo >>= ii;
/* 204 */               for (; ii < 8; ii++) {
/* 205 */                 if ((foo & 0x1) != 0) { d[(j + i)] = fhi;d[(j + i + 1)] = flo;
/* 206 */                 } else { d[(j + i)] = bhi;d[(j + i + 1)] = blo; }
/* 207 */                 i += 2;
/* 208 */                 foo >>= 1;
/* 209 */                 ww--; if (ww == 0) break label910;
/*     */               }
/* 211 */               iii++;
/*     */             }
/* 213 */             for (; iii < 4; iii++) {
/* 214 */               int foo = c.bbuffer[iii];
/* 215 */               for (ii = 0; ii < 8; ii++) {
/* 216 */                 if ((foo & 0x1) != 0) { d[(j + i)] = fhi;d[(j + i + 1)] = flo;
/* 217 */                 } else { d[(j + i)] = bhi;d[(j + i + 1)] = blo; }
/* 218 */                 i += 2;
/* 219 */                 foo >>= 1;
/* 220 */                 ww--; if (ww == 0) break label910;
/*     */               }
/*     */             }
/*     */           }
/*     */           label910:
/* 225 */           while (32 < ww) {
/* 226 */             comChannel.readByte(c.bbuffer, 0, 4);n--;
/* 227 */             for (int iii = 0; iii < 4; iii++) {
/* 228 */               int foo = c.bbuffer[iii];
/* 229 */               for (int ii = 0; ii < 8; ii++) {
/* 230 */                 if ((foo & 0x1) != 0) { d[(j + i)] = fhi;d[(j + i + 1)] = flo;
/* 231 */                 } else { d[(j + i)] = bhi;d[(j + i + 1)] = blo; }
/* 232 */                 i += 2;
/* 233 */                 foo >>= 1;
/*     */               }
/*     */             }
/* 236 */             ww -= 32;
/*     */           }
/* 238 */           if (ww != 0) {
/* 239 */             comChannel.readByte(c.bbuffer, 0, 4);n--;
/* 240 */             for (int iii = 0; iii < 4; iii++) {
/* 241 */               int foo = c.bbuffer[iii];
/* 242 */               for (int ii = 0; ii < 8; ii++) {
/* 243 */                 if (0 < ww) {
/* 244 */                   if ((foo & 0x1) != 0) { d[(j + i)] = fhi;d[(j + i + 1)] = flo;
/* 245 */                   } else { d[(j + i)] = bhi;d[(j + i + 1)] = blo; }
/* 246 */                   i += 2;
/* 247 */                   foo >>= 1;
/* 248 */                   ww--;
/*     */                 }
/*     */               }
/*     */             }
/*     */           }
/* 253 */           j += scanWidth;
/*     */         }
/*     */       }
/*     */       
/* 257 */       int resth = dheight;
/* 258 */       if (this.height < dsty + dheight) resth = (short)(this.height - dsty);
/* 259 */       resth += dsty;
/* 260 */       int restw = dwidth;
/* 261 */       if (this.width < dstx + dwidth) restw = (short)(this.width - dstx);
/* 262 */       restw += dstx;
/*     */       
/* 264 */       int iii = scanWidth * (dsty - 1);
/* 265 */       int iiii = scanWidth / 2 * (dsty - 1);
/*     */       
/*     */ 
/* 268 */       for (int ii = dsty; ii < resth; ii++) {
/* 269 */         iii += scanWidth;
/* 270 */         iiii += scanWidth / 2;
/* 271 */         int jjj = (dstx - 1) * 2;
/* 272 */         for (int jj = dstx; jj < restw; jj++) {
/* 273 */           jjj += 2;
/* 274 */           int foo = d[(iii + jjj + 1)] << 8 & 0xFF00 | d[(iii + jjj)] & 0xFF;
/* 275 */           this.idata[(iiii + jj)] = (0xFF000000 | (foo >> 11 & 0x1F) * 8 << 16 | (foo >> 5 & 0x3F) * 4 << 8 | (foo & 0x1F) * 8);
/*     */         }
/*     */       }
/*     */       
/*     */ 
/*     */ 
/* 281 */       return;
/*     */     }
/*     */     
/* 284 */     if (depth == 16) {
/* 285 */       int ww = 0;
/* 286 */       int i = 0;
/* 287 */       int j = dsty;
/* 288 */       int n = c.length;
/*     */       
/*     */ 
/* 291 */       if (format == 2) {
/* 292 */         int restw = dwidth;
/* 293 */         int restww = dwidth;
/* 294 */         int resth = dheight;
/*     */         
/* 296 */         if (this.width < dstx + dwidth) restw = (short)(this.width - dstx);
/* 297 */         restw *= 2;
/*     */         
/* 299 */         if (this.height < dsty + dheight) { resth = (short)(this.height - dsty);
/*     */         }
/* 301 */         j *= scanWidth;
/*     */         
/* 303 */         while (n != 0) {
/* 304 */           restww = restw;
/* 305 */           ww = dwidth;
/* 306 */           i = dstx * 2;
/* 307 */           while (0 < ww) {
/* 308 */             comChannel.readByte(c.bbuffer, 0, 4);n--;
/* 309 */             if (resth > 0) {
/* 310 */               if (restww > 0) {
/* 311 */                 System.arraycopy(c.bbuffer, 0, d, j + i, 2);
/* 312 */                 restww -= 2;i += 2;
/*     */               }
/* 314 */               if (restww > 0) {
/* 315 */                 System.arraycopy(c.bbuffer, 2, d, j + i, 2);
/* 316 */                 restww -= 2;i += 2;
/*     */               }
/*     */             }
/* 319 */             ww -= 2;
/*     */           }
/* 321 */           j += scanWidth;
/* 322 */           resth--;
/*     */         }
/*     */         
/* 325 */         resth = dheight;
/* 326 */         if (this.height < dsty + resth) resth = (short)(this.height - dsty);
/* 327 */         resth += dsty;
/*     */         
/* 329 */         restw = dwidth;
/* 330 */         if (this.width < dstx + restw) restw = (short)(this.width - dstx);
/* 331 */         restw += dstx;
/*     */         
/* 333 */         int iii = scanWidth * (dsty - 1);
/* 334 */         int iiii = scanWidth / 2 * (dsty - 1);
/*     */         
/*     */ 
/* 337 */         for (int ii = dsty; ii < resth; ii++) {
/* 338 */           iii += scanWidth;
/* 339 */           iiii += scanWidth / 2;
/* 340 */           int jjj = (dstx - 1) * 2;
/* 341 */           for (int jj = dstx; jj < restw; jj++) {
/* 342 */             jjj += 2;
/* 343 */             int foo = d[(iii + jjj + 1)] << 8 & 0xFF00 | d[(iii + jjj)] & 0xFF;
/* 344 */             this.idata[(iiii + jj)] = (0xFF000000 | (foo >> 11 & 0x1F) * 8 << 16 | (foo >> 5 & 0x3F) * 4 << 8 | (foo & 0x1F) * 8);
/*     */           }
/*     */           
/*     */         }
/*     */         
/*     */       }
/*     */       else
/*     */       {
/* 352 */         n *= 4;
/* 353 */         j = dsty * scanWidth;
/* 354 */         while (n != 0) {
/* 355 */           ww = this.width;
/* 356 */           i = dstx;
/* 357 */           while (4 < ww) {
/* 358 */             int foo = comChannel.readInt();n -= 4;
/* 359 */             this.data[(j + i)] = ((byte)(foo & 0xFF));i++;
/* 360 */             foo >>= 8;
/* 361 */             this.data[(j + i)] = ((byte)(foo & 0xFF));i++;
/* 362 */             foo >>= 8;
/* 363 */             this.data[(j + i)] = ((byte)(foo & 0xFF));i++;
/* 364 */             foo >>= 8;
/* 365 */             this.data[(j + i)] = ((byte)(foo & 0xFF));i++;
/* 366 */             ww -= 4;
/*     */           }
/* 368 */           if (ww != 0) {
/* 369 */             int foo = comChannel.readInt();n -= 4;
/* 370 */             while (0 < ww) {
/* 371 */               this.data[(j + i)] = ((byte)(foo & 0xFF));i++;
/* 372 */               foo >>= 8;
/* 373 */               ww--;
/*     */             }
/*     */           }
/* 376 */           j += scanWidth;
/*     */         }
/*     */       }
/*     */     }
/*     */     else {
/*     */       int foo;
/* 382 */       for (int n = c.length; 
/*     */           
/* 384 */           n != 0; 
/* 385 */           n--) foo = comChannel.readInt();
/*     */     }
/*     */   }
/*     */   
/*     */   public void image2data(int x, int y, int w, int h)
/*     */   {
/* 391 */     if (this.pixels.length < w * h) {
/* 392 */       this.pixels = new int[w * h];
/*     */     }
/* 394 */     PixelGrabber pg = new PixelGrabber(this.img, x, y, w, h, this.pixels, 0, w);
/*     */     try {
/* 396 */       pg.grabPixels();
/*     */     } catch (InterruptedException e) {
/* 398 */       System.err.println("interrupted waiting for pixels!");
/* 399 */       return;
/*     */     }
/* 401 */     if ((pg.getStatus() & 0x80) != 0) {
/* 402 */       System.err.println("image fetch aborted or errored");
/* 403 */       return;
/*     */     }
/*     */     
/* 406 */     int scanWidth = getScanWidth();
/*     */     
/* 408 */     byte[] dt = getData();
/*     */     
/* 410 */     for (int i = 0; i < h; i++) {
/* 411 */       for (int j = 0; j < w; j++) {
/* 412 */         int foo = this.pixels[(i * w + j)];
/* 413 */         foo = (foo >> 16 & 0xFF) / 8 << 11 | (foo >> 8 & 0xFF) / 4 << 5 | (foo & 0xFF) / 8;
/*     */         
/*     */ 
/* 416 */         dt[((y + i) * scanWidth + x + j * 2)] = ((byte)(foo >> 8 & 0xFF));
/* 417 */         dt[((y + i) * scanWidth + x + j * 2 + 1)] = ((byte)(foo & 0xFF));
/*     */       }
/*     */     }
/*     */     
/* 421 */     int iii = scanWidth * (y - 1);
/* 422 */     int iiii = scanWidth / 2 * (y - 1);
/*     */     
/* 424 */     for (int ii = y; ii < y + h; ii++) {
/* 425 */       iii += scanWidth;
/* 426 */       iiii += scanWidth / 2;
/* 427 */       int jjj = (x - 1) * 2;
/* 428 */       for (int jj = x; jj < x + w; jj++) {
/* 429 */         jjj += 2;
/* 430 */         int foo = dt[(iii + jjj)] << 8 & 0xFF00 | dt[(iii + jjj + 1)] & 0xFF;
/* 431 */         this.idata[(iiii + jj)] = (0xFF000000 | (foo >> 11 & 0x1F) * 8 << 16 | (foo >> 5 & 0x3F) * 4 << 8 | (foo & 0x1F) * 8);
/*     */       }
/*     */     }
/*     */     
/*     */ 
/*     */ 
/* 437 */     this.time = 0L;
/*     */   }
/*     */   
/*     */   public void mkMIS() {
/* 441 */     this.mis = new MemoryImageSource(this.width, this.height, this.idata, 0, this.width);
/* 442 */     this.mis.setAnimated(true);
/*     */   }
/*     */   
/*     */   public void copyArea(Pixmap dst, GC gc, int sx, int sy, int dx, int dy, int w, int h)
/*     */   {
/* 447 */     super.copyArea(dst, gc, sx, sy, dx, dy, w, h);
/*     */   }
/*     */ }


/* Location:              C:\Users\Joey\Downloads\launch.jar!\com\emt\proteus\xserver\display\Pixmap16.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */