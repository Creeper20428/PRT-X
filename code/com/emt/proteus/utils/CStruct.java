/*     */ package com.emt.proteus.utils;
/*     */ 
/*     */ import java.io.PrintStream;
/*     */ import java.lang.reflect.Field;
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class CStruct
/*     */ {
/*     */   private int length;
/*     */   private Data data;
/*     */   private int base;
/*  13 */   private int align = 4;
/*     */   
/*     */   protected CStruct(int length)
/*     */   {
/*  17 */     this(0, length, new Data.Byte(length));
/*     */   }
/*     */   
/*     */   protected CStruct(CField last) {
/*  21 */     this(0, last.nextOffset(), new Data.Byte(last.nextOffset()));
/*     */   }
/*     */   
/*     */   protected CStruct(CField last, Data data) {
/*  25 */     this(0, last.nextOffset(), data);
/*     */   }
/*     */   
/*     */   protected CStruct(int base, CField last, Data data) {
/*  29 */     this(base, last.nextOffset(), data);
/*     */   }
/*     */   
/*     */   protected CStruct(int base, int length, Data data) {
/*  33 */     this.base = base;
/*  34 */     this.length = length;
/*  35 */     this.data = data;
/*     */   }
/*     */   
/*     */   protected void setSize(int size)
/*     */   {
/*  40 */     this.length = size;
/*     */   }
/*     */   
/*     */   public int getAlign() {
/*  44 */     return this.align;
/*     */   }
/*     */   
/*     */   public void setAlign(int align) {
/*  48 */     this.align = align;
/*     */   }
/*     */   
/*     */   public byte[] asBytes() {
/*     */     try {
/*  53 */       Data.Byte dataBytes = (Data.Byte)this.data;
/*  54 */       return dataBytes.getBytes();
/*     */     } catch (ClassCastException cce) {
/*  56 */       byte[] b = new byte[this.length];
/*  57 */       this.data.load(this.base, b, 0, this.length);
/*  58 */       return b;
/*     */     }
/*     */   }
/*     */   
/*     */   public void clear() {
/*  63 */     Data.Utils.fill(this.data, this.base, getSize(), (byte)0);
/*     */   }
/*     */   
/*     */   public int next()
/*     */   {
/*  68 */     this.base += this.length;
/*  69 */     return this.base;
/*     */   }
/*     */   
/*     */   public int previous() {
/*  73 */     this.base -= this.length;
/*  74 */     return this.base;
/*     */   }
/*     */   
/*     */   public Data getData() {
/*  78 */     return this.data;
/*     */   }
/*     */   
/*     */   public int addressOf() {
/*  82 */     return this.base;
/*     */   }
/*     */   
/*     */   public void assign(CStruct other) {
/*  86 */     assign(other.base, other.data);
/*     */   }
/*     */   
/*     */   public void assign(int address) {
/*  90 */     assign(address, this.data);
/*     */   }
/*     */   
/*     */   public void assign(int address, Data data) {
/*  94 */     this.base = address;
/*  95 */     this.data = data;
/*     */   }
/*     */   
/*     */   public int getSize() {
/*  99 */     return this.length;
/*     */   }
/*     */   
/*     */   public int address(CField field) {
/* 103 */     return this.base + field.offset;
/*     */   }
/*     */   
/*     */   public CStruct set(CField field, long value) {
/* 107 */     field.set(this, value);
/* 108 */     return this;
/*     */   }
/*     */   
/* 111 */   public CStruct set(CField field, CStruct pointsTo) { field.setPointer(this, pointsTo);
/* 112 */     return this;
/*     */   }
/*     */   
/* 115 */   public CStruct string(CField field, String text) { field.setString(this, text);
/* 116 */     return this;
/*     */   }
/*     */   
/* 119 */   public CStruct bytes(CField field, byte[] data) { field.setBytes(this, data);
/* 120 */     return this;
/*     */   }
/*     */   
/* 123 */   public void writeTo(Data memory, int offset) { Data.Utils.copy(this.data, this.base, memory, offset, getSize()); }
/*     */   
/*     */ 
/*     */   public CStruct copy(CStruct src)
/*     */   {
/* 128 */     int limit = Math.min(getSize(), src.getSize());
/* 129 */     Data.Utils.copy(src.data, src.base, this.data, this.base, limit);
/* 130 */     return this;
/*     */   }
/*     */   
/*     */   public static class CField {
/*     */     private final int offset;
/*     */     private final String name;
/*     */     private final int size;
/*     */     private final int typeWidth;
/*     */     
/*     */     public CField(int offset, String name, int typeWidth) {
/* 140 */       this(offset, name, 1, typeWidth);
/*     */     }
/*     */     
/*     */     public CField(int offset, String name, int length, int typeWidth) {
/* 144 */       this.offset = offset;
/* 145 */       this.name = name;
/* 146 */       this.size = (length * typeWidth);
/* 147 */       this.typeWidth = typeWidth;
/*     */     }
/*     */     
/*     */     public CField(CField previous, String name, int typeWidth) {
/* 151 */       this(previous, name, 1, typeWidth);
/*     */     }
/*     */     
/*     */     public CField(CField previous, String name, int length, int typeWidth) {
/* 155 */       this.offset = nextOffset(previous, typeWidth);
/* 156 */       this.name = name;
/* 157 */       this.size = (length * typeWidth);
/* 158 */       this.typeWidth = typeWidth;
/*     */     }
/*     */     
/*     */     public byte byteValue(CStruct obj) {
/* 162 */       return obj.data.getByte(obj.base + this.offset);
/*     */     }
/*     */     
/*     */     public short shortValue(CStruct obj) {
/* 166 */       return obj.data.getWord(obj.base + this.offset);
/*     */     }
/*     */     
/*     */     public int intValue(CStruct obj) {
/* 170 */       return obj.data.getDoubleWord(obj.base + this.offset);
/*     */     }
/*     */     
/*     */     public long longValue(CStruct obj) {
/* 174 */       return obj.data.getQuadWord(obj.base + this.offset);
/*     */     }
/*     */     
/*     */     public String stringValue(CStruct obj) {
/* 178 */       return Data.Utils.getString(obj.data, obj.base + this.offset);
/*     */     }
/*     */     
/*     */     public long get(CStruct obj) {
/* 182 */       return Data.Utils.get(obj.data, obj.base + this.offset, this.size);
/*     */     }
/*     */     
/*     */     public long get(int base, Data data) {
/* 186 */       return Data.Utils.get(data, base + this.offset, this.size);
/*     */     }
/*     */     
/*     */     public void set(CStruct obj, long value)
/*     */     {
/* 191 */       Data.Utils.set(obj.data, obj.base + this.offset, this.size, value);
/*     */     }
/*     */     
/* 194 */     public void setPointer(CStruct obj, CStruct struct) { Data.Utils.set(obj.data, obj.base + this.offset, this.size, struct != null ? struct.base : 0L); }
/*     */     
/*     */     public long get(CStruct obj, int index)
/*     */     {
/* 198 */       int offset = fieldIndex(obj, index);
/* 199 */       return Data.Utils.get(obj.data, offset, this.typeWidth);
/*     */     }
/*     */     
/*     */     public void set(CStruct obj, int index, long value) {
/* 203 */       int offset = fieldIndex(obj, index);
/* 204 */       Data.Utils.set(obj.data, offset, this.typeWidth, value);
/*     */     }
/*     */     
/*     */     public void setString(CStruct obj, String value) {
/* 208 */       if ((this.typeWidth != 1) || ((value != null) && (value.length() + 1 > this.size)))
/* 209 */         throw new IllegalArgumentException("String too long");
/* 210 */       Data.Utils.setString(obj.data, obj.base + this.offset, this.size, value);
/*     */     }
/*     */     
/* 213 */     public void setBytes(CStruct obj, byte[] value) { if ((this.typeWidth != 1) || ((value != null) && (value.length > this.size)))
/* 214 */         throw new IllegalArgumentException("data too long");
/* 215 */       Data.Utils.setBytes(obj.data, obj.base + this.offset, this.size, value);
/*     */     }
/*     */     
/*     */     private int fieldIndex(CStruct obj, int index) {
/* 219 */       int offset = this.typeWidth * index;
/* 220 */       if (this.size - this.typeWidth < offset)
/* 221 */         throw new ArrayIndexOutOfBoundsException(index);
/* 222 */       offset += obj.base + this.offset;
/* 223 */       return offset;
/*     */     }
/*     */     
/*     */     public int nextOffset() {
/* 227 */       return this.offset + this.size;
/*     */     }
/*     */     
/*     */     public static int nextOffset(CField previous, int typeWidth) {
/* 231 */       int align = Math.min(typeWidth, 4);
/* 232 */       int align_mask = align - 1;
/* 233 */       int offset = previous.nextOffset();
/* 234 */       int aligned = offset;
/* 235 */       if ((offset & align_mask) != 0) {
/* 236 */         aligned = offset + align & (align_mask ^ 0xFFFFFFFF);
/* 237 */         System.err.printf("ALIGNING %X -> %X \n", new Object[] { Integer.valueOf(offset), Integer.valueOf(aligned) });
/*     */       }
/* 239 */       return aligned;
/*     */     }
/*     */     
/*     */     public String toString() {
/* 243 */       if (this.size == this.typeWidth) {
/* 244 */         return String.format("0x%3X : [%3d] %s", new Object[] { Integer.valueOf(this.offset), Integer.valueOf(this.size), this.name });
/*     */       }
/* 246 */       return String.format("0x%3X : [%dx%d] %s", new Object[] { Integer.valueOf(this.offset), Integer.valueOf(this.size / this.typeWidth), Integer.valueOf(this.typeWidth), this.name });
/*     */     }
/*     */   }
/*     */   
/*     */   public static CField _long(int offset, String name)
/*     */   {
/* 252 */     return new CField(offset, name, 8);
/*     */   }
/*     */   
/*     */   public static CField _long(CField previous, String name) {
/* 256 */     return new CField(previous, name, 8);
/*     */   }
/*     */   
/*     */   public static CField _integer(int offset, String name) {
/* 260 */     return new CField(offset, name, 4);
/*     */   }
/*     */   
/*     */   public static CField _integer(CField previous, String name) {
/* 264 */     return new CField(previous, name, 4);
/*     */   }
/*     */   
/*     */   public static CField structArray(CField previous, String name, int structSize, int length) {
/* 268 */     return new CField(previous, name, length, structSize);
/*     */   }
/*     */   
/*     */   public static CField structArray(int offset, String name, int structSize, int length) {
/* 272 */     return new CField(offset, name, length, structSize);
/*     */   }
/*     */   
/*     */   public static CField structArray(CField previous, String name, CStruct struct, int length) {
/* 276 */     return new CField(previous, name, length, struct.getSize());
/*     */   }
/*     */   
/*     */   public static CField _pointer(int offset, String name) {
/* 280 */     return new CField(offset, name, 4);
/*     */   }
/*     */   
/*     */   public static CField _pointer(CField previous, String name) {
/* 284 */     return new CField(previous, name, 4);
/*     */   }
/*     */   
/*     */   public static CField _pointerArray(CField previous, String name, int length) {
/* 288 */     return new CField(previous, name, length, 4);
/*     */   }
/*     */   
/*     */   public static CField _cstring(CField previous, String name, int length) {
/* 292 */     return new CField(previous, name, length, 1);
/*     */   }
/*     */   
/*     */   public static CField _cstring(int offset, String name, int length) {
/* 296 */     return new CField(offset, name, length, 1);
/*     */   }
/*     */   
/*     */   public static CField _short(int offset, String name) {
/* 300 */     return new CField(offset, name, 2);
/*     */   }
/*     */   
/*     */   public static CField _short(CField previous, String name) {
/* 304 */     return new CField(previous, name, 2);
/*     */   }
/*     */   
/*     */   public static CField _blob(CField previous, String name, int size) {
/* 308 */     return new CField(previous, name, size, 1);
/*     */   }
/*     */   
/*     */   public static CField _byte(int offset, String name) {
/* 312 */     return new CField(offset, name, 1);
/*     */   }
/*     */   
/*     */   public static CField _byte(CField previous, String name) {
/* 316 */     return new CField(previous, name, 1);
/*     */   }
/*     */   
/*     */   public static String structure(Class c) {
/* 320 */     StringBuilder b = new StringBuilder(c.getSimpleName()).append("\n");
/*     */     try {
/* 322 */       int len = 0;
/* 323 */       Field[] declaredFields = c.getDeclaredFields();
/* 324 */       for (int i = 0; i < declaredFields.length; i++) {
/* 325 */         Field field = declaredFields[i];
/* 326 */         if (field.getType().equals(CField.class)) {
/* 327 */           CField value = (CField)field.get(null);
/* 328 */           b.append(value).append("\n");
/* 329 */           len = value.nextOffset();
/*     */         }
/*     */       }
/* 332 */       b.append("sizeof = ").append(Integer.toHexString(len)).append("\n");
/*     */     }
/*     */     catch (IllegalAccessException e) {}
/* 335 */     return b.toString();
/*     */   }
/*     */ }


/* Location:              C:\Users\Joey\Downloads\codejar.jar!\com\emt\proteus\utils\CStruct.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */