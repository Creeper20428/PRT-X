/*     */ package com.emt.proteus.elf;
/*     */ 
/*     */ import com.emt.proteus.utils.Utils;
/*     */ import java.nio.ByteBuffer;
/*     */ import java.util.HashMap;
/*     */ import java.util.Iterator;
/*     */ import java.util.LinkedHashSet;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import java.util.TreeMap;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class DynamicInfo
/*     */ {
/*     */   public static final int DT_NULL = 0;
/*     */   public static final int DT_NEEDED = 1;
/*     */   public static final int DT_PLTRELSZ = 2;
/*     */   public static final int DT_PLTGOT = 3;
/*     */   public static final int DT_HASH = 4;
/*     */   public static final int DT_STRTAB = 5;
/*     */   public static final int DT_SYMTAB = 6;
/*     */   public static final int DT_RELA = 7;
/*     */   public static final int DT_RELASZ = 8;
/*     */   public static final int DT_RELAENT = 9;
/*     */   public static final int DT_STRSZ = 10;
/*     */   public static final int DT_SYMENT = 11;
/*     */   public static final int DT_INIT = 12;
/*     */   public static final int DT_FINI = 13;
/*     */   public static final int DT_SONAME = 14;
/*     */   public static final int DT_RPATH = 15;
/*     */   public static final int DT_SYMBOLIC = 16;
/*     */   public static final int DT_REL = 17;
/*     */   public static final int DT_RELSZ = 18;
/*     */   public static final int DT_RELENT = 19;
/*     */   public static final int DT_PLTREL = 20;
/*     */   public static final int DT_DEBUG = 21;
/*     */   public static final int DT_TEXTREL = 22;
/*     */   public static final int DT_JMPREL = 23;
/*     */   public static final int DT_BIND_NOW = 24;
/*     */   public static final int DT_INIT_ARRAY = 25;
/*     */   public static final int DT_FINI_ARRAY = 26;
/*     */   public static final int DT_INIT_ARRAYSZ = 27;
/*     */   public static final int DT_FINI_ARRAYSZ = 28;
/*     */   public static final int DT_RUNPATH = 29;
/*     */   public static final int DT_FLAGS = 30;
/*     */   public static final int DT_ENCODING = 32;
/*     */   public static final int DT_PREINIT_ARRAY = 32;
/*     */   public static final int DT_PREINIT_ARRAYSZ = 33;
/*     */   private static final int DT_NUM = 34;
/*     */   private static final int DT_THISPROCNUM = 0;
/*     */   private static final int DT_LOOS = 1610612736;
/*     */   private static final int DT_HIOS = 1879048191;
/*     */   private static final int DT_LOPROC = 1879048192;
/*     */   private static final int DT_HIPROC = Integer.MAX_VALUE;
/*     */   public static final int DT_VALRNGLO = 1879047424;
/*     */   public static final int DT_GNU_PRELINKED = 1879047669;
/*     */   public static final int DT_GNU_CONFLICTSZ = 1879047670;
/*     */   public static final int DT_GNU_LIBLISTSZ = 1879047671;
/*     */   public static final int DT_CHECKSUM = 1879047672;
/*     */   public static final int DT_PLTPADSZ = 1879047673;
/*     */   public static final int DT_MOVEENT = 1879047674;
/*     */   public static final int DT_MOVESZ = 1879047675;
/*     */   public static final int DT_FEATURE_1 = 1879047676;
/*     */   public static final int DT_POSFLAG_1 = 1879047677;
/*     */   public static final int DT_SYMINSZ = 1879047678;
/*     */   public static final int DT_SYMINENT = 1879047679;
/*     */   private static final int DT_VALRNGHI = 1879047679;
/*     */   private static final int DT_VALNUM = 12;
/*     */   private static final int DT_ADDRRNGLO = 1879047680;
/*     */   public static final int DT_GNU_HASH = 1879047925;
/*     */   public static final int DT_TLSDESC_PLT = 1879047926;
/*     */   public static final int DT_TLSDESC_GOT = 1879047927;
/*     */   public static final int DT_GNU_CONFLICT = 1879047928;
/*     */   public static final int DT_GNU_LIBLIST = 1879047929;
/*     */   public static final int DT_CONFIG = 1879047930;
/*     */   public static final int DT_DEPAUDIT = 1879047931;
/*     */   public static final int DT_AUDIT = 1879047932;
/*     */   public static final int DT_PLTPAD = 1879047933;
/*     */   public static final int DT_MOVETAB = 1879047934;
/*     */   public static final int DT_SYMINFO = 1879047935;
/*     */   private static final int DT_ADDRRNGHI = 1879047935;
/*     */   public static final int DT_VERSYM = 1879048176;
/*     */   public static final int DT_RELACOUNT = 1879048185;
/*     */   public static final int DT_RELCOUNT = 1879048186;
/*     */   public static final int DT_FLAGS_1 = 1879048187;
/*     */   public static final int DT_VERDEF = 1879048188;
/*     */   public static final int DT_VERDEFNUM = 1879048189;
/*     */   public static final int DT_VERNEED = 1879048190;
/*     */   public static final int DT_VERNEEDNUM = 1879048191;
/*     */   private static final int DT_ADDRNUM = 11;
/*     */   private static final int DT_VERSIONTAGNUM = 16;
/*     */   public static final int DT_AUXILIARY = 2147483645;
/*     */   public static final int DT_FILTER = Integer.MAX_VALUE;
/*     */   private static final int DT_EXTRANUM = 3;
/*     */   private static final int MAX_KEY_TABLE_ENTRY = 76;
/* 294 */   private static final Map<Integer, String> _INDEX = Utils.createConstantMap(DynamicInfo.class);
/*     */   
/*     */   static
/*     */   {
/* 298 */     Set<Integer> integers = _INDEX.keySet();
/* 299 */     Map<Integer, String> indexed = new TreeMap();
/* 300 */     for (Iterator<Integer> iterator = integers.iterator(); iterator.hasNext();) {
/* 301 */       Integer raw = (Integer)iterator.next();
/* 302 */       int converted = toIndex(raw.intValue());
/* 303 */       Integer index = Integer.valueOf(converted);
/* 304 */       String name = (String)_INDEX.get(raw);
/* 305 */       String current = (String)indexed.get(index);
/* 306 */       if (current != null) {
/* 307 */         throw new IllegalStateException("Collision detected " + current + " and " + name);
/*     */       }
/* 309 */       indexed.put(index, name);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/* 314 */   private StringSection strings = new StringSection();
/* 315 */   private SymbolTable symbols = new SymbolTable(this.strings);
/*     */   private int[] keyTable;
/*     */   private int pltGot;
/*     */   private int init;
/*     */   private int initarray;
/*     */   private int initarraysz;
/*     */   private int fini;
/*     */   private boolean bindNow;
/*     */   private boolean writeReadOnly;
/*     */   private boolean symbolic;
/*     */   private Relocations relTable;
/*     */   private Relocations relaTable;
/*     */   private Relocations jmpRelTable;
/* 328 */   private String[] dependencies = new String[0];
/* 329 */   private String[] paths = new String[0];
/*     */   
/*     */   public static int getMAX_KEY_TABLE_ENTRY() {
/* 332 */     return 76;
/*     */   }
/*     */   
/*     */   public void read(Elf elf, Section buffer)
/*     */   {
/* 337 */     if (buffer == null) return;
/* 338 */     this.keyTable = new int[76];
/* 339 */     Map<Integer, int[]> tags = readTags(buffer.asByteBuffer(), this.keyTable);
/*     */     
/*     */ 
/*     */ 
/* 343 */     int[] strTab = (int[])tags.get(Integer.valueOf(5));
/* 344 */     int[] strSize = (int[])tags.get(Integer.valueOf(10));
/* 345 */     if (strTab != null) {
/* 346 */       Section stringSection = elf.findSectionDataAt(strTab[0]);
/* 347 */       this.strings.read(stringSection.getData());
/*     */     }
/*     */     
/* 350 */     int[] symTab = (int[])tags.get(Integer.valueOf(6));
/* 351 */     int[] symbolSize = (int[])tags.get(Integer.valueOf(11));
/* 352 */     Section symbolTable = elf.findSectionDataAt(symTab[0]);
/* 353 */     this.symbols.read(symbolTable, symbolSize[0]);
/*     */     
/* 355 */     int[] depends = (int[])tags.get(Integer.valueOf(1));
/* 356 */     LinkedHashSet<String> needed = new LinkedHashSet();
/* 357 */     for (int i = 0; i < depends.length; i++) {
/* 358 */       int strTabOffset = depends[i];
/* 359 */       String s = this.strings.getNameAtIndex(strTabOffset);
/* 360 */       needed.add(s);
/*     */     }
/*     */     
/*     */ 
/*     */ 
/* 365 */     this.dependencies = ((String[])needed.toArray(new String[needed.size()]));
/* 366 */     this.pltGot = _get(tags, 3);
/*     */     
/*     */ 
/*     */ 
/* 370 */     this.init = _get(tags, 12, 0);
/* 371 */     this.fini = _get(tags, 13, 0);
/* 372 */     this.bindNow = _contains(tags, 24);
/* 373 */     this.writeReadOnly = _contains(tags, 22);
/* 374 */     this.symbolic = _contains(tags, 16);
/* 375 */     this.initarray = _get(tags, 25, 0);
/* 376 */     this.initarraysz = _get(tags, 27, 0);
/*     */     
/*     */ 
/*     */ 
/* 380 */     this.relTable = readRelocationTable(elf, this.symbols, _get(tags, 17), _get(tags, 18), _get(tags, 19));
/* 381 */     this.relaTable = readRelocationTable(elf, this.symbols, _get(tags, 7), _get(tags, 8), _get(tags, 9));
/* 382 */     this.jmpRelTable = readRelocationTable(elf, this.symbols, _get(tags, 23), _get(tags, 2), 0);
/*     */     
/* 384 */     if (_contains(tags, 15)) {
/* 385 */       String paths = this.strings.getNameAtIndex(_get(tags, 15));
/* 386 */       if (paths != null) {
/* 387 */         this.paths = paths.split(":");
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   private static Relocations readRelocationTable(Elf elf, SymbolTable symbols, int address, int size, int entrySize) {
/* 393 */     if (address <= 0) { return new Relocations();
/*     */     }
/* 395 */     Section section = elf.findSectionDataAt(address);
/* 396 */     String name = section.getHeader().getName();
/* 397 */     SectionHeader header = section.getHeader();
/* 398 */     ByteBuffer byteBuffer = section.asByteBuffer();
/* 399 */     int type = header.getType();
/* 400 */     boolean rela = type == 4;
/* 401 */     entrySize = rela ? Math.max(12, entrySize) : Math.max(8, entrySize);
/* 402 */     Relocations.Entry[] entries = new Relocations.Entry[size / entrySize];
/* 403 */     int i = 0; for (int index = 0; i < size; index++) {
/* 404 */       int offset = byteBuffer.getInt(i);
/* 405 */       int info = byteBuffer.getInt(i + 4);
/* 406 */       int addend = rela ? byteBuffer.getInt(i + 8) : 0;
/* 407 */       Relocations.Entry entry = new Relocations.Entry(offset, info, addend, rela, null);
/* 408 */       int symbolIndex = entry.getSymbolIndex();
/* 409 */       entry.setSymbol(symbols.getSymbol(symbolIndex));
/* 410 */       entries[index] = entry;i += entrySize;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 412 */     return new Relocations(name, rela, section.getHeader().getAddress(), entries);
/*     */   }
/*     */   
/*     */   private static int _get(Map<Integer, int[]> tags, int field)
/*     */   {
/* 417 */     return _get(tags, field, -1);
/*     */   }
/*     */   
/*     */   private static int _get(Map<Integer, int[]> tags, int field, int _default) {
/* 421 */     int[] ints = (int[])tags.get(Integer.valueOf(field));
/* 422 */     if ((ints != null) && (ints.length > 0)) {
/* 423 */       return ints[0];
/*     */     }
/* 425 */     return _default;
/*     */   }
/*     */   
/*     */   private static boolean _contains(Map<Integer, int[]> tags, int field)
/*     */   {
/* 430 */     int[] ints = (int[])tags.get(Integer.valueOf(field));
/* 431 */     return (ints != null) && (ints.length > 0);
/*     */   }
/*     */   
/*     */ 
/*     */   private static Map<Integer, int[]> readTags(ByteBuffer buffer, int[] keyTable)
/*     */   {
/* 437 */     Map<Integer, int[]> values = new HashMap();
/* 438 */     values.put(Integer.valueOf(1), new int[0]);
/*     */     for (;;) {
/* 440 */       int tag = buffer.getInt();
/* 441 */       int value = buffer.getInt();
/* 442 */       switch (tag) {
/*     */       case 0: 
/* 444 */         return values;
/*     */       }
/*     */       
/* 447 */       int[] vals = (int[])values.get(Integer.valueOf(tag));
/*     */       int[] newvals;
/* 449 */       int[] newvals; if (vals == null) {
/* 450 */         newvals = new int[] { value };
/*     */       } else {
/* 452 */         newvals = new int[vals.length + 1];
/* 453 */         System.arraycopy(vals, 0, newvals, 0, vals.length);
/* 454 */         newvals[vals.length] = value;
/*     */       }
/* 456 */       if (tag < 76) {
/* 457 */         keyTable[tag] = value;
/*     */       }
/* 459 */       values.put(Integer.valueOf(tag), newvals);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public static String toString(int type)
/*     */   {
/* 468 */     String x = (String)_INDEX.get(Integer.valueOf(type));
/* 469 */     if (x == null) x = String.format("PROC/unspecified %8X", new Object[] { Integer.valueOf(type) });
/* 470 */     return x;
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
/*     */   public int get(int tag)
/*     */   {
/* 531 */     return this.keyTable[tag];
/*     */   }
/*     */   
/*     */   public int getPltGot() {
/* 535 */     return get(3);
/*     */   }
/*     */   
/*     */   public int getInit() {
/* 539 */     return get(12);
/*     */   }
/*     */   
/*     */   public int getInitArraySz() {
/* 543 */     return get(27);
/*     */   }
/*     */   
/*     */   public int getInitArray() {
/* 547 */     return get(25);
/*     */   }
/*     */   
/* 550 */   public int getFiniArraySz() { return get(28); }
/*     */   
/*     */   public int getFiniArray()
/*     */   {
/* 554 */     return get(26);
/*     */   }
/*     */   
/*     */   public int getFini() {
/* 558 */     return get(13);
/*     */   }
/*     */   
/*     */   public boolean isBindNow() {
/* 562 */     return get(24) != 0;
/*     */   }
/*     */   
/*     */   public boolean isWriteReadOnly() {
/* 566 */     return this.writeReadOnly;
/*     */   }
/*     */   
/*     */   public boolean isSymbolic() {
/* 570 */     return this.symbolic;
/*     */   }
/*     */   
/*     */   public Relocations getRelTable() {
/* 574 */     return this.relTable;
/*     */   }
/*     */   
/*     */   public Relocations getRelaTable() {
/* 578 */     return this.relaTable;
/*     */   }
/*     */   
/*     */   public Relocations getJmpRelTable() {
/* 582 */     return this.jmpRelTable;
/*     */   }
/*     */   
/*     */   public String[] getDependencies() {
/* 586 */     return this.dependencies;
/*     */   }
/*     */   
/*     */   public String[] getSearchPath() {
/* 590 */     return this.paths;
/*     */   }
/*     */   
/*     */   public String getSoName() {
/* 594 */     return null;
/*     */   }
/*     */   
/*     */   public SymbolTable getSymbols() {
/* 598 */     return this.symbols;
/*     */   }
/*     */   
/*     */   public static DynamicInfo read(Elf elf)
/*     */   {
/* 603 */     Section sectionData = elf.findSectionData(6);
/* 604 */     DynamicInfo dynamic = null;
/* 605 */     if (sectionData != null) {
/* 606 */       dynamic = new DynamicInfo();
/*     */     }
/*     */     
/* 609 */     return dynamic;
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
/*     */   public static int toIndex(int dtTag)
/*     */   {
/* 633 */     dtTag &= 0x7FFFFFFF;
/*     */     
/* 635 */     if (Utils.inBounds(dtTag, 0, 33)) {
/* 636 */       return dtTag;
/*     */     }
/* 638 */     if (Utils.inBounds(dtTag, 1879048192, 1879048192)) {
/* 639 */       return 34 + (dtTag - 1879048192);
/*     */     }
/* 641 */     if (Utils.inBounds(dtTag, 1879048176, 1879048191)) {
/* 642 */       return 34 + (1879048191 - dtTag);
/*     */     }
/* 644 */     if (Utils.inBounds(dtTag, 2147483645, Integer.MAX_VALUE)) {
/* 645 */       return 50 + dtTag;
/*     */     }
/* 647 */     if (Utils.inBounds(dtTag, 1879047424, 1879047679)) {
/* 648 */       return 53 + (1879047679 - dtTag);
/*     */     }
/* 650 */     if (Utils.inBounds(dtTag, 1879047680, 1879047935)) {
/* 651 */       return 65 + (1879047935 - dtTag);
/*     */     }
/* 653 */     throw new IllegalArgumentException("Unhandled dt tag");
/*     */   }
/*     */ }


/* Location:              C:\Users\Joey\Downloads\codejar.jar!\com\emt\proteus\elf\DynamicInfo.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */