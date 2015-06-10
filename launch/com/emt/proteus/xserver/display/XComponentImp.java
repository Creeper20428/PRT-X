/*      */ package com.emt.proteus.xserver.display;
/*      */ 
/*      */ import com.emt.proteus.xserver.client.XClient;
/*      */ import com.emt.proteus.xserver.display.input.Keymap;
/*      */ import java.awt.Color;
/*      */ import java.awt.Component;
/*      */ import java.awt.Container;
/*      */ import java.awt.Dimension;
/*      */ import java.awt.Frame;
/*      */ import java.awt.Graphics;
/*      */ import java.awt.Image;
/*      */ import java.awt.Insets;
/*      */ import java.awt.Panel;
/*      */ import java.awt.Point;
/*      */ import java.awt.Rectangle;
/*      */ import java.awt.Shape;
/*      */ import java.awt.Toolkit;
/*      */ import java.awt.Window;
/*      */ import java.awt.event.KeyEvent;
/*      */ import java.awt.event.MouseEvent;
/*      */ import java.awt.event.MouseListener;
/*      */ import java.awt.event.MouseMotionListener;
/*      */ import java.awt.image.FilteredImageSource;
/*      */ import java.io.IOException;
/*      */ import java.io.PrintStream;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public class XComponentImp
/*      */   extends Panel
/*      */   implements XComponent, MouseListener, MouseMotionListener
/*      */ {
/*   38 */   static boolean clck_toggle = false;
/*      */   
/*      */   private static final byte s = 0;
/*      */   private static final byte sp = 1;
/*      */   private static final byte spp = 2;
/*      */   private static final byte sppr = 3;
/*      */   private static final int ALT_GR_MASK = 32;
/*   45 */   private static byte threeBstate = 0;
/*   46 */   private static MouseEvent threeBPressed = null;
/*   47 */   private static boolean threeButton = false;
/*      */   
/*      */   private static final int InputOnly = 2;
/*   50 */   private static XClient serverXClient = null;
/*   51 */   private static XComponent ddxwindow = null;
/*      */   
/*      */   private static final int VK_ALT_GRAPH = 21;
/*      */   XWindow XWindow;
/*      */   int width;
/*      */   int height;
/*      */   int bw;
/*   58 */   boolean bwc = false;
/*      */   
/*   60 */   private static Event event = new Event();
/*      */   
/*   62 */   Image offi = null;
/*   63 */   Graphics offg = null;
/*      */   
/*      */   private static int px;
/*      */   private static int py;
/*   67 */   private Rectangle exposed = new Rectangle();
/*      */   private int oldwindowx;
/*      */   private int oldwindowy;
/*      */   
/*      */   public void init(XDisplay xDisplay, XWindow w)
/*      */   {
/*   73 */     if (serverXClient == null) {
/*   74 */       ddxwindow = w.screen.root.ddxwindow;
/*   75 */       threeButton = XDisplay.threeButton;
/*   76 */       serverXClient = w.screen.root.XClient;
/*      */     }
/*   78 */     this.XWindow = w;
/*   79 */     this.bw = w.borderWidth;
/*   80 */     this.bwc = true;
/*   81 */     setSize(w.width, w.height);
/*   82 */     addMouseListener(this);
/*   83 */     addMouseMotionListener(this);
/*      */     
/*   85 */     px = 0;
/*   86 */     py = 0;
/*   87 */     enableEvents(8L);
/*   88 */     setVisible(false);
/*   89 */     setLayout(null);
/*      */     
/*   91 */     clck_toggle = false;
/*      */   }
/*      */   
/*      */   public void setBorder(int bw) {
/*   95 */     if (this.bw != bw) {
/*   96 */       this.bw = bw;
/*   97 */       this.bwc = true;
/*      */     }
/*      */   }
/*      */   
/*      */   public void setVisible(boolean b) {
/*  102 */     if (b) {
/*  103 */       if ((this.offi == null) && (this.XWindow.clss != 2)) {
/*  104 */         allocImage();
/*      */       }
/*  106 */       if ((this.XWindow != this.XWindow.screen.root) && (!isVisible())) {
/*  107 */         this.exposed.setBounds(0, 0, 0, 0);
/*      */       }
/*  109 */       super.setVisible(true);
/*  110 */       if ((this.XWindow.screen.windowmode != 0) && (this.XWindow.hasFrame())) {
/*  111 */         Window frame = this.XWindow.getFrame();
/*      */         
/*  113 */         if ((frame instanceof Frame)) {
/*  114 */           frame.add("Center", this);
/*  115 */           frame.pack();
/*  116 */           synchronized (XWindow.LOCK) {
/*  117 */             Property p = this.XWindow.getProperty();
/*  118 */             while ((p != null) && 
/*  119 */               (p.propertyName != 39)) {
/*  120 */               p = p.next;
/*      */             }
/*  122 */             if ((p != null) && (p.type == 31)) {
/*  123 */               String title = "";
/*  124 */               if (p.size > 0) {
/*  125 */                 title = new String(p.data);
/*      */               }
/*  127 */               ((Frame)frame).setTitle(title);
/*      */             }
/*      */           }
/*      */         } else {
/*  131 */           frame.add(this);
/*      */         }
/*      */         
/*  134 */         frame.validate();
/*  135 */         Insets insets = frame.getInsets();
/*  136 */         frame.setSize(this.XWindow.width + this.XWindow.borderWidth * 2 + insets.left + insets.right, this.XWindow.height + this.XWindow.borderWidth * 2 + insets.top + insets.bottom);
/*      */         
/*      */ 
/*      */ 
/*  140 */         super.setLocation(insets.left, insets.top);
/*  141 */         frame.validate();
/*      */       }
/*      */     }
/*  144 */     else if (isVisible()) {
/*  145 */       super.setVisible(false);
/*  146 */       if ((this.XWindow != this.XWindow.screen.root) && (this.XWindow.screen.root.width * this.XWindow.screen.root.height / 4 <= this.XWindow.width * this.XWindow.height))
/*      */       {
/*      */ 
/*  149 */         freeImage();
/*  150 */         this.exposed.setBounds(0, 0, 0, 0);
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */   public void setLocation(int x, int y)
/*      */   {
/*  157 */     Point p = getLocation();
/*  158 */     if ((p.x == x) && (p.y == y)) return;
/*  159 */     if ((this.XWindow != null) && (this.XWindow.parent != null)) {
/*  160 */       int orgx = p.x - this.XWindow.parent.borderWidth + this.XWindow.borderWidth;
/*  161 */       int orgy = p.y - this.XWindow.parent.borderWidth + this.XWindow.borderWidth;
/*  162 */       int bitgrabity = this.XWindow.attr & 0xF00;
/*  163 */       if ((orgx < 0) || (orgy < 0)) {
/*  164 */         if ((orgx < 0) && (orgx < x) && (bitgrabity != 768) && (bitgrabity != 1536) && (bitgrabity != 2304))
/*      */         {
/*      */ 
/*      */ 
/*  168 */           orgx = orgx * -1 - x * -1;
/*  169 */           this.exposed.x += orgx;
/*  170 */           this.exposed.width -= orgx;
/*  171 */           if (this.exposed.width < 0) this.exposed.width = 0;
/*      */         }
/*  173 */         if ((orgy < 0) && (orgy < y) && (bitgrabity != 1792) && (bitgrabity != 2048) && (bitgrabity != 2304))
/*      */         {
/*      */ 
/*      */ 
/*  177 */           orgy = orgy * -1 - y * -1;
/*  178 */           this.exposed.y += orgy;
/*  179 */           this.exposed.height -= orgy;
/*  180 */           if (this.exposed.height < 0) {
/*  181 */             this.exposed.width = 0;
/*  182 */             this.exposed.height = 0;
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/*  187 */     if ((this.XWindow.screen.windowmode != 0) && (this.XWindow.hasFrame())) {
/*  188 */       this.XWindow.getFrame().setLocation(this.XWindow.origin.x - this.XWindow.borderWidth + this.XWindow.parent.borderWidth, this.XWindow.origin.y - this.XWindow.borderWidth + this.XWindow.parent.borderWidth);
/*      */ 
/*      */ 
/*      */ 
/*      */     }
/*      */     else
/*      */     {
/*      */ 
/*      */ 
/*      */ 
/*  198 */       super.setLocation(x, y);
/*      */     }
/*  200 */     System.err.printf("Location (%d,%d)\n", new Object[] { Integer.valueOf(x), Integer.valueOf(y) });
/*      */   }
/*      */   
/*      */   public void setSize(int w, int h) {
/*  204 */     boolean resized = false;
/*  205 */     if ((w != this.width) || (h != this.height)) { resized = true;
/*      */     }
/*  207 */     if ((!resized) && (!this.bwc)) {
/*  208 */       return;
/*      */     }
/*      */     
/*  211 */     int offx = 0;int offy = 0;
/*      */     
/*  213 */     int bitgrabity = this.XWindow.attr & 0xF00;
/*  214 */     if (bitgrabity != 0) {
/*  215 */       if ((bitgrabity == 1792) || (bitgrabity == 2048) || (bitgrabity == 2304))
/*      */       {
/*      */ 
/*  218 */         offy = this.height - h;
/*      */       }
/*  220 */       if ((bitgrabity == 768) || (bitgrabity == 1536) || (bitgrabity == 2304))
/*      */       {
/*      */ 
/*  223 */         offx = this.width - w;
/*      */       }
/*      */     }
/*      */     
/*  227 */     int orgw = this.width;int orgh = this.height;
/*  228 */     this.width = w;
/*  229 */     this.height = h;
/*  230 */     super.setSize(w + 2 * this.bw, h + 2 * this.bw);
/*      */     
/*  232 */     this.bwc = false;
/*      */     
/*  234 */     if ((this.XWindow.screen.windowmode != 0) && (this.XWindow.hasFrame())) {
/*  235 */       Window frame = this.XWindow.getFrame();
/*  236 */       frame.validate();
/*  237 */       Insets insets = frame.getInsets();
/*  238 */       frame.setSize(w + 2 * this.bw + insets.left + insets.right, h + 2 * this.bw + insets.top + insets.bottom);
/*      */       
/*  240 */       frame.validate();
/*      */     }
/*      */     
/*  243 */     if (this.XWindow.clss == 2) {
/*  244 */       return;
/*      */     }
/*      */     
/*  247 */     if ((this.offi != null) && (resized)) {
/*  248 */       if ((w < this.exposed.x) || (h < this.exposed.y)) {
/*  249 */         this.exposed.setBounds(0, 0, 0, 0);
/*  250 */       } else if ((w < this.exposed.x + this.exposed.width) || (h < this.exposed.y + this.exposed.height))
/*      */       {
/*  252 */         this.exposed.setBounds(this.exposed.x, this.exposed.y, this.exposed.width < w - this.exposed.x ? this.exposed.width : w - this.exposed.x, this.exposed.height < h - this.exposed.y ? this.exposed.height : h - this.exposed.y);
/*      */       }
/*      */       
/*      */ 
/*      */       try
/*      */       {
/*  258 */         Image tmp = this.offi;
/*  259 */         Graphics tmpg = this.offg;
/*      */         
/*  261 */         this.offi = createImage(w, h);
/*  262 */         this.offg = this.offi.getGraphics();
/*  263 */         this.XWindow.makeBackgroundTile(0, 0, w, h);
/*  264 */         if (bitgrabity != 0) {
/*  265 */           this.offg.drawImage(tmp, 0, 0, this);
/*      */         }
/*      */         
/*  268 */         if ((offx != 0) || (offy != 0)) {
/*  269 */           int copyx = 0;int copyy = 0;int copyw = w;int copyh = h;int dx = 0;int dy = 0;
/*  270 */           if (offy > 0) {
/*  271 */             copyy = offy;
/*  272 */             copyh = h - offy;
/*  273 */             dy = offy * -1;
/*  274 */           } else if (offy < 0) {
/*  275 */             dy = offy * -1;
/*      */           }
/*  277 */           if (offx > 0) {
/*  278 */             copyx = offx;
/*  279 */             copyw = w - offx;
/*  280 */             dx = offx * -1;
/*  281 */           } else if (offx < 0) {
/*  282 */             dx = offx * -1;
/*      */           }
/*  284 */           this.offg.copyArea(copyx, copyy, copyw, copyh, dx, dy);
/*      */         }
/*      */         
/*  287 */         if (tmp != this.offi) {
/*  288 */           tmp.flush();
/*  289 */           tmpg.dispose();
/*      */         }
/*      */         
/*  292 */         if (bitgrabity == 0) {
/*  293 */           this.exposed.setBounds(0, 0, 0, 0);
/*  294 */           this.XWindow.makeBackgroundTile(0, 0, this.width, this.height);
/*      */         }
/*  296 */         this.XWindow.currentGC = null;
/*      */       } catch (Exception e) {
/*  298 */         System.err.println(e);
/*  299 */         this.offi = null;
/*  300 */         this.offg = null;
/*      */       } catch (OutOfMemoryError e) {
/*  302 */         System.err.println(e);
/*  303 */         this.offi = null;
/*  304 */         this.offg = null;
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */   private void allocImage()
/*      */   {
/*      */     try {
/*  312 */       this.offi = createImage(this.width, this.height);
/*  313 */       this.offg = this.offi.getGraphics();
/*  314 */       this.offg.setPaintMode();
/*  315 */       if (this.XWindow != this.XWindow.screen.root) {
/*  316 */         this.XWindow.makeBackgroundTile(0, 0, this.width, this.height);
/*      */       }
/*      */     } catch (Exception e) {
/*  319 */       System.err.println(e);
/*  320 */       this.offi = null;
/*  321 */       this.offg = null;
/*      */     } catch (OutOfMemoryError e) {
/*  323 */       System.err.println(e);
/*  324 */       this.offi = null;
/*  325 */       this.offg = null;
/*      */     }
/*      */   }
/*      */   
/*      */   private void freeImage() {
/*  330 */     if (this.offi != null) {
/*  331 */       this.offi.flush();
/*  332 */       this.offi = null;
/*      */     }
/*  334 */     if (this.offg != null) {
/*  335 */       this.offg.dispose();
/*  336 */       this.offg = null;
/*      */     }
/*  338 */     this.XWindow.gmask = 0;
/*      */   }
/*      */   
/*      */   public void setBackground(Color color, int x, int y, int w, int h) {
/*  342 */     super.setBackground(color);
/*      */     
/*  344 */     if (this.offg == null) {
/*  345 */       return;
/*      */     }
/*  347 */     Color tmp = this.offg.getColor();
/*  348 */     this.offg.setColor(color);
/*  349 */     this.offg.fillRect(x, y, w, h);
/*  350 */     this.offg.setColor(tmp);
/*      */   }
/*      */   
/*      */   public void setBackground(Color color) {
/*  354 */     setBackground(color, 0, 0, this.width, this.height);
/*      */   }
/*      */   
/*      */   public Dimension getPreferredSize() {
/*  358 */     return getSize();
/*      */   }
/*      */   
/*      */   public boolean isOptimizedDrawingEnabled() {
/*  362 */     return false;
/*      */   }
/*      */   
/*      */   public void draw(int x, int y, int width, int height) {
/*  366 */     if (!isVisible()) return;
/*  367 */     repaint(x, y, width, height);
/*      */   }
/*      */   
/*      */   public void draw() {
/*  371 */     if (!isVisible()) return;
/*  372 */     repaint();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void paintBorder(Graphics g) {}
/*      */   
/*      */ 
/*      */ 
/*      */   public void paintComponent(Graphics g)
/*      */   {
/*  384 */     if (this.offi == null) { return;
/*      */     }
/*  386 */     g.drawImage(this.offi, this.bw, this.bw, this);
/*      */     
/*  388 */     if (this.XWindow == null) { return;
/*      */     }
/*  390 */     Rectangle r = g.getClip().getBounds();
/*  391 */     if ((this.bw > 0) && ((r.x <= this.bw) || (r.y <= this.bw) || (this.width + this.bw <= r.width + r.x) || (this.height + this.bw <= r.height + r.y)))
/*      */     {
/*      */ 
/*      */ 
/*  395 */       if (this.XWindow.isBorderPixel()) {
/*  396 */         g.setColor(this.XWindow.getColormap().getColor(this.XWindow.border.pixel));
/*      */       } else {
/*  398 */         g.setColor(Color.black);
/*      */       }
/*  400 */       for (int i = this.bw - 1; 0 <= i; i--) {
/*  401 */         g.drawRect(i, i, this.width + 2 * this.bw - i * 2 - 1, this.height + 2 * this.bw - i * 2 - 1);
/*      */       }
/*      */     }
/*      */     
/*  405 */     if ((this.XWindow.screen.windowmode == 0) || (this.XWindow == this.XWindow.screen.root))
/*      */     {
/*  407 */       if ((LogoImage.logoimage != null) && (this.XWindow.x + r.x <= LogoImage.logoimagewidth) && (this.XWindow.y + r.y <= LogoImage.logoimageheight))
/*      */       {
/*      */ 
/*  410 */         g.drawImage(LogoImage.logoimage, 0, 0, LogoImage.logoimagewidth - (this.XWindow.x - this.bw), LogoImage.logoimageheight - (this.XWindow.y - this.bw), this.XWindow.x - this.bw, this.XWindow.y - this.bw, LogoImage.logoimagewidth, LogoImage.logoimageheight, this);
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean isOpaque()
/*      */   {
/*  430 */     return false;
/*      */   }
/*      */   
/*      */   private final void expunion(Rectangle r) {
/*  434 */     int x1 = Math.min(this.exposed.x, r.x);
/*  435 */     int x2 = Math.max(this.exposed.x + this.exposed.width, r.x + r.width);
/*  436 */     int y1 = Math.min(this.exposed.y, r.y);
/*  437 */     int y2 = Math.max(this.exposed.y + this.exposed.height, r.y + r.height);
/*  438 */     this.exposed.setBounds(x1, y1, x2 - x1, y2 - y1);
/*      */   }
/*      */   
/*      */   private final boolean expcontains(int X, int Y, int W, int H) {
/*  442 */     int ww = this.exposed.width;
/*  443 */     int hh = this.exposed.height;
/*  444 */     if ((ww <= 0) || (hh <= 0) || (W <= 0) || (H <= 0)) {
/*  445 */       return false;
/*      */     }
/*  447 */     int xx = this.exposed.x;
/*  448 */     int yy = this.exposed.y;
/*  449 */     return (X >= xx) && (Y >= yy) && (X + W <= xx + ww) && (Y + H <= yy + hh);
/*      */   }
/*      */   
/*      */   public void update(Graphics g) {
/*  453 */     paintComponent(g);
/*      */   }
/*      */   
/*      */   public void paint(Graphics g)
/*      */   {
/*  458 */     paintComponent(g);
/*      */     try {
/*  460 */       super.paint(g);
/*      */     } catch (Exception e) {
/*  462 */       return;
/*      */     }
/*  464 */     if (this.XWindow == null) return;
/*  465 */     XClient XClient = this.XWindow.XClient;
/*  466 */     if ((XClient == null) || (XClient == serverXClient)) return;
/*  467 */     Rectangle r = g.getClip().getBounds();
/*  468 */     if (this.exposed.width == 0) {
/*  469 */       this.exposed.setBounds(r);
/*  470 */     } else { if (expcontains(r.x, r.y, r.width, r.height)) {
/*  471 */         return;
/*      */       }
/*  473 */       expunion(r);
/*      */     }
/*      */     
/*  476 */     event.mkExpose(this.XWindow.id, r.x, r.y, r.width, r.height, 0);
/*      */     try {
/*  478 */       this.XWindow.sendEvent(event, 1, null);
/*      */     }
/*      */     catch (Exception ee) {}
/*      */   }
/*      */   
/*      */ 
/*      */   public void setBorderPixmap(Pixmap p) {}
/*      */   
/*      */   public void mouseClicked(MouseEvent e) {}
/*      */   
/*      */   public void mouseEntered(MouseEvent e)
/*      */   {
/*  490 */     if (this.XWindow == null) return;
/*  491 */     if (this.XWindow.getFrame() != null) {
/*  492 */       Window frame = this.XWindow.getFrame();
/*  493 */       if (((frame instanceof Frame)) && (frame == e.getSource()))
/*      */       {
/*      */         try {
/*  496 */           if (this.XWindow.isRealized()) {
/*  497 */             XWindow.setInputFocus(this.XWindow.XClient, this.XWindow.id, 1, (int)System.currentTimeMillis(), false);
/*      */           }
/*      */         }
/*      */         catch (Exception ee) {
/*  501 */           System.out.println(ee);
/*      */         }
/*  503 */         return;
/*      */       }
/*      */     }
/*      */     
/*  507 */     String str = CopyPaste.getString();
/*  508 */     if ((!CopyPaste.isOwner()) && (str != null)) {
/*  509 */       synchronized (this.XWindow.screen.root) {
/*  510 */         Property p = this.XWindow.screen.root.getProperty();
/*  511 */         while ((p != null) && 
/*  512 */           (p.propertyName != 9)) {
/*  513 */           p = p.next;
/*      */         }
/*  515 */         if (p != null) {
/*  516 */           p.data = str.getBytes();
/*  517 */           p.size = p.data.length;
/*      */         }
/*      */       }
/*      */       
/*  521 */       Selection sel = Selection.getSelection(1);
/*  522 */       if ((sel != null) && (sel.XClient != null)) {
/*  523 */         int time = (int)System.currentTimeMillis();
/*  524 */         Event event = new Event();
/*  525 */         event.mkSelectionClear(time, sel.wid, sel.selection);
/*      */         try {
/*  527 */           sel.XClient.sendEvent(event, 1, 0, 0, null);
/*      */         }
/*      */         catch (Exception ee) {}
/*      */         
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  536 */         sel.XWindow = this.XWindow.screen.root;
/*  537 */         sel.wid = this.XWindow.screen.root.id;
/*  538 */         sel.lastTimeChanged = time;
/*  539 */         sel.XClient = null;
/*      */       }
/*  541 */       CopyPaste.setString(str);
/*      */     }
/*      */     
/*  544 */     if (this.XWindow.id == this.XWindow.screen.rootId) {
/*  545 */       return;
/*      */     }
/*      */     
/*  548 */     if (this.XWindow.isMapped()) {
/*  549 */       requestFocus();
/*  550 */       XWindow.focus.win = this.XWindow.id;
/*      */     }
/*      */     
/*  553 */     int x = e.getX() + this.XWindow.x;
/*  554 */     int y = e.getY() + this.XWindow.y;
/*      */     
/*  556 */     XWindow.sprite.hot.x = x;
/*  557 */     XWindow.sprite.hot.y = y;
/*      */     
/*  559 */     int mod = e.getModifiers();
/*  560 */     int state = 0;
/*  561 */     if ((mod & 0x10) != 0) state |= 0x100;
/*  562 */     if ((mod & 0x8) != 0) state |= 0x200;
/*  563 */     if ((mod & 0x4) != 0) state |= 0x400;
/*  564 */     if ((mod & 0x1) != 0) state |= 0x1;
/*  565 */     if ((mod & 0x2) != 0) { state |= 0x4;
/*      */     }
/*  567 */     XClient XClient = this.XWindow.XClient;
/*  568 */     if ((XClient == null) || (XClient == serverXClient)) { return;
/*      */     }
/*  570 */     event.mkEnterNotify(0, this.XWindow.screen.rootId, this.XWindow.id, 0, x, y, e.getX(), e.getY(), state, 0, 3);
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */     try
/*      */     {
/*  577 */       XWindow.sendDeviceEvent(this.XWindow, event, XWindow.grab, null, 1);
/*      */     }
/*      */     catch (Exception ee) {}
/*      */   }
/*      */   
/*      */   public void mouseExited(MouseEvent e) {
/*  583 */     if (this.XWindow == null) { return;
/*      */     }
/*  585 */     if (this.XWindow.id == this.XWindow.screen.rootId) {
/*  586 */       return;
/*      */     }
/*      */     
/*  589 */     int x = e.getX() + this.XWindow.x;
/*  590 */     int y = e.getY() + this.XWindow.y;
/*      */     
/*  592 */     XWindow.sprite.hot.x = x;
/*  593 */     XWindow.sprite.hot.y = y;
/*      */     
/*  595 */     int mod = e.getModifiers();
/*  596 */     int state = 0;
/*  597 */     if ((mod & 0x10) != 0) state |= 0x100;
/*  598 */     if ((mod & 0x8) != 0) state |= 0x200;
/*  599 */     if ((mod & 0x4) != 0) state |= 0x400;
/*  600 */     if ((mod & 0x1) != 0) state |= 0x1;
/*  601 */     if ((mod & 0x2) != 0) { state |= 0x4;
/*      */     }
/*  603 */     XClient XClient = this.XWindow.XClient;
/*  604 */     if ((XClient == null) || (XClient == serverXClient)) { return;
/*      */     }
/*  606 */     event.mkLeaveNotify(0, this.XWindow.screen.rootId, this.XWindow.id, 0, x, y, e.getX(), e.getY(), state, 0, 3);
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */     try
/*      */     {
/*  613 */       XWindow.sendDeviceEvent(this.XWindow, event, XWindow.grab, null, 1);
/*      */     }
/*      */     catch (Exception ee) {}
/*      */   }
/*      */   
/*      */   public void mousePressed(MouseEvent e) {
/*  619 */     if (threeButton) {
/*  620 */       if (threeBstate == 0) {
/*  621 */         threeBPressed = e;
/*  622 */         threeBstate = 1;
/*  623 */         return;
/*      */       }
/*  625 */       if (threeBstate == 1)
/*      */       {
/*      */ 
/*  628 */         threeBPressed = null;
/*  629 */         threeBstate = 2;
/*  630 */         e = new MouseEvent((Component)e.getSource(), e.getID(), e.getWhen(), e.getModifiers() & 0xFFFFFFEB | 0x8, e.getX(), e.getY(), e.getClickCount(), e.isPopupTrigger());
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*  636 */     procPressed(e);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   private void procPressed(MouseEvent e)
/*      */   {
/*  644 */     int x = e.getX() + this.XWindow.x;
/*  645 */     int y = e.getY() + this.XWindow.y;
/*  646 */     this.oldwindowx = this.XWindow.x;
/*  647 */     this.oldwindowy = this.XWindow.y;
/*  648 */     XWindow.sprite.hot.x = x;
/*  649 */     XWindow.sprite.hot.y = y;
/*  650 */     int mod = e.getModifiers();
/*  651 */     if (mod == 0) {
/*  652 */       mod |= 0x10;
/*      */     }
/*      */     
/*  655 */     int state = 0;
/*  656 */     int detail = 1;
/*  657 */     if ((mod & 0x10) != 0) detail = 1;
/*  658 */     if ((mod & 0x8) != 0) detail = 2;
/*  659 */     if ((mod & 0x4) != 0) detail = 3;
/*  660 */     if ((mod & 0x1) != 0) state |= 0x1;
/*  661 */     if ((mod & 0x2) != 0) { state |= 0x4;
/*      */     }
/*      */     
/*  664 */     Event.filters[6] = (0x2040 | 128 << detail);
/*      */     
/*      */ 
/*      */ 
/*  668 */     event.mkButtonPress(detail, this.XWindow.screen.rootId, this.XWindow.id, 0, x, y, e.getX(), e.getY(), state, 1);
/*      */     
/*      */     try
/*      */     {
/*  672 */       if ((XWindow.grab == null) && 
/*  673 */         (XWindow.checkDeviceGrabs(event, 0, 1))) {
/*  674 */         return;
/*      */       }
/*      */       
/*  677 */       if (XWindow.grab != null) {
/*  678 */         XWindow.sendGrabbedEvent(event, false, 1);
/*      */       } else {
/*  680 */         XWindow.sendDeviceEvent(this.XWindow, event, XWindow.grab, null, 1);
/*      */       }
/*      */     }
/*      */     catch (Exception ee) {}
/*  684 */     if ((mod & 0x10) != 0) {
/*  685 */       state |= 0x100;
/*      */     }
/*  687 */     if ((mod & 0x8) != 0) {
/*  688 */       state |= 0x200;
/*      */     }
/*  690 */     if ((mod & 0x4) != 0) {
/*  691 */       state |= 0x400;
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*  697 */     XWindow.sprite.hot.state = state;
/*      */   }
/*      */   
/*      */   public void mouseReleased(MouseEvent e) {
/*  701 */     if (threeButton) {
/*  702 */       if (threeBstate == 3) {
/*  703 */         threeBPressed = null;
/*  704 */         threeBstate = 0;
/*  705 */         return;
/*      */       }
/*  707 */       if (threeBstate == 1) {
/*  708 */         procPressed(threeBPressed);
/*  709 */         threeBPressed = null;
/*  710 */         threeBstate = 0;
/*  711 */       } else if (threeBstate == 2) {
/*  712 */         threeBPressed = null;
/*  713 */         threeBstate = 3;
/*  714 */         e = new MouseEvent((Component)e.getSource(), e.getID(), e.getWhen(), e.getModifiers() & 0xFFFFFFEB | 0x8, e.getX(), e.getY(), e.getClickCount(), e.isPopupTrigger());
/*      */       }
/*      */     }
/*      */     
/*      */     try
/*      */     {
/*  720 */       procReleased(e);
/*      */     }
/*      */     catch (Exception ee) {}
/*      */   }
/*      */   
/*      */   private void procReleased(MouseEvent e) {
/*  726 */     int x = e.getX() + this.XWindow.x;
/*  727 */     int y = e.getY() + this.XWindow.y;
/*      */     
/*  729 */     XWindow.sprite.hot.x = x;
/*  730 */     XWindow.sprite.hot.y = y;
/*      */     
/*  732 */     int mod = e.getModifiers();
/*  733 */     int state = 0;
/*  734 */     int detail = 0;
/*      */     
/*  736 */     if ((mod & 0x10) != 0) {
/*  737 */       state |= 0x100;
/*  738 */       detail = 1;
/*      */     }
/*  740 */     if ((mod & 0x8) != 0) {
/*  741 */       state |= 0x200;
/*  742 */       detail = 2;
/*      */     }
/*  744 */     if ((mod & 0x4) != 0) {
/*  745 */       state |= 0x400;
/*  746 */       detail = 3;
/*      */     }
/*  748 */     if ((mod & 0x1) != 0) state |= 0x1;
/*  749 */     if ((mod & 0x2) != 0) { state |= 0x4;
/*      */     }
/*  751 */     XWindow.sprite.hot.state = 0;
/*  752 */     Event.filters[6] = 64;
/*      */     
/*  754 */     event.mkButtonRelease(detail, this.XWindow.screen.rootId, this.XWindow.id, 0, x, y, e.getX(), e.getY(), state, 1);
/*      */     
/*      */     try
/*      */     {
/*  758 */       if (XWindow.grab != null) XWindow.sendGrabbedEvent(event, true, 1); else {
/*  759 */         XWindow.sendDeviceEvent(this.XWindow, event, XWindow.grab, null, 1);
/*      */       }
/*      */     } catch (Exception ee) {}
/*  762 */     XWindow.grab = null;
/*      */   }
/*      */   
/*      */   public void mouseDragged(MouseEvent e)
/*      */   {
/*  767 */     if ((threeButton) && 
/*  768 */       (threeBstate != 0)) {
/*  769 */       if (threeBstate == 1) {
/*  770 */         procPressed(threeBPressed);
/*  771 */         threeBPressed = null;
/*  772 */         threeBstate = 0;
/*  773 */       } else if (threeBstate == 2) {
/*  774 */         e = new MouseEvent((Component)e.getSource(), e.getID(), e.getWhen(), e.getModifiers() & 0xFFFFFFEB | 0x8, e.getX(), e.getY(), e.getClickCount(), e.isPopupTrigger());
/*      */ 
/*      */       }
/*  777 */       else if (threeBstate == 3) {
/*  778 */         return;
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*  783 */     if (this.XWindow == null) { return;
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  790 */     int x = e.getX() + this.XWindow.x;
/*  791 */     int y = e.getY() + this.XWindow.y;
/*      */     
/*      */ 
/*  794 */     XWindow.sprite.hot.x = x;
/*  795 */     XWindow.sprite.hot.y = y;
/*      */     
/*  797 */     int mod = e.getModifiers();
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  806 */     int state = 0;
/*  807 */     int detail = 0;
/*      */     
/*  809 */     if ((mod & 0x10) != 0) {
/*  810 */       state |= 0x100;
/*  811 */       detail = 1;
/*      */     }
/*  813 */     if ((mod & 0x8) != 0) {
/*  814 */       state |= 0x200;
/*  815 */       detail = 2;
/*      */     }
/*  817 */     if ((mod & 0x4) != 0) {
/*  818 */       state |= 0x400;
/*  819 */       detail = 3;
/*      */     }
/*  821 */     if ((mod & 0x1) != 0) state |= 0x1;
/*  822 */     if ((mod & 0x2) != 0) { state |= 0x4;
/*      */     }
/*  824 */     XWindow.sprite.hot.state = state;
/*      */     
/*  826 */     px = x;
/*  827 */     py = y;
/*      */     
/*  829 */     event.mkMotionNotify(1, this.XWindow.screen.rootId, XWindow.sprite.win.id, 0, px, py, e.getX(), e.getY(), state, 1);
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     try
/*      */     {
/*  837 */       if (!XWindow.checkMotion(event, this.XWindow)) {
/*  838 */         return;
/*      */       }
/*  840 */       event.mkMotionNotify(1, this.XWindow.screen.rootId, XWindow.sprite.win.id, 0, px, py, px - XWindow.sprite.win.x, py - XWindow.sprite.win.y, state, 1);
/*      */       
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  846 */       if (XWindow.grab != null) {
/*  847 */         XWindow.sendGrabbedEvent(event, false, 1);
/*      */       } else {
/*  849 */         XWindow.sendDeviceEvent(XWindow.sprite.win, event, XWindow.grab, null, 1);
/*      */       }
/*      */     }
/*      */     catch (Exception ee) {}
/*      */   }
/*      */   
/*      */   public void mouseMoved(MouseEvent e)
/*      */   {
/*  857 */     int x = e.getX() + this.XWindow.x;
/*  858 */     int y = e.getY() + this.XWindow.y;
/*      */     
/*  860 */     XWindow.sprite.hot.x = x;
/*  861 */     XWindow.sprite.hot.y = y;
/*      */     
/*  863 */     int mod = e.getModifiers();
/*  864 */     int state = 0;
/*      */     
/*  866 */     px = x;
/*  867 */     py = y;
/*      */     
/*  869 */     if ((mod & 0x10) != 0) state |= 0x100;
/*  870 */     if ((mod & 0x8) != 0) state |= 0x200;
/*  871 */     if ((mod & 0x4) != 0) state |= 0x400;
/*  872 */     if ((mod & 0x1) != 0) state |= 0x1;
/*  873 */     if ((mod & 0x2) != 0) { state |= 0x4;
/*      */     }
/*  875 */     XWindow.sprite.hot.state = state;
/*      */     
/*  877 */     event.mkMotionNotify(0, this.XWindow.screen.rootId, this.XWindow.id, 0, x, y, x - this.XWindow.x, y - this.XWindow.y, state, 1);
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */     try
/*      */     {
/*  884 */       if (!XWindow.checkMotion(event, this.XWindow)) {
/*  885 */         return;
/*      */       }
/*  887 */       if (XWindow.grab != null) {
/*  888 */         XWindow.sendGrabbedEvent(event, false, 1);
/*      */       } else {
/*  890 */         XWindow.sendDeviceEvent(this.XWindow, event, XWindow.grab, null, 1);
/*      */       }
/*      */     }
/*      */     catch (Exception ee) {}
/*      */   }
/*      */   
/*      */   public void processKeyEvent(KeyEvent e) {
/*  897 */     int id = e.getID();
/*  898 */     if (id == 401) {
/*  899 */       keyPressed(e);
/*  900 */     } else if (id == 402) {
/*  901 */       keyReleased(e);
/*  902 */     } else if (id == 400) {
/*  903 */       keyTyped(e);
/*      */     }
/*  905 */     e.consume();
/*      */   }
/*      */   
/*      */   public void keyPressed(KeyEvent e) {
/*  909 */     if (!this.XWindow.isMapped()) { return;
/*      */     }
/*  911 */     if (e.getKeyCode() == 20) {
/*  912 */       if (clck_toggle) {
/*  913 */         clck_toggle = false;
/*  914 */         XWindow.sprite.hot.state &= 0xFFFFFFFE;
/*      */       } else {
/*  916 */         clck_toggle = true;
/*  917 */         XWindow.sprite.hot.state |= 0x1;
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*  922 */     XWindow dest = XWindow.sprite.win;
/*  923 */     if (XWindow.focus.XWindow != null) { dest = XWindow.focus.XWindow;
/*      */     }
/*  925 */     if ((this.XWindow.screen.windowmode != 0) && (dest == this.XWindow.screen.root))
/*      */     {
/*  927 */       if (XWindow.focus.XWindow != null) dest = XWindow.sprite.win; else {
/*  928 */         dest = this.XWindow;
/*      */       }
/*      */     }
/*  931 */     if (dest.XClient == null) { return;
/*      */     }
/*  933 */     int kcode = Keymap.km.getCode(e);
/*  934 */     event.mkKeyPress(kcode, this.XWindow.screen.rootId, dest.id, 0, XWindow.sprite.hot.x, XWindow.sprite.hot.y, XWindow.sprite.hot.x - this.XWindow.x, XWindow.sprite.hot.y - this.XWindow.y, XWindow.sprite.hot.state, 1);
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     try
/*      */     {
/*  944 */       if (XWindow.grab != null) {
/*  945 */         XWindow.sendGrabbedEvent(event, false, 1);
/*      */       } else {
/*  947 */         XWindow.sendDeviceEvent(dest, event, XWindow.grab, null, 1);
/*      */       }
/*      */     }
/*      */     catch (Exception ee) {}
/*  951 */     kcode = e.getKeyCode();
/*  952 */     int state = XWindow.sprite.hot.state;
/*  953 */     if (kcode == 17) {
/*  954 */       if ((state & 0x4) == 0) state |= 0x4;
/*  955 */     } else if (kcode == 16) {
/*  956 */       if ((state & 0x1) == 0) state |= 0x1;
/*  957 */     } else if (kcode == 18) {
/*  958 */       if ((state & 0x8) == 0) state |= 0x8;
/*  959 */     } else if ((kcode == 21) && 
/*  960 */       ((state & 0x20) == 0)) { state |= 0x20;
/*      */     }
/*      */     
/*  963 */     if ((state & 0xC) == 12) {
/*  964 */       state -= 12;
/*  965 */       state |= 0x20;
/*      */     }
/*  967 */     XWindow.sprite.hot.state = state;
/*      */   }
/*      */   
/*      */   public void keyReleased(KeyEvent e) {
/*  971 */     if ((this.XWindow == null) || (!this.XWindow.isMapped()))
/*  972 */       return;
/*  973 */     XClient XClient = this.XWindow.XClient;
/*  974 */     if (XClient == null) { return;
/*      */     }
/*  976 */     int kcode = Keymap.km.getCode(e);
/*  977 */     event.mkKeyRelease(kcode, this.XWindow.screen.rootId, this.XWindow.id, 0, XWindow.sprite.hot.x, XWindow.sprite.hot.y, XWindow.sprite.hot.x - this.XWindow.x, XWindow.sprite.hot.y - this.XWindow.y, XWindow.sprite.hot.state, 1);
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     try
/*      */     {
/*  987 */       if (XWindow.grab != null) {
/*  988 */         XWindow.sendGrabbedEvent(event, false, 1);
/*      */       } else {
/*  990 */         XWindow.sendDeviceEvent(this.XWindow, event, XWindow.grab, null, 1);
/*      */       }
/*      */     }
/*      */     catch (Exception ee) {}
/*      */     
/*  995 */     kcode = e.getKeyCode();
/*  996 */     int state = XWindow.sprite.hot.state;
/*  997 */     if (kcode == 17) {
/*  998 */       if ((state & 0x4) != 0) { state -= 4;
/*      */       }
/* 1000 */       if ((state & 0x20) != 0) state -= 32;
/* 1001 */     } else if ((kcode == 16) && (!clck_toggle)) {
/* 1002 */       if ((state & 0x1) != 0) state--;
/* 1003 */     } else if (kcode == 18) {
/* 1004 */       if ((state & 0x8) != 0) state -= 8;
/* 1005 */     } else if ((kcode == 21) && 
/* 1006 */       ((state & 0x20) != 0)) { state -= 32;
/*      */     }
/* 1008 */     XWindow.sprite.hot.state = state;
/*      */   }
/*      */   
/*      */ 
/*      */   public void keyTyped(KeyEvent e) {}
/*      */   
/*      */   public Image getImage()
/*      */   {
/* 1016 */     if (this.offi == null) allocImage();
/* 1017 */     return this.offi;
/*      */   }
/*      */   
/*      */   public Image getImage(GC gc, int x, int y, int w, int h) {
/* 1021 */     Image i = getImage();
/* 1022 */     if ((gc != null) && (gc.clip_mask != null) && ((gc.clip_mask instanceof ClipPixmap))) {
/* 1023 */       TransparentFilter tf = new TransparentFilter(0, 0, (Pixmap)gc.clip_mask.getMask());
/* 1024 */       i = Toolkit.getDefaultToolkit().createImage(new FilteredImageSource(i.getSource(), tf));
/*      */     }
/*      */     
/* 1027 */     return i;
/*      */   }
/*      */   
/*      */   public Graphics getGraphics2() {
/* 1031 */     if (this.XWindow.clss == 2) {
/* 1032 */       return getGraphics();
/*      */     }
/* 1034 */     if (!isVisible()) {
/* 1035 */       return null;
/*      */     }
/*      */     
/* 1038 */     if (this.offg == null) allocImage();
/* 1039 */     Graphics g = this.offg;
/* 1040 */     return g;
/*      */   }
/*      */   
/*      */   public final Graphics getGraphics(GC gc, int mask) {
/* 1044 */     if (!isVisible()) {
/* 1045 */       return null;
/*      */     }
/* 1047 */     if (this.offg == null) allocImage();
/* 1048 */     Graphics graphics = this.offg;
/* 1049 */     if (((mask & 0x8000) != 0) && ((gc.attr & 0x400) != 0))
/*      */     {
/* 1051 */       graphics = getGraphics();
/* 1052 */       this.XWindow.currentGC = null;
/*      */     } else {
/* 1054 */       if ((gc == this.XWindow.currentGC) && (gc.time == this.XWindow.gctime) && ((mask & (this.XWindow.gmask ^ 0xFFFFFFFF)) == 0))
/*      */       {
/*      */ 
/*      */ 
/* 1058 */         return graphics;
/*      */       }
/* 1060 */       this.XWindow.gctime = gc.time;
/* 1061 */       this.XWindow.currentGC = gc;
/* 1062 */       this.XWindow.gmask = mask;
/*      */     }
/*      */     
/* 1065 */     if ((gc.clip_mask != null) && ((gc.clip_mask instanceof ClipRectangles))) {
/* 1066 */       Rectangle rec = (Rectangle)gc.clip_mask.getMask();
/* 1067 */       if (rec != null) {
/* 1068 */         graphics = this.offg;
/*      */       }
/*      */       
/* 1071 */       if ((rec != null) && ((rec.x != 0) || (rec.y != 0) || (rec.width != this.XWindow.width) || (rec.height != this.XWindow.height)))
/*      */       {
/*      */ 
/*      */ 
/*      */ 
/* 1076 */         graphics.setClip(rec.x, rec.y, rec.width, rec.height);
/*      */       }
/*      */     }
/*      */     
/* 1080 */     if ((mask & 0x1) != 0) {
/* 1081 */       Color color = this.XWindow.getColormap().getColor(gc.fgPixel);
/* 1082 */       if (gc.function == 6) {
/* 1083 */         this.XWindow.gmask &= 0xFFFFFFFE;
/* 1084 */         graphics.setXORMode(new Color((color.getRGB() ^ graphics.getColor().getRGB()) & 0xFFFFFF));
/* 1085 */       } else if (gc.function == 10) {
/* 1086 */         this.XWindow.gmask &= 0xFFFFFFFE;
/* 1087 */         graphics.setXORMode(this.XWindow.screen.defaultColormap.getColor(this.XWindow.background.pixel));
/*      */       } else {
/* 1089 */         graphics.setColor(color);
/*      */       }
/*      */     }
/*      */     
/* 1093 */     if ((mask & 0x4000) != 0) {
/* 1094 */       XFont XFont = gc.XFont;
/* 1095 */       graphics.setFont(XFont.getFont());
/*      */     }
/*      */     
/* 1098 */     if (((mask & 0x10) == 0) && ((mask & 0x20) == 0) && ((mask & 0x40) == 0) && ((mask & 0x80) != 0)) {}
/*      */     
/*      */ 
/*      */ 
/*      */ 
/* 1103 */     return graphics;
/*      */   }
/*      */   
/*      */   public void drawImage(Clip clip, Image img, int dx, int dy, int w, int h) {
/* 1107 */     if ((clip == null) || ((clip instanceof ClipPixmap))) {
/* 1108 */       drawImage(img, dx, dy, w, h);
/* 1109 */       return;
/*      */     }
/* 1111 */     ClipRectangles rclip = (ClipRectangles)clip;
/* 1112 */     Rectangle rec = rclip.masks[0];
/* 1113 */     if (this.offg == null) allocImage();
/* 1114 */     Shape tmp = this.offg.getClip();
/* 1115 */     if (tmp == null) {
/* 1116 */       tmp = new Rectangle(0, 0, this.XWindow.width, this.XWindow.height);
/*      */     }
/* 1118 */     this.offg.clipRect(rec.x, rec.y, rec.width, rec.height);
/* 1119 */     drawImage(img, dx, dy, w, h);
/* 1120 */     for (int i = 1; i < rclip.masks.length; i++) {
/* 1121 */       this.offg.setClip(tmp);
/* 1122 */       rec = rclip.masks[i];
/* 1123 */       this.offg.clipRect(rec.x, rec.y, rec.width, rec.height);
/* 1124 */       drawImage(img, dx, dy, w, h);
/*      */     }
/* 1126 */     this.offg.setClip(tmp);
/*      */   }
/*      */   
/*      */   public void drawImage(Image img, int dx, int dy, int w, int h) {
/* 1130 */     if (this.offg == null) allocImage();
/* 1131 */     this.offg.drawImage(img, dx, dy, w, h, ddxwindow);
/*      */   }
/*      */   
/*      */   public void fillImage(Image img, int w, int h) {
/* 1135 */     for (int i = 0; i < this.height; i += h) {
/* 1136 */       for (int j = 0; j < this.width; j += w) {
/* 1137 */         this.offg.drawImage(img, j, i, ddxwindow);
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void fillImage(Image img, int w, int h, int offx, int offy)
/*      */   {
/* 1160 */     if (offx < 0) {
/* 1161 */       while (offx < 0) {
/* 1162 */         offx += w;
/*      */       }
/*      */     }
/* 1165 */     if (offy < 0) {
/* 1166 */       while (offy < 0) {
/* 1167 */         offy += h;
/*      */       }
/*      */     }
/* 1170 */     if (w < offx) {
/* 1171 */       while (w < offx) {
/* 1172 */         offx -= w;
/*      */       }
/*      */     }
/* 1175 */     if (h < offy) {
/* 1176 */       while (h < offy) {
/* 1177 */         offy -= h;
/*      */       }
/*      */     }
/* 1180 */     if ((offx == 0) && (offy == 0)) {
/* 1181 */       fillImage(img, w, h);
/* 1182 */       return;
/*      */     }
/* 1184 */     this.offg.drawImage(img, -offx, -offy, ddxwindow);
/* 1185 */     if (-offx + w < this.width) {
/* 1186 */       int i = -offx + w;
/* 1187 */       while (i < this.width) {
/* 1188 */         this.offg.drawImage(img, i, -offy, ddxwindow);
/* 1189 */         i += w;
/*      */       }
/*      */     }
/* 1192 */     if (-offy + h < this.height) {
/* 1193 */       int i = -offy + h;
/* 1194 */       while (i < this.height) {
/* 1195 */         this.offg.drawImage(img, -offx, i, ddxwindow);
/* 1196 */         i += h;
/*      */       }
/*      */     }
/*      */     
/* 1200 */     offx = -offx + w;
/* 1201 */     offy = -offy + h;
/* 1202 */     if ((this.width <= offx) && (this.height <= offy)) { return;
/*      */     }
/* 1204 */     for (int i = offy; i < this.height; i += h) {
/* 1205 */       for (int j = offx; j < this.width; j += w) {
/* 1206 */         this.offg.drawImage(img, j, i, ddxwindow);
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void copyArea(XWindow dst, GC gc, int srcx, int srcy, int width, int height, int destx, int desty)
/*      */   {
/* 1233 */     Graphics g = dst.getGraphics();
/* 1234 */     if (g == null) { return;
/*      */     }
/* 1236 */     if (this.XWindow == dst) {
/* 1237 */       copyArea(srcx, srcy, width, height, destx - srcx, desty - srcy);
/* 1238 */       dst.draw(destx, desty, width, height);
/* 1239 */       return;
/*      */     }
/*      */     
/* 1242 */     Image img = this.XWindow.getImage(gc, srcx, srcy, width, height);
/* 1243 */     if ((srcx == 0) && (srcy == 0) && (width == this.XWindow.width) && (height == this.XWindow.height)) {
/* 1244 */       dst.ddxwindow.drawImage(gc.clip_mask, img, destx, desty, width, height);
/*      */     } else {
/* 1246 */       Shape tmp = g.getClip();
/* 1247 */       g.clipRect(destx, desty, width, height);
/* 1248 */       dst.ddxwindow.drawImage(gc.clip_mask, img, destx - srcx, desty - srcy, this.XWindow.width, this.XWindow.height);
/*      */       
/* 1250 */       if (tmp == null) {
/* 1251 */         g.setClip(0, 0, dst.width, dst.height);
/*      */       } else {
/* 1253 */         g.setClip(tmp);
/*      */       }
/*      */     }
/* 1256 */     dst.draw(destx, desty, width, height);
/* 1257 */     if (img != this.XWindow.getImage()) {
/* 1258 */       img.flush();
/*      */     }
/*      */   }
/*      */   
/*      */   public void copyArea(int sx, int sy, int w, int h, int dx, int dy) {
/* 1263 */     if (this.offg == null) allocImage();
/* 1264 */     if (((dx <= 0) || (w <= dx)) && ((dy <= 0) || (h <= dy))) {
/* 1265 */       this.offg.copyArea(sx, sy, w, h, dx, dy);
/*      */     }
/* 1267 */     else if ((0 < dy) && (dy < h)) {
/* 1268 */       int ssy = sy + h - dy;
/*      */       
/* 1270 */       while (ssy >= sy) {
/* 1271 */         this.offg.copyArea(sx, ssy, w, dy, dx, dy);
/* 1272 */         ssy -= dy;
/*      */       }
/* 1274 */       ssy += dy;
/* 1275 */       if (sy < ssy) {
/* 1276 */         this.offg.copyArea(sx, sy, w, ssy - sy, dx, dy);
/*      */       }
/* 1278 */     } else if ((0 < dx) && (dx < w)) {
/* 1279 */       int ssx = sx + w - dx;
/*      */       
/* 1281 */       while (ssx >= sx) {
/* 1282 */         this.offg.copyArea(ssx, sy, dx, h, dx, dy);
/* 1283 */         ssx -= dx;
/*      */       }
/* 1285 */       ssx += dx;
/* 1286 */       if (sx < ssx) {
/* 1287 */         this.offg.copyArea(sx, sy, ssx - sx, h, dx, dy);
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */   public void delete() throws IOException
/*      */   {
/* 1294 */     if (this.offi != null) {
/* 1295 */       this.offi.flush();
/* 1296 */       this.offi = null;
/*      */     }
/* 1298 */     if (this.offg != null) {
/* 1299 */       this.offg.dispose();
/* 1300 */       this.offg = null;
/*      */     }
/* 1302 */     this.XWindow = null;
/* 1303 */     Container tmp = getParent();
/* 1304 */     if (tmp != null) {
/* 1305 */       tmp.remove(this);
/*      */     }
/*      */   }
/*      */   
/*      */   public void restoreClip() {
/* 1310 */     if (this.offg != null) this.offg.setClip(0, 0, this.XWindow.width, this.XWindow.height);
/*      */   }
/*      */   
/*      */   public XWindow getWindow() {
/* 1314 */     return this.XWindow;
/*      */   }
/*      */ }


/* Location:              C:\Users\Joey\Downloads\launch.jar!\com\emt\proteus\xserver\display\XComponentImp.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */