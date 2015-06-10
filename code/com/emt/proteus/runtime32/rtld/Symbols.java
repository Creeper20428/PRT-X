/*     */ package com.emt.proteus.runtime32.rtld;
/*     */ 
/*     */ import com.emt.proteus.utils.CStruct;
/*     */ import java.util.Collection;
/*     */ import java.util.Iterator;
/*     */ import java.util.LinkedHashMap;
/*     */ import java.util.Map;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class Symbols
/*     */ {
/*     */   public static final int STT_NOTYPE = 0;
/*     */   public static final int STT_OBJECT = 1;
/*     */   public static final int STT_FUNC = 2;
/*     */   public static final int STT_SECTION = 3;
/*     */   public static final int STT_FILE = 4;
/*     */   public static final int STT_COMMON = 5;
/*     */   public static final int STT_TLS = 6;
/*     */   public static final int STT_GNU_IFUNC = 10;
/*     */   public static final int STT_LO_PROC = 13;
/*     */   public static final int STT_HI_PROC = 15;
/*  25 */   public static final Symbols GLOBALS = new Symbols();
/*     */   private final Map<String, Symbol> symbolMap;
/*     */   
/*     */   public Symbols()
/*     */   {
/*  30 */     this.symbolMap = new LinkedHashMap();
/*     */   }
/*     */   
/*     */   public Symbol get(String name) {
/*  34 */     return (Symbol)this.symbolMap.get(name);
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
/*     */   public Symbol define(Module elfModule, String name, Object rtValue, int symValue, int size, int type)
/*     */   {
/*  48 */     return bind(new Symbol(elfModule, name, rtValue, symValue, size, type, null));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public Symbol bind(Symbol symbol)
/*     */   {
/*  56 */     this.symbolMap.put(symbol.getName(), symbol);
/*  57 */     return symbol;
/*     */   }
/*     */   
/*     */   public Iterator<Symbol> iterator() {
/*  61 */     return this.symbolMap.values().iterator();
/*     */   }
/*     */   
/*     */   public static class Symbol
/*     */   {
/*     */     private Module module;
/*     */     private final String name;
/*     */     private Object object;
/*     */     private int value;
/*     */     private int size;
/*     */     private final int type;
/*     */     private int location;
/*     */     
/*     */     private Symbol(Module module, String name, Object object, int value, int size, int type)
/*     */     {
/*  76 */       this.module = module;
/*  77 */       this.name = name;
/*  78 */       this.object = object;
/*  79 */       this.value = value;
/*  80 */       this.size = size;
/*  81 */       this.type = type;
/*     */     }
/*     */     
/*     */     public int getType() {
/*  85 */       return this.type;
/*     */     }
/*     */     
/*     */     public Module getModule() {
/*  89 */       return this.module;
/*     */     }
/*     */     
/*     */     public String getName() {
/*  93 */       return this.name;
/*     */     }
/*     */     
/*     */     public Object getObject() {
/*  97 */       return this.object;
/*     */     }
/*     */     
/*     */     public int getValue() {
/* 101 */       return this.value;
/*     */     }
/*     */     
/*     */     public int getSize() {
/* 105 */       return this.size;
/*     */     }
/*     */     
/*     */     public int getRelocatedAddress() {
/* 109 */       return this.value + this.module.getBaseAddress();
/*     */     }
/*     */     
/*     */     public String toString() {
/* 113 */       return String.format("[%08X:%08X +%X] %s:%s", new Object[] { Integer.valueOf(getRelocatedAddress()), Integer.valueOf(getValue()), Integer.valueOf(getSize()), this.module.getName(), this.name });
/*     */     }
/*     */     
/*     */     public String getFormatString() {
/* 117 */       return "[%08X:%6X] " + this.module.getName() + ":" + this.name;
/*     */     }
/*     */     
/* 120 */     public void setObject(Object object) { this.object = object; }
/*     */     
/*     */     public CStruct struct()
/*     */     {
/* 124 */       return (CStruct)this.object;
/*     */     }
/*     */     
/* 127 */     public Function function() { return (Function)this.object; }
/*     */     
/*     */     public int getLocation()
/*     */     {
/* 131 */       return this.module.getBaseAddress() + this.location;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     public void assignLocation(int location)
/*     */     {
/* 140 */       this.location = location;
/*     */     }
/*     */     
/*     */     public boolean isGnuIndirectFunction() {
/* 144 */       return this.type == 10;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Joey\Downloads\codejar.jar!\com\emt\proteus\runtime32\rtld\Symbols.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */