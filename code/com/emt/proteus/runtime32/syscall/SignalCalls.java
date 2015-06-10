/*     */ package com.emt.proteus.runtime32.syscall;
/*     */ 
/*     */ import com.emt.proteus.runtime32.DumpException;
/*     */ import com.emt.proteus.runtime32.Signals;
/*     */ import com.emt.proteus.runtime32.Signals.Action;
/*     */ import com.emt.proteus.runtime32.Signals.SignalSet;
/*     */ import com.emt.proteus.runtime32.TerminateException;
/*     */ import com.emt.proteus.runtime32.ThreadContext;
/*     */ import com.emt.proteus.runtime32.memory.MainMemory;
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
/*     */ public class SignalCalls
/*     */ {
/*     */   public static final int UNIMPLEMENTED = -4096;
/*     */   public static final int SIG_BLOCK = 0;
/*     */   public static final int SIG_UNBLOCK = 1;
/*     */   public static final int SIG_SETMASK = 2;
/*     */   
/*     */   public static int sigProcMask(ThreadContext ctx, int how, int set$, int oset$)
/*     */   {
/*  34 */     Signals signals = ctx.getSignals();
/*     */     
/*  36 */     Signals.SignalSet blocked = signals.getBlocked();
/*  37 */     long extra = ctx.getMemory().getQuadWord(set$);
/*  38 */     if (oset$ != 0) {
/*  39 */       ctx.getMemory().setQuadWord(oset$, blocked.get());
/*     */     }
/*  41 */     switch (how) {
/*     */     case 0: 
/*  43 */       blocked.add(extra);
/*  44 */       break;
/*     */     case 1: 
/*  46 */       blocked.remove(extra);
/*  47 */       break;
/*     */     case 2: 
/*  49 */       blocked.assign(extra);
/*  50 */       break;
/*  51 */     default:  return Errors.syscall(22);
/*     */     }
/*  53 */     return 0;
/*     */   }
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
/*     */   public static int sigAction(ThreadContext ctx, int signal, int newAction$, int oldAction$, int size_t)
/*     */   {
/*  67 */     if (!Signals.isValidSignal(signal)) return Errors.syscall(22);
/*  68 */     Signals signals = ctx.getSignals();
/*  69 */     MainMemory memory = ctx.getMemory();
/*  70 */     if (oldAction$ != 0) {
/*  71 */       Signals.Action action = signals.getHandler(signal);
/*  72 */       if (action == null) {
/*  73 */         action = Signals.Action.NULL;
/*     */       }
/*  75 */       memory.setDoubleWord(newAction$, action.getFunction$());
/*  76 */       memory.setDoubleWord(newAction$ + 4, action.getFlags());
/*  77 */       memory.setDoubleWord(newAction$ + 8, action.getRestorer$());
/*  78 */       memory.setQuadWord(newAction$ + 12, action.getMask());
/*     */     }
/*  80 */     if (newAction$ != 0) {
/*  81 */       int function$ = memory.getDoubleWord(newAction$);
/*  82 */       int flags = memory.getDoubleWord(newAction$ + 4);
/*  83 */       int restorer = memory.getDoubleWord(newAction$ + 8);
/*  84 */       long mask = memory.getQuadWord(newAction$ + 12);
/*  85 */       signals.install(signal, function$, restorer, flags, mask);
/*     */     }
/*  87 */     return 0;
/*     */   }
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
/*     */   public static int tgKill(ThreadContext ctx, int tig, int tid, int signal)
/*     */   {
/* 102 */     ThreadContext threadCtx = ThreadContext.getThreadCtx(tid);
/*     */     
/* 104 */     if (threadCtx == null) return Errors.syscall(3);
/* 105 */     if (!Signals.isValidSignal(signal)) return Errors.syscall(22);
/* 106 */     Signals signals = threadCtx.getSignals();
/* 107 */     signals.send(signal);
/* 108 */     return 0;
/*     */   }
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
/*     */   public static int tKill(ThreadContext ctx, int tig, int tid, int signal)
/*     */   {
/* 124 */     return tgKill(ctx, -1, tid, signal);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public static void pendingSignals(ThreadContext ctx)
/*     */   {
/* 131 */     Signals signals = ctx.getSignals();
/*     */     int signal;
/* 133 */     while ((signal = signals.pending()) != 0) {
/* 134 */       handleSignal(ctx, signals, signal);
/*     */     }
/*     */   }
/*     */   
/*     */   private static void handleSignal(ThreadContext ctx, Signals signals, int signal) {
/* 139 */     Signals.Action action = signals.getAction(signal);
/* 140 */     if (action == null) {
/* 141 */       doDefaultAction(ctx, signal);
/*     */     }
/*     */     else {
/* 144 */       action.invoke(ctx, signal, 0, 0);
/*     */     }
/*     */   }
/*     */   
/*     */   private static void doDefaultAction(ThreadContext ctx, int signal) {
/* 149 */     String s = ctx.getThreadId();
/* 150 */     switch (signal)
/*     */     {
/*     */     case 1: 
/* 153 */       throw new TerminateException();
/*     */     
/*     */     case 2: 
/* 156 */       throw new TerminateException();
/*     */     case 3: 
/* 158 */       throw new DumpException(s);
/*     */     
/*     */     case 4: 
/* 161 */       throw new DumpException(s);
/*     */     
/*     */     case 5: 
/* 164 */       throw new DumpException(s);
/*     */     
/*     */ 
/*     */     case 6: 
/* 168 */       throw new DumpException(s);
/*     */     case 7: 
/* 170 */       throw new DumpException(s);
/*     */     case 8: 
/* 172 */       throw new DumpException(s);
/*     */     case 9: 
/* 174 */       throw new TerminateException();
/*     */     case 10: 
/* 176 */       throw new TerminateException();
/*     */     case 11: 
/* 178 */       throw new DumpException(s);
/*     */     case 12: 
/* 180 */       throw new TerminateException();
/*     */     case 13: 
/* 182 */       throw new TerminateException();
/*     */     case 14: 
/* 184 */       throw new TerminateException();
/*     */     case 15: 
/* 186 */       throw new TerminateException();
/*     */     case 16: 
/* 188 */       throw new TerminateException();
/*     */     
/*     */     case 17: 
/*     */       break;
/*     */     
/*     */     case 18: 
/* 194 */       throw new UnsupportedOperationException("CONTINUE");
/*     */     
/*     */     case 19: 
/* 197 */       throw new UnsupportedOperationException("STOP");
/*     */     
/*     */     case 20: 
/* 200 */       throw new UnsupportedOperationException("STOP");
/*     */     
/*     */     case 21: 
/* 203 */       throw new UnsupportedOperationException("STOP");
/*     */     case 22: 
/* 205 */       throw new UnsupportedOperationException("STOP");
/*     */     case 23: 
/*     */       break;
/*     */     
/*     */     case 24: 
/* 210 */       throw new DumpException(s);
/*     */     case 25: 
/* 212 */       throw new DumpException(s);
/*     */     case 26: 
/* 214 */       throw new TerminateException();
/*     */     case 27: 
/* 216 */       throw new TerminateException();
/*     */     case 28: 
/*     */       break;
/*     */     
/*     */     case 29: 
/* 221 */       throw new TerminateException();
/*     */     case 30: 
/* 223 */       throw new TerminateException();
/*     */     case 31: 
/* 225 */       throw new DumpException(s);
/*     */     default: 
/* 227 */       throw new UnsupportedOperationException("Signal:" + signal);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Joey\Downloads\codejar.jar!\com\emt\proteus\runtime32\syscall\SignalCalls.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */