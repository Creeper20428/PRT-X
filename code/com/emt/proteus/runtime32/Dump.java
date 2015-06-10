/*     */ package com.emt.proteus.runtime32;
/*     */ 
/*     */ import com.emt.proteus.runtime32.io.IoLib;
/*     */ import com.emt.proteus.runtime32.memory.MainMemory;
/*     */ import java.io.PrintStream;
/*     */ import java.util.Formatter;
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class Dump
/*     */ {
/*  12 */   public static String[] _REGISTERS = { "" };
/*     */   
/*     */ 
/*     */   public static void dump(Processor cpu, MainMemory memory, IoLib io)
/*     */   {
/*  17 */     StringBuilder builder = new StringBuilder(8192);
/*  18 */     Formatter formatter = new Formatter(builder);
/*     */     
/*  20 */     stackImpl(cpu, memory, formatter, 36);
/*  21 */     traceImpl(cpu, memory, formatter, 4);
/*  22 */     registersImpl(cpu, formatter);
/*  23 */     gsImpl(cpu, memory, formatter, 8);
/*  24 */     System.out.flush();
/*  25 */     System.err.println(builder);
/*     */   }
/*     */   
/*     */   public static void dump(Processor cpu)
/*     */   {
/*  30 */     StringBuilder builder = new StringBuilder(8192);
/*  31 */     Formatter formatter = new Formatter(builder);
/*  32 */     registersImpl(cpu, formatter);
/*  33 */     System.out.flush();
/*  34 */     System.err.println(builder);
/*     */   }
/*     */   
/*     */   public static void registers(Processor cpu, MainMemory memory, IoLib io) {
/*  38 */     StringBuilder builder = new StringBuilder(8192);
/*  39 */     Formatter formatter = new Formatter(builder);
/*  40 */     registersImpl(cpu, formatter);
/*  41 */     System.out.flush();
/*  42 */     System.err.print(builder);
/*     */   }
/*     */   
/*     */   public static String formatRegisters(Processor cpu)
/*     */   {
/*  47 */     StringBuilder builder = new StringBuilder(8192);
/*  48 */     Formatter formatter = new Formatter(builder);
/*     */     
/*  50 */     registersOnly(cpu, formatter);
/*  51 */     String regString = builder.toString().trim();
/*  52 */     return regString;
/*     */   }
/*     */   
/*  55 */   public static String registerString(Processor cpu) { StringBuilder builder = new StringBuilder(8192);
/*  56 */     Formatter formatter = new Formatter(builder);
/*     */     
/*  58 */     registersImpl(cpu, formatter);
/*     */     
/*  60 */     String regString = builder.toString();
/*  61 */     return regString;
/*     */   }
/*     */   
/*     */   private static void stackImpl(Processor cpu, MainMemory memory, Formatter formatter, int length) {
/*  65 */     formatter.format("%4soffset [%8s]  %8s { %10s }\n", new Object[] { "", "address", "value", "registers" });
/*  66 */     formatter.format("-------------------------------------------------\n", new Object[0]);
/*     */     
/*  68 */     for (int i = length; i >= 0; i--) {
/*  69 */       int offset = i << 2;
/*  70 */       int addr = cpu.esp + offset;
/*  71 */       String registers = "";
/*  72 */       if (cpu.eax == addr) registers = registers + "eax ";
/*  73 */       if (cpu.ebx == addr) registers = registers + "ebx ";
/*  74 */       if (cpu.ecx == addr) registers = registers + "ecx ";
/*  75 */       if (cpu.edx == addr) registers = registers + "edx ";
/*  76 */       if (cpu.ebp == addr) registers = registers + "ebp ";
/*  77 */       if (cpu.esi == addr) registers = registers + "esi ";
/*  78 */       if (cpu.edi == addr) registers = registers + "edi ";
/*  79 */       int value = memory.getDoubleWord(addr);
/*  80 */       String ptr = "";
/*  81 */       if (addr == cpu.esp) {
/*  82 */         ptr = "esp->";
/*     */       }
/*  84 */       formatter.format("%6s +%2X [%08X]  %8X { %10s }\n", new Object[] { ptr, Integer.valueOf(offset), Integer.valueOf(addr), Integer.valueOf(value), registers });
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private static void traceImpl(Processor cpu, MainMemory memory, Formatter formatter, int limit) {}
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private static void gsImpl(Processor cpu, MainMemory memory, Formatter formatter, int length)
/*     */   {
/*  98 */     formatter.format("GS offset [%8s]  %8s \n", new Object[] { "", "address", "value" });
/*  99 */     formatter.format("-------------------------------------------------\n", new Object[0]);
/*     */     
/* 101 */     for (int i = length; i >= -length; i--) {
/* 102 */       int offset = i << 2;
/* 103 */       int addr = (cpu.gs != null ? cpu.gs.getBase() : 0) + offset;
/* 104 */       int value = memory.getDoubleWord(addr);
/* 105 */       formatter.format("       %2X [%08X]  %8X \n", new Object[] { Integer.valueOf(offset), Integer.valueOf(addr), Integer.valueOf(value) });
/*     */     }
/*     */   }
/*     */   
/*     */   private static void registersImpl(Processor cpu, Formatter formatter) {
/* 110 */     formatter.format("[%8s] [%8s] [%8s] [%8s] [%8s] [%8s] [%8s] [%8s] [%8s] [%8s] [%8s]\n", new Object[] { "eax", "ebx", "ecx", "edx", "esi", "edi", "ebp", "esp", "eip", "oszapc", "gs" });
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
/* 124 */     registersOnly(cpu, formatter);
/*     */   }
/*     */   
/*     */   private static void registersOnly(Processor cpu, Formatter formatter) {
/* 128 */     int flags = cpu.getEflags();
/*     */     
/* 130 */     formatter.format("[%8X] [%8X] [%8X] [%8X] [%8X] [%8X] [%8X] [%8X] [%8X] [%08X] [%08X]\n", new Object[] { Integer.valueOf(cpu.eax), Integer.valueOf(cpu.ebx), Integer.valueOf(cpu.ecx), Integer.valueOf(cpu.edx), Integer.valueOf(cpu.esi), Integer.valueOf(cpu.edi), Integer.valueOf(cpu.ebp), Integer.valueOf(cpu.esp), Integer.valueOf(cpu.eip), Integer.valueOf(flags), Integer.valueOf(cpu.gs != null ? cpu.gs.getBase() : 0) });
/*     */   }
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
/*     */   public static void hang()
/*     */   {
/*     */     try
/*     */     {
/*     */       for (;;)
/*     */       {
/* 149 */         Thread.sleep(1000L);
/*     */       }
/* 151 */     } catch (InterruptedException e) { e.printStackTrace();
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Joey\Downloads\codejar.jar!\com\emt\proteus\runtime32\Dump.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */