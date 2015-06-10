/*    */ package com.emt.proteus.demo.main;
/*    */ 
/*    */ import com.emt.proteus.demo.applet.Proteus2Applet;
/*    */ import java.awt.Container;
/*    */ import java.awt.Dimension;
/*    */ import java.io.File;
/*    */ import java.net.URI;
/*    */ import java.net.URL;
/*    */ import javax.swing.JFrame;
/*    */ import javax.swing.JPanel;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class Launch
/*    */ {
/*    */   public static void main(String[] args)
/*    */     throws Exception
/*    */   {
/* 25 */     URL[] urls = new URL[args.length];
/* 26 */     for (int i = 0; i < urls.length; i++) {
/* 27 */       String cp = args[i];
/* 28 */       File file = new File(cp);
/* 29 */       URL url = file.toURI().toURL();
/* 30 */       urls[i] = url;
/*    */     }
/* 32 */     Proteus2Applet applet = new Proteus2Applet(urls);
/* 33 */     JPanel host = new JPanel();
/* 34 */     host.setPreferredSize(new Dimension(300, 600));
/*    */     
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/* 43 */     JFrame frame = createFrame();
/* 44 */     Container contentPane = frame.getContentPane();
/* 45 */     applet.init();
/* 46 */     host.add(applet);
/* 47 */     contentPane.add(host);
/* 48 */     applet.start();
/* 49 */     frame.setVisible(true);
/*    */   }
/*    */   
/*    */ 
/*    */   private static JFrame createFrame()
/*    */   {
/* 55 */     JFrame frame = new JFrame("Demo");
/* 56 */     frame.setResizable(true);
/* 57 */     frame.setDefaultCloseOperation(3);
/* 58 */     frame.setSize(1000, 1000);
/* 59 */     return frame;
/*    */   }
/*    */ }


/* Location:              C:\Users\Joey\Downloads\launch.jar!\com\emt\proteus\demo\main\Launch.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */