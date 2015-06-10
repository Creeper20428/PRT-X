/*    */ package com.emt.proteus.runtime32;
/*    */ 
/*    */ 
/*    */ public class SystemClock
/*    */ {
/*    */   private final long start;
/*    */   
/*    */   private final long startUSec;
/*    */   
/*    */   private final long actualStart;
/*    */   
/*    */   private final long nanoStart;
/*    */   
/*    */   public SystemClock()
/*    */   {
/* 16 */     this(System.currentTimeMillis());
/*    */   }
/*    */   
/*    */   public SystemClock(long start) {
/* 20 */     this.actualStart = System.currentTimeMillis();
/* 21 */     this.start = start;
/* 22 */     this.nanoStart = System.nanoTime();
/* 23 */     this.startUSec = (start * 1000L);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public long getTimeOfDay()
/*    */   {
/* 35 */     long inUseconds = getInternalUSeconds();
/*    */     
/* 37 */     long seconds = inUseconds / 1000000L;
/* 38 */     long useconds = inUseconds % 1000000L;
/*    */     
/*    */ 
/*    */ 
/*    */ 
/* 43 */     return seconds << 32 | useconds & 0xFFFFFFFFFFFFFFFF;
/*    */   }
/*    */   
/*    */   private long getInternalUSeconds() {
/* 47 */     long usecDelta = (System.nanoTime() - this.nanoStart) / 1000L;
/* 48 */     long useconds = usecDelta + this.startUSec;
/* 49 */     return useconds;
/*    */   }
/*    */   
/*    */   private long getInternalMillis() {
/* 53 */     return System.currentTimeMillis() - this.actualStart + this.start;
/*    */   }
/*    */   
/*    */   public static int seconds(long timeofday) {
/* 57 */     return (int)(timeofday >>> 32);
/*    */   }
/*    */   
/* 60 */   public static int useconds(long timeofday) { return (int)timeofday; }
/*    */   
/*    */   public long getStartTime()
/*    */   {
/* 64 */     return this.start;
/*    */   }
/*    */ }


/* Location:              C:\Users\Joey\Downloads\codejar.jar!\com\emt\proteus\runtime32\SystemClock.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */