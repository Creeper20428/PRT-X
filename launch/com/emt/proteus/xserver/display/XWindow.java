/*      */ package com.emt.proteus.xserver.display;
/*      */ 
/*      */ import com.emt.proteus.xserver.client.Clients;
/*      */ import com.emt.proteus.xserver.client.OtherClients;
/*      */ import com.emt.proteus.xserver.client.XClient;
/*      */ import com.emt.proteus.xserver.io.ComChannel;
/*      */ import com.emt.proteus.xserver.protocol.Atom;
/*      */ import java.awt.Component;
/*      */ import java.awt.Frame;
/*      */ import java.awt.Graphics;
/*      */ import java.awt.Image;
/*      */ import java.awt.Insets;
/*      */ import java.awt.Point;
/*      */ import java.awt.Rectangle;
/*      */ import java.awt.Shape;
/*      */ import java.awt.Window;
/*      */ import java.awt.event.ComponentAdapter;
/*      */ import java.awt.event.ComponentEvent;
/*      */ import java.awt.event.MouseListener;
/*      */ import java.awt.event.WindowAdapter;
/*      */ import java.awt.event.WindowEvent;
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
/*      */ 
/*      */ 
/*      */ public class XWindow
/*      */   extends Drawable
/*      */ {
/*   37 */   public static Object LOCK = XWindow.class;
/*      */   
/*      */   private static final int CWBackPixmap = 1;
/*      */   
/*      */   private static final int CWBackPixel = 2;
/*      */   
/*      */   private static final int CWBorderPixmap = 4;
/*      */   
/*      */   private static final int CWBorderPixel = 8;
/*      */   
/*      */   private static final int CWBitGravity = 16;
/*      */   
/*      */   private static final int CWWinGravity = 32;
/*      */   
/*      */   private static final int CWBackingStore = 64;
/*      */   
/*      */   private static final int CWBackingPlanes = 128;
/*      */   
/*      */   private static final int CWBackingPixel = 256;
/*      */   
/*      */   private static final int CWOverrideRedirect = 512;
/*      */   
/*      */   private static final int CWSaveUnder = 1024;
/*      */   
/*      */   private static final int CWEventMask = 2048;
/*      */   
/*      */   private static final int CWDontPropagate = 4096;
/*      */   private static final int CWColormap = 8192;
/*      */   private static final int CWCursor = 16384;
/*      */   private static final int CWX = 1;
/*      */   private static final int CWY = 2;
/*      */   private static final int CWWidth = 4;
/*      */   private static final int CWHeight = 8;
/*      */   private static final int CWBorderWidth = 16;
/*      */   private static final int CWSibling = 32;
/*      */   private static final int CWStackMode = 64;
/*      */   private static final int ChangeMask = 15;
/*      */   private static final int IllegalInputOnlyConfigureMask = 16;
/*      */   private static final int CopyFromParent = 0;
/*      */   private static final int InputOutput = 1;
/*      */   private static final int InputOnly = 2;
/*      */   private static final int NotifyNormal = 0;
/*      */   private static final int NotifyGrab = 1;
/*      */   private static final int NotifyUngrab = 2;
/*      */   private static final int NotifyWhileGrabbed = 3;
/*      */   private static final int NotifyAncestor = 0;
/*      */   private static final int NotifyVirtual = 1;
/*      */   private static final int NotifyInferior = 2;
/*      */   private static final int NotifyNonlinear = 3;
/*      */   private static final int NotifyNonlinearVirtual = 4;
/*      */   private static final int NotifyPointer = 5;
/*      */   private static final int NotifyPointerRoot = 6;
/*      */   private static final int NotifyDetailNone = 7;
/*      */   private static final int RevertToNone = 0;
/*      */   private static final int RevertToPointerRoot = 1;
/*      */   private static final int RevertToParent = 2;
/*      */   private static final int NoneWin = 0;
/*      */   private static final int PointerRootWin = 1;
/*      */   private static final int PointerRoot = 1;
/*      */   private static final int FollowKeyboard = 3;
/*      */   private static final int FollowKeyboardWin = 3;
/*      */   private static final int AsyncPointer = 0;
/*      */   private static final int SyncPointer = 1;
/*      */   private static final int ReplayPointer = 2;
/*      */   private static final int AsyncKeyboard = 3;
/*      */   private static final int SyncKeyboard = 4;
/*      */   private static final int ReplayKeyboard = 5;
/*      */   private static final int AsyncBoth = 6;
/*      */   private static final int SyncBoth = 7;
/*      */   private static final int GrabModeSync = 0;
/*      */   private static final int GrabModeAsync = 1;
/*      */   private static final int GrabSuccess = 0;
/*      */   private static final int AlreadyGrabbed = 1;
/*      */   private static final int GrabInvalidTime = 2;
/*      */   private static final int GrabNotViewable = 3;
/*      */   private static final int GrabFrozen = 4;
/*  113 */   public static int deltaSaveUndersViewable = 0;
/*  114 */   public static int defaultBackingStore = 0;
/*      */   
/*  116 */   public static int AnyModifier = 32768;
/*  117 */   public static int AnyKey = 0;
/*      */   
/*      */   private static final int Above = 0;
/*      */   
/*      */   private static final int Below = 1;
/*      */   
/*      */   private static final int TopIf = 2;
/*      */   
/*      */   private static final int BottomIf = 3;
/*      */   
/*      */   private static final int Opposite = 4;
/*      */   
/*      */   private static final int Restack = 0;
/*      */   
/*      */   private static final int Move = 1;
/*      */   
/*      */   private static final int Resize = 2;
/*      */   private static final int Reborder = 3;
/*      */   private static final int NOT_GRABBED = 0;
/*      */   private static final int THAWED = 1;
/*      */   private static final int THAWED_BOTH = 2;
/*      */   private static final int FREEZE_NEXT_EVENT = 3;
/*      */   private static final int FREEZE_BOTH_NEXT_EVENT = 4;
/*      */   private static final int FROZEN = 5;
/*      */   private static final int FROZEN_NO_EVENT = 5;
/*      */   private static final int FROZEN_WITH_EVENT = 6;
/*      */   private static final int THAW_OTHERS = 7;
/*      */   private static final int backgroundStateOffset = 0;
/*      */   private static final int backgroundState = 3;
/*      */   private static final int ParentRelative = 1;
/*      */   private static final int BackgroundPixel = 2;
/*      */   private static final int BackgroundPixmap = 3;
/*      */   private static final int borderIsPixelOffset = 2;
/*      */   private static final int borderIsPixel = 4;
/*      */   private static final int cursorIsNoneOffset = 3;
/*      */   private static final int cursorIsNone = 8;
/*      */   private static final int backingStoreOffset = 4;
/*      */   private static final int backingStore = 48;
/*      */   private static final int saveUnderOffset = 6;
/*      */   private static final int saveUnder = 64;
/*      */   private static final int DIXsaveUnderOffset = 7;
/*      */   private static final int DIXsaveUnder = 128;
/*      */   private static final int bitGravityOffset = 8;
/*      */   private static final int bitGravity = 3840;
/*      */   private static final int ForgetGravity = 0;
/*      */   private static final int winGravityOffset = 12;
/*      */   private static final int winGravity = 61440;
/*      */   private static final int ForegetGravity = 0;
/*      */   private static final int NorthWestGravity = 1;
/*      */   private static final int NorthGravity = 2;
/*      */   private static final int NorthEastGravity = 3;
/*      */   private static final int WestGravity = 4;
/*      */   private static final int CenterGravity = 5;
/*      */   private static final int EastGravity = 6;
/*      */   private static final int SouthWestGravity = 7;
/*      */   private static final int SouthGravity = 8;
/*      */   private static final int SouthEastGravity = 9;
/*      */   private static final int StaticGravity = 10;
/*      */   private static final int UnmapGravity = 0;
/*      */   private static final int overrideRedirectOffset = 16;
/*      */   private static final int overrideRedirect = 65536;
/*      */   private static final int visibilityOffset = 17;
/*      */   private static final int visibility = 393216;
/*      */   private static final int VisibilityUnobscured = 0;
/*      */   private static final int VisibilityPartiallyObscured = 1;
/*      */   private static final int VisibilityFullyObscured = 2;
/*      */   private static final int VisibilityNotViewable = 3;
/*      */   private static final int mappedOffset = 19;
/*      */   private static final int mapped = 524288;
/*      */   private static final int realizedOffset = 20;
/*      */   private static final int realized = 1048576;
/*      */   private static final int viewableOffset = 21;
/*      */   private static final int viewable = 2097152;
/*      */   private static final int dontPropagateOffset = 22;
/*      */   private static final int dontPropagate = 29360128;
/*      */   private static final int forcedBSOffset = 25;
/*      */   private static final int forcedBS = 33554432;
/*      */   private static final int DBE_FRONT_BUFFER = 1;
/*      */   private static final int DBE_BACK_BUFFER = 0;
/*      */   private static final int dstBufferOffset = 26;
/*      */   private static final int dstBuffer = 67108864;
/*      */   private static final int srcBufferOffset = 27;
/*      */   private static final int srcBuffer = 134217728;
/*      */   private static final int PlaceOnTop = 0;
/*      */   private static final int PlaceOnBottom = 1;
/*      */   private static final int RaiseLowest = 0;
/*      */   private static final int LowerHighest = 1;
/*  204 */   public static final Focus focus = new Focus();
/*  205 */   public static Sprite sprite = new Sprite();
/*  206 */   public static int spriteTraceGood = 1;
/*  207 */   public static XWindow[] spriteTrace = new XWindow[10];
/*  208 */   public static Point gpoint = new Point();
/*  209 */   public static Grab grab = null;
/*      */   
/*  211 */   public static Class dDXWindow = XAlphaPanel.class;
/*      */   
/*      */   XClient XClient;
/*      */   
/*      */   XWindow parent;
/*      */   XWindow nextSib;
/*      */   XWindow prevSib;
/*      */   XWindow firstChild;
/*      */   XWindow lastChild;
/*  220 */   Point origin = new Point();
/*      */   int borderWidth;
/*      */   int deliverableEvents;
/*      */   int eventMask;
/*  224 */   Pix background = new Pix();
/*  225 */   Pix border = new Pix();
/*      */   
/*      */   public WindowOpt optional;
/*      */   int attr;
/*      */   XComponent ddxwindow;
/*  230 */   int frame_x = -1;
/*  231 */   int frame_y = -1;
/*  232 */   int frame_width = -1;
/*  233 */   int frame_height = -1;
/*      */   
/*      */   XWindow(int id) {
/*  236 */     super(id, -1073741823);
/*      */   }
/*      */   
/*      */ 
/*      */   XWindow(int wid, XWindow prnt, int x, int y, int width, int height, int bwidth, int clss, byte depth, XClient XClient, int visual, int msk)
/*      */     throws IOException
/*      */   {
/*  243 */     this(wid);
/*      */     
/*  245 */     this.XClient = XClient;
/*  246 */     this.parent = prnt;
/*  247 */     this.screen = prnt.screen;
/*      */     
/*  249 */     if (clss == 0) clss = prnt.clss;
/*  250 */     this.clss = clss;
/*      */     
/*  252 */     if ((clss != 1) && (clss != 2)) {
/*  253 */       XClient.errorValue = clss;
/*  254 */       XClient.errorReason = 2;
/*  255 */       return;
/*      */     }
/*      */     
/*  258 */     if ((clss != 2) && (prnt.clss == 2)) {
/*  259 */       XClient.errorValue = clss;
/*  260 */       XClient.errorReason = 8;
/*  261 */       return;
/*      */     }
/*  263 */     if ((clss == 2) && ((bwidth != 0) || (depth != 0))) {
/*  264 */       XClient.errorValue = 0;
/*  265 */       XClient.errorReason = 8;
/*  266 */       return;
/*      */     }
/*      */     
/*  269 */     if ((clss == 1) && (depth == 0)) {
/*  270 */       depth = prnt.depth;
/*      */     }
/*      */     
/*  273 */     WindowOpt opt = prnt.optional;
/*  274 */     if (opt == null) {
/*  275 */       opt = prnt.findOptional().optional;
/*      */     }
/*  277 */     if (visual == 0) {
/*  278 */       visual = opt.visual;
/*      */     }
/*  280 */     if ((visual != opt.visual) || (depth != prnt.depth)) {
/*  281 */       boolean foo = false;
/*      */       
/*  283 */       for (int i = 0; i < this.screen.depth.length; i++) {
/*  284 */         Depth pdepth = this.screen.depth[i];
/*  285 */         if (((depth == pdepth.depth) || (depth == 0)) && 
/*  286 */           (pdepth.visual != null)) {
/*  287 */           for (int j = 0; j < pdepth.visual.length; j++) {
/*  288 */             if (visual == pdepth.visual[j].id) {
/*  289 */               foo = true;
/*  290 */               break;
/*      */             }
/*      */           }
/*      */         }
/*      */       }
/*      */       
/*  296 */       if (!foo) {
/*  297 */         XClient.errorValue = 0;
/*  298 */         XClient.errorReason = 8;
/*  299 */         return;
/*      */       }
/*      */     }
/*  302 */     if (((msk & 0xC) == 0) && (clss != 2) && (depth != prnt.depth))
/*      */     {
/*      */ 
/*  305 */       XClient.errorValue = 0;
/*  306 */       XClient.errorReason = 8;
/*  307 */       return;
/*      */     }
/*      */     
/*  310 */     this.depth = depth;
/*  311 */     if (depth == prnt.depth) {
/*  312 */       this.bitsPerPixel = prnt.bitsPerPixel;
/*      */     }
/*      */     else {
/*  315 */       int ii = 0;
/*  316 */       while ((ii < Format.format.length) && 
/*  317 */         (Format.format[ii].depth != this.screen.rootDepth)) {
/*  318 */         ii++;
/*      */       }
/*  320 */       if (ii == Format.format.length)
/*      */       {
/*  322 */         ii = 0;
/*      */       }
/*  324 */       this.bitsPerPixel = Format.format[ii].bpp;
/*      */     }
/*      */     
/*  327 */     this.type = prnt.type;
/*  328 */     if (clss == 2) { this.type = 2;
/*      */     }
/*  330 */     setDefault();
/*      */     
/*  332 */     if (visual != opt.visual) {
/*  333 */       makeOptional();
/*  334 */       this.optional.visual = visual;
/*  335 */       this.optional.colormap = this.screen.defaultColormap;
/*      */     }
/*  337 */     this.borderWidth = bwidth;
/*  338 */     this.attr &= 0xFFFFFFFC;
/*  339 */     this.attr &= 0xFFFFFFFB;
/*  340 */     this.attr |= prnt.attr & 0x4;
/*      */     
/*  342 */     this.border = prnt.border.dup();
/*      */     
/*  344 */     if ((this.attr & 0x4) == 0) {
/*  345 */       this.border.pixmap.ref();
/*      */     }
/*  347 */     this.origin.x = ((short)(x + bwidth));
/*  348 */     this.origin.y = ((short)(y + bwidth));
/*  349 */     this.width = width;
/*  350 */     this.height = height;
/*  351 */     this.x = ((short)(prnt.x + x + bwidth));
/*  352 */     this.y = ((short)(prnt.y + y + bwidth));
/*      */     
/*  354 */     synchronized (LOCK) {
/*  355 */       this.nextSib = prnt.firstChild;
/*  356 */       if (prnt.firstChild != null) prnt.firstChild.prevSib = this; else
/*  357 */         prnt.lastChild = this;
/*  358 */       prnt.firstChild = this;
/*      */     }
/*      */     
/*  361 */     if ((msk & 0x800) == 0) {
/*  362 */       recalculateDeliverableEvents();
/*      */     }
/*      */     
/*  365 */     msk &= 0x7FFF;
/*  366 */     if (msk != 0) {
/*  367 */       changeAttr(XClient, msk);
/*      */     }
/*      */     
/*  370 */     if (XClient.errorReason != 0) {
/*  371 */       delete();
/*  372 */       return;
/*      */     }
/*      */     
/*  375 */     if (((msk & 0x40) != 0) && (defaultBackingStore != 0))
/*      */     {
/*  377 */       this.attr &= 0xFFFFFFCF;
/*  378 */       this.attr |= defaultBackingStore << 4;
/*  379 */       this.attr |= 0x2000000;
/*      */     }
/*      */     try
/*      */     {
/*  383 */       this.ddxwindow = ((XComponent)dDXWindow.newInstance());
/*  384 */     } catch (Exception e) { System.err.println(e); }
/*  385 */     this.ddxwindow.init(XDisplay.safe(null), this);
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     try
/*      */     {
/*  429 */       this.ddxwindow.setLocation(this.origin.x - this.borderWidth + this.parent.borderWidth, this.origin.y - this.borderWidth + this.parent.borderWidth);
/*      */       
/*  431 */       prnt.ddxwindow.add((Component)this.ddxwindow, 0);
/*      */       
/*  433 */       if ((this.attr & 0x8) == 0) {
/*  434 */         Cursor cur = getCursor();
/*  435 */         if (cur != null) {
/*  436 */           this.ddxwindow.setCursor(cur.cursor);
/*      */         }
/*      */       }
/*  439 */       if (bwidth > 0) {
/*  440 */         this.ddxwindow.setBorderPixmap(this.border.pixmap);
/*      */       }
/*      */     }
/*      */     catch (Exception ee) {}
/*      */     
/*      */ 
/*      */ 
/*  447 */     if (prnt.subSend()) {
/*  448 */       XClient.cevent.mkCreateNotify(prnt.id, this.id, x, y, width, height, this.borderWidth, (this.attr & 0x10000) != 0 ? 0 : 1);
/*      */       
/*      */ 
/*  451 */       prnt.sendEvent(XClient.cevent, 1, null);
/*      */     }
/*      */   }
/*      */   
/*      */   private final void changeAttr(XClient c, int vmask) throws IOException
/*      */   {
/*  457 */     boolean borderRelative = false;
/*  458 */     int index = 0;
/*  459 */     int mask = vmask;
/*  460 */     ComChannel comChannel = c.channel;
/*      */     
/*  462 */     while ((mask != 0) && (c.length != 0)) {
/*  463 */       index = lowbit(mask);
/*  464 */       mask &= (index ^ 0xFFFFFFFF);
/*  465 */       c.length -= 1;
/*  466 */       int foo; switch (index) {
/*      */       case 1: 
/*  468 */         foo = comChannel.readInt();
/*  469 */         if ((this.attr & 0x3) == 1) borderRelative = true;
/*  470 */         if (foo == 0) {
/*  471 */           if (((this.attr & 0x3) == 3) && 
/*  472 */             (this.background.pixmap != null))
/*      */           {
/*      */             try
/*      */             {
/*  476 */               this.background.pixmap.delete(); } catch (Exception e) {}
/*  477 */             this.background.pixmap = null;
/*      */           }
/*      */           
/*  480 */           this.attr &= 0xFFFFFFFC;
/*      */         }
/*  482 */         else if (foo == 1) {
/*  483 */           if (((this.parent == null) || (this.depth == this.parent.depth)) || (
/*      */           
/*  485 */             ((this.attr & 0x3) == 3) && 
/*  486 */             (this.background.pixmap != null)))
/*      */           {
/*      */             try
/*      */             {
/*  490 */               this.background.pixmap.delete(); } catch (Exception e) {}
/*  491 */             this.background.pixmap = null;
/*      */           }
/*      */           
/*  494 */           if (this.parent != null)
/*      */           {
/*      */ 
/*  497 */             this.attr = (this.attr & 0xFFFFFFFC | 0x1);
/*      */           }
/*  499 */           borderRelative = true;
/*      */         }
/*      */         else {
/*  502 */           Pixmap pixmap = (Pixmap)lookupIDByType(foo, -1073741822);
/*      */           
/*  504 */           if (pixmap != null) {
/*  505 */             if (((pixmap.depth != this.depth) || (pixmap.screen == this.screen)) || (
/*      */             
/*      */ 
/*      */ 
/*  509 */               ((this.attr & 0x3) == 3) && 
/*  510 */               (this.background.pixmap != null)))
/*      */             {
/*      */               try
/*      */               {
/*  514 */                 this.background.pixmap.delete(); } catch (Exception e) {}
/*  515 */               this.background.pixmap = null;
/*      */             }
/*      */             
/*  518 */             this.attr = (this.attr & 0xFFFFFFFC | 0x3);
/*  519 */             this.background.pixmap = pixmap;
/*  520 */             this.background.img = pixmap.getImage(this);
/*      */             
/*  522 */             pixmap.ref();
/*      */           }
/*      */         }
/*      */         
/*      */ 
/*      */ 
/*  528 */         break;
/*      */       case 2: 
/*  530 */         foo = comChannel.readInt();
/*  531 */         if ((this.attr & 0x3) == 1) {
/*  532 */           borderRelative = true;
/*      */         }
/*  534 */         if (((this.attr & 0x3) == 3) && 
/*  535 */           (this.background.pixmap != null))
/*      */         {
/*      */           try
/*      */           {
/*  539 */             this.background.pixmap.delete(); } catch (Exception e) {}
/*  540 */           this.background.pixmap = null;
/*      */         }
/*      */         
/*  543 */         this.attr &= 0xFFFFFFFC;
/*  544 */         this.attr |= 0x2;
/*  545 */         this.background.pixel = foo;
/*  546 */         break;
/*      */       case 4: 
/*  548 */         foo = comChannel.readInt();
/*  549 */         if (foo == 0) {
/*  550 */           if (((this.parent == null) || (this.depth == this.parent.depth)) || (
/*      */           
/*      */ 
/*  553 */             ((this.attr & 0x4) == 0) && 
/*  554 */             (this.border.pixmap != null)))
/*      */           {
/*      */             try
/*      */             {
/*  558 */               this.border.pixmap.delete(); } catch (Exception e) {}
/*  559 */             this.border.pixmap = null;
/*      */           }
/*      */           
/*      */ 
/*  563 */           this.border = this.parent.border.dup();
/*  564 */           this.attr &= 0xFFFFFFFB;
/*  565 */           this.attr |= this.parent.attr & 0x4;
/*  566 */           if ((this.attr & 0x4) == 0)
/*      */           {
/*      */ 
/*  569 */             if (this.border.pixmap != null)
/*      */             {
/*      */ 
/*      */ 
/*  573 */               this.border.pixmap.ref();
/*      */             }
/*      */           }
/*      */         }
/*      */         else {
/*  578 */           Pixmap pixmap = (Pixmap)lookupIDByType(foo, -1073741822);
/*  579 */           if (pixmap != null) {
/*  580 */             if (((pixmap.depth != this.depth) || (pixmap.screen == this.screen)) || (
/*      */             
/*      */ 
/*  583 */               ((this.attr & 0x4) == 0) && 
/*  584 */               (this.border.pixmap != null)))
/*      */             {
/*      */               try
/*      */               {
/*  588 */                 this.border.pixmap.delete(); } catch (Exception e) {}
/*  589 */               this.border.pixmap = null;
/*      */             }
/*      */             
/*  592 */             this.attr &= 0xFFFFFFFB;
/*  593 */             this.border.pixmap = pixmap;
/*  594 */             pixmap.ref();
/*      */           }
/*      */         }
/*      */         
/*      */ 
/*      */ 
/*  600 */         break;
/*      */       case 8: 
/*  602 */         foo = comChannel.readInt();
/*  603 */         if (((this.attr & 0x4) == 0) && 
/*  604 */           (this.border.pixmap != null))
/*      */         {
/*      */           try
/*      */           {
/*  608 */             this.border.pixmap.delete(); } catch (Exception e) {}
/*  609 */           this.border.pixmap = null;
/*      */         }
/*      */         
/*  612 */         this.attr |= 0x4;
/*  613 */         this.border.pixel = foo;
/*  614 */         break;
/*      */       case 16: 
/*  616 */         foo = comChannel.readInt();
/*  617 */         foo &= 0xFF;
/*      */         
/*  619 */         if (foo > 10) {}
/*      */         
/*      */ 
/*  622 */         this.attr &= 0xF0FF;
/*  623 */         this.attr |= foo << 8;
/*      */         
/*  625 */         break;
/*      */       case 32: 
/*  627 */         foo = comChannel.readInt();
/*  628 */         foo &= 0xFF;
/*      */         
/*  630 */         if (foo > 10) {}
/*      */         
/*      */ 
/*  633 */         this.attr &= 0xFFFF0FFF;
/*  634 */         this.attr |= foo << 12;
/*      */         
/*  636 */         break;
/*      */       case 64: 
/*  638 */         foo = comChannel.readInt();
/*  639 */         foo &= 0xFF;
/*      */         
/*  641 */         if ((foo != 0) && (foo != 1) && (foo != 2)) {}
/*      */         
/*      */ 
/*  644 */         this.attr &= 0xFFFFFFCF;
/*  645 */         this.attr |= foo << 4;
/*  646 */         this.attr &= 0xFDFFFFFF;
/*  647 */         break;
/*      */       case 128: 
/*  649 */         foo = comChannel.readInt();
/*  650 */         if ((this.optional != null) || (foo != -1)) {
/*  651 */           makeOptional();
/*  652 */           this.optional.backingBitPlanes = foo;
/*      */         }
/*      */         break;
/*      */       case 256: 
/*  656 */         foo = comChannel.readInt();
/*  657 */         if ((this.optional != null) || (foo != 0)) {
/*  658 */           makeOptional();
/*  659 */           this.optional.backingPixel = foo;
/*      */         }
/*      */         break;
/*      */       case 512: 
/*  663 */         foo = comChannel.readInt();
/*  664 */         foo &= 0xFF;
/*  665 */         if ((foo != 0) && (foo != 1)) {}
/*      */         
/*      */ 
/*      */ 
/*  669 */         this.attr &= 0xFFFEFFFF;
/*  670 */         this.attr |= foo << 16;
/*  671 */         break;
/*      */       case 1024: 
/*  673 */         foo = comChannel.readInt();
/*  674 */         foo &= 0xFF;
/*      */         
/*  676 */         if (((foo == 0) || (foo == 1)) || (
/*      */         
/*      */ 
/*  679 */           (this.parent != null) && ((this.attr & 0x40) != foo << 6) && ((this.attr & 0x200000) != 0)))
/*      */         {
/*  681 */           if ((this.attr & 0x40) != 0) deltaSaveUndersViewable -= 1; else
/*  682 */             deltaSaveUndersViewable += 1;
/*  683 */           this.attr &= 0xFFFFFFBF;
/*  684 */           this.attr |= foo << 6;
/*  685 */           if (this.firstChild == null) {
/*      */             break;
/*      */           }
/*      */         }
/*      */         else
/*      */         {
/*  691 */           this.attr &= 0xFFFFFFBF;
/*  692 */           this.attr |= foo << 6;
/*      */         }
/*  694 */         break;
/*      */       case 2048: 
/*  696 */         foo = comChannel.readInt();
/*  697 */         setEventMask(c, foo);
/*  698 */         break;
/*      */       case 4096: 
/*  700 */         foo = comChannel.readInt();
/*  701 */         eventSuppress(foo);
/*  702 */         break;
/*      */       case 8192: 
/*  704 */         foo = comChannel.readInt();
/*  705 */         if ((foo == 0) && 
/*  706 */           (this.parent != null)) { if (this.optional != null) { if (this.optional.visual != (this.parent.optional != null ? this.parent.optional.visual : this.parent.findOptional().optional.visual)) {}
/*      */ 
/*      */           }
/*      */           else
/*      */           {
/*  711 */             foo = this.parent.getColormap().id;
/*      */           }
/*      */         }
/*  714 */         if (foo == 0) {
/*  715 */           c.errorReason = 8;
/*  716 */           c.errorValue = foo;
/*  717 */           return;
/*      */         }
/*  719 */         Colormap cmap = (Colormap)lookupIDByType(foo, 6);
/*  720 */         if (cmap == null) {
/*  721 */           c.errorReason = 12;
/*  722 */           c.errorValue = foo;
/*  723 */           return;
/*      */         }
/*  725 */         makeOptional();
/*  726 */         this.optional.colormap = cmap;
/*  727 */         c.cevent.mkColormapNotify(this.id, foo, 1, 1);
/*  728 */         sendEvent(c.cevent, 1, null);
/*  729 */         break;
/*      */       case 16384: 
/*  731 */         foo = comChannel.readInt();
/*  732 */         Cursor cur = null;Cursor old = null;
/*  733 */         if (foo == 0) {
/*  734 */           if (this == this.screen.root) cur = Cursor.rootCursor; else {
/*  735 */             cur = null;
/*      */           }
/*      */         } else {
/*  738 */           cur = (Cursor)lookupIDByType(foo, 5);
/*  739 */           if (cur == null) {
/*  740 */             c.errorReason = 6;
/*  741 */             c.errorValue = foo;
/*  742 */             return;
/*      */           }
/*      */         }
/*  745 */         if (cur != getCursor()) {
/*  746 */           if (cur == null) {
/*  747 */             this.attr |= 0x8;
/*  748 */             if (this.optional != null) {
/*  749 */               old = this.optional.cursor;
/*  750 */               this.optional.cursor = null;
/*      */             }
/*      */           }
/*      */           else {
/*  754 */             makeOptional();
/*  755 */             old = this.optional.cursor;
/*  756 */             this.optional.cursor = cur;
/*  757 */             this.attr &= 0xFFFFFFF7;
/*  758 */             if (this.ddxwindow != null) {
/*  759 */               this.ddxwindow.setCursor(cur.cursor);
/*      */             }
/*      */           }
/*      */         }
/*      */         break;
/*      */       default: 
/*  765 */         c.length += 1;
/*  766 */         c.errorValue = vmask;
/*  767 */         c.errorReason = 2;
/*      */       }
/*      */       
/*      */       
/*  771 */       if (c.errorReason != 0) {
/*  772 */         return;
/*      */       }
/*      */     }
/*      */     
/*  776 */     if (c.length != 0) {
/*  777 */       c.errorValue = vmask;
/*  778 */       c.errorReason = 2;
/*      */     }
/*      */   }
/*      */   
/*      */   private int eventSuppress(int mask)
/*      */   {
/*  784 */     int i = 0;
/*  785 */     if ((this.attr & 0x1C00000) != 0) {
/*  786 */       DontPropagate.refc[((this.attr & 0x1C00000) >> 22)] -= 1;
/*      */     }
/*  788 */     if (mask != 0) {
/*  789 */       i = DontPropagate.store(mask);
/*      */     }
/*  791 */     if ((i != 0) || (mask == 0)) {
/*  792 */       this.attr &= 0xFE3FFFFF;
/*  793 */       this.attr |= i << 22;
/*  794 */       if (i != 0) {
/*  795 */         DontPropagate.refc[i] += 1;
/*      */       }
/*  797 */       if (this.optional != null) {
/*  798 */         this.optional.dontPropagateMask = mask;
/*      */       }
/*      */     }
/*      */     else {
/*  802 */       makeOptional();
/*  803 */       this.attr &= 0xFE3FFFFF;
/*  804 */       this.optional.dontPropagateMask = mask;
/*      */     }
/*  806 */     recalculateDeliverableEvents();
/*  807 */     return 0;
/*      */   }
/*      */   
/*      */   public static final void reqCirculateWindow(XClient c) throws IOException
/*      */   {
/*  812 */     ComChannel comChannel = c.channel;
/*      */     
/*  814 */     int direction = c.data;
/*  815 */     int foo = comChannel.readInt();
/*      */     
/*  817 */     c.length -= 2;
/*      */     
/*  819 */     XWindow w = c.lookupWindow(foo);
/*  820 */     if (w == null) {
/*  821 */       c.errorValue = foo;
/*  822 */       c.errorReason = 3;
/*  823 */       return;
/*      */     }
/*  825 */     if ((direction != 0) && (direction != 1)) {
/*  826 */       c.errorValue = direction;
/*  827 */       c.errorReason = 2;
/*  828 */       return;
/*      */     }
/*      */     
/*  831 */     synchronized (LOCK) {
/*  832 */       XWindow win = null;
/*  833 */       XWindow first = w.firstChild;
/*  834 */       if (direction == 0) {
/*  835 */         win = w.lastChild;
/*  836 */         while ((win != null) && (!win.isMapped())) win = win.prevSib;
/*  837 */         if (win != null) {}
/*      */       }
/*      */       else {
/*  840 */         win = first;
/*  841 */         while ((win != null) && (!win.isMapped())) win = win.nextSib;
/*  842 */         if (win == null) return;
/*      */       }
/*  844 */       c.cevent.mkCirculateRequest(w.id, win.id, direction == 0 ? 0 : 1);
/*      */       
/*      */ 
/*  847 */       if ((w.redirectSend()) && 
/*  848 */         (w.sendEvent(c.cevent, 1, 1048576, c) == 1)) {
/*  849 */         return;
/*      */       }
/*  851 */       c.cevent.mkCirculateNotify(win.id, w.id, w.id, direction == 0 ? 0 : 1);
/*      */       
/*      */ 
/*  854 */       win.sendEvent(c.cevent, 1, null);
/*  855 */       win.reflectStackChange(direction == 0 ? first : null);
/*      */     }
/*      */   }
/*      */   
/*      */   public static final void reqAllowEvents(XClient c)
/*      */     throws IOException
/*      */   {
/*  862 */     ComChannel comChannel = c.channel;
/*  863 */     int mode = c.data;
/*  864 */     int foo = comChannel.readInt();
/*  865 */     switch (mode)
/*      */     {
/*      */     case 0: 
/*      */       break;
/*      */     case 1: 
/*      */       break;
/*      */     case 2: 
/*      */       break;
/*      */     case 3: 
/*      */       break;
/*      */     case 4: 
/*      */       break;
/*      */     case 5: 
/*      */       break;
/*      */     case 6: 
/*      */       break;
/*      */     case 7: 
/*      */       break;
/*      */     }
/*  884 */     sprite.hot.state = 0;
/*      */   }
/*      */   
/*      */   public static final void reqGetMotionEvents(XClient c) throws IOException
/*      */   {
/*  889 */     ComChannel comChannel = c.channel;
/*      */     
/*  891 */     int foo = comChannel.readInt();
/*  892 */     XWindow w = c.lookupWindow(foo);
/*  893 */     foo = comChannel.readInt();
/*  894 */     foo = comChannel.readInt();
/*  895 */     c.length -= 4;
/*  896 */     if (w == null) {
/*  897 */       c.errorValue = foo;
/*  898 */       c.errorReason = 3;
/*  899 */       return;
/*      */     }
/*      */     
/*  902 */     synchronized (comChannel) {
/*  903 */       comChannel.writeByte(1);
/*  904 */       comChannel.writePad(1);
/*  905 */       comChannel.writeShort(c.getSequence());
/*  906 */       comChannel.writeInt(0);
/*  907 */       comChannel.writeInt(0);
/*  908 */       comChannel.writePad(20);
/*  909 */       comChannel.flush();
/*      */     }
/*      */   }
/*      */   
/*      */   public static final void reqTranslateCoordinates(XClient c) throws IOException {
/*  914 */     ComChannel comChannel = c.channel;
/*  915 */     int foo = comChannel.readInt();
/*  916 */     XWindow srcw = c.lookupWindow(foo);
/*  917 */     if (srcw == null) {
/*  918 */       c.errorValue = foo;
/*  919 */       c.errorReason = 3;
/*      */     }
/*  921 */     foo = comChannel.readInt();
/*  922 */     XWindow dstw = c.lookupWindow(foo);
/*  923 */     if ((dstw == null) && (c.errorReason == 0)) {
/*  924 */       c.errorValue = foo;
/*  925 */       c.errorReason = 3;
/*      */     }
/*  927 */     int x = comChannel.readShort();
/*  928 */     int y = comChannel.readShort();
/*  929 */     c.length -= 4;
/*  930 */     if (c.errorReason != 0) {
/*  931 */       return;
/*      */     }
/*      */     
/*  934 */     int gx = x + srcw.x;
/*  935 */     int gy = y + srcw.y;
/*      */     
/*  937 */     int child = 0;
/*      */     
/*  939 */     synchronized (LOCK) {
/*  940 */       XWindow win = dstw.firstChild;
/*  941 */       while (win != null) {
/*  942 */         if (((win.attr & 0x80000) != 0) && (win.contains(gx, gy))) {
/*  943 */           child = win.id;
/*  944 */           win = null;
/*      */         }
/*      */         else {
/*  947 */           win = win.nextSib;
/*      */         }
/*      */       }
/*      */     }
/*      */     
/*  952 */     x = gx - dstw.x;
/*  953 */     y = gy - dstw.y;
/*      */     
/*  955 */     synchronized (comChannel) {
/*  956 */       comChannel.writeByte(1);
/*  957 */       comChannel.writeByte(1);
/*  958 */       comChannel.writeShort(c.getSequence());
/*  959 */       comChannel.writeInt(0);
/*  960 */       comChannel.writeInt(child);
/*  961 */       comChannel.writeShort(x);
/*  962 */       comChannel.writeShort(y);
/*  963 */       comChannel.writePad(16);
/*  964 */       comChannel.flush();
/*      */     }
/*      */   }
/*      */   
/*      */   public static final void reqGetScreenSaver(XClient c) throws IOException
/*      */   {
/*  970 */     ComChannel comChannel = c.channel;
/*      */     
/*  972 */     synchronized (comChannel) {
/*  973 */       comChannel.writeByte(1);
/*  974 */       comChannel.writePad(1);
/*  975 */       comChannel.writeShort(c.getSequence());
/*  976 */       comChannel.writeInt(0);
/*  977 */       comChannel.writeShort(600);
/*  978 */       comChannel.writeShort(600);
/*  979 */       comChannel.writeByte((byte)1);
/*  980 */       comChannel.writeByte((byte)1);
/*  981 */       comChannel.writePad(18);
/*  982 */       comChannel.flush();
/*      */     }
/*      */   }
/*      */   
/*      */   public static final void reqQueryBestSize(XClient c) throws IOException
/*      */   {
/*  988 */     ComChannel comChannel = c.channel;
/*  989 */     int clss = c.data;
/*  990 */     int foo = comChannel.readInt();
/*  991 */     c.length -= 2;
/*  992 */     Drawable d = c.lookupDrawable(foo);
/*  993 */     if ((clss != 0) && (clss != 1) && (clss != 2))
/*      */     {
/*      */ 
/*  996 */       c.errorValue = clss;
/*  997 */       c.errorReason = 2;
/*  998 */       return;
/*      */     }
/* 1000 */     if (d == null) {
/* 1001 */       c.errorValue = foo;
/* 1002 */       c.errorReason = 9;
/* 1003 */       return;
/*      */     }
/* 1005 */     foo = comChannel.readShort();
/* 1006 */     foo = comChannel.readShort();
/*      */     
/* 1008 */     synchronized (comChannel) {
/* 1009 */       comChannel.writeByte(1);
/* 1010 */       comChannel.writePad(1);
/* 1011 */       comChannel.writeShort(c.getSequence());
/* 1012 */       comChannel.writeInt(0);
/* 1013 */       comChannel.writeShort(32);
/* 1014 */       comChannel.writeShort(32);
/* 1015 */       comChannel.writePad(20);
/* 1016 */       comChannel.flush();
/*      */     }
/*      */   }
/*      */   
/*      */   public static final void reqDestroySubwindows(XClient c) throws IOException
/*      */   {
/* 1022 */     ComChannel comChannel = c.channel;
/* 1023 */     int foo = comChannel.readInt();
/* 1024 */     c.length -= 2;
/* 1025 */     XWindow w = c.lookupWindow(foo);
/*      */     
/* 1027 */     if (w == null) {
/* 1028 */       c.errorValue = foo;
/* 1029 */       c.errorReason = 3;
/* 1030 */       return;
/*      */     }
/* 1032 */     synchronized (LOCK) {
/* 1033 */       w.unmapSubwindows(c);
/* 1034 */       while (w.lastChild != null) {
/* 1035 */         freeResource(w.lastChild.id, 0);
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */   void makeBackgroundTile(int xx, int yy, int w, int h) {
/* 1041 */     XWindow win = this;
/* 1042 */     int i = this.attr & 0x3;
/* 1043 */     if (i == 1) {
/* 1044 */       win = this.parent;
/* 1045 */       if (win == null) return;
/* 1046 */       i = win.attr & 0x3;
/*      */     }
/*      */     
/* 1049 */     if ((i == 3) && (win.background.pixmap != null)) {
/* 1050 */       Image img = win.background.img;
/* 1051 */       if (img == null) return;
/* 1052 */       if (!win.ddxwindow.isVisible()) return;
/* 1053 */       Graphics g = null;
/* 1054 */       Shape tmp = null;
/* 1055 */       if ((xx != 0) || (yy != 0) || (w != this.width) || (h != this.height)) {
/* 1056 */         g = getGraphics();
/* 1057 */         tmp = g.getClip();
/* 1058 */         g.clipRect(xx, yy, w, h);
/*      */       }
/* 1060 */       if (win == this) {
/* 1061 */         this.ddxwindow.fillImage(img, win.background.pixmap.width, win.background.pixmap.height);
/*      */ 
/*      */       }
/*      */       else
/*      */       {
/* 1066 */         this.ddxwindow.fillImage(img, win.background.pixmap.width, win.background.pixmap.height, this.origin.x - this.borderWidth, this.origin.y - this.borderWidth);
/*      */       }
/*      */       
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1073 */       if (g != null) {
/* 1074 */         if (tmp == null) g.setClip(0, 0, this.width, this.height); else {
/* 1075 */           g.setClip(tmp);
/*      */         }
/*      */       }
/* 1078 */     } else if (i == 2) {
/* 1079 */       Colormap cmap = win.getColormap();
/* 1080 */       this.ddxwindow.setBackground(cmap.getColor(win.background.pixel), xx, yy, w, h);
/*      */     }
/*      */   }
/*      */   
/*      */   private void free()
/*      */   {
/* 1086 */     SaveSet.delete(this);
/* 1087 */     Selection.delete(this);
/* 1088 */     try { deleteEvent(true);
/*      */     }
/*      */     catch (Exception e) {}
/*      */     try
/*      */     {
/* 1093 */       deleteProperties();
/*      */     }
/*      */     catch (Exception e) {}
/*      */     
/*      */ 
/* 1098 */     this.origin = null;
/* 1099 */     if (((this.attr & 0x3) == 3) && 
/* 1100 */       (this.background.pixmap != null))
/*      */     {
/*      */       try
/*      */       {
/* 1104 */         this.background.pixmap.delete(); } catch (Exception e) {}
/* 1105 */       this.background.pixmap = null;
/*      */     }
/*      */     
/* 1108 */     this.background = null;
/* 1109 */     if (((this.attr & 0x4) == 0) && 
/* 1110 */       (this.border.pixmap != null))
/*      */     {
/*      */       try
/*      */       {
/* 1114 */         this.border.pixmap.delete(); } catch (Exception e) {}
/* 1115 */       this.border.pixmap = null;
/*      */     }
/*      */     
/* 1118 */     this.border = null;
/* 1119 */     this.optional = null;
/*      */     try {
/* 1121 */       this.ddxwindow.delete();
/*      */     }
/*      */     catch (Exception e) {}
/* 1124 */     if (hasFrame()) {
/* 1125 */       delFrame();
/*      */     }
/*      */   }
/*      */   
/*      */   private void deleteEvent(boolean freeResources) throws IOException
/*      */   {
/* 1131 */     if ((grab != null) && (grab.window == this)) {
/* 1132 */       Grab.deactivatePointerGrab();
/*      */     }
/* 1134 */     if ((this.id == focus.win) && (this.parent != null)) {
/* 1135 */       int focusEventMode = 0;
/* 1136 */       switch (focus.revert) {
/*      */       case 0: 
/* 1138 */         doFocusEvents(null, this.id, 0, focusEventMode);
/* 1139 */         focus.win = 0;
/* 1140 */         focus.XWindow = null;
/* 1141 */         focus.traceGood = 0;
/* 1142 */         break;
/*      */       case 2: 
/* 1144 */         XWindow win = this;
/*      */         do {
/* 1146 */           win = win.parent;
/* 1147 */           focus.traceGood -= 1;
/*      */         }
/* 1149 */         while ((win != null) && (!win.isRealized()));
/*      */         
/* 1151 */         int p = win == null ? 0 : win.id;
/* 1152 */         doFocusEvents(null, this.id, p, focusEventMode);
/* 1153 */         focus.win = p;
/* 1154 */         focus.XWindow = win;
/* 1155 */         focus.revert = 0;
/* 1156 */         break;
/*      */       case 1: 
/* 1158 */         doFocusEvents(null, this.id, 1, focusEventMode);
/* 1159 */         focus.win = 1;
/* 1160 */         focus.XWindow = null;
/* 1161 */         focus.traceGood = 0;
/*      */       }
/*      */       
/*      */     }
/*      */     
/* 1166 */     if (freeResources) {
/* 1167 */       if ((this.attr & 0x1C00000) != 0) {
/* 1168 */         DontPropagate.refc[((this.attr & 0x1C00000) >> 22)] -= 1;
/*      */       }
/*      */       OtherClients oc;
/* 1171 */       while ((oc = getOtherClients()) != null)
/* 1172 */         freeResource(oc.id, 0);
/*      */       Grab passive;
/* 1174 */       while ((passive = getPassiveGrabs()) != null) {
/* 1175 */         freeResource(passive.id, 0);
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */   private void deleteProperties()
/*      */     throws IOException
/*      */   {
/* 1183 */     Property pProp = getProperty();
/* 1184 */     while (pProp != null) {
/* 1185 */       this.XClient.cevent.mkPropertyNotify(this.id, pProp.propertyName, (int)System.currentTimeMillis(), 1);
/*      */       
/*      */ 
/*      */ 
/*      */ 
/* 1190 */       sendEvent(this.XClient.cevent, 1, null);
/* 1191 */       Property pNextProp = pProp.next;
/* 1192 */       pProp.data = null;
/* 1193 */       pProp = pNextProp;
/*      */     }
/*      */   }
/*      */   
/*      */   private void crushTree() throws IOException {
/*      */     XWindow child;
/* 1199 */     if ((child = this.firstChild) == null) { return;
/*      */     }
/* 1201 */     while (child.firstChild != null) {
/* 1202 */       child = child.firstChild;
/*      */     }
/*      */     do
/*      */     {
/* 1206 */       XWindow cparent = child.parent;
/* 1207 */       if (child.substrSend()) {
/* 1208 */         Event event = new Event();
/* 1209 */         event.mkDestroyNotify(child.id, child.id);
/* 1210 */         try { child.sendEvent(event, 1, null);
/*      */         }
/*      */         catch (Exception e) {}
/*      */       }
/*      */       try {
/* 1215 */         freeResource(child.id, -1073741823);
/*      */       }
/*      */       catch (Exception e) {}
/*      */       
/*      */ 
/* 1220 */       XWindow sib = child.nextSib;
/* 1221 */       child.attr &= 0xFFDFFFFF;
/* 1222 */       if ((child.attr & 0x100000) != 0) {
/* 1223 */         child.attr &= 0xFFEFFFFF;
/* 1224 */         child.attr |= 0x60000;
/* 1225 */         child.ddxwindow.setVisible(false);
/*      */         
/* 1227 */         if ((this.screen.windowmode != 0) && (child.hasFrame())) {
/* 1228 */           child.getFrame().setVisible(false);
/*      */         }
/*      */       }
/*      */       try {
/* 1232 */         child.free();
/*      */       }
/*      */       catch (Exception e) {}
/*      */       
/*      */ 
/* 1237 */       if ((child = sib) != null)
/*      */         break;
/* 1239 */       child = cparent;
/* 1240 */       child.firstChild = null;
/* 1241 */       child.lastChild = null;
/* 1242 */     } while (child != this);
/*      */   }
/*      */   
/*      */   void delete()
/*      */     throws IOException
/*      */   {
/* 1248 */     synchronized (LOCK) {
/* 1249 */       try { unmapWindow(false);
/*      */       }
/*      */       catch (Exception e) {}
/*      */       try {
/* 1253 */         crushTree();
/*      */       }
/*      */       catch (Exception e) {}
/*      */       
/*      */ 
/* 1258 */       if ((this.id != 0) && (this.parent != null) && (substrSend())) {
/* 1259 */         Event event = new Event();
/* 1260 */         event.mkDestroyNotify(this.id, this.id);
/* 1261 */         try { sendEvent(event, 1, null);
/*      */         }
/*      */         catch (Exception e) {}
/*      */       }
/*      */       
/* 1266 */       if (this.parent != null) {
/* 1267 */         if (this.parent.firstChild == this) this.parent.firstChild = this.nextSib;
/* 1268 */         if (this.parent.lastChild == this) this.parent.lastChild = this.prevSib;
/* 1269 */         if (this.nextSib != null) this.nextSib.prevSib = this.prevSib;
/* 1270 */         if (this.prevSib != null) this.prevSib.nextSib = this.nextSib;
/*      */       }
/* 1272 */       this.parent = null;
/* 1273 */       this.nextSib = null;
/* 1274 */       this.prevSib = null;
/* 1275 */       this.firstChild = null;
/* 1276 */       this.lastChild = null;
/* 1277 */       this.prevSib = null;
/*      */       try {
/* 1279 */         free();
/*      */       }
/*      */       catch (Exception e) {}
/*      */     }
/*      */   }
/*      */   
/*      */   public static final void reqDestroyWindow(XClient c) throws IOException
/*      */   {
/* 1287 */     ComChannel comChannel = c.channel;
/* 1288 */     int foo = comChannel.readInt();
/* 1289 */     c.length -= 2;
/* 1290 */     XWindow w = c.lookupWindow(foo);
/* 1291 */     if (w == null) {
/* 1292 */       c.errorValue = foo;
/* 1293 */       c.errorReason = 3;
/* 1294 */       return;
/*      */     }
/*      */     
/* 1297 */     if (w.parent != null) {
/* 1298 */       freeResource(w.id, 0);
/*      */     }
/*      */   }
/*      */   
/*      */   public static final void reqSendEvent(XClient c) throws IOException
/*      */   {
/* 1304 */     XWindow effectiveFocus = null;
/*      */     
/* 1306 */     ComChannel comChannel = c.channel;
/* 1307 */     int prop = c.data;
/* 1308 */     int dest = comChannel.readInt();
/* 1309 */     int emask = comChannel.readInt();
/* 1310 */     byte[] bb = new byte[32];
/* 1311 */     comChannel.readByte(bb, 0, 32);
/* 1312 */     c.length = 0;
/* 1313 */     Event event = new Event(bb);
/* 1314 */     if (c.swap) event.swap();
/*      */     XWindow win;
/* 1316 */     XWindow win; if (dest == 0) {
/* 1317 */       win = sprite.win;
/*      */     }
/*      */     else {
/* 1320 */       win = c.lookupWindow(dest);
/*      */     }
/*      */     
/* 1323 */     if (win == null) {
/* 1324 */       c.errorValue = dest;
/* 1325 */       c.errorReason = 3;
/* 1326 */       return;
/*      */     }
/*      */     
/* 1329 */     if ((prop != 0) && (prop != 1)) {
/* 1330 */       c.errorValue = prop;
/* 1331 */       c.errorReason = 2;
/* 1332 */       return;
/*      */     }
/*      */     
/* 1335 */     event.setType((byte)(event.getType() | 0x80));
/* 1336 */     if (prop != 0) {
/* 1337 */       for (; win != null; win = win.parent) {
/* 1338 */         if (win.sendEvent(event, 1, emask, null, 0) != 0)
/* 1339 */           return;
/* 1340 */         emask &= (win.getDontPropagateMask() ^ 0xFFFFFFFF);
/* 1341 */         if (emask == 0) {
/*      */           break;
/*      */         }
/*      */       }
/*      */     }
/* 1346 */     win.sendEvent(event, 1, emask, null, 0);
/*      */   }
/*      */   
/*      */ 
/*      */   public static final void reqGetPointerMapping(XClient c)
/*      */     throws IOException
/*      */   {
/* 1353 */     ComChannel comChannel = c.channel;
/*      */     
/* 1355 */     synchronized (comChannel) {
/* 1356 */       comChannel.writeByte(1);
/* 1357 */       comChannel.writeByte(0);
/* 1358 */       comChannel.writeShort(c.getSequence());
/* 1359 */       comChannel.writeInt(0);
/* 1360 */       comChannel.writePad(24);
/* 1361 */       comChannel.flush();
/*      */     }
/*      */   }
/*      */   
/*      */   public static final void reqSetPointerMapping(XClient c) throws IOException
/*      */   {
/* 1367 */     ComChannel comChannel = c.channel;
/* 1368 */     int foo = c.data;
/* 1369 */     int n = c.length;
/* 1370 */     n--;
/* 1371 */     while (n != 0) {
/* 1372 */       foo = comChannel.readInt();
/* 1373 */       n--;
/*      */     }
/*      */     
/* 1376 */     synchronized (comChannel) {
/* 1377 */       comChannel.writeByte(1);
/* 1378 */       comChannel.writeByte((byte)1);
/* 1379 */       comChannel.writeShort(c.getSequence());
/* 1380 */       comChannel.writeInt(0);
/* 1381 */       comChannel.writePad(24);
/* 1382 */       comChannel.flush();
/*      */     }
/*      */   }
/*      */   
/*      */   public static final void reqGetPointerControl(XClient c) throws IOException {
/* 1387 */     ComChannel comChannel = c.channel;
/*      */     
/* 1389 */     synchronized (comChannel) {
/* 1390 */       comChannel.writeByte(1);
/* 1391 */       comChannel.writePad(1);
/* 1392 */       comChannel.writeShort(c.getSequence());
/* 1393 */       comChannel.writeInt(0);
/* 1394 */       comChannel.writeShort(2);
/* 1395 */       comChannel.writeShort(1);
/* 1396 */       comChannel.writeShort(4);
/* 1397 */       comChannel.writePad(18);
/* 1398 */       comChannel.flush();
/*      */     }
/*      */   }
/*      */   
/*      */   public static final void reqQueryPointer(XClient c) throws IOException
/*      */   {
/* 1404 */     ComChannel comChannel = c.channel;
/* 1405 */     int foo = comChannel.readInt();
/* 1406 */     c.length -= 2;
/* 1407 */     XWindow w = c.lookupWindow(foo);
/* 1408 */     if (w == null) {
/* 1409 */       c.errorValue = foo;
/* 1410 */       c.errorReason = 3;
/* 1411 */       return;
/*      */     }
/*      */     
/* 1414 */     int hx = sprite.hot.x;
/* 1415 */     int hy = sprite.hot.y;
/*      */     
/* 1417 */     XWindow child = null;
/* 1418 */     synchronized (LOCK) {
/* 1419 */       for (XWindow ww = sprite.win; ww != null; ww = ww.parent) {
/* 1420 */         if (ww.parent == w) {
/* 1421 */           child = ww;
/* 1422 */           break;
/*      */         }
/*      */       }
/*      */       
/* 1426 */       if ((child != null) && 
/* 1427 */         (!child.contains(hx, hy))) {
/* 1428 */         sprite.win = xy2Window(hx, hy, null);
/* 1429 */         child = null;
/* 1430 */         for (XWindow ww = sprite.win; ww != null; ww = ww.parent) {
/* 1431 */           if (ww.parent == w) {
/* 1432 */             child = ww;
/* 1433 */             break;
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/*      */     
/*      */ 
/* 1440 */     synchronized (comChannel) {
/* 1441 */       comChannel.writeByte(1);
/* 1442 */       comChannel.writeByte(1);
/* 1443 */       comChannel.writeShort(c.getSequence());
/* 1444 */       comChannel.writeInt(0);
/* 1445 */       comChannel.writeInt(w.screen.rootId);
/*      */       
/* 1447 */       comChannel.writeInt(child == null ? 0 : child.id);
/* 1448 */       comChannel.writeShort(hx);
/* 1449 */       comChannel.writeShort(hy);
/* 1450 */       comChannel.writeShort(hx - w.x);
/* 1451 */       comChannel.writeShort(hy - w.y);
/* 1452 */       comChannel.writeShort(sprite.hot.state);
/*      */       
/* 1454 */       comChannel.writePad(6);
/* 1455 */       comChannel.flush();
/*      */     }
/*      */   }
/*      */   
/*      */   public static final void reqGetGeometry(XClient c) throws IOException {
/* 1460 */     ComChannel comChannel = c.channel;
/* 1461 */     int foo = comChannel.readInt();
/*      */     
/* 1463 */     Drawable d = c.lookupDrawable(foo);
/* 1464 */     c.length -= 2;
/* 1465 */     if (d == null) {
/* 1466 */       c.errorValue = foo;
/* 1467 */       c.errorReason = 9;
/* 1468 */       return;
/*      */     }
/*      */     
/* 1471 */     synchronized (comChannel) {
/* 1472 */       comChannel.writeByte(1);
/* 1473 */       comChannel.writeByte(d.depth);
/* 1474 */       comChannel.writeShort(c.getSequence());
/* 1475 */       comChannel.writeInt(0);
/* 1476 */       comChannel.writeInt(d.screen.rootId);
/*      */       
/* 1478 */       if ((d.type == 2) || ((d.type == 0) && (foo == d.id)))
/*      */       {
/* 1480 */         XWindow w = (XWindow)d;
/* 1481 */         comChannel.writeShort(w.origin.x - w.borderWidth);
/* 1482 */         comChannel.writeShort(w.origin.y - w.borderWidth);
/* 1483 */         comChannel.writeShort(w.width);
/* 1484 */         comChannel.writeShort(w.height);
/* 1485 */         comChannel.writeShort(w.borderWidth);
/*      */       }
/*      */       else {
/* 1488 */         comChannel.writeShort(0);
/* 1489 */         comChannel.writeShort(0);
/* 1490 */         comChannel.writeShort(d.width);
/* 1491 */         comChannel.writeShort(d.height);
/* 1492 */         comChannel.writeShort(0);
/*      */       }
/* 1494 */       comChannel.writePad(10);
/* 1495 */       comChannel.flush();
/*      */     }
/*      */   }
/*      */   
/*      */   public static final void reqChangeWindowAttributes(XClient c) throws IOException
/*      */   {
/* 1501 */     ComChannel comChannel = c.channel;
/*      */     
/* 1503 */     int wid = comChannel.readInt();
/* 1504 */     int mask = comChannel.readInt();
/*      */     
/* 1506 */     c.length -= 3;
/*      */     
/* 1508 */     XWindow w = c.lookupWindow(wid);
/* 1509 */     if (w == null) {
/* 1510 */       c.errorValue = wid;
/* 1511 */       c.errorReason = 3;
/* 1512 */       return;
/*      */     }
/*      */     
/* 1515 */     mask &= 0x7FFF;
/* 1516 */     if (mask != 0) { w.changeAttr(c, mask);
/*      */     }
/* 1518 */     if (c.errorReason != 0) {
/* 1519 */       return;
/*      */     }
/*      */     
/* 1522 */     if (((mask & 0x4) != 0) || ((mask & 0x8) != 0)) {
/* 1523 */       w.ddxwindow.setBorderPixmap(w.border.pixmap);
/*      */     }
/*      */   }
/*      */   
/*      */   public static final void reqUngrabPointer(XClient c) throws IOException
/*      */   {
/* 1529 */     ComChannel comChannel = c.channel;
/* 1530 */     int foo = comChannel.readInt();
/* 1531 */     if ((grab != null) && (grab.sameClient(c))) {
/* 1532 */       Grab.deactivatePointerGrab();
/*      */     }
/*      */   }
/*      */   
/*      */   public static final void reqUngrabButton(XClient c)
/*      */     throws IOException
/*      */   {
/* 1539 */     ComChannel comChannel = c.channel;
/*      */     
/* 1541 */     int button = c.data;
/* 1542 */     int foo = comChannel.readInt();
/*      */     
/* 1544 */     c.length -= 2;
/* 1545 */     XWindow gw = c.lookupWindow(foo);
/* 1546 */     if (gw == null) {
/* 1547 */       c.errorValue = foo;
/* 1548 */       c.errorReason = 3;
/* 1549 */       return;
/*      */     }
/*      */     
/* 1552 */     int mod = comChannel.readShort();
/* 1553 */     comChannel.readPad(2);
/* 1554 */     c.length -= 1;
/* 1555 */     Grab grab = Grab.createGrab(c, gw, 0, 0, 0, 0, mod, 4, button, null);
/*      */     
/* 1557 */     if (!grab.deletePassiveGrabFromList()) {
/* 1558 */       c.errorReason = 11;
/*      */     }
/*      */   }
/*      */   
/*      */   public static final void reqGrabButton(XClient c) throws IOException
/*      */   {
/* 1564 */     ComChannel comChannel = c.channel;
/*      */     
/* 1566 */     int oe = c.data;
/* 1567 */     if ((oe != 0) && (oe != 1)) {}
/*      */     
/*      */ 
/* 1570 */     int foo = comChannel.readInt();
/* 1571 */     XWindow gw = c.lookupWindow(foo);
/* 1572 */     if (gw == null) {
/* 1573 */       c.errorValue = foo;
/* 1574 */       c.errorReason = 3;
/*      */     }
/* 1576 */     int emask = comChannel.readShort();
/* 1577 */     if ((emask & 0x8003) != 0) {}
/*      */     
/*      */ 
/* 1580 */     int pmode = comChannel.readByte();
/* 1581 */     if ((pmode != 0) && (pmode != 1)) {}
/*      */     
/*      */ 
/*      */ 
/* 1585 */     int kmode = comChannel.readByte();
/* 1586 */     if ((kmode != 0) && (kmode != 1)) {}
/*      */     
/*      */ 
/*      */ 
/* 1590 */     foo = comChannel.readInt();
/* 1591 */     XWindow cto = null;
/* 1592 */     if (foo != 0) {
/* 1593 */       cto = c.lookupWindow(foo);
/*      */     }
/*      */     
/* 1596 */     foo = comChannel.readInt();
/* 1597 */     int button = comChannel.readByte();
/* 1598 */     comChannel.readPad(1);
/* 1599 */     int mod = comChannel.readShort();
/* 1600 */     c.length -= 6;
/*      */     
/* 1602 */     if (c.errorReason != 0) {
/* 1603 */       return;
/*      */     }
/*      */     
/* 1606 */     if ((gw != null) && (cto == null)) {}
/*      */     
/*      */ 
/* 1609 */     Grab grab = Grab.createGrab(c, gw, emask, oe, kmode, pmode, mod, 4, button, cto);
/*      */     
/*      */ 
/*      */ 
/*      */ 
/* 1614 */     grab.addPassiveGrabToList();
/*      */   }
/*      */   
/*      */   public static final void reqGrabPointer(XClient c)
/*      */     throws IOException
/*      */   {
/* 1620 */     ComChannel comChannel = c.channel;
/*      */     
/* 1622 */     int oe = c.data;
/* 1623 */     if ((oe != 0) && (oe != 1)) {
/* 1624 */       c.errorValue = oe;
/* 1625 */       c.errorReason = 2;
/*      */     }
/*      */     
/* 1628 */     int foo = comChannel.readInt();
/* 1629 */     XWindow gw = c.lookupWindow(foo);
/* 1630 */     if (gw == null) {
/* 1631 */       c.errorValue = foo;
/* 1632 */       c.errorReason = 3;
/*      */     }
/*      */     
/* 1635 */     int emask = comChannel.readShort();
/* 1636 */     if ((emask & 0x8003) != 0) {
/* 1637 */       c.errorValue = emask;
/* 1638 */       c.errorReason = 2;
/*      */     }
/*      */     
/* 1641 */     int pmode = comChannel.readByte();
/* 1642 */     if ((pmode != 0) && (pmode != 1)) {
/* 1643 */       c.errorValue = pmode;
/* 1644 */       c.errorReason = 2;
/*      */     }
/*      */     
/* 1647 */     int kmode = comChannel.readByte();
/* 1648 */     if ((kmode != 0) && (kmode != 1)) {
/* 1649 */       c.errorValue = kmode;
/* 1650 */       c.errorReason = 2;
/*      */     }
/*      */     
/* 1653 */     foo = comChannel.readInt();
/* 1654 */     XWindow cto = null;
/* 1655 */     if (foo != 0) {
/* 1656 */       cto = c.lookupWindow(foo);
/* 1657 */       if (cto == null) {
/* 1658 */         c.errorValue = foo;
/* 1659 */         c.errorReason = 3;
/*      */       }
/*      */     }
/*      */     
/* 1663 */     foo = comChannel.readInt();
/* 1664 */     Cursor cur = null;
/* 1665 */     if (foo != 0) {
/* 1666 */       cur = (Cursor)lookupIDByType(foo, 5);
/* 1667 */       if (cur == null) {
/* 1668 */         c.errorValue = foo;
/* 1669 */         c.errorReason = 6;
/*      */       }
/*      */     }
/*      */     
/* 1673 */     int time = comChannel.readInt();
/*      */     
/* 1675 */     c.length -= 6;
/*      */     
/* 1677 */     if (c.errorReason != 0) {
/* 1678 */       return;
/*      */     }
/*      */     
/* 1681 */     int res = 0;
/* 1682 */     if ((grab != null) && (c != grab.getClient())) {
/* 1683 */       res = 1;
/*      */     }
/* 1685 */     else if (((gw.attr & 0x100000) == 0) || ((cto != null) && (((cto.attr & 0x100000) == 0) || ((cto.width == 0) && (cto.height == 0)))))
/*      */     {
/*      */ 
/* 1688 */       res = 3;
/*      */     }
/*      */     else {
/* 1691 */       Grab grab = new Grab(fakeClientId(c));
/* 1692 */       grab.set(c.clientAsMask, gw, oe, emask, kmode, pmode, cto);
/*      */       
/* 1694 */       grab.activatePointerGrab((int)System.currentTimeMillis(), true);
/* 1695 */       res = 0;
/*      */     }
/*      */     
/* 1698 */     synchronized (comChannel) {
/* 1699 */       comChannel.writeByte(1);
/* 1700 */       comChannel.writeByte(res);
/* 1701 */       comChannel.writeShort(c.getSequence());
/* 1702 */       comChannel.writeInt(0);
/* 1703 */       comChannel.writePad(24);
/* 1704 */       comChannel.flush();
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static final void reqCreateWindow(XClient c)
/*      */     throws IOException
/*      */   {
/* 1715 */     ComChannel comChannel = c.channel;
/*      */     
/* 1717 */     byte depth = (byte)c.data;
/*      */     
/* 1719 */     int wid = comChannel.readInt();
/*      */     
/* 1721 */     int foo = comChannel.readInt();
/* 1722 */     XWindow prnt = c.lookupWindow(foo);
/*      */     
/* 1724 */     int x = comChannel.readShort();
/* 1725 */     int y = comChannel.readShort();
/*      */     
/*      */ 
/* 1728 */     int width = comChannel.readShort();
/* 1729 */     int height = comChannel.readShort();
/* 1730 */     int bwidth = comChannel.readShort();
/* 1731 */     int clss = comChannel.readShort();
/* 1732 */     int visual = comChannel.readInt();
/* 1733 */     int mask = comChannel.readInt();
/*      */     
/* 1735 */     c.length -= 8;
/*      */     
/* 1737 */     if (prnt == null) {
/* 1738 */       c.errorValue = foo;
/* 1739 */       c.errorReason = 3;
/*      */     }
/*      */     
/* 1742 */     if (((width == 0) || (height == 0)) && (c.errorReason == 0)) {
/* 1743 */       c.errorValue = 0;
/* 1744 */       c.errorReason = 2;
/*      */     }
/*      */     
/* 1747 */     XWindow w = null;
/* 1748 */     if (c.errorReason == 0)
/*      */     {
/* 1750 */       if (prnt != null) {
/* 1751 */         x = prnt.adjustX(x, width, bwidth);
/* 1752 */         y = prnt.adjustY(y, height, bwidth);
/*      */       }
/* 1754 */       w = new XWindow(wid, prnt, x, y, width, height, bwidth, clss, depth, c, visual, mask);
/*      */     }
/*      */     
/*      */ 
/* 1758 */     if (c.errorReason != 0) {
/* 1759 */       return;
/*      */     }
/* 1761 */     add(w);
/*      */   }
/*      */   
/*      */   public int adjustY(int y, int height, int bwidth) {
/* 1765 */     return y;
/*      */   }
/*      */   
/* 1768 */   public int adjustX(int x, int width, int bwidth) { return x; }
/*      */   
/*      */   int sendEvent(Event event, int count, int filter, Grab grab, int mskidx)
/*      */     throws IOException
/*      */   {
/* 1773 */     int deliveries = 0;int nondeliveries = 0;
/*      */     
/* 1775 */     XClient c = null;
/* 1776 */     int deliveryMask = 0;
/* 1777 */     int type = event.getType();
/*      */     
/* 1779 */     if ((filter == 0) || ((type & 0x40) == 0))
/*      */     {
/* 1781 */       if ((filter != 0) && (((getOtherEventMask() | this.eventMask) & filter) == 0))
/*      */       {
/* 1783 */         return 0;
/*      */       }
/* 1785 */       int attempt = 1;
/* 1786 */       try { attempt = this.XClient.sendEvent(event, count, this.eventMask, filter, grab);
/*      */       }
/*      */       catch (Exception e) {}
/*      */       
/* 1790 */       if (attempt != 0) {
/* 1791 */         if (attempt > 0) {
/* 1792 */           deliveries++;
/* 1793 */           c = this.XClient;
/* 1794 */           deliveryMask = this.eventMask;
/*      */         }
/*      */         else {
/* 1797 */           nondeliveries--;
/*      */         }
/*      */       }
/*      */     }
/*      */     
/* 1802 */     if (filter != 0) { Clients other;
/* 1803 */       if ((type & 0x40) != 0)
/*      */       {
/*      */ 
/* 1806 */         OtherInputMasks inputMasks = this.optional == null ? null : this.optional.otherInputMasks;
/* 1807 */         if ((inputMasks == null) || ((inputMasks.inputEvents[mskidx] & filter) == 0))
/*      */         {
/* 1809 */           return 0;
/*      */         }
/* 1811 */         other = inputMasks.inputClients;
/*      */       }
/* 1813 */       for (Clients other = this.optional == null ? null : this.optional.otherClients; 
/*      */           
/* 1815 */           other != null; other = (OtherClients)other.next) {
/* 1816 */         int attempt = 1;
/*      */         try {
/* 1818 */           if (other.getClient() != null) {
/* 1819 */             int mask = (other instanceof OtherClients) ? ((OtherClients)other).mask : ((InputClients)other).mask[mskidx];
/*      */             
/*      */ 
/* 1822 */             attempt = other.getClient().sendEvent(event, count, mask, filter, grab);
/*      */           }
/*      */           else
/*      */           {
/* 1826 */             attempt = 0;
/*      */           }
/*      */         }
/*      */         catch (Exception e) {}
/*      */         
/*      */ 
/* 1832 */         if (attempt != 0) {
/* 1833 */           if (attempt > 0) {
/* 1834 */             deliveries++;
/* 1835 */             c = other.getClient();
/* 1836 */             deliveryMask = (other instanceof OtherClients) ? ((OtherClients)other).mask : ((InputClients)other).mask[mskidx];
/*      */ 
/*      */           }
/*      */           else
/*      */           {
/* 1841 */             nondeliveries--;
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/*      */     
/* 1847 */     if (c == null) {
/* 1848 */       if (deliveries != 0) {
/* 1849 */         return deliveries;
/*      */       }
/* 1851 */       return nondeliveries;
/*      */     }
/*      */     
/* 1854 */     if ((type == 4) && (deliveries != 0) && (grab == null)) {
/* 1855 */       grab = new Grab(fakeClientId(c));
/* 1856 */       grab.set(c.clientAsMask, this, deliveryMask & 0x1000000, deliveryMask, 1, 1, null);
/*      */       
/*      */ 
/* 1859 */       grab.activatePointerGrab((int)System.currentTimeMillis(), true);
/*      */     }
/* 1861 */     if (deliveries != 0)
/* 1862 */       return deliveries;
/* 1863 */     return nondeliveries;
/*      */   }
/*      */   
/*      */ 
/*      */   int sendEvent(Event event, int count, XWindow otherParent)
/*      */     throws IOException
/*      */   {
/* 1870 */     if (count == 0) { return 0;
/*      */     }
/* 1872 */     int filter = Event.filters[event.getType()];
/* 1873 */     if (((filter & 0x80000) != 0) && (event.getType() != 16))
/*      */     {
/* 1875 */       event.putEvent(this.id);
/*      */     }
/*      */     
/* 1878 */     if (filter != 655360) {
/* 1879 */       return sendEvent(event, count, filter, null, 0);
/*      */     }
/*      */     
/* 1882 */     int deliveries = sendEvent(event, count, 131072, null, 0);
/*      */     
/*      */ 
/*      */ 
/*      */ 
/* 1887 */     if (this.parent != null) {
/* 1888 */       event.putEvent(this.parent.id);
/* 1889 */       deliveries += this.parent.sendEvent(event, count, 524288, null, 0);
/*      */       
/*      */ 
/*      */ 
/* 1893 */       if (event.getType() == 21) {
/* 1894 */         event.putEvent(otherParent.id);
/* 1895 */         deliveries += otherParent.sendEvent(event, count, 524288, null, 0);
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*      */ 
/* 1901 */     return deliveries;
/*      */   }
/*      */   
/*      */   int sendEvent(Event event, int count, int filter, XClient c) throws IOException
/*      */   {
/* 1906 */     if ((this.eventMask & filter) != 0) {
/* 1907 */       if (this.XClient == c) return 0;
/* 1908 */       return this.XClient.sendEvent(event, count, this.eventMask, filter, null);
/*      */     }
/*      */     
/* 1911 */     for (OtherClients other = this.optional == null ? null : this.optional.otherClients; 
/* 1912 */         other != null; other = (OtherClients)other.next) {
/* 1913 */       if ((other.mask & filter) != 0) {
/* 1914 */         if (other.sameClient(c)) return 0;
/* 1915 */         if (other.getClient() == null)
/* 1916 */           return 0;
/* 1917 */         return other.getClient().sendEvent(event, count, other.mask, filter, null);
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*      */ 
/* 1923 */     return 2;
/*      */   }
/*      */   
/*      */   private XWindow findOptional() {
/* 1927 */     XWindow w = this;
/* 1928 */     do { w = w.parent; } while (w.optional == null);
/* 1929 */     return w;
/*      */   }
/*      */   
/*      */   private void setDefault() {
/* 1933 */     this.prevSib = null;
/* 1934 */     this.firstChild = null;
/* 1935 */     this.lastChild = null;
/* 1936 */     this.optional = null;
/* 1937 */     this.attr = 0;
/*      */     
/* 1939 */     this.attr |= 0x8;
/*      */     
/* 1941 */     this.attr |= 0x60000;
/* 1942 */     this.attr |= 0x1000;
/*      */     
/* 1944 */     this.eventMask = 0;
/* 1945 */     this.attr |= 0x8000000;
/* 1946 */     this.attr |= 0x4000000;
/*      */   }
/*      */   
/*      */   public static final void reqUnmapSubWindows(XClient c) throws IOException
/*      */   {
/* 1951 */     ComChannel comChannel = c.channel;
/* 1952 */     int foo = comChannel.readInt();
/*      */     
/* 1954 */     c.length -= 2;
/*      */     
/* 1956 */     XWindow w = c.lookupWindow(foo);
/* 1957 */     if (w == null) {
/* 1958 */       c.errorValue = foo;
/* 1959 */       c.errorReason = 3;
/* 1960 */       return;
/*      */     }
/* 1962 */     synchronized (LOCK) {
/* 1963 */       w.unmapSubwindows(c);
/*      */     }
/*      */   }
/*      */   
/*      */   private void unmapSubwindows(XClient c) throws IOException {
/* 1968 */     if (this.firstChild == null) { return;
/*      */     }
/* 1970 */     boolean wasRealized = (this.attr & 0x100000) != 0;
/* 1971 */     boolean wasViewable = (this.attr & 0x200000) != 0;
/* 1972 */     boolean parentNotify = subSend();
/* 1973 */     boolean anyMarked = false;
/*      */     
/* 1975 */     for (XWindow child = this.lastChild; child != null; child = child.prevSib) {
/* 1976 */       if ((child.attr & 0x80000) != 0) {
/* 1977 */         if ((parentNotify) || (child.strSend())) {
/* 1978 */           c.cevent.mkUnmapNotify(child.id, 0);
/* 1979 */           child.sendEvent(c.cevent, 1, null);
/*      */         }
/*      */         
/* 1982 */         child.attr &= 0xFFF7FFFF;
/*      */         
/* 1984 */         if ((child.attr & 0x100000) != 0) {
/* 1985 */           child.unrealizeTree(false);
/*      */         }
/*      */       }
/*      */     }
/*      */     
/* 1990 */     if (wasRealized) {
/* 1991 */       restructured();
/*      */     }
/*      */   }
/*      */   
/*      */   public static final void reqMapSubWindows(XClient c)
/*      */     throws IOException
/*      */   {
/* 1998 */     XWindow firstMapped = null;
/* 1999 */     boolean anyMarked = false;
/*      */     
/* 2001 */     ComChannel comChannel = c.channel;
/*      */     
/* 2003 */     int foo = comChannel.readInt();
/*      */     
/* 2005 */     c.length -= 2;
/*      */     
/* 2007 */     XWindow w = c.lookupWindow(foo);
/* 2008 */     if (w == null) {
/* 2009 */       c.errorValue = foo;
/* 2010 */       c.errorReason = 3;
/* 2011 */       return;
/*      */     }
/*      */     
/* 2014 */     boolean parentRedirect = w.parent.redirectSend();
/* 2015 */     boolean parentNotify = w.parent.subSend();
/*      */     
/* 2017 */     synchronized (LOCK) {
/* 2018 */       for (XWindow tmpw = w.firstChild; tmpw != null; tmpw = tmpw.nextSib) {
/* 2019 */         if ((w.screen.root != w) && (w.parent == null)) return;
/* 2020 */         if ((tmpw.attr & 0x80000) == 0) {
/* 2021 */           if ((parentRedirect) && ((tmpw.attr & 0x10000) == 0)) {
/* 2022 */             c.cevent.mkMapRequest(w.id, tmpw.id);
/* 2023 */             if (w.sendEvent(c.cevent, 1, 1048576, c) == 1)
/*      */             {
/* 2025 */               return;
/*      */             }
/*      */           }
/*      */           
/* 2029 */           tmpw.attr |= 0x80000;
/*      */           
/* 2031 */           if ((parentNotify) || (w.strSend())) {
/* 2032 */             c.cevent.mkMapNotify(tmpw.id, (tmpw.attr & 0x10000) >> 16);
/*      */             
/* 2034 */             tmpw.sendEvent(c.cevent, 1, null);
/*      */           }
/* 2036 */           if (firstMapped == null) firstMapped = tmpw;
/* 2037 */           if ((w.attr & 0x100000) != 0) {
/* 2038 */             tmpw.realizeTree();
/*      */           }
/*      */         }
/*      */       }
/*      */       
/* 2043 */       if (firstMapped != null) {
/* 2044 */         restructured();
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */   public static final void reqUnmapWindow(XClient c) throws IOException
/*      */   {
/* 2051 */     ComChannel comChannel = c.channel;
/*      */     
/* 2053 */     int foo = comChannel.readInt();
/*      */     
/* 2055 */     c.length -= 2;
/*      */     
/* 2057 */     XWindow w = c.lookupWindow(foo);
/* 2058 */     if (w == null) {
/* 2059 */       c.errorValue = foo;
/* 2060 */       c.errorReason = 3;
/* 2061 */       return;
/*      */     }
/* 2063 */     synchronized (LOCK) {
/* 2064 */       w.unmapWindow(false);
/*      */     }
/*      */   }
/*      */   
/*      */   private void unrealizeTree(boolean fromConfigure) {
/* 2069 */     XWindow child = this;
/*      */     for (;;) {
/* 2071 */       if ((child.attr & 0x100000) != 0) {
/* 2072 */         child.attr &= 0xFFEFFFFF;
/* 2073 */         child.attr |= 0x60000;
/* 2074 */         child.ddxwindow.setVisible(false);
/*      */         
/* 2076 */         if ((this.screen.windowmode != 0) && (child.hasFrame())) {
/* 2077 */           child.getFrame().setVisible(false);
/*      */         }
/*      */         try {
/* 2080 */           child.deleteEvent(false);
/*      */         }
/*      */         catch (Exception e) {}
/*      */         
/*      */ 
/* 2085 */         if ((child.attr & 0x200000) != 0) {
/* 2086 */           child.attr &= 0xFFDFFFFF;
/*      */         }
/* 2088 */         if (child.firstChild != null) {
/* 2089 */           child = child.firstChild;
/* 2090 */           continue;
/*      */         }
/*      */       }
/* 2093 */       while ((child.nextSib == null) && (child != this))
/* 2094 */         child = child.parent;
/* 2095 */       if (child == this) break;
/* 2096 */       child = child.nextSib;
/*      */     }
/*      */     
/* 2099 */     if ((this.parent != null) && (this.borderWidth > 0)) {
/* 2100 */       this.parent.ddxwindow.draw(this.origin.x - this.borderWidth, this.origin.y - this.borderWidth, this.width + 2 * this.borderWidth, this.height + 2 * this.borderWidth);
/*      */     }
/*      */   }
/*      */   
/*      */   private void realizeTree() throws IOException
/*      */   {
/* 2106 */     XWindow child = this;
/*      */     for (;;) {
/* 2108 */       if ((child.attr & 0x80000) != 0) {
/* 2109 */         child.attr |= 0x100000;
/* 2110 */         child.attr |= (child.clss == 1 ? 2097152 : 0);
/* 2111 */         child.attr &= 0xFFF9FFFF;
/* 2112 */         child.attr |= 0x0;
/* 2113 */         if ((this.screen.windowmode != 0) && (child.hasFrame()) && 
/* 2114 */           ((0 <= child.x) || (0 <= child.y) || (0 < child.x + child.width) || (0 < child.y + child.height)) && (this.clss != 2))
/*      */         {
/*      */ 
/* 2117 */           child.getFrame().setVisible(true);
/*      */         }
/*      */         
/* 2120 */         child.ddxwindow.setVisible(true);
/* 2121 */         child.ddxwindow.draw();
/* 2122 */         if (((child.eventMask | child.getOtherEventMask()) & 0x10000) != 0) {
/*      */           try {
/* 2124 */             child.sendVisibilityNotify();
/*      */           } catch (Exception e) {}
/*      */         }
/* 2127 */         if (child.firstChild != null) {
/* 2128 */           child = child.firstChild;
/* 2129 */           continue;
/*      */         }
/*      */       }
/* 2132 */       while ((child.nextSib == null) && (child != this)) {
/* 2133 */         child = child.parent;
/*      */       }
/* 2135 */       if (child == this) {
/* 2136 */         return;
/*      */       }
/* 2138 */       child = child.nextSib;
/*      */     }
/*      */   }
/*      */   
/*      */   private void unmapWindow(boolean fromConfigure) throws IOException {
/* 2143 */     Screen screen = this.screen;
/* 2144 */     boolean wasRealized = (this.attr & 0x100000) != 0;
/* 2145 */     boolean wasViewable = (this.attr & 0x200000) != 0;
/* 2146 */     XWindow layerWin = this;
/*      */     
/* 2148 */     if ((this.attr & 0x80000) == 0) { return;
/*      */     }
/* 2150 */     if (this.parent == null) return;
/* 2151 */     if (substrSend()) {
/* 2152 */       Event event = new Event();
/* 2153 */       event.mkUnmapNotify(this.id, fromConfigure ? 1 : 0);
/* 2154 */       try { sendEvent(event, 1, null);
/*      */       }
/*      */       catch (Exception e) {}
/*      */     }
/* 2158 */     this.attr &= 0xFFF7FFFF;
/* 2159 */     if (wasRealized) {
/* 2160 */       unrealizeTree(fromConfigure);
/*      */     }
/*      */     
/* 2163 */     if ((wasRealized) && (!fromConfigure)) {
/* 2164 */       restructured();
/*      */     }
/*      */   }
/*      */   
/*      */   public static final void reqMapWindow(XClient c) throws IOException
/*      */   {
/* 2170 */     ComChannel comChannel = c.channel;
/*      */     
/* 2172 */     int foo = comChannel.readInt();
/*      */     
/* 2174 */     c.length -= 2;
/*      */     
/* 2176 */     XWindow w = c.lookupWindow(foo);
/* 2177 */     if (w == null) {
/* 2178 */       c.errorValue = foo;
/* 2179 */       c.errorReason = 3;
/* 2180 */       return;
/*      */     }
/* 2182 */     synchronized (LOCK) {
/* 2183 */       w.mapWindow(c);
/*      */     }
/*      */   }
/*      */   
/*      */   public void mapWindow(XClient c) throws IOException {
/* 2188 */     if ((this.attr & 0x80000) != 0) return;
/* 2189 */     this.screen = this.screen;
/* 2190 */     if (this.parent != null) {
/* 2191 */       if (((this.attr & 0x10000) == 0) && (this.parent.redirectSend())) {
/* 2192 */         c.cevent.mkMapRequest(this.parent.id, this.id);
/* 2193 */         int ii = this.parent.sendEvent(c.cevent, 1, 1048576, c);
/*      */         
/* 2195 */         if (ii == 1) return;
/*      */       }
/* 2197 */       this.attr |= 0x80000;
/* 2198 */       if (substrSend()) {
/* 2199 */         c.cevent.mkMapNotify(this.id, (this.attr & 0x10000) >> 16);
/*      */         
/* 2201 */         sendEvent(c.cevent, 1, null);
/*      */       }
/* 2203 */       if ((this.parent.attr & 0x100000) == 0) return;
/* 2204 */       realizeTree();
/* 2205 */       restructured();
/*      */     }
/*      */     else {
/* 2208 */       this.attr |= 0x80000;
/* 2209 */       this.attr |= 0x100000;
/* 2210 */       this.attr |= (this.clss == 1 ? 2097152 : 0);
/*      */     }
/*      */   }
/*      */   
/*      */   public static void restructured() throws IOException {
/* 2215 */     try { checkMotion(null, null);
/*      */     }
/*      */     catch (Exception e) {}
/*      */   }
/*      */   
/*      */   public static boolean checkMotion(Event e, XWindow hint) throws IOException {
/* 2221 */     XWindow prevSpriteWin = sprite.win;
/* 2222 */     sprite.win = xy2Window(sprite.hot.x, sprite.hot.y, hint);
/* 2223 */     if (sprite.win != prevSpriteWin) {
/* 2224 */       if (prevSpriteWin != null) {
/* 2225 */         try { enter_leaveEvent(prevSpriteWin, sprite.win, 0);
/*      */         }
/*      */         catch (Exception ee) {}
/*      */       }
/*      */       
/* 2230 */       return false;
/*      */     }
/* 2232 */     return true;
/*      */   }
/*      */   
/*      */   public static void enter_leaveEvent(int type, int mode, int detail, XWindow win, int child)
/*      */     throws IOException
/*      */   {
/* 2238 */     Grab grab = grab;
/* 2239 */     int mask; if (grab != null) {
/* 2240 */       int mask = win == grab.window ? grab.eventMask : 0;
/* 2241 */       if ((grab.attr & 0x1) != 0) {
/* 2242 */         mask |= win.getEventMask(grab.getClient());
/*      */       }
/*      */     }
/*      */     else {
/* 2246 */       mask = win.eventMask | win.getOtherEventMask();
/*      */     }
/*      */     
/* 2249 */     if ((mask & Event.filters[type]) != 0) {
/* 2250 */       Event event = new Event();
/* 2251 */       event.fixUpEventFromWindow(win, 0, sprite.hot.x, sprite.hot.y, false);
/* 2252 */       int flags = 0;
/* 2253 */       flags = event.getSameScreen() != 0 ? 2 : 0;
/*      */       
/* 2255 */       flags |= 0x1;
/*      */       
/* 2257 */       mode = 0;
/* 2258 */       event.mkPointer(type, detail, (int)System.currentTimeMillis(), child, sprite.hot.x, sprite.hot.y, sprite.hot.state, mode, flags);
/*      */       
/*      */       try
/*      */       {
/* 2262 */         if (grab != null) {
/* 2263 */           if (grab.getClient() != null) {
/* 2264 */             grab.getClient().sendEvent(event, 1, mask, Event.filters[type], grab);
/*      */           }
/*      */         } else {
/* 2267 */           win.sendEvent(event, 1, Event.filters[type], null, 0);
/*      */         }
/*      */       }
/*      */       catch (Exception e) {}
/*      */     }
/*      */     
/*      */ 
/*      */ 
/* 2275 */     if ((type == 7) && ((mask & 0x4000) != 0)) {}
/*      */   }
/*      */   
/*      */ 
/*      */   private int getEventMask(XClient c)
/*      */   {
/* 2281 */     if (c == null) return 0;
/* 2282 */     if (this.XClient == c) return this.eventMask;
/* 2283 */     if (this.XClient == null) return 0;
/* 2284 */     for (OtherClients other = getOtherClients(); 
/* 2285 */         other != null; 
/* 2286 */         other = (OtherClients)other.next) {
/* 2287 */       if (other.sameClient(c)) return other.mask;
/*      */     }
/* 2289 */     return 0;
/*      */   }
/*      */   
/*      */   private static void enterNotifies(XWindow ancestor, XWindow child, int mode, int detail) throws IOException
/*      */   {
/* 2294 */     XWindow parent = child.parent;
/* 2295 */     if (ancestor == parent) return;
/* 2296 */     enterNotifies(ancestor, parent, mode, detail);
/* 2297 */     enter_leaveEvent(7, mode, detail, parent, child.id);
/*      */   }
/*      */   
/*      */   private static void leaveNotifies(XWindow child, XWindow ancestor, int mode, int detail) throws IOException
/*      */   {
/* 2302 */     if (ancestor == child) return;
/* 2303 */     for (XWindow win = child.parent; win != ancestor; win = win.parent) {
/* 2304 */       enter_leaveEvent(8, mode, detail, win, child.id);
/* 2305 */       child = win;
/*      */     }
/*      */   }
/*      */   
/*      */   public static void enter_leaveEvent(XWindow fromWin, XWindow toWin, int mode) throws IOException
/*      */   {
/* 2311 */     if (fromWin == toWin) return;
/* 2312 */     if (fromWin.isParent(toWin)) {
/*      */       try {
/* 2314 */         enter_leaveEvent(8, mode, 2, fromWin, 0);
/*      */       }
/*      */       catch (Exception e) {}
/*      */       try
/*      */       {
/* 2319 */         enterNotifies(fromWin, toWin, mode, 1);
/*      */       }
/*      */       catch (Exception e) {}
/*      */       try
/*      */       {
/* 2324 */         enter_leaveEvent(7, mode, 0, toWin, 0);
/*      */ 
/*      */       }
/*      */       catch (Exception e) {}
/*      */     }
/* 2329 */     else if (toWin.isParent(fromWin)) {
/*      */       try {
/* 2331 */         enter_leaveEvent(8, mode, 0, fromWin, 0);
/*      */       }
/*      */       catch (Exception e) {}
/*      */       try
/*      */       {
/* 2336 */         leaveNotifies(fromWin, toWin, mode, 1);
/*      */       }
/*      */       catch (Exception e) {}
/*      */       try
/*      */       {
/* 2341 */         enter_leaveEvent(7, mode, 2, toWin, 0);
/*      */       }
/*      */       catch (Exception e) {}
/*      */     }
/*      */     else
/*      */     {
/* 2347 */       XWindow common = toWin.commonAncestor(fromWin);
/*      */       try {
/* 2349 */         enter_leaveEvent(8, mode, 3, fromWin, 0);
/*      */       }
/*      */       catch (Exception e) {}
/*      */       try
/*      */       {
/* 2354 */         leaveNotifies(fromWin, common, mode, 4);
/*      */       }
/*      */       catch (Exception e) {}
/*      */       try
/*      */       {
/* 2359 */         enterNotifies(common, toWin, mode, 4);
/*      */       }
/*      */       catch (Exception e) {}
/*      */       try
/*      */       {
/* 2364 */         enter_leaveEvent(7, mode, 3, toWin, 0);
/*      */       }
/*      */       catch (Exception e) {}
/*      */     }
/*      */   }
/*      */   
/*      */   boolean contains(int xx, int yy)
/*      */   {
/* 2372 */     return this.ddxwindow.contains(xx - this.x - this.borderWidth, yy - this.y - this.borderWidth);
/*      */   }
/*      */   
/*      */   public static XWindow xy2Window(int x, int y, XWindow hint) {
/* 2376 */     if (hint != spriteTrace[(spriteTraceGood - 1)]) {
/* 2377 */       spriteTraceGood = 1;
/*      */     }
/*      */     
/*      */ 
/* 2381 */     XWindow win = spriteTrace[(spriteTraceGood - 1)];
/*      */     
/* 2383 */     if (((win.attr & 0x80000) == 0) || (!win.contains(x, y))) {
/* 2384 */       win = spriteTrace[0];
/* 2385 */       spriteTraceGood = 1;
/*      */     }
/*      */     
/* 2388 */     win = win.firstChild;
/* 2389 */     while (win != null) {
/* 2390 */       if (((win.attr & 0x80000) != 0) && (win.contains(x, y))) {
/* 2391 */         if (spriteTraceGood >= spriteTrace.length) {
/* 2392 */           XWindow[] foo = new XWindow[spriteTrace.length + 10];
/* 2393 */           System.arraycopy(spriteTrace, 0, foo, 0, spriteTrace.length);
/* 2394 */           spriteTrace = foo;
/*      */         }
/* 2396 */         spriteTrace[(spriteTraceGood++)] = win;
/* 2397 */         win = win.firstChild;
/*      */       }
/*      */       else {
/* 2400 */         win = win.nextSib;
/*      */       }
/*      */     }
/*      */     
/* 2404 */     return spriteTrace[(spriteTraceGood - 1)];
/*      */   }
/*      */   
/*      */   Cursor getCursor() {
/* 2408 */     if (this.optional != null) return this.optional.cursor;
/* 2409 */     XWindow p = findOptional();
/* 2410 */     return p.optional.cursor;
/*      */   }
/*      */   
/*      */   private boolean redirectSend() {
/* 2414 */     return ((this.eventMask | getOtherEventMask()) & 0x100000) != 0;
/*      */   }
/*      */   
/* 2417 */   private boolean subSend() { return ((this.eventMask | getOtherEventMask()) & 0x80000) != 0; }
/*      */   
/*      */   private boolean strSend() {
/* 2420 */     return ((this.eventMask | getOtherEventMask()) & 0x20000) != 0;
/*      */   }
/*      */   
/* 2423 */   private boolean substrSend() { return (strSend()) || (this.parent.subSend()); }
/*      */   
/*      */   void makeOptional()
/*      */   {
/* 2427 */     if (this.optional != null) return;
/* 2428 */     this.optional = new WindowOpt();
/* 2429 */     this.optional.dontPropagateMask = DontPropagate.masks[((this.attr & 0x1C00000) >> 22)];
/* 2430 */     this.optional.otherEventMasks = 0;
/* 2431 */     this.optional.backingBitPlanes = -1L;
/* 2432 */     this.optional.backingPixel = 0L;
/* 2433 */     if (this.parent != null) {
/* 2434 */       WindowOpt parentOptional = findOptional().optional;
/* 2435 */       this.optional.visual = parentOptional.visual;
/* 2436 */       if ((this.attr & 0x8) == 0) {
/* 2437 */         this.optional.cursor = parentOptional.cursor;
/*      */       }
/*      */       else {
/* 2440 */         this.optional.cursor = null;
/*      */       }
/* 2442 */       this.optional.colormap = parentOptional.colormap;
/*      */     }
/*      */   }
/*      */   
/*      */   private void sendVisibilityNotify() throws IOException {
/* 2447 */     Event event = new Event();
/* 2448 */     event.mkVisibilityNotify(this.id, (this.attr & 0x60000) >> 17);
/* 2449 */     sendEvent(event, 1, null);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static final void reqQueryTree(XClient c)
/*      */     throws IOException
/*      */   {
/* 2461 */     ComChannel comChannel = c.channel;
/* 2462 */     int wid = comChannel.readInt();
/* 2463 */     c.length -= 2;
/* 2464 */     XWindow w = c.lookupWindow(wid);
/* 2465 */     if (w == null) {
/* 2466 */       c.errorValue = wid;
/* 2467 */       c.errorReason = 3;
/* 2468 */       return;
/*      */     }
/*      */     
/* 2471 */     synchronized (LOCK) {
/* 2472 */       int numchild = 0;
/*      */       
/* 2474 */       for (XWindow p = w.lastChild; p != null; p = p.prevSib) {
/* 2475 */         numchild++;
/*      */       }
/*      */       
/* 2478 */       synchronized (comChannel) {
/* 2479 */         comChannel.writeByte(1);
/* 2480 */         comChannel.writePad(1);
/* 2481 */         comChannel.writeShort(c.getSequence());
/* 2482 */         comChannel.writeInt(numchild);
/* 2483 */         comChannel.writeInt(w.screen.rootId);
/*      */         
/* 2485 */         if (w.parent != null) comChannel.writeInt(w.parent.id); else {
/* 2486 */           comChannel.writeInt(0);
/*      */         }
/* 2488 */         comChannel.writeShort(numchild);
/* 2489 */         comChannel.writePad(14);
/*      */         
/* 2491 */         if (0 < numchild) {
/* 2492 */           for (XWindow p = w.lastChild; p != null; p = p.prevSib) {
/* 2493 */             comChannel.writeInt(p.id);
/*      */           }
/*      */         }
/* 2496 */         comChannel.flush();
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */   public static final void reqSetInputFocus(XClient c) throws IOException
/*      */   {
/* 2503 */     ComChannel comChannel = c.channel;
/*      */     
/* 2505 */     int reverto = c.data;
/* 2506 */     int foo = comChannel.readInt();
/* 2507 */     c.length -= 2;
/* 2508 */     XWindow win = null;
/* 2509 */     if ((foo != 0) && (foo != 1)) {
/* 2510 */       win = c.lookupWindow(foo);
/* 2511 */       if (win == null) {
/* 2512 */         c.errorValue = foo;
/* 2513 */         c.errorReason = 3;
/* 2514 */         return;
/*      */       }
/*      */     }
/*      */     
/* 2518 */     int time = comChannel.readInt();
/* 2519 */     c.length -= 1;
/*      */     
/* 2521 */     setInputFocus(c, foo, reverto, time, false);
/*      */   }
/*      */   
/*      */ 
/*      */   public static final void setInputFocus(XClient c, int focusId, int revertTo, int time, boolean followOk)
/*      */     throws IOException
/*      */   {
/* 2528 */     if ((revertTo != 2) && (revertTo != 1) && (revertTo != 0))
/*      */     {
/*      */ 
/* 2531 */       c.errorValue = revertTo;
/* 2532 */       c.errorReason = 2;
/* 2533 */       return;
/*      */     }
/*      */     
/* 2536 */     int focusWin = 0;
/* 2537 */     XWindow win = null;
/* 2538 */     if ((focusId == 0) || (focusId == 1)) {
/* 2539 */       focusWin = focusId;
/*      */     } else {
/* 2541 */       if ((win = c.lookupWindow(focusId)) == null) {
/* 2542 */         c.errorValue = focusId;
/* 2543 */         c.errorReason = 3;
/* 2544 */         return;
/*      */       }
/*      */       
/* 2547 */       if (!win.isRealized()) {
/* 2548 */         c.errorReason = 8;
/* 2549 */         return;
/*      */       }
/* 2551 */       focusWin = focusId;
/*      */     }
/*      */     
/* 2554 */     int mode = grab != null ? 3 : 0;
/* 2555 */     doFocusEvents(c, focus.win, focusWin, mode);
/* 2556 */     focus.time = time;
/* 2557 */     focus.revert = revertTo;
/* 2558 */     focus.win = focusWin;
/* 2559 */     focus.XWindow = win;
/*      */     
/* 2561 */     if ((focusWin == 0) || (focusWin == 1)) {
/* 2562 */       focus.traceGood = 0;
/* 2563 */       if (focusWin == 1) {
/* 2564 */         Screen.screen[0].root.ddxwindow.requestFocus();
/*      */       }
/*      */     } else {
/* 2567 */       synchronized (LOCK) {
/* 2568 */         win.ddxwindow.requestFocus();
/* 2569 */         int depth = 0;
/* 2570 */         for (XWindow tmpw = win; tmpw != null; tmpw = tmpw.parent) depth++;
/* 2571 */         if (depth > focus.traceSize) {
/* 2572 */           focus.traceSize = (depth + 1);
/* 2573 */           focus.trace = new XWindow[focus.traceSize];
/*      */         }
/* 2575 */         focus.traceGood = depth;
/* 2576 */         depth--;
/* 2577 */         for (XWindow tmpw = win; tmpw != null; depth--) {
/* 2578 */           focus.trace[depth] = tmpw;tmpw = tmpw.parent;
/*      */         }
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */   public static final void reqGetInputFocus(XClient c) throws IOException
/*      */   {
/* 2586 */     ComChannel comChannel = c.channel;
/*      */     
/* 2588 */     synchronized (comChannel) {
/* 2589 */       comChannel.writeByte(1);
/* 2590 */       comChannel.writeByte(focus.revert);
/* 2591 */       comChannel.writeShort(c.getSequence());
/* 2592 */       comChannel.writeInt(0);
/* 2593 */       comChannel.writeInt(focus.win);
/* 2594 */       comChannel.writePad(20);
/* 2595 */       comChannel.flush();
/*      */     }
/*      */   }
/*      */   
/*      */   public static final void reqGetWindowAttributes(XClient c) throws IOException
/*      */   {
/* 2601 */     ComChannel comChannel = c.channel;
/*      */     
/* 2603 */     int foo = comChannel.readInt();
/* 2604 */     c.length -= 2;
/* 2605 */     XWindow w = c.lookupWindow(foo);
/* 2606 */     if (w == null) {
/* 2607 */       c.errorValue = foo;
/* 2608 */       c.errorReason = 3;
/* 2609 */       return;
/*      */     }
/*      */     
/* 2612 */     synchronized (comChannel) {
/* 2613 */       comChannel.writeByte(1);
/*      */       
/* 2615 */       if (((w.attr & 0x2000000) != 0) && ((w.attr & 0x30) != 2)) {
/* 2616 */         comChannel.writeByte(0);
/*      */       } else {
/* 2618 */         comChannel.writeByte((w.attr & 0x30) >> 4);
/*      */       }
/* 2620 */       comChannel.writeShort(c.getSequence());
/* 2621 */       comChannel.writeInt(3);
/* 2622 */       comChannel.writeInt(w.getVisual());
/* 2623 */       comChannel.writeShort(w.clss);
/* 2624 */       comChannel.writeByte((w.attr & 0xF00) >> 8);
/* 2625 */       comChannel.writeByte((w.attr & 0xF000) >> 12);
/* 2626 */       comChannel.writeInt(w.getBackingBitPlanes());
/* 2627 */       comChannel.writeInt(w.getBackingPixel());
/* 2628 */       comChannel.writeByte((w.attr & 0x40) >> 6);
/*      */       
/* 2630 */       if (w.getColormapId() == 0) comChannel.writeByte(0); else {
/* 2631 */         comChannel.writeByte(Colormap.isMapInstalled(w.getColormapId(), w));
/*      */       }
/* 2633 */       if ((w.attr & 0x80000) == 0) { comChannel.writeByte(0);
/* 2634 */       } else if ((w.attr & 0x100000) != 0) comChannel.writeByte(2); else {
/* 2635 */         comChannel.writeByte(1);
/*      */       }
/* 2637 */       comChannel.writeByte((w.attr & 0x10000) >> 16);
/* 2638 */       comChannel.writeInt(w.getColormapId());
/* 2639 */       comChannel.writeInt(w.eventMask | w.getOtherEventMask());
/* 2640 */       comChannel.writeInt(w.getEventMask(c));
/* 2641 */       comChannel.writeShort(w.getDontPropagateMask());
/* 2642 */       comChannel.writePad(2);
/* 2643 */       comChannel.flush();
/*      */     }
/*      */   }
/*      */   
/*      */   public static final void reqCopyPlane(XClient c) throws IOException
/*      */   {
/* 2649 */     Drawable dsrc = null;Drawable ddst = null;
/* 2650 */     ComChannel comChannel = c.channel;
/* 2651 */     int foo = comChannel.readInt();
/* 2652 */     dsrc = c.lookupDrawable(foo);
/* 2653 */     if (dsrc == null) {
/* 2654 */       c.errorValue = foo;
/* 2655 */       c.errorReason = 9;
/*      */     }
/*      */     
/* 2658 */     int dest = comChannel.readInt();
/* 2659 */     ddst = c.lookupDrawable(dest);
/* 2660 */     if ((ddst == null) && (c.errorReason == 0)) {
/* 2661 */       c.errorValue = dest;
/* 2662 */       c.errorReason = 9;
/*      */     }
/* 2664 */     foo = comChannel.readInt();
/* 2665 */     GC gc = c.lookupGC(foo);
/* 2666 */     if ((gc == null) && (c.errorReason == 0)) {
/* 2667 */       c.errorValue = foo;
/* 2668 */       c.errorReason = 13;
/*      */     }
/*      */     
/*      */ 
/* 2672 */     int sx = (short)comChannel.readShort();
/* 2673 */     int sy = (short)comChannel.readShort();
/*      */     
/*      */ 
/*      */ 
/* 2677 */     int destx = (short)comChannel.readShort();
/* 2678 */     int dx = destx - sx;
/*      */     
/* 2680 */     int desty = (short)comChannel.readShort();
/* 2681 */     int dy = desty - sy;
/*      */     
/* 2683 */     int width = comChannel.readShort();
/* 2684 */     int height = comChannel.readShort();
/*      */     
/* 2686 */     int bplane = comChannel.readInt();
/* 2687 */     c.length -= 8;
/*      */     
/* 2689 */     if (c.errorReason != 0) {
/* 2690 */       return;
/*      */     }
/*      */     
/* 2693 */     Graphics g = ddst.getGraphics();
/* 2694 */     if (((dsrc instanceof XWindow)) && (!((XWindow)dsrc).ddxwindow.isVisible())) {
/* 2695 */       g = null;
/*      */     }
/*      */     
/* 2698 */     if ((dsrc.width <= sx) || (dsrc.height <= sy) || (sx + width <= 0) || (sy + height <= 0) || (destx + width <= 0) || (desty + height <= 0))
/*      */     {
/*      */ 
/* 2701 */       g = null;
/*      */     }
/*      */     
/* 2704 */     if (g != null) {
/* 2705 */       if ((dsrc instanceof XWindow)) {
/* 2706 */         if ((ddst instanceof XWindow)) {
/* 2707 */           ((XWindow)dsrc).ddxwindow.copyArea((XWindow)ddst, gc, sx, sy, width, height, destx, desty);
/*      */         }
/*      */       }
/*      */       else
/*      */       {
/* 2712 */         Image img = null;
/* 2713 */         if ((ddst instanceof Pixmap)) {
/* 2714 */           ((Pixmap)dsrc).copyPlane((Pixmap)ddst, gc, sx, sy, destx, desty, width, height);
/*      */         }
/*      */         else
/*      */         {
/* 2718 */           img = ((Pixmap)dsrc).getImage((XWindow)ddst, gc, sx, sy, width, height);
/* 2719 */           XWindow wdst = (XWindow)ddst;
/*      */           
/* 2721 */           if ((sx == 0) && (sy == 0) && (width == dsrc.width) && (height == dsrc.height)) {
/* 2722 */             wdst.ddxwindow.drawImage(gc.clip_mask, img, destx, desty, width, height);
/*      */ 
/*      */           }
/*      */           else
/*      */           {
/*      */ 
/* 2728 */             Shape tmp = g.getClip();
/* 2729 */             g.clipRect(destx, desty, width, height);
/* 2730 */             wdst.ddxwindow.drawImage(gc.clip_mask, img, destx - sx, desty - sy, dsrc.width, dsrc.height);
/*      */             
/*      */ 
/*      */ 
/* 2734 */             if (tmp == null) g.setClip(0, 0, wdst.width, wdst.height); else
/* 2735 */               g.setClip(tmp);
/*      */           }
/* 2737 */           wdst.draw(destx, desty, width, height);
/* 2738 */           if (img != ((Pixmap)dsrc).getImage()) {
/* 2739 */             img.flush();
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */   public static final void reqCopyArea(XClient c) throws IOException
/*      */   {
/* 2748 */     Drawable dsrc = null;Drawable ddst = null;
/* 2749 */     ComChannel comChannel = c.channel;
/*      */     
/* 2751 */     int foo = comChannel.readInt();
/* 2752 */     dsrc = c.lookupDrawable(foo);
/* 2753 */     if (dsrc == null) {
/* 2754 */       c.errorValue = foo;
/* 2755 */       c.errorReason = 9;
/*      */     }
/*      */     
/* 2758 */     int dest = comChannel.readInt();
/* 2759 */     ddst = c.lookupDrawable(dest);
/* 2760 */     if ((ddst == null) && (c.errorReason == 0)) {
/* 2761 */       c.errorValue = dest;
/* 2762 */       c.errorReason = 9;
/*      */     }
/*      */     
/* 2765 */     foo = comChannel.readInt();
/*      */     
/* 2767 */     GC gc = c.lookupGC(foo);
/*      */     
/* 2769 */     if ((gc == null) && (c.errorReason == 0)) {
/* 2770 */       c.errorValue = foo;
/* 2771 */       c.errorReason = 13;
/*      */     }
/*      */     
/*      */ 
/* 2775 */     int sx = (short)comChannel.readShort();
/* 2776 */     int sy = (short)comChannel.readShort();
/*      */     
/*      */ 
/* 2779 */     int destx = (short)comChannel.readShort();
/* 2780 */     int desty = (short)comChannel.readShort();
/*      */     
/*      */ 
/* 2783 */     int width = comChannel.readShort();
/* 2784 */     int height = comChannel.readShort();
/*      */     
/* 2786 */     c.length -= 7;
/*      */     
/* 2788 */     if (c.errorReason != 0) {
/* 2789 */       return;
/*      */     }
/*      */     
/* 2792 */     Graphics g = ddst.getGraphics();
/* 2793 */     if (((dsrc instanceof XWindow)) && (!((XWindow)dsrc).ddxwindow.isVisible())) {
/* 2794 */       g = null;
/*      */     }
/*      */     
/* 2797 */     if ((dsrc.width <= sx) || (dsrc.height <= sy) || (sx + width <= 0) || (sy + height <= 0) || (destx + width <= 0) || (desty + height <= 0))
/*      */     {
/*      */ 
/* 2800 */       g = null;
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 2809 */     if (g != null) {
/* 2810 */       if ((dsrc instanceof XWindow))
/*      */       {
/* 2812 */         if (sx < 0) sx = 0;
/* 2813 */         if (sy < 0) sy = 0;
/* 2814 */         if (destx < 0) destx = 0;
/* 2815 */         if (desty < 0) { desty = 0;
/*      */         }
/* 2817 */         if ((ddst instanceof XWindow)) {
/* 2818 */           ((XWindow)dsrc).ddxwindow.copyArea((XWindow)ddst, gc, sx, sy, width, height, destx, desty);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */         }
/*      */         
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       }
/* 2830 */       else if ((ddst instanceof Pixmap)) {
/* 2831 */         if (sx < 0) sx = 0;
/* 2832 */         if (sy < 0) sy = 0;
/* 2833 */         if (destx < 0) destx = 0;
/* 2834 */         if (desty < 0) { desty = 0;
/*      */         }
/* 2836 */         ((Pixmap)dsrc).copyArea((Pixmap)ddst, gc, sx, sy, destx, desty, width, height);
/*      */       }
/*      */       else
/*      */       {
/* 2840 */         Image img = ((Pixmap)dsrc).getImage((XWindow)ddst, gc, sx, sy, width, height);
/*      */         
/*      */ 
/* 2843 */         XWindow wdst = (XWindow)ddst;
/*      */         
/* 2845 */         if ((sx == 0) && (sy == 0) && (width == dsrc.width) && (height == dsrc.height)) {
/* 2846 */           wdst.ddxwindow.drawImage(gc.clip_mask, img, destx, desty, width, height);
/*      */ 
/*      */         }
/*      */         else
/*      */         {
/* 2851 */           Shape tmp = g.getClip();
/* 2852 */           if ((destx >= 0) && (desty >= 0)) {
/* 2853 */             g.clipRect(destx, desty, width, height);
/*      */           }
/*      */           else {
/* 2856 */             g.clipRect(destx < 0 ? 0 : destx, desty < 0 ? 0 : desty, destx < 0 ? width + destx : width, desty < 0 ? height + desty : height);
/*      */           }
/*      */           
/*      */ 
/*      */ 
/* 2861 */           wdst.ddxwindow.drawImage(gc.clip_mask, img, destx - sx, desty - sy, dsrc.width, dsrc.height);
/*      */           
/*      */ 
/* 2864 */           if (tmp == null) g.setClip(0, 0, wdst.width, wdst.height); else
/* 2865 */             g.setClip(tmp);
/*      */         }
/* 2867 */         wdst.draw(destx, desty, width, height);
/* 2868 */         if (img != ((Pixmap)dsrc).getImage()) {
/* 2869 */           img.flush();
/*      */         }
/*      */       }
/*      */     }
/*      */     
/*      */ 
/* 2875 */     if ((gc.attr & 0x800) != 0) {
/* 2876 */       c.cevent.mkNoExposure(dest, 0, 62);
/* 2877 */       c.sendEvent(c.cevent, 1, 0, 0, null);
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public static final void reqClearArea(XClient c)
/*      */     throws IOException
/*      */   {
/* 2887 */     int exposures = c.data;
/* 2888 */     ComChannel comChannel = c.channel;
/* 2889 */     int foo = comChannel.readInt();
/* 2890 */     c.length -= 2;
/* 2891 */     XWindow w = c.lookupWindow(foo);
/* 2892 */     if (w == null) {
/* 2893 */       c.errorValue = foo;
/* 2894 */       c.errorReason = 3;
/* 2895 */       return;
/*      */     }
/*      */     
/*      */ 
/*      */ 
/* 2900 */     int x = (short)comChannel.readShort();
/* 2901 */     int y = (short)comChannel.readShort();
/* 2902 */     int width = comChannel.readShort();
/* 2903 */     if (width == 0) width = w.width - x;
/* 2904 */     int height = comChannel.readShort();
/* 2905 */     if (height == 0) { height = w.height - y;
/*      */     }
/* 2907 */     c.length -= 2;
/*      */     
/* 2909 */     if (c.errorReason != 0) {
/* 2910 */       return;
/*      */     }
/*      */     
/* 2913 */     if (w.getGraphics() != null) {
/* 2914 */       w.makeBackgroundTile(x, y, width, height);
/* 2915 */       w.draw(x, y, width + 1, height + 1);
/*      */     }
/*      */     
/* 2918 */     if (exposures != 0) {
/* 2919 */       c.cevent.mkExpose(w.id, x, y, width, height, 0);
/* 2920 */       c.sendEvent(c.cevent, 1, Event.filters[12], Event.filters[12], grab);
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */   void draw()
/*      */   {
/* 2927 */     this.ddxwindow.draw();
/*      */   }
/*      */   
/*      */   void draw(int x, int y, int width, int height) {
/* 2931 */     this.ddxwindow.draw(x, y, width, height);
/*      */   }
/*      */   
/*      */   private XWindow trackParent() {
/* 2935 */     XWindow w = this;
/*      */     
/* 2937 */     while (w.optional == null) {
/* 2938 */       w = w.parent;
/*      */     }
/* 2940 */     return w;
/*      */   }
/*      */   
/*      */   private XClient getXClient() {
/* 2944 */     return XClient.X_CLIENTs[((this.id & 0x1FC00000) >> 22)];
/*      */   }
/*      */   
/*      */   private int getBackingBitPlanes() {
/* 2948 */     if (this.optional == null) return -1;
/* 2949 */     return (int)this.optional.backingBitPlanes;
/*      */   }
/*      */   
/*      */   private int getBackingPixel() {
/* 2953 */     if (this.optional == null) return 0;
/* 2954 */     return (int)this.optional.backingPixel;
/*      */   }
/*      */   
/*      */   OtherClients getOtherClients() {
/* 2958 */     if (this.optional == null) return null;
/* 2959 */     return this.optional.otherClients;
/*      */   }
/*      */   
/* 2962 */   public static Frame owner = new Frame();
/* 2963 */   Window frame = null;
/*      */   
/*      */   public static final int poolsize = 5;
/* 2966 */   public static Window[] frames = new Window[5];
/*      */   
/*      */   Window getFrame() {
/* 2969 */     if (hasFrame()) return this.optional.frame;
/* 2970 */     if (this.parent != this.screen.root) {
/* 2971 */       return null;
/*      */     }
/*      */     
/* 2974 */     makeOptional();
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 2983 */     synchronized (XWindow.class) {
/* 2984 */       Window foo = null;
/* 2985 */       for (int i = 0; i < 5; i++) {
/* 2986 */         if (frames[i] != null) {
/* 2987 */           foo = frames[i];
/* 2988 */           frames[i] = null;
/* 2989 */           this.optional.frame = foo;
/* 2990 */           return foo;
/*      */         }
/*      */       }
/*      */     }
/* 2994 */     this.optional.frame = new Window(owner);
/* 2995 */     return this.optional.frame;
/*      */   }
/*      */   
/* 2998 */   boolean hasFrame() { return (this.optional != null) && (this.optional.frame != null); }
/*      */   
/*      */   void delFrame()
/*      */   {
/* 3002 */     if ((this.optional == null) || (this.optional.frame == null)) return;
/* 3003 */     this.optional.frame.setVisible(false);
/* 3004 */     if ((this.screen.windowmode == 2) && ((this.optional.frame instanceof Frame)))
/*      */     {
/* 3006 */       this.optional.frame.removeMouseListener((MouseListener)this.ddxwindow);
/*      */       
/* 3008 */       this.optional.frame = null;
/* 3009 */       return;
/*      */     }
/*      */     
/* 3012 */     synchronized (XWindow.class) {
/* 3013 */       for (int i = 0; i < 5; i++) {
/* 3014 */         if (frames[i] == null) {
/* 3015 */           frames[i] = this.optional.frame;
/* 3016 */           this.optional.frame = null;
/* 3017 */           return;
/*      */         }
/*      */       }
/*      */     }
/* 3021 */     this.optional.frame.dispose();
/* 3022 */     this.optional.frame = null;
/*      */   }
/*      */   
/*      */   void setProperty(Property p) {
/* 3026 */     makeOptional();
/* 3027 */     this.optional.userProps = p;
/*      */   }
/*      */   
/*      */   Property getProperty() {
/* 3031 */     if (this.optional == null) return null;
/* 3032 */     return this.optional.userProps;
/*      */   }
/*      */   
/*      */   public static final void reqConfigureWindow(XClient c)
/*      */     throws IOException
/*      */   {
/* 3038 */     ComChannel comChannel = c.channel;
/* 3039 */     int foo = comChannel.readInt();
/* 3040 */     XWindow win = c.lookupWindow(foo);
/* 3041 */     if (win == null) {
/* 3042 */       c.errorValue = foo;
/* 3043 */       c.errorReason = 3;
/*      */     }
/*      */     
/* 3046 */     int mask = comChannel.readShort();
/* 3047 */     comChannel.readPad(2);
/* 3048 */     c.length -= 3;
/* 3049 */     if (c.errorReason != 0) return;
/* 3050 */     if (((win.clss == 2) && ((mask & 0x10) != 0)) || (((mask & 0x20) != 0) && ((mask & 0x40) == 0)))
/*      */     {
/*      */ 
/* 3053 */       c.errorReason = 8;
/* 3054 */       return;
/*      */     }
/*      */     
/*      */ 
/*      */ 
/* 3059 */     int w = win.width;
/* 3060 */     int h = win.height;
/* 3061 */     int bw = win.borderWidth;
/* 3062 */     XWindow sib = null;
/*      */     
/* 3064 */     synchronized (LOCK) {
/* 3065 */       win = c.lookupWindow(win.id);
/* 3066 */       if (win == null) {
/* 3067 */         c.errorValue = win.id;
/* 3068 */         c.errorReason = 3;
/* 3069 */         return;
/*      */       }
/* 3071 */       if ((win.screen.root != win) && (win.parent == null)) return;
/* 3072 */       int y; int x; int y; if (win.parent != null) {
/* 3073 */         int x = win.x - win.parent.x - bw;
/* 3074 */         y = win.y - win.parent.y - bw;
/*      */       }
/*      */       else {
/* 3077 */         x = win.x;
/* 3078 */         y = win.y;
/*      */       }
/*      */       
/* 3081 */       mask &= 0x7F;
/*      */       
/* 3083 */       int beforex = x;int beforey = y;
/*      */       
/* 3085 */       int smode = 0;
/* 3086 */       int tmp = mask;
/* 3087 */       while ((tmp != 0) && (c.length != 0)) {
/* 3088 */         int index = lowbit(tmp);
/* 3089 */         tmp &= (index ^ 0xFFFFFFFF);
/* 3090 */         switch (index) {
/*      */         case 1: 
/* 3092 */           foo = comChannel.readInt();
/* 3093 */           foo = (short)(foo & 0xFFFF);
/* 3094 */           if (foo == x) mask--; else
/* 3095 */             x = (short)foo;
/* 3096 */           break;
/*      */         case 2: 
/* 3098 */           foo = comChannel.readInt();
/* 3099 */           foo = (short)(foo & 0xFFFF);
/* 3100 */           if (foo == y) mask -= 2; else
/* 3101 */             y = (short)foo;
/* 3102 */           break;
/*      */         case 4: 
/* 3104 */           foo = comChannel.readInt();
/* 3105 */           foo &= 0xFFFF;
/* 3106 */           if (w == foo) mask -= 4; else
/* 3107 */             w = foo;
/* 3108 */           break;
/*      */         case 8: 
/* 3110 */           foo = comChannel.readInt();
/* 3111 */           foo &= 0xFFFF;
/* 3112 */           if (h == foo) mask -= 8; else
/* 3113 */             h = foo;
/* 3114 */           break;
/*      */         case 16: 
/* 3116 */           foo = comChannel.readInt();
/* 3117 */           foo &= 0xFFFF;
/* 3118 */           bw = foo;
/* 3119 */           break;
/*      */         case 32: 
/* 3121 */           foo = comChannel.readInt();
/* 3122 */           sib = c.lookupWindow(foo);
/* 3123 */           if (sib == null) {
/* 3124 */             c.errorValue = foo;
/* 3125 */             c.errorReason = 3;
/*      */           }
/* 3127 */           else if ((sib.parent != win.parent) || (sib == win)) {
/* 3128 */             c.errorReason = 8;
/*      */           }
/*      */           break;
/*      */         case 64: 
/* 3132 */           foo = comChannel.readInt();
/* 3133 */           foo &= 0xFF;
/* 3134 */           smode = foo;
/* 3135 */           if ((smode != 2) && (smode != 3) && (smode != 4) && (smode != 0) && (smode != 1))
/*      */           {
/* 3137 */             c.errorValue = foo;
/* 3138 */             c.errorReason = 2;
/*      */           }
/*      */           break;
/*      */         default: 
/* 3142 */           c.errorValue = mask;
/* 3143 */           c.errorReason = 2;
/* 3144 */           return;
/*      */         }
/* 3146 */         c.length -= 1;
/* 3147 */         if (c.errorReason != 0) {
/* 3148 */           return;
/*      */         }
/*      */       }
/* 3151 */       if (c.length != 0) {
/* 3152 */         c.errorValue = mask;
/* 3153 */         c.errorReason = 2;
/* 3154 */         return;
/*      */       }
/*      */       
/* 3157 */       if (mask == 0) {
/* 3158 */         return;
/*      */       }
/*      */       
/* 3161 */       if (win.parent == null) { return;
/*      */       }
/* 3163 */       if ((mask & 0x40) != 0) {
/* 3164 */         sib = win.getPlaceInTheStack(sib, win.parent.x + x, win.parent.y + y, w + bw * 2, h + bw * 2, smode);
/*      */       }
/*      */       else
/*      */       {
/* 3168 */         sib = win.nextSib;
/*      */       }
/* 3170 */       if (((win.attr & 0x10000) == 0) && (win.parent.redirectSend()))
/*      */       {
/* 3172 */         c.cevent.mkConfigureRequest((mask & 0x40) != 0 ? smode : 0, win.parent.id, win.id, (mask & 0x20) != 0 ? sib.id : 0, x, y, w, h, bw, mask);
/*      */         
/*      */ 
/*      */ 
/*      */ 
/* 3177 */         if (win.parent.sendEvent(c.cevent, 1, 1048576, c) == 1)
/*      */         {
/* 3179 */           return;
/*      */         }
/*      */       }
/*      */       
/*      */ 
/* 3184 */       int action = 0;
/* 3185 */       if (((mask & 0x3) != 0) && ((mask & 0xC) == 0)) {
/* 3186 */         action = 1;
/*      */       }
/* 3188 */       else if ((mask & 0xF) != 0) {
/* 3189 */         if ((w == 0) || (h == 0)) {
/* 3190 */           c.errorValue = 0;
/* 3191 */           c.errorReason = 2;
/* 3192 */           return;
/*      */         }
/* 3194 */         action = 2;
/*      */       }
/*      */       
/* 3197 */       if (action == 2) {
/* 3198 */         boolean sizec = (w != win.width) || (h != win.height);
/* 3199 */         if ((sizec) && (((win.eventMask | win.getOtherEventMask()) & 0x40000) != 0))
/*      */         {
/* 3201 */           c.cevent.mkResizeRequest(win.id, w, h);
/* 3202 */           if (win.sendEvent(c.cevent, 1, 262144, c) == 1)
/*      */           {
/* 3204 */             w = win.width;
/* 3205 */             h = win.height;
/* 3206 */             sizec = false;
/*      */           }
/*      */         }
/* 3209 */         if (!sizec) {
/* 3210 */           if ((mask & 0x3) != 0) { action = 1;
/* 3211 */           } else if ((mask & 0x50) != 0) action = 0; else {
/* 3212 */             return;
/*      */           }
/*      */         }
/*      */       }
/* 3216 */       if ((action == 2) || (((mask & 0x1) != 0) && (x != beforex)) || (((mask & 0x2) != 0) && (y != beforey)) || (((mask & 0x10) != 0) && (bw != win.borderWidth)) || (((mask & 0x40) != 0) && (win.nextSib != sib)))
/*      */       {
/*      */ 
/*      */ 
/*      */ 
/* 3221 */         if (win.substrSend()) {
/* 3222 */           c.cevent.mkConfigureNotify(win.id, sib != null ? sib.id : 0, x, y, w, h, bw, (win.attr & 0x10000) != 0 ? 0 : 1);
/*      */           
/*      */ 
/*      */ 
/* 3226 */           win.sendEvent(c.cevent, 1, null);
/*      */         }
/*      */         
/* 3229 */         if ((mask & 0x10) != 0) {
/* 3230 */           if (action == 0) {
/* 3231 */             action = 1;
/* 3232 */             win.borderWidth = bw;
/* 3233 */             win.ddxwindow.setBorder(bw);
/*      */           }
/* 3235 */           else if ((action == 1) && (beforex + win.borderWidth == x + bw) && (beforey + win.borderWidth == y + bw))
/*      */           {
/*      */ 
/* 3238 */             action = 3;
/*      */           }
/*      */           else {
/* 3241 */             win.borderWidth = bw;
/* 3242 */             win.ddxwindow.setBorder(bw);
/*      */           }
/*      */         }
/*      */         
/* 3246 */         if ((mask & 0x40) != 0) {
/* 3247 */           win.reflectStackChange(sib);
/*      */         }
/*      */         
/*      */ 
/* 3251 */         if (((mask & 0x1) != 0) || ((mask & 0x2) != 0) || ((mask & 0x4) != 0) || ((mask & 0x8) != 0) || ((mask & 0x10) != 0))
/*      */         {
/*      */ 
/* 3254 */           win.origin.x = (x + bw);
/* 3255 */           win.origin.y = (y + bw);
/*      */           
/*      */           int dy;
/* 3258 */           int dx = dy = dw = dh = 0;
/* 3259 */           int dw = w - win.width;
/* 3260 */           int dh = h - win.height;
/* 3261 */           win.height = h;
/* 3262 */           win.width = w;
/*      */           
/* 3264 */           int newx = win.parent.x + x + bw;
/* 3265 */           int newy = win.parent.y + y + bw;
/*      */           
/*      */ 
/* 3268 */           int oldx = win.x;
/* 3269 */           int oldy = win.y;
/*      */           
/* 3271 */           x = win.x = newx;
/* 3272 */           y = win.y = newy;
/*      */           
/* 3274 */           XWindow wToValidate = win.moveInStack(sib);
/* 3275 */           if (((mask & 0x4) != 0) || ((mask & 0x8) != 0) || ((mask & 0x10) != 0))
/*      */           {
/* 3277 */             if ((win.screen.windowmode != 0) && (win.hasFrame())) {
/* 3278 */               Window frame = win.getFrame();
/* 3279 */               frame.validate();
/* 3280 */               Insets insets = frame.getInsets();
/* 3281 */               frame.setSize(win.width + win.borderWidth * 2 + insets.left + insets.right, win.height + win.borderWidth * 2 + insets.bottom + insets.top);
/*      */               
/*      */ 
/*      */ 
/* 3285 */               frame.validate();
/*      */             }
/*      */             
/* 3288 */             win.ddxwindow.setSize(win.width, win.height);
/*      */           }
/*      */           
/* 3291 */           win.resizeChildrenWinSize(x - oldx, y - oldy, dw, dh);
/* 3292 */           win.ddxwindow.setLocation(win.origin.x - win.borderWidth + win.parent.borderWidth, win.origin.y - win.borderWidth + win.parent.borderWidth);
/*      */         }
/*      */         
/*      */         return;
/*      */       }
/*      */       
/*      */       return;
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */   private int getDontPropagateMask()
/*      */   {
/* 3305 */     if (this.optional == null)
/* 3306 */       return DontPropagate.masks[((this.attr & 0x1C00000) >> 22)];
/* 3307 */     return this.optional.dontPropagateMask;
/*      */   }
/*      */   
/*      */   Grab getPassiveGrabs() {
/* 3311 */     if (this.optional == null) return null;
/* 3312 */     return this.optional.passiveGrabs;
/*      */   }
/*      */   
/*      */   int getOtherEventMask() {
/* 3316 */     if (this.optional == null) return 0;
/* 3317 */     return this.optional.otherEventMasks;
/*      */   }
/*      */   
/*      */   void setColormap(Colormap cmap) {
/* 3321 */     makeOptional();
/* 3322 */     this.optional.colormap = cmap;
/*      */   }
/*      */   
/* 3325 */   Colormap getColormap() { return trackParent().optional.colormap; }
/*      */   
/*      */   private int getColormapId() {
/* 3328 */     if (this.clss == 2) return 0;
/* 3329 */     return getColormap().id;
/*      */   }
/*      */   
/*      */   void setVisual(int id) {
/* 3333 */     makeOptional();
/* 3334 */     this.optional.visual = id;
/*      */   }
/*      */   
/* 3337 */   private int getVisual() { return trackParent().optional.visual; }
/*      */   
/*      */ 
/* 3340 */   boolean isMapped() { return (this.attr & 0x80000) != 0; }
/* 3341 */   boolean isRealized() { return (this.attr & 0x100000) != 0; }
/* 3342 */   boolean isViewable() { return (this.attr & 0x200000) != 0; }
/* 3343 */   boolean isBorderPixel() { return (this.attr & 0x4) != 0; }
/* 3344 */   void setBorderIsPixel() { this.attr |= 0x4; }
/*      */   
/* 3346 */   void setBackgroundIsPixel() { this.attr &= 0xFFFFFFFC;
/* 3347 */     this.attr |= 0x2;
/*      */   }
/*      */   
/*      */   boolean hasParentRelativeBorder() {
/* 3351 */     return ((this.attr & 0x4) == 0) && (hasBorder()) && ((this.attr & 0x3) == 1);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/* 3356 */   boolean hasBorder() { return this.borderWidth != 0; }
/*      */   
/*      */   private void reflectStackChange(XWindow sib) throws IOException {
/* 3359 */     boolean wasViewable = (this.attr & 0x200000) != 0;
/* 3360 */     if (this.parent == null) { return;
/*      */     }
/* 3362 */     XWindow firstChange = moveInStack(sib);
/* 3363 */     if ((this.attr & 0x100000) != 0) {
/* 3364 */       restructured();
/*      */     }
/*      */   }
/*      */   
/*      */   private int isSiblingAboveMe(XWindow sib) {
/* 3369 */     XWindow win = this.parent.firstChild;
/* 3370 */     while (win != null) {
/* 3371 */       if (win == sib) return 0;
/* 3372 */       if (win == this) return 1;
/* 3373 */       win = win.nextSib;
/*      */     }
/* 3375 */     return 1;
/*      */   }
/*      */   
/*      */   private XWindow getPlaceInTheStack(XWindow sib, int x, int y, int w, int h, int smode)
/*      */   {
/* 3380 */     if ((this == this.parent.firstChild) && (this == this.parent.lastChild)) { return null;
/*      */     }
/* 3382 */     XWindow first = this.parent.firstChild;
/* 3383 */     switch (smode) {
/*      */     case 0: 
/* 3385 */       if (sib != null) return sib;
/* 3386 */       if (this == first) return this.nextSib;
/* 3387 */       return first;
/*      */     case 1: 
/* 3389 */       if (sib != null) {
/* 3390 */         if (sib.nextSib != this) return sib.nextSib;
/* 3391 */         return this.nextSib;
/*      */       }
/* 3393 */       return null;
/*      */     case 2: 
/* 3395 */       if (((this.attr & 0x80000) == 0) || ((sib != null) && ((sib.attr & 0x80000) == 0)))
/* 3396 */         return this.nextSib;
/* 3397 */       if (sib != null) {
/* 3398 */         if (isSiblingAboveMe(sib) == 0) {
/* 3399 */           return first;
/*      */         }
/* 3401 */         return this.nextSib;
/*      */       }
/*      */       
/* 3404 */       return first;
/*      */     case 3: 
/* 3406 */       if (((this.attr & 0x80000) == 0) || ((sib != null) && ((sib.attr & 0x80000) == 0)))
/* 3407 */         return this.nextSib;
/* 3408 */       if (sib != null) {
/* 3409 */         if (isSiblingAboveMe(sib) == 1) {
/* 3410 */           return null;
/*      */         }
/* 3412 */         return this.nextSib;
/*      */       }
/*      */       
/* 3415 */       return null;
/*      */     case 4: 
/* 3417 */       if (((this.attr & 0x80000) == 0) || ((sib != null) && ((sib.attr & 0x80000) == 0)))
/* 3418 */         return this.nextSib;
/* 3419 */       if (sib != null) {
/* 3420 */         if (isSiblingAboveMe(sib) == 0) {
/* 3421 */           return first;
/*      */         }
/* 3423 */         return null;
/*      */       }
/*      */       
/* 3426 */       return first;
/*      */     }
/* 3428 */     return this.nextSib;
/*      */   }
/*      */   
/*      */   private XWindow moveInStack(XWindow next)
/*      */   {
/* 3433 */     XWindow firstChange = this;
/* 3434 */     synchronized (LOCK) {
/* 3435 */       if (this.nextSib != next) {
/* 3436 */         if (next == null) {
/* 3437 */           if (this.parent.firstChild == this) this.parent.firstChild = this.nextSib;
/* 3438 */           firstChange = this.nextSib;
/* 3439 */           this.nextSib.prevSib = this.prevSib;
/* 3440 */           if (this.prevSib != null) this.prevSib.nextSib = this.nextSib;
/* 3441 */           this.parent.lastChild.nextSib = this;
/* 3442 */           this.prevSib = this.parent.lastChild;
/* 3443 */           this.nextSib = null;
/* 3444 */           this.parent.lastChild = this;
/*      */           
/* 3446 */           if ((this.screen.windowmode == 0) || (this.parent != this.screen.root)) {
/* 3447 */             this.parent.ddxwindow.add((Component)this.ddxwindow, -1);
/*      */           }
/*      */         }
/* 3450 */         else if (this.parent.firstChild == next) {
/* 3451 */           firstChange = this;
/* 3452 */           if (this.parent.lastChild == this) this.parent.lastChild = this.prevSib;
/* 3453 */           if (this.nextSib != null) this.nextSib.prevSib = this.prevSib;
/* 3454 */           if (this.prevSib != null) this.prevSib.nextSib = this.nextSib;
/* 3455 */           this.nextSib = this.parent.firstChild;
/* 3456 */           this.prevSib = null;
/* 3457 */           next.prevSib = this;
/* 3458 */           this.parent.firstChild = this;
/*      */           
/* 3460 */           if ((this.screen.windowmode == 0) || (this.parent != this.screen.root)) {
/* 3461 */             this.parent.ddxwindow.add((Component)this.ddxwindow, 0);
/*      */           }
/*      */         }
/*      */         else {
/* 3465 */           XWindow pOldNext = this.nextSib;
/* 3466 */           firstChange = null;
/* 3467 */           if (this.parent.firstChild == this)
/* 3468 */             firstChange = this.parent.firstChild = this.nextSib;
/* 3469 */           if (this.parent.lastChild == this) {
/* 3470 */             firstChange = this;
/* 3471 */             this.parent.lastChild = this.prevSib;
/*      */           }
/* 3473 */           if (this.nextSib != null) this.nextSib.prevSib = this.prevSib;
/* 3474 */           if (this.prevSib != null) this.prevSib.nextSib = this.nextSib;
/* 3475 */           this.nextSib = next;
/* 3476 */           this.prevSib = next.prevSib;
/* 3477 */           if (next.prevSib != null) next.prevSib.nextSib = this;
/* 3478 */           next.prevSib = this;
/* 3479 */           if (firstChange == null) {
/* 3480 */             firstChange = this.parent.firstChild;
/* 3481 */             while ((firstChange != this) && (firstChange != pOldNext)) {
/* 3482 */               firstChange = firstChange.nextSib;
/*      */             }
/*      */           }
/* 3485 */           Component[] component = this.parent.ddxwindow.getComponents();
/* 3486 */           for (int i = 0; i < component.length; i++) {
/* 3487 */             if (component[i] == next.ddxwindow) {
/* 3488 */               if (i != 0) i--;
/* 3489 */               if ((this.screen.windowmode != 0) && (this.parent == this.screen.root)) break;
/* 3490 */               this.parent.ddxwindow.add((Component)this.ddxwindow, i); break;
/*      */             }
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/*      */     
/*      */ 
/* 3498 */     draw();
/* 3499 */     return firstChange;
/*      */   }
/*      */   
/*      */   public static final void reqReparentWindow(XClient c)
/*      */     throws IOException
/*      */   {
/* 3505 */     ComChannel comChannel = c.channel;
/*      */     
/* 3507 */     int foo = comChannel.readInt();
/* 3508 */     XWindow win = c.lookupWindow(foo);
/* 3509 */     if (win == null) {
/* 3510 */       c.errorValue = foo;
/* 3511 */       c.errorReason = 3;
/*      */     }
/*      */     
/* 3514 */     foo = comChannel.readInt();
/* 3515 */     XWindow parent = c.lookupWindow(foo);
/* 3516 */     if ((parent == null) && (c.errorReason == 0)) {
/* 3517 */       c.errorValue = foo;
/* 3518 */       c.errorReason = 3;
/*      */     }
/*      */     
/* 3521 */     int x = (short)comChannel.readShort();
/* 3522 */     int y = (short)comChannel.readShort();
/*      */     
/* 3524 */     c.length -= 4;
/*      */     
/* 3526 */     if (c.errorReason != 0) return;
/* 3527 */     win.reparent(parent, x, y, c);
/*      */   }
/*      */   
/*      */ 
/*      */   void reparent(XWindow newparent, int xx, int yy, XClient c)
/*      */     throws IOException
/*      */   {
/* 3534 */     synchronized (LOCK) {
/* 3535 */       if (this.parent == null) { return;
/*      */       }
/* 3537 */       int bw = this.borderWidth;
/* 3538 */       boolean wasMapped = (this.attr & 0x80000) != 0;
/*      */       
/*      */ 
/* 3541 */       makeOptional();
/*      */       
/* 3543 */       if (wasMapped) {
/* 3544 */         unmapWindow(false);
/*      */       }
/*      */       
/* 3547 */       c.cevent.mkReparentNotify(this.id, this.id, newparent.id, xx, yy, (this.attr & 0x10000) != 0 ? 1 : 0);
/*      */       
/*      */ 
/* 3550 */       sendEvent(c.cevent, 1, newparent);
/*      */       
/* 3552 */       XWindow prev = this.parent;
/* 3553 */       if (prev.firstChild == this) prev.firstChild = this.nextSib;
/* 3554 */       if (prev.lastChild == this) { prev.lastChild = this.prevSib;
/*      */       }
/* 3556 */       if (this.nextSib != null) this.nextSib.prevSib = this.prevSib;
/* 3557 */       if (this.prevSib != null) { this.prevSib.nextSib = this.nextSib;
/*      */       }
/* 3559 */       this.parent = newparent;
/*      */       
/* 3561 */       this.nextSib = newparent.firstChild;
/* 3562 */       this.prevSib = null;
/* 3563 */       if (newparent.firstChild != null) newparent.firstChild.prevSib = this; else
/* 3564 */         newparent.lastChild = this;
/* 3565 */       newparent.firstChild = this;
/*      */       
/* 3567 */       this.origin.x = ((short)xx);
/* 3568 */       this.origin.y = ((short)yy);
/* 3569 */       this.x = ((short)(xx + newparent.x));
/* 3570 */       this.y = ((short)(yy + newparent.y));
/*      */       
/* 3572 */       if ((this.screen.windowmode != 0) && (hasFrame())) {
/* 3573 */         Window frame = getFrame();
/* 3574 */         frame.remove((Component)this.ddxwindow);
/*      */         
/* 3576 */         delFrame();
/*      */       }
/* 3578 */       if ((this.screen.windowmode != 0) && (newparent == this.screen.root)) {
/* 3579 */         Window frame = getFrame();
/*      */         
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 3585 */         if ((frame instanceof Frame)) {
/* 3586 */           ((Frame)frame).setMenuBar(null);
/* 3587 */           ((Frame)frame).setResizable(true);
/*      */         }
/*      */         
/* 3590 */         this.ddxwindow.setLocation(0, 0);
/*      */         
/* 3592 */         frame.add((Component)this.ddxwindow);
/*      */         
/* 3594 */         frame.validate();
/* 3595 */         Insets insets = frame.getInsets();
/* 3596 */         frame.setSize(this.width + this.borderWidth * 2 + insets.left + insets.right, this.height + this.borderWidth * 2 + insets.bottom + insets.top);
/*      */         
/*      */ 
/*      */ 
/* 3600 */         frame.validate();
/*      */         
/* 3602 */         if ((frame instanceof Frame)) {
/* 3603 */           addWindowListener((Frame)frame);
/* 3604 */           addComponentListener((Frame)frame);
/*      */         }
/*      */       }
/*      */       else {
/* 3608 */         newparent.ddxwindow.add((Component)this.ddxwindow, 0);
/* 3609 */         this.ddxwindow.setLocation(this.origin.x - this.borderWidth + this.parent.borderWidth, this.origin.y - this.borderWidth + this.parent.borderWidth);
/*      */       }
/*      */       
/*      */ 
/* 3613 */       resizeChildrenWinSize(0, 0, 0, 0);
/*      */       
/* 3615 */       if (wasMapped) {
/* 3616 */         mapWindow(c);
/*      */       }
/*      */       
/* 3619 */       recalculateDeliverableEvents();
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */   public void recalculateDeliverableEvents()
/*      */   {
/* 3626 */     XWindow child = this;
/*      */     for (;;) {
/* 3628 */       if (child.optional != null) {
/* 3629 */         child.optional.otherEventMasks = 0;
/* 3630 */         for (OtherClients others = this.optional == null ? null : this.optional.otherClients; 
/* 3631 */             others != null; others = (OtherClients)others.next) {
/* 3632 */           child.optional.otherEventMasks |= others.mask;
/*      */         }
/*      */       }
/*      */       
/* 3636 */       child.deliverableEvents = (child.eventMask | child.getOtherEventMask());
/* 3637 */       if (child.parent != null) {
/* 3638 */         child.deliverableEvents |= child.parent.deliverableEvents & 0x3F4F & (child.getDontPropagateMask() ^ 0xFFFFFFFF);
/*      */       }
/*      */       
/*      */ 
/* 3642 */       if (child.firstChild != null) {
/* 3643 */         child = child.firstChild;
/*      */       }
/*      */       else
/*      */       {
/* 3647 */         while ((child.nextSib == null) && (child != this))
/* 3648 */           child = child.parent;
/* 3649 */         if (child == this)
/*      */           break;
/* 3651 */         child = child.nextSib;
/*      */       }
/*      */     }
/*      */   }
/*      */   
/* 3656 */   final Graphics getGraphics() { if (this.clss == 2) return this.ddxwindow.getGraphics();
/* 3657 */     return this.ddxwindow.getGraphics2();
/*      */   }
/*      */   
/*      */   final Graphics getGraphics(GC gc, int mask) {
/* 3661 */     if (this.clss == 2) return this.ddxwindow.getGraphics();
/* 3662 */     return this.ddxwindow.getGraphics(gc, mask);
/*      */   }
/*      */   
/* 3665 */   final Image getImage() { return this.ddxwindow.getImage(); }
/*      */   
/*      */   final Image getImage(int x, int y, int width, int height) {
/* 3668 */     return this.ddxwindow.getImage(null, x, y, width, height);
/*      */   }
/*      */   
/*      */   final Image getImage(GC gc, int x, int y, int width, int height) {
/* 3672 */     return this.ddxwindow.getImage(gc, x, y, width, height);
/*      */   }
/*      */   
/*      */ 
/*      */   private void setEventMask(XClient c, int mask)
/*      */   {
/* 3678 */     int check = mask & 0x140004;
/* 3679 */     if ((check & (this.eventMask | getOtherEventMask())) != 0) {
/* 3680 */       if ((getXClient() != c) && ((check & this.eventMask) != 0)) {
/* 3681 */         c.errorReason = 10;
/*      */       }
/* 3683 */       for (OtherClients others = getOtherClients(); others != null; 
/* 3684 */           others = (OtherClients)others.next) {
/* 3685 */         if ((!others.sameClient(c)) && ((check & others.mask) != 0)) {
/* 3686 */           c.errorReason = 10;
/* 3687 */           return;
/*      */         }
/*      */       }
/*      */     }
/* 3691 */     if (this.XClient == c) {
/* 3692 */       check = this.eventMask;
/* 3693 */       this.eventMask = mask;
/*      */     }
/*      */     else {
/* 3696 */       for (OtherClients others = this.optional == null ? null : this.optional.otherClients; 
/* 3697 */           others != null; others = (OtherClients)others.next) {
/* 3698 */         if (others.sameClient(c)) {
/* 3699 */           check = others.mask;
/* 3700 */           if (mask == 0) {
/* 3701 */             freeResource(others.id, 0);
/* 3702 */             return;
/*      */           }
/*      */           
/* 3705 */           others.mask = mask;
/*      */           
/* 3707 */           recalculateDeliverableEvents();
/* 3708 */           return;
/*      */         }
/*      */       }
/* 3711 */       check = 0;
/* 3712 */       makeOptional();
/*      */       
/* 3714 */       others = new OtherClients(fakeClientId(c));
/* 3715 */       others.mask = mask;
/* 3716 */       others.resource = others.id;
/* 3717 */       others.XWindow = this;
/* 3718 */       others.next = this.optional.otherClients;
/* 3719 */       this.optional.otherClients = others;
/* 3720 */       add(others);
/*      */     }
/* 3722 */     recalculateDeliverableEvents();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   private static void gravityTranslate(int newx, int newy, int oldx, int oldy, int dw, int dh, int gravity, Point point)
/*      */   {
/* 3730 */     switch (gravity) {
/*      */     case 2: 
/* 3732 */       point.x = (newx + dw / 2);
/* 3733 */       point.y = newy;
/* 3734 */       break;
/*      */     case 3: 
/* 3736 */       point.x = (newx + dw);
/* 3737 */       point.y = newy;
/* 3738 */       break;
/*      */     case 4: 
/* 3740 */       point.x = newx;
/* 3741 */       point.y = (newy + dh / 2);
/* 3742 */       break;
/*      */     case 5: 
/* 3744 */       point.x = (newx + dw / 2);
/* 3745 */       point.y = (newy + dh / 2);
/* 3746 */       break;
/*      */     case 6: 
/* 3748 */       point.x = (newx + dw);
/* 3749 */       point.y = (newy + dh / 2);
/* 3750 */       break;
/*      */     case 7: 
/* 3752 */       point.x = newx;
/* 3753 */       point.y = (newy + dh);
/* 3754 */       break;
/*      */     case 8: 
/* 3756 */       point.x = (newx + dw / 2);
/* 3757 */       point.y = (newy + dh);
/* 3758 */       break;
/*      */     case 9: 
/* 3760 */       point.x = (newx + dw);
/* 3761 */       point.y = (newy + dh);
/* 3762 */       break;
/*      */     case 10: 
/* 3764 */       point.x = oldx;
/* 3765 */       point.y = oldy;
/* 3766 */       break;
/*      */     default: 
/* 3768 */       point.x = newx;
/* 3769 */       point.y = newy;
/*      */     }
/*      */   }
/*      */   
/*      */   private void resizeChildrenWinSize(int dx, int dy, int dw, int dh)
/*      */     throws IOException
/*      */   {
/* 3776 */     boolean resized = (dw != 0) || (dh != 0);
/* 3777 */     for (XWindow sib = this.firstChild; sib != null; sib = sib.nextSib) {
/* 3778 */       if ((resized) && ((sib.attr & 0xF000) >> 12 > 1))
/*      */       {
/* 3780 */         int newx = sib.origin.x;
/* 3781 */         int newy = sib.origin.y;
/* 3782 */         gravityTranslate(newx, newy, newx - dx, newy - dy, dw, dh, (sib.attr & 0xF000) >> 12, gpoint);
/*      */         
/*      */ 
/* 3785 */         if ((gpoint.x != sib.origin.x) || (gpoint.y != sib.origin.y)) {
/* 3786 */           Event event = new Event();
/* 3787 */           event.mkGravityNotify(sib.id, sib.id, gpoint.x - sib.borderWidth, gpoint.y - sib.borderWidth);
/*      */           
/*      */ 
/*      */ 
/* 3791 */           sib.sendEvent(event, 1, null);
/* 3792 */           sib.origin.x = gpoint.x;
/* 3793 */           sib.origin.y = gpoint.y;
/*      */         }
/*      */       }
/*      */       
/* 3797 */       this.x += sib.origin.x;
/* 3798 */       this.y += sib.origin.y;
/* 3799 */       if (sib.ddxwindow != null) {
/* 3800 */         sib.ddxwindow.setLocation(sib.origin.x - sib.borderWidth + sib.parent.borderWidth, sib.origin.y - sib.borderWidth + sib.parent.borderWidth);
/*      */       }
/* 3802 */       XWindow child = sib.firstChild;
/* 3803 */       if (child != null) {
/*      */         for (;;) {
/* 3805 */           child.x = (child.parent.x + child.origin.x);
/* 3806 */           child.y = (child.parent.y + child.origin.y);
/* 3807 */           if (child.ddxwindow != null) {
/* 3808 */             child.ddxwindow.setLocation(child.origin.x - child.borderWidth + child.parent.borderWidth, child.origin.y - child.borderWidth + child.parent.borderWidth);
/*      */           }
/* 3810 */           if (child.firstChild != null) {
/* 3811 */             child = child.firstChild;
/*      */           }
/*      */           else {
/* 3814 */             while ((child.nextSib == null) && (child != sib))
/* 3815 */               child = child.parent;
/* 3816 */             if (child == sib) break;
/* 3817 */             child = child.nextSib;
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */   public static boolean checkDeviceGrabs(Event e, int checkFirst, int count)
/*      */     throws IOException
/*      */   {
/* 3827 */     for (int i = checkFirst; 
/* 3828 */         i < spriteTraceGood; i++) {
/* 3829 */       XWindow win = spriteTrace[i];
/* 3830 */       if ((win.optional != null) && (win.checkPassiveGrabsOnWindow(e, count)))
/*      */       {
/* 3832 */         return true;
/*      */       }
/*      */     }
/* 3835 */     return false;
/*      */   }
/*      */   
/*      */   boolean checkPassiveGrabsOnWindow(Event e, int count) throws IOException {
/* 3839 */     Grab grab = getPassiveGrabs();
/*      */     
/* 3841 */     if (grab == null) { return false;
/*      */     }
/* 3843 */     synchronized (LOCK) {
/* 3844 */       Grab tempGrab = new Grab(fakeClientId(this.XClient));
/* 3845 */       tempGrab.window = this;
/* 3846 */       tempGrab.type = e.getType();
/* 3847 */       tempGrab.detail.exact = e.getDetail();
/* 3848 */       tempGrab.detail.pMask = null;
/* 3849 */       tempGrab.modifiersDetail.pMask = null;
/* 3850 */       tempGrab.modifiersDetail.exact = (e.getState() & 0xF);
/* 3851 */       for (; grab != null; grab = grab.next) {
/* 3852 */         if ((tempGrab.grabMatchesSecond(grab)) && ((grab.confineTo == null) || (((grab.confineTo.attr & 0x100000) != 0) && ((grab.confineTo.x != 0) || (grab.confineTo.y != 0)))))
/*      */         {
/*      */ 
/*      */ 
/* 3856 */           grab.activatePointerGrab((int)System.currentTimeMillis(), true);
/* 3857 */           e.fixUpEventFromWindow(grab.window, 0, sprite.hot.x, sprite.hot.y, true);
/*      */           
/*      */ 
/* 3860 */           if (grab.getClient() != null) {
/* 3861 */             grab.getClient().sendEvent(e, count, Event.filters[e.getType()], Event.filters[e.getType()], grab);
/*      */           }
/*      */           
/* 3864 */           return true;
/*      */         }
/*      */       }
/*      */     }
/* 3868 */     return false;
/*      */   }
/*      */   
/*      */   public static void sendGrabbedEvent(Event e, boolean deactivateGrab, int count) throws IOException
/*      */   {
/* 3873 */     Grab grab = grab;
/* 3874 */     int deliveries = 0;
/*      */     
/* 3876 */     if ((grab.attr & 0x1) != 0) {
/* 3877 */       XWindow focus = null;
/* 3878 */       if (focus == null) {
/* 3879 */         deliveries = sendDeviceEvent(sprite.win, e, grab, null, count);
/* 3880 */       } else if ((focus != null) && ((focus == sprite.win) || (focus.isParent(sprite.win))))
/*      */       {
/* 3882 */         deliveries = sendDeviceEvent(sprite.win, e, grab, focus, count);
/* 3883 */       } else if (focus != null) {
/* 3884 */         deliveries = sendDeviceEvent(focus, e, grab, focus, count);
/*      */       }
/*      */     }
/* 3887 */     if (deliveries == 0) {
/* 3888 */       e.fixUpEventFromWindow(grab.window, 0, sprite.hot.x, sprite.hot.y, true);
/*      */       
/* 3890 */       if (grab.getClient() != null) {
/* 3891 */         deliveries = grab.getClient().sendEvent(e, count, grab.eventMask, Event.filters[e.getType()], grab);
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public static int sendDeviceEvent(XWindow win, Event e, Grab grab, XWindow stopAt, int count)
/*      */     throws IOException
/*      */   {
/* 3901 */     int child = 0;
/* 3902 */     int type = e.getType();
/* 3903 */     int filter = Event.filters[type];
/* 3904 */     int deliveries = 0;
/*      */     
/* 3906 */     if ((type & 0x40) != 0)
/*      */     {
/* 3908 */       int mskidx = 0;
/* 3909 */       OtherInputMasks inputMasks = win.optional == null ? null : win.optional.otherInputMasks;
/* 3910 */       if ((inputMasks != null) && ((filter & inputMasks.deliverableEvents[mskidx]) == 0))
/*      */       {
/* 3912 */         return 0; }
/* 3913 */       while (win != null) {
/* 3914 */         if ((inputMasks != null) && ((inputMasks.inputEvents[mskidx] & filter) != 0))
/*      */         {
/* 3916 */           e.fixUpEventFromWindow(win, child, sprite.hot.x, sprite.hot.y, false);
/*      */           
/* 3918 */           deliveries = win.sendEvent(e, count, filter, grab, mskidx);
/* 3919 */           if (deliveries > 0) return deliveries;
/*      */         }
/* 3921 */         if ((deliveries < 0) || (win == stopAt) || ((inputMasks != null) && ((filter & inputMasks.dontPropagateMask[mskidx]) != 0)))
/*      */         {
/*      */ 
/*      */ 
/* 3925 */           return 0; }
/* 3926 */         child = win.id;
/* 3927 */         win = win.parent;
/* 3928 */         if (win != null) {
/* 3929 */           inputMasks = win.optional == null ? null : win.optional.otherInputMasks;
/*      */         }
/*      */       }
/*      */     }
/*      */     else {
/* 3934 */       if ((filter & win.deliverableEvents) == 0) { return 0;
/*      */       }
/* 3936 */       while (win != null) {
/* 3937 */         if (((win.getOtherEventMask() | win.eventMask) & filter) != 0) {
/* 3938 */           e.fixUpEventFromWindow(win, child, sprite.hot.x, sprite.hot.y, false);
/*      */           
/* 3940 */           deliveries = win.sendEvent(e, count, filter, grab, 0);
/* 3941 */           if (deliveries > 0) return deliveries;
/*      */         }
/* 3943 */         if ((deliveries < 0) || (win == stopAt) || ((filter & win.getDontPropagateMask()) != 0))
/*      */         {
/* 3945 */           return 0; }
/* 3946 */         child = win.id;
/* 3947 */         win = win.parent;
/*      */       }
/*      */     }
/* 3950 */     return 0;
/*      */   }
/*      */   
/*      */   boolean isParent(XWindow w) {
/* 3954 */     for (w = w.parent; w != null; w = w.parent) {
/* 3955 */       if (w == this) return true;
/*      */     }
/* 3957 */     return false;
/*      */   }
/*      */   
/* 3960 */   XWindow commonAncestor(XWindow w) { for (w = w.parent; w != null; w = w.parent) {
/* 3961 */       if (w.isParent(this)) return w;
/*      */     }
/* 3963 */     return null;
/*      */   }
/*      */   
/*      */ 
/*      */   private static void doFocusEvents(XClient c, int fromWin, int toWin, int mode)
/*      */     throws IOException
/*      */   {
/* 3970 */     if (fromWin == toWin) { return;
/*      */     }
/* 3972 */     int out = fromWin == 0 ? 7 : 6;
/* 3973 */     int in = toWin == 0 ? 7 : 6;
/*      */     
/* 3975 */     if ((toWin == 0) || (toWin == 1)) {
/* 3976 */       if ((fromWin == 0) || (fromWin == 1)) {
/* 3977 */         if (fromWin == 1) {
/* 3978 */           focusOutEvents(c, sprite.win, sprite.win.screen.root, mode, 5, true);
/*      */         }
/*      */       }
/*      */       else
/*      */       {
/* 3983 */         XWindow from = c != null ? c.lookupWindow(fromWin) : (XWindow)lookupIDByClass(fromWin, 1073741824);
/*      */         
/* 3985 */         if (from == null) return;
/* 3986 */         if (from.isParent(sprite.win)) {
/* 3987 */           focusOutEvents(c, sprite.win, from, mode, 5, false);
/*      */         }
/* 3989 */         from.sendFocusEvent(c, 10, mode, 3);
/* 3990 */         focusOutEvents(c, from.parent, null, mode, 4, false);
/*      */       }
/*      */       
/* 3993 */       if (toWin == 1) {
/* 3994 */         focusInEvents(c, sprite.win.screen.root, sprite.win, null, mode, 5, true);
/*      */       }
/*      */       
/*      */ 
/*      */     }
/* 3999 */     else if ((fromWin == 0) || (fromWin == 1)) {
/* 4000 */       if (fromWin == 1) {
/* 4001 */         focusOutEvents(c, sprite.win, sprite.win.screen.root, mode, 5, true);
/*      */       }
/*      */       
/*      */ 
/* 4005 */       XWindow to = c != null ? c.lookupWindow(toWin) : (XWindow)lookupIDByClass(toWin, 1073741824);
/*      */       
/* 4007 */       if (to == null) return;
/* 4008 */       if (to.parent != null) {
/* 4009 */         focusInEvents(c, to.screen.root, to, to, mode, 4, true);
/*      */       }
/*      */       
/* 4012 */       to.sendFocusEvent(c, 9, mode, 3);
/*      */       
/* 4014 */       if (to.isParent(sprite.win)) {
/* 4015 */         focusInEvents(c, to, sprite.win, null, mode, 5, false);
/*      */       }
/*      */     }
/*      */     else
/*      */     {
/* 4020 */       XWindow to = c != null ? c.lookupWindow(toWin) : (XWindow)lookupIDByClass(toWin, 1073741824);
/*      */       
/* 4022 */       XWindow from = c != null ? c.lookupWindow(fromWin) : (XWindow)lookupIDByClass(fromWin, 1073741824);
/*      */       
/* 4024 */       if ((from == null) || (to == null)) return;
/* 4025 */       if (to.isParent(from)) {
/* 4026 */         from.sendFocusEvent(c, 10, mode, 0);
/*      */         
/* 4028 */         focusOutEvents(c, from.parent, to, mode, 1, false);
/*      */         
/* 4030 */         to.sendFocusEvent(c, 9, mode, 2);
/*      */         
/* 4032 */         if ((to.isParent(sprite.win)) && (sprite.win != from) && (!from.isParent(sprite.win)) && (!sprite.win.isParent(from)))
/*      */         {
/*      */ 
/*      */ 
/* 4036 */           focusInEvents(c, to, sprite.win, null, mode, 5, false);
/*      */         }
/*      */         
/*      */       }
/* 4040 */       else if (from.isParent(to)) {
/* 4041 */         if ((from.isParent(sprite.win)) && (sprite.win != from) && (!to.isParent(sprite.win)) && (!sprite.win.isParent(to)))
/*      */         {
/*      */ 
/*      */ 
/* 4045 */           focusOutEvents(c, sprite.win, from, mode, 5, false);
/*      */         }
/* 4047 */         from.sendFocusEvent(c, 10, mode, 2);
/* 4048 */         focusInEvents(c, from, to, to, mode, 1, false);
/* 4049 */         to.sendFocusEvent(c, 9, mode, 0);
/*      */       }
/*      */       else {
/* 4052 */         XWindow common = to.commonAncestor(from);
/*      */         
/* 4054 */         if (from.isParent(sprite.win)) {
/* 4055 */           focusOutEvents(c, sprite.win, from, mode, 5, false);
/*      */         }
/* 4057 */         from.sendFocusEvent(c, 10, mode, 3);
/* 4058 */         if (from.parent != null) {
/* 4059 */           focusOutEvents(c, from.parent, common, mode, 4, false);
/*      */         }
/* 4061 */         if (to.parent != null) {
/* 4062 */           focusInEvents(c, common, to, to, mode, 4, false);
/*      */         }
/* 4064 */         to.sendFocusEvent(c, 9, mode, 3);
/* 4065 */         if (to.isParent(sprite.win)) {
/* 4066 */           focusInEvents(c, to, sprite.win, null, mode, 5, false);
/*      */         }
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   private static void focusOutEvents(XClient c, XWindow child, XWindow ancestor, int mode, int detail, boolean doAncestor)
/*      */     throws IOException
/*      */   {
/* 4078 */     for (XWindow win = child; win != ancestor; win = win.parent) {
/* 4079 */       win.sendFocusEvent(c, 10, mode, detail);
/*      */     }
/* 4081 */     if (doAncestor) {
/* 4082 */       ancestor.sendFocusEvent(c, 10, mode, detail);
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */   private static boolean focusInEvents(XClient c, XWindow ancestor, XWindow child, XWindow skipChild, int mode, int detail, boolean doAncestor)
/*      */     throws IOException
/*      */   {
/* 4090 */     if (child == null) {
/* 4091 */       return ancestor == null;
/*      */     }
/* 4093 */     if (ancestor == child) {
/* 4094 */       if (doAncestor) {
/* 4095 */         child.sendFocusEvent(c, 9, mode, detail);
/*      */       }
/* 4097 */       return true;
/*      */     }
/* 4099 */     if (focusInEvents(c, ancestor, child.parent, skipChild, mode, detail, doAncestor))
/*      */     {
/* 4101 */       if (child != skipChild) {
/* 4102 */         child.sendFocusEvent(c, 9, mode, detail);
/*      */       }
/* 4104 */       return true;
/*      */     }
/* 4106 */     return false;
/*      */   }
/*      */   
/*      */   void sendFocusEvent(XClient c, int type, int mode, int detail) throws IOException
/*      */   {
/* 4111 */     Event event = c != null ? c.cevent : new Event();
/* 4112 */     if (type == 9) { event.mkFocusIn(detail, this.id, mode);
/* 4113 */     } else if (type == 10) event.mkFocusOut(detail, this.id, mode); else
/* 4114 */       return;
/* 4115 */     sendEvent(event, 1, Event.filters[type], null, 0);
/*      */   }
/*      */   
/*      */   void initAttr() {
/* 4119 */     setBackgroundIsPixel();
/* 4120 */     this.background.pixel = this.screen.white;
/* 4121 */     setBorderIsPixel();
/* 4122 */     this.border.pixel = this.screen.black;
/* 4123 */     this.borderWidth = 0;
/*      */   }
/*      */   
/* 4126 */   void restoreClip() { this.ddxwindow.restoreClip(); }
/*      */   
/*      */   private static int lowbit(int mask) {
/* 4129 */     int result = 1;
/* 4130 */     for (int i = 0; i < 32; i++) {
/* 4131 */       if ((mask & 0x1) == 1) { result <<= i; break; }
/* 4132 */       mask >>= 1;
/*      */     }
/* 4134 */     return result;
/*      */   }
/*      */   
/*      */   public static void printWindow(XWindow p1, int indent) {
/* 4138 */     for (int i = 0; i < indent; i++) System.err.print(" ");
/*      */     try {
/* 4140 */       System.err.print(Integer.toHexString(p1.id));
/*      */       
/*      */ 
/* 4143 */       System.err.print(" (" + p1.x + "," + p1.y + "," + p1.width + "," + p1.height + ")");
/*      */       
/* 4145 */       System.err.println(" isVisible: " + p1.ddxwindow.isVisible());
/*      */     }
/*      */     catch (Exception ee) {}
/*      */   }
/*      */   
/*      */   public static void printChildren(XWindow p1, int indent) {
/* 4151 */     while (p1 != null) {
/* 4152 */       XWindow p2 = p1.firstChild;
/* 4153 */       printWindow(p1, indent);
/* 4154 */       printChildren(p2, indent + 4);
/* 4155 */       p1 = p1.nextSib;
/*      */     }
/*      */   }
/*      */   
/* 4159 */   public static void printWindowTree(XWindow win) { System.err.println("printWindowTree");
/* 4160 */     printWindow(win, 0);
/*      */     
/* 4162 */     for (int i = 0; i < 1; i++) {
/* 4163 */       XWindow p1 = win.firstChild;
/* 4164 */       printChildren(p1, 4);
/*      */     }
/*      */   }
/*      */   
/*      */   private void addWindowListener(Frame foo) {
/* 4169 */     Frame frame = foo;
/* 4170 */     frame.addWindowListener(new WindowAdapter()
/*      */     {
/*      */       public void windowClosed(WindowEvent e) {}
/*      */       
/*      */       public void windowClosing(WindowEvent e) {
/* 4175 */         int message_type = Atom.find("WM_PROTOCOLS");
/* 4176 */         int del = Atom.find("WM_DELETE_WINDOW");
/*      */         
/* 4178 */         synchronized (XWindow.LOCK) {
/* 4179 */           Property p = XWindow.this.getProperty();
/* 4180 */           while ((p != null) && 
/* 4181 */             (p.propertyName != message_type)) {
/* 4182 */             p = p.next;
/*      */           }
/*      */           
/* 4185 */           if ((p == null) || (p.data == null))
/*      */           {
/*      */             try
/*      */             {
/* 4189 */               XWindow.this.getXClient().closeDown();
/*      */             }
/*      */             catch (Exception ee) {}
/* 4192 */             return;
/*      */           }
/*      */         }
/*      */         
/* 4196 */         if ((message_type == 0) || (del == 0)) { return;
/*      */         }
/* 4198 */         Event event = new Event();
/* 4199 */         event.mkClientMessage(XWindow.this.id, message_type);
/* 4200 */         byte[] eb = event.event;
/* 4201 */         eb[1] = 32;
/*      */         
/* 4203 */         eb[12] = ((byte)(del >>> 24));
/* 4204 */         eb[13] = ((byte)(del >>> 16));
/* 4205 */         eb[14] = ((byte)(del >>> 8));
/* 4206 */         eb[15] = ((byte)del);
/*      */         
/* 4208 */         int tm = (int)System.currentTimeMillis();
/* 4209 */         eb[16] = ((byte)(tm >>> 24));
/* 4210 */         eb[17] = ((byte)(tm >>> 16));
/* 4211 */         eb[18] = ((byte)(tm >>> 8));
/* 4212 */         eb[19] = ((byte)tm);
/*      */         
/* 4214 */         eb[20] = (eb[21] = eb[22] = eb[23] = 0);
/* 4215 */         eb[24] = (eb[25] = eb[26] = eb[27] = 0);
/* 4216 */         eb[28] = (eb[29] = eb[30] = eb[31] = 0); int 
/*      */         
/* 4218 */           tmp307_306 = 0; byte[] tmp307_304 = eb;tmp307_304[tmp307_306] = ((byte)(tmp307_304[tmp307_306] | 0x80));
/*      */         try {
/* 4220 */           XWindow.this.sendEvent(event, 1, 0, null, 0);
/*      */         }
/*      */         catch (Exception ee) {}
/*      */       }
/*      */     });
/*      */   }
/*      */   
/*      */   private void addComponentListener(Frame foo) {
/* 4228 */     final Frame frame = foo;
/* 4229 */     frame.addComponentListener(new ComponentAdapter()
/*      */     {
/*      */       public void componentResized(ComponentEvent e) {
/* 4232 */         if (XWindow.this.parent == null) return;
/* 4233 */         Rectangle rectangle = frame.getBounds();
/* 4234 */         if ((rectangle.width == 0) || (rectangle.height == 0)) return;
/* 4235 */         if ((XWindow.this.frame_width != rectangle.width) || (XWindow.this.frame_height != rectangle.height))
/*      */         {
/* 4237 */           Insets insets = frame.getInsets();
/* 4238 */           synchronized (XWindow.LOCK) {
/*      */             try {
/* 4240 */               Point point = frame.getLocation();
/* 4241 */               int ww = rectangle.width - insets.left - insets.right - XWindow.this.borderWidth * 2;
/* 4242 */               int hh = rectangle.height - insets.top - insets.bottom - XWindow.this.borderWidth * 2;
/* 4243 */               if ((ww > 0) && (hh > 0)) {
/* 4244 */                 XWindow.this.ddxwindow.setSize(ww, hh);
/* 4245 */                 frame.pack();
/* 4246 */                 Event event = new Event();
/* 4247 */                 event.mkConfigureNotify(XWindow.this.id, XWindow.this.id, rectangle.x + insets.left, rectangle.y + insets.top, ww, hh, XWindow.this.borderWidth, (XWindow.this.attr & 0x10000) != 0 ? 1 : 0);
/*      */                 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 4255 */                 XWindow.this.sendEvent(event, 1, null);
/* 4256 */                 XWindow.this.frame_x = rectangle.x;
/* 4257 */                 XWindow.this.frame_y = rectangle.y;
/* 4258 */                 XWindow.this.frame_width = rectangle.width;
/* 4259 */                 XWindow.this.frame_height = rectangle.height;
/*      */               }
/*      */             }
/*      */             catch (Exception ee) {
/* 4263 */               System.out.println(ee);
/*      */             }
/*      */           }
/*      */         }
/*      */       }
/*      */     });
/*      */   }
/*      */ }


/* Location:              C:\Users\Joey\Downloads\launch.jar!\com\emt\proteus\xserver\display\XWindow.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */