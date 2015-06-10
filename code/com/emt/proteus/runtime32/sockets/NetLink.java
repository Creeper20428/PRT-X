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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class NetLink
/*    */ {
/*    */   public static final int NETLINK_ROUTE = 0;
/*    */   public static final int NETLINK_UNUSED = 1;
/*    */   public static final int NETLINK_USERSOCK = 2;
/*    */   public static final int NETLINK_FIREWALL = 3;
/*    */   public static final int NETLINK_INET_DIAG = 4;
/*    */   public static final int NETLINK_NFLOG = 5;
/*    */   public static final int NETLINK_XFRM = 6;
/*    */   public static final int NETLINK_SELINUX = 7;
/*    */   public static final int NETLINK_ISCSI = 8;
/*    */   public static final int NETLINK_AUDIT = 9;
/*    */   public static final int NETLINK_FIB_LOOKUP = 10;
/*    */   public static final int NETLINK_CONNECTOR = 11;
/*    */   public static final int NETLINK_NETFILTER = 12;
/*    */   public static final int NETLINK_IP6_FW = 13;
/*    */   public static final int NETLINK_DNRTMSG = 14;
/*    */   public static final int NETLINK_KOBJECT_UEVENT = 15;
/*    */   public static final int NETLINK_GENERIC = 16;
/*    */   public static final int NETLINK_SCSITRANSPORT = 18;
/*    */   public static final int NETLINK_ECRYPTFS = 19;
/*    */   public static final int MAX_LINKS = 32;
/* 55 */   private static Map<Integer, String> _constants = Utils.createConstantMap(NetLink.class);
/*    */   
/* 57 */   public static String toString(int value) { return (String)_constants.get(Integer.valueOf(value)); }
/*    */ }


/* Location:              C:\Users\Joey\Downloads\codejar.jar!\com\emt\proteus\runtime32\sockets\NetLink.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */