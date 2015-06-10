/*      */ package com.emt.proteus.xserver.display;
/*      */ 
/*      */ 
/*      */ public final class Event
/*      */ {
/*      */   public static final int KeyPress = 2;
/*      */   
/*      */   public static final int KeyRelease = 3;
/*      */   
/*      */   public static final int ButtonPress = 4;
/*      */   
/*      */   public static final int ButtonRelease = 5;
/*      */   
/*      */   public static final int MotionNotify = 6;
/*      */   
/*      */   public static final int EnterNotify = 7;
/*      */   
/*      */   public static final int LeaveNotify = 8;
/*      */   
/*      */   public static final int FocusIn = 9;
/*      */   
/*      */   public static final int FocusOut = 10;
/*      */   
/*      */   public static final int KeymapNotify = 11;
/*      */   
/*      */   public static final int Expose = 12;
/*      */   
/*      */   public static final int GraphicsExpose = 13;
/*      */   
/*      */   public static final int NoExpose = 14;
/*      */   
/*      */   public static final int VisibilityNotify = 15;
/*      */   
/*      */   public static final int CreateNotify = 16;
/*      */   
/*      */   public static final int DestroyNotify = 17;
/*      */   
/*      */   public static final int UnmapNotify = 18;
/*      */   
/*      */   public static final int MapNotify = 19;
/*      */   
/*      */   public static final int MapRequest = 20;
/*      */   
/*      */   public static final int ReparentNotify = 21;
/*      */   
/*      */   public static final int ConfigureNotify = 22;
/*      */   
/*      */   public static final int ConfigureRequest = 23;
/*      */   
/*      */   public static final int GravityNotify = 24;
/*      */   
/*      */   public static final int ResizeRequest = 25;
/*      */   
/*      */   public static final int CirculateNotify = 26;
/*      */   public static final int CirculateRequest = 27;
/*      */   public static final int PropertyNotify = 28;
/*      */   public static final int SelectionClear = 29;
/*      */   public static final int SelectionRequest = 30;
/*      */   public static final int SelectionNotify = 31;
/*      */   public static final int ColormapNotify = 32;
/*      */   public static final int ClientMessage = 33;
/*      */   public static final int MappingNotify = 34;
/*      */   public static final int LASTEvent = 35;
/*      */   public static final int NoEventMask = 0;
/*      */   public static final int KeyPressMask = 1;
/*      */   public static final int KeyReleaseMask = 2;
/*      */   public static final int ButtonPressMask = 4;
/*      */   public static final int ButtonReleaseMask = 8;
/*      */   public static final int EnterWindowMask = 16;
/*      */   public static final int LeaveWindowMask = 32;
/*      */   public static final int PointerMotionMask = 64;
/*      */   public static final int PointerMotionHintMask = 128;
/*      */   public static final int Button1MotionMask = 256;
/*      */   public static final int Button2MotionMask = 512;
/*      */   public static final int Button3MotionMask = 1024;
/*      */   public static final int Button4MotionMask = 2048;
/*      */   public static final int Button5MotionMask = 4096;
/*      */   public static final int ButtonMotionMask = 8192;
/*      */   public static final int KeymapStateMask = 16384;
/*      */   public static final int ExposureMask = 32768;
/*      */   public static final int VisibilityChangeMask = 65536;
/*      */   public static final int StructureNotifyMask = 131072;
/*      */   public static final int ResizeRedirectMask = 262144;
/*      */   public static final int SubstructureNotifyMask = 524288;
/*      */   public static final int SubstructureRedirectMask = 1048576;
/*      */   public static final int FocusChangeMask = 2097152;
/*      */   public static final int PropertyChangeMask = 4194304;
/*      */   public static final int ColormapChangeMask = 8388608;
/*      */   public static final int OwnerGrabButtonMask = 16777216;
/*      */   public static final int Button1Mask = 256;
/*      */   public static final int Button2Mask = 512;
/*      */   public static final int Button3Mask = 1024;
/*      */   public static final int Button4Mask = 2048;
/*      */   public static final int Button5Mask = 4096;
/*      */   public static final int NoSuchEvent = Integer.MIN_VALUE;
/*      */   public static final int StructureAndSubMask = 655360;
/*      */   public static final int CantBeFiltered = 0;
/*   98 */   public static final int[] filters = { Integer.MIN_VALUE, Integer.MIN_VALUE, 1, 2, 4, 8, 64, 16, 32, 2097152, 2097152, 16384, 32768, 0, 0, 65536, 524288, 655360, 655360, 655360, 1048576, 655360, 655360, 1048576, 655360, 262144, 655360, 1048576, 4194304, 0, 0, 0, 8388608, 0, 0 };
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static final int AtMostOneClient = 1310724;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static final int MotionMask = 16192;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static final int PropagateMask = 16207;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static final int PointerGrabMask = 32764;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static final int EXTENSION_EVENT_BASE = 64;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public byte[] event;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  157 */   public int index = 0;
/*  158 */   public static final byte[] zeros = new byte[32];
/*      */   
/*  160 */   public Event(byte[] bb) { this.event = bb; }
/*  161 */   public Event() { this.event = new byte[32]; }
/*      */   
/*      */   public void clear() {
/*  164 */     System.arraycopy(zeros, 0, this.event, 0, 32);
/*  165 */     this.index = 0;
/*      */   }
/*      */   
/*      */   public void writeByte(byte val) {
/*  169 */     this.event[(this.index++)] = val;
/*      */   }
/*      */   
/*      */   public void writeShort(int val) {
/*  173 */     this.event[(this.index++)] = ((byte)(val >> 8 & 0xFF));
/*  174 */     this.event[(this.index++)] = ((byte)(val & 0xFF));
/*      */   }
/*      */   
/*      */   public void writeInt(int val) {
/*  178 */     this.event[(this.index++)] = ((byte)(val >> 24 & 0xFF));
/*  179 */     this.event[(this.index++)] = ((byte)(val >> 16 & 0xFF));
/*  180 */     this.event[(this.index++)] = ((byte)(val >> 8 & 0xFF));
/*  181 */     this.event[(this.index++)] = ((byte)(val & 0xFF));
/*      */   }
/*      */   
/*  184 */   public void writePad(int i) { this.index += i; }
/*      */   
/*      */   public void _clear() {
/*  187 */     System.arraycopy(zeros, 0, this.event, 0, 32);
/*  188 */     this.index = 0;
/*      */   }
/*      */   
/*      */   private void _writeByte(byte val) {
/*  192 */     this.event[(this.index++)] = val;
/*      */   }
/*      */   
/*      */   private void _writeShort(int val) {
/*  196 */     this.event[(this.index++)] = ((byte)(val >> 8 & 0xFF));
/*  197 */     this.event[(this.index++)] = ((byte)(val & 0xFF));
/*      */   }
/*      */   
/*      */   private void _writeInt(int val)
/*      */   {
/*  202 */     this.event[(this.index++)] = ((byte)(val >> 24 & 0xFF));
/*  203 */     this.event[(this.index++)] = ((byte)(val >> 16 & 0xFF));
/*  204 */     this.event[(this.index++)] = ((byte)(val >> 8 & 0xFF));
/*  205 */     this.event[(this.index++)] = ((byte)(val & 0xFF));
/*      */   }
/*      */   
/*      */ 
/*  209 */   private void _writePad(int i) { this.index += i; }
/*      */   
/*      */   public void putEvent(int e) {
/*  212 */     this.index = 4;
/*  213 */     _writeInt(e);
/*      */   }
/*      */   
/*      */   public void putSequence(int seq) {
/*  217 */     this.index = 2;
/*  218 */     _writeShort(seq);
/*      */   }
/*      */   
/*      */   private int readShort() {
/*  222 */     int s = 0;
/*  223 */     s = this.event[(this.index++)] & 0xFF;
/*  224 */     s = s << 8 & 0xFFFF | this.event[(this.index++)] & 0xFF;
/*  225 */     return s;
/*      */   }
/*      */   
/*      */   public int getState() {
/*  229 */     this.index = 28;
/*  230 */     return readShort(); }
/*      */   
/*  232 */   public int getSameScreen() { return this.event[30]; }
/*  233 */   public int getDetail() { return this.event[1]; }
/*  234 */   public int getFlags() { return this.event[31]; }
/*      */   
/*      */ 
/*      */   public byte[] sevent;
/*      */   public void fixUpEventFromWindow(XWindow w, int child, int rootx, int rooty, boolean calcChild)
/*      */   {
/*  240 */     int flags = 0;
/*  241 */     if (calcChild) {
/*  242 */       XWindow tmpw = XWindow.spriteTrace[(XWindow.spriteTraceGood - 1)];
/*  243 */       while (tmpw != null) {
/*  244 */         if (tmpw == w) {
/*  245 */           child = 0;
/*  246 */           break;
/*      */         }
/*  248 */         if (tmpw.parent == w) {
/*  249 */           child = w.id;
/*  250 */           break;
/*      */         }
/*  252 */         tmpw = tmpw.parent;
/*      */       }
/*      */     }
/*  255 */     int ex = 0;int ey = 0;
/*  256 */     flags = 1;
/*  257 */     ex = rootx - w.x;
/*  258 */     ey = rooty - w.y;
/*  259 */     this.index = 0;
/*  260 */     _writePad(8);
/*  261 */     _writeInt(XWindow.spriteTrace[0].id);
/*  262 */     _writeInt(w.id);
/*  263 */     _writeInt(child);
/*  264 */     _writeShort(rootx);
/*  265 */     _writeShort(rooty);
/*  266 */     _writeShort(ex);
/*  267 */     _writeShort(ey);
/*  268 */     _writePad(2);
/*  269 */     _writeByte((byte)flags);
/*      */   }
/*      */   
/*      */   public void mkCirculateRequest(int parent, int window, int place) {
/*  273 */     _clear();
/*  274 */     _writeByte((byte)27);
/*  275 */     _writePad(1);_writePad(2);
/*  276 */     _writeInt(parent);
/*  277 */     _writeInt(window);
/*  278 */     _writePad(4);
/*  279 */     _writeByte((byte)place);
/*      */   }
/*      */   
/*      */   public void mkCirculateNotify(int event, int window, int parent, int place) {
/*  283 */     _clear();
/*  284 */     _writeByte((byte)26);
/*  285 */     _writePad(1);_writePad(2);
/*  286 */     _writeInt(event);
/*  287 */     _writeInt(window);
/*  288 */     _writeInt(parent);
/*  289 */     _writeByte((byte)place);
/*      */   }
/*      */   
/*      */   public void mkColormapNotify(int window, int cmap, int neww, int state) {
/*  293 */     _clear();
/*  294 */     _writeByte((byte)32);
/*  295 */     _writePad(1);
/*  296 */     _writePad(2);
/*  297 */     _writeInt(window);
/*  298 */     _writeInt(cmap);
/*  299 */     _writeByte((byte)neww);
/*  300 */     _writeByte((byte)state);
/*      */   }
/*      */   
/*      */   public void mkFocusIn(int detail, int event, int mode) {
/*  304 */     _clear();
/*  305 */     _writeByte((byte)9);
/*  306 */     _writeByte((byte)detail);
/*  307 */     _writePad(2);
/*  308 */     _writeInt(event);
/*  309 */     _writeByte((byte)mode);
/*      */   }
/*      */   
/*      */   public void mkFocusOut(int detail, int event, int mode) {
/*  313 */     _clear();
/*  314 */     _writeByte((byte)10);
/*  315 */     _writeByte((byte)detail);
/*  316 */     _writePad(2);
/*  317 */     _writeInt(event);
/*  318 */     _writeByte((byte)mode);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public void mkPropertyNotify(int window, int atom, int time, int state)
/*      */   {
/*  325 */     _clear();
/*  326 */     _writeByte((byte)28);
/*  327 */     _writePad(1);
/*  328 */     _writePad(2);
/*  329 */     _writeInt(window);
/*  330 */     _writeInt(atom);
/*  331 */     _writeInt(time);
/*  332 */     _writeByte((byte)state);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public void mkSelectionNotify(int time, int requestor, int selection, int target, int property)
/*      */   {
/*  339 */     _clear();
/*  340 */     _writeByte((byte)31);
/*  341 */     _writePad(1);
/*  342 */     _writePad(2);
/*  343 */     _writeInt(time);
/*  344 */     _writeInt(requestor);
/*  345 */     _writeInt(selection);
/*  346 */     _writeInt(target);
/*  347 */     _writeInt(property);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public void mkSelectionRequest(int time, int owner, int requestor, int selection, int target, int property)
/*      */   {
/*  354 */     _clear();
/*  355 */     _writeByte((byte)30);
/*  356 */     _writePad(1);
/*  357 */     _writePad(2);
/*  358 */     _writeInt(time);
/*  359 */     _writeInt(owner);
/*  360 */     _writeInt(requestor);
/*  361 */     _writeInt(selection);
/*  362 */     _writeInt(target);
/*  363 */     _writeInt(property);
/*      */   }
/*      */   
/*      */   public void mkSelectionClear(int time, int owner, int selection) {
/*  367 */     _clear();
/*  368 */     _writeByte((byte)29);
/*  369 */     _writePad(1);
/*  370 */     _writePad(2);
/*  371 */     _writeInt(time);
/*  372 */     _writeInt(owner);
/*  373 */     _writeInt(selection);
/*      */   }
/*      */   
/*      */   public void mkVisibilityNotify(int window, int state) {
/*  377 */     _clear();
/*  378 */     _writeByte((byte)15);
/*  379 */     _writePad(1);
/*  380 */     _writePad(2);
/*  381 */     _writeInt(window);
/*  382 */     _writeByte((byte)state);
/*      */   }
/*      */   
/*      */   public void mkGravityNotify(int event, int window, int x, int y)
/*      */   {
/*  387 */     _clear();
/*  388 */     _writeByte((byte)24);
/*  389 */     _writePad(1);
/*  390 */     _writePad(2);
/*  391 */     _writeInt(event);
/*  392 */     _writeInt(window);
/*  393 */     _writeShort(x);
/*  394 */     _writeShort(y);
/*      */   }
/*      */   
/*      */   public void mkReparentNotify(int event, int window, int parent, int x, int y, int overr)
/*      */   {
/*  399 */     _clear();
/*  400 */     _writeByte((byte)21);
/*  401 */     _writePad(1);
/*  402 */     _writePad(2);
/*  403 */     _writeInt(event);
/*  404 */     _writeInt(window);
/*  405 */     _writeInt(parent);
/*  406 */     _writeShort(x);
/*  407 */     _writeShort(y);
/*  408 */     _writeByte((byte)overr);
/*  409 */     _writePad(11);
/*      */   }
/*      */   
/*  412 */   public void mkError(int code, int id, int minor, int major) { _clear();
/*  413 */     _writeByte((byte)0);
/*  414 */     _writeByte((byte)code);
/*  415 */     _writePad(2);
/*  416 */     _writeInt(id);
/*  417 */     _writeShort(minor);
/*  418 */     _writeByte((byte)major);
/*  419 */     _writePad(21);
/*      */   }
/*      */   
/*  422 */   public void mkDestroyNotify(int event, int window) { _clear();
/*  423 */     _writeByte((byte)17);
/*  424 */     _writePad(1);
/*  425 */     _writePad(2);
/*  426 */     _writeInt(event);
/*  427 */     _writeInt(window);
/*  428 */     _writePad(20);
/*      */   }
/*      */   
/*      */   public void mkEnterNotify(int detail, int root, int event, int child, int rootx, int rooty, int ex, int ey, int state, int mode, int sames_focus)
/*      */   {
/*  433 */     _clear();
/*  434 */     _writeByte((byte)7);
/*  435 */     _writeByte((byte)detail);
/*  436 */     _writePad(2);
/*  437 */     _writeInt((int)System.currentTimeMillis());
/*  438 */     _writeInt(root);
/*  439 */     _writeInt(event);
/*  440 */     _writeInt(child);
/*  441 */     _writeShort(rootx);_writeShort(rooty);
/*  442 */     _writeShort(ex);_writeShort(ey);
/*      */     
/*  444 */     _writeShort(0);
/*  445 */     _writeByte((byte)mode);
/*  446 */     _writeByte((byte)sames_focus);
/*      */   }
/*      */   
/*      */   public void mkLeaveNotify(int detail, int root, int event, int child, int rootx, int rooty, int ex, int ey, int state, int mode, int sames_focus)
/*      */   {
/*  451 */     _clear();
/*  452 */     _writeByte((byte)8);
/*  453 */     _writeByte((byte)detail);
/*  454 */     _writePad(2);
/*  455 */     _writeInt((int)System.currentTimeMillis());
/*  456 */     _writeInt(root);
/*  457 */     _writeInt(event);
/*  458 */     _writeInt(child);
/*  459 */     _writeShort(rootx);_writeShort(rooty);
/*  460 */     _writeShort(ex);_writeShort(ey);
/*  461 */     _writeShort(state);
/*  462 */     _writeByte((byte)mode);
/*  463 */     _writeByte((byte)sames_focus);
/*      */   }
/*      */   
/*  466 */   public void mkClientMessage(int window, int typ) { _clear();
/*  467 */     _writeByte((byte)33);
/*  468 */     _writePad(1);
/*  469 */     _writePad(2);
/*  470 */     _writeInt(window);
/*  471 */     _writeInt(typ);
/*      */   }
/*      */   
/*      */ 
/*      */   public void mkKeyPress(int detail, int root, int ev, int child, int rootx, int rooty, int ex, int ey, int state, int sames)
/*      */   {
/*  477 */     this.index = 0;
/*      */     
/*  479 */     this.event[(this.index++)] = 2;
/*  480 */     this.event[(this.index++)] = ((byte)detail);
/*      */     
/*  482 */     this.index += 2;
/*      */     
/*  484 */     int i = (int)System.currentTimeMillis();
/*  485 */     this.event[(this.index++)] = ((byte)(i >> 24 & 0xFF));
/*  486 */     this.event[(this.index++)] = ((byte)(i >> 16 & 0xFF));
/*  487 */     this.event[(this.index++)] = ((byte)(i >> 8 & 0xFF));
/*  488 */     this.event[(this.index++)] = ((byte)(i & 0xFF));
/*      */     
/*  490 */     i = root;
/*  491 */     this.event[(this.index++)] = ((byte)(i >> 24 & 0xFF));
/*  492 */     this.event[(this.index++)] = ((byte)(i >> 16 & 0xFF));
/*  493 */     this.event[(this.index++)] = ((byte)(i >> 8 & 0xFF));
/*  494 */     this.event[(this.index++)] = ((byte)(i & 0xFF));
/*      */     
/*  496 */     i = ev;
/*  497 */     this.event[(this.index++)] = ((byte)(i >> 24 & 0xFF));
/*  498 */     this.event[(this.index++)] = ((byte)(i >> 16 & 0xFF));
/*  499 */     this.event[(this.index++)] = ((byte)(i >> 8 & 0xFF));
/*  500 */     this.event[(this.index++)] = ((byte)(i & 0xFF));
/*      */     
/*  502 */     i = child;
/*  503 */     this.event[(this.index++)] = ((byte)(i >> 24 & 0xFF));
/*  504 */     this.event[(this.index++)] = ((byte)(i >> 16 & 0xFF));
/*  505 */     this.event[(this.index++)] = ((byte)(i >> 8 & 0xFF));
/*  506 */     this.event[(this.index++)] = ((byte)(i & 0xFF));
/*      */     
/*  508 */     i = rootx;
/*  509 */     this.event[(this.index++)] = ((byte)(i >> 8 & 0xFF));
/*  510 */     this.event[(this.index++)] = ((byte)(i & 0xFF));
/*      */     
/*  512 */     i = rooty;
/*  513 */     this.event[(this.index++)] = ((byte)(i >> 8 & 0xFF));
/*  514 */     this.event[(this.index++)] = ((byte)(i & 0xFF));
/*      */     
/*  516 */     i = ex;
/*  517 */     this.event[(this.index++)] = ((byte)(i >> 8 & 0xFF));
/*  518 */     this.event[(this.index++)] = ((byte)(i & 0xFF));
/*      */     
/*  520 */     i = ey;
/*  521 */     this.event[(this.index++)] = ((byte)(i >> 8 & 0xFF));
/*  522 */     this.event[(this.index++)] = ((byte)(i & 0xFF));
/*      */     
/*  524 */     i = state;
/*  525 */     this.event[(this.index++)] = ((byte)(i >> 8 & 0xFF));
/*  526 */     this.event[(this.index++)] = ((byte)(i & 0xFF));
/*      */     
/*  528 */     this.event[(this.index++)] = ((byte)sames);
/*      */   }
/*      */   
/*      */ 
/*      */   public void mkKeyRelease(int detail, int root, int ev, int child, int rootx, int rooty, int ex, int ey, int state, int sames)
/*      */   {
/*  534 */     this.index = 0;
/*      */     
/*  536 */     this.event[(this.index++)] = 3;
/*  537 */     this.event[(this.index++)] = ((byte)detail);
/*      */     
/*  539 */     this.index += 2;
/*      */     
/*  541 */     int i = (int)System.currentTimeMillis();
/*  542 */     this.event[(this.index++)] = ((byte)(i >> 24 & 0xFF));
/*  543 */     this.event[(this.index++)] = ((byte)(i >> 16 & 0xFF));
/*  544 */     this.event[(this.index++)] = ((byte)(i >> 8 & 0xFF));
/*  545 */     this.event[(this.index++)] = ((byte)(i & 0xFF));
/*      */     
/*  547 */     i = root;
/*  548 */     this.event[(this.index++)] = ((byte)(i >> 24 & 0xFF));
/*  549 */     this.event[(this.index++)] = ((byte)(i >> 16 & 0xFF));
/*  550 */     this.event[(this.index++)] = ((byte)(i >> 8 & 0xFF));
/*  551 */     this.event[(this.index++)] = ((byte)(i & 0xFF));
/*      */     
/*  553 */     i = ev;
/*  554 */     this.event[(this.index++)] = ((byte)(i >> 24 & 0xFF));
/*  555 */     this.event[(this.index++)] = ((byte)(i >> 16 & 0xFF));
/*  556 */     this.event[(this.index++)] = ((byte)(i >> 8 & 0xFF));
/*  557 */     this.event[(this.index++)] = ((byte)(i & 0xFF));
/*      */     
/*  559 */     i = child;
/*  560 */     this.event[(this.index++)] = ((byte)(i >> 24 & 0xFF));
/*  561 */     this.event[(this.index++)] = ((byte)(i >> 16 & 0xFF));
/*  562 */     this.event[(this.index++)] = ((byte)(i >> 8 & 0xFF));
/*  563 */     this.event[(this.index++)] = ((byte)(i & 0xFF));
/*      */     
/*  565 */     i = rootx;
/*  566 */     this.event[(this.index++)] = ((byte)(i >> 8 & 0xFF));
/*  567 */     this.event[(this.index++)] = ((byte)(i & 0xFF));
/*      */     
/*  569 */     i = rooty;
/*  570 */     this.event[(this.index++)] = ((byte)(i >> 8 & 0xFF));
/*  571 */     this.event[(this.index++)] = ((byte)(i & 0xFF));
/*      */     
/*  573 */     i = ex;
/*  574 */     this.event[(this.index++)] = ((byte)(i >> 8 & 0xFF));
/*  575 */     this.event[(this.index++)] = ((byte)(i & 0xFF));
/*      */     
/*  577 */     i = ey;
/*  578 */     this.event[(this.index++)] = ((byte)(i >> 8 & 0xFF));
/*  579 */     this.event[(this.index++)] = ((byte)(i & 0xFF));
/*      */     
/*  581 */     i = state;
/*  582 */     this.event[(this.index++)] = ((byte)(i >> 8 & 0xFF));
/*  583 */     this.event[(this.index++)] = ((byte)(i & 0xFF));
/*      */     
/*  585 */     this.event[(this.index++)] = ((byte)sames);
/*      */   }
/*      */   
/*  588 */   public void mkNoExposure(int drawable, int minor, int major) { _clear();
/*  589 */     _writeByte((byte)14);
/*  590 */     _writePad(1);
/*  591 */     _writePad(2);
/*  592 */     _writeInt(drawable);
/*  593 */     _writeShort(minor);
/*  594 */     _writeByte((byte)major);
/*      */   }
/*      */   
/*      */ 
/*      */   public void mkButtonPress(int detail, int root, int event, int child, int rootx, int rooty, int ex, int ey, int state, int sames)
/*      */   {
/*  600 */     _clear();
/*  601 */     _writeByte((byte)4);
/*  602 */     _writeByte((byte)detail);
/*  603 */     _writePad(2);
/*  604 */     _writeInt((int)System.currentTimeMillis());
/*  605 */     _writeInt(root);
/*  606 */     _writeInt(event);
/*  607 */     _writeInt(child);
/*  608 */     _writeShort(rootx);_writeShort(rooty);
/*  609 */     _writeShort(ex);_writeShort(ey);
/*  610 */     _writeShort(state);
/*  611 */     _writeByte((byte)sames);
/*      */   }
/*      */   
/*      */   public void mkResizeRequest(int window, int w, int h) {
/*  615 */     _clear();
/*  616 */     _writeByte((byte)25);
/*  617 */     _writePad(1);
/*  618 */     _writePad(2);
/*  619 */     _writeInt(window);
/*  620 */     _writeShort(w);_writeShort(h);
/*      */   }
/*      */   
/*      */ 
/*      */   public void mkCreateNotify(int parent, int window, int x, int y, int w, int h, int bw, int redirect)
/*      */   {
/*  626 */     _clear();
/*  627 */     _writeByte((byte)16);
/*  628 */     _writePad(1);
/*  629 */     _writePad(2);
/*  630 */     _writeInt(parent);
/*  631 */     _writeInt(window);
/*  632 */     _writeShort(x);_writeShort(y);
/*  633 */     _writeShort(w);_writeShort(h);_writeShort(bw);
/*  634 */     _writeByte((byte)redirect);
/*      */   }
/*      */   
/*      */ 
/*      */   public void mkConfigureNotify(int window, int sibling, int x, int y, int w, int h, int bw, int redirect)
/*      */   {
/*  640 */     _clear();
/*  641 */     _writeByte((byte)22);
/*  642 */     _writePad(1);
/*  643 */     _writePad(2);
/*  644 */     _writePad(4);
/*  645 */     _writeInt(window);
/*  646 */     _writeInt(sibling);
/*  647 */     _writeShort(x);_writeShort(y);
/*  648 */     _writeShort(w);_writeShort(h);_writeShort(bw);
/*  649 */     _writeByte((byte)redirect);
/*      */   }
/*      */   
/*      */ 
/*      */   public void mkConfigureRequest(int smode, int parent, int window, int sibling, int x, int y, int w, int h, int bw, int mask)
/*      */   {
/*  655 */     _clear();
/*  656 */     _writeByte((byte)23);
/*  657 */     _writeByte((byte)smode);
/*  658 */     _writePad(2);
/*  659 */     _writeInt(parent);
/*  660 */     _writeInt(window);
/*  661 */     _writeInt(sibling);
/*  662 */     _writeShort(x);_writeShort(y);
/*  663 */     _writeShort(w);_writeShort(h);_writeShort(bw);
/*  664 */     _writeShort(mask);
/*      */   }
/*      */   
/*      */ 
/*      */   public void mkButtonRelease(int detail, int root, int event, int child, int rootx, int rooty, int ex, int ey, int state, int sames)
/*      */   {
/*  670 */     _clear();
/*  671 */     _writeByte((byte)5);
/*  672 */     _writeByte((byte)detail);
/*  673 */     _writePad(2);
/*  674 */     _writeInt((int)System.currentTimeMillis());
/*  675 */     _writeInt(root);
/*  676 */     _writeInt(event);
/*  677 */     _writeInt(child);
/*  678 */     _writeShort(rootx);_writeShort(rooty);
/*  679 */     _writeShort(ex);_writeShort(ey);
/*  680 */     _writeShort(state);
/*  681 */     _writeByte((byte)sames);
/*      */   }
/*      */   
/*      */ 
/*      */   public void mkMotionNotify(int detail, int root, int event, int child, int rootx, int rooty, int ex, int ey, int state, int sames)
/*      */   {
/*  687 */     _clear();
/*  688 */     _writeByte((byte)6);
/*  689 */     _writeByte((byte)detail);
/*  690 */     _writePad(2);
/*  691 */     _writeInt((int)System.currentTimeMillis());
/*  692 */     _writeInt(root);
/*  693 */     _writeInt(event);
/*  694 */     _writeInt(child);
/*  695 */     _writeShort(rootx);_writeShort(rooty);
/*  696 */     _writeShort(ex);_writeShort(ey);
/*  697 */     _writeShort(state);
/*  698 */     _writeByte((byte)sames);
/*      */   }
/*      */   
/*      */   public void mkPointer(int type, int detail, int time, int child, int rootx, int rooty, int state, int mode, int flags)
/*      */   {
/*  703 */     this.index = 0;
/*  704 */     _writeByte((byte)type);
/*  705 */     _writeByte((byte)detail);
/*  706 */     _writePad(2);
/*  707 */     _writeInt(time);
/*  708 */     _writePad(4);
/*  709 */     _writePad(4);
/*  710 */     _writeInt(child);
/*  711 */     _writeShort(rootx);
/*  712 */     _writeShort(rooty);
/*  713 */     _writePad(4);
/*  714 */     _writeShort(state);
/*  715 */     _writeByte((byte)mode);
/*  716 */     _writeByte((byte)flags);
/*      */   }
/*      */   
/*      */   public void setPointer(int type, int detail, int time, int child, int rootx, int rooty, int state, int mode, int flags)
/*      */   {
/*  721 */     this.index = 0;
/*  722 */     _writeByte((byte)type);
/*  723 */     _writeByte((byte)detail);
/*  724 */     _writePad(2);
/*  725 */     _writeInt(time);
/*  726 */     _writePad(8);
/*  727 */     _writeInt(child);
/*  728 */     _writeShort(rootx);
/*  729 */     _writeShort(rooty);
/*  730 */     _writePad(4);
/*  731 */     _writeShort(state);
/*  732 */     _writeByte((byte)mode);
/*  733 */     _writeByte((byte)flags);
/*      */   }
/*      */   
/*  736 */   public void setType(byte bb) { this.event[0] = bb; }
/*  737 */   public byte getType() { return this.event[0]; }
/*      */   
/*      */   public void mkExpose(int id, int x, int y, int w, int h, int count) {
/*  740 */     _clear();
/*  741 */     _writeByte((byte)12);
/*  742 */     _writePad(1);_writePad(2);
/*  743 */     _writeInt(id);
/*  744 */     _writeShort(x);_writeShort(y);_writeShort(w);_writeShort(h);
/*  745 */     _writeShort(count);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public void mkGraphicsExposure(int id, int x, int y, int w, int h, int minorEvent, int count, int majorEvent)
/*      */   {
/*  752 */     _clear();
/*  753 */     _writeByte((byte)13);
/*  754 */     _writePad(1);_writePad(2);
/*  755 */     _writeInt(id);
/*  756 */     _writeShort(x);_writeShort(y);_writeShort(w);_writeShort(h);
/*  757 */     _writeShort(minorEvent);
/*  758 */     _writeShort(count);
/*  759 */     _writeByte((byte)majorEvent);
/*      */   }
/*      */   
/*      */ 
/*      */   public void mkMapNotify(int window, int override)
/*      */   {
/*  765 */     _clear();
/*  766 */     _writeByte((byte)19);
/*  767 */     _writePad(1);_writePad(2);
/*  768 */     _writeInt(window);
/*  769 */     _writeInt(window);
/*  770 */     _writeByte((byte)override);
/*      */   }
/*      */   
/*      */   public void mkUnmapNotify(int window, int from) {
/*  774 */     _clear();
/*  775 */     _writeByte((byte)18);
/*  776 */     _writePad(1);_writePad(2);
/*  777 */     _writePad(4);
/*  778 */     _writeInt(window);
/*  779 */     _writeByte((byte)from);
/*      */   }
/*      */   
/*      */   public void mkMapRequest(int parent, int window) {
/*  783 */     _clear();
/*  784 */     _writeByte((byte)20);
/*  785 */     _writePad(1);_writePad(2);
/*  786 */     _writeInt(parent);
/*  787 */     _writeInt(window);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public void swapShort()
/*      */   {
/*  794 */     byte i = this.event[this.index];
/*  795 */     this.sevent[this.index] = this.event[(this.index + 1)];
/*  796 */     this.sevent[(this.index + 1)] = i;
/*  797 */     this.index += 2;
/*      */   }
/*      */   
/*      */   public void swapInt() {
/*  801 */     byte i = this.event[this.index];
/*  802 */     this.sevent[this.index] = this.event[(this.index + 3)];
/*  803 */     this.sevent[(this.index + 3)] = i;
/*  804 */     i = this.event[(this.index + 1)];
/*  805 */     this.sevent[(this.index + 1)] = this.event[(this.index + 2)];
/*  806 */     this.sevent[(this.index + 2)] = i;
/*  807 */     this.index += 4;
/*      */   }
/*      */   
/*      */   private void _swapShort() {
/*  811 */     byte i = this.event[this.index];
/*  812 */     this.sevent[this.index] = this.event[(this.index + 1)];
/*  813 */     this.sevent[(this.index + 1)] = i;
/*  814 */     this.index += 2;
/*      */   }
/*      */   
/*      */   private void _swapInt() {
/*  818 */     byte i = this.event[this.index];
/*  819 */     this.sevent[this.index] = this.event[(this.index + 3)];
/*  820 */     this.sevent[(this.index + 3)] = i;
/*  821 */     i = this.event[(this.index + 1)];
/*  822 */     this.sevent[(this.index + 1)] = this.event[(this.index + 2)];
/*  823 */     this.sevent[(this.index + 2)] = i;
/*  824 */     this.index += 4;
/*      */   }
/*      */   
/*      */   public void swap(byte[] b) {
/*  828 */     System.arraycopy(this.event, 0, b, 0, 32);
/*  829 */     this.sevent = b;
/*  830 */     swapaux();
/*      */   }
/*      */   
/*      */   public void swap() {
/*  834 */     this.sevent = this.event;
/*  835 */     swapaux();
/*      */   }
/*      */   
/*      */   private void swapaux() {
/*  839 */     this.index = 2;
/*      */     
/*  841 */     byte i = this.event[this.index];this.sevent[this.index] = this.event[(this.index + 1)];this.sevent[(this.index + 1)] = i;
/*  842 */     this.index += 2;
/*      */     
/*  844 */     i = (byte)(this.event[0] & 0xFF7F & 0xFF);
/*      */     
/*  846 */     switch (i) {
/*      */     case 0: 
/*  848 */       _swapInt();
/*  849 */       _swapShort();
/*  850 */       break;
/*      */     
/*      */     case 2: 
/*      */     case 3: 
/*      */     case 4: 
/*      */     case 5: 
/*      */     case 6: 
/*      */     case 7: 
/*      */     case 8: 
/*  859 */       i = this.event[this.index];this.sevent[this.index] = this.event[(this.index + 3)];this.sevent[(this.index + 3)] = i;
/*  860 */       i = this.event[(this.index + 1)];this.sevent[(this.index + 1)] = this.event[(this.index + 2)];this.sevent[(this.index + 2)] = i;
/*  861 */       this.index += 4;
/*      */       
/*  863 */       i = this.event[this.index];this.sevent[this.index] = this.event[(this.index + 3)];this.sevent[(this.index + 3)] = i;
/*  864 */       i = this.event[(this.index + 1)];this.sevent[(this.index + 1)] = this.event[(this.index + 2)];this.sevent[(this.index + 2)] = i;
/*  865 */       this.index += 4;
/*      */       
/*  867 */       i = this.event[this.index];this.sevent[this.index] = this.event[(this.index + 3)];this.sevent[(this.index + 3)] = i;
/*  868 */       i = this.event[(this.index + 1)];this.sevent[(this.index + 1)] = this.event[(this.index + 2)];this.sevent[(this.index + 2)] = i;
/*  869 */       this.index += 4;
/*      */       
/*  871 */       i = this.event[this.index];this.sevent[this.index] = this.event[(this.index + 3)];this.sevent[(this.index + 3)] = i;
/*  872 */       i = this.event[(this.index + 1)];this.sevent[(this.index + 1)] = this.event[(this.index + 2)];this.sevent[(this.index + 2)] = i;
/*  873 */       this.index += 4;
/*      */       
/*  875 */       i = this.event[this.index];this.sevent[this.index] = this.event[(this.index + 1)];this.sevent[(this.index + 1)] = i;
/*  876 */       this.index += 2;
/*      */       
/*  878 */       i = this.event[this.index];this.sevent[this.index] = this.event[(this.index + 1)];this.sevent[(this.index + 1)] = i;
/*  879 */       this.index += 2;
/*      */       
/*  881 */       i = this.event[this.index];this.sevent[this.index] = this.event[(this.index + 1)];this.sevent[(this.index + 1)] = i;
/*  882 */       this.index += 2;
/*      */       
/*  884 */       i = this.event[this.index];this.sevent[this.index] = this.event[(this.index + 1)];this.sevent[(this.index + 1)] = i;
/*  885 */       this.index += 2;
/*      */       
/*  887 */       i = this.event[this.index];this.sevent[this.index] = this.event[(this.index + 1)];this.sevent[(this.index + 1)] = i;
/*  888 */       break;
/*      */     case 9: 
/*      */     case 10: 
/*  891 */       _swapInt();
/*  892 */       break;
/*      */     case 12: 
/*  894 */       _swapInt();
/*  895 */       _swapShort();
/*  896 */       _swapShort();
/*  897 */       _swapShort();
/*  898 */       _swapShort();
/*  899 */       _swapShort();
/*  900 */       break;
/*      */     case 13: 
/*  902 */       _swapInt();
/*  903 */       _swapShort();
/*  904 */       _swapShort();
/*  905 */       _swapShort();
/*  906 */       _swapShort();
/*  907 */       _swapShort();
/*  908 */       _swapShort();
/*  909 */       break;
/*      */     case 14: 
/*  911 */       _swapInt();
/*  912 */       _swapShort();
/*  913 */       break;
/*      */     case 15: 
/*  915 */       _swapInt();
/*  916 */       break;
/*      */     case 16: 
/*  918 */       _swapInt();
/*  919 */       _swapInt();
/*  920 */       _swapShort();
/*  921 */       _swapShort();
/*  922 */       _swapShort();
/*  923 */       _swapShort();
/*  924 */       _swapShort();
/*  925 */       break;
/*      */     case 17: 
/*      */     case 18: 
/*      */     case 19: 
/*      */     case 20: 
/*  930 */       _swapInt();
/*  931 */       _swapInt();
/*  932 */       break;
/*      */     case 21: 
/*  934 */       _swapInt();
/*  935 */       _swapInt();
/*  936 */       _swapInt();
/*  937 */       _swapShort();
/*  938 */       _swapShort();
/*  939 */       break;
/*      */     case 22: 
/*  941 */       _swapInt();
/*  942 */       _swapInt();
/*  943 */       _swapInt();
/*  944 */       _swapShort();
/*  945 */       _swapShort();
/*  946 */       _swapShort();
/*  947 */       _swapShort();
/*  948 */       _swapShort();
/*  949 */       break;
/*      */     case 23: 
/*  951 */       _swapInt();
/*  952 */       _swapInt();
/*  953 */       _swapInt();
/*  954 */       _swapShort();
/*  955 */       _swapShort();
/*  956 */       _swapShort();
/*  957 */       _swapShort();
/*  958 */       _swapShort();
/*  959 */       _swapShort();
/*  960 */       break;
/*      */     case 24: 
/*  962 */       _swapInt();
/*  963 */       _swapInt();
/*  964 */       _swapShort();
/*  965 */       _swapShort();
/*  966 */       break;
/*      */     case 25: 
/*  968 */       _swapInt();
/*  969 */       _swapShort();
/*  970 */       _swapShort();
/*  971 */       break;
/*      */     case 26: 
/*      */     case 27: 
/*      */     case 28: 
/*      */     case 29: 
/*  976 */       _swapInt();
/*  977 */       _swapInt();
/*  978 */       _swapInt();
/*  979 */       break;
/*      */     case 30: 
/*  981 */       _swapInt();
/*  982 */       _swapInt();
/*  983 */       _swapInt();
/*  984 */       _swapInt();
/*  985 */       _swapInt();
/*  986 */       _swapInt();
/*  987 */       break;
/*      */     case 31: 
/*  989 */       _swapInt();
/*  990 */       _swapInt();
/*  991 */       _swapInt();
/*  992 */       _swapInt();
/*  993 */       _swapInt();
/*  994 */       break;
/*      */     case 32: 
/*  996 */       _swapInt();
/*  997 */       _swapInt();
/*  998 */       break;
/*      */     case 33: 
/* 1000 */       _swapInt();
/* 1001 */       _swapInt();
/* 1002 */       switch (this.event[1]) {
/*      */       case 32: 
/* 1004 */         for (int ii = 0; ii < 5; ii++) {
/* 1005 */           _swapInt();
/*      */         }
/*      */       }
/*      */       
/* 1009 */       break;
/*      */     case 1: case 11: default: 
/* 1011 */       if (64 < i) {
/* 1012 */         Extension.swap(i, this);
/*      */       }
/*      */       break;
/*      */     }
/*      */   }
/*      */ }


/* Location:              C:\Users\Joey\Downloads\launch.jar!\com\emt\proteus\xserver\display\Event.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */