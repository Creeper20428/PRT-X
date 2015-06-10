/*    */ package com.emt.proteus.xserver.io;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import java.io.InputStream;
/*    */ import java.io.OutputStream;
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
/*    */ public final class IOLSB
/*    */   extends ComChannel
/*    */ {
/*    */   public IOLSB() {}
/*    */   
/*    */   public IOLSB(InputStream input, OutputStream output)
/*    */   {
/* 32 */     super(input, output);
/*    */   }
/*    */   
/*    */   public int readShort() throws IOException {
/* 36 */     if (this.inrest < 2) {
/* 37 */       read(2);
/*    */     }
/* 39 */     this.inrest -= 2;
/* 40 */     int s = this.inbuffer[(this.instart++)] & 0xFF;
/* 41 */     s |= (this.inbuffer[(this.instart++)] & 0xFF) << 8;
/* 42 */     return s;
/*    */   }
/*    */   
/*    */   public int readInt() throws IOException {
/* 46 */     if (this.inrest < 4) {
/* 47 */       read(4);
/*    */     }
/* 49 */     this.inrest -= 4;
/* 50 */     int i = this.inbuffer[(this.instart++)] & 0xFF;
/* 51 */     i |= (this.inbuffer[(this.instart++)] & 0xFF) << 8;
/* 52 */     i |= (this.inbuffer[(this.instart++)] & 0xFF) << 16;
/* 53 */     i |= (this.inbuffer[(this.instart++)] & 0xFF) << 24;
/* 54 */     return i;
/*    */   }
/*    */   
/*    */   public void writeShort(int val) throws IOException {
/* 58 */     if (this.outbuffer.length - this.outindex < 2) {
/* 59 */       flush();
/*    */     }
/* 61 */     this.outbuffer[(this.outindex++)] = ((byte)(val & 0xFF));
/* 62 */     this.outbuffer[(this.outindex++)] = ((byte)(val >> 8 & 0xFF));
/*    */   }
/*    */   
/*    */   public void writeInt(int val) throws IOException {
/* 66 */     if (this.outbuffer.length - this.outindex < 4) {
/* 67 */       flush();
/*    */     }
/* 69 */     this.outbuffer[(this.outindex++)] = ((byte)(val & 0xFF));
/* 70 */     this.outbuffer[(this.outindex++)] = ((byte)(val >> 8 & 0xFF));
/* 71 */     this.outbuffer[(this.outindex++)] = ((byte)(val >> 16 & 0xFF));
/* 72 */     this.outbuffer[(this.outindex++)] = ((byte)(val >> 24 & 0xFF));
/*    */   }
/*    */ }


/* Location:              C:\Users\Joey\Downloads\launch.jar!\com\emt\proteus\xserver\io\IOLSB.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */