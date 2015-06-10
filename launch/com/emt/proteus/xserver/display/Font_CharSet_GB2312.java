/*    */ package com.emt.proteus.xserver.display;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ class Font_CharSet_GB2312
/*    */   implements Font_CharSet
/*    */ {
/* 24 */   static int min_byte1 = 33;
/* 25 */   static int max_byte1 = 119;
/* 26 */   static int min_char_or_byte2 = 33;
/* 27 */   static int max_char_or_byte2 = 126;
/* 28 */   static int default_char = 8481;
/* 29 */   static String encoding = "GB2312";
/* 30 */   static String charset = "gb2312.1980";
/*    */   
/* 32 */   public int getMinByte1() { return min_byte1; }
/* 33 */   public int getMaxByte1() { return max_byte1; }
/* 34 */   public int getMinCharOrByte2() { return min_char_or_byte2; }
/* 35 */   public int getMaxCharOrByte2() { return max_char_or_byte2; }
/* 36 */   public int getDefaultChar() { return default_char; }
/* 37 */   public String getEncoding() { return encoding; }
/* 38 */   public String getCharset() { return charset; }
/*    */   
/* 40 */   private static String[] _flist = { "-isas-fangsong ti-medium-r-normal--0-0-72-72-c-0-gb2312.1980-0", "-isas-fangsong ti-medium-r-normal--16-160-72-72-c-160-gb2312.1980-0", "-isas-fangsong ti-medium-r-normal--24-240-72-72-c-240-gb2312.1980-0", "-isas-song ti-medium-r-normal--16-160-72-72-c-160-gb2312.1980-0", "-isas-song ti-medium-r-normal--24-240-72-72-c-240-gb2312.1980-0" };
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public void init()
/*    */   {
/* 52 */     if (_flist == null) return;
/* 53 */     XFont.addFont(_flist);
/* 54 */     _flist = null;
/*    */   }
/*    */   
/* 57 */   public int encode(byte[] bbuffer, int start, int len, char[] cbuffer) { int foo = 0;
/*    */     try {
/* 59 */       String s = new String(bbuffer, start, len, encoding);
/* 60 */       foo = s.length();
/* 61 */       s.getChars(0, foo, cbuffer, 0);
/*    */     }
/*    */     catch (Exception e) {}
/*    */     
/* 65 */     return foo;
/*    */   }
/*    */ }


/* Location:              C:\Users\Joey\Downloads\launch.jar!\com\emt\proteus\xserver\display\Font_CharSet_GB2312.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */