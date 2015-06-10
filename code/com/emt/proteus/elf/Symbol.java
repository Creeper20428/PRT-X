/*     */ package com.emt.proteus.elf;
/*     */ 
/*     */ import java.nio.ByteBuffer;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class Symbol
/*     */ {
/*     */   public static final int STB_LOCAL = 0;
/*     */   public static final int STB_GLOBAL = 1;
/*     */   public static final int STB_WEAK = 2;
/*     */   public static final int STB_LOOS = 10;
/*     */   public static final int STB_HIOS = 12;
/*     */   public static final int STB_LOPROC = 13;
/*     */   public static final int STB_HIPROC = 15;
/*     */   public static final int STT_NOTYPE = 0;
/*     */   public static final int STT_OBJECT = 1;
/*     */   public static final int STT_FUNC = 2;
/*     */   public static final int STT_SECTION = 3;
/*     */   public static final int STT_FILE = 4;
/*     */   public static final int STT_COMMON = 5;
/*     */   public static final int STT_TLS = 6;
/*     */   public static final int STT_GNU_IFUNC = 10;
/*     */   public static final int STT_LO_PROC = 13;
/*     */   public static final int STT_HI_PROC = 15;
/*     */   public static final int STV_DEFAULT = 0;
/*     */   public static final int STV_INTERNAL = 1;
/*     */   public static final int STV_HIDDEN = 2;
/*     */   public static final int STV_PROTECTED = 3;
/*     */   public static final int SHN_UNDEF = 0;
/*     */   public static final int SHN_ABS = 65521;
/*     */   public static final int SHN_COMMON = 65522;
/*     */   public static final int SHN_LORESERVE = 65280;
/*     */   public static final int SHN_LOPROC = 65280;
/*     */   public static final int SHN_HIPROC = 65311;
/*     */   public static final int SHN_HIRESERVE = 65535;
/*     */   public String name;
/*     */   public int nameIndex;
/*     */   public int address;
/*     */   public int size;
/*     */   public int info;
/*     */   public int other;
/*     */   public int sectionIndex;
/*     */   private int tableIndex;
/*     */   
/*     */   public Symbol()
/*     */   {
/*  49 */     this.name = null;
/*  50 */     this.nameIndex = (this.address = this.size = this.info = this.sectionIndex = 0);
/*     */   }
/*     */   
/*     */   public int getBinding()
/*     */   {
/*  55 */     return this.info >> 4;
/*     */   }
/*     */   
/*     */   public String getBindingInfo()
/*     */   {
/*  60 */     int binding = getBinding();
/*     */     
/*  62 */     if (binding == 0)
/*  63 */       return "Local";
/*  64 */     if (binding == 1)
/*  65 */       return "Global";
/*  66 */     if (binding == 2) {
/*  67 */       return "Weak";
/*     */     }
/*  69 */     return "?" + binding;
/*     */   }
/*     */   
/*     */   public int getType()
/*     */   {
/*  74 */     return 0xF & this.info;
/*     */   }
/*     */   
/*     */   public String getTypeInfo()
/*     */   {
/*  79 */     int tt = getType();
/*     */     
/*  81 */     if (tt == 0)
/*  82 */       return "None";
/*  83 */     if (tt == 1)
/*  84 */       return "Object";
/*  85 */     if (tt == 2)
/*  86 */       return "Function";
/*  87 */     if (tt == 10)
/*  88 */       return "Gnu Indirect Function";
/*  89 */     if (tt == 3)
/*  90 */       return "Section";
/*  91 */     if (tt == 4)
/*  92 */       return "File";
/*  93 */     if (tt == 5)
/*  94 */       return "Common";
/*  95 */     if (tt == 6) {
/*  96 */       return "ThrdLcl";
/*     */     }
/*  98 */     return "?" + tt;
/*     */   }
/*     */   
/*     */   public String getVisibility()
/*     */   {
/* 103 */     if (this.other == 0)
/* 104 */       return "Default";
/* 105 */     if (this.other == 1)
/* 106 */       return "Internal";
/* 107 */     if (this.other == 2)
/* 108 */       return "Hidden";
/* 109 */     if (this.other == 3) {
/* 110 */       return "Protected";
/*     */     }
/* 112 */     return "?" + this.other;
/*     */   }
/*     */   
/*     */   public void read(ByteBuffer buffer)
/*     */   {
/* 117 */     this.nameIndex = buffer.getInt();
/* 118 */     this.address = buffer.getInt();
/* 119 */     this.size = buffer.getInt();
/* 120 */     this.info = (0xFF & buffer.get());
/* 121 */     this.other = (0xFF & buffer.get());
/* 122 */     this.sectionIndex = (0xFFFF & buffer.getShort());
/*     */   }
/*     */   
/*     */   public void setName(String name)
/*     */   {
/* 127 */     this.name = name;
/*     */   }
/*     */   
/*     */   public void resolveName(StringSection names)
/*     */   {
/* 132 */     this.name = names.getNameAtIndex(this.nameIndex);
/*     */   }
/*     */   
/*     */   public boolean isExported() {
/* 136 */     int binding = getBinding();
/* 137 */     if ((binding != 1) && (binding != 2)) return false;
/* 138 */     if (this.sectionIndex == 0) return false;
/* 139 */     if (this.address == 0) return false;
/* 140 */     return true;
/*     */   }
/*     */   
/*     */   public String toString()
/*     */   {
/* 145 */     String indexString = "" + this.sectionIndex;
/* 146 */     if (this.sectionIndex == 0) {
/* 147 */       indexString = "UNDEF";
/* 148 */     } else if (this.sectionIndex == 65521) {
/* 149 */       indexString = "ABS";
/* 150 */     } else if (this.sectionIndex == 65522) {
/* 151 */       indexString = "COMMON";
/*     */     }
/* 153 */     return this.name + " [" + this.nameIndex + "] at 0x" + Integer.toHexString(this.address) + "  " + this.size + "  " + getTypeInfo() + "  " + getBindingInfo() + "  " + getVisibility() + " -> " + indexString;
/*     */   }
/*     */   
/*     */   public String getName() {
/* 157 */     return this.name;
/*     */   }
/*     */   
/*     */   public int getSize() {
/* 161 */     return this.size;
/*     */   }
/*     */   
/*     */   public int getSectionIndex() {
/* 165 */     return this.sectionIndex;
/*     */   }
/*     */   
/*     */   public int getAddress() {
/* 169 */     return this.address;
/*     */   }
/*     */   
/* 172 */   public int getValue() { return this.address; }
/*     */   
/*     */   public int getNameIndex()
/*     */   {
/* 176 */     return this.nameIndex;
/*     */   }
/*     */   
/*     */   public boolean isWeak() {
/* 180 */     return getBinding() == 2;
/*     */   }
/*     */   
/*     */   public boolean isLocal() {
/* 184 */     return getBinding() == 0;
/*     */   }
/*     */   
/*     */   public boolean isFunction() {
/* 188 */     return getType() == 2;
/*     */   }
/*     */   
/* 191 */   public boolean isGnuIndirectFunction() { return getType() == 10; }
/*     */   
/*     */   public boolean isDefined() {
/* 194 */     switch (this.sectionIndex) {
/* 195 */     default:  return true;
/*     */     }
/*     */     
/*     */     
/*     */ 
/* 200 */     return false;
/*     */   }
/*     */   
/*     */   public boolean isObject() {
/* 204 */     return getType() == 1;
/*     */   }
/*     */   
/*     */   public void assignIndex(int i) {
/* 208 */     this.tableIndex = i;
/*     */   }
/*     */   
/*     */   public int getIndex() {
/* 212 */     return this.tableIndex;
/*     */   }
/*     */ }


/* Location:              C:\Users\Joey\Downloads\codejar.jar!\com\emt\proteus\elf\Symbol.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */