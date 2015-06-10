/*     */ package com.emt.proteus.elf;
/*     */ 
/*     */ import java.nio.ByteBuffer;
/*     */ 
/*     */ public class SectionHeader {
/*     */   public static final int SHT_NULL = 0;
/*     */   public static final int SHT_PROGBITS = 1;
/*     */   public static final int SHT_SYMTAB = 2;
/*     */   public static final int SHT_STRTAB = 3;
/*     */   public static final int SHT_RELA = 4;
/*     */   public static final int SHT_HASH = 5;
/*     */   public static final int SHT_DYNAMIC = 6;
/*     */   public static final int SHT_NOTE = 7;
/*     */   public static final int SHT_NOBITS = 8;
/*     */   public static final int SHT_REL = 9;
/*     */   public static final int SHT_SHLIB = 10;
/*     */   public static final int SHT_DYNSYM = 11;
/*     */   public static final int SHT_INIT_ARRAY = 14;
/*     */   public static final int SHT_FINI_ARRAY = 15;
/*     */   public static final int SHT_PREINIT_ARRAY = 16;
/*     */   public static final int SHT_GROUP = 17;
/*     */   public static final int SHT_SYMTAB_SHNDX = 18;
/*     */   public static final int SHF_WRITE = 1;
/*     */   public static final int SHF_ALLOC = 2;
/*     */   public static final int SHF_EXECINSTR = 4;
/*     */   public static final int SHF_MERGE = 16;
/*     */   public static final int SHF_STRINGS = 32;
/*     */   public static final int SHF_INFO_LINK = 64;
/*     */   public static final int SHF_LINK_ORDER = 128;
/*     */   public static final int SHF_OS_NONCONFORMING = 256;
/*     */   public static final int SHF_GROUP = 512;
/*     */   public static final int SHF_TLS = 1024;
/*     */   private String name;
/*     */   private int nameIndex;
/*     */   private int type;
/*     */   private int flags;
/*     */   private int address;
/*     */   private int fileOffset;
/*     */   private int fileSize;
/*     */   private int link;
/*     */   private int info;
/*     */   private int alignment;
/*     */   private int entrySize;
/*     */   
/*  45 */   public SectionHeader() { this.name = null;
/*  46 */     this.nameIndex = (this.type = this.flags = this.address = this.fileOffset = this.fileSize = this.link = this.info = this.alignment = this.entrySize = 0);
/*     */   }
/*     */   
/*     */   public void read(ByteBuffer buffer)
/*     */   {
/*  51 */     this.nameIndex = buffer.getInt();
/*  52 */     this.type = buffer.getInt();
/*  53 */     this.flags = buffer.getInt();
/*  54 */     this.address = buffer.getInt();
/*  55 */     this.fileOffset = buffer.getInt();
/*  56 */     this.fileSize = buffer.getInt();
/*  57 */     this.link = buffer.getInt();
/*  58 */     this.info = buffer.getInt();
/*  59 */     this.alignment = buffer.getInt();
/*  60 */     this.entrySize = buffer.getInt();
/*     */   }
/*     */   
/*     */   public String getName()
/*     */   {
/*  65 */     return this.name;
/*     */   }
/*     */   
/*     */   public int getType()
/*     */   {
/*  70 */     return this.type;
/*     */   }
/*     */   
/*     */   public int getFileOffset()
/*     */   {
/*  75 */     return this.fileOffset;
/*     */   }
/*     */   
/*     */   public int getFileSize()
/*     */   {
/*  80 */     return this.fileSize;
/*     */   }
/*     */   
/*     */   public int getAddress() {
/*  84 */     return this.address;
/*     */   }
/*     */   
/*     */   public void resolveName(StringSection names)
/*     */   {
/*  89 */     this.name = names.getNameAtIndex(this.nameIndex);
/*     */   }
/*     */   
/*     */   public String getTypeDescription()
/*     */   {
/*  94 */     String typeString = null;
/*     */     
/*  96 */     if (this.type == 0) {
/*  97 */       typeString = "NULL";
/*  98 */     } else if (this.type == 1) {
/*  99 */       typeString = "PROG BITS";
/* 100 */     } else if (this.type == 2) {
/* 101 */       typeString = "SYM TAB";
/* 102 */     } else if (this.type == 3) {
/* 103 */       typeString = "STR TAB";
/* 104 */     } else if (this.type == 4) {
/* 105 */       typeString = "RELA";
/* 106 */     } else if (this.type == 5) {
/* 107 */       typeString = "HASH";
/* 108 */     } else if (this.type == 6) {
/* 109 */       typeString = "DYNAMIC";
/* 110 */     } else if (this.type == 7) {
/* 111 */       typeString = "NOTE";
/* 112 */     } else if (this.type == 8) {
/* 113 */       typeString = "NOBITS";
/* 114 */     } else if (this.type == 9) {
/* 115 */       typeString = "REL";
/* 116 */     } else if (this.type == 10) {
/* 117 */       typeString = "SHLIB";
/* 118 */     } else if (this.type == 11) {
/* 119 */       typeString = "DYNAMIC";
/* 120 */     } else if (this.type == 14) {
/* 121 */       typeString = "INIT ARR";
/* 122 */     } else if (this.type == 15) {
/* 123 */       typeString = "FINI ARR";
/* 124 */     } else if (this.type == 16) {
/* 125 */       typeString = "PRE INIT ARR";
/*     */     } else {
/* 127 */       typeString = "PROC/USR";
/*     */     }
/* 129 */     return typeString;
/*     */   }
/*     */   
/*     */   public String getFlagDescription()
/*     */   {
/* 134 */     String flagStr = "";
/* 135 */     if ((this.flags & 0x2) != 0)
/* 136 */       flagStr = flagStr + "A";
/* 137 */     if ((this.flags & 0x1) != 0)
/* 138 */       flagStr = flagStr + "W";
/* 139 */     if ((this.flags & 0x4) != 0)
/* 140 */       flagStr = flagStr + "X";
/* 141 */     if ((this.flags & 0x10) != 0)
/* 142 */       flagStr = flagStr + "M";
/* 143 */     if ((this.flags & 0x20) != 0)
/* 144 */       flagStr = flagStr + "S";
/* 145 */     if ((this.flags & 0x40) != 0)
/* 146 */       flagStr = flagStr + "L";
/* 147 */     if ((this.flags & 0x100) != 0)
/* 148 */       flagStr = flagStr + "Y";
/* 149 */     if ((this.flags & 0x200) != 0)
/* 150 */       flagStr = flagStr + "G";
/* 151 */     if ((this.flags & 0x400) != 0) {
/* 152 */       flagStr = flagStr + "T";
/*     */     }
/* 154 */     return flagStr;
/*     */   }
/*     */   
/*     */   public String toString()
/*     */   {
/* 159 */     return this.name + " [" + this.nameIndex + "]   " + getTypeDescription() + " 0x" + Integer.toHexString(this.address) + " 0x" + Integer.toHexString(this.fileOffset) + "  0x" + Integer.toHexString(this.fileSize) + "  " + this.flags + "(" + getFlagDescription() + ")  " + this.link + "  " + this.info + "  " + this.entrySize + "  " + this.alignment;
/*     */   }
/*     */ }


/* Location:              C:\Users\Joey\Downloads\codejar.jar!\com\emt\proteus\elf\SectionHeader.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */