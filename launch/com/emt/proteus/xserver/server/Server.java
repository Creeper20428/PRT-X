/*    */ package com.emt.proteus.xserver.server;
/*    */ 
/*    */ import com.emt.proteus.xserver.api.Context;
/*    */ import com.emt.proteus.xserver.client.XClient;
/*    */ import com.emt.proteus.xserver.display.XDisplay;
/*    */ import com.emt.proteus.xserver.io.ComChannel;
/*    */ import java.io.IOException;
/*    */ import java.io.InputStream;
/*    */ import java.io.OutputStream;
/*    */ import java.util.concurrent.ExecutorService;
/*    */ import java.util.concurrent.Executors;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class Server
/*    */   implements Runnable
/*    */ {
/*    */   private ExecutorService connections;
/*    */   private volatile boolean running;
/*    */   private ConnectionFactory factory;
/* 27 */   private byte[] sniff = new byte[1];
/*    */   private XDisplay XDisplay;
/*    */   
/*    */   public void initialize(Context context) {
/* 31 */     String key = "";String def = "";
/* 32 */     context.set("server", this);
/* 33 */     this.factory = ((ConnectionFactory)context.getInstance("connection.factory", def));
/*    */     
/* 35 */     this.connections = Executors.newSingleThreadScheduledExecutor();
/*    */     
/*    */ 
/* 38 */     this.XDisplay = new XDisplay();
/* 39 */     this.XDisplay.initialize(context);
/*    */   }
/*    */   
/*    */ 
/*    */   public void start() {}
/*    */   
/*    */   public void run()
/*    */   {
/*    */     try
/*    */     {
/* 49 */       this.factory.start();
/*    */     } catch (IOException e) {
/* 51 */       e.printStackTrace();
/* 52 */       return;
/*    */     }
/* 54 */     this.running = true;
/*    */     try {
/* 56 */       while (this.running) {
/*    */         try {
/* 58 */           XConnection XConnection = this.factory.accept(2000L);
/* 59 */           XClient xXClient = connect(XConnection);
/* 60 */           if (xXClient != null) {
/* 61 */             this.connections.submit(xXClient);
/*    */           }
/*    */         } catch (IOException e) {
/* 64 */           e.printStackTrace();
/*    */         }
/*    */       }
/*    */     } finally {
/* 68 */       this.running = false;
/*    */     }
/*    */   }
/*    */   
/*    */   public void connectAndRun(XConnection XConnection) {}
/*    */   
/*    */   public XClient connect(XConnection XConnection)
/*    */     throws IOException
/*    */   {
/* 77 */     if (XConnection == null) return null;
/* 78 */     ComChannel channel = null;
/* 79 */     InputStream inputStream = XConnection.getInputStream();
/* 80 */     OutputStream outputStream = XConnection.getOutputStream();
/* 81 */     return connect(inputStream, outputStream);
/*    */   }
/*    */   
/*    */   public XClient connect(InputStream inputStream, OutputStream outputStream) throws IOException {
/* 85 */     XDisplay XDisplay = this.XDisplay;
/* 86 */     return XClient.connect(XDisplay, inputStream, outputStream);
/*    */   }
/*    */   
/*    */   public void dispose() {
/*    */     try {
/* 91 */       this.factory.close();
/*    */     } catch (IOException e) {
/* 93 */       e.printStackTrace();
/*    */     }
/* 95 */     this.connections.shutdownNow();
/*    */   }
/*    */ }


/* Location:              C:\Users\Joey\Downloads\launch.jar!\com\emt\proteus\xserver\server\Server.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */