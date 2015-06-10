/*     */ package com.emt.proteus.runtime32.io;
/*     */ 
/*     */ import com.emt.proteus.runtime32.api.FileProxy;
/*     */ import com.emt.proteus.runtime32.memory.MainMemory;
/*     */ import java.io.IOException;
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
/*     */ public class Streams
/*     */ {
/*     */   private static final int MEM_STREAM_RTTI = -16711680;
/*     */   private static final int MEM_OSTREAM_RTTI = -16711672;
/*     */   private static final int MEM_STRBUF_RTTI = -16711668;
/*     */   private static final int MEM_IOSBASE_RTTI = -16711628;
/*     */   private static final int FILE_RTTI = -16646144;
/*     */   private static final int FILE_OSTREAM_RTTI = -16646136;
/*     */   private static final int FILE_STRBUF_RTTI = -16646132;
/*     */   private static final int FILE_IOSBASE_RTTI = -16646092;
/*     */   private static final int IO_VTABLE_MEM_STREAM_START = 2304;
/*     */   private static final int IO_VTABLE_FILE_STREAM_START = 2560;
/*  29 */   private static final int[] FILESTREAM_VTABLE = { 0, 0, 0, 0, 0, 52, 0, -16646144, 0, 0, 0, 0, 0, 0, -8, -16646136, 0, 0, 0, 0, 0, 40, -12, -16646132, 0, 0, 0, 0, 0, 0, -52, -16646092 };
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  40 */   private static final int[] MEMSTREAM_VTABLE = { 0, 0, 0, 0, 0, 52, 0, -16711680, 0, 0, 0, 0, 0, 44, -8, -16711672, 0, 0, 0, 0, 0, 40, -12, -16711668, 0, 0, 0, 0, 0, 0, -52, -16711628 };
/*     */   
/*     */ 
/*     */   public static final int DEFAULT_FMT = 4224;
/*     */   
/*     */ 
/*     */   private static final int IO_VTABLE_FILE_STREAM = 2592;
/*     */   
/*     */ 
/*     */   private static final int IO_VTABLE_FILE_OSTREAM = 2624;
/*     */   
/*     */ 
/*     */   private static final int IO_VTABLE_FILE_BUF = 2656;
/*     */   
/*     */ 
/*     */   private static final int IO_VTABLE_FILE_IOSBASE = 2688;
/*     */   
/*     */   private static final int IO_VTABLE_MEM_STREAM = 2336;
/*     */   
/*     */   private static final int IO_VTABLE_MEM_OSTREAM = 2368;
/*     */   
/*     */   private static final int IO_VTABLE_MEM_STRINGBUF = 2368;
/*     */   
/*     */   private static final int IO_VTABLE_MEM_IOSBASE = 2384;
/*     */   
/*  65 */   private static int[] MEMSTREAM_IMAGE = { 2336, 0, 2368, 2368, 0, 0, 0, 0, 0, 0, 0, 0, 0, 2384, 0, 0, 4224, 0, 0, 0 };
/*     */   
/*     */ 
/*     */ 
/*  69 */   private static int[] FILESTREAM_IMAGE = { 2592, 0, 2624, 2656, 0, 0, 0, 0, 0, 0, 0, 0, 0, 2688, 0, 0, 4224, 0, 0, 0 };
/*     */   
/*     */   private IoSys ioSys;
/*     */   
/*     */   private MainMemory mem;
/*     */   
/*     */ 
/*     */   public Streams(IoSys ioSys, MainMemory mem)
/*     */   {
/*  78 */     this.ioSys = ioSys;
/*  79 */     this.mem = mem;
/*     */   }
/*     */   
/*     */   private void initStreamsSupport() {
/*  83 */     IoHandle console = this.ioSys.getConsole();
/*  84 */     IoHandle stderr = this.ioSys.getStdErr();
/*  85 */     this.mem.store(2560, FILESTREAM_VTABLE, FILESTREAM_VTABLE.length);
/*  86 */     this.mem.store(2304, MEMSTREAM_VTABLE, MEMSTREAM_VTABLE.length);
/*  87 */     initStream(1808, console, FILESTREAM_IMAGE);
/*  88 */     initStream(2048, stderr, FILESTREAM_IMAGE);
/*  89 */     setStreamDefaults(console);
/*  90 */     setStreamDefaults(stderr);
/*     */   }
/*     */   
/*     */   private void setStreamDefaults(IoHandle handle) {
/*  94 */     handle.storeFlags(4224, this.mem);
/*  95 */     handle.setFill(32);
/*  96 */     handle.setWidth(0);
/*  97 */     handle.setPrecision(6);
/*     */   }
/*     */   
/*     */   private int getHandleAddressFor(int stream_address)
/*     */   {
/* 102 */     int vttable = this.mem.getDoubleWord(stream_address);
/* 103 */     int topoffset = this.mem.getDoubleWord(vttable - 8);
/* 104 */     return stream_address + topoffset + 4;
/*     */   }
/*     */   
/*     */   private int getFormatAddressFor(int stream_address) {
/* 108 */     int vttable = this.mem.getDoubleWord(stream_address);
/* 109 */     int baseoffset = this.mem.getDoubleWord(vttable - 12);
/* 110 */     int fieldoffset = 12;
/* 111 */     return stream_address + baseoffset + fieldoffset;
/*     */   }
/*     */   
/*     */ 
/*     */   private void initStream(int stream, IoHandle ioHandle, int[] baseImage)
/*     */   {
/* 117 */     this.ioSys.registerHandle(ioHandle);
/* 118 */     this.mem.setDoubleWord(stream, ioHandle.getDescriptor());
/* 119 */     this.mem.store(stream, baseImage, baseImage.length);
/* 120 */     int fd = ioHandle.getDescriptor();
/* 121 */     int address = getHandleAddressFor(stream);
/* 122 */     this.mem.setDoubleWord(address, fd);
/* 123 */     ioHandle.setFormatAddress(getFormatAddressFor(stream));
/*     */   }
/*     */   
/*     */   public IoHandle openFileStream(int stream, String fileName, int openMode) {
/*     */     try {
/* 128 */       this.mem.setDoubleWord(stream, 0);
/*     */       
/* 130 */       boolean writeable = OpenMode.isWritable(openMode);
/* 131 */       FileProxy file = this.ioSys.getFile(fileName, writeable);
/* 132 */       if (file == null) return null;
/* 133 */       String mode = writeable ? "rw" : "r";
/*     */       
/*     */ 
/*     */ 
/* 137 */       IoHandle ioHandle = new ProxyIoHandle(this.ioSys, file, mode, 0);
/* 138 */       if (OpenMode.seekEnd(openMode)) {
/* 139 */         ioHandle.end();
/* 140 */       } else if (writeable) {
/* 141 */         ioHandle.truncate(0L);
/*     */       }
/* 143 */       int[] baseImage = FILESTREAM_IMAGE;
/* 144 */       initStream(stream, ioHandle, baseImage);
/* 145 */       return ioHandle;
/*     */     } catch (IOException e) {}
/* 147 */     return null;
/*     */   }
/*     */   
/*     */   public IoHandle createMemoryStream(int stream, byte[] content, int openMode)
/*     */   {
/*     */     try
/*     */     {
/* 154 */       boolean writeable = OpenMode.isWritable(openMode);
/* 155 */       String mode = writeable ? "rw" : "r";
/* 156 */       IoHandle ioHandle = new MemoryIoHandle(this.ioSys, "string", content, mode, 0, stream);
/* 157 */       if (OpenMode.seekEnd(openMode)) {
/* 158 */         ioHandle.end();
/* 159 */       } else if (writeable) {
/* 160 */         ioHandle.truncate(0L);
/*     */       }
/* 162 */       initStream(stream, ioHandle, MEMSTREAM_IMAGE);
/* 163 */       return ioHandle;
/*     */     } catch (IOException e) {}
/* 165 */     return null;
/*     */   }
/*     */   
/*     */   public IoHandle getStreamByAddress(int stream)
/*     */   {
/* 170 */     switch (stream) {
/*     */     case 2048: 
/* 172 */       return this.ioSys.getStdErr();
/*     */     case 1808: 
/* 174 */       return this.ioSys.getConsole();
/*     */     }
/* 176 */     int handleAddress = getHandleAddressFor(stream);
/* 177 */     if (handleAddress != 0) {
/* 178 */       int handleId = this.mem.getDoubleWord(handleAddress);
/* 179 */       if (handleId > 0) {
/* 180 */         return this.ioSys.getByDescriptor(handleId);
/*     */       }
/*     */     }
/* 183 */     System.out.printf(".......No Handle at %08X\n", new Object[] { Integer.valueOf(stream) });
/* 184 */     return null;
/*     */   }
/*     */ }


/* Location:              C:\Users\Joey\Downloads\codejar.jar!\com\emt\proteus\runtime32\io\Streams.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */