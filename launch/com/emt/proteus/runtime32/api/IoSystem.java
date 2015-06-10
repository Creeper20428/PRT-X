package com.emt.proteus.runtime32.api;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.net.MalformedURLException;
import java.net.URL;

public abstract interface IoSystem
{
  public static final String CLASSPATH = "classpath://";
  
  public abstract PrintStream getStdOut();
  
  public abstract InputStream getStdIn();
  
  public abstract PrintStream getStdErr();
  
  public abstract Connection connect(byte[] paramArrayOfByte, int paramInt)
    throws IOException;
  
  public abstract InputStream open(URL paramURL)
    throws IOException;
  
  public abstract InputStream createInputstream(String paramString)
    throws IOException;
  
  public abstract URL getUrl(String paramString)
    throws MalformedURLException;
  
  public abstract URL getResource(String paramString)
    throws MalformedURLException;
  
  public abstract FileProxy getFile(String paramString, boolean paramBoolean)
    throws IOException;
  
  public abstract boolean exists(String paramString)
    throws IOException;
  
  public abstract void programExit();
}


/* Location:              C:\Users\Joey\Downloads\launch.jar!\com\emt\proteus\runtime32\api\IoSystem.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */