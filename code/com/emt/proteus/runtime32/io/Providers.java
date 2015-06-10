/*    */ package com.emt.proteus.runtime32.io;
/*    */ 
/*    */ import com.emt.proteus.runtime32.api.FileProxy;
/*    */ import java.io.IOException;
/*    */ import java.util.ArrayList;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class Providers
/*    */ {
/* 14 */   private ArrayList<Provider> providers = new ArrayList();
/*    */   private Provider defaultProvider;
/*    */   
/*    */   public Providers() {
/* 18 */     this(new NoFiles());
/*    */   }
/*    */   
/*    */   public Providers(Provider defaultProvider) {
/* 22 */     this.defaultProvider = defaultProvider;
/*    */   }
/*    */   
/*    */   public Providers add(Provider provider) {
/* 26 */     this.providers.add(provider);
/* 27 */     return this;
/*    */   }
/*    */   
/*    */   public FileProxy getFile(String path, boolean create) throws IOException {
/* 31 */     int size = this.providers.size();
/* 32 */     for (int i = 0; i < size; i++) {
/* 33 */       Provider provider = (Provider)this.providers.get(i);
/* 34 */       if (provider.provides(path)) {
/* 35 */         return provider.create(path, create);
/*    */       }
/*    */     }
/* 38 */     return this.defaultProvider.create(path, create);
/*    */   }
/*    */   
/*    */   public void setDefaultProvider(Provider provider) {
/* 42 */     this.defaultProvider = provider;
/*    */   }
/*    */   
/*    */   public static class NoFiles implements Provider
/*    */   {
/*    */     public boolean provides(String fileName) {
/* 48 */       return true;
/*    */     }
/*    */     
/*    */     public FileProxy create(String fileName, boolean create) throws IOException
/*    */     {
/* 53 */       return null;
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\Joey\Downloads\codejar.jar!\com\emt\proteus\runtime32\io\Providers.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */