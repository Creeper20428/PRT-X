/*    */ package com.emt.proteus.xserver.display.input;
/*    */ 
/*    */ import java.io.Serializable;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class Keymodifier
/*    */   implements Serializable
/*    */ {
/* 25 */   public static Keymodifier kmod = null;
/*    */   int width;
/*    */   byte[] keys;
/*    */   
/*    */   public Keymodifier() {}
/*    */   
/* 31 */   public Keymodifier(int width) { this.width = width;
/* 32 */     this.keys = new byte[8 * width];
/*    */   }
/*    */ }


/* Location:              C:\Users\Joey\Downloads\launch.jar!\com\emt\proteus\xserver\display\input\Keymodifier.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */