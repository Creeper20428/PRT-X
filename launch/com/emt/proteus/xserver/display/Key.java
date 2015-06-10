/*    */ package com.emt.proteus.xserver.display;
/*    */ 
/*    */ 
/*    */ class Key
/*    */   extends Resource
/*    */ {
/*  7 */   int clss = 0;
/*    */   
/*  9 */   public int hashCode() { return this.id; }
/*    */   
/*    */   public boolean equals(Object o)
/*    */   {
/* 13 */     if (o == null) return false;
/* 14 */     return this.id == o.hashCode();
/*    */   }
/*    */ }


/* Location:              C:\Users\Joey\Downloads\launch.jar!\com\emt\proteus\xserver\display\Key.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */