/*     */ package com.emt.proteus.runtime32.syscall;
/*     */ 
/*     */ import com.emt.proteus.runtime32.Processor;
/*     */ import com.emt.proteus.runtime32.Segment;
/*     */ import com.emt.proteus.runtime32.SystemClock;
/*     */ import com.emt.proteus.runtime32.SystemConstants;
/*     */ import com.emt.proteus.runtime32.ThreadContext;
/*     */ import com.emt.proteus.runtime32.io.IoLib;
/*     */ import com.emt.proteus.runtime32.memory.MainMemory;
/*     */ import java.lang.reflect.Field;
/*     */ import java.util.HashMap;
/*     */ 
/*     */ public class SystemCalls
/*     */ {
/*  15 */   public static boolean strace = com.emt.proteus.runtime32.Option.strace.value();
/*     */   
/*     */   public static final int RESTART = 0;
/*     */   
/*     */   public static final int EXIT = 1;
/*     */   
/*     */   public static final int FORK = 2;
/*     */   
/*     */   public static final int READ = 3;
/*     */   
/*     */   public static final int WRITE = 4;
/*     */   
/*     */   public static final int OPEN = 5;
/*     */   
/*     */   public static final int CLOSE = 6;
/*     */   
/*     */   public static final int WAITPID = 7;
/*     */   
/*     */   public static final int CREATE = 8;
/*     */   
/*     */   public static final int LINK = 9;
/*     */   
/*     */   public static final int UNLINK = 10;
/*     */   
/*     */   public static final int EXECVE = 11;
/*     */   
/*     */   public static final int CHDIR = 12;
/*     */   
/*     */   public static final int TIME = 13;
/*     */   
/*     */   public static final int MKNOD = 14;
/*     */   
/*     */   public static final int CHMOD = 15;
/*     */   
/*     */   public static final int LCHOWN = 16;
/*     */   
/*     */   public static final int BREAK = 17;
/*     */   
/*     */   public static final int OLDSTAT = 18;
/*     */   
/*     */   public static final int LSEEK = 19;
/*     */   
/*     */   public static final int GETPID = 20;
/*     */   
/*     */   public static final int MOUNT = 21;
/*     */   
/*     */   public static final int UMOUNT = 22;
/*     */   
/*     */   public static final int SETUID = 23;
/*     */   
/*     */   public static final int GETUID = 24;
/*     */   public static final int STIME = 25;
/*     */   public static final int PTRACE = 26;
/*     */   public static final int ALARM = 27;
/*     */   public static final int OLDFSTAT = 28;
/*     */   public static final int PAUSE = 29;
/*     */   public static final int UTIME = 30;
/*     */   public static final int STTY = 31;
/*     */   public static final int GTTY = 32;
/*     */   public static final int ACCESS = 33;
/*     */   public static final int NICE = 34;
/*     */   public static final int FTIME = 35;
/*     */   public static final int SYNC = 36;
/*     */   public static final int KILL = 37;
/*     */   public static final int RENAME = 38;
/*     */   public static final int MKDIR = 39;
/*     */   public static final int RMDIR = 40;
/*     */   public static final int DUP = 41;
/*     */   public static final int PIPE = 42;
/*     */   public static final int TIMES = 43;
/*     */   public static final int PROF = 44;
/*     */   public static final int BRK = 45;
/*     */   public static final int SETGID = 46;
/*     */   public static final int GETGID = 47;
/*     */   public static final int SIGNAL = 48;
/*     */   public static final int GETEUID = 49;
/*     */   public static final int SETEUID = 50;
/*     */   public static final int ACCT = 51;
/*     */   public static final int UMOUNT2 = 52;
/*     */   public static final int LOCK = 53;
/*     */   public static final int IOCTL = 54;
/*     */   public static final int FCNTL = 55;
/*     */   public static final int MPX = 56;
/*     */   public static final int SETPGID = 57;
/*     */   public static final int ULIMIT = 58;
/*     */   public static final int OLDOLDUNAME = 59;
/*     */   public static final int UMASK = 60;
/*     */   public static final int CHROOT = 61;
/*     */   public static final int USTAT = 62;
/*     */   public static final int DUP2 = 63;
/*     */   public static final int GETPPID = 64;
/*     */   public static final int GETPGRP = 65;
/*     */   public static final int SETSID = 66;
/*     */   public static final int SIGACTION = 67;
/*     */   public static final int SGETMASK = 68;
/*     */   public static final int SSETMASK = 69;
/*     */   public static final int SETREUID = 70;
/*     */   public static final int SETREGID = 71;
/*     */   public static final int SIGSUSPEND = 72;
/*     */   public static final int SIGPENDING = 73;
/*     */   public static final int SETHOSTNAME = 74;
/*     */   public static final int SETRLIMIT = 75;
/*     */   public static final int GETRLIMIT = 76;
/*     */   public static final int GETRUSAGE = 77;
/*     */   public static final int GETTIMEOFDAY = 78;
/*     */   public static final int SETTIMEOFDAY = 79;
/*     */   public static final int GETGROUPS = 80;
/*     */   public static final int SETGROUPS = 81;
/*     */   public static final int SELECT = 82;
/*     */   public static final int SYMLINK = 83;
/*     */   public static final int OLDLSTAT = 84;
/*     */   public static final int READLINK = 85;
/*     */   public static final int USELIB = 86;
/*     */   public static final int SWAPON = 87;
/*     */   public static final int REBOOT = 88;
/*     */   public static final int READDIR = 89;
/*     */   public static final int MMAP = 90;
/*     */   public static final int MUNMAP = 91;
/*     */   public static final int TRUNCATE = 92;
/*     */   public static final int FTRUNCATE = 93;
/*     */   public static final int FCHMOD = 94;
/*     */   public static final int FCHOWN = 95;
/*     */   public static final int GETPRIORITY = 96;
/*     */   public static final int SETPRIORITY = 97;
/*     */   public static final int PROFILE = 98;
/*     */   public static final int STATFS = 99;
/*     */   public static final int FSTATFS = 100;
/*     */   public static final int IOPERM = 101;
/*     */   public static final int SOCKETCALL = 102;
/*     */   public static final int SYSLOG = 103;
/*     */   public static final int SETITIMER = 104;
/*     */   public static final int GETITIMER = 105;
/*     */   public static final int STAT = 106;
/*     */   public static final int LSTAT = 107;
/*     */   public static final int FSTAT = 108;
/*     */   public static final int OLDUNAME = 109;
/*     */   public static final int IOPL = 110;
/*     */   public static final int VHANGUP = 111;
/*     */   public static final int IDLE = 112;
/*     */   public static final int VM86OLD = 113;
/*     */   public static final int WAIT4 = 114;
/*     */   public static final int SWAPOFF = 115;
/*     */   public static final int SYSINFO = 116;
/*     */   public static final int IPC = 117;
/*     */   public static final int FSYNC = 118;
/*     */   public static final int SIGRETURN = 119;
/*     */   public static final int CLONE = 120;
/*     */   public static final int SETDOMAINNAME = 121;
/*     */   public static final int UNAME = 122;
/*     */   public static final int MODIFY_LDT = 123;
/*     */   public static final int ADJTIMEX = 124;
/*     */   public static final int MPROTECT = 125;
/*     */   public static final int SIGPROCMASK = 126;
/*     */   public static final int CREATE_MODULE = 127;
/*     */   public static final int INIT_MODULE = 128;
/*     */   public static final int DELETE_MODULE = 129;
/*     */   public static final int GET_KERNEL_SYMS = 130;
/*     */   public static final int QUOTACTL = 131;
/*     */   public static final int GETPGID = 132;
/*     */   public static final int FCHDIR = 133;
/*     */   public static final int BDFLUSH = 134;
/*     */   public static final int SYSFS = 135;
/*     */   public static final int PERSONALITY = 136;
/*     */   public static final int AFS_SYSCALL = 137;
/*     */   public static final int SETFSUID = 138;
/*     */   public static final int SETFSGID = 139;
/*     */   public static final int LLSEEK = 140;
/*     */   public static final int GETDENTS = 141;
/*     */   public static final int NEWSELECT = 142;
/*     */   public static final int FLOCK = 143;
/*     */   public static final int MSYNC = 144;
/*     */   public static final int READV = 145;
/*     */   public static final int WRITEV = 146;
/*     */   public static final int GETSID = 147;
/*     */   public static final int FDATASYNC = 148;
/*     */   public static final int SYSCTL = 149;
/*     */   public static final int MLOCK = 150;
/*     */   public static final int MUNLOCK = 151;
/*     */   public static final int MLOCKALL = 152;
/*     */   public static final int MUNLOCKALL = 153;
/*     */   public static final int SCHED_SETPARAM = 154;
/*     */   public static final int SCHED_GETPARAM = 155;
/*     */   public static final int SCHED_SETSCHEDULER = 156;
/*     */   public static final int SCHED_GETSCHEDULER = 157;
/*     */   public static final int SCHED_YIELD = 158;
/*     */   public static final int SCHED_GET_PRIORITY_MAX = 159;
/*     */   public static final int SCHED_GET_PRIORITY_MIN = 160;
/*     */   public static final int SCHED_RR_GET_INTERVAL = 161;
/*     */   public static final int NANOSLEEP = 162;
/*     */   public static final int MREMAP = 163;
/*     */   public static final int SETRESUID = 164;
/*     */   public static final int GETRESUID = 165;
/*     */   public static final int VM86 = 166;
/*     */   public static final int QUERY_MODULE = 167;
/*     */   public static final int POLL = 168;
/*     */   public static final int NFSSERVCTL = 169;
/*     */   public static final int SETRESGID = 170;
/*     */   public static final int GETRESGID = 171;
/*     */   public static final int PRCTL = 172;
/*     */   public static final int RT_SIGRETURN = 173;
/*     */   public static final int RT_SIGACTION = 174;
/*     */   public static final int RT_SIGPROCMASK = 175;
/*     */   public static final int RT_SIGPENDING = 176;
/*     */   public static final int RT_SIGTIMEDWAIT = 177;
/*     */   public static final int RT_SIQQUEUEINFO = 178;
/*     */   public static final int RT_SIGSUSPEND = 179;
/*     */   public static final int PREAD64 = 180;
/*     */   public static final int PWRITE64 = 181;
/*     */   public static final int CHOWN = 182;
/*     */   public static final int GETCWD = 183;
/*     */   public static final int CAPGET = 184;
/*     */   public static final int CAPSET = 185;
/*     */   public static final int SIGALTSTACK = 186;
/*     */   public static final int SENDFILE = 187;
/*     */   public static final int GETPMSG = 188;
/*     */   public static final int PUTPMSG = 189;
/*     */   public static final int VFORK = 190;
/*     */   public static final int UGETRLIMIT = 191;
/*     */   public static final int MMAP2 = 192;
/*     */   public static final int TRUNCATE64 = 193;
/*     */   public static final int FTRUNCATE64 = 194;
/*     */   public static final int STAT64 = 195;
/*     */   public static final int LSTAT64 = 196;
/*     */   public static final int FSTAT64 = 197;
/*     */   public static final int LCHOWN32 = 198;
/*     */   public static final int GETUID32 = 199;
/*     */   public static final int GETGID32 = 200;
/*     */   public static final int GETEUID32 = 201;
/*     */   public static final int GETEGID32 = 202;
/*     */   public static final int SETREUID32 = 203;
/*     */   public static final int SETREGID32 = 204;
/*     */   public static final int GETGROUPS32 = 205;
/*     */   public static final int SETGROUPS32 = 206;
/*     */   public static final int FCHOWN32 = 207;
/*     */   public static final int SETRESUID32 = 208;
/*     */   public static final int GETRESUID32 = 209;
/*     */   public static final int SETRESGID32 = 210;
/*     */   public static final int GETRESGID32 = 211;
/*     */   public static final int CHOWN32 = 212;
/*     */   public static final int SETUID32 = 213;
/*     */   public static final int SETGID32 = 214;
/*     */   public static final int SETFSUID32 = 215;
/*     */   public static final int SETFSGID32 = 216;
/*     */   public static final int PIVOT_ROOT = 217;
/*     */   public static final int MINCORE = 218;
/*     */   public static final int MADVISE = 219;
/*     */   public static final int MADVISE1 = 220;
/*     */   public static final int GETDENTS64 = 221;
/*     */   public static final int FCNTL64 = 222;
/*     */   public static final int READAHEAD = 225;
/*     */   public static final int SETXATTR = 226;
/*     */   public static final int LSETXATTR = 227;
/*     */   public static final int FSETXATTR = 228;
/*     */   public static final int GETXATTR = 229;
/*     */   public static final int LGETXATTR = 230;
/*     */   public static final int FGETXATTR = 231;
/*     */   public static final int LISTXATTR = 232;
/*     */   public static final int LLISTXATTR = 233;
/*     */   public static final int FLISTXATTR = 234;
/*     */   public static final int REMOVEXATTR = 235;
/*     */   public static final int LREMOVEXATTR = 236;
/*     */   public static final int FREMOVEXATTR = 237;
/*     */   public static final int TKILL = 238;
/*     */   public static final int SENDFILE64 = 239;
/*     */   public static final int FUTEX = 240;
/*     */   public static final int SCHED_SETAFFINITY = 241;
/*     */   public static final int SCHED_GETAFFINITY = 242;
/*     */   public static final int SET_THREAD_AREA = 243;
/*     */   public static final int GET_THREAD_AREA = 244;
/*     */   public static final int IO_SETUP = 245;
/*     */   public static final int IO_DESTROY = 246;
/*     */   public static final int IO_GETEVENTS = 247;
/*     */   public static final int IO_SUBMIT = 248;
/*     */   public static final int IO_CANCEL = 249;
/*     */   public static final int FADVISE64 = 250;
/*     */   public static final int EXIT_GROUP = 252;
/*     */   public static final int LOOKUP_DCOOKIE = 253;
/*     */   public static final int EPOLL_CREATE = 254;
/*     */   public static final int EPOLL_CTL = 255;
/*     */   public static final int EPOLL_WAIT = 256;
/*     */   public static final int REMAP_FILE_PAGES = 257;
/*     */   public static final int SET_TID_ADDRESS = 258;
/*     */   public static final int TIMER_CREATE = 259;
/*     */   public static final int TIMER_SETTIME = 260;
/*     */   public static final int TIMER_GETTIME = 261;
/*     */   public static final int TIMER_GETOVERRUN = 262;
/*     */   public static final int TIMER_DELETE = 263;
/*     */   public static final int CLOCK_SETTIME = 264;
/*     */   public static final int CLOCK_GETTIME = 265;
/*     */   public static final int CLOCK_GETRES = 266;
/*     */   public static final int CLOCK_NANOSLEEP = 267;
/*     */   public static final int STATFS64 = 268;
/*     */   public static final int FSTATFS64 = 269;
/*     */   public static final int TGKILL = 270;
/*     */   public static final int UTIMES = 271;
/*     */   public static final int FADVISE64_64 = 272;
/*     */   public static final int VSERVER = 273;
/*     */   public static final int MBIND = 274;
/*     */   public static final int GET_MEMPOLICY = 275;
/*     */   public static final int SET_MEMPOLICY = 276;
/*     */   public static final int MQ_OPEN = 277;
/*     */   public static final int MQ_UNLINK = 278;
/*     */   public static final int MQ_TIMEDSEND = 279;
/*     */   public static final int MQ_TIMEDRECEIVE = 280;
/*     */   public static final int MQ_NOTIFY = 281;
/*     */   public static final int MQ_GETSETATTR = 282;
/*     */   public static final int KEXEC_LOAD = 283;
/*     */   public static final int WAITID = 284;
/*     */   public static final int SYS_SETALTROOT = 285;
/*     */   public static final int ADD_KEY = 286;
/*     */   public static final int REQUEST_KEY = 287;
/*     */   public static final int KEYCTL = 288;
/*     */   public static final int IOPRIO_SET = 289;
/*     */   public static final int IOPRIO_GET = 290;
/*     */   public static final int INOTIFY_INIT = 291;
/*     */   public static final int INOTIFY_ADD_WATCH = 292;
/*     */   public static final int INOTIFY_RM_WATCH = 293;
/*     */   public static final int MIGRATE_PAGES = 294;
/*     */   public static final int OPENAT = 295;
/*     */   public static final int MKDIRAT = 296;
/*     */   public static final int MKNODAT = 297;
/*     */   public static final int FCHOWNAT = 298;
/*     */   public static final int FUTIMESAT = 299;
/*     */   public static final int FSTATAT64 = 300;
/*     */   public static final int UNLINKAT = 301;
/*     */   public static final int RENAMEAT = 302;
/*     */   public static final int LINKAT = 303;
/*     */   public static final int SYMLINKAT = 304;
/*     */   public static final int READLINKAT = 305;
/*     */   public static final int FCHMODAT = 306;
/*     */   public static final int FACCESSAT = 307;
/*     */   public static final int PSELECT6 = 308;
/*     */   public static final int PPOLL = 309;
/*     */   public static final int UNSHARE = 310;
/*     */   public static final int SET_ROBUST_LIST = 311;
/*     */   public static final int GET_ROBUST_LIST = 312;
/*     */   public static final int SPLICE = 313;
/*     */   public static final int SYNC_FILE_RANGE = 314;
/*     */   public static final int TEE = 315;
/*     */   public static final int VMSPLICE = 316;
/*     */   public static final int MOVE_PAGES = 317;
/*     */   public static final int GETCPU = 318;
/*     */   public static final int EPOLL_PWAIT = 319;
/*     */   public static final int UTIMENSAT = 320;
/*     */   public static final int SIGNALFD = 321;
/*     */   public static final int TIMERFD = 322;
/*     */   public static final int EVENTFD = 323;
/*     */   public static final int FALLOCATE = 324;
/*     */   public static final int TIMERFD_SETTIME = 325;
/*     */   public static final int TIMERFD_GETTIME = 326;
/*     */   public static final int SIGNALFD4 = 327;
/*     */   public static final int EVENTFD2 = 328;
/*     */   public static final int EPOLL_CREATE1 = 329;
/*     */   public static final int DUP3 = 330;
/*     */   public static final int PIPE2 = 331;
/*     */   public static final int INOTIFY_INIT1 = 332;
/*     */   public static final int PREADV = 333;
/*     */   public static final int PWRITEV = 334;
/*     */   public static final int RT_TGSIGQUEUEINFO = 335;
/*     */   public static final int PERF_EVENT_OPEN = 336;
/*     */   public static final int RECVMMSG = 337;
/*     */   public static final int FANOTIFY_INIT = 338;
/*     */   public static final int FANOTIFY_MARK = 339;
/*     */   public static final int PRLIMIT64 = 340;
/*     */   public static final int NAME_TO_HANDLE_AT = 341;
/*     */   public static final int OPEN_BY_HANDLE_AT = 342;
/*     */   public static final int CLOCK_ADJTIME = 343;
/*     */   public static final int SYNCFS = 344;
/*     */   public static final int PRIVATE_FUTEX = 128;
/*     */   
/*     */   public static String getName(int ucode)
/*     */   {
/* 387 */     return (String)reverseIndex.get(Integer.valueOf(ucode));
/*     */   }
/*     */   
/* 390 */   private static HashMap index = new HashMap();
/* 391 */   private static HashMap reverseIndex = new HashMap();
/*     */   public static final int TARGET_GDT_ENTRY_TLS_MIN = 12;
/*     */   public static final int TARGET_GDT_ENTRY_TLS_MAX = 14;
/*     */   
/* 395 */   static { Class cls = SystemCalls.class;
/* 396 */     Field[] flds = cls.getDeclaredFields();
/*     */     
/* 398 */     for (int i = 0; i < flds.length; i++)
/*     */     {
/*     */       try
/*     */       {
/* 402 */         if (flds[i].getType() == Integer.TYPE)
/*     */         {
/* 404 */           int mods = flds[i].getModifiers();
/* 405 */           if ((!java.lang.reflect.Modifier.isStatic(mods)) || (java.lang.reflect.Modifier.isFinal(mods)))
/*     */           {
/*     */ 
/* 408 */             String name = flds[i].getName();
/* 409 */             int value = flds[i].getInt(null);
/* 410 */             Integer val = Integer.valueOf(value);
/*     */             
/* 412 */             index.put(name, val);
/* 413 */             reverseIndex.put(val, name);
/*     */           }
/*     */         }
/*     */       } catch (Exception e) {
/* 417 */         e.printStackTrace();
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public static int writev(MainMemory mem, IoLib iolib, int fd, int iovec$, int iovecCount)
/*     */   {
/* 424 */     int written = 0;
/* 425 */     for (int i = 0; i < iovecCount; i++)
/*     */     {
/* 427 */       int address = mem.getDoubleWord(iovec$ + 8 * i);
/* 428 */       int amount = mem.getDoubleWord(iovec$ + 8 * i + 4);
/* 429 */       written += iolib.write(fd, address, amount);
/*     */     }
/* 431 */     return written;
/*     */   }
/*     */   
/*     */ 
/*     */   public static int uname(MainMemory mem, int addr)
/*     */   {
/* 437 */     byte[] uname = new byte['Ɔ'];
/* 438 */     System.arraycopy(SystemConstants.UNAME_SYSNAME.getBytes(), 0, uname, 0, SystemConstants.UNAME_SYSNAME.length());
/* 439 */     System.arraycopy(SystemConstants.UNAME_NODENAME.getBytes(), 0, uname, 65, SystemConstants.UNAME_NODENAME.length());
/* 440 */     System.arraycopy(SystemConstants.UNAME_RELEASE.getBytes(), 0, uname, 130, SystemConstants.UNAME_RELEASE.length());
/* 441 */     System.arraycopy(SystemConstants.UNAME_VERSION.getBytes(), 0, uname, 195, SystemConstants.UNAME_VERSION.length());
/* 442 */     System.arraycopy(SystemConstants.UNAME_MACHINE.getBytes(), 0, uname, 260, SystemConstants.UNAME_MACHINE.length());
/* 443 */     if (!SystemConstants.UNAME_RELEASE.startsWith("2"))
/* 444 */       System.arraycopy("(none)".getBytes(), 0, uname, 325, "(none)".length());
/* 445 */     mem.copyArrayIntoContents(addr, uname, 0, uname.length);
/* 446 */     return 0;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static int syscall(MainMemory memory, Processor cpu, IoLib iolib, int eax, int ebx, int ecx, int edx, int esi, int edi, int esp, int ebp)
/*     */   {
/* 455 */     ThreadContext ctx = cpu.ctx;
/* 456 */     int result = syscallInternal(memory, cpu, iolib, eax, ebx, ecx, edx, esi, edi, esp, ebp);
/* 457 */     SignalCalls.pendingSignals(ctx);
/* 458 */     if (ctx.isKilled()) {
/* 459 */       syscallInternal(memory, cpu, iolib, 1, ebx, ecx, edx, esi, edi, esp, ebp);
/*     */     }
/* 461 */     return result;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   private static int syscallInternal(MainMemory memory, Processor cpu, IoLib iolib, int eax, int ebx, int ecx, int edx, int esi, int edi, int esp, int ebp)
/*     */   {
/* 468 */     ThreadContext ctx = cpu.ctx;
/*     */     
/*     */     int result;
/* 471 */     switch (eax)
/*     */     {
/*     */     case 122: 
/* 474 */       if (strace)
/* 475 */         ctx.out("STRACE", "Syscall-uname(" + Integer.toHexString(ebx) + ")", new Object[0]);
/* 476 */       return uname(memory, ebx);
/*     */     case 45: 
/* 478 */       int brk = memory.allocateUpTo(ebx);
/* 479 */       if (strace)
/* 480 */         ctx.out("STRACE", "Syscall-brk(0x" + Integer.toHexString(ebx) + ") = 0x" + Integer.toHexString(brk), new Object[0]);
/* 481 */       return brk;
/*     */     case 243: 
/* 483 */       if (strace) {
/* 484 */         ctx.out("STRACE", "Syscall-set_thread_area()", new Object[0]);
/*     */       }
/*     */       
/* 487 */       int entryNumber = memory.getDoubleWord(ebx);
/* 488 */       int base = memory.getDoubleWord(ebx + 4);
/* 489 */       int limit = memory.getDoubleWord(ebx + 8);
/* 490 */       int flags = memory.getDoubleWord(ebx + 12);
/*     */       
/*     */ 
/* 493 */       if (entryNumber == -1) {
/* 494 */         for (int i = 12; i <= 14; i++) {
/* 495 */           if (memory.getDoubleWord(cpu.gdt.getBase() + 8 * i) == 0) {
/* 496 */             entryNumber = i;
/* 497 */             break;
/*     */           }
/*     */         }
/*     */       }
/* 501 */       int seg_32bit = flags & 0x1;
/* 502 */       int contents = flags >> 1 & 0x3;
/* 503 */       int read_exec_only = flags >> 3 & 0x1;
/* 504 */       int limit_in_pages = flags >> 4 & 0x1;
/* 505 */       int seg_not_present = flags >> 5 & 0x1;
/* 506 */       int useable = flags >> 6 & 0x1;
/* 507 */       int lm = flags >> 7 & 0x1;
/*     */       
/* 509 */       int entry_1 = (base & 0xFFFF) << 16 | limit & 0xFFFF;
/* 510 */       int entry_2 = base & 0xFF000000 | (base & 0xFF0000) >> 16 | limit & 0xF0000 | (read_exec_only ^ 0x1) << 9 | contents << 10 | (seg_not_present ^ 0x1) << 15 | seg_32bit << 22 | limit_in_pages << 23 | useable << 20 | lm << 21 | 0x7000;
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
/* 522 */       memory.setDoubleWord(cpu.gdt.getBase() + 8 * entryNumber, entry_1);
/* 523 */       memory.setDoubleWord(cpu.gdt.getBase() + 8 * entryNumber + 4, entry_2);
/*     */       
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 529 */       memory.setDoubleWord(cpu.ebp - 40, 6);
/* 530 */       return 0;
/*     */     
/*     */     case 192: 
/* 533 */       int mmap2 = memory.mmap(ebx, ecx, edx, esi, edi, ebp);
/* 534 */       if (!MMap.Flags.isAnonymous(edi))
/*     */       {
/*     */ 
/*     */ 
/* 538 */         if (ebp != 0) ctx.iolib.lseek(edi, ebp, 0);
/* 539 */         ctx.iolib.read(edi, mmap2, ecx);
/*     */       }
/* 541 */       if (strace)
/* 542 */         ctx.out("STRACE", "Syscall-mmap2( 0x%08X, 0x%8X, %s, %s, %d, 0x%x) = 0x%X \n", new Object[] { Integer.valueOf(ebx), Integer.valueOf(ecx), MMap.Prot.toString(edx), MMap.Flags.toString(esi), Integer.valueOf(edi), Integer.valueOf(ebp), Integer.valueOf(mmap2) });
/* 543 */       return mmap2;
/*     */     
/*     */     case 91: 
/* 546 */       if (strace) {
/* 547 */         ctx.out("STRACE", "Syscall-munmap(0x" + Integer.toHexString(ebx) + ", 0x" + Integer.toHexString(ecx) + ")", new Object[0]);
/*     */       }
/* 549 */       return 0;
/*     */     case 163: 
/* 551 */       int mremap = memory.remap(ebx, ecx, edx, esi, edi);
/* 552 */       if (strace)
/* 553 */         ctx.out("STRACE", "Syscall-mremap(0x" + Integer.toHexString(ebx) + ", 0x" + Integer.toHexString(ecx) + ", 0x" + Integer.toHexString(edx) + ", 0x" + Integer.toHexString(esi) + ", 0x" + Integer.toHexString(edi) + ") = 0x" + Integer.toHexString(mremap), new Object[0]);
/* 554 */       return mremap;
/*     */     case 219: 
/* 556 */       int ptr = ebx;
/* 557 */       int len = ecx;
/* 558 */       int advice = edx;
/* 559 */       result = 0;
/* 560 */       if (strace)
/* 561 */         ctx.out("STRACE", "Syscall-madvise( 0x%X , 0x%X, 0x%X)=0x%X ", new Object[] { Integer.valueOf(ptr), Integer.valueOf(len), Integer.valueOf(advice), Integer.valueOf(result) });
/* 562 */       return 0;
/*     */     
/*     */ 
/*     */     case 4: 
/* 566 */       if (strace)
/* 567 */         ctx.out("STRACE", "Syscall-write(0x" + Integer.toHexString(ebx) + ", 0x" + Integer.toHexString(ecx) + ", 0x" + Integer.toHexString(edx) + ")", new Object[0]);
/* 568 */       return iolib.write(ebx, ecx, edx);
/*     */     case 146: 
/* 570 */       if (strace)
/* 571 */         ctx.out("STRACE", "Syscall-writev(0x" + Integer.toHexString(ebx) + ", 0x" + Integer.toHexString(ecx) + ", 0x" + Integer.toHexString(edx) + ")", new Object[0]);
/* 572 */       return writev(memory, iolib, ebx, ecx, edx);
/*     */     case 1: 
/* 574 */       if (strace)
/* 575 */         ctx.out("STRACE", "Syscall-exit()", new Object[0]);
/* 576 */       ctx.exit();
/*     */     case 252: 
/* 578 */       if (strace)
/* 579 */         ctx.out("STRACE", "Syscall-exit_group()", new Object[0]);
/* 580 */       ctx.exitGroup();
/*     */     case 5: 
/* 582 */       int open = iolib.open(ebx, ecx, null);
/* 583 */       String fName = ctx.memory.string(ebx);
/* 584 */       if (strace) {
/* 585 */         ctx.out("STRACE", "Syscall-open(0x" + Integer.toHexString(ebx) + ":" + fName + ", 0x" + Integer.toHexString(ecx) + ") = " + open, new Object[0]);
/*     */       }
/* 587 */       return open;
/*     */     
/*     */     case 3: 
/* 590 */       int read = iolib.read(ebx, ecx, edx);
/* 591 */       if (strace)
/* 592 */         ctx.out("STRACE", "Syscall-read(0x" + Integer.toHexString(ebx) + ", 0x" + Integer.toHexString(ecx) + ", 0x" + Integer.toHexString(edx) + ") = 0x" + Integer.toHexString(read), new Object[0]);
/* 593 */       if (read == 0) return Errors.syscall(11);
/* 594 */       return read;
/*     */     
/*     */     case 6: 
/* 597 */       if (strace)
/* 598 */         ctx.out("STRACE", "Syscall-close(" + ebx + ")", new Object[0]);
/* 599 */       return iolib.close(ebx);
/*     */     
/*     */     case 125: 
/* 602 */       if (strace)
/* 603 */         ctx.out("STRACE", "Syscall-mprotect(0x%X +0x%X ,PROT=0x%X ) = 0x%X UNIMPLEMENTED", new Object[] { Integer.valueOf(ebx), Integer.valueOf(ecx), Integer.valueOf(edx), Integer.valueOf(0) });
/* 604 */       return 0;
/*     */     
/*     */     case 140: 
/* 607 */       long llseek = iolib.lseek(ebx, ecx << 32 | edx, edi);
/*     */       
/* 609 */       memory.setDoubleWord(esp + 4, (int)llseek);
/* 610 */       memory.setDoubleWord(esp + 8, (int)(llseek >> 32));
/* 611 */       if (strace)
/* 612 */         ctx.out("STRACE", "Syscall-llseek(0x" + Integer.toHexString(ebx) + ", 0x" + Long.toHexString(ecx << 32 | edx) + ", 0x" + Integer.toHexString(edi) + ") = " + llseek, new Object[0]);
/* 613 */       return (int)llseek;
/*     */     
/*     */     case 201: 
/* 616 */       int euid = 1000;
/* 617 */       if (strace)
/* 618 */         ctx.out("STRACE", "Syscall-geteuid() = " + euid, new Object[0]);
/* 619 */       return euid;
/*     */     case 199: 
/* 621 */       int uid = 1000;
/* 622 */       if (strace)
/* 623 */         ctx.out("STRACE", "Syscall-getuid() = " + uid, new Object[0]);
/* 624 */       return uid;
/*     */     case 202: 
/* 626 */       int egid = 1000;
/* 627 */       if (strace)
/* 628 */         ctx.out("STRACE", "Syscall-getegid() = " + egid, new Object[0]);
/* 629 */       return egid;
/*     */     case 200: 
/* 631 */       int gid = 1000;
/* 632 */       if (strace)
/* 633 */         ctx.out("STRACE", "Syscall-getgid() = " + gid, new Object[0]);
/* 634 */       return gid;
/*     */     case 120: 
/* 636 */       cpu.esp = esp;
/* 637 */       int tid = Clone.clone(ctx, ebx, ecx, edx, esi, edi);
/* 638 */       if (strace) {
/* 639 */         ctx.out("STRACE", "Syscall-clone(0x%X ,0x%X ,0x%X ,0x%X ,0x%X ) = 0x%X ", new Object[] { Integer.valueOf(ebx), Integer.valueOf(ecx), Integer.valueOf(edx), Integer.valueOf(esi), Integer.valueOf(edi), Integer.valueOf(tid) });
/*     */       }
/* 641 */       return tid;
/*     */     case 240: 
/* 643 */       result = Futex.syscall(ctx, ebx, ecx, edx, esi, edi, ebp);
/* 644 */       if (strace)
/* 645 */         ctx.out("STRACE", "Syscall-futex(0x%X ,0x%X ,0x%X ,0x%X ,0x%X ,0x%X ) = 0x%X ", new Object[] { Integer.valueOf(ebx), Integer.valueOf(ecx), Integer.valueOf(edx), Integer.valueOf(esi), Integer.valueOf(edi), Integer.valueOf(ebp), Integer.valueOf(result) });
/* 646 */       return result;
/*     */     case 13: 
/* 648 */       if (strace)
/* 649 */         ctx.out("STRACE", "Syscall-time() UNIMPLEMENTED", new Object[0]);
/* 650 */       return (int)(System.currentTimeMillis() / 1000L);
/*     */     case 270: 
/* 652 */       result = SignalCalls.tgKill(ctx, ebx, ecx, edx);
/* 653 */       if (strace)
/* 654 */         ctx.out("STRACE", "Syscall-TGKILL(0x%X,0x%X,0x%X) =%X \n", new Object[] { Integer.valueOf(ebx), Integer.valueOf(ecx), Integer.valueOf(edx), Integer.valueOf(result) });
/* 655 */       return result;
/*     */     
/*     */     case 175: 
/* 658 */       result = SignalCalls.sigProcMask(ctx, ebx, ecx, edx);
/* 659 */       if (strace)
/* 660 */         ctx.out("STRACE", "Syscall-RT_SIGPROCMASK(0x%X,0x%X,0x%X) =%X \n", new Object[] { Integer.valueOf(ebx), Integer.valueOf(ecx), Integer.valueOf(edx), Integer.valueOf(result) });
/* 661 */       return result;
/*     */     
/*     */     case 174: 
/* 664 */       result = SignalCalls.sigAction(ctx, ebx, ecx, edx, esi);
/* 665 */       if (strace)
/* 666 */         ctx.out("STRACE", "Syscall-RT_SIGACTION(0x%X,0x%X,0x%X,0x%X) =%X \n", new Object[] { Integer.valueOf(ebx), Integer.valueOf(ecx), Integer.valueOf(edx), Integer.valueOf(esi), Integer.valueOf(result) });
/* 667 */       return result;
/*     */     
/*     */ 
/*     */     case 158: 
/* 671 */       ctx.yield();
/* 672 */       if (strace)
/* 673 */         ctx.out("STRACE", "Syscall-SCHED_YIELD()", new Object[0]);
/* 674 */       return 0;
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     case 258: 
/* 681 */       int tid = ctx.getTid();
/* 682 */       ctx.setClearChildTid(ebx);
/* 683 */       if (strace)
/* 684 */         ctx.out("STRACE", "Syscall-set_tid_address( 0x" + Integer.toHexString(ebx) + ") - needs verification", new Object[0]);
/* 685 */       return tid;
/*     */     
/*     */     case 311: 
/* 688 */       ctx.setRobustList(ebx, ecx);
/* 689 */       if (strace)
/* 690 */         ctx.out("STRACE", "Syscall-set_robust_list( 0x%X , 0x%X)=0 ", new Object[] { Integer.valueOf(ebx), Integer.valueOf(ecx) });
/* 691 */       return 0;
/*     */     
/*     */     case 312: 
/* 694 */       ThreadContext other = ThreadContext.getThreadCtx(ebx);
/* 695 */       if (other == null) return Errors.syscall(3);
/* 696 */       memory.setDoubleWord(ecx, other.getRobustlistHead$());
/* 697 */       memory.setDoubleWord(edx, other.getRobustlistLen());
/* 698 */       if (strace)
/* 699 */         ctx.out("STRACE", "Syscall-get_robust_list( 0x%X , 0x%X, 0x%X ", new Object[] { Integer.valueOf(ebx), Integer.valueOf(ecx), Integer.valueOf(edx) });
/* 700 */       return 0;
/*     */     
/*     */     case 102: 
/* 703 */       int call = ebx;
/* 704 */       int args$ = ecx;
/*     */       
/* 706 */       result = Sockets.socketcall(ctx, call, args$);
/* 707 */       if (strace)
/* 708 */         ctx.out("STRACE", "Syscall-socketcall( 0x%X , 0x%X)=0x%X ", new Object[] { Integer.valueOf(ebx), Integer.valueOf(ecx), Integer.valueOf(result) });
/* 709 */       return result;
/*     */     
/*     */     case 33: 
/* 712 */       int name$ = ebx;
/* 713 */       int mode = ecx;
/* 714 */       String name = ctx.memory.string(name$);
/* 715 */       result = -ctx.iolib.access(ctx, name, mode);
/* 716 */       if (strace)
/* 717 */         ctx.out("STRACE", "Syscall-access( 0x%X {%s} , 0x%X )=0x%X ", new Object[] { Integer.valueOf(name$), name, Integer.valueOf(mode), Integer.valueOf(result) });
/* 718 */       return result;
/*     */     
/*     */     case 168: 
/* 721 */       int fds$ = ebx;
/* 722 */       int numFds = ecx;
/* 723 */       int timeout = edx;
/* 724 */       result = Poll.poll(ctx, fds$, numFds, timeout);
/* 725 */       if (strace)
/* 726 */         ctx.out("STRACE", "Syscall-poll( 0x%X , %d, %d)=0x%X ", new Object[] { Integer.valueOf(fds$), Integer.valueOf(numFds), Integer.valueOf(timeout), Integer.valueOf(result) });
/* 727 */       return result;
/*     */     
/*     */ 
/*     */     case 78: 
/* 731 */       long timeofday = ctx.getClock().getTimeOfDay();
/* 732 */       int timeval$ = ebx;
/* 733 */       int timezone$ = ecx;
/* 734 */       int seconds = SystemClock.seconds(timeofday);
/* 735 */       int useconds = SystemClock.useconds(timeofday);
/* 736 */       ctx.memory.setDoubleWord(timeval$, seconds);
/* 737 */       ctx.memory.setDoubleWord(timeval$ + 4, useconds);
/* 738 */       result = 0;
/* 739 */       if (strace)
/* 740 */         ctx.out("STRACE", "Syscall-gettimeofday( 0x%X  , 0x%X)=0x%X {seconds=%d,useconds=%d} ", new Object[] { Integer.valueOf(timeval$), Integer.valueOf(timezone$), Integer.valueOf(result), Integer.valueOf(seconds), Integer.valueOf(useconds) });
/* 741 */       return result;
/*     */     
/*     */ 
/*     */     case 195: 
/* 745 */       int name$ = ebx;
/* 746 */       int struct$ = ecx;
/* 747 */       String name = ctx.memory.string(name$);
/* 748 */       int fd = ctx.iolib.open(name);
/* 749 */       result = Errors.syscall(2);
/* 750 */       if (fd != -1) {
/* 751 */         result = -ctx.iolib.fstat64(fd, struct$, ctx.getClock().getStartTime());
/* 752 */         ctx.iolib.close(fd);
/*     */       }
/* 754 */       if (strace)
/* 755 */         ctx.out("STRACE", "Syscall-stat64( 0x%X {%s} , 0x%X)=0x%X ", new Object[] { Integer.valueOf(name$), name, Integer.valueOf(struct$), Integer.valueOf(result) });
/* 756 */       return result;
/*     */     
/*     */     case 197: 
/* 759 */       result = -iolib.fstat64(ebx, ecx, ctx.getClock().getStartTime());
/* 760 */       if (strace) {
/* 761 */         ctx.out("STRACE", "Syscall-fstat64(0x" + Integer.toHexString(ebx) + ", 0x" + Integer.toHexString(ecx) + ") = " + result, new Object[0]);
/*     */       }
/* 763 */       return result;
/*     */     case 268: 
/* 765 */       int name$ = ebx;
/* 766 */       int statfs$ = ecx;
/* 767 */       String name = ctx.memory.string(name$);
/* 768 */       int fd = ctx.iolib.open(name);
/* 769 */       result = Errors.syscall(2);
/* 770 */       if (fd != -1) {
/* 771 */         result = -ctx.iolib.fstatfs64(ctx, statfs$);
/* 772 */         ctx.iolib.close(fd);
/*     */       }
/* 774 */       if (strace)
/* 775 */         ctx.out("STRACE", "Syscall-statfs64( 0x%X {%s} , 0x%X)=0x%X ", new Object[] { Integer.valueOf(name$), name, Integer.valueOf(statfs$), Integer.valueOf(result) });
/* 776 */       return result;
/*     */     
/*     */ 
/*     */     case 269: 
/* 780 */       int statfs$ = ecx;
/* 781 */       int fd = ebx;
/* 782 */       result = Errors.syscall(2);
/* 783 */       if (fd != -1) {
/* 784 */         result = -ctx.iolib.fstatfs64(ctx, statfs$);
/*     */       }
/* 786 */       if (strace)
/* 787 */         ctx.out("STRACE", "Syscall-fstatfs64( 0x%X , 0x%X)=0x%X ", new Object[] { Integer.valueOf(fd), Integer.valueOf(statfs$), Integer.valueOf(result) });
/* 788 */       return result;
/*     */     
/*     */     case 155: 
/* 791 */       result = Scheduling.getParam(ctx, ebx, ecx);
/* 792 */       if (strace)
/* 793 */         ctx.out("STRACE", "Syscall-sched_getparam( 0x%X , 0x%X)=0x%X ", new Object[] { Integer.valueOf(ebx), Integer.valueOf(ecx), Integer.valueOf(result) });
/* 794 */       return result;
/*     */     
/*     */     case 157: 
/* 797 */       result = Scheduling.getScheduler(ctx, ebx);
/* 798 */       if (strace)
/* 799 */         ctx.out("STRACE", "Syscall-sched_getscheduler( 0x%X , 0x%X)=0x%X ", new Object[] { Integer.valueOf(ebx), Integer.valueOf(ecx), Integer.valueOf(result) });
/* 800 */       return result;
/*     */     
/*     */     case 154: 
/* 803 */       result = Scheduling.setParam(ctx, ebx, ecx);
/* 804 */       if (strace)
/* 805 */         ctx.out("STRACE", "Syscall-sched_setparam( 0x%X , 0x%X)=0x%X ", new Object[] { Integer.valueOf(ebx), Integer.valueOf(ecx), Integer.valueOf(result) });
/* 806 */       return result;
/*     */     
/*     */     case 156: 
/* 809 */       result = Scheduling.setScheduler(ctx, ebx, ecx, edx);
/* 810 */       if (strace)
/* 811 */         ctx.out("STRACE", "Syscall-sched_setscheduler( 0x%X ,%d, 0x%X,0x%X,)=0x%X ", new Object[] { Integer.valueOf(ebx), Integer.valueOf(ecx), Integer.valueOf(edx), Integer.valueOf(result) });
/* 812 */       return result;
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
/* 836 */     return 61440;
/*     */   }
/*     */ }


/* Location:              C:\Users\Joey\Downloads\codejar.jar!\com\emt\proteus\runtime32\syscall\SystemCalls.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */