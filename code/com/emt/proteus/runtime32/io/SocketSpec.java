/*    */ package com.emt.proteus.runtime32.io;
/*    */ 
/*    */ import java.util.LinkedHashMap;
/*    */ import java.util.Map;
/*    */ 
/*    */ 
/*    */ public final class SocketSpec
/*    */ {
/*    */   private String name;
/*    */   private int port;
/*    */   public SocketSpec() {}
/*    */   
/*    */   public static enum Types
/*    */   {
/* 15 */     TCP,  FILE,  UDP,  NETLINK;
/*    */     
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */     private Types() {}
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/* 27 */   private final Map<String, Object> attributes = new LinkedHashMap();
/*    */   
/*    */ 
/*    */ 
/*    */   public SocketSpec(String name, int port)
/*    */   {
/* 33 */     this.name = name;
/* 34 */     this.port = port;
/*    */   }
/*    */ }


/* Location:              C:\Users\Joey\Downloads\codejar.jar!\com\emt\proteus\runtime32\io\SocketSpec.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */