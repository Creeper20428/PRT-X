/*     */ package com.emt.proteus.runtime32.api;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.OutputStream;
/*     */ import java.nio.ByteBuffer;
/*     */ import java.nio.channels.Channel;
/*     */ import java.nio.channels.ReadableByteChannel;
/*     */ import java.nio.channels.WritableByteChannel;
/*     */ 
/*     */ 
/*     */ 
/*     */ public class Connection
/*     */ {
/*  15 */   public static final int[] XPORT = { 6000, 6001, 6002 };
/*  16 */   public static final byte[] LOCALHOST = { Byte.MAX_VALUE, 0, 0, 1 };
/*     */   private final String name;
/*     */   
/*     */   public Connection(String name)
/*     */   {
/*  21 */     this.name = name;
/*     */   }
/*     */   
/*     */ 
/*     */   public void close() {}
/*     */   
/*     */ 
/*     */   public String getName()
/*     */   {
/*  30 */     return this.name;
/*     */   }
/*     */   
/*     */   public void writeBytes(byte[] bytes, int offset, int length) throws IOException
/*     */   {}
/*     */   
/*     */   public int readBytes(byte[] bytes, int offset, int length) throws IOException {
/*  37 */     return -1;
/*     */   }
/*     */   
/*     */   public void flush() throws IOException
/*     */   {}
/*     */   
/*     */   public int available() throws IOException
/*     */   {
/*  45 */     return 0;
/*     */   }
/*     */   
/*     */   public boolean writeAvailable() throws IOException {
/*  49 */     return true;
/*     */   }
/*     */   
/*     */   public boolean readAvailable() throws IOException
/*     */   {
/*  54 */     return available() != 0;
/*     */   }
/*     */   
/*     */ 
/*     */   public static boolean isLocalHost(byte[] ip)
/*     */   {
/*  60 */     for (int i = 0; i < ip.length; i++) {
/*  61 */       if (LOCALHOST[i] != ip[i]) return false;
/*     */     }
/*  63 */     return true;
/*     */   }
/*     */   
/*     */   public static boolean isXServer(byte[] ip, int port) {
/*  67 */     return (isLocalHost(ip)) && (isXServerPort(port));
/*     */   }
/*     */   
/*     */   public static boolean isXServerPort(int port) {
/*  71 */     for (int i = 0; i < XPORT.length; i++) {
/*  72 */       if (port == XPORT[i]) return true;
/*     */     }
/*  74 */     return false;
/*     */   }
/*     */   
/*     */   public static class NIO extends Connection
/*     */   {
/*     */     private final ReadableByteChannel in;
/*     */     private final WritableByteChannel out;
/*     */     private final ByteBuffer readBuffer;
/*     */     private final ByteBuffer writeBuffer;
/*     */     
/*     */     public NIO(String name, Channel inout)
/*     */     {
/*  86 */       super();
/*  87 */       this.in = ((ReadableByteChannel)((inout instanceof ReadableByteChannel) ? inout : null));
/*  88 */       this.out = ((WritableByteChannel)((inout instanceof WritableByteChannel) ? inout : null));
/*  89 */       this.readBuffer = ByteBuffer.allocate(4096);
/*  90 */       this.writeBuffer = ByteBuffer.allocate(4096);
/*     */     }
/*     */     
/*  93 */     public NIO(String name, ReadableByteChannel in, WritableByteChannel out) { super();
/*  94 */       this.in = in;
/*  95 */       this.out = out;
/*  96 */       this.readBuffer = ByteBuffer.allocate(4096);
/*  97 */       this.writeBuffer = ByteBuffer.allocate(4096);
/*     */     }
/*     */     
/*     */     public void writeBytes(byte[] bytes, int offset, int length)
/*     */       throws IOException
/*     */     {
/* 103 */       int total = 0;
/* 104 */       while (length > 0) {
/* 105 */         int remaining = this.writeBuffer.remaining();
/* 106 */         int tocopy = Math.min(length, remaining);
/* 107 */         this.writeBuffer.put(bytes, offset, tocopy);
/* 108 */         this.writeBuffer.flip();
/* 109 */         this.out.write(this.writeBuffer);
/* 110 */         this.writeBuffer.compact();
/* 111 */         offset += tocopy;
/* 112 */         total += tocopy;
/* 113 */         length -= tocopy;
/*     */       }
/*     */     }
/*     */     
/*     */ 
/*     */     public int readBytes(byte[] bytes, int offset, int length)
/*     */       throws IOException
/*     */     {
/* 121 */       int total = 0;
/*     */       int read;
/* 123 */       do { read = this.in.read(this.readBuffer);
/* 124 */         this.readBuffer.flip();
/* 125 */         int remaining = this.readBuffer.remaining();
/* 126 */         int tocopy = Math.min(length, remaining);
/* 127 */         this.readBuffer.get(bytes, offset, tocopy);
/* 128 */         this.readBuffer.compact();
/* 129 */         offset += tocopy;
/* 130 */         total += tocopy;
/* 131 */         length -= tocopy;
/*     */       }
/* 133 */       while ((read != 0) && (length > 0));
/*     */       
/* 135 */       return total == 0 ? -1 : total;
/*     */     }
/*     */     
/*     */     public void flush()
/*     */       throws IOException
/*     */     {}
/*     */     
/*     */     public void close()
/*     */     {
/* 144 */       if ((this.in != null) && (this.in.isOpen())) {
/*     */         try {
/* 146 */           this.in.close();
/*     */         } catch (IOException e) {
/* 148 */           e.printStackTrace();
/*     */         }
/*     */       }
/* 151 */       if ((this.out != null) && (this.out != this.in) && (this.out.isOpen())) {
/*     */         try {
/* 153 */           this.out.close();
/*     */         } catch (IOException e) {
/* 155 */           e.printStackTrace();
/*     */         }
/*     */       }
/*     */     }
/*     */     
/*     */     public int available() throws IOException
/*     */     {
/* 162 */       int available = this.readBuffer.capacity() - this.readBuffer.remaining();
/* 163 */       if (available > 0) return available;
/* 164 */       return this.in.read(this.readBuffer);
/*     */     }
/*     */     
/*     */     public boolean writeAvailable() {
/* 168 */       if (this.writeBuffer.hasRemaining()) {
/* 169 */         return true;
/*     */       }
/* 171 */       return false;
/*     */     }
/*     */   }
/*     */   
/*     */   public static class Stream extends Connection
/*     */   {
/*     */     private final InputStream input;
/*     */     private final OutputStream output;
/*     */     
/*     */     public Stream(String name, InputStream input, OutputStream output) {
/* 181 */       super();
/* 182 */       this.input = input;
/* 183 */       this.output = output;
/*     */     }
/*     */     
/*     */     public InputStream getInput() {
/* 187 */       return this.input;
/*     */     }
/*     */     
/*     */     public OutputStream getOutput() {
/* 191 */       return this.output;
/*     */     }
/*     */     
/*     */     public void close() {
/*     */       try {
/* 196 */         this.input.close();
/*     */       }
/*     */       catch (IOException e) {}
/*     */       try
/*     */       {
/* 201 */         this.output.close();
/*     */       }
/*     */       catch (IOException e) {}
/*     */     }
/*     */     
/*     */     public void writeBytes(byte[] bytes, int offset, int length) throws IOException
/*     */     {
/* 208 */       this.output.write(bytes, offset, length);
/* 209 */       this.output.flush();
/*     */     }
/*     */     
/*     */     public int readBytes(byte[] bytes, int offset, int length) throws IOException {
/* 213 */       int len = Math.min(length, this.input.available());
/* 214 */       return this.input.read(bytes, offset, len);
/*     */     }
/*     */     
/*     */     public void flush() throws IOException {
/* 218 */       this.output.flush();
/*     */     }
/*     */     
/*     */     public int available() throws IOException {
/* 222 */       return this.input.available();
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Joey\Downloads\codejar.jar!\com\emt\proteus\runtime32\api\Connection.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */