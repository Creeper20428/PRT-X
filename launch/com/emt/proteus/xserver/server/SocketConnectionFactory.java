/*    */ package com.emt.proteus.xserver.server;
/*    */ 
/*    */ import com.emt.proteus.xserver.api.Context;
/*    */ import java.io.IOException;
/*    */ import java.net.Inet4Address;
/*    */ import java.net.ServerSocket;
/*    */ import java.net.Socket;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class SocketConnectionFactory
/*    */   implements ConnectionFactory
/*    */ {
/* 15 */   public static final byte[] IP = { Byte.MAX_VALUE, 0, 0, 1 };
/*    */   private ServerSocket serverSocket;
/*    */   
/*    */   public void start() throws IOException
/*    */   {
/* 20 */     this.serverSocket = new ServerSocket(6002, 1, Inet4Address.getByAddress(IP));
/*    */   }
/*    */   
/*    */   public XConnection accept(long timeoutMillis) throws IOException
/*    */   {
/* 25 */     Socket socket = this.serverSocket.accept();
/* 26 */     socket.setTcpNoDelay(true);
/*    */     
/* 28 */     return new SocketXConnection(socket);
/*    */   }
/*    */   
/*    */   public void close()
/*    */     throws IOException
/*    */   {}
/*    */   
/*    */   public void initialize(Context context) {}
/*    */ }


/* Location:              C:\Users\Joey\Downloads\launch.jar!\com\emt\proteus\xserver\server\SocketConnectionFactory.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */