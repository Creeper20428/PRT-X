/*    */ package com.emt.proteus.demo.common;
/*    */ 
/*    */ import java.awt.Color;
/*    */ import javax.swing.JPanel;
/*    */ import javax.swing.JTextArea;
/*    */ 
/*    */ public class JConsolePanel extends JPanel
/*    */ {
/*    */   private JTextArea text;
/*    */   private javax.swing.JScrollPane scrollPane;
/*    */   private Color textFace;
/*    */   private Color background;
/*    */   private Color inactive;
/*    */   private volatile String lastText;
/*    */   
/*    */   public JConsolePanel(String title)
/*    */   {
/* 18 */     this(title, Color.white, Color.black, Color.gray);
/*    */   }
/*    */   
/*    */   public JConsolePanel(String title, Color textFace, Color background, Color inactive)
/*    */   {
/* 23 */     super(new java.awt.BorderLayout());
/*    */     
/* 25 */     if (title != null)
/* 26 */       setBorder(javax.swing.BorderFactory.createTitledBorder(title));
/* 27 */     this.textFace = textFace;
/* 28 */     this.background = background;
/* 29 */     this.inactive = inactive;
/*    */     
/* 31 */     this.lastText = "";
/* 32 */     this.text = new JTextArea();
/* 33 */     this.text.setEditable(false);
/* 34 */     this.text.setBackground(background);
/* 35 */     this.text.setForeground(textFace);
/* 36 */     this.text.setColumns(80);
/* 37 */     this.text.setFont(new java.awt.Font("Monospaced", 0, 12));
/*    */     
/* 39 */     this.scrollPane = new javax.swing.JScrollPane(this.text);
/* 40 */     add("Center", this.scrollPane);
/* 41 */     this.text.setRows(10);
/*    */   }
/*    */   
/*    */   public void setTitle(String title)
/*    */   {
/* 46 */     setBorder(javax.swing.BorderFactory.createTitledBorder(title));
/*    */   }
/*    */   
/*    */   public void setInactive(boolean value)
/*    */   {
/* 51 */     if (value) {
/* 52 */       this.text.setBackground(this.inactive);
/*    */     } else
/* 54 */       this.text.setBackground(this.background);
/* 55 */     this.text.revalidate();
/* 56 */     this.text.repaint();
/*    */   }
/*    */   
/*    */   public void updateText(String newText)
/*    */   {
/* 61 */     if (newText.equals(this.lastText))
/* 62 */       return;
/* 63 */     this.lastText = newText;
/* 64 */     this.text.setText(newText);
/* 65 */     this.text.setCaretPosition(this.text.getDocument().getLength());
/*    */   }
/*    */   
/*    */   public String getText()
/*    */   {
/* 70 */     return this.text.getText();
/*    */   }
/*    */ }


/* Location:              C:\Users\Joey\Downloads\launch.jar!\com\emt\proteus\demo\common\JConsolePanel.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */