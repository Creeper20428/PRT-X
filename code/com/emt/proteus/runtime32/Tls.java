/*    */ package com.emt.proteus.runtime32;
/*    */ 
/*    */ import com.emt.proteus.runtime32.memory.MainMemory;
/*    */ import com.emt.proteus.runtime32.memory.ManagedMemory;
/*    */ import com.emt.proteus.utils.Data;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class Tls
/*    */ {
/*    */   public static final int DTV_ENTRY_SIZE = 8;
/*    */   public static final int MIN_DTV_SIZE = 128;
/*    */   public static final int SPARE_DTV_ENTRIES = 8;
/* 19 */   public static int TCB_DTV_ADDRESS = 4;
/* 20 */   public static int TCB_THR_DESC_PTR = 8;
/* 21 */   public static int TCB_MULTI_THREAD = 12;
/* 22 */   public static int TCB_SYS_INFO = 16;
/* 23 */   public static int TCB_STACK_GUARD = 20;
/* 24 */   public static int TCB_PTR_GUARD = 24;
/* 25 */   public static int TCB_GSCOPE = 28;
/* 26 */   public static int TCB_PRIVATE_FUTEX = 32;
/* 27 */   public static int TCB_PRIVATE_TM = 36;
/* 28 */   public static int DTV_ENTRY_ADDRESS_OFFSET = 0;
/* 29 */   public static int DTV_ENTRY_FLAGS_OFFSET = 4;
/*    */   
/*    */ 
/*    */ 
/*    */   public static final int PTHREAD_ETC = 40;
/*    */   
/*    */ 
/*    */ 
/* 37 */   public static int PTHREAD_LIST = 40;
/* 38 */   public static int PTHREAD_TID = 44;
/* 39 */   public static int PTHREAD_PID = 48;
/* 40 */   public static int PTHREAD_ROBUST = 52;
/* 41 */   public static int PTHREAD_JMP_BUF = 56;
/*    */   public static final int ROOT_FLAGS = 0;
/*    */   
/*    */   public static int calculateDtvLength(int maxIndex)
/*    */   {
/* 46 */     return 1 + maxIndex + 8;
/*    */   }
/*    */   
/*    */   public static int calculateDtvMemorySize(int maxIndex) {
/* 50 */     return calculateDtvLength(maxIndex) * 8;
/*    */   }
/*    */   
/*    */ 
/*    */   public static int allocateDTV(ManagedMemory managedMemory, int threadPointer, int maxIndex)
/*    */   {
/* 56 */     int length = calculateDtvLength(maxIndex);
/* 57 */     int size = length * 8;
/* 58 */     int dtv$ = managedMemory.calloc(size);
/* 59 */     Data memory = managedMemory.getMemory();
/* 60 */     memory.setDoubleWord(threadPointer + TCB_DTV_ADDRESS, dtv$ + 8);
/* 61 */     memory.setDoubleWord(dtv$, length);
/* 62 */     return dtv$;
/*    */   }
/*    */   
/*    */   public static int getDtvEntry(MainMemory memory, int threadPointer, int module) {
/* 66 */     int dtvAddress = getDtvAddress(memory, threadPointer);
/* 67 */     int entryOffset = (module + 1) * 8;
/* 68 */     return dtvAddress + entryOffset;
/*    */   }
/*    */   
/*    */   private static int getDtvAddress(MainMemory memory, int threadPointer) {
/* 72 */     return memory.getDoubleWord(threadPointer + TCB_DTV_ADDRESS);
/*    */   }
/*    */   
/*    */   public static int getAddress(MainMemory memory, int threadPointer, int module, int offset) {
/* 76 */     int entry = getDtvEntry(memory, threadPointer, module);
/* 77 */     return memory.getDoubleWord(entry + DTV_ENTRY_ADDRESS_OFFSET) + offset;
/*    */   }
/*    */   
/*    */   public static void setAddress(MainMemory memory, int threadPointer, int moduleIndex, int addr)
/*    */   {
/* 82 */     int entry = getDtvEntry(memory, threadPointer, moduleIndex);
/* 83 */     memory.setDoubleWord(entry + DTV_ENTRY_ADDRESS_OFFSET, addr);
/*    */   }
/*    */   
/* 86 */   public static void setFlags(MainMemory memory, int threadPointer, int moduleIndex, int addr) { int entry = getDtvEntry(memory, threadPointer, moduleIndex);
/* 87 */     memory.setDoubleWord(entry + DTV_ENTRY_FLAGS_OFFSET, addr);
/*    */   }
/*    */ }


/* Location:              C:\Users\Joey\Downloads\codejar.jar!\com\emt\proteus\runtime32\Tls.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */