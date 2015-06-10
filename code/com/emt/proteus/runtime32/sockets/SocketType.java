/*    */ package com.emt.proteus.runtime32.sockets;
/*    */ 
/*    */ import com.emt.proteus.utils.Utils;
/*    */ import java.util.Map;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public final class SocketType
/*    */ {
/*    */   public static final int SOCK_STREAM = 1;
/*    */   public static final int SOCK_DGRAM = 2;
/*    */   public static final int SOCK_RAW = 3;
/*    */   public static final int SOCK_RDM = 4;
/*    */   public static final int SOCK_SEQPACKET = 5;
/*    */   public static final int SOCK_DCCP = 6;
/*    */   public static final int SOCK_PACKET = 10;
/*    */   public static final int SOCK_NONBLOCK = 16384;
/*    */   public static final int SOCK_CLOEXEC = 33554432;
/* 24 */   private static Map<Integer, String> _constants = Utils.createConstantMap(SocketType.class);
/*    */   
/* 26 */   public static String toString(int value) { int rva = value & 0xFF;
/* 27 */     String s = (String)_constants.get(Integer.valueOf(rva));
/* 28 */     if (rva != value) {
/* 29 */       if ((value & 0x4000) != 0) s = s + "|SOCK_NONCLOCK";
/* 30 */       if ((value & 0x2000000) != 0) s = s + "|SOCK_CLOEXEC";
/*    */     }
/* 32 */     return s;
/*    */   }
/*    */   
/*    */   public static boolean isStream(int type) {
/* 36 */     return (type & 0x1) != 0;
/*    */   }
/*    */ }


/* Location:              C:\Users\Joey\Downloads\codejar.jar!\com\emt\proteus\runtime32\sockets\SocketType.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */