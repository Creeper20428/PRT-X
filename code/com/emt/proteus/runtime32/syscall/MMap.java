/*     */ package com.emt.proteus.runtime32.syscall;
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
/*     */ public class MMap
/*     */ {
/*     */   public static class Prot
/*     */   {
/*     */     public static final int PROT_READ = 1;
/*     */     public static final int PROT_WRITE = 2;
/*     */     public static final int PROT_EXEC = 4;
/*     */     public static final int PROT_NONE = 0;
/*     */     public static final int PROT_GROWSDOWN = 16777216;
/*     */     public static final int PROT_GROWSUP = 33554432;
/*  33 */     private static Map<Integer, String> _constants = Utils.createConstantMap(Prot.class);
/*     */     
/*     */     public static String toString(int value) {
/*  36 */       return Utils.toOrBitString(_constants, value);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public static class Flags
/*     */   {
/*     */     public static final int MAP_SHARED = 1;
/*     */     
/*     */ 
/*     */     public static final int MAP_PRIVATE = 2;
/*     */     
/*     */ 
/*     */     public static final int MAP_TYPE = 15;
/*     */     
/*     */ 
/*     */     public static final int MAP_FIXED = 16;
/*     */     
/*     */ 
/*     */     public static final int MAP_ANONYMOUS = 32;
/*     */     
/*     */ 
/*     */     public static final int MAP_ANON = 32;
/*     */     
/*     */ 
/*     */     public static final int MAP_GROWSDOWN = 256;
/*     */     
/*     */ 
/*     */     public static final int MAP_DENYWRITE = 2048;
/*     */     
/*     */ 
/*     */     public static final int MAP_EXECUTABLE = 4096;
/*     */     
/*     */ 
/*     */     public static final int MAP_LOCKED = 8192;
/*     */     
/*     */ 
/*     */     public static final int MAP_NORESERVE = 16384;
/*     */     
/*     */ 
/*     */     public static final int MAP_POPULATE = 32768;
/*     */     
/*     */ 
/*     */     public static final int MAP_NONBLOCK = 65536;
/*     */     
/*     */ 
/*     */     public static final int MAP_STACK = 131072;
/*     */     
/*     */ 
/*     */     public static final int MAP_HUGETLB = 262144;
/*     */     
/*     */ 
/*     */     public static final int MS_ASYNC = 1;
/*     */     
/*     */     public static final int MS_SYNC = 4;
/*     */     
/*     */     public static final int MS_INVALIDATE = 2;
/*     */     
/*     */     public static final int MCL_CURRENT = 1;
/*     */     
/*     */     public static final int MREMAP_MAYMOVE = 1;
/*     */     
/*     */     public static final int MREMAP_FIXED = 2;
/*     */     
/* 101 */     private static Map<Integer, String> _constants = Utils.createConstantMap(Flags.class);
/*     */     
/*     */     public static String toString(int value) {
/* 104 */       return Utils.toOrBitString(_constants, value);
/*     */     }
/*     */     
/*     */     public static boolean isAnonymous(int flags) {
/* 108 */       return (flags & 0x20) != 0;
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   public static class Madvise
/*     */   {
/*     */     public static final int MADV_NORMAL = 0;
/*     */     
/*     */     public static final int MADV_RANDOM = 1;
/*     */     
/*     */     public static final int MADV_SEQUENTIAL = 2;
/*     */     
/*     */     public static final int MADV_WILLNEED = 3;
/*     */     
/*     */     public static final int MADV_DONTNEED = 4;
/*     */     
/*     */     public static final int MADV_REMOVE = 9;
/*     */     
/*     */     public static final int MADV_DONTFORK = 10;
/*     */     
/*     */     public static final int MADV_DOFORK = 11;
/*     */     
/*     */     public static final int MADV_MERGEABLE = 12;
/*     */     
/*     */     public static final int MADV_UNMERGEABLE = 13;
/*     */     
/*     */     public static final int MADV_HUGEPAGE = 14;
/*     */     
/*     */     public static final int MADV_NOHUGEPAGE = 15;
/*     */     
/*     */     public static final int MADV_HWPOISON = 100;
/*     */     
/*     */     public static final int POSIX_MADV_NORMAL = 0;
/*     */     
/*     */     public static final int POSIX_MADV_RANDOM = 1;
/*     */     
/*     */     public static final int POSIX_MADV_SEQUENTIAL = 2;
/*     */     
/*     */     public static final int POSIX_MADV_WILLNEED = 3;
/*     */     
/*     */     public static final int POSIX_MADV_DONTNEED = 4;
/* 150 */     private static Map<Integer, String> _constants = Utils.createConstantMap(Madvise.class);
/*     */     
/*     */     public static String toString(int value) {
/* 153 */       return Utils.toOrBitString(_constants, value);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Joey\Downloads\codejar.jar!\com\emt\proteus\runtime32\syscall\MMap.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */