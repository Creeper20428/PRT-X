/*     */ package com.emt.proteus.decoder;
/*     */ 
/*     */ public class X86Opcode
/*     */ {
/*   5 */   public static final String[] DISP_VARS = { "$d", "$dd", null, "$dddd" };
/*   6 */   public static final String[] IMM_VARS = { "$i", "$ii", null, "$iiii" };
/*     */   
/*     */   public static final int PREFIX_NONE = 0;
/*     */   
/*     */   public static final int PREFIX_LOCK_MASK = 1;
/*     */   public static final int PREFIX_REPNE_MASK = 2;
/*     */   public static final int PREFIX_REP_MASK = 4;
/*     */   public static final int PREFIX_CS_MASK = 8;
/*     */   public static final int PREFIX_SS_MASK = 16;
/*     */   public static final int PREFIX_DS_MASK = 32;
/*     */   public static final int PREFIX_ES_MASK = 64;
/*     */   public static final int PREFIX_FS_MASK = 128;
/*     */   public static final int PREFIX_GS_MASK = 256;
/*     */   public static final int PREFIX_OP_SIZE_MASK = 512;
/*     */   public static final int PREFIX_ADDR_SIZE_MASK = 1024;
/*  21 */   public static final String[] OPERANDS_8BIT = { "ah", "al", "bh", "bl", "ch", "cl", "dh", "dl" };
/*  22 */   public static final String[] OPERANDS_16BIT = { "ax", "bx", "cx", "dx", "si", "di", "sp", "bp" };
/*     */   private String opName;
/*     */   private String pattern;
/*     */   private String generalizedPattern;
/*     */   private int x86Length;
/*     */   private int dispSize;
/*     */   
/*  29 */   public X86Opcode(int prefices, String opName, String pattern, int operandCount, int x86Length, int dispSize, int displacement, int immSize, int immediate) { this.prefices = prefices;
/*  30 */     this.opName = opName;
/*  31 */     this.pattern = pattern.trim();
/*  32 */     this.operandCount = operandCount;
/*  33 */     this.x86Length = x86Length;
/*  34 */     this.dispSize = dispSize;
/*  35 */     this.displacement = displacement;
/*  36 */     this.immSize = immSize;
/*  37 */     this.immediate = immediate;
/*  38 */     this.generalizedPattern = generalizePattern(this.pattern).toUpperCase();
/*     */   }
/*     */   
/*     */   private String generalizePattern(String p)
/*     */   {
/*  43 */     String[] ops = p.split(",");
/*  44 */     for (int i = 0; i < ops.length; i++)
/*  45 */       ops[i] = generalizeOperand(ops[i]);
/*  46 */     String res = ops[0];
/*  47 */     for (int i = 1; i < ops.length; i++)
/*  48 */       res = res + "," + ops[i];
/*  49 */     return res;
/*     */   }
/*     */   
/*     */   private String generalizeOperand(String op)
/*     */   {
/*  54 */     if (op.contains("BYTE PTR"))
/*  55 */       return op.substring(0, op.indexOf("BYTE")) + "MEM";
/*  56 */     if (op.contains("DWORD PTR"))
/*  57 */       return op.substring(0, op.indexOf("DWORD")) + "MEM";
/*  58 */     if (op.contains("QWORD PTR"))
/*  59 */       return op.substring(0, op.indexOf("QWORD")) + "MEM";
/*  60 */     if (op.contains("WORD PTR"))
/*  61 */       return op.substring(0, op.indexOf("WORD")) + "MEM";
/*  62 */     return op;
/*     */   }
/*     */   
/*     */   public int getPrefices()
/*     */   {
/*  67 */     return this.prefices;
/*     */   }
/*     */   
/*     */   public String opName()
/*     */   {
/*  72 */     return this.opName;
/*     */   }
/*     */   
/*     */   public int getOperandCount()
/*     */   {
/*  77 */     return this.operandCount;
/*     */   }
/*     */   
/*     */   public String getPattern()
/*     */   {
/*  82 */     return this.pattern;
/*     */   }
/*     */   
/*     */   public int x86Length()
/*     */   {
/*  87 */     return this.x86Length;
/*     */   }
/*     */   
/*     */   public String getGeneralizedPattern()
/*     */   {
/*  92 */     return this.generalizedPattern;
/*     */   }
/*     */   
/*     */   public int getOperandSize(String op)
/*     */   {
/*  97 */     if (op.contains("BYTE PTR"))
/*  98 */       return 8;
/*  99 */     if (op.contains("QWORD PTR"))
/* 100 */       return 64;
/* 101 */     if ((op.contains("WORD PTR")) && (!op.contains("DWORD PTR"))) {
/* 102 */       return 16;
/*     */     }
/* 104 */     for (int i = 0; i < OPERANDS_8BIT.length; i++) {
/* 105 */       if (op.contains(OPERANDS_8BIT[i]))
/* 106 */         return 8;
/*     */     }
/* 108 */     for (int i = 0; i < OPERANDS_16BIT.length; i++) {
/* 109 */       if ((op.contains(OPERANDS_16BIT[i])) && (!op.contains("e" + OPERANDS_16BIT[i])))
/* 110 */         return 16;
/*     */     }
/* 112 */     return 32;
/*     */   }
/*     */   
/*     */   public int getOperand1Size()
/*     */   {
/* 117 */     String[] ops = this.pattern.split(",");
/* 118 */     return getOperandSize(ops[0]);
/*     */   }
/*     */   
/*     */   public int getOperand2Size()
/*     */   {
/* 123 */     String[] ops = this.pattern.split(",");
/* 124 */     if (ops.length > 1)
/* 125 */       return getOperandSize(ops[1]);
/* 126 */     return 0;
/*     */   }
/*     */   
/*     */   private int displacement;
/*     */   private int immSize;
/*     */   
/* 132 */   public int getOperandSize() { if (this.pattern.contains("BYTE PTR"))
/* 133 */       return 8;
/* 134 */     if (this.pattern.contains("QWORD PTR"))
/* 135 */       return 64;
/* 136 */     if ((this.pattern.contains("WORD PTR")) && (!this.pattern.contains("DWORD PTR")))
/* 137 */       return 16;
/* 138 */     String[] ops = this.pattern.split(",");
/*     */     
/* 140 */     String op = ops[0].substring(ops[0].indexOf(" ") + 1);
/* 141 */     return getOperandSize(op); }
/*     */   
/*     */   private int immediate;
/*     */   private int prefices;
/*     */   private int operandCount;
/* 146 */   public int getDispSize() { return this.dispSize; }
/*     */   
/*     */ 
/*     */   public int getDisplacement()
/*     */   {
/* 151 */     return this.displacement;
/*     */   }
/*     */   
/*     */   public int getImmSize()
/*     */   {
/* 156 */     return this.immSize;
/*     */   }
/*     */   
/*     */   public int getImmediate()
/*     */   {
/* 161 */     return this.immediate;
/*     */   }
/*     */   
/*     */   public boolean isBranch()
/*     */   {
/* 166 */     if ((this.opName.startsWith("j")) || (this.opName.startsWith("ret")) || (this.opName.startsWith("call")) || (this.opName.startsWith("loop")))
/* 167 */       return true;
/* 168 */     return false;
/*     */   }
/*     */   
/*     */   public boolean isDirect()
/*     */   {
/* 173 */     if (!isBranch())
/* 174 */       return false;
/* 175 */     if (this.opName.startsWith("ret"))
/* 176 */       return false;
/* 177 */     if (getGeneralizedPattern().equals("CALL MEM"))
/* 178 */       return false;
/* 179 */     if ((this.opName.startsWith("j")) && (!this.opName.startsWith("jmp"))) {
/* 180 */       return false;
/*     */     }
/* 182 */     return true;
/*     */   }
/*     */   
/*     */   public boolean isStatic()
/*     */   {
/* 187 */     if (!isBranch())
/* 188 */       return false;
/* 189 */     if (this.pattern.equals("call $iiii"))
/* 190 */       return true;
/* 191 */     if (this.pattern.equals("jmp $iiii"))
/* 192 */       return true;
/* 193 */     if (this.pattern.equals("jmp $i"))
/* 194 */       return true;
/* 195 */     if ((this.opName.startsWith("j")) && (!this.opName.startsWith("jmp")))
/* 196 */       return true;
/* 197 */     return false;
/*     */   }
/*     */   
/*     */   public boolean isRet()
/*     */   {
/* 202 */     return this.opName.startsWith("ret");
/*     */   }
/*     */   
/*     */   public boolean isCall()
/*     */   {
/* 207 */     return this.opName.startsWith("call");
/*     */   }
/*     */   
/*     */   public boolean isJmp()
/*     */   {
/* 212 */     return this.opName.startsWith("jmp");
/*     */   }
/*     */   
/*     */   public boolean isLock() {
/* 216 */     return (this.prefices & 0x1) != 0;
/*     */   }
/*     */   
/*     */   public boolean isRelative() {
/* 220 */     if (!isBranch())
/* 221 */       return false;
/* 222 */     if (this.pattern.equals("call $iiii"))
/* 223 */       return true;
/* 224 */     if (this.pattern.equals("jmp $iiii"))
/* 225 */       return true;
/* 226 */     if (this.pattern.equals("jmp $i"))
/* 227 */       return true;
/* 228 */     if (this.opName.startsWith("jmp"))
/* 229 */       return false;
/* 230 */     if (this.opName.startsWith("j")) {
/* 231 */       return true;
/*     */     }
/* 233 */     return false;
/*     */   }
/*     */   
/*     */   public String toString()
/*     */   {
/* 238 */     String result = this.pattern;
/*     */     
/* 240 */     if (this.dispSize > 0)
/*     */     {
/* 242 */       boolean useColon = this.pattern.indexOf("[") < 0;
/*     */       
/* 244 */       if (useColon) {
/* 245 */         result = result.replace(DISP_VARS[(this.dispSize - 1)], ":0x" + Integer.toHexString(this.displacement));
/*     */ 
/*     */       }
/* 248 */       else if (this.displacement < 0) {
/* 249 */         result = result.replace(DISP_VARS[(this.dispSize - 1)], "-0x" + Integer.toHexString(-this.displacement));
/*     */       } else {
/* 251 */         result = result.replace(DISP_VARS[(this.dispSize - 1)], "+0x" + Integer.toHexString(this.displacement));
/*     */       }
/*     */     }
/*     */     
/* 255 */     if (this.immSize > 0) {
/* 256 */       result = result.replace(IMM_VARS[(this.immSize - 1)], "0x" + Integer.toHexString(this.immediate));
/*     */     }
/* 258 */     return result;
/*     */   }
/*     */   
/*     */   public int[] getUCodes()
/*     */   {
/* 263 */     return UCodes.decode(this);
/*     */   }
/*     */ }


/* Location:              C:\Users\Joey\Downloads\codejar.jar!\com\emt\proteus\decoder\X86Opcode.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */