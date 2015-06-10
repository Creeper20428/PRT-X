/*     */ package com.emt.proteus.utils;
/*     */ 
/*     */ public abstract interface Data {
/*     */   public abstract int getDoubleWord(int paramInt);
/*     */   
/*     */   public abstract long getQuadWord(int paramInt);
/*     */   
/*     */   public abstract short getWord(int paramInt);
/*     */   
/*     */   public abstract byte getByte(int paramInt);
/*     */   
/*     */   public abstract void setDoubleWord(int paramInt1, int paramInt2);
/*     */   
/*     */   public abstract void setQuadWord(int paramInt, long paramLong);
/*     */   
/*     */   public abstract void setWord(int paramInt, short paramShort);
/*     */   
/*     */   public abstract void setByte(int paramInt, byte paramByte);
/*     */   
/*     */   public abstract void load(int paramInt1, byte[] paramArrayOfByte, int paramInt2, int paramInt3);
/*     */   
/*     */   public abstract void store(int paramInt1, byte[] paramArrayOfByte, int paramInt2, int paramInt3);
/*     */   
/*     */   public static class Byte implements Data {
/*     */     private byte[] data;
/*     */     
/*     */     public Byte(int length) {
/*  28 */       this(new byte[length]);
/*     */     }
/*     */     
/*     */     public Byte(byte[] data) {
/*  32 */       this.data = data;
/*     */     }
/*     */     
/*     */     public int getDoubleWord(int offset)
/*     */     {
/*  37 */       return ByteTools.loadI32(this.data, offset);
/*     */     }
/*     */     
/*     */     public long getQuadWord(int offset)
/*     */     {
/*  42 */       return ByteTools.loadI64(this.data, offset);
/*     */     }
/*     */     
/*     */     public short getWord(int offset)
/*     */     {
/*  47 */       return ByteTools.loadI16(this.data, offset);
/*     */     }
/*     */     
/*     */     public byte getByte(int offset)
/*     */     {
/*  52 */       return ByteTools.loadI8(this.data, offset);
/*     */     }
/*     */     
/*     */     public void setDoubleWord(int offset, int value)
/*     */     {
/*  57 */       ByteTools.storeI32(this.data, offset, value);
/*     */     }
/*     */     
/*     */     public void setQuadWord(int offset, long value)
/*     */     {
/*  62 */       ByteTools.storeI64(this.data, offset, value);
/*     */     }
/*     */     
/*     */     public void setWord(int offset, short value)
/*     */     {
/*  67 */       ByteTools.storeI16(this.data, offset, value);
/*     */     }
/*     */     
/*     */     public void setByte(int offset, byte value)
/*     */     {
/*  72 */       ByteTools.storeI8(this.data, offset, value);
/*     */     }
/*     */     
/*     */     public void set(int offset, int length, long value) {
/*  76 */       switch (length) {
/*     */       case 1: 
/*  78 */         setByte(offset, (byte)(int)value);
/*  79 */         break;
/*     */       case 2: 
/*  81 */         setWord(offset, (short)(int)value);
/*  82 */         break;
/*     */       case 4: 
/*  84 */         setDoubleWord(offset, (int)value);
/*  85 */         break;
/*     */       case 8: 
/*  87 */         setQuadWord(offset, value);
/*  88 */         break;
/*  89 */       case 3: case 5: case 6: case 7: default:  throw new IllegalArgumentException("Unsupported length " + length);
/*     */       }
/*     */     }
/*     */     
/*     */     public long get(int offset, int length) {
/*  94 */       switch (length) {
/*     */       case 1: 
/*  96 */         return getByte(offset);
/*     */       case 2: 
/*  98 */         return getWord(offset);
/*     */       case 4: 
/* 100 */         return getDoubleWord(offset);
/*     */       case 8: 
/* 102 */         return getQuadWord(offset); }
/* 103 */       throw new IllegalArgumentException("Unsupported length " + length);
/*     */     }
/*     */     
/*     */     public byte[] getBytes()
/*     */     {
/* 108 */       return this.data;
/*     */     }
/*     */     
/*     */     public void load(int address, byte[] dest, int offset, int length)
/*     */     {
/* 113 */       System.arraycopy(this.data, address, dest, offset, length);
/*     */     }
/*     */     
/*     */     public void store(int address, byte[] src, int offset, int length) {
/* 117 */       System.arraycopy(src, offset, this.data, address, length);
/*     */     }
/*     */   }
/*     */   
/*     */   public static class Utils
/*     */   {
/*     */     public static void fill(Data data, int offset, int length, byte value) {
/* 124 */       for (int i = 0; i < length; i++)
/* 125 */         data.setByte(offset + i, value);
/*     */     }
/*     */     
/*     */     public static void storeClear(Data data, int address, byte[] bytes, int length) {
/* 129 */       if (bytes != null) {
/* 130 */         data.store(address, bytes, 0, bytes.length);
/* 131 */         address += bytes.length;
/* 132 */         length -= bytes.length;
/*     */       }
/* 134 */       for (int i = 0; i < length; i++) {
/* 135 */         data.setByte(address++, (byte)0);
/*     */       }
/*     */     }
/*     */     
/*     */     public static void set(Data data, int offset, int length, long value)
/*     */     {
/* 141 */       switch (length) {
/*     */       case 1: 
/* 143 */         data.setByte(offset, (byte)(int)value);
/* 144 */         break;
/*     */       case 2: 
/* 146 */         data.setWord(offset, (short)(int)value);
/* 147 */         break;
/*     */       case 4: 
/* 149 */         data.setDoubleWord(offset, (int)value);
/* 150 */         break;
/*     */       case 8: 
/* 152 */         data.setQuadWord(offset, value);
/* 153 */         break;
/* 154 */       case 3: case 5: case 6: case 7: default:  throw new IllegalArgumentException("Unsupported length " + length); }
/*     */     }
/*     */     
/*     */     public static long get(Data data, int offset, int length) {
/* 158 */       switch (length) {
/*     */       case 1: 
/* 160 */         return data.getByte(offset);
/*     */       case 2: 
/* 162 */         return data.getWord(offset);
/*     */       case 4: 
/* 164 */         return data.getDoubleWord(offset);
/*     */       case 8: 
/* 166 */         return data.getQuadWord(offset); }
/* 167 */       throw new IllegalArgumentException("Unsupported length " + length);
/*     */     }
/*     */     
/*     */     public static String getString(Data data, int str$)
/*     */     {
/* 172 */       StringBuilder b = new StringBuilder();
/*     */       for (;;) {
/* 174 */         int c = data.getByte(str$++) & 0xFF;
/* 175 */         if (c == 0) return b.toString();
/* 176 */         b.append((char)c);
/*     */       }
/*     */     }
/*     */     
/*     */     public static void setString(Data data, int str$, int limit, String value) {
/* 181 */       int len = value != null ? value.length() : 0;
/* 182 */       if (len > limit - 1) throw new ArrayIndexOutOfBoundsException(len);
/* 183 */       for (int i = 0; i < len; i++) {
/* 184 */         data.setByte(str$++, (byte)value.charAt(i));
/*     */       }
/* 186 */       data.setByte(str$, (byte)0);
/*     */     }
/*     */     
/* 189 */     public static void setBytes(Data data, int str$, int limit, byte[] value) { int len = value != null ? value.length : 0;
/* 190 */       if (len > limit) throw new ArrayIndexOutOfBoundsException(len);
/* 191 */       for (int i = 0; i < len; i++) {
/* 192 */         data.setByte(str$++, value[i]);
/*     */       }
/*     */     }
/*     */     
/*     */ 
/*     */     public static void memcopy(Data memory, int src, int len, int dst)
/*     */     {
/* 199 */       for (int i = 0; i < len; i++) {
/* 200 */         memory.setByte(dst++, memory.getByte(src++));
/*     */       }
/*     */     }
/*     */     
/*     */     public static void copy(Data src, int srcOffset, Data dest, int destOffset, int amount) {
/* 205 */       for (int i = 0; i < amount; i++) {
/* 206 */         dest.setByte(destOffset++, src.getByte(srcOffset++));
/*     */       }
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Joey\Downloads\codejar.jar!\com\emt\proteus\utils\Data.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */