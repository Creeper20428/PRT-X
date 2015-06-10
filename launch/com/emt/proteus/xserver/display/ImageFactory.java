/*    */ package com.emt.proteus.xserver.display;
/*    */ 
/*    */ import java.awt.Image;
/*    */ 
/*    */ class ImageFactory
/*    */ {
/*    */   static XComponent ddxwindow;
/*    */   
/*    */   static void init(Screen screen)
/*    */   {
/* 11 */     ddxwindow = screen.root.ddxwindow;
/*    */   }
/*    */   
/* 14 */   static Image createImage(int width, int height) { return ddxwindow.createImage(width, height); }
/*    */ }


/* Location:              C:\Users\Joey\Downloads\launch.jar!\com\emt\proteus\xserver\display\ImageFactory.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */