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
/*    */ 
/*    */ 
/*    */ public final class SocketState
/*    */ {
/*    */   public static final int SS_FREE = 0;
/*    */   public static final int SS_UNCONNECTED = 1;
/*    */   public static final int SS_CONNECTING = 2;
/*    */   public static final int SS_CONNECTED = 3;
/*    */   public static final int SS_DISCONNECTING = 4;
/* 22 */   private static Map<Integer, String> _constants = Utils.createConstantMap(SocketState.class);
/*    */   
/* 24 */   public static String toString(int value) { return (String)_constants.get(Integer.valueOf(value)); }
/*    */ }


/* Location:              C:\Users\Joey\Downloads\codejar.jar!\com\emt\proteus\runtime32\sockets\SocketState.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */