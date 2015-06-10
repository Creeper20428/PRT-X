/*    */ package com.emt.proteus.xserver.server;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import java.net.Socket;
/*    */ 
/*    */ 
/*    */ public class SocketXConnection
/*    */   extends XConnection
/*    */ {
/*    */   private Socket socket;
/*    */   
/*    */   public SocketXConnection(Socket socket)
/*    */     throws IOException
/*    */   {
/* 15 */     super(socket.getOutputStream(), socket.getInputStream());
/* 16 */     this.socket = socket;
/*    */   }
/*    */   
/*    */ 
/*    */   public void dispose()
/*    */   {
/* 22 */     super.dispose();
/*    */     try {
/* 24 */       this.socket.close();
/*    */     } catch (IOException e) {
/* 26 */       e.printStackTrace();
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\Joey\Downloads\launch.jar!\com\emt\proteus\xserver\server\SocketXConnection.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */