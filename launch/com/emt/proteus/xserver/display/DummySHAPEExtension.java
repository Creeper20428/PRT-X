/*     */ package com.emt.proteus.xserver.display;
/*     */ 
/*     */ import com.emt.proteus.xserver.client.XClient;
/*     */ import com.emt.proteus.xserver.io.ComChannel;
/*     */ import java.awt.Rectangle;
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
/*     */ public final class DummySHAPEExtension
/*     */   extends Extension
/*     */ {
/*     */   static final int ShapeSet = 0;
/*     */   static final int ShapeUnion = 1;
/*     */   static final int ShapeIntersect = 2;
/*     */   static final int ShapeSubtract = 3;
/*     */   static final int ShapeInvert = 4;
/*     */   static final int ShapeBounding = 0;
/*     */   static final int ShapeClip = 1;
/*     */   static int ctyp;
/*     */   static int etyp;
/*     */   
/*     */   public DummySHAPEExtension()
/*     */   {
/*  46 */     this.eventcount = 1;
/*  47 */     this.errorcount = 0;
/*  48 */     ctyp = Resource.newType();
/*  49 */     etyp = Resource.newType();
/*  50 */     this.name = "SHAPE";
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void dispatch(XClient c)
/*     */     throws IOException
/*     */   {
/*  58 */     ComChannel comChannel = c.channel;
/*     */     int op;
/*  60 */     int kind; int foo; XWindow XWindow; int x; int y; switch (c.data) {
/*     */     case 0: 
/*  62 */       synchronized (comChannel) {
/*  63 */         comChannel.writeByte(1);
/*  64 */         comChannel.writePad(1);
/*  65 */         comChannel.writeShort(c.getSequence());
/*  66 */         comChannel.writeInt(0);
/*  67 */         comChannel.writeShort(1);
/*  68 */         comChannel.writeShort(0);
/*  69 */         comChannel.writePad(20);
/*  70 */         comChannel.flush();
/*     */       }
/*  72 */       break;
/*     */     case 1: 
/*  74 */       op = comChannel.readByte();
/*  75 */       kind = comChannel.readByte();
/*  76 */       int oder = comChannel.readByte();
/*  77 */       comChannel.readPad(1);
/*  78 */       foo = comChannel.readInt();
/*  79 */       c.length -= 3;
/*  80 */       XWindow = c.lookupWindow(foo);
/*  81 */       if (XWindow == null) {
/*  82 */         c.errorValue = foo;
/*  83 */         c.errorReason = 3;
/*  84 */         return;
/*     */       }
/*  86 */       x = (short)comChannel.readShort();
/*  87 */       y = (short)comChannel.readShort();
/*  88 */       c.length -= 1;
/*  89 */       int len = c.length / 2;
/*  90 */       Rectangle[] rect = new Rectangle[len];
/*     */       
/*     */ 
/*  93 */       while (len > 0) {
/*  94 */         int xx = (short)comChannel.readShort();
/*  95 */         int yy = (short)comChannel.readShort();
/*  96 */         int ww = (short)comChannel.readShort();
/*  97 */         int hh = (short)comChannel.readShort();
/*  98 */         rect[(rect.length - len)] = new Rectangle(xx, yy, ww, hh);
/*  99 */         len--;
/*     */       }
/*     */       
/*     */ 
/*     */ 
/*     */ 
/* 105 */       break;
/*     */     case 2: 
/* 107 */       op = comChannel.readByte();
/* 108 */       kind = comChannel.readByte();
/* 109 */       comChannel.readPad(2);
/* 110 */       foo = comChannel.readInt();
/* 111 */       c.length -= 3;
/* 112 */       XWindow = c.lookupWindow(foo);
/* 113 */       if (XWindow == null) {
/* 114 */         c.errorValue = foo;
/* 115 */         c.errorReason = 3;
/* 116 */         return;
/*     */       }
/* 118 */       x = (short)comChannel.readShort();
/* 119 */       y = (short)comChannel.readShort();
/* 120 */       foo = comChannel.readInt();
/* 121 */       c.length -= 2;
/* 122 */       if (foo == 0) {
/* 123 */         sendShapeNotify(kind, XWindow, x, y, XWindow.width, XWindow.height, 0);
/* 124 */         return;
/*     */       }
/*     */       
/* 127 */       Drawable d = c.lookupDrawable(foo);
/* 128 */       if ((d == null) || (!(d instanceof Pixmap))) {
/* 129 */         c.errorValue = foo;
/* 130 */         c.errorReason = 4;
/* 131 */         return;
/*     */       }
/* 133 */       Pixmap pixmap = (Pixmap)d;
/* 134 */       if (pixmap.depth != 1) {
/* 135 */         c.errorValue = foo;
/* 136 */         c.errorReason = 8;
/* 137 */         return;
/*     */       }
/*     */       
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 145 */       int w = pixmap.width;int h = pixmap.height;int shaped = 1;
/* 146 */       sendShapeNotify(kind, XWindow, x, y, w, h, shaped);
/* 147 */       break;
/*     */     
/*     */     case 3: 
/* 150 */       op = comChannel.readByte();
/* 151 */       int dk = comChannel.readByte();
/* 152 */       int sk = comChannel.readByte();
/* 153 */       comChannel.readPad(1);
/* 154 */       foo = comChannel.readInt();
/* 155 */       c.length -= 3;
/* 156 */       XWindow dwindow = c.lookupWindow(foo);
/* 157 */       if (dwindow == null) {
/* 158 */         c.errorValue = foo;
/* 159 */         c.errorReason = 3;
/* 160 */         return;
/*     */       }
/* 162 */       x = (short)comChannel.readShort();
/* 163 */       y = (short)comChannel.readShort();
/* 164 */       foo = comChannel.readInt();
/* 165 */       c.length -= 2;
/* 166 */       XWindow swindow = c.lookupWindow(foo);
/* 167 */       if (swindow == null) {
/* 168 */         c.errorValue = foo;
/* 169 */         c.errorReason = 3; return;
/*     */       }
/*     */       
/*     */ 
/*     */ 
/*     */ 
/*     */       break;
/*     */     case 4: 
/* 177 */       foo = comChannel.readByte();
/* 178 */       comChannel.readPad(3);
/* 179 */       foo = comChannel.readInt();
/* 180 */       foo = comChannel.readShort();
/* 181 */       foo = comChannel.readShort();
/* 182 */       break;
/*     */     case 5: 
/* 184 */       foo = comChannel.readInt();
/* 185 */       c.length -= 2;
/* 186 */       XWindow = c.lookupWindow(foo);
/* 187 */       if (XWindow == null) {
/* 188 */         c.errorValue = foo;
/* 189 */         c.errorReason = 3;
/* 190 */         return;
/*     */       }
/* 192 */       synchronized (comChannel) {
/* 193 */         comChannel.writeByte(1);
/* 194 */         comChannel.writePad(1);
/* 195 */         comChannel.writeShort(c.getSequence());
/* 196 */         comChannel.writeInt(0);
/* 197 */         comChannel.writeByte(0);
/* 198 */         comChannel.writeByte(0);
/* 199 */         comChannel.writePad(2);
/* 200 */         comChannel.writeShort(-XWindow.borderWidth);
/* 201 */         comChannel.writeShort(-XWindow.borderWidth);
/* 202 */         comChannel.writeShort(XWindow.width + 2 * XWindow.borderWidth);
/* 203 */         comChannel.writeShort(XWindow.height + 2 * XWindow.borderWidth);
/* 204 */         comChannel.writeShort(0);
/* 205 */         comChannel.writeShort(0);
/* 206 */         comChannel.writeShort(XWindow.width);
/* 207 */         comChannel.writeShort(XWindow.height);
/* 208 */         comChannel.writePad(4);
/* 209 */         comChannel.flush();
/*     */       }
/* 211 */       break;
/*     */     case 6: 
/* 213 */       foo = comChannel.readInt();
/* 214 */       c.length -= 2;
/* 215 */       XWindow = c.lookupWindow(foo);
/* 216 */       if (XWindow == null) {
/* 217 */         c.errorValue = foo;
/* 218 */         c.errorReason = 3;
/* 219 */         return;
/*     */       }
/* 221 */       foo = comChannel.readByte();
/* 222 */       comChannel.readPad(3);
/* 223 */       c.length -= 1;
/* 224 */       if ((foo != 0) && (foo != 1)) {
/* 225 */         c.errorValue = foo;
/* 226 */         c.errorReason = 2;
/* 227 */         return;
/*     */       }
/*     */       
/*     */ 
/* 231 */       Head head = (Head)Resource.lookupIDByType(XWindow.id, etyp);
/* 232 */       if (foo == 1) {
/* 233 */         if (head != null) {
/* 234 */           for (ShapeEvent se = head.next; se != null; se = se.next) {
/* 235 */             if (se.XClient == c) {
/* 236 */               return;
/*     */             }
/*     */           }
/*     */         }
/* 240 */         ShapeEvent se = new ShapeEvent(Resource.fakeClientId(c), ctyp, c, XWindow);
/*     */         
/*     */ 
/* 243 */         Resource.add(se);
/* 244 */         if (head == null) {
/* 245 */           head = new Head(XWindow.id, etyp);
/* 246 */           Resource.add(head);
/*     */         }
/*     */         else {
/* 249 */           se.next = head.next;
/*     */         }
/* 251 */         head.next = se;
/* 252 */         return;
/*     */       }
/*     */       
/* 255 */       if (head != null) {
/* 256 */         ShapeEvent nse = null;
/* 257 */         ShapeEvent se = null;
/* 258 */         for (se = head.next; (se != null) && 
/* 259 */               (se.XClient != c); se = se.next)
/*     */         {
/*     */ 
/* 261 */           nse = se;
/*     */         }
/* 263 */         if (se != null) {
/* 264 */           Resource.freeResource(se.id, ctyp);
/* 265 */           if (nse != null) {
/* 266 */             nse.next = se.next;
/*     */           }
/*     */           else {
/* 269 */             head.next = se.next;
/*     */           }
/*     */         }
/*     */       }
/*     */       
/* 274 */       break;
/*     */     case 7: 
/* 276 */       foo = comChannel.readInt();
/* 277 */       c.length -= 2;
/* 278 */       XWindow = c.lookupWindow(foo);
/* 279 */       if (XWindow == null) {
/* 280 */         c.errorValue = foo;
/* 281 */         c.errorReason = 3;
/* 282 */         return;
/*     */       }
/* 284 */       byte result = 0;
/*     */       
/* 286 */       Head head = (Head)Resource.lookupIDByType(XWindow.id, etyp);
/*     */       
/* 288 */       if (head != null) {
/* 289 */         for (ShapeEvent se = head.next; se != null; se = se.next) {
/* 290 */           if (se.XClient == c) { result = 1; break;
/*     */           }
/*     */         }
/*     */       }
/*     */       
/* 295 */       synchronized (comChannel) {
/* 296 */         comChannel.writeByte(1);
/* 297 */         comChannel.writeByte(result);
/* 298 */         comChannel.writeShort(c.getSequence());
/* 299 */         comChannel.writeInt(0);
/* 300 */         comChannel.writePad(24);
/* 301 */         comChannel.flush();
/*     */       }
/* 303 */       break;
/*     */     case 8: 
/* 305 */       foo = comChannel.readInt();
/* 306 */       c.length -= 2;
/* 307 */       XWindow = c.lookupWindow(foo);
/* 308 */       if (XWindow == null) {
/* 309 */         c.errorValue = foo;
/* 310 */         c.errorReason = 3;
/* 311 */         return;
/*     */       }
/*     */       
/* 314 */       kind = comChannel.readByte();
/* 315 */       comChannel.readPad(3);
/*     */       
/* 317 */       synchronized (comChannel) {
/* 318 */         comChannel.writeByte(1);
/* 319 */         comChannel.writeByte(0);
/* 320 */         comChannel.writeShort(c.getSequence());
/* 321 */         comChannel.writeInt(2);
/* 322 */         comChannel.writeInt(1);
/* 323 */         comChannel.writePad(20);
/* 324 */         comChannel.writeShort(0);
/* 325 */         comChannel.writeShort(0);
/* 326 */         comChannel.writeShort(XWindow.width);
/* 327 */         comChannel.writeShort(XWindow.height);
/* 328 */         comChannel.flush();
/*     */       }
/* 330 */       break;
/*     */     default: 
/* 332 */       System.err.println("Shape: unknown code=" + c.data);
/*     */     }
/*     */   }
/*     */   
/*     */   void sendShapeNotify(int kind, XWindow XWindow, int x, int y, int w, int h, int shaped)
/*     */     throws IOException
/*     */   {
/* 339 */     Head head = (Head)Resource.lookupIDByType(XWindow.id, etyp);
/* 340 */     if (head == null) return;
/* 341 */     for (ShapeEvent se = head.next; se != null; se = se.next) {
/* 342 */       XClient c = se.XClient;
/* 343 */       if ((c != XClient.X_CLIENTs[0]) && (!c.clientGone))
/*     */       {
/*     */ 
/* 346 */         mkShapeNotify(c.cevent, kind, XWindow.id, x, y, w, h, shaped);
/* 347 */         c.cevent.putSequence(c.getSequence());
/* 348 */         c.sendEvent(1, c.cevent);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   void mkShapeNotify(Event e, int kind, int window, int x, int y, int w, int h, int shaped) {
/* 354 */     e.clear();
/* 355 */     e.writeByte((byte)this.eventbase);
/* 356 */     e.writeByte((byte)kind);
/* 357 */     e.writePad(2);
/* 358 */     e.writeInt(window);
/* 359 */     e.writeShort(x);e.writeShort(y);
/* 360 */     e.writeShort(w);e.writeShort(h);
/* 361 */     int time = (int)System.currentTimeMillis();
/* 362 */     e.writeInt(time);
/* 363 */     e.writeByte((byte)shaped);
/*     */   }
/*     */   
/*     */   public void swap(Event e) {
/* 367 */     e.index = 4;
/* 368 */     e.swapInt();
/* 369 */     e.swapShort();e.swapShort();
/* 370 */     e.swapShort();e.swapShort();
/* 371 */     e.swapInt();
/*     */   }
/*     */   
/*     */   class Head extends Resource {
/*     */     DummySHAPEExtension.ShapeEvent next;
/*     */     
/* 377 */     Head(int id, int typ) { super(typ);
/* 378 */       this.next = null;
/*     */     }
/*     */     
/*     */     void delete() throws IOException { DummySHAPEExtension.ShapeEvent pNext;
/* 382 */       for (DummySHAPEExtension.ShapeEvent pCur = this.next; pCur != null; pCur = pNext) {
/* 383 */         pNext = pCur.next;
/* 384 */         freeResource(pCur.id, DummySHAPEExtension.ctyp);
/*     */       }
/* 386 */       this.next = null;
/*     */     }
/*     */   }
/*     */   
/*     */   class ShapeEvent extends Resource {
/*     */     ShapeEvent next;
/*     */     XClient XClient;
/*     */     XWindow XWindow;
/*     */     
/*     */     ShapeEvent(int id, int typ, XClient XClient, XWindow XWindow) {
/* 396 */       super(typ);
/* 397 */       this.XClient = XClient;
/* 398 */       this.XWindow = XWindow;
/* 399 */       this.next = null;
/*     */     }
/*     */     
/*     */     void delete() throws IOException {
/* 403 */       DummySHAPEExtension.Head head = (DummySHAPEExtension.Head)lookupIDByType(this.XWindow.id, DummySHAPEExtension.etyp);
/* 404 */       if (head != null) {
/* 405 */         ShapeEvent pPrev = null;
/* 406 */         for (ShapeEvent pCur = head.next; (pCur != null) && (pCur != this); pCur = pCur.next) {
/* 407 */           pPrev = pCur;
/*     */         }
/* 409 */         if (pCur != null) {
/* 410 */           if (pPrev != null) pPrev.next = this.next; else {
/* 411 */             head.next = this.next;
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Joey\Downloads\launch.jar!\com\emt\proteus\xserver\display\DummySHAPEExtension.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */