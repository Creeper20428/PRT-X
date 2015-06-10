/*     */ package com.emt.proteus.xserver.display;
/*     */ 
/*     */ import com.emt.proteus.xserver.client.XClient;
/*     */ import com.emt.proteus.xserver.io.ComChannel;
/*     */ import java.io.IOException;
/*     */ import java.io.PrintStream;
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
/*     */ public abstract class Extension
/*     */ {
/*  29 */   public static int currentMaxType = 127;
/*  30 */   public static int currentMaxEventType = 64;
/*  31 */   public static int currentMaxErrorType = 17;
/*     */   
/*  33 */   public static Extension[] ext = new Extension[10];
/*     */   public int type;
/*     */   
/*  36 */   public static void init(String foo) { if (foo == null) return;
/*  37 */     int start = 0;
/*     */     int end;
/*  39 */     while ((end = foo.indexOf(',', start)) > 0) {
/*  40 */       load(foo.substring(start, end));
/*  41 */       start = end + 1;
/*     */     }
/*     */     
/*  44 */     if (start < foo.length()) { load(foo.substring(start));
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public int eventbase;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public int eventcount;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public int errorbase;
/*     */   
/*     */ 
/*     */ 
/*     */   public int errorcount;
/*     */   
/*     */ 
/*     */ 
/*     */   public String name;
/*     */   
/*     */ 
/*     */ 
/*     */   public static void load(String name) {}
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public static final void dispatch(int reqType, XClient c)
/*     */     throws IOException
/*     */   {
/*  82 */     for (int i = 0; i < ext.length; i++) {
/*  83 */       if (ext[i] == null) break;
/*  84 */       if (ext[i].type == reqType) {
/*  85 */         ext[i].dispatch(c);
/*  86 */         return;
/*     */       }
/*     */     }
/*  89 */     System.err.println("Extension: unknown reqType " + reqType);
/*     */   }
/*     */   
/*     */   public static void swap(int etyp, Event e) {
/*  93 */     for (int i = 0; i < ext.length; i++) {
/*  94 */       if (ext[i] == null) break;
/*  95 */       if ((ext[i].eventcount != 0) && 
/*  96 */         (ext[i].eventbase <= etyp) && (etyp <= ext[i].eventbase + ext[i].eventcount))
/*     */       {
/*  98 */         ext[i].swap(e);
/*  99 */         return;
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public abstract void dispatch(XClient paramXClient) throws IOException;
/*     */   
/*     */   public abstract void swap(Event paramEvent);
/*     */   
/*     */   public static void reqListExtensions(XClient c) throws IOException
/*     */   {
/* 110 */     ComChannel comChannel = c.channel;
/* 111 */     int foo = c.length;
/*     */     
/* 113 */     synchronized (comChannel) {
/* 114 */       comChannel.writeByte(1);
/* 115 */       int count = 0;
/*     */       
/* 117 */       while (ext[count] != null) {
/* 118 */         count++;
/*     */       }
/*     */       
/* 121 */       comChannel.writeByte(count);
/* 122 */       comChannel.writeShort(c.getSequence());
/*     */       
/* 124 */       if (count == 0) {
/* 125 */         comChannel.writeInt(0);
/* 126 */         comChannel.writePad(24);
/* 127 */         return;
/*     */       }
/*     */       
/* 130 */       int i = 0;
/* 131 */       byte[] buf = c.bbuffer;
/*     */       
/* 133 */       int n = 0;
/*     */       
/* 135 */       while (ext[i] != null) {
/* 136 */         byte[] b = ext[i].name.getBytes();
/* 137 */         buf[n] = ((byte)b.length);n++;
/* 138 */         System.arraycopy(b, 0, buf, n, b.length);n += b.length;
/* 139 */         i++;
/*     */       }
/* 141 */       comChannel.writeInt((n + 3) / 4);
/* 142 */       comChannel.writePad(24);
/* 143 */       comChannel.writeByte(buf, 0, n);
/* 144 */       if ((-n & 0x3) > 0) {
/* 145 */         comChannel.writePad(-n & 0x3);
/*     */       }
/* 147 */       comChannel.flush();
/*     */     }
/*     */   }
/*     */   
/*     */   public static void reqQueryExtension(XClient c)
/*     */     throws IOException
/*     */   {
/* 154 */     ComChannel comChannel = c.channel;
/* 155 */     int foo = c.length;
/* 156 */     int n = comChannel.readShort();
/* 157 */     comChannel.readPad(2);
/*     */     
/* 159 */     comChannel.readByte(c.bbuffer, 0, n);
/* 160 */     comChannel.readPad(-n & 0x3);
/*     */     
/* 162 */     String name = new String(c.bbuffer, 0, n);
/*     */     
/* 164 */     synchronized (comChannel) {
/* 165 */       comChannel.writeByte(1);
/* 166 */       comChannel.writePad(1);
/* 167 */       comChannel.writeShort(c.getSequence());
/* 168 */       comChannel.writeInt(0);
/* 169 */       for (int i = 0; i < ext.length; i++) {
/* 170 */         if (ext[i] == null) break;
/* 171 */         if (ext[i].name.equals(name)) {
/* 172 */           comChannel.writeByte(1);
/* 173 */           comChannel.writeByte(ext[i].type);
/* 174 */           if (ext[i].eventcount == 0) comChannel.writeByte(0); else
/* 175 */             comChannel.writeByte(ext[i].eventbase);
/* 176 */           if (ext[i].errorcount == 0) comChannel.writeByte(0); else
/* 177 */             comChannel.writeByte(ext[i].errorbase);
/* 178 */           comChannel.writePad(20);
/* 179 */           return;
/*     */         }
/*     */       }
/* 182 */       comChannel.writeByte(0);
/* 183 */       comChannel.writeByte(0);
/* 184 */       comChannel.writeByte(0);
/* 185 */       comChannel.writeByte(0);
/* 186 */       comChannel.writePad(20);
/* 187 */       comChannel.flush();
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Joey\Downloads\launch.jar!\com\emt\proteus\xserver\display\Extension.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */