/*     */ package com.emt.proteus.runtime32;
/*     */ 
/*     */ 
/*     */ public class FpuState64
/*     */   extends FpuState
/*     */ {
/*     */   public static final int FPU_SPECIAL_TAG_NONE = 0;
/*     */   public static final int FPU_SPECIAL_TAG_NAN = 1;
/*     */   public static final int FPU_SPECIAL_TAG_UNSUPPORTED = 2;
/*     */   public static final int FPU_SPECIAL_TAG_INFINITY = 3;
/*     */   public static final int FPU_SPECIAL_TAG_DENORMAL = 4;
/*     */   public static final int FPU_SPECIAL_TAG_SNAN = 5;
/*  13 */   public static final double UNDERFLOW_THRESHOLD = Math.pow(2.0D, -1022.0D);
/*     */   
/*     */   private Processor cpu;
/*     */   
/*     */   double[] data;
/*     */   int[] tag;
/*     */   int[] specialTag;
/*     */   private int statusWord;
/*     */   private boolean invalidOperation;
/*     */   private boolean denormalizedOperand;
/*     */   private boolean zeroDivide;
/*     */   private boolean overflow;
/*     */   private boolean underflow;
/*     */   private boolean precision;
/*     */   private boolean stackFault;
/*     */   private int maskWord;
/*     */   private int precisionControl;
/*     */   private int roundingControl;
/*     */   
/*  32 */   public boolean getInvalidOperation() { return (this.statusWord & 0x1) != 0; }
/*  33 */   public boolean getDenormalizedOperand() { return (this.statusWord & 0x2) != 0; }
/*  34 */   public boolean getZeroDivide() { return (this.statusWord & 0x4) != 0; }
/*  35 */   public boolean getOverflow() { return (this.statusWord & 0x8) != 0; }
/*  36 */   public boolean getUnderflow() { return (this.statusWord & 0x10) != 0; }
/*  37 */   public boolean getPrecision() { return (this.statusWord & 0x20) != 0; }
/*  38 */   public boolean getStackFault() { return (this.statusWord & 0x40) != 0; }
/*     */   
/*  40 */   public void setInvalidOperation() { this.statusWord |= 0x1; }
/*  41 */   public void setDenormalizedOperand() { this.statusWord |= 0x2; }
/*  42 */   public void setZeroDivide() { this.statusWord |= 0x4; }
/*  43 */   public void setOverflow() { this.statusWord |= 0x8; }
/*  44 */   public void setUnderflow() { this.statusWord |= 0x10; }
/*  45 */   public void setPrecision() { this.statusWord |= 0x20; }
/*  46 */   public void setStackFault() { this.statusWord |= 0x40; }
/*     */   
/*  48 */   public boolean getBusy() { return getErrorSummaryStatus(); }
/*     */   
/*     */ 
/*     */   public boolean getErrorSummaryStatus()
/*     */   {
/*  53 */     return (this.statusWord & 0x3F & (this.maskWord ^ 0xFFFFFFFF)) != 0;
/*     */   }
/*     */   
/*     */   public void checkExceptions()
/*     */   {
/*  58 */     if (getErrorSummaryStatus()) {
/*  59 */       this.cpu.reportFPUException();
/*     */     }
/*     */   }
/*     */   
/*     */   public void clearExceptions() {
/*  64 */     this.statusWord = 0;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  72 */   public boolean getInvalidOperationMask() { return (this.maskWord & 0x1) != 0; }
/*  73 */   public boolean getDenormalizedOperandMask() { return (this.maskWord & 0x2) != 0; }
/*  74 */   public boolean getZeroDivideMask() { return (this.maskWord & 0x4) != 0; }
/*  75 */   public boolean getOverflowMask() { return (this.maskWord & 0x8) != 0; }
/*  76 */   public boolean getUnderflowMask() { return (this.maskWord & 0x10) != 0; }
/*  77 */   public boolean getPrecisionMask() { return (this.maskWord & 0x20) != 0; }
/*  78 */   public int getPrecisionControl() { return this.precisionControl; }
/*  79 */   public int getRoundingControl() { return this.roundingControl; }
/*     */   
/*     */   public void setInvalidOperationMask(boolean value)
/*     */   {
/*  83 */     if (value) {
/*  84 */       this.maskWord |= 0x1;
/*     */     } else {
/*  86 */       this.maskWord &= 0xFFFFFFFE;
/*     */     }
/*     */   }
/*     */   
/*     */   public void setDenormalizedOperandMask(boolean value) {
/*  91 */     if (value) {
/*  92 */       this.maskWord |= 0x2;
/*     */     } else {
/*  94 */       this.maskWord &= 0xFFFFFFFD;
/*     */     }
/*     */   }
/*     */   
/*     */   public void setZeroDivideMask(boolean value) {
/*  99 */     if (value) {
/* 100 */       this.maskWord |= 0x4;
/*     */     } else {
/* 102 */       this.maskWord &= 0xFFFFFFFB;
/*     */     }
/*     */   }
/*     */   
/*     */   public void setOverflowMask(boolean value) {
/* 107 */     if (value) this.maskWord |= 0x8; else {
/* 108 */       this.maskWord &= 0xFFFFFFF7;
/*     */     }
/*     */   }
/*     */   
/*     */   public void setUnderflowMask(boolean value) {
/* 113 */     if (value) {
/* 114 */       this.maskWord |= 0x10;
/*     */     } else {
/* 116 */       this.maskWord &= 0xFFFFFFEF;
/*     */     }
/*     */   }
/*     */   
/*     */   public void setPrecisionMask(boolean value) {
/* 121 */     if (value) {
/* 122 */       this.maskWord |= 0x20;
/*     */     } else {
/* 124 */       this.maskWord &= 0xFFFFFFDF;
/*     */     }
/*     */   }
/*     */   
/*     */   public void setAllMasks(boolean value) {
/* 129 */     if (value) {
/* 130 */       this.maskWord |= 0x3F;
/*     */     } else {
/* 132 */       this.maskWord = 0;
/*     */     }
/*     */   }
/*     */   
/*     */   public void setPrecisionControl(int value) {
/* 137 */     this.precisionControl = 2;
/*     */   }
/*     */   
/*     */   public void setRoundingControl(int value)
/*     */   {
/* 142 */     this.roundingControl = 0;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public FpuState64(Processor owner)
/*     */   {
/* 149 */     this.cpu = owner;
/*     */     
/* 151 */     this.data = new double[8];
/* 152 */     this.tag = new int[8];
/* 153 */     this.specialTag = new int[8];
/* 154 */     init();
/*     */   }
/*     */   
/*     */ 
/*     */   public void init()
/*     */   {
/* 160 */     for (int i = 0; i < this.tag.length; i++)
/* 161 */       this.tag[i] = 3;
/* 162 */     for (int i = 0; i < this.specialTag.length; i++) {
/* 163 */       this.specialTag[i] = 0;
/*     */     }
/* 165 */     clearExceptions();
/* 166 */     this.conditionCode = 0;
/* 167 */     this.top = 0;
/*     */     
/* 169 */     setAllMasks(true);
/* 170 */     this.infinityControl = false;
/* 171 */     setPrecisionControl(2);
/*     */     
/* 173 */     setRoundingControl(0);
/* 174 */     this.lastIP = (this.lastData = this.lastOpcode = 0);
/*     */   }
/*     */   
/*     */   public int tagCode(double x)
/*     */   {
/* 179 */     if (x == 0.0D)
/* 180 */       return 1;
/* 181 */     if ((Double.isNaN(x)) || (Double.isInfinite(x))) {
/* 182 */       return 2;
/*     */     }
/* 184 */     return 0;
/*     */   }
/*     */   
/*     */   public static boolean isDenormal(double x)
/*     */   {
/* 189 */     long n = Double.doubleToRawLongBits(x);
/* 190 */     int exponent = (int)(n >> 52 & 0x7FF);
/* 191 */     if (exponent != 0)
/* 192 */       return false;
/* 193 */     long fraction = n & 0xFFFFFFFFFFFFF;
/* 194 */     if (fraction == 0L)
/* 195 */       return false;
/* 196 */     return true;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public static boolean isSNaN(long n)
/*     */   {
/* 203 */     int exponent = (int)(n >> 52 & 0x7FF);
/* 204 */     if (exponent != 2047)
/* 205 */       return false;
/* 206 */     long fraction = n & 0xFFFFFFFFFFFFF;
/* 207 */     if ((fraction & 0x8000000000000) != 0L)
/* 208 */       return false;
/* 209 */     return fraction != 0L;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static int specialTagCode(double x)
/*     */   {
/* 219 */     if (Double.isNaN(x))
/* 220 */       return 1;
/* 221 */     if (Double.isInfinite(x)) {
/* 222 */       return 3;
/*     */     }
/* 224 */     if (isDenormal(x)) {
/* 225 */       return 4;
/*     */     }
/* 227 */     return 0;
/*     */   }
/*     */   
/*     */   public void push(double x)
/*     */   {
/* 232 */     if (--this.top < 0)
/* 233 */       this.top = 7;
/* 234 */     if (this.tag[this.top] != 3)
/*     */     {
/* 236 */       setInvalidOperation();
/* 237 */       setStackFault();
/* 238 */       this.conditionCode |= 0x2;
/* 239 */       checkExceptions();
/*     */     }
/*     */     
/* 242 */     this.data[this.top] = x;
/* 243 */     this.tag[this.top] = tagCode(x);
/* 244 */     this.specialTag[this.top] = specialTagCode(x);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public double pop()
/*     */   {
/* 254 */     if (this.tag[this.top] == 3)
/*     */     {
/* 256 */       setInvalidOperation();
/* 257 */       setStackFault();
/* 258 */       this.conditionCode &= 0xFFFFFFFD;
/* 259 */       checkExceptions();
/*     */ 
/*     */ 
/*     */ 
/*     */     }
/* 264 */     else if (this.specialTag[this.top] == 5)
/*     */     {
/* 266 */       setInvalidOperation();
/* 267 */       checkExceptions();
/* 268 */       return NaN.0D;
/*     */     }
/* 270 */     double x = this.data[this.top];
/* 271 */     this.tag[this.top] = 3;
/* 272 */     if (++this.top >= 8)
/* 273 */       this.top = 0;
/* 274 */     return x;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public double ST(int index)
/*     */   {
/* 284 */     int i = this.top + index & 0x7;
/* 285 */     if (this.tag[i] == 3)
/*     */     {
/*     */ 
/*     */ 
/* 289 */       setInvalidOperation();
/* 290 */       setStackFault();
/* 291 */       this.conditionCode &= 0xFFFFFFFD;
/* 292 */       checkExceptions();
/*     */     }
/* 294 */     else if (this.specialTag[i] == 5)
/*     */     {
/* 296 */       setInvalidOperation();
/* 297 */       checkExceptions();
/* 298 */       return NaN.0D;
/*     */     }
/* 300 */     return this.data[i];
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public int getTag(int index)
/*     */   {
/* 310 */     int i = this.top + index & 0x7;
/* 311 */     return this.tag[i];
/*     */   }
/*     */   
/*     */   public int getSpecialTag(int index)
/*     */   {
/* 316 */     int i = this.top + index & 0x7;
/* 317 */     return this.specialTag[i];
/*     */   }
/*     */   
/*     */ 
/*     */   public void setTagEmpty(int index)
/*     */   {
/* 323 */     int i = this.top + index & 0x7;
/* 324 */     this.tag[i] = 3;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setST(int index, double value)
/*     */   {
/* 332 */     int i = this.top + index & 0x7;
/* 333 */     this.data[i] = value;
/* 334 */     this.tag[i] = tagCode(value);
/* 335 */     this.specialTag[i] = specialTagCode(value);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public int getStatus()
/*     */   {
/* 345 */     int w = this.statusWord;
/* 346 */     if (getErrorSummaryStatus())
/* 347 */       w |= 0x80;
/* 348 */     if (getBusy())
/* 349 */       w |= 0x8000;
/* 350 */     w |= this.top << 11;
/* 351 */     w |= (this.conditionCode & 0x7) << 8;
/* 352 */     w |= (this.conditionCode & 0x8) << 11;
/* 353 */     return w;
/*     */   }
/*     */   
/*     */   public void setStatus(int w)
/*     */   {
/* 358 */     this.statusWord &= 0xFFFFFF80;
/* 359 */     this.statusWord |= w & 0x7F;
/* 360 */     this.top = (w >> 11 & 0x7);
/* 361 */     this.conditionCode = (w >> 8 & 0x7);
/* 362 */     this.conditionCode |= w >>> 14 & 0x1;
/*     */   }
/*     */   
/*     */   public int getControl()
/*     */   {
/* 367 */     int w = this.maskWord;
/* 368 */     w |= (this.precisionControl & 0x3) << 8;
/* 369 */     w |= (this.roundingControl & 0x3) << 10;
/* 370 */     if (this.infinityControl)
/* 371 */       w |= 0x1000;
/* 372 */     return w;
/*     */   }
/*     */   
/*     */   public void setControl(int w)
/*     */   {
/* 377 */     this.maskWord &= 0xFFFFFFC0;
/* 378 */     this.maskWord |= w & 0x3F;
/* 379 */     this.infinityControl = ((w & 0x1000) != 0);
/* 380 */     setPrecisionControl(w >> 8 & 0x3);
/* 381 */     setRoundingControl(w >> 10 & 0x3);
/*     */   }
/*     */   
/*     */   public int getTagWord()
/*     */   {
/* 386 */     int w = 0;
/* 387 */     for (int i = 7; i >= 0; i--)
/* 388 */       w = w << 2 | this.tag[i] & 0x3;
/* 389 */     return w;
/*     */   }
/*     */   
/*     */   public void setTagWord(int w)
/*     */   {
/* 394 */     for (int i = 0; i < this.tag.length; i++)
/*     */     {
/* 396 */       int t = w & 0x3;
/* 397 */       if (t == 3) {
/* 398 */         this.tag[i] = 3;
/*     */       }
/*     */       else {
/* 401 */         this.tag[i] = tagCode(this.data[i]);
/* 402 */         if (this.specialTag[i] != 5) {
/* 403 */           this.specialTag[i] = specialTagCode(this.data[i]);
/*     */         }
/*     */       }
/* 406 */       w >>= 2;
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
/*     */   public static byte[] doubleToExtended(double x, boolean isSignalNaN)
/*     */   {
/* 419 */     byte[] b = new byte[10];
/* 420 */     long fraction = 0L;
/* 421 */     int iexp = 0;
/*     */     
/* 423 */     if (isSignalNaN)
/*     */     {
/* 425 */       fraction = -4611686018427387904L;
/*     */     }
/*     */     else
/*     */     {
/* 429 */       long n = Double.doubleToRawLongBits(x);
/* 430 */       fraction = n & 0xFFFFF;
/* 431 */       iexp = (int)(n >> 52) & 0x7FF;
/* 432 */       boolean sgn = (n & 0xFFFFFFFF80000000) != 0L;
/*     */       
/* 434 */       fraction |= 0x100000;
/* 435 */       fraction <<= 11;
/*     */       
/* 437 */       iexp += 15360;
/* 438 */       if (sgn) iexp |= 0x8000;
/*     */     }
/* 440 */     for (int i = 0; i < 8; i++)
/*     */     {
/* 442 */       b[i] = ((byte)(int)fraction);
/* 443 */       fraction >>>= 8;
/*     */     }
/* 445 */     b[8] = ((byte)iexp);
/* 446 */     b[9] = ((byte)(iexp >>> 8));
/* 447 */     return b;
/*     */   }
/*     */   
/*     */   public static int specialTagCode(byte[] b)
/*     */   {
/* 452 */     long fraction = 0L;
/* 453 */     for (int i = 7; i >= 0; i--)
/*     */     {
/* 455 */       long w = b[i] & 0xFF;
/* 456 */       fraction |= w;
/* 457 */       fraction <<= 8;
/*     */     }
/* 459 */     int iexp = b[8] & 0xFF | (b[9] & 0x7F) << 8;
/* 460 */     boolean sgn = (b[9] & 0x80) != 0;
/* 461 */     boolean integ = (b[7] & 0x80) != 0;
/*     */     
/* 463 */     if (iexp == 0)
/*     */     {
/* 465 */       if (integ)
/*     */       {
/*     */ 
/* 468 */         return 4;
/*     */       }
/*     */       
/*     */ 
/*     */ 
/* 473 */       return 4;
/*     */     }
/*     */     
/* 476 */     if (iexp == 32767)
/*     */     {
/* 478 */       if (fraction == 0L)
/*     */       {
/*     */ 
/* 481 */         return 2;
/*     */       }
/* 483 */       if (integ)
/*     */       {
/* 485 */         if (fraction << 1 == 0L)
/*     */         {
/*     */ 
/* 488 */           return 3;
/*     */         }
/*     */         
/*     */ 
/*     */ 
/* 493 */         if (fraction >>> 62 == 0L) return 5;
/* 494 */         return 1;
/*     */       }
/*     */       
/*     */ 
/*     */ 
/*     */ 
/* 500 */       return 2;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/* 505 */     if (integ)
/*     */     {
/*     */ 
/* 508 */       return 0;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/* 513 */     return 2;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public static double extendedToDouble(byte[] b)
/*     */   {
/* 520 */     long fraction = 0L;
/* 521 */     for (int i = 7; i >= 0; i--)
/*     */     {
/* 523 */       long w = b[i] & 0xFF;
/* 524 */       fraction |= w;
/* 525 */       fraction <<= 8;
/*     */     }
/* 527 */     int iexp = b[8] & 0xFF | (b[9] & 0x7F) << 8;
/* 528 */     boolean sgn = (b[9] & 0x80) != 0;
/* 529 */     boolean integ = (b[7] & 0x80) != 0;
/*     */     
/* 531 */     if (iexp == 0)
/*     */     {
/* 533 */       if (integ)
/*     */       {
/*     */ 
/*     */ 
/*     */ 
/* 538 */         iexp = 1;
/*     */       }
/*     */       
/*     */ 
/*     */ 
/*     */ 
/* 544 */       return 0.0D;
/*     */     }
/* 546 */     if (iexp == 32767)
/*     */     {
/* 548 */       if (fraction == 0L)
/*     */       {
/*     */ 
/*     */ 
/*     */ 
/* 553 */         return NaN.0D;
/*     */       }
/* 555 */       if (integ)
/*     */       {
/* 557 */         if (fraction << 1 == 0L)
/*     */         {
/* 559 */           return sgn ? Double.NEGATIVE_INFINITY : Double.POSITIVE_INFINITY;
/*     */         }
/*     */         
/*     */ 
/*     */ 
/*     */ 
/* 565 */         return NaN.0D;
/*     */       }
/*     */       
/*     */ 
/*     */ 
/*     */ 
/* 571 */       return NaN.0D;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/* 576 */     if (integ)
/*     */     {
/*     */ 
/* 579 */       iexp -= 15360;
/* 580 */       fraction >>>= 11;
/* 581 */       if (iexp > 2047)
/*     */       {
/*     */ 
/* 584 */         return sgn ? Double.NEGATIVE_INFINITY : Double.POSITIVE_INFINITY;
/*     */       }
/*     */       
/* 587 */       if (iexp < 0)
/*     */       {
/*     */ 
/* 590 */         fraction >>>= -iexp;
/* 591 */         iexp = 0;
/*     */       }
/* 593 */       fraction &= 0xFFFFFFFFFFFFF;
/* 594 */       fraction |= (iexp & 0x7FF) << 52;
/* 595 */       if (sgn) fraction |= 0xFFFFFFFF80000000;
/* 596 */       return Double.longBitsToDouble(fraction);
/*     */     }
/*     */     
/*     */ 
/*     */ 
/* 601 */     return NaN.0D;
/*     */   }
/*     */ }


/* Location:              C:\Users\Joey\Downloads\codejar.jar!\com\emt\proteus\runtime32\FpuState64.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */