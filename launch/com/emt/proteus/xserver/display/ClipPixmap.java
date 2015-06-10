/*    */ package com.emt.proteus.xserver.display;
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
/*    */ final class ClipPixmap
/*    */   implements Clip
/*    */ {
/*    */   Pixmap pixmap;
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   ClipPixmap(Pixmap p)
/*    */   {
/* 27 */     this.pixmap = p;
/*    */   }
/*    */   
/* 30 */   public Object getMask() { return this.pixmap; }
/*    */ }


/* Location:              C:\Users\Joey\Downloads\launch.jar!\com\emt\proteus\xserver\display\ClipPixmap.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */