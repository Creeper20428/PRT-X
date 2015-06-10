/*     */ package com.emt.proteus.xserver.io;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.OutputStream;
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
/*     */ public abstract class ComChannel
/*     */ {
/*  26 */   private InputStream in = null;
/*  27 */   private OutputStream out = null;
/*  28 */   byte[] inbuffer = new byte['Ѐ'];
/*  29 */   byte[] outbuffer = new byte['Ѐ'];
/*  30 */   int instart = 0; int inend = 0; int outindex = 0; int inrest = 0;
/*     */   
/*     */   ComChannel() {}
/*     */   
/*  34 */   byte[] ba = new byte[1]; byte[] sa = new byte[2]; byte[] ia = new byte[8];
/*     */   
/*     */   public ComChannel(InputStream input, OutputStream output) {
/*  37 */     this(); }
/*     */   
/*     */   public abstract int readShort() throws IOException;
/*     */   
/*     */   public abstract int readInt() throws IOException;
/*     */   
/*     */   public abstract void writeShort(int paramInt) throws IOException;
/*     */   
/*     */   public abstract void writeInt(int paramInt) throws IOException;
/*     */   
/*  47 */   public void setInputStream(InputStream in) { this.in = in; }
/*  48 */   public void setOutputStream(OutputStream out) { this.out = out; }
/*     */   
/*  50 */   public int available() throws IOException { if (0 < this.inrest) return 1;
/*  51 */     return this.in.available();
/*     */   }
/*     */   
/*  54 */   public int readByte() throws IOException { if (this.inrest < 1) read(1);
/*  55 */     this.inrest -= 1;
/*  56 */     return this.inbuffer[(this.instart++)] & 0xFF;
/*     */   }
/*     */   
/*  59 */   public void readByte(byte[] array) throws IOException { readByte(array, 0, array.length); }
/*     */   
/*     */   public void readByte(byte[] array, int begin, int length) throws IOException {
/*  62 */     int i = 0;
/*     */     
/*  64 */     while ((i = this.inrest) < length) {
/*  65 */       if (i != 0) {
/*  66 */         System.arraycopy(this.inbuffer, this.instart, array, begin, i);
/*  67 */         begin += i;
/*  68 */         length -= i;
/*  69 */         this.instart += i;
/*  70 */         this.inrest -= i;
/*     */       }
/*  72 */       read(length);
/*     */     }
/*     */     
/*  75 */     System.arraycopy(this.inbuffer, this.instart, array, begin, length);
/*  76 */     this.instart += length;
/*  77 */     this.inrest -= length;
/*     */   }
/*     */   
/*     */   public void readPad(int n)
/*     */     throws IOException
/*     */   {
/*  83 */     while (n > 0) {
/*  84 */       if (this.inrest < n) {
/*  85 */         n -= this.inrest;
/*  86 */         this.instart += this.inrest;
/*  87 */         this.inrest = 0;
/*  88 */         read(n);
/*     */       }
/*     */       else {
/*  91 */         this.instart += n;
/*  92 */         this.inrest -= n;
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   protected final void read(int n) throws IOException {
/*  98 */     if (n > this.inbuffer.length) {
/*  99 */       n = this.inbuffer.length;
/*     */     }
/* 101 */     this.instart = (this.inend = 0);
/*     */     
/*     */ 
/*     */     for (;;)
/*     */     {
/* 106 */       int i = this.in.read(this.inbuffer, this.inend, this.inbuffer.length - this.inend);
/* 107 */       if (i == -1) throw new IOException();
/* 108 */       this.inend += i;
/* 109 */       if (n <= this.inend) break;
/*     */     }
/* 111 */     this.inrest = (this.inend - this.instart);
/*     */   }
/*     */   
/*     */   public void writeByte(byte val) throws IOException {
/* 115 */     if (this.outbuffer.length - this.outindex < 1) flush();
/* 116 */     this.outbuffer[(this.outindex++)] = val;
/*     */   }
/*     */   
/*     */   public void writeByte(int val) throws IOException {
/* 120 */     writeByte((byte)val);
/*     */   }
/*     */   
/*     */   public void writeByte(byte[] array) throws IOException {
/* 124 */     writeByte(array, 0, array.length);
/*     */   }
/*     */   
/*     */   public void writeByte(byte[] array, int begin, int length) throws IOException {
/* 128 */     if (length <= 0) return;
/* 129 */     int i = 0;
/*     */     
/* 131 */     while ((i = this.outbuffer.length - this.outindex) < length) {
/* 132 */       if (i != 0) {
/* 133 */         System.arraycopy(array, begin, this.outbuffer, this.outindex, i);
/* 134 */         begin += i;
/* 135 */         length -= i;
/* 136 */         this.outindex += i;
/*     */       }
/* 138 */       flush();
/*     */     }
/*     */     
/* 141 */     System.arraycopy(array, begin, this.outbuffer, this.outindex, length);
/* 142 */     this.outindex += length;
/*     */   }
/*     */   
/*     */ 
/*     */   public void writePad(int n)
/*     */     throws IOException
/*     */   {
/*     */     int i;
/* 150 */     while ((i = this.outbuffer.length - this.outindex) < n) {
/* 151 */       if (i != 0) {
/* 152 */         this.outindex += i;
/* 153 */         n -= i;
/*     */       }
/* 155 */       flush();
/*     */     }
/*     */     
/* 158 */     this.outindex += n;
/*     */   }
/*     */   
/*     */   public synchronized void flush()
/*     */     throws IOException
/*     */   {
/* 164 */     if (this.outindex == 0) return;
/* 165 */     this.out.write(this.outbuffer, 0, this.outindex);
/* 166 */     this.outindex = 0;
/*     */   }
/*     */   
/* 169 */   public synchronized void immediateWrite(byte[] array, int b, int l) throws IOException { this.out.write(array, b, l); }
/*     */   
/*     */   public void close() throws IOException {
/* 172 */     this.in.close();this.out.close();
/*     */   }
/*     */ }


/* Location:              C:\Users\Joey\Downloads\launch.jar!\com\emt\proteus\xserver\io\ComChannel.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */