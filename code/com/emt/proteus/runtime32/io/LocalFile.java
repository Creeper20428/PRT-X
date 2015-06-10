/*    */ package com.emt.proteus.runtime32.io;
/*    */ 
/*    */ import com.emt.proteus.runtime32.api.FileProxy;
/*    */ import java.io.FileInputStream;
/*    */ import java.io.IOException;
/*    */ import java.io.InputStream;
/*    */ import java.io.RandomAccessFile;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public final class LocalFile
/*    */   extends FileProxy
/*    */ {
/*    */   private String path;
/*    */   private long length;
/*    */   private RandomAccessFile file;
/*    */   public static final boolean LOG_ACCESS = false;
/*    */   
/*    */   public LocalFile(String relativeName, RandomAccessFile url)
/*    */     throws IOException
/*    */   {
/* 47 */     super(relativeName);
/* 48 */     this.file = url;
/* 49 */     this.path = relativeName;
/* 50 */     this.length = this.file.length();
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */   protected int loadImpl(long offset, byte[] dest, int dest_off, int len)
/*    */     throws IOException
/*    */   {
/* 58 */     this.file.seek(offset);
/* 59 */     this.file.readFully(dest, dest_off, len);
/* 60 */     return len;
/*    */   }
/*    */   
/* 63 */   public long getLength() { return this.length; }
/*    */   
/*    */ 
/*    */   public boolean exists()
/*    */   {
/* 68 */     return true;
/*    */   }
/*    */   
/*    */   public InputStream getInputStream() throws IOException {
/* 72 */     return new FileInputStream(this.file.getFD());
/*    */   }
/*    */   
/*    */   public long truncate(long newsize) throws IOException
/*    */   {
/* 77 */     this.file.setLength(newsize);
/* 78 */     return newsize;
/*    */   }
/*    */   
/*    */   public void write(long position, byte[] data, int start, int length)
/*    */     throws IOException
/*    */   {
/* 84 */     this.file.seek(position);
/* 85 */     this.file.write(data, start, length);
/* 86 */     this.length = this.file.length();
/*    */   }
/*    */ }


/* Location:              C:\Users\Joey\Downloads\codejar.jar!\com\emt\proteus\runtime32\io\LocalFile.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */