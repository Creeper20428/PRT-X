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
/*    */ public final class ProtoFamily
/*    */ {
/*    */   public static final short UNSPEC = 0;
/*    */   public static final short UNIX = 1;
/*    */   public static final short LOCAL = 1;
/*    */   public static final short INET = 2;
/*    */   public static final short AX25 = 3;
/*    */   public static final short IPX = 4;
/*    */   public static final short APPLETALK = 5;
/*    */   public static final short NETROM = 6;
/*    */   public static final short BRIDGE = 7;
/*    */   public static final short ATMPVC = 8;
/*    */   public static final short X25 = 9;
/*    */   public static final short INET6 = 10;
/*    */   public static final short ROSE = 11;
/*    */   public static final short DECnet = 12;
/*    */   public static final short NETBEUI = 13;
/*    */   public static final short SECURITY = 14;
/*    */   public static final short KEY = 15;
/*    */   public static final short NETLINK = 16;
/*    */   public static final short ROUTE = 16;
/*    */   public static final short PACKET = 17;
/*    */   public static final short ASH = 18;
/*    */   public static final short ECONET = 19;
/*    */   public static final short ATMSVC = 20;
/*    */   public static final short RDS = 21;
/*    */   public static final short SNA = 22;
/*    */   public static final short IRDA = 23;
/*    */   public static final short PPPOX = 24;
/*    */   public static final short WANPIPE = 25;
/*    */   public static final short LLC = 26;
/*    */   public static final short CAN = 29;
/*    */   public static final short TIPC = 30;
/*    */   public static final short BLUETOOTH = 31;
/*    */   public static final short IUCV = 32;
/*    */   public static final short RXRPC = 33;
/*    */   public static final short ISDN = 34;
/*    */   public static final short PHONET = 35;
/*    */   public static final short IEEE802154 = 36;
/*    */   public static final short CAIF = 37;
/*    */   public static final short MAX = 38;
/* 55 */   private static Map<Integer, String> _constants = Utils.createConstantMap(ProtoFamily.class);
/*    */   
/* 57 */   public static String toString(int value) { return (String)_constants.get(Integer.valueOf(value)); }
/*    */ }


/* Location:              C:\Users\Joey\Downloads\codejar.jar!\com\emt\proteus\runtime32\sockets\ProtoFamily.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */