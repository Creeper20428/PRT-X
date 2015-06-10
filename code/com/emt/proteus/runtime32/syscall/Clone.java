/*     */ package com.emt.proteus.runtime32.syscall;
/*     */ 
/*     */ import com.emt.proteus.runtime32.ThreadContext;
/*     */ import com.emt.proteus.runtime32.memory.MainMemory;
/*     */ import com.emt.proteus.utils.ILog;
/*     */ import com.emt.proteus.utils.Utils;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
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
/*     */ public class Clone
/*     */ {
/*     */   public static final int CSIGNAL = 255;
/*     */   public static final int CLONE_VM = 256;
/*     */   public static final int CLONE_FS = 512;
/*     */   public static final int CLONE_FILES = 1024;
/*     */   public static final int CLONE_SIGHAND = 2048;
/*     */   public static final int CLONE_PTRACE = 8192;
/*     */   public static final int CLONE_VFORK = 16384;
/*     */   public static final int CLONE_PARENT = 32768;
/*     */   public static final int CLONE_THREAD = 65536;
/*     */   public static final int CLONE_NEWNS = 131072;
/*     */   public static final int CLONE_SYSVSEM = 262144;
/*     */   public static final int CLONE_SETTLS = 524288;
/*     */   public static final int CLONE_PARENT_SETTID = 1048576;
/*     */   public static final int CLONE_CHILD_CLEARTID = 2097152;
/*     */   public static final int CLONE_DETACHED = 4194304;
/*     */   public static final int CLONE_UNTRACED = 8388608;
/*     */   public static final int CLONE_CHILD_SETTID = 16777216;
/*     */   public static final int CLONE_NEWUTS = 67108864;
/*     */   public static final int CLONE_NEWIPC = 134217728;
/*     */   public static final int CLONE_NEWUSER = 268435456;
/*     */   public static final int CLONE_NEWPID = 536870912;
/*     */   public static final int CLONE_NEWNET = 1073741824;
/*     */   public static final int CLONE_IO = Integer.MIN_VALUE;
/*     */   private static final int SUPPORTED_FLAGS = 4001791;
/*  75 */   private static final Map<Integer, String> FLAGS_MAP = Utils.createConstantMap(Clone.class);
/*  76 */   private static final Integer[] FLAGS = (Integer[])FLAGS_MAP.keySet().toArray(new Integer[FLAGS_MAP.size()]);
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
/*     */   public static int clone(ThreadContext ctx, int flags, int child_stack$, int ptid$, int tlspid$, int ctid$)
/*     */   {
/*  98 */     flags = checkFlags(ctx, flags);
/*  99 */     ThreadContext child = new ThreadContext(ctx, flags);
/* 100 */     int tid = child.getTid();
/*     */     
/*     */ 
/* 103 */     child.esp(child_stack$);
/*     */     
/* 105 */     if (isChildClearTID(flags)) child.setClearChildTid(ctid$);
/* 106 */     if (isChildSetTID(flags)) child.setSetChildTid(ctid$);
/* 107 */     if (test(flags, 1048576)) ctx.getMemory().setDoubleWord(ptid$, tid);
/* 108 */     if (test(flags, 524288)) {
/* 109 */       int threadPointer = ctx.getMemory().getDoubleWord(tlspid$ + 4);
/* 110 */       ctx.log.debug("THREAD", "Setting TLS %08X", new Object[] { Integer.valueOf(threadPointer) });
/* 111 */       child.setThreadPointer(threadPointer);
/*     */     }
/*     */     
/*     */ 
/* 115 */     child.pop();
/* 116 */     child.pop();
/* 117 */     int function = child.pop();
/* 118 */     child.spawn(function);
/* 119 */     if (isChildSetTID(flags)) {
/* 120 */       child.setChildTid(tid);
/*     */     }
/* 122 */     child.start();
/* 123 */     return tid;
/*     */   }
/*     */   
/*     */   private static int checkFlags(ThreadContext ctx, int flags) {
/* 127 */     int supported = flags & 0x3D0FFF;
/* 128 */     if (supported != flags) {
/* 129 */       ctx.log.warn("CLONE", "Unsupported flags %s ", new Object[] { toFlagString(flags) });
/* 130 */       ctx.log.warn("CLONE", "Supported flags =%s ", new Object[] { toFlagString(4001791) });
/*     */     }
/*     */     else {
/* 133 */       ctx.log.info("CLONE", "Spawning [%s] ", new Object[] { toFlagString(flags) });
/*     */     }
/* 135 */     return supported;
/*     */   }
/*     */   
/*     */   public static int getSignal(int flags) {
/* 139 */     return flags & 0xFF;
/*     */   }
/*     */   
/*     */   public static String toFlagString(int flags) {
/* 143 */     StringBuilder b = new StringBuilder();
/* 144 */     b.append(String.format("[%08X]=", new Object[] { Integer.valueOf(flags) }));
/* 145 */     for (int i = 0; i < FLAGS.length; i++) {
/* 146 */       Integer flag = FLAGS[i];
/* 147 */       int set = flags & flag.intValue();
/* 148 */       if (set != 0) {
/* 149 */         b.append((String)FLAGS_MAP.get(flag));
/* 150 */         int supported = flag.intValue() & 0x3D0FFF;
/* 151 */         if (supported == 0) {
/* 152 */           b.append("** ");
/*     */         } else {
/* 154 */           b.append(" ");
/*     */         }
/*     */       }
/*     */     }
/* 158 */     return b.toString();
/*     */   }
/*     */   
/*     */   public static boolean isChildClearTID(int flags)
/*     */   {
/* 163 */     return (0x200000 & flags) != 0;
/*     */   }
/*     */   
/* 166 */   public static boolean isChildSetTID(int flags) { return (0x1000000 & flags) != 0; }
/*     */   
/*     */   public static boolean test(int flags, int test) {
/* 169 */     return (test & flags) != 0;
/*     */   }
/*     */ }


/* Location:              C:\Users\Joey\Downloads\codejar.jar!\com\emt\proteus\runtime32\syscall\Clone.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */