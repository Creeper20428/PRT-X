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
/*    */ class Font_CharSet_KSC5601
/*    */   implements Font_CharSet
/*    */ {
/* 24 */   static int min_byte1 = 33;
/* 25 */   static int max_byte1 = 125;
/* 26 */   static int min_char_or_byte2 = 33;
/* 27 */   static int max_char_or_byte2 = 126;
/* 28 */   static int default_char = 8481;
/* 29 */   static String encoding = "KSC5601";
/* 30 */   static String charset = "ksc5601.1987";
/*    */   
/* 32 */   public int getMinByte1() { return min_byte1; }
/* 33 */   public int getMaxByte1() { return max_byte1; }
/* 34 */   public int getMinCharOrByte2() { return min_char_or_byte2; }
/* 35 */   public int getMaxCharOrByte2() { return max_char_or_byte2; }
/* 36 */   public int getDefaultChar() { return default_char; }
/* 37 */   public String getEncoding() { return encoding; }
/* 38 */   public String getCharset() { return charset; }
/*    */   
/* 40 */   private static String[] _flist = { "-daewoo-mincho-medium-r-normal--0-0-100-100-c-0-ksc5601.1987-0", "-daewoo-mincho-medium-r-normal--16-120-100-100-c-160-ksc5601.1987-0", "-daewoo-mincho-medium-r-normal--24-170-100-100-c-240-ksc5601.1987-0", "-daewoo-gothic-medium-r-normal--16-120-100-100-c-160-ksc5601.1987-0", "-daewoo-gothic-medium-r-normal--24-170-100-100-c-240-ksc5601.1987-0" };
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


/* Location:              C:\Users\Joey\Downloads\launch.jar!\com\emt\proteus\xserver\display\Font_CharSet_KSC5601.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */