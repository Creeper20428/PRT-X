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
/*     */ public final class Cursor
/*     */   extends Resource
/*     */ {
/*     */   public static Cursor rootCursor;
/*     */   java.awt.Cursor cursor;
/*  31 */   public static int[] cursors = { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13 };
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
/*     */   public Cursor(int id)
/*     */   {
/*  48 */     super(id, 5);
/*  49 */     this.cursor = java.awt.Cursor.getPredefinedCursor(0);
/*     */   }
/*     */   
/*     */ 
/*     */   public Cursor(int id, Pixmap src, Pixmap msk, int fr, int fg, int fb, int br, int bg, int bb, int hx, int hy)
/*     */   {
/*  55 */     super(id, 5);
/*  56 */     this.cursor = java.awt.Cursor.getPredefinedCursor(0);
/*     */   }
/*     */   
/*  59 */   private int getType(int i) { int j = 0;
/*  60 */     switch (i) {
/*     */     case 0: 
/*     */     case 2: 
/*     */       break;
/*     */     case 4: 
/*     */     case 6: 
/*     */     case 8: 
/*     */     case 10: 
/*     */     case 12: 
/*  69 */       j = 4;
/*  70 */       break;
/*     */     case 14: 
/*  72 */       j = 5;
/*  73 */       break;
/*     */     case 16: 
/*  75 */       j = 9;
/*  76 */       break;
/*     */     case 18: 
/*  78 */       j = 9;
/*  79 */       break;
/*     */     case 20: 
/*     */     case 22: 
/*     */     case 24: 
/*     */       break;
/*     */     case 26: 
/*  85 */       j = 3;
/*  86 */       break;
/*     */     case 28: 
/*     */       break;
/*     */     case 30: 
/*     */     case 32: 
/*     */     case 34: 
/*  92 */       j = 1;
/*  93 */       break;
/*     */     case 36: 
/*     */     case 38: 
/*     */     case 40: 
/*     */     case 42: 
/*     */     case 44: 
/*     */     case 46: 
/*     */     case 48: 
/*     */     case 50: 
/*     */       break;
/*     */     case 52: 
/* 104 */       j = 13;
/* 105 */       break;
/*     */     case 54: 
/*     */     case 56: 
/*     */       break;
/*     */     case 58: 
/*     */     case 60: 
/* 111 */       j = 12;
/* 112 */       break;
/*     */     case 62: 
/*     */     case 64: 
/*     */     case 66: 
/*     */     case 68: 
/*     */       break;
/*     */     case 70: 
/*     */     case 72: 
/* 120 */       j = 10;
/* 121 */       break;
/*     */     case 74: 
/*     */     case 76: 
/*     */     case 78: 
/*     */     case 80: 
/*     */     case 82: 
/*     */     case 84: 
/*     */     case 86: 
/*     */     case 88: 
/*     */     case 90: 
/*     */     case 92: 
/*     */     case 94: 
/*     */       break;
/*     */     case 96: 
/*     */     case 98: 
/* 136 */       j = 11;
/* 137 */       break;
/*     */     case 100: 
/*     */     case 102: 
/*     */     case 104: 
/*     */     case 106: 
/*     */     case 108: 
/*     */     case 110: 
/*     */     case 112: 
/*     */     case 114: 
/*     */     case 116: 
/*     */     case 118: 
/*     */     case 120: 
/*     */     case 122: 
/*     */     case 124: 
/*     */     case 126: 
/*     */     case 128: 
/*     */     case 130: 
/*     */     case 132: 
/*     */       break;
/*     */     case 134: 
/* 157 */       j = 6;
/* 158 */       break;
/*     */     case 136: 
/* 160 */       j = 7;
/* 161 */       break;
/*     */     case 138: 
/*     */     case 140: 
/* 164 */       j = 8;
/* 165 */       break;
/*     */     case 142: 
/*     */     case 144: 
/*     */     case 146: 
/*     */     case 148: 
/*     */       break;
/*     */     case 150: 
/* 172 */       j = 3;
/* 173 */       break;
/*     */     case 152: 
/* 175 */       j = 2;
/* 176 */       break;
/*     */     }
/*     */     
/* 179 */     return j;
/*     */   }
/*     */   
/*     */ 
/*     */   Cursor(int id, XFont src, XFont msk, int srcc, int mskc, int fr, int fg, int fb, int br, int bg, int bb)
/*     */   {
/* 185 */     super(id, 5);
/* 186 */     this.cursor = java.awt.Cursor.getPredefinedCursor(getType(srcc));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public static void reqCreateGlyphCursor(XClient c)
/*     */     throws IOException
/*     */   {
/* 195 */     int cid = c.channel.readInt();
/* 196 */     int foo = c.channel.readInt();
/* 197 */     c.length -= 3;
/* 198 */     XFont src = (XFont)lookupIDByType(foo, 4);
/* 199 */     if (src == null) {
/* 200 */       c.errorValue = foo;
/* 201 */       c.errorReason = 4;
/* 202 */       return;
/*     */     }
/* 204 */     foo = c.channel.readInt();
/* 205 */     c.length -= 1;
/* 206 */     XFont msk = (XFont)lookupIDByType(foo, 4);
/* 207 */     if (msk == null) {
/* 208 */       c.errorValue = foo;
/* 209 */       c.errorReason = 4;
/* 210 */       return;
/*     */     }
/* 212 */     int srcc = (short)c.channel.readShort();
/* 213 */     int mskc = (short)c.channel.readShort();
/*     */     
/* 215 */     int fr = (short)c.channel.readShort();
/* 216 */     int fg = (short)c.channel.readShort();
/* 217 */     int fb = (short)c.channel.readShort();
/* 218 */     int br = (short)c.channel.readShort();
/* 219 */     int bg = (short)c.channel.readShort();
/* 220 */     int bb = (short)c.channel.readShort();
/*     */     
/* 222 */     Cursor cur = new Cursor(cid, src, msk, srcc, mskc, fr, fg, fb, br, bg, bb);
/*     */     
/* 224 */     add(cur);
/*     */   }
/*     */   
/*     */   public static void reqFreeCursor(XClient c) throws IOException {
/* 228 */     int foo = c.channel.readInt();
/* 229 */     c.length -= 2;
/* 230 */     Cursor cur = (Cursor)lookupIDByType(foo, 5);
/* 231 */     if (cur == null) {
/* 232 */       c.errorValue = foo;
/* 233 */       c.errorReason = 4;
/* 234 */       return;
/*     */     }
/*     */     
/* 237 */     freeResource(foo, 0);
/*     */   }
/*     */   
/*     */   void recolor(int fr, int fg, int fb, int br, int bg, int bb) {}
/*     */   
/* 242 */   void delete() throws IOException { this.cursor = null; }
/*     */   
/*     */ 
/*     */ 
/*     */   public static void reqRecolorCursor(XClient c)
/*     */     throws IOException
/*     */   {
/* 249 */     int foo = c.channel.readInt();
/* 250 */     c.length -= 2;
/* 251 */     Cursor cur = (Cursor)lookupIDByType(foo, 5);
/* 252 */     if (cur == null) {
/* 253 */       c.errorValue = foo;
/* 254 */       c.errorReason = 4;
/* 255 */       return;
/*     */     }
/*     */     
/* 258 */     int fr = (short)c.channel.readShort();
/* 259 */     int fg = (short)c.channel.readShort();
/* 260 */     int fb = (short)c.channel.readShort();
/* 261 */     int br = (short)c.channel.readShort();
/* 262 */     int bg = (short)c.channel.readShort();
/* 263 */     int bb = (short)c.channel.readShort();
/*     */     
/* 265 */     cur.recolor(fr, fg, fb, br, bg, bb);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public static void reqCreateCursor(XClient c)
/*     */     throws IOException
/*     */   {
/* 273 */     int cid = c.channel.readInt();
/* 274 */     int foo = c.channel.readInt();
/* 275 */     c.length -= 3;
/* 276 */     Resource r = lookupIDByType(foo, -1073741822);
/* 277 */     if ((r == null) || (!(r instanceof Pixmap))) {
/* 278 */       c.errorValue = foo;
/* 279 */       c.errorReason = 4;
/* 280 */       return;
/*     */     }
/* 282 */     Pixmap src = (Pixmap)r;
/*     */     
/* 284 */     foo = c.channel.readInt();
/* 285 */     c.length -= 1;
/* 286 */     r = lookupIDByType(foo, -1073741822);
/* 287 */     if ((r == null) || (!(r instanceof Pixmap))) {
/* 288 */       c.errorValue = foo;
/* 289 */       c.errorReason = 4;
/* 290 */       return;
/*     */     }
/* 292 */     Pixmap msk = (Pixmap)r;
/*     */     
/* 294 */     int fr = (short)c.channel.readShort();
/* 295 */     int fg = (short)c.channel.readShort();
/* 296 */     int fb = (short)c.channel.readShort();
/* 297 */     int br = (short)c.channel.readShort();
/* 298 */     int bg = (short)c.channel.readShort();
/* 299 */     int bb = (short)c.channel.readShort();
/* 300 */     int x = (short)c.channel.readShort();
/* 301 */     int y = (short)c.channel.readShort();
/* 302 */     Cursor cur = new Cursor(cid, src, msk, fr, fg, fb, br, bg, bb, x, y);
/* 303 */     add(cur);
/*     */   }
/*     */ }


/* Location:              C:\Users\Joey\Downloads\launch.jar!\com\emt\proteus\xserver\display\Cursor.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */