/*     */ package com.emt.proteus.runtime32.io;
/*     */ 
/*     */ import com.emt.proteus.runtime32.memory.MainMemory;
/*     */ import java.io.IOException;
/*     */ import java.io.PrintStream;
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
/*     */ public class PrintStreamIoHandle
/*     */   extends IoHandle
/*     */ {
/*     */   private final PrintStream stream;
/*     */   
/*     */   public PrintStreamIoHandle(IoSys sys, String path, PrintStream stream, int descriptor)
/*     */   {
/*  42 */     super(sys, path, "w", descriptor);
/*  43 */     this.stream = stream;
/*     */   }
/*     */   
/*     */ 
/*     */   public int flush()
/*     */   {
/*  49 */     this.stream.flush();
/*  50 */     return 0;
/*     */   }
/*     */   
/*     */   public void writeByte(int i) throws IOException
/*     */   {
/*  55 */     this.stream.print((char)i);
/*     */   }
/*     */   
/*     */   public int writeBytes(byte[] bytes, int offset, int length) throws IOException
/*     */   {
/*  60 */     this.stream.write(bytes, offset, length);
/*  61 */     return length;
/*     */   }
/*     */   
/*     */   public void closeImpl() throws IOException
/*     */   {
/*  66 */     this.stream.close();
/*     */   }
/*     */   
/*     */   public int readBytes(byte[] bytes, int offset, int length) throws IOException
/*     */   {
/*  71 */     throw new UnsupportedOperationException();
/*     */   }
/*     */   
/*     */   public int readByte() throws IOException
/*     */   {
/*  76 */     throw new UnsupportedOperationException();
/*     */   }
/*     */   
/*     */   public long seekImpl(long offset, int from) throws IOException
/*     */   {
/*  81 */     throw new UnsupportedOperationException();
/*     */   }
/*     */   
/*     */   public void truncate(long newsize) throws IOException
/*     */   {
/*  86 */     throw new UnsupportedOperationException();
/*     */   }
/*     */   
/*     */   public int getType()
/*     */   {
/*  91 */     return -2;
/*     */   }
/*     */   
/*     */   public long getLength() {
/*  95 */     return -1L;
/*     */   }
/*     */   
/*     */   public long getPosition()
/*     */   {
/* 100 */     return -1L;
/*     */   }
/*     */   
/*     */   public int stat64(MainMemory mem, int stat64$, long currentTime)
/*     */   {
/* 105 */     return Stat.stat64Terminal(mem, stat64$, false, currentTime, currentTime);
/*     */   }
/*     */ }


/* Location:              C:\Users\Joey\Downloads\codejar.jar!\com\emt\proteus\runtime32\io\PrintStreamIoHandle.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */