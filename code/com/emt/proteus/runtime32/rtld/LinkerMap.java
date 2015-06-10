/*     */ package com.emt.proteus.runtime32.rtld;
/*     */ 
/*     */ import com.emt.proteus.utils.CStruct;
/*     */ import com.emt.proteus.utils.Data;
/*     */ import java.io.PrintStream;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class LinkerMap
/*     */   extends CStruct
/*     */ {
/*     */   private int maxEntries;
/*     */   private CLinkEntry root;
/*     */   private CLinkEntry linker;
/*     */   private int arrayStart;
/*     */   private int count;
/*     */   private CLinkEntry[] instances;
/*     */   
/*  22 */   public LinkerMap() { this(256); }
/*     */   
/*     */   public LinkerMap(int entries) {
/*  25 */     super(sizeof(entries));
/*  26 */     setAlign(CLinkEntry.SIZEOF);
/*  27 */     this.maxEntries = entries;
/*  28 */     this.instances = new CLinkEntry[this.maxEntries];
/*  29 */     this.linker = create();
/*  30 */     this.root = create();
/*     */   }
/*     */   
/*     */   public static int sizeof(int nEntries) {
/*  34 */     return (nEntries + 1) * CLinkEntry.SIZEOF;
/*     */   }
/*     */   
/*     */   public void assign(int address, Data data)
/*     */   {
/*  39 */     super.assign(address, data);
/*  40 */     this.arrayStart = (address + CLinkEntry.SIZEOF);
/*  41 */     this.root.assign(toAddress(0), data);
/*  42 */     this.linker.assign(toAddress(1), data);
/*  43 */     this.count = 2;
/*  44 */     this.root.setNext(this.linker);
/*  45 */     this.root.setPrevious(0);
/*  46 */     this.linker.setNext(0);
/*  47 */     this.linker.setPrevious(this.root);
/*     */   }
/*     */   
/*     */   private int toAddress(int entry)
/*     */   {
/*  52 */     return this.arrayStart + entry * CLinkEntry.SIZEOF;
/*     */   }
/*     */   
/*     */   public CLinkEntry getRoot() {
/*  56 */     return this.root;
/*     */   }
/*     */   
/*  59 */   public CLinkEntry getLinker() { return this.linker; }
/*     */   
/*     */   public CLinkEntry create()
/*     */   {
/*  63 */     int address = toAddress(this.count);
/*  64 */     CLinkEntry e = new CLinkEntry(address, getData());
/*  65 */     this.instances[(this.count++)] = e;
/*  66 */     return e;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public synchronized Entry entry(Entry previous, Module mod)
/*     */   {
/*  73 */     Entry entry = new Entry(mod, create());
/*  74 */     String before = toMapString();
/*  75 */     entry.insert(previous, previous.next);
/*  76 */     assert (validateList(before));
/*  77 */     return entry;
/*     */   }
/*     */   
/*  80 */   public Entry entry(Module mod) { return new Entry(mod, create()); }
/*     */   
/*     */   public synchronized void insertBefore(Entry toInsert, Entry beforeElement)
/*     */   {
/*  84 */     String before = toMapString();
/*  85 */     toInsert.insert(beforeElement.previous, beforeElement);
/*  86 */     assert (validateList(before));
/*     */   }
/*     */   
/*     */   private boolean validateList(String before)
/*     */   {
/*  91 */     CLinkEntry tmp = new CLinkEntry();
/*  92 */     tmp.assign(this.root);
/*  93 */     int limit = validateLimit();
/*  94 */     while ((tmp.addressOf() != 0) && (limit-- > 0)) {
/*  95 */       tmp.assign(tmp.getNext());
/*     */     }
/*  97 */     boolean isNull = tmp.addressOf() == 0;
/*  98 */     if (!isNull) {
/*  99 */       System.err.println("BEFORE");
/* 100 */       System.err.println(before);
/* 101 */       System.err.println("AFTER");
/* 102 */       System.err.println(toMapString());
/*     */     }
/* 104 */     return isNull;
/*     */   }
/*     */   
/*     */   private int validateLimit() {
/* 108 */     return this.count + 2;
/*     */   }
/*     */   
/*     */   public void printMap() {
/* 112 */     String out = toMapString();
/* 113 */     System.out.println(out);
/*     */   }
/*     */   
/*     */   private String toMapString() {
/* 117 */     StringBuilder b = new StringBuilder();
/* 118 */     CLinkEntry tmp = new CLinkEntry();
/* 119 */     tmp.assign(this.root);
/* 120 */     int limit = validateLimit();
/* 121 */     while ((tmp.addressOf() != 0) && (limit-- > 0)) {
/* 122 */       b.append(tmp).append("\n");
/* 123 */       tmp.assign(tmp.getNext());
/*     */     }
/* 125 */     return b.toString();
/*     */   }
/*     */   
/*     */ 
/*     */   public static class Entry
/*     */   {
/*     */     private final Module module;
/*     */     
/*     */     private final CLinkEntry map;
/*     */     private Entry previous;
/*     */     private Entry next;
/*     */     private boolean inMemory;
/*     */     private boolean relocated;
/*     */     private boolean initCalled;
/*     */     private boolean finiCalled;
/*     */     private int opened;
/*     */     
/*     */     public Entry(Module mod, CLinkEntry map)
/*     */     {
/* 144 */       this.map = map;
/* 145 */       this.module = mod;
/* 146 */       this.module.setMap(this.map);
/* 147 */       this.map.setModule(this.module);
/* 148 */       this.map.setFilePath(this.module.getFilePath());
/*     */     }
/*     */     
/*     */     public Entry(Entry after, Module module, CLinkEntry map) {
/* 152 */       this(module, map);
/* 153 */       insert(after, after.next);
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     public Entry append(Entry entry)
/*     */     {
/* 166 */       Entry e = entry;
/* 167 */       while ((e != null) && (e != this)) e = e.next;
/* 168 */       if (e != null) {
/* 169 */         throw new IllegalArgumentException("This will create a circular reference");
/*     */       }
/* 171 */       if (entry.previous != null) entry.previous.next = null;
/* 172 */       entry.previous = this;
/* 173 */       this.next = entry;
/*     */       
/* 175 */       return entry;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     private void insert(Entry previous, Entry next)
/*     */     {
/* 185 */       if ((previous == this) || (next == this))
/* 186 */         throw new IllegalArgumentException("This will create a circular reference");
/* 187 */       remove();
/* 188 */       this.previous = previous;
/* 189 */       this.next = next;
/* 190 */       this.map.setPrevious(intValue(previous));
/* 191 */       this.map.setNext(intValue(next));
/* 192 */       if (previous != null) {
/* 193 */         previous.next = this;
/* 194 */         previous.map.setNext(this.map);
/*     */       }
/* 196 */       if (next != null) {
/* 197 */         next.previous = this;
/* 198 */         next.map.setPrevious(this.map);
/*     */       }
/*     */     }
/*     */     
/*     */ 
/*     */     private void remove()
/*     */     {
/* 205 */       if (this.next != null) {
/* 206 */         this.next.previous = this.previous;
/* 207 */         this.next.map.setPrevious(this.previous.map);
/*     */       }
/* 209 */       if (this.previous != null) {
/* 210 */         this.previous.next = this.next;
/* 211 */         this.previous.map.setNext(this.next.map);
/*     */       }
/* 213 */       this.next = null;
/* 214 */       this.previous = null;
/* 215 */       this.map.setPrevious(0);
/* 216 */       this.map.setNext(0);
/*     */     }
/*     */     
/*     */     public int getBaseAddress() {
/* 220 */       return this.map.getBaseAddress();
/*     */     }
/*     */     
/*     */     public String getName() {
/* 224 */       return this.module.getName();
/*     */     }
/*     */     
/*     */     public String getFilePath() {
/* 228 */       return this.module.getFilePath();
/*     */     }
/*     */     
/*     */     public Entry next() {
/* 232 */       return this.next;
/*     */     }
/*     */     
/*     */     public Module getModule() {
/* 236 */       return this.module;
/*     */     }
/*     */     
/*     */     public boolean hasNext() {
/* 240 */       return this.next != null;
/*     */     }
/*     */     
/*     */     public Entry previous() {
/* 244 */       return this.previous;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     public boolean isInMemory()
/*     */     {
/* 253 */       return this.map.getMapStart() != this.map.getMapEnd();
/*     */     }
/*     */     
/*     */     public boolean isBefore(Entry current)
/*     */     {
/* 258 */       Entry e = current;
/* 259 */       while ((e != null) && (e != this)) {
/* 260 */         e = e.previous;
/*     */       }
/* 262 */       return e != null;
/*     */     }
/*     */     
/*     */     public Entry relocated() {
/* 266 */       this.relocated = true;
/* 267 */       return this;
/*     */     }
/*     */     
/*     */     public boolean isRelocated() {
/* 271 */       return this.relocated;
/*     */     }
/*     */     
/*     */     public int open() {
/* 275 */       return this.opened++;
/*     */     }
/*     */     
/*     */ 
/* 279 */     public int close() { return --this.opened; }
/*     */     
/*     */     public Entry initCalled() {
/* 282 */       this.initCalled = true;
/* 283 */       return this;
/*     */     }
/*     */     
/*     */     public Entry finiCalled() {
/* 287 */       this.finiCalled = true;
/* 288 */       return this;
/*     */     }
/*     */     
/*     */     public boolean isInUse() {
/* 292 */       return this.opened != 0;
/*     */     }
/*     */     
/*     */     public boolean isInitCalled() {
/* 296 */       return this.initCalled;
/*     */     }
/*     */     
/*     */     public boolean isFiniCalled()
/*     */     {
/* 301 */       return this.finiCalled;
/*     */     }
/*     */     
/*     */     public String toString() {
/* 305 */       Entry e = this;
/* 306 */       StringBuilder b = new StringBuilder();
/* 307 */       while (e != null) {
/* 308 */         b.append(" >> ");
/* 309 */         b.append(e.getName());
/* 310 */         e = e.next;
/*     */       }
/* 312 */       return b.toString();
/*     */     }
/*     */     
/*     */     public Entry tail() {
/* 316 */       Entry e = this;
/* 317 */       while (e.next != null) e = e.next;
/* 318 */       return e;
/*     */     }
/*     */     
/*     */     private static int intValue(Entry e) {
/* 322 */       return e == null ? 0 : e.map.addressOf();
/*     */     }
/*     */     
/*     */     public CLinkEntry getMap() {
/* 326 */       return this.map;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Joey\Downloads\codejar.jar!\com\emt\proteus\runtime32\rtld\LinkerMap.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */