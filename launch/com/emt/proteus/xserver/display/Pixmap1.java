/*     */ package com.emt.proteus.xserver.display;
/*     */ 
/*     */ import com.emt.proteus.xserver.client.XClient;
/*     */ import com.emt.proteus.xserver.io.ComChannel;
/*     */ import java.awt.Image;
/*     */ import java.awt.Toolkit;
/*     */ import java.awt.image.FilteredImageSource;
/*     */ import java.awt.image.MemoryImageSource;
/*     */ import java.io.IOException;
/*     */ 
/*     */ 
/*     */ public class Pixmap1
/*     */   extends Pixmap
/*     */ {
/*  15 */   FBFilter fbFilter = null;
/*  16 */   Image fgImg = null;
/*     */   
/*     */   public Pixmap1(int id, Drawable d, int width, int height) {
/*  19 */     super(id, d, width, height, (byte)1);
/*  20 */     reset();
/*     */   }
/*     */   
/*     */   public Image getImage() {
/*  24 */     if (!this.fbFilter.through) return this.fgImg;
/*  25 */     return super.getImage();
/*     */   }
/*     */   
/*     */   public Image getImage(XWindow win, int fgPixel, int bgPixel) {
/*  29 */     setFgBg(win.getColormap(), fgPixel, bgPixel);
/*  30 */     return super.getImage(null, fgPixel, bgPixel);
/*     */   }
/*     */   
/*     */   public void reset() {
/*  34 */     if (this.fgImg != null) {
/*  35 */       this.fgImg.flush();
/*     */     }
/*  37 */     this.fbFilter = new FBFilter();
/*  38 */     this.fgImg = Toolkit.getDefaultToolkit().createImage(new FilteredImageSource(this.img.getSource(), this.fbFilter));
/*     */   }
/*     */   
/*     */ 
/*     */   static void putData(XClient c, byte[] d, int width, int dx, int dy, int dwidth, int lpad)
/*     */     throws IOException
/*     */   {
/*  45 */     ComChannel comChannel = c.channel;
/*  46 */     int j = dy * width;
/*  47 */     int n = c.length;
/*     */     
/*     */ 
/*  50 */     if (n * 4 < c.cbuffer.length) {
/*  51 */       comChannel.readByte(c.bbuffer, 0, n * 4);
/*  52 */       int start = 0;
/*  53 */       while (n != 0) {
/*  54 */         int ww = dwidth;
/*  55 */         int i = dx;
/*  56 */         if (lpad != 0) {
/*  57 */           n--;
/*     */           
/*     */ 
/*  60 */           int ii = lpad % 8;
/*  61 */           int iii = lpad / 8;
/*  62 */           if (ii != 0) {
/*  63 */             int foo = c.bbuffer[(start + iii)];
/*  64 */             foo <<= ii;
/*  65 */             for (; ii < 8; ii++) {
/*  66 */               d[(j + i)] = ((byte)((foo & 0x80) != 0 ? 1 : 0));
/*  67 */               i++;foo <<= 1;
/*  68 */               ww--; if (ww == 0) break label245;
/*     */             }
/*  70 */             iii++;
/*     */           }
/*  72 */           for (; iii < 4; iii++) {
/*  73 */             int foo = c.bbuffer[(start + iii)];
/*  74 */             for (ii = 0; ii < 8; ii++) {
/*  75 */               d[(j + i)] = ((byte)((foo & 0x80) != 0 ? 1 : 0));
/*  76 */               i++;foo <<= 1;
/*  77 */               ww--; if (ww == 0) break label245;
/*     */             }
/*     */           }
/*     */           label245:
/*  81 */           start += 4;
/*     */         }
/*  83 */         while (32 < ww) {
/*  84 */           n--;
/*  85 */           for (int iii = 0; iii < 4; iii++) {
/*  86 */             int foo = c.bbuffer[start];start++;
/*  87 */             for (int ii = 0; ii < 8; ii++) {
/*  88 */               d[(j + i)] = ((byte)((foo & 0x80) != 0 ? 1 : 0));
/*  89 */               i++;foo <<= 1;
/*     */             }
/*     */           }
/*  92 */           ww -= 32;
/*     */         }
/*  94 */         if (ww != 0) {
/*  95 */           n--;
/*  96 */           for (int iii = 0; iii < 4; iii++) {
/*  97 */             int foo = c.bbuffer[start];start++;
/*  98 */             for (int ii = 0; ii < 8; ii++) {
/*  99 */               if (0 < ww) {
/* 100 */                 d[(j + i)] = ((byte)((foo & 0x80) != 0 ? 1 : 0));
/* 101 */                 i++;foo <<= 1;
/* 102 */                 ww--;
/*     */               }
/*     */             }
/*     */           }
/*     */         }
/* 107 */         j += width;
/*     */       }
/*     */     }
/*     */     else {
/* 111 */       while (n != 0) {
/* 112 */         int ww = dwidth;
/* 113 */         int i = dx;
/* 114 */         if (lpad != 0) {
/* 115 */           comChannel.readByte(c.bbuffer, 0, 4);n--;
/*     */           
/*     */ 
/* 118 */           int ii = lpad % 8;
/* 119 */           int iii = lpad / 8;
/* 120 */           if (ii != 0) {
/* 121 */             int foo = c.bbuffer[iii];
/* 122 */             foo <<= ii;
/* 123 */             for (; ii < 8; ii++) {
/* 124 */               d[(j + i)] = ((byte)((foo & 0x80) != 0 ? 1 : 0));
/* 125 */               i++;foo <<= 1;
/* 126 */               ww--; if (ww == 0) break label644;
/*     */             }
/* 128 */             iii++;
/*     */           }
/* 130 */           for (; iii < 4; iii++) {
/* 131 */             int foo = c.bbuffer[iii];
/* 132 */             for (ii = 0; ii < 8; ii++) {
/* 133 */               d[(j + i)] = ((byte)((foo & 0x80) != 0 ? 1 : 0));
/* 134 */               i++;foo <<= 1;
/* 135 */               ww--; if (ww == 0) break label644;
/*     */             }
/*     */           }
/*     */         }
/*     */         label644:
/* 140 */         while (32 < ww) {
/* 141 */           comChannel.readByte(c.bbuffer, 0, 4);n--;
/* 142 */           for (int iii = 0; iii < 4; iii++) {
/* 143 */             int foo = c.bbuffer[iii];
/* 144 */             for (int ii = 0; ii < 8; ii++) {
/* 145 */               d[(j + i)] = ((byte)((foo & 0x80) != 0 ? 1 : 0));
/* 146 */               i++;foo <<= 1;
/*     */             }
/*     */           }
/* 149 */           ww -= 32;
/*     */         }
/* 151 */         if (ww != 0) {
/* 152 */           comChannel.readByte(c.bbuffer, 0, 4);n--;
/* 153 */           for (int iii = 0; iii < 4; iii++) {
/* 154 */             int foo = c.bbuffer[iii];
/* 155 */             for (int ii = 0; ii < 8; ii++) {
/* 156 */               if (0 < ww) {
/* 157 */                 d[(j + i)] = ((byte)((foo & 0x80) != 0 ? 1 : 0));
/* 158 */                 i++;foo <<= 1;
/* 159 */                 ww--;
/*     */               }
/*     */             }
/*     */           }
/*     */         }
/* 164 */         j += width;
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   static void putData_LSB(XClient c, byte[] d, int width, int dx, int dy, int dwidth, int lpad)
/*     */     throws IOException
/*     */   {
/* 172 */     ComChannel comChannel = c.channel;
/* 173 */     int j = dy * width;
/* 174 */     int n = c.length;
/*     */     
/*     */ 
/* 177 */     if (n * 4 < c.cbuffer.length) {
/* 178 */       comChannel.readByte(c.bbuffer, 0, n * 4);
/* 179 */       int start = 0;
/* 180 */       while (n != 0) {
/* 181 */         int ww = dwidth;
/* 182 */         int i = dx;
/* 183 */         if (lpad != 0) {
/* 184 */           n--;
/*     */           
/*     */ 
/* 187 */           int ii = lpad % 8;
/* 188 */           int iii = lpad / 8;
/* 189 */           if (ii != 0) {
/* 190 */             int foo = c.bbuffer[(start + iii)];
/* 191 */             foo >>>= ii;
/* 192 */             for (; ii < 8; ii++) {
/* 193 */               d[(j + i)] = ((byte)((foo & 0x1) != 0 ? 1 : 0));
/* 194 */               i++;foo >>>= 1;
/* 195 */               ww--; if (ww == 0) break label241;
/*     */             }
/* 197 */             iii++;
/*     */           }
/* 199 */           for (; iii < 4; iii++) {
/* 200 */             int foo = c.bbuffer[(start + iii)];
/* 201 */             for (ii = 0; ii < 8; ii++) {
/* 202 */               d[(j + i)] = ((byte)((foo & 0x1) != 0 ? 1 : 0));
/* 203 */               i++;foo >>>= 1;
/* 204 */               ww--; if (ww == 0) break label241;
/*     */             }
/*     */           }
/*     */           label241:
/* 208 */           start += 4;
/*     */         }
/* 210 */         while (32 < ww) {
/* 211 */           n--;
/* 212 */           for (int iii = 0; iii < 4; iii++) {
/* 213 */             int foo = c.bbuffer[start];start++;
/* 214 */             for (int ii = 0; ii < 8; ii++) {
/* 215 */               d[(j + i)] = ((byte)((foo & 0x1) != 0 ? 1 : 0));
/* 216 */               i++;foo >>>= 1;
/*     */             }
/*     */           }
/* 219 */           ww -= 32;
/*     */         }
/* 221 */         if (ww != 0) {
/* 222 */           n--;
/* 223 */           for (int iii = 0; iii < 4; iii++) {
/* 224 */             int foo = c.bbuffer[start];start++;
/* 225 */             for (int ii = 0; ii < 8; ii++) {
/* 226 */               if (0 < ww) {
/* 227 */                 d[(j + i)] = ((byte)((foo & 0x1) != 0 ? 1 : 0));
/* 228 */                 i++;foo >>>= 1;
/* 229 */                 ww--;
/*     */               }
/*     */             }
/*     */           }
/*     */         }
/* 234 */         j += width;
/*     */       }
/*     */     }
/*     */     else {
/* 238 */       while (n != 0) {
/* 239 */         int ww = dwidth;
/* 240 */         int i = dx;
/* 241 */         if (lpad != 0) {
/* 242 */           comChannel.readByte(c.bbuffer, 0, 4);n--;
/*     */           
/*     */ 
/* 245 */           int ii = lpad % 8;
/* 246 */           int iii = lpad / 8;
/* 247 */           if (ii != 0) {
/* 248 */             int foo = c.bbuffer[iii];
/* 249 */             foo >>>= ii;
/* 250 */             for (; ii < 8; ii++) {
/* 251 */               d[(j + i)] = ((byte)((foo & 0x1) != 0 ? 1 : 0));
/* 252 */               i++;foo >>>= 1;
/* 253 */               ww--; if (ww == 0) break label632;
/*     */             }
/* 255 */             iii++;
/*     */           }
/* 257 */           for (; iii < 4; iii++) {
/* 258 */             int foo = c.bbuffer[iii];
/* 259 */             for (ii = 0; ii < 8; ii++) {
/* 260 */               d[(j + i)] = ((byte)((foo & 0x1) != 0 ? 1 : 0));
/* 261 */               i++;foo >>>= 1;
/* 262 */               ww--; if (ww == 0) break label632;
/*     */             }
/*     */           }
/*     */         }
/*     */         label632:
/* 267 */         while (32 < ww) {
/* 268 */           comChannel.readByte(c.bbuffer, 0, 4);n--;
/* 269 */           for (int iii = 0; iii < 4; iii++) {
/* 270 */             int foo = c.bbuffer[iii];
/* 271 */             for (int ii = 0; ii < 8; ii++) {
/* 272 */               d[(j + i)] = ((byte)((foo & 0x1) != 0 ? 1 : 0));
/* 273 */               i++;foo >>>= 1;
/*     */             }
/*     */           }
/* 276 */           ww -= 32;
/*     */         }
/* 278 */         if (ww != 0) {
/* 279 */           comChannel.readByte(c.bbuffer, 0, 4);n--;
/* 280 */           for (int iii = 0; iii < 4; iii++) {
/* 281 */             int foo = c.bbuffer[iii];
/* 282 */             for (int ii = 0; ii < 8; ii++) {
/* 283 */               if (0 < ww) {
/* 284 */                 d[(j + i)] = ((byte)((foo & 0x1) != 0 ? 1 : 0));
/* 285 */                 i++;foo >>>= 1;
/* 286 */                 ww--;
/*     */               }
/*     */             }
/*     */           }
/*     */         }
/* 291 */         j += width;
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public void delete() throws IOException {
/* 297 */     if (this.refcnt > 0) {
/* 298 */       super.delete();
/* 299 */       return;
/*     */     }
/* 301 */     super.delete();
/* 302 */     this.fbFilter = null;
/* 303 */     if (this.fgImg != null) {
/* 304 */       this.fgImg.flush();
/* 305 */       this.fgImg = null;
/*     */     }
/*     */   }
/*     */   
/*     */   public void setFgBg(Colormap cmap, int fgPixel, int bgPixel) {
/* 310 */     if (this.img == null) return;
/* 311 */     if (this.fbFilter != null) {
/* 312 */       this.fbFilter.setFgBg(cmap.getColor(fgPixel), cmap.getColor(bgPixel));
/*     */     }
/*     */   }
/*     */   
/*     */   public void copyArea(Pixmap dst, GC gc, int sx, int sy, int dx, int dy, int w, int h)
/*     */   {
/* 318 */     if (dst.depth != 1) {
/* 319 */       setFgBg(dst.colormap, gc.fgPixel, gc.bgPixel);
/*     */     }
/* 321 */     super.copyArea(dst, gc, sx, sy, dx, dy, w, h);
/*     */   }
/*     */   
/* 324 */   public void mkMIS() { this.mis = new MemoryImageSource(this.width, this.height, Colormap.bwicm, this.data, 0, this.width);
/* 325 */     this.mis.setAnimated(true);
/*     */   }
/*     */ }


/* Location:              C:\Users\Joey\Downloads\launch.jar!\com\emt\proteus\xserver\display\Pixmap1.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */