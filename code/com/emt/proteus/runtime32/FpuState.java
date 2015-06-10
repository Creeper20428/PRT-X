/*     */ package com.emt.proteus.runtime32;
/*     */ 
/*     */ public abstract class FpuState { public abstract void setInvalidOperation();
/*     */   
/*     */   public abstract void setDenormalizedOperand();
/*     */   
/*     */   public abstract void setZeroDivide();
/*     */   
/*     */   public abstract void setOverflow();
/*     */   
/*     */   public abstract void setUnderflow();
/*     */   
/*     */   public abstract void setPrecision();
/*     */   
/*     */   public abstract void setStackFault();
/*     */   
/*     */   public abstract void setTagEmpty(int paramInt);
/*     */   
/*     */   public abstract void clearExceptions();
/*     */   
/*     */   public abstract void checkExceptions();
/*     */   
/*     */   public abstract boolean getInvalidOperation();
/*     */   
/*     */   public abstract boolean getDenormalizedOperand();
/*     */   
/*     */   public abstract boolean getZeroDivide();
/*     */   
/*     */   public abstract boolean getOverflow();
/*     */   
/*     */   public abstract boolean getUnderflow();
/*     */   
/*     */   public abstract boolean getPrecision();
/*     */   
/*     */   public abstract boolean getStackFault();
/*     */   
/*     */   public abstract boolean getErrorSummaryStatus();
/*     */   
/*     */   public abstract boolean getBusy();
/*     */   
/*     */   public abstract boolean getInvalidOperationMask();
/*     */   
/*     */   public abstract boolean getDenormalizedOperandMask();
/*     */   
/*     */   public abstract boolean getZeroDivideMask();
/*     */   
/*     */   public abstract boolean getOverflowMask();
/*     */   
/*     */   public abstract boolean getUnderflowMask();
/*     */   
/*     */   public abstract boolean getPrecisionMask();
/*     */   
/*     */   public abstract int getPrecisionControl();
/*     */   
/*     */   public abstract int getRoundingControl();
/*     */   
/*     */   public abstract void setInvalidOperationMask(boolean paramBoolean);
/*     */   
/*     */   public abstract void setDenormalizedOperandMask(boolean paramBoolean);
/*     */   
/*     */   public abstract void setZeroDivideMask(boolean paramBoolean);
/*     */   
/*     */   public abstract void setOverflowMask(boolean paramBoolean);
/*     */   
/*     */   public abstract void setUnderflowMask(boolean paramBoolean);
/*     */   
/*     */   public abstract void setPrecisionMask(boolean paramBoolean);
/*     */   
/*     */   public abstract void setPrecisionControl(int paramInt);
/*     */   
/*     */   public abstract void setRoundingControl(int paramInt);
/*     */   
/*     */   public abstract void setAllMasks(boolean paramBoolean);
/*     */   
/*     */   public abstract void init();
/*     */   
/*     */   public abstract void push(double paramDouble);
/*     */   
/*     */   public abstract double pop();
/*     */   
/*     */   public abstract double ST(int paramInt);
/*     */   
/*     */   public abstract void setST(int paramInt, double paramDouble);
/*     */   
/*     */   public abstract int getStatus();
/*     */   
/*     */   public abstract void setStatus(int paramInt);
/*     */   public abstract int getControl();
/*     */   public abstract void setControl(int paramInt);
/*     */   public abstract int getTagWord();
/*     */   public abstract void setTagWord(int paramInt);
/*     */   public abstract int getTag(int paramInt);
/*  93 */   public void copyStateInto(FpuState copy) { copy.conditionCode = this.conditionCode;
/*  94 */     copy.top = this.top;
/*  95 */     copy.infinityControl = this.infinityControl;
/*  96 */     copy.lastIP = this.lastIP;
/*  97 */     copy.lastData = this.lastData;
/*  98 */     copy.lastOpcode = this.lastOpcode; }
/*     */   
/*     */   public static final int STACK_DEPTH = 8;
/*     */   public static final int FPU_PRECISION_CONTROL_SINGLE = 0;
/*     */   
/* 103 */   public boolean equals(Object another) { if (!(another instanceof FpuState))
/* 104 */       return false;
/* 105 */     FpuState s = (FpuState)another;
/* 106 */     if ((s.conditionCode != this.conditionCode) || (s.top != this.top) || (s.infinityControl != this.infinityControl) || (s.lastIP != this.lastIP) || (s.lastData != this.lastData) || (s.lastOpcode != this.lastOpcode)) {
/* 107 */       return false;
/*     */     }
/* 109 */     return true;
/*     */   }
/*     */   
/*     */   public static final int FPU_PRECISION_CONTROL_DOUBLE = 2;
/*     */   public static final int FPU_PRECISION_CONTROL_EXTENDED = 3;
/*     */   public static final int FPU_ROUNDING_CONTROL_EVEN = 0;
/*     */   public static final int FPU_ROUNDING_CONTROL_DOWN = 1;
/*     */   public static final int FPU_ROUNDING_CONTROL_UP = 2;
/*     */   public static final int FPU_ROUNDING_CONTROL_TRUNCATE = 3;
/*     */   public static final int FPU_TAG_VALID = 0;
/*     */   public static final int FPU_TAG_ZERO = 1;
/*     */   public static final int FPU_TAG_SPECIAL = 2;
/*     */   public static final int FPU_TAG_EMPTY = 3;
/*     */   public int conditionCode;
/*     */   public int top;
/*     */   public boolean infinityControl;
/*     */   public long lastIP;
/*     */   public long lastData;
/*     */   public int lastOpcode;
/*     */ }


/* Location:              C:\Users\Joey\Downloads\codejar.jar!\com\emt\proteus\runtime32\FpuState.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */