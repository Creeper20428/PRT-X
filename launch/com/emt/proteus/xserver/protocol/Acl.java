/*     */ package com.emt.proteus.xserver.protocol;
/*     */ 
/*     */ import com.emt.proteus.xserver.client.XClient;
/*     */ import com.emt.proteus.xserver.io.ComChannel;
/*     */ import java.io.IOException;
/*     */ import java.net.InetAddress;
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
/*     */ public final class Acl
/*     */ {
/*  29 */   private static int mode = 0;
/*  30 */   private static int count = 0;
/*     */   
/*     */ 
/*  33 */   private static int[] hosts = new int[3];
/*  34 */   static { for (int i = 0; i < hosts.length; i++) hosts[i] = 0;
/*     */   }
/*     */   
/*  37 */   public static void parse(String str) { str = "," + str;
/*  38 */     char[] b = str.toCharArray();
/*  39 */     int i = 0;int s = 0;int e = 0;
/*  40 */     while (i < b.length) {
/*  41 */       while ((i < b.length) && (b[i] == ' ')) i++; if ((b.length > i) && 
/*  42 */         (b[i] == ',')) {
/*  43 */         i++; if (b.length > i) {
/*  44 */           while ((i < b.length) && (b[i] == ' ')) i++; if (b.length > i)
/*     */           {
/*  46 */             if (b[i] == '-') {
/*  47 */               i++;s = i;
/*  48 */               while ((i < b.length) && (b[i] != ' ') && (b[i] != ',')) i++;
/*  49 */               e = i;
/*  50 */               if (e - s == 0)
/*     */               {
/*  52 */                 mode = 1;
/*     */               }
/*     */               else {
/*     */                 try
/*     */                 {
/*  57 */                   InetAddress[] addr = InetAddress.getAllByName(new String(b, s, e - s));
/*     */                   
/*  59 */                   for (int j = 0; j < addr.length; j++) {
/*  60 */                     byte[] bb = addr[j].getAddress();
/*  61 */                     delete((bb[0] & 0xFF) << 24 | (bb[1] & 0xFF) << 16 | (bb[2] & 0xFF) << 8 | bb[3] & 0xFF);
/*     */                   }
/*     */                 }
/*     */                 catch (Exception ee) {}
/*     */               }
/*     */               
/*     */ 
/*     */ 
/*  69 */               if (b.length <= i) {
/*     */                 break;
/*     */               }
/*     */             } else {
/*  73 */               if (b[i] == '+') {
/*  74 */                 i++;
/*     */               }
/*  76 */               s = i;
/*  77 */               while ((i < b.length) && (b[i] != ' ') && (b[i] != ',')) i++;
/*  78 */               e = i;
/*  79 */               if (e - s == 0)
/*     */               {
/*  81 */                 mode = 0;
/*     */               }
/*     */               else {
/*     */                 try
/*     */                 {
/*  86 */                   InetAddress[] addr = InetAddress.getAllByName(new String(b, s, e - s));
/*     */                   
/*  88 */                   for (int j = 0; j < addr.length; j++) {
/*  89 */                     byte[] bb = addr[j].getAddress();
/*  90 */                     add((bb[0] & 0xFF) << 24 | (bb[1] & 0xFF) << 16 | (bb[2] & 0xFF) << 8 | bb[3] & 0xFF);
/*     */                   }
/*     */                 }
/*     */                 catch (Exception ee) {}
/*     */               }
/*     */               
/*     */ 
/*     */ 
/*  98 */               if (b.length <= i) break;
/*     */             } }
/*     */         }
/*     */       } } }
/*     */   
/* 103 */   public static synchronized void add(int address) { for (int i = 0; 
/* 104 */         i < hosts.length; i++) {
/* 105 */       if (hosts[i] == address) return;
/* 106 */       if (hosts[i] == 0) break;
/*     */     }
/* 108 */     if (i == hosts.length) {
/* 109 */       int[] foo = new int[hosts.length * 2];
/* 110 */       System.arraycopy(hosts, 0, foo, 0, hosts.length);
/* 111 */       hosts = foo;
/*     */     }
/* 113 */     hosts[i] = address;
/* 114 */     count += 1;
/*     */   }
/*     */   
/*     */   public static synchronized void delete(int address)
/*     */   {
/* 119 */     int i = 0;
/* 120 */     while ((i < hosts.length) && (hosts[i] != address)) i++;
/* 121 */     if (i == hosts.length) return;
/* 122 */     hosts[i] = 0;
/* 123 */     count -= 1;
/* 124 */     if (i == hosts.length - 1) { return;
/*     */     }
/* 126 */     System.arraycopy(hosts, i + 1, hosts, i, hosts.length - i - 1);
/* 127 */     hosts[(hosts.length - 1)] = 0;
/*     */   }
/*     */   
/*     */   public static boolean check(InetAddress inet) {
/* 131 */     if (mode == 0) return true;
/* 132 */     byte[] bb = inet.getAddress();
/* 133 */     int address = (bb[0] & 0xFF) << 24 | (bb[1] & 0xFF) << 16 | (bb[2] & 0xFF) << 8 | bb[3] & 0xFF;
/*     */     
/*     */ 
/*     */ 
/* 137 */     int i = 0;
/*     */     
/* 139 */     while ((hosts.length > i) && (hosts[i] != 0))
/*     */     {
/* 141 */       if (hosts[i] == address) return true;
/* 142 */       i++;
/*     */     }
/* 144 */     return false;
/*     */   }
/*     */   
/*     */   public static void reqChangeHosts(XClient c)
/*     */     throws IOException
/*     */   {
/* 150 */     int foo = c.data;
/* 151 */     boolean mode; boolean mode; if (foo != 0) mode = false; else
/* 152 */       mode = true;
/* 153 */     int n = c.length;
/* 154 */     foo = c.channel.readByte();
/* 155 */     if (foo != 0) {}
/*     */     
/*     */ 
/* 158 */     c.channel.readPad(1);
/* 159 */     foo = c.channel.readShort();
/* 160 */     n -= 2;
/* 161 */     while (n != 0) {
/* 162 */       foo = 0;
/* 163 */       foo |= c.channel.readByte() << 24;
/* 164 */       foo |= c.channel.readByte() << 16;
/* 165 */       foo |= c.channel.readByte() << 8;
/* 166 */       foo |= c.channel.readByte();
/*     */       
/* 168 */       n--;
/* 169 */       if (mode) add(foo); else
/* 170 */         delete(foo);
/*     */     }
/*     */   }
/*     */   
/*     */   public static void reqSetAccessControl(XClient c) throws IOException {
/* 175 */     int mode = c.data;
/* 176 */     if (mode != 0) mode = 1; else
/* 177 */       mode = 0;
/*     */   }
/*     */   
/*     */   public static void reqListHosts(XClient c) throws IOException {
/* 181 */     ComChannel comChannel = c.channel;
/* 182 */     synchronized (Acl.class) {
/* 183 */       synchronized (comChannel) {
/* 184 */         comChannel.writeByte(1);
/* 185 */         comChannel.writeByte((byte)mode);
/* 186 */         comChannel.writeShort(c.getSequence());
/* 187 */         comChannel.writeInt(count * 2);
/* 188 */         comChannel.writeShort(count);
/* 189 */         comChannel.writePad(22);
/* 190 */         for (int i = 0; i < count; i++) {
/* 191 */           comChannel.writeByte((byte)0);
/* 192 */           comChannel.writePad(1);
/* 193 */           comChannel.writeShort(4);
/* 194 */           int foo = hosts[i];
/* 195 */           comChannel.writeByte((byte)((foo & 0xFF000000) >> 24) & 0xFF);
/* 196 */           comChannel.writeByte((byte)((foo & 0xFF0000) >> 16) & 0xFF);
/* 197 */           comChannel.writeByte((byte)((foo & 0xFF00) >> 8) & 0xFF);
/* 198 */           comChannel.writeByte((byte)(foo & 0xFF));
/*     */         }
/* 200 */         comChannel.flush();
/*     */       }
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Joey\Downloads\launch.jar!\com\emt\proteus\xserver\protocol\Acl.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */