/*     */ package com.emt.proteus.runtime32.rtld;
/*     */ 
/*     */ import com.emt.proteus.runtime32.Trace;
/*     */ import com.emt.proteus.runtime32.memory.MainMemory;
/*     */ import com.emt.proteus.utils.CStruct;
/*     */ import com.emt.proteus.utils.Data;
/*     */ import java.io.IOException;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Iterator;
/*     */ import java.util.NoSuchElementException;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class JavaModule
/*     */   extends Module
/*     */ {
/*  20 */   private ArrayList<Trace> callbacks = new ArrayList();
/*  21 */   private int allocated = 256;
/*     */   
/*     */   public JavaModule(String name, String filePath) {
/*  24 */     super(name, filePath);
/*     */   }
/*     */   
/*     */   protected Symbols.Symbol function(String name, Function implementation) {
/*  28 */     this.allocated = align(this.allocated, 4);
/*  29 */     Symbols.Symbol symbol = function(name, null, this.allocated, 4);
/*  30 */     this.allocated += 4;
/*  31 */     if (implementation == null) {
/*  32 */       implementation = new Function.Default(symbol);
/*     */     } else
/*  34 */       implementation.setLabel(symbol);
/*  35 */     symbol.setObject(implementation);
/*     */     
/*  37 */     this.callbacks.add(implementation);
/*  38 */     return symbol;
/*     */   }
/*     */   
/*  41 */   protected Symbols.Symbol object(String name, int size) { this.allocated = align(this.allocated, 4);
/*  42 */     Symbols.Symbol symbol = object(name, null, this.allocated, size);
/*  43 */     this.allocated += size;
/*  44 */     return symbol;
/*     */   }
/*     */   
/*  47 */   protected Symbols.Symbol object(String name, CStruct struct) { this.allocated = align(this.allocated, struct.getAlign());
/*  48 */     int size = struct.getSize();
/*  49 */     Symbols.Symbol symbol = object(name, struct, this.allocated, size);
/*  50 */     this.allocated += size;
/*  51 */     return symbol;
/*     */   }
/*     */   
/*  54 */   protected int reserve(int amount, int align) { this.allocated = align(this.allocated, align);
/*  55 */     int address = this.allocated;
/*  56 */     this.allocated += amount;
/*  57 */     return address;
/*     */   }
/*     */   
/*     */   protected Symbols.Symbol aliasObject(String name, Symbols.Symbol field, int relative, CStruct struct)
/*     */   {
/*  62 */     int size = struct.getSize();
/*  63 */     int offset = field.getValue() + relative;
/*  64 */     this.allocated = Math.max(this.allocated, size + offset);
/*  65 */     Symbols.Symbol symbol = object(name, struct, offset, size);
/*  66 */     return symbol;
/*     */   }
/*     */   
/*     */   protected void align(int alignBytes) {
/*  70 */     this.allocated = align(this.allocated, alignBytes);
/*     */   }
/*     */   
/*     */   protected int align(int addr, int align)
/*     */   {
/*  75 */     int mask = align - 1;
/*  76 */     if ((mask & addr) != 0) {
/*  77 */       addr = addr + align & (mask ^ 0xFFFFFFFF);
/*     */     }
/*  79 */     return addr;
/*     */   }
/*     */   
/*     */   protected int loadImpl(Data memory, int baseAddress, CLinkEntry map)
/*     */     throws IOException
/*     */   {
/*  85 */     int end = baseAddress + this.allocated;
/*  86 */     Iterator<Symbols.Symbol> iterator = this.symbols.iterator();
/*  87 */     for (long addr = baseAddress; addr < end; addr += 4096L) {
/*  88 */       memory.setDoubleWord((int)addr, -1);
/*     */     }
/*  90 */     while (iterator.hasNext()) {
/*  91 */       Symbols.Symbol next = (Symbols.Symbol)iterator.next();
/*  92 */       Object o = next.getObject();
/*  93 */       int address = next.getValue() + baseAddress;
/*  94 */       byte[] data = null;
/*  95 */       if ((o instanceof byte[])) {
/*  96 */         data = (byte[])o;
/*  97 */       } else if ((o instanceof String)) {
/*  98 */         data = toAscii(o.toString());
/*  99 */       } else { if (!(o instanceof CStruct)) continue;
/* 100 */         CStruct struct = (CStruct)o;
/* 101 */         struct.assign(address, memory);
/*     */       }
/*     */       
/*     */ 
/*     */ 
/* 106 */       if (data != null) memory.store(address, data, 0, data.length);
/*     */     }
/* 108 */     map.setBounds(baseAddress, end, baseAddress);
/* 109 */     map.setFilePath(getFilePath());
/* 110 */     return end;
/*     */   }
/*     */   
/*     */   protected void relocateImpl(DynamicLinker dynamicLinker, int baseAddress, MainMemory memory)
/*     */     throws IllegalStateException, NoSuchElementException
/*     */   {
/* 116 */     Iterator<Symbols.Symbol> iterator = this.symbols.iterator();
/* 117 */     while (iterator.hasNext()) {
/* 118 */       Symbols.Symbol next = (Symbols.Symbol)iterator.next();
/* 119 */       Object o = next.getObject();
/* 120 */       if ((o instanceof Trace)) {
/* 121 */         Trace t = (Trace)o;
/* 122 */         dynamicLinker.addMapping(next.getRelocatedAddress(), t);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   protected byte[] toAscii(String s)
/*     */   {
/* 130 */     byte[] b = new byte[s.length()];
/* 131 */     for (int i = 0; i < b.length; i++) {
/* 132 */       b[i] = ((byte)s.charAt(i));
/*     */     }
/* 134 */     return b;
/*     */   }
/*     */ }


/* Location:              C:\Users\Joey\Downloads\codejar.jar!\com\emt\proteus\runtime32\rtld\JavaModule.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */