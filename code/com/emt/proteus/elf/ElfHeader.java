/*     */ package com.emt.proteus.elf;
/*     */ 
/*     */ import java.io.PrintStream;
/*     */ import java.nio.ByteBuffer;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ElfHeader
/*     */ {
/*     */   public static final int ELF_CLASS_32 = 1;
/*     */   public static final int ELF_DATA_2_LSB = 1;
/*     */   public static final int EV_CURRENT = 1;
/*     */   public static final int ET_EXEC = 2;
/*     */   public static final int ET_REL = 1;
/*     */   public static final int ET_SHARED_OBJECT = 3;
/*     */   public static final int EM_386 = 3;
/*     */   private int elfType;
/*     */   private int machineType;
/*     */   private int version;
/*     */   private int osAbi;
/*     */   
/*     */   public ElfHeader(byte[] raw)
/*     */   {
/*  25 */     ByteBuffer buffer = Elf.createBuffer(raw);
/*     */     
/*  27 */     if ((raw[0] != Byte.MAX_VALUE) || (raw[1] != 69) || (raw[2] != 76) || (raw[3] != 70))
/*  28 */       throw new IllegalStateException("Magic number mismatch - not an ELF file");
/*  29 */     if (raw[4] != 1)
/*  30 */       throw new IllegalStateException("Unsupported file class (only 32 bit allowed)");
/*  31 */     if (raw[5] != 1)
/*  32 */       throw new IllegalStateException("Byte ordering not compatible with Intel x86: " + raw[5]);
/*  33 */     if (raw[6] != 1)
/*  34 */       throw new IllegalStateException("Unsupported ELF header version (" + raw[6] + ")");
/*  35 */     this.osAbi = (0xFF & raw[7]);
/*  36 */     this.abiVersion = (0xFF & raw[8]);
/*  37 */     buffer.position(16);
/*     */     
/*  39 */     this.elfType = (0xFFFF & buffer.getShort());
/*  40 */     if ((this.elfType != 2) && (this.elfType != 3)) {
/*  41 */       throw new IllegalStateException("Not an executable file (type is " + this.elfType + ")");
/*     */     }
/*  43 */     this.machineType = (0xFFFF & buffer.getShort());
/*  44 */     if (this.machineType != 3) {
/*  45 */       throw new IllegalStateException("Wrong machine target - only 386 supported");
/*     */     }
/*  47 */     this.version = buffer.getInt();
/*  48 */     this.entryPoint = buffer.getInt();
/*  49 */     this.programHeaderOffset = buffer.getInt();
/*  50 */     if (this.programHeaderOffset == 0)
/*  51 */       throw new IllegalStateException("No program header table: not an executable file?");
/*  52 */     this.sectionHeaderOffset = buffer.getInt();
/*  53 */     this.flags = buffer.getInt();
/*  54 */     this.headerSize = (0xFFFF & buffer.getShort());
/*  55 */     this.programHeaderEntrySize = (0xFFFF & buffer.getShort());
/*  56 */     this.programHeaderCount = (0xFFFF & buffer.getShort());
/*  57 */     this.sectionHeaderEntrySize = (0xFFFF & buffer.getShort());
/*  58 */     this.sectionHeaderCount = (0xFFFF & buffer.getShort());
/*  59 */     this.shstrndx = (0xFFFF & buffer.getShort());
/*     */     
/*  61 */     if (this.sectionHeaderCount > 1024)
/*  62 */       throw new IllegalStateException("Too many sections (>1024)");
/*  63 */     if (this.shstrndx > this.sectionHeaderCount)
/*  64 */       throw new IllegalStateException("String section index out of range"); }
/*     */   
/*     */   private int abiVersion;
/*     */   private int programHeaderEntrySize;
/*     */   
/*  69 */   public int getEntryPoint() { return this.entryPoint; }
/*     */   
/*     */   private int programHeaderCount;
/*     */   
/*     */   public int getSharedStringsSectionIndex() {
/*  74 */     return this.shstrndx;
/*     */   }
/*     */   
/*     */   private int sectionHeaderEntrySize;
/*     */   
/*  79 */   public int getProgramHeaderOffset() { return this.programHeaderOffset; }
/*     */   
/*     */   private int sectionHeaderCount;
/*     */   private int shstrndx;
/*     */   
/*  84 */   public int getSectionHeaderOffset() { return this.sectionHeaderOffset; }
/*     */   
/*     */   private int entryPoint;
/*     */   private int programHeaderOffset;
/*     */   
/*  89 */   public int getSectionHeaderSize() { return this.sectionHeaderCount * this.sectionHeaderEntrySize; }
/*     */   
/*     */   private int sectionHeaderOffset;
/*     */   private int flags;
/*     */   private int headerSize;
/*  94 */   public int getProgramHeaderSize() { return this.programHeaderCount * this.programHeaderEntrySize; }
/*     */   
/*     */ 
/*     */   public int getSectionHeaderCount()
/*     */   {
/*  99 */     return this.sectionHeaderCount;
/*     */   }
/*     */   
/*     */   public int getProgramHeaderCount()
/*     */   {
/* 104 */     return this.programHeaderCount;
/*     */   }
/*     */   
/*     */   public int getProgramHeaderEntrySize() {
/* 108 */     return this.programHeaderEntrySize;
/*     */   }
/*     */   
/*     */   public int getSectionHeaderEntrySize() {
/* 112 */     return this.sectionHeaderEntrySize;
/*     */   }
/*     */   
/*     */   public void print(PrintStream ps)
/*     */   {
/* 117 */     ps.println("Flags " + this.flags);
/* 118 */     ps.println("Elf version " + this.version);
/* 119 */     ps.println("OSABI " + this.osAbi);
/* 120 */     ps.println("ABI Version " + this.abiVersion);
/* 121 */     ps.println("Entry address: 0x" + Integer.toHexString(this.entryPoint));
/* 122 */     ps.println("Section header offset 0x" + Integer.toHexString(this.sectionHeaderOffset));
/* 123 */     ps.println("Program header offset 0x" + Integer.toHexString(this.programHeaderOffset));
/* 124 */     ps.println("Header Size " + this.headerSize);
/* 125 */     ps.println("Program Entry Size " + this.programHeaderEntrySize);
/* 126 */     ps.println("Section Entry Size " + this.sectionHeaderEntrySize);
/* 127 */     ps.println("Program Entry Count " + this.programHeaderCount);
/* 128 */     ps.println("Section Entry Count " + this.sectionHeaderCount);
/* 129 */     ps.println("shstrndx " + this.shstrndx);
/*     */   }
/*     */ }


/* Location:              C:\Users\Joey\Downloads\codejar.jar!\com\emt\proteus\elf\ElfHeader.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */