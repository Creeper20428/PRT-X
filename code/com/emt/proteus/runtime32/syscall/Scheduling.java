/*    */ package com.emt.proteus.runtime32.syscall;
/*    */ 
/*    */ import com.emt.proteus.runtime32.ThreadContext;
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
/*    */ public class Scheduling
/*    */ {
/*    */   public static final int PRIORITY_OFFSET = 0;
/*    */   
/*    */   public static int getParam(ThreadContext context, int pid, int sched_param$)
/*    */   {
/* 22 */     ThreadContext ctx = pid == 0 ? context : ThreadContext.getThreadCtx(pid);
/* 23 */     context.memory.setDoubleWord(sched_param$ + 0, ctx.getSchedulePriority());
/* 24 */     return 0;
/*    */   }
/*    */   
/*    */   public static int setParam(ThreadContext context, int pid, int sched_param$) {
/* 28 */     ThreadContext ctx = pid == 0 ? context : ThreadContext.getThreadCtx(pid);
/* 29 */     ctx.setSchedulePriority(getPriority(context, sched_param$));
/* 30 */     return 0;
/*    */   }
/*    */   
/*    */   private static int getPriority(ThreadContext context, int sched_param$) {
/* 34 */     return context.memory.getDoubleWord(sched_param$ + 0);
/*    */   }
/*    */   
/*    */   public static int getScheduler(ThreadContext context, int pid) {
/* 38 */     ThreadContext ctx = pid == 0 ? context : ThreadContext.getThreadCtx(pid);
/* 39 */     return ctx.getSchedulePolicy();
/*    */   }
/*    */   
/*    */   public static int setScheduler(ThreadContext context, int pid, int policy, int sched_param$) {
/* 43 */     ThreadContext ctx = pid == 0 ? context : ThreadContext.getThreadCtx(pid);
/* 44 */     ctx.setSchedulePolicy(policy);
/* 45 */     if (sched_param$ != 0) ctx.setSchedulePriority(getPriority(ctx, sched_param$));
/* 46 */     return 0;
/*    */   }
/*    */   
/*    */   public static class Policy
/*    */   {
/*    */     public static final int SCHED_OTHER = 0;
/*    */     public static final int SCHED_FIFO = 1;
/*    */     public static final int SCHED_RR = 2;
/*    */     public static final int SCHED_BATCH = 3;
/*    */     public static final int SCHED_IDLE = 5;
/*    */     public static final int SCHED_RESET_ON_FORK = 1073741824;
/*    */   }
/*    */ }


/* Location:              C:\Users\Joey\Downloads\codejar.jar!\com\emt\proteus\runtime32\syscall\Scheduling.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */