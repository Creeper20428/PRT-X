/*    */ package com.emt.proteus.demo.common;
/*    */ 
/*    */ import com.emt.proteus.xserver.api.Context;
/*    */ import com.emt.proteus.xserver.display.XDisplay;
/*    */ import java.awt.Container;
/*    */ import java.awt.Dimension;
/*    */ import java.awt.event.ComponentAdapter;
/*    */ import java.awt.event.ComponentEvent;
/*    */ import javax.swing.JComponent;
/*    */ import javax.swing.JPanel;
/*    */ 
/*    */ public class XPanel extends JPanel
/*    */ {
/*    */   private XDisplay display;
/*    */   private JPanel child;
/*    */   
/*    */   public XPanel(int width, int height)
/*    */   {
/* 19 */     setLayout(new java.awt.BorderLayout());
/*    */     
/* 21 */     this.child = new JPanel();
/*    */     
/* 23 */     Dimension dimension = new Dimension(width, height);
/* 24 */     this.child.setPreferredSize(dimension);
/* 25 */     this.child.setMaximumSize(dimension);
/* 26 */     add(this.child);
/*    */     
/* 28 */     ComponentAdapter adapter = new ComponentAdapter()
/*    */     {
/*    */       public void componentResized(ComponentEvent e) {
/* 31 */         doInit(e);
/*    */       }
/*    */       
/*    */ 
/*    */       public void componentMoved(ComponentEvent e)
/*    */       {
/* 37 */         doInit(e);
/*    */       }
/*    */       
/*    */       public void componentHidden(ComponentEvent e)
/*    */       {
/* 42 */         doInit(e);
/*    */       }
/*    */       
/*    */       public void componentShown(ComponentEvent e)
/*    */       {
/* 47 */         doInit(e);
/*    */       }
/*    */       
/*    */       private void doInit(ComponentEvent e) {
/* 51 */         XPanel.this.initialise();
/* 52 */         XPanel.this.removeComponentListener(this);
/*    */       }
/*    */       
/* 55 */     };
/* 56 */     addComponentListener(adapter);
/*    */   }
/*    */   
/*    */ 
/*    */   public void initialise()
/*    */   {
/* 62 */     Context context = new Context();
/* 63 */     Container parent = this;
/* 64 */     Container eventRoot = parent;
/* 65 */     while ((parent instanceof JComponent)) {
/* 66 */       eventRoot = parent;
/* 67 */       parent = parent.getParent();
/*    */     }
/* 69 */     context.set("EVENT_ROOT", this);
/* 70 */     context.set("CONTAINER", this.child);
/* 71 */     this.display = new XDisplay();
/* 72 */     this.display.initialize(context);
/*    */   }
/*    */   
/*    */   public void start() {}
/*    */   
/*    */   public void stop() {}
/*    */ }


/* Location:              C:\Users\Joey\Downloads\launch.jar!\com\emt\proteus\demo\common\XPanel.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */