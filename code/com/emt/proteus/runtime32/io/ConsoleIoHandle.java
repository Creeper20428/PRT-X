/*     */ package com.emt.proteus.runtime32.io;
/*     */ 
/*     */ import com.emt.proteus.runtime32.memory.MainMemory;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
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
/*     */ public class ConsoleIoHandle
/*     */   extends IoHandle
/*     */ {
/*     */   private final PrintStream outStream;
/*     */   private final InputStream input;
/*     */   
/*     */   public ConsoleIoHandle(IoSys sys, String name, int descriptor, int address)
/*     */   {
/*  44 */     this(sys, System.out, System.in, name, descriptor, address);
/*     */   }
/*     */   
/*     */   public ConsoleIoHandle(IoSys sys, PrintStream out, String name, int descriptor, int address) {
/*  48 */     this(sys, out, System.in, name, descriptor, address);
/*     */   }
/*     */   
/*     */   public ConsoleIoHandle(IoSys sys, PrintStream out, InputStream in, String name, int descriptor, int address) {
/*  52 */     super(sys, name, "rw", descriptor);
/*  53 */     this.outStream = out;
/*  54 */     this.input = in;
/*     */   }
/*     */   
/*     */   public int flush()
/*     */   {
/*  59 */     this.outStream.flush();
/*  60 */     return 0;
/*     */   }
/*     */   
/*     */   public void writeByte(int i) throws IOException
/*     */   {
/*  65 */     this.outStream.print((char)i);
/*     */   }
/*     */   
/*     */   public int writeBytes(byte[] bytes, int offset, int length) throws IOException
/*     */   {
/*  70 */     this.outStream.write(bytes, offset, length);
/*  71 */     return length;
/*     */   }
/*     */   
/*     */   public void closeImpl() throws IOException
/*     */   {
/*  76 */     this.outStream.close();
/*     */   }
/*     */   
/*     */   public int readBytes(byte[] bytes, int offset, int length) throws IOException
/*     */   {
/*  81 */     return this.input.read(bytes, offset, length);
/*     */   }
/*     */   
/*     */   public int readByte() throws IOException
/*     */   {
/*  86 */     return this.input.read();
/*     */   }
/*     */   
/*     */   public long seekImpl(long offset, int from) throws IOException
/*     */   {
/*  91 */     throw new UnsupportedOperationException();
/*     */   }
/*     */   
/*     */   public void truncate(long newsize) throws IOException
/*     */   {
/*  96 */     throw new UnsupportedOperationException();
/*     */   }
/*     */   
/*     */   public int getType()
/*     */   {
/* 101 */     return -1;
/*     */   }
/*     */   
/*     */   public long getLength() {
/* 105 */     return -1L;
/*     */   }
/*     */   
/*     */   public long getPosition()
/*     */   {
/* 110 */     return -1L;
/*     */   }
/*     */   
/*     */   protected boolean exists()
/*     */   {
/* 115 */     return true;
/*     */   }
/*     */   
/*     */   public int stat64(MainMemory mem, int stat64$, long currentTime) {
/* 119 */     return Stat.stat64Terminal(mem, stat64$, false, currentTime, currentTime);
/*     */   }
/*     */ }


/* Location:              C:\Users\Joey\Downloads\codejar.jar!\com\emt\proteus\runtime32\io\ConsoleIoHandle.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */