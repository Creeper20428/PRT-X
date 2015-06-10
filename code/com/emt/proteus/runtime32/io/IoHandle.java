/*     */ package com.emt.proteus.runtime32.io;
/*     */ 
/*     */ import com.emt.proteus.runtime32.memory.MainMemory;
/*     */ import com.emt.proteus.utils.CharSource;
/*     */ import java.io.Closeable;
/*     */ import java.io.IOException;
/*     */ import java.math.RoundingMode;
/*     */ import java.text.DecimalFormat;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class IoHandle
/*     */   implements Closeable
/*     */ {
/*     */   private final IoSys sys;
/*     */   private final String name;
/*     */   private final String mode;
/*     */   private int descriptor;
/*     */   private final DecimalFormat doubleFormat;
/*     */   private final DecimalFormat intFormat;
/*     */   private int error;
/*     */   private int eof;
/*  50 */   private int width = 0;
/*  51 */   private int fill = 32;
/*  52 */   private int precision = 6;
/*     */   private int formatAddr;
/*  54 */   private int ungetc = -1;
/*  55 */   private boolean debug = false;
/*     */   
/*     */   public IoHandle(IoSys sys, String path, String mode, int descriptor)
/*     */   {
/*  59 */     this.sys = sys;
/*  60 */     this.name = path;
/*  61 */     this.mode = mode;
/*  62 */     this.descriptor = descriptor;
/*     */     
/*     */ 
/*     */ 
/*  66 */     this.doubleFormat = new DecimalFormat();
/*  67 */     this.doubleFormat.setMinimumFractionDigits(0);
/*  68 */     this.doubleFormat.setMaximumFractionDigits(6);
/*  69 */     this.doubleFormat.setGroupingUsed(false);
/*  70 */     this.doubleFormat.setRoundingMode(RoundingMode.HALF_UP);
/*  71 */     this.intFormat = new DecimalFormat();
/*  72 */     this.intFormat.setGroupingUsed(false);
/*  73 */     this.intFormat.setMaximumFractionDigits(0);
/*     */   }
/*     */   
/*     */   public String getMode()
/*     */   {
/*  78 */     return this.mode;
/*     */   }
/*     */   
/*     */   public void assign(int descriptor)
/*     */   {
/*  83 */     this.descriptor = descriptor;
/*     */   }
/*     */   
/*     */   public abstract void writeByte(int paramInt)
/*     */     throws IOException;
/*     */   
/*     */   public abstract int writeBytes(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
/*     */     throws IOException;
/*     */   
/*     */   public abstract int readBytes(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
/*     */     throws IOException;
/*     */   
/*     */   public abstract int readByte()
/*     */     throws IOException;
/*     */   
/*     */   public abstract void truncate(long paramLong)
/*     */     throws IOException;
/*     */   
/*     */   protected abstract void closeImpl() throws IOException;
/*     */   
/*     */   public int write(byte[] bytes) throws IOException
/*     */   {
/* 105 */     return writeBytes(bytes, 0, bytes.length);
/*     */   }
/*     */   
/*     */   public int getDescriptor() {
/* 109 */     return this.descriptor;
/*     */   }
/*     */   
/*     */ 
/*     */   public String getName()
/*     */   {
/* 115 */     return this.name;
/*     */   }
/*     */   
/*     */   public abstract int flush();
/*     */   
/*     */   public int fclose()
/*     */   {
/*     */     try {
/* 123 */       closeImpl();
/* 124 */       return 0;
/*     */     } catch (IOException e) {
/* 126 */       e.printStackTrace();
/*     */       
/* 128 */       return 1;
/*     */     } finally {
/* 130 */       this.sys.dispose(this);
/*     */     }
/*     */   }
/*     */   
/*     */   public final void close() throws IOException
/*     */   {
/*     */     try
/*     */     {
/* 138 */       closeImpl();
/*     */     } finally {
/* 140 */       this.sys.dispose(this);
/*     */     }
/*     */   }
/*     */   
/*     */   public long end()
/*     */   {
/* 146 */     return seek(0L, 2);
/*     */   }
/*     */   
/*     */   public long seek(long offset, int from) {
/*     */     try {
/* 151 */       return seekImpl(offset, from);
/*     */     }
/*     */     catch (IOException ioe) {}
/* 154 */     return -1L;
/*     */   }
/*     */   
/*     */ 
/*     */   public abstract long seekImpl(long paramLong, int paramInt)
/*     */     throws IOException;
/*     */   
/*     */   public int stat64(MainMemory mem, int stat64$, long currentTime)
/*     */   {
/* 163 */     boolean exists = exists();
/* 164 */     if (!exists) return 2;
/* 165 */     return Stat.stat64(mem, stat64$, getName(), getLastModified(currentTime), getLength(), isFile(), true, !isReadOnly(), false);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected boolean exists()
/*     */   {
/* 177 */     long length = getLength();
/* 178 */     return length > 0L;
/*     */   }
/*     */   
/*     */   private long getLastModified(long currentTime) {
/* 182 */     return currentTime;
/*     */   }
/*     */   
/*     */   protected boolean isReadOnly() {
/* 186 */     return false;
/*     */   }
/*     */   
/* 189 */   protected boolean isFile() { return true; }
/*     */   
/*     */   public abstract long getPosition();
/*     */   
/*     */   public int read(int addr, int amount, MainMemory mem)
/*     */   {
/*     */     try
/*     */     {
/* 197 */       amount = mem.read(addr, this, amount);
/* 198 */       this.eof = amount;
/* 199 */       return amount;
/*     */     } catch (IOException e) {
/* 201 */       e.printStackTrace();
/* 202 */       this.error = 1; }
/* 203 */     return -1;
/*     */   }
/*     */   
/*     */ 
/*     */   public abstract int getType();
/*     */   
/*     */   public abstract long getLength();
/*     */   
/*     */   public void saveChanges()
/*     */     throws IOException
/*     */   {}
/*     */   
/*     */   public CharSource getCharSource()
/*     */   {
/* 217 */     return new Source(this);
/*     */   }
/*     */   
/*     */   public int checkEvents(int eventMask) {
/* 221 */     return 0;
/*     */   }
/*     */   
/*     */   public static final class Source extends CharSource
/*     */   {
/*     */     private final IoHandle handle;
/*     */     
/*     */     public Source(IoHandle handle)
/*     */     {
/* 230 */       this.handle = handle;
/*     */     }
/*     */     
/*     */     protected int nextImpl() throws IOException
/*     */     {
/* 235 */       return this.handle.readByte();
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setWidth(int width)
/*     */   {
/* 607 */     this.width = width;
/*     */   }
/*     */   
/*     */   public void setFill(int fill) {
/* 611 */     this.fill = fill;
/*     */   }
/*     */   
/*     */   protected int loadFlags(MainMemory mem)
/*     */   {
/* 616 */     return mem.getDoubleWord(this.formatAddr);
/*     */   }
/*     */   
/*     */   public void storeFlags(int flags, MainMemory mem) {
/* 620 */     mem.setDoubleWord(this.formatAddr, flags);
/*     */   }
/*     */   
/*     */   public void setFlags(int flag_toset, MainMemory mem)
/*     */   {
/* 625 */     int old_value = loadFlags(mem);
/* 626 */     int newvalue = IosFlags.set(old_value, flag_toset);
/* 627 */     storeFlags(newvalue, mem);
/*     */   }
/*     */   
/*     */   public void resetFlags(int flag_toset, MainMemory mem) {
/* 631 */     int old_value = loadFlags(mem);
/* 632 */     int newvalue = IosFlags.unset(old_value, flag_toset);
/* 633 */     storeFlags(newvalue, mem);
/*     */   }
/*     */   
/*     */   public void setPrecision(int precision) {
/* 637 */     this.precision = precision;
/* 638 */     this.doubleFormat.setMaximumFractionDigits(precision);
/*     */   }
/*     */   
/*     */   public void setFormatAddress(int address) {
/* 642 */     this.formatAddr = address;
/*     */   }
/*     */   
/*     */   private boolean isSkipWs(MainMemory mem) {
/* 646 */     int flags = loadFlags(mem);
/* 647 */     return IosFlags.isSkipWs(flags);
/*     */   }
/*     */   
/*     */   private boolean isAlignRight(MainMemory mem) {
/* 651 */     int flags = loadFlags(mem);
/* 652 */     return IosFlags.isAlignRight(flags);
/*     */   }
/*     */ }


/* Location:              C:\Users\Joey\Downloads\codejar.jar!\com\emt\proteus\runtime32\io\IoHandle.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */