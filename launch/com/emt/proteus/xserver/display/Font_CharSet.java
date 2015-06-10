package com.emt.proteus.xserver.display;

abstract interface Font_CharSet
{
  public abstract int getMinByte1();
  
  public abstract int getMaxByte1();
  
  public abstract int getMinCharOrByte2();
  
  public abstract int getMaxCharOrByte2();
  
  public abstract int getDefaultChar();
  
  public abstract String getEncoding();
  
  public abstract String getCharset();
  
  public abstract void init();
  
  public abstract int encode(byte[] paramArrayOfByte, int paramInt1, int paramInt2, char[] paramArrayOfChar);
}


/* Location:              C:\Users\Joey\Downloads\launch.jar!\com\emt\proteus\xserver\display\Font_CharSet.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */