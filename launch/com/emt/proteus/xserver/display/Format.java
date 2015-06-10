/*    */ package com.emt.proteus.xserver.display;
/*    */ 
/*    */ import com.emt.proteus.xserver.io.ComChannel;
/*    */ import java.io.IOException;
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
/*    */ public final class Format
/*    */ {
/* 26 */   public static Format[] format = null;
/*    */   public byte depth;
/*    */   public byte bpp;
/*    */   public byte scanLinePad;
/*    */   
/* 31 */   public Format(byte d, byte b, byte s) { this.depth = d;this.bpp = b;this.scanLinePad = s;
/*    */   }
/*    */   
/* 34 */   public void writeByte(ComChannel out) throws IOException { out.writeByte(this.depth);
/* 35 */     out.writeByte(this.bpp);
/* 36 */     out.writeByte(this.scanLinePad);
/* 37 */     out.writePad(5);
/*    */   }
/*    */ }


/* Location:              C:\Users\Joey\Downloads\launch.jar!\com\emt\proteus\xserver\display\Format.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */