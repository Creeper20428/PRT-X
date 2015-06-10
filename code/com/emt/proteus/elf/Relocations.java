/*     */ package com.emt.proteus.elf;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class Relocations
/*     */ {
/*     */   public static final int R_386_NONE = 0;
/*     */   
/*     */ 
/*     */ 
/*     */   public static final int R_386_32 = 1;
/*     */   
/*     */ 
/*     */ 
/*     */   public static final int R_386_PC32 = 2;
/*     */   
/*     */ 
/*     */ 
/*     */   public static final int R_386_GOT32 = 3;
/*     */   
/*     */ 
/*     */ 
/*     */   public static final int R_386_PLT32 = 4;
/*     */   
/*     */ 
/*     */ 
/*     */   public static final int R_386_COPY = 5;
/*     */   
/*     */ 
/*     */ 
/*     */   public static final int R_386_GLOB_DAT = 6;
/*     */   
/*     */ 
/*     */ 
/*     */   public static final int R_386_JMP_SLOT = 7;
/*     */   
/*     */ 
/*     */ 
/*     */   public static final int R_386_RELATIVE = 8;
/*     */   
/*     */ 
/*     */ 
/*     */   public static final int R_386_GOTOFF = 9;
/*     */   
/*     */ 
/*     */ 
/*     */   public static final int R_386_GOTPC = 10;
/*     */   
/*     */ 
/*     */ 
/*     */   public static final int R_386_32PLT = 11;
/*     */   
/*     */ 
/*     */ 
/*     */   public static final int R_386_TLS_TPOFF = 14;
/*     */   
/*     */ 
/*     */ 
/*     */   public static final int R_386_TLS_IE = 15;
/*     */   
/*     */ 
/*     */ 
/*     */   public static final int R_386_TLS_GOTIE = 16;
/*     */   
/*     */ 
/*     */ 
/*     */   public static final int R_386_TLS_LE = 17;
/*     */   
/*     */ 
/*     */   public static final int R_386_TLS_GD = 18;
/*     */   
/*     */ 
/*     */   public static final int R_386_TLS_LDM = 19;
/*     */   
/*     */ 
/*     */   public static final int R_386_TLS_GD_32 = 24;
/*     */   
/*     */ 
/*     */   public static final int R_386_TLS_GD_PUSH = 25;
/*     */   
/*     */ 
/*     */   public static final int R_386_TLS_GD_CALL = 26;
/*     */   
/*     */ 
/*     */   public static final int R_386_TLS_GD_POP = 27;
/*     */   
/*     */ 
/*     */   public static final int R_386_TLS_LDM_32 = 28;
/*     */   
/*     */ 
/*     */   public static final int R_386_TLS_LDM_PUSH = 29;
/*     */   
/*     */ 
/*     */   public static final int R_386_TLS_LDM_CALL = 30;
/*     */   
/*     */ 
/*     */   public static final int R_386_TLS_LDM_POP = 31;
/*     */   
/*     */ 
/*     */   public static final int R_386_TLS_LDO_32 = 32;
/*     */   
/*     */ 
/*     */   public static final int R_386_TLS_IE_32 = 33;
/*     */   
/*     */ 
/*     */   public static final int R_386_TLS_LE_32 = 34;
/*     */   
/*     */ 
/*     */   public static final int R_386_TLS_DTPMOD32 = 35;
/*     */   
/*     */ 
/*     */   public static final int R_386_TLS_DTPOFF32 = 36;
/*     */   
/*     */ 
/*     */   public static final int R_386_TLS_TPOFF32 = 37;
/*     */   
/*     */ 
/* 119 */   private static final Entry[] EMPTY = new Entry[0];
/*     */   private final String name;
/*     */   private final boolean rela;
/*     */   private final int address;
/*     */   private final Entry[] entries;
/*     */   
/*     */   public Relocations()
/*     */   {
/* 127 */     this("-", false, 0, EMPTY);
/*     */   }
/*     */   
/*     */   public Relocations(String name, boolean rela, int address, Entry[] entries) {
/* 131 */     this.name = name;
/* 132 */     this.rela = rela;
/* 133 */     this.address = address;
/* 134 */     this.entries = entries;
/*     */   }
/*     */   
/*     */   public boolean isRela() {
/* 138 */     return this.rela;
/*     */   }
/*     */   
/*     */   public String toString()
/*     */   {
/* 143 */     StringBuilder b = new StringBuilder();
/* 144 */     b.append(" Relocation Table ").append(this.name).append(this.rela ? "[RELA]" : "[REL] ").append("\n");
/* 145 */     for (int i = 0; i < this.entries.length; i++) {
/* 146 */       Entry entry = this.entries[i];
/* 147 */       b.append(String.format("%3d) %s", new Object[] { Integer.valueOf(i), entry })).append("\n");
/*     */     }
/* 149 */     return b.toString();
/*     */   }
/*     */   
/*     */   public Entry[] getEntries() {
/* 153 */     return this.entries;
/*     */   }
/*     */   
/*     */   public int getAddress() {
/* 157 */     return this.address;
/*     */   }
/*     */   
/*     */   public static boolean isCopy(int type) {
/* 161 */     switch (type)
/*     */     {
/*     */ 
/*     */ 
/*     */     case 5: 
/* 166 */       return true; }
/*     */     
/* 168 */     return false;
/*     */   }
/*     */   
/*     */ 
/*     */   public static class Entry
/*     */   {
/*     */     private boolean rela;
/*     */     private int offset;
/*     */     private int info;
/*     */     private int addend;
/*     */     private Symbol symbol;
/*     */     
/*     */     public Entry(int offset, int info, int addend, boolean rela, Symbol symbol)
/*     */     {
/* 182 */       this.offset = offset;
/* 183 */       this.info = info;
/* 184 */       this.addend = addend;
/* 185 */       this.rela = rela;
/* 186 */       this.symbol = symbol;
/*     */     }
/*     */     
/*     */     public int getOffset() {
/* 190 */       return this.offset;
/*     */     }
/*     */     
/*     */     public int getInfo() {
/* 194 */       return this.info;
/*     */     }
/*     */     
/*     */     public int getAddend() {
/* 198 */       return this.addend;
/*     */     }
/*     */     
/*     */     public int getType() {
/* 202 */       return this.info & 0xFF;
/*     */     }
/*     */     
/*     */     public int getSymbolIndex() {
/* 206 */       return toSymbolIndex(this.info);
/*     */     }
/*     */     
/*     */     public String toString() {
/* 210 */       return this.rela ? String.format("%08X %02X %08X %s ", new Object[] { Integer.valueOf(getOffset()), Integer.valueOf(getType()), Integer.valueOf(getAddend()), this.symbol.name }) : String.format("%08X %02X - %s ", new Object[] { Integer.valueOf(getOffset()), Integer.valueOf(getType()), this.symbol.name });
/*     */     }
/*     */     
/*     */ 
/*     */     public Symbol getSymbol()
/*     */     {
/* 216 */       return this.symbol;
/*     */     }
/*     */     
/*     */     public void setSymbol(Symbol symbol) {
/* 220 */       this.symbol = symbol;
/*     */     }
/*     */     
/*     */     public static int toSymbolIndex(int info) {
/* 224 */       return info >>> 8;
/*     */     }
/*     */   }
/*     */   
/*     */   public static String toRtDescription(int type)
/*     */   {
/* 230 */     switch (type) {
/*     */     case 0: 
/* 232 */       return "R_386_NONE";
/*     */     case 1: 
/* 234 */       return "R_386_32";
/*     */     case 2: 
/* 236 */       return "R_386_PC32";
/*     */     case 3: 
/* 238 */       return "R_386_GOT32";
/*     */     case 4: 
/* 240 */       return "R_386_PLT32";
/*     */     case 5: 
/* 242 */       return "R_386_COPY";
/*     */     case 6: 
/* 244 */       return "R_386_GLOB_DAT";
/*     */     case 7: 
/* 246 */       return "R_386_JMP_SLOT";
/*     */     case 8: 
/* 248 */       return "R_386_RELATIVE";
/*     */     case 9: 
/* 250 */       return "R_386_GOTOFF";
/*     */     case 10: 
/* 252 */       return "R_386_GOTPC";
/*     */     case 14: 
/* 254 */       return "R_386_TLS_TPOFF";
/*     */     case 15: 
/* 256 */       return "R_386_TLS_IE";
/*     */     case 16: 
/* 258 */       return "R_386_TLS_GOTIE";
/*     */     case 17: 
/* 260 */       return "R_386_TLS_LE";
/*     */     case 18: 
/* 262 */       return "R_386_TLS_GD";
/*     */     case 19: 
/* 264 */       return "R_386_TLS_LDM";
/*     */     case 24: 
/* 266 */       return "R_386_TLS_GD_32";
/*     */     case 25: 
/* 268 */       return "R_386_TLS_GD_PUSH";
/*     */     case 26: 
/* 270 */       return "R_386_TLS_GD_CALL";
/*     */     case 27: 
/* 272 */       return "R_386_TLS_GD_POP";
/*     */     case 28: 
/* 274 */       return "R_386_TLS_LDM_32";
/*     */     case 29: 
/* 276 */       return "R_386_TLS_LDM_PUSH";
/*     */     case 30: 
/* 278 */       return "R_386_TLS_LDM_CALL";
/*     */     case 31: 
/* 280 */       return "R_386_TLS_LDM_POP";
/*     */     case 32: 
/* 282 */       return "R_386_TLS_LDO_32";
/*     */     case 33: 
/* 284 */       return "R_386_TLS_IE_32";
/*     */     case 34: 
/* 286 */       return "R_386_TLS_LE_32";
/*     */     case 35: 
/* 288 */       return "R_386_TLS_DTPMOD32";
/*     */     case 36: 
/* 290 */       return "R_386_TLS_DTPOFF32";
/*     */     case 37: 
/* 292 */       return "R_386_TLS_TPOFF32";
/*     */     }
/* 294 */     return "R_????";
/*     */   }
/*     */ }


/* Location:              C:\Users\Joey\Downloads\codejar.jar!\com\emt\proteus\elf\Relocations.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */