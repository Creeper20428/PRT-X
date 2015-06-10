/*    */ package com.emt.proteus.elf;
/*    */ 
/*    */ import java.io.PrintStream;
/*    */ import java.nio.ByteBuffer;
/*    */ import java.util.Arrays;
/*    */ import java.util.HashMap;
/*    */ import java.util.Iterator;
/*    */ import java.util.Map;
/*    */ 
/*    */ public class SymbolTable
/*    */ {
/*    */   public static final int SYMBOL_SIZE = 16;
/*    */   private StringSection strings;
/*    */   private Symbol[] symbols;
/*    */   private String name;
/*    */   private Map<String, Symbol> index;
/*    */   private int location;
/*    */   
/*    */   public SymbolTable(StringSection strings)
/*    */   {
/* 21 */     this.strings = strings;
/* 22 */     this.index = new HashMap();
/*    */   }
/*    */   
/*    */   private void readSymbols(Section symbolTable) {
/* 26 */     this.index.clear();
/* 27 */     SectionHeader header = symbolTable.getHeader();
/* 28 */     this.name = header.getName();
/* 29 */     this.location = header.getAddress();
/*    */     
/* 31 */     this.symbols = new Symbol[symbolTable.getData().length / 16];
/* 32 */     ByteBuffer buffer = symbolTable.asByteBuffer();
/* 33 */     for (int i = 0; i < this.symbols.length; i++) {
/* 34 */       Symbol s = new Symbol();
/* 35 */       s.read(buffer);
/* 36 */       this.symbols[i] = s;
/* 37 */       s.assignIndex(i);
/*    */     }
/*    */   }
/*    */   
/*    */   public void read(Section symbolTable, int symbolEntrySize) {
/* 42 */     if ((symbolEntrySize > 0) && (symbolEntrySize != 16))
/* 43 */       throw new UnsupportedOperationException("Untested Symbol size " + symbolEntrySize);
/* 44 */     resolveNames(symbolTable);
/*    */   }
/*    */   
/*    */   void resolveNames(Section symbolTable)
/*    */   {
/* 49 */     readSymbols(symbolTable);
/* 50 */     for (int i = 0; i < this.symbols.length; i++) {
/* 51 */       Symbol symbol = this.symbols[i];
/* 52 */       symbol.resolveName(this.strings);
/* 53 */       this.index.put(symbol.getName(), symbol);
/*    */     }
/*    */   }
/*    */   
/*    */   public Symbol get(String name) {
/* 58 */     return (Symbol)this.index.get(name);
/*    */   }
/*    */   
/*    */   public Symbol getSymbol(int symbolIndex) {
/* 62 */     return this.symbols[symbolIndex];
/*    */   }
/*    */   
/*    */   public void dump() {
/* 66 */     for (int i = 0; i < this.symbols.length; i++) {
/* 67 */       Symbol symbol = this.symbols[i];
/* 68 */       System.out.println(symbol);
/*    */     }
/*    */   }
/*    */   
/*    */   public Iterator<Symbol> iterate() {
/* 73 */     return Arrays.asList(this.symbols).iterator();
/*    */   }
/*    */   
/*    */   public int getLocation(Symbol symbol) {
/* 77 */     return this.location + symbol.getIndex() * 16;
/*    */   }
/*    */   
/*    */   public StringSection getStringTable() {
/* 81 */     return this.strings;
/*    */   }
/*    */ }


/* Location:              C:\Users\Joey\Downloads\codejar.jar!\com\emt\proteus\elf\SymbolTable.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */