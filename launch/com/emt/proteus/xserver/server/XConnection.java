/*    */ package com.emt.proteus.xserver.server;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import java.io.InputStream;
/*    */ import java.io.OutputStream;
/*    */ 
/*    */ 
/*    */ public class XConnection
/*    */ {
/*    */   private OutputStream outputStream;
/*    */   private InputStream inputStream;
/*    */   
/*    */   public XConnection(OutputStream outputStream, InputStream inputStream)
/*    */   {
/* 15 */     this.outputStream = outputStream;
/* 16 */     this.inputStream = inputStream;
/*    */   }
/*    */   
/*    */   public OutputStream getOutputStream() throws IOException {
/* 20 */     return this.outputStream;
/*    */   }
/*    */   
/*    */   public InputStream getInputStream() throws IOException {
/* 24 */     return this.inputStream;
/*    */   }
/*    */   
/*    */   public void dispose() {
/*    */     try {
/* 29 */       this.inputStream.close();
/*    */     } catch (IOException e) {
/* 31 */       e.printStackTrace();
/*    */     }
/*    */     try {
/* 34 */       this.outputStream.close();
/*    */     } catch (IOException e) {
/* 36 */       e.printStackTrace();
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\Joey\Downloads\launch.jar!\com\emt\proteus\xserver\server\XConnection.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */