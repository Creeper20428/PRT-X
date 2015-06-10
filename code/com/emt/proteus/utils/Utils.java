/*     */ package com.emt.proteus.utils;
/*     */ 
/*     */ import java.awt.Font;
/*     */ import java.awt.Image;
/*     */ import java.awt.image.ImageObserver;
/*     */ import java.io.ByteArrayInputStream;
/*     */ import java.io.ByteArrayOutputStream;
/*     */ import java.io.Closeable;
/*     */ import java.io.File;
/*     */ import java.io.FileInputStream;
/*     */ import java.io.FileOutputStream;
/*     */ import java.io.FileWriter;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.InputStreamReader;
/*     */ import java.io.OutputStream;
/*     */ import java.io.PrintStream;
/*     */ import java.io.Reader;
/*     */ import java.io.Writer;
/*     */ import java.lang.reflect.Field;
/*     */ import java.lang.reflect.Modifier;
/*     */ import java.net.URI;
/*     */ import java.net.URL;
/*     */ import java.net.URLEncoder;
/*     */ import java.nio.channels.FileChannel;
/*     */ import java.nio.charset.Charset;
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.Enumeration;
/*     */ import java.util.Iterator;
/*     */ import java.util.LinkedHashMap;
/*     */ import java.util.LinkedHashSet;
/*     */ import java.util.LinkedList;
/*     */ import java.util.Map;
/*     */ import java.util.Properties;
/*     */ import java.util.Set;
/*     */ import java.util.logging.LogManager;
/*     */ import java.util.logging.Logger;
/*     */ import java.util.zip.GZIPInputStream;
/*     */ import java.util.zip.GZIPOutputStream;
/*     */ import java.util.zip.ZipEntry;
/*     */ import java.util.zip.ZipInputStream;
/*     */ import java.util.zip.ZipOutputStream;
/*     */ import javax.swing.Icon;
/*     */ import javax.swing.ImageIcon;
/*     */ import javax.swing.JFrame;
/*     */ import javax.swing.JOptionPane;
/*     */ import javax.swing.JScrollPane;
/*     */ import javax.swing.JTextArea;
/*     */ 
/*     */ public class Utils
/*     */ {
/*     */   public static final int BUFFER_SIZE = 81920;
/*     */   private static String frameImage;
/*  55 */   private static char[] PADDING = "                                                                                                                                                                                                                                                                                                                                                                                                                                            ".toCharArray();
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  63 */   public static final long START = System.currentTimeMillis();
/*  64 */   private static final ImageObserver OBSERVER = new ImageObserver() {
/*     */     public boolean imageUpdate(Image img, int infoflags, int x, int y, int width, int height) {
/*  66 */       return false;
/*     */     }
/*     */   };
/*  69 */   public static final Charset ASCII = Charset.forName("US-ASCII");
/*     */   
/*     */ 
/*  72 */   public static final Integer ZERO = Integer.valueOf(0);
/*  73 */   public static final Integer ONE = Integer.valueOf(1);
/*  74 */   public static final Integer MINUS_ONE = Integer.valueOf(-1);
/*  75 */   private static final Logger BOOT_LOGGER = Logger.getLogger("BOOT");
/*  76 */   public static final Font MONOSPACED = new Font("Courier", 0, 11);
/*  77 */   public static final Font LUCIDA_1 = new Font("Lucida Console", 0, 8);
/*  78 */   public static final Font LUCIDA_2 = new Font("Lucida Console", 0, 10);
/*  79 */   public static final Font LUCIDA_3 = new Font("Lucida Console", 0, 12);
/*     */   
/*  81 */   private static char[] HEX_CHARS = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F' };
/*     */   
/*     */ 
/*     */   public static boolean areEqual(Object one, Object two)
/*     */   {
/*  86 */     return (one == two) || ((one != null) && (two != null) && (one.equals(two)));
/*     */   }
/*     */   
/*     */   public static void setOsLookAndFeel()
/*     */   {
/*  91 */     System.out.println("Warning - bypassed request to set system look and feel");
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static void setLookAndFeel(String name)
/*     */   {
/* 103 */     System.out.println("Warning - bypassed request to set look and feel '" + name + "'");
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
/*     */   public static JScrollPane createTextComponent(String text)
/*     */   {
/* 125 */     JTextArea view = new JTextArea(text);
/* 126 */     view.setFont(MONOSPACED);
/* 127 */     return new JScrollPane(view);
/*     */   }
/*     */   
/*     */   public static void boot() {
/* 131 */     loadLogConfiguration();
/* 132 */     setOsLookAndFeel();
/*     */   }
/*     */   
/*     */   public static void loadLogConfiguration() {
/* 136 */     InputStream in = ClassLoader.getSystemResourceAsStream("logging.properties");
/* 137 */     if (in != null) {
/*     */       try {
/* 139 */         LogManager.getLogManager().readConfiguration(in);
/* 140 */         BOOT_LOGGER.fine("Initialised logging");
/*     */       } catch (IOException ioe) {
/* 142 */         ioe.printStackTrace();
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public static byte[] toAsciiBytes(String str)
/*     */   {
/* 149 */     int len = str.length();
/* 150 */     byte[] array = new byte[len];
/* 151 */     for (int i = 0; i != len; i++) {
/* 152 */       array[i] = ((byte)str.charAt(i));
/*     */     }
/* 154 */     return array;
/*     */   }
/*     */   
/*     */   public static String toNativeString(byte[] bytes) {
/* 158 */     return toNativeString(bytes, 0, bytes.length);
/*     */   }
/*     */   
/*     */   public static String toNativeString(byte[] bytes, int offset, int len)
/*     */   {
/* 163 */     char[] chrs = new char[len];
/* 164 */     for (int i = 0; i < len; i++) {
/* 165 */       int c = bytes[(offset++)] & 0xFF;
/* 166 */       chrs[i] = ((char)c);
/*     */     }
/* 168 */     return new String(chrs);
/*     */   }
/*     */   
/*     */   public static String toHexString(byte[] bytes, int offset, int len) {
/* 172 */     StringBuilder b = new StringBuilder();
/* 173 */     appendHex(bytes, offset, len, b);
/* 174 */     return b.toString();
/*     */   }
/*     */   
/*     */   public static StringBuilder appendHex(byte[] bytes, int offset, int len, StringBuilder b) {
/* 178 */     int i = 0; for (int index = offset; i < len; index++) {
/* 179 */       int val = bytes[index] & 0xFF;
/* 180 */       int digit = val >> 4;
/* 181 */       b.append(HEX_CHARS[(val >> 4)]);
/* 182 */       b.append(HEX_CHARS[(val & 0xF)]);
/* 183 */       if (i % 40 == 39) {
/* 184 */         b.append('\n');
/*     */       } else {
/* 186 */         b.append(' ');
/*     */       }
/* 178 */       i++;
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
/* 189 */     return b;
/*     */   }
/*     */   
/*     */   public static StringBuilder appendByte(int value, StringBuilder b) {
/* 193 */     int val = value & 0xFF;
/* 194 */     b.append(HEX_CHARS[(val >> 4)]);
/* 195 */     b.append(HEX_CHARS[(val & 0xF)]);
/* 196 */     return b;
/*     */   }
/*     */   
/* 199 */   public static StringBuilder appendInt(int value, StringBuilder b) { appendByte(value >> 24, b);
/* 200 */     appendByte(value >> 16, b);
/* 201 */     appendByte(value >> 8, b);
/* 202 */     appendByte(value, b);
/* 203 */     return b;
/*     */   }
/*     */   
/*     */   public static StringBuilder padding(int cars, StringBuilder b) {
/* 207 */     int len = Math.min(cars, PADDING.length);
/* 208 */     if (len > 0) b.append(PADDING, 0, len);
/* 209 */     return b;
/*     */   }
/*     */   
/* 212 */   public static String padding(int cars) { return padding(cars, new StringBuilder()).toString(); }
/*     */   
/*     */   public static URL resolve(URI base, String folder, String name) throws IOException
/*     */   {
/* 216 */     String path = folder + "/" + URLEncoder.encode(name, "UTF-8");
/* 217 */     return base.resolve(path).toURL();
/*     */   }
/*     */   
/*     */   public static void setImage(JFrame frame) {
/*     */     try {
/* 222 */       URL resource = Utils.class.getResource(frameImage);
/* 223 */       ImageIcon icon = new ImageIcon(resource);
/* 224 */       frame.setIconImage(icon.getImage());
/*     */     } catch (Exception e) {
/* 226 */       Logger.getLogger("GUI").info("Unable to set Image " + frameImage);
/*     */     }
/*     */   }
/*     */   
/*     */   public static String getFrameImage() {
/* 231 */     return frameImage;
/*     */   }
/*     */   
/*     */   public static void setFrameImage(String frameImage) {
/* 235 */     frameImage = frameImage;
/*     */   }
/*     */   
/*     */   public static int digitValue(char c) {
/* 239 */     switch (c) {
/*     */     case '0': 
/*     */     case '1': 
/*     */     case '2': 
/*     */     case '3': 
/*     */     case '4': 
/*     */     case '5': 
/*     */     case '6': 
/*     */     case '7': 
/*     */     case '8': 
/*     */     case '9': 
/* 250 */       return c - '0';
/*     */     }
/* 252 */     return -1;
/*     */   }
/*     */   
/*     */   public static int digitValue(char c, int base)
/*     */   {
/*     */     int v;
/* 258 */     switch (c) {
/*     */     case '0': 
/*     */     case '1': 
/*     */     case '2': 
/*     */     case '3': 
/*     */     case '4': 
/*     */     case '5': 
/*     */     case '6': 
/*     */     case '7': 
/*     */     case '8': 
/*     */     case '9': 
/* 269 */       return c - '0';
/*     */     case 'A': 
/*     */     case 'B': 
/*     */     case 'C': 
/*     */     case 'D': 
/*     */     case 'E': 
/*     */     case 'F': 
/*     */     case 'G': 
/*     */     case 'H': 
/*     */     case 'I': 
/*     */     case 'J': 
/*     */     case 'K': 
/*     */     case 'L': 
/*     */     case 'M': 
/*     */     case 'N': 
/*     */     case 'O': 
/*     */     case 'P': 
/*     */     case 'Q': 
/*     */     case 'R': 
/*     */     case 'S': 
/*     */     case 'T': 
/*     */     case 'U': 
/*     */     case 'V': 
/*     */     case 'W': 
/*     */     case 'X': 
/*     */     case 'Y': 
/*     */     case 'Z': 
/* 296 */       v = c + '\n' - 65;
/* 297 */       return v < base ? v : -1;
/*     */     case 'a': 
/*     */     case 'b': 
/*     */     case 'c': 
/*     */     case 'd': 
/*     */     case 'e': 
/*     */     case 'f': 
/*     */     case 'g': 
/*     */     case 'h': 
/*     */     case 'i': 
/*     */     case 'j': 
/*     */     case 'k': 
/*     */     case 'l': 
/*     */     case 'm': 
/*     */     case 'n': 
/*     */     case 'o': 
/*     */     case 'p': 
/*     */     case 'q': 
/*     */     case 'r': 
/*     */     case 's': 
/*     */     case 't': 
/*     */     case 'u': 
/*     */     case 'v': 
/*     */     case 'w': 
/*     */     case 'x': 
/*     */     case 'y': 
/*     */     case 'z': 
/* 324 */       v = c + '\n' - 97;
/* 325 */       return v < base ? v : -1;
/*     */     }
/* 327 */     return -1;
/*     */   }
/*     */   
/*     */   public static int hexDigitValue(char c)
/*     */   {
/* 332 */     switch (c) {
/*     */     case '0': 
/*     */     case '1': 
/*     */     case '2': 
/*     */     case '3': 
/*     */     case '4': 
/*     */     case '5': 
/*     */     case '6': 
/*     */     case '7': 
/*     */     case '8': 
/*     */     case '9': 
/* 343 */       return c - '0';
/*     */     case 'A': 
/*     */     case 'B': 
/*     */     case 'C': 
/*     */     case 'D': 
/*     */     case 'E': 
/*     */     case 'F': 
/* 350 */       return c + '\n' - 65;
/*     */     case 'a': 
/*     */     case 'b': 
/*     */     case 'c': 
/*     */     case 'd': 
/*     */     case 'e': 
/*     */     case 'f': 
/* 357 */       return c + '\n' - 97;
/*     */     }
/* 359 */     return -1;
/*     */   }
/*     */   
/*     */ 
/*     */   public static String truncate(String s, int max_len)
/*     */   {
/* 365 */     if (s.length() >= max_len) {
/* 366 */       return s.substring(0, max_len);
/*     */     }
/* 368 */     return s;
/*     */   }
/*     */   
/*     */   public static Properties load_properties(String resource) {
/* 372 */     Properties tmp = new Properties();
/*     */     try {
/* 374 */       Enumeration e = Thread.currentThread().getContextClassLoader().getResources(resource);
/* 375 */       while (e.hasMoreElements()) {
/* 376 */         URL url = (URL)e.nextElement();
/*     */         try {
/* 378 */           tmp.load(url.openStream());
/*     */         } catch (Exception ex) {
/* 380 */           System.err.println("Failed to load:" + url);
/*     */         }
/*     */       }
/*     */     } catch (IOException e) {
/* 384 */       e.printStackTrace();
/*     */     }
/* 386 */     return tmp;
/*     */   }
/*     */   
/*     */   public static URL getResourceUrl(String resourceName) {
/* 390 */     ClassLoader contextClassLoader = Thread.currentThread().getContextClassLoader();
/* 391 */     URL resource = contextClassLoader.getResource(resourceName);
/* 392 */     if (resource == null) {
/* 393 */       resource = Utils.class.getResource(resourceName);
/* 394 */       if (resource == null) {
/* 395 */         resource = Utils.class.getResource(resourceName);
/*     */       }
/*     */     }
/* 398 */     return resource;
/*     */   }
/*     */   
/*     */   public static InputStream getResource(String resourceName) {
/* 402 */     ClassLoader contextClassLoader = Thread.currentThread().getContextClassLoader();
/* 403 */     InputStream resource = contextClassLoader.getResourceAsStream(resourceName);
/* 404 */     if (resource == null) {
/* 405 */       resource = Utils.class.getResourceAsStream(resourceName);
/* 406 */       if (resource == null) {
/* 407 */         resource = Utils.class.getResourceAsStream(resourceName);
/*     */       }
/*     */     }
/* 410 */     return resource;
/*     */   }
/*     */   
/*     */   public static void showThrowable(Throwable e) {
/* 414 */     StackTraceElement[] stackTrace = e.getStackTrace();
/* 415 */     StringBuilder b = new StringBuilder();
/* 416 */     for (int i = 0; i < stackTrace.length; i++) {
/* 417 */       StackTraceElement stackTraceElement = stackTrace[i];
/* 418 */       b.append(stackTraceElement.toString()).append("\n");
/*     */     }
/* 420 */     JOptionPane.showMessageDialog(null, b.toString());
/*     */   }
/*     */   
/*     */   public static Object instanceOrNull(String clazz) {
/*     */     try {
/* 425 */       Class aClass = Thread.currentThread().getContextClassLoader().loadClass(clazz);
/*     */       
/* 427 */       return aClass.newInstance();
/*     */     } catch (Exception e) {
/* 429 */       e.printStackTrace();
/*     */       try
/*     */       {
/* 432 */         Class aClass = Class.forName(clazz);
/*     */         
/* 434 */         return aClass.newInstance();
/*     */       } catch (Exception e) {
/* 436 */         e.printStackTrace();
/*     */       } }
/* 438 */     return null;
/*     */   }
/*     */   
/*     */   public static int readFully(InputStream input, byte[] dest) throws IOException
/*     */   {
/* 443 */     int read = 0;
/* 444 */     int length = dest.length;
/* 445 */     int offset = 0;
/* 446 */     while (length > 0) {
/* 447 */       int amount = input.read(dest, offset, length);
/* 448 */       if (amount < 0) return read;
/* 449 */       read += amount;
/* 450 */       length -= amount;
/* 451 */       offset += amount;
/*     */     }
/* 453 */     return read;
/*     */   }
/*     */   
/*     */   public static int[] toIntArrayLe(byte[] dest, int offset, int byteLength) {
/* 457 */     int length = byteLength >> 2;
/* 458 */     int[] ints = new int[length];
/* 459 */     assert ((offset & 0x3) == 0);
/* 460 */     int i = 0; for (int pos = offset; i < length; pos += 4) {
/* 461 */       ints[i] = ByteTools.loadI32(dest, pos);i++;
/*     */     }
/* 463 */     return ints;
/*     */   }
/*     */   
/*     */   public static Icon getIcon(String s)
/*     */   {
/* 468 */     URL url = Thread.currentThread().getContextClassLoader().getResource(s);
/* 469 */     ImageIcon imageIcon = new ImageIcon(url);
/* 470 */     return imageIcon;
/*     */   }
/*     */   
/*     */   public static String formatPartial(String file, int count) {
/* 474 */     return String.format("%s.%05d.gz", new Object[] { file, Integer.valueOf(count) });
/*     */   }
/*     */   
/*     */   public static long parseHex(String s) {
/* 478 */     int length = s.length();
/* 479 */     if (length > 16) throw new IllegalArgumentException("String too long " + s);
/* 480 */     long val = 0L;
/* 481 */     for (int i = 0; i < length; i++) {
/* 482 */       char c = s.charAt(i);
/* 483 */       long digit = hexDigitValue(c);
/* 484 */       if (digit < 0L) throw new IllegalArgumentException("Invalid digit " + c);
/* 485 */       val = val << 4 | digit;
/*     */     }
/* 487 */     String h = Long.toHexString(val);
/*     */     
/* 489 */     if (!h.toUpperCase().equals(s.toUpperCase()))
/* 490 */       throw new NumberFormatException(s + " inconsistent parsing :" + h);
/* 491 */     return val;
/*     */   }
/*     */   
/* 494 */   public static boolean any(int flags, int mask) { return (flags & mask) != 0; }
/*     */   
/*     */   public static boolean all(int flags, int mask)
/*     */   {
/* 498 */     return (flags & mask) == mask;
/*     */   }
/*     */   
/*     */   public static boolean none(int flags, int mask) {
/* 502 */     return (flags & mask) == 0;
/*     */   }
/*     */   
/*     */   public static double toDouble(float i)
/*     */   {
/* 507 */     return i;
/*     */   }
/*     */   
/*     */   public static void copy(File from, File to) throws IOException
/*     */   {
/* 512 */     FileChannel channel = new FileInputStream(from).getChannel();
/* 513 */     FileChannel out = new FileOutputStream(to).getChannel();
/*     */     try {
/* 515 */       long length = from.length();
/* 516 */       long dest = 0L;
/* 517 */       long amount = length;
/* 518 */       while (amount > 0L) {
/* 519 */         long tranferred = channel.transferTo(dest, amount, out);
/* 520 */         amount -= tranferred;
/* 521 */         dest += tranferred;
/*     */       }
/*     */     } finally {
/* 524 */       channel.close();
/* 525 */       out.close();
/*     */     }
/*     */   }
/*     */   
/*     */   /* Error */
/*     */   public static long copy(InputStream from, OutputStream to, boolean closeInput, boolean closeOutput)
/*     */     throws IOException
/*     */   {
/*     */     // Byte code:
/*     */     //   0: ldc 113
/*     */     //   2: newarray <illegal type>
/*     */     //   4: astore 4
/*     */     //   6: lconst_0
/*     */     //   7: lstore 5
/*     */     //   9: aload_0
/*     */     //   10: aload 4
/*     */     //   12: invokevirtual 114	java/io/InputStream:read	([B)I
/*     */     //   15: istore 7
/*     */     //   17: iload 7
/*     */     //   19: ifge +30 -> 49
/*     */     //   22: lload 5
/*     */     //   24: lstore 8
/*     */     //   26: iload_2
/*     */     //   27: ifeq +7 -> 34
/*     */     //   30: aload_0
/*     */     //   31: invokestatic 115	com/emt/proteus/utils/Utils:close	(Ljava/io/Closeable;)V
/*     */     //   34: aload_1
/*     */     //   35: invokevirtual 116	java/io/OutputStream:flush	()V
/*     */     //   38: iload_3
/*     */     //   39: ifeq +7 -> 46
/*     */     //   42: aload_1
/*     */     //   43: invokestatic 115	com/emt/proteus/utils/Utils:close	(Ljava/io/Closeable;)V
/*     */     //   46: lload 8
/*     */     //   48: lreturn
/*     */     //   49: iload 7
/*     */     //   51: ifle +20 -> 71
/*     */     //   54: aload_1
/*     */     //   55: aload 4
/*     */     //   57: iconst_0
/*     */     //   58: iload 7
/*     */     //   60: invokevirtual 117	java/io/OutputStream:write	([BII)V
/*     */     //   63: lload 5
/*     */     //   65: iload 7
/*     */     //   67: i2l
/*     */     //   68: ladd
/*     */     //   69: lstore 5
/*     */     //   71: goto -62 -> 9
/*     */     //   74: astore 10
/*     */     //   76: iload_2
/*     */     //   77: ifeq +7 -> 84
/*     */     //   80: aload_0
/*     */     //   81: invokestatic 115	com/emt/proteus/utils/Utils:close	(Ljava/io/Closeable;)V
/*     */     //   84: aload_1
/*     */     //   85: invokevirtual 116	java/io/OutputStream:flush	()V
/*     */     //   88: iload_3
/*     */     //   89: ifeq +7 -> 96
/*     */     //   92: aload_1
/*     */     //   93: invokestatic 115	com/emt/proteus/utils/Utils:close	(Ljava/io/Closeable;)V
/*     */     //   96: aload 10
/*     */     //   98: athrow
/*     */     // Line number table:
/*     */     //   Java source line #529	-> byte code offset #0
/*     */     //   Java source line #531	-> byte code offset #6
/*     */     //   Java source line #533	-> byte code offset #9
/*     */     //   Java source line #534	-> byte code offset #17
/*     */     //   Java source line #541	-> byte code offset #26
/*     */     //   Java source line #542	-> byte code offset #34
/*     */     //   Java source line #543	-> byte code offset #38
/*     */     //   Java source line #535	-> byte code offset #49
/*     */     //   Java source line #536	-> byte code offset #54
/*     */     //   Java source line #537	-> byte code offset #63
/*     */     //   Java source line #539	-> byte code offset #71
/*     */     //   Java source line #541	-> byte code offset #74
/*     */     //   Java source line #542	-> byte code offset #84
/*     */     //   Java source line #543	-> byte code offset #88
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	signature
/*     */     //   0	99	0	from	InputStream
/*     */     //   0	99	1	to	OutputStream
/*     */     //   0	99	2	closeInput	boolean
/*     */     //   0	99	3	closeOutput	boolean
/*     */     //   4	52	4	buffer	byte[]
/*     */     //   7	63	5	length	long
/*     */     //   15	51	7	read	int
/*     */     //   24	23	8	l1	long
/*     */     //   74	23	10	localObject	Object
/*     */     // Exception table:
/*     */     //   from	to	target	type
/*     */     //   6	26	74	finally
/*     */     //   49	76	74	finally
/*     */   }
/*     */   
/*     */   /* Error */
/*     */   public static byte[] load(InputStream input)
/*     */     throws IOException
/*     */   {
/*     */     // Byte code:
/*     */     //   0: new 118	java/io/ByteArrayOutputStream
/*     */     //   3: dup
/*     */     //   4: invokespecial 119	java/io/ByteArrayOutputStream:<init>	()V
/*     */     //   7: astore_1
/*     */     //   8: ldc 113
/*     */     //   10: newarray <illegal type>
/*     */     //   12: astore_2
/*     */     //   13: aload_0
/*     */     //   14: aload_2
/*     */     //   15: invokevirtual 114	java/io/InputStream:read	([B)I
/*     */     //   18: istore_3
/*     */     //   19: iload_3
/*     */     //   20: ifge +16 -> 36
/*     */     //   23: aload_1
/*     */     //   24: invokevirtual 120	java/io/ByteArrayOutputStream:toByteArray	()[B
/*     */     //   27: astore 4
/*     */     //   29: aload_0
/*     */     //   30: invokevirtual 121	java/io/InputStream:close	()V
/*     */     //   33: aload 4
/*     */     //   35: areturn
/*     */     //   36: aload_1
/*     */     //   37: aload_2
/*     */     //   38: iconst_0
/*     */     //   39: iload_3
/*     */     //   40: invokevirtual 122	java/io/ByteArrayOutputStream:write	([BII)V
/*     */     //   43: goto -30 -> 13
/*     */     //   46: astore 5
/*     */     //   48: aload_0
/*     */     //   49: invokevirtual 121	java/io/InputStream:close	()V
/*     */     //   52: aload 5
/*     */     //   54: athrow
/*     */     // Line number table:
/*     */     //   Java source line #548	-> byte code offset #0
/*     */     //   Java source line #549	-> byte code offset #8
/*     */     //   Java source line #552	-> byte code offset #13
/*     */     //   Java source line #553	-> byte code offset #19
/*     */     //   Java source line #554	-> byte code offset #23
/*     */     //   Java source line #559	-> byte code offset #29
/*     */     //   Java source line #556	-> byte code offset #36
/*     */     //   Java source line #557	-> byte code offset #43
/*     */     //   Java source line #559	-> byte code offset #46
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	signature
/*     */     //   0	55	0	input	InputStream
/*     */     //   7	30	1	baos	ByteArrayOutputStream
/*     */     //   12	26	2	buf	byte[]
/*     */     //   18	22	3	amount	int
/*     */     //   27	7	4	arrayOfByte1	byte[]
/*     */     //   46	7	5	localObject	Object
/*     */     // Exception table:
/*     */     //   from	to	target	type
/*     */     //   13	29	46	finally
/*     */     //   36	48	46	finally
/*     */   }
/*     */   
/*     */   public static void save(OutputStream out, byte[] data)
/*     */     throws IOException
/*     */   {
/*     */     try
/*     */     {
/* 565 */       out.write(data);
/* 566 */       out.close();
/*     */     } finally {
/* 568 */       if (out != null) out.close();
/*     */     }
/*     */   }
/*     */   
/*     */   public static void save(String fileName, String content)
/*     */   {
/*     */     try {
/* 575 */       Writer writer = new FileWriter(fileName);
/* 576 */       writer.write(content);
/* 577 */       writer.close();
/*     */     } catch (IOException e) {
/* 579 */       System.err.println("Unable to save " + fileName);
/*     */     }
/*     */   }
/*     */   
/*     */   public static byte[] read(File file) throws IOException {
/* 584 */     return getBytes(new FileInputStream(file));
/*     */   }
/*     */   
/*     */   public static void save(File file, byte[] bytes) throws IOException
/*     */   {
/* 589 */     FileOutputStream output = null;
/*     */     try {
/* 591 */       output = new FileOutputStream(file, false);
/* 592 */       output.write(bytes);
/*     */     } finally {
/* 594 */       close(output);
/*     */     }
/*     */   }
/*     */   
/*     */   public static byte[] getBytes(InputStream inputStream)
/*     */     throws IOException
/*     */   {
/* 601 */     ByteArrayOutputStream bais = new ByteArrayOutputStream();
/* 602 */     byte[] buf = new byte[81920];
/* 603 */     int amount = 0;
/*     */     try {
/* 605 */       while ((amount = inputStream.read(buf, 0, buf.length)) >= 0) {
/* 606 */         bais.write(buf, 0, amount);
/*     */       }
/* 608 */       return bais.toByteArray();
/*     */     } finally {
/* 610 */       close(inputStream);
/*     */     }
/*     */   }
/*     */   
/*     */   public static String getString(InputStream inputStream)
/*     */     throws IOException
/*     */   {
/* 617 */     InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
/* 618 */     return getString(inputStreamReader);
/*     */   }
/*     */   
/*     */   public static String getString(Reader inputStreamReader) throws IOException
/*     */   {
/* 623 */     StringBuilder b = new StringBuilder();
/* 624 */     char[] buf = new char[81920];
/* 625 */     int amount = 0;
/*     */     try {
/* 627 */       while ((amount = inputStreamReader.read(buf, 0, buf.length)) >= 0) {
/* 628 */         b.append(buf, 0, amount);
/*     */       }
/* 630 */       return b.toString();
/*     */     } finally {
/* 632 */       close(inputStreamReader);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/* 637 */   public static String getExtension(File path) { return getExtension(path.getName()); }
/*     */   
/*     */   public static String getExtension(String path) {
/* 640 */     int idx = path.lastIndexOf('.');
/* 641 */     String ext = idx < 0 ? "" : path.substring(idx + 1).toLowerCase();
/* 642 */     return ext;
/*     */   }
/*     */   
/*     */   public static void close(Closeable closeable) {
/*     */     try {
/* 647 */       if (closeable != null) closeable.close();
/*     */     } catch (IOException e) {
/* 649 */       e.printStackTrace();
/*     */     }
/*     */   }
/*     */   
/*     */   public static byte[] getGZBytes(byte[] src) throws IOException
/*     */   {
/* 655 */     ByteArrayInputStream bais = new ByteArrayInputStream(src);
/* 656 */     GZIPInputStream input = new GZIPInputStream(bais);
/* 657 */     return getBytes(input);
/*     */   }
/*     */   
/*     */   public static byte[] toGZBytes(byte[] src) throws IOException {
/* 661 */     ByteArrayOutputStream bais = new ByteArrayOutputStream(Math.max(8192, src.length >> 1));
/* 662 */     GZIPOutputStream outputStream = new GZIPOutputStream(bais);
/* 663 */     outputStream.write(src, 0, src.length);
/* 664 */     outputStream.close();
/* 665 */     return bais.toByteArray();
/*     */   }
/*     */   
/*     */   public static void addFilesWithExtension(Collection<File> files, File root, String extension) {
/* 669 */     extension = extension.toLowerCase();
/* 670 */     appendFileInternal(files, root, extension);
/*     */   }
/*     */   
/*     */   private static void appendFileInternal(Collection<File> files, File file, String extension) {
/* 674 */     if (file.isFile()) {
/* 675 */       String ext = getExtension(file.getName());
/* 676 */       if (ext.equals(extension)) {
/* 677 */         files.add(file.getAbsoluteFile());
/*     */       }
/* 679 */     } else if (file.isDirectory()) {
/* 680 */       File[] kids = file.listFiles();
/* 681 */       for (int i = 0; i < kids.length; i++) {
/* 682 */         File kid = kids[i];
/* 683 */         appendFileInternal(files, kid, extension);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/* 689 */   public static boolean cleanDirectory(File directory) { return cleanDirectory(directory, null); }
/*     */   
/*     */   public static boolean cleanDirectory(File directory, String extension) {
/* 692 */     extension = extension != null ? extension.toLowerCase() : null;
/*     */     
/* 694 */     LinkedList<File> retries = new LinkedList();
/* 695 */     cleanRecursive(directory, retries, extension);
/* 696 */     int attempts = 3;
/* 697 */     long millis = 500L;
/* 698 */     while ((attempts > 0) && (!retries.isEmpty())) {
/* 699 */       pause(millis);
/* 700 */       for (Iterator<File> iterator = retries.iterator(); iterator.hasNext();) {
/* 701 */         File next = (File)iterator.next();
/* 702 */         if ((!next.exists()) || (next.delete())) {
/* 703 */           retries.remove();
/*     */         }
/*     */       }
/* 706 */       attempts--;
/*     */     }
/* 708 */     if (!retries.isEmpty()) {
/* 709 */       return false;
/*     */     }
/* 711 */     return true;
/*     */   }
/*     */   
/*     */   private static void cleanRecursive(File file, Collection<File> retry, String extension)
/*     */   {
/* 716 */     if (file.isFile()) {
/* 717 */       String ext = getExtension(file.getName());
/* 718 */       if ((extension != null) && (!ext.equals(extension))) return;
/* 719 */       if (!file.delete()) retry.add(file);
/* 720 */     } else if (file.isDirectory()) {
/* 721 */       File[] kids = file.listFiles();
/* 722 */       for (int i = 0; i < kids.length; i++) {
/* 723 */         File kid = kids[i];
/* 724 */         cleanRecursive(kid, retry, extension);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public static void addContentsToZip(ZipOutputStream zip, File file, Filter accept) throws IOException {
/* 730 */     if (file.isFile()) {
/* 731 */       ZipInputStream zIn = new ZipInputStream(new FileInputStream(file));
/* 732 */       addToZip(zip, zIn, accept);
/* 733 */     } else if (file.isDirectory()) {
/* 734 */       File[] files = file.listFiles();
/* 735 */       for (int i = 0; i < files.length; i++) {
/* 736 */         File kid = files[i];
/* 737 */         addToZip(zip, kid, "", accept);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public static void addToZip(ZipOutputStream zip, File file, String prefix, Filter accept) throws IOException {
/* 743 */     String name = prefix + file.getName();
/* 744 */     if (file.isFile()) {
/* 745 */       if (accept.accept(name)) {
/* 746 */         ZipEntry entry = new ZipEntry(name);
/* 747 */         zip.putNextEntry(entry);
/* 748 */         copy(new FileInputStream(file), zip, true, false);
/* 749 */         zip.closeEntry();
/*     */       }
/* 751 */     } else if (file.isDirectory()) {
/* 752 */       File[] files = file.listFiles();
/* 753 */       for (int i = 0; i < files.length; i++) {
/* 754 */         File kid = files[i];
/* 755 */         addToZip(zip, kid, name + "/", accept);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public static void addToZip(ZipOutputStream zip, ZipInputStream input, Filter accept) throws IOException {
/*     */     try { ZipEntry nextEntry;
/* 762 */       while ((nextEntry = input.getNextEntry()) != null) {
/* 763 */         String name = nextEntry.getName();
/* 764 */         if (accept.accept(name)) {
/* 765 */           ZipEntry entry = new ZipEntry(nextEntry);
/* 766 */           zip.putNextEntry(entry);
/* 767 */           copy(input, zip, false, false);
/* 768 */           zip.closeEntry();
/*     */         }
/* 770 */         input.closeEntry();
/*     */       }
/*     */     } finally {
/* 773 */       close(input);
/*     */     }
/*     */   }
/*     */   
/*     */   private static void pause(long pause) {
/*     */     try {
/* 779 */       Thread.sleep(pause);
/*     */     } catch (InterruptedException e) {
/* 781 */       e.printStackTrace();
/*     */     }
/*     */   }
/*     */   
/*     */   public static String replaceExtension(String fileName, String newext) {
/* 786 */     String stem = getStem(fileName);
/* 787 */     return stem + "." + newext;
/*     */   }
/*     */   
/*     */   public static String getStem(String fileName) {
/* 791 */     int index = fileName.lastIndexOf('.');
/* 792 */     String stem = fileName;
/* 793 */     if (index > 0) {
/* 794 */       stem = fileName.substring(0, index);
/*     */     }
/* 796 */     return stem;
/*     */   }
/*     */   
/*     */   public static Map<Integer, String> createConstantMap(Class cls)
/*     */   {
/* 801 */     Map<Integer, String> intName = new LinkedHashMap();
/* 802 */     Field[] flds = cls.getDeclaredFields();
/*     */     
/* 804 */     for (int i = 0; i < flds.length; i++)
/*     */     {
/*     */       try
/*     */       {
/* 808 */         int mods = flds[i].getModifiers();
/* 809 */         if ((!Modifier.isStatic(mods)) || (!Modifier.isFinal(mods)) || (Modifier.isPublic(mods)))
/*     */         {
/*     */ 
/*     */ 
/* 813 */           Class type = flds[i].getType();
/* 814 */           int key; if ((type == Integer.TYPE) || (type == Short.TYPE) || (type == Byte.TYPE) || (type == Byte.TYPE))
/*     */           {
/*     */ 
/*     */ 
/*     */ 
/* 819 */             key = flds[i].getInt(null); } else {
/*     */             continue;
/*     */           }
/*     */           int key;
/* 823 */           String name = flds[i].getName();
/* 824 */           Integer val = Integer.valueOf(key);
/* 825 */           if (!intName.containsKey(val)) {
/* 826 */             intName.put(val, name);
/*     */           }
/*     */         }
/*     */       } catch (Exception e) {
/* 830 */         e.printStackTrace();
/*     */       }
/*     */     }
/* 833 */     return Collections.unmodifiableMap(intName);
/*     */   }
/*     */   
/*     */   public static int[] grow(int[] src)
/*     */   {
/* 838 */     int[] tmp = new int[src.length << 1];
/* 839 */     System.arraycopy(src, 0, tmp, 0, src.length);
/* 840 */     return tmp;
/*     */   }
/*     */   
/*     */   public static boolean inBounds(int test, int min, int max)
/*     */   {
/* 845 */     return (test <= max) && (test >= min);
/*     */   }
/*     */   
/*     */   public static int align(int pos, int align) {
/* 849 */     int aMinus1 = align - 1;
/* 850 */     return pos + aMinus1 & (aMinus1 ^ 0xFFFFFFFF);
/*     */   }
/*     */   
/*     */   public static String toOrBitString(Map<Integer, String> bits, int value) {
/* 854 */     StringBuilder b = new StringBuilder();
/*     */     for (;;) {
/* 856 */       int bit = Integer.lowestOneBit(value);
/* 857 */       if (bit == 0) return b.toString();
/* 858 */       if (b.length() != 0) b.append("| ");
/* 859 */       b.append((String)bits.get(Integer.valueOf(bit)));
/* 860 */       value &= (bit ^ 0xFFFFFFFF);
/*     */     }
/*     */   }
/*     */   
/*     */   public static byte[] toBytes(long value) {
/* 865 */     return new byte[] { (byte)(int)value, (byte)(int)(value >> 8), (byte)(int)(value >> 16), (byte)(int)(value >> 24), (byte)(int)(value >> 32), (byte)(int)(value >> 40), (byte)(int)(value >> 48), (byte)(int)(value >> 56) };
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static byte[] toBytes(int value)
/*     */   {
/* 877 */     return new byte[] { (byte)(value & 0xFF), (byte)(value >> 8), (byte)(value >> 16), (byte)(value >> 24) };
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public static byte[] toBytes(short value)
/*     */   {
/* 885 */     return new byte[] { (byte)(value & 0xFF), (byte)(value >> 8) };
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public static Set toSet(Object... element)
/*     */   {
/* 892 */     Set set = new LinkedHashSet();
/* 893 */     for (int i = 0; i < element.length; i++) {
/* 894 */       Object o = element[i];
/* 895 */       set.add(o);
/*     */     }
/* 897 */     return Collections.unmodifiableSet(set);
/*     */   }
/*     */   
/*     */   public static MapBuilder map(Map target)
/*     */   {
/* 902 */     return new MapBuilder(target);
/*     */   }
/*     */   
/*     */   public static MapBuilder map() {
/* 906 */     return map(new LinkedHashMap());
/*     */   }
/*     */   
/*     */   public static class MapBuilder {
/*     */     private final Map target;
/*     */     
/*     */     public MapBuilder(Map target) {
/* 913 */       this.target = target;
/*     */     }
/*     */     
/*     */     public MapBuilder $(Object key, Object value) {
/* 917 */       this.target.put(key, value);
/* 918 */       return this;
/*     */     }
/*     */     
/*     */     public Map close(boolean immutable) {
/* 922 */       return immutable ? Collections.unmodifiableMap(this.target) : this.target;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Joey\Downloads\codejar.jar!\com\emt\proteus\utils\Utils.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */