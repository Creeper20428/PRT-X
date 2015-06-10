/*     */ package com.emt.proteus.runtime32.api;
/*     */ 
/*     */ import java.io.ByteArrayInputStream;
/*     */ import java.io.ByteArrayOutputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.net.URL;
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
/*     */ public abstract class FileProxy
/*     */ {
/*  41 */   private static long uniqueId = ;
/*     */   private String path;
/*     */   
/*     */   protected FileProxy(String path)
/*     */   {
/*  46 */     this.path = path;
/*     */   }
/*     */   
/*     */   public static byte[] getBytes(InputStream inputStream) throws IOException
/*     */   {
/*  51 */     ByteArrayOutputStream bais = new ByteArrayOutputStream();
/*  52 */     byte[] buf = new byte[81920];
/*  53 */     int amount = 0;
/*     */     try {
/*  55 */       while ((amount = inputStream.read(buf, 0, buf.length)) >= 0) {
/*  56 */         bais.write(buf, 0, amount);
/*     */       }
/*  58 */       return bais.toByteArray();
/*     */     } finally {
/*  60 */       inputStream.close();
/*     */     }
/*     */   }
/*     */   
/*     */   public static byte[] getBytes(URL url) throws IOException
/*     */   {
/*  66 */     return getBytes(url.openStream());
/*     */   }
/*     */   
/*     */   public final int load(long offset, int len, byte[] dest, int dest_off) throws IOException
/*     */   {
/*  71 */     long theLength = getLength();
/*  72 */     long available_length = theLength - offset;
/*  73 */     int length = (int)Math.min(len, available_length);
/*  74 */     if (length > 0) {
/*  75 */       return loadImpl(offset, dest, dest_off, length);
/*     */     }
/*  77 */     return -1;
/*     */   }
/*     */   
/*     */   protected abstract int loadImpl(long paramLong, byte[] paramArrayOfByte, int paramInt1, int paramInt2) throws IOException;
/*     */   
/*     */   public abstract long getLength();
/*     */   
/*     */   public abstract InputStream getInputStream() throws IOException;
/*     */   
/*     */   public boolean exists()
/*     */   {
/*  88 */     return getLength() > 0L;
/*     */   }
/*     */   
/*     */   public static int readFully(InputStream input, byte[] dest, int dest_offset, int length) throws IOException
/*     */   {
/*  93 */     int read = 0;
/*  94 */     while (length > 0) {
/*  95 */       int amount = input.read(dest, dest_offset, length);
/*  96 */       if (amount < 0) { return read;
/*     */       }
/*  98 */       read += amount;
/*  99 */       length -= amount;
/* 100 */       dest_offset += amount;
/*     */     }
/* 102 */     return read;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public abstract void write(long paramLong, byte[] paramArrayOfByte, int paramInt1, int paramInt2)
/*     */     throws IOException;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public final String getPath()
/*     */   {
/* 117 */     return this.path;
/*     */   }
/*     */   
/*     */   public abstract long truncate(long paramLong)
/*     */     throws IOException;
/*     */   
/*     */   public static class Memory
/*     */     extends FileProxy
/*     */   {
/*     */     public static final int GROWBY = 4096;
/*     */     private byte[] data;
/*     */     private int length;
/*     */     
/*     */     public Memory(String path, URL contents)
/*     */       throws IOException
/*     */     {
/* 133 */       this(path, getBytes(contents));
/*     */     }
/*     */     
/* 136 */     public Memory(String path, InputStream contents) throws IOException { this(path, getBytes(contents)); }
/*     */     
/*     */     public Memory(String path, String src) {
/* 139 */       super();
/* 140 */       this.data = new byte[src.length()];
/* 141 */       for (int i = 0; i < this.data.length; i++) {
/* 142 */         this.data[i] = ((byte)src.charAt(i));
/*     */       }
/* 144 */       this.length = this.data.length;
/*     */     }
/*     */     
/* 147 */     public Memory(String path, byte[] data) { this(path, data, data.length); }
/*     */     
/*     */     public Memory(String path, byte[] data, int length) {
/* 150 */       super();
/* 151 */       this.data = data;
/* 152 */       this.length = length;
/*     */     }
/*     */     
/*     */     protected int loadImpl(long offset, byte[] dest, int dest_off, int length) throws IOException
/*     */     {
/* 157 */       int available = (int)Math.min(getLength() - offset, length);
/* 158 */       System.arraycopy(this.data, (int)offset, dest, dest_off, available);
/* 159 */       return available;
/*     */     }
/*     */     
/*     */     public long getLength()
/*     */     {
/* 164 */       return this.length;
/*     */     }
/*     */     
/*     */     public InputStream getInputStream() throws IOException
/*     */     {
/* 169 */       return new ByteArrayInputStream(this.data, 0, this.length);
/*     */     }
/*     */     
/*     */     public void write(long position, byte[] data, int start, int length) throws IOException
/*     */     {
/* 174 */       int index = (int)position;
/* 175 */       int maxlength = index + length;
/* 176 */       if (maxlength > this.length) {
/* 177 */         byte[] tmp = new byte[Math.max(this.length + 4096, maxlength)];
/* 178 */         System.arraycopy(this.data, 0, tmp, 0, this.data.length);
/* 179 */         this.data = tmp;
/* 180 */         this.length = maxlength;
/*     */       }
/* 182 */       System.arraycopy(data, index, data, index, length);
/*     */     }
/*     */     
/*     */     public long truncate(long newsize) throws IOException {
/* 186 */       this.length = ((int)newsize);
/* 187 */       return newsize;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Joey\Downloads\codejar.jar!\com\emt\proteus\runtime32\api\FileProxy.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */