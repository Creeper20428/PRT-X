/*     */ package com.emt.proteus.runtime32.io;
/*     */ 
/*     */ import com.emt.proteus.runtime32.SystemClock;
/*     */ import com.emt.proteus.runtime32.ThreadContext;
/*     */ import com.emt.proteus.runtime32.api.IoSystem;
/*     */ import com.emt.proteus.runtime32.memory.MainMemory;
/*     */ import java.io.BufferedReader;
/*     */ import java.io.FileInputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStreamReader;
/*     */ import java.io.PrintStream;
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
/*     */ public final class IoLib
/*     */ {
/*     */   private IoSys __HANDLES;
/*     */   private MainMemory mem;
/*     */   public static final int SEEK_SET = 0;
/*     */   public static final int SEEK_CUR = 1;
/*     */   public static final int SEEK_END = 2;
/*     */   
/*     */   public IoLib(IoSystem io, MainMemory mem)
/*     */   {
/*  48 */     this.mem = mem;
/*  49 */     this.__HANDLES = new IoSys(io, mem);
/*  50 */     this.__HANDLES.start();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void load(String openfiles)
/*     */     throws IOException
/*     */   {
/*  63 */     BufferedReader r = new BufferedReader(new InputStreamReader(new FileInputStream(openfiles)));
/*  64 */     String line = r.readLine();
/*  65 */     while (line != null)
/*     */     {
/*  67 */       String filename = line;
/*  68 */       int handle = Integer.parseInt(r.readLine());
/*  69 */       String offset = r.readLine();
/*  70 */       open(filename, handle);
/*  71 */       lseek(handle, Long.parseLong(offset), 0);
/*  72 */       System.out.println("LOADED FILE: " + filename + " at " + handle);
/*  73 */       line = r.readLine();
/*     */     }
/*     */   }
/*     */   
/*     */   public IoHandle get(int descriptor) {
/*  78 */     return this.__HANDLES.getByDescriptor(descriptor);
/*     */   }
/*     */   
/*     */   public int fstat64(int fd, int stat64$, long currentStart) {
/*  82 */     IoHandle fh = this.__HANDLES.getByDescriptor(fd);
/*  83 */     fh.stat64(this.mem, stat64$, currentStart);
/*  84 */     return 0;
/*     */   }
/*     */   
/*     */ 
/*     */   public int stat64(ThreadContext ctx, String name, int struct$)
/*     */   {
/*  90 */     IoHandle fh = this.__HANDLES.temporary(name);
/*  91 */     return fh.stat64(ctx.memory, struct$, ctx.getClock().getStartTime());
/*     */   }
/*     */   
/*     */   public void start()
/*     */   {
/*  96 */     this.__HANDLES.start();
/*     */   }
/*     */   
/*  99 */   public void stop() { this.__HANDLES.stop(); }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public int open(int p_path, int imode, int[] var_args)
/*     */   {
/* 106 */     return open(this.mem.string(p_path), imode, var_args);
/*     */   }
/*     */   
/*     */   IoHandle open(String fileName, int desc) {
/* 110 */     return this.__HANDLES.open(fileName, "r", desc);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public int open(String fileName)
/*     */   {
/* 120 */     return safeHandle(this.__HANDLES.open(fileName, "r", 0));
/*     */   }
/*     */   
/*     */   public int open(String fileName, int imode, int[] var_args) {
/* 124 */     if (fileName.equals("/dev/console"))
/* 125 */       return -1;
/* 126 */     String modestring = "rw";
/* 127 */     if ((imode & 0xF) == 0) modestring = "r";
/* 128 */     IoHandle handle = this.__HANDLES.open(fileName, modestring);
/* 129 */     return safeHandle(handle);
/*     */   }
/*     */   
/*     */   private static int safeHandle(IoHandle handle) {
/* 133 */     return handle != null ? handle.getDescriptor() : -1;
/*     */   }
/*     */   
/*     */   public int read(int handle$, int buf$, int len) {
/* 137 */     IoHandle handle = this.__HANDLES.getByDescriptor(handle$);
/* 138 */     return handle.read(buf$, len, this.mem);
/*     */   }
/*     */   
/* 141 */   public int write(int fd, int address, int amount) { IoHandle fHandle = this.__HANDLES.getByDescriptor(fd);
/*     */     try {
/* 143 */       return this.mem.write(address, amount, fHandle);
/*     */     } catch (IOException e) {}
/* 145 */     return -1;
/*     */   }
/*     */   
/*     */   public long lseek(int fd, long offset, int from) {
/* 149 */     IoHandle fh = this.__HANDLES.getByDescriptor(fd);
/* 150 */     long seek = fh.seek(offset, from);
/* 151 */     return seek;
/*     */   }
/*     */   
/*     */ 
/*     */   public int close(int fd)
/*     */   {
/* 157 */     IoHandle fh = this.__HANDLES.getByDescriptor(fd);
/* 158 */     int fclose = fh.fclose();
/* 159 */     this.__HANDLES.dispose(fh);
/* 160 */     return fclose;
/*     */   }
/*     */   
/*     */   public int newSocket(PSocket socket)
/*     */   {
/* 165 */     IoHandle handle = this.__HANDLES.create(socket);
/* 166 */     return handle.getDescriptor();
/*     */   }
/*     */   
/*     */   public int access(ThreadContext ctx, String name, int mode) {
/* 170 */     return this.__HANDLES.access(name, mode);
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
/* 505 */   public PSocket getSocket(int socket) { return getSocketHandle(socket).getSocket(); }
/*     */   
/*     */   public SocketHandle getSocketHandle(int socket) {
/* 508 */     SocketHandle ioHandle = (SocketHandle)get(socket);
/* 509 */     return ioHandle;
/*     */   }
/*     */   
/*     */   public int connect(int socket) {
/*     */     try {
/* 514 */       this.__HANDLES.connect(socket);
/* 515 */       return 0;
/*     */     }
/*     */     catch (Exception e) {}
/* 518 */     return 99;
/*     */   }
/*     */   
/*     */   public int checkEvents(int fd, int eventMask)
/*     */   {
/* 523 */     return get(fd).checkEvents(eventMask);
/*     */   }
/*     */   
/*     */   public int fstatfs64(ThreadContext ctx, int struct$) {
/* 527 */     return Stat.statfs64(ctx.memory, struct$);
/*     */   }
/*     */   
/*     */   public IoSystem getIoSystem() {
/* 531 */     return this.__HANDLES.getIoSystem();
/*     */   }
/*     */ }


/* Location:              C:\Users\Joey\Downloads\codejar.jar!\com\emt\proteus\runtime32\io\IoLib.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */