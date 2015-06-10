package com.emt.proteus.elf;

import java.io.IOException;

public abstract interface SeekableDataSource
{
  public abstract void close();
  
  public abstract void seek(long paramLong)
    throws IOException;
  
  public abstract long position()
    throws IOException;
  
  public abstract void readFully(byte[] paramArrayOfByte)
    throws IOException;
  
  public abstract void readFully(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
    throws IOException;
}


/* Location:              C:\Users\Joey\Downloads\codejar.jar!\com\emt\proteus\elf\SeekableDataSource.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */