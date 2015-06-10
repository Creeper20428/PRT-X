/*     */ package com.emt.proteus.decoder;
/*     */ 
/*     */ import java.io.PrintStream;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import support.BasicDisassembler.X86Opcode;
/*     */ 
/*     */ public class Decoder
/*     */ {
/*     */   public static final int LOAD = -1;
/*     */   public static final int STORE = -2;
/*     */   public static final int LOADL = -3;
/*     */   public static final int STOREL = -4;
/*     */   public static final int LOADF = -5;
/*     */   public static final int STOREF = -6;
/*     */   public static final int OPERAND_SIZE = -7;
/*     */   public static final int IMMEDIATE = -8;
/*  19 */   private static final Map<String, int[]> overrides = new java.util.HashMap();
/*     */   private static final Map<String, String> flagTypes;
/*     */   
/*     */   static {
/*  23 */     overrides.put("nop", new int[0]);
/*  24 */     overrides.put("push", new int[] { -1, 0, 0, 229 });
/*  25 */     overrides.put("inc", new int[] { -1, 0, 0, 310, 1, -7, 188, -7 });
/*  26 */     overrides.put("dec", new int[] { -1, 0, 0, 310, 1, -7, 188, -7 });
/*  27 */     overrides.put("mov", new int[] { -1, 2, 1, -2, 2, 0 });
/*  28 */     overrides.put("movsx", new int[] { -1, 2, 1, -7, -2, 2, 0 });
/*  29 */     overrides.put("movzx", new int[] { -1, 2, 1, -7, -2, 2, 0 });
/*  30 */     overrides.put("lea", new int[] { 456, -2, 2, 0, 279 });
/*  31 */     overrides.put("sahf", new int[] { 183 });
/*  32 */     overrides.put("rdtsc", new int[] { 47, 398, 365 });
/*  33 */     overrides.put("stmxcsr", new int[] { 111 });
/*  34 */     overrides.put("ldmxcsr", new int[] { 112 });
/*  35 */     overrides.put("popf", new int[] { 48 });
/*  36 */     overrides.put("pushf", new int[] { 49 });
/*  37 */     overrides.put("xchg", new int[] { -1, 0, 1, -1, 1, 1, -2, 0, 1, -2, 1, 0 });
/*  38 */     overrides.put("cmpxchg", new int[] { 281, -1, 1, 0, 136, 184, 136, -1, 2, 0, 10, -2, 0, 1, -2, 2, 0 });
/*  39 */     overrides.put("shrd", new int[] { -1, 0, 0, -1, 1, 1, -1, 3, 2, -7, -2, 2, 0, 187, -7 });
/*  40 */     overrides.put("shld", new int[] { -1, 0, 0, -1, 1, 1, -1, 3, 2, -7, -2, 2, 0, 187, -7 });
/*  41 */     overrides.put("not", new int[] { -1, 0, 0, 4, -2, 2, 0 });
/*     */     
/*  43 */     overrides.put("mul", new int[] { -1, 0, 0, -7, 189 });
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  52 */     overrides.put("fldenv", new int[] { -1, 0, 0, 102 });
/*  53 */     overrides.put("fldenvw", new int[] { -1, 0, 0, 102 });
/*  54 */     overrides.put("fstenv", new int[] { -1, 0, 0, 101 });
/*  55 */     overrides.put("fnstenv", new int[] { -1, 0, 0, 101 });
/*  56 */     overrides.put("fnstenvw", new int[] { -1, 0, 0, 101 });
/*  57 */     overrides.put("fnsavew", new int[] { -1, 0, 0, 103 });
/*  58 */     overrides.put("fnsave", new int[] { -1, 0, 0, 103 });
/*  59 */     overrides.put("frstorw", new int[] { -1, 0, 0, 104 });
/*  60 */     overrides.put("frstor", new int[] { -1, 0, 0, 104 });
/*  61 */     overrides.put("ffree", new int[0]);
/*  62 */     overrides.put("finit", new int[] { 110 });
/*  63 */     overrides.put("fstcw", new int[] { 52, -2, 2, 0 });
/*  64 */     overrides.put("fstsw", new int[] { 51, -2, 2, 0 });
/*  65 */     overrides.put("fldcw", new int[] { -1, 0, 0, 53 });
/*  66 */     overrides.put("fxam", new int[] { 55 });
/*  67 */     overrides.put("fabs", new int[] { 77 });
/*  68 */     overrides.put("fchs", new int[] { 78 });
/*  69 */     overrides.put("fsqrt", new int[] { 79 });
/*  70 */     overrides.put("fscale", new int[] { 80 });
/*  71 */     overrides.put("f2xm1", new int[] { 81 });
/*  72 */     overrides.put("frndint", new int[] { 82 });
/*  73 */     overrides.put("fsincos", new int[] { 83 });
/*  74 */     overrides.put("fcos", new int[] { 85 });
/*  75 */     overrides.put("fptan", new int[] { 86 });
/*  76 */     overrides.put("fpatan", new int[] { 87 });
/*  77 */     overrides.put("fprem", new int[] { 88 });
/*  78 */     overrides.put("fprem1", new int[] { 88 });
/*  79 */     overrides.put("fwait", new int[0]);
/*  80 */     overrides.put("fld1", new int[] { 60 });
/*  81 */     overrides.put("fdlpi", new int[] { 61 });
/*  82 */     overrides.put("fldln2", new int[] { 62 });
/*  83 */     overrides.put("fldl2e", new int[] { 63 });
/*  84 */     overrides.put("fldl2t", new int[] { 64 });
/*  85 */     overrides.put("fldlg2", new int[] { 65 });
/*  86 */     overrides.put("fcom", new int[] { 439, -5, 1, 0, 54 });
/*  87 */     overrides.put("fcomi", new int[] { 475, -5, 1, 1, 54, 72 });
/*  88 */     overrides.put("fcomp", new int[] { 439, -5, 1, 0, 54, 50 });
/*  89 */     overrides.put("fucom", new int[] { 439, -5, 1, 0, 54 });
/*  90 */     overrides.put("fucomp", new int[] { 439, 449, 54, 50 });
/*  91 */     overrides.put("fucompp", new int[] { 439, 449, 54, 50, 50 });
/*  92 */     overrides.put("fucomi", new int[] { -5, 0, 0, -5, 1, 1, 72 });
/*  93 */     overrides.put("fucomip", new int[] { -5, 0, 0, -5, 1, 1, 72, 50 });
/*  94 */     overrides.put("fyl2xp1", new int[] { 91, 417, 50 });
/*  95 */     overrides.put("fy2lx", new int[] { 92, 417, 50 });
/*  96 */     overrides.put("fst", new int[] { 57, -6, 0, 0 });
/*  97 */     overrides.put("fstp", new int[] { 438, -6, 0, 0, 58 });
/*  98 */     overrides.put("fbstp", new int[] { 439, 469, 108, 58, 279 });
/*  99 */     overrides.put("fbld", new int[] { 469, 109, 279 });
/* 100 */     overrides.put("fld", new int[] { -5, 0, 0, 59 });
/* 101 */     overrides.put("fild", new int[] { -3, 0, 0, 73, 59 });
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 107 */     overrides.put("int", new int[] { 194, -8 });
/*     */     
/* 109 */     overrides.put("ret", new int[] { 204 });
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 117 */     flagTypes = new java.util.HashMap();
/*     */     
/*     */ 
/* 120 */     flagTypes.put("neg", "FLAGS");
/* 121 */     flagTypes.put("add", "FLAGS");
/* 122 */     flagTypes.put("adc", "FLAGS");
/* 123 */     flagTypes.put("sub", "FLAGS");
/* 124 */     flagTypes.put("sbb", "FLAGS");
/* 125 */     flagTypes.put("cmp", "FLAGS");
/* 126 */     flagTypes.put("shr", "FLAGS_SHIFT");
/* 127 */     flagTypes.put("sar", "FLAGS_SHIFT");
/* 128 */     flagTypes.put("shl", "FLAGS_SHIFT");
/*     */     
/*     */ 
/* 131 */     substitutes = new java.util.HashMap();
/*     */     
/*     */ 
/* 134 */     substitutes.put("cmp", "SUB");
/* 135 */     substitutes.put("bts", "BT");
/* 136 */     substitutes.put("inc", "ADD");
/* 137 */     substitutes.put("dec", "SUB");
/* 138 */     substitutes.put("movsx", "SIGN_EXTEND");
/* 139 */     substitutes.put("movzx", "MASK2_");
/* 140 */     substitutes.put("fsubp", "FSUB");
/* 141 */     substitutes.put("faddp", "FADD");
/* 142 */     substitutes.put("fmulp", "FMUL");
/* 143 */     substitutes.put("fdivp", "FDIV");
/* 144 */     substitutes.put("fist", "D2I");
/*     */     
/*     */ 
/* 147 */     bitflags = new java.util.HashSet();
/*     */     
/*     */ 
/* 150 */     bitflags.add("and");
/* 151 */     bitflags.add("test");
/* 152 */     bitflags.add("xor");
/* 153 */     bitflags.add("or");
/*     */     
/*     */ 
/* 156 */     needsOperandSize = new java.util.HashSet();
/*     */     
/*     */ 
/* 159 */     needsOperandSize.add("bt");
/* 160 */     needsOperandSize.add("bts");
/* 161 */     needsOperandSize.add("btc");
/* 162 */     needsOperandSize.add("btr");
/* 163 */     needsOperandSize.add("rol");
/* 164 */     needsOperandSize.add("ror");
/* 165 */     needsOperandSize.add("rcr");
/* 166 */     needsOperandSize.add("rcl");
/*     */     
/*     */ 
/* 169 */     doesntStoreResult = new java.util.HashSet();
/*     */     
/*     */ 
/* 172 */     doesntStoreResult.add("bt");
/* 173 */     doesntStoreResult.add("cmp");
/*     */     
/*     */ 
/* 176 */     doesntLoadInput = new java.util.HashSet();
/*     */     
/*     */ 
/* 179 */     doesntLoadInput.add("sete");
/* 180 */     doesntLoadInput.add("setne");
/* 181 */     doesntLoadInput.add("setg");
/* 182 */     doesntLoadInput.add("setge");
/* 183 */     doesntLoadInput.add("setl");
/* 184 */     doesntLoadInput.add("setle");
/* 185 */     doesntLoadInput.add("seta");
/* 186 */     doesntLoadInput.add("setae");
/* 187 */     doesntLoadInput.add("setb");
/* 188 */     doesntLoadInput.add("setbe");
/* 189 */     doesntLoadInput.add("seto");
/* 190 */     doesntLoadInput.add("setno");
/* 191 */     doesntLoadInput.add("setp");
/* 192 */     doesntLoadInput.add("setnp");
/* 193 */     doesntLoadInput.add("sets");
/* 194 */     doesntLoadInput.add("setns");
/*     */   }
/*     */   
/*     */   private static final Map<String, String> substitutes;
/*     */   private static final Set<String> bitflags;
/*     */   public static int[] decode(BasicDisassembler.X86Opcode x86)
/*     */   {
/* 201 */     ArrayList<Integer> ucodes = new ArrayList();
/* 202 */     if (overrides.containsKey(x86.mnemonic))
/*     */     {
/* 204 */       int[] special = (int[])overrides.get(x86.mnemonic);
/* 205 */       for (int i = 0; i < special.length; i++)
/*     */       {
/* 207 */         if (special[i] == -1)
/*     */         {
/* 209 */           handleOperand(x86, ucodes, "LOAD", special[(i + 1)], special[(i + 2)]);
/* 210 */           i += 2;
/*     */         }
/* 212 */         else if (special[i] == -2)
/*     */         {
/* 214 */           handleOperand(x86, ucodes, "STORE", special[(i + 1)], special[(i + 2)]);
/* 215 */           i += 2;
/*     */         }
/* 217 */         else if (special[i] == -3)
/*     */         {
/* 219 */           handleOperand(x86, ucodes, "LOADL", special[(i + 1)], special[(i + 2)]);
/* 220 */           i += 2;
/*     */         }
/* 222 */         else if (special[i] == -4)
/*     */         {
/* 224 */           handleOperand(x86, ucodes, "STOREL", special[(i + 1)], special[(i + 2)]);
/* 225 */           i += 2;
/*     */         }
/* 227 */         else if (special[i] == -5)
/*     */         {
/* 229 */           handleOperand(x86, ucodes, "LOADF", special[(i + 1)], special[(i + 2)]);
/* 230 */           i += 2;
/*     */         }
/* 232 */         else if (special[i] == -6)
/*     */         {
/* 234 */           handleOperand(x86, ucodes, "STOREF", special[(i + 1)], special[(i + 2)]);
/* 235 */           i += 2;
/*     */         }
/* 237 */         else if (special[i] == -7)
/*     */         {
/* 239 */           ucodes.add(UCodes.getUCode(getOpcode(x86) + getOperandSize(x86)));
/*     */         }
/* 241 */         else if (special[i] == -8)
/*     */         {
/* 243 */           ucodes.add(Integer.valueOf(x86.getImmediate()));
/*     */         }
/*     */         else
/*     */         {
/* 247 */           ucodes.add(Integer.valueOf(special[i]));
/*     */         }
/*     */       }
/*     */     }
/* 251 */     else if (x86.isBranch())
/*     */     {
/* 253 */       ucodes.add(Integer.valueOf(1));
/* 254 */       ucodes.add(Integer.valueOf(x86.x86Length()));
/* 255 */       if (!x86.hasImmediate())
/* 256 */         handleOperand(x86, ucodes, "LOAD", 0, 0);
/* 257 */       loadOpcode(x86, ucodes);
/*     */     }
/*     */     else
/*     */     {
/* 261 */       if (isFpuOp(x86))
/*     */       {
/* 263 */         handleOperand(x86, ucodes, "LOADF", 0, 0);
/* 264 */         handleOperand(x86, ucodes, "LOADF", 1, 1);
/* 265 */         loadOpcode(x86, ucodes);
/* 266 */         handleOperand(x86, ucodes, "STOREF", 2, 0);
/* 267 */         if (x86.mnemonic.endsWith("p"))
/* 268 */           ucodes.add(Integer.valueOf(50));
/* 269 */         if (x86.mnemonic.endsWith("pp")) {
/* 270 */           ucodes.add(Integer.valueOf(50));
/*     */         }
/*     */       }
/*     */       else {
/* 274 */         if (!doesntLoadInput.contains(x86.mnemonic))
/*     */         {
/* 276 */           handleOperand(x86, ucodes, "LOAD", 0, 0);
/* 277 */           handleOperand(x86, ucodes, "LOAD", 1, 1);
/*     */         }
/* 279 */         loadOpcode(x86, ucodes);
/* 280 */         if (!doesntStoreResult.contains(x86.mnemonic))
/* 281 */           handleOperand(x86, ucodes, "STORE", 2, 0);
/* 282 */         if (flagTypes.containsKey(x86.mnemonic))
/*     */         {
/* 284 */           ucodes.add(UCodes.getUCode(getFlagsOp(x86)));
/* 285 */           ucodes.add(UCodes.getUCode(getOpcode(x86) + getOperandSize(x86)));
/* 286 */         } else if (bitflags.contains(x86.mnemonic))
/*     */         {
/* 288 */           ucodes.add(UCodes.getUCode("BITWISE_FLAGS" + getOperandSize(x86)));
/*     */         }
/*     */       }
/* 291 */       ucodes.add(Integer.valueOf(1));
/* 292 */       ucodes.add(Integer.valueOf(x86.x86Length()));
/*     */     }
/*     */     
/* 295 */     int[] res = new int[ucodes.size()];
/* 296 */     for (int i = 0; i < res.length; i++)
/* 297 */       res[i] = ((Integer)ucodes.get(i)).intValue();
/* 298 */     return res;
/*     */   }
/*     */   
/*     */   public static void loadOpcode(BasicDisassembler.X86Opcode x86, ArrayList<Integer> ucodes)
/*     */   {
/* 303 */     if (needsOperandSize(x86)) {
/* 304 */       ucodes.add(UCodes.getUCode(getOpcode(x86) + getOperandSize(x86)));
/*     */     } else {
/* 306 */       ucodes.add(UCodes.getUCode(getOpcode(x86)));
/*     */     }
/* 308 */     if ((x86.isBranch()) && 
/* 309 */       (x86.hasImmediate())) {
/* 310 */       ucodes.add(Integer.valueOf(x86.getImmediate()));
/*     */     }
/*     */   }
/*     */   
/*     */   public static String getOpcode(BasicDisassembler.X86Opcode x86) {
/* 315 */     if (substitutes.containsKey(x86.mnemonic))
/* 316 */       return (String)substitutes.get(x86.mnemonic);
/* 317 */     return x86.mnemonic.toUpperCase();
/*     */   }
/*     */   
/*     */   public static boolean needsOperandSize(BasicDisassembler.X86Opcode x86)
/*     */   {
/* 322 */     if ((flagTypes.containsKey(x86.mnemonic)) || (bitflags.contains(x86.mnemonic)))
/* 323 */       return true;
/* 324 */     if (needsOperandSize.contains(x86.mnemonic))
/* 325 */       return true;
/* 326 */     return false;
/*     */   }
/*     */   
/*     */ 
/*     */   public static void handleOperand(BasicDisassembler.X86Opcode x86, ArrayList<Integer> ucodes, String prefix, int regIndex, int argIndex)
/*     */   {
/* 332 */     ucodes.add(UCodes.getUCode(prefix + regIndex + "_" + x86.getArg(argIndex)));
/*     */   }
/*     */   
/*     */ 
/*     */   public static int getOperandSize(BasicDisassembler.X86Opcode x86)
/*     */   {
/* 338 */     return 32;
/*     */   }
/*     */   
/*     */   public static String getFlagsOp(BasicDisassembler.X86Opcode x86)
/*     */   {
/* 343 */     return (String)flagTypes.get(x86.mnemonic);
/*     */   }
/*     */   
/*     */   public static boolean isFpuOp(BasicDisassembler.X86Opcode x86)
/*     */   {
/* 348 */     return x86.mnemonic.startsWith("f");
/*     */   }
/*     */   
/*     */ 
/*     */   private static final Set<String> needsOperandSize;
/*     */   private static final Set<String> doesntStoreResult;
/*     */   private static final Set<String> doesntLoadInput;
/*     */   public static void randomTests()
/*     */     throws java.io.IOException
/*     */   {
/* 358 */     java.util.Random r = new java.util.Random();
/* 359 */     byte[] next = new byte[16];
/* 360 */     for (int i = 0; i < 1000000; i++)
/*     */     {
/* 362 */       r.nextBytes(next);
/* 363 */       test(next);
/*     */     }
/*     */   }
/*     */   
/*     */   public static void test(byte[] raw) throws java.io.IOException
/*     */   {
/* 369 */     int[] olducodes = new int[0];
/* 370 */     int[] newucodes = new int[0];
/*     */     try {
/* 372 */       olducodes = UCodes.decode(Disassembler.getDisassembler().disassemble(new java.io.ByteArrayInputStream(raw)));
/* 373 */     } catch (Exception e) { return;
/*     */     }
/* 375 */     BasicDisassembler.X86Opcode x86 = null;
/*     */     try {
/* 377 */       support.BasicDisassembler dis = new support.BasicDisassembler();
/* 378 */       x86 = dis.getOpcode(new java.io.ByteArrayInputStream(raw));
/* 379 */       newucodes = decode(x86);
/* 380 */     } catch (Exception e) { e.printStackTrace();
/*     */     }
/* 382 */     if (olducodes.length != newucodes.length) {
/* 383 */       printArrays(olducodes, newucodes, x86);
/*     */     } else {
/* 385 */       for (int i = 0; i < olducodes.length; i++)
/* 386 */         if (olducodes[i] != newucodes[i])
/* 387 */           printArrays(olducodes, newucodes, x86);
/*     */     }
/*     */   }
/*     */   
/*     */   private static void printArrays(int[] oldu, int[] newu, BasicDisassembler.X86Opcode x86) {
/* 392 */     System.out.println("Difference in: " + (x86 != null ? x86.mnemonic : null));
/* 393 */     System.out.print("OLD: ");
/* 394 */     for (int i = 0; i < oldu.length; i++)
/* 395 */       System.out.print(oldu[i] + " ");
/* 396 */     System.out.println();
/*     */     
/* 398 */     System.out.print("NEW: ");
/* 399 */     for (int i = 0; i < newu.length; i++)
/* 400 */       System.out.print(newu[i] + " ");
/* 401 */     System.out.println();
/*     */   }
/*     */   
/*     */   public static void main(String[] args)
/*     */     throws java.io.IOException
/*     */   {}
/*     */ }


/* Location:              C:\Users\Joey\Downloads\codejar.jar!\com\emt\proteus\decoder\Decoder.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */