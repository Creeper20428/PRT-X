/*     */ package com.emt.proteus.runtime32.rtld;
/*     */ 
/*     */ import com.emt.proteus.elf.DynamicInfo;
/*     */ import com.emt.proteus.elf.Elf;
/*     */ import com.emt.proteus.elf.ElfHeader;
/*     */ import com.emt.proteus.elf.ProgramHeader;
/*     */ import com.emt.proteus.elf.ProgramSegment;
/*     */ import com.emt.proteus.elf.Relocations;
/*     */ import com.emt.proteus.elf.Relocations.Entry;
/*     */ import com.emt.proteus.elf.SeekableDataSource;
/*     */ import com.emt.proteus.elf.SeekableFile;
/*     */ import com.emt.proteus.elf.Symbol;
/*     */ import com.emt.proteus.elf.SymbolTable;
/*     */ import com.emt.proteus.runtime32.ThreadContext;
/*     */ import com.emt.proteus.runtime32.memory.MainMemory;
/*     */ import com.emt.proteus.utils.Data;
/*     */ import com.emt.proteus.utils.Data.Utils;
/*     */ import com.emt.proteus.utils.Utils;
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.io.PrintStream;
/*     */ import java.net.MalformedURLException;
/*     */ import java.net.URL;
/*     */ import java.util.Iterator;
/*     */ import java.util.NoSuchElementException;
/*     */ 
/*     */ public class ElfModule extends Module
/*     */ {
/*     */   public static final int UNDEFINED = 0;
/*     */   private String interpreter;
/*     */   private DynamicInfo dynamicInfo;
/*     */   private URL url;
/*     */   
/*     */   public ElfModule(String fileName) throws MalformedURLException
/*     */   {
/*  36 */     this(new File(fileName).toURI().toURL());
/*     */   }
/*     */   
/*     */   public ElfModule(URL url) {
/*  40 */     super(basename(url.getPath()), url.getPath());
/*  41 */     this.url = url;
/*     */   }
/*     */   
/*     */   protected int loadImpl(Data memory, int baseAddress, CLinkEntry map) throws IOException
/*     */   {
/*  46 */     Elf elf = this.url != null ? open(this.url) : open(getFilePath());
/*     */     
/*  48 */     ElfHeader header = elf.getHeader();
/*  49 */     int phent = header.getProgramHeaderEntrySize();
/*  50 */     int phnum = header.getProgramHeaderCount();
/*  51 */     int entry = header.getEntryPoint();
/*  52 */     int phoff = header.getProgramHeaderOffset();
/*  53 */     int phsize = phnum * phent;
/*  54 */     int phdr$ = 0;
/*  55 */     int dynamic$ = 0;
/*     */     
/*  57 */     int minimum = Integer.MAX_VALUE;
/*  58 */     int maximum = Integer.MIN_VALUE;
/*     */     
/*  60 */     for (int i = 0; i < phnum; i++) {
/*  61 */       ProgramHeader phdr = elf.getProgramHeader(i);
/*  62 */       if (phdr.getType() == 1) {
/*  63 */         int segmentStart = phdr.getVirtualAddress();
/*  64 */         int segmentSize = phdr.getMemorySize();
/*  65 */         int segmentEnd = segmentStart + segmentSize;
/*  66 */         int dataEnd = segmentStart + segmentStart + phdr.getFileSize();
/*  67 */         if ((phdr$ == 0) && 
/*  68 */           (segmentStart <= phoff) && (phoff + phsize < dataEnd)) {
/*  69 */           phdr$ = phoff;
/*     */         }
/*     */         
/*  72 */         minimum = Math.min(segmentStart, minimum);
/*  73 */         int end = Utils.align(segmentEnd, 4096);
/*  74 */         maximum = Math.max(end, maximum);
/*  75 */       } else if (phdr.getType() == 6) {
/*  76 */         phdr$ = phdr.getVirtualAddress();
/*     */       }
/*     */     }
/*     */     
/*  80 */     if (minimum != 0)
/*     */     {
/*  82 */       baseAddress = 0;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*  87 */     if (phdr$ == 0) {
/*  88 */       System.err.println("No PHDR Segment Found!");
/*     */     } else {
/*  90 */       phdr$ += baseAddress;
/*     */     }
/*     */     
/*  93 */     map.setBounds(minimum + baseAddress, maximum + baseAddress, baseAddress);
/*     */     
/*     */ 
/*     */ 
/*  97 */     int interpreter$ = 0;
/*  98 */     for (int i = 0; i < phnum; i++) {
/*  99 */       ProgramHeader phdr = elf.getProgramHeader(i);
/*     */       
/* 101 */       long vaddr = 0xFFFFFFFF & phdr.getVirtualAddress() + (baseAddress & 0xFFFFFFFF);
/* 102 */       int size = phdr.getMemorySize();
/* 103 */       int flags = phdr.getFlags();
/* 104 */       switch (phdr.getType())
/*     */       {
/*     */       case 1: 
/* 107 */         ProgramSegment programSegment = elf.getProgramSegment(i);
/* 108 */         byte[] data = programSegment.getData();
/* 109 */         loadSegment(memory, vaddr, data, size, flags);
/*     */         
/* 111 */         break;
/*     */       case 7: 
/* 113 */         map.tlsImage((int)vaddr, size, phdr.getFileSize(), phdr.getAlignment());
/* 114 */         break;
/*     */       case 2: 
/* 116 */         dynamic$ = (int)vaddr;
/* 117 */         break;
/*     */       case 3: 
/* 119 */         interpreter$ = (int)vaddr;
/* 120 */         map.setInterpreter(interpreter$);
/*     */       }
/*     */       
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 129 */     entry += baseAddress;
/* 130 */     map.setPhNum(phnum);
/* 131 */     map.setPhEnt(phent);
/* 132 */     map.setPhr(phdr$);
/* 133 */     map.setEntry(entry);
/* 134 */     map.setDynamic(dynamic$);
/* 135 */     if (dynamic$ != 0)
/*     */     {
/* 137 */       this.dynamicInfo = elf.getDynamicSection();
/* 138 */       loadDynamic(memory, dynamic$);
/* 139 */       map.info.readDynamicTags(true);
/*     */     }
/* 141 */     if (interpreter$ != 0) {
/* 142 */       this.interpreter = Data.Utils.getString(memory, interpreter$);
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 151 */     return maximum;
/*     */   }
/*     */   
/*     */   private void loadDynamic(Data memory, int dynamic$) {
/* 155 */     setDependencies(this.dynamicInfo.getDependencies(), null);
/*     */   }
/*     */   
/*     */   private long loadSegment(Data memory, long vaddr, byte[] data, int size, int flags) {
/* 159 */     Data.Utils.storeClear(memory, (int)vaddr, data, size);
/* 160 */     MainMemory mainMemory = (MainMemory)memory;
/* 161 */     if ((flags & 0x2) == 0) {
/* 162 */       for (long addr = vaddr; addr < vaddr + size; addr += 4096L) {
/* 163 */         mainMemory.setPageReadOnly((int)addr);
/*     */       }
/*     */     }
/* 166 */     boolean execute = (flags & 0x1) != 0;
/*     */     
/* 168 */     for (long addr = vaddr; addr < vaddr + size; addr += 4096L)
/* 169 */       mainMemory.setPageExecutePermission((int)addr, execute);
/* 170 */     return addr;
/*     */   }
/*     */   
/*     */   protected void relocateImpl(DynamicLinker dynamicLinker, int baseAddress, MainMemory memory)
/*     */     throws IllegalStateException, NoSuchElementException
/*     */   {
/* 176 */     CLinkEntry map = getMap();
/* 177 */     if (this.dynamicInfo != null) {
/* 178 */       apply(dynamicLinker, memory, this.dynamicInfo.getRelTable());
/* 179 */       apply(dynamicLinker, memory, this.dynamicInfo.getRelaTable());
/* 180 */       apply(dynamicLinker, memory, this.dynamicInfo.getJmpRelTable());
/* 181 */       registerSymbols(dynamicLinker);
/*     */     }
/*     */   }
/*     */   
/*     */   public void registerSymbols(DynamicLinker dynamicLinker)
/*     */   {
/* 187 */     if (this.dynamicInfo != null) {
/* 188 */       SymbolTable symbols = this.dynamicInfo.getSymbols();
/* 189 */       Iterator<Symbol> iterate = symbols.iterate();
/* 190 */       while (iterate.hasNext()) {
/* 191 */         Symbol elfSymbol = (Symbol)iterate.next();
/* 192 */         String name = elfSymbol.getName();
/* 193 */         if ((elfSymbol.isDefined()) && 
/* 194 */           (elfSymbol.isFunction())) {
/* 195 */           Symbols.Symbol rtSymbol = this.symbols.get(name);
/* 196 */           if (rtSymbol == null)
/*     */           {
/* 198 */             rtSymbol = define(symbols, elfSymbol);
/*     */             
/* 200 */             dynamicLinker.ensureSymbolFunction(rtSymbol);
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */   
/* 207 */   private Symbols.Symbol define(SymbolTable symbols, Symbol elfSymbol) { String name = elfSymbol.getName();
/*     */     
/* 209 */     Symbols.Symbol rtSymbol = this.symbols.define(this, name, null, elfSymbol.getValue(), elfSymbol.getSize(), elfSymbol.getType());
/* 210 */     int location = symbols.getLocation(elfSymbol);
/* 211 */     rtSymbol.assignLocation(location);
/*     */     
/*     */ 
/* 214 */     return rtSymbol;
/*     */   }
/*     */   
/*     */ 
/*     */   public Symbols.Symbol find(String name)
/*     */   {
/* 220 */     Symbols.Symbol dynSymbol = super.find(name);
/* 221 */     if (dynSymbol == null) {
/* 222 */       SymbolTable symbolTable = this.dynamicInfo.getSymbols();
/* 223 */       Symbol symbol = symbolTable.get(name);
/* 224 */       if ((symbol != null) && (symbol.isExported())) {
/* 225 */         dynSymbol = define(symbolTable, symbol);
/*     */       }
/*     */     }
/* 228 */     return dynSymbol;
/*     */   }
/*     */   
/*     */ 
/*     */   public void init(ThreadContext executionCtx)
/*     */   {
/* 234 */     if (!isMainModule()) {
/* 235 */       int old = executionCtx.getBase();
/*     */       
/* 237 */       executionCtx.setBase(getBaseAddress());
/* 238 */       int init = this.dynamicInfo.getInit();
/* 239 */       int initarray = this.dynamicInfo.getInitArray();
/* 240 */       int initarraysz = this.dynamicInfo.getInitArraySz();
/* 241 */       if (init != 0) {
/* 242 */         executionCtx.call(init + getBaseAddress(), getName() + " init");
/*     */       }
/* 244 */       execArray(executionCtx, initarray, initarraysz, getName() + " init[]");
/* 245 */       executionCtx.setBase(old);
/*     */     }
/*     */   }
/*     */   
/*     */   private void execArray(ThreadContext executionCtx, int array$, int ibytes, Object label) {
/* 250 */     while (ibytes > 0) {
/* 251 */       executionCtx.callIndirect(array$ + getBaseAddress(), label);
/* 252 */       ibytes -= 4;
/* 253 */       array$ += 4;
/*     */     }
/*     */   }
/*     */   
/*     */   public void fini(ThreadContext ctx)
/*     */   {
/* 259 */     if (!isMainModule()) {
/* 260 */       ctx.setBase(getBaseAddress());
/*     */       
/* 262 */       int fini = this.dynamicInfo.getFini();
/* 263 */       int finiarray = this.dynamicInfo.getFiniArray();
/* 264 */       int finiarraysz = this.dynamicInfo.getFiniArraySz();
/* 265 */       if (fini != 0) {
/* 266 */         ctx.call(fini + getBaseAddress(), getName() + " fini");
/*     */       }
/* 268 */       execArray(ctx, finiarray, finiarraysz, getName() + " fini[]");
/*     */     }
/*     */   }
/*     */   
/*     */   private void apply(DynamicLinker dynamicLinker, Data memory, Relocations relocations) {
/* 273 */     if (relocations == null) return;
/* 274 */     if (relocations.isRela()) throw new UnsupportedOperationException("RELA Relocation not supported yet");
/* 275 */     int tableAddress = relocations.getAddress() + getBaseAddress();
/*     */     
/* 277 */     Relocations.Entry[] entries = relocations.getEntries();
/* 278 */     for (int i = 0; i < entries.length; i++) {
/* 279 */       int entryAddress = tableAddress + i * 8;
/* 280 */       boolean log = false;
/* 281 */       Relocations.Entry entry = entries[i];
/* 282 */       int offset = entry.getOffset();
/* 283 */       int addressToChange = getBaseAddress() + offset;
/* 284 */       Symbol symbol = entry.getSymbol();
/* 285 */       String symbolName = symbol.getName();
/*     */       
/* 287 */       int type = entry.getType();
/* 288 */       boolean searchThis = (!isMainModule()) || (!Relocations.isCopy(type));
/* 289 */       Symbols.Symbol dynSymbol = null;
/*     */       try {
/* 291 */         dynSymbol = findRelocationSymbol(symbol, searchThis);
/* 292 */         if (Relocations.isCopy(type))
/* 293 */           log = true;
/*     */         int currentValue;
/*     */         int newValue;
/*     */         int tlsOffset;
/* 297 */         int symValue; switch (type) {
/*     */         case 1: 
/* 299 */           currentValue = memory.getDoubleWord(addressToChange);
/*     */           
/* 301 */           newValue = dynSymbol.getRelocatedAddress() + currentValue;
/* 302 */           relocation("<%2d> %08X = %04X + %s ", new Object[] { Integer.valueOf(type), Integer.valueOf(addressToChange), Integer.valueOf(currentValue), dynSymbol });
/* 303 */           memory.setDoubleWord(addressToChange, newValue);
/* 304 */           break;
/*     */         case 2: 
/* 306 */           currentValue = memory.getDoubleWord(addressToChange);
/* 307 */           int symAddress = dynSymbol.getRelocatedAddress();
/* 308 */           newValue = symAddress + currentValue - addressToChange;
/*     */           
/* 310 */           relocation("<%2d> %08X = (%08X + %08X) - %08X %s ", new Object[] { Integer.valueOf(type), Integer.valueOf(addressToChange), Integer.valueOf(currentValue), Integer.valueOf(symAddress), Integer.valueOf(addressToChange), dynSymbol });
/* 311 */           memory.setDoubleWord(addressToChange, newValue);
/* 312 */           break;
/*     */         case 5: 
/* 314 */           int src = dynSymbol.getRelocatedAddress();
/* 315 */           int len = symbol.getSize();
/* 316 */           Data.Utils.memcopy(memory, src, len, addressToChange);
/* 317 */           relocation("<%2d> %08X = *(%s)...%02X ", new Object[] { Integer.valueOf(type), Integer.valueOf(addressToChange), dynSymbol, Integer.valueOf(len) });
/* 318 */           break;
/*     */         
/*     */         case 6: 
/* 321 */           newValue = dynSymbol.getRelocatedAddress();
/* 322 */           memory.setDoubleWord(addressToChange, newValue);
/* 323 */           relocation("<%2d> %08X = %s ", new Object[] { Integer.valueOf(type), Integer.valueOf(addressToChange), dynSymbol });
/*     */           
/* 325 */           break;
/*     */         case 8: 
/* 327 */           currentValue = memory.getDoubleWord(addressToChange);
/* 328 */           newValue = currentValue + getBaseAddress();
/* 329 */           memory.setDoubleWord(addressToChange, newValue);
/* 330 */           relocation("<%2d> %08X = %08X + %04X ", new Object[] { Integer.valueOf(type), Integer.valueOf(addressToChange), Integer.valueOf(getBaseAddress()), Integer.valueOf(currentValue) });
/* 331 */           break;
/*     */         
/*     */         case 14: 
/* 334 */           currentValue = memory.getDoubleWord(addressToChange);
/* 335 */           tlsOffset = dynSymbol.getModule().getTlsOffset();
/* 336 */           symValue = dynSymbol.getValue();
/* 337 */           newValue = currentValue + (symValue - tlsOffset);
/* 338 */           memory.setDoubleWord(addressToChange, newValue);
/* 339 */           relocation("<%2d > %08X = %08X + (%08X - %08X) %s ", new Object[] { Integer.valueOf(type), Integer.valueOf(addressToChange), Integer.valueOf(currentValue), Integer.valueOf(symValue), Integer.valueOf(tlsOffset), dynSymbol });
/* 340 */           break;
/*     */         case 37: 
/* 342 */           currentValue = memory.getDoubleWord(addressToChange);
/* 343 */           tlsOffset = dynSymbol.getModule().getTlsOffset();
/* 344 */           symValue = dynSymbol.getValue();
/* 345 */           newValue = currentValue + (tlsOffset - symValue);
/* 346 */           memory.setDoubleWord(addressToChange, newValue);
/* 347 */           relocation("<%2d > %08X = %08X + (%08X - %08X) %s ", new Object[] { Integer.valueOf(type), Integer.valueOf(addressToChange), Integer.valueOf(currentValue), Integer.valueOf(symValue), Integer.valueOf(tlsOffset), dynSymbol });
/* 348 */           break;
/*     */         
/*     */         case 35: 
/* 351 */           currentValue = memory.getDoubleWord(addressToChange);
/* 352 */           int tlsIndex = dynamicLinker.getTlsIndex(dynSymbol);
/* 353 */           newValue = tlsIndex;
/* 354 */           memory.setDoubleWord(addressToChange, newValue);
/* 355 */           relocation("<%2d > %08X = %08X + %08X  %s ", new Object[] { Integer.valueOf(type), Integer.valueOf(addressToChange), Integer.valueOf(currentValue), Integer.valueOf(tlsIndex), dynSymbol });
/* 356 */           break;
/*     */         
/*     */         case 36: 
/* 359 */           currentValue = memory.getDoubleWord(addressToChange);
/* 360 */           newValue = currentValue + dynSymbol.getValue();
/* 361 */           memory.setDoubleWord(addressToChange, newValue);
/* 362 */           relocation("<%2d > %08X = %08X + %04X %s ", new Object[] { Integer.valueOf(type), Integer.valueOf(addressToChange), Integer.valueOf(currentValue), Integer.valueOf(dynSymbol.getValue()), dynSymbol });
/* 363 */           break;
/*     */         
/*     */ 
/*     */         case 7: 
/* 367 */           currentValue = memory.getDoubleWord(addressToChange) + getBaseAddress();
/* 368 */           newValue = dynSymbol.getRelocatedAddress();
/* 369 */           int callAddress = currentValue - 6;
/* 370 */           if (dynSymbol.isGnuIndirectFunction())
/*     */           {
/*     */ 
/* 373 */             dynamicLinker.addGnuIndirectionCall(callAddress, dynSymbol, addressToChange);
/*     */           } else {
/* 375 */             dynamicLinker.addTrampoline(callAddress, dynSymbol);
/*     */           }
/* 377 */           memory.setDoubleWord(addressToChange, newValue);
/* 378 */           relocation("<%2d > %08X (%08X) = %s ", new Object[] { Integer.valueOf(type), Integer.valueOf(addressToChange), Integer.valueOf(currentValue), dynSymbol });
/* 379 */           break;
/*     */         case 3: case 4: case 9: case 10: case 11: case 12: case 13: case 15: case 16: case 17: case 18: case 19: case 20: case 21: 
/*     */         case 22: case 23: case 24: case 25: case 26: case 27: case 28: case 29: case 30: case 31: case 32: case 33: case 34: default: 
/* 382 */           warn("WARNING : Unsupported Relocation %14s (%2d) offset %08X", new Object[] { Relocations.toRtDescription(type), Integer.valueOf(type), Integer.valueOf(offset) });
/*     */         }
/*     */       } catch (NoSuchElementException nsee) {
/* 385 */         if (symbol.isWeak()) {
/* 386 */           log = true;
/*     */         } else {
/* 388 */           throw new com.emt.proteus.runtime32.FatalException(String.format("%10s :%s (%s)", new Object[] { getName(), symbolName, Relocations.toRtDescription(type) }));
/*     */         }
/*     */       }
/*     */       
/* 392 */       if (log) {
/* 393 */         info("%10s :%08X %s (%s) ->%s ", new Object[] { getName(), Integer.valueOf(addressToChange), symbolName, Relocations.toRtDescription(type), dynSymbol });
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   private Symbols.Symbol findRelocationSymbol(Symbol symbol, boolean searchThis)
/*     */   {
/* 400 */     String name = symbol.getName();
/*     */     
/* 402 */     Symbols.Symbol dynSymbol = null;
/* 403 */     if (searchThis) {
/* 404 */       dynSymbol = this.symbols.get(name);
/* 405 */       if (dynSymbol != null)
/* 406 */         return dynSymbol;
/* 407 */       if (symbol.isLocal()) {
/* 408 */         return this.symbols.define(this, name, null, symbol.getValue(), symbol.getSize(), symbol.getType());
/*     */       }
/*     */     }
/*     */     
/* 412 */     dynSymbol = getLinker().findSymbol(this, name, symbol.isWeak(), searchThis);
/* 413 */     return dynSymbol;
/*     */   }
/*     */   
/*     */   private static Elf open(String fileName) throws IOException
/*     */   {
/* 418 */     return open(fileName, new SeekableFile(new File(fileName)));
/*     */   }
/*     */   
/*     */   private static Elf open(URL fileName) throws IOException {
/* 422 */     return open(fileName.getPath(), new com.emt.proteus.elf.SeekableData(fileName));
/*     */   }
/*     */   
/*     */   private static Elf open(String filename, SeekableDataSource src) throws IOException {
/*     */     try {
/* 427 */       return new Elf(src);
/*     */     } catch (IllegalStateException e) {
/* 429 */       throw new IOException(filename + " : " + e.getLocalizedMessage());
/*     */     } catch (IOException e) {
/* 431 */       throw e;
/*     */     } catch (Exception e) {
/* 433 */       throw new IOException(e);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Joey\Downloads\codejar.jar!\com\emt\proteus\runtime32\rtld\ElfModule.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */