/*    */ package com.emt.proteus.elf;
/*    */ 
/*    */ 
/*    */ 
/*    */ public class ProgramSegment
/*    */ {
/*    */   private ProgramHeader header;
/*    */   
/*    */ 
/*    */   private byte[] data;
/*    */   
/*    */ 
/*    */ 
/*    */   public ProgramSegment(ProgramHeader header, byte[] data)
/*    */   {
/* 16 */     this.header = header;
/* 17 */     this.data = data;
/*    */   }
/*    */   
/*    */   public ProgramHeader getHeader()
/*    */   {
/* 22 */     return this.header;
/*    */   }
/*    */   
/*    */   public byte[] getData()
/*    */   {
/* 27 */     return this.data;
/*    */   }
/*    */   
/*    */   public String toString()
/*    */   {
/* 32 */     return "PSEG: " + this.header;
/*    */   }
/*    */ }


/* Location:              C:\Users\Joey\Downloads\codejar.jar!\com\emt\proteus\elf\ProgramSegment.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */