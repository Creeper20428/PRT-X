/*    */ package com.emt.proteus.xserver.server;
/*    */ 
/*    */ import com.emt.proteus.xserver.api.Configurable;
/*    */ import com.emt.proteus.xserver.api.Context;
/*    */ import java.awt.HeadlessException;
/*    */ import javax.swing.JFrame;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class Main
/*    */ {
/*    */   public static void main(String[] args)
/*    */   {
/* 37 */     Server server = new Server();
/*    */     
/* 39 */     Context context = new Context();
/* 40 */     Root frame = new Root(null);
/* 41 */     frame.initialize(context);
/*    */     
/*    */     try
/*    */     {
/* 45 */       server.initialize(context);
/* 46 */       server.start();
/* 47 */       server.run();
/*    */     } catch (Throwable t) {
/* 49 */       t.printStackTrace();
/*    */     } finally {
/* 51 */       server.dispose();
/*    */     }
/*    */   }
/*    */   
/*    */   private static class Root extends JFrame implements Configurable
/*    */   {
/*    */     private Root() throws HeadlessException
/*    */     {
/* 59 */       super();
/*    */     }
/*    */     
/*    */     public void initialize(Context context)
/*    */     {
/* 64 */       int width = context.intValue("display.width", 768) + 24;
/* 65 */       int height = context.intValue("display.height", 578) + 50;
/* 66 */       setSize(width, height);
/* 67 */       setVisible(true);
/* 68 */       context.set("CONTAINER", getContentPane());
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\Joey\Downloads\launch.jar!\com\emt\proteus\xserver\server\Main.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */