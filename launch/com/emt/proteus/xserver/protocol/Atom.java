/*     */ package com.emt.proteus.xserver.protocol;
/*     */ 
/*     */ import com.emt.proteus.xserver.client.XClient;
/*     */ import com.emt.proteus.xserver.io.ComChannel;
/*     */ import java.io.IOException;
/*     */ import java.util.Hashtable;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class Atom
/*     */ {
/*  29 */   private static int lastAtom = 0;
/*     */   
/*  31 */   private static String[] idTable = { null, "PRIMARY", "SECONDARY", "ARC", "ATOM", "BITMAP", "CARDINAL", "COLORMAP", "CURSOR", "CUT_BUFFER0", "CUT_BUFFER1", "CUT_BUFFER2", "CUT_BUFFER3", "CUT_BUFFER4", "CUT_BUFFER5", "CUT_BUFFER6", "CUT_BUFFER7", "DRAWABLE", "FONT", "INTEGER", "PIXMAP", "POINT", "RECTANGLE", "RESOURCE_MANAGER", "RGB_COLOR_MAP", "RGB_BEST_MAP", "RGB_BLUE_MAP", "RGB_DEFAULT_MAP", "RGB_GRAY_MAP", "RGB_GREEN_MAP", "RGB_RED_MAP", "STRING", "VISUALID", "WINDOW", "WM_COMMAND", "WM_HINTS", "WM_CLIENT_MACHINE", "WM_ICON_NAME", "WM_ICON_SIZE", "WM_NAME", "WM_NORMAL_HINTS", "WM_SIZE_HINTS", "WM_ZOOM_HINTS", "MIN_SPACE", "NORM_SPACE", "MAX_SPACE", "END_SPACE", "SUPERSC.LPT_X", "SUPERSC.LPT_Y", "SUBSC.LPT_X", "SUBSC.LPT_Y", "UNDERLINE_POSITION", "UNDERLINE_THICKNESS", "STRIKEOUT_ASCENT", "STRIKEOUT_DESCENT", "ITALIC_ANGLE", "X_HEIGHT", "QUAD_WIDTH", "WEIGHT", "POINT_SIZE", "RESOLUTION", "COPYRIGHT", "NOTICE", "FONT_NAME", "FAMILY_NAME", "FULL_NAME", "CAP_HEIGHT", "WM_CLASS", "WM_TRANSIENT_FOR" };
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 103 */   private static final int lastPredefined = idTable.length - 1;
/*     */   
/* 105 */   static { String[] foo = new String[100];
/* 106 */     System.arraycopy(idTable, 0, foo, 0, idTable.length);
/* 107 */     lastAtom = idTable.length - 1;
/* 108 */     idTable = foo;
/*     */   }
/*     */   
/* 111 */   private static final Hashtable nameTable = nameTablePredefined();
/*     */   
/*     */   private static Hashtable nameTablePredefined() {
/* 114 */     Hashtable hash = new Hashtable();
/* 115 */     for (int i = 1; 
/* 116 */         idTable[i] != null; i++)
/*     */     {
/* 117 */       hash.put(idTable[i], new Integer(i));
/*     */     }
/*     */     
/*     */ 
/* 121 */     return hash;
/*     */   }
/*     */   
/*     */   public static int make(byte[] name, int start, int length, boolean makeit) {
/* 125 */     return make(new String(name, start, length), makeit);
/*     */   }
/*     */   
/* 128 */   public static int make(byte[] name, boolean makeit) { return make(new String(name), makeit); }
/*     */   
/*     */   public static synchronized int make(String name, boolean makeit)
/*     */   {
/*     */     int id;
/* 133 */     if ((id = find(name)) != 0) return id;
/* 134 */     if (!makeit) return 0;
/* 135 */     lastAtom += 1;
/*     */     
/* 137 */     if (idTable.length <= lastAtom)
/*     */     {
/* 139 */       String[] foo = new String[idTable.length * 2];
/* 140 */       System.arraycopy(idTable, 0, foo, 0, idTable.length);
/* 141 */       idTable = foo;
/*     */     }
/* 143 */     idTable[lastAtom] = name;
/* 144 */     nameTable.put(name, new Integer(lastAtom));
/* 145 */     return lastAtom;
/*     */   }
/*     */   
/* 148 */   public static boolean valid(int id) { return (id != 0) && (id <= lastAtom); }
/*     */   
/*     */   public static String find(int id) {
/* 151 */     if (id == 0) return null;
/* 152 */     if (id <= lastAtom) return idTable[id];
/* 153 */     return null;
/*     */   }
/*     */   
/*     */   public static int find(String name) {
/* 157 */     if (name == null) return 0;
/*     */     try {
/* 159 */       Integer i = (Integer)nameTable.get(name);
/* 160 */       return i.intValue();
/*     */     }
/*     */     catch (Exception e) {}
/*     */     
/* 164 */     return 0;
/*     */   }
/*     */   
/*     */   public static int find(byte[] name) {
/* 168 */     if (name == null) return 0;
/*     */     try {
/* 170 */       Integer i = (Integer)nameTable.get(new String(name));
/* 171 */       return i.intValue();
/*     */     }
/*     */     catch (Exception e) {}
/*     */     
/* 175 */     return 0;
/*     */   }
/*     */   
/*     */ 
/*     */   public static void reqInternAtom(XClient c)
/*     */     throws IOException
/*     */   {
/* 182 */     int n = 0;
/* 183 */     ComChannel comChannel = c.channel;
/*     */     
/* 185 */     int exp = c.data;
/* 186 */     n = comChannel.readShort();
/* 187 */     comChannel.readPad(2);
/* 188 */     c.length -= 2;
/* 189 */     if (n <= 0) {
/* 190 */       c.errorReason = 11;
/* 191 */       return;
/*     */     }
/* 193 */     byte[] bb = c.bbuffer;
/* 194 */     comChannel.readByte(bb, 0, n);
/* 195 */     comChannel.readPad(-n & 0x3);
/*     */     
/* 197 */     c.length = 0;
/* 198 */     int atm = make(bb, 0, n, exp == 0);
/*     */     
/* 200 */     synchronized (comChannel) {
/* 201 */       comChannel.writeByte(1);
/* 202 */       comChannel.writePad(1);
/* 203 */       comChannel.writeShort(c.getSequence());
/* 204 */       comChannel.writeInt(0);
/* 205 */       comChannel.writeInt(atm);
/* 206 */       comChannel.writePad(20);
/* 207 */       comChannel.flush();
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   public static void reqGetAtomName(XClient c)
/*     */     throws IOException
/*     */   {
/* 215 */     ComChannel comChannel = c.channel;
/* 216 */     int atom = comChannel.readInt();
/* 217 */     if (atom < 0) atom &= 0x7FFFFFFF;
/* 218 */     if (!valid(atom)) {
/* 219 */       synchronized (comChannel) {
/* 220 */         comChannel.writeByte(0);
/* 221 */         comChannel.writeByte(5);
/* 222 */         comChannel.writeShort(c.getSequence());
/* 223 */         comChannel.writeInt(atom);
/* 224 */         comChannel.writeShort(0);
/* 225 */         comChannel.writeByte(17);
/* 226 */         comChannel.writePad(21);
/* 227 */         comChannel.flush();
/*     */       }
/*     */     }
/*     */     else {
/* 231 */       String s = find(atom);
/* 232 */       synchronized (comChannel) {
/* 233 */         comChannel.writeByte(1);
/* 234 */         comChannel.writePad(1);
/* 235 */         comChannel.writeShort(c.getSequence());
/* 236 */         comChannel.writeInt((s.length() + 3) / 4);
/* 237 */         comChannel.writeShort(s.length());
/* 238 */         comChannel.writePad(22);
/* 239 */         comChannel.writeByte(s.getBytes());
/* 240 */         if (s.length() > 0) comChannel.writePad(-s.length() & 0x3);
/* 241 */         comChannel.flush();
/*     */       }
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Joey\Downloads\launch.jar!\com\emt\proteus\xserver\protocol\Atom.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */