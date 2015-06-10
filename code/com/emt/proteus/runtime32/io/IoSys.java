/*     */ package com.emt.proteus.runtime32.io;
/*     */ 
/*     */ import com.emt.proteus.runtime32.api.Connection;
/*     */ import com.emt.proteus.runtime32.api.FileProxy;
/*     */ import com.emt.proteus.runtime32.api.IoSystem;
/*     */ import com.emt.proteus.runtime32.memory.MainMemory;
/*     */ import java.io.IOException;
/*     */ import java.net.UnknownHostException;
/*     */ import java.util.Arrays;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class IoSys
/*     */ {
/*  49 */   private static final String[] _BLOCKED_FILES = { "/usr/lib/locale/locale-archive", "/usr/share/locale/locale.alias", "/usr/share/X11/locale/locale.alias", "/usr/share/X11/locale/locale.dir", "/usr/lib/i386-linux-gnu/X11/XtErrorDB" };
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  58 */   private final Providers providers = new Providers().add(new BlockedProvider(_BLOCKED_FILES)).add(new ResourceProvider()).add(new RandomProvider());
/*     */   
/*     */   public static final int MEMORY_FILE_LIMIT = 16777216;
/*     */   
/*     */   private final DescriptorIndex descriptors2handles;
/*     */   
/*     */   public static final String DEV_CONSOLE = "/dev/console";
/*     */   
/*     */   public static final int PRINTSTREAM = -2;
/*     */   
/*     */   public static final int CONSOLE = -1;
/*     */   
/*     */   public static final int DEFAULT = 0;
/*     */   
/*     */   public static final int PROXY = 1;
/*     */   
/*     */   public static final int CHANNEL = 2;
/*     */   
/*     */   public static final int CONNECTION = 3;
/*     */   
/*     */   public static final int MEMORY = 4;
/*     */   
/*     */   public static final int COPYONWRITE = 5;
/*     */   
/*     */   private static final String DEV_STDERR = "/dev/stderr";
/*     */   
/*     */   private final ConsoleIoHandle console;
/*     */   private final IoHandle stderr;
/*     */   private IoSystem ioSystem;
/*     */   
/*     */   public IoSys(IoSystem system, MainMemory mem)
/*     */   {
/*  90 */     this.ioSystem = system;
/*  91 */     this.descriptors2handles = new DescriptorIndex(null);
/*  92 */     this.stderr = new PrintStreamIoHandle(this, "/dev/stderr", system.getStdErr(), 2);
/*  93 */     this.console = new ConsoleIoHandle(this, system.getStdOut(), system.getStdIn(), "/dev/console", 1, 1792);
/*  94 */     this.providers.setDefaultProvider(new IoSystemProvider(system));
/*     */   }
/*     */   
/*     */   public void start() {
/*  98 */     this.descriptors2handles.clear();
/*  99 */     registerHandle(this.console);
/* 100 */     registerHandle(this.stderr);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public IoHandle getByDescriptor(int desc)
/*     */   {
/* 107 */     return this.descriptors2handles.get(desc);
/*     */   }
/*     */   
/*     */   public ConsoleIoHandle getConsole() {
/* 111 */     return this.console;
/*     */   }
/*     */   
/*     */   public IoHandle getStdErr() {
/* 115 */     return this.stderr;
/*     */   }
/*     */   
/*     */   public IoHandle open(String fileName, String mode) {
/* 119 */     return open(fileName, mode, 0);
/*     */   }
/*     */   
/*     */   public IoHandle temporary(String fileName)
/*     */   {
/* 124 */     FileProxy file = null;
/*     */     try {
/* 126 */       file = getFile(fileName, false);
/* 127 */       return new ProxyIoHandle(this, file, "r", 0);
/*     */     } catch (IOException e) {}
/* 129 */     return null;
/*     */   }
/*     */   
/*     */ 
/*     */   public IoHandle open(String fileName, String mode, int desc)
/*     */   {
/*     */     try
/*     */     {
/* 137 */       boolean readOnly = mode.equals("r");
/* 138 */       FileProxy file = getFile(fileName, !readOnly);
/*     */       
/* 140 */       if (file == null) {
/* 141 */         return null;
/*     */       }
/* 143 */       int address = 0;
/* 144 */       if (desc == 0) {
/* 145 */         desc = this.descriptors2handles.nextDescriptor();
/*     */       }
/* 147 */       long length = file.getLength();
/*     */       
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 158 */       IoHandle handle = new ProxyIoHandle(this, file, mode, desc);
/*     */       
/* 160 */       return registerHandle(handle);
/*     */     } catch (IOException e) {}
/* 162 */     return null;
/*     */   }
/*     */   
/*     */   public IoHandle registerHandle(IoHandle handle)
/*     */   {
/* 167 */     this.descriptors2handles.put(handle);
/* 168 */     return handle;
/*     */   }
/*     */   
/*     */   public void dispose(IoHandle ioHandle)
/*     */   {
/* 173 */     this.descriptors2handles.remove(ioHandle);
/*     */   }
/*     */   
/*     */   public void stop() {
/* 177 */     IoHandle[] handles = this.descriptors2handles.handles;
/* 178 */     for (int i = 0; i < handles.length; i++) {
/* 179 */       IoHandle handle = handles[i];
/* 180 */       if (handle != null) {
/*     */         try {
/* 182 */           handle.close();
/*     */         }
/*     */         catch (IOException e) {}
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public IoHandle create(PSocket socket) {
/* 190 */     SocketHandle handle = new SocketHandle(this, socket);
/* 191 */     registerHandle(handle);
/* 192 */     return handle;
/*     */   }
/*     */   
/*     */   public void connect(int socket) throws UnknownHostException, IOException {
/* 196 */     SocketHandle socketHandle = (SocketHandle)getByDescriptor(socket);
/* 197 */     PSocket pSocket = socketHandle.getSocket();
/* 198 */     PSocket.Address address = pSocket.getAddress();
/* 199 */     String addressString = address.toAddressString();
/*     */     
/* 201 */     switch (pSocket.getType()) {
/*     */     case TCP: 
/* 203 */       Connection connection = this.ioSystem.connect(address.IP4Address(), address.getPort());
/*     */       
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 213 */       socketHandle.connect(connection);
/* 214 */       break;
/*     */     case UDP: 
/*     */       break;
/*     */     case FILE: 
/*     */       break;
/*     */     }
/*     */     
/*     */   }
/*     */   
/*     */ 
/*     */   public int access(String name, int mode)
/*     */   {
/*     */     try
/*     */     {
/* 228 */       if (IoMode.Access.isExec(mode)) return 13;
/* 229 */       FileProxy file = getFile(name, false);
/* 230 */       if (file == null) return 2;
/* 231 */       if ((IoMode.Access.isWrite(mode)) && 
/* 232 */         (getFile(name, true) == null)) return 13;
/*     */     }
/*     */     catch (IOException e) {
/* 235 */       return 5;
/*     */     }
/* 237 */     return 0;
/*     */   }
/*     */   
/*     */   private static class DescriptorIndex
/*     */   {
/* 242 */     private IoHandle[] handles = new IoHandle['Ā'];
/* 243 */     private int length = 0;
/*     */     
/*     */     public IoHandle get(int desc) {
/* 246 */       return this.handles[to_index(desc)];
/*     */     }
/*     */     
/*     */     private int to_index(int desc) {
/* 250 */       return Math.max(0, desc);
/*     */     }
/*     */     
/*     */     public int nextDescriptor() {
/* 254 */       return this.length;
/*     */     }
/*     */     
/*     */     public void put(IoHandle handle) {
/* 258 */       int descriptor = handle.getDescriptor();
/* 259 */       if (descriptor == 0) {
/* 260 */         descriptor = nextDescriptor();
/* 261 */         handle.assign(descriptor);
/*     */       }
/* 263 */       int index = to_index(descriptor);
/* 264 */       this.handles[index] = handle;
/* 265 */       this.length = Math.max(++this.length, index + 1);
/*     */     }
/*     */     
/*     */     public void clear()
/*     */     {
/* 270 */       this.length = 0;
/* 271 */       Arrays.fill(this.handles, null);
/*     */     }
/*     */     
/*     */     public void remove(IoHandle handle)
/*     */     {
/* 276 */       int index = to_index(handle.getDescriptor());
/* 277 */       this.handles[index] = null;
/* 278 */       for (int i = this.length; i >= 0; i--) {
/* 279 */         if (this.handles[i] != null) break;
/* 280 */         this.length = i;
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public FileProxy getFile(String path, boolean create)
/*     */     throws IOException
/*     */   {
/* 291 */     this.providers.getFile(path, create);
/* 292 */     return this.providers.getFile(path, create);
/*     */   }
/*     */   
/*     */   public IoSystem getIoSystem() {
/* 296 */     return this.ioSystem;
/*     */   }
/*     */ }


/* Location:              C:\Users\Joey\Downloads\codejar.jar!\com\emt\proteus\runtime32\io\IoSys.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */