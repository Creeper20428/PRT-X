/*    */ package com.emt.proteus.demo.common;
/*    */ 
/*    */ import java.awt.BorderLayout;
/*    */ import java.awt.event.ActionEvent;
/*    */ import java.awt.event.ActionListener;
/*    */ import javax.swing.BorderFactory;
/*    */ import javax.swing.JButton;
/*    */ import javax.swing.JPanel;
/*    */ import javax.swing.JScrollPane;
/*    */ import javax.swing.JSplitPane;
/*    */ 
/*    */ public class ControlPanel extends JPanel implements ActionListener
/*    */ {
/*    */   private XPanel xPanel;
/*    */   private JConsolePanel consolePanel;
/*    */   private JButton run;
/*    */   private JButton reset;
/*    */   private final ProteusHandle handle;
/*    */   
/*    */   public ControlPanel(ProteusHandle handle, int width, int height)
/*    */   {
/* 22 */     super(new BorderLayout());
/* 23 */     setBorder(BorderFactory.createBevelBorder(0));
/* 24 */     this.handle = handle;
/* 25 */     this.run = new JButton("Run");
/* 26 */     this.reset = new JButton("Reset");
/* 27 */     JPanel buttons = new JPanel();
/* 28 */     buttons.setAlignmentX(1.0F);
/* 29 */     buttons.add(this.reset);
/* 30 */     buttons.add(this.run);
/*    */     
/* 32 */     this.run.addActionListener(this);
/* 33 */     this.reset.addActionListener(this);
/* 34 */     this.xPanel = new XPanel(width, height);
/* 35 */     this.consolePanel = new JConsolePanel("Emulation Log");
/* 36 */     JScrollPane scroller = new JScrollPane(this.xPanel);
/* 37 */     JSplitPane split = new JSplitPane(0, scroller, this.consolePanel);
/* 38 */     split.setOneTouchExpandable(true);
/* 39 */     split.setDividerSize(10);
/* 40 */     split.setResizeWeight(0.7D);
/* 41 */     add(split);
/*    */   }
/*    */   
/*    */   public XPanel getXPanel()
/*    */   {
/* 46 */     return this.xPanel;
/*    */   }
/*    */   
/*    */   public JConsolePanel getConsolePanel() {
/* 50 */     return this.consolePanel;
/*    */   }
/*    */   
/*    */   public void actionPerformed(ActionEvent e)
/*    */   {
/* 55 */     if (e.getSource() == this.run) {
/* 56 */       this.handle.run();
/* 57 */     } else if (e.getSource() == this.reset) {
/* 58 */       this.handle.reset();
/*    */     }
/*    */   }
/*    */   
/*    */   public void start() {
/* 63 */     this.xPanel.start();
/*    */   }
/*    */ }


/* Location:              C:\Users\Joey\Downloads\launch.jar!\com\emt\proteus\demo\common\ControlPanel.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */