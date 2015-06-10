/*     */ package com.emt.proteus.runtime32.io;
/*     */ 
/*     */ import com.emt.proteus.runtime32.api.IoSystem;
/*     */ import com.emt.proteus.utils.Utils;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class MemoryIoHandle
/*     */   extends IoHandle
/*     */ {
/*     */   public static final int GROWBY = 1048576;
/*     */   private byte[] contents;
/*     */   private int length;
/*     */   private int position;
/*     */   
/*     */   public MemoryIoHandle(IoSys sys, String name, String mode, int desc)
/*     */     throws IOException
/*     */   {
/*  46 */     super(sys, name, mode, desc);
/*  47 */     InputStream input = sys.getIoSystem().createInputstream(name);
/*  48 */     if (input != null) {
/*  49 */       readFully(input);
/*     */     } else {
/*  51 */       System.out.println("Couldn't open file");
/*  52 */       this.contents = new byte[1048576];
/*  53 */       this.length = 0;
/*  54 */       this.position = 0;
/*     */     }
/*     */   }
/*     */   
/*     */   public MemoryIoHandle(IoSys sys, String name, byte[] contents, String mode, int desc, int addr) throws IOException
/*     */   {
/*  60 */     super(sys, name, mode, desc);
/*  61 */     this.contents = (contents == null ? new byte[1048576] : contents);
/*  62 */     this.length = 0;
/*  63 */     this.position = 0;
/*     */   }
/*     */   
/*     */   public void writeByte(int i)
/*     */     throws IOException
/*     */   {
/*  69 */     int new_position = this.position + 1;
/*     */     try {
/*  71 */       this.contents[this.position] = ((byte)i);
/*     */     } catch (Exception ignored) {
/*  73 */       grow(new_position);
/*  74 */       this.contents[this.position] = ((byte)i);
/*     */     }
/*  76 */     update_position(new_position);
/*     */   }
/*     */   
/*     */   private void update_position(int new_position) {
/*  80 */     this.position = new_position;
/*  81 */     this.length = Math.max(new_position, this.length);
/*     */   }
/*     */   
/*     */   public int writeBytes(byte[] bytes, int offset, int length)
/*     */     throws IOException
/*     */   {
/*  87 */     int new_position = this.position + length;
/*     */     try {
/*  89 */       System.arraycopy(bytes, offset, this.contents, this.position, length);
/*     */     } catch (IndexOutOfBoundsException i) {
/*  91 */       if (new_position > this.contents.length) {
/*  92 */         grow(new_position);
/*  93 */         System.arraycopy(bytes, offset, this.contents, this.position, length);
/*     */       } else {
/*  95 */         throw new IOException("I haven't a clue!");
/*     */       }
/*     */     }
/*  98 */     update_position(new_position);
/*  99 */     return length;
/*     */   }
/*     */   
/*     */   private byte[] grow(int newlength) {
/* 103 */     byte[] tmp = new byte[Math.max(this.contents.length + 1048576, newlength)];
/* 104 */     System.arraycopy(this.contents, 0, tmp, 0, this.contents.length);
/* 105 */     this.contents = tmp;
/* 106 */     return this.contents;
/*     */   }
/*     */   
/*     */ 
/*     */   public int readBytes(byte[] bytes, int offset, int length)
/*     */     throws IOException
/*     */   {
/* 113 */     int available = available();
/* 114 */     int amount = Math.min(length, available);
/* 115 */     if (amount > 0) {
/* 116 */       System.arraycopy(this.contents, this.position, bytes, offset, amount);
/* 117 */       update_position(this.position + amount);
/*     */     }
/* 119 */     return amount;
/*     */   }
/*     */   
/*     */   public void truncate(long newsize)
/*     */     throws IOException
/*     */   {
/* 125 */     this.length = ((int)newsize);
/* 126 */     this.position = ((int)newsize);
/*     */   }
/*     */   
/*     */   public int readByte() throws IOException
/*     */   {
/* 131 */     if (available() > 0) {
/* 132 */       return this.contents[(this.position++)];
/*     */     }
/* 134 */     return -1;
/*     */   }
/*     */   
/*     */   private int available() {
/* 138 */     return this.position >= this.length ? -1 : this.length - this.position;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public int flush()
/*     */   {
/* 145 */     return 0;
/*     */   }
/*     */   
/*     */ 
/*     */   public void closeImpl()
/*     */     throws IOException
/*     */   {}
/*     */   
/*     */ 
/*     */   public long seekImpl(long offset, int from)
/*     */     throws IOException
/*     */   {
/* 157 */     long dest = 0L;
/* 158 */     switch (from) {
/*     */     case 1: 
/* 160 */       dest = this.position + offset;
/* 161 */       break;
/*     */     case 2: 
/* 163 */       dest = this.length + offset;
/* 164 */       break;
/*     */     case 0: 
/* 166 */       dest = offset;
/*     */     }
/*     */     
/* 169 */     update_position((int)dest);
/* 170 */     return dest;
/*     */   }
/*     */   
/*     */   private byte[] readFully(InputStream inputStream)
/*     */     throws IOException
/*     */   {
/* 176 */     byte[] bytes = Utils.getBytes(inputStream);
/* 177 */     this.contents = bytes;
/* 178 */     this.position = 0;
/* 179 */     this.length = this.contents.length;
/* 180 */     return this.contents;
/*     */   }
/*     */   
/*     */   public int getType()
/*     */   {
/* 185 */     return 4;
/*     */   }
/*     */   
/*     */   public long getLength()
/*     */   {
/* 190 */     return this.length;
/*     */   }
/*     */   
/*     */   public long getPosition() {
/* 194 */     return this.position;
/*     */   }
/*     */   
/*     */   public void setContents(byte[] contents) {
/* 198 */     this.contents = contents;
/* 199 */     this.position = 0;
/* 200 */     this.length = contents.length;
/*     */   }
/*     */   
/*     */   public String getStringContents() {
/* 204 */     return Utils.toNativeString(this.contents, 0, this.position);
/*     */   }
/*     */   
/* 207 */   public byte[] getContents() { return this.contents; }
/*     */ }


/* Location:              C:\Users\Joey\Downloads\codejar.jar!\com\emt\proteus\runtime32\io\MemoryIoHandle.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */