/*     */ package com.emt.proteus.xserver.display;
/*     */ 
/*     */ import com.emt.proteus.xserver.api.Configurable;
/*     */ import com.emt.proteus.xserver.api.Context;
/*     */ import com.emt.proteus.xserver.client.XClient;
/*     */ import com.emt.proteus.xserver.display.input.Keyboard;
/*     */ import com.emt.proteus.xserver.display.input.Keymap;
/*     */ import com.emt.proteus.xserver.display.input.Keymap_101;
/*     */ import com.emt.proteus.xserver.display.input.Keymodifier;
/*     */ import com.emt.proteus.xserver.display.input.Keymodifier_gen;
/*     */ import com.emt.proteus.xserver.protocol.XConstants;
/*     */ import java.awt.Dimension;
/*     */ import java.util.Vector;
/*     */ import javax.swing.JComponent;
/*     */ 
/*     */ public class XDisplay
/*     */   implements Configurable
/*     */ {
/*     */   private static XDisplay SINGLETON;
/*  20 */   public static boolean threeButton = false;
/*     */   public static final int InBrowser = 0;
/*     */   public static final int Rootless = 1;
/*     */   public static final int RootlessWM = 2;
/*  24 */   public static String alphaBackground = null;
/*  25 */   public static Format[] format = null;
/*  26 */   public static int imageByteOrder = 1;
/*  27 */   public static int bitmapBitOrder = 1;
/*     */   static final int bitmapScanUnit = 32;
/*     */   static final int bitmapScanPad = 32;
/*  30 */   public static short width = 768;
/*  31 */   public static short height = 576;
/*  32 */   public static XClient serverXClient = null;
/*     */   public static Screen[] screen;
/*  34 */   public static String visuals = "TrueColor16";
/*     */   
/*  36 */   public static String extension = "DummySHAPE";
/*  37 */   public static String charset = null;
/*     */   
/*     */   public static final String PREFIX = "com.emt.proteus.xserver.display.";
/*     */   private JComponent container;
/*     */   private JComponent eventRoot;
/*     */   
/*     */   public XDisplay()
/*     */   {
/*  45 */     if (SINGLETON == null) SINGLETON = this;
/*     */   }
/*     */   
/*     */   public void initialize(Context context)
/*     */   {
/*  50 */     this.container = ((JComponent)context.get("CONTAINER"));
/*  51 */     this.eventRoot = ((JComponent)context.get("EVENT_ROOT"));
/*  52 */     if (this.eventRoot == null) {
/*  53 */       this.eventRoot = this.container;
/*     */     }
/*  55 */     Keymap km = new Keymap_101();
/*  56 */     Keymap.km = km;
/*  57 */     Keymodifier modifier = new Keymodifier_gen();
/*  58 */     Keymodifier.kmod = modifier;
/*  59 */     Keyboard.keyboard = new Keyboard(km.getStart(), km.getLast());
/*  60 */     serverXClient = new XClient();
/*  61 */     serverXClient.index = 0;
/*  62 */     XClient.X_CLIENTs[0] = serverXClient;
/*  63 */     Resource.initClientResource(serverXClient);
/*  64 */     Dimension dm = this.container.getMaximumSize();
/*  65 */     width = context.shortValue("display.width", dm.width);
/*  66 */     height = context.shortValue("display.height", dm.height);
/*     */     
/*  68 */     screen = Screen.init();
/*     */     
/*  70 */     Colormap.init();
/*  71 */     Extension.init(extension);
/*     */     
/*  73 */     Depth[] depth = null;
/*     */     
/*  75 */     Visual[] visual = null;
/*  76 */     Visual defaultv = null;
/*     */     
/*     */ 
/*  79 */     Vector depthv = new Vector();
/*     */     
/*  81 */     if (visuals.indexOf("TrueColor16") != -1) {
/*  82 */       visual = Visual.getTrueColor16(serverXClient);
/*  83 */       defaultv = visual[0];
/*  84 */       depthv.addElement(new Depth(16, visual));
/*     */       
/*  86 */       imageByteOrder = 0;
/*  87 */       bitmapBitOrder = 0;
/*     */     }
/*     */     
/*  90 */     if ((defaultv == null) && (visuals.indexOf("PseudoColor8") != -1)) {
/*  91 */       visual = Visual.getPseudoColor8(serverXClient);
/*  92 */       defaultv = visual[0];
/*  93 */       depthv.addElement(new Depth(8, visual));
/*     */     }
/*     */     
/*  96 */     if ((defaultv == null) && (visuals.indexOf("StaticGray8") != -1)) {
/*  97 */       visual = Visual.getStaticGray8(serverXClient);
/*  98 */       defaultv = visual[0];
/*  99 */       depthv.addElement(new Depth(8, visual));
/*     */     }
/*     */     
/* 102 */     if (defaultv == null) {
/* 103 */       visual = Visual.getStaticGray1(serverXClient);
/* 104 */       defaultv = visual[0];
/* 105 */       depthv.addElement(new Depth(1, visual));
/*     */     }
/*     */     
/* 108 */     depth = new Depth[depthv.size()];
/* 109 */     for (int i = 0; i < depthv.size(); i++) {
/* 110 */       depth[i] = ((Depth)depthv.elementAt(i));
/*     */     }
/* 112 */     depthv.removeAllElements();
/*     */     
/*     */ 
/* 115 */     int rootid = Resource.fakeClientId(serverXClient);
/* 116 */     int colormapid = Resource.fakeClientId(serverXClient);
/*     */     
/* 118 */     XFont.init(Resource.fakeClientId(serverXClient), charset);
/*     */     
/* 120 */     Cursor.rootCursor = new Cursor(Resource.fakeClientId(serverXClient));
/* 121 */     Resource.add(Cursor.rootCursor);
/*     */     
/* 123 */     int fgPixel = 1;int bgPixel = 0;
/* 124 */     if (visuals.indexOf("TrueColor16") != -1) {
/* 125 */       fgPixel = 65535;
/* 126 */       bgPixel = 0;
/*     */     }
/*     */     
/* 129 */     screen[0] = new Screen(rootid, colormapid, fgPixel, bgPixel, 0, width, height, width / 3, height / 3, 1, 1, defaultv.id, 0, 0, defaultv.depth.depth, depth);
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
/* 146 */     String mode = context.stringValue("mode", "InBrowser");
/* 147 */     if ((mode.equals("MultiWindow")) || (mode.equals("Rootless")))
/*     */     {
/* 149 */       screen[0].windowmode = 1;
/* 150 */     } else if ((mode.equals("MultiWindowWM")) || (mode.equals("RootlessWM")))
/*     */     {
/* 152 */       screen[0].windowmode = 2;
/*     */     } else {
/* 154 */       screen[0].windowmode = 0;
/*     */     }
/* 156 */     screen[0].windowmode = 0;
/*     */     
/* 158 */     int vcount = 0;
/* 159 */     for (int i = 0; i < depth.length; i++) {
/* 160 */       vcount += depth[i].getVisual().length;
/*     */     }
/* 162 */     visual = new Visual[vcount];
/* 163 */     vcount = 0;
/* 164 */     for (int i = 0; i < depth.length; i++) {
/* 165 */       Visual[] tmp = depth[i].getVisual();
/* 166 */       for (int j = 0; j < tmp.length; j++) {
/* 167 */         visual[vcount] = tmp[j];
/* 168 */         vcount++;
/*     */       }
/*     */     }
/*     */     
/*     */ 
/* 173 */     screen[0].visual = visual;
/*     */     
/* 175 */     if (defaultv.depth.depth == 8) {
/* 176 */       format = new Format[2];
/* 177 */       format[0] = new Format(1, 1, 32);
/* 178 */       format[1] = new Format(8, 8, 32);
/* 179 */     } else if (defaultv.depth.depth == 16) {
/* 180 */       format = new Format[2];
/* 181 */       format[0] = new Format(1, 1, 32);
/* 182 */       format[1] = new Format(16, 16, 32);
/*     */     } else {
/* 184 */       format = new Format[1];
/* 185 */       format[0] = new Format(1, 1, 32);
/*     */     }
/*     */     
/* 188 */     Format.format = format;
/*     */     
/* 190 */     int len = 0;
/* 191 */     for (int i = 0; i < screen.length; i++) {
/* 192 */       len += screen[i].getLength();
/*     */     }
/* 194 */     if (format != null) len += 2 * format.length;
/* 195 */     len += (XConstants.VENDOR.length + 3) / 4;
/* 196 */     len += 8;
/* 197 */     XClient.initialLength = len;
/*     */     
/* 199 */     screen[0].defaultColormap = Colormap.getColormap(colormapid, screen[0], defaultv, 0, serverXClient);
/*     */     
/*     */ 
/* 202 */     Colormap cmap = screen[0].defaultColormap;
/* 203 */     Colormap.installed[0] = cmap;
/*     */     
/* 205 */     if (defaultv.depth.depth != 16) {
/* 206 */       cmap.flags |= 0x4;
/*     */       try {
/* 208 */         cmap.allocColor(serverXClient, 0, 0, 0);
/* 209 */         cmap.allocColor(serverXClient, 255, 255, 255);
/* 210 */         if ((defaultv.clss == 0) && (defaultv.depth.depth == 8))
/*     */         {
/* 212 */           for (int i = 1; i < 255; i++) {
/* 213 */             cmap.allocColor(serverXClient, i, i, i);
/*     */           }
/*     */         }
/*     */       }
/*     */       catch (Exception e) {}
/* 218 */       cmap.mkIcm();
/* 219 */       cmap.flags &= 0xFFFFFFFB;
/*     */     }
/*     */     
/* 222 */     RootXWindow w = null;
/*     */     
/*     */ 
/* 225 */     w = new RootXWindow(this, this.container, screen[0], format, serverXClient);
/*     */     try {
/* 227 */       w.mapWindow(serverXClient);
/*     */     }
/*     */     catch (Exception e) {}
/*     */     
/* 231 */     Pixmap.init(screen);
/*     */     
/* 233 */     if (context.booleanValue("copy.paste")) {
/* 234 */       CopyPaste.init();
/*     */     }
/*     */   }
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
/*     */   public static void resetScreen(int scrn)
/*     */   {
/* 251 */     XClient.closeDownAll();
/* 252 */     Colormap cmap = screen[0].defaultColormap;
/*     */     
/* 254 */     if ((cmap.visual.clss & 0x1) != 0) {
/* 255 */       cmap.flags |= 0x4;
/* 256 */       cmap.freeAll();
/*     */       try {
/* 258 */         cmap.allocColor(serverXClient, 0, 0, 0);
/* 259 */         cmap.allocColor(serverXClient, 255, 255, 255);
/*     */       }
/*     */       catch (Exception e) {}
/* 262 */       cmap.mkIcm();
/* 263 */       cmap.flags &= 0xFFFFFFFB;
/*     */     }
/* 265 */     screen[0].root.initAttr();
/*     */   }
/*     */   
/*     */   public JComponent getEventRoot() {
/* 269 */     return this.eventRoot;
/*     */   }
/*     */   
/*     */   public JComponent getContainer() {
/* 273 */     return this.container;
/*     */   }
/*     */   
/*     */   public static XDisplay safe(XDisplay display)
/*     */   {
/* 278 */     if (display == null)
/* 279 */       return SINGLETON;
/* 280 */     return display;
/*     */   }
/*     */ }


/* Location:              C:\Users\Joey\Downloads\launch.jar!\com\emt\proteus\xserver\display\XDisplay.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */