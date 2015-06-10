package com.emt.proteus.xserver.server;

import com.emt.proteus.xserver.api.Configurable;
import java.io.IOException;

public abstract interface ConnectionFactory
  extends Configurable
{
  public abstract void start()
    throws IOException;
  
  public abstract XConnection accept(long paramLong)
    throws IOException;
  
  public abstract void close()
    throws IOException;
}


/* Location:              C:\Users\Joey\Downloads\launch.jar!\com\emt\proteus\xserver\server\ConnectionFactory.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */