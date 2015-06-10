package com.emt.proteus.runtime32.api;

import java.io.IOException;

public abstract interface ProteusProcess
{
  public abstract void init(IoSystem paramIoSystem);
  
  public abstract void exec(String[] paramArrayOfString)
    throws IOException;
  
  public abstract void kill();
  
  public abstract void dispose();
}


/* Location:              C:\Users\Joey\Downloads\launch.jar!\com\emt\proteus\runtime32\api\ProteusProcess.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */