/*     */ package com.emt.proteus.demo.common;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.OutputStream;
/*     */ import java.io.PrintStream;
/*     */ import java.io.UnsupportedEncodingException;
/*     */ 
/*     */ 
/*     */ public class ConsoleStream
/*     */   extends PrintStream
/*     */ {
/*     */   private BackingStream backing;
/*     */   
/*     */   public ConsoleStream()
/*     */     throws UnsupportedEncodingException
/*     */   {
/*  17 */     this(65536, true, "US-ASCII");
/*     */   }
/*     */   
/*     */   public ConsoleStream(int backContent, boolean autoFlush, String encoding) throws UnsupportedEncodingException
/*     */   {
/*  22 */     super(new BackingStream(backContent), autoFlush, encoding);
/*  23 */     this.backing = ((BackingStream)this.out);
/*     */   }
/*     */   
/*     */   public void clear()
/*     */   {
/*  28 */     this.backing.clear();
/*     */   }
/*     */   
/*     */   public String getConsoleOutput()
/*     */   {
/*  33 */     flush();
/*     */     
/*  35 */     int len = this.backing.contentLength();
/*  36 */     if (len == 0) return "";
/*  37 */     byte[] buffer = new byte[len];
/*  38 */     int read = this.backing.readContents(0, buffer);
/*  39 */     int start = 0;
/*  40 */     int limit = read - 1;
/*  41 */     while ((start < read) && (buffer[start] != 10)) start++;
/*  42 */     if (start < limit) {
/*  43 */       start++;
/*     */     }
/*     */     else {
/*  46 */       start = 0;
/*     */     }
/*  48 */     for (int i = 0; i < buffer.length; i++) {
/*  49 */       int val = 0xFF & buffer[i];
/*  50 */       if (val < 32) {
/*  51 */         if ((val != 10) && (val != 13) && (val != 9))
/*  52 */           buffer[i] = 63;
/*  53 */       } else if (val >= 127) {
/*  54 */         buffer[i] = 63;
/*     */       }
/*     */     }
/*     */     try {
/*  58 */       return new String(buffer, start, read - start, "US-ASCII");
/*     */     } catch (Exception e) {}
/*  60 */     return new String(buffer, start, read - start);
/*     */   }
/*     */   
/*     */ 
/*     */   public void close() {}
/*     */   
/*     */ 
/*     */   private static class BackingStream
/*     */     extends OutputStream
/*     */   {
/*     */     private final int mask;
/*     */     private int position;
/*     */     private byte[] buffer;
/*     */     
/*     */     public BackingStream(int backlog)
/*     */     {
/*  76 */       int pow2 = Integer.highestOneBit(backlog);
/*  77 */       if (pow2 < backlog) pow2 <<= 1;
/*  78 */       this.mask = (pow2 - 1);
/*  79 */       this.buffer = new byte[pow2];
/*     */     }
/*     */     
/*  82 */     public synchronized void clear() { this.position = 0; }
/*     */     
/*     */     public synchronized int contentLength()
/*     */     {
/*  86 */       return Math.min(this.position, this.buffer.length);
/*     */     }
/*     */     
/*     */     public synchronized int readContents(int offset, byte[] buffer) {
/*  90 */       int toWrite = buffer.length - offset;
/*  91 */       if ((this.position == 0) || (toWrite == 0)) return 0;
/*  92 */       int start = Math.max(0, this.position - toWrite) & this.mask;
/*  93 */       int end = this.position & this.mask;
/*  94 */       if (start < end) {
/*  95 */         System.arraycopy(this.buffer, start, buffer, offset, end - start);
/*     */       } else {
/*  97 */         int n = this.buffer.length - end;
/*  98 */         System.arraycopy(this.buffer, end, buffer, offset, n);
/*  99 */         System.arraycopy(this.buffer, 0, buffer, offset + n, start);
/*     */       }
/* 101 */       return toWrite;
/*     */     }
/*     */     
/* 104 */     public synchronized void write(int b) { this.buffer[(this.position++ & this.mask)] = ((byte)b); }
/*     */     
/*     */     public synchronized void write(byte[] b, int off, int len)
/*     */     {
/* 108 */       int index = this.position & this.mask;
/* 109 */       int first = this.buffer.length - index;
/* 110 */       int amt = Math.min(len, first);
/* 111 */       System.arraycopy(b, off, this.buffer, index, amt);
/* 112 */       this.position += amt;
/* 113 */       len -= amt;
/* 114 */       off += amt;
/* 115 */       if (len > 0) {
/* 116 */         System.arraycopy(b, off, this.buffer, 0, len);
/* 117 */         this.position += len;
/*     */       }
/*     */     }
/*     */     
/*     */     public void close()
/*     */       throws IOException
/*     */     {}
/*     */   }
/*     */ }


/* Location:              C:\Users\Joey\Downloads\launch.jar!\com\emt\proteus\demo\common\ConsoleStream.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */