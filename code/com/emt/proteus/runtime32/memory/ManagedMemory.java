/*     */ package com.emt.proteus.runtime32.memory;
/*     */ 
/*     */ import com.emt.proteus.runtime32.FatalException;
/*     */ import com.emt.proteus.utils.CStruct;
/*     */ import com.emt.proteus.utils.Data;
/*     */ import com.emt.proteus.utils.Data.Utils;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class ManagedMemory
/*     */ {
/*     */   public static final int _16K = 14;
/*     */   private final Data memory;
/*     */   private final int capacity;
/*     */   private final int align;
/*     */   private final int mask;
/*     */   private final int alignMask;
/*     */   private final int address;
/*     */   private final Block[] entries;
/*     */   private int used;
/*     */   private Block freed;
/*     */   
/*  32 */   public ManagedMemory(MainMemory memory) { this(memory, 0, 14, 6); }
/*     */   
/*     */   public ManagedMemory(MainMemory memory, int address, int scale, int log2Align) {
/*  35 */     this.freed = null;
/*  36 */     this.memory = memory;
/*  37 */     this.capacity = (1 << scale);
/*  38 */     this.align = log2Align;
/*  39 */     this.alignMask = ((1 << this.align) - 1);
/*  40 */     int maxBlocks = Math.min(this.capacity >>> this.align, 256);
/*  41 */     this.entries = new Block[maxBlocks];
/*  42 */     this.mask = (this.entries.length - 1);
/*  43 */     if (address == 0) {
/*  44 */       this.address = memory.allocate(this.capacity);
/*     */     } else {
/*  46 */       this.address = address;
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*  52 */   public int malloc(int amount) { return alloc(amount).address; }
/*     */   
/*     */   public int calloc(int amount) {
/*  55 */     Block alloc = alloc(amount);
/*  56 */     Data.Utils.fill(this.memory, alloc.address, alloc.size, (byte)0);
/*  57 */     return alloc.address;
/*     */   }
/*     */   
/*     */   public CStruct malloc(CStruct struct) {
/*  61 */     int addr = malloc(struct.getSize());
/*  62 */     struct.assign(addr, this.memory);
/*  63 */     return struct;
/*     */   }
/*     */   
/*     */   public CStruct calloc(CStruct struct) {
/*  67 */     int addr = calloc(struct.getSize());
/*  68 */     struct.assign(addr, this.memory);
/*  69 */     return struct;
/*     */   }
/*     */   
/*     */   public Data getMemory() {
/*  73 */     return this.memory;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private synchronized Block alloc(int amount)
/*     */   {
/*  83 */     amount = round(amount);
/*  84 */     Block entry = this.freed;
/*  85 */     while ((entry != null) && (entry.size < amount)) {
/*  86 */       entry = entry.next;
/*     */     }
/*  88 */     if (entry != null) {
/*  89 */       entry.remove();
/*  90 */     } else if (this.used + amount <= this.capacity)
/*     */     {
/*  92 */       entry = new Block(this.used + this.address, amount);
/*  93 */       this.used += amount;
/*     */     }
/*     */     else {
/*  96 */       throw new FatalException("Out Of Memory");
/*     */     }
/*     */     
/*  99 */     int hash = index(entry.address);
/* 100 */     this.entries[hash] = entry.head(this.entries[hash]);
/* 101 */     return entry;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private int round(int amount)
/*     */   {
/* 110 */     return amount + this.alignMask & (this.alignMask ^ 0xFFFFFFFF);
/*     */   }
/*     */   
/*     */   public synchronized Block getBlock(int address)
/*     */   {
/* 115 */     int hash = index(address);
/* 116 */     Block block = this.entries[hash];
/*     */     try {
/*     */       for (;;) {
/* 119 */         if (block.address == address) return block;
/* 120 */         block = block.next;
/*     */       }
/*     */       
/* 123 */       return null;
/*     */     } catch (NullPointerException npe) {}
/*     */   }
/*     */   
/*     */   private int index(int address) {
/* 128 */     return address - this.address >>> this.align & this.mask;
/*     */   }
/*     */   
/*     */   public synchronized Block getContainer(int address) {
/* 132 */     int hash = index(address);
/* 133 */     Block block = this.entries[hash];
/* 134 */     while ((block != null) && (!block.contains(address))) {
/* 135 */       block = block.next;
/*     */     }
/* 137 */     return block;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public synchronized void free(int address)
/*     */   {
/* 148 */     int hash = index(address);
/* 149 */     Block head = this.entries[hash];
/* 150 */     Block block = head;
/*     */     try {
/* 152 */       while (block.address != address) {
/* 153 */         block = block.next;
/*     */       }
/*     */       
/* 156 */       if (block == head) {
/* 157 */         this.entries[hash] = block.next;
/*     */       }
/* 159 */       block.remove();
/*     */       
/* 161 */       this.freed = block.sizeInsert(this.freed);
/*     */     } catch (NullPointerException npe) {
/* 163 */       throw new IllegalArgumentException("Address not allocated " + Integer.toHexString(address));
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   public final class Block
/*     */   {
/*     */     private Block previous;
/*     */     
/*     */     private Block next;
/*     */     private final int address;
/*     */     private final int size;
/*     */     
/*     */     public Block(int address, int size)
/*     */     {
/* 178 */       this.address = address;
/* 179 */       this.size = size;
/*     */     }
/*     */     
/* 182 */     public boolean contains(int address) { int index = address - this.address;
/* 183 */       return (index >= 0) && (index < this.size);
/*     */     }
/*     */     
/*     */     private void remove() {
/* 187 */       if (this.next != null) this.next.previous = this.previous;
/* 188 */       if (this.previous != null) this.previous.next = this.previous;
/* 189 */       this.previous = null;
/* 190 */       this.next = null;
/*     */     }
/*     */     
/*     */     private Block head(Block headList) {
/* 194 */       if (headList != null) {
/* 195 */         headList.previous = this;
/* 196 */         this.next = headList;
/*     */       }
/* 198 */       return this;
/*     */     }
/*     */     
/* 201 */     private Block sizeInsert(Block headList) { remove();
/* 202 */       if (headList == null) {
/* 203 */         this.previous = null;
/* 204 */         this.next = null;
/* 205 */         return this;
/*     */       }
/* 207 */       Block e = headList;
/* 208 */       while ((e.next != null) && (e.size < this.size)) { e = e.next;
/*     */       }
/* 210 */       this.next = e.next;
/* 211 */       if (this.next != null) {
/* 212 */         this.next.previous = this;
/*     */       }
/* 214 */       e.next = this;
/* 215 */       this.previous = e;
/* 216 */       return headList;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Joey\Downloads\codejar.jar!\com\emt\proteus\runtime32\memory\ManagedMemory.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */