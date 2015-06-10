/*     */ package com.emt.proteus.runtime32.memory;
/*     */ 
/*     */ import com.emt.proteus.runtime32.Option;
/*     */ import com.emt.proteus.runtime32.Option.Opt;
/*     */ import com.emt.proteus.runtime32.Processor;
/*     */ import com.emt.proteus.runtime32.ThreadContext;
/*     */ import com.emt.proteus.runtime32.Trace;
/*     */ import com.emt.proteus.runtime32.io.IoHandle;
/*     */ import java.io.DataInput;
/*     */ import java.io.DataOutput;
/*     */ import java.io.IOException;
/*     */ import java.util.Set;
/*     */ 
/*     */ public class MainMemory extends AbstractMemory
/*     */ {
/*     */   public static final int PAGE_MASK = 4095;
/*     */   public static final int INDEX_MASK = -4096;
/*     */   public static final int INDEX_SHIFT = 12;
/*     */   public static final int INDEX_SIZE = 1048576;
/*     */   private AbstractMemory[] pages;
/*     */   private int breakPageAddr;
/*  22 */   private static final boolean WATCH = Option.watch.isSet();
/*  23 */   private static final int WATCH_ADDR_START = Option.watch.intValue(0, 16);
/*  24 */   private static final int WATCH_RANGE_END = WATCH_ADDR_START + Option.watch_len.intValue(4, 16);
/*     */   public boolean[] dirtyPages;
/*     */   public boolean[] empty;
/*  27 */   public final Set<Integer> dirtyPageSet = new java.util.HashSet();
/*     */   public static final int MREMAP_MAYMOVE = 1;
/*     */   public static final int MREMAP_FIXED = 2;
/*     */   
/*  31 */   public MainMemory() { this.pages = new AbstractMemory[1048576];
/*  32 */     if (Trace.compareToInt)
/*     */     {
/*  34 */       this.dirtyPages = new boolean[1048576];
/*  35 */       this.empty = new boolean[1048576];
/*     */     }
/*     */   }
/*     */   
/*     */   public void clearDirtyPages()
/*     */   {
/*  41 */     System.arraycopy(this.empty, 0, this.dirtyPages, 0, this.dirtyPages.length);
/*     */   }
/*     */   
/*     */   public Set<Integer> getDirtyPageSet()
/*     */   {
/*  46 */     return this.dirtyPageSet;
/*     */   }
/*     */   
/*     */   public void saveState(DataOutput out) throws IOException
/*     */   {
/*  51 */     out.writeInt(this.breakPageAddr);
/*  52 */     int nullCount = 0;
/*  53 */     for (int i = 0; i < this.pages.length; i++)
/*     */     {
/*  55 */       if (this.pages[i] == null)
/*     */       {
/*  57 */         nullCount++;
/*     */       }
/*     */       else {
/*  60 */         if (nullCount > 0)
/*     */         {
/*  62 */           out.writeInt(1);
/*  63 */           out.writeInt(nullCount);
/*  64 */           nullCount = 0;
/*     */         }
/*  66 */         if ((this.pages[i] instanceof BasicMemory))
/*     */         {
/*  68 */           byte[] data = ((BasicMemory)this.pages[i]).getData();
/*  69 */           if (data == null) {
/*  70 */             out.writeInt(3);
/*     */           }
/*     */           else {
/*  73 */             out.writeInt(2);
/*  74 */             out.write(data);
/*     */           }
/*  76 */           out.writeBoolean(((BasicMemory)this.pages[i]).executable());
/*  77 */         } else if ((this.pages[i] instanceof ReadOnlyMemory))
/*     */         {
/*  79 */           out.writeInt(4);
/*  80 */           out.write(((ReadOnlyMemory)this.pages[i]).getData());
/*  81 */           out.writeBoolean(((ReadOnlyMemory)this.pages[i]).executable());
/*     */         } else {
/*  83 */           throw new IllegalStateException("Unknown memory page type");
/*     */         }
/*     */       } }
/*  86 */     out.writeInt(8);
/*     */   }
/*     */   
/*     */   public void loadState(DataInput in) throws IOException
/*     */   {
/*  91 */     this.breakPageAddr = in.readInt();
/*  92 */     int type = 0;
/*  93 */     int index = 0;
/*     */     
/*  95 */     while ((type = in.readInt()) != 0)
/*     */     {
/*  97 */       switch (type)
/*     */       {
/*     */       case 1: 
/* 100 */         index += in.readInt();
/* 101 */         break;
/*     */       case 2: 
/* 103 */         byte[] buf = new byte['က'];
/* 104 */         in.readFully(buf);
/* 105 */         BasicMemory bm = new BasicMemory(buf);
/* 106 */         bm.setExecutable(in.readBoolean());
/* 107 */         this.pages[(index++)] = bm;
/* 108 */         break;
/*     */       case 3: 
/* 110 */         BasicMemory bme = new BasicMemory();
/* 111 */         bme.setExecutable(in.readBoolean());
/* 112 */         this.pages[(index++)] = bme;
/* 113 */         break;
/*     */       case 4: 
/* 115 */         byte[] bufr = new byte['က'];
/* 116 */         in.readFully(bufr);
/* 117 */         ReadOnlyMemory rom = new ReadOnlyMemory(bufr);
/* 118 */         rom.setExecutable(in.readBoolean());
/* 119 */         this.pages[(index++)] = rom;
/* 120 */         break;
/*     */       case 8: 
/* 122 */         return;
/*     */       case 5: case 6: case 7: default: 
/* 124 */         throw new IllegalStateException("Error parsing memory snapshot");
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public long getSize()
/*     */   {
/* 131 */     return 4294967296L;
/*     */   }
/*     */   
/*     */   public void setBreakAddress(int addr)
/*     */   {
/* 136 */     this.breakPageAddr = addr;
/*     */   }
/*     */   
/*     */   public int allocateBlock(int start, int len)
/*     */   {
/* 141 */     if (start < 1082142720) {
/* 142 */       start = 1082142720;
/*     */     }
/* 144 */     int reqPages = len >> 12;
/* 145 */     int pageIndex = start >> 12;
/* 146 */     int startIndex = pageIndex;
/* 147 */     int numPages = 1;
/*     */     for (;;)
/*     */     {
/* 150 */       if (this.pages[startIndex] != null) {
/* 151 */         startIndex++;
/* 152 */       } else if (numPages != reqPages)
/*     */       {
/* 154 */         pageIndex = startIndex;
/* 155 */         while (numPages < reqPages)
/*     */         {
/* 157 */           if (this.pages[(pageIndex++)] == null) {
/* 158 */             numPages++;
/*     */           }
/*     */           else {
/* 161 */             startIndex = pageIndex;
/* 162 */             numPages = 1;
/*     */           }
/*     */         }
/*     */         
/* 166 */         if (numPages == reqPages)
/*     */           break;
/*     */       }
/*     */     }
/* 170 */     for (int i = startIndex; i < startIndex + reqPages; i++)
/* 171 */       this.pages[i] = new BasicMemory();
/* 172 */     return startIndex << 12;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private void movePages(int oldAddr, int len, int newAddr)
/*     */   {
/* 180 */     int reqPages = len >> 12;
/* 181 */     int pageIndex = oldAddr >> 12;
/* 182 */     int newIndex = newAddr >> 12;
/* 183 */     for (int i = 0; i < reqPages; i++)
/*     */     {
/* 185 */       this.pages[(newIndex + i)] = this.pages[(pageIndex + i)];
/* 186 */       this.pages[(pageIndex + i)] = null;
/*     */     }
/*     */   }
/*     */   
/*     */   public void unmap(int addr, int len)
/*     */   {
/* 192 */     int reqPages = len + 4095 >> 12;
/* 193 */     int pageIndex = addr >> 12;
/* 194 */     for (int i = 0; i < reqPages; i++)
/*     */     {
/* 196 */       this.pages[(pageIndex + i)] = null;
/*     */     }
/*     */   }
/*     */   
/*     */   public int remap(int oldAddr, int oldSize, int newSize, int flags, int hintAddr)
/*     */   {
/* 202 */     if ((flags & 0x2) != 0)
/*     */     {
/* 204 */       throw new IllegalStateException("Unimplemented MRemap mode");
/*     */     }
/* 206 */     if ((flags & 0x1) != 0)
/*     */     {
/* 208 */       if (hintAddr == 0)
/* 209 */         hintAddr = oldAddr + oldSize;
/* 210 */       int newAddr = allocateBlock(hintAddr, newSize);
/* 211 */       movePages(oldAddr, oldSize, newAddr);
/* 212 */       return newAddr;
/*     */     }
/*     */     
/*     */ 
/* 216 */     throw new IllegalStateException("Unimplemented MRemap mode");
/*     */   }
/*     */   
/*     */ 
/*     */   private long bytes;
/*     */   
/*     */   private long count;
/*     */   
/*     */   public int allocate(int nbytes)
/*     */   {
/* 226 */     int start = (this.breakPageAddr & 0xF000) + 4096;
/*     */     
/* 228 */     int allocated = (nbytes & 0xFFF) == 0 ? nbytes : nbytes + 4096 & 0xF000;
/*     */     
/*     */ 
/* 231 */     allocateUpTo(start + allocated);
/* 232 */     return start;
/*     */   }
/*     */   
/*     */   public int allocateUpTo(int newbrk)
/*     */   {
/* 237 */     if (newbrk == 0)
/* 238 */       return this.breakPageAddr;
/* 239 */     if (newbrk < this.breakPageAddr)
/*     */     {
/* 241 */       for (int addr = this.breakPageAddr & 0xF000; addr >= newbrk; addr -= 4096)
/* 242 */         this.pages[(addr >>> 12)] = null;
/* 243 */       return newbrk;
/*     */     }
/* 245 */     for (int addr = this.breakPageAddr & 0xF000; addr < newbrk; addr += 4096)
/*     */     {
/* 247 */       if (getPageFor(addr) == null) {
/* 248 */         allocatePage(addr);
/*     */       }
/*     */     }
/* 251 */     this.breakPageAddr = newbrk;
/* 252 */     return newbrk;
/*     */   }
/*     */   
/*     */   public AbstractMemory getPageFor(int address)
/*     */   {
/* 257 */     return this.pages[(address >>> 12)];
/*     */   }
/*     */   
/*     */   public AbstractMemory allocatePage(int address)
/*     */   {
/* 262 */     BasicMemory m = new BasicMemory();
/* 263 */     this.pages[(address >>> 12)] = m;
/* 264 */     return m;
/*     */   }
/*     */   
/*     */   public final void setPageReadOnly(int address)
/*     */   {
/* 269 */     AbstractMemory m = this.pages[(address >>> 12)];
/* 270 */     if ((m instanceof BasicMemory))
/*     */     {
/* 272 */       byte[] raw = ((BasicMemory)m).getData();
/* 273 */       if (raw != null) {
/* 274 */         this.pages[(address >>> 12)] = new ReadOnlyMemory(raw);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public final void setPageExecutePermission(int address, boolean exec) {
/* 280 */     AbstractMemory m = this.pages[(address >>> 12)];
/* 281 */     if ((m instanceof BasicMemory)) {
/* 282 */       ((BasicMemory)m).setExecutable(exec);
/* 283 */     } else if ((m instanceof ReadOnlyMemory)) {
/* 284 */       ((ReadOnlyMemory)m).setExecutable(exec);
/*     */     }
/*     */   }
/*     */   
/*     */   public java.io.InputStream getInputStream(int address) {
/* 289 */     return new MemoryInputStream(this, address);
/*     */   }
/*     */   
/*     */   public com.emt.proteus.decoder.X86Opcode getX86Opcode(int address) throws IOException
/*     */   {
/* 294 */     java.io.InputStream in = getInputStream(address);
/*     */     
/* 296 */     com.emt.proteus.decoder.X86Opcode decoded = com.emt.proteus.decoder.Disassembler.getDisassembler().disassemble(in);
/* 297 */     return decoded;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public byte getByte(int address)
/*     */   {
/* 304 */     watch_read(address);
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     try
/*     */     {
/* 317 */       return getPageFor(address).getByte(address & 0xFFF);
/*     */     }
/*     */     catch (NullPointerException e) {}
/*     */     
/* 321 */     return 0;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public short getWord(int address)
/*     */   {
/* 329 */     watch_read(address);
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */     try
/*     */     {
/* 336 */       return getPageFor(address).getWord(address & 0xFFF);
/*     */     }
/*     */     catch (NullPointerException e)
/*     */     {
/* 340 */       return 0;
/*     */     }
/*     */     catch (Exception e) {}
/*     */     
/* 344 */     return getWordInBytes(address);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public int getDoubleWord(int address)
/*     */   {
/* 354 */     watch_read(address);
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     try
/*     */     {
/* 371 */       return getPageFor(address).getDoubleWord(address & 0xFFF);
/*     */     }
/*     */     catch (NullPointerException e)
/*     */     {
/* 375 */       return 0;
/*     */     }
/*     */     catch (Exception e) {}
/*     */     
/* 379 */     return getDoubleWordInBytes(address);
/*     */   }
/*     */   
/*     */ 
/*     */   public long getQuadWord(int address)
/*     */   {
/* 385 */     watch_read(address);
/*     */     try
/*     */     {
/* 388 */       return getPageFor(address).getQuadWord(address & 0xFFF);
/*     */     }
/*     */     catch (NullPointerException e)
/*     */     {
/* 392 */       return 0L;
/*     */     }
/*     */     catch (Exception e) {}
/*     */     
/* 396 */     return getQuadWordInBytes(address);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private long watchReadLong(int address)
/*     */   {
/*     */     try
/*     */     {
/* 408 */       return getPageFor(address).getQuadWord(address & 0xFFF);
/*     */     }
/*     */     catch (NullPointerException e)
/*     */     {
/* 412 */       return 0L;
/*     */     }
/*     */     catch (Exception e)
/*     */     {
/* 416 */       long l = watchReadByte(address++);
/* 417 */       l |= watchReadByte(address++) << 8;
/* 418 */       l |= watchReadByte(address++) << 16;
/* 419 */       l |= watchReadByte(address++) << 24;
/* 420 */       l |= watchReadByte(address++) << 32;
/* 421 */       l |= watchReadByte(address++) << 40;
/* 422 */       l |= watchReadByte(address++) << 48;
/* 423 */       return l | watchReadByte(address++) << 56;
/*     */     }
/*     */   }
/*     */   
/*     */   private int watchReadByte(int address)
/*     */   {
/*     */     try
/*     */     {
/* 431 */       return getPageFor(address).getByte(address & 0xFFF) & 0xFF;
/*     */     }
/*     */     catch (NullPointerException e) {}
/*     */     
/* 435 */     return 0;
/*     */   }
/*     */   
/*     */   public void setByte(int address, byte data)
/*     */   {
/* 440 */     watch_write(address, data);
/* 441 */     if (Trace.compareToInt) {
/* 442 */       this.dirtyPageSet.add(Integer.valueOf(address >>> 12));
/*     */     }
/*     */     try
/*     */     {
/* 446 */       getPageFor(address).setByte(address & 0xFFF, data);
/*     */     }
/*     */     catch (NullPointerException e)
/*     */     {
/* 450 */       AbstractMemory m = allocatePage(address);
/* 451 */       m.setByte(address & 0xFFF, data);
/*     */     }
/*     */   }
/*     */   
/*     */   public void setWord(int address, short data)
/*     */   {
/* 457 */     watch_write(address, data);
/* 458 */     if (Trace.compareToInt) {
/* 459 */       this.dirtyPageSet.add(Integer.valueOf(address >>> 12));
/*     */     }
/*     */     try
/*     */     {
/* 463 */       getPageFor(address).setWord(address & 0xFFF, data);
/*     */     }
/*     */     catch (NullPointerException e)
/*     */     {
/* 467 */       AbstractMemory m = allocatePage(address);
/* 468 */       setWord(address, data);
/*     */     }
/*     */     catch (Exception e)
/*     */     {
/* 472 */       setWordInBytes(address, data);
/*     */     }
/*     */   }
/*     */   
/*     */   public void setDoubleWord(int address, int data)
/*     */   {
/* 478 */     watch_write(address, data);
/* 479 */     if (Trace.compareToInt) {
/* 480 */       this.dirtyPageSet.add(Integer.valueOf(address >>> 12));
/*     */     }
/*     */     try
/*     */     {
/* 484 */       getPageFor(address).setDoubleWord(address & 0xFFF, data);
/*     */     }
/*     */     catch (NullPointerException e)
/*     */     {
/* 488 */       AbstractMemory m = allocatePage(address);
/* 489 */       setDoubleWord(address, data);
/*     */     }
/*     */     catch (Exception e)
/*     */     {
/* 493 */       setDoubleWordInBytes(address, data);
/*     */     }
/*     */   }
/*     */   
/*     */   public void setQuadWord(int address, long data)
/*     */   {
/* 499 */     watch_write(address, data);
/* 500 */     if (Trace.compareToInt) {
/* 501 */       this.dirtyPageSet.add(Integer.valueOf(address >>> 12));
/*     */     }
/*     */     try
/*     */     {
/* 505 */       getPageFor(address).setQuadWord(address & 0xFFF, data);
/*     */     }
/*     */     catch (NullPointerException e)
/*     */     {
/* 509 */       AbstractMemory m = allocatePage(address);
/* 510 */       setQuadWord(address, data);
/*     */     }
/*     */     catch (Exception e)
/*     */     {
/* 514 */       setQuadWordInBytes(address, data);
/*     */     }
/*     */   }
/*     */   
/*     */   private void watch_read(int address) {
/* 519 */     if ((WATCH) && 
/* 520 */       (address >= WATCH_ADDR_START) && (address < WATCH_RANGE_END)) {
/* 521 */       ThreadContext context = ThreadContext.currentCtx();
/* 522 */       if (!context.isSilent()) {
/* 523 */         long l = watchReadLong(address);
/* 524 */         context.warn("READ :%08X [%3X] %08x EIP=%s", new Object[] { Integer.valueOf(address), Integer.valueOf(address - WATCH_ADDR_START), Long.valueOf(l), format(context.cpu.eip) });
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   private void watch_write(int address, long data)
/*     */   {
/* 531 */     if ((WATCH) && 
/* 532 */       (address >= WATCH_ADDR_START) && (address < WATCH_RANGE_END)) {
/* 533 */       ThreadContext context = ThreadContext.currentCtx();
/* 534 */       if (!context.isSilent()) {
/* 535 */         long l = watchReadLong(address);
/*     */         
/* 537 */         ThreadContext.currentCtx().warn("WRITE:%08X [%3X]=%X %08x EIP=%s", new Object[] { Integer.valueOf(address), Integer.valueOf(address - WATCH_ADDR_START), Long.valueOf(data), Long.valueOf(l), format(context.cpu.eip) });
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   public float getF32(int addr)
/*     */   {
/* 545 */     return Float.intBitsToFloat(getDoubleWord(addr));
/*     */   }
/*     */   
/*     */   public double getF64(int addr)
/*     */   {
/* 550 */     return Double.longBitsToDouble(getQuadWord(addr));
/*     */   }
/*     */   
/*     */   public void setF32(int addr, float f)
/*     */   {
/* 555 */     setDoubleWord(addr, Float.floatToIntBits(f));
/*     */   }
/*     */   
/*     */   public void setF64(int addr, double d)
/*     */   {
/* 560 */     setQuadWord(addr, Double.doubleToLongBits(d));
/*     */   }
/*     */   
/*     */   public boolean hasAnnotations()
/*     */   {
/* 565 */     return true;
/*     */   }
/*     */   
/*     */   public final Object getAnnotation(int address)
/*     */   {
/*     */     try
/*     */     {
/* 572 */       return getPageFor(address).getAnnotation(address & 0xFFF);
/*     */     }
/*     */     catch (NullPointerException e) {}
/*     */     
/* 576 */     return null;
/*     */   }
/*     */   
/*     */ 
/*     */   public final void setAnnotation(int address, Object value)
/*     */   {
/*     */     try
/*     */     {
/* 584 */       getPageFor(address).setAnnotation(address & 0xFFF, value);
/*     */     }
/*     */     catch (NullPointerException e)
/*     */     {
/* 588 */       AbstractMemory m = allocatePage(address);
/* 589 */       m.setAnnotation(address & 0xFFF, value);
/*     */     }
/*     */   }
/*     */   
/*     */   public void clear()
/*     */   {
/* 595 */     java.util.Arrays.fill(this.pages, null);
/*     */   }
/*     */   
/*     */   public void clearAnnotations()
/*     */   {
/* 600 */     for (int i = 0; i < this.pages.length; i++)
/*     */     {
/* 602 */       if (this.pages[i] != null)
/*     */       {
/* 604 */         this.pages[i].clearAnnotations();
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public void clear(int start, int length) {
/* 610 */     int startIndex = start >>> 12;
/* 611 */     int endIndex = start + length >>> 12;
/*     */     
/* 613 */     for (int i = startIndex + 1; i < endIndex; i++) {
/* 614 */       this.pages[i] = null;
/*     */     }
/* 616 */     if (this.pages[startIndex] != null)
/*     */     {
/* 618 */       int partial = start - startIndex * 4096;
/* 619 */       this.pages[startIndex].clear(partial, Math.min(length, 4096));
/*     */     }
/*     */     
/* 622 */     if ((endIndex > startIndex) && (this.pages[endIndex] != null))
/*     */     {
/* 624 */       int remainder = start + length - endIndex * 4096;
/* 625 */       this.pages[endIndex].clear(0, remainder);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   public void store(int address, byte[] src)
/*     */   {
/* 632 */     store(address, src, 0, src.length);
/*     */   }
/*     */   
/*     */   public void store(int address, byte[] src, int offset, int amount) {
/* 636 */     for (int i = 0; i < amount; i++) {
/* 637 */       setByte(i + address, src[i]);
/*     */     }
/*     */   }
/*     */   
/*     */   public char getChar(int addr) {
/* 642 */     return (char)getByte(addr);
/*     */   }
/*     */   
/*     */   public String getString(int addr) {
/* 646 */     StringBuilder b = getStringBuilder(addr);
/* 647 */     return b.toString();
/*     */   }
/*     */   
/*     */   public StringBuilder getStringBuilder(int addr) {
/* 651 */     StringBuilder b = new StringBuilder();
/* 652 */     append(addr, b);
/* 653 */     return b;
/*     */   }
/*     */   
/*     */   public void append(int addr, StringBuilder b) {
/*     */     char c;
/* 658 */     while ((c = (char)getByte(addr++)) > 0) {
/* 659 */       b.append(c);
/*     */     }
/*     */   }
/*     */   
/*     */   public int setString(int addr, String s, boolean write_null) {
/* 664 */     int len = s.length();
/* 665 */     int pos = addr;
/* 666 */     for (int i = 0; i < len; i++) {
/* 667 */       byte chr = (byte)s.charAt(i);
/* 668 */       setByte(pos++, chr);
/*     */     }
/* 670 */     if (write_null) setByte(pos++, (byte)0);
/* 671 */     return pos - addr;
/*     */   }
/*     */   
/* 674 */   private final byte[] IO_BUFFER = new byte[1048576];
/*     */   public static final int PROT_READ = 1;
/*     */   public static final int PROT_WRITE = 2;
/*     */   public static final int PROT_EXEC = 4;
/*     */   public static final int PROT_NONE = 0;
/*     */   public static final int MAP_SHARED = 1;
/*     */   public static final int MAP_PRIVATE = 2;
/*     */   public static final int MAP_TYPE = 3;
/*     */   public static final int MAP_FIXED = 16;
/*     */   public static final int MAP_ANONYMOUS = 2048;
/*     */   
/*     */   public int write(int address, int amountInBytes, IoHandle handle) throws IOException
/*     */   {
/*     */     try
/*     */     {
/* 689 */       int written = 0;
/* 690 */       int max_length = this.IO_BUFFER.length;
/* 691 */       if (amountInBytes > max_length) System.out.println("Writing " + amountInBytes);
/* 692 */       while (amountInBytes > 0) {
/* 693 */         int wr = Math.min(amountInBytes, max_length);
/* 694 */         load(address, this.IO_BUFFER, 0, wr);
/* 695 */         wr = handle.writeBytes(this.IO_BUFFER, 0, wr);
/* 696 */         written += wr;
/* 697 */         amountInBytes -= wr;
/* 698 */         address += wr;
/*     */       }
/*     */       
/* 701 */       int i = written;return i;
/*     */     }
/*     */     finally {}
/*     */   }
/*     */   
/*     */   public void memcopy(int src, int len, int dst)
/*     */   {
/* 708 */     load(src, this.IO_BUFFER, 0, len);
/* 709 */     store(dst, this.IO_BUFFER, 0, len);
/*     */   }
/*     */   
/* 712 */   public void memset(int dst, int len, byte val) { for (int i = 0; i < len; i++) { setByte(dst++, val);
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
/*     */   public int read(int addr, IoHandle handle, int amount)
/*     */     throws IOException
/*     */   {
/*     */     try
/*     */     {
/* 729 */       int result = 0;
/* 730 */       int max_length = this.IO_BUFFER.length;
/* 731 */       while (amount > 0) {
/* 732 */         int read = handle.readBytes(this.IO_BUFFER, 0, Math.min(amount, max_length));
/* 733 */         if (read < 0) {
/*     */           break;
/*     */         }
/* 736 */         store(addr, this.IO_BUFFER, 0, read);
/* 737 */         result += read;
/* 738 */         amount -= read;
/* 739 */         addr += read;
/*     */       }
/*     */       
/* 742 */       int i = result;return i;
/*     */     }
/*     */     finally {}
/*     */   }
/*     */   
/*     */ 
/*     */   public void store(int address, int[] src, int amount)
/*     */   {
/* 750 */     for (int i = 0; i < amount; i++)
/*     */     {
/* 752 */       setDoubleWord(address, src[i]);
/* 753 */       address += 4;
/*     */     }
/*     */   }
/*     */   
/*     */   public void load(int address, byte[] dest)
/*     */   {
/* 759 */     load(address, dest, 0, dest.length);
/*     */   }
/*     */   
/*     */   public void load(int address, byte[] dest, int offset, int length)
/*     */   {
/* 764 */     for (int i = 0; i < length; i++) {
/* 765 */       dest[i] = getByte(address++);
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
/*     */   public int mmap(int start, int len, int prot, int flags, int fd, int offset)
/*     */   {
/* 782 */     if ((flags & 0x10) != 0)
/*     */     {
/* 784 */       throw new IllegalStateException("Unimplemented mmap mode: MAP_FIXED");
/*     */     }
/*     */     
/* 787 */     return allocateBlock(start, len);
/*     */   }
/*     */   
/*     */   public int getBreakAddress() {
/* 791 */     return this.breakPageAddr;
/*     */   }
/*     */   
/*     */   public String string(int str$) {
/* 795 */     StringBuilder b = new StringBuilder();
/*     */     for (;;) {
/* 797 */       int c = getByte(str$++) & 0xFF;
/* 798 */       if (c == 0) return b.toString();
/* 799 */       b.append((char)c);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Joey\Downloads\codejar.jar!\com\emt\proteus\runtime32\memory\MainMemory.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */