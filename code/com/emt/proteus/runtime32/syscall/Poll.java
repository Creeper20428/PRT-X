/*    */ package com.emt.proteus.runtime32.syscall;
/*    */ 
/*    */ import com.emt.proteus.runtime32.ThreadContext;
/*    */ import com.emt.proteus.runtime32.io.IoLib;
/*    */ import com.emt.proteus.runtime32.memory.MainMemory;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public final class Poll
/*    */ {
/*    */   public static final int POLLFD_SIZE = 8;
/*    */   public static final int DESCRIPTOR = 0;
/*    */   
/*    */   public static int poll(ThreadContext ctx, int fds$, int numFds, int timeout)
/*    */   {
/* 25 */     int updated = 0;
/* 26 */     long end = timeout < 0 ? Long.MAX_VALUE : System.currentTimeMillis() + timeout;
/* 27 */     while ((updated == 0) && (end > System.currentTimeMillis())) {
/* 28 */       int current$ = fds$;
/* 29 */       for (int i = 0; i < numFds; i++) {
/* 30 */         int fd = getDescriptor(ctx, current$);
/* 31 */         int requested = getRequestedEvents(ctx, current$);
/*    */         
/* 33 */         requested = requested | 0x8 | 0x10 | 0x20;
/* 34 */         int received = ctx.iolib.checkEvents(fd, requested);
/* 35 */         received &= requested;
/* 36 */         setReceivedEvents(ctx, current$, (short)received);
/* 37 */         if (received != 0) updated++;
/* 38 */         current$ += 8;
/*    */       }
/* 40 */       if (updated == 0) {
/* 41 */         long ms = 10L;
/* 42 */         sleep(ms);
/*    */       }
/*    */     }
/* 45 */     return updated;
/*    */   }
/*    */   
/*    */   private static void sleep(long ms)
/*    */   {
/*    */     try {
/* 51 */       Thread.sleep(ms);
/*    */     } catch (InterruptedException e) {
/* 53 */       e.printStackTrace();
/*    */     }
/*    */   }
/*    */   
/*    */   private static int getDescriptor(ThreadContext ctx, int pollfd$)
/*    */   {
/* 59 */     return ctx.memory.getDoubleWord(pollfd$);
/*    */   }
/*    */   
/*    */   private static short getRequestedEvents(ThreadContext ctx, int pollfd$) {
/* 63 */     return ctx.memory.getWord(pollfd$ + 4);
/*    */   }
/*    */   
/*    */   private static void setReceivedEvents(ThreadContext ctx, int pollfd$, short events) {
/* 67 */     ctx.memory.setWord(pollfd$ + 6, events);
/*    */   }
/*    */ }


/* Location:              C:\Users\Joey\Downloads\codejar.jar!\com\emt\proteus\runtime32\syscall\Poll.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */