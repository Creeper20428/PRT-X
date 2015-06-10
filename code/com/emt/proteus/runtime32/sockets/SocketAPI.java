/*     */ package com.emt.proteus.runtime32.sockets;
/*     */ 
/*     */ import com.emt.proteus.runtime32.FatalException;
/*     */ import com.emt.proteus.runtime32.ThreadContext;
/*     */ import com.emt.proteus.runtime32.io.IoLib;
/*     */ import com.emt.proteus.runtime32.io.PSocket;
/*     */ import com.emt.proteus.runtime32.io.PSocket.Address;
/*     */ import com.emt.proteus.runtime32.io.PSocket.Type;
/*     */ import com.emt.proteus.runtime32.memory.MainMemory;
/*     */ import com.emt.proteus.runtime32.memory.ManagedMemory;
/*     */ import com.emt.proteus.utils.CStruct;
/*     */ import java.net.Socket;
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
/*     */ public class SocketAPI
/*     */ {
/*     */   public static final int EAI_BADFLAGS = -1;
/*     */   public static final int EAI_NONAME = -2;
/*     */   public static final int EAI_AGAIN = -3;
/*     */   public static final int EAI_FAIL = -4;
/*     */   public static final int EAI_NODATA = -5;
/*     */   public static final int EAI_FAMILY = -6;
/*     */   public static final int EAI_SOCKTYPE = -7;
/*     */   public static final int EAI_SERVICE = -8;
/*     */   public static final int EAI_ADDRFAMILY = -9;
/*     */   public static final int EAI_MEMORY = -10;
/*     */   public static final int EAI_SYSTEM = -11;
/*     */   public static final int EAI_OVERFLOW = -12;
/*     */   public static final int EAI_INPROGRESS = -100;
/*     */   public static final int EAI_CANCELED = -101;
/*     */   public static final int EAI_NOTCANCELED = -102;
/*     */   public static final int EAI_ALLDONE = -103;
/*     */   public static final int EAI_INTR = -104;
/*     */   public static final int EAI_IDN_ENCODE = -105;
/*     */   private static KernelData _kernelData;
/*     */   public static final int LOCAL_HOST_IP = 2130706433;
/*     */   public static final int XSERVER_PORT = 6002;
/*     */   
/*     */   public static int getAddrInfo(ThreadContext ctx, int node$, int service$, int hintAI$, int resAI$$)
/*     */   {
/*  62 */     KernelData kernelData = getKernelData(ctx);
/*  63 */     String node = ctx.memory.string(node$);
/*  64 */     String serviceString = ctx.memory.string(service$);
/*  65 */     String key = node + ":" + serviceString;
/*  66 */     int service = Integer.parseInt(serviceString);
/*  67 */     if (key.equals("127.0.0.1:6002"))
/*     */     {
/*  69 */       ManagedMemory systemAllocator = ctx.getSystemAllocator();
/*  70 */       AddrInfo info = (AddrInfo)systemAllocator.calloc(new AddrInfo());
/*  71 */       info.copy(kernelData.xserverAddrInfo);
/*  72 */       ctx.memory.setDoubleWord(resAI$$, info.addressOf());
/*  73 */       return 0;
/*     */     }
/*  75 */     return -5;
/*     */   }
/*     */   
/*     */   private static synchronized KernelData getKernelData(ThreadContext ctx)
/*     */   {
/*  80 */     if (_kernelData == null) {
/*  81 */       _kernelData = new KernelData(null);
/*  82 */       ManagedMemory systemAllocator = ctx.getSystemAllocator();
/*  83 */       _kernelData.xserver = ((PSocket.Address)systemAllocator.calloc(new PSocket.Address())).assignIP4((short)2, 2130706433, 6002);
/*     */       
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  91 */       _kernelData.xserverAddrInfo = ((AddrInfo)systemAllocator.calloc(new AddrInfo()).set(AddrInfo.ai_flags, 0L).set(AddrInfo.ai_family, 2L).set(AddrInfo.ai_socktype, 1L).set(AddrInfo.ai_protocol, 6L).set(AddrInfo.ai_addr$, _kernelData.xserver).set(AddrInfo.ai_addrlen, _kernelData.xserver.getSize()).set(AddrInfo.ai_canonname$, null).set(AddrInfo.ai_next$, null));
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
/* 103 */     return _kernelData;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static int socket(ThreadContext ctx, int domain, int type, int protocol)
/*     */   {
/* 115 */     String name = String.format("%s %s %s ", new Object[] { ProtoFamily.toString(domain), SocketType.toString(type), IPProto.toString(protocol) });
/*     */     
/*     */ 
/*     */ 
/* 119 */     ctx.warn(name, new Object[0]);
/* 120 */     PSocket socket = null;
/* 121 */     switch (domain) {
/*     */     case 2: 
/* 123 */       if (SocketType.isStream(type)) {
/* 124 */         socket = new PSocket(PSocket.Type.TCP, new PSocket.Address());
/* 125 */         socket.getAddress().family((short)domain);
/* 126 */         return ctx.iolib.newSocket(socket);
/*     */       }
/*     */       break;
/*     */     default: 
/* 130 */       return -1;
/*     */     }
/* 132 */     throw new FatalException("TDB");
/*     */   }
/*     */   
/*     */   public static int setsockopt(ThreadContext context, int socket, int level, int option_name, int value$, int valueLen) {
/* 136 */     context.warn("SETSOCKOPT %d:%s %s ", new Object[] { Integer.valueOf(socket), SockOpt.toString(level), SockOpt.toString(level, option_name) });
/* 137 */     PSocket pSocket = context.getIoLib().getSocket(socket);
/*     */     
/* 139 */     if (SockOpt.isNoDelay(level, option_name)) {
/* 140 */       pSocket.setNoDelay(true);
/* 141 */     } else if (SockOpt.isKeepAlive(level, option_name)) {
/* 142 */       pSocket.setKeepAlive(true);
/*     */     }
/* 144 */     return 0;
/*     */   }
/*     */   
/* 147 */   public static int getsockopt(ThreadContext context, int socket, int level, int option_name, int value$, int valueLength$) { context.warn("GETSOCKOPT %d:%s %s ", new Object[] { Integer.valueOf(socket), SockOpt.toString(level), SockOpt.toString(level, option_name) });
/* 148 */     return 0;
/*     */   }
/*     */   
/*     */   public static void freeAddrInfo(ThreadContext ctx, int res$) {
/* 152 */     ctx.getSystemAllocator().free(res$);
/*     */   }
/*     */   
/*     */   public static int connect(ThreadContext context, int socket, int sockaddr$, int sockaddrLength) {
/* 156 */     context.warn("CONNECT %d:%X %d ", new Object[] { Integer.valueOf(socket), Integer.valueOf(sockaddr$), Integer.valueOf(sockaddrLength) });
/* 157 */     PSocket pSocket = context.getIoLib().getSocket(socket);
/* 158 */     pSocket.getAddress().assign(sockaddr$, context.memory);
/* 159 */     return context.iolib.connect(socket);
/*     */   }
/*     */   
/*     */   public static int getPeerName(ThreadContext context, int socket, int sockaddr$, int sockaddrLength$) {
/* 163 */     new Socket();
/* 164 */     context.warn("GetPeerName %d:%X %d ", new Object[] { Integer.valueOf(socket), Integer.valueOf(sockaddr$), Integer.valueOf(context.memory.getDoubleWord(sockaddrLength$)) });
/* 165 */     PSocket pSocket = context.getIoLib().getSocket(socket);
/* 166 */     pSocket.getAddress().writeTo(context.memory, sockaddr$);
/* 167 */     context.memory.setDoubleWord(sockaddrLength$, pSocket.getAddress().getSize());
/* 168 */     return 0;
/*     */   }
/*     */   
/*     */   private static class KernelData
/*     */   {
/*     */     public PSocket.Address xserver;
/*     */     public AddrInfo xserverAddrInfo;
/*     */   }
/*     */ }


/* Location:              C:\Users\Joey\Downloads\codejar.jar!\com\emt\proteus\runtime32\sockets\SocketAPI.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */