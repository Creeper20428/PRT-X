/*    */ package com.emt.proteus.runtime32.io;
/*    */ 
/*    */ import com.emt.proteus.runtime32.memory.MainMemory;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class StdIo
/*    */ {
/*    */   private IoSys ioSys;
/*    */   private MainMemory mem;
/*    */   
/*    */   public StdIo(IoSys ioSys, MainMemory mem)
/*    */   {
/* 15 */     this.ioSys = ioSys;
/* 16 */     this.mem = mem;
/*    */   }
/*    */   
/*    */ 
/*    */   public void initStdIoSupport()
/*    */   {
/* 22 */     this.mem.setDoubleWord(1792, 1792);
/* 23 */     this.mem.setDoubleWord(1800, 1800);
/*    */   }
/*    */   
/*    */ 
/*    */   public IoHandle getByAddress(int address)
/*    */   {
/* 29 */     switch (address) {
/*    */     case 1800: 
/* 31 */       return this.ioSys.getStdErr();
/*    */     case 1792: 
/* 33 */       return this.ioSys.getConsole();
/*    */     }
/*    */     try {
/* 36 */       int handleId = this.mem.getDoubleWord(address);
/* 37 */       return this.ioSys.getByDescriptor(handleId);
/*    */     } catch (ArrayIndexOutOfBoundsException aie) {
/* 39 */       aie.printStackTrace();
/* 40 */       throw aie;
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\Joey\Downloads\codejar.jar!\com\emt\proteus\runtime32\io\StdIo.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */