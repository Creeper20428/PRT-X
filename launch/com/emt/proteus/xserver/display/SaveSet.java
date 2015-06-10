/*     */ package com.emt.proteus.xserver.display;
/*     */ 
/*     */ import com.emt.proteus.xserver.client.XClient;
/*     */ import com.emt.proteus.xserver.io.ComChannel;
/*     */ import java.io.IOException;
/*     */ import java.util.Enumeration;
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
/*     */ public final class SaveSet
/*     */ {
/*     */   private static final int SetModeInsert = 0;
/*     */   private static final int SetModeDelete = 1;
/*     */   private Vector bag;
/*     */   
/*     */   private SaveSet()
/*     */   {
/*  33 */     this.bag = new Vector();
/*     */   }
/*     */   
/*     */   public static void reqChangeSaveSet(XClient c) throws IOException
/*     */   {
/*  38 */     int foo = c.channel.readInt();
/*  39 */     c.length -= 2;
/*  40 */     XWindow w = c.lookupWindow(foo);
/*  41 */     if (w == null) {
/*  42 */       c.errorValue = foo;
/*  43 */       c.errorReason = 3;
/*  44 */       return;
/*     */     }
/*  46 */     if ((c.data != 0) && (c.data != 1)) {
/*  47 */       c.errorValue = c.data;
/*  48 */       c.errorReason = 2;
/*  49 */       return;
/*     */     }
/*  51 */     if (c.saveSet == null) {
/*  52 */       c.saveSet = new SaveSet();
/*     */     }
/*  54 */     c.saveSet.proc(w, c.data);
/*     */   }
/*     */   
/*     */   public void proc(XWindow w, int mode) {
/*  58 */     if (mode == 0) {
/*  59 */       this.bag.addElement(w);
/*  60 */       return;
/*     */     }
/*  62 */     this.bag.removeElement(w);
/*     */   }
/*     */   
/*     */   public void delete() throws IOException {
/*  66 */     this.bag.removeAllElements();
/*  67 */     this.bag = null;
/*     */   }
/*     */   
/*     */   public static void delete(XWindow w) {
/*  71 */     for (int i = 0; i < XClient.currentMaxClients; i++) {
/*  72 */       XClient c = XClient.X_CLIENTs[i];
/*  73 */       if ((c != null) && (c.saveSet != null)) {
/*  74 */         c.saveSet.proc(w, 1);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public static void handle(XClient c) throws IOException {
/*  80 */     if (c.saveSet != null) {
/*  81 */       SaveSet ss = c.saveSet;
/*  82 */       for (Enumeration e = ss.bag.elements(); e.hasMoreElements();) {
/*  83 */         XWindow w = (XWindow)e.nextElement();
/*     */         
/*  85 */         XWindow parent = w.parent;
/*  86 */         while ((parent != null) && (XClient.X_CLIENTs[((parent.id & 0x1FC00000) >> 22)] == c))
/*     */         {
/*  88 */           parent = parent.parent;
/*     */         }
/*  90 */         if ((parent != null) && 
/*  91 */           (parent != w.parent)) {
/*     */           try {
/*  93 */             w.reparent(parent, w.x - w.borderWidth - parent.x, w.y - w.borderWidth - parent.y, c);
/*     */           }
/*     */           catch (Exception ee) {}
/*     */           
/*     */ 
/*     */ 
/*     */ 
/* 100 */           if ((!w.isRealized()) && (w.isMapped()))
/* 101 */             w.attr &= 0xFFF7FFFF;
/*     */           try {
/* 103 */             w.mapWindow(c);
/*     */           }
/*     */           catch (Exception ee) {}
/*     */         }
/*     */       }
/*     */       
/*     */ 
/* 110 */       c.saveSet.delete();
/* 111 */       c.saveSet = null;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Joey\Downloads\launch.jar!\com\emt\proteus\xserver\display\SaveSet.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */