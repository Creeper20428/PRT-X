/*     */ package com.emt.proteus.runtime32.rtld;
/*     */ 
/*     */ import com.emt.proteus.elf.DynamicInfo;
/*     */ import com.emt.proteus.utils.CStruct;
/*     */ import com.emt.proteus.utils.CStruct.CField;
/*     */ import com.emt.proteus.utils.Data;
/*     */ import com.emt.proteus.utils.Data.Utils;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class CLinkEntry
/*     */   extends CStruct
/*     */ {
/*  52 */   private static int counter = 1;
/*     */   
/*  54 */   public static int SIZEOF = 1024;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*  59 */   public static final CStruct.CField BASE = _integer(0, "l_addr");
/*     */   
/*     */ 
/*     */ 
/*  63 */   public static final CStruct.CField FQNAME = _pointer(BASE, "l_name");
/*     */   
/*     */ 
/*     */ 
/*  67 */   public static final CStruct.CField DYNAMIC = _pointer(FQNAME, "l_ld");
/*     */   
/*     */ 
/*     */ 
/*  71 */   public static final CStruct.CField NEXT = _pointer(DYNAMIC, "l_next");
/*     */   
/*     */ 
/*     */ 
/*  75 */   public static final CStruct.CField PREVIOUS = _pointer(NEXT, "l_previous");
/*     */   
/*  77 */   public static final CStruct.CField REAL_MAP = _pointer(PREVIOUS, "l_real");
/*  78 */   public static final CStruct.CField NAMESPACE = _pointer(REAL_MAP, "l_ns");
/*  79 */   public static final CStruct.CField LIBNAME_LIST = _pointer(NAMESPACE, "libname_list");
/*  80 */   public static final CStruct.CField DYN_ENTRIES = _pointerArray(LIBNAME_LIST, "l_info", DynamicInfo.getMAX_KEY_TABLE_ENTRY());
/*  81 */   public static final CStruct.CField PHDR = _pointer(DYN_ENTRIES, "l_phdr");
/*  82 */   public static final CStruct.CField ENTRY = _pointer(PHDR, "l_entry");
/*  83 */   public static final CStruct.CField PHNUM = _short(ENTRY, "l_phnum");
/*  84 */   public static final CStruct.CField LDNUM = _short(PHNUM, "l_ldnum");
/*  85 */   public static final CStruct.CField SEARCHLIST = _pointerArray(LDNUM, "l_searchlist", 2);
/*  86 */   public static final CStruct.CField SYMB_SEARCHLIST = _pointerArray(SEARCHLIST, "l_symbolic_searchlist", 2);
/*  87 */   public static final CStruct.CField LOADER = _pointer(SYMB_SEARCHLIST, "l_loader");
/*  88 */   public static final CStruct.CField FOUND_VERSIONS = _pointer(LOADER, "l_versions");
/*  89 */   public static final CStruct.CField NVERSIONS = _pointer(FOUND_VERSIONS, "l_nversions");
/*  90 */   public static final CStruct.CField SYMB_HASHTABLE = _pointerArray(NVERSIONS, "symhash", 8);
/*  91 */   public static final CStruct.CField DIRECT_OPEN_COUNT = _integer(SYMB_HASHTABLE, "l_direct_opencount");
/*  92 */   public static final CStruct.CField FLAGS = _integer(DIRECT_OPEN_COUNT, "mixed flags maybe 16bit");
/*  93 */   public static final CStruct.CField SEARCHPATH_DIRS = _pointerArray(FLAGS, "l_rpath_dirs", 2);
/*  94 */   public static final CStruct.CField RELOC_RESULT = _pointer(SEARCHPATH_DIRS, "l_reloc_result");
/*  95 */   public static final CStruct.CField VERSYMS = _pointer(RELOC_RESULT, "l_versyms");
/*  96 */   public static final CStruct.CField ORIGIN = _pointer(VERSYMS, "l_origin");
/*  97 */   public static final CStruct.CField MAP_START = _pointer(ORIGIN, "l_map_start");
/*  98 */   public static final CStruct.CField MAP_END = _pointer(MAP_START, "l_map_end");
/*  99 */   public static final CStruct.CField TEXT_END = _pointer(MAP_END, "l_text_end");
/*     */   
/*     */ 
/* 102 */   public static final CStruct.CField BLOB_SPACER_ONE = _blob(TEXT_END, "undefined", 108);
/*     */   
/* 104 */   public static final CStruct.CField TLS_INIT_IMAGE = _pointer(BLOB_SPACER_ONE, "l_tls_initimage");
/* 105 */   public static final CStruct.CField TLS_INIT_SIZE = _integer(TLS_INIT_IMAGE, "l_tls_initsize");
/* 106 */   public static final CStruct.CField TLS_BLOCK_SIZE = _integer(TLS_INIT_SIZE, "l_tls_blocksize");
/* 107 */   public static final CStruct.CField TLS_ALIGN = _integer(TLS_BLOCK_SIZE, "l_tls_firstbyte_offset");
/* 108 */   public static final CStruct.CField TLS_FB_OFF = _integer(TLS_ALIGN, "l_tls_firstbyte");
/* 109 */   public static final CStruct.CField TLS_OFFSET = _integer(TLS_FB_OFF, "l_tls_offset");
/* 110 */   public static final CStruct.CField TLS_MOD_ID = _integer(TLS_OFFSET, "l_tls_modid");
/* 111 */   public static final CStruct.CField BLOB_SPACER_TWO = _blob(TLS_MOD_ID, "undefined", 4);
/* 112 */   public static final CStruct.CField FILENAME = _cstring(700, "fullName", 299);
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private transient Module module;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private int interpreter;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 132 */   public Info info = new Info();
/*     */   
/* 134 */   public CLinkEntry() { super(SIZEOF); }
/*     */   
/*     */   public CLinkEntry(int address, Data store) {
/* 137 */     super(address, SIZEOF, store);
/*     */   }
/*     */   
/*     */ 
/*     */   public void assign(int address, Data data)
/*     */   {
/* 143 */     super.assign(address, data);
/*     */   }
/*     */   
/*     */ 
/* 147 */   public String getFilePath() { return FILENAME.stringValue(this); }
/*     */   
/*     */   public void setFilePath(String val) {
/* 150 */     FILENAME.setString(this, val);
/* 151 */     FQNAME.set(this, address(FILENAME));
/*     */   }
/*     */   
/*     */   public int getNext() {
/* 155 */     return NEXT.intValue(this);
/*     */   }
/*     */   
/* 158 */   public void setNext(int value) { NEXT.set(this, value); }
/*     */   
/*     */   public void setNext(CLinkEntry value) {
/* 161 */     NEXT.set(this, value != null ? value.addressOf() : 0L);
/*     */   }
/*     */   
/* 164 */   public int getPrevious() { return PREVIOUS.intValue(this); }
/*     */   
/*     */   public void setPrevious(CLinkEntry value) {
/* 167 */     PREVIOUS.set(this, value != null ? value.addressOf() : 0L);
/*     */   }
/*     */   
/* 170 */   public void setPrevious(int value) { PREVIOUS.set(this, value); }
/*     */   
/*     */   public int getTlsModId()
/*     */   {
/* 174 */     return TLS_MOD_ID.intValue(this);
/*     */   }
/*     */   
/* 177 */   public void setTlsModId(int value) { TLS_MOD_ID.set(this, value); }
/*     */   
/*     */   public int getTlsAlign() {
/* 180 */     return TLS_ALIGN.intValue(this);
/*     */   }
/*     */   
/* 183 */   public void setTlsAlign(int value) { TLS_ALIGN.set(this, value); }
/*     */   
/*     */   public int getTlsInit() {
/* 186 */     return TLS_INIT_IMAGE.intValue(this);
/*     */   }
/*     */   
/* 189 */   public void setTlsInit(int value) { TLS_INIT_IMAGE.set(this, value); }
/*     */   
/*     */   public int getTlsInitSize() {
/* 192 */     return TLS_INIT_SIZE.intValue(this);
/*     */   }
/*     */   
/* 195 */   public void setTlsInitSize(int value) { TLS_INIT_SIZE.set(this, value); }
/*     */   
/*     */   public int getTlsSize() {
/* 198 */     return TLS_BLOCK_SIZE.intValue(this);
/*     */   }
/*     */   
/* 201 */   public void setTlsSize(int value) { TLS_BLOCK_SIZE.set(this, value); }
/*     */   
/*     */   public int getTlsOffset()
/*     */   {
/* 205 */     return TLS_OFFSET.intValue(this);
/*     */   }
/*     */   
/* 208 */   public void setTlsOffset(int value) { TLS_OFFSET.set(this, value); }
/*     */   
/*     */   public int getMapStart()
/*     */   {
/* 212 */     return MAP_START.intValue(this);
/*     */   }
/*     */   
/*     */   public int getMapEnd() {
/* 216 */     return MAP_END.intValue(this);
/*     */   }
/*     */   
/* 219 */   public int getBaseAddress() { return BASE.intValue(this); }
/*     */   
/*     */   public int getPhr()
/*     */   {
/* 223 */     return PHDR.intValue(this);
/*     */   }
/*     */   
/* 226 */   public void setPhr(int ptr) { PHDR.set(this, ptr); }
/*     */   
/*     */   public int getPhNum()
/*     */   {
/* 230 */     return PHNUM.intValue(this);
/*     */   }
/*     */   
/* 233 */   public void setPhNum(int ptr) { PHNUM.set(this, ptr); }
/*     */   
/*     */   public int getDynamic() {
/* 236 */     return DYNAMIC.intValue(this);
/*     */   }
/*     */   
/* 239 */   public void setDynamic(int ptr) { DYNAMIC.set(this, ptr); }
/*     */   
/*     */   public int getEntry()
/*     */   {
/* 243 */     int ptr = ENTRY.intValue(this);
/* 244 */     return ptr;
/*     */   }
/*     */   
/* 247 */   public void setEntry(int ptr) { ENTRY.set(this, ptr); }
/*     */   
/*     */ 
/*     */ 
/*     */   public void markAsExecutable() {}
/*     */   
/*     */ 
/*     */   public Module getModule()
/*     */   {
/* 256 */     return this.module;
/*     */   }
/*     */   
/*     */   public void setModule(Module module) {
/* 260 */     this.module = module;
/*     */   }
/*     */   
/*     */   public void setBounds(int mapStart, int mapEnd, int baseAddress) {
/* 264 */     BASE.set(this, baseAddress);
/* 265 */     MAP_START.set(this, mapStart);
/* 266 */     MAP_END.set(this, mapEnd);
/*     */   }
/*     */   
/*     */   public int getPhEnt() {
/* 270 */     return 0;
/*     */   }
/*     */   
/*     */ 
/*     */   public void setPhEnt(int phent) {}
/*     */   
/*     */ 
/*     */   public void tlsImage(int address, int size, int copySize, int alignment)
/*     */   {
/* 279 */     TLS_INIT_IMAGE.set(this, address);
/* 280 */     TLS_INIT_SIZE.set(this, copySize);
/* 281 */     TLS_ALIGN.set(this, alignment);
/* 282 */     TLS_BLOCK_SIZE.set(this, size);
/*     */   }
/*     */   
/*     */   public void setInterpreter(int interpreter) {
/* 286 */     this.interpreter = interpreter;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/* 291 */   private static final int[] DYNAMIC_ADJUSTED_VALUES = { 4, 3, 5, 6, 17, 7, 23, 1879048176, 1879047925 };
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private boolean isLoaded()
/*     */   {
/* 304 */     boolean loaded = getMapStart() != getMapEnd();
/* 305 */     if (loaded) {
/* 306 */       System.err.printf("LOADED TEST (%08X == %08X) : %s \n", new Object[] { Integer.valueOf(getMapStart()), Integer.valueOf(getMapEnd()), getFilePath() });
/*     */     }
/*     */     else {
/* 309 */       System.err.printf("------------(%08X == %08X) : %s \n", new Object[] { Integer.valueOf(getMapStart()), Integer.valueOf(getMapEnd()), getFilePath() });
/*     */     }
/* 311 */     return loaded;
/*     */   }
/*     */   
/*     */ 
/* 315 */   public String toString() { return String.format(" [%08x-%08x] GOT=%08x map=%5x prev=%5x next=%5x phdr=%08X phnum=%d modid=%d (%08X) %s", new Object[] { Integer.valueOf(getMapStart()), Integer.valueOf(getMapEnd()), Integer.valueOf(this.info.getGotPLT()), Integer.valueOf(addressOf()), Integer.valueOf(getPrevious()), Integer.valueOf(getNext()), Integer.valueOf(getPhr()), Integer.valueOf(getPhNum()), Integer.valueOf(getTlsModId()), Integer.valueOf(address(TLS_MOD_ID)), getFilePath() }); }
/*     */   
/*     */   public class Info {
/*     */     public Info() {}
/*     */     
/*     */     public void setAddress(int tag, int value) {
/* 321 */       int index = DynamicInfo.toIndex(tag);
/* 322 */       CLinkEntry.DYN_ENTRIES.set(CLinkEntry.this, index, value);
/*     */     }
/*     */     
/* 325 */     public int getAddress(int tag) { int index = DynamicInfo.toIndex(tag);
/* 326 */       return (int)CLinkEntry.DYN_ENTRIES.get(CLinkEntry.this, index);
/*     */     }
/*     */     
/*     */     public int getValue(int tag) {
/* 330 */       int address = getAddress(tag);
/* 331 */       if (address == 0) return 0;
/* 332 */       return CLinkEntry.this.getData().getDoubleWord(address + 4);
/*     */     }
/*     */     
/*     */     public int getGotPLT() {
/* 336 */       return getValue(3);
/*     */     }
/*     */     
/* 339 */     public String getSoName() { return Data.Utils.getString(CLinkEntry.this.getData(), getValue(14)); }
/*     */     
/*     */ 
/*     */     public void readDynamicTags(boolean adjust)
/*     */     {
/* 344 */       int dyn$ = CLinkEntry.DYNAMIC.intValue(CLinkEntry.this);
/* 345 */       if (dyn$ == 0) return;
/* 346 */       Data source = CLinkEntry.this.getData();
/*     */       for (;;) {
/* 348 */         int dTag = source.getDoubleWord(dyn$);
/* 349 */         int val = source.getDoubleWord(dyn$ + 4);
/* 350 */         if (dTag == 0) break;
/* 351 */         setAddress(dTag, dyn$);
/* 352 */         assert (getAddress(dTag) == dyn$);
/* 353 */         dyn$ += 8;
/*     */       }
/*     */       
/* 356 */       if (adjust) {
/* 357 */         int baseAddress = CLinkEntry.this.getBaseAddress();
/* 358 */         for (int i = 0; i < CLinkEntry.DYNAMIC_ADJUSTED_VALUES.length; i++) {
/* 359 */           int tag = CLinkEntry.DYNAMIC_ADJUSTED_VALUES[i];
/* 360 */           int addr = getAddress(tag);
/*     */           
/* 362 */           if (addr != 0) {
/* 363 */             int value = source.getDoubleWord(addr + 4);
/*     */             
/* 365 */             source.setDoubleWord(addr + 4, value + baseAddress);
/*     */           }
/* 367 */           assert (getAddress(tag) == addr);
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Joey\Downloads\codejar.jar!\com\emt\proteus\runtime32\rtld\CLinkEntry.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */