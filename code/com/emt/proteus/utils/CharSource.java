/*     */ package com.emt.proteus.utils;
/*     */ 
/*     */ import java.io.IOException;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class CharSource
/*     */ {
/*     */   public static final int EOF = -1;
/*     */   private boolean pushedback;
/*     */   private int last;
/*     */   
/*     */   public final int next()
/*     */   {
/*     */     int val;
/*     */     int val;
/*  39 */     if (this.pushedback) {
/*  40 */       val = this.last;
/*     */     } else {
/*     */       try {
/*  43 */         val = nextImpl();
/*     */       } catch (IOException ioe) {
/*  45 */         val = -1;
/*     */       }
/*     */     }
/*  48 */     this.pushedback = false;
/*  49 */     this.last = val;
/*  50 */     return val;
/*     */   }
/*     */   
/*     */   protected abstract int nextImpl() throws IOException;
/*     */   
/*     */   public final void pushback()
/*     */   {
/*  57 */     this.pushedback = true;
/*     */   }
/*     */   
/*     */   public final void skip(int amt) {
/*     */     try {
/*  62 */       doSkip(amt);
/*     */     }
/*     */     catch (IOException e) {}
/*     */     
/*  66 */     this.pushedback = false;
/*     */   }
/*     */   
/*     */   protected void doSkip(int amt) throws IOException
/*     */   {
/*  71 */     for (int i = 0; i < amt; i++) {
/*  72 */       next();
/*     */     }
/*     */   }
/*     */   
/*     */   public static final class Str extends CharSource
/*     */   {
/*     */     private int index;
/*     */     private final int length;
/*     */     private final String src;
/*     */     
/*     */     public Str(String src) {
/*  83 */       this.src = src;
/*  84 */       this.length = src.length();
/*  85 */       this.index = 0;
/*     */     }
/*     */     
/*     */     protected int nextImpl()
/*     */       throws IOException
/*     */     {
/*  91 */       if (this.index < this.length)
/*  92 */         return this.src.charAt(this.index++);
/*  93 */       return -1;
/*     */     }
/*     */     
/*     */     protected void doSkip(int amt)
/*     */       throws IOException
/*     */     {
/*  99 */       this.index += amt;
/*     */     }
/*     */     
/*     */     public String toString() {
/* 103 */       return "Str{" + this.src + "}";
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Joey\Downloads\codejar.jar!\com\emt\proteus\utils\CharSource.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */