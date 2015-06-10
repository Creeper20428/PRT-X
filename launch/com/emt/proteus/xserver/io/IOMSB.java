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
/*    */ public final class IOMSB
/*    */   extends ComChannel
/*    */ {
/*    */   public IOMSB() {}
/*    */   
/*    */   public IOMSB(InputStream input, OutputStream output)
/*    */   {
/* 32 */     super(input, output);
/*    */   }
/*    */   
/*    */   public int readShort() throws IOException
/*    */   {
/* 37 */     if (this.inrest < 2) read(2);
/* 38 */     this.inrest -= 2;
/* 39 */     int s = this.inbuffer[(this.instart++)] & 0xFF;
/* 40 */     s = s << 8 & 0xFFFF | this.inbuffer[(this.instart++)] & 0xFF;
/* 41 */     return s;
/*    */   }
/*    */   
/* 44 */   public int readInt() throws IOException { if (this.inrest < 4) read(4);
/* 45 */     this.inrest -= 4;
/* 46 */     int i = this.inbuffer[(this.instart++)] & 0xFF;
/* 47 */     i = i << 8 & 0xFFFF | this.inbuffer[(this.instart++)] & 0xFF;
/* 48 */     i = i << 8 & 0xFFFFFF | this.inbuffer[(this.instart++)] & 0xFF;
/* 49 */     i = i << 8 | this.inbuffer[(this.instart++)] & 0xFF;
/* 50 */     return i;
/*    */   }
/*    */   
/* 53 */   public void writeShort(int val) throws IOException { if (this.outbuffer.length - this.outindex < 2) flush();
/* 54 */     this.outbuffer[(this.outindex++)] = ((byte)(val >> 8 & 0xFF));
/* 55 */     this.outbuffer[(this.outindex++)] = ((byte)(val & 0xFF));
/*    */   }
/*    */   
/* 58 */   public void writeInt(int val) throws IOException { if (this.outbuffer.length - this.outindex < 4) flush();
/* 59 */     this.outbuffer[(this.outindex++)] = ((byte)(val >> 24 & 0xFF));
/* 60 */     this.outbuffer[(this.outindex++)] = ((byte)(val >> 16 & 0xFF));
/* 61 */     this.outbuffer[(this.outindex++)] = ((byte)(val >> 8 & 0xFF));
/* 62 */     this.outbuffer[(this.outindex++)] = ((byte)(val & 0xFF));
/*    */   }
/*    */ }


/* Location:              C:\Users\Joey\Downloads\launch.jar!\com\emt\proteus\xserver\io\IOMSB.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */