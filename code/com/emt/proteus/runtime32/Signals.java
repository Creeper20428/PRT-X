/*     */ package com.emt.proteus.runtime32;
/*     */ 
/*     */ import com.emt.proteus.utils.Utils;
/*     */ import java.util.Map;
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
/*     */ public class Signals
/*     */ {
/*     */   public static final int SIGHUP = 1;
/*     */   public static final int SIGINT = 2;
/*     */   public static final int SIGQUIT = 3;
/*     */   public static final int SIGILL = 4;
/*     */   public static final int SIGTRAP = 5;
/*     */   public static final int SIGABRT = 6;
/*     */   public static final int SIGIOT = 6;
/*     */   public static final int SIGBUS = 7;
/*     */   public static final int SIGFPE = 8;
/*     */   public static final int SIGKILL = 9;
/*     */   public static final int SIGUSR1 = 10;
/*     */   public static final int SIGSEGV = 11;
/*     */   public static final int SIGUSR2 = 12;
/*     */   public static final int SIGPIPE = 13;
/*     */   public static final int SIGALRM = 14;
/*     */   public static final int SIGTERM = 15;
/*     */   public static final int SIGSTKFLT = 16;
/*     */   public static final int SIGCHLD = 17;
/*     */   public static final int SIGCLD = 17;
/*     */   public static final int SIGCONT = 18;
/*     */   public static final int SIGSTOP = 19;
/*     */   public static final int SIGTSTP = 20;
/*     */   public static final int SIGTTIN = 21;
/*     */   public static final int SIGTTOU = 22;
/*     */   public static final int SIGURG = 23;
/*     */   public static final int SIGXCPU = 24;
/*     */   public static final int SIGXFSZ = 25;
/*     */   public static final int SIGVTALRM = 26;
/*     */   public static final int SIGPROF = 27;
/*     */   public static final int SIGWINCH = 28;
/*     */   public static final int SIGIO = 29;
/*     */   public static final int SIGPOLL = 29;
/*     */   public static final int SIGPWR = 30;
/*     */   public static final int SIGSYS = 31;
/*     */   public static final int MAX_SIGNAL = 64;
/*  89 */   private static final Map<Integer, String> mapping = Utils.createConstantMap(Signals.class);
/*     */   private final Actions actions;
/*     */   private final SignalSet pending;
/*     */   
/*  93 */   public static boolean isValidSignal(int signal) { return (signal > 0) && (signal <= 64); }
/*     */   
/*     */ 
/*     */   private final SignalSet blocked;
/*     */   
/*     */   private final SignalSet ignored;
/*     */   
/*     */   public Signals()
/*     */   {
/* 102 */     this.actions = new Actions();
/* 103 */     this.pending = new SignalSet();
/* 104 */     this.blocked = new SignalSet();
/* 105 */     this.ignored = new SignalSet();
/*     */   }
/*     */   
/*     */   public Signals(Signals signals) {
/* 109 */     this.actions = signals.actions.copy();
/* 110 */     this.ignored = new SignalSet();
/* 111 */     this.pending = new SignalSet();
/* 112 */     this.blocked = new SignalSet(signals.blocked);
/*     */   }
/*     */   
/*     */   public Actions getActions() {
/* 116 */     return this.actions;
/*     */   }
/*     */   
/*     */   public SignalSet getPending() {
/* 120 */     return this.pending;
/*     */   }
/*     */   
/*     */   public SignalSet getBlocked() {
/* 124 */     return this.blocked;
/*     */   }
/*     */   
/*     */   public synchronized void send(int signal)
/*     */   {
/* 129 */     long masked = this.ignored.mask(toLong(signal));
/* 130 */     this.pending.add(masked);
/*     */   }
/*     */   
/*     */   public Action getHandler(int signal) {
/* 134 */     return this.actions.get(signal);
/*     */   }
/*     */   
/*     */   public void install(int signal, int function$, int flags, int restorer$, long mask)
/*     */   {
/* 139 */     this.actions.set(signal, new Action(function$, restorer$, flags, mask));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public int pending()
/*     */   {
/*     */     long l;
/*     */     
/* 148 */     synchronized (this) {
/* 149 */       long mask = this.blocked.mask(this.pending);
/* 150 */       l = Long.lowestOneBit(mask);
/* 151 */       if (l == 0L) {
/* 152 */         return 0;
/*     */       }
/* 154 */       this.pending.remove(l);
/*     */     }
/*     */     
/* 157 */     return Long.numberOfTrailingZeros(l) + 1;
/*     */   }
/*     */   
/*     */   public Action getAction(int signal) {
/* 161 */     return this.actions.get(signal);
/*     */   }
/*     */   
/*     */   public static final class SignalSet {
/*     */     private long bits;
/*     */     
/*     */     public SignalSet() {
/* 168 */       this.bits = 0L;
/*     */     }
/*     */     
/*     */     public SignalSet(SignalSet original) {
/* 172 */       this.bits = original.bits;
/*     */     }
/*     */     
/*     */     public void add(long mask) {
/* 176 */       this.bits |= mask;
/*     */     }
/*     */     
/*     */     public void remove(long mask) {
/* 180 */       this.bits &= (mask ^ 0xFFFFFFFFFFFFFFFF);
/*     */     }
/*     */     
/*     */     public long get() {
/* 184 */       return this.bits;
/*     */     }
/*     */     
/*     */     public void assign(long newValue) {
/* 188 */       this.bits = newValue;
/*     */     }
/*     */     
/*     */     public void set(int signal) {
/* 192 */       this.bits |= 1L << Signals.toSignalIndex(signal);
/*     */     }
/*     */     
/*     */     public void clear(int signal) {
/* 196 */       this.bits &= (Signals.toLong(signal) ^ 0xFFFFFFFFFFFFFFFF);
/*     */     }
/*     */     
/*     */     public long mask(SignalSet other) {
/* 200 */       return (this.bits ^ 0xFFFFFFFFFFFFFFFF) & other.bits;
/*     */     }
/*     */     
/*     */     public long mask(long bits) {
/* 204 */       return (this.bits ^ 0xFFFFFFFFFFFFFFFF) & bits;
/*     */     }
/*     */   }
/*     */   
/*     */   public static final class Actions
/*     */   {
/* 210 */     private final Signals.Action[] actions = new Signals.Action[64];
/*     */     
/*     */     public Signals.Action get(int signal) {
/* 213 */       int index = Signals.toSignalIndex(signal);
/* 214 */       return this.actions[index];
/*     */     }
/*     */     
/*     */     public Actions copy() {
/* 218 */       Actions copy = new Actions();
/* 219 */       System.arraycopy(this.actions, 0, copy.actions, 0, this.actions.length);
/* 220 */       return copy;
/*     */     }
/*     */     
/*     */     public void set(int signal, Signals.Action action) {
/* 224 */       this.actions[signal] = action;
/*     */     }
/*     */   }
/*     */   
/*     */   private static int toSignalIndex(int signal) {
/* 229 */     return signal - 1;
/*     */   }
/*     */   
/*     */   private static long toLong(int signal) {
/* 233 */     return 1L << toSignalIndex(signal);
/*     */   }
/*     */   
/*     */   public static final class Action
/*     */   {
/* 238 */     public static final Action NULL = new Action(0, 0, 0, 0L);
/*     */     
/*     */ 
/*     */     public static final int SA_NOCLDSTOP = 1;
/*     */     
/*     */     public static final int SA_NOCLDWAIT = 2;
/*     */     
/*     */     public static final int SA_SIGINFO = 4;
/*     */     
/*     */     public static final int SA_ONSTACK = 134217728;
/*     */     
/*     */     public static final int SA_RESTART = 268435456;
/*     */     
/*     */     public static final int SA_NODEFER = 1073741824;
/*     */     
/*     */     public static final int SA_RESETHAND = Integer.MIN_VALUE;
/*     */     
/*     */     public static final int SA_INTERRUPT = 536870912;
/*     */     
/*     */     private final int function$;
/*     */     
/*     */     private final int restorer$;
/*     */     
/*     */     private final long mask;
/*     */     
/*     */     private final int flags;
/*     */     
/*     */ 
/*     */     public Action(int function$, int restorer$, int flags, long mask)
/*     */     {
/* 268 */       this.function$ = function$;
/* 269 */       this.restorer$ = restorer$;
/* 270 */       this.mask = mask;
/* 271 */       this.flags = flags;
/*     */     }
/*     */     
/*     */     public void invoke(ThreadContext ctx, int signal, int siginfo_t$, int _void$) {
/* 275 */       Signals signals = ctx.getSignals();
/* 276 */       signals.blocked.add(this.mask);
/*     */       try {
/* 278 */         handle(ctx, signal, siginfo_t$, _void$);
/*     */       } finally {
/* 280 */         signals.blocked.remove(this.mask);
/*     */       }
/*     */     }
/*     */     
/*     */     public void handle(ThreadContext ctx, int signal, int siginfo_t$, int _void$) {
/* 285 */       if (useSigInfo(this.flags)) {}
/*     */       
/*     */ 
/*     */ 
/*     */ 
/* 290 */       ctx.call(this.function$, "SIGNAL " + signal);
/*     */     }
/*     */     
/*     */     public int getFunction$() {
/* 294 */       return this.function$;
/*     */     }
/*     */     
/*     */     public int getRestorer$() {
/* 298 */       return this.restorer$;
/*     */     }
/*     */     
/*     */     public long getMask() {
/* 302 */       return this.mask;
/*     */     }
/*     */     
/*     */     public int getFlags() {
/* 306 */       return this.flags;
/*     */     }
/*     */     
/*     */     public static boolean useSigInfo(int flags) {
/* 310 */       return (flags & 0x4) != 0;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Joey\Downloads\codejar.jar!\com\emt\proteus\runtime32\Signals.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */