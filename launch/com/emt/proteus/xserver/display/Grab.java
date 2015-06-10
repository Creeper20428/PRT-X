/*     */ package com.emt.proteus.xserver.display;
/*     */ 
/*     */ import com.emt.proteus.xserver.client.XClient;
/*     */ import java.io.IOException;
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
/*     */ public final class Grab
/*     */   extends Resource
/*     */ {
/*     */   static final int ownerEvents = 1;
/*     */   static final int keyboardMode = 2;
/*     */   static final int pointerMode = 4;
/*     */   static final int coreGrab = 8;
/*     */   static final int coreMods = 16;
/*     */   private static final int NotifyGrab = 1;
/*     */   
/*     */   public Grab(int id)
/*     */   {
/*  36 */     super(id, 536870921);
/*  37 */     this.modifiersDetail = new Detail();
/*  38 */     this.detail = new Detail();
/*  39 */     enable();
/*     */   }
/*     */   
/*  42 */   private boolean enable = false;
/*     */   
/*  44 */   public boolean isEnabled() { return this.enable; }
/*  45 */   public void enable() { this.enable = true; }
/*     */   
/*  47 */   public void disable() { this.enable = false;
/*  48 */     this.attr = 0;this.eventMask = 0;
/*     */   }
/*     */   
/*     */   public boolean sameClient(XClient c) {
/*  52 */     return (this.resource & 0x1FC00000) == c.clientAsMask;
/*     */   }
/*     */   
/*     */   public void set(int resource, XWindow XWindow, int oevents, int emask, int kmode, int pmode, XWindow confineto)
/*     */   {
/*  57 */     this.resource = resource;
/*  58 */     this.window = XWindow;
/*  59 */     this.attr = 0;
/*  60 */     this.attr |= (oevents != 0 ? 1 : 0);
/*  61 */     this.eventMask = emask;
/*  62 */     this.attr |= (kmode != 0 ? 2 : 0);
/*  63 */     this.attr |= (pmode != 0 ? 4 : 0);
/*  64 */     this.confineTo = confineto;
/*     */   }
/*     */   
/*     */   public Grab next;
/*     */   public int resource;
/*     */   public XWindow window;
/*     */   public int attr;
/*     */   public int type;
/*     */   public Detail modifiersDetail;
/*     */   public Detail detail;
/*     */   public XWindow confineTo;
/*     */   public int eventMask;
/*     */   public XClient getClient()
/*     */   {
/*  78 */     return XClient.X_CLIENTs[((this.resource & 0x1FC00000) >> 22)];
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public static Grab createGrab(XClient c, XWindow gw, int emask, int oe, int kmode, int pmode, int mod, int type, int button, XWindow cto)
/*     */   {
/*  85 */     Grab grab = new Grab(fakeClientId(c));
/*  86 */     grab.resource = grab.id;
/*  87 */     grab.window = gw;
/*  88 */     grab.eventMask = emask;
/*  89 */     grab.attr |= (oe != 0 ? 1 : 0);
/*  90 */     grab.attr |= (kmode != 0 ? 2 : 0);
/*  91 */     grab.attr |= (pmode != 0 ? 4 : 0);
/*  92 */     grab.modifiersDetail.exact = mod;
/*  93 */     grab.modifiersDetail.pMask = null;
/*  94 */     grab.type = type;
/*  95 */     grab.detail.exact = button;
/*  96 */     grab.detail.pMask = null;
/*  97 */     grab.confineTo = cto;
/*  98 */     return grab;
/*     */   }
/*     */   
/*     */   public void freeGrab() {}
/*     */   
/*     */   public boolean grabSupersedesSecond(Grab pSecondGrab) {
/* 104 */     if (!this.modifiersDetail.detailSupersedesSecond(pSecondGrab.modifiersDetail, XWindow.AnyModifier))
/*     */     {
/* 106 */       return false;
/*     */     }
/* 108 */     if (this.detail.detailSupersedesSecond(pSecondGrab.detail, XWindow.AnyKey))
/* 109 */       return true;
/* 110 */     return false;
/*     */   }
/*     */   
/*     */   public boolean grabMatchesSecond(Grab pSecondGrab) {
/* 114 */     if (this.type != pSecondGrab.type) return false;
/* 115 */     if ((grabSupersedesSecond(pSecondGrab)) || (pSecondGrab.grabSupersedesSecond(this)))
/*     */     {
/* 117 */       return true; }
/* 118 */     if ((pSecondGrab.detail.detailSupersedesSecond(this.detail, XWindow.AnyKey)) && (this.modifiersDetail.detailSupersedesSecond(pSecondGrab.modifiersDetail, XWindow.AnyModifier)))
/*     */     {
/*     */ 
/* 121 */       return true; }
/* 122 */     if ((this.detail.detailSupersedesSecond(pSecondGrab.detail, XWindow.AnyKey)) && (pSecondGrab.modifiersDetail.detailSupersedesSecond(this.modifiersDetail, XWindow.AnyModifier)))
/*     */     {
/*     */ 
/* 125 */       return true; }
/* 126 */     return false;
/*     */   }
/*     */   
/*     */   public int addPassiveGrabToList() {
/* 130 */     for (Grab grab = this.window.getPassiveGrabs(); grab != null; grab = grab.next) {
/* 131 */       if ((grabMatchesSecond(grab)) && 
/* 132 */         ((this.resource & 0x1FC00000) != (grab.resource & 0x1FC00000)))
/*     */       {
/* 134 */         freeGrab();
/* 135 */         return 0;
/*     */       }
/*     */     }
/*     */     
/*     */ 
/* 140 */     this.window.makeOptional();
/* 141 */     this.next = this.window.optional.passiveGrabs;
/* 142 */     this.window.optional.passiveGrabs = this;
/* 143 */     add(this);
/* 144 */     return 1;
/*     */   }
/*     */   
/*     */   public static void deactivatePointerGrab() throws IOException {
/* 148 */     XWindow.enter_leaveEvent(XWindow.grab.window, XWindow.sprite.win, 1);
/* 149 */     XWindow.grab = null;
/*     */   }
/*     */   
/*     */   public void activatePointerGrab(int time, boolean autoGrab) throws IOException
/*     */   {
/* 154 */     XWindow oldWin = XWindow.grab != null ? XWindow.grab.window : XWindow.sprite.win;
/* 155 */     XWindow.enter_leaveEvent(oldWin, this.window, 1);
/* 156 */     XWindow.grab = this;
/*     */   }
/*     */   
/*     */   public boolean deletePassiveGrabFromList()
/*     */   {
/* 161 */     int i = 0;
/* 162 */     for (Grab grab = this.window.getPassiveGrabs(); grab != null; grab = grab.next) {
/* 163 */       i++;
/*     */     }
/* 165 */     if (i == 0) {
/* 166 */       return true;
/*     */     }
/*     */     
/* 169 */     Grab[] deletes = new Grab[i];
/* 170 */     int ndels = 0;
/* 171 */     int nadds = 0;
/* 172 */     int nups = 0;
/* 173 */     boolean ok = true;
/*     */     
/* 175 */     for (grab = this.window.getPassiveGrabs(); (grab != null) && (ok); grab = grab.next) {
/* 176 */       if (((grab.resource & 0x1FC00000) == (this.resource & 0x1FC00000)) && (grab.grabMatchesSecond(this)))
/*     */       {
/*     */ 
/*     */ 
/* 180 */         if (grabSupersedesSecond(grab)) {
/* 181 */           deletes[(ndels++)] = grab;
/*     */         }
/*     */       }
/*     */     }
/* 185 */     if (ok)
/*     */     {
/*     */ 
/* 188 */       for (i = 0; i < ndels; i++) {
/* 189 */         freeResource(deletes[i].id, 0);
/*     */       }
/*     */     }
/* 192 */     return ok;
/*     */   }
/*     */   
/*     */   public void delete() throws IOException {
/* 196 */     Grab prev = null;
/* 197 */     for (Grab g = this.window.getPassiveGrabs(); g != null; g = g.next) {
/* 198 */       if (this == g) {
/* 199 */         if (prev != null) {
/* 200 */           prev.next = g.next;
/*     */         }
/*     */         else {
/* 203 */           this.window.optional.passiveGrabs = g.next;
/* 204 */           break;
/*     */         }
/* 206 */         prev = g;
/*     */       }
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Joey\Downloads\launch.jar!\com\emt\proteus\xserver\display\Grab.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */