/*     */ package com.emt.proteus.runtime32;
/*     */ 
/*     */ import com.emt.proteus.decoder.UCodes;
/*     */ import java.io.DataInput;
/*     */ import java.io.DataOutput;
/*     */ import java.io.IOException;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class Processor
/*     */ {
/*  15 */   private static final boolean[] parityMap = new boolean['Ā'];
/*  16 */   static { for (int i = 0; i < parityMap.length; i++) {
/*  17 */       parityMap[i] = ((Integer.bitCount(i) & 0x1) == 0 ? 1 : false);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   public static final int EFLAGS_RESERVED_MASK = 4161493;
/*     */   
/*     */   public static final int EFLAGS_CARRY = 1;
/*     */   
/*     */   public static final int EFLAGS_PARITY = 4;
/*     */   public static final int EFLAGS_AUX_CARRY = 16;
/*     */   public static final int EFLAGS_ZERO = 64;
/*     */   public static final int EFLAGS_SIGN = 128;
/*     */   public static final int EFLAGS_TRAP = 256;
/*     */   public static final int EFLAGS_INT = 512;
/*     */   public static final int EFLAGS_DIR = 1024;
/*     */   public static final int EFLAGS_OFLW = 2048;
/*     */   public static final int EFLAGS_NESTED = 16384;
/*     */   public static final int EFLAGS_RESUME = 65536;
/*     */   public static final int EFLAGS_V8086 = 131072;
/*     */   public static final int EFLAGS_ALIGN = 262144;
/*     */   public static final int EFLAGS_VINT = 524288;
/*     */   public static final int EFLAGS_VINT_PEND = 1048576;
/*     */   public static final int EFLAGS_ID = 2097152;
/*     */   public static final int FPU_TAG_VALID = 0;
/*     */   public static final int FPU_TAG_ZERO = 1;
/*     */   public static final int FPU_TAG_SPECIAL = 2;
/*     */   public static final int FPU_TAG_EMPTY = 3;
/*     */   public static final int FPU_SPECIAL_TAG_NONE = 0;
/*     */   public static final int FPU_SPECIAL_TAG_NAN = 1;
/*     */   public static final int FPU_SPECIAL_TAG_UNSUPPORTED = 2;
/*     */   public static final int FPU_SPECIAL_TAG_INFINITY = 3;
/*     */   public static final int FPU_SPECIAL_TAG_DENORMAL = 4;
/*     */   public static final int FPU_SPECIAL_TAG_SNAN = 5;
/*  51 */   public static final double UNDERFLOW_THRESHOLD = Math.pow(2.0D, -1022.0D);
/*     */   
/*  53 */   public static final ProcessorException DIVIDE_ERROR = new ProcessorException(0, "Divide Error");
/*  54 */   public static final ProcessorException BOUND_RANGE = new ProcessorException(5, "Bound Range");
/*  55 */   public static final ProcessorException UNDEFINED = new ProcessorException(6, "Undefined");
/*  56 */   public static final ProcessorException DOUBLE_FAULT = new ProcessorException(8, "Double Fault");
/*  57 */   public static final ProcessorException STACK_SEGMENT = new ProcessorException(12, "Stack Segment");
/*  58 */   public static final ProcessorException GENERAL_PROTECTION = new ProcessorException(13, "General Protection");
/*  59 */   public static final ProcessorException FLOATING_POINT = new ProcessorException(16, "Floading Point");
/*  60 */   public static final ProcessorException ALIGNMENT_CHECK = new ProcessorException(17, "Alignment Check");
/*     */   public volatile int eax;
/*     */   public volatile int ebx;
/*     */   public volatile int ecx;
/*     */   public volatile int edx;
/*     */   public volatile int esi;
/*     */   public volatile int edi;
/*     */   public volatile int esp;
/*     */   public volatile int ebp;
/*     */   public volatile int eip;
/*     */   public volatile int eflags;
/*  71 */   public int flagOp1; public int flagOp2; public int flagResult; public int flagIns; public int flagStatus; public boolean of; public boolean sf; public boolean zf; public boolean af; public boolean pf; public boolean cf; public boolean df; public Segment gs; public Segment gdt = new Segment(1082138624, 4096);
/*     */   public double[] fpuData;
/*     */   public int[] fpuTags;
/*     */   public int[] fpuSpecialTags;
/*     */   public volatile int fpuStatusWord;
/*     */   public volatile int fpuStackSize;
/*     */   public volatile int ftop;
/*     */   public FPU fpu;
/*     */   public final ThreadContext ctx;
/*     */   
/*     */   public Processor() {
/*  82 */     this(null);
/*     */   }
/*     */   
/*     */   public Processor(ThreadContext ctx) {
/*  86 */     this.ctx = ctx;
/*  87 */     this.fpu = new FPU();
/*  88 */     this.eflags = 514;
/*  89 */     this.ftop = 8;
/*  90 */     this.fpuStackSize = 8;
/*  91 */     this.fpuData = new double[8];
/*  92 */     this.fpuTags = new int[8];
/*  93 */     this.fpuSpecialTags = new int[8];
/*     */     
/*  95 */     for (int i = 0; i < this.fpuTags.length; i++)
/*  96 */       this.fpuTags[i] = 3;
/*  97 */     for (int i = 0; i < this.fpuSpecialTags.length; i++) {
/*  98 */       this.fpuSpecialTags[i] = 0;
/*     */     }
/*     */   }
/*     */   
/*     */   public void saveState(DataOutput out) throws IOException {
/* 103 */     out.writeInt(this.eax);
/* 104 */     out.writeInt(this.ebx);
/* 105 */     out.writeInt(this.ecx);
/* 106 */     out.writeInt(this.edx);
/* 107 */     out.writeInt(this.esi);
/* 108 */     out.writeInt(this.edi);
/* 109 */     out.writeInt(this.esp);
/* 110 */     out.writeInt(this.ebp);
/* 111 */     out.writeInt(this.eip);
/* 112 */     out.writeInt(getEflags());
/*     */     
/* 114 */     out.writeInt(this.fpuStatusWord);
/* 115 */     out.writeInt(this.fpuStackSize);
/* 116 */     out.writeInt(this.ftop);
/* 117 */     for (int i = 0; i < 8; i++)
/*     */     {
/* 119 */       out.writeDouble(this.fpuData[i]);
/* 120 */       out.writeInt(this.fpuTags[i]);
/* 121 */       out.writeInt(this.fpuSpecialTags[i]);
/*     */     }
/* 123 */     if (this.gs != null)
/*     */     {
/* 125 */       out.writeInt(this.gs.selector);
/* 126 */       out.writeLong(this.gs.descriptor);
/*     */     }
/*     */     else {
/* 129 */       out.writeInt(0);
/* 130 */       out.writeLong(0L);
/*     */     }
/*     */   }
/*     */   
/*     */   public void loadState(DataInput in) throws IOException
/*     */   {
/* 136 */     this.eax = in.readInt();
/* 137 */     this.ebx = in.readInt();
/* 138 */     this.ecx = in.readInt();
/* 139 */     this.edx = in.readInt();
/* 140 */     this.esi = in.readInt();
/* 141 */     this.edi = in.readInt();
/* 142 */     this.esp = in.readInt();
/* 143 */     this.ebp = in.readInt();
/* 144 */     this.eip = in.readInt();
/* 145 */     setEflags(in.readInt());
/*     */     
/* 147 */     this.fpuStatusWord = in.readInt();
/* 148 */     this.fpuStackSize = in.readInt();
/* 149 */     this.ftop = in.readInt();
/* 150 */     for (int i = 0; i < 8; i++)
/*     */     {
/* 152 */       this.fpuData[i] = in.readDouble();
/* 153 */       this.fpuTags[i] = in.readInt();
/* 154 */       this.fpuSpecialTags[i] = in.readInt();
/*     */     }
/* 156 */     int selector = in.readInt();
/* 157 */     long descriptor = in.readLong();
/* 158 */     this.gs = new Segment(selector, descriptor);
/*     */   }
/*     */   
/*     */   public int getEflags()
/*     */   {
/* 163 */     int tflags = this.eflags & 0xF32A;
/* 164 */     if (getCarryFlag(this.flagStatus, this.cf, this.flagOp1, this.flagOp2, this.flagResult, this.flagIns))
/* 165 */       tflags |= 0x1;
/* 166 */     if (getParityFlag(this.flagStatus, this.pf, this.flagResult))
/* 167 */       tflags |= 0x4;
/* 168 */     if (getAuxCarryFlag(this.flagStatus, this.af, this.flagOp1, this.flagOp2, this.flagResult, this.flagIns))
/* 169 */       tflags |= 0x10;
/* 170 */     if (getZeroFlag(this.flagStatus, this.zf, this.flagResult))
/* 171 */       tflags |= 0x40;
/* 172 */     if (getSignFlag(this.flagStatus, this.sf, this.flagResult))
/* 173 */       tflags |= 0x80;
/* 174 */     if (getOverflowFlag(this.flagStatus, this.of, this.flagOp1, this.flagOp2, this.flagResult, this.flagIns))
/* 175 */       tflags |= 0x800;
/* 176 */     if (this.df)
/* 177 */       tflags |= 0x400;
/* 178 */     return tflags;
/*     */   }
/*     */   
/*     */   public void setEflags(int flags)
/*     */   {
/* 183 */     this.eflags = flags;
/* 184 */     this.cf = ((flags & 0x1) != 0);
/* 185 */     this.pf = ((flags & 0x4) != 0);
/* 186 */     this.af = ((flags & 0x10) != 0);
/* 187 */     this.zf = ((flags & 0x40) != 0);
/* 188 */     this.sf = ((flags & 0x80) != 0);
/* 189 */     this.of = ((flags & 0x800) != 0);
/* 190 */     this.df = ((flags & 0x400) != 0);
/* 191 */     this.flagStatus = 0;
/*     */   }
/*     */   
/*     */   public static String getFlags(int flags)
/*     */   {
/* 196 */     StringBuffer b = new StringBuffer();
/* 197 */     if ((flags & 0x1) != 0)
/* 198 */       b.append("CARRY ");
/* 199 */     if ((flags & 0x4) != 0)
/* 200 */       b.append("PARITY ");
/* 201 */     if ((flags & 0x10) != 0)
/* 202 */       b.append("AUX_CARRY ");
/* 203 */     if ((flags & 0x40) != 0)
/* 204 */       b.append("ZERO ");
/* 205 */     if ((flags & 0x80) != 0)
/* 206 */       b.append("SIGN ");
/* 207 */     if ((flags & 0x200) != 0)
/* 208 */       b.append("INT ");
/* 209 */     if ((flags & 0x400) != 0)
/* 210 */       b.append("DIR ");
/* 211 */     if ((flags & 0x800) != 0)
/* 212 */       b.append("OF ");
/* 213 */     return b.toString();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public static class ProcessorException
/*     */     extends RuntimeException
/*     */   {
/*     */     private String name;
/*     */     
/*     */     private int vector;
/*     */     
/*     */ 
/*     */     ProcessorException(int v, String name)
/*     */     {
/* 228 */       this.vector = v;
/* 229 */       this.name = name;
/*     */     }
/*     */   }
/*     */   
/*     */   public int getCPL()
/*     */   {
/* 235 */     return 3;
/*     */   }
/*     */   
/*     */   public void reportFPUException()
/*     */   {
/* 240 */     throw new IllegalStateException("Floating point syscall");
/*     */   }
/*     */   
/*     */ 
/*     */   public static boolean getSignFlag(int status, boolean sf, int result)
/*     */   {
/* 246 */     if ((status & 0x80) == 0) {
/* 247 */       return sf;
/*     */     }
/* 249 */     return getSignFlag(result);
/*     */   }
/*     */   
/*     */   public static boolean getZeroFlag(int status, boolean zf, int result)
/*     */   {
/* 254 */     if ((status & 0x40) == 0) {
/* 255 */       return zf;
/*     */     }
/* 257 */     return getZeroFlag(result);
/*     */   }
/*     */   
/*     */   public static boolean getParityFlag(int status, boolean pf, int result)
/*     */   {
/* 262 */     if ((status & 0x4) == 0) {
/* 263 */       return pf;
/*     */     }
/* 265 */     return getParityFlag(result);
/*     */   }
/*     */   
/*     */   public static boolean getCarryFlag(int status, boolean cf, int op1, int op2, int result, int instr)
/*     */   {
/* 270 */     if ((status & 0x1) == 0) {
/* 271 */       return cf;
/*     */     }
/* 273 */     return getCarryFlag(op1, op2, result, instr);
/*     */   }
/*     */   
/*     */   public static boolean getAuxCarryFlag(int status, boolean af, int op1, int op2, int result, int instr)
/*     */   {
/* 278 */     if ((status & 0x10) == 0) {
/* 279 */       return af;
/*     */     }
/* 281 */     return getAuxCarryFlag(op1, op2, result, instr);
/*     */   }
/*     */   
/*     */   public static boolean getOverflowFlag(int status, boolean of, int op1, int op2, int result, int instr)
/*     */   {
/* 286 */     if ((status & 0x800) == 0) {
/* 287 */       return of;
/*     */     }
/* 289 */     return getOverflowFlag(op1, op2, result, instr);
/*     */   }
/*     */   
/*     */ 
/*     */   public static boolean getSignFlag(int result)
/*     */   {
/* 295 */     return result < 0;
/*     */   }
/*     */   
/*     */   public static boolean getZeroFlag(int result)
/*     */   {
/* 300 */     return result == 0;
/*     */   }
/*     */   
/*     */   public static boolean getParityFlag(int result)
/*     */   {
/* 305 */     return parityMap[(result & 0xFF)];
/*     */   }
/*     */   
/*     */   public static boolean getCarryFlag(int op1, int op2, int result, int instr)
/*     */   {
/* 310 */     switch (instr)
/*     */     {
/*     */     case 131: 
/* 313 */       if ((result & 0xFF) != (op1 & 0xFF) + (op2 & 0xFF)) {
/* 314 */         return (op1 & 0xFF) + (op2 & 0xFF) + 1 > 255;
/*     */       }
/* 316 */       return (op1 & 0xFF) + (op2 & 0xFF) > 255;
/*     */     case 132: 
/* 318 */       if ((result & 0xFFFF) != (op1 & 0xFFFF) + (op2 & 0xFFFF)) {
/* 319 */         return (op1 & 0xFFFF) + (op2 & 0xFFFF) + 1 > 65535;
/*     */       }
/* 321 */       return (op1 & 0xFFFF) + (op2 & 0xFFFF) > 65535;
/*     */     case 133: 
/* 323 */       if (result != op1 + op2) {
/* 324 */         return (op1 & 0xFFFFFFFF) + (op2 & 0xFFFFFFFF) + 1L > 4294967295L;
/*     */       }
/* 326 */       return (op1 & 0xFFFFFFFF) + (op2 & 0xFFFFFFFF) > 4294967295L;
/*     */     case 128: 
/* 328 */       return (result & 0xFF) < (op1 & 0xFF);
/*     */     case 129: 
/* 330 */       return (result & 0xFFFF) < (op1 & 0xFFFF);
/*     */     case 130: 
/* 332 */       return (result & 0xFFFFFFFF) < (op1 & 0xFFFFFFFF);
/*     */     case 134: 
/* 334 */       return (op1 & 0xFF) < (op2 & 0xFF);
/*     */     case 135: 
/*     */     case 136: 
/* 337 */       return (op1 & 0xFFFFFFFF) < (op2 & 0xFFFFFFFF);
/*     */     case 137: 
/* 339 */       if ((byte)result - (byte)op1 + (byte)op2 != 0) {
/* 340 */         return ((op1 & 0xFF) < (result & 0xFF)) || ((op2 & 0xFF) == 255);
/*     */       }
/* 342 */       return (op1 & 0xFF) < (op2 & 0xFF);
/*     */     case 138: 
/* 344 */       if ((short)result - (short)op1 + (short)op2 != 0) {
/* 345 */         return ((op1 & 0xFFFF) < (result & 0xFFFF)) || ((op2 & 0xFFFF) == 65535);
/*     */       }
/* 347 */       return (op1 & 0xFFFF) < (op2 & 0xFFFF);
/*     */     case 139: 
/* 349 */       if (result - op1 + op2 != 0) {
/* 350 */         return ((op1 & 0xFFFFFFFF) < (result & 0xFFFFFFFF)) || (op2 == -1);
/*     */       }
/* 352 */       return (op1 & 0xFFFFFFFF) < (op2 & 0xFFFFFFFF);
/*     */     case 142: 
/*     */     case 143: 
/*     */     case 144: 
/* 356 */       return result != 0;
/*     */     case 145: 
/*     */     case 146: 
/*     */     case 147: 
/* 360 */       return (op1 >> op2 - 1 & 0x1) != 0;
/*     */     case 148: 
/* 362 */       return (op1 >> 8 - op2 & 0x1) != 0;
/*     */     case 149: 
/* 364 */       return (op1 >> 16 - op2 & 0x1) != 0;
/*     */     case 150: 
/* 366 */       return (op1 >> 32 - op2 & 0x1) != 0;
/*     */     case 151: 
/* 368 */       if (op2 <= 16) {
/* 369 */         return (op1 >> 16 - op2 & 0x1) != 0;
/*     */       }
/* 371 */       return (op1 >> 32 - op2 & 0x1) != 0;
/*     */     case 152: 
/* 373 */       return (op1 >> 32 - op2 & 0x1) != 0;
/*     */     case 153: 
/* 375 */       return ((op1 & 0x80) == (op2 & 0x80)) && ((result & 0xFF00) != 0);
/*     */     case 154: 
/* 377 */       return ((op1 & 0x8000) == (op2 & 0x8000)) && ((op1 * op2 & 0xFFFF0000) != 0);
/*     */     case 155: 
/* 379 */       return ((op1 & 0x80000000) == (op2 & 0x80000000)) && ((op1 * op2 & 0xFFFFFFFF00000000) != 0L);
/*     */     case 125: 
/*     */     case 126: 
/*     */     case 127: 
/* 383 */       return (op1 >> op2 - 1 & 0x1) != 0;
/*     */     }
/* 385 */     throw new IllegalStateException("Unknown flag method: " + instr);
/*     */   }
/*     */   
/*     */ 
/*     */   public static boolean getAuxCarryFlag(int op1, int op2, int result, int instr)
/*     */   {
/* 391 */     switch (instr)
/*     */     {
/*     */     case 128: 
/*     */     case 129: 
/*     */     case 130: 
/*     */     case 131: 
/*     */     case 132: 
/*     */     case 133: 
/*     */     case 134: 
/*     */     case 135: 
/*     */     case 136: 
/*     */     case 137: 
/*     */     case 138: 
/*     */     case 139: 
/* 405 */       return ((op1 ^ op2 ^ result) & 0x10) != 0;
/*     */     case 142: 
/*     */     case 143: 
/*     */     case 144: 
/* 409 */       return (result & 0xF) != 0;
/*     */     case 140: 
/* 411 */       return (result & 0xF) == 0;
/*     */     case 141: 
/* 413 */       return (result & 0xF) == 15;
/*     */     
/*     */     case 145: 
/*     */     case 146: 
/*     */     case 147: 
/* 418 */       return (result & 0x1) != 0;
/*     */     
/*     */ 
/*     */     case 148: 
/*     */     case 149: 
/*     */     case 150: 
/*     */     case 153: 
/*     */     case 154: 
/*     */     case 155: 
/* 427 */       return (result & 134217728 >> op2) != 0;
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     case 126: 
/*     */     case 127: 
/*     */     case 151: 
/*     */     case 152: 
/* 437 */       return false;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     case 125: 
/* 460 */       return false;
/*     */     }
/* 462 */     throw new IllegalStateException("Unknown flag method: " + instr);
/*     */   }
/*     */   
/*     */ 
/*     */   public static boolean getOverflowFlag(int op1, int op2, int result, int instr)
/*     */   {
/* 468 */     switch (instr)
/*     */     {
/*     */     case 128: 
/*     */     case 131: 
/* 472 */       return ((op1 ^ op2 ^ 0xFFFFFFFF) & (op2 ^ result) & 0x80) != 0;
/*     */     case 129: 
/*     */     case 132: 
/* 475 */       return ((op1 ^ op2 ^ 0xFFFFFFFF) & (op2 ^ result) & 0x8000) != 0;
/*     */     case 130: 
/*     */     case 133: 
/* 478 */       return ((op1 ^ op2 ^ 0xFFFFFFFF) & (op2 ^ result) & 0x80000000) != 0;
/*     */     case 134: 
/*     */     case 137: 
/* 481 */       return ((op1 ^ op2) & (op1 ^ result) & 0x80) != 0;
/*     */     case 135: 
/*     */     case 138: 
/* 484 */       return ((op1 ^ op2) & (op1 ^ result) & 0x8000) != 0;
/*     */     case 136: 
/*     */     case 139: 
/* 487 */       return ((op1 ^ op2) & (op1 ^ result) & 0x80000000) != 0;
/*     */     case 142: 
/* 489 */       return (result & 0xFF) == 128;
/*     */     case 143: 
/* 491 */       return (result & 0xFFFF) == 32768;
/*     */     case 140: 
/*     */     case 144: 
/* 494 */       return result == Integer.MIN_VALUE;
/*     */     case 141: 
/* 496 */       return result == 134217727;
/*     */     case 145: 
/*     */     case 146: 
/*     */     case 147: 
/* 500 */       return false;
/*     */     
/*     */     case 148: 
/*     */     case 149: 
/*     */     case 150: 
/* 505 */       return (result >> 31 != 0 ? 1 : 0) ^ ((op1 >> 32 - op2 & 0x1) != 0 ? 1 : 0);
/*     */     
/*     */ 
/*     */ 
/*     */     case 151: 
/*     */     case 152: 
/* 511 */       return getCarryFlag(op1, op2, result, instr) ^ result >> 31 != 0;
/*     */     case 126: 
/* 513 */       if (op2 == 1)
/* 514 */         return ((result << 1 ^ result) & 0x8000) != 0;
/* 515 */       return false;
/*     */     case 127: 
/* 517 */       if (op2 == 1)
/* 518 */         return (result << 1 ^ result) >> 31 != 0;
/* 519 */       return false;
/*     */     case 125: 
/* 521 */       return (result << 1 ^ result) >> 31 != 0;
/*     */     
/*     */     case 153: 
/* 524 */       return ((op1 & 0x80) == (op2 & 0x80)) && ((result & 0xFF00) != 0);
/*     */     case 154: 
/* 526 */       return ((op1 & 0x8000) == (op2 & 0x8000)) && ((op1 * op2 & 0xFFFF0000) != 0);
/*     */     case 155: 
/* 528 */       return ((op1 & 0x80000000) == (op2 & 0x80000000)) && ((op1 * op2 & 0xFFFFFFFF00000000) != 0L);
/*     */     }
/* 530 */     throw new IllegalStateException("Unknown flag method: " + instr + " = " + UCodes.getName(instr));
/*     */   }
/*     */   
/*     */ 
/*     */   public static int numberOfLeadingZeros(int i)
/*     */   {
/* 536 */     if (i == 0)
/* 537 */       return 32;
/* 538 */     int n = 1;
/* 539 */     if (i >>> 16 == 0) { n += 16;i <<= 16; }
/* 540 */     if (i >>> 24 == 0) { n += 8;i <<= 8; }
/* 541 */     if (i >>> 28 == 0) { n += 4;i <<= 4; }
/* 542 */     if (i >>> 30 == 0) { n += 2;i <<= 2; }
/* 543 */     n -= (i >>> 31);
/* 544 */     return n;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public static int numberOfTrailingZeros(int i)
/*     */   {
/* 551 */     if (i == 0) return 32;
/* 552 */     int n = 31;
/* 553 */     int y = i << 16; if (y != 0) { n -= 16;i = y; }
/* 554 */     y = i << 8; if (y != 0) { n -= 8;i = y; }
/* 555 */     y = i << 4; if (y != 0) { n -= 4;i = y; }
/* 556 */     y = i << 2; if (y != 0) { n -= 2;i = y; }
/* 557 */     return n - (i << 1 >>> 31);
/*     */   }
/*     */   
/*     */   public static double scaleTowardsZero(double d)
/*     */   {
/* 562 */     return (int)d;
/*     */   }
/*     */ }


/* Location:              C:\Users\Joey\Downloads\codejar.jar!\com\emt\proteus\runtime32\Processor.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */