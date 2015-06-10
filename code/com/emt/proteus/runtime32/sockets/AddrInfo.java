/*    */ package com.emt.proteus.runtime32.sockets;
/*    */ 
/*    */ import com.emt.proteus.utils.CStruct;
/*    */ import com.emt.proteus.utils.CStruct.CField;
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
/*    */ public class AddrInfo
/*    */   extends CStruct
/*    */ {
/*    */   public static final int AI_PASSIVE = 1;
/*    */   public static final int AI_CANONNAME = 2;
/*    */   public static final int AI_NUMERICHOST = 4;
/*    */   public static final int AI_V4MAPPED = 8;
/*    */   public static final int AI_ALL = 16;
/*    */   public static final int AI_ADDRCONFIG = 32;
/*    */   public static final int AI_IDN = 64;
/*    */   public static final int AI_CANONIDN = 128;
/*    */   public static final int AI_IDN_ALLOW_UNASSIGNED = 256;
/*    */   public static final int AI_IDN_USE_STD3_ASCII_RULES = 512;
/*    */   public static final int AI_NUMERICSERV = 1024;
/* 37 */   public static final CStruct.CField ai_flags = _integer(0, "ai_flags");
/* 38 */   public static final CStruct.CField ai_family = _integer(ai_flags, "ai_family");
/* 39 */   public static final CStruct.CField ai_socktype = _integer(ai_family, "ai_socktype");
/* 40 */   public static final CStruct.CField ai_protocol = _integer(ai_socktype, "ai_protocol");
/* 41 */   public static final CStruct.CField ai_addrlen = _integer(ai_protocol, "ai_addrlen");
/* 42 */   public static final CStruct.CField ai_addr$ = _pointer(ai_addrlen, "ai_addr");
/* 43 */   public static final CStruct.CField ai_canonname$ = _pointer(ai_addr$, "ai_canonname");
/* 44 */   public static final CStruct.CField ai_next$ = _pointer(ai_canonname$, "ai_next");
/*    */   
/*    */   public AddrInfo() {
/* 47 */     super(ai_next$);
/*    */   }
/*    */ }


/* Location:              C:\Users\Joey\Downloads\codejar.jar!\com\emt\proteus\runtime32\sockets\AddrInfo.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */