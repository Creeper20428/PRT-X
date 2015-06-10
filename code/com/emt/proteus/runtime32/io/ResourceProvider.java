/*    */ package com.emt.proteus.runtime32.io;
/*    */ 
/*    */ import com.emt.proteus.runtime32.api.FileProxy;
/*    */ import com.emt.proteus.runtime32.api.FileProxy.Memory;
/*    */ import java.io.IOException;
/*    */ import java.net.URL;
/*    */ 
/*    */ public class ResourceProvider
/*    */   implements Provider
/*    */ {
/*    */   private ClassLoader loader;
/*    */   
/*    */   public ResourceProvider()
/*    */   {
/* 15 */     this(null);
/*    */   }
/*    */   
/*    */   public ResourceProvider(ClassLoader loader) {
/* 19 */     this.loader = loader;
/*    */   }
/*    */   
/*    */   public boolean provides(String fileName)
/*    */   {
/* 24 */     URL url = getURL(fileName);
/* 25 */     return url != null;
/*    */   }
/*    */   
/*    */   private URL getURL(String fileName)
/*    */   {
/* 30 */     String name = "/resources/fsroot" + fileName;
/*    */     URL resource;
/* 32 */     URL resource; if (this.loader != null) {
/* 33 */       resource = this.loader.getResource(name);
/*    */     } else {
/* 35 */       resource = ResourceProvider.class.getResource(name);
/*    */     }
/* 37 */     return resource;
/*    */   }
/*    */   
/*    */   public FileProxy create(String fileName, boolean create) throws IOException
/*    */   {
/* 42 */     URL url = getURL(fileName);
/* 43 */     if (url != null) {
/* 44 */       return new FileProxy.Memory(fileName, url);
/*    */     }
/* 46 */     return null;
/*    */   }
/*    */ }


/* Location:              C:\Users\Joey\Downloads\codejar.jar!\com\emt\proteus\runtime32\io\ResourceProvider.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */