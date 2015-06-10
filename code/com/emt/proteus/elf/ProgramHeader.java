/*     */ package com.emt.proteus.elf;
/*     */ 
/*     */ import java.nio.ByteBuffer;
/*     */ 
/*     */ public class ProgramHeader
/*     */ {
/*     */   public static final int PT_NULL = 0;
/*     */   public static final int PT_LOAD = 1;
/*     */   public static final int PT_DYNAMIC = 2;
/*     */   public static final int PT_INTERP = 3;
/*     */   public static final int PT_NOTE = 4;
/*     */   public static final int PT_SHLIB = 5;
/*     */   public static final int PT_PHDR = 6;
/*     */   public static final int PT_TLS = 7;
/*     */   public static final int PF_X = 1;
/*     */   public static final int PF_W = 2;
/*     */   public static final int PF_R = 4;
/*     */   private int type;
/*     */   private int fileOffset;
/*     */   private int virtualAddress;
/*     */   private int fileSize;
/*     */   private int memorySize;
/*     */   private int flags;
/*     */   private int alignment;
/*     */   
/*     */   public ProgramHeader()
/*     */   {
/*  28 */     this.type = (this.fileOffset = this.virtualAddress = this.fileSize = this.memorySize = this.flags = this.alignment = 0);
/*     */   }
/*     */   
/*     */   public int getType()
/*     */   {
/*  33 */     return this.type;
/*     */   }
/*     */   
/*     */   public int getFileOffset()
/*     */   {
/*  38 */     return this.fileOffset;
/*     */   }
/*     */   
/*     */   public int getVirtualAddress()
/*     */   {
/*  43 */     return this.virtualAddress;
/*     */   }
/*     */   
/*     */   public int getFileSize()
/*     */   {
/*  48 */     return this.fileSize;
/*     */   }
/*     */   
/*     */   public int getMemorySize()
/*     */   {
/*  53 */     return this.memorySize;
/*     */   }
/*     */   
/*     */   public int getFlags()
/*     */   {
/*  58 */     return this.flags;
/*     */   }
/*     */   
/*     */   public int getAlignment()
/*     */   {
/*  63 */     return this.alignment;
/*     */   }
/*     */   
/*     */   public void read(ByteBuffer buffer)
/*     */   {
/*  68 */     this.type = buffer.getInt();
/*  69 */     this.fileOffset = buffer.getInt();
/*  70 */     this.virtualAddress = buffer.getInt();
/*  71 */     buffer.getInt();
/*  72 */     this.fileSize = buffer.getInt();
/*  73 */     this.memorySize = buffer.getInt();
/*  74 */     this.flags = buffer.getInt();
/*  75 */     this.alignment = buffer.getInt();
/*     */   }
/*     */   
/*     */   public String getTypeDescription()
/*     */   {
/*  80 */     String typeString = null;
/*     */     
/*  82 */     if (this.type == 0) {
/*  83 */       typeString = "NULL";
/*  84 */     } else if (this.type == 1) {
/*  85 */       typeString = "LOAD";
/*  86 */     } else if (this.type == 2) {
/*  87 */       typeString = "DYNAMIC";
/*  88 */     } else if (this.type == 3) {
/*  89 */       typeString = "INTERP";
/*  90 */     } else if (this.type == 4) {
/*  91 */       typeString = "NOTE";
/*  92 */     } else if (this.type == 5) {
/*  93 */       typeString = "SHLIB";
/*  94 */     } else if (this.type == 6) {
/*  95 */       typeString = "PHDR";
/*  96 */     } else if (this.type == 7) {
/*  97 */       typeString = "TLS";
/*     */     } else {
/*  99 */       typeString = "?" + this.type;
/*     */     }
/* 101 */     return typeString;
/*     */   }
/*     */   
/*     */   public String getFlagDescription()
/*     */   {
/* 106 */     String flagString = "";
/* 107 */     if ((this.flags & 0x1) != 0)
/* 108 */       flagString = flagString + "X";
/* 109 */     if ((this.flags & 0x2) != 0)
/* 110 */       flagString = flagString + "W";
/* 111 */     if ((this.flags & 0x4) != 0) {
/* 112 */       flagString = flagString + "R";
/*     */     }
/* 114 */     return flagString;
/*     */   }
/*     */   
/*     */   public String toString()
/*     */   {
/* 119 */     return getTypeDescription() + "  0x" + Integer.toHexString(this.fileOffset) + "  -> 0x" + Integer.toHexString(this.virtualAddress) + "  [" + this.fileSize + "  " + this.memorySize + "] " + getFlagDescription() + "  " + this.alignment;
/*     */   }
/*     */ }


/* Location:              C:\Users\Joey\Downloads\codejar.jar!\com\emt\proteus\elf\ProgramHeader.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */