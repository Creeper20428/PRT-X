package com.emt.proteus.runtime32.io;

import com.emt.proteus.runtime32.api.FileProxy;
import java.io.IOException;

public abstract interface Provider
{
  public abstract boolean provides(String paramString);
  
  public abstract FileProxy create(String paramString, boolean paramBoolean)
    throws IOException;
}


/* Location:              C:\Users\Joey\Downloads\codejar.jar!\com\emt\proteus\runtime32\io\Provider.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */