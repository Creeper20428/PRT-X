/*     */ package com.emt.proteus.runtime32.memory;
/*     */ 
/*     */ import java.nio.ByteBuffer;
/*     */ import java.nio.ByteOrder;
/*     */ 
/*     */ public class ReadOnlyMemory extends AbstractMemory
/*     */ {
/*     */   public static final int PAGE_SIZE = 4096;
/*     */   private boolean executable;
/*     */   private ByteBuffer buffer;
/*     */   private Object[] annotations;
/*     */   
/*     */   public ReadOnlyMemory(byte[] rawData)
/*     */   {
/*  15 */     if (rawData.length != 4096)
/*  16 */       throw new IllegalStateException("Raw data must be a full page");
/*  17 */     this.buffer = ByteBuffer.wrap(rawData);
/*  18 */     this.buffer.order(ByteOrder.LITTLE_ENDIAN);
/*     */     
/*  20 */     this.annotations = null;
/*  21 */     this.executable = true;
/*     */   }
/*     */   
/*     */   public long getSize()
/*     */   {
/*  26 */     return 4096L;
/*     */   }
/*     */   
/*     */   public byte[] getData()
/*     */   {
/*  31 */     return this.buffer.array();
/*     */   }
/*     */   
/*     */   public void setExecutable(boolean exec)
/*     */   {
/*  36 */     this.executable = exec;
/*     */   }
/*     */   
/*     */   public boolean executable()
/*     */   {
/*  41 */     return this.executable;
/*     */   }
/*     */   
/*     */   public byte getByte(int offset)
/*     */   {
/*  46 */     return this.buffer.get(offset);
/*     */   }
/*     */   
/*     */   public void setByte(int offset, byte data)
/*     */   {
/*  51 */     throw new IllegalStateException("Write to read only memory at: 0x" + Integer.toHexString(offset) + ", of " + data);
/*     */   }
/*     */   
/*     */   public boolean hasAnnotations()
/*     */   {
/*  56 */     return this.annotations != null;
/*     */   }
/*     */   
/*     */   public Object getAnnotation(int offset)
/*     */   {
/*     */     try
/*     */     {
/*  63 */       return this.annotations[offset];
/*     */     }
/*     */     catch (NullPointerException e) {}
/*     */     
/*  67 */     return null;
/*     */   }
/*     */   
/*     */ 
/*     */   public void setAnnotation(int offset, Object value)
/*     */   {
/*     */     try
/*     */     {
/*  75 */       this.annotations[offset] = value;
/*     */     }
/*     */     catch (NullPointerException e)
/*     */     {
/*  79 */       if (value == null) {
/*  80 */         return;
/*     */       }
/*  82 */       this.annotations = new Object['က'];
/*  83 */       this.annotations[offset] = value;
/*     */     }
/*     */   }
/*     */   
/*     */   public void clearAnnotations()
/*     */   {
/*  89 */     this.annotations = null;
/*     */   }
/*     */   
/*     */   public void clear()
/*     */   {
/*  94 */     this.annotations = null;
/*     */   }
/*     */   
/*     */   protected final short getWordInBytes(int offset)
/*     */   {
/*  99 */     return this.buffer.getShort(offset);
/*     */   }
/*     */   
/*     */   protected final int getDoubleWordInBytes(int offset)
/*     */   {
/* 104 */     return this.buffer.getInt(offset);
/*     */   }
/*     */   
/*     */   protected final long getQuadWordInBytes(int offset)
/*     */   {
/* 109 */     return this.buffer.getLong(offset);
/*     */   }
/*     */   
/*     */   protected final void setWordInBytes(int offset, short data)
/*     */   {
/* 114 */     throw new IllegalStateException("Write to read only memory");
/*     */   }
/*     */   
/*     */   protected final void setDoubleWordInBytes(int offset, int data)
/*     */   {
/* 119 */     throw new IllegalStateException("Write to read only memory");
/*     */   }
/*     */   
/*     */   protected final void setQuadWordInBytes(int offset, long data)
/*     */   {
/* 124 */     throw new IllegalStateException("Write to read only memory");
/*     */   }
/*     */   
/*     */   public String toString()
/*     */   {
/* 129 */     if (this.executable) {
/* 130 */       return "RX";
/*     */     }
/* 132 */     return "R";
/*     */   }
/*     */ }


/* Location:              C:\Users\Joey\Downloads\codejar.jar!\com\emt\proteus\runtime32\memory\ReadOnlyMemory.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */