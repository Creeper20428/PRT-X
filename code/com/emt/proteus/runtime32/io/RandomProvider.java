/*    */ package com.emt.proteus.runtime32.io;
/*    */ 
/*    */ import com.emt.proteus.runtime32.api.FileProxy;
/*    */ import java.io.IOException;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class RandomProvider
/*    */   implements Provider
/*    */ {
/* 12 */   private static final String[] FILES = { "/dev/urandom" };
/*    */   
/*    */   public boolean provides(String fileName)
/*    */   {
/* 16 */     return fileName.equals("/dev/urandom");
/*    */   }
/*    */   
/*    */   public FileProxy create(String fileName, boolean create) throws IOException
/*    */   {
/* 21 */     return new RandomFile(fileName);
/*    */   }
/*    */ }


/* Location:              C:\Users\Joey\Downloads\codejar.jar!\com\emt\proteus\runtime32\io\RandomProvider.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */