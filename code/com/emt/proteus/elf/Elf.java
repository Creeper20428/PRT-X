/*     */ package com.emt.proteus.elf;
/*     */ 
/*     */ import java.nio.ByteBuffer;
/*     */ import java.nio.ByteOrder;
/*     */ 
/*     */ 
/*     */ public class Elf
/*     */ {
/*     */   public static final int DEFAULT_MAX_DATA = 8388608;
/*     */   private int maxDataSize;
/*     */   private ElfHeader header;
/*     */   private StringSection nameStrings;
/*     */   private StringSection symbolNames;
/*     */   private ProgramHeader[] programHeaders;
/*     */   private SectionHeader[] sectionHeaders;
/*     */   private ProgramSegment[] programSegments;
/*     */   private Section[] sections;
/*     */   private Symbol[] symbols;
/*     */   private DynamicInfo dynamic;
/*     */   
/*     */   public Elf(SeekableDataSource src)
/*     */     throws Exception
/*     */   {
/*  24 */     this(src, 8388608);
/*     */   }
/*     */   
/*     */   public Elf(SeekableDataSource src, int maxDataSize) throws Exception
/*     */   {
/*  29 */     this.maxDataSize = maxDataSize;
/*     */     
/*  31 */     byte[] headerBytes = new byte[52];
/*  32 */     src.readFully(headerBytes);
/*  33 */     this.header = new ElfHeader(headerBytes);
/*     */     
/*  35 */     readSectionHeaders(src);
/*     */     
/*     */ 
/*     */ 
/*  39 */     int shstrndx = this.header.getSharedStringsSectionIndex();
/*  40 */     if (shstrndx < 0)
/*  41 */       throw new IllegalStateException("No String section found (implies no symbols)");
/*  42 */     this.nameStrings = readStrings(this.sectionHeaders[shstrndx], src);
/*     */     
/*  44 */     for (int i = 0; i < this.sectionHeaders.length; i++) {
/*  45 */       this.sectionHeaders[i].resolveName(this.nameStrings);
/*     */     }
/*  47 */     SectionHeader strTab = findSection(".strtab");
/*     */     
/*  49 */     SectionHeader symTab = findSection(".symtab");
/*  50 */     if ((symTab != null) && (strTab != null)) {
/*  51 */       this.symbolNames = readStrings(strTab, src);
/*  52 */       readSymbols(src, symTab);
/*  53 */       for (int i = 0; i < this.symbols.length; i++) {
/*  54 */         this.symbols[i].resolveName(this.symbolNames);
/*     */       }
/*     */     }
/*     */     
/*  58 */     readProgramHeaders(src);
/*     */     
/*  60 */     this.sections = new Section[this.sectionHeaders.length];
/*  61 */     for (int i = 0; i < this.sectionHeaders.length; i++)
/*     */     {
/*  63 */       byte[] rawData = null;
/*  64 */       if (this.sectionHeaders[i].getType() != 8)
/*     */       {
/*  66 */         src.seek(this.sectionHeaders[i].getFileOffset());
/*  67 */         rawData = createArray(this.sectionHeaders[i].getFileSize());
/*  68 */         src.readFully(rawData);
/*     */       }
/*     */       else {
/*  71 */         rawData = new byte[0];
/*     */       }
/*  73 */       this.sections[i] = new Section(this.sectionHeaders[i], rawData);
/*     */     }
/*     */     
/*  76 */     this.programSegments = new ProgramSegment[this.programHeaders.length];
/*  77 */     for (int i = 0; i < this.programHeaders.length; i++)
/*     */     {
/*  79 */       src.seek(this.programHeaders[i].getFileOffset());
/*  80 */       byte[] rawData = createArray(this.programHeaders[i].getFileSize());
/*  81 */       src.readFully(rawData);
/*  82 */       this.programSegments[i] = new ProgramSegment(this.programHeaders[i], rawData);
/*     */     }
/*     */   }
/*     */   
/*     */   public ElfHeader getHeader()
/*     */   {
/*  88 */     return this.header;
/*     */   }
/*     */   
/*     */   public ProgramHeader getProgramHeader(int index)
/*     */   {
/*  93 */     return this.programHeaders[index];
/*     */   }
/*     */   
/*     */   public ProgramSegment getProgramSegment(int index)
/*     */   {
/*  98 */     return this.programSegments[index];
/*     */   }
/*     */   
/*     */   public SectionHeader getSectionHeader(int index)
/*     */   {
/* 103 */     return this.sectionHeaders[index];
/*     */   }
/*     */   
/*     */   public Section getSection(int index)
/*     */   {
/* 108 */     return this.sections[index];
/*     */   }
/*     */   
/*     */   public StringSection getNameStrings()
/*     */   {
/* 113 */     return this.nameStrings;
/*     */   }
/*     */   
/*     */   public StringSection getSymbolNames()
/*     */   {
/* 118 */     return this.symbolNames;
/*     */   }
/*     */   
/*     */   public Symbol[] getSymbols()
/*     */   {
/* 123 */     return this.symbols;
/*     */   }
/*     */   
/*     */   public Symbol getSymbolAtAddress(int address)
/*     */   {
/* 128 */     for (int i = 0; i < this.symbols.length; i++)
/*     */     {
/* 130 */       if (this.symbols[i].address == address) {
/* 131 */         return this.symbols[i];
/*     */       }
/*     */     }
/* 134 */     return null;
/*     */   }
/*     */   
/*     */   private byte[] createArray(int size)
/*     */   {
/* 139 */     if (size > this.maxDataSize)
/* 140 */       throw new IllegalStateException("Max data size exceeded (" + size + " > " + this.maxDataSize + ")");
/* 141 */     return new byte[size];
/*     */   }
/*     */   
/*     */   public SectionHeader findSection(String name)
/*     */   {
/* 146 */     for (int i = 0; i < this.sectionHeaders.length; i++)
/* 147 */       if (name.equals(this.sectionHeaders[i].getName()))
/* 148 */         return this.sectionHeaders[i];
/* 149 */     return null;
/*     */   }
/*     */   
/*     */   public SectionHeader findSection(int type)
/*     */   {
/* 154 */     for (int i = 0; i < this.sectionHeaders.length; i++)
/* 155 */       if (this.sectionHeaders[i].getType() == type)
/* 156 */         return this.sectionHeaders[i];
/* 157 */     return null;
/*     */   }
/*     */   
/*     */   public Section findSectionData(int type) {
/* 161 */     for (int i = 0; i < this.sectionHeaders.length; i++)
/* 162 */       if (this.sectionHeaders[i].getType() == type)
/* 163 */         return this.sections[i];
/* 164 */     return null;
/*     */   }
/*     */   
/*     */   public Section findSectionDataAt(int address) {
/* 168 */     for (int i = 0; i < this.sectionHeaders.length; i++)
/* 169 */       if (this.sectionHeaders[i].getAddress() == address)
/* 170 */         return this.sections[i];
/* 171 */     return null;
/*     */   }
/*     */   
/*     */   public DynamicInfo getDynamicSection() {
/* 175 */     if (this.dynamic != null) {
/* 176 */       return this.dynamic;
/*     */     }
/* 178 */     Section sectionData = findSectionData(6);
/* 179 */     this.dynamic = new DynamicInfo();
/* 180 */     if (sectionData != null) {
/* 181 */       this.dynamic.read(this, sectionData);
/*     */     }
/* 183 */     return this.dynamic;
/*     */   }
/*     */   
/*     */ 
/*     */   public static ByteBuffer createBuffer(byte[] raw)
/*     */   {
/* 189 */     ByteBuffer buffer = ByteBuffer.wrap(raw);
/* 190 */     buffer.order(ByteOrder.LITTLE_ENDIAN);
/* 191 */     return buffer;
/*     */   }
/*     */   
/*     */   private byte[] loadSection(SectionHeader hdr, SeekableDataSource src) throws Exception
/*     */   {
/* 196 */     byte[] raw = createArray(hdr.getFileSize());
/* 197 */     src.seek(hdr.getFileOffset());
/* 198 */     src.readFully(raw);
/* 199 */     return raw;
/*     */   }
/*     */   
/*     */   private StringSection readStrings(SectionHeader hdr, SeekableDataSource src) throws Exception
/*     */   {
/* 204 */     StringSection result = new StringSection();
/* 205 */     result.read(loadSection(hdr, src));
/* 206 */     return result;
/*     */   }
/*     */   
/*     */   private void readProgramHeaders(SeekableDataSource src) throws Exception
/*     */   {
/* 211 */     byte[] raw = createArray(this.header.getProgramHeaderSize());
/* 212 */     ByteBuffer buffer = createBuffer(raw);
/* 213 */     src.seek(this.header.getProgramHeaderOffset());
/* 214 */     src.readFully(raw);
/*     */     
/* 216 */     this.programHeaders = new ProgramHeader[this.header.getProgramHeaderCount()];
/* 217 */     for (int i = 0; i < this.programHeaders.length; i++)
/*     */     {
/* 219 */       ProgramHeader ph = new ProgramHeader();
/* 220 */       ph.read(buffer);
/* 221 */       this.programHeaders[i] = ph;
/*     */     }
/*     */   }
/*     */   
/*     */   private void readSectionHeaders(SeekableDataSource src) throws Exception
/*     */   {
/* 227 */     byte[] raw = createArray(this.header.getSectionHeaderSize());
/* 228 */     ByteBuffer buffer = createBuffer(raw);
/* 229 */     src.seek(this.header.getSectionHeaderOffset());
/* 230 */     src.readFully(raw);
/*     */     
/* 232 */     this.sectionHeaders = new SectionHeader[this.header.getSectionHeaderCount()];
/* 233 */     for (int i = 0; i < this.sectionHeaders.length; i++)
/*     */     {
/* 235 */       SectionHeader sh = new SectionHeader();
/* 236 */       sh.read(buffer);
/* 237 */       this.sectionHeaders[i] = sh;
/*     */     }
/*     */   }
/*     */   
/*     */   private void readSymbols(SeekableDataSource src, SectionHeader symTab) throws Exception
/*     */   {
/* 243 */     byte[] raw = loadSection(symTab, src);
/* 244 */     ByteBuffer buffer = createBuffer(raw);
/*     */     
/* 246 */     this.symbols = new Symbol[raw.length / 16];
/* 247 */     for (int i = 0; i < this.symbols.length; i++)
/*     */     {
/* 249 */       Symbol s = new Symbol();
/* 250 */       s.read(buffer);
/* 251 */       this.symbols[i] = s;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Joey\Downloads\codejar.jar!\com\emt\proteus\elf\Elf.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */