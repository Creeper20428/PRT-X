/*     */ package com.emt.proteus.runtime32.rtld;
/*     */ 
/*     */ import com.emt.proteus.runtime32.Option;
/*     */ import com.emt.proteus.runtime32.Option.Switch;
/*     */ import com.emt.proteus.runtime32.ThreadContext;
/*     */ import com.emt.proteus.runtime32.memory.MainMemory;
/*     */ import com.emt.proteus.utils.Data;
/*     */ import com.emt.proteus.utils.ILog;
/*     */ import java.io.IOException;
/*     */ import java.util.NoSuchElementException;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class Module
/*     */ {
/*  19 */   private static String[] EMPTY = new String[0];
/*     */   
/*     */   public static final int NOT_LOADED = -1;
/*     */   
/*     */   private final String name;
/*     */   
/*     */   private final String filePath;
/*     */   private CLinkEntry map;
/*  27 */   private static final boolean REL_LOG = Option.log_relocation.value();
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private transient DynamicLinker linker;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected final Symbols symbols;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  51 */   private String[] dependencies = EMPTY;
/*  52 */   private String[] searchPath = EMPTY;
/*     */   
/*     */   protected final Symbols.Symbol nullSymbol;
/*     */   private boolean mainModule;
/*     */   protected int startAddress;
/*     */   
/*  58 */   protected Module(String name) { this(name, name); }
/*     */   
/*     */   protected Module(String name, String filePath) {
/*  61 */     this.symbols = new Symbols();
/*  62 */     this.name = name;
/*  63 */     this.filePath = filePath;
/*  64 */     this.dependencies = EMPTY;
/*  65 */     this.searchPath = EMPTY;
/*  66 */     this.nullSymbol = define("", null, 0, 0, 0);
/*     */   }
/*     */   
/*     */   public void setMap(CLinkEntry map)
/*     */   {
/*  71 */     this.map = map;
/*  72 */     map.setModule(this);
/*     */   }
/*     */   
/*     */   public CLinkEntry getMap() {
/*  76 */     return this.map;
/*     */   }
/*     */   
/*     */   public boolean hasTls() {
/*  80 */     return getTlsSize() != 0;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected Symbols.Symbol define(String name, Object value, int relAddress, int size, int type)
/*     */   {
/*  91 */     return this.symbols.define(this, name, value, relAddress, size, type);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected Symbols.Symbol function(String name, Object value, int relAddress, int size)
/*     */   {
/* 101 */     return this.symbols.define(this, name, value, relAddress, size, 2);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected Symbols.Symbol indirection(String name, Object value, int relAddress, int size)
/*     */   {
/* 111 */     return this.symbols.define(this, name, value, relAddress, size, 10);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected Symbols.Symbol object(String name, Object value, int relAddress, int size)
/*     */   {
/* 121 */     return this.symbols.define(this, name, value, relAddress, size, 1);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public final String getName()
/*     */   {
/* 131 */     return this.name;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public final String getFilePath()
/*     */   {
/* 140 */     return this.filePath;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public final int getGotPlt()
/*     */   {
/* 148 */     return this.map.info.getGotPLT();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public final String[] getDependencies()
/*     */   {
/* 158 */     return this.dependencies;
/*     */   }
/*     */   
/*     */   public int getEntryPoint() {
/* 162 */     return this.map.getEntry();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public final String[] getSearchPaths()
/*     */   {
/* 172 */     return this.searchPath;
/*     */   }
/*     */   
/*     */   public final Symbols.Symbol getNullSymbol() {
/* 176 */     return this.nullSymbol;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Symbols.Symbol find(String name)
/*     */   {
/* 187 */     return this.symbols.get(name);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public final int getBaseAddress()
/*     */   {
/* 197 */     return this.map.getBaseAddress();
/*     */   }
/*     */   
/*     */   public final int getTlsOffset()
/*     */   {
/* 202 */     return this.map.getTlsOffset();
/*     */   }
/*     */   
/*     */   public int getTlsAlign() {
/* 206 */     return this.map.getTlsAlign();
/*     */   }
/*     */   
/*     */   public final int getTlsIndex() {
/* 210 */     return this.map.getTlsModId();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public int getTlsSize()
/*     */   {
/* 218 */     return this.map.getTlsSize();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public int getTlsInit()
/*     */   {
/* 226 */     return this.map.getTlsInit();
/*     */   }
/*     */   
/*     */   public int getTlsInitSize() {
/* 230 */     return this.map.getTlsInitSize();
/*     */   }
/*     */   
/*     */   public void setTlsIndex(int tlsIndex) {
/* 234 */     this.map.setTlsModId(tlsIndex);
/*     */   }
/*     */   
/*     */   public void setTlsOffset(int tlsOffset) {
/* 238 */     this.map.setTlsOffset(tlsOffset);
/*     */   }
/*     */   
/*     */   public int getPhdr() {
/* 242 */     return this.map.getPhr();
/*     */   }
/*     */   
/*     */   public int getPhent() {
/* 246 */     return this.map.getPhEnt();
/*     */   }
/*     */   
/*     */   public int getPhnum() {
/* 250 */     return this.map.getPhNum();
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
/*     */   public final int load(MainMemory memory, int baseAddress, CLinkEntry map)
/*     */     throws IOException
/*     */   {
/* 264 */     int hiAddress = loadImpl(memory, baseAddress, map);
/* 265 */     map.setFilePath(getFilePath());
/* 266 */     return hiAddress;
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
/*     */   public void relocate(DynamicLinker dynamicLinker, MainMemory memory)
/*     */     throws IllegalStateException, NoSuchElementException
/*     */   {
/* 282 */     this.linker = dynamicLinker;
/* 283 */     relocateImpl(dynamicLinker, getBaseAddress(), memory);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void init(ThreadContext executionCtx) {}
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void fini(ThreadContext memory) {}
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void dispose(DynamicLinker linker) {}
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected abstract int loadImpl(Data paramData, int paramInt, CLinkEntry paramCLinkEntry)
/*     */     throws IOException;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected abstract void relocateImpl(DynamicLinker paramDynamicLinker, int paramInt, MainMemory paramMainMemory)
/*     */     throws IllegalStateException, NoSuchElementException;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public DynamicLinker getLinker()
/*     */   {
/* 330 */     return this.linker;
/*     */   }
/*     */   
/*     */   protected void tlsImage(int address, int size, int copySize, int alignment) {
/* 334 */     this.map.setTlsInit(address);
/* 335 */     this.map.setTlsInitSize(copySize);
/* 336 */     this.map.setTlsAlign(alignment);
/* 337 */     this.map.setTlsSize(size);
/*     */   }
/*     */   
/*     */   protected void debug(String fmt, Object... args) {
/* 341 */     this.linker.getLog().debug(getName(), fmt, args);
/*     */   }
/*     */   
/*     */   protected void info(String fmt, Object... args) {
/* 345 */     this.linker.getLog().info(getName(), fmt, args);
/*     */   }
/*     */   
/*     */   protected void warn(String fmt, Object... args)
/*     */   {
/* 350 */     this.linker.getLog().warn(getName(), fmt, args);
/*     */   }
/*     */   
/*     */   protected void relocation(String fmt, Object... args) {
/* 354 */     if (REL_LOG)
/* 355 */       debug(fmt, args);
/*     */   }
/*     */   
/*     */   protected void verbose(boolean verbose, String fmt, Object... args) {
/* 359 */     if (verbose) {
/* 360 */       info(fmt, args);
/*     */     } else {
/* 362 */       debug(fmt, args);
/*     */     }
/*     */   }
/*     */   
/*     */   protected void setElf(int phdr, int phnum, int phent) {
/* 367 */     this.map.setPhr(phdr);
/* 368 */     this.map.setPhNum(phnum);
/* 369 */     this.map.setPhEnt(phent);
/*     */   }
/*     */   
/*     */   protected void setDependencies(String[] dependencies, String[] searchPath) {
/* 373 */     if (searchPath != null) this.searchPath = searchPath;
/* 374 */     if (dependencies != null) this.dependencies = dependencies;
/*     */   }
/*     */   
/* 377 */   protected void setRelativeGotPlt(int address) { throw new UnsupportedOperationException(); }
/*     */   
/*     */   protected void setDynamicAddress(int address)
/*     */   {
/* 381 */     this.map.setDynamic(address);
/*     */   }
/*     */   
/*     */   public int getDynamicAddress() {
/* 385 */     return this.map.getDynamic();
/*     */   }
/*     */   
/*     */   public boolean isMainModule() {
/* 389 */     return this.mainModule;
/*     */   }
/*     */   
/*     */   public void markAsExecutable() {
/* 393 */     this.mainModule = true;
/*     */   }
/*     */   
/*     */ 
/* 397 */   public String toString() { return String.format("%s@%08X", new Object[] { getName(), Integer.valueOf(getBaseAddress()) }); }
/*     */   
/*     */   public static String basename(String filePath) {
/* 400 */     String tmp = filePath.replace('\\', '/');
/* 401 */     int index = tmp.lastIndexOf('/');
/* 402 */     if (index < 0) {
/* 403 */       return filePath;
/*     */     }
/* 405 */     return filePath.substring(index + 1);
/*     */   }
/*     */   
/*     */   protected void setBounds(int loAddress, int hiAddress, int baseAddress)
/*     */   {
/* 410 */     this.map.setBounds(loAddress, hiAddress, baseAddress);
/*     */   }
/*     */   
/*     */   public int getStartAddress() {
/* 414 */     return this.startAddress;
/*     */   }
/*     */ }


/* Location:              C:\Users\Joey\Downloads\codejar.jar!\com\emt\proteus\runtime32\rtld\Module.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */