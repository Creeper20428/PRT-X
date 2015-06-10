package com.emt.proteus.xserver.display;

import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.ImageObserver;
import java.io.IOException;

public abstract interface XComponent
  extends ImageObserver
{
  public abstract void init(XDisplay paramXDisplay, XWindow paramXWindow);
  
  public abstract void setBorder(int paramInt);
  
  public abstract void setVisible(boolean paramBoolean);
  
  public abstract boolean isVisible();
  
  public abstract boolean contains(int paramInt1, int paramInt2);
  
  public abstract Component add(Component paramComponent, int paramInt);
  
  public abstract void requestFocus();
  
  public abstract Component[] getComponents();
  
  public abstract void setLocation(int paramInt1, int paramInt2);
  
  public abstract void setSize(int paramInt1, int paramInt2);
  
  public abstract void setBackground(Color paramColor, int paramInt1, int paramInt2, int paramInt3, int paramInt4);
  
  public abstract void setBackground(Color paramColor);
  
  public abstract void setCursor(Cursor paramCursor);
  
  public abstract Image createImage(int paramInt1, int paramInt2);
  
  public abstract void draw(int paramInt1, int paramInt2, int paramInt3, int paramInt4);
  
  public abstract void draw();
  
  public abstract void setBorderPixmap(Pixmap paramPixmap);
  
  public abstract Image getImage();
  
  public abstract Image getImage(GC paramGC, int paramInt1, int paramInt2, int paramInt3, int paramInt4);
  
  public abstract Graphics getGraphics();
  
  public abstract Graphics getGraphics2();
  
  public abstract Graphics getGraphics(GC paramGC, int paramInt);
  
  public abstract XWindow getWindow();
  
  public abstract void drawImage(Clip paramClip, Image paramImage, int paramInt1, int paramInt2, int paramInt3, int paramInt4);
  
  public abstract void drawImage(Image paramImage, int paramInt1, int paramInt2, int paramInt3, int paramInt4);
  
  public abstract void fillImage(Image paramImage, int paramInt1, int paramInt2);
  
  public abstract void fillImage(Image paramImage, int paramInt1, int paramInt2, int paramInt3, int paramInt4);
  
  public abstract void copyArea(XWindow paramXWindow, GC paramGC, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6);
  
  public abstract void copyArea(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6);
  
  public abstract void delete()
    throws IOException;
  
  public abstract void restoreClip();
}


/* Location:              C:\Users\Joey\Downloads\launch.jar!\com\emt\proteus\xserver\display\XComponent.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */