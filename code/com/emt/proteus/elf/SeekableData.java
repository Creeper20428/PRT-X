/*    */ package com.emt.proteus.elf;
/*    */ 
/*    */ import java.io.IOException;
/*    */ 
/*    */ public class SeekableData implements SeekableDataSource
/*    */ {
/*    */   private byte[] data;
/*    */   private final int offset;
/*    */   private final int length;
/*    */   private long position;
/*    */   
/*    */   public SeekableData(java.net.URL url) throws IOException {
/* 13 */     this(read(url));
/*    */   }
/*    */   
/*    */   public SeekableData(java.io.File f) throws IOException {
/* 17 */     this(read(f));
/*    */   }
/*    */   
/* 20 */   public SeekableData(java.io.InputStream f) throws IOException { this(read(f)); }
/*    */   
/*    */   public SeekableData(byte[] data)
/*    */   {
/* 24 */     this(data, 0, data.length);
/*    */   }
/*    */   
/*    */   public SeekableData(byte[] data, int offset, int len) {
/* 28 */     this.data = data;
/* 29 */     this.offset = offset;
/* 30 */     this.length = len;
/*    */   }
/*    */   
/*    */   public void close() {}
/*    */   
/*    */   public void seek(long pos) throws IOException
/*    */   {
/* 37 */     if ((pos < 0L) || (pos > this.length)) throw new IllegalArgumentException(String.format("Value of range %d ", new Object[] { Long.valueOf(pos) }));
/* 38 */     this.position = pos;
/*    */   }
/*    */   
/*    */   public long position() throws IOException {
/* 42 */     return this.position;
/*    */   }
/*    */   
/*    */   public void readFully(byte[] destination) throws IOException {
/* 46 */     readFully(destination, 0, destination.length);
/*    */   }
/*    */   
/*    */   public void readFully(byte[] destination, int offset, int length) throws IOException {
/* 50 */     int start = (int)(this.position + this.offset);
/* 51 */     if (this.position + length > this.length) throw new java.io.EOFException();
/* 52 */     System.arraycopy(this.data, start, destination, offset, destination.length);
/*    */   }
/*    */   
/*    */   public static byte[] read(java.net.URL url) throws IOException {
/* 56 */     return read(url.openStream());
/*    */   }
/*    */   
/* 59 */   public static byte[] read(java.io.File file) throws IOException { return read(new java.io.FileInputStream(file)); }
/*    */   
/*    */   /* Error */
/*    */   public static byte[] read(java.io.InputStream input)
/*    */     throws IOException
/*    */   {
/*    */     // Byte code:
/*    */     //   0: ldc 24
/*    */     //   2: newarray <illegal type>
/*    */     //   4: astore_1
/*    */     //   5: new 25	java/io/ByteArrayOutputStream
/*    */     //   8: dup
/*    */     //   9: invokespecial 26	java/io/ByteArrayOutputStream:<init>	()V
/*    */     //   12: astore_2
/*    */     //   13: aload_0
/*    */     //   14: aload_1
/*    */     //   15: invokevirtual 27	java/io/InputStream:read	([B)I
/*    */     //   18: istore_3
/*    */     //   19: iload_3
/*    */     //   20: ifge +20 -> 40
/*    */     //   23: aload_2
/*    */     //   24: invokevirtual 28	java/io/ByteArrayOutputStream:toByteArray	()[B
/*    */     //   27: astore 4
/*    */     //   29: aload_0
/*    */     //   30: invokevirtual 29	java/io/InputStream:close	()V
/*    */     //   33: aload_2
/*    */     //   34: invokevirtual 30	java/io/ByteArrayOutputStream:close	()V
/*    */     //   37: aload 4
/*    */     //   39: areturn
/*    */     //   40: iload_3
/*    */     //   41: ifle +10 -> 51
/*    */     //   44: aload_2
/*    */     //   45: aload_1
/*    */     //   46: iconst_0
/*    */     //   47: iload_3
/*    */     //   48: invokevirtual 31	java/io/ByteArrayOutputStream:write	([BII)V
/*    */     //   51: goto -38 -> 13
/*    */     //   54: astore 5
/*    */     //   56: aload_0
/*    */     //   57: invokevirtual 29	java/io/InputStream:close	()V
/*    */     //   60: aload_2
/*    */     //   61: invokevirtual 30	java/io/ByteArrayOutputStream:close	()V
/*    */     //   64: aload 5
/*    */     //   66: athrow
/*    */     // Line number table:
/*    */     //   Java source line #63	-> byte code offset #0
/*    */     //   Java source line #64	-> byte code offset #5
/*    */     //   Java source line #67	-> byte code offset #13
/*    */     //   Java source line #68	-> byte code offset #19
/*    */     //   Java source line #69	-> byte code offset #23
/*    */     //   Java source line #75	-> byte code offset #29
/*    */     //   Java source line #76	-> byte code offset #33
/*    */     //   Java source line #70	-> byte code offset #40
/*    */     //   Java source line #71	-> byte code offset #44
/*    */     //   Java source line #73	-> byte code offset #51
/*    */     //   Java source line #75	-> byte code offset #54
/*    */     //   Java source line #76	-> byte code offset #60
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	signature
/*    */     //   0	67	0	input	java.io.InputStream
/*    */     //   4	42	1	buf	byte[]
/*    */     //   12	49	2	baos	java.io.ByteArrayOutputStream
/*    */     //   18	30	3	n	int
/*    */     //   27	11	4	arrayOfByte1	byte[]
/*    */     //   54	11	5	localObject	Object
/*    */     // Exception table:
/*    */     //   from	to	target	type
/*    */     //   13	29	54	finally
/*    */     //   40	56	54	finally
/*    */   }
/*    */ }


/* Location:              C:\Users\Joey\Downloads\codejar.jar!\com\emt\proteus\elf\SeekableData.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */