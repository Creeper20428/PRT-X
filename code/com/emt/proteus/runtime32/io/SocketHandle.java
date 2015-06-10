/*     */ package com.emt.proteus.runtime32.io;
/*     */ 
/*     */ import com.emt.proteus.runtime32.api.Connection;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class SocketHandle
/*     */   extends IoHandle
/*     */ {
/*     */   private int in;
/*     */   private int out;
/*     */   private Connection implementation;
/*  46 */   private byte[] small = new byte[3];
/*     */   private PSocket socket;
/*     */   
/*     */   public SocketHandle(IoSys sys, PSocket socket) {
/*  50 */     super(sys, null, null, 0);
/*  51 */     this.socket = socket;
/*     */   }
/*     */   
/*     */   public PSocket getSocket()
/*     */   {
/*  56 */     return this.socket;
/*     */   }
/*     */   
/*     */   void connect(Connection implementation) {
/*  60 */     this.implementation = implementation;
/*     */   }
/*     */   
/*     */   public void writeByte(int i)
/*     */     throws IOException
/*     */   {
/*  66 */     this.small[0] = ((byte)i);
/*  67 */     writeBytes(this.small, 0, 1);
/*     */   }
/*     */   
/*     */   public int writeBytes(byte[] bytes, int offset, int length)
/*     */     throws IOException
/*     */   {
/*  73 */     this.implementation.writeBytes(bytes, offset, length);
/*  74 */     this.out += length;
/*  75 */     return length;
/*     */   }
/*     */   
/*     */ 
/*     */   public int readBytes(byte[] bytes, int offset, int length)
/*     */     throws IOException
/*     */   {
/*  82 */     int ret = this.implementation.readBytes(bytes, offset, length);
/*  83 */     assert (ret <= length);
/*  84 */     return ret;
/*     */   }
/*     */   
/*     */   public int read(int addr, int length, MainMemory mem) {
/*     */     try {
/*  89 */       int available = this.implementation.available();
/*  90 */       int amount = mem.read(addr, this, Math.min(available, length));
/*  91 */       if (amount > length) {
/*  92 */         System.err.printf("TOO MUCH %d > %d\n", new Object[] { Integer.valueOf(length), Integer.valueOf(amount) });
/*  93 */         System.exit(0);
/*     */       }
/*  95 */       this.in += amount;
/*  96 */       return amount;
/*     */     } catch (IOException e) {
/*  98 */       e.printStackTrace(); }
/*  99 */     return -1;
/*     */   }
/*     */   
/*     */ 
/*     */   public void truncate(long newsize)
/*     */     throws IOException
/*     */   {
/* 106 */     throw new UnsupportedOperationException("No possible");
/*     */   }
/*     */   
/*     */   public int readByte()
/*     */     throws IOException
/*     */   {
/* 112 */     int i = readBytes(this.small, 0, 1);
/* 113 */     return i == 1 ? this.small[0] : -1;
/*     */   }
/*     */   
/*     */   public int flush()
/*     */   {
/*     */     try
/*     */     {
/* 120 */       this.implementation.flush();
/* 121 */       return 0;
/*     */     } catch (IOException e) {
/* 123 */       e.printStackTrace(); }
/* 124 */     return -1;
/*     */   }
/*     */   
/*     */ 
/*     */   public void closeImpl()
/*     */     throws IOException
/*     */   {
/* 131 */     this.implementation.close();
/*     */   }
/*     */   
/*     */   public long seekImpl(long offset, int from)
/*     */     throws IOException
/*     */   {
/* 137 */     throw new UnsupportedOperationException("Not Seekable");
/*     */   }
/*     */   
/*     */   public int getType()
/*     */   {
/* 142 */     return 2;
/*     */   }
/*     */   
/*     */   public long getLength()
/*     */   {
/* 147 */     throw new UnsupportedOperationException("Not a channel operation");
/*     */   }
/*     */   
/*     */   public long getPosition() {
/* 151 */     throw new UnsupportedOperationException("Not a implementation operation");
/*     */   }
/*     */   
/*     */   public int checkEvents(int eventMask)
/*     */   {
/* 156 */     int result = 0;
/*     */     try {
/* 158 */       if (this.implementation.readAvailable()) result |= 0x1;
/* 159 */       if (this.implementation.writeAvailable()) {} return result | 0x4;
/*     */     }
/*     */     catch (IOException e) {}
/*     */     
/* 163 */     return 8;
/*     */   }
/*     */ }


/* Location:              C:\Users\Joey\Downloads\codejar.jar!\com\emt\proteus\runtime32\io\SocketHandle.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */