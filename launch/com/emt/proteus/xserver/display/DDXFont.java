/*     */ package com.emt.proteus.xserver.display;
/*     */ 
/*     */ import com.emt.proteus.xserver.protocol.Atom;
/*     */ import java.awt.Font;
/*     */ import java.awt.FontMetrics;
/*     */ import java.awt.Toolkit;
/*     */ import java.io.UnsupportedEncodingException;
/*     */ import java.util.Enumeration;
/*     */ import java.util.Hashtable;
/*     */ import java.util.Vector;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ class DDXFont
/*     */ {
/*  29 */   static Hashtable table = new Hashtable();
/*     */   byte[] lfname;
/*     */   
/*  32 */   static class RefCount { int count = 1;
/*  33 */     String key = null;
/*  34 */     Font font = null;
/*  35 */     RefCount(String key, Font font) { this.key = key;this.font = font;
/*     */     }
/*     */   }
/*     */   
/*  39 */   private static synchronized Font getFont(String name, int style, int size) { String key = name + style + size;
/*  40 */     RefCount foo = (RefCount)table.get(key);
/*  41 */     if (foo != null) {
/*  42 */       foo.count += 1;
/*  43 */       return foo.font;
/*     */     }
/*  45 */     Font f = new Font(name, style, size);
/*  46 */     foo = new RefCount(key, f);
/*  47 */     table.put(key, foo);
/*  48 */     return f;
/*     */   }
/*     */   
/*     */   private static synchronized void delFont(Font f) {
/*  52 */     RefCount foo = null;
/*  53 */     for (Enumeration e = table.elements(); e.hasMoreElements();) {
/*  54 */       foo = (RefCount)e.nextElement();
/*  55 */       if (foo.font == f) {
/*  56 */         foo.count -= 1;
/*  57 */         if (foo.count == 0) {
/*  58 */           table.remove(foo.key);
/*  59 */           foo.font = null;
/*     */         }
/*  61 */         return;
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   void delete() {
/*  67 */     delFont(this.font);
/*  68 */     this.font = null;
/*     */   }
/*     */   
/*     */ 
/*     */   Font font;
/*     */   
/*     */   FontMetrics metric;
/*     */   int[] prop;
/*  76 */   int min_char_or_byte2 = 32;
/*  77 */   int max_char_or_byte2 = 255;
/*  78 */   int min_byte1 = 0;
/*  79 */   int max_byte1 = 0;
/*  80 */   int default_char = 32;
/*     */   
/*     */   int min_width;
/*     */   
/*     */   int max_width;
/*  85 */   String encoding = null;
/*  86 */   Font_CharSet charset = null;
/*     */   
/*     */   void init(byte[] lfname)
/*     */     throws UnsupportedEncodingException
/*     */   {
/*  91 */     if (this.encoding != null) {
/*  92 */       if (this.charset == null) return;
/*  93 */       int tmp = this.default_char;
/*  94 */       int i = 0;
/*  95 */       for (; tmp != 0; 
/*     */           
/*  97 */           tmp &= 0xFFFFFF)
/*     */       {
/*  96 */         i++;
/*  97 */         tmp >>= 8;
/*     */       }
/*  99 */       byte[] btmp = new byte[i];
/* 100 */       tmp = this.default_char;
/* 101 */       i--;
/* 102 */       while (tmp != 0) {
/* 103 */         btmp[i] = ((byte)(tmp & 0xFF));
/* 104 */         tmp >>= 8;tmp &= 0xFFFFFF;
/* 105 */         i--;
/*     */       }
/* 107 */       char[] ctmp = new char[1];
/* 108 */       if (this.charset.encode(btmp, 0, btmp.length, ctmp) == 0) return;
/*     */     }
/* 110 */     this.lfname = lfname;
/*     */   }
/*     */   
/*     */   Font getFont() {
/* 114 */     if (this.font != null) return this.font;
/* 115 */     int size = 12;
/*     */     try {
/* 117 */       int tmp = Integer.parseInt(getFontSize());
/* 118 */       if (tmp != 0) size = tmp;
/*     */     }
/*     */     catch (Exception e) {}
/* 121 */     int style = 0;
/* 122 */     if (getWeight().equals("bold")) style |= 0x1;
/* 123 */     if (getStyle().equals("i")) style |= 0x2;
/* 124 */     if ((getFamily().equals("times")) || (getFamily().equals("times new roman")) || (getFamily().equals("new century schoolbook")))
/*     */     {
/*     */ 
/* 127 */       this.font = getFont("Serif", style, size);
/*     */     }
/* 129 */     else if ((getFamily().equals("helvetica")) || (getFamily().equals("helvetic")) || (getFamily().equals("courier")))
/*     */     {
/*     */ 
/* 132 */       this.font = getFont("SansSerif", style, size);
/*     */     }
/*     */     else {
/* 135 */       this.font = getFont("Monospaced", style, size);
/*     */     }
/*     */     
/* 138 */     this.metric = Toolkit.getDefaultToolkit().getFontMetrics(this.font);
/*     */     
/* 140 */     String reg = getCharsetRegistry();
/* 141 */     String enc = getCharsetEncoding();
/* 142 */     for (Enumeration e = XFont.charSets.elements(); e.hasMoreElements();) {
/* 143 */       Font_CharSet foo = (Font_CharSet)e.nextElement();
/* 144 */       if ((reg.equals(foo.getCharset())) || (enc.equals(foo.getCharset())))
/*     */       {
/* 146 */         this.min_byte1 = foo.getMinByte1();
/* 147 */         this.max_byte1 = foo.getMaxByte1();
/* 148 */         this.min_char_or_byte2 = foo.getMinCharOrByte2();
/* 149 */         this.max_char_or_byte2 = foo.getMaxCharOrByte2();
/* 150 */         this.default_char = foo.getDefaultChar();
/* 151 */         this.encoding = foo.getEncoding();
/* 152 */         this.charset = foo;
/* 153 */         break;
/*     */       }
/*     */     }
/*     */     
/* 157 */     this.min_width = getMaxAdvance();
/* 158 */     this.max_width = getMaxAdvance();
/*     */     
/* 160 */     if (this.encoding != null) {
/* 161 */       int tmp = this.default_char;
/* 162 */       int i = 0;
/* 163 */       for (; tmp != 0; 
/*     */           
/* 165 */           tmp &= 0xFFFFFF)
/*     */       {
/* 164 */         i++;
/* 165 */         tmp >>= 8;
/*     */       }
/* 167 */       byte[] btmp = new byte[i];
/* 168 */       tmp = this.default_char;
/* 169 */       i--;
/* 170 */       while (tmp != 0) {
/* 171 */         btmp[i] = ((byte)(tmp & 0xFF));
/* 172 */         tmp >>= 8;tmp &= 0xFFFFFF;
/* 173 */         i--;
/*     */       }
/* 175 */       char[] ctmp = new char[1];
/* 176 */       if (this.charset != null)
/* 177 */         this.charset.encode(btmp, 0, btmp.length, ctmp);
/* 178 */       this.max_width = (this.min_width = this.metric.charWidth(ctmp[0]));
/* 179 */       if (getSpace().equals("p")) {
/* 180 */         this.min_width = 0;
/*     */       }
/*     */     }
/*     */     else {
/* 184 */       char[] ctmp = new char[1];
/* 185 */       ctmp[0] = '@';this.max_width = this.metric.charsWidth(ctmp, 0, 1);
/* 186 */       ctmp[0] = ' ';this.min_width = this.metric.charsWidth(ctmp, 0, 1);
/*     */     }
/* 188 */     return this.font;
/*     */   }
/*     */   
/*     */   int[] getProp() {
/* 192 */     if (this.prop == null)
/* 193 */       initprop();
/* 194 */     return this.prop;
/*     */   }
/*     */   
/* 197 */   void initprop() { this.prop = new int[2];
/* 198 */     this.prop[0] = Atom.make("FONT", true);
/* 199 */     this.prop[1] = Atom.make(new String(this.lfname), true); }
/*     */   
/* 201 */   int getAscent() { return this.metric.getAscent(); }
/* 202 */   int getDescent() { return this.metric.getDescent(); }
/* 203 */   int getLeading() { return this.metric.getLeading(); }
/* 204 */   int getHeight() { return this.metric.getHeight(); }
/* 205 */   int getMaxAdvance() { return this.metric.charWidth('@'); }
/* 206 */   int getMaxAscent() { return this.metric.getAscent(); }
/* 207 */   int getMaxDescent() { return this.metric.getDescent(); }
/* 208 */   int[] getWidths() { return this.metric.getWidths(); }
/* 209 */   int charWidth(char c) { return this.metric.charWidth(c); }
/*     */   
/* 211 */   String getFamily() { return chop(1); }
/* 212 */   String getWeight() { return chop(2); }
/* 213 */   String getStyle() { return chop(3); }
/* 214 */   String getFontSize() { return chop(6); }
/* 215 */   String getSpace() { return chop(10); }
/* 216 */   String getCharsetRegistry() { return chop(12); }
/* 217 */   String getCharsetEncoding() { return chop(13); }
/*     */   
/*     */   private String chop(int i)
/*     */   {
/* 221 */     int s = 1;
/* 222 */     for (; i != 0; i--) { s = skip(s);s++; }
/* 223 */     int e = skip(s);
/* 224 */     return new String(this.lfname, s, e - s);
/*     */   }
/*     */   
/* 227 */   private int skip(int i) { while ((i < this.lfname.length) && (this.lfname[i] != 45)) i++;
/* 228 */     return i;
/*     */   }
/*     */   
/*     */   boolean getScalable() {
/* 232 */     int tmp = 0;
/* 233 */     try { tmp = Integer.parseInt(getFontSize());
/*     */     } catch (Exception e) {}
/* 235 */     return tmp == 0;
/*     */   }
/*     */   
/*     */   DDXFont getScalableFont(byte[] name) {
/* 239 */     DDXFont f = null;
/* 240 */     try { f = new DDXFont();f.init(XFont.genScaleName(this.lfname, name));
/*     */     } catch (Exception e) {}
/* 242 */     return f;
/*     */   }
/*     */ }


/* Location:              C:\Users\Joey\Downloads\launch.jar!\com\emt\proteus\xserver\display\DDXFont.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */