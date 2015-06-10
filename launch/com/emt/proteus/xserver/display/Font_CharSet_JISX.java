/*     */ package com.emt.proteus.xserver.display;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ class Font_CharSet_JISX
/*     */   implements Font_CharSet
/*     */ {
/*  24 */   static int min_byte1 = 33;
/*  25 */   static int max_byte1 = 116;
/*  26 */   static int min_char_or_byte2 = 33;
/*  27 */   static int max_char_or_byte2 = 126;
/*  28 */   static int default_char = 8481;
/*  29 */   static String encoding = "EUC-JP";
/*  30 */   static String charset = "jisx0208.1983";
/*     */   
/*  32 */   public int getMinByte1() { return min_byte1; }
/*  33 */   public int getMaxByte1() { return max_byte1; }
/*  34 */   public int getMinCharOrByte2() { return min_char_or_byte2; }
/*  35 */   public int getMaxCharOrByte2() { return max_char_or_byte2; }
/*  36 */   public int getDefaultChar() { return default_char; }
/*  37 */   public String getEncoding() { return encoding; }
/*  38 */   public String getCharset() { return charset; }
/*     */   
/*  40 */   private static String[] _flist = { "-jis-fixed-medium-r-normal--0-0-100-100-c-0-jisx0208.1983-0", "-jis-fixed-medium-r-normal--16-110-100-100-c-160-jisx0208.1983-0", "-jis-fixed-medium-r-normal--16-150-75-75-c-160-jisx0208.1983-0", "-jis-fixed-medium-r-normal--24-170-100-100-c-240-jisx0208.1983-0", "-jis-fixed-medium-r-normal--24-230-75-75-c-240-jisx0208.1983-0", "-misc-fixed-medium-r-normal--14-130-75-75-c-140-jisx0208.1983-0", "-jis-fixed-medium-r-normal--14-120-100-100-c-160-jisx0208.1983-0", "-jis-fixed-medium-r-normal--14-120-100-100-c-140-jisx0208.1983-0", "-jis-fixed-medium-r-normal--14-120-75-75-c-140-jisx0208.1983-0", "-misc-fixed-medium-r-normal--0-0-75-75-c-0-jisx0201.1976-0", "-misc-fixed-medium-r-normal--14-130-75-75-c-70-jisx0201.1976-0", "-misc-fixed-medium-r-normal--16-150-75-75-c-80-jisx0201.1976-0", "-sony-fixed-medium-r-normal--0-0-75-75-c-0-jisx0201.1976-0", "-sony-fixed-medium-r-normal--14-170-100-100-c-120-jisx0201.1976-0", "-sony-fixed-medium-r-normal--16-120-100-100-c-80-jisx0201.1976-0", "-sony-fixed-medium-r-normal--16-150-75-75-c-80-jisx0201.1976-0", "-sony-fixed-medium-r-normal--24-170-100-100-c-120-jisx0201.1976-0", "-sony-fixed-medium-r-normal--24-230-75-75-c-120-jisx0201.1976-0" };
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  63 */   private static String[] _aliases = { "k14", "-jis-fixed-medium-r-normal--14-120-100-100-c-160-jisx0208.1983-0", "r14", "-sony-fixed-medium-r-normal--14-170-100-100-c-120-jisx0201.1976-0", "rk14", "-sony-fixed-medium-r-normal--14-170-100-100-c-120-jisx0201.1976-0", "r16", "-sony-fixed-medium-r-normal--16-120-100-100-c-80-jisx0201.1976-0", "rk16", "-sony-fixed-medium-r-normal--16-120-100-100-c-80-jisx0201.1976-0", "r24", "-sony-fixed-medium-r-normal--24-230-75-75-c-120-jisx0201.1976-0", "rk24", "-sony-fixed-medium-r-normal--24-230-75-75-c-120-jisx0201.1976-0", "kana14", "-sony-fixed-medium-r-normal--14-170-100-100-c-120-jisx0201.1976-0", "8x16kana", "-sony-fixed-medium-r-normal--16-120-100-100-c-80-jisx0201.1976-0", "8x16romankana", "-sony-fixed-medium-r-normal--16-120-100-100-c-80-jisx0201.1976-0", "12x24kana", "-sony-fixed-medium-r-normal--24-170-100-100-c-120-jisx0201.1976-0", "12x24romankana", "-sony-fixed-medium-r-normal--24-170-100-100-c-120-jisx0201.1976-0", "kanji16", "-jis-fixed-medium-r-normal--16-110-100-100-c-160-jisx0208.1983-0", "kanji24", "-jis-fixed-medium-r-normal--24-170-100-100-c-240-jisx0208.1983-0" };
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void init()
/*     */   {
/* 100 */     if (_flist == null) return;
/* 101 */     XFont.addFont(_flist);
/* 102 */     XFont.addAlias(_aliases);
/* 103 */     _flist = null;
/*     */   }
/*     */   
/* 106 */   public int encode(byte[] bbuffer, int start, int len, char[] cbuffer) { int foo = 0;
/*     */     try {
/* 108 */       for (int i = start; i < start + len; i++) {
/* 109 */         bbuffer[i] = ((byte)(bbuffer[i] | 0x80));
/*     */       }
/* 111 */       String s = new String(bbuffer, start, len, encoding);
/* 112 */       foo = s.length();
/* 113 */       s.getChars(0, foo, cbuffer, 0);
/*     */     }
/*     */     catch (Exception e) {}
/*     */     
/* 117 */     return foo;
/*     */   }
/*     */ }


/* Location:              C:\Users\Joey\Downloads\launch.jar!\com\emt\proteus\xserver\display\Font_CharSet_JISX.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */