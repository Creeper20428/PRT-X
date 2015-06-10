/*     */ package com.emt.proteus.runtime32.sockets;
/*     */ 
/*     */ import com.emt.proteus.utils.Utils;
/*     */ import java.util.Map;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class SockOpt
/*     */ {
/*     */   public static final int SOL_SOCKET = 1;
/*     */   public static final int SOL_TCP = 6;
/*     */   
/*     */   public static class SO
/*     */   {
/*     */     public static final int SO_DEBUG = 1;
/*     */     public static final int SO_ACCEPTCONN = 2;
/*     */     public static final int SO_REUSEADDR = 4;
/*     */     public static final int SO_KEEPALIVE = 8;
/*     */     public static final int SO_DONTROUTE = 16;
/*     */     public static final int SO_BROADCAST = 32;
/*     */     public static final int SO_USELOOPBACK = 64;
/*     */     public static final int SO_LINGER = 128;
/*     */     public static final int SO_OOBINLINE = 256;
/*     */     public static final int SO_REUSEPORT = 512;
/*     */     public static final int SO_SNDBUF = 4097;
/*     */     public static final int SO_RCVBUF = 4098;
/*     */     public static final int SO_SNDLOWAT = 4099;
/*     */     public static final int SO_RCVLOWAT = 4100;
/*     */     public static final int SO_SNDTIMEO = 4101;
/*     */     public static final int SO_RCVTIMEO = 4102;
/*     */     public static final int SO_ERROR = 4103;
/*     */     public static final int SO_STYLE = 4104;
/*     */     public static final int SO_TYPE = 4104;
/*  72 */     private static Map<Integer, String> _constants = Utils.createConstantMap(SO.class);
/*     */     
/*     */     public static String toString(int value) {
/*  75 */       if (value < 256)
/*     */       {
/*  77 */         Map<Integer, String> bits = _constants;
/*  78 */         return Utils.toOrBitString(bits, value);
/*     */       }
/*  80 */       return (String)_constants.get(Integer.valueOf(value));
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public static class TCP
/*     */   {
/*     */     public static final int NODELAY = 1;
/*     */     
/*     */ 
/*     */     public static final int MAXSEG = 2;
/*     */     
/*     */     public static final int CORK = 3;
/*     */     
/*     */     public static final int KEEPIDLE = 4;
/*     */     
/*     */     public static final int KEEPINTVL = 5;
/*     */     
/*     */     public static final int KEEPCNT = 6;
/*     */     
/*     */     public static final int SYNCNT = 7;
/*     */     
/*     */     public static final int LINGER2 = 8;
/*     */     
/*     */     public static final int DEFER_ACCEPT = 9;
/*     */     
/*     */     public static final int WINDOW_CLAMP = 10;
/*     */     
/*     */     public static final int INFO = 11;
/*     */     
/*     */     public static final int QUICKACK = 12;
/*     */     
/*     */     public static final int CONGESTION = 13;
/*     */     
/*     */     public static final int MD5SIG = 14;
/*     */     
/* 117 */     private static Map<Integer, String> _constants = Utils.createConstantMap(TCP.class);
/*     */     
/*     */     public static String toString(int value)
/*     */     {
/* 121 */       return (String)_constants.get(Integer.valueOf(value));
/*     */     }
/*     */   }
/*     */   
/* 125 */   private static Map<Integer, String> _constants = Utils.createConstantMap(SockOpt.class);
/*     */   
/*     */ 
/* 128 */   public static String toString(int value) { return (String)_constants.get(Integer.valueOf(value)); }
/*     */   
/*     */   public static String toString(int level, int value) {
/* 131 */     switch (level) {
/* 132 */     case 1:  return SO.toString(value);
/* 133 */     case 6:  return TCP.toString(value); }
/* 134 */     return "*error*";
/*     */   }
/*     */   
/*     */ 
/*     */   public static boolean isKeepAlive(int level, int option_name)
/*     */   {
/* 140 */     return (1 == level) && ((option_name & 0x8) != 0);
/*     */   }
/*     */   
/* 143 */   public static boolean isNoDelay(int level, int option_name) { return (6 == level) && ((option_name & 0x8) != 0); }
/*     */ }


/* Location:              C:\Users\Joey\Downloads\codejar.jar!\com\emt\proteus\runtime32\sockets\SockOpt.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */