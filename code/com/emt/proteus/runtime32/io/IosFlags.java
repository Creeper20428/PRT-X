/*     */ package com.emt.proteus.runtime32.io;
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class IosFlags
/*     */ {
/*     */   public static final int BOOL_ALPHA = 1;
/*     */   
/*     */ 
/*     */   public static final int DEC = 2;
/*     */   
/*     */ 
/*     */   public static final int FIXED = 4;
/*     */   
/*     */ 
/*     */   public static final int HEX = 8;
/*     */   
/*     */ 
/*     */   public static final int ALIGN_INTERNAL = 16;
/*     */   
/*     */ 
/*     */   public static final int ALIGN_LEFT = 32;
/*     */   
/*     */ 
/*     */   public static final int OCT = 64;
/*     */   
/*     */   public static final int ALIGN_RIGHT = 128;
/*     */   
/*     */   public static final int SCIENTIFIC = 256;
/*     */   
/*     */   public static final int SHOW_BASE = 512;
/*     */   
/*     */   public static final int SHOW_POINT = 1024;
/*     */   
/*     */   public static final int SHOW_POS = 2048;
/*     */   
/*     */   public static final int SKIPWS = 4096;
/*     */   
/*     */   public static final int UNIT_BUF = 8192;
/*     */   
/*     */   public static final int UPPER = 16384;
/*     */   
/*     */   public static final int BASE_FIELD = 74;
/*     */   
/*     */   public static final int ADJUSTFIELD = 176;
/*     */   
/*     */   public static final int FLOAT_FIELD = 260;
/*     */   
/*     */   public static final int ALIGN_LEFT_RIGHT = 160;
/*     */   
/*     */   public static final int ALIGN_LEFT_INTERNAL = 48;
/*     */   
/*     */ 
/*     */   public static boolean isSkipWs(int flags)
/*     */   {
/*  56 */     return get(flags, 4096);
/*     */   }
/*     */   
/*     */   public static boolean isAlignLeft(int flags) {
/*  60 */     return get(flags, 32);
/*     */   }
/*     */   
/*     */   public static boolean isAlignRight(int flags) {
/*  64 */     return get(flags, 128);
/*     */   }
/*     */   
/*     */   public static boolean isAlignInternal(int flags) {
/*  68 */     return get(flags, 16);
/*     */   }
/*     */   
/*     */   public static boolean isFixed(int flags) {
/*  72 */     return get(flags, 4);
/*     */   }
/*     */   
/*     */   public static boolean isScientific(int flags) {
/*  76 */     return get(flags, 256);
/*     */   }
/*     */   
/*     */   public static boolean isBoolAlpha(int flags) {
/*  80 */     return get(flags, 1);
/*     */   }
/*     */   
/*     */   public static boolean showbase(int flags) {
/*  84 */     return get(flags, 512);
/*     */   }
/*     */   
/*     */   public static boolean showPoint(int flags) {
/*  88 */     return get(flags, 1024);
/*     */   }
/*     */   
/*     */   public static boolean showPos(int flags) {
/*  92 */     return get(flags, 2048);
/*     */   }
/*     */   
/*     */   public static boolean unitbuf(int flags) {
/*  96 */     return get(flags, 8192);
/*     */   }
/*     */   
/*     */   public static boolean uppercase(int flags) {
/* 100 */     return get(flags, 16384);
/*     */   }
/*     */   
/*     */   public static boolean isDecimal(int flags) {
/* 104 */     return get(flags, 2);
/*     */   }
/*     */   
/*     */   public static boolean isOctal(int flags) {
/* 108 */     return get(flags, 64);
/*     */   }
/*     */   
/*     */   public static boolean isHex(int flags) {
/* 112 */     return get(flags, 8);
/*     */   }
/*     */   
/*     */   public static int unset(int flags, int flagmask) {
/* 116 */     return flags & (flagmask ^ 0xFFFFFFFF);
/*     */   }
/*     */   
/*     */   public static int set(int flags, int flagmask) {
/* 120 */     return flags | flagmask;
/*     */   }
/*     */   
/* 123 */   public static boolean get(int flags, int flagmask) { return (flags & flagmask) != 0; }
/*     */ }


/* Location:              C:\Users\Joey\Downloads\codejar.jar!\com\emt\proteus\runtime32\io\IosFlags.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */