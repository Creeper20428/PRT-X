/*     */ package com.emt.proteus.runtime32;
/*     */ 
/*     */ 
/*     */ public class FPU
/*     */ {
/*     */   int top;
/*     */   int PC;
/*     */   int RC;
/*   9 */   boolean IM = true; boolean DM = true; boolean ZM = true; boolean OM = true; boolean UM = true; boolean PM = true; boolean IEM = true; boolean IC = true;
/*     */   
/*     */ 
/*     */   boolean B;
/*     */   
/*     */   boolean C3;
/*     */   boolean C2;
/*     */   boolean C1;
/*     */   boolean C0;
/*     */   boolean IR;
/*     */   
/*  20 */   public int getRC() { return this.RC; }
/*     */   
/*     */   boolean SF;
/*     */   
/*     */   public void setCW(short cw) {
/*  25 */     this.IM = ((cw & 0x1) != 0);
/*  26 */     this.DM = ((cw & 0x2) != 0);
/*  27 */     this.ZM = ((cw & 0x4) != 0);
/*  28 */     this.OM = ((cw & 0x8) != 0);
/*  29 */     this.UM = ((cw & 0x10) != 0);
/*  30 */     this.PM = ((cw & 0x20) != 0);
/*  31 */     this.IEM = ((cw & 0x80) != 0);
/*  32 */     this.IC = ((cw & 0x1000) != 0);
/*  33 */     this.PC = (cw >> 8 & 0x3);
/*  34 */     this.RC = (cw >> 10 & 0x3); }
/*     */   
/*     */   boolean P;
/*     */   boolean U;
/*     */   
/*  39 */   public short getCW() { int c = 0;
/*  40 */     if (this.IM)
/*  41 */       c |= 0x1;
/*  42 */     if (this.DM)
/*  43 */       c |= 0x2;
/*  44 */     if (this.ZM)
/*  45 */       c |= 0x4;
/*  46 */     if (this.OM)
/*  47 */       c |= 0x8;
/*  48 */     if (this.UM)
/*  49 */       c |= 0x10;
/*  50 */     if (this.PM)
/*  51 */       c |= 0x20;
/*  52 */     if (this.IEM)
/*  53 */       c |= 0x80;
/*  54 */     if (this.IC)
/*  55 */       c |= 0x1000;
/*  56 */     c |= (this.PC & 0x3) << 8;
/*  57 */     c |= (this.RC & 0x3) << 10;
/*  58 */     return (short)c; }
/*     */   
/*     */   boolean O;
/*     */   boolean Z;
/*     */   
/*  63 */   public void setI(boolean b) { this.I = b; }
/*     */   
/*     */   boolean D;
/*     */   boolean I;
/*     */   public void setC0(boolean b) {
/*  68 */     this.C0 = b;
/*     */   }
/*     */   
/*     */   public void setC1(boolean b)
/*     */   {
/*  73 */     this.C1 = b;
/*     */   }
/*     */   
/*     */   public void setC2(boolean b)
/*     */   {
/*  78 */     this.C2 = b;
/*     */   }
/*     */   
/*     */   public void setC3(boolean b)
/*     */   {
/*  83 */     this.C3 = b;
/*     */   }
/*     */   
/*     */   public short getStatusWord(int ftop)
/*     */   {
/*  88 */     int s = 0;
/*  89 */     if (this.I)
/*  90 */       s |= 0x1;
/*  91 */     if (this.D)
/*  92 */       s |= 0x2;
/*  93 */     if (this.Z)
/*  94 */       s |= 0x4;
/*  95 */     if (this.O)
/*  96 */       s |= 0x8;
/*  97 */     if (this.U)
/*  98 */       s |= 0x10;
/*  99 */     if (this.P)
/* 100 */       s |= 0x20;
/* 101 */     if (this.SF)
/* 102 */       s |= 0x40;
/* 103 */     if (this.IR)
/* 104 */       s |= 0x80;
/* 105 */     if (this.C0)
/* 106 */       s |= 0x100;
/* 107 */     if (this.C1)
/* 108 */       s |= 0x200;
/* 109 */     if (this.C2)
/* 110 */       s |= 0x400;
/* 111 */     s |= ftop << 11;
/* 112 */     if (this.C3)
/* 113 */       s |= 0x4000;
/* 114 */     if (this.B)
/* 115 */       s |= 0x8000;
/* 116 */     return (short)s;
/*     */   }
/*     */ }


/* Location:              C:\Users\Joey\Downloads\codejar.jar!\com\emt\proteus\runtime32\FPU.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */