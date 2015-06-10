/*    */ package com.emt.proteus.elf;
/*    */ 
/*    */ import java.nio.ByteBuffer;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class Section
/*    */ {
/*    */   private SectionHeader header;
/*    */   private byte[] data;
/*    */   
/*    */   public Section(SectionHeader header, byte[] data)
/*    */   {
/* 16 */     this.header = header;
/* 17 */     this.data = data;
/*    */   }
/*    */   
/*    */   public SectionHeader getHeader()
/*    */   {
/* 22 */     return this.header;
/*    */   }
/*    */   
/*    */   public byte[] getData()
/*    */   {
/* 27 */     return this.data;
/*    */   }
/*    */   
/*    */   public int getSize() {
/* 31 */     return this.data.length;
/*    */   }
/*    */   
/* 34 */   public ByteBuffer asByteBuffer() { return Elf.createBuffer(this.data); }
/*    */ }


/* Location:              C:\Users\Joey\Downloads\codejar.jar!\com\emt\proteus\elf\Section.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */