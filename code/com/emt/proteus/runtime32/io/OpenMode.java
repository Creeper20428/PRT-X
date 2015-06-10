/*    */ package com.emt.proteus.runtime32.io;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public final class OpenMode
/*    */ {
/*    */   public static final int IOS_OPEN_APP = 1;
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public static final int IOS_OPEN_ATE = 2;
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public static final int IOS_OPEN_BIN = 4;
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public static final int IOS_OPEN_IN = 8;
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public static final int IOS_OPEN_OUT = 16;
/*    */   
/*    */ 
/*    */ 
/*    */   public static final int IOS_OPEN_TRUNC = 32;
/*    */   
/*    */ 
/*    */ 
/*    */   public static final int IOS_OPEN_END = 2;
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public static boolean isWritable(int mode)
/*    */   {
/* 44 */     if ((mode & 0x10) != 0) return true;
/* 45 */     if ((mode & 0x1) != 0) return true;
/* 46 */     if ((mode & 0x20) != 0) return true;
/* 47 */     return false;
/*    */   }
/*    */   
/*    */   public static boolean isAppend(int mode) {
/* 51 */     return (mode & 0x1) != 0;
/*    */   }
/*    */   
/* 54 */   public static boolean isTruncate(int mode) { return (mode & 0x20) != 0; }
/*    */   
/*    */   public static boolean atEnd(int mode)
/*    */   {
/* 58 */     return (mode & 0x2) != 0;
/*    */   }
/*    */   
/* 61 */   public static boolean seekEnd(int mode) { return (atEnd(mode)) || (isAppend(mode)); }
/*    */   
/*    */   public static boolean isBin(int mode) {
/* 64 */     return (mode & 0x4) != 0;
/*    */   }
/*    */ }


/* Location:              C:\Users\Joey\Downloads\codejar.jar!\com\emt\proteus\runtime32\io\OpenMode.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */