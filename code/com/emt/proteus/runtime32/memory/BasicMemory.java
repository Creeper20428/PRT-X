/*     */ package com.emt.proteus.runtime32.memory;
/*     */ 
/*     */ import java.nio.IntBuffer;
/*     */ 
/*     */ public class BasicMemory
/*     */   extends AbstractMemory
/*     */ {
/*     */   public static final int PAGE_SIZE = 4096;
/*     */   private boolean executable;
/*     */   private IntBuffer buffer;
/*     */   private Object[] annotations;
/*     */   
/*     */   public BasicMemory()
/*     */   {
/*  15 */     this.buffer = null;
/*  16 */     this.annotations = null;
/*  17 */     this.executable = true;
/*     */   }
/*     */   
/*     */   public BasicMemory(byte[] b)
/*     */   {
/*  22 */     this.annotations = null;
/*  23 */     this.executable = true;
/*  24 */     int[] ib = new int[b.length / 4];
/*  25 */     for (int i = 0; i < ib.length; i++)
/*  26 */       ib[i] = (b[(4 * i)] & 0xFF | (b[(4 * i + 1)] & 0xFF) << 8 | (b[(4 * i + 2)] & 0xFF) << 16 | (b[(4 * i + 3)] & 0xFF) << 24);
/*  27 */     this.buffer = IntBuffer.wrap(ib);
/*     */   }
/*     */   
/*     */   public long getSize()
/*     */   {
/*  32 */     return 4096L;
/*     */   }
/*     */   
/*     */   public byte[] getData()
/*     */   {
/*  37 */     if (this.buffer == null)
/*  38 */       return null;
/*  39 */     int[] ib = this.buffer.array();
/*  40 */     byte[] b = new byte[ib.length * 4];
/*  41 */     for (int i = 0; i < ib.length; i++)
/*     */     {
/*  43 */       b[(4 * i)] = ((byte)ib[i]);
/*  44 */       b[(4 * i + 1)] = ((byte)(ib[i] >> 8));
/*  45 */       b[(4 * i + 2)] = ((byte)(ib[i] >> 16));
/*  46 */       b[(4 * i + 3)] = ((byte)(ib[i] >> 24));
/*     */     }
/*  48 */     return b;
/*     */   }
/*     */   
/*     */   public void setExecutable(boolean exec)
/*     */   {
/*  53 */     this.executable = exec;
/*     */   }
/*     */   
/*     */   public boolean executable()
/*     */   {
/*  58 */     return this.executable;
/*     */   }
/*     */   
/*     */   public byte getByte(int offset)
/*     */   {
/*     */     try
/*     */     {
/*  65 */       return (byte)(this.buffer.get(offset / 4) >> offset % 4 * 8);
/*     */     }
/*     */     catch (NullPointerException e) {}
/*     */     
/*  69 */     return 0;
/*     */   }
/*     */   
/*     */ 
/*     */   public void setByte(int offset, byte data)
/*     */   {
/*     */     try
/*     */     {
/*  77 */       int t = this.buffer.get(offset / 4);
/*  78 */       t &= (255 << offset % 4 * 8 ^ 0xFFFFFFFF);
/*  79 */       t |= (data & 0xFF) << offset % 4 * 8;
/*  80 */       this.buffer.put(offset / 4, t);
/*     */     }
/*     */     catch (NullPointerException e)
/*     */     {
/*  84 */       if (data == 0) {
/*  85 */         return;
/*     */       }
/*  87 */       this.buffer = IntBuffer.allocate(1024);
/*  88 */       setByte(offset, data);
/*     */     }
/*     */   }
/*     */   
/*     */   public boolean hasAnnotations()
/*     */   {
/*  94 */     return this.annotations != null;
/*     */   }
/*     */   
/*     */   public Object getAnnotation(int offset)
/*     */   {
/*     */     try
/*     */     {
/* 101 */       return this.annotations[offset];
/*     */     }
/*     */     catch (NullPointerException e) {}
/*     */     
/* 105 */     return null;
/*     */   }
/*     */   
/*     */ 
/*     */   public void setAnnotation(int offset, Object value)
/*     */   {
/*     */     try
/*     */     {
/* 113 */       this.annotations[offset] = value;
/*     */     }
/*     */     catch (NullPointerException e)
/*     */     {
/* 117 */       if (value == null) {
/* 118 */         return;
/*     */       }
/* 120 */       this.annotations = new Object['က'];
/* 121 */       this.annotations[offset] = value;
/*     */     }
/*     */   }
/*     */   
/*     */   public void clearAnnotations()
/*     */   {
/* 127 */     this.annotations = null;
/*     */   }
/*     */   
/*     */   public void clear()
/*     */   {
/* 132 */     this.buffer = null;
/* 133 */     this.annotations = null;
/*     */   }
/*     */   
/*     */   protected final short getWordInBytes(int offset)
/*     */   {
/*     */     try
/*     */     {
/* 140 */       if (offset % 4 != 3)
/*     */       {
/* 142 */         int t = this.buffer.get(offset / 4);
/* 143 */         return (short)(t >> offset % 4 * 8);
/*     */       }
/* 145 */       return (short)(getByte(offset) & 0xFF | getByte(offset + 1) << 8);
/*     */     }
/*     */     catch (NullPointerException e) {}
/*     */     
/* 149 */     return 0;
/*     */   }
/*     */   
/*     */ 
/*     */   protected final int getDoubleWordInBytes(int offset)
/*     */   {
/*     */     try
/*     */     {
/* 157 */       if (offset % 4 == 0)
/* 158 */         return this.buffer.get(offset / 4);
/* 159 */       int t = this.buffer.get(offset / 4) >>> offset % 4 * 8;
/* 160 */       return t | this.buffer.get(offset / 4 + 1) << (4 - offset % 4) * 8;
/*     */     }
/*     */     catch (NullPointerException e) {}
/*     */     
/*     */ 
/* 165 */     return 0;
/*     */   }
/*     */   
/*     */ 
/*     */   protected final long getQuadWordInBytes(int offset)
/*     */   {
/*     */     try
/*     */     {
/* 173 */       if (offset % 4 == 0)
/* 174 */         return this.buffer.get(offset / 4) & 0xFFFFFFFF | this.buffer.get(offset / 4 + 1) << 32;
/* 175 */       return getDoubleWordInBytes(offset) & 0xFFFFFFFF | getDoubleWordInBytes(offset + 4) << 32;
/*     */     }
/*     */     catch (NullPointerException e) {}
/*     */     
/* 179 */     return 0L;
/*     */   }
/*     */   
/*     */ 
/*     */   protected final void setWordInBytes(int offset, short data)
/*     */   {
/*     */     try
/*     */     {
/* 187 */       setByte(offset, (byte)data);
/* 188 */       setByte(offset + 1, (byte)(data >> 8));
/*     */     }
/*     */     catch (NullPointerException e)
/*     */     {
/* 192 */       if (data == 0) {
/* 193 */         return;
/*     */       }
/* 195 */       this.buffer = IntBuffer.allocate(1024);
/* 196 */       setWordInBytes(offset, data);
/*     */     }
/*     */   }
/*     */   
/*     */   protected final void setDoubleWordInBytes(int offset, int data)
/*     */   {
/*     */     try
/*     */     {
/* 204 */       if (offset % 4 == 0)
/*     */       {
/* 206 */         this.buffer.put(offset / 4, data);
/* 207 */         return;
/*     */       }
/* 209 */       setWordInBytes(offset, (short)data);
/* 210 */       setWordInBytes(offset + 2, (short)(data >> 16));
/*     */     }
/*     */     catch (NullPointerException e)
/*     */     {
/* 214 */       if (data == 0) {
/* 215 */         return;
/*     */       }
/* 217 */       this.buffer = IntBuffer.allocate(1024);
/* 218 */       setDoubleWordInBytes(offset, data);
/*     */     }
/*     */   }
/*     */   
/*     */   protected final void setQuadWordInBytes(int offset, long data)
/*     */   {
/*     */     try
/*     */     {
/* 226 */       if (offset % 4 == 0)
/*     */       {
/* 228 */         this.buffer.put(offset / 4, (int)data);
/* 229 */         this.buffer.put(offset / 4 + 1, (int)(data >> 32));
/* 230 */         return;
/*     */       }
/* 232 */       setDoubleWordInBytes(offset, (int)data);
/* 233 */       setDoubleWordInBytes(offset + 4, (int)(data >> 32));
/*     */     }
/*     */     catch (NullPointerException e)
/*     */     {
/* 237 */       if (data == 0L) {
/* 238 */         return;
/*     */       }
/* 240 */       this.buffer = IntBuffer.allocate(1024);
/* 241 */       setQuadWordInBytes(offset, data);
/*     */     }
/*     */   }
/*     */   
/*     */   public String toString()
/*     */   {
/* 247 */     if (this.executable) {
/* 248 */       return "RWX";
/*     */     }
/* 250 */     return "RW";
/*     */   }
/*     */ }


/* Location:              C:\Users\Joey\Downloads\codejar.jar!\com\emt\proteus\runtime32\memory\BasicMemory.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */