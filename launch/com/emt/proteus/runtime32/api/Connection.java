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
/*     */   public static final int XPORT = 6002;
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
/*  67 */     return (isLocalHost(ip)) && (port == 6002);
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
/*  79 */       super();
/*  80 */       this.in = ((ReadableByteChannel)((inout instanceof ReadableByteChannel) ? inout : null));
/*  81 */       this.out = ((WritableByteChannel)((inout instanceof WritableByteChannel) ? inout : null));
/*  82 */       this.readBuffer = ByteBuffer.allocate(4096);
/*  83 */       this.writeBuffer = ByteBuffer.allocate(4096);
/*     */     }
/*     */     
/*  86 */     public NIO(String name, ReadableByteChannel in, WritableByteChannel out) { super();
/*  87 */       this.in = in;
/*  88 */       this.out = out;
/*  89 */       this.readBuffer = ByteBuffer.allocate(4096);
/*  90 */       this.writeBuffer = ByteBuffer.allocate(4096);
/*     */     }
/*     */     
/*     */     public void writeBytes(byte[] bytes, int offset, int length)
/*     */       throws IOException
/*     */     {
/*  96 */       int total = 0;
/*  97 */       while (length > 0) {
/*  98 */         int remaining = this.writeBuffer.remaining();
/*  99 */         int tocopy = Math.min(length, remaining);
/* 100 */         this.writeBuffer.put(bytes, offset, tocopy);
/* 101 */         this.writeBuffer.flip();
/* 102 */         this.out.write(this.writeBuffer);
/* 103 */         this.writeBuffer.compact();
/* 104 */         offset += tocopy;
/* 105 */         total += tocopy;
/* 106 */         length -= tocopy;
/*     */       }
/*     */     }
/*     */     
/*     */ 
/*     */     public int readBytes(byte[] bytes, int offset, int length)
/*     */       throws IOException
/*     */     {
/* 114 */       int total = 0;
/*     */       int read;
/* 116 */       do { read = this.in.read(this.readBuffer);
/* 117 */         this.readBuffer.flip();
/* 118 */         int remaining = this.readBuffer.remaining();
/* 119 */         int tocopy = Math.min(length, remaining);
/* 120 */         this.readBuffer.get(bytes, offset, tocopy);
/* 121 */         this.readBuffer.compact();
/* 122 */         offset += tocopy;
/* 123 */         total += tocopy;
/* 124 */         length -= tocopy;
/*     */       }
/* 126 */       while ((read != 0) && (length > 0));
/*     */       
/* 128 */       return total == 0 ? -1 : total;
/*     */     }
/*     */     
/*     */     public void flush()
/*     */       throws IOException
/*     */     {}
/*     */     
/*     */     public void close()
/*     */     {
/* 137 */       if ((this.in != null) && (this.in.isOpen())) {
/*     */         try {
/* 139 */           this.in.close();
/*     */         } catch (IOException e) {
/* 141 */           e.printStackTrace();
/*     */         }
/*     */       }
/* 144 */       if ((this.out != null) && (this.out != this.in) && (this.out.isOpen())) {
/*     */         try {
/* 146 */           this.out.close();
/*     */         } catch (IOException e) {
/* 148 */           e.printStackTrace();
/*     */         }
/*     */       }
/*     */     }
/*     */     
/*     */     public int available() throws IOException
/*     */     {
/* 155 */       int available = this.readBuffer.capacity() - this.readBuffer.remaining();
/* 156 */       if (available > 0) return available;
/* 157 */       return this.in.read(this.readBuffer);
/*     */     }
/*     */     
/*     */     public boolean writeAvailable() {
/* 161 */       if (this.writeBuffer.hasRemaining()) {
/* 162 */         return true;
/*     */       }
/* 164 */       return false;
/*     */     }
/*     */   }
/*     */   
/*     */   public static class Stream extends Connection
/*     */   {
/*     */     private final InputStream input;
/*     */     private final OutputStream output;
/*     */     
/*     */     public Stream(String name, InputStream input, OutputStream output) {
/* 174 */       super();
/* 175 */       this.input = input;
/* 176 */       this.output = output;
/*     */     }
/*     */     
/*     */     public InputStream getInput() {
/* 180 */       return this.input;
/*     */     }
/*     */     
/*     */     public OutputStream getOutput() {
/* 184 */       return this.output;
/*     */     }
/*     */     
/*     */     public void close() {
/*     */       try {
/* 189 */         this.input.close();
/*     */       }
/*     */       catch (IOException e) {}
/*     */       try
/*     */       {
/* 194 */         this.output.close();
/*     */       }
/*     */       catch (IOException e) {}
/*     */     }
/*     */     
/*     */     public void writeBytes(byte[] bytes, int offset, int length) throws IOException
/*     */     {
/* 201 */       this.output.write(bytes, offset, length);
/* 202 */       this.output.flush();
/*     */     }
/*     */     
/*     */     public int readBytes(byte[] bytes, int offset, int length) throws IOException {
/* 206 */       int len = Math.min(length, this.input.available());
/* 207 */       return this.input.read(bytes, offset, len);
/*     */     }
/*     */     
/*     */     public void flush() throws IOException {
/* 211 */       this.output.flush();
/*     */     }
/*     */     
/*     */     public int available() throws IOException {
/* 215 */       return this.input.available();
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Joey\Downloads\launch.jar!\com\emt\proteus\runtime32\api\Connection.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */