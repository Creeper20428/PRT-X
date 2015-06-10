/*     */ package com.emt.proteus.runtime32.io;
/*     */ 
/*     */ import com.emt.proteus.runtime32.api.FileProxy;
/*     */ import java.io.IOException;
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
/*     */ public class ProxyIoHandle
/*     */   extends IoHandle
/*     */ {
/*     */   private long position;
/*     */   private FileProxy proxy;
/*  38 */   private byte[] small = new byte[3];
/*     */   
/*     */   public ProxyIoHandle(IoSys sys, FileProxy proxy, String mode, int desc) throws IOException
/*     */   {
/*  42 */     super(sys, proxy.getPath(), mode, desc);
/*  43 */     this.proxy = proxy;
/*  44 */     this.position = 0L;
/*     */   }
/*     */   
/*     */   private void update_position(long new_position) {
/*  48 */     this.position = new_position;
/*     */   }
/*     */   
/*     */ 
/*     */   public void writeByte(int i)
/*     */     throws IOException
/*     */   {
/*  55 */     this.small[0] = ((byte)i);
/*  56 */     writeBytes(this.small, 0, 1);
/*     */   }
/*     */   
/*     */   public int writeBytes(byte[] bytes, int offset, int length)
/*     */     throws IOException
/*     */   {
/*  62 */     long new_position = this.position + length;
/*  63 */     this.proxy.write(this.position, bytes, offset, length);
/*  64 */     update_position(new_position);
/*  65 */     return length;
/*     */   }
/*     */   
/*     */   public int readBytes(byte[] bytes, int offset, int length)
/*     */     throws IOException
/*     */   {
/*  71 */     int amount = this.proxy.load(this.position, length, bytes, offset);
/*  72 */     if (amount > 0) {
/*  73 */       update_position(this.position + amount);
/*     */     }
/*  75 */     return amount;
/*     */   }
/*     */   
/*     */   public void truncate(long newsize)
/*     */     throws IOException
/*     */   {
/*  81 */     this.position = this.proxy.truncate(newsize);
/*     */   }
/*     */   
/*     */   public int readByte()
/*     */     throws IOException
/*     */   {
/*  87 */     int i = readBytes(this.small, 0, 1);
/*  88 */     return i == 1 ? this.small[0] : -1;
/*     */   }
/*     */   
/*     */ 
/*     */   public int flush()
/*     */   {
/*  94 */     return 0;
/*     */   }
/*     */   
/*     */ 
/*     */   public void closeImpl()
/*     */     throws IOException
/*     */   {}
/*     */   
/*     */ 
/*     */   public long seekImpl(long offset, int from)
/*     */     throws IOException
/*     */   {
/* 106 */     long dest = 0L;
/* 107 */     switch (from) {
/*     */     case 1: 
/* 109 */       dest = this.position + offset;
/* 110 */       break;
/*     */     case 2: 
/* 112 */       dest = this.proxy.getLength() + offset;
/* 113 */       break;
/*     */     case 0: 
/* 115 */       dest = offset;
/*     */     }
/*     */     
/* 118 */     update_position((int)dest);
/* 119 */     return dest;
/*     */   }
/*     */   
/*     */   public int getType()
/*     */   {
/* 124 */     return 1;
/*     */   }
/*     */   
/*     */   public long getLength()
/*     */   {
/* 129 */     return this.proxy.getLength();
/*     */   }
/*     */   
/*     */   public long getPosition() {
/* 133 */     return this.position;
/*     */   }
/*     */ }


/* Location:              C:\Users\Joey\Downloads\codejar.jar!\com\emt\proteus\runtime32\io\ProxyIoHandle.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */