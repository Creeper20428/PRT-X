/*      */ package com.emt.proteus.xserver.client;
/*      */ 
/*      */ import com.emt.proteus.xserver.display.Colormap;
/*      */ import com.emt.proteus.xserver.display.Cursor;
/*      */ import com.emt.proteus.xserver.display.Draw;
/*      */ import com.emt.proteus.xserver.display.Drawable;
/*      */ import com.emt.proteus.xserver.display.Event;
/*      */ import com.emt.proteus.xserver.display.Extension;
/*      */ import com.emt.proteus.xserver.display.Format;
/*      */ import com.emt.proteus.xserver.display.GC;
/*      */ import com.emt.proteus.xserver.display.Grab;
/*      */ import com.emt.proteus.xserver.display.Pixmap;
/*      */ import com.emt.proteus.xserver.display.Property;
/*      */ import com.emt.proteus.xserver.display.Resource;
/*      */ import com.emt.proteus.xserver.display.SaveSet;
/*      */ import com.emt.proteus.xserver.display.Screen;
/*      */ import com.emt.proteus.xserver.display.Selection;
/*      */ import com.emt.proteus.xserver.display.XDisplay;
/*      */ import com.emt.proteus.xserver.display.XFont;
/*      */ import com.emt.proteus.xserver.display.XWindow;
/*      */ import com.emt.proteus.xserver.display.input.Keyboard;
/*      */ import com.emt.proteus.xserver.io.ComChannel;
/*      */ import com.emt.proteus.xserver.io.IOLSB;
/*      */ import com.emt.proteus.xserver.io.IOMSB;
/*      */ import com.emt.proteus.xserver.protocol.Acl;
/*      */ import com.emt.proteus.xserver.protocol.Atom;
/*      */ import com.emt.proteus.xserver.protocol.XConstants;
/*      */ import java.awt.Toolkit;
/*      */ import java.io.IOException;
/*      */ import java.io.InputStream;
/*      */ import java.io.OutputStream;
/*      */ import java.io.PrintStream;
/*      */ import java.util.Enumeration;
/*      */ import java.util.Vector;
/*      */ 
/*      */ public final class XClient
/*      */   implements Runnable
/*      */ {
/*   39 */   public static Object LOCK = XClient.class;
/*   40 */   private static final Object GrabServerLOCK = new Object();
/*      */   
/*      */   public static final int BITSFORRESOURCES = 22;
/*      */   
/*      */   public static final int BITSFORCLIENTS = 7;
/*      */   
/*      */   public static final int MAXCLIENTS = 128;
/*      */   
/*      */   public static final int CLIENTOFFSET = 22;
/*      */   
/*      */   public static final int CLIENTMASK = 532676608;
/*      */   
/*      */   public static final int IDMASK = 4194303;
/*      */   
/*      */   public static final int FALSE = 0;
/*      */   
/*      */   public static final int TRUE = 1;
/*      */   
/*      */   private static final int DestroyAll = 0;
/*      */   private static final int AllTemporary = 0;
/*      */   private static final int RetainPermanent = 1;
/*      */   private static final int RetainTemporary = 2;
/*      */   private static final int ClientStateInitial = 0;
/*      */   private static final int ClientStateAuthenticating = 1;
/*      */   private static final int ClientStateRunning = 2;
/*      */   private static final int ClientStateRetained = 3;
/*      */   private static final int ClientStateGone = 4;
/*      */   private static final int ClientStateCheckingSecurity = 5;
/*      */   private static final int ClientStateCheckedSecurity = 6;
/*   69 */   public static final XClient[] X_CLIENTs = new XClient[''];
/*   70 */   public static int nextClient = 1;
/*   71 */   public static int currentMaxClients = 1;
/*      */   
/*   73 */   public static int servergraber = -1;
/*      */   
/*      */   public static final int BIGE = 66;
/*      */   public static final int LITTLEE = 108;
/*      */   private int sequence;
/*      */   public int index;
/*   79 */   public boolean swap = false;
/*      */   
/*      */   public int clientAsMask;
/*      */   public int requestBuffer;
/*   83 */   public boolean serverisgrabed = false;
/*      */   
/*   85 */   public boolean suspended = false;
/*   86 */   public boolean waitforreq = false;
/*      */   
/*   88 */   public int closeDownMode = 0;
/*   89 */   public boolean clientGone = false;
/*      */   
/*   91 */   public byte[] bbuffer = null;
/*   92 */   public byte[] sevent = null;
/*   93 */   public char[] cbuffer = null;
/*   94 */   public int[] xarray = new int[4];
/*   95 */   public int[] yarray = new int[4];
/*   96 */   public final Event cevent = new Event();
/*      */   
/*      */   public ComChannel channel;
/*   99 */   public SaveSet saveSet = null;
/*      */   
/*      */   public int reqType;
/*      */   
/*      */   public int data;
/*      */   
/*      */   public int length;
/*      */   public int errorValue;
/*      */   public int errorReason;
/*  108 */   static Vector listeners = new Vector();
/*      */   private volatile boolean running;
/*      */   
/*  111 */   public XClient() { this(null); }
/*      */   
/*      */   public XClient(ComChannel channel) {
/*  114 */     if (channel == null) {
/*  115 */       this.closeDownMode = 1;
/*  116 */       this.index = 0;
/*  117 */       return;
/*      */     }
/*  119 */     this.channel = channel;
/*      */     
/*  121 */     if (!(channel instanceof IOMSB)) this.swap = true; else {
/*  122 */       this.swap = false;
/*      */     }
/*  124 */     this.bbuffer = new byte['Ѐ'];
/*  125 */     this.cbuffer = new char['Ѐ'];
/*      */     
/*  127 */     if (this.swap) { this.sevent = new byte[32];
/*      */     }
/*  129 */     this.index = -1;
/*      */     
/*  131 */     synchronized (LOCK) {
/*  132 */       if (servergraber != -1) this.serverisgrabed = true; else
/*  133 */         this.serverisgrabed = false;
/*  134 */       if (nextClient < 128) {
/*  135 */         X_CLIENTs[nextClient] = this;
/*  136 */         this.index = nextClient;
/*  137 */         this.clientAsMask = (this.index << 22);
/*  138 */         while ((nextClient < 128) && 
/*  139 */           (X_CLIENTs[nextClient] != null)) {
/*  140 */           nextClient += 1;
/*      */         }
/*  142 */         if (this.index == currentMaxClients) {
/*  143 */           currentMaxClients += 1;
/*      */         }
/*  145 */         Resource.initClientResource(this);
/*      */       }
/*      */       else {
/*  148 */         this.index = -1;
/*  149 */         return;
/*      */       }
/*      */     }
/*      */     
/*  153 */     connected(this.index);
/*      */     
/*  155 */     if (this.index != -1) {
/*  156 */       try { init();
/*      */       } catch (Exception e) {
/*  158 */         e.printStackTrace();
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public static void addListener(ClientListener cl)
/*      */   {
/*  167 */     listeners.addElement(cl);
/*      */   }
/*      */   
/*  170 */   public static void removeListener(ClientListener cl) { listeners.removeElement(cl); }
/*      */   
/*      */   static void connected(int index) {
/*  173 */     for (Enumeration e = listeners.elements(); e.hasMoreElements();)
/*  174 */       ((ClientListener)e.nextElement()).connected(index);
/*      */   }
/*      */   
/*      */   static void disconnected(int index) {
/*  178 */     for (Enumeration e = listeners.elements(); e.hasMoreElements();) {
/*  179 */       ((ClientListener)e.nextElement()).disconnected(index);
/*      */     }
/*      */   }
/*      */   
/*      */   public static XClient connect(XDisplay XDisplay, InputStream inputStream, OutputStream outputStream) throws IOException
/*      */   {
/*  185 */     byte[] sniff = new byte[1];
/*  186 */     inputStream.read(sniff);
/*      */     
/*      */ 
/*  189 */     int order = sniff[0] & 0xFF;
/*  190 */     ComChannel channel; switch (order) {
/*      */     case 108: 
/*  192 */       channel = new IOLSB(inputStream, outputStream);
/*  193 */       break;
/*      */     case 66: 
/*  195 */       channel = new IOMSB(inputStream, outputStream);
/*  196 */       break;
/*      */     default: 
/*  198 */       System.err.printf("Unexpected byte order %2X\n", new Object[] { Integer.valueOf(order) });
/*  199 */       return null;
/*      */     }
/*  201 */     return new XClient(channel);
/*      */   }
/*      */   
/*      */   public ComChannel getChannel() {
/*  205 */     return this.channel;
/*      */   }
/*      */   
/*      */   public int getSequence() {
/*  209 */     return this.sequence;
/*      */   }
/*      */   
/*      */ 
/*      */   public void run()
/*      */   {
/*      */     try
/*      */     {
/*  217 */       this.running = true;
/*  218 */       while (this.running) {
/*  219 */         this.waitforreq = true;
/*  220 */         this.errorReason = 0;
/*  221 */         this.reqType = this.channel.readByte();
/*  222 */         this.sequence += 1;
/*      */         
/*  224 */         this.suspended = false;
/*  225 */         this.waitforreq = false;
/*      */         
/*  227 */         if (this.reqType == 36) {
/*  228 */           setGraber(this.index);
/*  229 */           if (servergraber != this.index) {
/*  230 */             this.suspended = true;
/*  231 */             while (servergraber != this.index) {
/*  232 */               try { Thread.sleep(10L); } catch (Exception e) {}
/*  233 */               setGraber(this.index);
/*      */             }
/*  235 */             this.suspended = false;
/*      */           }
/*      */           
/*      */         }
/*  239 */         else if ((servergraber != -1) && (servergraber != this.index)) {
/*  240 */           this.suspended = true;
/*  241 */           while ((servergraber != -1) && (servergraber != this.index))
/*  242 */             try { Thread.sleep(10L);
/*      */             } catch (Exception e) {}
/*  244 */           this.suspended = false;
/*      */         }
/*      */         
/*  247 */         this.data = this.channel.readByte();
/*  248 */         this.length = this.channel.readShort();
/*  249 */         int foo; int n; switch (this.reqType) {
/*      */         case 1: 
/*  251 */           XWindow.reqCreateWindow(this);
/*  252 */           break;
/*      */         case 2: 
/*  254 */           XWindow.reqChangeWindowAttributes(this);
/*  255 */           break;
/*      */         case 3: 
/*  257 */           XWindow.reqGetWindowAttributes(this);
/*  258 */           break;
/*      */         case 4: 
/*  260 */           XWindow.reqDestroyWindow(this);
/*  261 */           break;
/*      */         case 5: 
/*  263 */           XWindow.reqDestroySubwindows(this);
/*  264 */           break;
/*      */         case 6: 
/*  266 */           SaveSet.reqChangeSaveSet(this);
/*  267 */           break;
/*      */         case 7: 
/*  269 */           XWindow.reqReparentWindow(this);
/*  270 */           break;
/*      */         case 8: 
/*  272 */           XWindow.reqMapWindow(this);
/*  273 */           break;
/*      */         case 9: 
/*  275 */           XWindow.reqMapSubWindows(this);
/*  276 */           break;
/*      */         case 10: 
/*  278 */           XWindow.reqUnmapWindow(this);
/*  279 */           break;
/*      */         case 11: 
/*  281 */           XWindow.reqUnmapSubWindows(this);
/*  282 */           break;
/*      */         case 12: 
/*  284 */           XWindow.reqConfigureWindow(this);
/*  285 */           break;
/*      */         case 13: 
/*  287 */           XWindow.reqCirculateWindow(this);
/*  288 */           break;
/*      */         case 14: 
/*  290 */           XWindow.reqGetGeometry(this);
/*  291 */           break;
/*      */         case 15: 
/*  293 */           XWindow.reqQueryTree(this);
/*  294 */           break;
/*      */         case 16: 
/*  296 */           Atom.reqInternAtom(this);
/*  297 */           break;
/*      */         case 17: 
/*  299 */           Atom.reqGetAtomName(this);
/*  300 */           break;
/*      */         case 18: 
/*  302 */           Property.reqChangeProperty(this);
/*  303 */           break;
/*      */         case 19: 
/*  305 */           Property.reqDeleteProperty(this);
/*  306 */           break;
/*      */         case 20: 
/*  308 */           Property.reqGetProperty(this);
/*  309 */           break;
/*      */         case 21: 
/*  311 */           Property.reqListProperties(this);
/*  312 */           break;
/*      */         case 22: 
/*  314 */           Selection.reqSetSelectionOwner(this);
/*  315 */           break;
/*      */         case 23: 
/*  317 */           Selection.reqGetSelectionOwner(this);
/*  318 */           break;
/*      */         case 24: 
/*  320 */           Selection.reqConvertSelection(this);
/*  321 */           break;
/*      */         case 25: 
/*  323 */           XWindow.reqSendEvent(this);
/*  324 */           break;
/*      */         case 26: 
/*  326 */           XWindow.reqGrabPointer(this);
/*  327 */           break;
/*      */         case 27: 
/*  329 */           XWindow.reqUngrabPointer(this);
/*  330 */           break;
/*      */         case 28: 
/*  332 */           XWindow.reqGrabButton(this);
/*  333 */           break;
/*      */         case 29: 
/*  335 */           XWindow.reqUngrabButton(this);
/*  336 */           break;
/*      */         
/*      */         case 30: 
/*  339 */           foo = this.channel.readInt();
/*  340 */           foo = this.channel.readInt();
/*  341 */           foo = this.channel.readShort();
/*  342 */           this.channel.readPad(2);
/*  343 */           break;
/*      */         case 31: 
/*  345 */           Keyboard.reqGrabKeyboard(this);
/*  346 */           break;
/*      */         
/*      */         case 32: 
/*  349 */           foo = this.channel.readInt();
/*  350 */           break;
/*      */         
/*      */         case 33: 
/*  353 */           foo = this.channel.readInt();
/*  354 */           foo = this.channel.readShort();
/*  355 */           foo = this.channel.readByte();
/*  356 */           foo = this.channel.readByte();
/*  357 */           foo = this.channel.readByte();
/*  358 */           this.channel.readPad(3);
/*  359 */           break;
/*      */         
/*      */         case 34: 
/*  362 */           foo = this.channel.readInt();
/*  363 */           foo = this.channel.readShort();
/*  364 */           this.channel.readPad(2);
/*  365 */           break;
/*      */         case 35: 
/*  367 */           XWindow.reqAllowEvents(this);
/*  368 */           break;
/*      */         case 36: 
/*  370 */           reqGrabServer(this);
/*  371 */           break;
/*      */         case 37: 
/*  373 */           reqUngrabServer(this);
/*  374 */           break;
/*      */         case 38: 
/*  376 */           XWindow.reqQueryPointer(this);
/*  377 */           break;
/*      */         case 39: 
/*  379 */           XWindow.reqGetMotionEvents(this);
/*  380 */           break;
/*      */         case 40: 
/*  382 */           XWindow.reqTranslateCoordinates(this);
/*  383 */           break;
/*      */         
/*      */         case 41: 
/*  386 */           foo = this.channel.readInt();
/*  387 */           foo = this.channel.readInt();
/*  388 */           foo = this.channel.readShort();
/*  389 */           foo = this.channel.readShort();
/*  390 */           foo = this.channel.readShort();
/*  391 */           foo = this.channel.readShort();
/*  392 */           foo = this.channel.readShort();
/*  393 */           foo = this.channel.readShort();
/*  394 */           break;
/*      */         case 42: 
/*  396 */           XWindow.reqSetInputFocus(this);
/*  397 */           break;
/*      */         case 43: 
/*  399 */           XWindow.reqGetInputFocus(this);
/*  400 */           break;
/*      */         case 44: 
/*  402 */           Keyboard.reqQueryKeymap(this);
/*  403 */           break;
/*      */         case 45: 
/*  405 */           XFont.reqOpenFont(this);
/*  406 */           break;
/*      */         case 46: 
/*  408 */           XFont.reqCloseFont(this);
/*  409 */           break;
/*      */         case 47: 
/*  411 */           XFont.reqQueryFont(this);
/*  412 */           break;
/*      */         case 48: 
/*  414 */           XFont.reqQueryTextExtents(this);
/*  415 */           break;
/*      */         case 49: 
/*  417 */           XFont.reqListFonts(this);
/*  418 */           break;
/*      */         case 50: 
/*  420 */           XFont.reqListFontsWithInfo(this);
/*  421 */           break;
/*      */         case 51: 
/*  423 */           XFont.reqSetFontPath(this);
/*  424 */           break;
/*      */         case 52: 
/*  426 */           XFont.reqGetFontPath(this);
/*  427 */           break;
/*      */         case 53: 
/*  429 */           Pixmap.reqCreatePixmap(this);
/*  430 */           break;
/*      */         case 54: 
/*  432 */           Pixmap.reqFreePixmap(this);
/*  433 */           break;
/*      */         case 55: 
/*  435 */           GC.reqCreateGC(this);
/*  436 */           break;
/*      */         case 56: 
/*  438 */           GC.reqChangeGC(this);
/*  439 */           break;
/*      */         case 57: 
/*  441 */           GC.reqCopyGC(this);
/*  442 */           break;
/*      */         case 58: 
/*  444 */           GC.reqSetDashes(this);
/*  445 */           break;
/*      */         case 59: 
/*  447 */           GC.reqSetClipRectangles(this);
/*  448 */           break;
/*      */         case 60: 
/*  450 */           GC.reqFreeGC(this);
/*  451 */           break;
/*      */         case 61: 
/*  453 */           XWindow.reqClearArea(this);
/*  454 */           break;
/*      */         case 62: 
/*  456 */           XWindow.reqCopyArea(this);
/*  457 */           break;
/*      */         case 63: 
/*  459 */           XWindow.reqCopyPlane(this);
/*  460 */           break;
/*      */         case 64: 
/*      */         case 65: 
/*      */         case 66: 
/*      */         case 67: 
/*      */         case 68: 
/*      */         case 69: 
/*      */         case 70: 
/*      */         case 71: 
/*      */         case 74: 
/*      */         case 75: 
/*      */         case 76: 
/*      */         case 77: 
/*  473 */           foo = this.channel.readInt();
/*  474 */           this.length -= 2;
/*  475 */           Drawable d = lookupDrawable(foo);
/*  476 */           if (d == null) {
/*  477 */             this.errorValue = foo;
/*  478 */             this.errorReason = 9;
/*      */           }
/*      */           else {
/*  481 */             foo = this.channel.readInt();
/*  482 */             this.length -= 1;
/*  483 */             GC gc = lookupGC(foo);
/*  484 */             if (gc == null) {
/*  485 */               this.errorValue = foo;
/*  486 */               this.errorReason = 13;
/*      */             } else { int x;
/*      */               int y;
/*  489 */               switch (this.reqType) {
/*      */               case 64: 
/*  491 */                 Draw.reqPolyPoint(this, d, gc);
/*  492 */                 break;
/*      */               case 65: 
/*  494 */                 Draw.reqPolyLine(this, d, gc);
/*  495 */                 break;
/*      */               case 66: 
/*  497 */                 Draw.reqPolySegment(this, d, gc);
/*  498 */                 break;
/*      */               case 67: 
/*  500 */                 Draw.reqPolyRectangle(this, d, gc);
/*  501 */                 break;
/*      */               case 68: 
/*  503 */                 Draw.reqPolyArc(this, d, gc);
/*  504 */                 break;
/*      */               case 69: 
/*  506 */                 Draw.reqFillPoly(this, d, gc);
/*  507 */                 break;
/*      */               case 70: 
/*  509 */                 Draw.reqPolyFillRectangle(this, d, gc);
/*  510 */                 break;
/*      */               case 71: 
/*  512 */                 Draw.reqFillPolyArc(this, d, gc);
/*  513 */                 break;
/*      */               case 74: 
/*  515 */                 x = (short)this.channel.readShort();
/*  516 */                 y = (short)this.channel.readShort();
/*  517 */                 this.length -= 1;
/*  518 */                 Draw.reqPolyText8(this, d, gc, x, y);
/*  519 */                 break;
/*      */               case 75: 
/*  521 */                 x = (short)this.channel.readShort();
/*  522 */                 y = (short)this.channel.readShort();
/*  523 */                 this.length -= 1;
/*  524 */                 Draw.reqPolyText16(this, d, gc, x, y);
/*  525 */                 break;
/*      */               case 76: 
/*  527 */                 x = (short)this.channel.readShort();
/*  528 */                 y = (short)this.channel.readShort();
/*  529 */                 this.length -= 1;
/*  530 */                 Draw.reqImageText8(this, d, gc, x, y);
/*  531 */                 break;
/*      */               case 77: 
/*  533 */                 x = (short)this.channel.readShort();
/*  534 */                 y = (short)this.channel.readShort();
/*  535 */                 this.length -= 1;
/*  536 */                 Draw.reqImageText16(this, d, gc, x, y); }
/*      */             }
/*      */           }
/*  539 */           break;
/*      */         case 72: 
/*  541 */           Pixmap.reqPutImage(this);
/*  542 */           break;
/*      */         case 73: 
/*  544 */           Pixmap.reqGetImage(this);
/*  545 */           break;
/*      */         case 78: 
/*  547 */           Colormap.reqCreateColormap(this);
/*  548 */           break;
/*      */         
/*      */         case 79: 
/*  551 */           foo = this.channel.readInt();
/*  552 */           break;
/*      */         
/*      */         case 80: 
/*  555 */           foo = this.channel.readInt();
/*  556 */           foo = this.channel.readInt();
/*  557 */           break;
/*      */         case 81: 
/*  559 */           Colormap.reqInstallColormap(this);
/*  560 */           break;
/*      */         case 82: 
/*  562 */           Colormap.reqUninstallColormap(this);
/*  563 */           break;
/*      */         case 83: 
/*  565 */           Colormap.reqListInstalledColormaps(this);
/*  566 */           break;
/*      */         case 84: 
/*  568 */           Colormap.reqAllocColor(this);
/*  569 */           break;
/*      */         case 85: 
/*  571 */           Colormap.reqAllocNamedColor(this);
/*  572 */           break;
/*      */         case 86: 
/*  574 */           Colormap.reqAllocColorCells(this);
/*  575 */           break;
/*      */         case 87: 
/*  577 */           Colormap.reqAllocColorPlanes(this);
/*  578 */           break;
/*      */         case 88: 
/*  580 */           Colormap.reqFreeColors(this);
/*  581 */           break;
/*      */         case 89: 
/*  583 */           Colormap.reqStoreColors(this);
/*  584 */           break;
/*      */         case 90: 
/*  586 */           Colormap.reqStoreNamedColor(this);
/*  587 */           break;
/*      */         case 91: 
/*  589 */           Colormap.reqQueryColors(this);
/*  590 */           break;
/*      */         case 92: 
/*  592 */           Colormap.reqLookupColor(this);
/*  593 */           break;
/*      */         case 93: 
/*  595 */           Cursor.reqCreateCursor(this);
/*  596 */           break;
/*      */         case 94: 
/*  598 */           Cursor.reqCreateGlyphCursor(this);
/*  599 */           break;
/*      */         case 95: 
/*  601 */           Cursor.reqFreeCursor(this);
/*  602 */           break;
/*      */         case 96: 
/*  604 */           Cursor.reqRecolorCursor(this);
/*  605 */           break;
/*      */         case 97: 
/*  607 */           XWindow.reqQueryBestSize(this);
/*  608 */           break;
/*      */         case 98: 
/*  610 */           Extension.reqQueryExtension(this);
/*  611 */           break;
/*      */         case 99: 
/*  613 */           Extension.reqListExtensions(this);
/*  614 */           break;
/*      */         case 100: 
/*  616 */           Keyboard.reqChangeKeyboardMapping(this);
/*  617 */           break;
/*      */         case 101: 
/*  619 */           Keyboard.reqGetKeyboardMapping(this);
/*  620 */           break;
/*      */         
/*      */         case 102: 
/*  623 */           n = this.length;
/*  624 */           foo = this.channel.readInt();
/*  625 */           n -= 2;
/*  626 */           this.channel.readPad(n * 4);
/*  627 */           break;
/*      */         case 103: 
/*  629 */           Keyboard.reqGetKeyboardControl(this);
/*  630 */           break;
/*      */         
/*      */         case 104: 
/*  633 */           Toolkit.getDefaultToolkit().beep();
/*  634 */           break;
/*      */         
/*      */         case 105: 
/*  637 */           foo = this.length;
/*  638 */           foo = this.channel.readShort();
/*  639 */           foo = this.channel.readShort();
/*  640 */           foo = this.channel.readShort();
/*  641 */           foo = this.channel.readByte();
/*  642 */           foo = this.channel.readByte();
/*  643 */           break;
/*      */         case 106: 
/*  645 */           XWindow.reqGetPointerControl(this);
/*  646 */           break;
/*      */         
/*      */         case 107: 
/*  649 */           foo = this.channel.readShort();
/*  650 */           foo = this.channel.readShort();
/*  651 */           foo = this.channel.readByte();
/*  652 */           foo = this.channel.readByte();
/*  653 */           this.channel.readPad(2);
/*  654 */           break;
/*      */         case 108: 
/*  656 */           XWindow.reqGetScreenSaver(this);
/*  657 */           break;
/*      */         case 109: 
/*  659 */           Acl.reqChangeHosts(this);
/*  660 */           break;
/*      */         case 110: 
/*  662 */           Acl.reqListHosts(this);
/*  663 */           break;
/*      */         case 111: 
/*  665 */           Acl.reqSetAccessControl(this);
/*  666 */           break;
/*      */         
/*      */         case 112: 
/*  669 */           this.length -= 1;
/*  670 */           if ((this.data == 0) || (this.data == 1) || (this.data == 2))
/*      */           {
/*      */ 
/*  673 */             this.closeDownMode = this.data;
/*      */           }
/*      */           else {
/*  676 */             this.errorValue = this.data;
/*  677 */             this.errorReason = 2;
/*      */           }
/*  679 */           break;
/*      */         case 113: 
/*  681 */           reqKillClient(this);
/*  682 */           break;
/*      */         case 114: 
/*  684 */           Property.reqRotateProperties(this);
/*  685 */           break;
/*      */         case 115: 
/*      */           break;
/*      */         
/*      */         case 116: 
/*  690 */           XWindow.reqSetPointerMapping(this);
/*  691 */           break;
/*      */         case 117: 
/*  693 */           XWindow.reqGetPointerMapping(this);
/*  694 */           break;
/*      */         case 118: 
/*  696 */           Keyboard.reqSetModifierMapping(this);
/*  697 */           break;
/*      */         case 119: 
/*  699 */           Keyboard.reqGetModifierMapping(this);
/*  700 */           break;
/*      */         
/*      */         case 127: 
/*  703 */           n = this.length;
/*  704 */           n--;
/*  705 */         case 120: case 121: case 122: case 123: case 124: case 125: case 126: default:  while (n != 0) {
/*  706 */             foo = this.channel.readInt();
/*  707 */             n--; continue;
/*      */             
/*      */ 
/*      */ 
/*  711 */             if (this.reqType == -1) {
/*      */               return;
/*      */             }
/*      */             
/*  715 */             Extension.dispatch(this.reqType, this);
/*      */           }
/*      */         }
/*  718 */         if (this.errorReason != 0) {
/*  719 */           while (this.length != 0) {
/*  720 */             this.channel.readInt();
/*  721 */             this.length -= 1;
/*      */           }
/*  723 */           this.cevent.mkError(this.errorReason, this.errorValue, this.reqType <= 127 ? 0 : this.data, this.reqType);
/*      */           
/*  725 */           sendEvent(this.cevent, 1, 0, 0, null);
/*      */         }
/*      */         
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  732 */         this.channel.flush();
/*      */       }
/*      */     }
/*      */     catch (Exception e) {}finally
/*      */     {
/*  737 */       closeDown();
/*      */     }
/*      */   }
/*      */   
/*  741 */   static int motionBufferSize = 0;
/*  742 */   static int maxRequestLength = 65535;
/*  743 */   static byte[] vendor = null;
/*      */   
/*      */ 
/*  746 */   static int bitmapScanUnit = 32;
/*  747 */   static int bitmapScanPad = 32;
/*  748 */   public static int initialLength = 0;
/*      */   
/*      */   void writeByte(ComChannel out) throws IOException {
/*  751 */     synchronized (out) {
/*  752 */       out.writeByte(1);
/*  753 */       out.writePad(1);
/*  754 */       out.writeShort(11);
/*  755 */       out.writeShort(0);
/*  756 */       out.writeShort(initialLength);
/*  757 */       out.writeInt(1032);
/*  758 */       out.writeInt(this.clientAsMask);
/*  759 */       out.writeInt(4194303);
/*      */       
/*  761 */       out.writeInt(motionBufferSize);
/*  762 */       out.writeShort(XConstants.VENDOR.length);
/*  763 */       out.writeShort(maxRequestLength);
/*  764 */       out.writeByte(Screen.screen.length);
/*  765 */       out.writeByte(Format.format.length);
/*  766 */       out.writeByte(XDisplay.imageByteOrder);
/*  767 */       out.writeByte(XDisplay.bitmapBitOrder);
/*  768 */       out.writeByte(bitmapScanUnit);
/*  769 */       out.writeByte(bitmapScanPad);
/*  770 */       out.writeByte(Keyboard.keyboard.minKeyCode);
/*  771 */       out.writeByte(Keyboard.keyboard.maxKeyCode);
/*  772 */       out.writePad(4);
/*  773 */       out.writeByte(XConstants.VENDOR);
/*  774 */       if ((-XConstants.VENDOR.length & 0x3) != 0) out.writePad(-XConstants.VENDOR.length & 0x3);
/*  775 */       for (int i = 0; i < Format.format.length; i++) {
/*  776 */         Format.format[i].writeByte(out);
/*      */       }
/*  778 */       for (int i = 0; i < Screen.screen.length; i++) {
/*  779 */         Screen.screen[i].writeByte(out);
/*      */       }
/*  781 */       out.flush();
/*      */     }
/*      */   }
/*      */   
/*      */   private final void prolog() throws IOException
/*      */   {
/*  787 */     int foo = this.channel.readByte();
/*  788 */     foo = this.channel.readShort();
/*  789 */     foo = this.channel.readShort();
/*  790 */     int name = foo = this.channel.readShort();
/*  791 */     int data = foo = this.channel.readShort();
/*  792 */     this.channel.readPad(2);
/*  793 */     if (name > 0) {
/*  794 */       this.channel.readByte(this.bbuffer, 0, name);
/*  795 */       this.channel.readPad(-name & 0x3);
/*      */     }
/*  797 */     if (data > 0) {
/*  798 */       this.channel.readByte(this.bbuffer, 0, data);
/*  799 */       this.channel.readPad(-data & 0x3);
/*      */     }
/*      */   }
/*      */   
/*      */   private final void init() throws IOException {
/*  804 */     prolog();
/*  805 */     writeByte(this.channel);
/*  806 */     this.sequence = 0;
/*      */   }
/*      */   
/*      */   private static final void reqGrabServer(XClient c) throws IOException {
/*  810 */     synchronized (LOCK) {
/*  811 */       for (int i = 1; i < X_CLIENTs.length; i++) {
/*  812 */         if ((i != c.index) && 
/*  813 */           (X_CLIENTs[i] != null) && 
/*  814 */           (X_CLIENTs[i].isRunning()))
/*  815 */           X_CLIENTs[i].serverisgrabed = true;
/*      */       }
/*      */     }
/*  818 */     int i = 0;
/*  819 */     int ii = 100;
/*      */     for (;;) {
/*  821 */       try { Thread.yield(); } catch (Exception e) {}
/*  822 */       for (i = 1; i < X_CLIENTs.length; i++)
/*  823 */         if ((i != c.index) && (X_CLIENTs[i] != null) && (X_CLIENTs[i].isRunning()) && (!X_CLIENTs[i].waitforreq) && (!X_CLIENTs[i].suspended))
/*      */         {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  830 */           i = 0;
/*  831 */           break;
/*      */         }
/*  833 */       if (i == 0) {
/*  834 */         ii--;
/*  835 */         if (ii == 0) {
/*      */           break;
/*      */         }
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */   private static final void reqUngrabServer(XClient c) throws IOException {
/*  843 */     synchronized (LOCK) {
/*  844 */       for (int i = 1; i < X_CLIENTs.length; i++) {
/*  845 */         if ((i != c.index) && (X_CLIENTs[i] != null) && (X_CLIENTs[i].isRunning()))
/*      */         {
/*      */ 
/*      */ 
/*      */ 
/*  850 */           X_CLIENTs[i].serverisgrabed = false; }
/*      */       }
/*  852 */       unGraber();
/*      */     }
/*      */   }
/*      */   
/*      */   private static final void reqKillClient(XClient c) throws IOException
/*      */   {
/*  858 */     int foo = c.channel.readInt();
/*  859 */     c.length -= 2;
/*  860 */     if (foo == 0) {
/*  861 */       closeDownRetainedResources(); return;
/*      */     }
/*      */     
/*      */     XClient killclient;
/*      */     
/*  866 */     if ((killclient = Resource.lookupClient(foo)) != null) {
/*  867 */       killclient.closeDown();
/*      */     }
/*      */     else {
/*  870 */       c.errorValue = foo;
/*  871 */       c.errorReason = 2;
/*      */     }
/*      */   }
/*      */   
/*      */   public static final void closeDownRetainedResources()
/*      */   {
/*  877 */     synchronized (LOCK) {
/*  878 */       for (int i = 1; i < X_CLIENTs.length; i++) {
/*  879 */         XClient c = X_CLIENTs[i];
/*  880 */         if ((c != null) && (c.closeDownMode == 2) && (c.clientGone))
/*      */         {
/*      */ 
/*      */ 
/*  884 */           c.closeDown();
/*      */         }
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */   public static final void closeDownAll() {
/*  891 */     synchronized (LOCK) {
/*  892 */       for (int i = 1; i < X_CLIENTs.length; i++) {
/*  893 */         if (X_CLIENTs[i] != null) {
/*  894 */           X_CLIENTs[i].closeDown();
/*      */         }
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */   public final void closeDown()
/*      */   {
/*  902 */     boolean really_close_down = (this.clientGone) || (this.closeDownMode == 0);
/*  903 */     synchronized (LOCK) {
/*  904 */       if (!this.clientGone) {
/*  905 */         if (servergraber == this.index) {
/*  906 */           for (int i = 1; i < X_CLIENTs.length; i++) {
/*  907 */             if ((i != this.index) && (X_CLIENTs[i] != null) && (X_CLIENTs[i].isRunning()))
/*      */             {
/*      */ 
/*      */ 
/*      */ 
/*  912 */               X_CLIENTs[i].serverisgrabed = false; }
/*      */           }
/*  914 */           unGraber();
/*      */         }
/*  916 */         if ((XWindow.grab != null) && (this != XWindow.grab.getClient()))
/*      */         {
/*  918 */           XWindow.grab = null;
/*      */         }
/*  920 */         Selection.delete(this);
/*      */         
/*  922 */         if (!really_close_down) {
/*  923 */           Resource.freeClientNeverResources(this);
/*      */         }
/*  925 */         this.clientGone = true;
/*  926 */         if (this.channel != null)
/*  927 */           try { this.channel.close();
/*      */           } catch (Exception ee) {}
/*      */       }
/*  930 */       if (really_close_down) {
/*  931 */         try { SaveSet.handle(this);
/*      */         }
/*      */         catch (Exception ee) {}
/*      */         
/*  935 */         Resource.freeClientResources(this);
/*  936 */         if (this.index < nextClient) {
/*  937 */           nextClient = this.index;
/*      */         }
/*  939 */         X_CLIENTs[this.index] = null;
/*  940 */         while (X_CLIENTs[(currentMaxClients - 1)] == null) {
/*  941 */           currentMaxClients -= 1;
/*      */         }
/*  943 */         disconnected(this.index);
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */   public final int sendEvent(Event event, int count, int mask, int filter, Grab grab)
/*      */     throws IOException
/*      */   {
/*  952 */     if ((this != X_CLIENTs[0]) && ((filter == 0) || ((mask & filter) != 0))) {
/*  953 */       if ((grab != null) && (!grab.sameClient(this))) {
/*  954 */         return -1;
/*      */       }
/*  956 */       synchronized (this.channel) {
/*  957 */         event.putSequence(this.sequence);
/*  958 */         sendEvent(count, event);
/*      */       }
/*  960 */       return 1;
/*      */     }
/*      */     
/*  963 */     return 0;
/*      */   }
/*      */   
/*      */   public void sendEvent(int count, Event event) throws IOException
/*      */   {
/*      */     byte[] bb;
/*  969 */     if (this.swap) {
/*  970 */       byte[] bb = this.sevent;
/*  971 */       event.swap(bb);
/*      */     } else {
/*  973 */       bb = event.event;
/*      */     }
/*      */     try {
/*  976 */       this.channel.immediateWrite(bb, 0, 32);
/*      */     }
/*      */     catch (IOException e) {
/*  979 */       if (!this.clientGone) {
/*  980 */         closeDown();
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*  985 */   int bag1id = 0; int bag2id = 0;
/*      */   Drawable bag1d;
/*      */   
/*  988 */   public Drawable lookupDrawable(int id) { if (this.bag1id == id) return this.bag1d;
/*  989 */     if (this.bag2id == id) {
/*  990 */       this.bag2id = this.bag1id;this.bag1id = id;
/*  991 */       Drawable d = this.bag2d;this.bag2d = this.bag1d;this.bag1d = d;
/*  992 */       return d;
/*      */     }
/*  994 */     Resource r = Resource.lookupIDByClass(id, 1073741824);
/*  995 */     if ((r != null) && ((r instanceof Drawable))) {
/*  996 */       this.bag2id = this.bag1id;this.bag2d = this.bag1d;
/*  997 */       this.bag1id = id;this.bag1d = ((Drawable)r);
/*  998 */       return this.bag1d;
/*      */     }
/* 1000 */     return null;
/*      */   }
/*      */   
/*      */   Drawable bag2d;
/* 1004 */   public XWindow lookupWindow(int id) { Drawable d = lookupDrawable(id);
/* 1005 */     if ((d == null) || ((d instanceof XWindow))) return (XWindow)d;
/* 1006 */     return null;
/*      */   }
/*      */   
/* 1009 */   int bag1gid = 0; int bag2gid = 0;
/*      */   GC bag1gc;
/*      */   
/* 1012 */   public GC lookupGC(int id) { if (this.bag1gid == id) return this.bag1gc;
/* 1013 */     if (this.bag2gid == id) {
/* 1014 */       this.bag2gid = this.bag1gid;this.bag1gid = id;
/* 1015 */       GC gc = this.bag2gc;this.bag2gc = this.bag1gc;this.bag1gc = gc;
/* 1016 */       return gc;
/*      */     }
/* 1018 */     Resource r = Resource.lookupIDByType(id, -2147483645);
/* 1019 */     if ((r != null) && ((r instanceof GC))) {
/* 1020 */       this.bag2gid = this.bag1gid;this.bag2gc = this.bag1gc;
/* 1021 */       this.bag1gid = id;this.bag1gc = ((GC)r);
/* 1022 */       return this.bag1gc;
/*      */     }
/* 1024 */     return null;
/*      */   }
/*      */   
/*      */   GC bag2gc;
/* 1028 */   public static void flushCache(int id) { for (int i = 1; i < X_CLIENTs.length; i++) {
/* 1029 */       XClient c = X_CLIENTs[i];
/* 1030 */       if (c != null) {
/* 1031 */         if (c.bag1id == id) {
/* 1032 */           c.bag1id = c.bag2id;c.bag1d = c.bag2d;
/* 1033 */           c.bag2id = 0;c.bag2d = null;
/*      */ 
/*      */         }
/* 1036 */         else if (c.bag2id == id) {
/* 1037 */           c.bag2id = 0;c.bag2d = null;
/*      */ 
/*      */         }
/* 1040 */         else if (c.bag1gid == id) {
/* 1041 */           c.bag1gid = c.bag2gid;c.bag1gc = c.bag2gc;
/* 1042 */           c.bag2gid = 0;c.bag2gc = null;
/*      */ 
/*      */         }
/* 1045 */         else if (c.bag2gid == id) {
/* 1046 */           c.bag2gid = 0;c.bag2gc = null;
/*      */         }
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */   private static final int setGraber(int i)
/*      */   {
/* 1054 */     synchronized (GrabServerLOCK) {
/* 1055 */       if (servergraber == -1) servergraber = i;
/* 1056 */       return servergraber;
/*      */     }
/*      */   }
/*      */   
/*      */   private static final void unGraber() {
/* 1061 */     servergraber = -1;
/*      */   }
/*      */   
/*      */   public boolean isRunning() {
/* 1065 */     return this.running;
/*      */   }
/*      */ }


/* Location:              C:\Users\Joey\Downloads\launch.jar!\com\emt\proteus\xserver\client\XClient.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */