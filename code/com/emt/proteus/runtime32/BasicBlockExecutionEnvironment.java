/*     */ package com.emt.proteus.runtime32;
/*     */ 
/*     */ import com.emt.proteus.decoder.Disassembler;
/*     */ import com.emt.proteus.decoder.X86Opcode;
/*     */ import com.emt.proteus.runtime32.memory.MainMemory;
/*     */ import com.emt.proteus.runtime32.memory.MemoryInputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.PrintStream;
/*     */ import java.util.Collections;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ 
/*     */ 
/*     */ public class BasicBlockExecutionEnvironment
/*     */   extends ExecutionEnvironment
/*     */ {
/*     */   public static final int CONTINUE = 0;
/*     */   public static final int SUPPLY_CPU_STATE = 1;
/*     */   public static final int HALT_EXECUTION = -1;
/*  20 */   private static final Map codeblocks = Collections.synchronizedMap(new HashMap());
/*     */   private boolean blockAtATime;
/*     */   
/*     */   public BasicBlockExecutionEnvironment(boolean blockAtATime)
/*     */   {
/*  25 */     this.blockAtATime = blockAtATime;
/*     */   }
/*     */   
/*     */   public int getStatus(int currentAddress)
/*     */   {
/*  30 */     if (!this.blockAtATime) {
/*  31 */       return 0;
/*     */     }
/*  33 */     return -1;
/*     */   }
/*     */   
/*     */ 
/*     */   public void cpuStateUpdate(Processor cpu) {}
/*     */   
/*     */ 
/*     */   public int[] extractUCodes(Processor cpu, MainMemory memory, int address)
/*     */   {
/*  42 */     int[] result = (int[])codeblocks.get(Integer.valueOf(address));
/*  43 */     if (result != null)
/*  44 */       return result;
/*  45 */     return decodeUCodes(memory, address);
/*     */   }
/*     */   
/*     */   public int[] decodeUCodes(MainMemory memory, int address)
/*     */   {
/*  50 */     Disassembler disassembler = Disassembler.getDisassembler();
/*     */     
/*  52 */     synchronized (disassembler)
/*     */     {
/*  54 */       int addr = address;
/*  55 */       int[] result = new int[''];
/*  56 */       int index = 0;
/*  57 */       int eip = 0;
/*  58 */       int growth = 0;
/*     */       try
/*     */       {
/*  61 */         boolean done = false;
/*  62 */         while (!done)
/*     */         {
/*  64 */           MemoryInputStream min = new MemoryInputStream(memory, addr);
/*  65 */           X86Opcode decoded = disassembler.disassemble(min);
/*  66 */           int[] tmp = decoded.getUCodes();
/*  67 */           if (!decoded.isBranch())
/*     */           {
/*     */             try
/*     */             {
/*  71 */               System.arraycopy(tmp, 0, result, index, tmp.length - 2);
/*     */             }
/*     */             catch (ArrayIndexOutOfBoundsException e) {
/*  74 */               growth++;
/*  75 */               if (growth > 12) {
/*  76 */                 System.out.printf(" %08X %04X\n", new Object[] { Integer.valueOf(address), Integer.valueOf(result.length) });
/*     */               }
/*  78 */               int[] n = new int[2 * result.length];
/*  79 */               System.arraycopy(result, 0, n, 0, result.length);
/*  80 */               result = n;
/*     */               
/*  82 */               System.arraycopy(tmp, 0, result, index, tmp.length - 2);
/*     */             }
/*  84 */             index += tmp.length - 2;
/*  85 */             eip += tmp[(tmp.length - 1)];
/*  86 */             addr += tmp[(tmp.length - 1)];
/*     */           }
/*     */           else
/*     */           {
/*  90 */             addr += tmp[1];
/*  91 */             tmp[1] += eip;
/*     */             try {
/*  93 */               System.arraycopy(tmp, 0, result, index, tmp.length);
/*     */             }
/*     */             catch (ArrayIndexOutOfBoundsException e) {
/*  96 */               int[] n = new int[2 * result.length];
/*  97 */               System.arraycopy(result, 0, n, 0, result.length);
/*  98 */               result = n;
/*  99 */               System.arraycopy(tmp, 0, result, index, tmp.length);
/*     */             }
/* 101 */             index += tmp.length;
/*     */             
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 115 */             done = true;
/*     */           }
/*     */         }
/* 118 */         int[] finalResult = new int[index];
/* 119 */         System.arraycopy(result, 0, finalResult, 0, index);
/* 120 */         result = finalResult;
/* 121 */         codeblocks.put(Integer.valueOf(address), finalResult);
/*     */ 
/*     */       }
/*     */       catch (IOException e) {}catch (IllegalStateException e)
/*     */       {
/*     */ 
/* 127 */         System.out.println("Address at exception: " + Integer.toHexString(address));
/* 128 */         throw e;
/*     */       }
/*     */       catch (NullPointerException e) {
/* 131 */         System.out.println("Address at exception: " + Integer.toHexString(address));
/* 132 */         throw e;
/*     */       }
/*     */       
/*     */ 
/* 136 */       return result;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Joey\Downloads\codejar.jar!\com\emt\proteus\runtime32\BasicBlockExecutionEnvironment.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */