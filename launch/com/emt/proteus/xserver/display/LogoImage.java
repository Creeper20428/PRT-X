/*    */ package com.emt.proteus.xserver.display;
/*    */ 
/*    */ import java.awt.Image;
/*    */ import java.awt.Toolkit;
/*    */ import java.awt.image.ImageProducer;
/*    */ import java.net.URL;
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
/*    */ 
/*    */ 
/*    */ public final class LogoImage
/*    */ {
/*    */   static Screen screen;
/* 30 */   static String name = "emt.jpg";
/* 31 */   static Image logoimage = null;
/*    */   
/*    */   static int logoimagex;
/*    */   static int logoimagey;
/*    */   static int logoimagewidth;
/*    */   static int logoimageheight;
/*    */   
/* 38 */   public static void init(Screen[] scrn) { screen = scrn[0]; }
/*    */   
/*    */   public static void storeImage(Image img) {
/* 41 */     logoimage = img;
/* 42 */     logoimagewidth = 90;
/* 43 */     logoimageheight = 30;
/*    */   }
/*    */   
/* 46 */   public static boolean loadLogo(String _name) { if (_name == null) return false;
/* 47 */     if ((logoimage == null) || (!name.equals(_name))) {
/* 48 */       name = _name;
/* 49 */       if (logoimage != null) logoimage.flush();
/*    */       try {
/* 51 */         URL url = LogoImage.class.getResource(name);
/* 52 */         Image img = Toolkit.getDefaultToolkit().createImage((ImageProducer)url.getContent());
/*    */         
/* 54 */         storeImage(img);
/*    */       }
/*    */       catch (Exception e) {}
/*    */     }
/*    */     
/*    */ 
/* 60 */     return logoimage != null;
/*    */   }
/*    */   
/* 63 */   public static boolean loadLogo() { return loadLogo(name); }
/*    */   
/*    */   public static void toggle() {
/* 66 */     if (logoimage == null) up(); else
/* 67 */       down();
/*    */   }
/*    */   
/* 70 */   public static void up() { if (logoimage == null)
/* 71 */       loadLogo();
/*    */   }
/*    */   
/*    */   public static void down() {
/* 75 */     if (logoimage != null) {
/* 76 */       Image img = logoimage;
/* 77 */       logoimage = null;
/* 78 */       img.flush();
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\Joey\Downloads\launch.jar!\com\emt\proteus\xserver\display\LogoImage.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */