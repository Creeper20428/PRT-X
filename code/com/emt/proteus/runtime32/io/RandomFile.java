/*     */ package com.emt.proteus.runtime32.io;
/*     */ 
/*     */ import com.emt.proteus.runtime32.api.FileProxy;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.util.Random;
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
/*     */ public final class RandomFile
/*     */   extends FileProxy
/*     */ {
/*     */   private String path;
/*     */   private long length;
/*     */   public static final boolean LOG_ACCESS = false;
/*     */   public final InputStream input;
/*     */   
/*     */   public RandomFile(String relativeName)
/*     */     throws IOException
/*     */   {
/*  46 */     super(relativeName);
/*  47 */     this.path = relativeName;
/*  48 */     this.length = 8L;
/*  49 */     this.input = new RandomInputStream();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected int loadImpl(long offset, byte[] dest, int dest_off, int len)
/*     */     throws IOException
/*     */   {
/*  58 */     if (len > this.length)
/*  59 */       len = (int)this.length;
/*  60 */     for (int i = 0; i < len; i++)
/*  61 */       dest[(dest_off + i)] = ((byte)this.input.read());
/*  62 */     return len;
/*     */   }
/*     */   
/*  65 */   public long getLength() { return this.length; }
/*     */   
/*     */ 
/*     */   public boolean exists()
/*     */   {
/*  70 */     return true;
/*     */   }
/*     */   
/*     */   public InputStream getInputStream() throws IOException {
/*  74 */     return this.input;
/*     */   }
/*     */   
/*     */   public long truncate(long newsize) throws IOException
/*     */   {
/*  79 */     return newsize;
/*     */   }
/*     */   
/*     */   public void write(long position, byte[] data, int start, int length)
/*     */     throws IOException
/*     */   {}
/*     */   
/*     */   public class RandomInputStream
/*     */     extends InputStream
/*     */   {
/*  89 */     private transient Random generator = new Random();
/*     */     
/*     */     public RandomInputStream() {}
/*     */     
/*  93 */     public int read() { int result = this.generator.nextInt() % 256;
/*  94 */       if (result < 0) result = -result;
/*  95 */       return result;
/*     */     }
/*     */     
/*     */ 
/*     */     public int read(byte[] data, int offset, int length)
/*     */       throws IOException
/*     */     {
/* 102 */       byte[] temp = new byte[length];
/* 103 */       this.generator.nextBytes(temp);
/* 104 */       System.arraycopy(temp, 0, data, offset, length);
/* 105 */       return length;
/*     */     }
/*     */     
/*     */     public int read(byte[] data)
/*     */       throws IOException
/*     */     {
/* 111 */       this.generator.nextBytes(data);
/* 112 */       return data.length;
/*     */     }
/*     */     
/*     */ 
/*     */     public long skip(long bytesToSkip)
/*     */       throws IOException
/*     */     {
/* 119 */       return bytesToSkip;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Joey\Downloads\codejar.jar!\com\emt\proteus\runtime32\io\RandomFile.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */