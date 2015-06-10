/*    */ package com.emt.proteus.runtime32.io;
/*    */ 
/*    */ import com.emt.proteus.runtime32.api.FileProxy;
/*    */ import java.io.IOException;
/*    */ import java.util.HashSet;
/*    */ import java.util.Set;
/*    */ 
/*    */ 
/*    */ 
/*    */ public class BlockedProvider
/*    */   implements Provider
/*    */ {
/* 13 */   private Set<String> blocked = new HashSet();
/*    */   
/*    */   public BlockedProvider(String... paths)
/*    */   {
/* 17 */     for (int i = 0; i < paths.length; i++) {
/* 18 */       String path = paths[i];
/* 19 */       this.blocked.add(path);
/*    */     }
/*    */   }
/*    */   
/*    */   public boolean provides(String fileName)
/*    */   {
/* 25 */     return this.blocked.contains(fileName);
/*    */   }
/*    */   
/*    */   public FileProxy create(String fileName, boolean create) throws IOException
/*    */   {
/* 30 */     return null;
/*    */   }
/*    */ }


/* Location:              C:\Users\Joey\Downloads\codejar.jar!\com\emt\proteus\runtime32\io\BlockedProvider.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */