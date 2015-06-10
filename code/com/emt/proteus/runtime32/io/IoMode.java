/*    */ package com.emt.proteus.runtime32.io;
/*    */ 
/*    */ 
/*    */ public class IoMode
/*    */ {
/*    */   public static final int O_ACCMODE = 3;
/*    */   
/*    */   public static final int O_RDONLY = 0;
/*    */   
/*    */   public static final int O_WRONLY = 1;
/*    */   
/*    */   public static final int O_RDWR = 2;
/*    */   public static final int O_CREAT = 64;
/*    */   public static final int O_EXCL = 128;
/*    */   public static final int O_NOCTTY = 256;
/*    */   public static final int O_TRUNC = 512;
/*    */   public static final int O_APPEND = 1024;
/*    */   public static final int O_NONBLOCK = 2048;
/*    */   public static final int O_NDELAY = 2048;
/*    */   public static final int O_SYNC = 4096;
/*    */   public static final int FASYNC = 8192;
/*    */   public static final int O_DIRECT = 16384;
/*    */   public static final int O_LARGEFILE = 32768;
/*    */   public static final int O_DIRECTORY = 65536;
/*    */   public static final int O_NOFOLLOW = 131072;
/*    */   
/*    */   public static class Access
/*    */   {
/*    */     public static final int R_OK = 4;
/*    */     public static final int W_OK = 2;
/*    */     public static final int X_OK = 1;
/*    */     public static final int F_OK = 0;
/*    */     
/*    */     public static boolean isExec(int mode)
/*    */     {
/* 36 */       return (0x1 & mode) != 0;
/*    */     }
/*    */     
/* 39 */     public static boolean isRead(int mode) { return (0x4 & mode) != 0; }
/*    */     
/*    */     public static boolean isWrite(int mode) {
/* 42 */       return (0x2 & mode) != 0;
/*    */     }
/*    */     
/* 45 */     public static boolean isExistsOnly(int mode) { return mode == 0; }
/*    */   }
/*    */ }


/* Location:              C:\Users\Joey\Downloads\codejar.jar!\com\emt\proteus\runtime32\io\IoMode.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */