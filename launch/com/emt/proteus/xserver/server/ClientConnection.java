/*    */ package com.emt.proteus.xserver.server;
/*    */ 
/*    */ import com.emt.proteus.xserver.client.XClient;
/*    */ 
/*    */ 
/*    */ 
/*    */ public class ClientConnection
/*    */ {
/*    */   private XConnection XConnection;
/*    */   private XClient XClient;
/*    */   
/*    */   public ClientConnection(XConnection XConnection, XClient XClient)
/*    */   {
/* 14 */     this.XConnection = XConnection;
/* 15 */     this.XClient = XClient;
/*    */   }
/*    */ }


/* Location:              C:\Users\Joey\Downloads\launch.jar!\com\emt\proteus\xserver\server\ClientConnection.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */