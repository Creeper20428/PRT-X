/*      */ package com.emt.proteus.xserver.display;
/*      */ 
/*      */ import com.emt.proteus.xserver.client.XClient;
/*      */ import com.emt.proteus.xserver.io.ComChannel;
/*      */ import java.awt.Color;
/*      */ import java.awt.Graphics;
/*      */ import java.awt.Rectangle;
/*      */ import java.awt.Shape;
/*      */ import java.io.IOException;
/*      */ import java.io.PrintStream;
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
/*      */ public final class Draw
/*      */ {
/*      */   public static void reqPolyPoint(XClient c, Drawable d, GC gc)
/*      */     throws IOException
/*      */   {
/*   31 */     int n = c.length;
/*   32 */     ComChannel comChannel = c.channel;
/*   33 */     Graphics graphics = d.getGraphics(gc, 32769);
/*   34 */     if (graphics == null) {
/*   35 */       comChannel.readPad(n * 4);
/*   36 */       return;
/*      */     }
/*   38 */     boolean coor = false;
/*   39 */     if (c.data != 0) coor = true;
/*   40 */     if ((gc.clip_mask != null) && ((gc.clip_mask instanceof ClipRectangles))) {
/*   41 */       Rectangle rec = (Rectangle)gc.clip_mask.getMask();
/*   42 */       if (rec == null) {
/*   43 */         comChannel.readPad(4 * n);
/*   44 */         return;
/*      */       }
/*      */     }
/*      */     
/*   48 */     int sx = d.width;
/*   49 */     int sy = d.height;
/*   50 */     int lx = 0;
/*   51 */     int ly = 0;
/*   52 */     if (coor) {
/*   53 */       int x = (short)comChannel.readShort();int y = (short)comChannel.readShort();
/*   54 */       n--;
/*   55 */       graphics.drawLine(x, y, x, y);
/*      */       
/*   57 */       if (x <= sx) sx = x;
/*   58 */       if (x >= lx) lx = x;
/*   59 */       if (y <= sy) sy = y;
/*   60 */       if (y >= ly) { ly = y;
/*      */       }
/*   62 */       while (n != 0) {
/*   63 */         x += (short)comChannel.readShort();y += (short)comChannel.readShort();n--;
/*   64 */         graphics.drawLine(x, y, x, y);
/*      */         
/*   66 */         if (x <= sx) sx = x;
/*   67 */         if (x >= lx) lx = x;
/*   68 */         if (y <= sy) sy = y;
/*   69 */         if (y >= ly) { ly = y;
/*      */         }
/*      */       }
/*      */     }
/*      */     
/*   74 */     while (n != 0) {
/*   75 */       int x = (short)comChannel.readShort();int y = (short)comChannel.readShort();n--;
/*   76 */       graphics.drawLine(x, y, x, y);
/*      */       
/*   78 */       if (x <= sx) sx = x;
/*   79 */       if (x >= lx) lx = x;
/*   80 */       if (y <= sy) sy = y;
/*   81 */       if (y >= ly) { ly = y;
/*      */       }
/*      */     }
/*      */     
/*   85 */     if (sx < 0) sx = 0;
/*   86 */     if (sy < 0) sy = 0;
/*   87 */     if ((d instanceof XWindow)) {
/*   88 */       ((XWindow)d).draw(sx, sy, lx - sx + 1, ly - sy + 1);
/*      */     }
/*   90 */     if ((gc.function == 6) || (gc.function == 10))
/*      */     {
/*   92 */       graphics.setPaintMode();
/*      */     }
/*   94 */     if ((gc.clip_mask != null) && ((gc.clip_mask instanceof ClipRectangles))) {
/*   95 */       d.restoreClip();
/*      */     }
/*      */   }
/*      */   
/*      */   public static void reqFillPolyArc(XClient c, Drawable d, GC gc)
/*      */     throws IOException
/*      */   {
/*  102 */     int n = c.length;
/*  103 */     ComChannel comChannel = c.channel;
/*      */     
/*  105 */     Graphics graphics = d.getGraphics(gc, 32769);
/*  106 */     if (graphics == null) {
/*  107 */       comChannel.readPad(n * 4);
/*  108 */       return;
/*      */     }
/*      */     
/*  111 */     if ((gc.clip_mask != null) && ((gc.clip_mask instanceof ClipRectangles))) {
/*  112 */       Rectangle rec = (Rectangle)gc.clip_mask.getMask();
/*  113 */       if (rec == null) {
/*  114 */         comChannel.readPad(n * 4);
/*  115 */         return;
/*      */       }
/*      */     }
/*  118 */     n *= 4;
/*      */     
/*      */ 
/*  121 */     int sx = d.width;
/*  122 */     int sy = d.height;
/*  123 */     int lx = 0;
/*  124 */     int ly = 0;
/*  125 */     while (n != 0) {
/*  126 */       short x = (short)comChannel.readShort();short y = (short)comChannel.readShort();
/*  127 */       if (x < sx) sx = x;
/*  128 */       if (y < sy) { sy = y;
/*      */       }
/*  130 */       int width = comChannel.readShort();
/*  131 */       int height = comChannel.readShort();
/*      */       
/*  133 */       if (lx < x + width) lx = x + width;
/*  134 */       if (ly < y + height) { ly = y + height;
/*      */       }
/*  136 */       short a1 = (short)comChannel.readShort();short a2 = (short)comChannel.readShort();
/*  137 */       n -= 12;
/*  138 */       graphics.fillArc(x, y, width, height, a1 / 64, a2 / 64);
/*      */     }
/*      */     
/*  141 */     if ((d instanceof XWindow)) {
/*  142 */       if (sx < 0) sx = 0;
/*  143 */       if (sy < 0) sy = 0;
/*  144 */       ((XWindow)d).draw(sx, sy, lx - sx + 2, ly - sy + 2);
/*      */     }
/*      */     
/*  147 */     if ((gc.function == 6) || (gc.function == 10))
/*      */     {
/*  149 */       graphics.setPaintMode();
/*      */     }
/*  151 */     if ((gc.clip_mask != null) && ((gc.clip_mask instanceof ClipRectangles))) {
/*  152 */       d.restoreClip();
/*      */     }
/*      */   }
/*      */   
/*      */   public static void reqPolyArc(XClient c, Drawable d, GC gc) throws IOException
/*      */   {
/*  158 */     int n = c.length;
/*  159 */     ComChannel comChannel = c.channel;
/*      */     
/*  161 */     Graphics graphics = d.getGraphics(gc, 32785);
/*      */     
/*      */ 
/*      */ 
/*  165 */     if (graphics == null) {
/*  166 */       comChannel.readPad(n * 4);
/*  167 */       return;
/*      */     }
/*      */     
/*  170 */     if ((gc.clip_mask != null) && ((gc.clip_mask instanceof ClipRectangles))) {
/*  171 */       Rectangle rec = (Rectangle)gc.clip_mask.getMask();
/*  172 */       if (rec == null) {
/*  173 */         comChannel.readPad(n * 4);
/*  174 */         return;
/*      */       }
/*      */     }
/*      */     
/*  178 */     n *= 4;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*  183 */     int sx = d.width;
/*  184 */     int sy = d.height;
/*  185 */     int lx = 0;
/*  186 */     int ly = 0;
/*      */     
/*  188 */     while (n != 0) {
/*  189 */       short x = (short)comChannel.readShort();short y = (short)comChannel.readShort();
/*  190 */       if (x < sx) sx = x;
/*  191 */       if (y < sy) { sy = y;
/*      */       }
/*  193 */       int width = comChannel.readShort();int height = comChannel.readShort();
/*  194 */       if (lx < x + width) lx = x + width;
/*  195 */       if (ly < y + height) { ly = y + height;
/*      */       }
/*  197 */       short a1 = (short)comChannel.readShort();short a2 = (short)comChannel.readShort();
/*  198 */       n -= 12;
/*  199 */       graphics.drawArc(x, y, width, height, a1 / 64, a2 / 64);
/*      */     }
/*      */     
/*  202 */     if ((d instanceof XWindow)) {
/*  203 */       if (sx < 0) sx = 0;
/*  204 */       if (sy < 0) sy = 0;
/*  205 */       ((XWindow)d).draw(sx, sy, lx - sx + 2, ly - sy + 2);
/*      */     }
/*  207 */     if ((gc.function == 6) || (gc.function == 10))
/*      */     {
/*  209 */       graphics.setPaintMode();
/*      */     }
/*  211 */     if ((gc.clip_mask != null) && ((gc.clip_mask instanceof ClipRectangles))) {
/*  212 */       d.restoreClip();
/*      */     }
/*      */   }
/*      */   
/*      */   public static void reqPolyText16(XClient c, Drawable d, GC gc, int x, int y) throws IOException
/*      */   {
/*  218 */     int n = c.length;
/*      */     
/*      */ 
/*      */ 
/*  222 */     Graphics graphics = d.getGraphics(gc, 16385);
/*      */     
/*      */ 
/*  225 */     if (graphics == null) {
/*  226 */       c.channel.readPad(n * 4);
/*  227 */       return;
/*      */     }
/*      */     
/*  230 */     if ((gc.clip_mask != null) && ((gc.clip_mask instanceof ClipRectangles))) {
/*  231 */       Rectangle rec = (Rectangle)gc.clip_mask.getMask();
/*  232 */       if (rec == null) {
/*  233 */         while (n > 0) {
/*  234 */           c.channel.readPad(4);
/*  235 */           n--;
/*      */         }
/*  237 */         return;
/*      */       }
/*      */     }
/*      */     
/*  241 */     XFont XFont = gc.XFont;
/*      */     
/*  243 */     n *= 4;
/*      */     
/*  245 */     int sx = d.width;
/*  246 */     int sy = d.height;
/*  247 */     int lx = 0;
/*  248 */     int ly = 0;
/*      */     
/*  250 */     while ((n != 0) && 
/*  251 */       (n >= 2)) {
/*  252 */       int foo = c.channel.readByte();n--;
/*  253 */       if (foo == 255) {
/*  254 */         int i = 0;
/*  255 */         foo = c.channel.readByte();n--;i = foo & 0xFF;
/*  256 */         foo = c.channel.readByte();n--;i = i << 8 & 0xFFFF | foo & 0xFF;
/*  257 */         foo = c.channel.readByte();n--;i = i << 8 & 0xFFFFFF | foo & 0xFF;
/*  258 */         foo = c.channel.readByte();n--;i = i << 8 | foo & 0xFF;
/*  259 */         XFont tmp = (XFont)Resource.lookupIDByType(i, 4);
/*  260 */         if (tmp != null) { XFont = tmp;graphics.setFont(XFont.getFont());
/*  261 */         } else { System.out.println("font is null!!");
/*      */         }
/*      */       } else {
/*  264 */         int delta = c.channel.readByte();n--;
/*  265 */         x += delta;
/*  266 */         if (foo > 0) {
/*  267 */           foo *= 2;
/*  268 */           c.channel.readByte(c.bbuffer, 0, foo);
/*  269 */           n -= foo;
/*  270 */           if (XFont.encoding != null) {
/*  271 */             foo = XFont.encode(c.bbuffer, 0, foo, c.cbuffer);
/*  272 */             if (foo == 0) {
/*  273 */               c.length = n;
/*      */             }
/*      */           }
/*      */           else
/*      */           {
/*  278 */             for (int i = 0; i < foo; i++) {
/*  279 */               c.cbuffer[i] = ((char)(c.bbuffer[i] & 0xFF));
/*      */             }
/*  281 */             for (int i = 0; i < foo; i++) {
/*  282 */               if (c.cbuffer[i] != 0) {
/*  283 */                 c.cbuffer[(i / 2)] = c.cbuffer[i];
/*      */               }
/*      */             }
/*  286 */             foo /= 2;
/*      */           }
/*      */           
/*  289 */           graphics.drawChars(c.cbuffer, 0, foo, x, y);
/*  290 */           if (x < sx) sx = x;
/*  291 */           foo = XFont.charsWidth(c.cbuffer, 0, foo) + x;
/*  292 */           x = foo;
/*  293 */           if (lx < foo) lx = foo;
/*  294 */           if (y - XFont.ascent < sy) sy = y - XFont.ascent;
/*  295 */           if (ly < y + XFont.descent) ly = y + XFont.descent;
/*      */         }
/*      */       }
/*      */     }
/*  299 */     if (n != 0) {
/*  300 */       c.channel.readPad(n);
/*      */     }
/*      */     
/*  303 */     if (sx < 0) sx = 0;
/*  304 */     if (sy < 0) sy = 0;
/*  305 */     if ((d instanceof XWindow)) {
/*  306 */       ((XWindow)d).draw(sx, sy, lx - sx + 1, ly - sy + 1);
/*      */     }
/*      */     
/*  309 */     if ((gc.function == 6) || (gc.function == 10))
/*      */     {
/*  311 */       graphics.setPaintMode();
/*      */     }
/*  313 */     if ((gc.clip_mask != null) && ((gc.clip_mask instanceof ClipRectangles))) {
/*  314 */       d.restoreClip();
/*      */     }
/*      */   }
/*      */   
/*      */   public static void reqImageText16(XClient c, Drawable d, GC gc, int x, int y) throws IOException
/*      */   {
/*  320 */     int len = c.data;
/*  321 */     int n = c.length;
/*      */     
/*  323 */     Graphics graphics = d.getGraphics(gc, 16385);
/*  324 */     if (graphics == null) {
/*  325 */       c.channel.readPad(n * 4);
/*  326 */       return;
/*      */     }
/*      */     
/*  329 */     if ((gc.clip_mask != null) && ((gc.clip_mask instanceof ClipRectangles))) {
/*  330 */       Rectangle rec = (Rectangle)gc.clip_mask.getMask();
/*  331 */       if (rec == null) {
/*  332 */         while (n > 0) {
/*  333 */           c.channel.readPad(4);
/*  334 */           n--;
/*      */         }
/*  336 */         return;
/*      */       }
/*      */     }
/*      */     
/*  340 */     XFont XFont = gc.XFont;
/*      */     
/*  342 */     n *= 4;
/*      */     
/*  344 */     c.channel.readByte(c.bbuffer, 0, n);
/*      */     
/*  346 */     len *= 2;
/*  347 */     if (XFont.encoding != null) {
/*  348 */       len = XFont.encode(c.bbuffer, 0, len, c.cbuffer);
/*  349 */       if (len != 0) {}
/*      */ 
/*      */     }
/*      */     else
/*      */     {
/*  354 */       for (int i = 0; i < len; i++) {
/*  355 */         c.cbuffer[i] = ((char)(c.bbuffer[i] & 0xFF));
/*      */       }
/*  357 */       for (int i = 0; i < len; i++) {
/*  358 */         if (c.cbuffer[i] != 0) {
/*  359 */           c.cbuffer[(i / 2)] = c.cbuffer[i];
/*      */         }
/*      */       }
/*  362 */       len /= 2;
/*      */     }
/*      */     
/*      */ 
/*  366 */     Color tmp = graphics.getColor();
/*  367 */     graphics.setColor(d.getColormap().getColor(gc.bgPixel));
/*  368 */     graphics.fillRect(x, y - XFont.ascent, XFont.charsWidth(c.cbuffer, 0, len), XFont.ascent + XFont.descent);
/*      */     
/*      */ 
/*      */ 
/*  372 */     graphics.setColor(tmp);
/*  373 */     graphics.drawChars(c.cbuffer, 0, len, x, y);
/*      */     
/*      */ 
/*  376 */     if ((d instanceof XWindow)) {
/*  377 */       ((XWindow)d).draw(x, y - XFont.ascent, XFont.charsWidth(c.cbuffer, 0, len), XFont.ascent + XFont.descent);
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*  382 */     if ((gc.function == 6) || (gc.function == 10))
/*      */     {
/*  384 */       graphics.setPaintMode();
/*      */     }
/*      */     
/*  387 */     if ((gc.clip_mask != null) && ((gc.clip_mask instanceof ClipRectangles))) {
/*  388 */       d.restoreClip();
/*      */     }
/*      */   }
/*      */   
/*      */   public static void reqPolyText8(XClient c, Drawable d, GC gc, int x, int y) throws IOException
/*      */   {
/*  394 */     int n = c.length;
/*      */     
/*      */ 
/*  397 */     ComChannel comChannel = c.channel;
/*      */     
/*  399 */     Graphics graphics = d.getGraphics(gc, 16385);
/*      */     
/*      */ 
/*  402 */     if (graphics == null) {
/*  403 */       c.channel.readPad(n * 4);
/*  404 */       return;
/*      */     }
/*      */     
/*  407 */     if ((gc.clip_mask != null) && ((gc.clip_mask instanceof ClipRectangles))) {
/*  408 */       Rectangle rec = (Rectangle)gc.clip_mask.getMask();
/*  409 */       if (rec == null) {
/*  410 */         comChannel.readPad(n * 4);
/*  411 */         return;
/*      */       }
/*      */     }
/*      */     
/*  415 */     XFont XFont = gc.XFont;
/*      */     
/*      */ 
/*  418 */     n *= 4;
/*      */     
/*  420 */     int sx = d.width;
/*  421 */     int sy = d.height;
/*  422 */     int lx = 0;
/*  423 */     int ly = 0;
/*      */     
/*  425 */     while ((n != 0) && 
/*  426 */       (n >= 2)) {
/*  427 */       int foo = comChannel.readByte();n--;
/*  428 */       if (foo == 255) {
/*  429 */         int i = 0;
/*  430 */         foo = comChannel.readByte();n--;i = foo & 0xFF;
/*  431 */         foo = comChannel.readByte();n--;i = i << 8 & 0xFFFF | foo & 0xFF;
/*  432 */         foo = comChannel.readByte();n--;i = i << 8 & 0xFFFFFF | foo & 0xFF;
/*  433 */         foo = comChannel.readByte();n--;i = i << 8 | foo & 0xFF;
/*  434 */         XFont tmp = (XFont)Resource.lookupIDByType(i, 4);
/*  435 */         if (tmp != null) { XFont = tmp;graphics.setFont(XFont.getFont());
/*  436 */         } else { System.out.println("font is null!!");
/*      */         }
/*      */       }
/*      */       else {
/*  440 */         int delta = comChannel.readByte();n--;
/*  441 */         x += delta;
/*  442 */         if (foo > 0) {
/*  443 */           comChannel.readByte(c.bbuffer, 0, foo);
/*  444 */           n -= foo;
/*      */           
/*  446 */           if (XFont.encoding != null) {
/*  447 */             foo = XFont.encode(c.bbuffer, 0, foo, c.cbuffer);
/*      */           }
/*      */           else {
/*  450 */             char ccc = '\000';
/*  451 */             for (int i = 0; i < foo; i++) {
/*  452 */               ccc = c.cbuffer[i] = (char)(c.bbuffer[i] & 0xFF);
/*  453 */               if (ccc < ' ') {
/*  454 */                 if (ccc == '\013') { c.cbuffer[i] = '+';
/*  455 */                 } else if (ccc == '\f') { c.cbuffer[i] = '+';
/*  456 */                 } else if (ccc == '\r') { c.cbuffer[i] = '+';
/*  457 */                 } else if (ccc == '\016') { c.cbuffer[i] = '+';
/*  458 */                 } else if (ccc == '\017') { c.cbuffer[i] = '+';
/*  459 */                 } else if (ccc == '\022') { c.cbuffer[i] = '-';
/*  460 */                 } else if (ccc == '\025') { c.cbuffer[i] = '+';
/*  461 */                 } else if (ccc == '\026') { c.cbuffer[i] = '+';
/*  462 */                 } else if (ccc == '\027') { c.cbuffer[i] = '+';
/*  463 */                 } else if (ccc == '\030') { c.cbuffer[i] = '+';
/*  464 */                 } else if (ccc == '\031') c.cbuffer[i] = '|'; else
/*  465 */                   c.cbuffer[i] = ' ';
/*      */               }
/*      */             }
/*      */           }
/*  469 */           graphics.drawChars(c.cbuffer, 0, foo, x, y);
/*      */           
/*  471 */           if (x < sx) sx = x;
/*  472 */           foo = XFont.charsWidth(c.cbuffer, 0, foo) + x;
/*      */           
/*  474 */           x = foo;
/*      */           
/*  476 */           if (lx < foo) lx = foo;
/*  477 */           if (y - XFont.ascent < sy) sy = y - XFont.ascent;
/*  478 */           if (ly < y + XFont.descent) ly = y + XFont.descent;
/*      */         }
/*      */       }
/*      */     }
/*  482 */     if (n != 0) {
/*  483 */       comChannel.readPad(n);
/*      */     }
/*      */     
/*  486 */     if (sx < 0) sx = 0;
/*  487 */     if (sy < 0) sy = 0;
/*  488 */     if ((d instanceof XWindow)) {
/*  489 */       ((XWindow)d).draw(sx, sy, lx - sx + 1, ly - sy + 1);
/*      */     }
/*  491 */     if ((gc.function == 6) || (gc.function == 10))
/*      */     {
/*  493 */       graphics.setPaintMode();
/*      */     }
/*  495 */     if ((gc.clip_mask != null) && ((gc.clip_mask instanceof ClipRectangles))) {
/*  496 */       d.restoreClip();
/*      */     }
/*      */   }
/*      */   
/*      */   public static void reqImageText8(XClient c, Drawable d, GC gc, int x, int y) throws IOException
/*      */   {
/*  502 */     int len = c.data;
/*  503 */     int n = c.length;
/*      */     
/*  505 */     ComChannel comChannel = c.channel;
/*      */     
/*  507 */     Graphics graphics = d.getGraphics(gc, 16385);
/*  508 */     if (graphics == null) {
/*  509 */       comChannel.readPad(n * 4);
/*  510 */       return;
/*      */     }
/*      */     
/*  513 */     if ((gc.clip_mask != null) && ((gc.clip_mask instanceof ClipRectangles))) {
/*  514 */       Rectangle rec = (Rectangle)gc.clip_mask.getMask();
/*  515 */       if (rec == null) {
/*  516 */         comChannel.readPad(4 * n);
/*  517 */         return;
/*      */       }
/*      */     }
/*      */     
/*  521 */     XFont XFont = gc.XFont;
/*      */     
/*      */ 
/*  524 */     comChannel.readByte(c.bbuffer, 0, len);
/*      */     
/*      */ 
/*  527 */     Color tmp = graphics.getColor();
/*  528 */     graphics.setColor(d.getColormap().getColor(gc.bgPixel));
/*      */     
/*  530 */     if (len > 0) {
/*  531 */       comChannel.readPad(-len & 0x3);
/*      */     }
/*      */     
/*  534 */     if (XFont.encoding != null) {
/*  535 */       len = XFont.encode(c.bbuffer, 0, len, c.cbuffer);
/*      */     }
/*      */     else {
/*  538 */       char ccc = '\000';
/*  539 */       for (int i = 0; i < len; i++) {
/*  540 */         ccc = c.cbuffer[i] = (char)(c.bbuffer[i] & 0xFF);
/*  541 */         if (ccc < ' ') {
/*  542 */           if (ccc == '\013') { c.cbuffer[i] = '+';
/*  543 */           } else if (ccc == '\f') { c.cbuffer[i] = '+';
/*  544 */           } else if (ccc == '\r') { c.cbuffer[i] = '+';
/*  545 */           } else if (ccc == '\016') { c.cbuffer[i] = '+';
/*  546 */           } else if (ccc == '\017') { c.cbuffer[i] = '+';
/*  547 */           } else if (ccc == '\022') { c.cbuffer[i] = '-';
/*  548 */           } else if (ccc == '\025') { c.cbuffer[i] = '+';
/*  549 */           } else if (ccc == '\026') { c.cbuffer[i] = '+';
/*  550 */           } else if (ccc == '\027') { c.cbuffer[i] = '+';
/*  551 */           } else if (ccc == '\030') { c.cbuffer[i] = '+';
/*  552 */           } else if (ccc == '\031') c.cbuffer[i] = '|'; else
/*  553 */             c.cbuffer[i] = ' ';
/*      */         }
/*      */       }
/*      */     }
/*  557 */     graphics.fillRect(x, y - XFont.ascent, XFont.charsWidth(c.cbuffer, 0, len), XFont.ascent + XFont.descent);
/*      */     
/*      */ 
/*  560 */     graphics.setColor(tmp);
/*  561 */     graphics.drawChars(c.cbuffer, 0, len, x, y);
/*      */     
/*      */ 
/*  564 */     if ((d instanceof XWindow)) {
/*  565 */       ((XWindow)d).draw(x, y - XFont.ascent, XFont.charsWidth(c.cbuffer, 0, len), XFont.ascent + XFont.descent);
/*      */     }
/*      */     
/*      */ 
/*  569 */     if ((gc.function == 6) || (gc.function == 10))
/*      */     {
/*  571 */       graphics.setPaintMode();
/*      */     }
/*      */     
/*  574 */     if ((gc.clip_mask != null) && ((gc.clip_mask instanceof ClipRectangles))) {
/*  575 */       d.restoreClip();
/*      */     }
/*      */   }
/*      */   
/*      */   public static void reqPolySegment(XClient c, Drawable d, GC gc)
/*      */     throws IOException
/*      */   {
/*  582 */     int n = c.length;
/*      */     
/*  584 */     Graphics graphics = d.getGraphics(gc, 32785);
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*  589 */     if (graphics == null) {
/*  590 */       c.channel.readPad(n * 4);
/*  591 */       return;
/*      */     }
/*      */     
/*  594 */     if ((gc.clip_mask != null) && ((gc.clip_mask instanceof ClipRectangles))) {
/*  595 */       Rectangle rec = (Rectangle)gc.clip_mask.getMask();
/*  596 */       if (rec == null) {
/*  597 */         while (n > 0) {
/*  598 */           c.channel.readPad(4);
/*  599 */           n--;
/*      */         }
/*  601 */         return;
/*      */       }
/*      */     }
/*      */     
/*  605 */     n /= 2;
/*  606 */     int sx = d.width;
/*  607 */     int sy = d.height;
/*  608 */     int lx = 0;
/*  609 */     int ly = 0;
/*  610 */     while (n != 0) {
/*  611 */       int foo = c.xarray[0] = (short)c.channel.readShort();
/*  612 */       if (foo <= sx) sx = foo;
/*  613 */       if (foo >= lx) lx = foo;
/*  614 */       foo = c.yarray[0] = (short)c.channel.readShort();
/*  615 */       if (foo <= sy) sy = foo;
/*  616 */       if (foo >= ly) ly = foo;
/*  617 */       foo = c.xarray[1] = (short)c.channel.readShort();
/*  618 */       if (foo <= sx) sx = foo;
/*  619 */       if (foo >= lx) lx = foo;
/*  620 */       foo = c.yarray[1] = (short)c.channel.readShort();
/*  621 */       if (foo <= sy) sy = foo;
/*  622 */       if (foo >= ly) ly = foo;
/*  623 */       if (gc.lineWidth <= 1) {
/*  624 */         graphics.drawPolyline(c.xarray, c.yarray, 2);
/*      */       }
/*  626 */       else if (c.yarray[0] == c.yarray[1]) {
/*  627 */         graphics.drawPolyline(c.xarray, c.yarray, 2);
/*  628 */         c.yarray[0] -= gc.lineWidth / 2;
/*  629 */         c.yarray[1] -= gc.lineWidth / 2;
/*  630 */         for (int i = 0; i < gc.lineWidth; i++) {
/*  631 */           graphics.drawPolyline(c.xarray, c.yarray, 2);
/*  632 */           c.yarray[0] += 1;
/*  633 */           c.yarray[1] += 1;
/*      */         }
/*      */       }
/*      */       else {
/*  637 */         drawThickLine(graphics, c.xarray[0], c.yarray[0], c.xarray[1], c.yarray[1], gc.lineWidth);
/*      */       }
/*      */       
/*      */ 
/*  641 */       n--;
/*      */     }
/*  643 */     if (sx < 0) sx = 0;
/*  644 */     if (sy < 0) { sy = 0;
/*      */     }
/*  646 */     if ((d instanceof XWindow)) {
/*  647 */       if ((gc.attr & 0x400) == 0) {
/*  648 */         ((XWindow)d).draw(sx, sy, lx - sx + 1, ly - sy + 1);
/*      */       }
/*      */     }
/*      */     else {
/*  652 */       ((Pixmap)d).draw(sx, sy, lx - sx + 1, ly - sy + 1);
/*      */     }
/*      */     
/*  655 */     if ((gc.function == 6) || (gc.function == 10))
/*      */     {
/*  657 */       graphics.setPaintMode();
/*      */     }
/*  659 */     if ((gc.clip_mask != null) && ((gc.clip_mask instanceof ClipRectangles))) {
/*  660 */       d.restoreClip();
/*      */     }
/*      */   }
/*      */   
/*      */   private static void drawThickLine(Graphics graphics, int x1, int y1, int x2, int y2, int linewidth)
/*      */   {
/*  666 */     linewidth--;
/*  667 */     int lw2 = linewidth / 2;
/*  668 */     graphics.fillOval(x1 - lw2, y1 - lw2, linewidth, linewidth);
/*  669 */     if ((x1 == x2) && (y1 == y2))
/*  670 */       return;
/*  671 */     if (Math.abs(x2 - x1) > Math.abs(y2 - y1)) {
/*      */       int dx;
/*      */       int dx;
/*  674 */       if (x2 > x1) {
/*  675 */         dx = 1;
/*      */       } else
/*  677 */         dx = -1;
/*  678 */       int dy = (y2 - y1) * 8192 / Math.abs(x2 - x1);
/*  679 */       int row; int prevrow = row = y1;
/*  680 */       int srow = row * 8192 + 4096;
/*  681 */       int col = x1;
/*      */       for (;;) {
/*  683 */         if (row != prevrow) {
/*  684 */           graphics.drawOval(col - lw2, prevrow - lw2, linewidth, linewidth);
/*  685 */           prevrow = row;
/*      */         }
/*  687 */         graphics.drawOval(col - lw2, row - lw2, linewidth, linewidth);
/*  688 */         if (col == x2)
/*      */           break;
/*  690 */         srow += dy;
/*  691 */         row = srow / 8192;
/*  692 */         col += dx;
/*      */       }
/*      */     }
/*      */     else {
/*      */       int dy;
/*      */       int dy;
/*  698 */       if (y2 > y1) {
/*  699 */         dy = 1;
/*      */       } else
/*  701 */         dy = -1;
/*  702 */       int dx = (x2 - x1) * 8192 / Math.abs(y2 - y1);
/*  703 */       int row = y1;
/*  704 */       int col; int prevcol = col = x1;
/*  705 */       int scol = col * 8192 + 4096;
/*      */       for (;;) {
/*  707 */         if (col != prevcol) {
/*  708 */           graphics.drawOval(prevcol - lw2, row - lw2, linewidth, linewidth);
/*  709 */           prevcol = col;
/*      */         }
/*  711 */         graphics.drawOval(col - lw2, row - lw2, linewidth, linewidth);
/*  712 */         if (row == y2)
/*      */           break;
/*  714 */         row += dy;
/*  715 */         scol += dx;
/*  716 */         col = scol / 8192;
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */   public static void reqPolyLine(XClient c, Drawable d, GC gc) throws IOException {
/*  722 */     int n = c.length;
/*      */     
/*      */ 
/*  725 */     Graphics graphics = d.getGraphics(gc, 32785);
/*      */     
/*      */ 
/*      */ 
/*  729 */     if (graphics == null) {
/*  730 */       c.channel.readPad(n * 4);
/*  731 */       return;
/*      */     }
/*      */     
/*  734 */     int mode = c.data;
/*      */     
/*  736 */     if ((gc.clip_mask != null) && ((gc.clip_mask instanceof ClipRectangles))) {
/*  737 */       Rectangle rec = (Rectangle)gc.clip_mask.getMask();
/*  738 */       if (rec == null) {
/*  739 */         while (n > 0) {
/*  740 */           c.channel.readPad(4);
/*  741 */           n--;
/*      */         }
/*  743 */         return;
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*  748 */     if (c.xarray.length < n) {
/*  749 */       c.xarray = new int[n];
/*  750 */       c.yarray = new int[n];
/*      */     }
/*  752 */     int sx = d.width;
/*  753 */     int sy = d.height;
/*  754 */     int lx = 0;
/*  755 */     int ly = 0;
/*  756 */     int foo = c.xarray[0] = (short)c.channel.readShort();
/*  757 */     if (foo <= sx) sx = foo;
/*  758 */     if (foo >= lx) lx = foo;
/*  759 */     foo = c.yarray[0] = (short)c.channel.readShort();
/*  760 */     if (foo <= sy) sy = foo;
/*  761 */     if (foo >= ly) ly = foo;
/*  762 */     for (int i = 1; i < n; i++) {
/*  763 */       c.xarray[i] = ((short)c.channel.readShort());
/*  764 */       c.yarray[i] = ((short)c.channel.readShort());
/*  765 */       if (mode == 1) {
/*  766 */         c.xarray[i] += c.xarray[(i - 1)];c.yarray[i] += c.yarray[(i - 1)];
/*      */       }
/*  768 */       foo = c.xarray[i];
/*  769 */       if (foo <= sx) sx = foo;
/*  770 */       if (foo >= lx) lx = foo;
/*  771 */       foo = c.yarray[i];
/*  772 */       if (foo <= sy) sy = foo;
/*  773 */       if (foo >= ly) { ly = foo;
/*      */       }
/*      */     }
/*  776 */     if ((gc.lineWidth > 1) && (n > 1)) {
/*  777 */       n--;
/*  778 */       for (int i = 0; i < n; i++) {
/*  779 */         int j = i + 1;
/*  780 */         drawThickLine(graphics, c.xarray[i], c.yarray[i], c.xarray[j], c.yarray[j], gc.lineWidth);
/*      */       }
/*      */     }
/*      */     else
/*      */     {
/*  785 */       graphics.drawPolyline(c.xarray, c.yarray, n);
/*      */     }
/*  787 */     if ((d instanceof XWindow)) {
/*  788 */       ((XWindow)d).draw(sx, sy, lx - sx + 1, ly - sy + 1);
/*      */     }
/*      */     else {
/*  791 */       ((Pixmap)d).draw(sx, sy, lx - sx + 1, ly - sy + 1);
/*      */     }
/*      */     
/*  794 */     if ((gc.function == 6) || (gc.function == 10)) {
/*  795 */       graphics.setPaintMode();
/*      */     }
/*  797 */     if ((gc.clip_mask != null) && ((gc.clip_mask instanceof ClipRectangles))) {
/*  798 */       d.restoreClip();
/*      */     }
/*      */   }
/*      */   
/*      */   public static void reqFillPoly(XClient c, Drawable d, GC gc) throws IOException {
/*  803 */     int n = c.length;
/*      */     
/*      */ 
/*  806 */     Graphics graphics = d.getGraphics(gc, 32769);
/*      */     
/*      */ 
/*  809 */     if (graphics == null) {
/*  810 */       c.channel.readPad(n * 4);
/*  811 */       return;
/*      */     }
/*      */     
/*      */ 
/*  815 */     byte shape = (byte)c.channel.readByte();
/*      */     
/*  817 */     byte cmode = (byte)c.channel.readByte();
/*  818 */     c.channel.readPad(2);
/*      */     
/*  820 */     n--;
/*      */     
/*  822 */     if ((gc.clip_mask != null) && ((gc.clip_mask instanceof ClipRectangles))) {
/*  823 */       Rectangle rec = (Rectangle)gc.clip_mask.getMask();
/*  824 */       if (rec == null) {
/*  825 */         while (n > 0) {
/*  826 */           c.channel.readPad(4);
/*  827 */           n--;
/*      */         }
/*  829 */         return;
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*  835 */     int sx = d.width;
/*  836 */     int sy = d.height;
/*  837 */     int lx = 0;
/*  838 */     int ly = 0;
/*      */     
/*  840 */     if (c.xarray.length < n) {
/*  841 */       c.xarray = new int[n];
/*  842 */       c.yarray = new int[n];
/*      */     }
/*      */     
/*  845 */     int foo = c.xarray[0] = (short)c.channel.readShort();
/*  846 */     if (foo < sx) sx = foo;
/*  847 */     if (lx < foo) { lx = foo;
/*      */     }
/*  849 */     foo = c.yarray[0] = (short)c.channel.readShort();
/*  850 */     if (foo < sy) sy = foo;
/*  851 */     if (ly < foo) { ly = foo;
/*      */     }
/*  853 */     for (int i = 1; i < n; i++) {
/*  854 */       c.xarray[i] = ((short)c.channel.readShort());
/*  855 */       c.yarray[i] = ((short)c.channel.readShort());
/*  856 */       if (cmode == 1) {
/*  857 */         c.xarray[i] += c.xarray[(i - 1)];
/*  858 */         c.yarray[i] += c.yarray[(i - 1)];
/*      */       }
/*  860 */       foo = c.xarray[i];
/*  861 */       if (foo < sx) sx = foo;
/*  862 */       if (lx < foo) { lx = foo;
/*      */       }
/*  864 */       foo = c.yarray[i];
/*  865 */       if (foo < sy) sy = foo;
/*  866 */       if (ly < foo) ly = foo;
/*      */     }
/*  868 */     graphics.fillPolygon(c.xarray, c.yarray, n);
/*  869 */     if (sx < 0) sx = 0;
/*  870 */     if (sy < 0) sy = 0;
/*  871 */     if ((d instanceof XWindow)) {
/*  872 */       ((XWindow)d).draw(sx, sy, lx - sx + 1, ly - sy + 1);
/*      */     }
/*  874 */     if ((gc.function == 6) || (gc.function == 10))
/*      */     {
/*  876 */       graphics.setPaintMode();
/*      */     }
/*  878 */     if ((gc.clip_mask != null) && ((gc.clip_mask instanceof ClipRectangles))) {
/*  879 */       d.restoreClip();
/*      */     }
/*      */   }
/*      */   
/*      */   public static void reqPolyRectangle(XClient c, Drawable d, GC gc)
/*      */     throws IOException
/*      */   {
/*  886 */     int n = c.length;
/*      */     
/*      */ 
/*  889 */     Graphics graphics = d.getGraphics(gc, 32769);
/*      */     
/*      */ 
/*  892 */     if (graphics == null) {
/*  893 */       c.channel.readPad(n * 4);
/*  894 */       return;
/*      */     }
/*      */     
/*  897 */     if ((gc.clip_mask != null) && ((gc.clip_mask instanceof ClipRectangles))) {
/*  898 */       Rectangle rec = (Rectangle)gc.clip_mask.getMask();
/*  899 */       if (rec == null) {
/*  900 */         while (n > 0) {
/*  901 */           c.channel.readPad(4);
/*  902 */           n--;
/*      */         }
/*  904 */         return;
/*      */       }
/*      */     }
/*      */     
/*  908 */     n /= 2;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*  913 */     int sx = d.width;
/*  914 */     int sy = d.height;
/*  915 */     int lx = 0;
/*  916 */     int ly = 0;
/*      */     
/*  918 */     while (n != 0) {
/*  919 */       short x = (short)c.channel.readShort();short y = (short)c.channel.readShort();
/*  920 */       int ww = c.channel.readShort();int hh = c.channel.readShort();
/*      */       
/*  922 */       if (x < sx) sx = x;
/*  923 */       if (lx < x + ww) lx = x + ww;
/*  924 */       if (y < sy) sy = y;
/*  925 */       if (ly < y + hh) { ly = y + hh;
/*      */       }
/*  927 */       if (gc.lineWidth > 1) {
/*  928 */         int l2 = gc.lineWidth / 2;
/*  929 */         x = (short)(x - l2);y = (short)(y - l2);
/*  930 */         ww += gc.lineWidth;hh += gc.lineWidth;
/*  931 */         for (int i = 0; i < gc.lineWidth; i++) {
/*  932 */           ww -= 2;hh -= 2;x = (short)(x + 1);y = (short)(y + 1);
/*  933 */           graphics.drawRect(x, y, ww, hh);
/*      */         }
/*      */       }
/*      */       else {
/*  937 */         graphics.drawRect(x, y, ww, hh);
/*      */       }
/*  939 */       n--;
/*      */     }
/*      */     
/*  942 */     if (sx < 0) sx = 0;
/*  943 */     if (sy < 0) sy = 0;
/*  944 */     if ((d instanceof XWindow)) {
/*  945 */       ((XWindow)d).draw(sx, sy, lx - sx + 2, ly - sy + 2);
/*      */     }
/*  947 */     if ((gc.function == 6) || (gc.function == 10))
/*      */     {
/*  949 */       graphics.setPaintMode();
/*      */     }
/*  951 */     if ((gc.clip_mask != null) && ((gc.clip_mask instanceof ClipRectangles))) {
/*  952 */       d.restoreClip();
/*      */     }
/*      */   }
/*      */   
/*      */   public static void reqPolyFillRectangle(XClient c, Drawable d, GC gc) throws IOException
/*      */   {
/*  958 */     int n = c.length;
/*      */     
/*      */ 
/*  961 */     Graphics graphics = d.getGraphics(gc, 32769);
/*      */     
/*      */ 
/*  964 */     if (graphics == null) {
/*  965 */       c.channel.readPad(n * 4);
/*  966 */       return;
/*      */     }
/*      */     
/*  969 */     if ((gc.clip_mask != null) && ((gc.clip_mask instanceof ClipPixmap)) && ((d instanceof Pixmap)) && (((Pixmap)d).data != null))
/*      */     {
/*      */ 
/*      */ 
/*      */ 
/*  974 */       Pixmap p = (Pixmap)d;
/*  975 */       byte[] data = p.data;
/*      */       
/*  977 */       Pixmap cpixmap = (Pixmap)gc.clip_mask.getMask();
/*      */       
/*      */ 
/*      */ 
/*      */ 
/*  982 */       byte f = (byte)gc.fgPixel;
/*      */       
/*  984 */       while (n != 0) {
/*  985 */         short x = (short)c.channel.readShort();
/*  986 */         short y = (short)c.channel.readShort();
/*  987 */         int ww = c.channel.readShort();
/*  988 */         int hh = c.channel.readShort();
/*  989 */         n -= 2;
/*      */         
/*  991 */         if (x < 0) { ww += x;x = 0; }
/*  992 */         if (y < 0) { hh += y;y = 0; }
/*  993 */         if ((d.width > x) && (d.height > y) && 
/*  994 */           (ww > 0) && (hh > 0))
/*      */         {
/*      */ 
/*  997 */           if (ww > cpixmap.width) ww = cpixmap.width;
/*  998 */           if (hh > cpixmap.height) hh = cpixmap.height;
/*  999 */           if (ww > d.width) ww = d.width;
/* 1000 */           if (hh > d.height) hh = d.height;
/* 1001 */           for (int i = 0; i < hh; i++) {
/* 1002 */             for (int j = 0; j < ww; j++) {
/* 1003 */               if (p.data[((i + y) * p.width + x + j)] == 0)
/* 1004 */                 p.data[((i + y) * p.width + x + j)] = f;
/*      */             }
/*      */           }
/*      */         }
/*      */       }
/* 1009 */       return;
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
/* 1051 */     if ((gc.clip_mask != null) && ((gc.clip_mask instanceof ClipRectangles))) {
/* 1052 */       Rectangle rec = (Rectangle)gc.clip_mask.getMask();
/* 1053 */       if (rec == null) {
/* 1054 */         while (n > 0) {
/* 1055 */           c.channel.readPad(8);
/* 1056 */           n -= 2;
/*      */         }
/* 1058 */         return;
/*      */       }
/*      */     }
/*      */     
/* 1062 */     Pixmap p = null;
/* 1063 */     if (((gc.attr & 0xC0) == 192) || ((gc.attr & 0xC0) == 128))
/*      */     {
/* 1065 */       p = gc.stipple;
/*      */     }
/* 1067 */     if ((gc.attr & 0xC0) == 64) {
/* 1068 */       p = gc.tile.pixmap;
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/* 1074 */     int sx = d.width;
/* 1075 */     int sy = d.height;
/* 1076 */     int lx = 0;
/* 1077 */     int ly = 0;
/*      */     
/* 1079 */     while (n != 0) {
/* 1080 */       short x = (short)c.channel.readShort();
/* 1081 */       short y = (short)c.channel.readShort();
/* 1082 */       int ww = c.channel.readShort();
/* 1083 */       int hh = c.channel.readShort();
/*      */       
/* 1085 */       n -= 2;
/*      */       
/* 1087 */       if (x < 0) { ww += x;x = 0; }
/* 1088 */       if (y < 0) { hh += y;y = 0; }
/* 1089 */       if ((d.width > x) && (d.height > y) && 
/* 1090 */         (ww > 0) && (hh > 0))
/*      */       {
/* 1092 */         if (x < sx) sx = x;
/* 1093 */         if (lx < x + ww) lx = x + ww;
/* 1094 */         if (y < sy) sy = y;
/* 1095 */         if (ly < y + hh) { ly = y + hh;
/*      */         }
/* 1097 */         if (((gc.attr & 0xC0) == 192) || ((gc.attr & 0xC0) == 128) || ((gc.attr & 0xC0) == 64))
/*      */         {
/*      */ 
/*      */ 
/* 1101 */           Shape tmp = null;
/* 1102 */           ww += x;hh += y;
/* 1103 */           if ((p.width > ww - x) || (p.height > hh - y)) {
/* 1104 */             tmp = graphics.getClip();
/* 1105 */             graphics.clipRect(x, y, ww - x, hh - y);
/*      */           }
/* 1107 */           graphics.drawImage(p.img, x, y, Screen.screen[0].root.ddxwindow);
/* 1108 */           if (tmp != null) graphics.setClip(tmp);
/* 1109 */           for (int i = x + p.width; i < ww; i += p.width) {
/* 1110 */             int www = p.width;
/* 1111 */             int hhh = p.height;
/* 1112 */             if (i + www > ww) www = ww - i;
/* 1113 */             if (y + hhh >= hh) hhh = hh - y;
/* 1114 */             graphics.copyArea(x, y, www, hhh, i - x, 0);
/*      */           }
/* 1116 */           for (int j = y + p.height; j < hh; j += p.height) {
/* 1117 */             int hhh = p.height;
/* 1118 */             if (j + hhh >= hh) hhh = hh - j;
/* 1119 */             graphics.copyArea(x, y, ww - x, hhh, 0, j - y);
/*      */           }
/*      */         }
/*      */         else {
/* 1123 */           graphics.fillRect(x, y, ww, hh);
/*      */         }
/*      */       }
/*      */     }
/* 1127 */     if (sx < 0) sx = 0;
/* 1128 */     if (sy < 0) sy = 0;
/* 1129 */     if ((d instanceof XWindow)) {
/* 1130 */       ((XWindow)d).draw(sx, sy, lx - sx + 2, ly - sy + 2);
/*      */     }
/*      */     
/* 1133 */     if ((gc.function == 6) || (gc.function == 10)) {
/* 1134 */       graphics.setPaintMode();
/*      */     }
/*      */     
/* 1137 */     if ((gc.clip_mask != null) && ((gc.clip_mask instanceof ClipRectangles))) {
/* 1138 */       d.restoreClip();
/*      */     }
/*      */   }
/*      */ }


/* Location:              C:\Users\Joey\Downloads\launch.jar!\com\emt\proteus\xserver\display\Draw.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */