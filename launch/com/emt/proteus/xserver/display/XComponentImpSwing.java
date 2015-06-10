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
/*      */ import javax.swing.JPanel;
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
/*      */ public class XComponentImpSwing
/*      */   extends JPanel
/*      */   implements XComponent, MouseListener, MouseMotionListener
/*      */ {
/*   44 */   static boolean clck_toggle = false;
/*      */   
/*      */   private static final byte s = 0;
/*      */   private static final byte sp = 1;
/*      */   private static final byte spp = 2;
/*      */   private static final byte sppr = 3;
/*      */   private static final int ALT_GR_MASK = 32;
/*   51 */   private static byte threeBstate = 0;
/*   52 */   private static MouseEvent threeBPressed = null;
/*   53 */   private static boolean threeButton = false;
/*      */   
/*      */   private static final int InputOnly = 2;
/*   56 */   private static XClient serverXClient = null;
/*   57 */   private static XComponent ddxwindow = null;
/*      */   
/*      */   private static final int VK_ALT_GRAPH = 21;
/*      */   XWindow xWindow;
/*      */   int width;
/*      */   int height;
/*      */   int bw;
/*   64 */   boolean bwc = false;
/*      */   
/*   66 */   static Event event = new Event();
/*      */   
/*   68 */   Image offi = null;
/*   69 */   Graphics offg = null;
/*      */   
/*      */   static int px;
/*      */   static int py;
/*   73 */   Rectangle exposed = new Rectangle();
/*      */   
/*      */   private XDisplay xDisplay;
/*      */   
/*      */ 
/*      */   public void init(XDisplay xDisplay, XWindow w)
/*      */   {
/*   80 */     if (serverXClient == null) {
/*   81 */       ddxwindow = w.screen.root.ddxwindow;
/*   82 */       threeButton = XDisplay.threeButton;
/*   83 */       serverXClient = w.screen.root.XClient;
/*      */     }
/*   85 */     this.xDisplay = xDisplay;
/*   86 */     this.xWindow = w;
/*   87 */     this.bw = w.borderWidth;
/*   88 */     this.bwc = true;
/*   89 */     setSize(w.width, w.height);
/*      */     
/*   91 */     addMouseListener(this);
/*   92 */     addMouseMotionListener(this);
/*      */     
/*   94 */     px = 0;
/*   95 */     py = 0;
/*   96 */     enableEvents(8L);
/*   97 */     setVisible(false);
/*   98 */     setLayout(null);
/*      */     
/*  100 */     clck_toggle = false;
/*      */   }
/*      */   
/*      */   public void setBorder(int bw) {
/*  104 */     if (this.bw != bw) {
/*  105 */       this.bw = bw;
/*  106 */       this.bwc = true;
/*      */     }
/*      */   }
/*      */   
/*      */   public void setVisible(boolean b) {
/*  111 */     if (b) {
/*  112 */       if ((this.offi == null) && (this.xWindow.clss != 2)) {
/*  113 */         allocImage();
/*      */       }
/*  115 */       if ((this.xWindow != this.xWindow.screen.root) && (!isVisible())) {
/*  116 */         this.exposed.setBounds(0, 0, 0, 0);
/*      */       }
/*  118 */       super.setVisible(true);
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
/*      */     }
/*  155 */     else if (isVisible()) {
/*  156 */       super.setVisible(false);
/*  157 */       if ((this.xWindow != this.xWindow.screen.root) && (this.xWindow.screen.root.width * this.xWindow.screen.root.height / 4 <= this.xWindow.width * this.xWindow.height))
/*      */       {
/*      */ 
/*  160 */         freeImage();
/*  161 */         this.exposed.setBounds(0, 0, 0, 0);
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */   public void setLocation(int x, int y)
/*      */   {
/*  168 */     Point p = getLocation();
/*  169 */     if ((p.x == x) && (p.y == y)) return;
/*  170 */     if ((this.xWindow != null) && (this.xWindow.parent != null)) {
/*  171 */       int orgx = p.x - this.xWindow.parent.borderWidth + this.xWindow.borderWidth;
/*  172 */       int orgy = p.y - this.xWindow.parent.borderWidth + this.xWindow.borderWidth;
/*  173 */       int bitgrabity = this.xWindow.attr & 0xF00;
/*  174 */       if ((orgx < 0) || (orgy < 0)) {
/*  175 */         if ((orgx < 0) && (orgx < x) && (bitgrabity != 768) && (bitgrabity != 1536) && (bitgrabity != 2304))
/*      */         {
/*      */ 
/*      */ 
/*  179 */           orgx = orgx * -1 - x * -1;
/*  180 */           this.exposed.x += orgx;
/*  181 */           this.exposed.width -= orgx;
/*  182 */           if (this.exposed.width < 0) this.exposed.width = 0;
/*      */         }
/*  184 */         if ((orgy < 0) && (orgy < y) && (bitgrabity != 1792) && (bitgrabity != 2048) && (bitgrabity != 2304))
/*      */         {
/*      */ 
/*      */ 
/*  188 */           orgy = orgy * -1 - y * -1;
/*  189 */           this.exposed.y += orgy;
/*  190 */           this.exposed.height -= orgy;
/*  191 */           if (this.exposed.height < 0) { this.exposed.width = 0;this.exposed.height = 0;
/*      */           }
/*      */         }
/*      */       } }
/*  195 */     if ((this.xWindow.screen.windowmode != 0) && (this.xWindow.hasFrame())) {
/*  196 */       this.xWindow.getFrame().setLocation(this.xWindow.origin.x - this.xWindow.borderWidth + this.xWindow.parent.borderWidth, this.xWindow.origin.y - this.xWindow.borderWidth + this.xWindow.parent.borderWidth);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     }
/*      */     else
/*      */     {
/*      */ 
/*      */ 
/*      */ 
/*  207 */       super.setLocation(x, y);
/*      */     }
/*      */   }
/*      */   
/*      */   public void setSize(int w, int h) {
/*  212 */     boolean resized = false;
/*  213 */     if ((w != this.width) || (h != this.height)) { resized = true;
/*      */     }
/*  215 */     if ((!resized) && (!this.bwc)) {
/*  216 */       return;
/*      */     }
/*      */     
/*  219 */     int offx = 0;int offy = 0;
/*      */     
/*  221 */     int bitgrabity = this.xWindow.attr & 0xF00;
/*  222 */     if (bitgrabity != 0) {
/*  223 */       if ((bitgrabity == 1792) || (bitgrabity == 2048) || (bitgrabity == 2304))
/*      */       {
/*      */ 
/*  226 */         offy = this.height - h;
/*      */       }
/*  228 */       if ((bitgrabity == 768) || (bitgrabity == 1536) || (bitgrabity == 2304))
/*      */       {
/*      */ 
/*  231 */         offx = this.width - w;
/*      */       }
/*      */     }
/*      */     
/*  235 */     int orgw = this.width;int orgh = this.height;
/*  236 */     this.width = w;
/*  237 */     this.height = h;
/*  238 */     super.setSize(w + 2 * this.bw, h + 2 * this.bw);
/*      */     
/*  240 */     this.bwc = false;
/*      */     
/*  242 */     if ((this.xWindow.screen.windowmode != 0) && (this.xWindow.hasFrame())) {
/*  243 */       Window frame = this.xWindow.getFrame();
/*  244 */       frame.validate();
/*  245 */       Insets insets = frame.getInsets();
/*  246 */       frame.setSize(w + 2 * this.bw + insets.left + insets.right, h + 2 * this.bw + insets.top + insets.bottom);
/*      */       
/*  248 */       frame.validate();
/*      */     }
/*      */     
/*  251 */     if (this.xWindow.clss == 2) {
/*  252 */       return;
/*      */     }
/*      */     
/*  255 */     if ((this.offi != null) && (resized)) {
/*  256 */       if ((w < this.exposed.x) || (h < this.exposed.y)) {
/*  257 */         this.exposed.setBounds(0, 0, 0, 0);
/*      */       }
/*  259 */       else if ((w < this.exposed.x + this.exposed.width) || (h < this.exposed.y + this.exposed.height))
/*      */       {
/*  261 */         this.exposed.setBounds(this.exposed.x, this.exposed.y, this.exposed.width < w - this.exposed.x ? this.exposed.width : w - this.exposed.x, this.exposed.height < h - this.exposed.y ? this.exposed.height : h - this.exposed.y);
/*      */       }
/*      */       
/*      */ 
/*      */       try
/*      */       {
/*  267 */         Image tmp = this.offi;
/*  268 */         Graphics tmpg = this.offg;
/*      */         
/*  270 */         this.offi = createImage(w, h);
/*  271 */         this.offg = this.offi.getGraphics();
/*  272 */         this.xWindow.makeBackgroundTile(0, 0, w, h);
/*  273 */         if (bitgrabity != 0) {
/*  274 */           this.offg.drawImage(tmp, 0, 0, this);
/*      */         }
/*      */         
/*  277 */         if ((offx != 0) || (offy != 0)) {
/*  278 */           int copyx = 0;int copyy = 0;int copyw = w;int copyh = h;int dx = 0;int dy = 0;
/*  279 */           if (offy > 0) { copyy = offy;copyh = h - offy;dy = offy * -1;
/*  280 */           } else if (offy < 0) { dy = offy * -1; }
/*  281 */           if (offx > 0) { copyx = offx;copyw = w - offx;dx = offx * -1; } else if (offx < 0) {
/*  282 */             dx = offx * -1; }
/*  283 */           this.offg.copyArea(copyx, copyy, copyw, copyh, dx, dy);
/*      */         }
/*      */         
/*  286 */         if (tmp != this.offi) {
/*  287 */           tmp.flush();
/*  288 */           tmpg.dispose();
/*      */         }
/*      */         
/*  291 */         if (bitgrabity == 0) {
/*  292 */           this.exposed.setBounds(0, 0, 0, 0);
/*  293 */           this.xWindow.makeBackgroundTile(0, 0, this.width, this.height);
/*      */         }
/*  295 */         this.xWindow.currentGC = null;
/*      */       }
/*      */       catch (Exception e) {
/*  298 */         System.err.println(e);
/*  299 */         this.offi = null;
/*  300 */         this.offg = null;
/*      */       }
/*      */       catch (OutOfMemoryError e) {
/*  303 */         System.err.println(e);
/*  304 */         this.offi = null;
/*  305 */         this.offg = null;
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */   private void allocImage()
/*      */   {
/*      */     try {
/*  313 */       this.offi = createImage(this.width, this.height);
/*  314 */       this.offg = this.offi.getGraphics();
/*  315 */       this.offg.setPaintMode();
/*  316 */       if (this.xWindow != this.xWindow.screen.root) {
/*  317 */         this.xWindow.makeBackgroundTile(0, 0, this.width, this.height);
/*      */       }
/*      */     }
/*      */     catch (Exception e) {
/*  321 */       System.err.println(e);
/*  322 */       this.offi = null;
/*  323 */       this.offg = null;
/*      */     }
/*      */     catch (OutOfMemoryError e) {
/*  326 */       System.err.println(e);
/*  327 */       this.offi = null;
/*  328 */       this.offg = null;
/*      */     }
/*      */   }
/*      */   
/*      */   private void freeImage() {
/*  333 */     if (this.offi != null) {
/*  334 */       this.offi.flush();
/*  335 */       this.offi = null;
/*      */     }
/*  337 */     if (this.offg != null) {
/*  338 */       this.offg.dispose();
/*  339 */       this.offg = null;
/*      */     }
/*  341 */     this.xWindow.gmask = 0;
/*      */   }
/*      */   
/*      */   public void setBackground(Color color, int x, int y, int w, int h) {
/*  345 */     super.setBackground(color);
/*      */     
/*  347 */     if (this.offg == null) return;
/*  348 */     Color tmp = this.offg.getColor();
/*  349 */     this.offg.setColor(color);
/*  350 */     this.offg.fillRect(x, y, w, h);
/*  351 */     this.offg.setColor(tmp);
/*      */   }
/*      */   
/*      */   public void setBackground(Color color) {
/*  355 */     setBackground(color, 0, 0, this.width, this.height);
/*      */   }
/*      */   
/*      */   public Dimension getPreferredSize() {
/*  359 */     return getSize();
/*      */   }
/*      */   
/*      */   public boolean isOptimizedDrawingEnabled() {
/*  363 */     return false;
/*      */   }
/*      */   
/*      */   public void draw(int x, int y, int width, int height) {
/*  367 */     if (!isVisible()) return;
/*  368 */     repaint(x, y, width, height);
/*      */   }
/*      */   
/*      */   public void draw() {
/*  372 */     if (!isVisible()) return;
/*  373 */     repaint();
/*      */   }
/*      */   
/*      */   public void paintBorder(Graphics g) {}
/*      */   
/*      */   public void paintComponent(Graphics g)
/*      */   {
/*  380 */     if (this.xWindow.clss == 2) {
/*  381 */       super.paintComponent(g);
/*  382 */       return;
/*      */     }
/*      */     
/*  385 */     if (this.offi == null) { return;
/*      */     }
/*  387 */     g.drawImage(this.offi, this.bw, this.bw, this);
/*      */     
/*  389 */     Rectangle r = g.getClip().getBounds();
/*  390 */     if ((this.bw > 0) && ((r.x <= this.bw) || (r.y <= this.bw) || (this.width + this.bw <= r.width + r.x) || (this.height + this.bw <= r.height + r.y)))
/*      */     {
/*      */ 
/*      */ 
/*  394 */       if (this.xWindow.isBorderPixel()) {
/*  395 */         g.setColor(this.xWindow.getColormap().getColor(this.xWindow.border.pixel));
/*      */       }
/*      */       else {
/*  398 */         g.setColor(Color.black);
/*      */       }
/*  400 */       for (int i = this.bw - 1; 0 <= i; i--) {
/*  401 */         g.drawRect(i, i, this.width + 2 * this.bw - i * 2 - 1, this.height + 2 * this.bw - i * 2 - 1);
/*      */       }
/*      */     }
/*      */     
/*  405 */     if ((this.xWindow.screen.windowmode == 0) || (this.xWindow == this.xWindow.screen.root))
/*      */     {
/*  407 */       if ((LogoImage.logoimage != null) && (this.xWindow.x + r.x <= LogoImage.logoimagewidth) && (this.xWindow.y + r.y <= LogoImage.logoimageheight))
/*      */       {
/*      */ 
/*  410 */         g.drawImage(LogoImage.logoimage, 0, 0, LogoImage.logoimagewidth - (this.xWindow.x - this.bw), LogoImage.logoimageheight - (this.xWindow.y - this.bw), this.xWindow.x - this.bw, this.xWindow.y - this.bw, LogoImage.logoimagewidth, LogoImage.logoimageheight, this);
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
/*      */   public boolean isOpaque()
/*      */   {
/*  425 */     if (isVisible()) {
/*  426 */       if (this.xWindow.clss == 2) {
/*  427 */         return false;
/*      */       }
/*  429 */       return super.isOpaque();
/*      */     }
/*  431 */     return false;
/*      */   }
/*      */   
/*      */   private final void expunion(Rectangle r) {
/*  435 */     int x1 = Math.min(this.exposed.x, r.x);
/*  436 */     int x2 = Math.max(this.exposed.x + this.exposed.width, r.x + r.width);
/*  437 */     int y1 = Math.min(this.exposed.y, r.y);
/*  438 */     int y2 = Math.max(this.exposed.y + this.exposed.height, r.y + r.height);
/*  439 */     this.exposed.setBounds(x1, y1, x2 - x1, y2 - y1);
/*      */   }
/*      */   
/*      */   private final boolean expcontains(int X, int Y, int W, int H) {
/*  443 */     int ww = this.exposed.width;
/*  444 */     int hh = this.exposed.height;
/*  445 */     if ((ww <= 0) || (hh <= 0) || (W <= 0) || (H <= 0)) {
/*  446 */       return false;
/*      */     }
/*  448 */     int xx = this.exposed.x;
/*  449 */     int yy = this.exposed.y;
/*  450 */     return (X >= xx) && (Y >= yy) && (X + W <= xx + ww) && (Y + H <= yy + hh);
/*      */   }
/*      */   
/*  453 */   public void update(Graphics g) { System.out.println("update: "); }
/*      */   
/*      */   public void paint(Graphics g) {
/*      */     try {
/*  457 */       super.paint(g);
/*      */     } catch (Exception e) {
/*  459 */       return;
/*      */     }
/*      */     try {
/*  462 */       XClient XClient = this.xWindow.XClient;
/*  463 */       if ((XClient == null) || (XClient == serverXClient)) return;
/*  464 */       Rectangle r = g.getClip().getBounds();
/*  465 */       if (this.exposed.width == 0) {
/*  466 */         this.exposed.setBounds(r);
/*      */       } else {
/*  468 */         if (expcontains(r.x, r.y, r.width, r.height)) {
/*  469 */           return;
/*      */         }
/*      */         
/*  472 */         expunion(r);
/*      */       }
/*      */       
/*  475 */       event.mkExpose(this.xWindow.id, r.x, r.y, r.width, r.height, 0);
/*  476 */       this.xWindow.sendEvent(event, 1, null);
/*      */     }
/*      */     catch (Exception ee) {}
/*      */   }
/*      */   
/*      */   public void setBorderPixmap(Pixmap p) {}
/*      */   
/*      */   public void mouseClicked(MouseEvent e) {}
/*      */   
/*      */   public void mouseEntered(MouseEvent e)
/*      */   {
/*  487 */     if ((this.xWindow != null) && (this.xWindow.getFrame() != null)) {
/*  488 */       Window frame = this.xWindow.getFrame();
/*  489 */       if (((frame instanceof Frame)) && (frame == e.getSource()))
/*      */       {
/*      */         try {
/*  492 */           if (this.xWindow.isRealized()) {
/*  493 */             XWindow.setInputFocus(this.xWindow.XClient, this.xWindow.id, 1, (int)System.currentTimeMillis(), false);
/*      */           }
/*      */         }
/*      */         catch (Exception ee)
/*      */         {
/*  498 */           System.out.println(ee);
/*      */         }
/*  500 */         return;
/*      */       }
/*      */     }
/*      */     
/*  504 */     String str = CopyPaste.getString();
/*  505 */     if ((!CopyPaste.isOwner()) && (str != null)) {
/*  506 */       synchronized (this.xWindow.screen.root) {
/*  507 */         Property p = this.xWindow.screen.root.getProperty();
/*  508 */         while ((p != null) && 
/*  509 */           (p.propertyName != 9)) {
/*  510 */           p = p.next;
/*      */         }
/*  512 */         if (p != null) {
/*  513 */           p.data = str.getBytes();
/*  514 */           p.size = p.data.length;
/*      */         }
/*      */       }
/*      */       
/*  518 */       Selection sel = Selection.getSelection(1);
/*  519 */       if ((sel != null) && (sel.XClient != null)) {
/*  520 */         int time = (int)System.currentTimeMillis();
/*  521 */         Event event = new Event();
/*  522 */         event.mkSelectionClear(time, sel.wid, sel.selection);
/*      */         try {
/*  524 */           sel.XClient.sendEvent(event, 1, 0, 0, null);
/*      */         }
/*      */         catch (Exception ee) {}
/*      */         
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  533 */         sel.XWindow = this.xWindow.screen.root;
/*  534 */         sel.wid = this.xWindow.screen.root.id;
/*  535 */         sel.lastTimeChanged = time;
/*  536 */         sel.XClient = null;
/*      */       }
/*  538 */       CopyPaste.setString(str);
/*      */     }
/*      */     
/*  541 */     if (this.xWindow.id == this.xWindow.screen.rootId) {
/*  542 */       return;
/*      */     }
/*      */     
/*  545 */     if (this.xWindow.isMapped()) {
/*  546 */       requestFocus();
/*  547 */       XWindow.focus.win = this.xWindow.id;
/*      */     }
/*      */     
/*  550 */     int x = e.getX() + this.xWindow.x;
/*  551 */     int y = e.getY() + this.xWindow.y;
/*      */     
/*  553 */     XWindow.sprite.hot.x = x;
/*  554 */     XWindow.sprite.hot.y = y;
/*      */     
/*  556 */     int mod = e.getModifiers();
/*  557 */     int state = 0;
/*  558 */     if ((mod & 0x10) != 0) state |= 0x100;
/*  559 */     if ((mod & 0x8) != 0) state |= 0x200;
/*  560 */     if ((mod & 0x4) != 0) state |= 0x400;
/*  561 */     if ((mod & 0x1) != 0) state |= 0x1;
/*  562 */     if ((mod & 0x2) != 0) { state |= 0x4;
/*      */     }
/*  564 */     XClient XClient = this.xWindow.XClient;
/*  565 */     if ((XClient == null) || (XClient == serverXClient)) { return;
/*      */     }
/*  567 */     event.mkEnterNotify(0, this.xWindow.screen.rootId, this.xWindow.id, 0, x, y, e.getX(), e.getY(), state, 0, 3);
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */     try
/*      */     {
/*  574 */       XWindow.sendDeviceEvent(this.xWindow, event, XWindow.grab, null, 1);
/*      */     }
/*      */     catch (Exception ee) {}
/*      */   }
/*      */   
/*      */   public void mouseExited(MouseEvent e)
/*      */   {
/*  581 */     if (this.xWindow == null) { return;
/*      */     }
/*  583 */     if (this.xWindow.id == this.xWindow.screen.rootId) {
/*  584 */       return;
/*      */     }
/*      */     
/*  587 */     int x = e.getX() + this.xWindow.x;
/*  588 */     int y = e.getY() + this.xWindow.y;
/*      */     
/*  590 */     XWindow.sprite.hot.x = x;
/*  591 */     XWindow.sprite.hot.y = y;
/*      */     
/*  593 */     int mod = e.getModifiers();
/*  594 */     int state = 0;
/*  595 */     if ((mod & 0x10) != 0) state |= 0x100;
/*  596 */     if ((mod & 0x8) != 0) state |= 0x200;
/*  597 */     if ((mod & 0x4) != 0) state |= 0x400;
/*  598 */     if ((mod & 0x1) != 0) state |= 0x1;
/*  599 */     if ((mod & 0x2) != 0) { state |= 0x4;
/*      */     }
/*  601 */     XClient XClient = this.xWindow.XClient;
/*  602 */     if ((XClient == null) || (XClient == serverXClient)) { return;
/*      */     }
/*  604 */     event.mkLeaveNotify(0, this.xWindow.screen.rootId, this.xWindow.id, 0, x, y, e.getX(), e.getY(), state, 0, 3);
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */     try
/*      */     {
/*  611 */       XWindow.sendDeviceEvent(this.xWindow, event, XWindow.grab, null, 1);
/*      */     }
/*      */     catch (Exception ee) {}
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
/*      */   public void mousePressed(MouseEvent e)
/*      */   {
/*  635 */     procPressed(e);
/*      */   }
/*      */   
/*      */   private void procPressed(MouseEvent e) {
/*  639 */     int x = e.getX() + this.xWindow.x;
/*  640 */     int y = e.getY() + this.xWindow.y;
/*  641 */     XWindow.sprite.hot.x = x;
/*  642 */     XWindow.sprite.hot.y = y;
/*  643 */     int mod = e.getModifiers();
/*  644 */     if (mod == 0) { mod |= 0x10;
/*      */     }
/*  646 */     int state = 0;
/*  647 */     int detail = 1;
/*  648 */     if ((mod & 0x10) != 0) detail = 1;
/*  649 */     if ((mod & 0x8) != 0) detail = 2;
/*  650 */     if ((mod & 0x4) != 0) detail = 3;
/*  651 */     if ((mod & 0x1) != 0) state |= 0x1;
/*  652 */     if ((mod & 0x2) != 0) { state |= 0x4;
/*      */     }
/*      */     
/*  655 */     Event.filters[6] = (0x2040 | 128 << detail);
/*      */     
/*      */ 
/*      */ 
/*  659 */     event.mkButtonPress(detail, this.xWindow.screen.rootId, this.xWindow.id, 0, x, y, e.getX(), e.getY(), state, 1);
/*      */     
/*      */     try
/*      */     {
/*  663 */       if ((XWindow.grab == null) && 
/*  664 */         (XWindow.checkDeviceGrabs(event, 0, 1))) {
/*  665 */         return;
/*      */       }
/*      */       
/*  668 */       if (XWindow.grab != null) {
/*  669 */         XWindow.sendGrabbedEvent(event, false, 1);
/*      */       } else {
/*  671 */         XWindow.sendDeviceEvent(this.xWindow, event, XWindow.grab, null, 1);
/*      */       }
/*      */     }
/*      */     catch (Exception ee) {}
/*      */     
/*  676 */     if ((mod & 0x10) != 0) state |= 0x100;
/*  677 */     if ((mod & 0x8) != 0) state |= 0x200;
/*  678 */     if ((mod & 0x4) != 0) { state |= 0x400;
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*  683 */     XWindow.sprite.hot.state = state;
/*      */   }
/*      */   
/*  686 */   public void mouseReleased(MouseEvent e) { if (threeButton) {
/*  687 */       if (threeBstate == 3) {
/*  688 */         threeBPressed = null;
/*  689 */         threeBstate = 0;
/*  690 */         return;
/*      */       }
/*  692 */       if (threeBstate == 1) {
/*  693 */         procPressed(threeBPressed);
/*  694 */         threeBPressed = null;
/*  695 */         threeBstate = 0;
/*      */       }
/*  697 */       else if (threeBstate == 2) {
/*  698 */         threeBPressed = null;
/*  699 */         threeBstate = 3;
/*  700 */         e = new MouseEvent((Component)e.getSource(), e.getID(), e.getWhen(), e.getModifiers() & 0xFFFFFFEB | 0x8, e.getX(), e.getY(), e.getClickCount(), e.isPopupTrigger());
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*  706 */     procReleased(e);
/*      */   }
/*      */   
/*  709 */   private void procReleased(MouseEvent e) { int x = e.getX() + this.xWindow.x;
/*  710 */     int y = e.getY() + this.xWindow.y;
/*      */     
/*  712 */     XWindow.sprite.hot.x = x;
/*  713 */     XWindow.sprite.hot.y = y;
/*      */     
/*  715 */     int mod = e.getModifiers();
/*  716 */     int state = 0;
/*  717 */     int detail = 0;
/*      */     
/*  719 */     if ((mod & 0x10) != 0) { state |= 0x100;detail = 1; }
/*  720 */     if ((mod & 0x8) != 0) { state |= 0x200;detail = 2; }
/*  721 */     if ((mod & 0x4) != 0) { state |= 0x400;detail = 3; }
/*  722 */     if ((mod & 0x1) != 0) state |= 0x1;
/*  723 */     if ((mod & 0x2) != 0) { state |= 0x4;
/*      */     }
/*  725 */     XWindow.sprite.hot.state = 0;
/*  726 */     Event.filters[6] = 64;
/*      */     
/*  728 */     event.mkButtonRelease(detail, this.xWindow.screen.rootId, this.xWindow.id, 0, x, y, e.getX(), e.getY(), state, 1);
/*      */     
/*      */     try
/*      */     {
/*  732 */       if (XWindow.grab != null) XWindow.sendGrabbedEvent(event, true, 1); else {
/*  733 */         XWindow.sendDeviceEvent(this.xWindow, event, XWindow.grab, null, 1);
/*      */       }
/*      */     }
/*      */     catch (Exception ee) {}
/*  737 */     XWindow.grab = null;
/*      */   }
/*      */   
/*      */   public void mouseDragged(MouseEvent e)
/*      */   {
/*  742 */     if ((threeButton) && 
/*  743 */       (threeBstate != 0)) {
/*  744 */       if (threeBstate == 1) {
/*  745 */         procPressed(threeBPressed);
/*  746 */         threeBPressed = null;
/*  747 */         threeBstate = 0;
/*      */       }
/*  749 */       else if (threeBstate == 2) {
/*  750 */         e = new MouseEvent((Component)e.getSource(), e.getID(), e.getWhen(), e.getModifiers() & 0xFFFFFFEB | 0x8, e.getX(), e.getY(), e.getClickCount(), e.isPopupTrigger());
/*      */ 
/*      */ 
/*      */       }
/*  754 */       else if (threeBstate == 3) {
/*  755 */         return;
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*  760 */     int x = e.getX() + this.xWindow.x;
/*  761 */     int y = e.getY() + this.xWindow.y;
/*      */     
/*  763 */     XWindow.sprite.hot.x = x;
/*  764 */     XWindow.sprite.hot.y = y;
/*      */     
/*  766 */     int mod = e.getModifiers();
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  775 */     int state = 0;
/*  776 */     int detail = 0;
/*      */     
/*  778 */     if ((mod & 0x10) != 0) { state |= 0x100;detail = 1; }
/*  779 */     if ((mod & 0x8) != 0) { state |= 0x200;detail = 2; }
/*  780 */     if ((mod & 0x4) != 0) { state |= 0x400;detail = 3; }
/*  781 */     if ((mod & 0x1) != 0) state |= 0x1;
/*  782 */     if ((mod & 0x2) != 0) { state |= 0x4;
/*      */     }
/*  784 */     XWindow.sprite.hot.state = state;
/*      */     
/*  786 */     px = x;
/*  787 */     py = y;
/*      */     
/*  789 */     event.mkMotionNotify(1, this.xWindow.screen.rootId, XWindow.sprite.win.id, 0, px, py, e.getX(), e.getY(), state, 1);
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     try
/*      */     {
/*  798 */       if (!XWindow.checkMotion(event, this.xWindow)) {
/*  799 */         return;
/*      */       }
/*  801 */       event.mkMotionNotify(1, this.xWindow.screen.rootId, XWindow.sprite.win.id, 0, px, py, px - XWindow.sprite.win.x, py - XWindow.sprite.win.y, state, 1);
/*      */       
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  809 */       if (XWindow.grab != null) {
/*  810 */         XWindow.sendGrabbedEvent(event, false, 1);
/*      */       } else {
/*  812 */         XWindow.sendDeviceEvent(XWindow.sprite.win, event, XWindow.grab, null, 1);
/*      */       }
/*      */     }
/*      */     catch (Exception ee) {}
/*      */   }
/*      */   
/*      */   public void mouseMoved(MouseEvent e)
/*      */   {
/*  820 */     int x = e.getX() + this.xWindow.x;
/*  821 */     int y = e.getY() + this.xWindow.y;
/*      */     
/*  823 */     XWindow.sprite.hot.x = x;
/*  824 */     XWindow.sprite.hot.y = y;
/*      */     
/*  826 */     int mod = e.getModifiers();
/*  827 */     int state = 0;
/*      */     
/*  829 */     px = x;
/*  830 */     py = y;
/*      */     
/*  832 */     if ((mod & 0x10) != 0) state |= 0x100;
/*  833 */     if ((mod & 0x8) != 0) state |= 0x200;
/*  834 */     if ((mod & 0x4) != 0) state |= 0x400;
/*  835 */     if ((mod & 0x1) != 0) state |= 0x1;
/*  836 */     if ((mod & 0x2) != 0) { state |= 0x4;
/*      */     }
/*  838 */     XWindow.sprite.hot.state = state;
/*      */     
/*  840 */     event.mkMotionNotify(0, this.xWindow.screen.rootId, this.xWindow.id, 0, e.getX(), e.getY(), x - this.xWindow.origin.x, y - this.xWindow.origin.y, state, 1);
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     try
/*      */     {
/*  850 */       if (!XWindow.checkMotion(event, this.xWindow)) {
/*  851 */         return;
/*      */       }
/*  853 */       if (XWindow.grab != null) {
/*  854 */         XWindow.sendGrabbedEvent(event, false, 1);
/*      */       } else {
/*  856 */         XWindow.sendDeviceEvent(this.xWindow, event, XWindow.grab, null, 1);
/*      */       }
/*      */     }
/*      */     catch (Exception ee) {}
/*      */   }
/*      */   
/*      */   public void processKeyEvent(KeyEvent e) {
/*  863 */     int id = e.getID();
/*  864 */     if (id == 401) { keyPressed(e);
/*  865 */     } else if (id == 402) { keyReleased(e);
/*  866 */     } else if (id == 400) keyTyped(e);
/*  867 */     e.consume();
/*      */   }
/*      */   
/*      */   public void keyPressed(KeyEvent e) {
/*  871 */     if (!this.xWindow.isMapped()) { return;
/*      */     }
/*  873 */     if (e.getKeyCode() == 20) {
/*  874 */       if (clck_toggle) {
/*  875 */         clck_toggle = false;
/*  876 */         XWindow.sprite.hot.state &= 0xFFFFFFFE;
/*      */       }
/*      */       else {
/*  879 */         clck_toggle = true;
/*  880 */         XWindow.sprite.hot.state |= 0x1;
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*  885 */     XWindow dest = XWindow.sprite.win;
/*  886 */     if (XWindow.focus.XWindow != null) { dest = XWindow.focus.XWindow;
/*      */     }
/*  888 */     if ((this.xWindow.screen.windowmode != 0) && (dest == this.xWindow.screen.root))
/*      */     {
/*  890 */       if (XWindow.focus.XWindow != null) dest = XWindow.sprite.win; else {
/*  891 */         dest = this.xWindow;
/*      */       }
/*      */     }
/*  894 */     if (dest.XClient == null) { return;
/*      */     }
/*  896 */     int kcode = Keymap.km.getCode(e);
/*  897 */     event.mkKeyPress(kcode, this.xWindow.screen.rootId, dest.id, 0, XWindow.sprite.hot.x, XWindow.sprite.hot.y, XWindow.sprite.hot.x - this.xWindow.x, XWindow.sprite.hot.y - this.xWindow.y, XWindow.sprite.hot.state, 1);
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     try
/*      */     {
/*  907 */       if (XWindow.grab != null) {
/*  908 */         XWindow.sendGrabbedEvent(event, false, 1);
/*      */       } else {
/*  910 */         XWindow.sendDeviceEvent(dest, event, XWindow.grab, null, 1);
/*      */       }
/*      */     }
/*      */     catch (Exception ee) {}
/*      */     
/*  915 */     kcode = e.getKeyCode();
/*  916 */     int state = XWindow.sprite.hot.state;
/*  917 */     if (kcode == 17) {
/*  918 */       if ((state & 0x4) == 0) state |= 0x4;
/*      */     }
/*  920 */     else if (kcode == 16) {
/*  921 */       if ((state & 0x1) == 0) state |= 0x1;
/*      */     }
/*  923 */     else if (kcode == 18) {
/*  924 */       if ((state & 0x8) == 0) state |= 0x8;
/*      */     }
/*  926 */     else if ((kcode == 21) && 
/*  927 */       ((state & 0x20) == 0)) { state |= 0x20;
/*      */     }
/*      */     
/*  930 */     if ((state & 0xC) == 12) {
/*  931 */       state -= 12;
/*  932 */       state |= 0x20;
/*      */     }
/*      */     
/*  935 */     XWindow.sprite.hot.state = state;
/*      */   }
/*      */   
/*  938 */   public void keyReleased(KeyEvent e) { if ((this.xWindow == null) || (!this.xWindow.isMapped()))
/*  939 */       return;
/*  940 */     XClient XClient = this.xWindow.XClient;
/*  941 */     if (XClient == null) { return;
/*      */     }
/*  943 */     int kcode = Keymap.km.getCode(e);
/*  944 */     event.mkKeyRelease(kcode, this.xWindow.screen.rootId, this.xWindow.id, 0, XWindow.sprite.hot.x, XWindow.sprite.hot.y, XWindow.sprite.hot.x - this.xWindow.x, XWindow.sprite.hot.y - this.xWindow.y, XWindow.sprite.hot.state, 1);
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     try
/*      */     {
/*  954 */       if (XWindow.grab != null) {
/*  955 */         XWindow.sendGrabbedEvent(event, false, 1);
/*      */       } else {
/*  957 */         XWindow.sendDeviceEvent(this.xWindow, event, XWindow.grab, null, 1);
/*      */       }
/*      */     }
/*      */     catch (Exception ee) {}
/*      */     
/*  962 */     kcode = e.getKeyCode();
/*  963 */     int state = XWindow.sprite.hot.state;
/*  964 */     if (kcode == 17) {
/*  965 */       if ((state & 0x4) != 0) { state -= 4;
/*      */       }
/*  967 */       if ((state & 0x20) != 0) state -= 32;
/*      */     }
/*  969 */     else if ((kcode == 16) && (!clck_toggle)) {
/*  970 */       if ((state & 0x1) != 0) state--;
/*      */     }
/*  972 */     else if (kcode == 18) {
/*  973 */       if ((state & 0x8) != 0) state -= 8;
/*      */     }
/*  975 */     else if ((kcode == 21) && 
/*  976 */       ((state & 0x20) != 0)) { state -= 32;
/*      */     }
/*  978 */     XWindow.sprite.hot.state = state;
/*      */   }
/*      */   
/*      */ 
/*      */   public void keyTyped(KeyEvent e) {}
/*      */   
/*      */   public Image getImage()
/*      */   {
/*  986 */     if (this.offi == null) allocImage();
/*  987 */     return this.offi;
/*      */   }
/*      */   
/*      */   public Image getImage(GC gc, int x, int y, int w, int h) {
/*  991 */     Image i = getImage();
/*  992 */     if ((gc != null) && (gc.clip_mask != null) && ((gc.clip_mask instanceof ClipPixmap))) {
/*  993 */       TransparentFilter tf = new TransparentFilter(0, 0, (Pixmap)gc.clip_mask.getMask());
/*  994 */       i = Toolkit.getDefaultToolkit().createImage(new FilteredImageSource(i.getSource(), tf));
/*      */     }
/*      */     
/*  997 */     return i;
/*      */   }
/*      */   
/*      */   public Graphics getGraphics2() {
/* 1001 */     if (this.xWindow.clss == 2) {
/* 1002 */       return getGraphics();
/*      */     }
/* 1004 */     if (!isVisible()) {
/* 1005 */       return null;
/*      */     }
/*      */     
/* 1008 */     if (this.offg == null) allocImage();
/* 1009 */     Graphics g = this.offg;
/* 1010 */     return g;
/*      */   }
/*      */   
/*      */   public final Graphics getGraphics(GC gc, int mask) {
/* 1014 */     if (!isVisible()) {
/* 1015 */       return null;
/*      */     }
/* 1017 */     if (this.offg == null) allocImage();
/* 1018 */     Graphics graphics = this.offg;
/* 1019 */     if (((mask & 0x8000) != 0) && ((gc.attr & 0x400) != 0))
/*      */     {
/* 1021 */       graphics = getGraphics();
/* 1022 */       this.xWindow.currentGC = null;
/*      */     }
/*      */     else {
/* 1025 */       if ((gc == this.xWindow.currentGC) && (gc.time == this.xWindow.gctime) && ((mask & (this.xWindow.gmask ^ 0xFFFFFFFF)) == 0))
/*      */       {
/*      */ 
/*      */ 
/* 1029 */         return graphics;
/*      */       }
/* 1031 */       this.xWindow.gctime = gc.time;
/* 1032 */       this.xWindow.currentGC = gc;
/* 1033 */       this.xWindow.gmask = mask;
/*      */     }
/*      */     
/* 1036 */     if ((gc.clip_mask != null) && ((gc.clip_mask instanceof ClipRectangles))) {
/* 1037 */       Rectangle rec = (Rectangle)gc.clip_mask.getMask();
/* 1038 */       if (rec != null) {
/* 1039 */         graphics = this.offg;
/*      */       }
/*      */       
/* 1042 */       if ((rec != null) && ((rec.x != 0) || (rec.y != 0) || (rec.width != this.xWindow.width) || (rec.height != this.xWindow.height)))
/*      */       {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1048 */         graphics.setClip(rec.x, rec.y, rec.width, rec.height);
/*      */       }
/*      */     }
/*      */     
/* 1052 */     if ((mask & 0x1) != 0) {
/* 1053 */       Color color = this.xWindow.getColormap().getColor(gc.fgPixel);
/* 1054 */       if (gc.function == 6) {
/* 1055 */         this.xWindow.gmask &= 0xFFFFFFFE;
/* 1056 */         graphics.setXORMode(new Color((color.getRGB() ^ graphics.getColor().getRGB()) & 0xFFFFFF));
/*      */       }
/* 1058 */       else if (gc.function == 10) {
/* 1059 */         this.xWindow.gmask &= 0xFFFFFFFE;
/* 1060 */         graphics.setXORMode(this.xWindow.screen.defaultColormap.getColor(this.xWindow.background.pixel));
/*      */       }
/*      */       else {
/* 1063 */         graphics.setColor(color);
/*      */       }
/*      */     }
/*      */     
/* 1067 */     if ((mask & 0x4000) != 0) {
/* 1068 */       XFont XFont = gc.XFont;
/* 1069 */       graphics.setFont(XFont.getFont());
/*      */     }
/*      */     
/* 1072 */     if (((mask & 0x10) == 0) && ((mask & 0x20) == 0) && ((mask & 0x40) == 0) && ((mask & 0x80) != 0)) {}
/*      */     
/*      */ 
/*      */ 
/*      */ 
/* 1077 */     return graphics;
/*      */   }
/*      */   
/*      */   public void drawImage(Clip clip, Image img, int dx, int dy, int w, int h) {
/* 1081 */     if ((clip == null) || ((clip instanceof ClipPixmap))) {
/* 1082 */       drawImage(img, dx, dy, w, h);
/* 1083 */       return;
/*      */     }
/* 1085 */     ClipRectangles rclip = (ClipRectangles)clip;
/* 1086 */     Rectangle rec = rclip.masks[0];
/* 1087 */     if (this.offg == null) allocImage();
/* 1088 */     Shape tmp = this.offg.getClip();
/* 1089 */     if (tmp == null) {
/* 1090 */       tmp = new Rectangle(0, 0, this.xWindow.width, this.xWindow.height);
/*      */     }
/* 1092 */     this.offg.clipRect(rec.x, rec.y, rec.width, rec.height);
/* 1093 */     drawImage(img, dx, dy, w, h);
/* 1094 */     for (int i = 1; i < rclip.masks.length; i++) {
/* 1095 */       this.offg.setClip(tmp);
/* 1096 */       rec = rclip.masks[i];
/* 1097 */       this.offg.clipRect(rec.x, rec.y, rec.width, rec.height);
/* 1098 */       drawImage(img, dx, dy, w, h);
/*      */     }
/* 1100 */     this.offg.setClip(tmp);
/*      */   }
/*      */   
/*      */   public void drawImage(Image img, int dx, int dy, int w, int h) {
/* 1104 */     if (this.offg == null) allocImage();
/* 1105 */     this.offg.drawImage(img, dx, dy, w, h, ddxwindow);
/*      */   }
/*      */   
/* 1108 */   public void fillImage(Image img, int w, int h) { for (int i = 0; i < this.height; i += h) {
/* 1109 */       for (int j = 0; j < this.width; j += w) {
/* 1110 */         this.offg.drawImage(img, j, i, ddxwindow);
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
/* 1133 */     for (offx >= 0; offx < 0; offx += w) {}
/* 1134 */     for (offy >= 0; offy < 0; offy += h) {}
/* 1135 */     for (w >= offx; w < offx; offx -= w) {}
/* 1136 */     for (h >= offy; h < offy; offy -= h) {}
/* 1137 */     if ((offx == 0) && (offy == 0)) {
/* 1138 */       fillImage(img, w, h);
/* 1139 */       return;
/*      */     }
/* 1141 */     this.offg.drawImage(img, -offx, -offy, ddxwindow);
/* 1142 */     if (-offx + w < this.width) {
/* 1143 */       int i = -offx + w;
/* 1144 */       while (i < this.width) {
/* 1145 */         this.offg.drawImage(img, i, -offy, ddxwindow);
/* 1146 */         i += w;
/*      */       }
/*      */     }
/* 1149 */     if (-offy + h < this.height) {
/* 1150 */       int i = -offy + h;
/* 1151 */       while (i < this.height) {
/* 1152 */         this.offg.drawImage(img, -offx, i, ddxwindow);
/* 1153 */         i += h;
/*      */       }
/*      */     }
/*      */     
/* 1157 */     offx = -offx + w;offy = -offy + h;
/* 1158 */     if ((this.width <= offx) && (this.height <= offy)) { return;
/*      */     }
/* 1160 */     for (int i = offy; i < this.height; i += h) {
/* 1161 */       for (int j = offx; j < this.width; j += w) {
/* 1162 */         this.offg.drawImage(img, j, i, ddxwindow);
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
/*      */   public void copyArea(XWindow dst, GC gc, int srcx, int srcy, int width, int height, int destx, int desty)
/*      */   {
/* 1188 */     Graphics g = dst.getGraphics();
/* 1189 */     if (g == null) { return;
/*      */     }
/* 1191 */     if (this.xWindow == dst) {
/* 1192 */       copyArea(srcx, srcy, width, height, destx - srcx, desty - srcy);
/* 1193 */       dst.draw(destx, desty, width, height);
/* 1194 */       return;
/*      */     }
/*      */     
/* 1197 */     Image img = this.xWindow.getImage(gc, srcx, srcy, width, height);
/* 1198 */     if ((srcx == 0) && (srcy == 0) && (width == this.xWindow.width) && (height == this.xWindow.height)) {
/* 1199 */       dst.ddxwindow.drawImage(gc.clip_mask, img, destx, desty, width, height);
/*      */     }
/*      */     else {
/* 1202 */       Shape tmp = g.getClip();
/* 1203 */       g.clipRect(destx, desty, width, height);
/* 1204 */       dst.ddxwindow.drawImage(gc.clip_mask, img, destx - srcx, desty - srcy, this.xWindow.width, this.xWindow.height);
/*      */       
/* 1206 */       if (tmp == null) g.setClip(0, 0, dst.width, dst.height); else
/* 1207 */         g.setClip(tmp);
/*      */     }
/* 1209 */     dst.draw(destx, desty, width, height);
/* 1210 */     if (img != this.xWindow.getImage()) {
/* 1211 */       img.flush();
/*      */     }
/*      */   }
/*      */   
/*      */   public void copyArea(int sx, int sy, int w, int h, int dx, int dy) {
/* 1216 */     if (this.offg == null) allocImage();
/* 1217 */     if (((dx <= 0) || (w <= dx)) && ((dy <= 0) || (h <= dy))) {
/* 1218 */       this.offg.copyArea(sx, sy, w, h, dx, dy);
/*      */ 
/*      */     }
/* 1221 */     else if ((0 < dy) && (dy < h)) {
/* 1222 */       int ssy = sy + h - dy;
/*      */       
/* 1224 */       while (ssy >= sy) {
/* 1225 */         this.offg.copyArea(sx, ssy, w, dy, dx, dy);
/* 1226 */         ssy -= dy;
/*      */       }
/* 1228 */       ssy += dy;
/* 1229 */       if (sy < ssy) this.offg.copyArea(sx, sy, w, ssy - sy, dx, dy);
/*      */     }
/* 1231 */     else if ((0 < dx) && (dx < w)) {
/* 1232 */       int ssx = sx + w - dx;
/*      */       
/* 1234 */       while (ssx >= sx) {
/* 1235 */         this.offg.copyArea(ssx, sy, dx, h, dx, dy);
/* 1236 */         ssx -= dx;
/*      */       }
/* 1238 */       ssx += dx;
/* 1239 */       if (sx < ssx) this.offg.copyArea(sx, sy, ssx - sx, h, dx, dy);
/*      */     }
/*      */   }
/*      */   
/*      */   public void delete() throws IOException
/*      */   {
/* 1245 */     if (this.offi != null) {
/* 1246 */       this.offi.flush();
/* 1247 */       this.offi = null;
/*      */     }
/* 1249 */     if (this.offg != null) {
/* 1250 */       this.offg.dispose();
/* 1251 */       this.offg = null;
/*      */     }
/* 1253 */     this.xWindow = null;
/* 1254 */     Container tmp = getParent();
/* 1255 */     if (tmp != null) tmp.remove(this);
/*      */   }
/*      */   
/*      */   public void restoreClip() {
/* 1259 */     if (this.offg != null) this.offg.setClip(0, 0, this.xWindow.width, this.xWindow.height);
/*      */   }
/*      */   
/*      */   public XWindow getWindow() {
/* 1263 */     return this.xWindow;
/*      */   }
/*      */ }


/* Location:              C:\Users\Joey\Downloads\launch.jar!\com\emt\proteus\xserver\display\XComponentImpSwing.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */