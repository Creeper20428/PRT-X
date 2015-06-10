/*    */ package com.emt.proteus.runtime32.rtld;
/*    */ 
/*    */ 
/*    */ public class SearchPath
/*    */ {
/*    */   private SearchPath next;
/*    */   private String path;
/*    */   
/*    */   public SearchPath(SearchPath next, String path)
/*    */   {
/* 11 */     this.next = next;
/* 12 */     this.path = path;
/*    */   }
/*    */   
/*    */   public SearchPath getNext() {
/* 16 */     return this.next;
/*    */   }
/*    */   
/*    */   public String getPath() {
/* 20 */     return this.path;
/*    */   }
/*    */   
/* 23 */   public String resolve(String filePath) { return this.path + "/" + filePath; }
/*    */   
/*    */   public SearchPath append(String[] paths)
/*    */   {
/* 27 */     return appendImpl(this, paths);
/*    */   }
/*    */   
/*    */   public static SearchPath compile(String[] paths) {
/* 31 */     return appendImpl(new SearchPath(null, "."), paths);
/*    */   }
/*    */   
/*    */   public static SearchPath appendImpl(SearchPath root, String[] paths) {
/* 35 */     SearchPath current = root;
/* 36 */     for (int i = paths.length - 1; i >= 0; i--) {
/* 37 */       current = new SearchPath(current, paths[i]);
/*    */     }
/* 39 */     return current;
/*    */   }
/*    */ }


/* Location:              C:\Users\Joey\Downloads\codejar.jar!\com\emt\proteus\runtime32\rtld\SearchPath.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */