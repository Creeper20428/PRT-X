/*    */ package com.emt.proteus.runtime32.memory;
/*    */ 
/*    */ import java.io.InputStream;
/*    */ 
/*    */ public class MemoryInputStream extends InputStream
/*    */ {
/*    */   protected long position;
/*    */   protected AbstractMemory memory;
/*    */   
/*    */   public MemoryInputStream(AbstractMemory memory)
/*    */   {
/* 12 */     this(memory, 0L);
/*    */   }
/*    */   
/*    */   public MemoryInputStream(AbstractMemory memory, long initialPosition)
/*    */   {
/* 17 */     this.memory = memory;
/* 18 */     seekPosition(initialPosition);
/*    */   }
/*    */   
/*    */   public int available()
/*    */   {
/* 23 */     int remain = (int)(this.memory.getSize() - this.position);
/* 24 */     if (remain < 0)
/* 25 */       return Integer.MAX_VALUE;
/* 26 */     return remain;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */   public void close() {}
/*    */   
/*    */ 
/*    */ 
/*    */   public void mark(int readlimit) {}
/*    */   
/*    */ 
/*    */ 
/*    */   public boolean markSupported()
/*    */   {
/* 41 */     return false;
/*    */   }
/*    */   
/*    */   public int read()
/*    */   {
/* 46 */     return 0xFF & this.memory.getByte((int)this.position++);
/*    */   }
/*    */   
/*    */   public int read(byte[] b)
/*    */   {
/* 51 */     return read(b, 0, b.length);
/*    */   }
/*    */   
/*    */   public int read(byte[] b, int off, int len)
/*    */   {
/* 56 */     int remain = (int)(this.memory.getSize() - this.position);
/* 57 */     if (remain == 0) {
/* 58 */       return -1;
/*    */     }
/* 60 */     int toRead = Math.min(b.length - off, len);
/* 61 */     if (toRead <= 0) {
/* 62 */       return 0;
/*    */     }
/* 64 */     if (remain > 0)
/* 65 */       toRead = Math.min(remain, toRead);
/* 66 */     if (toRead <= 0) {
/* 67 */       return 0;
/*    */     }
/* 69 */     for (int i = 0; i < toRead; i++)
/* 70 */       b[(i + off)] = ((byte)read());
/* 71 */     return toRead;
/*    */   }
/*    */   
/*    */   public void reset()
/*    */   {
/* 76 */     this.position = 0L;
/*    */   }
/*    */   
/*    */   public long skip(long n)
/*    */   {
/* 81 */     long newPos = Math.min(this.memory.getSize(), this.position + n);
/* 82 */     long result = this.position - newPos;
/* 83 */     this.position = newPos;
/* 84 */     return result;
/*    */   }
/*    */   
/*    */   public void seekPosition(long pos)
/*    */   {
/* 89 */     this.position = Math.max(0L, Math.min(this.memory.getSize(), pos));
/*    */   }
/*    */ }


/* Location:              C:\Users\Joey\Downloads\codejar.jar!\com\emt\proteus\runtime32\memory\MemoryInputStream.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */