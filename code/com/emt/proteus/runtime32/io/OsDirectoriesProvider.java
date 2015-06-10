/*    */ package com.emt.proteus.runtime32.io;
/*    */ 
/*    */ import com.emt.proteus.runtime32.api.FileProxy;
/*    */ import com.emt.proteus.utils.Utils;
/*    */ import java.io.IOException;
/*    */ import java.io.InputStream;
/*    */ import java.util.Set;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class OsDirectoriesProvider
/*    */   implements Provider
/*    */ {
/* 15 */   public static final Set<String> FILES = Utils.toSet(new Object[] { "/selinux" });
/*    */   
/*    */ 
/*    */ 
/*    */   public boolean provides(String fileName)
/*    */   {
/* 21 */     return FILES.contains(fileName);
/*    */   }
/*    */   
/*    */   public FileProxy create(String fileName, boolean create)
/*    */     throws IOException
/*    */   {
/* 27 */     return new Directory(fileName, null);
/*    */   }
/*    */   
/*    */   private static class Directory extends FileProxy {
/*    */     private Directory(String path) {
/* 32 */       super();
/*    */     }
/*    */     
/*    */     protected int loadImpl(long offset, byte[] dest, int dest_off, int length) throws IOException
/*    */     {
/* 37 */       throw new IOException("Cannot read dir");
/*    */     }
/*    */     
/*    */     public long getLength()
/*    */     {
/* 42 */       return 1L;
/*    */     }
/*    */     
/*    */     public InputStream getInputStream() throws IOException
/*    */     {
/* 47 */       throw new IOException("Cannot read dir");
/*    */     }
/*    */     
/*    */     public void write(long position, byte[] data, int start, int length) throws IOException
/*    */     {
/* 52 */       throw new IOException("Cannot write dir");
/*    */     }
/*    */     
/*    */     public long truncate(long newsize) throws IOException
/*    */     {
/* 57 */       throw new IOException("Cannot truncate dir");
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\Joey\Downloads\codejar.jar!\com\emt\proteus\runtime32\io\OsDirectoriesProvider.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */