/*     */ package com.emt.proteus.xserver.display;
/*     */ 
/*     */ import java.awt.Color;
/*     */ import java.awt.Graphics;
/*     */ import java.awt.Rectangle;
/*     */ import java.awt.Shape;
/*     */ import java.io.IOException;
/*     */ import java.util.Hashtable;
/*     */ import java.util.Vector;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class XAlphaPanel
/*     */   extends XComponentImpSwing
/*     */ {
/*  32 */   static int dalpha = 0;
/*  33 */   AlphaBackground alpha = null;
/*  34 */   static Hashtable ctable = new Hashtable();
/*     */   
/*  36 */   private static boolean init = true;
/*     */   
/*     */   public XAlphaPanel()
/*     */   {
/*  40 */     if (init) {
/*  41 */       String foo = XDisplay.alphaBackground;
/*  42 */       if (foo != null)
/*     */         try {
/*  44 */           int balpha = Integer.parseInt(foo);
/*  45 */           if ((balpha & 0xFF) != 255) setAlpha(balpha & 0xFF);
/*     */         } catch (Exception e) {
/*  47 */           setAlpha(foo);
/*     */         }
/*  49 */       init = false;
/*     */     }
/*     */   }
/*     */   
/*     */   static void setAlpha(int alpha) {
/*  54 */     dalpha = alpha;
/*     */   }
/*     */   
/*     */   static void setAlpha(String alphas) {
/*  58 */     byte[] array = alphas.getBytes();
/*  59 */     int end = array.length;
/*  60 */     int start = 0;int current = 0;
/*  61 */     Vector tmp = new Vector();
/*     */     try {
/*  63 */       while (current < end) {
/*  64 */         if ((array[current] == 32) || (array[current] == 44) || (array[current] == 58))
/*     */         {
/*     */ 
/*  67 */           tmp.addElement(getVal(array, start, current - start));
/*  68 */           current++;
/*  69 */           while ((current < end) && (
/*  70 */             (array[current] == 32) || (array[current] == 44) || (array[current] == 58)))
/*     */           {
/*     */ 
/*  73 */             current++;
/*     */           }
/*     */           
/*     */ 
/*     */ 
/*  78 */           if (current >= end) break;
/*  79 */           start = current;
/*     */         }
/*  81 */         current++;
/*     */       }
/*  83 */       if (current != start) {
/*  84 */         tmp.addElement(getVal(array, start, current - start));
/*     */       }
/*     */     }
/*     */     catch (Exception e) {}
/*     */     
/*     */ 
/*  90 */     if (tmp.size() == 1) {
/*  91 */       dalpha = ((Integer)tmp.firstElement()).intValue();
/*     */     }
/*     */     else {
/*  94 */       dalpha = 255;
/*  95 */       int i = 0;
/*  96 */       while (i < tmp.size()) {
/*  97 */         ctable.put(tmp.elementAt(i), tmp.elementAt(i + 1));
/*  98 */         i += 2;
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public void setSize(int w, int h) {
/* 104 */     boolean skip = true;
/* 105 */     if ((w != this.width) || (h != this.height)) skip = false;
/* 106 */     if (this.bwc) skip = false;
/* 107 */     super.setSize(w, h);
/* 108 */     if ((!skip) && (this.alpha != null)) this.alpha.setImage(this.offi);
/*     */   }
/*     */   
/*     */   public void setBackground(Color color, int x, int y, int w, int h) {
/* 112 */     super.setBackground(color, x, y, w, h);
/* 113 */     if (this.alpha != null) {
/* 114 */       int foo = dalpha;
/* 115 */       int bar = getBackground().getRGB() & 0xFFFFFF;
/* 116 */       Object o = ctable.get(new Integer(bar));
/* 117 */       if (o != null) foo = ((Integer)o).intValue();
/* 118 */       if (foo != 255) {
/* 119 */         this.alpha.setColor(color, foo);
/*     */       }
/*     */       else {
/* 122 */         this.alpha = null;
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public void draw(int x, int y, int width, int height) {
/* 128 */     super.draw(x, y, width, height);
/* 129 */     if (!isVisible()) return;
/* 130 */     if (this.alpha == null) {
/* 131 */       if ((this.xWindow != this.xWindow.screen.root) && ((this.xWindow.attr & 0x3) == 2))
/*     */       {
/*     */ 
/*     */ 
/* 135 */         int foo = dalpha;
/* 136 */         int color = getBackground().getRGB() & 0xFFFFFF;
/* 137 */         Object o = ctable.get(new Integer(color));
/*     */         
/* 139 */         if (o != null) foo = ((Integer)o).intValue();
/* 140 */         if (foo != 255) {
/* 141 */           this.alpha = new AlphaBackground(this.offi, foo);
/* 142 */           this.alpha.setColor(getBackground(), foo);
/*     */         }
/*     */       }
/*     */     }
/*     */     else {
/* 147 */       this.alpha.setImage(this.offi);
/*     */     }
/*     */   }
/*     */   
/*     */   public void draw() {
/* 152 */     super.draw();
/* 153 */     if (!isVisible()) return;
/* 154 */     if (this.alpha == null) {
/* 155 */       if ((this.xWindow != this.xWindow.screen.root) && ((this.xWindow.attr & 0x3) == 2))
/*     */       {
/*     */ 
/*     */ 
/* 159 */         int foo = dalpha;
/* 160 */         int color = getBackground().getRGB() & 0xFFFFFF;
/* 161 */         Object o = ctable.get(new Integer(color));
/* 162 */         if (o != null) foo = ((Integer)o).intValue();
/* 163 */         if (foo != 255) {
/* 164 */           this.alpha = new AlphaBackground(this.offi, foo);
/* 165 */           this.alpha.setColor(getBackground(), foo);
/*     */         }
/*     */       }
/*     */     }
/*     */     else {
/* 170 */       this.alpha.setImage(this.offi);
/*     */     }
/*     */   }
/*     */   
/*     */   public void paintComponent(Graphics g) {
/* 175 */     if (this.xWindow.clss == 2) {
/* 176 */       super.paintComponent(g);
/* 177 */       return;
/*     */     }
/* 179 */     if (this.offi == null) { return;
/*     */     }
/* 181 */     if (this.alpha != null) {
/* 182 */       this.alpha.drawImage(g, this.bw, this.bw, this);
/*     */     }
/*     */     else {
/* 185 */       g.drawImage(this.offi, this.bw, this.bw, this);
/*     */     }
/*     */     
/* 188 */     Rectangle r = g.getClip().getBounds();
/* 189 */     if ((this.bw > 0) && ((r.x <= this.bw) || (r.y <= this.bw) || (this.width + this.bw <= r.width + r.x) || (this.height + this.bw <= r.height + r.y)))
/*     */     {
/*     */ 
/*     */ 
/* 193 */       if (this.xWindow.isBorderPixel()) {
/* 194 */         g.setColor(this.xWindow.getColormap().getColor(this.xWindow.border.pixel));
/*     */       }
/*     */       else {
/* 197 */         g.setColor(Color.black);
/*     */       }
/* 199 */       for (int i = this.bw - 1; 0 <= i; i--) {
/* 200 */         g.drawRect(i, i, this.width + 2 * this.bw - i * 2 - 1, this.height + 2 * this.bw - i * 2 - 1);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public boolean isOpaque() {
/* 206 */     if (isVisible()) {
/* 207 */       if (this.xWindow.clss == 2) {
/* 208 */         return false;
/*     */       }
/* 210 */       if (this.alpha != null) return false;
/* 211 */       return super.isOpaque();
/*     */     }
/* 213 */     return false;
/*     */   }
/*     */   
/*     */   public void delete() throws IOException {
/* 217 */     super.delete();
/* 218 */     if (this.alpha != null) {
/* 219 */       this.alpha.dispose();
/* 220 */       this.alpha = null;
/*     */     }
/*     */   }
/*     */   
/*     */   private static Integer getVal(byte[] array, int s, int len) {
/* 225 */     String foo = new String(array, s, len);
/* 226 */     int radix = 10;
/* 227 */     if (foo.startsWith("0x")) {
/* 228 */       radix = 16;
/* 229 */       foo = new String(array, s + 2, len - 2);
/*     */     }
/* 231 */     int i = 0;
/*     */     try {
/* 233 */       i = Integer.parseInt(foo, radix);
/*     */     }
/*     */     catch (Exception e) {}
/* 236 */     return new Integer(i);
/*     */   }
/*     */   
/*     */   public XWindow getWindow() {
/* 240 */     return this.xWindow;
/*     */   }
/*     */ }


/* Location:              C:\Users\Joey\Downloads\launch.jar!\com\emt\proteus\xserver\display\XAlphaPanel.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */