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
/*     */ public final class IPProto
/*     */ {
/*     */   public static final int IP = 0;
/*     */   public static final int HOPOPTS = 0;
/*     */   public static final int ICMP = 1;
/*     */   public static final int IGMP = 2;
/*     */   public static final int IPIP = 4;
/*     */   public static final int TCP = 6;
/*     */   public static final int EGP = 8;
/*     */   public static final int PUP = 12;
/*     */   public static final int UDP = 17;
/*     */   public static final int IDP = 22;
/*     */   public static final int TP = 29;
/*     */   public static final int DCCP = 33;
/*     */   public static final int IPV6 = 41;
/*     */   public static final int ROUTING = 43;
/*     */   public static final int FRAGMENT = 44;
/*     */   public static final int RSVP = 46;
/*     */   public static final int GRE = 47;
/*     */   public static final int ESP = 50;
/*     */   public static final int AH = 51;
/*     */   public static final int ICMPV6 = 58;
/*     */   public static final int NONE = 59;
/*     */   public static final int DSTOPTS = 60;
/*     */   public static final int MTP = 92;
/*     */   public static final int ENCAP = 98;
/*     */   public static final int PIM = 103;
/*     */   public static final int COMP = 108;
/*     */   public static final int SCTP = 132;
/*     */   public static final int UDPLITE = 136;
/*     */   public static final int RAW = 255;
/* 102 */   private static Map<Integer, String> _constants = Utils.createConstantMap(IPProto.class);
/*     */   
/* 104 */   public static String toString(int value) { return (String)_constants.get(Integer.valueOf(value)); }
/*     */ }


/* Location:              C:\Users\Joey\Downloads\codejar.jar!\com\emt\proteus\runtime32\sockets\IPProto.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */