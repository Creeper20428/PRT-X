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
/*     */ public final class GC
/*     */   extends Resource
/*     */ {
/*     */   public static final byte GXclear = 0;
/*     */   public static final byte GXand = 1;
/*     */   public static final byte GXandReverse = 2;
/*     */   public static final byte GXcopy = 3;
/*     */   public static final byte GXandInverted = 4;
/*     */   public static final byte GXnoop = 5;
/*     */   public static final byte GXxor = 6;
/*     */   public static final byte GXor = 7;
/*     */   public static final byte GXnor = 8;
/*     */   public static final byte GXequiv = 9;
/*     */   public static final byte GXinvert = 10;
/*     */   public static final byte GXorReverse = 11;
/*     */   public static final byte GXcopyInverted = 12;
/*     */   public static final byte GXorInverted = 13;
/*     */   public static final byte GXnand = 14;
/*     */   public static final byte GXset = 15;
/*     */   public static final int lineStyleOffset = 0;
/*     */   public static final int lineStyle = 3;
/*     */   public static final int LineSolid = 0;
/*     */   public static final int LineOnOffDash = 1;
/*     */   public static final int LineDoubleDash = 2;
/*     */   public static final int capStyleOffset = 2;
/*     */   public static final int capStyle = 12;
/*     */   public static final int CapNotLast = 0;
/*     */   public static final int CapButt = 4;
/*     */   public static final int CapRound = 8;
/*     */   public static final int CapProjecting = 12;
/*     */   public static final int joinStyleOffset = 4;
/*     */   public static final int joinStyle = 48;
/*     */   public static final int JoinMiter = 0;
/*     */   public static final int JoinRound = 16;
/*     */   public static final int JoinBevel = 32;
/*     */   public static final int fillStyleOffset = 6;
/*     */   public static final int fillStyle = 192;
/*     */   public static final int FillSolid = 0;
/*     */   public static final int FillTiled = 64;
/*     */   public static final int FillStippled = 128;
/*     */   public static final int FillOpaqueStippled = 192;
/*     */   public static final int fillRuleOffset = 8;
/*     */   public static final int fillRule = 256;
/*     */   public static final int EvenOddRule = 0;
/*     */   public static final int WindingRule = 256;
/*     */   public static final int arcModeOffset = 9;
/*     */   public static final int arcMode = 512;
/*     */   public static final int ArcChord = 0;
/*     */   public static final int ArcPieSlice = 512;
/*     */   public static final int subWindowModeOffset = 10;
/*     */   public static final int subWindowMode = 1024;
/*     */   public static final int ClipByChildren = 0;
/*     */   public static final int IncludeInferiors = 1024;
/*     */   public static final int graphicsExposuresOffset = 11;
/*     */   public static final int graphicsExposures = 2048;
/*     */   public static final int tileIsPixelOffset = 15;
/*     */   public static final int tileIsPixel = 32768;
/*     */   public static final int GCFunction = 1;
/*     */   public static final int GCPlaneMask = 2;
/*     */   public static final int GCForeground = 4;
/*     */   public static final int GCBackground = 8;
/*     */   public static final int GCLineWidth = 16;
/*     */   public static final int GCLineStyle = 32;
/*     */   public static final int GCCapStyle = 64;
/*     */   public static final int GCJoinStyle = 128;
/*     */   public static final int GCFillStyle = 256;
/*     */   public static final int GCFillRule = 512;
/*     */   public static final int GCTile = 1024;
/*     */   public static final int GCStipple = 2048;
/*     */   public static final int GCTileStipXOrigin = 4096;
/*     */   public static final int GCTileStipYOrigin = 8192;
/*     */   public static final int GCFont = 16384;
/*     */   public static final int GCSubwindowMode = 32768;
/*     */   public static final int GCGraphicsExposures = 65536;
/*     */   public static final int GCClipXOrigin = 131072;
/*     */   public static final int GCClipYOrigin = 262144;
/*     */   public static final int GCClipMask = 524288;
/*     */   public static final int GCDashOffset = 1048576;
/*     */   public static final int GCDashList = 2097152;
/*     */   public static final int GCArcMode = 4194304;
/*     */   public static final int CT_NONE = 0;
/*     */   public static final int CT_PIXMAP = 1;
/*     */   public static final int CT_REGION = 2;
/*     */   public static final int CT_UNSORTED = 6;
/*     */   public static final int CT_YSORTED = 10;
/*     */   public static final int CT_YXSORTED = 14;
/*     */   public static final int CT_YXBANDED = 18;
/*     */   Drawable drawable;
/*     */   XFont XFont;
/*     */   short lineWidth;
/*     */   byte function;
/*     */   float[] dash;
/*     */   float dash_phase;
/*     */   int attr;
/*     */   int time;
/*     */   Pix tile;
/*     */   Pixmap stipple;
/*     */   short tile_stipple_x_origin;
/*     */   short tile_stipple_y_origin;
/*     */   int clip_x_origin;
/*     */   int clip_y_origin;
/*     */   Clip clip_mask;
/*     */   int fgPixel;
/*     */   int bgPixel;
/*     */   
/*     */   GC(int id, Drawable dr)
/*     */   {
/* 148 */     super(id, -2147483645);
/* 149 */     this.drawable = dr;
/* 150 */     init();
/*     */   }
/*     */   
/*     */   private void init() {
/* 154 */     this.XFont = XFont.dflt;
/* 155 */     this.lineWidth = 1;
/* 156 */     this.function = 3;
/* 157 */     this.dash = null;
/* 158 */     this.dash_phase = 0.0F;
/*     */     
/* 160 */     this.attr = 2564;
/*     */     
/*     */ 
/* 163 */     this.time = 0;
/* 164 */     this.tile = new Pix();
/* 165 */     this.stipple = null;
/* 166 */     this.tile_stipple_x_origin = 0;
/* 167 */     this.tile_stipple_y_origin = 0;
/* 168 */     this.clip_x_origin = 0;
/* 169 */     this.clip_y_origin = 0;
/* 170 */     this.clip_mask = null;
/* 171 */     this.fgPixel = 0;
/* 172 */     this.bgPixel = 1;
/*     */   }
/*     */   
/*     */   public static void reqSetDashes(XClient c)
/*     */     throws IOException
/*     */   {
/* 178 */     ComChannel comChannel = c.channel;
/* 179 */     int n = c.length;
/* 180 */     int foo = comChannel.readInt();
/*     */     
/* 182 */     c.length -= 2;
/* 183 */     GC gc = c.lookupGC(foo);
/* 184 */     if (gc == null) {
/* 185 */       c.errorValue = foo;
/* 186 */       c.errorReason = 13;
/* 187 */       return;
/*     */     }
/*     */     
/* 190 */     foo = comChannel.readShort();
/* 191 */     gc.dash_phase = foo;
/* 192 */     foo = comChannel.readShort();
/* 193 */     if (foo == 0) {
/* 194 */       gc.dash = null;
/* 195 */       return;
/*     */     }
/*     */     
/* 198 */     gc.dash = new float[foo];
/*     */     
/* 200 */     n -= 3;
/* 201 */     n *= 4;
/* 202 */     for (int i = 0; i < foo; i++) {
/* 203 */       gc.dash[i] = comChannel.readByte();
/* 204 */       n--;
/*     */     }
/* 206 */     if (n > 0) {
/* 207 */       comChannel.readPad(n);
/*     */     }
/* 209 */     gc.time = 0;
/*     */   }
/*     */   
/*     */   public static void reqSetClipRectangles(XClient c)
/*     */     throws IOException
/*     */   {
/* 215 */     ComChannel comChannel = c.channel;
/*     */     
/* 217 */     int ordering = c.data;
/* 218 */     int n = c.length;
/* 219 */     int foo = comChannel.readInt();
/* 220 */     c.length -= 2;
/* 221 */     GC gc = c.lookupGC(foo);
/* 222 */     if (gc == null) {
/* 223 */       c.errorValue = foo;
/* 224 */       c.errorReason = 13;
/* 225 */       return;
/*     */     }
/* 227 */     gc.clip_x_origin = comChannel.readShort();
/* 228 */     gc.clip_y_origin = comChannel.readShort();
/* 229 */     c.length -= 1;
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 234 */     n -= 3;
/*     */     
/* 236 */     ClipRectangles cr = null;
/* 237 */     if ((gc.clip_mask != null) && 
/* 238 */       ((gc.clip_mask instanceof ClipPixmap))) {
/* 239 */       try { ((Pixmap)gc.clip_mask.getMask()).delete();
/*     */       } catch (Exception e) {}
/* 241 */       gc.clip_mask = null;
/*     */     }
/*     */     
/*     */ 
/* 245 */     if (cr == null) {
/* 246 */       cr = new ClipRectangles(ordering, n / 2);
/* 247 */       gc.clip_mask = cr;
/*     */     }
/*     */     
/* 250 */     while (n != 0) {
/* 251 */       short x = (short)comChannel.readShort();
/* 252 */       short y = (short)comChannel.readShort();
/* 253 */       int w = comChannel.readShort();
/* 254 */       int h = comChannel.readShort();
/*     */       
/* 256 */       n -= 2;
/* 257 */       cr.add(x, y, w, h);
/*     */     }
/*     */     
/* 260 */     cr.validate();
/* 261 */     c.length = 0;
/*     */   }
/*     */   
/*     */   public static void reqFreeGC(XClient c) throws IOException
/*     */   {
/* 266 */     ComChannel comChannel = c.channel;
/*     */     
/* 268 */     int foo = comChannel.readInt();
/*     */     
/* 270 */     c.length -= 2;
/*     */     
/* 272 */     GC gc = c.lookupGC(foo);
/* 273 */     if (gc == null) {
/* 274 */       c.errorValue = foo;
/* 275 */       c.errorReason = 13;
/* 276 */       return;
/*     */     }
/* 278 */     freeResource(foo, 0);
/*     */   }
/*     */   
/* 281 */   void delete() throws IOException { this.dash = null;
/* 282 */     if (this.stipple != null) {
/* 283 */       try { this.stipple.delete(); } catch (Exception e) {}
/* 284 */       this.stipple = null;
/*     */     }
/* 286 */     if (this.clip_mask != null) {
/*     */       try {
/* 288 */         if ((this.clip_mask instanceof ClipPixmap)) {
/* 289 */           ((Pixmap)this.clip_mask.getMask()).delete();
/*     */         }
/*     */       } catch (Exception e) {}
/* 292 */       this.clip_mask = null;
/*     */     }
/* 294 */     if (this.tile.pixmap != null) {
/* 295 */       try { this.tile.pixmap.delete(); } catch (Exception e) {}
/* 296 */       this.tile.pixmap = null;
/* 297 */       this.tile = null;
/*     */     }
/* 299 */     put(this);
/*     */   }
/*     */   
/*     */   public static void reqCopyGC(XClient c) throws IOException {
/* 303 */     ComChannel comChannel = c.channel;
/* 304 */     int foo = comChannel.readInt();
/* 305 */     c.length -= 2;
/* 306 */     GC srcgc = c.lookupGC(foo);
/* 307 */     if (srcgc == null) {
/* 308 */       c.errorValue = foo;
/* 309 */       c.errorReason = 13;
/* 310 */       return;
/*     */     }
/* 312 */     foo = comChannel.readInt();
/* 313 */     c.length -= 1;
/* 314 */     GC dstgc = c.lookupGC(foo);
/* 315 */     if (dstgc == null) {
/* 316 */       c.errorValue = foo;
/* 317 */       c.errorReason = 13;
/* 318 */       return;
/*     */     }
/* 320 */     int msk = comChannel.readInt();
/* 321 */     c.length -= 1;
/* 322 */     msk &= 0x7FFFFF;
/* 323 */     dstgc.copyAttr(c, msk, srcgc);
/*     */   }
/*     */   
/*     */   public static void reqChangeGC(XClient c) throws IOException
/*     */   {
/* 328 */     ComChannel comChannel = c.channel;
/* 329 */     int n = c.length;
/* 330 */     int foo = comChannel.readInt();
/* 331 */     c.length -= 2;
/* 332 */     GC gc = c.lookupGC(foo);
/* 333 */     if (gc == null) {
/* 334 */       c.errorValue = foo;
/* 335 */       c.errorReason = 13;
/* 336 */       return;
/*     */     }
/* 338 */     foo = comChannel.readInt();
/* 339 */     c.length -= 1;
/* 340 */     foo &= 0x7FFFFF;
/* 341 */     if (foo != 0) {
/* 342 */       gc.changeAttr(c, foo);
/*     */     }
/*     */   }
/*     */   
/*     */   public static void reqCreateGC(XClient c) throws IOException {
/* 347 */     ComChannel comChannel = c.channel;
/* 348 */     int cid = comChannel.readInt();
/* 349 */     int foo = comChannel.readInt();
/* 350 */     c.length -= 3;
/* 351 */     Drawable d = c.lookupDrawable(foo);
/* 352 */     if (d == null) {
/* 353 */       c.errorValue = foo;
/* 354 */       c.errorReason = 9;
/* 355 */       return;
/*     */     }
/*     */     
/* 358 */     int mask = comChannel.readInt();
/* 359 */     c.length -= 1;
/* 360 */     GC gc = getGC(cid, d);
/* 361 */     mask &= 0x7FFFFF;
/* 362 */     if (mask != 0) {
/* 363 */       gc.changeAttr(c, mask);
/*     */     }
/* 365 */     add(gc);
/*     */   }
/*     */   
/*     */   private void copyAttr(XClient c, int vmask, GC srcgc) throws IOException {
/* 369 */     int index = 0;
/*     */     
/* 371 */     int mask = vmask;
/*     */     
/* 373 */     while (mask != 0) {
/* 374 */       index = lowbit(mask);
/* 375 */       mask &= (index ^ 0xFFFFFFFF);
/* 376 */       switch (index) {
/*     */       case 1: 
/* 378 */         this.function = srcgc.function;
/* 379 */         break;
/*     */       case 2: 
/*     */         break;
/*     */       case 4: 
/* 383 */         this.fgPixel = srcgc.fgPixel;
/* 384 */         if (((this.attr & 0x8000) == 0) && (this.tile.pixmap == null)) {
/* 385 */           this.attr |= 0x8000;
/* 386 */           this.tile.pixel = this.fgPixel;
/*     */         }
/*     */         break;
/*     */       case 8: 
/* 390 */         this.bgPixel = srcgc.bgPixel;
/* 391 */         break;
/*     */       case 16: 
/* 393 */         this.lineWidth = srcgc.lineWidth;
/* 394 */         break;
/*     */       case 32: 
/* 396 */         this.attr &= 0xFFFFFFFC;
/* 397 */         this.attr |= srcgc.attr & 0x3;
/* 398 */         break;
/*     */       case 64: 
/* 400 */         this.attr &= 0xFFFFFFF3;
/* 401 */         this.attr |= srcgc.attr & 0xC;
/* 402 */         break;
/*     */       case 128: 
/* 404 */         this.attr &= 0xFFFFFFCF;
/* 405 */         this.attr |= srcgc.attr & 0x30;
/* 406 */         break;
/*     */       case 256: 
/* 408 */         this.attr &= 0xFF3F;
/* 409 */         this.attr |= srcgc.attr & 0xC0;
/* 410 */         break;
/*     */       case 512: 
/* 412 */         this.attr &= 0xFEFF;
/* 413 */         this.attr |= srcgc.attr & 0x100;
/* 414 */         break;
/*     */       
/*     */       case 1024: 
/* 417 */         if ((srcgc.attr & 0x8000) != 0) {
/* 418 */           this.tile.pixel = srcgc.tile.pixel;
/*     */         }
/*     */         else {
/* 421 */           this.tile.pixmap = srcgc.tile.pixmap;
/*     */         }
/* 423 */         break;
/*     */       case 2048: 
/* 425 */         if (this.stipple != null) {
/* 426 */           try { this.stipple.delete(); } catch (Exception e) {}
/* 427 */           this.stipple = null;
/*     */         }
/* 429 */         this.stipple = srcgc.stipple;
/* 430 */         if (this.stipple != null) {
/* 431 */           this.stipple.ref();
/*     */         }
/*     */         break;
/*     */       case 4096: 
/* 435 */         this.tile_stipple_x_origin = srcgc.tile_stipple_x_origin;
/* 436 */         break;
/*     */       case 8192: 
/* 438 */         this.tile_stipple_y_origin = srcgc.tile_stipple_y_origin;
/* 439 */         break;
/*     */       case 16384: 
/* 441 */         this.XFont = srcgc.XFont;
/* 442 */         break;
/*     */       case 32768: 
/* 444 */         this.attr &= 0xFBFF;
/* 445 */         this.attr |= srcgc.attr & 0x400;
/* 446 */         break;
/*     */       case 65536: 
/* 448 */         this.attr &= 0xF7FF;
/* 449 */         this.attr |= srcgc.attr & 0x800;
/* 450 */         break;
/*     */       case 131072: 
/* 452 */         this.clip_x_origin = srcgc.clip_x_origin;
/* 453 */         break;
/*     */       case 262144: 
/* 455 */         this.clip_y_origin = srcgc.clip_y_origin;
/* 456 */         break;
/*     */       case 524288: 
/* 458 */         if (this.clip_mask != null) {
/*     */           try {
/* 460 */             if ((this.clip_mask instanceof ClipPixmap)) {
/* 461 */               ((Pixmap)this.clip_mask.getMask()).delete();
/*     */             }
/*     */           } catch (Exception e) {}
/* 464 */           this.clip_mask = null;
/*     */         }
/* 466 */         this.clip_mask = srcgc.clip_mask;
/* 467 */         if ((this.clip_mask != null) && 
/* 468 */           ((this.clip_mask instanceof ClipPixmap))) {
/* 469 */           ((Pixmap)this.clip_mask.getMask()).ref();
/*     */         }
/*     */         
/*     */         break;
/*     */       case 1048576: 
/* 474 */         this.dash_phase = srcgc.dash_phase;
/* 475 */         break;
/*     */       case 2097152: 
/* 477 */         this.dash = srcgc.dash;
/* 478 */         if (this.dash != null) {
/* 479 */           float[] bar = new float[this.dash.length];
/* 480 */           System.arraycopy(this.dash, 0, bar, 0, bar.length);
/* 481 */           this.dash = bar; }
/* 482 */         break;
/*     */       
/*     */       case 4194304: 
/*     */         break;
/*     */       
/*     */       default: 
/* 488 */         c.errorValue = vmask;
/* 489 */         c.errorReason = 2;
/*     */       }
/* 491 */       if (c.errorReason != 0) {
/* 492 */         return;
/*     */       }
/*     */     }
/*     */     
/* 496 */     if (c.length != 0) {
/* 497 */       c.errorValue = vmask;
/* 498 */       c.errorReason = 2;
/*     */     }
/*     */     else {
/* 501 */       this.time = c.getSequence();
/*     */     }
/*     */   }
/*     */   
/*     */   private void changeAttr(XClient c, int vmask) throws IOException {
/* 506 */     int index = 0;
/*     */     
/* 508 */     int mask = vmask;
/* 509 */     ComChannel comChannel = c.channel;
/*     */     
/* 511 */     while (mask != 0) {
/* 512 */       index = lowbit(mask);
/* 513 */       mask &= (index ^ 0xFFFFFFFF);
/* 514 */       c.length -= 1;
/* 515 */       int foo; switch (index) {
/*     */       case 1: 
/* 517 */         foo = comChannel.readInt();
/* 518 */         foo &= 0xFF;
/* 519 */         this.function = ((byte)foo);
/* 520 */         break;
/*     */       case 2: 
/* 522 */         foo = comChannel.readInt();
/* 523 */         break;
/*     */       case 4: 
/* 525 */         foo = comChannel.readInt();
/* 526 */         this.fgPixel = foo;
/* 527 */         if (((this.attr & 0x8000) == 0) && (this.tile.pixmap == null)) {
/* 528 */           this.attr |= 0x8000;
/* 529 */           this.tile.pixel = this.fgPixel;
/*     */         }
/*     */         
/*     */         break;
/*     */       case 8: 
/* 534 */         this.bgPixel = comChannel.readInt();
/*     */         
/* 536 */         break;
/*     */       case 16: 
/* 538 */         foo = comChannel.readInt();
/* 539 */         foo &= 0xFFFF;
/* 540 */         if (foo == 0) foo = 1;
/* 541 */         this.lineWidth = ((short)foo);
/* 542 */         break;
/*     */       case 32: 
/* 544 */         foo = comChannel.readInt();
/* 545 */         foo &= 0xFF;
/* 546 */         foo = foo << 0 & 0x3;
/* 547 */         if ((foo != 0) && (foo != 1) && (foo != 2))
/*     */         {
/*     */ 
/* 550 */           c.errorValue = (foo >> 0);
/* 551 */           c.errorReason = 2;
/*     */         }
/*     */         else {
/* 554 */           this.attr &= 0xFFFFFFFC;
/* 555 */           this.attr |= foo; }
/* 556 */         break;
/*     */       case 64: 
/* 558 */         foo = comChannel.readInt();
/* 559 */         foo &= 0xFF;
/* 560 */         foo = foo << 2 & 0xC;
/* 561 */         if ((foo != 0) && (foo != 4) && (foo != 8) && (foo != 12))
/*     */         {
/*     */ 
/*     */ 
/* 565 */           c.errorValue = (foo >> 2);
/* 566 */           c.errorReason = 2;
/*     */         }
/*     */         else {
/* 569 */           this.attr |= foo;
/*     */         }
/* 571 */         break;
/*     */       case 128: 
/* 573 */         foo = comChannel.readInt();
/* 574 */         foo &= 0xFF;
/* 575 */         foo = foo << 4 & 0x30;
/* 576 */         if ((foo != 0) && (foo != 16) && (foo != 32))
/*     */         {
/*     */ 
/* 579 */           c.errorValue = (foo >> 4);
/* 580 */           c.errorReason = 2;
/*     */         }
/*     */         else {
/* 583 */           this.attr |= foo;
/*     */         }
/* 585 */         break;
/*     */       case 256: 
/* 587 */         foo = comChannel.readInt();
/* 588 */         foo &= 0xFF;
/* 589 */         this.attr = (this.attr & 0xFF3F | foo << 6);
/* 590 */         break;
/*     */       case 512: 
/* 592 */         foo = comChannel.readInt();
/* 593 */         foo &= 0xFF;
/* 594 */         break;
/*     */       case 1024: 
/* 596 */         foo = comChannel.readInt();
/*     */         
/* 598 */         Resource o = lookupIDByType(foo, -1073741822);
/* 599 */         if ((o != null) && ((o instanceof Pixmap))) {
/* 600 */           Pixmap tmp = (Pixmap)o;
/* 601 */           if ((tmp.depth != this.drawable.depth) || (tmp.screen != this.drawable.screen))
/*     */           {
/* 603 */             c.errorValue = foo;
/* 604 */             c.errorReason = 8;
/*     */           }
/*     */           else {
/* 607 */             if (this.tile.pixmap != null)
/* 608 */               try { this.tile.pixmap.delete();
/*     */               } catch (Exception e) {}
/* 610 */             this.tile.pixmap = tmp;
/* 611 */             this.tile.pixmap.ref();
/* 612 */             this.attr &= 0xFFFF7FFF;
/*     */           }
/*     */         } else {
/* 615 */           c.errorValue = foo;
/* 616 */           c.errorReason = 2;
/* 617 */           break;
/*     */         }
/*     */         
/* 620 */         break;
/*     */       case 2048: 
/* 622 */         foo = comChannel.readInt();
/*     */         
/* 624 */         Resource o = lookupIDByType(foo, -1073741822);
/* 625 */         if ((o != null) && ((o instanceof Pixmap))) {
/* 626 */           Pixmap tmp = (Pixmap)o;
/* 627 */           if ((tmp.depth != 1) || (tmp.screen != this.drawable.screen))
/*     */           {
/* 629 */             c.errorValue = foo;
/* 630 */             c.errorReason = 8;
/*     */           }
/*     */           else {
/* 633 */             if (this.stipple != null)
/* 634 */               try { this.stipple.delete();
/*     */               } catch (Exception e) {}
/* 636 */             this.stipple = tmp;
/* 637 */             this.stipple.ref();
/*     */           }
/*     */         } else {
/* 640 */           c.errorValue = foo;
/* 641 */           c.errorReason = 2;
/* 642 */           break;
/*     */         }
/*     */         
/* 645 */         break;
/*     */       case 4096: 
/* 647 */         foo = comChannel.readInt();
/* 648 */         this.tile_stipple_x_origin = ((short)(foo & 0xFFFF));
/* 649 */         break;
/*     */       case 8192: 
/* 651 */         foo = comChannel.readInt();
/* 652 */         this.tile_stipple_y_origin = ((short)(foo & 0xFFFF));
/* 653 */         break;
/*     */       case 16384: 
/* 655 */         foo = comChannel.readInt();
/* 656 */         this.XFont = ((XFont)lookupIDByType(foo, 4));
/* 657 */         if (this.XFont != null)
/*     */           break;
/*     */         break;
/*     */       case 32768: 
/* 661 */         foo = comChannel.readInt();
/* 662 */         foo &= 0xFF;
/* 663 */         if ((foo != 0) && (foo != 1)) {}
/*     */         
/* 665 */         this.attr &= 0xFBFF;
/* 666 */         if (foo == 1) { this.attr |= 0x400;
/*     */         }
/*     */         
/*     */         break;
/*     */       case 65536: 
/* 671 */         foo = comChannel.readInt();
/* 672 */         foo &= 0xFF;
/* 673 */         if (foo == 1) this.attr |= 0x800;
/* 674 */         if (foo == 0) this.attr &= 0xF7FF;
/*     */         break;
/*     */       case 131072: 
/* 677 */         foo = comChannel.readInt();
/* 678 */         this.clip_x_origin = ((short)(foo & 0xFFFF));
/* 679 */         break;
/*     */       case 262144: 
/* 681 */         foo = comChannel.readInt();
/* 682 */         this.clip_y_origin = ((short)(foo & 0xFFFF));
/* 683 */         break;
/*     */       case 524288: 
/* 685 */         foo = comChannel.readInt();
/* 686 */         if (foo == 0) {
/* 687 */           if (this.clip_mask != null) {
/*     */             try {
/* 689 */               if ((this.clip_mask instanceof ClipPixmap)) {
/* 690 */                 ((Pixmap)this.clip_mask.getMask()).delete();
/*     */               }
/*     */             } catch (Exception e) {}
/*     */           }
/* 694 */           this.clip_mask = null;
/*     */         }
/*     */         else {
/* 697 */           Drawable d = c.lookupDrawable(foo);
/* 698 */           if ((d == null) || (!(d instanceof Pixmap)) || (d.depth != 1)) {
/* 699 */             c.errorValue = foo;
/* 700 */             c.errorReason = 8;
/*     */           }
/*     */           else {
/* 703 */             if (this.clip_mask != null) {
/*     */               try {
/* 705 */                 if ((this.clip_mask instanceof ClipPixmap)) {
/* 706 */                   ((Pixmap)this.clip_mask.getMask()).delete();
/*     */                 }
/*     */               } catch (Exception e) {}
/*     */             }
/* 710 */             ((Pixmap)d).ref();
/* 711 */             this.clip_mask = new ClipPixmap((Pixmap)d);
/*     */           }
/*     */         }
/* 714 */         break;
/*     */       case 1048576: 
/* 716 */         foo = comChannel.readInt();
/* 717 */         foo &= 0xFFFF;
/* 718 */         break;
/*     */       case 2097152: 
/* 720 */         foo = comChannel.readInt();
/* 721 */         foo &= 0xFF;
/* 722 */         break;
/*     */       case 4194304: 
/* 724 */         foo = comChannel.readInt();
/* 725 */         foo &= 0xFF;
/* 726 */         break;
/*     */       default: 
/* 728 */         c.length += 1;
/* 729 */         c.errorValue = vmask;
/* 730 */         c.errorReason = 2;
/*     */       }
/* 732 */       if (c.errorReason != 0) {
/* 733 */         return;
/*     */       }
/*     */     }
/*     */     
/* 737 */     if (c.length != 0) {
/* 738 */       c.errorValue = vmask;
/* 739 */       c.errorReason = 2;
/*     */     }
/*     */     else {
/* 742 */       this.time = c.getSequence();
/*     */     }
/*     */   }
/*     */   
/*     */   private static int lowbit(int mask) {
/* 747 */     int result = 1;
/* 748 */     for (int i = 0; i < 32; i++) {
/* 749 */       if ((mask & 0x1) == 1) { result <<= i; break; }
/* 750 */       mask >>= 1;
/*     */     }
/* 752 */     return result;
/*     */   }
/*     */   
/*     */   private static GC getGC(int id, Drawable d) {
/* 756 */     GC gc = get();
/* 757 */     if (gc == null) return new GC(id, d);
/* 758 */     gc.id = id;gc.drawable = d;
/* 759 */     gc.init();
/* 760 */     return gc;
/*     */   }
/*     */   
/* 763 */   private static GC[] pool = new GC[32];
/* 764 */   private static boolean full = false;
/*     */   
/*     */   private static synchronized GC get() {
/* 767 */     GC tmp = null;
/* 768 */     if (full) full = false;
/* 769 */     for (int i = 0; i < 32; i++)
/* 770 */       if (pool[i] != null) { tmp = pool[i];pool[i] = null; break;
/*     */       }
/* 772 */     return tmp;
/*     */   }
/*     */   
/*     */   private static synchronized void put(GC gc) {
/* 776 */     if (full) return;
/* 777 */     for (int i = 0; i < 32; i++)
/* 778 */       if (pool[i] == null) { pool[i] = gc;return;
/*     */       }
/* 780 */     full = true;
/*     */   }
/*     */ }


/* Location:              C:\Users\Joey\Downloads\launch.jar!\com\emt\proteus\xserver\display\GC.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */