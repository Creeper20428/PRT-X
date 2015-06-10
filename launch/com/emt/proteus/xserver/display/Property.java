/*     */ package com.emt.proteus.xserver.display;
/*     */ 
/*     */ import com.emt.proteus.xserver.client.XClient;
/*     */ import com.emt.proteus.xserver.io.ComChannel;
/*     */ import com.emt.proteus.xserver.protocol.Atom;
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
/*     */ public final class Property
/*     */ {
/*     */   static final byte PropModeReplace = 0;
/*     */   static final byte PropModePrepend = 1;
/*     */   static final byte PropModeAppend = 2;
/*     */   Property next;
/*     */   int propertyName;
/*     */   int type;
/*     */   short format;
/*     */   int size;
/*     */   byte[] data;
/*     */   
/*     */   public static Property getProperty(XWindow w, int name, int type)
/*     */   {
/*  45 */     synchronized (w) {
/*  46 */       Property p = w.getProperty();
/*  47 */       while ((p != null) && 
/*  48 */         (p.propertyName != name)) {
/*  49 */         p = p.next;
/*     */       }
/*  51 */       if (p != null) {
/*  52 */         return p;
/*     */       }
/*  54 */       return null;
/*     */     }
/*     */   }
/*     */   
/*     */   public static void delProperty(XClient c, XWindow w, int name, int type) throws IOException
/*     */   {
/*     */     Property p;
/*  61 */     synchronized (w) {
/*  62 */       p = w.getProperty();
/*  63 */       Property prev = null;
/*  64 */       while ((p != null) && 
/*  65 */         (p.propertyName != name)) {
/*  66 */         prev = p;
/*  67 */         p = p.next;
/*     */       }
/*  69 */       if (p != null) {
/*  70 */         if (prev == null) {
/*  71 */           w.setProperty(p.next);
/*  72 */           if (p.next != null) {}
/*     */         }
/*     */         else
/*     */         {
/*  76 */           prev.next = p.next;
/*     */         }
/*  78 */         c.cevent.mkPropertyNotify(w.id, p.propertyName, (int)System.currentTimeMillis(), 1);
/*     */       }
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*  84 */     if (p != null) {
/*  85 */       w.sendEvent(c.cevent, 1, null);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static void changeWindowProperty(XClient c, XWindow w, int property, int type, short format, byte mode, int len, byte[] value, boolean sendevent)
/*     */     throws IOException
/*     */   {
/*  96 */     synchronized (w)
/*     */     {
/*  98 */       int totalSize = len * (format / 8);
/*  99 */       Property p = w.getProperty();
/* 100 */       while ((p != null) && 
/* 101 */         (p.propertyName != property)) {
/* 102 */         p = p.next;
/*     */       }
/*     */       
/* 105 */       if (p != null) {
/* 106 */         if ((format != p.format) && (mode != 0)) {
/* 107 */           System.err.println("error!");
/* 108 */           c.errorReason = 8;
/* 109 */           return;
/*     */         }
/* 111 */         if ((type != p.type) && (mode != 0)) {
/* 112 */           System.err.println("error!");
/* 113 */           c.errorReason = 8;
/* 114 */           return;
/*     */         }
/* 116 */         if (mode == 0) {
/* 117 */           p.data = value;
/* 118 */           p.size = len;
/* 119 */           p.format = format;
/* 120 */           p.type = type;
/* 121 */         } else if (len != 0) {
/* 122 */           if (mode == 2) {
/* 123 */             byte[] foo = new byte[format / 8 * (len + p.size)];
/* 124 */             if (p.size > 0)
/* 125 */               System.arraycopy(p.data, 0, foo, 0, p.size);
/* 126 */             System.arraycopy(value, 0, foo, p.size, totalSize);
/* 127 */             p.size += len;
/* 128 */             p.data = foo;
/* 129 */           } else if (mode == 1) {
/* 130 */             byte[] foo = new byte[format / 8 * (len + p.size)];
/* 131 */             System.arraycopy(value, 0, foo, 0, totalSize);
/* 132 */             if (p.size > 0)
/* 133 */               System.arraycopy(p.data, 0, foo, totalSize, p.size);
/* 134 */             p.size += len;
/* 135 */             p.data = foo;
/*     */           }
/*     */         }
/*     */       }
/*     */       else {
/* 140 */         p = new Property();
/* 141 */         p.propertyName = property;
/* 142 */         p.type = type;
/* 143 */         p.format = format;
/* 144 */         p.data = value;
/* 145 */         p.size = len;
/* 146 */         p.next = w.getProperty();
/* 147 */         w.setProperty(p);
/*     */       }
/*     */       
/* 150 */       if ((p != null) && 
/* 151 */         (p.propertyName == 9) && (w == w.screen.root) && (p.size > 0))
/*     */       {
/* 153 */         CopyPaste.setString(new String(p.data, 0, p.size));
/*     */       }
/*     */     }
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
/* 168 */     if (sendevent) {
/* 169 */       c.cevent.mkPropertyNotify(w.id, property, (int)System.currentTimeMillis(), 0);
/*     */       
/*     */ 
/* 172 */       w.sendEvent(c.cevent, 1, null);
/*     */     }
/*     */   }
/*     */   
/*     */   public static void reqRotateProperties(XClient c) throws IOException
/*     */   {
/* 178 */     ComChannel comChannel = c.channel;
/*     */     
/* 180 */     int foo = comChannel.readInt();
/* 181 */     XWindow w = c.lookupWindow(foo);
/* 182 */     c.length -= 2;
/* 183 */     if (w == null) {
/* 184 */       c.errorValue = foo;
/* 185 */       c.errorReason = 3;
/* 186 */       return;
/*     */     }
/* 188 */     int n = (short)comChannel.readShort();
/* 189 */     int delta = (short)comChannel.readShort();
/*     */     
/* 191 */     c.length -= 1;
/*     */     
/* 193 */     if (n == 0) {
/* 194 */       return;
/*     */     }
/*     */     
/* 197 */     int[] atoms = new int[n];
/* 198 */     Property[] props = new Property[n];
/*     */     
/* 200 */     int i = 0;
/* 201 */     while (n != 0) {
/* 202 */       atoms[i] = comChannel.readInt();
/* 203 */       c.length -= 1;
/* 204 */       if (!Atom.valid(atoms[i])) {
/* 205 */         c.errorValue = atoms[i];
/* 206 */         c.errorReason = 5;
/* 207 */         return;
/*     */       }
/* 209 */       Property p = w.getProperty();
/* 210 */       while (p != null) {
/* 211 */         if (p.propertyName == atoms[i]) {
/* 212 */           props[i] = p;
/* 213 */           break;
/*     */         }
/* 215 */         p = p.next;
/*     */       }
/* 217 */       if (p == null) {
/* 218 */         c.errorReason = 8;
/* 219 */         return;
/*     */       }
/* 221 */       i++;
/* 222 */       n--;
/*     */     }
/* 224 */     for (int j = 0; j < atoms.length; j++) {
/* 225 */       for (int k = j + 1; k < atoms.length; k++) {
/* 226 */         if (atoms[j] == atoms[k]) {
/* 227 */           c.errorReason = 8;
/* 228 */           return;
/*     */         }
/*     */       }
/*     */     }
/* 232 */     if ((delta < 0 ? -1 * delta : delta) % atoms.length != 0) {
/* 233 */       while (delta < 0) {
/* 234 */         delta += atoms.length;
/*     */       }
/* 236 */       for (i = 0; i < atoms.length; i++) {
/* 237 */         c.cevent.mkPropertyNotify(w.id, props[i].propertyName, (int)System.currentTimeMillis(), 0);
/*     */         
/*     */ 
/*     */ 
/*     */ 
/* 242 */         w.sendEvent(c.cevent, 1, null);
/* 243 */         props[i].propertyName = atoms[((i + delta) % atoms.length)];
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public static void reqDeleteProperty(XClient c) throws IOException
/*     */   {
/* 250 */     ComChannel comChannel = c.channel;
/* 251 */     int foo = comChannel.readInt();
/* 252 */     XWindow w = c.lookupWindow(foo);
/* 253 */     if (w == null) {
/* 254 */       c.errorValue = foo;
/* 255 */       c.errorReason = 3;
/*     */     }
/* 257 */     int propty = foo = comChannel.readInt();
/* 258 */     c.length -= 3;
/* 259 */     if (c.errorReason != 0) {
/* 260 */       return;
/*     */     }
/* 262 */     if (w.parent != null) {
/* 263 */       delProperty(c, w, propty, 0);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public static void reqGetProperty(XClient c)
/*     */     throws IOException
/*     */   {
/* 273 */     ComChannel comChannel = c.channel;
/* 274 */     int dlt = c.data;
/*     */     
/* 276 */     int foo = comChannel.readInt();
/* 277 */     XWindow w = c.lookupWindow(foo);
/* 278 */     if (w == null) {
/* 279 */       c.errorValue = foo;
/* 280 */       c.errorReason = 3;
/*     */     }
/* 282 */     int prprty = comChannel.readInt();
/* 283 */     int typ = comChannel.readInt();
/* 284 */     int ffst = comChannel.readInt();
/* 285 */     int lngth = comChannel.readInt();
/* 286 */     c.length -= 6;
/* 287 */     if (c.errorReason != 0) {
/* 288 */       return;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 294 */     Property prop = getProperty(w, prprty, typ);
/*     */     int ba;
/* 296 */     synchronized (comChannel) {
/* 297 */       comChannel.writeByte(1);
/* 298 */       if (prop == null) {
/* 299 */         comChannel.writeByte(0);
/* 300 */         comChannel.writeShort(c.getSequence());
/* 301 */         comChannel.writeInt(0);
/* 302 */         comChannel.writeInt(0);
/* 303 */         comChannel.writeInt(0);
/* 304 */         comChannel.writeInt(0);
/* 305 */         comChannel.writePad(12);
/* 306 */         comChannel.flush();
/* 307 */         return;
/*     */       }
/*     */       
/* 310 */       if ((typ != prop.type) && (typ != 0)) {
/* 311 */         comChannel.writeByte(prop.format);
/* 312 */         comChannel.writeShort(c.getSequence());
/* 313 */         comChannel.writeInt(0);
/* 314 */         comChannel.writeInt(prop.type);
/* 315 */         comChannel.writeInt(0);
/* 316 */         comChannel.writeInt(0);
/* 317 */         comChannel.writePad(12);
/* 318 */         comChannel.flush();
/* 319 */         return;
/*     */       }
/*     */       
/* 322 */       int n = prop.format / 8 * prop.size;
/*     */       
/* 324 */       int ind = ffst * 4;
/*     */       
/*     */ 
/*     */ 
/*     */ 
/* 329 */       int len = (n >= ind) || (n - ind < lngth * 4) ? n - ind : lngth * 4;
/* 330 */       if (lngth * 4 < 0) len = n - ind;
/* 331 */       ba = n - (ind + len);
/* 332 */       if ((dlt != 0) && (ba == 0)) {
/* 333 */         c.cevent.mkPropertyNotify(w.id, prprty, (int)System.currentTimeMillis(), 1);
/*     */         
/* 335 */         w.sendEvent(c.cevent, 1, null);
/*     */       }
/*     */       
/* 338 */       comChannel.writeByte(prop.format);
/* 339 */       comChannel.writeShort(c.getSequence());
/* 340 */       comChannel.writeInt((len + 3) / 4);
/* 341 */       comChannel.writeInt(prop.type);
/* 342 */       comChannel.writeInt(ba);
/* 343 */       if (prop.format / 8 == 0) comChannel.writeInt(0); else {
/* 344 */         comChannel.writeInt(len / (prop.format / 8));
/*     */       }
/* 346 */       comChannel.writePad(12);
/*     */       
/* 348 */       if (len > 0) {
/* 349 */         if ((c.swap) && ((prop.format == 16) || (prop.format == 32))) {
/* 350 */           byte[] b = new byte[len];
/* 351 */           System.arraycopy(prop.data, ind, b, 0, len);
/* 352 */           switch (prop.format) {
/*     */           case 16: 
/* 354 */             swapS(b, 0, len);
/* 355 */             break;
/*     */           case 32: 
/* 357 */             swapL(b, 0, len);
/* 358 */             break;
/*     */           }
/*     */           
/* 361 */           comChannel.writeByte(b, 0, len);
/*     */         } else {
/* 363 */           comChannel.writeByte(prop.data, ind, len);
/*     */         }
/* 365 */         if ((-len & 0x3) > 0) {
/* 366 */           comChannel.writePad(-len & 0x3);
/*     */         }
/*     */       }
/* 369 */       comChannel.flush();
/*     */     }
/*     */     
/* 372 */     if ((dlt != 0) && (ba == 0)) {
/* 373 */       delProperty(c, w, prprty, typ);
/*     */     }
/*     */   }
/*     */   
/*     */   public static void reqListProperties(XClient c) throws IOException
/*     */   {
/* 379 */     ComChannel comChannel = c.channel;
/*     */     
/* 381 */     int foo = comChannel.readInt();
/* 382 */     XWindow w = c.lookupWindow(foo);
/* 383 */     c.length -= 2;
/* 384 */     if (w == null) {
/* 385 */       c.errorValue = foo;
/* 386 */       c.errorReason = 3;
/* 387 */       return;
/*     */     }
/* 389 */     synchronized (comChannel) {
/* 390 */       comChannel.writeByte(1);
/* 391 */       Property p = w.getProperty();
/* 392 */       int i = 0;
/* 393 */       while (p != null) {
/* 394 */         i++;
/* 395 */         p = p.next;
/*     */       }
/*     */       
/* 398 */       comChannel.writePad(1);
/* 399 */       comChannel.writeShort(c.getSequence());
/* 400 */       comChannel.writeInt(i);
/* 401 */       comChannel.writeShort(i);
/* 402 */       comChannel.writePad(22);
/*     */       
/* 404 */       p = w.getProperty();
/* 405 */       while (p != null) {
/* 406 */         comChannel.writeInt(p.propertyName);
/* 407 */         p = p.next;
/*     */       }
/* 409 */       comChannel.flush();
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static void reqChangeProperty(XClient c)
/*     */     throws IOException
/*     */   {
/* 420 */     ComChannel comChannel = c.channel;
/*     */     
/* 422 */     byte mode = (byte)c.data;
/* 423 */     if ((mode != 0) && (mode != 2) && (mode != 1))
/*     */     {
/*     */ 
/* 426 */       c.errorValue = mode;
/* 427 */       c.errorReason = 2;
/*     */     }
/*     */     
/* 430 */     int n = c.length;
/*     */     
/* 432 */     int foo = comChannel.readInt();
/* 433 */     XWindow w = c.lookupWindow(foo);
/* 434 */     if ((c.errorReason == 0) && (w == null)) {
/* 435 */       c.errorValue = foo;
/* 436 */       c.errorReason = 3;
/*     */     }
/* 438 */     int prpty = comChannel.readInt();
/* 439 */     int typ = comChannel.readInt();
/* 440 */     byte frmt = (byte)comChannel.readByte();
/* 441 */     if ((c.errorReason == 0) && (frmt != 8) && (frmt != 16) && (frmt != 32)) {
/* 442 */       c.errorValue = frmt;
/* 443 */       c.errorReason = 3;
/*     */     }
/* 445 */     comChannel.readPad(3);
/* 446 */     foo = comChannel.readInt();
/* 447 */     int totalSize = foo * (frmt / 8);
/* 448 */     byte[] bar = null;
/* 449 */     if (totalSize > 0) {
/* 450 */       bar = new byte[totalSize];
/* 451 */       comChannel.readByte(bar, 0, totalSize);
/* 452 */       if (c.swap) {
/* 453 */         switch (frmt) {
/*     */         case 16: 
/* 455 */           swapS(bar, 0, totalSize);
/* 456 */           break;
/*     */         case 32: 
/* 458 */           swapL(bar, 0, totalSize);
/* 459 */           break;
/*     */         }
/*     */         
/*     */       }
/* 463 */       comChannel.readPad(-totalSize & 0x3);
/*     */     }
/* 465 */     c.length = 0;
/* 466 */     if (c.errorReason != 0) {
/* 467 */       return;
/*     */     }
/* 469 */     changeWindowProperty(c, w, prpty, typ, (short)frmt, mode, foo, bar, true);
/*     */   }
/*     */   
/*     */   private static final void swapS(byte[] b, int s, int len) {
/* 473 */     if (len % 2 != 0)
/*     */     {
/* 475 */       return;
/*     */     }
/* 477 */     len += s;
/*     */     
/* 479 */     for (int i = s; i < len; i += 2) {
/* 480 */       byte bb = b[i];
/* 481 */       b[i] = b[(i + 1)];
/* 482 */       b[(i + 1)] = bb;
/*     */     }
/*     */   }
/*     */   
/*     */   private static final void swapL(byte[] b, int s, int len) {
/* 487 */     if (len % 4 != 0)
/*     */     {
/* 489 */       return;
/*     */     }
/* 491 */     len += s;
/*     */     
/* 493 */     for (int i = s; i < len; i += 4) {
/* 494 */       byte bb = b[i];
/* 495 */       b[i] = b[(i + 3)];
/* 496 */       b[(i + 3)] = bb;
/* 497 */       bb = b[(i + 1)];
/* 498 */       b[(i + 1)] = b[(i + 2)];
/* 499 */       b[(i + 2)] = bb;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Joey\Downloads\launch.jar!\com\emt\proteus\xserver\display\Property.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */