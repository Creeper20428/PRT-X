/*     */ package com.emt.proteus.utils;
/*     */ 
/*     */ import java.util.Arrays;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class AddressMap<T>
/*     */ {
/*     */   public static final int CAPACITY = 128;
/*     */   public static final long INT_ADDR_MASK = 4294967295L;
/*     */   private Entry[] entries;
/*     */   private int size;
/*     */   private T defaultEntry;
/*     */   
/*     */   public AddressMap(T defaultEntry)
/*     */   {
/*  19 */     this.defaultEntry = defaultEntry;
/*  20 */     this.entries = new Entry[''];
/*  21 */     this.size = 0;
/*     */   }
/*     */   
/*     */   public void put(long start, long end, T value) {
/*  25 */     start &= 0xFFFFFFFF;
/*  26 */     end &= 0xFFFFFFFF;
/*  27 */     Entry newEntry = new Entry(start, end, value);
/*     */     
/*  29 */     int index = Arrays.binarySearch(this.entries, 0, this.size, newEntry);
/*  30 */     if (index < 0) {
/*  31 */       index = -index - 1;
/*  32 */       Entry[] src = this.entries;
/*  33 */       Entry[] dest = this.entries;
/*  34 */       if (this.size >= this.entries.length) {
/*  35 */         dest = new Entry[this.entries.length << 1];
/*  36 */         this.entries = dest;
/*  37 */         if ((index > 0) && (src != dest)) {
/*  38 */           System.arraycopy(src, 0, dest, 0, index);
/*     */         }
/*     */       }
/*  41 */       if (index < this.size) {
/*  42 */         System.arraycopy(src, index, dest, index + 1, this.size - index);
/*     */       }
/*  44 */       this.size += 1;
/*  45 */       this.entries[index] = newEntry;
/*     */     }
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
/*     */   public T get(long address)
/*     */   {
/*     */     try
/*     */     {
/*  63 */       return (T)getEntry(address).value;
/*     */     } catch (NullPointerException npe) {}
/*  65 */     return (T)this.defaultEntry;
/*     */   }
/*     */   
/*     */   public Entry<T> getEntry(long address) {
/*  69 */     long key = address & 0xFFFFFFFF;
/*  70 */     int lo = 0;
/*  71 */     int hi = this.size - 1;
/*  72 */     Entry[] entries = this.entries;
/*  73 */     while (lo < hi) {
/*  74 */       int mididx = lo + hi >>> 1;
/*  75 */       long mid = entries[mididx].start;
/*  76 */       if (mid > key) {
/*  77 */         hi = mididx - 1;
/*  78 */       } else if ((mid < key) && (entries[mididx].end < address)) {
/*  79 */         lo = mididx + 1;
/*     */       } else {
/*  81 */         hi = mididx;
/*     */       }
/*     */     }
/*  84 */     Entry entry = entries[lo];
/*  85 */     if (entry.contains(address)) {
/*  86 */       return entry;
/*     */     }
/*  88 */     return null;
/*     */   }
/*     */   
/*     */ 
/*     */   public static class Entry<T>
/*     */     implements Comparable<Entry>
/*     */   {
/*     */     private final long start;
/*     */     
/*     */     private final long end;
/*     */     private T value;
/*     */     
/*     */     public Entry(long start, long end, T value)
/*     */     {
/* 102 */       this.start = start;
/* 103 */       this.end = end;
/* 104 */       this.value = value;
/*     */     }
/*     */     
/*     */     public long getStart() {
/* 108 */       return this.start;
/*     */     }
/*     */     
/*     */     public long getEnd() {
/* 112 */       return this.end;
/*     */     }
/*     */     
/*     */     public T getValue() {
/* 116 */       return (T)this.value;
/*     */     }
/*     */     
/*     */     public int compareTo(Entry o)
/*     */     {
/* 121 */       int diff = (int)(this.start - o.start);
/* 122 */       if (diff != 0) { return diff;
/*     */       }
/* 124 */       diff = (int)(this.end - o.end);
/* 125 */       return diff;
/*     */     }
/*     */     
/* 128 */     public int hashCode() { return (int)this.start; }
/*     */     
/*     */ 
/*     */     public boolean equals(Object obj)
/*     */     {
/* 133 */       if (obj == this) return true;
/* 134 */       if (!(obj instanceof Entry)) return false;
/* 135 */       Entry other = (Entry)obj;
/* 136 */       return (other.start == this.start) && (other.end == this.end);
/*     */     }
/*     */     
/*     */     public boolean contains(long address) {
/* 140 */       return (address >= this.start) && (address < this.end);
/*     */     }
/*     */     
/* 143 */     public String toString() { return String.format("%08X-%08X %s", new Object[] { Long.valueOf(this.start), Long.valueOf(this.end), this.value }); }
/*     */   }
/*     */ }


/* Location:              C:\Users\Joey\Downloads\codejar.jar!\com\emt\proteus\utils\AddressMap.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */