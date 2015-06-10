/*     */ package com.emt.proteus.runtime32.memory;
/*     */ 
/*     */ import com.emt.proteus.utils.AddressMap;
/*     */ import com.emt.proteus.utils.Data;
/*     */ import java.util.concurrent.locks.ReentrantLock;
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
/*     */ public abstract class AbstractMemory
/*     */   implements Data
/*     */ {
/*     */   public static final int PAGE_SIZE = 4096;
/*     */   private AddressMap<Format> formats;
/*     */   private ReentrantLock memoryLock;
/*     */   
/*     */   public boolean writable()
/*     */   {
/*  26 */     return true;
/*     */   }
/*     */   
/*     */   public boolean executable()
/*     */   {
/*  31 */     return true;
/*     */   }
/*     */   
/*     */   public void clear()
/*     */   {
/*  36 */     for (int i = 0; i < 4096; i++)
/*  37 */       setByte(i, (byte)0);
/*  38 */     clearAnnotations();
/*     */   }
/*     */   
/*     */   public int setAnnotations(int offset, int length, Object value)
/*     */   {
/*  43 */     for (int i = 0; i < length; i++)
/*  44 */       setAnnotation(offset + i, value);
/*  45 */     return length;
/*     */   }
/*     */   
/*     */   public void clearAnnotations()
/*     */   {
/*  50 */     for (int i = 0; i < 4096; i++) {
/*  51 */       setAnnotation(i, null);
/*     */     }
/*     */   }
/*     */   
/*     */   public void clear(int start, int length) {
/*  56 */     int limit = start + length;
/*  57 */     if (limit > 4096) {
/*  58 */       throw new ArrayIndexOutOfBoundsException("Attempt to clear memory outside of memory bounds");
/*     */     }
/*  60 */     for (int i = start; i < limit; i++) {
/*  61 */       setByte(i, (byte)0);
/*     */     }
/*  63 */     clearAnnotations(start, length);
/*     */   }
/*     */   
/*     */   public void clearAnnotations(int start, int length)
/*     */   {
/*  68 */     int limit = start + length;
/*  69 */     if (limit > 4096) {
/*  70 */       throw new ArrayIndexOutOfBoundsException("Attempt to clear annotations outside of memory bounds");
/*     */     }
/*  72 */     for (int i = start; i < limit; i++) {
/*  73 */       setAnnotation(i, null);
/*     */     }
/*     */   }
/*     */   
/*     */   public void copyContentsIntoArray(int address, byte[] buffer, int off, int len) {
/*  78 */     for (int i = off; i < off + len; address++) {
/*  79 */       buffer[i] = getByte(address);i++;
/*     */     }
/*     */   }
/*     */   
/*     */   public void copyArrayIntoContents(int address, byte[] buffer, int off, int len) {
/*  84 */     for (int i = off; i < off + len; address++)
/*     */     {
/*  86 */       setByte(address, buffer[i]);
/*  87 */       setAnnotation(address, null);i++;
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
/*     */   protected short getWordInBytes(int offset)
/*     */   {
/*  99 */     int result = 0xFF & getByte(offset + 1);
/* 100 */     result <<= 8;
/* 101 */     result |= 0xFF & getByte(offset);
/* 102 */     return (short)result;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected int getDoubleWordInBytes(int offset)
/*     */   {
/* 113 */     int result = 0xFFFF & getWordInBytes(offset + 2);
/* 114 */     result <<= 16;
/* 115 */     result |= 0xFFFF & getWordInBytes(offset);
/* 116 */     return result;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected long getQuadWordInBytes(int offset)
/*     */   {
/* 127 */     long result = 0xFFFFFFFF & getDoubleWordInBytes(offset + 4);
/* 128 */     result <<= 32;
/* 129 */     result |= 0xFFFFFFFF & getDoubleWordInBytes(offset);
/* 130 */     return result;
/*     */   }
/*     */   
/*     */   public short getWord(int offset)
/*     */   {
/* 135 */     return getWordInBytes(offset);
/*     */   }
/*     */   
/*     */   public int getDoubleWord(int offset)
/*     */   {
/* 140 */     return getDoubleWordInBytes(offset);
/*     */   }
/*     */   
/*     */   public long getQuadWord(int offset)
/*     */   {
/* 145 */     return getQuadWordInBytes(offset);
/*     */   }
/*     */   
/*     */   public long getLowerDoubleQuadWord(int offset)
/*     */   {
/* 150 */     return getQuadWordInBytes(offset);
/*     */   }
/*     */   
/*     */   public long getUpperDoubleQuadWord(int offset)
/*     */   {
/* 155 */     return getQuadWordInBytes(offset + 8);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void setWordInBytes(int offset, short data)
/*     */   {
/* 166 */     setByte(offset, (byte)data);
/* 167 */     offset++;
/* 168 */     setByte(offset, (byte)(data >> 8));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void setDoubleWordInBytes(int offset, int data)
/*     */   {
/* 179 */     setByte(offset, (byte)data);
/* 180 */     offset++;
/* 181 */     data >>= 8;
/* 182 */     setByte(offset, (byte)data);
/* 183 */     offset++;
/* 184 */     data >>= 8;
/* 185 */     setByte(offset, (byte)data);
/* 186 */     offset++;
/* 187 */     data >>= 8;
/* 188 */     setByte(offset, (byte)data);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void setQuadWordInBytes(int offset, long data)
/*     */   {
/* 199 */     setDoubleWordInBytes(offset, (int)data);
/* 200 */     setDoubleWordInBytes(offset + 4, (int)(data >> 32));
/*     */   }
/*     */   
/*     */   public void setWord(int offset, short data)
/*     */   {
/* 205 */     setWordInBytes(offset, data);
/*     */   }
/*     */   
/*     */   public void setDoubleWord(int offset, int data)
/*     */   {
/* 210 */     setDoubleWordInBytes(offset, data);
/*     */   }
/*     */   
/*     */   public void setQuadWord(int offset, long data)
/*     */   {
/* 215 */     setQuadWordInBytes(offset, data);
/*     */   }
/*     */   
/*     */   public void setLowerDoubleQuadWord(int offset, long data)
/*     */   {
/* 220 */     setQuadWordInBytes(offset, data);
/*     */   }
/*     */   
/*     */   public void setUpperDoubleQuadWord(int offset, long data)
/*     */   {
/* 225 */     setQuadWordInBytes(offset + 8, data);
/*     */   }
/*     */   
/*     */   public String hexString(int offset, int length)
/*     */   {
/* 230 */     StringBuffer buf = new StringBuffer();
/* 231 */     for (int i = 0; i < length; i++)
/*     */     {
/* 233 */       byte b = getByte(offset + i);
/* 234 */       buf.append(Integer.toHexString(0xF & b >> 4));
/* 235 */       buf.append(Integer.toHexString(0xF & b));
/*     */     }
/* 237 */     return buf.toString();
/*     */   }
/*     */   
/* 240 */   public synchronized int readAtomicDoubleWord(int $addr) { return getDoubleWord($addr); }
/*     */   
/*     */   public synchronized void writeAtomicDoubleWord(int $addr, int value) {
/* 243 */     setDoubleWord($addr, value);
/*     */   }
/*     */   
/*     */   public synchronized boolean compareXCHGDoubleWord(int $addr, int value, int newValue) {
/* 247 */     if (getDoubleWord($addr) != value) return false;
/* 248 */     setDoubleWord($addr, value);
/* 249 */     return true;
/*     */   }
/*     */   
/*     */   public void load(int address, byte[] dest, int offset, int length)
/*     */   {
/* 254 */     copyContentsIntoArray(address, dest, offset, length);
/*     */   }
/*     */   
/*     */   public void store(int address, byte[] src, int offset, int length)
/*     */   {
/* 259 */     copyArrayIntoContents(address, src, offset, length);
/*     */   }
/*     */   
/*     */ 
/*     */   public void addFormat(long address, long end, int base, String fmt)
/*     */   {
/* 265 */     this.formats.put(address, end, new Format(base, fmt));
/*     */   }
/*     */   
/*     */   public String format(int address) {
/* 269 */     Format format = (Format)this.formats.get(address);
/* 270 */     return format.format(address);
/*     */   }
/*     */   
/*     */   public AbstractMemory()
/*     */   {
/* 262 */     this.formats = new AddressMap(new Format(0L, "0x%08X"));
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
/* 275 */     this.memoryLock = new ReentrantLock(); }
/*     */   
/* 277 */   public void unlock(int address) { this.memoryLock.unlock(); }
/*     */   
/*     */ 
/*     */ 
/* 281 */   public void lock(int address) { this.memoryLock.lock(); }
/*     */   
/*     */   public abstract long getSize();
/*     */   
/*     */   public abstract byte getByte(int paramInt);
/*     */   
/*     */   public abstract void setByte(int paramInt, byte paramByte);
/*     */   
/* 289 */   public static class Format { public Format(long base, String format) { this.base = base;
/* 290 */       this.format = format; }
/*     */     
/*     */     private final long base;
/*     */     private final String format;
/* 294 */     public String format(long address) { address &= 0xFFFFFFFF;
/* 295 */       return String.format(this.format, new Object[] { Long.valueOf(address), Long.valueOf(address - this.base) });
/*     */     }
/*     */     
/*     */     public String toString() {
/* 299 */       return String.format("%08X %s ", new Object[] { Long.valueOf(this.base), this.format });
/*     */     }
/*     */   }
/*     */   
/*     */   public abstract boolean hasAnnotations();
/*     */   
/*     */   public abstract Object getAnnotation(int paramInt);
/*     */   
/*     */   public abstract void setAnnotation(int paramInt, Object paramObject);
/*     */ }


/* Location:              C:\Users\Joey\Downloads\codejar.jar!\com\emt\proteus\runtime32\memory\AbstractMemory.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */