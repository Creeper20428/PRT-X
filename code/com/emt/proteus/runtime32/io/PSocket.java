/*     */ package com.emt.proteus.runtime32.io;
/*     */ 
/*     */ import com.emt.proteus.utils.CStruct;
/*     */ import com.emt.proteus.utils.CStruct.CField;
/*     */ import com.emt.proteus.utils.Utils;
/*     */ 
/*     */ 
/*     */ public class PSocket
/*     */ {
/*     */   private boolean noDelay;
/*     */   private boolean keepAlive;
/*     */   private Type type;
/*     */   private final Address address;
/*     */   
/*     */   public static enum Type
/*     */   {
/*  17 */     TCP, 
/*  18 */     UDP, 
/*  19 */     FILE, 
/*  20 */     NETLINK;
/*     */     
/*     */ 
/*     */     private Type() {}
/*     */   }
/*     */   
/*     */   public PSocket(Type type, Address address)
/*     */   {
/*  28 */     this.type = type;
/*  29 */     this.address = address;
/*     */   }
/*     */   
/*     */   public void setNoDelay(boolean noDelay) {
/*  33 */     this.noDelay = noDelay;
/*     */   }
/*     */   
/*     */   public void setKeepAlive(boolean keepAlive) {
/*  37 */     this.keepAlive = keepAlive;
/*     */   }
/*     */   
/*     */   public boolean isNoDelay() {
/*  41 */     return this.noDelay;
/*     */   }
/*     */   
/*     */   public boolean isKeepAlive() {
/*  45 */     return this.keepAlive;
/*     */   }
/*     */   
/*     */   public Type getType() {
/*  49 */     return this.type;
/*     */   }
/*     */   
/*     */   public Address getAddress() {
/*  53 */     return this.address;
/*     */   }
/*     */   
/*     */   public String toString() {
/*  57 */     return "psocket : addr=" + this.address;
/*     */   }
/*     */   
/*     */   public static class Address
/*     */     extends CStruct
/*     */   {
/*  63 */     public static CStruct.CField sa_family = _short(0, "sa_family");
/*  64 */     public static CStruct.CField sa_data = _cstring(sa_family, "sa_data", 14);
/*  65 */     public static CStruct.CField in_port = _short(sa_family, "in_port");
/*  66 */     public static CStruct.CField in_addr = _integer(in_port, "in_addr");
/*     */     
/*     */     public Address()
/*     */     {
/*  70 */       super();
/*     */     }
/*     */     
/*  73 */     public Address(int size) { super(); }
/*     */     
/*     */     public Address family(short family)
/*     */     {
/*  77 */       setFamily(family);
/*  78 */       return this;
/*     */     }
/*     */     
/*  81 */     public Address port(int newval) { setPort(newval);
/*  82 */       return this;
/*     */     }
/*     */     
/*     */     public Address ip(int a, int b, int c, int d) {
/*  86 */       setIP((a & 0xFF) << 24 | (b & 0xFF) << 24 | (c & 0xFF) << 8 | d & 0xFF);
/*     */       
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  92 */       return this;
/*     */     }
/*     */     
/*  95 */     public Address ip(int ip) { setIP(ip);
/*  96 */       return this;
/*     */     }
/*     */     
/*     */     public int getPort() {
/* 100 */       return Short.reverseBytes(in_port.shortValue(this));
/*     */     }
/*     */     
/* 103 */     public void setPort(int port) { in_port.set(this, Short.reverseBytes((short)port)); }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     public int getIP()
/*     */     {
/* 112 */       return Integer.reverseBytes(in_addr.intValue(this));
/*     */     }
/*     */     
/* 115 */     public void setIP(int ip) { in_addr.set(this, Integer.reverseBytes(ip)); }
/*     */     
/*     */     public short getFamily() {
/* 118 */       return sa_family.shortValue(this);
/*     */     }
/*     */     
/* 121 */     public void setFamily(short family) { sa_family.set(this, family); }
/*     */     
/*     */     public String toString() {
/* 124 */       return String.format(" ip=%08x port=%d", new Object[] { Integer.valueOf(getIP()), Integer.valueOf(getPort()) });
/*     */     }
/*     */     
/*     */     public String toAddressString() {
/* 128 */       byte[] bytes = IP4Address();
/* 129 */       return String.format("%d.%d.%d.%d:%d", new Object[] { Integer.valueOf(0xFF & bytes[0]), Integer.valueOf(0xFF & bytes[1]), Integer.valueOf(0xFF & bytes[2]), Integer.valueOf(0xFF & bytes[3]), Integer.valueOf(getPort()) });
/*     */     }
/*     */     
/*     */     public byte[] IP4Address() {
/* 133 */       int bigEndianAddress = in_addr.intValue(this);
/* 134 */       return Utils.toBytes(bigEndianAddress);
/*     */     }
/*     */     
/*     */     public Address assignIP4(short inet, int ip, int port) {
/* 138 */       return family(inet).ip(ip).port(port);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Joey\Downloads\codejar.jar!\com\emt\proteus\runtime32\io\PSocket.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */