/*     */ package com.emt.proteus.runtime32.syscall;
/*     */ 
/*     */ import com.emt.proteus.runtime32.ThreadContext;
/*     */ import com.emt.proteus.runtime32.memory.MainMemory;
/*     */ import com.emt.proteus.runtime32.sockets.SocketAPI;
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
/*     */ public final class Sockets
/*     */ {
/*     */   public static final int SYS_SOCKET = 1;
/*     */   public static final int SYS_BIND = 2;
/*     */   public static final int SYS_CONNECT = 3;
/*     */   public static final int SYS_LISTEN = 4;
/*     */   public static final int SYS_ACCEPT = 5;
/*     */   public static final int SYS_GETSOCKNAME = 6;
/*     */   public static final int SYS_GETPEERNAME = 7;
/*     */   public static final int SYS_SOCKETPAIR = 8;
/*     */   public static final int SYS_SEND = 9;
/*     */   public static final int SYS_RECV = 10;
/*     */   public static final int SYS_SENDTO = 11;
/*     */   public static final int SYS_RECVFROM = 12;
/*     */   public static final int SYS_SHUTDOWN = 13;
/*     */   public static final int SYS_SETSOCKOPT = 14;
/*     */   public static final int SYS_GETSOCKOPT = 15;
/*     */   public static final int SYS_SENDMSG = 16;
/*     */   public static final int SYS_RECVMSG = 17;
/*     */   public static final int SYS_ACCEPT4 = 18;
/*     */   public static final int SYS_RECVMMSG = 19;
/*     */   public static final int SYS_SENDMMSG = 20;
/*     */   public static final int __SO_ACCEPTCON = 65536;
/*     */   public static final int SOCK_ASYNC_NOSPACE = 0;
/*     */   public static final int SOCK_ASYNC_WAITDATA = 1;
/*     */   public static final int SOCK_NOSPACE = 2;
/*     */   public static final int SOCK_PASSCRED = 3;
/*     */   public static final int SOCK_PASSSEC = 4;
/*  64 */   private static final int[] ARGCOUNT = { 0, 3, 3, 3, 2, 3, 3, 3, 4, 4, 4, 6, 6, 2, 5, 5, 3, 3, 6, 2, 5, 5, 3, 3 };
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static int socketcall(ThreadContext context, int call, int args$)
/*     */   {
/*  73 */     switch (call) {
/*     */     case 1: 
/*  75 */       return SocketAPI.socket(context, arg(context, args$, 0), arg(context, args$, 1), arg(context, args$, 2));
/*     */     
/*     */ 
/*     */     case 2: 
/*     */       break;
/*     */     
/*     */ 
/*     */     case 3: 
/*  83 */       return Errors.syscall(SocketAPI.connect(context, arg(context, args$, 0), arg(context, args$, 1), arg(context, args$, 2)));
/*     */     
/*     */     case 4: 
/*     */       break;
/*     */     
/*     */     case 5: 
/*     */       break;
/*     */     
/*     */     case 6: 
/*     */       break;
/*     */     
/*     */     case 7: 
/*  95 */       return Errors.syscall(SocketAPI.getPeerName(context, arg(context, args$, 0), arg(context, args$, 1), arg(context, args$, 2)));
/*     */     case 8: 
/*     */       break;
/*     */     case 9: 
/*     */       break;
/*     */     case 10: 
/*     */       break;
/*     */     
/*     */     case 11: 
/*     */       break;
/*     */     
/*     */     case 12: 
/*     */       break;
/*     */     
/*     */     case 13: 
/*     */       break;
/*     */     
/*     */     case 14: 
/* 113 */       return SocketAPI.setsockopt(context, arg(context, args$, 0), arg(context, args$, 1), arg(context, args$, 2), arg(context, args$, 3), arg(context, args$, 4));
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     case 15: 
/* 121 */       return SocketAPI.getsockopt(context, arg(context, args$, 0), arg(context, args$, 1), arg(context, args$, 2), arg(context, args$, 3), arg(context, args$, 4));
/*     */     
/*     */     case 16: 
/*     */       break;
/*     */     
/*     */     case 17: 
/*     */       break;
/*     */     
/*     */     case 18: 
/*     */       break;
/*     */     
/*     */     case 19: 
/*     */       break;
/*     */     
/*     */     case 20: 
/*     */       break;
/*     */     
/*     */ 
/*     */     default: 
/* 140 */       return Errors.syscall(22);
/*     */     }
/*     */     
/* 143 */     return 0;
/*     */   }
/*     */   
/*     */   private static int arg(ThreadContext context, int args$, int index) {
/* 147 */     return context.memory.getDoubleWord(args$ + (index << 2));
/*     */   }
/*     */ }


/* Location:              C:\Users\Joey\Downloads\codejar.jar!\com\emt\proteus\runtime32\syscall\Sockets.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */