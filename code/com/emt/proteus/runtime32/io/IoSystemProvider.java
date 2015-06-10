/*    */ package com.emt.proteus.runtime32.io;
/*    */ 
/*    */ import com.emt.proteus.runtime32.api.FileProxy;
/*    */ import com.emt.proteus.runtime32.api.IoSystem;
/*    */ import java.io.IOException;
/*    */ 
/*    */ 
/*    */ public class IoSystemProvider
/*    */   implements Provider
/*    */ {
/*    */   private IoSystem ioSystem;
/*    */   
/*    */   public IoSystemProvider(IoSystem ioSystem)
/*    */   {
/* 15 */     this.ioSystem = ioSystem;
/*    */   }
/*    */   
/*    */   public boolean provides(String fileName)
/*    */   {
/* 20 */     return true;
/*    */   }
/*    */   
/*    */   public FileProxy create(String fileName, boolean create) throws IOException
/*    */   {
/* 25 */     return this.ioSystem.getFile(fileName, create);
/*    */   }
/*    */ }


/* Location:              C:\Users\Joey\Downloads\codejar.jar!\com\emt\proteus\runtime32\io\IoSystemProvider.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */