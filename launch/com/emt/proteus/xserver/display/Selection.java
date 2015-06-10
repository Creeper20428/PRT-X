/*     */ package com.emt.proteus.xserver.display;
/*     */ 
/*     */ import com.emt.proteus.xserver.client.XClient;
/*     */ import com.emt.proteus.xserver.io.ComChannel;
/*     */ import com.emt.proteus.xserver.protocol.Atom;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class Selection
/*     */ {
/*     */   public int selection;
/*     */   public int lastTimeChanged;
/*     */   public int wid;
/*     */   public XWindow XWindow;
/*     */   public XClient XClient;
/*  36 */   public static Selection[] currentSelection = new Selection[10];
/*     */   
/*     */   public Selection(int selection, int time, XWindow w, XClient c)
/*     */   {
/*  40 */     this.selection = selection;
/*  41 */     this.lastTimeChanged = time;
/*  42 */     this.XWindow = w;
/*  43 */     this.wid = (w != null ? w.id : 0);
/*  44 */     this.XClient = c;
/*     */   }
/*     */   
/*     */   public static synchronized Selection getSelection(int selection) {
/*  48 */     Selection p = null;
/*  49 */     int len = currentSelection.length;
/*  50 */     for (int i = 0; (i < len) && (currentSelection[i] != null); i++) {
/*  51 */       if (currentSelection[i].selection == selection) {
/*  52 */         p = currentSelection[i];
/*  53 */         break;
/*     */       }
/*     */     }
/*  56 */     return p;
/*     */   }
/*     */   
/*     */   public static synchronized void addSelection(int selection, int time, XWindow w, XClient c)
/*     */   {
/*  61 */     Selection p = new Selection(selection, time, w, w != null ? c : null);
/*  62 */     if (currentSelection[(currentSelection.length - 1)] != null) {
/*  63 */       Selection[] foo = new Selection[currentSelection.length * 2];
/*  64 */       System.arraycopy(currentSelection, 0, foo, 0, currentSelection.length);
/*  65 */       currentSelection = foo;
/*     */     }
/*     */     
/*  68 */     for (int i = 0; currentSelection[i] != null; i++) {}
/*  69 */     currentSelection[i] = p;
/*     */   }
/*     */   
/*     */   public static void reqConvertSelection(XClient c)
/*     */     throws IOException
/*     */   {
/*  75 */     boolean paramsOkay = true;
/*  76 */     ComChannel comChannel = c.channel;
/*  77 */     int requestor = comChannel.readInt();
/*  78 */     XWindow w = c.lookupWindow(requestor);
/*  79 */     if (w == null) {
/*  80 */       c.errorValue = requestor;
/*  81 */       c.errorReason = 3;
/*     */     }
/*  83 */     int selection = comChannel.readInt();
/*  84 */     paramsOkay = Atom.valid(selection);
/*  85 */     int target = comChannel.readInt();
/*  86 */     paramsOkay &= Atom.valid(target);
/*  87 */     int property = comChannel.readInt();
/*  88 */     if (property != 0) {
/*  89 */       paramsOkay &= Atom.valid(property);
/*     */     }
/*  91 */     int time = comChannel.readInt();
/*  92 */     c.length -= 6;
/*  93 */     if (c.errorReason != 0) {
/*  94 */       return;
/*     */     }
/*     */     
/*  97 */     if (paramsOkay) {
/*  98 */       Selection s = getSelection(selection);
/*  99 */       if ((s != null) && (s.XClient != null)) {
/* 100 */         c.cevent.mkSelectionRequest(time, s.wid, requestor, selection, target, property);
/*     */         
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 106 */         if (s.XClient.sendEvent(c.cevent, 1, 0, 0, null) != 0)
/*     */         {
/*     */ 
/*     */ 
/*     */ 
/* 111 */           return; }
/*     */       }
/* 113 */       c.cevent.mkSelectionNotify(time, requestor, selection, target, 0);
/*     */       
/*     */ 
/*     */ 
/*     */ 
/* 118 */       c.sendEvent(c.cevent, 1, 0, 0, null);
/*     */       
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 124 */       return;
/*     */     }
/*     */     
/*     */ 
/* 128 */     c.errorValue = property;
/* 129 */     c.errorReason = 5;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public static void reqSetSelectionOwner(XClient c)
/*     */     throws IOException
/*     */   {
/* 137 */     ComChannel comChannel = c.channel;
/* 138 */     int foo = comChannel.readInt();
/* 139 */     c.length -= 2;
/* 140 */     XWindow w = null;
/* 141 */     if (foo != 0) {
/* 142 */       w = c.lookupWindow(foo);
/* 143 */       if (w == null) {
/* 144 */         c.errorValue = foo;
/* 145 */         c.errorReason = 3;
/* 146 */         return;
/*     */       }
/*     */     }
/*     */     
/* 150 */     int selection = comChannel.readInt();
/* 151 */     foo = comChannel.readInt();
/* 152 */     c.length -= 2;
/* 153 */     int time = 0;
/* 154 */     time = (int)System.currentTimeMillis();
/* 155 */     time = foo;
/*     */     
/* 157 */     if (Atom.valid(selection)) {
/* 158 */       int i = 0;
/* 159 */       Selection s = getSelection(selection);
/* 160 */       if (s != null) {
/* 161 */         if ((s.XClient != null) && ((w == null) || (s.XClient != c)) && 
/* 162 */           (s.XClient != null)) {
/* 163 */           c.cevent.mkSelectionClear(time, s.wid, s.selection);
/* 164 */           s.XClient.sendEvent(c.cevent, 1, 0, 0, null);
/*     */         }
/*     */         
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 172 */         s.XWindow = w;
/* 173 */         s.wid = (w != null ? w.id : 0);
/* 174 */         s.lastTimeChanged = time;
/* 175 */         s.XClient = (w != null ? c : null);
/*     */       }
/*     */       else
/*     */       {
/* 179 */         addSelection(selection, time, w, c);
/*     */       }
/* 181 */       return;
/*     */     }
/*     */     
/* 184 */     c.errorValue = selection;
/* 185 */     c.errorReason = 5;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public static void reqGetSelectionOwner(XClient c)
/*     */     throws IOException
/*     */   {
/* 193 */     ComChannel comChannel = c.channel;
/* 194 */     int selection = comChannel.readInt();
/*     */     
/* 196 */     c.length -= 2;
/*     */     
/* 198 */     if (!Atom.valid(selection)) {
/* 199 */       c.errorValue = selection;
/* 200 */       c.errorReason = 5;
/* 201 */       return;
/*     */     }
/*     */     
/* 204 */     synchronized (comChannel) {
/* 205 */       comChannel.writeByte(1);
/* 206 */       Selection s = getSelection(selection);
/* 207 */       comChannel.writePad(1);
/* 208 */       comChannel.writeShort(c.getSequence());
/* 209 */       comChannel.writeInt(0);
/* 210 */       if (s != null) {
/* 211 */         comChannel.writeInt(s.wid);
/*     */       } else
/* 213 */         comChannel.writeInt(0);
/* 214 */       comChannel.writePad(20);
/* 215 */       comChannel.flush();
/*     */     }
/*     */   }
/*     */   
/*     */   public static synchronized void delete(XClient c)
/*     */   {
/* 221 */     int len = currentSelection.length;
/* 222 */     Selection s; for (int i = 0; (i < len) && ((s = currentSelection[i]) != null); i++) {
/* 223 */       if (s.XClient == c) {
/* 224 */         if (i + 1 == len) {
/* 225 */           currentSelection[i] = null;
/*     */         }
/*     */         else
/* 228 */           for (int j = i + 1; j < len; j++) {
/* 229 */             currentSelection[(j - 1)] = currentSelection[j];
/* 230 */             if (currentSelection[j] == null)
/*     */               break;
/*     */           }
/* 233 */         s.XWindow = null;
/* 234 */         s.XClient = null;
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public static synchronized void delete(XWindow w)
/*     */   {
/* 241 */     int len = currentSelection.length;
/* 242 */     Selection s; for (int i = 0; (i < len) && ((s = currentSelection[i]) != null); i++) {
/* 243 */       if (s.XWindow == w) {
/* 244 */         if (i + 1 == len) { currentSelection[i] = null;
/*     */         } else
/* 246 */           for (int j = i + 1; j < len; j++) {
/* 247 */             currentSelection[(j - 1)] = currentSelection[j];
/* 248 */             if (currentSelection[j] == null)
/*     */               break;
/*     */           }
/* 251 */         s.XWindow = null;
/* 252 */         s.XClient = null;
/*     */       }
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Joey\Downloads\launch.jar!\com\emt\proteus\xserver\display\Selection.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */