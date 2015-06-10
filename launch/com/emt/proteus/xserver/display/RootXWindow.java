/*     */ package com.emt.proteus.xserver.display;
/*     */ 
/*     */ import com.emt.proteus.xserver.client.XClient;
/*     */ import java.awt.Container;
/*     */ import java.io.PrintStream;
/*     */ import javax.swing.JComponent;
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
/*     */ public class RootXWindow
/*     */   extends XWindow
/*     */ {
/*     */   private static final int InputOutput = 1;
/*     */   Container rootwindow;
/*     */   
/*     */   public RootXWindow(XDisplay xDisplay, Container container, Screen screen, Format[] format, XClient c)
/*     */   {
/*  33 */     super(screen.rootId);
/*  34 */     this.rootwindow = container;
/*  35 */     this.XClient = c;
/*  36 */     screen.setRoot(this);
/*  37 */     this.width = screen.width;
/*  38 */     this.height = screen.height;
/*  39 */     this.screen = screen;
/*  40 */     this.depth = screen.rootDepth;
/*  41 */     this.id = screen.rootId;
/*  42 */     this.type = 0;
/*  43 */     this.x = (this.y = 0);
/*  44 */     this.origin.x = 0;
/*  45 */     this.origin.y = 0;
/*  46 */     this.clss = 1;
/*  47 */     for (int i = 0; i < format.length; i++) {
/*  48 */       if (format[i].depth == screen.rootDepth) {
/*  49 */         this.bitsPerPixel = format[i].bpp;
/*     */       }
/*     */     }
/*  52 */     setVisual(screen.rootVisual);
/*  53 */     setBackgroundIsPixel();
/*  54 */     this.background.pixel = screen.white;
/*     */     
/*  56 */     setBorderIsPixel();
/*  57 */     this.border.pixel = screen.black;
/*  58 */     this.borderWidth = 0;
/*  59 */     add(this);
/*  60 */     makeOptional();
/*  61 */     this.attr &= 0xFFFFFFF7;
/*     */     
/*  63 */     this.optional.cursor = Cursor.rootCursor;
/*  64 */     setColormap(screen.defaultColormap);
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     try
/*     */     {
/*  73 */       this.ddxwindow = ((XComponent)XWindow.dDXWindow.newInstance());
/*     */     } catch (Exception e) {
/*  75 */       System.err.println(e);
/*     */     }
/*     */     
/*     */ 
/*  79 */     this.ddxwindow.init(xDisplay, this);
/*  80 */     xDisplay.getContainer().add((JComponent)this.ddxwindow);
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
/* 118 */     this.ddxwindow.setVisible(true);
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
/* 129 */     this.ddxwindow.requestFocus();
/* 130 */     XWindow.focus.win = this.id;
/*     */     
/* 132 */     XWindow.LOCK = this.rootwindow.getTreeLock();
/* 133 */     XClient.LOCK = this.rootwindow.getTreeLock();
/* 134 */     Resource.LOCK = this.rootwindow.getTreeLock();
/*     */     
/* 136 */     spriteTrace[0] = this;
/* 137 */     sprite.win = this;
/*     */   }
/*     */   
/*     */   public boolean contains(int xx, int yy) {
/* 141 */     return this.ddxwindow.contains(xx, yy);
/*     */   }
/*     */   
/*     */   public int adjustY(int y, int height, int bwidth)
/*     */   {
/* 146 */     return Math.min(150, (this.height - height - bwidth) / 2);
/*     */   }
/*     */   
/*     */   public int adjustX(int x, int width, int bwidth)
/*     */   {
/* 151 */     return (this.width - width - bwidth) / 2;
/*     */   }
/*     */ }


/* Location:              C:\Users\Joey\Downloads\launch.jar!\com\emt\proteus\xserver\display\RootXWindow.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */