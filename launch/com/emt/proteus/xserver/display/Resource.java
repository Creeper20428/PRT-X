/*     */ package com.emt.proteus.xserver.display;
/*     */ 
/*     */ import com.emt.proteus.xserver.client.XClient;
/*     */ import java.io.IOException;
/*     */ import java.util.Enumeration;
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
/*     */ public class Resource
/*     */ {
/*  28 */   static Object LOCK = XClient.class;
/*     */   
/*     */   public static final int RC_VANILLA = 0;
/*     */   
/*     */   public static final int RC_CACHED = Integer.MIN_VALUE;
/*     */   
/*     */   public static final int RC_DRAWABLE = 1073741824;
/*     */   
/*     */   public static final int SERVER_BIT = 536870912;
/*     */   
/*     */   public static final int SERVER_MINID = 32;
/*     */   
/*     */   public static final int RESOURCE_ID_MASK = 4194303;
/*     */   public static final int CLIENTOFFSET = 22;
/*     */   public static final int CLIENTMASK = 532676608;
/*     */   public static final int RC_NEVERRETAIN = 536870912;
/*     */   public static final int RC_LASTPREDEF = 536870912;
/*     */   public static final int RC_ANY = -1;
/*     */   public static final int RT_WINDOW = -1073741823;
/*     */   public static final int RT_PIXMAP = -1073741822;
/*     */   public static final int RT_GC = -2147483645;
/*     */   public static final int RT_FONT = 4;
/*     */   public static final int RT_CURSOR = 5;
/*     */   public static final int RT_COLORMAP = 6;
/*     */   public static final int RT_CMAPENTRY = 7;
/*     */   public static final int RT_OTHERCLIENT = 536870920;
/*     */   public static final int RT_PASSIVEGRAB = 536870921;
/*     */   public static final int RT_LASTPREDEF = 9;
/*     */   public static final int RT_NONE = 0;
/*  57 */   public static ClientResource[] clients = new ClientResource[''];
/*     */   
/*  59 */   public static int lastResourceType = 9;
/*  60 */   public static int lastResourceClass = 536870912;
/*     */   protected int id;
/*     */   
/*  63 */   public static synchronized int newType() { int next = lastResourceType + 1;
/*  64 */     if ((next & lastResourceClass) != 0) return 0;
/*  65 */     lastResourceType = next;
/*  66 */     return next;
/*     */   }
/*     */   
/*  69 */   public static synchronized int newClss() { int next = lastResourceClass >> 1;
/*  70 */     if ((next & lastResourceType) != 0) return 0;
/*  71 */     lastResourceClass = next;
/*  72 */     return next;
/*     */   }
/*     */   
/*     */   protected int rtype;
/*     */   public Resource() {}
/*     */   
/*     */   public Resource(int id, int rtype)
/*     */   {
/*  80 */     this.id = id;this.rtype = rtype;
/*     */   }
/*     */   
/*     */   public int hashCode() {
/*  84 */     return this.id;
/*     */   }
/*     */   
/*     */   public boolean equals(Object o) {
/*  88 */     if ((o == null) || (this.id != o.hashCode())) return false;
/*  89 */     if (!(o instanceof Resource)) return true;
/*  90 */     if (!(o instanceof Key)) {
/*  91 */       return this.rtype == ((Resource)o).rtype;
/*     */     }
/*  93 */     Key key = (Key)o;
/*  94 */     if (key.clss == 0) return this.rtype == key.rtype;
/*  95 */     if (key.clss == -1) return true;
/*  96 */     return (this.rtype & key.clss) != 0;
/*     */   }
/*     */   
/*     */   public static void add(Resource r) {
/* 100 */     int client = (r.id & 0x1FC00000) >> 22;
/* 101 */     ClientResource cr = clients[client];
/* 102 */     cr.put(r, r);
/*     */   }
/*     */   
/*     */   public static void RemoveResource(Resource r) {
/* 106 */     int client = (r.id & 0x1FC00000) >> 22;
/* 107 */     ClientResource cr = clients[client];
/* 108 */     RemoveResource(cr, r);
/*     */   }
/*     */   
/*     */   public static void RemoveResource(ClientResource cr, Resource r) {
/* 112 */     synchronized (LOCK) {
/* 113 */       if ((r.rtype & 0x80000000) != 0) XClient.flushCache(r.id);
/*     */     }
/* 115 */     cr.remove(r);
/*     */   }
/*     */   
/*     */   public static void freeResource(int id, int skip) {
/*     */     for (;;) {
/* 120 */       Resource r = lookupIDByClass(id, -1);
/* 121 */       if (r == null) break;
/* 122 */       RemoveResource(r);
/*     */       try {
/* 124 */         if (r.rtype != skip) {
/* 125 */           deleteit(r);
/*     */         }
/*     */       } catch (Exception e) {}
/*     */     }
/*     */   }
/*     */   
/*     */   public static void freeResourceByType(int id, int typ, int skip) {
/* 132 */     Resource r = lookupIDByType(id, typ);
/* 133 */     if (r != null) {
/* 134 */       RemoveResource(r);
/*     */       try {
/* 136 */         if (r.rtype != skip) {
/* 137 */           deleteit(r);
/*     */         }
/*     */       }
/*     */       catch (Exception e) {}
/*     */     }
/*     */   }
/*     */   
/*     */   public static Resource lookupIDByClass(int id, int clss) {
/* 145 */     int client = (id & 0x1FC00000) >> 22;
/* 146 */     if (clients.length <= client) return null;
/* 147 */     ClientResource cr = clients[client];
/* 148 */     if (cr == null) { return null;
/*     */     }
/* 150 */     synchronized (cr) {
/*     */       try {
/* 152 */         cr.key.id = id;
/* 153 */         cr.key.clss = clss;
/* 154 */         Resource r = (Resource)cr.get(cr.key);
/* 155 */         return r;
/*     */       }
/*     */       catch (Exception e) {
/* 158 */         return null;
/*     */       }
/*     */     }
/*     */   }
/*     */   
/* 163 */   public static Resource lookupIDByType(int id, int rtype) { int client = (id & 0x1FC00000) >> 22;
/* 164 */     if (clients.length <= client) return null;
/* 165 */     ClientResource cr = clients[client];
/* 166 */     if (cr == null) { return null;
/*     */     }
/* 168 */     synchronized (cr) {
/* 169 */       cr.key.id = id;
/* 170 */       cr.key.clss = 0;
/* 171 */       cr.key.rtype = rtype;
/* 172 */       Resource r = (Resource)cr.get(cr.key);
/* 173 */       return r;
/*     */     }
/*     */   }
/*     */   
/*     */   public static void freeClientNeverResources(XClient c) {
/* 178 */     if (c == null) return;
/* 179 */     ClientResource cr = clients[c.index];
/* 180 */     synchronized (LOCK) {
/*     */       Enumeration e;
/* 182 */       try { for (e = cr.elements(); e.hasMoreElements();) {
/* 183 */           Resource r = (Resource)e.nextElement();
/* 184 */           if ((r.rtype & 0x20000000) != 0) {
/* 185 */             deleteit(r);
/*     */             
/* 187 */             RemoveResource(cr, r);
/*     */           }
/*     */         }
/*     */       } catch (Exception e) {}
/*     */     }
/*     */   }
/*     */   
/*     */   public static void freeClientResources(XClient c) {
/* 195 */     if (c == null) return;
/* 196 */     ClientResource cr = clients[c.index];
/* 197 */     synchronized (LOCK) {
/*     */       Enumeration e;
/* 199 */       try { for (e = cr.elements(); e.hasMoreElements();) {
/* 200 */           Resource r = (Resource)e.nextElement();
/* 201 */           deleteit(r);
/* 202 */           RemoveResource(cr, r);
/*     */         }
/*     */       } catch (Exception e) {}
/*     */     }
/*     */   }
/*     */   
/*     */   public static void deleteit(Resource r) {
/* 209 */     try { r.delete();
/*     */     }
/*     */     catch (Exception ee) {}
/*     */   }
/*     */   
/*     */   void delete() throws IOException
/*     */   {}
/*     */   
/*     */   public static XClient lookupClient(int rid)
/*     */   {
/* 219 */     Resource res = lookupIDByClass(rid, -1);
/* 220 */     int clientIndex = (rid & 0x1FC00000) >> 22;
/* 221 */     if ((clientIndex != 0) && (res != null) && (clients[clientIndex] != null) && ((rid & 0x20000000) == 0))
/*     */     {
/*     */ 
/*     */ 
/* 225 */       return XClient.X_CLIENTs[clientIndex];
/*     */     }
/* 227 */     return null;
/*     */   }
/*     */   
/*     */   public static void initClientResource(XClient c) {
/* 231 */     ClientResource cr = clients[c.index] = new ClientResource();
/* 232 */     cr.fakeID = (c.clientAsMask | (c.index != 0 ? 536870912 : 32));
/* 233 */     cr.endFakeID = ((cr.fakeID | 0x3FFFFF) + 1);
/* 234 */     cr.expectID = c.clientAsMask;
/*     */   }
/*     */   
/*     */   public static int fakeClientId(XClient c)
/*     */   {
/* 239 */     int id = clients[c.index].fakeID++;
/* 240 */     if (id != clients[c.index].endFakeID) {
/* 241 */       return id;
/*     */     }
/*     */     
/* 244 */     return id;
/*     */   }
/*     */ }


/* Location:              C:\Users\Joey\Downloads\launch.jar!\com\emt\proteus\xserver\display\Resource.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */