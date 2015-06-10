/*    */ package com.emt.proteus.elf;
/*    */ 
/*    */ import java.io.RandomAccessFile;
/*    */ 
/*    */ public class SeekableFile implements SeekableDataSource
/*    */ {
/*    */   private RandomAccessFile raf;
/*    */   
/*    */   public SeekableFile(String s) throws java.io.IOException
/*    */   {
/* 11 */     this(new java.io.File(s));
/*    */   }
/*    */   
/*    */   public SeekableFile(java.io.File f) throws java.io.IOException
/*    */   {
/* 16 */     this(new RandomAccessFile(f, "r"));
/*    */   }
/*    */   
/*    */   public SeekableFile(RandomAccessFile raf)
/*    */   {
/* 21 */     this.raf = raf;
/*    */   }
/*    */   
/*    */   public void close()
/*    */   {
/*    */     try
/*    */     {
/* 28 */       this.raf.close();
/*    */     }
/*    */     catch (Exception e) {}
/*    */   }
/*    */   
/*    */   public void seek(long pos) throws java.io.IOException
/*    */   {
/* 35 */     this.raf.seek(pos);
/*    */   }
/*    */   
/*    */   public long position() throws java.io.IOException
/*    */   {
/* 40 */     return this.raf.getFilePointer();
/*    */   }
/*    */   
/*    */   public void readFully(byte[] destination) throws java.io.IOException
/*    */   {
/* 45 */     readFully(destination, 0, destination.length);
/*    */   }
/*    */   
/*    */   public void readFully(byte[] destination, int offset, int length) throws java.io.IOException
/*    */   {
/* 50 */     this.raf.readFully(destination, offset, length);
/*    */   }
/*    */   
/*    */   public static Elf readElf(String s) throws Exception
/*    */   {
/* 55 */     return readElf(new java.io.File(s));
/*    */   }
/*    */   
/*    */   public static Elf readElf(java.io.File f) throws Exception
/*    */   {
/* 60 */     SeekableDataSource src = new SeekableFile(f);
/*    */     try
/*    */     {
/* 63 */       return new Elf(src);
/*    */     }
/*    */     finally
/*    */     {
/*    */       try
/*    */       {
/* 69 */         src.close();
/*    */       }
/*    */       catch (Exception e) {}
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\Joey\Downloads\codejar.jar!\com\emt\proteus\elf\SeekableFile.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */