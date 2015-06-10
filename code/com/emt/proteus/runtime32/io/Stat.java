/*     */ package com.emt.proteus.runtime32.io;
/*     */ 
/*     */ import com.emt.proteus.runtime32.memory.MainMemory;
/*     */ import com.emt.proteus.utils.ByteTools;
/*     */ import com.emt.proteus.utils.CStruct;
/*     */ import com.emt.proteus.utils.CStruct.CField;
/*     */ import com.emt.proteus.utils.Data;
/*     */ import java.io.File;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class Stat
/*     */ {
/*     */   public static final int S_IFIFO = 4096;
/*     */   public static final int S_IFCHR = 8192;
/*     */   public static final int S_IFDIR = 16384;
/*     */   public static final int S_IFBLK = 24576;
/*     */   public static final int S_IFREG = 32768;
/*     */   public static final int S_IFSOCK = 49152;
/*     */   public static final int S_IFLNK = 40960;
/*     */   public static final int S_IFMT = 61440;
/*     */   public static final int S_IRWXU = 448;
/*     */   public static final int S_IRUSR = 256;
/*     */   public static final int S_IWUSR = 128;
/*     */   public static final int S_IXUSR = 64;
/*     */   public static final int S_IRWXG = 56;
/*     */   public static final int S_IRGRP = 32;
/*     */   public static final int S_IWGRP = 16;
/*     */   public static final int S_IXGRP = 8;
/*     */   public static final int S_IRWXO = 7;
/*     */   public static final int S_IROTH = 4;
/*     */   public static final int S_IWOTH = 2;
/*     */   public static final int S_IXOTH = 1;
/*     */   public static final int CONSOLE_MODE_TTY = 8592;
/*     */   public static final int CONSOLE_MODE_PIPED = 4480;
/*     */   public static final int _INODE_32 = 12;
/*     */   public static final int _MODE_32 = 16;
/*     */   public static final int _NLINK_32 = 20;
/*     */   public static final int _UID_32 = 24;
/*     */   public static final int _GID_32 = 28;
/*     */   public static final int _SIZE_32 = 44;
/*     */   public static final int _BLOCK_SIZE_32 = 48;
/*     */   public static final int _BLOCK_COUNT_32 = 52;
/*     */   public static final int _ATIME_1_32 = 56;
/*     */   public static final int _ATIME_2_32 = 60;
/*     */   public static final int _MTIME_1_32 = 64;
/*     */   public static final int _MTIME_2_32 = 68;
/*     */   public static final int _CTIME_1_32 = 72;
/*     */   public static final int _CTIME_2_32 = 76;
/*     */   public static final int _ANON_1_32 = 80;
/*     */   public static final int _ANON_2_32 = 80;
/*     */   public static final int TOTAL_SIZE = 88;
/*     */   public static final int TERMINAL_DEVICE = 6;
/*     */   public static final int HARDWARE_DEVICE_MAJOR_MINOR = 2065;
/*     */   public static final int TERMINAL_RDEVICE = 34816;
/*     */   public static final int IO_BLOCK_SIZE = 4096;
/*     */   public static final int LARGE_IO_BLOCK_SIZE = 65536;
/*     */   private byte[] buffer;
/*     */   public static final int STAT64_DEV = 0;
/*     */   public static final int STAT64_MODE = 16;
/*     */   public static final int STAT64_LINK = 20;
/*     */   public static final int STAT64_UID = 24;
/*     */   public static final int STAT64_GID = 28;
/*     */   public static final int STAT64_RDEV = 32;
/*     */   public static final int STAT64_SIZE = 44;
/*     */   public static final int STAT64_BLOCK_SIZE = 52;
/*     */   public static final int STAT64_BLOCK_COUNT = 56;
/*     */   public static final int STAT64_ATIME = 64;
/*     */   public static final int STAT64_MTIME = 72;
/*     */   public static final int STAT64_CTIME = 80;
/*     */   public static final int STAT64_INODE = 88;
/*     */   public static final int STAT64_TERMINATOR = 92;
/*     */   public static final int STAT64_LENGTH = 96;
/*     */   
/*     */   public Stat()
/*     */   {
/* 118 */     this.buffer = new byte[88];
/*     */   }
/*     */   
/*     */   public void setINode(int inode) {
/* 122 */     ByteTools.storeI32(this.buffer, 12, inode);
/*     */   }
/*     */   
/*     */   public void setMode(int mode) {
/* 126 */     ByteTools.storeI32(this.buffer, 16, mode);
/*     */   }
/*     */   
/*     */   public void setNumberOfLinks(int count) {
/* 130 */     ByteTools.storeI32(this.buffer, 20, count);
/*     */   }
/*     */   
/*     */   public void setUid(int uid) {
/* 134 */     ByteTools.storeI32(this.buffer, 24, uid);
/*     */   }
/*     */   
/*     */   public void setGid(int gid) {
/* 138 */     ByteTools.storeI32(this.buffer, 28, gid);
/*     */   }
/*     */   
/*     */   public void setFileSize(int fileSize) {
/* 142 */     ByteTools.storeI32(this.buffer, 44, fileSize);
/* 143 */     ByteTools.storeI32(this.buffer, 52, 1 + fileSize / 512);
/* 144 */     ByteTools.storeI32(this.buffer, 48, 4096);
/*     */   }
/*     */   
/*     */   public void setFileSize(long fileSize) {
/* 148 */     setFileSize((int)fileSize);
/*     */   }
/*     */   
/*     */   public byte[] getBytes() {
/* 152 */     return this.buffer;
/*     */   }
/*     */   
/*     */   public void setLastModified(long last) {
/* 156 */     ByteTools.storeI64(this.buffer, 72, last);
/* 157 */     ByteTools.storeI64(this.buffer, 56, last);
/* 158 */     ByteTools.storeI64(this.buffer, 64, last);
/*     */   }
/*     */   
/*     */   public void setMode(boolean isFile, boolean isDirectory, boolean canRead, boolean canWrite, boolean canExecute) {
/* 162 */     int mode = getMode(isFile, isDirectory, canRead, canWrite, canExecute);
/* 163 */     setMode(mode);
/*     */   }
/*     */   
/*     */   private static int getMode(boolean isFile, boolean isDirectory, boolean canRead, boolean canWrite, boolean canExecute) {
/* 167 */     int mode = 0;
/* 168 */     if (isFile) {
/* 169 */       mode |= (isDirectory ? 16384 : 32768);
/*     */     } else {
/* 171 */       mode |= 0xC000;
/*     */     }
/* 173 */     if (canRead)
/* 174 */       mode |= 0x124;
/* 175 */     if (canWrite)
/* 176 */       mode |= 0x80;
/* 177 */     if (canExecute) {
/* 178 */       mode |= 0x49;
/*     */     }
/* 180 */     return mode;
/*     */   }
/*     */   
/*     */   private static int getINode(File file) {
/* 184 */     int inode = file.getAbsolutePath().hashCode();
/* 185 */     return inode;
/*     */   }
/*     */   
/*     */   public static Stat stat(String path)
/*     */   {
/* 190 */     return stat(new File(path));
/*     */   }
/*     */   
/*     */   public static Stat stat(File file) {
/* 194 */     Stat stat = new Stat();
/* 195 */     stat.setFileSize(file.length());
/* 196 */     stat.setNumberOfLinks(1);
/* 197 */     stat.setMode(true, false, file.canRead(), file.canWrite(), file.canExecute());
/* 198 */     int inode = file.getAbsolutePath().hashCode() & 0xFF;
/* 199 */     stat.setINode(inode);
/* 200 */     long last = file.lastModified();
/* 201 */     stat.setLastModified(last);
/* 202 */     return stat;
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
/*     */   public static int stat64(MainMemory mem, int stat64$, String path, long lastModified, long length, boolean file, boolean read, boolean write, boolean exec)
/*     */   {
/* 249 */     return stat64(mem, stat64$, 2065, getMode(file, !file, read, write, exec), path.hashCode(), length, lastModified, lastModified, lastModified);
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
/*     */   public static int stat64Terminal(MainMemory mem, int stat64$, boolean piped, long processStart, long currentTime)
/*     */   {
/* 288 */     return stat64(mem, stat64$, 6, 34816L, piped ? 4480 : 8592, 1, 1000, 1, 3, 0L, 1024L, 0L, processStart, currentTime, currentTime);
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
/*     */   public static int stat64(MainMemory mem, int stat64$, int device, int mode, int inode, long length, long created, long modified, long accessed)
/*     */   {
/* 311 */     int blockSize = length > 65536L ? 65536 : 4096;
/* 312 */     return stat64(mem, stat64$, device, 0L, mode, 1, 1000, 1000, inode, length, blockSize, (511L + length) / 512L, created, modified, accessed);
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
/*     */   public static int stat64(MainMemory mem, int stat64$, int device, long rdev, int mode, int link, int uid, int gid, int inode, long length, long blockSize, long blocksAllocated, long created, long modified, long accessed)
/*     */   {
/* 329 */     mem.memset(stat64$, 96, (byte)0);
/* 330 */     mem.setDoubleWord(stat64$ + 0, device);
/* 331 */     mem.setDoubleWord(stat64$ + 16, mode);
/* 332 */     mem.setDoubleWord(stat64$ + 20, link);
/* 333 */     mem.setDoubleWord(stat64$ + 24, uid);
/* 334 */     mem.setDoubleWord(stat64$ + 28, gid);
/* 335 */     mem.setQuadWord(stat64$ + 44, length);
/* 336 */     mem.setQuadWord(stat64$ + 32, rdev);
/* 337 */     mem.setQuadWord(stat64$ + 52, blockSize);
/* 338 */     mem.setQuadWord(stat64$ + 56, blocksAllocated);
/* 339 */     mem.setQuadWord(stat64$ + 64, accessed);
/* 340 */     mem.setQuadWord(stat64$ + 80, created);
/* 341 */     mem.setQuadWord(stat64$ + 72, modified);
/* 342 */     mem.setDoubleWord(stat64$ + 88, inode);
/* 343 */     mem.setDoubleWord(stat64$ + 92, 0);
/* 344 */     return 0;
/*     */   }
/*     */   
/*     */   public static int statfs64(MainMemory memory, int struct$) {
/* 348 */     Fs fs = new Fs(struct$, memory);
/* 349 */     fs.clear();
/* 350 */     Fs.f_type.set(fs, 61267L);
/* 351 */     Fs.f_bsize.set(fs, 4096L);
/* 352 */     Fs.f_blocks.set(fs, 345424L);
/* 353 */     Fs.f_bfree.set(fs, 4621708L);
/* 354 */     Fs.f_bavail.set(fs, 270336L);
/* 355 */     Fs.f_files.set(fs, 65536L);
/* 356 */     Fs.f_ffree.set(fs, 86016L);
/* 357 */     Fs.f_fsid.set(fs, 4096L);
/* 358 */     Fs.f_frsize.set(fs, 4096L);
/* 359 */     return 0;
/*     */   }
/*     */   
/*     */ 
/*     */   public static class Fs
/*     */     extends CStruct
/*     */   {
/*     */     public static final long ADFS_SUPER_MAGIC = 44533L;
/*     */     
/*     */     public static final long AFFS_SUPER_MAGIC = 44543L;
/*     */     
/*     */     public static final long BEFS_SUPER_MAGIC = 1111905073L;
/*     */     
/*     */     public static final long BFS_MAGIC = 464386766L;
/*     */     
/*     */     public static final long CIFS_MAGIC_NUMBER = -11317950L;
/*     */     
/*     */     public static final long CODA_SUPER_MAGIC = 1937076805L;
/*     */     
/*     */     public static final long COH_SUPER_MAGIC = 19920823L;
/*     */     
/*     */     public static final long CRAMFS_MAGIC = 684539205L;
/*     */     
/*     */     public static final long DEVFS_SUPER_MAGIC = 4979L;
/*     */     
/*     */     public static final long EFS_SUPER_MAGIC = 4278867L;
/*     */     
/*     */     public static final long EXT_SUPER_MAGIC = 4989L;
/*     */     
/*     */     public static final long EXT2_OLD_SUPER_MAGIC = 61265L;
/*     */     
/*     */     public static final long EXT2_SUPER_MAGIC = 61267L;
/*     */     
/*     */     public static final long EXT3_SUPER_MAGIC = 61267L;
/*     */     
/*     */     public static final long EXT4_SUPER_MAGIC = 61267L;
/*     */     
/*     */     public static final long HFS_SUPER_MAGIC = 16964L;
/*     */     
/*     */     public static final long HPFS_SUPER_MAGIC = -107616183L;
/*     */     
/*     */     public static final long HUGETLBFS_MAGIC = -1786488586L;
/*     */     
/*     */     public static final long ISOFS_SUPER_MAGIC = 38496L;
/*     */     
/*     */     public static final long JFFS2_SUPER_MAGIC = 29366L;
/*     */     
/*     */     public static final long JFS_SUPER_MAGIC = 827541066L;
/*     */     
/*     */     public static final long MINIX_SUPER_MAGIC = 4991L;
/*     */     
/*     */     public static final long MINIX_SUPER_MAGIC2 = 5007L;
/*     */     
/*     */     public static final long MINIX2_SUPER_MAGIC = 9320L;
/*     */     
/*     */     public static final long MINIX2_SUPER_MAGIC2 = 9336L;
/*     */     
/*     */     public static final long MSDOS_SUPER_MAGIC = 19780L;
/*     */     
/*     */     public static final long NCP_SUPER_MAGIC = 22092L;
/*     */     
/*     */     public static final long NFS_SUPER_MAGIC = 26985L;
/*     */     
/*     */     public static final long NTFS_SB_MAGIC = 1397118030L;
/*     */     
/*     */     public static final long OPENPROM_SUPER_MAGIC = 40865L;
/*     */     
/*     */     public static final long PROC_SUPER_MAGIC = 40864L;
/*     */     
/*     */     public static final long QNX4_SUPER_MAGIC = 47L;
/*     */     
/*     */     public static final long REISERFS_SUPER_MAGIC = 1382369651L;
/*     */     
/*     */     public static final long ROMFS_MAGIC = 29301L;
/*     */     
/*     */     public static final long SMB_SUPER_MAGIC = 20859L;
/*     */     
/*     */     public static final long SYSV2_SUPER_MAGIC = 19920822L;
/*     */     
/*     */     public static final long SYSV4_SUPER_MAGIC = 19920821L;
/*     */     
/*     */     public static final long TMPFS_MAGIC = 16914836L;
/*     */     
/*     */     public static final long UDF_SUPER_MAGIC = 352400198L;
/*     */     
/*     */     public static final long UFS_MAGIC = 72020L;
/*     */     
/*     */     public static final long USBDEVICE_SUPER_MAGIC = 40866L;
/*     */     
/*     */     public static final long VXFS_SUPER_MAGIC = -1526596363L;
/*     */     
/*     */     public static final long XENIX_SUPER_MAGIC = 19920820L;
/*     */     
/*     */     public static final long XFS_SUPER_MAGIC = 1481003842L;
/*     */     
/*     */     public static final long _XIAFS_SUPER_MAGIC = 19911021L;
/*     */     
/* 456 */     public static final CStruct.CField f_type = _integer(0, "f_type");
/*     */     
/* 458 */     public static final CStruct.CField f_bsize = _integer(f_type, "f_bsize");
/*     */     
/* 460 */     public static final CStruct.CField f_blocks = _long(f_bsize, "f_blocks");
/*     */     
/* 462 */     public static final CStruct.CField f_bfree = _long(f_blocks, "f_bfree");
/*     */     
/* 464 */     public static final CStruct.CField f_bavail = _long(f_bfree, "f_bavail");
/*     */     
/* 466 */     public static final CStruct.CField f_files = _long(f_bavail, "f_files");
/*     */     
/* 468 */     public static final CStruct.CField f_ffree = _long(f_files, "f_ffree");
/*     */     
/* 470 */     public static final CStruct.CField f_fsid = _long(f_ffree, "f_fsid");
/*     */     
/* 472 */     public static final CStruct.CField f_namelen = _integer(f_fsid, "f_namelen");
/*     */     
/* 474 */     public static final CStruct.CField f_frsize = _long(f_namelen, "f_frsize");
/* 475 */     public static final CStruct.CField f_spare = _pointerArray(f_frsize, "f_spare", 5);
/*     */     
/*     */     public Fs(int base, Data data) {
/* 478 */       super(f_spare, data);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Joey\Downloads\codejar.jar!\com\emt\proteus\runtime32\io\Stat.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */