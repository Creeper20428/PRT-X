/*     */ package com.emt.proteus.runtime32.syscall;
/*     */ 
/*     */ import com.emt.proteus.runtime32.Option;
/*     */ import com.emt.proteus.runtime32.Option.Switch;
/*     */ import com.emt.proteus.runtime32.ThreadContext;
/*     */ import com.emt.proteus.runtime32.memory.MainMemory;
/*     */ import java.util.Collection;
/*     */ import java.util.HashMap;
/*     */ import java.util.LinkedList;
/*     */ import java.util.concurrent.Executors;
/*     */ import java.util.concurrent.ScheduledExecutorService;
/*     */ import java.util.concurrent.TimeUnit;
/*     */ import java.util.concurrent.atomic.AtomicInteger;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class Futex
/*     */ {
/*  23 */   private static final boolean FUTEX_TRACE = Option.futex.value();
/*     */   
/*     */   public static final int FUTEX_WAIT = 0;
/*     */   
/*     */   public static final int FUTEX_WAKE = 1;
/*     */   
/*     */   public static final int FUTEX_CMP_REQUEUE = 4;
/*     */   public static final int FUTEX_WAKE_OP = 5;
/*     */   public static final int FUTEX_LOCK_PI = 6;
/*     */   public static final int FUTEX_UNLOCK_PI = 7;
/*     */   public static final int FUTEX_TRYLOCK_PI = 8;
/*     */   public static final int FUTEX_WAIT_BITSET = 9;
/*     */   public static final int FUTEX_WAKE_BITSET = 10;
/*     */   public static final int FUTEX_WAIT_REQUEUE_PI = 11;
/*     */   public static final int FUTEX_CMP_REQUEUE_PI = 12;
/*     */   public static final int OP_MASK = 127;
/*     */   public static final int FUTEX_PRIVATE_FLAG = 128;
/*     */   public static final int FUTEX_CLOCK_REALTIME = 256;
/*  41 */   private static final ScheduledExecutorService _TIMEOUT_SERVICE = Executors.newScheduledThreadPool(1);
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
/*     */   public static int syscall(ThreadContext context, int $addr, int op, int val1, int $timeout, int $addr2, int val3)
/*     */   {
/*  59 */     boolean privateFutex = 0 != (op & 0x80);
/*  60 */     boolean realtime = 0 != (op & 0x100);
/*  61 */     MainMemory memory = context.memory;
/*     */     
/*  63 */     int opimpl = op & 0x7F;
/*     */     
/*  65 */     if ($addr == 21724) {}
/*     */     
/*     */     long timeout;
/*  68 */     switch (opimpl) {
/*     */     case 0: 
/*  70 */       timeout = toJavaMs(memory, $timeout);
/*  71 */       return wait(context, $addr, val1, timeout);
/*     */     case 1: 
/*  73 */       return wake(context, $addr, val1);
/*     */     case 4: 
/*     */       break;
/*     */     case 5: 
/*     */       break;
/*     */     case 6: 
/*     */       break;
/*     */     case 7: 
/*     */       break;
/*     */     case 8: 
/*     */       break;
/*     */     case 9: 
/*  85 */       timeout = toJavaMs(memory, $timeout);
/*  86 */       if (timeout != 0L) {
/*  87 */         timeout -= System.currentTimeMillis();
/*  88 */         return wait(context, $addr, val1, timeout);
/*     */       }
/*  90 */       return relativeTimeTest(memory, $addr, val1);
/*     */     case 10: 
/*     */     case 11: 
/*     */       break;
/*     */     case 12: 
/*     */       break;
/*     */     }
/*     */     
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 105 */     return Errors.syscall(38);
/*     */   }
/*     */   
/*     */   private static int relativeTimeTest(MainMemory memory, int $addr, int val1) {
/* 109 */     if (memory.readAtomicDoubleWord($addr) != val1) {
/* 110 */       return Errors.syscall(11);
/*     */     }
/*     */     
/* 113 */     return 0;
/*     */   }
/*     */   
/*     */   private static long toJavaMs(MainMemory memory, int $timeout)
/*     */   {
/* 118 */     if ($timeout == 0) {
/* 119 */       return forever();
/*     */     }
/*     */     
/* 122 */     int seconds = memory.getDoubleWord($timeout);
/* 123 */     int nanos = memory.getDoubleWord($timeout + 4);
/*     */     
/* 125 */     if ((seconds == 0) && (nanos == 0)) { return forever();
/*     */     }
/* 127 */     long minMillis = 1L;
/* 128 */     return Math.max(minMillis, seconds * 1000L + nanos / 1000000L);
/*     */   }
/*     */   
/*     */   private static long forever() {
/* 132 */     return 0L;
/*     */   }
/*     */   
/*     */   public static int wake(ThreadContext ctx, int addr$, int maxWakers) {
/* 136 */     return futexes.wake(ctx, addr$, maxWakers);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/* 141 */   public static int wait(ThreadContext ctx, int addr$, int value, long timeout) { return futexes.wait(ctx, addr$, value, timeout); }
/*     */   
/*     */   public static void wakeAll() {
/* 144 */     Futex[] futex_array = futexes.getFutexes();
/* 145 */     for (int i = 0; i < futex_array.length; i++) {
/* 146 */       Futex futex = futex_array[i];
/* 147 */       futex.wake(Integer.MAX_VALUE, -1);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/* 154 */   private static final FutexMap futexes = new FutexMap(null);
/*     */   
/*     */ 
/*     */   private final MainMemory memory;
/*     */   
/*     */   private final int address;
/*     */   
/*     */ 
/*     */   private static class FutexMap
/*     */   {
/* 164 */     private final HashMap<Integer, Futex> futexes = new HashMap();
/* 165 */     private final Object enqueueLock = new Object();
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */     private Futex getFutex(ThreadContext ctx, int address, boolean create)
/*     */     {
/* 172 */       synchronized (this.enqueueLock) {
/* 173 */         Futex futex = (Futex)this.futexes.get(Integer.valueOf(address));
/* 174 */         if ((futex == null) && (create)) {
/* 175 */           futex = new Futex(ctx.getMemory(), address);
/* 176 */           this.futexes.put(Integer.valueOf(address), futex);
/*     */         }
/* 178 */         return futex;
/*     */       }
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     public int wake(ThreadContext ctx, int addr$, int maxWakers)
/*     */     {
/* 192 */       int woken = -1;
/* 193 */       synchronized (this.enqueueLock) {
/* 194 */         Futex queue = getFutex(ctx, addr$, false);
/* 195 */         if (queue != null) {
/* 196 */           woken = queue.wake(maxWakers, -1);
/*     */         }
/* 198 */         if (Futex.FUTEX_TRACE) ctx.warn("(%08X) Wake  woken=%d max=%d queue=%s", new Object[] { Integer.valueOf(addr$), Integer.valueOf(woken), Integer.valueOf(maxWakers), queue });
/*     */       }
/* 200 */       return woken;
/*     */     }
/*     */     
/*     */     public int wait(ThreadContext ctx, int addr$, int value, long timeout) {
/* 204 */       Futex queue = null;
/* 205 */       Futex.Entry entry = null;
/* 206 */       int result = 0;
/*     */       
/* 208 */       synchronized (this.enqueueLock) {
/* 209 */         MainMemory memory = ctx.getMemory();
/*     */         
/* 211 */         if (memory.readAtomicDoubleWord(addr$) != value) {
/* 212 */           result = Errors.syscall(11);
/*     */         } else {
/* 214 */           queue = getFutex(ctx, addr$, true);
/* 215 */           entry = queue.enqueue(ctx, -1, value);
/*     */         }
/* 217 */         if (Futex.FUTEX_TRACE) { ctx.warn("(%08X) Start wait value=%d timeout=%d queue=%s", new Object[] { Integer.valueOf(addr$), Integer.valueOf(value), Long.valueOf(timeout), queue });
/*     */         }
/*     */       }
/* 220 */       if (entry != null) {
/* 221 */         if (timeout != 0L) Futex._TIMEOUT_SERVICE.schedule(entry, timeout, TimeUnit.MILLISECONDS);
/* 222 */         if (entry.sleep(timeout)) {
/* 223 */           result = 0;
/*     */         } else {
/* 225 */           result = Errors.syscall(110);
/*     */         }
/*     */       }
/* 228 */       if (Futex.FUTEX_TRACE) ctx.warn("(%08X) End Wait %d result=%d", new Object[] { Integer.valueOf(addr$), Integer.valueOf(value), Integer.valueOf(result) });
/* 229 */       return result;
/*     */     }
/*     */     
/*     */     public Futex[] getFutexes() {
/* 233 */       synchronized (this.enqueueLock) {
/* 234 */         Collection<Futex> values = this.futexes.values();
/* 235 */         return (Futex[])values.toArray(new Futex[values.size()]);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/* 242 */   private final LinkedList<Entry> entries = new LinkedList();
/*     */   
/*     */   public Futex(MainMemory memory, int address)
/*     */   {
/* 246 */     this.memory = memory;
/* 247 */     this.address = address;
/*     */   }
/*     */   
/*     */   public int wake(int amount, int mask) {
/* 251 */     int woken = 0;
/* 252 */     if (mask != -1) {
/* 253 */       throw new UnsupportedOperationException("Expected full bit mask: Not supported partial guards");
/*     */     }
/* 255 */     while (amount > 0) {
/*     */       Entry entry;
/* 257 */       synchronized (this.entries) {
/* 258 */         if (this.entries.isEmpty()) break;
/* 259 */         entry = (Entry)this.entries.removeFirst();
/*     */       }
/* 261 */       entry.wake();
/* 262 */       woken++;
/* 263 */       amount--;
/*     */     }
/* 265 */     return woken;
/*     */   }
/*     */   
/*     */   public Entry enqueue(ThreadContext ctx, int mask, int value) {
/* 269 */     synchronized (this.entries) {
/* 270 */       Entry entry = new Entry(this, ctx, mask, value);
/* 271 */       this.entries.add(entry);
/* 272 */       return entry;
/*     */     }
/*     */   }
/*     */   
/*     */   public String toString() {
/*     */     Entry[] entries1;
/* 278 */     synchronized (this.entries) {
/* 279 */       entries1 = (Entry[])this.entries.toArray(new Entry[this.entries.size()]);
/*     */     }
/* 281 */     StringBuilder b = new StringBuilder();
/* 282 */     b.append(Integer.toHexString(this.address)).append("={");
/*     */     
/* 284 */     for (int i = 0; i < entries1.length; i++) {
/* 285 */       Entry entry = entries1[i];
/* 286 */       if (i != 0) b.append(", ");
/* 287 */       b.append(entry._thread.getThreadId());
/*     */     }
/* 289 */     b.append("}");
/* 290 */     return b.toString();
/*     */   }
/*     */   
/*     */   private static class Entry
/*     */     implements Runnable
/*     */   {
/*     */     public static final int INIT = 0;
/*     */     public static final int SLEEPING = 1;
/*     */     public static final int WOKEN = 2;
/*     */     public static final int TIME_OUT = -1;
/* 300 */     private AtomicInteger state = new AtomicInteger();
/*     */     private final Futex owner;
/*     */     private ThreadContext _thread;
/*     */     private final int mask;
/*     */     private final int value;
/*     */     private Entry next;
/*     */     
/*     */     public Entry(Futex owner, ThreadContext ctx, int mask, int value) {
/* 308 */       this.owner = owner;
/* 309 */       this._thread = ctx;
/* 310 */       this.mask = mask;
/* 311 */       this.value = value;
/* 312 */       this.state.set(1);
/*     */     }
/*     */     
/*     */     public synchronized boolean sleep(long timeout) {
/* 316 */       int count = 0;
/* 317 */       while (this.state.get() == 1) {
/*     */         try {
/* 319 */           wait(10000L);
/*     */         } catch (InterruptedException ie) {
/* 321 */           ie.printStackTrace();
/*     */         }
/* 323 */         if ((Futex.FUTEX_TRACE) && (count != 0)) this._thread.warn("(%08X) Still waiting value=%d", new Object[] { Integer.valueOf(this.owner.address), Integer.valueOf(this._thread.getMemory().getDoubleWord(this.owner.address)) });
/* 324 */         count++;
/*     */       }
/* 326 */       return this.state.get() == 2;
/*     */     }
/*     */     
/*     */     public synchronized void wake() {
/* 330 */       this.state.compareAndSet(1, 2);
/* 331 */       notify();
/*     */     }
/*     */     
/*     */     public synchronized void timeout() {
/* 335 */       this.state.compareAndSet(1, -1);
/* 336 */       notify();
/*     */     }
/*     */     
/*     */     public void run()
/*     */     {
/* 341 */       timeout();
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Joey\Downloads\codejar.jar!\com\emt\proteus\runtime32\syscall\Futex.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */