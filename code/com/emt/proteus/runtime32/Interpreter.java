/*      */ package com.emt.proteus.runtime32;
/*      */ 
/*      */ import com.emt.proteus.decoder.UCodes;
/*      */ import com.emt.proteus.runtime32.io.IoLib;
/*      */ import com.emt.proteus.runtime32.memory.MainMemory;
/*      */ import com.emt.proteus.runtime32.syscall.SystemCalls;
/*      */ import java.io.PrintStream;
/*      */ import java.util.HashMap;
/*      */ import java.util.Map;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public class Interpreter
/*      */ {
/*   16 */   public static int[] freq = new int['ǋ'];
/*   17 */   public static int[] rare = new int['ǋ'];
/*      */   
/*      */   public static final int CF = 1;
/*      */   
/*      */   public static final int PF = 4;
/*      */   public static final int AF = 16;
/*      */   public static final int ZF = 64;
/*      */   public static final int SF = 128;
/*      */   public static final int OF = 2048;
/*      */   public static final int OSZAPC = 2261;
/*      */   public static final int SZP = 196;
/*      */   public static final int SP = 132;
/*      */   public static final int NCF = 2260;
/*      */   public static final int NOFCF = 212;
/*      */   public static final int NZ = 2197;
/*      */   public static final int NP = 2257;
/*   33 */   public static boolean frequencies = false;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static String intToHexAddress(int a)
/*      */   {
/*   45 */     String className = Integer.toHexString(a);
/*   46 */     while (className.length() < 8)
/*   47 */       className = "0" + className;
/*   48 */     return className;
/*      */   }
/*      */   
/*      */   public static void printFrequencies()
/*      */   {
/*   53 */     System.out.println("********** Microcode frequencies**************");
/*   54 */     for (int i = 0; i < freq.length; i++)
/*   55 */       System.out.println(UCodes.getName(i) + ": " + freq[i]);
/*   56 */     System.out.println("********** RARE Microcode frequencies**************");
/*   57 */     for (int i = 0; i < rare.length; i++)
/*   58 */       if (rare[i] > 0)
/*   59 */         System.out.println(UCodes.getName(i) + ": " + rare[i]);
/*      */   }
/*      */   
/*   62 */   private Class[] args = { Processor.class, MainMemory.class, IoLib.class };
/*      */   
/*   64 */   private Map<Integer, Trace> traces = new HashMap();
/*   65 */   private Map<Integer, Map<Integer, Long>> branches = new HashMap();
/*      */   
/*   67 */   public final boolean logBranches = false;
/*   68 */   private int lastAddr = -1;
/*      */   
/*      */   public boolean interpret(ThreadContext context, ExecutionEnvironment env)
/*      */   {
/*   72 */     int mark = context.cpu.esp;
/*   73 */     while (context.cpu.esp <= mark) {
/*   74 */       executeInterpreted(context, env);
/*      */     }
/*   76 */     return true;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean execute(ThreadContext context, ExecutionEnvironment env)
/*      */   {
/*   99 */     int eip = context.cpu.eip;
/*  100 */     Trace c = (Trace)this.traces.get(Integer.valueOf(eip));
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  106 */     if (c != null) {
/*  107 */       c.executeEntry(context, env);
/*      */     }
/*      */     else
/*      */     {
/*      */       try
/*      */       {
/*  113 */         c = (Trace)Class.forName("com.emt.proteus.compiled.Trace" + intToHexAddress(eip)).newInstance();
/*      */         
/*  115 */         this.traces.put(Integer.valueOf(eip), c);
/*      */         
/*  117 */         int[] targets = c.getEntryPoints();
/*  118 */         for (int i = 0; i < targets.length; i++)
/*  119 */           this.traces.put(Integer.valueOf(targets[i]), c);
/*  120 */         c.executeEntry(context, env);
/*      */       }
/*      */       catch (ClassNotFoundException f)
/*      */       {
/*  124 */         return interpret(context, env);
/*      */       }
/*      */       catch (InstantiationException f) {
/*  127 */         f.printStackTrace();
/*  128 */         return executeInterpreted(context, env);
/*      */       }
/*      */       catch (IllegalAccessException f) {
/*  131 */         f.printStackTrace();
/*  132 */         return executeInterpreted(context, env);
/*      */       }
/*      */     }
/*  135 */     return true;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void addTrace(int address, Trace trace)
/*      */   {
/*  157 */     this.traces.put(Integer.valueOf(address), trace);
/*      */   }
/*      */   
/*      */   public static boolean executeInterpreted(ThreadContext context, ExecutionEnvironment env) {
/*  161 */     Processor cpu = context.cpu;
/*  162 */     MainMemory memory = context.memory;
/*  163 */     IoLib iolib = context.iolib;
/*      */     
/*      */ 
/*      */ 
/*  167 */     int reg0 = 0;int reg1 = 0;int reg2 = 0;int addr = 0;int reg3 = 0;
/*  168 */     long regl0 = 0L;
/*      */     
/*      */ 
/*  171 */     double[] fpuData = context.cpu.fpuData;
/*  172 */     int[] fpuTags = new int[8];
/*  173 */     int[] fpuSpecialTags = new int[8];
/*  174 */     double freg0 = 0.0D;double freg1 = 0.0D;double freg2 = 0.0D;
/*      */     
/*      */ 
/*  177 */     int flagOp1 = cpu.flagOp1;int flagOp2 = cpu.flagOp2;int flagResult = cpu.flagResult;int flagIns = cpu.flagIns;
/*      */     
/*  179 */     int flagStatus = cpu.flagStatus;
/*      */     
/*  181 */     int eax = cpu.eax;
/*  182 */     int ebx = cpu.ebx;
/*  183 */     int edx = cpu.edx;
/*  184 */     int ecx = cpu.ecx;
/*  185 */     int esi = cpu.esi;
/*  186 */     int edi = cpu.edi;
/*  187 */     int esp = cpu.esp;
/*  188 */     int ebp = cpu.ebp;
/*  189 */     int eip = cpu.eip;
/*      */     
/*  191 */     boolean of = cpu.of;
/*  192 */     boolean sf = cpu.sf;
/*  193 */     boolean zf = cpu.zf;
/*  194 */     boolean af = cpu.af;
/*  195 */     boolean pf = cpu.pf;
/*  196 */     boolean cf = cpu.cf;
/*      */     
/*  198 */     int ftop = cpu.ftop;
/*      */     
/*      */     for (;;)
/*      */     {
/*  202 */       cpu.eip = eip;
/*      */       
/*  204 */       Trace.beginBlock(cpu);
/*      */       try {
/*  206 */         int pos = 0;
/*  207 */         int[] uops = env.extractUCodes(cpu, memory, eip);
/*  208 */         int limit = uops.length;
/*  209 */         boolean sync = true;
/*  210 */         byte f; while (pos < limit)
/*      */         {
/*  212 */           int uop = uops[(pos++)];
/*  213 */           if (frequencies) {
/*  214 */             freq[uop] += 1;
/*      */           }
/*  216 */           switch (uop)
/*      */           {
/*      */ 
/*      */           case 1: 
/*  220 */             eip += uops[(pos++)];
/*  221 */             break;
/*      */           
/*      */ 
/*      */           case 2: 
/*  225 */             reg2 = reg0 & reg1;
/*  226 */             break;
/*      */           
/*      */ 
/*      */           case 3: 
/*  230 */             reg2 = reg0 | reg1;
/*  231 */             break;
/*      */           
/*      */ 
/*      */           case 4: 
/*  235 */             reg2 = reg0 ^ 0xFFFFFFFF;
/*  236 */             break;
/*      */           
/*      */ 
/*      */           case 5: 
/*  240 */             reg2 = reg0 ^ reg1;
/*  241 */             break;
/*      */           
/*      */ 
/*      */           case 7: 
/*  245 */             reg2 &= 0xFFFF;
/*  246 */             break;
/*      */           
/*      */ 
/*      */           case 8: 
/*  250 */             reg2 = (byte)reg2;
/*  251 */             break;
/*      */           
/*      */ 
/*      */           case 9: 
/*  255 */             reg2 = (short)reg2;
/*  256 */             break;
/*      */           
/*      */ 
/*      */           case 10: 
/*  260 */             if (reg0 == reg1)
/*      */             {
/*  262 */               zf = true;
/*  263 */               flagStatus &= 0xFFFFFFBF;
/*  264 */               reg0 = reg2;
/*      */             }
/*      */             else {
/*  267 */               zf = false;
/*  268 */               flagStatus &= 0xFFFFFFBF;
/*  269 */               reg0 = reg2;
/*  270 */               reg2 = reg1;
/*  271 */               eax = reg1;
/*      */             }
/*  273 */             break;
/*      */           
/*      */ 
/*      */           case 13: 
/*  277 */             int inAddr = edi;
/*  278 */             int outAddr = esi;
/*      */             
/*  280 */             memory.setByte(inAddr, memory.getByte(outAddr));
/*  281 */             if (cpu.df) {
/*  282 */               outAddr--;
/*  283 */               inAddr--;
/*      */             } else {
/*  285 */               outAddr++;
/*  286 */               inAddr++;
/*      */             }
/*      */             
/*  289 */             edi = inAddr;
/*  290 */             esi = outAddr;
/*  291 */             break;
/*      */           
/*      */ 
/*      */           case 14: 
/*  295 */             int inAddr = edi;
/*  296 */             int outAddr = esi;
/*      */             
/*  298 */             memory.setWord(inAddr, memory.getWord(outAddr));
/*  299 */             if (cpu.df) {
/*  300 */               outAddr -= 2;
/*  301 */               inAddr -= 2;
/*      */             } else {
/*  303 */               outAddr += 2;
/*  304 */               inAddr += 2;
/*      */             }
/*      */             
/*  307 */             edi = inAddr;
/*  308 */             esi = outAddr;
/*  309 */             break;
/*      */           
/*      */ 
/*      */           case 15: 
/*  313 */             if (!Processor.getZeroFlag(flagStatus, zf, flagResult)) {
/*  314 */               reg2 = reg1;
/*      */             } else
/*  316 */               reg2 = reg0;
/*  317 */             break;
/*      */           
/*      */ 
/*      */           case 18: 
/*  321 */             if (Processor.getZeroFlag(flagStatus, zf, flagResult)) {
/*  322 */               reg2 = reg1;
/*      */             } else
/*  324 */               reg2 = reg0;
/*  325 */             break;
/*      */           
/*      */ 
/*      */           case 23: 
/*  329 */             if ((!Processor.getZeroFlag(flagStatus, zf, flagResult)) && (Processor.getSignFlag(flagStatus, sf, flagResult) == Processor.getOverflowFlag(flagStatus, of, flagOp1, flagOp2, flagResult, flagIns))) {
/*  330 */               reg2 = reg1;
/*      */             } else
/*  332 */               reg2 = reg0;
/*  333 */             break;
/*      */           
/*      */ 
/*      */           case 22: 
/*  337 */             if (Processor.getCarryFlag(flagStatus, cf, flagOp1, flagOp2, flagResult, flagIns)) {
/*  338 */               reg2 = reg1;
/*      */             } else
/*  340 */               reg2 = reg0;
/*  341 */             break;
/*      */           
/*      */ 
/*      */           case 25: 
/*  345 */             if ((!Processor.getCarryFlag(flagStatus, cf, flagOp1, flagOp2, flagResult, flagIns)) && (!Processor.getZeroFlag(flagStatus, zf, flagResult))) {
/*  346 */               reg2 = reg1;
/*      */             } else
/*  348 */               reg2 = reg0;
/*  349 */             break;
/*      */           
/*      */ 
/*      */           case 24: 
/*  353 */             if ((Processor.getCarryFlag(flagStatus, cf, flagOp1, flagOp2, flagResult, flagIns)) || (Processor.getZeroFlag(flagStatus, zf, flagResult))) {
/*  354 */               reg2 = reg1;
/*      */             } else
/*  356 */               reg2 = reg0;
/*  357 */             break;
/*      */           
/*      */ 
/*      */           case 26: 
/*  361 */             if (!Processor.getCarryFlag(flagStatus, cf, flagOp1, flagOp2, flagResult, flagIns)) {
/*  362 */               reg2 = reg1;
/*      */             } else
/*  364 */               reg2 = reg0;
/*  365 */             break;
/*      */           
/*      */ 
/*      */           case 31: 
/*  369 */             int count = ecx;
/*  370 */             int inAddr = edi;
/*  371 */             int outAddr = esi;
/*      */             try
/*      */             {
/*  374 */               if (cpu.df) {
/*  375 */                 while (count != 0)
/*      */                 {
/*  377 */                   memory.setDoubleWord(inAddr, memory.getDoubleWord(outAddr));
/*  378 */                   count--;
/*  379 */                   outAddr -= 4;
/*  380 */                   inAddr -= 4;
/*      */                 }
/*      */               }
/*  383 */               while (count != 0)
/*      */               {
/*  385 */                 memory.setDoubleWord(inAddr, memory.getDoubleWord(outAddr));
/*  386 */                 count--;
/*  387 */                 outAddr += 4;
/*  388 */                 inAddr += 4;
/*      */               }
/*      */             }
/*      */             finally
/*      */             {
/*  393 */               ecx = count;
/*  394 */               edi = inAddr;
/*  395 */               esi = outAddr;
/*      */             }
/*      */             
/*  398 */             break;
/*      */           
/*      */ 
/*      */           case 32: 
/*  402 */             int count = ecx;
/*  403 */             int inAddr = edi;
/*  404 */             int outAddr = esi;
/*      */             try
/*      */             {
/*  407 */               if (cpu.df) {
/*  408 */                 while (count != 0)
/*      */                 {
/*  410 */                   memory.setByte(inAddr, memory.getByte(outAddr));
/*  411 */                   count--;
/*  412 */                   outAddr--;
/*  413 */                   inAddr--;
/*      */                 }
/*      */               }
/*  416 */               while (count != 0)
/*      */               {
/*  418 */                 memory.setByte(inAddr, memory.getByte(outAddr));
/*  419 */                 count--;
/*  420 */                 outAddr++;
/*  421 */                 inAddr++;
/*      */               }
/*      */             }
/*      */             finally
/*      */             {
/*  426 */               ecx = count;
/*  427 */               edi = inAddr;
/*  428 */               esi = outAddr;
/*      */             }
/*  430 */             break;
/*      */           
/*      */ 
/*      */           case 33: 
/*  434 */             int count = ecx;
/*  435 */             int addOne = esi;
/*  436 */             int addTwo = edi;
/*  437 */             boolean used = count != 0;
/*  438 */             int dataOne = 0;
/*  439 */             int dataTwo = 0;
/*      */             try
/*      */             {
/*  442 */               if (cpu.df) {
/*  443 */                 do { if (count == 0) break;
/*  444 */                   dataOne = memory.getByte(addOne);
/*  445 */                   dataTwo = memory.getByte(addTwo);
/*  446 */                   count--;
/*  447 */                   addOne--;
/*  448 */                   addTwo--;
/*  449 */                 } while (dataOne == dataTwo);
/*      */               }
/*      */               else {
/*  452 */                 while (count != 0) {
/*  453 */                   dataOne = memory.getByte(addOne);
/*  454 */                   dataTwo = memory.getByte(addTwo);
/*  455 */                   count--;
/*  456 */                   addOne++;
/*  457 */                   addTwo++;
/*  458 */                   if (dataOne != dataTwo)
/*      */                     break;
/*      */                 }
/*      */               }
/*      */             } finally {
/*  463 */               ecx = count;
/*  464 */               esi = addOne;
/*  465 */               edi = addTwo;
/*  466 */               reg0 = dataOne;
/*  467 */               reg1 = dataTwo;
/*  468 */               reg2 = dataOne - dataTwo;
/*      */             }
/*  470 */             break;
/*      */           
/*      */ 
/*      */           case 44: 
/*  474 */             if (reg1 == 0) {
/*  475 */               zf = true;
/*  476 */               flagStatus &= 0x895;
/*  477 */               reg2 = reg0;
/*      */             } else {
/*  479 */               zf = false;
/*  480 */               reg2 = 31 - Processor.numberOfLeadingZeros(reg1);
/*  481 */               of = cf = af = 0;
/*  482 */               flagResult = reg2;
/*  483 */               flagStatus = 132;
/*      */             }
/*      */             
/*      */ 
/*      */ 
/*  488 */             break;
/*      */           
/*      */ 
/*      */           case 51: 
/*  492 */             reg2 = cpu.fpu.getStatusWord(ftop);
/*  493 */             break;
/*      */           
/*      */ 
/*      */           case 50: 
/*  497 */             ftop = ftop + 1 & 0x7;
/*  498 */             break;
/*      */           
/*      */ 
/*      */ 
/*      */           case 55: 
/*  503 */             cpu.fpu.setC1(fpuData[ftop] < 0.0D);
/*  504 */             if (Double.isInfinite(fpuData[ftop]))
/*      */             {
/*  506 */               cpu.fpu.setC0(true);
/*  507 */               cpu.fpu.setC2(true);
/*  508 */               cpu.fpu.setC3(false);
/*  509 */             } else if (Double.isNaN(fpuData[ftop]))
/*      */             {
/*  511 */               cpu.fpu.setC0(true);
/*  512 */               cpu.fpu.setC2(false);
/*  513 */               cpu.fpu.setC3(false);
/*  514 */             } else if (fpuData[ftop] == 0.0D)
/*      */             {
/*  516 */               cpu.fpu.setC0(false);
/*  517 */               cpu.fpu.setC2(false);
/*  518 */               cpu.fpu.setC3(true);
/*      */             }
/*      */             else {
/*  521 */               cpu.fpu.setC0(false);
/*  522 */               cpu.fpu.setC2(true);
/*  523 */               cpu.fpu.setC3(false);
/*      */             }
/*  525 */             break;
/*      */           
/*      */ 
/*      */ 
/*      */           case 54: 
/*  530 */             if ((Double.isNaN(freg0)) || (Double.isNaN(freg1)))
/*      */             {
/*  532 */               cpu.fpu.setI(true);
/*  533 */               cpu.fpu.setC0(true);
/*  534 */               cpu.fpu.setC2(true);
/*  535 */               cpu.fpu.setC3(true);
/*      */ 
/*      */ 
/*      */             }
/*  539 */             else if (freg0 > freg1)
/*      */             {
/*  541 */               cpu.fpu.setC0(false);
/*  542 */               cpu.fpu.setC2(false);
/*  543 */               cpu.fpu.setC3(false);
/*      */             }
/*  545 */             else if (freg0 < freg1)
/*      */             {
/*  547 */               cpu.fpu.setC0(true);
/*  548 */               cpu.fpu.setC2(false);
/*  549 */               cpu.fpu.setC3(false);
/*      */             }
/*  551 */             else if (freg0 == freg1)
/*      */             {
/*  553 */               cpu.fpu.setC0(false);
/*  554 */               cpu.fpu.setC2(false);
/*  555 */               cpu.fpu.setC3(true);
/*      */             }
/*      */             else
/*      */             {
/*  559 */               cpu.fpu.setC0(true);
/*  560 */               cpu.fpu.setC2(true);
/*  561 */               cpu.fpu.setC3(true);
/*      */             }
/*      */             
/*  564 */             break;
/*      */           
/*      */ 
/*      */           case 53: 
/*  568 */             cpu.fpu.setCW((short)reg0);
/*  569 */             break;
/*      */           
/*      */ 
/*      */           case 52: 
/*  573 */             reg2 = cpu.fpu.getCW();
/*  574 */             break;
/*      */           
/*      */ 
/*      */           case 59: 
/*  578 */             ftop = decFtop(ftop);
/*  579 */             pushFPU(freg0, fpuData, ftop);
/*  580 */             break;
/*      */           
/*      */ 
/*      */           case 58: 
/*  584 */             freg0 = popFPU(fpuData, ftop);
/*  585 */             ftop = ftop + 1 & 0x7;
/*  586 */             break;
/*      */           
/*      */ 
/*      */           case 57: 
/*  590 */             freg0 = fpuData[ftop];
/*  591 */             break;
/*      */           
/*      */ 
/*      */           case 56: 
/*  595 */             ftop = decFtop(ftop);
/*  596 */             pushFPU(0.0D, fpuData, ftop);
/*  597 */             cpu.fpu.setC1(false);
/*  598 */             break;
/*      */           
/*      */ 
/*      */           case 62: 
/*  602 */             ftop = decFtop(ftop);
/*  603 */             pushFPU(Math.log(2.0D), fpuData, ftop);
/*  604 */             cpu.fpu.setC1(false);
/*  605 */             break;
/*      */           
/*      */ 
/*      */           case 60: 
/*  609 */             ftop = decFtop(ftop);
/*  610 */             pushFPU(1.0D, fpuData, ftop);
/*  611 */             cpu.fpu.setC1(false);
/*  612 */             break;
/*      */           
/*      */ 
/*      */           case 66: 
/*  616 */             double d = fpuData[ftop];
/*  617 */             fpuData[ftop] = fpuData[(ftop + 1 & 0x7)];
/*  618 */             fpuData[(ftop + 1 & 0x7)] = d;
/*  619 */             break;
/*      */           
/*      */ 
/*      */           case 67: 
/*  623 */             double d = fpuData[ftop];
/*  624 */             fpuData[ftop] = fpuData[(ftop + 2 & 0x7)];
/*  625 */             fpuData[(ftop + 2 & 0x7)] = d;
/*  626 */             break;
/*      */           
/*      */ 
/*      */           case 76: 
/*  630 */             freg2 = freg0 - freg1;
/*  631 */             cpu.fpu.setC1(false);
/*  632 */             break;
/*      */           
/*      */ 
/*      */           case 77: 
/*  636 */             fpuData[ftop] = Math.abs(fpuData[ftop]);
/*  637 */             break;
/*      */           
/*      */ 
/*      */           case 72: 
/*  641 */             if (freg0 > freg1)
/*      */             {
/*  643 */               zf = pf = cf = 0;
/*  644 */               flagStatus &= 0xFFFFFFBA;
/*  645 */             } else if (freg0 < freg1)
/*      */             {
/*  647 */               zf = pf = 0;
/*  648 */               cf = true;
/*  649 */               flagStatus &= 0xFFFFFFBA;
/*  650 */             } else if (freg0 == freg1)
/*      */             {
/*  652 */               cf = pf = 0;
/*  653 */               zf = true;
/*  654 */               sf = false;
/*  655 */               flagStatus &= 0xFF3A;
/*      */             }
/*      */             else {
/*  658 */               zf = pf = cf = 1;
/*  659 */               flagStatus &= 0xFFFFFFBA;
/*      */             }
/*  661 */             af = false;
/*  662 */             sf = false;
/*  663 */             flagStatus &= 0xFF6F;
/*  664 */             break;
/*      */           
/*      */ 
/*      */           case 73: 
/*  668 */             freg0 = regl0;
/*  669 */             break;
/*      */           
/*      */ 
/*      */           case 74: 
/*  673 */             freg2 = freg0 * freg1;
/*  674 */             break;
/*      */           
/*      */ 
/*      */           case 75: 
/*  678 */             freg2 = freg0 + freg1;
/*  679 */             break;
/*      */           
/*      */ 
/*      */           case 92: 
/*  683 */             freg0 = fpuData[(ftop + 1 & 0x7)] * Math.log(fpuData[ftop]) / Math.log(2.0D);
/*  684 */             cpu.fpu.setC1(false);
/*  685 */             break;
/*      */           
/*      */ 
/*      */           case 89: 
/*  689 */             freg2 = freg0 / freg1;
/*  690 */             break;
/*      */           
/*      */ 
/*      */           case 90: 
/*  694 */             freg2 = freg1 / freg0;
/*  695 */             break;
/*      */           
/*      */ 
/*      */           case 106: 
/*  699 */             reg2 = (int)freg0;
/*  700 */             break;
/*      */           
/*      */ 
/*      */           case 115: 
/*  704 */             reg1 &= 0x1F;reg2 = reg0 << reg1 | reg0 >>> 32 - reg1;
/*  705 */             boolean bit0 = (reg2 & 0x1) != 0;
/*  706 */             boolean bit31 = reg2 >> 31 != 0;
/*  707 */             if (reg1 > 0)
/*      */             {
/*  709 */               cf = bit0;
/*  710 */               of = bit0 ^ bit31;
/*  711 */               flagStatus &= 0xD4;
/*      */             }
/*  713 */             break;
/*      */           
/*      */ 
/*      */           case 125: 
/*  717 */             reg2 = reg0 >>> reg1;
/*  718 */             break;
/*      */           
/*      */ 
/*      */           case 136: 
/*  722 */             reg2 = reg0 - reg1;
/*  723 */             break;
/*      */           
/*      */ 
/*      */           case 139: 
/*  727 */             reg2 = reg0 - (reg1 + (Processor.getCarryFlag(flagStatus, cf, flagOp1, flagOp2, flagResult, flagIns) ? 1 : 0));
/*  728 */             break;
/*      */           
/*      */ 
/*      */           case 130: 
/*  732 */             reg2 = reg0 + reg1;
/*  733 */             break;
/*      */           
/*      */ 
/*      */           case 133: 
/*  737 */             reg2 = reg0 + reg1 + (Processor.getCarryFlag(flagStatus, cf, flagOp1, flagOp2, flagResult, flagIns) ? 1 : 0);
/*  738 */             break;
/*      */           
/*      */ 
/*      */           case 135: 
/*  742 */             reg2 = (short)(reg0 - reg1);
/*  743 */             break;
/*      */           
/*      */ 
/*      */           case 134: 
/*  747 */             reg2 = (byte)(reg0 - reg1);
/*  748 */             break;
/*      */           
/*      */ 
/*      */           case 155: 
/*  752 */             long res64 = reg0 * reg1;
/*  753 */             reg2 = (int)res64;
/*  754 */             reg3 = (int)(res64 >> 32);
/*  755 */             break;
/*      */           
/*      */ 
/*      */           case 144: 
/*  759 */             reg2 = -reg0;
/*  760 */             break;
/*      */           
/*      */ 
/*      */           case 147: 
/*  764 */             reg1 &= 0x1F;
/*  765 */             reg2 = reg0 >> reg1;
/*  766 */             break;
/*      */           
/*      */ 
/*      */           case 150: 
/*  770 */             reg1 &= 0x1F;
/*  771 */             reg2 = reg0 << reg1;
/*  772 */             break;
/*      */           
/*      */ 
/*      */           case 171: 
/*  776 */             reg2 = (!Processor.getZeroFlag(flagStatus, zf, flagResult)) && (!Processor.getCarryFlag(flagStatus, cf, flagOp1, flagOp2, flagResult, flagIns)) ? 1 : 0;
/*  777 */             break;
/*      */           
/*      */ 
/*      */           case 173: 
/*  781 */             reg2 = Processor.getCarryFlag(flagStatus, cf, flagOp1, flagOp2, flagResult, flagIns) ? 1 : 0;
/*  782 */             break;
/*      */           
/*      */ 
/*      */           case 166: 
/*  786 */             reg2 = Processor.getZeroFlag(flagStatus, zf, flagResult) ? 1 : 0;
/*  787 */             break;
/*      */           
/*      */ 
/*      */           case 165: 
/*  791 */             reg2 = Processor.getZeroFlag(flagStatus, zf, flagResult) ? 0 : 1;
/*  792 */             break;
/*      */           
/*      */ 
/*      */           case 186: 
/*  796 */             if (reg1 != 0)
/*      */             {
/*  798 */               flagIns = uops[(pos++)];
/*  799 */               flagOp1 = reg0;
/*  800 */               flagOp2 = reg1;
/*  801 */               flagResult = reg2;
/*  802 */               flagStatus = 2261;
/*      */             }
/*      */             
/*      */ 
/*      */             break;
/*      */           case 187: 
/*  808 */             if (reg3 != 0)
/*      */             {
/*  810 */               flagIns = uops[(pos++)];
/*  811 */               flagOp1 = reg0;
/*  812 */               flagOp2 = reg1;
/*  813 */               flagResult = reg2;
/*  814 */               flagStatus = 2261;
/*      */             }
/*      */             
/*      */ 
/*      */             break;
/*      */           case 184: 
/*  820 */             flagIns = uops[(pos++)];
/*  821 */             flagOp1 = reg0;
/*  822 */             flagOp2 = reg1;
/*  823 */             flagResult = reg2;
/*  824 */             flagStatus = 2261;
/*  825 */             of = sf = zf = af = pf = cf = 0;
/*  826 */             break;
/*      */           
/*      */ 
/*      */           case 185: 
/*  830 */             flagIns = 155;
/*  831 */             flagOp1 = reg0;
/*  832 */             flagOp2 = reg1;
/*  833 */             flagResult = reg2;
/*  834 */             flagStatus = 2261;
/*  835 */             if (reg3 < 0) {
/*  836 */               sf = true;
/*      */             } else
/*  838 */               sf = false;
/*  839 */             flagStatus &= 0xFF7F;
/*  840 */             break;
/*      */           
/*      */ 
/*      */           case 190: 
/*  844 */             of = cf = af = 0;
/*  845 */             flagResult = reg2;
/*  846 */             flagStatus = 196;
/*  847 */             flagOp1 = flagOp2 = flagIns = 0;
/*  848 */             sf = zf = pf = 0;
/*  849 */             break;
/*      */           
/*      */ 
/*      */           case 188: 
/*  853 */             cf = Processor.getCarryFlag(flagStatus, cf, flagOp1, flagOp2, flagResult, flagIns);
/*  854 */             flagOp1 = reg0;
/*  855 */             flagOp2 = reg1;
/*  856 */             flagResult = reg2;
/*  857 */             flagIns = uops[(pos++)];
/*  858 */             flagStatus = 2260;
/*  859 */             break;
/*      */           
/*      */ 
/*      */           case 189: 
/*  863 */             flagResult = reg2;
/*  864 */             flagStatus = 196;
/*  865 */             break;
/*      */           
/*      */ 
/*      */           case 182: 
/*  869 */             cpu.df = true;
/*  870 */             break;
/*      */           
/*      */ 
/*      */           case 183: 
/*  874 */             f = (byte)((eax & 0xFF00) >> 8);
/*  875 */             flagStatus &= 0x800;
/*  876 */             sf = (f & 0x80) != 0;
/*  877 */             zf = (f & 0x40) != 0;
/*  878 */             af = (f & 0x10) != 0;
/*  879 */             pf = (f & 0x4) != 0;
/*  880 */             cf = (f & 0x1) != 0;
/*  881 */             break;
/*      */           
/*      */ 
/*      */           case 181: 
/*  885 */             cpu.df = false;
/*  886 */             break;
/*      */           
/*      */ 
/*      */           case 204: 
/*  890 */             eip = memory.getDoubleWord(esp);
/*  891 */             esp += 4;
/*  892 */             synchronize(context, eax, ebx, ecx, edx, esi, edi, esp, ebp, eip, of, sf, zf, af, pf, cf, flagOp1, flagOp2, flagResult, flagStatus, flagIns, ftop);
/*  893 */             f = 1;return f;
/*      */           
/*      */ 
/*      */           case 207: 
/*  897 */             eip = reg0;
/*  898 */             break;
/*      */           
/*      */ 
/*      */           case 206: 
/*  902 */             eip += uops[(pos++)];
/*  903 */             break;
/*      */           
/*      */ 
/*      */           case 203: 
/*  907 */             esp -= 4;
/*  908 */             memory.setDoubleWord(esp, eip);
/*  909 */             eip = reg0;
/*  910 */             synchronize(context, eax, ebx, ecx, edx, esi, edi, esp, ebp, eip, of, sf, zf, af, pf, cf, flagOp1, flagOp2, flagResult, flagStatus, flagIns, ftop);
/*  911 */             context.call();
/*  912 */             break;
/*      */           
/*      */ 
/*      */           case 193: 
/*  916 */             esp = ebp;
/*  917 */             ebp = memory.getDoubleWord(esp);
/*  918 */             esp += 4;
/*  919 */             break;
/*      */           
/*      */ 
/*      */           case 192: 
/*  923 */             of = cf = af = 0;
/*  924 */             flagResult = (byte)reg2;
/*  925 */             flagStatus = 196;
/*  926 */             flagOp1 = flagOp2 = flagIns = 0;
/*  927 */             break;
/*      */           
/*      */ 
/*      */           case 195: 
/*  931 */             esp -= 4;
/*  932 */             memory.setDoubleWord(esp, eip);
/*  933 */             eip += uops[(pos++)];
/*  934 */             synchronize(context, eax, ebx, ecx, edx, esi, edi, esp, ebp, eip, of, sf, zf, af, pf, cf, flagOp1, flagOp2, flagResult, flagStatus, flagIns, ftop);
/*  935 */             context.call();
/*  936 */             break;
/*      */           
/*      */ 
/*      */           case 221: 
/*  940 */             if (Processor.getParityFlag(flagStatus, pf, flagResult)) {
/*  941 */               eip += uops[(pos++)];
/*      */             } else
/*  943 */               pos++;
/*  944 */             break;
/*      */           
/*      */ 
/*      */           case 222: 
/*  948 */             if (!Processor.getParityFlag(flagStatus, pf, flagResult)) {
/*  949 */               eip += uops[(pos++)];
/*      */             } else
/*  951 */               pos++;
/*  952 */             break;
/*      */           
/*      */ 
/*      */           case 223: 
/*  956 */             if ((!Processor.getZeroFlag(flagStatus, zf, flagResult)) && (Processor.getSignFlag(flagStatus, sf, flagResult) == Processor.getOverflowFlag(flagStatus, of, flagOp1, flagOp2, flagResult, flagIns))) {
/*  957 */               eip += uops[(pos++)];
/*      */             } else
/*  959 */               pos++;
/*  960 */             break;
/*      */           
/*      */ 
/*      */           case 216: 
/*  964 */             if (!Processor.getZeroFlag(flagStatus, zf, flagResult)) {
/*  965 */               eip += uops[(pos++)];
/*      */             } else
/*  967 */               pos++;
/*  968 */             break;
/*      */           
/*      */ 
/*      */           case 217: 
/*  972 */             if (Processor.getSignFlag(flagStatus, sf, flagResult) != Processor.getOverflowFlag(flagStatus, of, flagOp1, flagOp2, flagResult, flagIns)) {
/*  973 */               eip += uops[(pos++)];
/*      */             } else
/*  975 */               pos++;
/*  976 */             break;
/*      */           
/*      */ 
/*      */           case 218: 
/*  980 */             if ((Processor.getZeroFlag(flagStatus, zf, flagResult)) || (Processor.getSignFlag(flagStatus, sf, flagResult) != Processor.getOverflowFlag(flagStatus, of, flagOp1, flagOp2, flagResult, flagIns))) {
/*  981 */               eip += uops[(pos++)];
/*      */             } else
/*  983 */               pos++;
/*  984 */             break;
/*      */           
/*      */ 
/*      */           case 212: 
/*  988 */             if (Processor.getCarryFlag(flagStatus, cf, flagOp1, flagOp2, flagResult, flagIns)) {
/*  989 */               eip += uops[(pos++)];
/*      */             } else
/*  991 */               pos++;
/*  992 */             break;
/*      */           
/*      */ 
/*      */           case 213: 
/*  996 */             if ((Processor.getCarryFlag(flagStatus, cf, flagOp1, flagOp2, flagResult, flagIns)) || (Processor.getZeroFlag(flagStatus, zf, flagResult))) {
/*  997 */               eip += uops[(pos++)];
/*      */             } else
/*  999 */               pos++;
/* 1000 */             break;
/*      */           
/*      */ 
/*      */           case 215: 
/* 1004 */             if (Processor.getZeroFlag(flagStatus, zf, flagResult)) {
/* 1005 */               eip += uops[(pos++)];
/*      */             } else
/* 1007 */               pos++;
/* 1008 */             break;
/*      */           
/*      */ 
/*      */           case 208: 
/* 1012 */             if (Processor.getSignFlag(flagStatus, sf, flagResult)) {
/* 1013 */               eip += uops[(pos++)];
/*      */             } else
/* 1015 */               pos++;
/* 1016 */             break;
/*      */           
/*      */ 
/*      */           case 209: 
/* 1020 */             if (!Processor.getSignFlag(flagStatus, sf, flagResult)) {
/* 1021 */               eip += uops[(pos++)];
/*      */             } else
/* 1023 */               pos++;
/* 1024 */             break;
/*      */           
/*      */ 
/*      */           case 210: 
/* 1028 */             if ((!Processor.getCarryFlag(flagStatus, cf, flagOp1, flagOp2, flagResult, flagIns)) && (!Processor.getZeroFlag(flagStatus, zf, flagResult))) {
/* 1029 */               eip += uops[(pos++)];
/*      */             } else
/* 1031 */               pos++;
/* 1032 */             break;
/*      */           
/*      */ 
/*      */           case 211: 
/* 1036 */             if (!Processor.getCarryFlag(flagStatus, cf, flagOp1, flagOp2, flagResult, flagIns)) {
/* 1037 */               eip += uops[(pos++)];
/*      */             } else
/* 1039 */               pos++;
/* 1040 */             break;
/*      */           
/*      */ 
/*      */           case 239: 
/* 1044 */             eax = memory.getDoubleWord(esp);
/* 1045 */             esp += 4;
/* 1046 */             break;
/*      */           
/*      */ 
/*      */           case 237: 
/* 1050 */             esp -= 4;
/* 1051 */             memory.setDoubleWord(esp, ebp);
/* 1052 */             break;
/*      */           
/*      */ 
/*      */           case 235: 
/* 1056 */             esp -= 4;
/* 1057 */             memory.setDoubleWord(esp, edi);
/* 1058 */             break;
/*      */           
/*      */ 
/*      */           case 234: 
/* 1062 */             esp -= 4;
/* 1063 */             memory.setDoubleWord(esp, esi);
/* 1064 */             break;
/*      */           
/*      */ 
/*      */           case 231: 
/* 1068 */             esp -= 4;
/* 1069 */             memory.setDoubleWord(esp, ebx);
/* 1070 */             break;
/*      */           
/*      */ 
/*      */           case 230: 
/* 1074 */             esp -= 4;
/* 1075 */             memory.setDoubleWord(esp, eax);
/* 1076 */             break;
/*      */           
/*      */ 
/*      */           case 224: 
/* 1080 */             if (Processor.getSignFlag(flagStatus, sf, flagResult) == Processor.getOverflowFlag(flagStatus, of, flagOp1, flagOp2, flagResult, flagIns)) {
/* 1081 */               eip += uops[(pos++)];
/*      */             } else
/* 1083 */               pos++;
/* 1084 */             break;
/*      */           
/*      */ 
/*      */           case 255: 
/* 1088 */             addr += 4 * ebx;
/* 1089 */             break;
/*      */           
/*      */ 
/*      */           case 252: 
/* 1093 */             addr += 8 * eax;
/* 1094 */             break;
/*      */           
/*      */ 
/*      */           case 253: 
/* 1098 */             addr += ebx;
/* 1099 */             break;
/*      */           
/*      */ 
/*      */           case 251: 
/* 1103 */             addr += 4 * eax;
/* 1104 */             break;
/*      */           
/*      */ 
/*      */           case 248: 
/* 1108 */             addr += uops[(pos++)];
/* 1109 */             break;
/*      */           
/*      */ 
/*      */           case 249: 
/* 1113 */             addr += eax;
/* 1114 */             break;
/*      */           
/*      */ 
/*      */           case 246: 
/* 1118 */             ebp = esp;
/* 1119 */             break;
/*      */           
/*      */ 
/*      */           case 244: 
/* 1123 */             edi = memory.getDoubleWord(esp);
/* 1124 */             esp += 4;
/* 1125 */             break;
/*      */           
/*      */ 
/*      */           case 245: 
/* 1129 */             ebp = memory.getDoubleWord(esp);
/* 1130 */             esp += 4;
/* 1131 */             break;
/*      */           
/*      */ 
/*      */           case 243: 
/* 1135 */             esi = memory.getDoubleWord(esp);
/* 1136 */             esp += 4;
/* 1137 */             break;
/*      */           
/*      */ 
/*      */           case 240: 
/* 1141 */             ebx = memory.getDoubleWord(esp);
/* 1142 */             esp += 4;
/* 1143 */             break;
/*      */           
/*      */ 
/*      */           case 274: 
/* 1147 */             addr += ebp;
/* 1148 */             break;
/*      */           
/*      */ 
/*      */           case 273: 
/* 1152 */             addr += esp;
/* 1153 */             break;
/*      */           
/*      */ 
/*      */           case 279: 
/* 1157 */             addr = 0;
/* 1158 */             break;
/*      */           
/*      */ 
/*      */           case 278: 
/* 1162 */             if (cpu.gs != null) {
/* 1163 */               addr += cpu.gs.getBase();
/*      */             }
/*      */             
/*      */             break;
/*      */           case 283: 
/* 1168 */             reg0 = ecx;
/* 1169 */             break;
/*      */           
/*      */ 
/*      */           case 282: 
/* 1173 */             reg0 = ebx;
/* 1174 */             break;
/*      */           
/*      */ 
/*      */           case 281: 
/* 1178 */             reg0 = eax;
/* 1179 */             break;
/*      */           
/*      */ 
/*      */           case 280: 
/* 1183 */             reg0 = uops[(pos++)];
/* 1184 */             break;
/*      */           
/*      */ 
/*      */           case 287: 
/* 1188 */             reg0 = esp;
/* 1189 */             break;
/*      */           
/*      */ 
/*      */           case 286: 
/* 1193 */             reg0 = edi;
/* 1194 */             break;
/*      */           
/*      */ 
/*      */           case 285: 
/* 1198 */             reg0 = esi;
/* 1199 */             break;
/*      */           
/*      */ 
/*      */           case 284: 
/* 1203 */             reg0 = edx;
/* 1204 */             break;
/*      */           
/*      */ 
/*      */           case 259: 
/* 1208 */             addr += 4 * ecx;
/* 1209 */             break;
/*      */           
/*      */ 
/*      */           case 256: 
/* 1213 */             addr += 8 * ebx;
/* 1214 */             break;
/*      */           
/*      */ 
/*      */           case 257: 
/* 1218 */             addr += ecx;
/* 1219 */             break;
/*      */           
/*      */ 
/*      */           case 263: 
/* 1223 */             addr += 4 * edx;
/* 1224 */             break;
/*      */           
/*      */ 
/*      */           case 260: 
/* 1228 */             addr += 8 * ecx;
/* 1229 */             break;
/*      */           
/*      */ 
/*      */           case 261: 
/* 1233 */             addr += edx;
/* 1234 */             break;
/*      */           
/*      */ 
/*      */           case 267: 
/* 1238 */             addr += 4 * esi;
/* 1239 */             break;
/*      */           
/*      */ 
/*      */           case 264: 
/* 1243 */             addr += 8 * edx;
/* 1244 */             break;
/*      */           
/*      */ 
/*      */           case 265: 
/* 1248 */             addr += esi;
/* 1249 */             break;
/*      */           
/*      */ 
/*      */           case 271: 
/* 1253 */             addr += 4 * edi;
/* 1254 */             break;
/*      */           
/*      */ 
/*      */           case 268: 
/* 1258 */             addr += 8 * esi;
/* 1259 */             break;
/*      */           
/*      */ 
/*      */           case 269: 
/* 1263 */             addr += edi;
/* 1264 */             break;
/*      */           
/*      */ 
/*      */           case 305: 
/* 1268 */             reg0 = memory.getWord(addr);
/* 1269 */             break;
/*      */           
/*      */ 
/*      */           case 304: 
/* 1273 */             reg0 = memory.getByte(addr);
/* 1274 */             break;
/*      */           
/*      */ 
/*      */           case 306: 
/* 1278 */             reg0 = memory.getDoubleWord(addr);
/* 1279 */             break;
/*      */           
/*      */ 
/*      */           case 308: 
/* 1283 */             regl0 = memory.getDoubleWord(addr);
/* 1284 */             break;
/*      */           
/*      */ 
/*      */           case 311: 
/* 1288 */             reg1 = eax;
/* 1289 */             break;
/*      */           
/*      */ 
/*      */           case 310: 
/* 1293 */             reg1 = uops[(pos++)];
/* 1294 */             break;
/*      */           
/*      */ 
/*      */           case 313: 
/* 1298 */             reg1 = ecx;
/* 1299 */             break;
/*      */           
/*      */ 
/*      */           case 312: 
/* 1303 */             reg1 = ebx;
/* 1304 */             break;
/*      */           
/*      */ 
/*      */           case 315: 
/* 1308 */             reg1 = esi;
/* 1309 */             break;
/*      */           
/*      */ 
/*      */           case 314: 
/* 1313 */             reg1 = edx;
/* 1314 */             break;
/*      */           
/*      */ 
/*      */           case 316: 
/* 1318 */             reg1 = edi;
/* 1319 */             break;
/*      */           
/*      */ 
/*      */           case 289: 
/* 1323 */             reg0 = (byte)eax;
/* 1324 */             break;
/*      */           
/*      */ 
/*      */           case 290: 
/* 1328 */             reg0 = (byte)(eax >> 8);
/* 1329 */             break;
/*      */           
/*      */ 
/*      */           case 291: 
/* 1333 */             reg0 = eax & 0xFFFF;
/* 1334 */             break;
/*      */           
/*      */ 
/*      */           case 292: 
/* 1338 */             reg0 = ebx & 0xFF;
/* 1339 */             break;
/*      */           
/*      */ 
/*      */           case 295: 
/* 1343 */             reg0 = ecx & 0xFF;
/* 1344 */             break;
/*      */           
/*      */ 
/*      */           case 296: 
/* 1348 */             reg0 = (ecx & 0xFF00) >> 8;
/* 1349 */             break;
/*      */           
/*      */ 
/*      */           case 298: 
/* 1353 */             reg0 = edx & 0xFF;
/* 1354 */             break;
/*      */           
/*      */ 
/*      */           case 343: 
/* 1358 */             reg2 = edi;
/* 1359 */             break;
/*      */           
/*      */ 
/*      */           case 342: 
/* 1363 */             reg2 = esi;
/* 1364 */             break;
/*      */           
/*      */ 
/*      */           case 341: 
/* 1368 */             reg2 = edx;
/* 1369 */             break;
/*      */           
/*      */ 
/*      */           case 340: 
/* 1373 */             reg2 = ecx;
/* 1374 */             break;
/*      */           
/*      */ 
/*      */           case 339: 
/* 1378 */             reg2 = ebx;
/* 1379 */             break;
/*      */           
/*      */ 
/*      */           case 338: 
/* 1383 */             reg2 = eax;
/* 1384 */             break;
/*      */           
/*      */ 
/*      */           case 337: 
/* 1388 */             reg2 = uops[(pos++)];
/* 1389 */             break;
/*      */           
/*      */ 
/*      */           case 336: 
/* 1393 */             reg1 = 1;
/* 1394 */             break;
/*      */           
/*      */ 
/*      */           case 350: 
/* 1398 */             reg2 = ebx & 0xFF;
/* 1399 */             break;
/*      */           
/*      */ 
/*      */           case 348: 
/* 1403 */             reg2 = eax & 0xFF;
/* 1404 */             break;
/*      */           
/*      */ 
/*      */           case 346: 
/* 1408 */             reg2 = eax & 0xFFFF;
/* 1409 */             break;
/*      */           
/*      */ 
/*      */           case 345: 
/* 1413 */             reg2 = ebp;
/* 1414 */             break;
/*      */           
/*      */ 
/*      */           case 326: 
/* 1418 */             reg1 = (ecx & 0xFF00) >> 8;
/* 1419 */             break;
/*      */           
/*      */ 
/*      */           case 325: 
/* 1423 */             reg1 = ecx & 0xFF;
/* 1424 */             break;
/*      */           
/*      */ 
/*      */           case 323: 
/* 1428 */             reg1 = ebx & 0xFF;
/* 1429 */             break;
/*      */           
/*      */ 
/*      */           case 321: 
/* 1433 */             reg1 = eax & 0xFF;
/* 1434 */             break;
/*      */           
/*      */ 
/*      */           case 333: 
/* 1438 */             reg1 = memory.getDoubleWord(addr);
/* 1439 */             break;
/*      */           
/*      */ 
/*      */           case 328: 
/* 1443 */             reg1 = edx & 0xFF;
/* 1444 */             break;
/*      */           
/*      */ 
/*      */           case 329: 
/* 1448 */             reg1 = (edx & 0xFF00) >> 8;
/* 1449 */             break;
/*      */           
/*      */ 
/*      */           case 373: 
/* 1453 */             eax = eax & 0xFFFF0000 | reg0 & 0xFFFF;
/* 1454 */             break;
/*      */           
/*      */ 
/*      */           case 383: 
/* 1458 */             eax = reg2;
/* 1459 */             break;
/*      */           
/*      */ 
/*      */           case 356: 
/* 1463 */             reg2 = edx & 0xFF;
/* 1464 */             break;
/*      */           
/*      */ 
/*      */           case 357: 
/* 1468 */             reg2 = esi & 0xFFFF;
/* 1469 */             break;
/*      */           
/*      */ 
/*      */           case 358: 
/* 1473 */             reg2 = edi & 0xFFFF;
/* 1474 */             break;
/*      */           
/*      */ 
/*      */           case 352: 
/* 1478 */             reg2 = (ecx & 0xFF00) >> 8;
/* 1479 */             break;
/*      */           
/*      */ 
/*      */           case 353: 
/* 1483 */             reg2 = ecx & 0xFF;
/* 1484 */             break;
/*      */           
/*      */ 
/*      */           case 360: 
/* 1488 */             reg2 = memory.getDoubleWord(addr);
/* 1489 */             break;
/*      */           
/*      */ 
/*      */           case 361: 
/* 1493 */             reg2 = memory.getWord(addr);
/* 1494 */             break;
/*      */           
/*      */ 
/*      */           case 362: 
/* 1498 */             reg2 = memory.getByte(addr);
/* 1499 */             break;
/*      */           
/*      */ 
/*      */           case 411: 
/* 1503 */             edx = reg3;
/* 1504 */             break;
/*      */           
/*      */ 
/*      */           case 408: 
/* 1508 */             memory.setByte(addr, (byte)reg2);
/* 1509 */             break;
/*      */           
/*      */ 
/*      */           case 415: 
/* 1513 */             pushFPU(freg0, fpuData, ftop);
/* 1514 */             break;
/*      */           
/*      */ 
/*      */           case 412: 
/* 1518 */             memory.setF32(addr, (float)freg0);
/* 1519 */             break;
/*      */           
/*      */ 
/*      */           case 413: 
/* 1523 */             memory.setF64(addr, freg0);
/* 1524 */             break;
/*      */           
/*      */ 
/*      */           case 403: 
/* 1528 */             esp = reg2;
/* 1529 */             break;
/*      */           
/*      */ 
/*      */           case 401: 
/* 1533 */             edi = reg2;
/* 1534 */             break;
/*      */           
/*      */ 
/*      */           case 406: 
/* 1538 */             memory.setDoubleWord(addr, reg2);
/* 1539 */             break;
/*      */           
/*      */ 
/*      */           case 407: 
/* 1543 */             memory.setWord(addr, (short)reg2);
/* 1544 */             break;
/*      */           
/*      */ 
/*      */           case 393: 
/* 1548 */             edx = edx & 0xFF00 | reg2 & 0xFF;
/* 1549 */             break;
/*      */           
/*      */ 
/*      */           case 399: 
/* 1553 */             esi = reg2;
/* 1554 */             break;
/*      */           
/*      */ 
/*      */           case 398: 
/* 1558 */             edx = reg2;
/* 1559 */             break;
/*      */           
/*      */ 
/*      */           case 397: 
/* 1563 */             ecx = reg2;
/* 1564 */             break;
/*      */           
/*      */ 
/*      */           case 396: 
/* 1568 */             ebx = reg2;
/* 1569 */             break;
/*      */           
/*      */ 
/*      */           case 386: 
/* 1573 */             eax = eax & 0xFF00 | reg2 & 0xFF;
/* 1574 */             break;
/*      */           
/*      */ 
/*      */           case 385: 
/* 1578 */             eax = eax & 0xFFFF00FF | (reg2 & 0xFF) << 8;
/* 1579 */             break;
/*      */           
/*      */ 
/*      */           case 384: 
/* 1583 */             eax = eax & 0xFFFF0000 | reg2 & 0xFFFF;
/* 1584 */             break;
/*      */           
/*      */ 
/*      */           case 391: 
/* 1588 */             ecx = ecx & 0xFF00 | reg2 & 0xFF;
/* 1589 */             break;
/*      */           
/*      */ 
/*      */           case 440: 
/* 1593 */             freg0 = fpuData[(ftop + 1 & 0x7)];
/* 1594 */             break;
/*      */           
/*      */ 
/*      */           case 447: 
/* 1598 */             freg1 = fpuData[ftop];
/* 1599 */             break;
/*      */           
/*      */ 
/*      */           case 433: 
/* 1603 */             freg0 = memory.getF32(addr);
/* 1604 */             break;
/*      */           
/*      */ 
/*      */           case 434: 
/* 1608 */             freg0 = memory.getF64(addr);
/* 1609 */             break;
/*      */           
/*      */ 
/*      */           case 436: 
/* 1613 */             freg1 = memory.getF32(addr);
/* 1614 */             break;
/*      */           
/*      */ 
/*      */           case 437: 
/* 1618 */             freg1 = memory.getF64(addr);
/* 1619 */             break;
/*      */           
/*      */ 
/*      */           case 438: 
/* 1623 */             freg0 = fpuData[ftop];
/* 1624 */             break;
/*      */           
/*      */ 
/*      */           case 439: 
/* 1628 */             freg0 = fpuData[ftop];
/* 1629 */             break;
/*      */           
/*      */ 
/*      */           case 425: 
/* 1633 */             pushFPU(freg2, fpuData, ftop);
/* 1634 */             break;
/*      */           
/*      */ 
/*      */           case 427: 
/* 1638 */             fpuData[(ftop + 2 & 0x7)] = freg2;
/* 1639 */             break;
/*      */           
/*      */ 
/*      */           case 426: 
/* 1643 */             fpuData[(ftop + 1 & 0x7)] = freg2;
/* 1644 */             break;
/*      */           
/*      */ 
/*      */           case 429: 
/* 1648 */             fpuData[(ftop + 4 & 0x7)] = freg2;
/* 1649 */             break;
/*      */           
/*      */ 
/*      */           case 428: 
/* 1653 */             fpuData[(ftop + 3 & 0x7)] = freg2;
/* 1654 */             break;
/*      */           
/*      */ 
/*      */           case 417: 
/* 1658 */             fpuData[(ftop + 1 & 0x7)] = freg0;
/* 1659 */             break;
/*      */           
/*      */ 
/*      */           case 416: 
/* 1663 */             pushFPU(freg0, fpuData, ftop);
/* 1664 */             break;
/*      */           
/*      */ 
/*      */           case 418: 
/* 1668 */             fpuData[(ftop + 2 & 0x7)] = freg0;
/* 1669 */             break;
/*      */           
/*      */ 
/*      */           case 458: 
/* 1673 */             flagOp1 = cpu.flagOp1;flagOp2 = cpu.flagOp2;flagResult = cpu.flagResult;flagIns = cpu.flagIns;
/* 1674 */             flagStatus = cpu.flagStatus;
/* 1675 */             eax = cpu.eax;
/* 1676 */             ebx = cpu.ebx;
/* 1677 */             edx = cpu.edx;
/* 1678 */             ecx = cpu.ecx;
/* 1679 */             esi = cpu.esi;
/* 1680 */             edi = cpu.edi;
/* 1681 */             esp = cpu.esp;
/* 1682 */             ebp = cpu.ebp;
/* 1683 */             eip = cpu.eip;
/* 1684 */             of = cpu.of;
/* 1685 */             sf = cpu.sf;
/* 1686 */             zf = cpu.zf;
/* 1687 */             af = cpu.af;
/* 1688 */             pf = cpu.pf;
/* 1689 */             cf = cpu.cf;
/* 1690 */             ftop = cpu.ftop;
/* 1691 */             break;
/*      */           
/*      */ 
/*      */           case 456: 
/* 1695 */             reg2 = addr;
/* 1696 */             addr = 0;
/* 1697 */             break;
/*      */           
/*      */ 
/*      */           case 452: 
/* 1701 */             freg1 = fpuData[(ftop + 4 & 0x7)];
/* 1702 */             break;
/*      */           
/*      */ 
/*      */           case 451: 
/* 1706 */             freg1 = fpuData[(ftop + 3 & 0x7)];
/* 1707 */             break;
/*      */           
/*      */ 
/*      */           case 450: 
/* 1711 */             freg1 = fpuData[(ftop + 2 & 0x7)];
/* 1712 */             break;
/*      */           
/*      */ 
/*      */           case 449: 
/* 1716 */             freg1 = fpuData[(ftop + 1 & 0x7)];
/* 1717 */             break;
/*      */           
/*      */ 
/*      */           case 448: 
/* 1721 */             freg1 = fpuData[ftop];
/* 1722 */             break;
/*      */           case 6: case 11: case 12: case 16: case 17: case 19: case 20: case 21: case 27: case 28: case 29: case 30: case 34: case 35: case 36: case 37: case 38: case 39: case 40: case 41: case 42: case 43: case 45: case 46: case 47: case 48: case 49: case 61: case 63: case 64: case 65: case 68: case 69: case 70: case 71: case 78: case 79: case 80: case 81: case 82: case 83: case 84: case 85: case 86: case 87: case 88: case 91: case 93: 
/*      */           case 94: case 95: case 96: case 97: case 98: case 99: case 100: case 101: case 102: case 103: case 104: case 105: case 107: case 108: case 109: case 110: case 111: case 112: case 113: case 114: case 116: case 117: case 118: case 119: case 120: case 121: case 122: case 123: case 124: case 126: case 127: case 128: case 129: case 131: case 132: case 137: case 138: case 140: case 141: case 142: case 143: case 145: case 146: case 148: case 149: case 151: case 152: case 153: 
/*      */           case 154: case 156: case 157: case 158: case 159: case 160: case 161: case 162: case 163: case 164: case 167: case 168: case 169: case 170: case 172: case 174: case 175: case 176: case 177: case 178: case 179: case 180: case 191: case 194: case 196: case 197: case 198: case 199: case 200: case 201: case 202: case 205: case 214: case 219: case 220: case 225: case 226: case 227: case 228: case 229: case 232: case 233: case 236: case 238: case 241: case 242: case 247: 
/*      */           case 250: case 254: case 258: case 262: case 266: case 270: case 272: case 275: case 276: case 277: case 288: case 293: case 294: case 297: case 299: case 300: case 301: case 302: case 303: case 307: case 309: case 317: case 318: case 319: case 320: case 322: case 324: case 327: case 330: case 331: case 332: case 334: case 335: case 344: case 347: case 349: case 351: case 354: case 355: case 359: case 363: case 364: case 365: case 366: case 367: case 368: case 369: 
/*      */           case 370: case 371: case 372: case 374: case 375: case 376: case 377: case 378: case 379: case 380: case 381: case 382: case 387: case 388: case 389: case 390: case 392: case 394: case 395: case 400: case 402: case 404: case 405: case 409: case 410: case 414: case 419: case 420: case 421: case 422: case 423: case 424: case 430: case 431: case 432: case 435: case 441: case 442: case 443: case 444: case 445: case 446: case 453: case 454: case 455: case 457: default: 
/* 1728 */             cpu.eax = eax;
/* 1729 */             cpu.ebx = ebx;
/* 1730 */             cpu.ecx = ecx;
/* 1731 */             cpu.edx = edx;
/* 1732 */             cpu.esi = esi;
/* 1733 */             cpu.edi = edi;
/* 1734 */             cpu.esp = esp;
/* 1735 */             cpu.ebp = ebp;
/* 1736 */             cpu.eip = eip;
/*      */             
/* 1738 */             cpu.ftop = ftop;
/*      */             
/* 1740 */             if (executeUncommon(context, env, uops, pos - 1, reg0, reg1, reg2, reg3, addr, freg0, freg1, freg2, regl0, of, sf, zf, af, pf, cf, flagOp1, flagOp2, flagResult, flagIns, flagStatus)) {
/* 1741 */               f = 1;return f;
/*      */             }
/* 1743 */             pos = uops.length;
/*      */             
/* 1745 */             env.getStatus(cpu.eip);
/* 1746 */             env.cpuStateUpdate(cpu);
/*      */             
/* 1748 */             flagOp1 = cpu.flagOp1;flagOp2 = cpu.flagOp2;flagResult = cpu.flagResult;flagIns = cpu.flagIns;
/*      */             
/* 1750 */             flagStatus = cpu.flagStatus;
/*      */             
/* 1752 */             eax = cpu.eax;
/* 1753 */             ebx = cpu.ebx;
/* 1754 */             edx = cpu.edx;
/* 1755 */             ecx = cpu.ecx;
/* 1756 */             esi = cpu.esi;
/* 1757 */             edi = cpu.edi;
/* 1758 */             esp = cpu.esp;
/* 1759 */             ebp = cpu.ebp;
/* 1760 */             eip = cpu.eip;
/*      */             
/* 1762 */             of = cpu.of;
/* 1763 */             sf = cpu.sf;
/* 1764 */             zf = cpu.zf;
/* 1765 */             af = cpu.af;
/* 1766 */             pf = cpu.pf;
/* 1767 */             cf = cpu.cf;
/*      */             
/* 1769 */             ftop = cpu.ftop;
/*      */             
/* 1771 */             sync = false;
/*      */           }
/*      */           
/*      */         }
/*      */         
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1780 */         int status = env.getStatus(eip);
/* 1781 */         if (status == 0) {
/*      */           continue;
/*      */         }
/* 1784 */         if (sync) { synchronize(context, eax, ebx, ecx, edx, esi, edi, esp, ebp, eip, of, sf, zf, af, pf, cf, flagOp1, flagOp2, flagResult, flagStatus, flagIns, ftop);
/*      */         }
/* 1786 */         Trace.endBlock(cpu);
/*      */         
/* 1788 */         env.cpuStateUpdate(cpu);
/* 1789 */         if (status == -1) {
/* 1790 */           f = 0;return f;
/*      */         }
/*      */       }
/*      */       finally {}
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   private static boolean executeUncommon(ThreadContext context, ExecutionEnvironment env, int[] uops, int pos, int reg0, int reg1, int reg2, int reg3, int addr, double freg0, double freg1, double freg2, long regl0, boolean of, boolean sf, boolean zf, boolean af, boolean pf, boolean cf, int flagOp1, int flagOp2, int flagResult, int flagIns, int flagStatus)
/*      */   {
/* 1801 */     Processor cpu = context.cpu;
/* 1802 */     MainMemory memory = context.memory;
/* 1803 */     IoLib iolib = context.iolib;
/*      */     
/*      */ 
/*      */ 
/* 1807 */     double[] fpuData = context.cpu.fpuData;
/*      */     
/* 1809 */     int eax = cpu.eax;
/* 1810 */     int ebx = cpu.ebx;
/* 1811 */     int edx = cpu.edx;
/* 1812 */     int ecx = cpu.ecx;
/* 1813 */     int esi = cpu.esi;
/* 1814 */     int edi = cpu.edi;
/* 1815 */     int esp = cpu.esp;
/* 1816 */     int ebp = cpu.ebp;
/* 1817 */     int eip = cpu.eip;
/*      */     
/* 1819 */     int ftop = cpu.ftop;
/* 1820 */     if (frequencies)
/* 1821 */       rare[uops[pos]] += 1;
/* 1822 */     while (pos < uops.length)
/*      */     {
/* 1824 */       int uop = uops[(pos++)];
/* 1825 */       if (frequencies) {
/* 1826 */         freq[uop] += 1;
/*      */       }
/* 1828 */       switch (uop)
/*      */       {
/*      */ 
/*      */ 
/*      */       case 1: 
/* 1833 */         eip += uops[(pos++)];
/* 1834 */         break;
/*      */       
/*      */ 
/*      */       case 2: 
/* 1838 */         reg2 = reg0 & reg1;
/* 1839 */         break;
/*      */       
/*      */ 
/*      */       case 3: 
/* 1843 */         reg2 = reg0 | reg1;
/* 1844 */         break;
/*      */       
/*      */ 
/*      */       case 4: 
/* 1848 */         reg2 = reg0 ^ 0xFFFFFFFF;
/* 1849 */         break;
/*      */       
/*      */ 
/*      */       case 5: 
/* 1853 */         reg2 = reg0 ^ reg1;
/* 1854 */         break;
/*      */       
/*      */ 
/*      */       case 6: 
/* 1858 */         reg2 &= 0xFF;
/* 1859 */         break;
/*      */       
/*      */ 
/*      */       case 7: 
/* 1863 */         reg2 &= 0xFFFF;
/* 1864 */         break;
/*      */       
/*      */ 
/*      */       case 8: 
/* 1868 */         reg2 = (byte)reg2;
/* 1869 */         break;
/*      */       
/*      */ 
/*      */       case 9: 
/* 1873 */         reg2 = (short)reg2;
/* 1874 */         break;
/*      */       
/*      */ 
/*      */       case 10: 
/* 1878 */         if (reg0 == reg1)
/*      */         {
/* 1880 */           zf = true;
/* 1881 */           flagStatus &= 0xFFFFFFBF;
/* 1882 */           reg0 = reg2;
/*      */         }
/*      */         else {
/* 1885 */           zf = false;
/* 1886 */           flagStatus &= 0xFFFFFFBF;
/* 1887 */           reg0 = reg2;
/* 1888 */           reg2 = reg1;
/* 1889 */           eax = reg1;
/*      */         }
/* 1891 */         break;
/*      */       
/*      */ 
/*      */       case 11: 
/* 1895 */         reg0 ^= reg1;
/* 1896 */         reg1 = reg0 ^ reg1;
/* 1897 */         reg0 ^= reg1;
/* 1898 */         break;
/*      */       
/*      */ 
/*      */       case 12: 
/* 1902 */         reg0 ^= reg1;
/* 1903 */         reg1 = reg0 ^ reg1;
/* 1904 */         reg0 ^= reg1;
/* 1905 */         reg0 += reg1;
/* 1906 */         break;
/*      */       
/*      */ 
/*      */       case 13: 
/* 1910 */         int inAddr = edi;
/* 1911 */         int outAddr = esi;
/*      */         
/* 1913 */         memory.setByte(inAddr, memory.getByte(outAddr));
/* 1914 */         if (cpu.df) {
/* 1915 */           outAddr--;
/* 1916 */           inAddr--;
/*      */         } else {
/* 1918 */           outAddr++;
/* 1919 */           inAddr++;
/*      */         }
/*      */         
/* 1922 */         edi = inAddr;
/* 1923 */         esi = outAddr;
/* 1924 */         break;
/*      */       
/*      */ 
/*      */       case 14: 
/* 1928 */         int inAddr = edi;
/* 1929 */         int outAddr = esi;
/*      */         
/* 1931 */         memory.setWord(inAddr, memory.getWord(outAddr));
/* 1932 */         if (cpu.df) {
/* 1933 */           outAddr -= 2;
/* 1934 */           inAddr -= 2;
/*      */         } else {
/* 1936 */           outAddr += 2;
/* 1937 */           inAddr += 2;
/*      */         }
/*      */         
/* 1940 */         edi = inAddr;
/* 1941 */         esi = outAddr;
/* 1942 */         break;
/*      */       
/*      */ 
/*      */       case 15: 
/* 1946 */         if (!Processor.getZeroFlag(flagStatus, zf, flagResult)) {
/* 1947 */           reg2 = reg1;
/*      */         } else
/* 1949 */           reg2 = reg0;
/* 1950 */         break;
/*      */       
/*      */ 
/*      */       case 17: 
/* 1954 */         if (!Processor.getSignFlag(flagStatus, sf, flagResult)) {
/* 1955 */           reg2 = reg1;
/*      */         } else
/* 1957 */           reg2 = reg0;
/* 1958 */         break;
/*      */       
/*      */ 
/*      */       case 16: 
/* 1962 */         if (Processor.getSignFlag(flagStatus, sf, flagResult)) {
/* 1963 */           reg2 = reg1;
/*      */         } else
/* 1965 */           reg2 = reg0;
/* 1966 */         break;
/*      */       
/*      */ 
/*      */       case 19: 
/* 1970 */         if (Processor.getSignFlag(flagStatus, sf, flagResult) == Processor.getOverflowFlag(flagStatus, of, flagOp1, flagOp2, flagResult, flagIns)) {
/* 1971 */           reg2 = reg1;
/*      */         } else
/* 1973 */           reg2 = reg0;
/* 1974 */         break;
/*      */       
/*      */ 
/*      */       case 18: 
/* 1978 */         if (Processor.getZeroFlag(flagStatus, zf, flagResult)) {
/* 1979 */           reg2 = reg1;
/*      */         } else
/* 1981 */           reg2 = reg0;
/* 1982 */         break;
/*      */       
/*      */ 
/*      */       case 21: 
/* 1986 */         if (Processor.getSignFlag(flagStatus, sf, flagResult) != Processor.getOverflowFlag(flagStatus, of, flagOp1, flagOp2, flagResult, flagIns)) {
/* 1987 */           reg2 = reg1;
/*      */         } else
/* 1989 */           reg2 = reg0;
/* 1990 */         break;
/*      */       
/*      */ 
/*      */       case 20: 
/* 1994 */         if ((Processor.getZeroFlag(flagStatus, zf, flagResult)) || (Processor.getSignFlag(flagStatus, sf, flagResult) != Processor.getOverflowFlag(flagStatus, of, flagOp1, flagOp2, flagResult, flagIns))) {
/* 1995 */           reg2 = reg1;
/*      */         } else
/* 1997 */           reg2 = reg0;
/* 1998 */         break;
/*      */       
/*      */ 
/*      */       case 23: 
/* 2002 */         if ((!Processor.getZeroFlag(flagStatus, zf, flagResult)) && (Processor.getSignFlag(flagStatus, sf, flagResult) == Processor.getOverflowFlag(flagStatus, of, flagOp1, flagOp2, flagResult, flagIns))) {
/* 2003 */           reg2 = reg1;
/*      */         } else
/* 2005 */           reg2 = reg0;
/* 2006 */         break;
/*      */       
/*      */ 
/*      */       case 22: 
/* 2010 */         if (Processor.getCarryFlag(flagStatus, cf, flagOp1, flagOp2, flagResult, flagIns)) {
/* 2011 */           reg2 = reg1;
/*      */         } else
/* 2013 */           reg2 = reg0;
/* 2014 */         break;
/*      */       
/*      */ 
/*      */       case 25: 
/* 2018 */         if ((!Processor.getCarryFlag(flagStatus, cf, flagOp1, flagOp2, flagResult, flagIns)) && (!Processor.getZeroFlag(flagStatus, zf, flagResult))) {
/* 2019 */           reg2 = reg1;
/*      */         } else
/* 2021 */           reg2 = reg0;
/* 2022 */         break;
/*      */       
/*      */ 
/*      */       case 24: 
/* 2026 */         if ((Processor.getCarryFlag(flagStatus, cf, flagOp1, flagOp2, flagResult, flagIns)) || (Processor.getZeroFlag(flagStatus, zf, flagResult))) {
/* 2027 */           reg2 = reg1;
/*      */         } else
/* 2029 */           reg2 = reg0;
/* 2030 */         break;
/*      */       
/*      */ 
/*      */       case 27: 
/* 2034 */         if (Processor.getOverflowFlag(flagStatus, of, flagOp1, flagOp2, flagResult, flagIns)) {
/* 2035 */           reg2 = reg1;
/*      */         } else
/* 2037 */           reg2 = reg0;
/* 2038 */         break;
/*      */       
/*      */ 
/*      */       case 26: 
/* 2042 */         if (!Processor.getCarryFlag(flagStatus, cf, flagOp1, flagOp2, flagResult, flagIns)) {
/* 2043 */           reg2 = reg1;
/*      */         } else
/* 2045 */           reg2 = reg0;
/* 2046 */         break;
/*      */       
/*      */ 
/*      */       case 29: 
/* 2050 */         if (Processor.getParityFlag(flagStatus, pf, flagResult)) {
/* 2051 */           reg2 = reg1;
/*      */         } else
/* 2053 */           reg2 = reg0;
/* 2054 */         break;
/*      */       
/*      */ 
/*      */       case 28: 
/* 2058 */         if (!Processor.getOverflowFlag(flagStatus, of, flagOp1, flagOp2, flagResult, flagIns)) {
/* 2059 */           reg2 = reg1;
/*      */         } else
/* 2061 */           reg2 = reg0;
/* 2062 */         break;
/*      */       
/*      */ 
/*      */       case 31: 
/* 2066 */         int count = ecx;
/* 2067 */         int inAddr = edi;
/* 2068 */         int outAddr = esi;
/*      */         try
/*      */         {
/* 2071 */           if (cpu.df) {
/* 2072 */             while (count != 0)
/*      */             {
/* 2074 */               memory.setDoubleWord(inAddr, memory.getDoubleWord(outAddr));
/* 2075 */               count--;
/* 2076 */               outAddr -= 4;
/* 2077 */               inAddr -= 4;
/*      */             }
/*      */           }
/* 2080 */           while (count != 0)
/*      */           {
/* 2082 */             memory.setDoubleWord(inAddr, memory.getDoubleWord(outAddr));
/* 2083 */             count--;
/* 2084 */             outAddr += 4;
/* 2085 */             inAddr += 4;
/*      */           }
/*      */         }
/*      */         finally
/*      */         {
/* 2090 */           ecx = count;
/* 2091 */           edi = inAddr;
/* 2092 */           esi = outAddr;
/*      */         }
/*      */         
/* 2095 */         break;
/*      */       
/*      */ 
/*      */       case 30: 
/* 2099 */         if (!Processor.getParityFlag(flagStatus, pf, flagResult)) {
/* 2100 */           reg2 = reg1;
/*      */         } else
/* 2102 */           reg2 = reg0;
/* 2103 */         break;
/*      */       
/*      */ 
/*      */       case 34: 
/* 2107 */         int count = ecx;
/* 2108 */         int tadd = edi;
/* 2109 */         boolean used = count != 0;
/* 2110 */         int input = 0;
/*      */         try
/*      */         {
/* 2113 */           if (cpu.df) {
/* 2114 */             do { if (count == 0) break;
/* 2115 */               input = 0xFF & memory.getByte(tadd);
/* 2116 */               count--;
/* 2117 */               tadd--;
/* 2118 */             } while (reg0 != input);
/*      */           }
/*      */           else
/* 2121 */             while (count != 0) {
/* 2122 */               input = 0xFF & memory.getByte(tadd);
/* 2123 */               count--;
/* 2124 */               tadd++;
/* 2125 */               if (reg0 == input)
/*      */                 break;
/*      */             }
/*      */         } finally {
/* 2129 */           ecx = count;
/* 2130 */           edi = tadd;
/* 2131 */           reg1 = input;
/* 2132 */           reg2 = reg0 - reg1;
/*      */         }
/* 2134 */         break;
/*      */       
/*      */ 
/*      */       case 35: 
/* 2138 */         int count = ecx;
/* 2139 */         int taddr = edi;
/*      */         try
/*      */         {
/* 2142 */           if (cpu.df) {
/* 2143 */             while (count != 0) {
/* 2144 */               memory.setDoubleWord(taddr, reg1);
/* 2145 */               count--;
/* 2146 */               taddr -= 4;
/*      */             }
/*      */           }
/* 2149 */           while (count != 0) {
/* 2150 */             memory.setDoubleWord(taddr, reg1);
/* 2151 */             count--;
/* 2152 */             taddr += 4;
/*      */           }
/*      */         }
/*      */         finally
/*      */         {
/* 2157 */           ecx = count;
/* 2158 */           edi = taddr;
/*      */         }
/* 2160 */         break;
/*      */       
/*      */ 
/*      */       case 32: 
/* 2164 */         int count = ecx;
/* 2165 */         int inAddr = edi;
/* 2166 */         int outAddr = esi;
/*      */         try
/*      */         {
/* 2169 */           if (cpu.df) {
/* 2170 */             while (count != 0)
/*      */             {
/* 2172 */               memory.setByte(inAddr, memory.getByte(outAddr));
/* 2173 */               count--;
/* 2174 */               outAddr--;
/* 2175 */               inAddr--;
/*      */             }
/*      */           }
/* 2178 */           while (count != 0)
/*      */           {
/* 2180 */             memory.setByte(inAddr, memory.getByte(outAddr));
/* 2181 */             count--;
/* 2182 */             outAddr++;
/* 2183 */             inAddr++;
/*      */           }
/*      */         }
/*      */         finally
/*      */         {
/* 2188 */           ecx = count;
/* 2189 */           edi = inAddr;
/* 2190 */           esi = outAddr;
/*      */         }
/* 2192 */         break;
/*      */       
/*      */ 
/*      */       case 33: 
/* 2196 */         int count = ecx;
/* 2197 */         int addOne = esi;
/* 2198 */         int addTwo = edi;
/* 2199 */         boolean used = count != 0;
/* 2200 */         int dataOne = 0;
/* 2201 */         int dataTwo = 0;
/*      */         try
/*      */         {
/* 2204 */           if (cpu.df) {
/* 2205 */             do { if (count == 0) break;
/* 2206 */               dataOne = memory.getByte(addOne);
/* 2207 */               dataTwo = memory.getByte(addTwo);
/* 2208 */               count--;
/* 2209 */               addOne--;
/* 2210 */               addTwo--;
/* 2211 */             } while (dataOne == dataTwo);
/*      */           }
/*      */           else {
/* 2214 */             while (count != 0) {
/* 2215 */               dataOne = memory.getByte(addOne);
/* 2216 */               dataTwo = memory.getByte(addTwo);
/* 2217 */               count--;
/* 2218 */               addOne++;
/* 2219 */               addTwo++;
/* 2220 */               if (dataOne != dataTwo)
/*      */                 break;
/*      */             }
/*      */           }
/*      */         } finally {
/* 2225 */           ecx = count;
/* 2226 */           esi = addOne;
/* 2227 */           edi = addTwo;
/* 2228 */           reg0 = dataOne;
/* 2229 */           reg1 = dataTwo;
/* 2230 */           reg2 = dataOne - dataTwo;
/*      */         }
/* 2232 */         break;
/*      */       
/*      */ 
/*      */       case 38: 
/* 2236 */         flagStatus &= 0x8D4;
/* 2237 */         cf = (reg0 & 1 << reg1 % 16) != 0;
/* 2238 */         reg2 = reg0 | 1 << reg1 % 16;
/*      */         
/* 2240 */         flagResult = 0;
/* 2241 */         flagOp1 = 0;
/* 2242 */         flagOp2 = 0;
/* 2243 */         flagIns = 129;
/* 2244 */         break;
/*      */       
/*      */ 
/*      */       case 39: 
/* 2248 */         flagStatus &= 0x8D4;
/* 2249 */         cf = (reg0 & 1 << reg1) != 0;
/* 2250 */         reg2 = reg0 | 1 << reg1;
/*      */         
/* 2252 */         flagResult = 0;
/* 2253 */         flagOp1 = 0;
/* 2254 */         flagOp2 = 0;
/* 2255 */         flagIns = 130;
/* 2256 */         break;
/*      */       
/*      */ 
/*      */       case 36: 
/* 2260 */         int count = ecx;
/* 2261 */         int taddr = edi;
/*      */         try
/*      */         {
/* 2264 */           if (cpu.df) {
/* 2265 */             while (count != 0) {
/* 2266 */               memory.setByte(taddr, (byte)reg1);
/* 2267 */               count--;
/* 2268 */               taddr--;
/*      */             }
/*      */           }
/* 2271 */           while (count != 0) {
/* 2272 */             memory.setByte(taddr, (byte)reg1);
/* 2273 */             count--;
/* 2274 */             taddr++;
/*      */           }
/*      */         }
/*      */         finally
/*      */         {
/* 2279 */           ecx = count;
/* 2280 */           edi = taddr;
/*      */         }
/* 2282 */         break;
/*      */       
/*      */ 
/*      */       case 37: 
/* 2286 */         int adr = edi;
/* 2287 */         memory.setByte(adr, (byte)reg1);
/*      */         
/* 2289 */         if (cpu.df) {
/* 2290 */           adr--;
/*      */         } else {
/* 2292 */           adr++;
/*      */         }
/* 2294 */         edi = adr;
/* 2295 */         break;
/*      */       
/*      */ 
/*      */       case 42: 
/* 2299 */         flagStatus &= 0x8D4;
/* 2300 */         cf = (reg0 & 1 << reg1 % 16) != 0;
/* 2301 */         reg2 = reg0 & (1 << reg1 % 16 ^ 0xFFFFFFFF);
/* 2302 */         break;
/*      */       
/*      */ 
/*      */       case 43: 
/* 2306 */         flagStatus &= 0x8D4;
/* 2307 */         cf = (reg0 & 1 << reg1) != 0;
/* 2308 */         reg2 = reg0 & (1 << reg1 ^ 0xFFFFFFFF);
/* 2309 */         break;
/*      */       
/*      */ 
/*      */       case 40: 
/* 2313 */         flagStatus &= 0x8D4;
/* 2314 */         cf = (reg0 & 1 << reg1 % 16) != 0;
/* 2315 */         reg2 = reg0 ^ 1 << reg1 % 16;
/* 2316 */         break;
/*      */       
/*      */ 
/*      */       case 41: 
/* 2320 */         flagStatus &= 0x8D4;
/* 2321 */         cf = (reg0 & 1 << reg1) != 0;
/* 2322 */         reg2 = reg0 ^ 1 << reg1;
/* 2323 */         break;
/*      */       
/*      */ 
/*      */       case 46: 
/* 2327 */         switch (eax) {
/*      */         case 0: 
/* 2329 */           eax = 2;
/* 2330 */           ebx = 1970169159;
/* 2331 */           edx = 1231384169;
/* 2332 */           ecx = 1818588270;
/* 2333 */           break;
/*      */         case 1: 
/* 2335 */           eax = 1618;
/* 2336 */           ebx = 2048;
/* 2337 */           ecx = Integer.MIN_VALUE;
/* 2338 */           edx = 25426937;
/* 2339 */           break;
/*      */         default: 
/* 2341 */           context.warn("CPUID method %02X needs implementing ", new Object[] { Integer.valueOf(eax) });
/* 2342 */           eax = ebx = ecx = edx = 0;
/*      */         }
/* 2344 */         break;
/*      */       
/*      */ 
/*      */       case 47: 
/* 2348 */         reg1 = 0;
/* 2349 */         reg2 = 0;
/* 2350 */         context.warn("Used RDTSC", new Object[0]);
/* 2351 */         break;
/*      */       
/*      */ 
/*      */       case 44: 
/* 2355 */         if (reg1 == 0) {
/* 2356 */           zf = true;
/* 2357 */           flagStatus &= 0x895;
/* 2358 */           reg2 = reg0;
/*      */         } else {
/* 2360 */           zf = false;
/* 2361 */           reg2 = 31 - Processor.numberOfLeadingZeros(reg1);
/* 2362 */           of = cf = af = 0;
/* 2363 */           flagResult = reg2;
/* 2364 */           flagStatus = 132;
/*      */         }
/*      */         
/*      */ 
/*      */ 
/* 2369 */         break;
/*      */       
/*      */ 
/*      */       case 45: 
/* 2373 */         if (reg1 == 0) {
/* 2374 */           zf = true;
/* 2375 */           flagStatus &= 0x895;
/* 2376 */           reg2 = reg0;
/*      */         } else {
/* 2378 */           zf = false;
/* 2379 */           reg2 = Processor.numberOfTrailingZeros(reg1);
/* 2380 */           of = cf = af = 0;
/* 2381 */           flagResult = reg1;
/* 2382 */           flagStatus = 132;
/*      */         }
/* 2384 */         break;
/*      */       
/*      */ 
/*      */       case 51: 
/* 2388 */         reg2 = cpu.fpu.getStatusWord(ftop);
/* 2389 */         break;
/*      */       
/*      */ 
/*      */       case 50: 
/* 2393 */         ftop = ftop + 1 & 0x7;
/* 2394 */         break;
/*      */       
/*      */ 
/*      */       case 49: 
/* 2398 */         int flags = cpu.eflags & 0xF72A;
/* 2399 */         flags &= 0xFFFE7FFF;
/* 2400 */         if (Processor.getCarryFlag(flagStatus, cf, flagOp1, flagOp2, flagResult, flagIns))
/*      */         {
/* 2402 */           flags |= 0x1;
/* 2403 */           cf = true;
/*      */         }
/*      */         else {
/* 2406 */           cf = false; }
/* 2407 */         if (Processor.getParityFlag(flagStatus, pf, flagResult))
/*      */         {
/* 2409 */           flags |= 0x4;
/* 2410 */           pf = true;
/*      */         }
/*      */         else {
/* 2413 */           pf = false; }
/* 2414 */         if (Processor.getAuxCarryFlag(flagStatus, af, flagOp1, flagOp2, flagResult, flagIns))
/*      */         {
/* 2416 */           flags |= 0x10;
/* 2417 */           af = true;
/*      */         }
/*      */         else {
/* 2420 */           af = false; }
/* 2421 */         if (Processor.getZeroFlag(flagStatus, zf, flagResult))
/*      */         {
/* 2423 */           flags |= 0x40;
/* 2424 */           zf = true;
/*      */         }
/*      */         else {
/* 2427 */           zf = false; }
/* 2428 */         if (Processor.getSignFlag(flagStatus, sf, flagResult))
/*      */         {
/* 2430 */           flags |= 0x80;
/* 2431 */           sf = true;
/*      */         }
/*      */         else {
/* 2434 */           sf = false; }
/* 2435 */         if (Processor.getOverflowFlag(flagStatus, of, flagOp1, flagOp2, flagResult, flagIns))
/*      */         {
/* 2437 */           flags |= 0x800;
/* 2438 */           of = true;
/*      */         }
/*      */         else {
/* 2441 */           of = false; }
/* 2442 */         flagStatus = 0;
/* 2443 */         esp -= 4;
/* 2444 */         memory.setDoubleWord(esp, flags);
/* 2445 */         break;
/*      */       
/*      */ 
/*      */       case 48: 
/* 2449 */         int tflags = memory.getDoubleWord(esp);
/* 2450 */         esp += 4;
/* 2451 */         flagStatus = 0;
/* 2452 */         cf = (tflags & 0x1) != 0;
/* 2453 */         pf = (tflags & 0x4) != 0;
/* 2454 */         af = (tflags & 0x10) != 0;
/* 2455 */         zf = (tflags & 0x40) != 0;
/* 2456 */         sf = (tflags & 0x80) != 0;
/* 2457 */         of = (tflags & 0x800) != 0;
/* 2458 */         break;
/*      */       
/*      */ 
/*      */ 
/*      */       case 55: 
/* 2463 */         cpu.fpu.setC1(fpuData[ftop] < 0.0D);
/* 2464 */         if (Double.isInfinite(fpuData[ftop]))
/*      */         {
/* 2466 */           cpu.fpu.setC0(true);
/* 2467 */           cpu.fpu.setC2(true);
/* 2468 */           cpu.fpu.setC3(false);
/* 2469 */         } else if (Double.isNaN(fpuData[ftop]))
/*      */         {
/* 2471 */           cpu.fpu.setC0(true);
/* 2472 */           cpu.fpu.setC2(false);
/* 2473 */           cpu.fpu.setC3(false);
/* 2474 */         } else if (fpuData[ftop] == 0.0D)
/*      */         {
/* 2476 */           cpu.fpu.setC0(false);
/* 2477 */           cpu.fpu.setC2(false);
/* 2478 */           cpu.fpu.setC3(true);
/*      */         }
/*      */         else {
/* 2481 */           cpu.fpu.setC0(false);
/* 2482 */           cpu.fpu.setC2(true);
/* 2483 */           cpu.fpu.setC3(false);
/*      */         }
/* 2485 */         break;
/*      */       
/*      */ 
/*      */ 
/*      */       case 54: 
/* 2490 */         if ((Double.isNaN(freg0)) || (Double.isNaN(freg1)))
/*      */         {
/* 2492 */           cpu.fpu.setI(true);
/* 2493 */           cpu.fpu.setC0(true);
/* 2494 */           cpu.fpu.setC2(true);
/* 2495 */           cpu.fpu.setC3(true);
/*      */ 
/*      */ 
/*      */         }
/* 2499 */         else if (freg0 > freg1)
/*      */         {
/* 2501 */           cpu.fpu.setC0(false);
/* 2502 */           cpu.fpu.setC2(false);
/* 2503 */           cpu.fpu.setC3(false);
/*      */         }
/* 2505 */         else if (freg0 < freg1)
/*      */         {
/* 2507 */           cpu.fpu.setC0(true);
/* 2508 */           cpu.fpu.setC2(false);
/* 2509 */           cpu.fpu.setC3(false);
/*      */         }
/* 2511 */         else if (freg0 == freg1)
/*      */         {
/* 2513 */           cpu.fpu.setC0(false);
/* 2514 */           cpu.fpu.setC2(false);
/* 2515 */           cpu.fpu.setC3(true);
/*      */         }
/*      */         else
/*      */         {
/* 2519 */           cpu.fpu.setC0(true);
/* 2520 */           cpu.fpu.setC2(true);
/* 2521 */           cpu.fpu.setC3(true);
/*      */         }
/*      */         
/* 2524 */         break;
/*      */       
/*      */ 
/*      */       case 53: 
/* 2528 */         cpu.fpu.setCW((short)reg0);
/* 2529 */         break;
/*      */       
/*      */ 
/*      */       case 52: 
/* 2533 */         reg2 = cpu.fpu.getCW();
/* 2534 */         break;
/*      */       
/*      */ 
/*      */       case 59: 
/* 2538 */         ftop = decFtop(ftop);
/* 2539 */         pushFPU(freg0, fpuData, ftop);
/* 2540 */         break;
/*      */       
/*      */ 
/*      */       case 58: 
/* 2544 */         freg0 = popFPU(fpuData, ftop);
/* 2545 */         ftop = ftop + 1 & 0x7;
/* 2546 */         break;
/*      */       
/*      */ 
/*      */       case 57: 
/* 2550 */         freg0 = fpuData[ftop];
/* 2551 */         break;
/*      */       
/*      */ 
/*      */       case 56: 
/* 2555 */         ftop = decFtop(ftop);
/* 2556 */         pushFPU(0.0D, fpuData, ftop);
/* 2557 */         cpu.fpu.setC1(false);
/* 2558 */         break;
/*      */       
/*      */ 
/*      */       case 63: 
/* 2562 */         ftop = decFtop(ftop);
/* 2563 */         pushFPU(1.0D / Math.log(2.0D), fpuData, ftop);
/* 2564 */         cpu.fpu.setC1(false);
/* 2565 */         break;
/*      */       
/*      */ 
/*      */       case 62: 
/* 2569 */         ftop = decFtop(ftop);
/* 2570 */         pushFPU(Math.log(2.0D), fpuData, ftop);
/* 2571 */         cpu.fpu.setC1(false);
/* 2572 */         break;
/*      */       
/*      */ 
/*      */       case 61: 
/* 2576 */         ftop = decFtop(ftop);
/* 2577 */         pushFPU(3.141592653589793D, fpuData, ftop);
/* 2578 */         cpu.fpu.setC1(false);
/* 2579 */         break;
/*      */       
/*      */ 
/*      */       case 60: 
/* 2583 */         ftop = decFtop(ftop);
/* 2584 */         pushFPU(1.0D, fpuData, ftop);
/* 2585 */         cpu.fpu.setC1(false);
/* 2586 */         break;
/*      */       
/*      */ 
/*      */       case 68: 
/* 2590 */         double d = fpuData[ftop];
/* 2591 */         fpuData[ftop] = fpuData[(ftop + 3 & 0x7)];
/* 2592 */         fpuData[(ftop + 3 & 0x7)] = d;
/* 2593 */         break;
/*      */       
/*      */ 
/*      */       case 69: 
/* 2597 */         double d = fpuData[ftop];
/* 2598 */         fpuData[ftop] = fpuData[(ftop + 4 & 0x7)];
/* 2599 */         fpuData[(ftop + 4 & 0x7)] = d;
/* 2600 */         break;
/*      */       
/*      */ 
/*      */       case 70: 
/* 2604 */         double d = fpuData[ftop];
/* 2605 */         fpuData[ftop] = fpuData[(ftop + 5 & 0x7)];
/* 2606 */         fpuData[(ftop + 5 & 0x7)] = d;
/* 2607 */         break;
/*      */       
/*      */ 
/*      */       case 71: 
/* 2611 */         double d = fpuData[ftop];
/* 2612 */         fpuData[ftop] = fpuData[(ftop + 6 & 0x7)];
/* 2613 */         fpuData[(ftop + 6 & 0x7)] = d;
/* 2614 */         break;
/*      */       
/*      */ 
/*      */       case 64: 
/* 2618 */         ftop = decFtop(ftop);
/* 2619 */         pushFPU(Math.log(10.0D) / Math.log(2.0D), fpuData, ftop);
/* 2620 */         cpu.fpu.setC1(false);
/* 2621 */         break;
/*      */       
/*      */ 
/*      */       case 65: 
/* 2625 */         ftop = decFtop(ftop);
/* 2626 */         pushFPU(Math.log(2.0D) / Math.log(10.0D), fpuData, ftop);
/* 2627 */         cpu.fpu.setC1(false);
/* 2628 */         break;
/*      */       
/*      */ 
/*      */       case 66: 
/* 2632 */         double d = fpuData[ftop];
/* 2633 */         fpuData[ftop] = fpuData[(ftop + 1 & 0x7)];
/* 2634 */         fpuData[(ftop + 1 & 0x7)] = d;
/* 2635 */         break;
/*      */       
/*      */ 
/*      */       case 67: 
/* 2639 */         double d = fpuData[ftop];
/* 2640 */         fpuData[ftop] = fpuData[(ftop + 2 & 0x7)];
/* 2641 */         fpuData[(ftop + 2 & 0x7)] = d;
/* 2642 */         break;
/*      */       
/*      */ 
/*      */       case 76: 
/* 2646 */         freg2 = freg0 - freg1;
/* 2647 */         cpu.fpu.setC1(false);
/* 2648 */         break;
/*      */       
/*      */ 
/*      */       case 77: 
/* 2652 */         fpuData[ftop] = Math.abs(fpuData[ftop]);
/* 2653 */         break;
/*      */       
/*      */ 
/*      */       case 78: 
/* 2657 */         fpuData[ftop] = (-fpuData[ftop]);
/* 2658 */         break;
/*      */       
/*      */ 
/*      */       case 79: 
/* 2662 */         fpuData[ftop] = Math.sqrt(fpuData[ftop]);
/* 2663 */         cpu.fpu.setC1(false);
/* 2664 */         break;
/*      */       
/*      */ 
/*      */       case 72: 
/* 2668 */         if (freg0 > freg1)
/*      */         {
/* 2670 */           zf = pf = cf = 0;
/* 2671 */           flagStatus &= 0xFFFFFFBA;
/* 2672 */         } else if (freg0 < freg1)
/*      */         {
/* 2674 */           zf = pf = 0;
/* 2675 */           cf = true;
/* 2676 */           flagStatus &= 0xFFFFFFBA;
/* 2677 */         } else if (freg0 == freg1)
/*      */         {
/* 2679 */           cf = pf = 0;
/* 2680 */           zf = true;
/* 2681 */           sf = false;
/* 2682 */           flagStatus &= 0xFF3A;
/*      */         }
/*      */         else {
/* 2685 */           zf = pf = cf = 1;
/* 2686 */           flagStatus &= 0xFFFFFFBA;
/*      */         }
/* 2688 */         af = false;
/* 2689 */         sf = false;
/* 2690 */         flagStatus &= 0xFF6F;
/* 2691 */         break;
/*      */       
/*      */ 
/*      */       case 73: 
/* 2695 */         freg0 = regl0;
/* 2696 */         break;
/*      */       
/*      */ 
/*      */       case 74: 
/* 2700 */         freg2 = freg0 * freg1;
/* 2701 */         break;
/*      */       
/*      */ 
/*      */       case 75: 
/* 2705 */         freg2 = freg0 + freg1;
/* 2706 */         break;
/*      */       
/*      */ 
/*      */       case 85: 
/* 2710 */         fpuData[ftop] = Math.cos(fpuData[ftop]);
/* 2711 */         cpu.fpu.setC2(false);
/* 2712 */         cpu.fpu.setC1(false);
/* 2713 */         break;
/*      */       
/*      */ 
/*      */       case 84: 
/* 2717 */         fpuData[ftop] = Math.sin(fpuData[ftop]);
/* 2718 */         cpu.fpu.setC2(false);
/* 2719 */         cpu.fpu.setC1(false);
/* 2720 */         break;
/*      */       
/*      */ 
/*      */       case 87: 
/* 2724 */         int newtop = ftop + 1 & 0x7;
/* 2725 */         double res = Math.atan(fpuData[newtop] / fpuData[ftop]);
/* 2726 */         boolean st0P = fpuData[ftop] > 0.0D;
/* 2727 */         boolean st1P = fpuData[newtop] > 0.0D;
/* 2728 */         if (st1P)
/*      */         {
/* 2730 */           if (st0P) {
/* 2731 */             fpuData[newtop] = res;
/*      */           } else {
/* 2733 */             fpuData[newtop] = (3.141592653589793D + res);
/*      */           }
/*      */           
/*      */         }
/* 2737 */         else if (st0P) {
/* 2738 */           fpuData[newtop] = res;
/*      */         } else {
/* 2740 */           fpuData[newtop] = (3.141592653589793D + res);
/*      */         }
/* 2742 */         ftop = newtop;
/* 2743 */         cpu.fpu.setC1(false);
/* 2744 */         break;
/*      */       
/*      */ 
/*      */       case 86: 
/* 2748 */         fpuData[ftop] = Math.tan(fpuData[ftop]);
/* 2749 */         ftop = ftop - 1 & 0x7;
/* 2750 */         fpuData[ftop] = 1.0D;
/* 2751 */         cpu.fpu.setC2(false);
/* 2752 */         break;
/*      */       
/*      */ 
/*      */       case 81: 
/* 2756 */         fpuData[ftop] = (Math.pow(2.0D, fpuData[ftop]) - 1.0D);
/* 2757 */         break;
/*      */       
/*      */ 
/*      */       case 80: 
/* 2761 */         fpuData[ftop] *= Math.pow(2.0D, Processor.scaleTowardsZero(fpuData[(ftop + 1 & 0x7)]));
/* 2762 */         break;
/*      */       
/*      */ 
/*      */       case 83: 
/* 2766 */         fpuData[(ftop - 1 & 0x7)] = Math.cos(fpuData[ftop]);
/* 2767 */         fpuData[ftop] = Math.sin(fpuData[ftop]);
/* 2768 */         ftop = decFtop(ftop);
/* 2769 */         cpu.fpu.setC2(false);
/* 2770 */         cpu.fpu.setC1(false);
/* 2771 */         break;
/*      */       
/*      */ 
/*      */       case 82: 
/* 2775 */         int RC = cpu.fpu.getRC();
/* 2776 */         switch (RC)
/*      */         {
/*      */         case 0: 
/* 2779 */           fpuData[ftop] = Math.rint(fpuData[ftop]);
/* 2780 */           break;
/*      */         case 1: 
/* 2782 */           fpuData[ftop] = Math.floor(fpuData[ftop]);
/* 2783 */           break;
/*      */         case 2: 
/* 2785 */           fpuData[ftop] = Math.ceil(fpuData[ftop]);
/* 2786 */           break;
/*      */         case 3: 
/* 2788 */           if (fpuData[ftop] > 0.0D) {
/* 2789 */             fpuData[ftop] = Math.floor(fpuData[ftop]);
/*      */           } else {
/* 2791 */             fpuData[ftop] = Math.ceil(fpuData[ftop]);
/*      */           }
/*      */           break;
/*      */         }
/* 2795 */         break;
/*      */       
/*      */ 
/*      */       case 93: 
/* 2799 */         if (Processor.getZeroFlag(flagStatus, zf, flagResult)) {
/* 2800 */           freg2 = freg1;
/*      */         } else
/* 2802 */           freg2 = freg0;
/* 2803 */         break;
/*      */       
/*      */ 
/*      */       case 92: 
/* 2807 */         freg0 = fpuData[(ftop + 1 & 0x7)] * Math.log(fpuData[ftop]) / Math.log(2.0D);
/* 2808 */         cpu.fpu.setC1(false);
/* 2809 */         break;
/*      */       
/*      */ 
/*      */       case 95: 
/* 2813 */         if (Processor.getCarryFlag(flagStatus, cf, flagOp1, flagOp2, flagResult, flagIns)) {
/* 2814 */           freg2 = freg1;
/*      */         } else
/* 2816 */           freg2 = freg0;
/* 2817 */         break;
/*      */       
/*      */ 
/*      */       case 94: 
/* 2821 */         if (!Processor.getZeroFlag(flagStatus, zf, flagResult)) {
/* 2822 */           freg2 = freg1;
/*      */         } else
/* 2824 */           freg2 = freg0;
/* 2825 */         break;
/*      */       
/*      */ 
/*      */       case 89: 
/* 2829 */         freg2 = freg0 / freg1;
/* 2830 */         break;
/*      */       
/*      */ 
/*      */       case 88: 
/* 2834 */         fpuData[ftop] %= fpuData[(ftop + 1 & 0x7)];
/* 2835 */         break;
/*      */       
/*      */ 
/*      */       case 91: 
/* 2839 */         freg0 = fpuData[(ftop + 1 & 0x7)] * Math.log(1.0D + fpuData[ftop]) / Math.log(2.0D);
/* 2840 */         cpu.fpu.setC1(false);
/* 2841 */         break;
/*      */       
/*      */ 
/*      */       case 90: 
/* 2845 */         freg2 = freg1 / freg0;
/* 2846 */         break;
/*      */       
/*      */ 
/*      */       case 102: 
/* 2850 */         context.warn("Used FLDENV", new Object[0]);
/* 2851 */         break;
/*      */       
/*      */ 
/*      */       case 103: 
/* 2855 */         context.warn("Used FSAVE", new Object[0]);
/* 2856 */         break;
/*      */       
/*      */ 
/*      */       case 100: 
/* 2860 */         if (!Processor.getParityFlag(flagStatus, pf, flagResult)) {
/* 2861 */           freg2 = freg1;
/*      */         } else
/* 2863 */           freg2 = freg0;
/* 2864 */         break;
/*      */       
/*      */ 
/*      */       case 101: 
/* 2868 */         memory.setDoubleWord(reg0, 895);
/* 2869 */         memory.setDoubleWord(reg0 + 4, 0);
/* 2870 */         memory.setDoubleWord(reg0 + 8, 0);
/* 2871 */         memory.setDoubleWord(reg0 + 12, 0);
/* 2872 */         memory.setWord(reg0 + 16, (short)0);
/* 2873 */         memory.setWord(reg0 + 18, (short)1024);
/* 2874 */         memory.setDoubleWord(reg0 + 20, 0);
/* 2875 */         memory.setWord(reg0 + 24, (short)0);
/* 2876 */         memory.setWord(reg0 + 26, (short)0);
/* 2877 */         context.warn("Used FSTENV", new Object[0]);
/* 2878 */         break;
/*      */       
/*      */ 
/*      */       case 98: 
/* 2882 */         if ((!Processor.getCarryFlag(flagStatus, cf, flagOp1, flagOp2, flagResult, flagIns)) && (!Processor.getZeroFlag(flagStatus, zf, flagResult))) {
/* 2883 */           freg2 = freg1;
/*      */         } else
/* 2885 */           freg2 = freg0;
/* 2886 */         break;
/*      */       
/*      */ 
/*      */       case 99: 
/* 2890 */         if (Processor.getParityFlag(flagStatus, pf, flagResult)) {
/* 2891 */           freg2 = freg1;
/*      */         } else
/* 2893 */           freg2 = freg0;
/* 2894 */         break;
/*      */       
/*      */ 
/*      */       case 96: 
/* 2898 */         if (!Processor.getCarryFlag(flagStatus, cf, flagOp1, flagOp2, flagResult, flagIns)) {
/* 2899 */           freg2 = freg1;
/*      */         } else
/* 2901 */           freg2 = freg0;
/* 2902 */         break;
/*      */       
/*      */ 
/*      */       case 97: 
/* 2906 */         if ((Processor.getCarryFlag(flagStatus, cf, flagOp1, flagOp2, flagResult, flagIns)) || (Processor.getZeroFlag(flagStatus, zf, flagResult))) {
/* 2907 */           freg2 = freg1;
/*      */         } else
/* 2909 */           freg2 = freg0;
/* 2910 */         break;
/*      */       
/*      */ 
/*      */       case 110: 
/* 2914 */         ftop = 8;
/* 2915 */         context.warn("Used FINIT", new Object[0]);
/* 2916 */         break;
/*      */       
/*      */ 
/*      */       case 111: 
/* 2920 */         context.warn("Used STMXCSR", new Object[0]);
/* 2921 */         break;
/*      */       
/*      */ 
/*      */ 
/*      */       case 108: 
/* 2926 */         context.warn("WARNING: used D2BCD", new Object[0]);
/* 2927 */         break;
/*      */       
/*      */ 
/*      */ 
/*      */       case 109: 
/* 2932 */         ftop = ftop - 1 & 0x7;
/* 2933 */         context.warn("WARNING: used BCD2D", new Object[0]);
/* 2934 */         break;
/*      */       
/*      */ 
/*      */       case 106: 
/* 2938 */         reg2 = (int)freg0;
/* 2939 */         break;
/*      */       
/*      */ 
/*      */       case 107: 
/* 2943 */         regl0 = freg0;
/* 2944 */         break;
/*      */       
/*      */ 
/*      */       case 104: 
/* 2948 */         context.warn("Used FRSTOR", new Object[0]);
/* 2949 */         break;
/*      */       
/*      */ 
/*      */       case 105: 
/* 2953 */         reg2 = (short)(int)freg0;
/* 2954 */         break;
/*      */       
/*      */ 
/*      */       case 119: 
/* 2958 */         int shift = (reg1 & 0x1F) % 9;
/* 2959 */         reg2 = (reg0 | (Processor.getCarryFlag(flagStatus, cf, flagOp1, flagOp2, flagResult, flagIns) ? 256 : 0)) >>> shift | reg0 << 9 - shift;
/* 2960 */         if (reg1 > 0)
/*      */         {
/* 2962 */           cf = (reg2 & 0x100) != 0;
/* 2963 */           if (reg1 == 1)
/*      */           {
/* 2965 */             boolean bit30 = (reg2 & 0x40) != 0;
/* 2966 */             boolean bit31 = (reg2 & 0x80) != 0;
/* 2967 */             of = bit30 ^ bit31;
/* 2968 */             flagStatus &= 0xD4;
/*      */           }
/*      */           else
/*      */           {
/* 2972 */             flagStatus &= 0x8D4;
/*      */           }
/*      */         }
/* 2975 */         break;
/*      */       
/*      */ 
/*      */       case 118: 
/* 2979 */         reg1 &= 0x1F;reg2 = reg0 >>> reg1 | reg0 << 32 - reg1;
/* 2980 */         boolean bit30 = (reg2 & 0x40000000) != 0;
/* 2981 */         boolean bit31 = reg2 >> 31 != 0;
/* 2982 */         if (reg1 > 0)
/*      */         {
/* 2984 */           cf = bit31;
/* 2985 */           if (reg1 == 1)
/*      */           {
/* 2987 */             of = bit30 ^ bit31;
/* 2988 */             flagStatus &= 0xD4;
/*      */           }
/*      */           else {
/* 2991 */             flagStatus &= 0x8D4;
/*      */           }
/*      */         }
/* 2994 */         break;
/*      */       
/*      */ 
/*      */       case 117: 
/* 2998 */         int shift = reg1 & 0xF;reg2 = reg0 >>> shift | reg0 << 16 - shift;
/* 2999 */         boolean bit30 = (reg2 & 0x4000) != 0;
/* 3000 */         boolean bit31 = (reg2 & 0x8000) != 0;
/* 3001 */         if (reg1 > 0)
/*      */         {
/* 3003 */           cf = bit31;
/* 3004 */           if (reg1 == 1)
/*      */           {
/* 3006 */             of = bit30 ^ bit31;
/* 3007 */             flagStatus &= 0xD4;
/*      */           }
/*      */           else {
/* 3010 */             flagStatus &= 0x8D4;
/*      */           }
/*      */         }
/* 3013 */         break;
/*      */       
/*      */ 
/*      */       case 116: 
/* 3017 */         int shift = reg1 & 0x7;reg2 = reg0 >>> shift | reg0 << 8 - shift;
/* 3018 */         boolean bit30 = (reg2 & 0x40) != 0;
/* 3019 */         boolean bit31 = (reg2 & 0x80) != 0;
/* 3020 */         if (reg1 > 0)
/*      */         {
/* 3022 */           cf = bit31;
/* 3023 */           if (reg1 == 1)
/*      */           {
/* 3025 */             of = bit30 ^ bit31;
/* 3026 */             flagStatus &= 0xD4;
/*      */           }
/*      */           else {
/* 3029 */             flagStatus &= 0x8D4;
/*      */           }
/*      */         }
/* 3032 */         break;
/*      */       
/*      */ 
/*      */       case 115: 
/* 3036 */         reg1 &= 0x1F;reg2 = reg0 << reg1 | reg0 >>> 32 - reg1;
/* 3037 */         boolean bit0 = (reg2 & 0x1) != 0;
/* 3038 */         boolean bit31 = reg2 >> 31 != 0;
/* 3039 */         if (reg1 > 0)
/*      */         {
/* 3041 */           cf = bit0;
/* 3042 */           of = bit0 ^ bit31;
/* 3043 */           flagStatus &= 0xD4;
/*      */         }
/* 3045 */         break;
/*      */       
/*      */ 
/*      */       case 114: 
/* 3049 */         int shift = reg1 & 0xF;reg2 = reg0 << shift | reg0 >> 16 - shift;
/* 3050 */         boolean bit0 = (reg2 & 0x1) != 0;
/* 3051 */         boolean bit31 = reg2 >> 15 != 0;
/* 3052 */         if (reg1 > 0)
/*      */         {
/* 3054 */           cf = bit0;
/* 3055 */           of = bit0 ^ bit31;
/* 3056 */           flagStatus &= 0xD4;
/*      */         }
/* 3058 */         break;
/*      */       
/*      */ 
/*      */       case 113: 
/* 3062 */         int shift = reg1 & 0x7;reg2 = reg0 << shift | reg0 >> 8 - shift;
/* 3063 */         boolean bit0 = (reg2 & 0x1) != 0;
/* 3064 */         boolean bit31 = reg2 >> 7 != 0;
/* 3065 */         if (reg1 > 0)
/*      */         {
/* 3067 */           cf = bit0;
/*      */           
/*      */ 
/* 3070 */           of = bit0 ^ bit31;
/* 3071 */           flagStatus &= 0xD4;
/*      */         }
/*      */         
/*      */ 
/*      */ 
/*      */ 
/* 3077 */         break;
/*      */       
/*      */ 
/*      */       case 112: 
/* 3081 */         context.warn("Used LDMXCSR", new Object[0]);
/* 3082 */         break;
/*      */       
/*      */ 
/*      */       case 127: 
/* 3086 */         int count = reg3 & 0x1F;
/* 3087 */         if (count != 0) {
/* 3088 */           reg2 = reg0 >>> count | reg1 << 32 - count;
/*      */         } else
/* 3090 */           reg2 = reg0;
/* 3091 */         reg1 = reg3;
/* 3092 */         break;
/*      */       
/*      */ 
/*      */       case 126: 
/* 3096 */         int count = reg3 & 0x1F;
/* 3097 */         if (count != 0) {
/* 3098 */           if (count < 16) {
/* 3099 */             reg2 = (short)(reg0 >>> count | reg1 << 16 - count);
/*      */           }
/*      */           else {
/* 3102 */             reg2 = (short)(reg1 >>> count - 16 | reg0 << 32 - count);
/* 3103 */             reg0 = reg1;
/* 3104 */             reg3 -= 16;
/*      */           }
/*      */         } else
/* 3107 */           reg2 = reg0;
/* 3108 */         reg1 = reg3;
/* 3109 */         break;
/*      */       
/*      */ 
/*      */       case 125: 
/* 3113 */         reg2 = reg0 >>> reg1;
/* 3114 */         break;
/*      */       
/*      */ 
/*      */       case 124: 
/* 3118 */         reg1 &= 0x1F;
/* 3119 */         long reg0l = 0xFFFFFFFF & reg0 | (Processor.getCarryFlag(flagStatus, cf, flagOp1, flagOp2, flagResult, flagIns) ? 4294967296L : 0L);
/* 3120 */         reg2 = (int)(reg0l = reg0l << reg1 | reg0l >>> 33 - reg1);
/* 3121 */         if (reg1 > 0)
/*      */         {
/* 3123 */           cf = (reg0l & 0x100000000) != 0L;
/* 3124 */           if (reg1 == 1)
/*      */           {
/* 3126 */             boolean bit31 = reg2 >> 31 != 0;
/* 3127 */             of = cf ^ bit31;
/* 3128 */             flagStatus &= 0xD4;
/*      */           }
/*      */           else
/*      */           {
/* 3132 */             flagStatus &= 0x8D4;
/*      */           }
/*      */         }
/* 3135 */         break;
/*      */       
/*      */ 
/*      */       case 123: 
/* 3139 */         int shift = (reg1 & 0x1F) % 17;
/* 3140 */         int cfreg0 = reg0 | (Processor.getCarryFlag(flagStatus, cf, flagOp1, flagOp2, flagResult, flagIns) ? 65536 : 0);
/* 3141 */         reg2 = cfreg0 << shift | cfreg0 >> 17 - shift;
/* 3142 */         if (reg1 > 0)
/*      */         {
/* 3144 */           cf = (reg2 & 0x10000) != 0;
/* 3145 */           if (reg1 == 1)
/*      */           {
/* 3147 */             boolean bit31 = (reg2 & 0x8000) != 0;
/* 3148 */             of = cf ^ bit31;
/* 3149 */             flagStatus &= 0xD4;
/*      */           }
/*      */           else
/*      */           {
/* 3153 */             flagStatus &= 0x8D4;
/*      */           }
/*      */         }
/* 3156 */         break;
/*      */       
/*      */ 
/*      */       case 122: 
/* 3160 */         int shift = (reg1 & 0x1F) % 9;
/* 3161 */         int cfreg0 = reg0 | (Processor.getCarryFlag(flagStatus, cf, flagOp1, flagOp2, flagResult, flagIns) ? 256 : 0);
/* 3162 */         reg2 = cfreg0 << shift | cfreg0 >> 9 - shift;
/* 3163 */         if (reg1 > 0)
/*      */         {
/* 3165 */           cf = (reg2 & 0x100) != 0;
/* 3166 */           if (reg1 == 1)
/*      */           {
/* 3168 */             boolean bit31 = (reg2 & 0x80) != 0;
/* 3169 */             of = cf ^ bit31;
/* 3170 */             flagStatus &= 0xD4;
/*      */           }
/*      */           else
/*      */           {
/* 3174 */             flagStatus &= 0x8D4;
/*      */           }
/*      */         }
/* 3177 */         break;
/*      */       
/*      */ 
/*      */       case 121: 
/* 3181 */         int shift = reg1 & 0x1F;long reg0l = 0xFFFFFFFF & reg0 | (Processor.getCarryFlag(flagStatus, cf, flagOp1, flagOp2, flagResult, flagIns) ? 4294967296L : 0L);
/* 3182 */         reg2 = (int)(reg0l = reg0l >>> shift | reg0l << 33 - shift);
/* 3183 */         if (reg1 > 0)
/*      */         {
/* 3185 */           cf = (reg0l & 0x100000000) != 0L;
/* 3186 */           if (reg1 == 1)
/*      */           {
/* 3188 */             boolean bit30 = (reg2 & 0x40000000) != 0;
/* 3189 */             boolean bit31 = (reg2 & 0x80000000) != 0;
/* 3190 */             of = bit30 ^ bit31;
/* 3191 */             flagStatus &= 0xD4;
/*      */           }
/*      */           else
/*      */           {
/* 3195 */             flagStatus &= 0x8D4;
/*      */           }
/*      */         }
/* 3198 */         break;
/*      */       
/*      */ 
/*      */       case 120: 
/* 3202 */         int shift = (reg1 & 0x1F) % 17;
/* 3203 */         reg2 = (reg0 | (Processor.getCarryFlag(flagStatus, cf, flagOp1, flagOp2, flagResult, flagIns) ? 65536 : 0)) >>> shift | reg0 << 17 - shift;
/* 3204 */         if (reg1 > 0)
/*      */         {
/* 3206 */           cf = (reg2 & 0x10000) != 0;
/* 3207 */           if (reg1 == 1)
/*      */           {
/* 3209 */             boolean bit30 = (reg2 & 0x4000) != 0;
/* 3210 */             boolean bit31 = (reg2 & 0x8000) != 0;
/* 3211 */             of = bit30 ^ bit31;
/* 3212 */             flagStatus &= 0xD4;
/*      */           }
/*      */           else
/*      */           {
/* 3216 */             flagStatus &= 0x8D4;
/*      */           }
/*      */         }
/* 3219 */         break;
/*      */       
/*      */ 
/*      */       case 137: 
/* 3223 */         reg2 = (byte)(reg0 - (reg1 + (Processor.getCarryFlag(flagStatus, cf, flagOp1, flagOp2, flagResult, flagIns) ? 1 : 0)));
/* 3224 */         break;
/*      */       
/*      */ 
/*      */       case 136: 
/* 3228 */         reg2 = reg0 - reg1;
/* 3229 */         break;
/*      */       
/*      */ 
/*      */       case 139: 
/* 3233 */         reg2 = reg0 - (reg1 + (Processor.getCarryFlag(flagStatus, cf, flagOp1, flagOp2, flagResult, flagIns) ? 1 : 0));
/* 3234 */         break;
/*      */       
/*      */ 
/*      */       case 138: 
/* 3238 */         reg2 = (short)(reg0 - (reg1 + (Processor.getCarryFlag(flagStatus, cf, flagOp1, flagOp2, flagResult, flagIns) ? 1 : 0)));
/* 3239 */         break;
/*      */       
/*      */ 
/*      */       case 143: 
/* 3243 */         reg2 = (short)-(short)reg0;
/* 3244 */         break;
/*      */       
/*      */ 
/*      */       case 142: 
/* 3248 */         reg2 = (byte)-(byte)reg0;
/* 3249 */         break;
/*      */       
/*      */ 
/*      */       case 129: 
/* 3253 */         reg2 = (short)(reg0 + reg1);
/* 3254 */         break;
/*      */       
/*      */ 
/*      */       case 128: 
/* 3258 */         reg2 = (byte)(reg0 + reg1);
/* 3259 */         break;
/*      */       
/*      */ 
/*      */       case 131: 
/* 3263 */         reg2 = (byte)(reg0 + reg1 + (Processor.getCarryFlag(flagStatus, cf, flagOp1, flagOp2, flagResult, flagIns) ? 1 : 0));
/* 3264 */         break;
/*      */       
/*      */ 
/*      */       case 130: 
/* 3268 */         reg2 = reg0 + reg1;
/* 3269 */         break;
/*      */       
/*      */ 
/*      */       case 133: 
/* 3273 */         reg2 = reg0 + reg1 + (Processor.getCarryFlag(flagStatus, cf, flagOp1, flagOp2, flagResult, flagIns) ? 1 : 0);
/* 3274 */         break;
/*      */       
/*      */ 
/*      */       case 132: 
/* 3278 */         reg2 = (short)(reg0 + reg1 + (Processor.getCarryFlag(flagStatus, cf, flagOp1, flagOp2, flagResult, flagIns) ? 1 : 0));
/* 3279 */         break;
/*      */       
/*      */ 
/*      */       case 135: 
/* 3283 */         reg2 = (short)(reg0 - reg1);
/* 3284 */         break;
/*      */       
/*      */ 
/*      */       case 134: 
/* 3288 */         reg2 = (byte)(reg0 - reg1);
/* 3289 */         break;
/*      */       
/*      */ 
/*      */       case 152: 
/* 3293 */         reg3 &= 0x1F;
/* 3294 */         if (reg3 > 0) {
/* 3295 */           reg2 = reg0 << reg3 | reg1 >>> 32 - reg3;
/*      */         } else
/* 3297 */           reg2 = reg0;
/* 3298 */         reg1 = reg3;
/* 3299 */         break;
/*      */       
/*      */ 
/*      */       case 153: 
/* 3303 */         reg2 = (short)(byte)reg0 * (byte)reg1;
/* 3304 */         break;
/*      */       
/*      */ 
/*      */       case 154: 
/* 3308 */         int res32 = (short)reg0 * (short)reg1;
/* 3309 */         reg2 = (short)res32;
/* 3310 */         reg3 = (short)(res32 >> 16);
/* 3311 */         break;
/*      */       
/*      */ 
/*      */       case 155: 
/* 3315 */         long res64 = reg0 * reg1;
/* 3316 */         reg2 = (int)res64;
/* 3317 */         reg3 = (int)(res64 >> 32);
/* 3318 */         break;
/*      */       
/*      */ 
/*      */       case 156: 
/* 3322 */         int res16 = (reg0 & 0xFF) * (eax & 0xFF);
/* 3323 */         eax = eax & 0xFFFF0000 | res16;
/* 3324 */         reg2 = (short)res16;
/* 3325 */         af = false;
/* 3326 */         cf = of = (eax & 0xFF00) != 0 ? 1 : 0;
/* 3327 */         break;
/*      */       
/*      */ 
/*      */       case 157: 
/* 3331 */         int res32 = (reg0 & 0xFFFF) * (eax & 0xFFFF);
/* 3332 */         eax = eax & 0xFFFF0000 | res32 & 0xFFFF;
/* 3333 */         edx = edx & 0xFFFF0000 | res32 >>> 16;
/* 3334 */         reg2 = res32;
/* 3335 */         af = false;
/* 3336 */         cf = of = (edx & 0xFFFF) != 0 ? 1 : 0;
/* 3337 */         break;
/*      */       
/*      */ 
/*      */       case 158: 
/* 3341 */         long res64 = (reg0 & 0xFFFFFFFF) * (eax & 0xFFFFFFFF);
/* 3342 */         eax = (int)res64;
/* 3343 */         edx = (int)(res64 >> 32);
/* 3344 */         reg2 = eax;
/* 3345 */         af = false;
/* 3346 */         cf = of = edx != 0 ? 1 : 0;
/* 3347 */         break;
/*      */       
/*      */ 
/*      */       case 159: 
/* 3351 */         int op1 = eax & 0xFFFF;
/* 3352 */         reg2 = op1 / reg0;
/* 3353 */         reg3 = op1 % reg0;
/* 3354 */         break;
/*      */       
/*      */ 
/*      */       case 144: 
/* 3358 */         reg2 = -reg0;
/* 3359 */         break;
/*      */       
/*      */ 
/*      */       case 145: 
/* 3363 */         reg1 &= 0x1F;
/* 3364 */         reg2 = reg0 >> reg1;
/* 3365 */         break;
/*      */       
/*      */ 
/*      */       case 146: 
/* 3369 */         reg1 &= 0x1F;
/* 3370 */         reg2 = reg0 >> reg1;
/* 3371 */         break;
/*      */       
/*      */ 
/*      */       case 147: 
/* 3375 */         reg1 &= 0x1F;
/* 3376 */         reg2 = reg0 >> reg1;
/* 3377 */         break;
/*      */       
/*      */ 
/*      */       case 148: 
/* 3381 */         reg1 &= 0x1F;
/* 3382 */         reg2 = (byte)(reg0 << reg1);
/* 3383 */         break;
/*      */       
/*      */ 
/*      */       case 149: 
/* 3387 */         reg1 &= 0x1F;
/* 3388 */         reg2 = (short)(reg0 << reg1);
/* 3389 */         break;
/*      */       
/*      */ 
/*      */       case 150: 
/* 3393 */         reg1 &= 0x1F;
/* 3394 */         reg2 = reg0 << reg1;
/* 3395 */         break;
/*      */       
/*      */ 
/*      */       case 151: 
/* 3399 */         reg3 &= 0x1F;
/* 3400 */         if (reg3 < 16) {
/* 3401 */           reg2 = (short)(reg0 << reg3 | reg1 >>> 16 - reg3);
/*      */         } else
/* 3403 */           reg2 = (short)(reg1 << reg3 - 16 | reg0 >>> 32 - reg3);
/* 3404 */         if (reg3 > 16)
/* 3405 */           reg0 = reg1;
/* 3406 */         reg1 = reg3;
/* 3407 */         break;
/*      */       
/*      */ 
/*      */       case 171: 
/* 3411 */         reg2 = (!Processor.getZeroFlag(flagStatus, zf, flagResult)) && (!Processor.getCarryFlag(flagStatus, cf, flagOp1, flagOp2, flagResult, flagIns)) ? 1 : 0;
/* 3412 */         break;
/*      */       
/*      */ 
/*      */       case 170: 
/* 3416 */         reg2 = Processor.getSignFlag(flagStatus, sf, flagResult) != Processor.getOverflowFlag(flagStatus, of, flagOp1, flagOp2, flagResult, flagIns) ? 1 : 0;
/* 3417 */         break;
/*      */       
/*      */ 
/*      */       case 169: 
/* 3421 */         reg2 = (Processor.getZeroFlag(flagStatus, zf, flagResult)) || (Processor.getSignFlag(flagStatus, sf, flagResult) != Processor.getOverflowFlag(flagStatus, of, flagOp1, flagOp2, flagResult, flagIns)) ? 1 : 0;
/* 3422 */         break;
/*      */       
/*      */ 
/*      */       case 168: 
/* 3426 */         reg2 = (!Processor.getZeroFlag(flagStatus, zf, flagResult)) && (Processor.getSignFlag(flagStatus, sf, flagResult) == Processor.getOverflowFlag(flagStatus, of, flagOp1, flagOp2, flagResult, flagIns)) ? 1 : 0;
/* 3427 */         break;
/*      */       
/*      */ 
/*      */       case 175: 
/* 3431 */         reg2 = Processor.getOverflowFlag(flagStatus, of, flagOp1, flagOp2, flagResult, flagIns) ? 1 : 0;
/* 3432 */         break;
/*      */       
/*      */ 
/*      */       case 174: 
/* 3436 */         reg2 = (Processor.getZeroFlag(flagStatus, zf, flagResult)) || (Processor.getCarryFlag(flagStatus, cf, flagOp1, flagOp2, flagResult, flagIns)) ? 1 : 0;
/* 3437 */         break;
/*      */       
/*      */ 
/*      */       case 173: 
/* 3441 */         reg2 = Processor.getCarryFlag(flagStatus, cf, flagOp1, flagOp2, flagResult, flagIns) ? 1 : 0;
/* 3442 */         break;
/*      */       
/*      */ 
/*      */       case 172: 
/* 3446 */         reg2 = !Processor.getCarryFlag(flagStatus, cf, flagOp1, flagOp2, flagResult, flagIns) ? 1 : 0;
/* 3447 */         break;
/*      */       
/*      */ 
/*      */       case 163: 
/* 3451 */         int op1 = (edx & 0xFFFF) << 16 | 0xFFFF & eax;
/* 3452 */         reg2 = (short)(op1 / (short)reg0);
/* 3453 */         reg3 = (short)(op1 % (short)reg0);
/* 3454 */         break;
/*      */       
/*      */ 
/*      */       case 162: 
/* 3458 */         short op1 = (short)eax;
/* 3459 */         reg2 = (byte)(op1 / (byte)reg0);
/* 3460 */         reg3 = (byte)(op1 % (byte)reg0);
/* 3461 */         break;
/*      */       
/*      */ 
/*      */       case 161: 
/* 3465 */         long op1 = edx << 32 | 0xFFFFFFFF & eax;
/* 3466 */         reg2 = (int)(op1 / (0xFFFFFFFF & reg0));
/* 3467 */         reg3 = (int)(op1 % (0xFFFFFFFF & reg0));
/* 3468 */         break;
/*      */       
/*      */ 
/*      */       case 160: 
/* 3472 */         int op1 = (edx & 0xFFFF) << 16 | 0xFFFF & eax;
/* 3473 */         reg2 = op1 / reg0;
/* 3474 */         reg3 = op1 % reg0;
/* 3475 */         break;
/*      */       
/*      */ 
/*      */       case 167: 
/* 3479 */         reg2 = Processor.getSignFlag(flagStatus, sf, flagResult) == Processor.getOverflowFlag(flagStatus, of, flagOp1, flagOp2, flagResult, flagIns) ? 1 : 0;
/* 3480 */         break;
/*      */       
/*      */ 
/*      */       case 166: 
/* 3484 */         reg2 = Processor.getZeroFlag(flagStatus, zf, flagResult) ? 1 : 0;
/* 3485 */         break;
/*      */       
/*      */ 
/*      */       case 165: 
/* 3489 */         reg2 = Processor.getZeroFlag(flagStatus, zf, flagResult) ? 0 : 1;
/* 3490 */         break;
/*      */       
/*      */ 
/*      */       case 164: 
/* 3494 */         long op1 = edx << 32 | 0xFFFFFFFF & eax;
/* 3495 */         reg2 = (int)(op1 / reg0);
/* 3496 */         reg3 = (int)(op1 % reg0);
/* 3497 */         break;
/*      */       
/*      */ 
/*      */       case 186: 
/* 3501 */         if (reg1 != 0)
/*      */         {
/* 3503 */           flagIns = uops[(pos++)];
/* 3504 */           flagOp1 = reg0;
/* 3505 */           flagOp2 = reg1;
/* 3506 */           flagResult = reg2;
/* 3507 */           flagStatus = 2261;
/*      */         }
/*      */         
/*      */ 
/*      */         break;
/*      */       case 187: 
/* 3513 */         if (reg3 != 0)
/*      */         {
/* 3515 */           flagIns = uops[(pos++)];
/* 3516 */           flagOp1 = reg0;
/* 3517 */           flagOp2 = reg1;
/* 3518 */           flagResult = reg2;
/* 3519 */           flagStatus = 2261;
/*      */         }
/*      */         
/*      */ 
/*      */         break;
/*      */       case 184: 
/* 3525 */         flagIns = uops[(pos++)];
/* 3526 */         flagOp1 = reg0;
/* 3527 */         flagOp2 = reg1;
/* 3528 */         flagResult = reg2;
/* 3529 */         flagStatus = 2261;
/* 3530 */         of = sf = zf = af = pf = cf = 0;
/* 3531 */         break;
/*      */       
/*      */ 
/*      */       case 185: 
/* 3535 */         flagIns = 155;
/* 3536 */         flagOp1 = reg0;
/* 3537 */         flagOp2 = reg1;
/* 3538 */         flagResult = reg2;
/* 3539 */         flagStatus = 2261;
/* 3540 */         if (reg3 < 0) {
/* 3541 */           sf = true;
/*      */         } else
/* 3543 */           sf = false;
/* 3544 */         flagStatus &= 0xFF7F;
/* 3545 */         break;
/*      */       
/*      */ 
/*      */       case 190: 
/* 3549 */         of = cf = af = 0;
/* 3550 */         flagResult = reg2;
/* 3551 */         flagStatus = 196;
/* 3552 */         flagOp1 = flagOp2 = flagIns = 0;
/* 3553 */         sf = zf = pf = 0;
/* 3554 */         break;
/*      */       
/*      */ 
/*      */       case 191: 
/* 3558 */         of = cf = af = 0;
/* 3559 */         flagResult = (short)reg2;
/* 3560 */         flagStatus = 196;
/* 3561 */         flagOp1 = flagOp2 = flagIns = 0;
/* 3562 */         break;
/*      */       
/*      */ 
/*      */       case 188: 
/* 3566 */         cf = Processor.getCarryFlag(flagStatus, cf, flagOp1, flagOp2, flagResult, flagIns);
/* 3567 */         flagOp1 = reg0;
/* 3568 */         flagOp2 = reg1;
/* 3569 */         flagResult = reg2;
/* 3570 */         flagIns = uops[(pos++)];
/* 3571 */         flagStatus = 2260;
/* 3572 */         break;
/*      */       
/*      */ 
/*      */       case 189: 
/* 3576 */         flagResult = reg2;
/* 3577 */         flagStatus = 196;
/* 3578 */         break;
/*      */       
/*      */ 
/*      */       case 178: 
/* 3582 */         reg2 = Processor.getParityFlag(flagStatus, pf, flagResult) ? 1 : 0;
/* 3583 */         break;
/*      */       
/*      */ 
/*      */       case 179: 
/* 3587 */         reg2 = Processor.getSignFlag(flagStatus, sf, flagResult) ? 1 : 0;
/* 3588 */         break;
/*      */       
/*      */ 
/*      */       case 176: 
/* 3592 */         reg2 = !Processor.getOverflowFlag(flagStatus, of, flagOp1, flagOp2, flagResult, flagIns) ? 1 : 0;
/* 3593 */         break;
/*      */       
/*      */ 
/*      */       case 177: 
/* 3597 */         reg2 = Processor.getParityFlag(flagStatus, pf, flagResult) ? 0 : 1;
/* 3598 */         break;
/*      */       
/*      */ 
/*      */       case 182: 
/* 3602 */         cpu.df = true;
/* 3603 */         break;
/*      */       
/*      */ 
/*      */       case 183: 
/* 3607 */         byte f = (byte)((eax & 0xFF00) >> 8);
/* 3608 */         flagStatus &= 0x800;
/* 3609 */         sf = (f & 0x80) != 0;
/* 3610 */         zf = (f & 0x40) != 0;
/* 3611 */         af = (f & 0x10) != 0;
/* 3612 */         pf = (f & 0x4) != 0;
/* 3613 */         cf = (f & 0x1) != 0;
/* 3614 */         break;
/*      */       
/*      */ 
/*      */       case 180: 
/* 3618 */         reg2 = !Processor.getSignFlag(flagStatus, sf, flagResult) ? 1 : 0;
/* 3619 */         break;
/*      */       
/*      */ 
/*      */       case 181: 
/* 3623 */         cpu.df = false;
/* 3624 */         break;
/*      */       
/*      */ 
/*      */       case 205: 
/* 3628 */         eip = memory.getDoubleWord(esp);
/* 3629 */         esp += 4 + uops[(pos++)];
/* 3630 */         synchronize(context, eax, ebx, ecx, edx, esi, edi, esp, ebp, eip, of, sf, zf, af, pf, cf, flagOp1, flagOp2, flagResult, flagStatus, flagIns, ftop);
/* 3631 */         return true;
/*      */       
/*      */ 
/*      */       case 204: 
/* 3635 */         eip = memory.getDoubleWord(esp);
/* 3636 */         esp += 4;
/* 3637 */         synchronize(context, eax, ebx, ecx, edx, esi, edi, esp, ebp, eip, of, sf, zf, af, pf, cf, flagOp1, flagOp2, flagResult, flagStatus, flagIns, ftop);
/* 3638 */         return true;
/*      */       
/*      */ 
/*      */       case 207: 
/* 3642 */         eip = reg0;
/* 3643 */         break;
/*      */       
/*      */ 
/*      */       case 206: 
/* 3647 */         eip += uops[(pos++)];
/* 3648 */         break;
/*      */       
/*      */ 
/*      */       case 201: 
/* 3652 */         esp -= 4;
/* 3653 */         memory.setDoubleWord(esp, eip);
/* 3654 */         eip = ebx;
/* 3655 */         synchronize(context, eax, ebx, ecx, edx, esi, edi, esp, ebp, eip, of, sf, zf, af, pf, cf, flagOp1, flagOp2, flagResult, flagStatus, flagIns, ftop);
/* 3656 */         context.call();
/* 3657 */         break;
/*      */       
/*      */ 
/*      */       case 200: 
/* 3661 */         esp -= 4;
/* 3662 */         memory.setDoubleWord(esp, eip);
/* 3663 */         eip = eax;
/* 3664 */         synchronize(context, eax, ebx, ecx, edx, esi, edi, esp, ebp, eip, of, sf, zf, af, pf, cf, flagOp1, flagOp2, flagResult, flagStatus, flagIns, ftop);
/* 3665 */         context.call();
/* 3666 */         break;
/*      */       
/*      */ 
/*      */       case 203: 
/* 3670 */         esp -= 4;
/* 3671 */         memory.setDoubleWord(esp, eip);
/* 3672 */         eip = reg0;
/* 3673 */         synchronize(context, eax, ebx, ecx, edx, esi, edi, esp, ebp, eip, of, sf, zf, af, pf, cf, flagOp1, flagOp2, flagResult, flagStatus, flagIns, ftop);
/* 3674 */         context.call();
/* 3675 */         break;
/*      */       
/*      */ 
/*      */       case 202: 
/* 3679 */         esp -= 4;
/* 3680 */         memory.setDoubleWord(esp, eip);
/* 3681 */         eip = ebp;
/* 3682 */         synchronize(context, eax, ebx, ecx, edx, esi, edi, esp, ebp, eip, of, sf, zf, af, pf, cf, flagOp1, flagOp2, flagResult, flagStatus, flagIns, ftop);
/* 3683 */         context.call();
/* 3684 */         break;
/*      */       
/*      */ 
/*      */       case 197: 
/* 3688 */         esp -= 4;
/* 3689 */         memory.setDoubleWord(esp, eip);
/* 3690 */         eip = edx;
/* 3691 */         synchronize(context, eax, ebx, ecx, edx, esi, edi, esp, ebp, eip, of, sf, zf, af, pf, cf, flagOp1, flagOp2, flagResult, flagStatus, flagIns, ftop);
/* 3692 */         context.call();
/* 3693 */         break;
/*      */       
/*      */ 
/*      */       case 196: 
/* 3697 */         esp -= 4;
/* 3698 */         memory.setDoubleWord(esp, eip);
/* 3699 */         eip = ecx;
/* 3700 */         synchronize(context, eax, ebx, ecx, edx, esi, edi, esp, ebp, eip, of, sf, zf, af, pf, cf, flagOp1, flagOp2, flagResult, flagStatus, flagIns, ftop);
/* 3701 */         context.call();
/* 3702 */         break;
/*      */       
/*      */ 
/*      */       case 199: 
/* 3706 */         esp -= 4;
/* 3707 */         memory.setDoubleWord(esp, eip);
/* 3708 */         eip = edi;
/* 3709 */         synchronize(context, eax, ebx, ecx, edx, esi, edi, esp, ebp, eip, of, sf, zf, af, pf, cf, flagOp1, flagOp2, flagResult, flagStatus, flagIns, ftop);
/* 3710 */         context.call();
/* 3711 */         break;
/*      */       
/*      */ 
/*      */       case 198: 
/* 3715 */         esp -= 4;
/* 3716 */         memory.setDoubleWord(esp, eip);
/* 3717 */         eip = esi;
/* 3718 */         synchronize(context, eax, ebx, ecx, edx, esi, edi, esp, ebp, eip, of, sf, zf, af, pf, cf, flagOp1, flagOp2, flagResult, flagStatus, flagIns, ftop);
/* 3719 */         context.call();
/* 3720 */         break;
/*      */       
/*      */ 
/*      */       case 193: 
/* 3724 */         esp = ebp;
/* 3725 */         ebp = memory.getDoubleWord(esp);
/* 3726 */         esp += 4;
/* 3727 */         break;
/*      */       
/*      */ 
/*      */       case 192: 
/* 3731 */         of = cf = af = 0;
/* 3732 */         flagResult = (byte)reg2;
/* 3733 */         flagStatus = 196;
/* 3734 */         flagOp1 = flagOp2 = flagIns = 0;
/* 3735 */         break;
/*      */       
/*      */ 
/*      */       case 195: 
/* 3739 */         esp -= 4;
/* 3740 */         memory.setDoubleWord(esp, eip);
/* 3741 */         eip += uops[(pos++)];
/* 3742 */         synchronize(context, eax, ebx, ecx, edx, esi, edi, esp, ebp, eip, of, sf, zf, af, pf, cf, flagOp1, flagOp2, flagResult, flagStatus, flagIns, ftop);
/* 3743 */         context.call();
/* 3744 */         break;
/*      */       
/*      */ 
/*      */       case 194: 
/* 3748 */         int pendingSignal = uops[(pos++)] & 0xFF;
/* 3749 */         if (pendingSignal == 128)
/*      */         {
/* 3751 */           eax = SystemCalls.syscall(memory, cpu, iolib, eax, ebx, ecx, edx, esi, edi, esp, ebp);
/*      */         } else
/* 3753 */           System.out.println("UNKNOWN signal: " + pendingSignal);
/* 3754 */         break;
/*      */       
/*      */ 
/*      */       case 220: 
/* 3758 */         if (!Processor.getOverflowFlag(flagStatus, of, flagOp1, flagOp2, flagResult, flagIns)) {
/* 3759 */           eip += uops[(pos++)];
/*      */         } else
/* 3761 */           pos++;
/* 3762 */         break;
/*      */       
/*      */ 
/*      */       case 221: 
/* 3766 */         if (Processor.getParityFlag(flagStatus, pf, flagResult)) {
/* 3767 */           eip += uops[(pos++)];
/*      */         } else
/* 3769 */           pos++;
/* 3770 */         break;
/*      */       
/*      */ 
/*      */       case 222: 
/* 3774 */         if (!Processor.getParityFlag(flagStatus, pf, flagResult)) {
/* 3775 */           eip += uops[(pos++)];
/*      */         } else
/* 3777 */           pos++;
/* 3778 */         break;
/*      */       
/*      */ 
/*      */       case 223: 
/* 3782 */         if ((!Processor.getZeroFlag(flagStatus, zf, flagResult)) && (Processor.getSignFlag(flagStatus, sf, flagResult) == Processor.getOverflowFlag(flagStatus, of, flagOp1, flagOp2, flagResult, flagIns))) {
/* 3783 */           eip += uops[(pos++)];
/*      */         } else
/* 3785 */           pos++;
/* 3786 */         break;
/*      */       
/*      */ 
/*      */       case 216: 
/* 3790 */         if (!Processor.getZeroFlag(flagStatus, zf, flagResult)) {
/* 3791 */           eip += uops[(pos++)];
/*      */         } else
/* 3793 */           pos++;
/* 3794 */         break;
/*      */       
/*      */ 
/*      */       case 217: 
/* 3798 */         if (Processor.getSignFlag(flagStatus, sf, flagResult) != Processor.getOverflowFlag(flagStatus, of, flagOp1, flagOp2, flagResult, flagIns)) {
/* 3799 */           eip += uops[(pos++)];
/*      */         } else
/* 3801 */           pos++;
/* 3802 */         break;
/*      */       
/*      */ 
/*      */       case 218: 
/* 3806 */         if ((Processor.getZeroFlag(flagStatus, zf, flagResult)) || (Processor.getSignFlag(flagStatus, sf, flagResult) != Processor.getOverflowFlag(flagStatus, of, flagOp1, flagOp2, flagResult, flagIns))) {
/* 3807 */           eip += uops[(pos++)];
/*      */         } else
/* 3809 */           pos++;
/* 3810 */         break;
/*      */       
/*      */ 
/*      */       case 219: 
/* 3814 */         if (Processor.getOverflowFlag(flagStatus, of, flagOp1, flagOp2, flagResult, flagIns)) {
/* 3815 */           eip += uops[(pos++)];
/*      */         } else
/* 3817 */           pos++;
/* 3818 */         break;
/*      */       
/*      */ 
/*      */       case 212: 
/* 3822 */         if (Processor.getCarryFlag(flagStatus, cf, flagOp1, flagOp2, flagResult, flagIns)) {
/* 3823 */           eip += uops[(pos++)];
/*      */         } else
/* 3825 */           pos++;
/* 3826 */         break;
/*      */       
/*      */ 
/*      */       case 213: 
/* 3830 */         if ((Processor.getCarryFlag(flagStatus, cf, flagOp1, flagOp2, flagResult, flagIns)) || (Processor.getZeroFlag(flagStatus, zf, flagResult))) {
/* 3831 */           eip += uops[(pos++)];
/*      */         } else
/* 3833 */           pos++;
/* 3834 */         break;
/*      */       
/*      */ 
/*      */       case 214: 
/* 3838 */         if ((ecx & 0xFFFF) == 0) {
/* 3839 */           eip += uops[(pos++)];
/*      */         } else
/* 3841 */           pos++;
/* 3842 */         break;
/*      */       
/*      */ 
/*      */       case 215: 
/* 3846 */         if (Processor.getZeroFlag(flagStatus, zf, flagResult)) {
/* 3847 */           eip += uops[(pos++)];
/*      */         } else
/* 3849 */           pos++;
/* 3850 */         break;
/*      */       
/*      */ 
/*      */       case 208: 
/* 3854 */         if (Processor.getSignFlag(flagStatus, sf, flagResult)) {
/* 3855 */           eip += uops[(pos++)];
/*      */         } else
/* 3857 */           pos++;
/* 3858 */         break;
/*      */       
/*      */ 
/*      */       case 209: 
/* 3862 */         if (!Processor.getSignFlag(flagStatus, sf, flagResult)) {
/* 3863 */           eip += uops[(pos++)];
/*      */         } else
/* 3865 */           pos++;
/* 3866 */         break;
/*      */       
/*      */ 
/*      */       case 210: 
/* 3870 */         if ((!Processor.getCarryFlag(flagStatus, cf, flagOp1, flagOp2, flagResult, flagIns)) && (!Processor.getZeroFlag(flagStatus, zf, flagResult))) {
/* 3871 */           eip += uops[(pos++)];
/*      */         } else
/* 3873 */           pos++;
/* 3874 */         break;
/*      */       
/*      */ 
/*      */       case 211: 
/* 3878 */         if (!Processor.getCarryFlag(flagStatus, cf, flagOp1, flagOp2, flagResult, flagIns)) {
/* 3879 */           eip += uops[(pos++)];
/*      */         } else
/* 3881 */           pos++;
/* 3882 */         break;
/*      */       
/*      */ 
/*      */       case 239: 
/* 3886 */         eax = memory.getDoubleWord(esp);
/* 3887 */         esp += 4;
/* 3888 */         break;
/*      */       
/*      */ 
/*      */       case 237: 
/* 3892 */         esp -= 4;
/* 3893 */         memory.setDoubleWord(esp, ebp);
/* 3894 */         break;
/*      */       
/*      */ 
/*      */       case 236: 
/* 3898 */         esp -= 4;
/* 3899 */         memory.setDoubleWord(esp, esp + 4);
/* 3900 */         break;
/*      */       
/*      */ 
/*      */       case 235: 
/* 3904 */         esp -= 4;
/* 3905 */         memory.setDoubleWord(esp, edi);
/* 3906 */         break;
/*      */       
/*      */ 
/*      */       case 234: 
/* 3910 */         esp -= 4;
/* 3911 */         memory.setDoubleWord(esp, esi);
/* 3912 */         break;
/*      */       
/*      */ 
/*      */       case 233: 
/* 3916 */         esp -= 4;
/* 3917 */         memory.setDoubleWord(esp, edx);
/* 3918 */         break;
/*      */       
/*      */ 
/*      */       case 232: 
/* 3922 */         esp -= 4;
/* 3923 */         memory.setDoubleWord(esp, ecx);
/* 3924 */         break;
/*      */       
/*      */ 
/*      */       case 231: 
/* 3928 */         esp -= 4;
/* 3929 */         memory.setDoubleWord(esp, ebx);
/* 3930 */         break;
/*      */       
/*      */ 
/*      */       case 230: 
/* 3934 */         esp -= 4;
/* 3935 */         memory.setDoubleWord(esp, eax);
/* 3936 */         break;
/*      */       
/*      */ 
/*      */       case 229: 
/* 3940 */         esp -= 4;
/* 3941 */         memory.setDoubleWord(esp, reg0);
/* 3942 */         break;
/*      */       
/*      */ 
/*      */       case 228: 
/* 3946 */         if ((reg2 != 0) && (!Processor.getZeroFlag(flagStatus, zf, flagResult))) {
/* 3947 */           eip += uops[(pos++)];
/*      */         } else
/* 3949 */           pos++;
/* 3950 */         break;
/*      */       
/*      */ 
/*      */       case 227: 
/* 3954 */         if ((reg2 != 0) && (Processor.getZeroFlag(flagStatus, zf, flagResult))) {
/* 3955 */           eip += uops[(pos++)];
/*      */         } else
/* 3957 */           pos++;
/* 3958 */         break;
/*      */       
/*      */ 
/*      */       case 226: 
/* 3962 */         if (reg2 != 0) {
/* 3963 */           eip += uops[(pos++)];
/*      */         } else
/* 3965 */           pos++;
/* 3966 */         break;
/*      */       
/*      */ 
/*      */       case 225: 
/* 3970 */         if (ecx == 0) {
/* 3971 */           eip += uops[(pos++)];
/*      */         } else
/* 3973 */           pos++;
/* 3974 */         break;
/*      */       
/*      */ 
/*      */       case 224: 
/* 3978 */         if (Processor.getSignFlag(flagStatus, sf, flagResult) == Processor.getOverflowFlag(flagStatus, of, flagOp1, flagOp2, flagResult, flagIns)) {
/* 3979 */           eip += uops[(pos++)];
/*      */         } else
/* 3981 */           pos++;
/* 3982 */         break;
/*      */       
/*      */ 
/*      */       case 254: 
/* 3986 */         addr += 2 * ebx;
/* 3987 */         break;
/*      */       
/*      */ 
/*      */       case 255: 
/* 3991 */         addr += 4 * ebx;
/* 3992 */         break;
/*      */       
/*      */ 
/*      */       case 252: 
/* 3996 */         addr += 8 * eax;
/* 3997 */         break;
/*      */       
/*      */ 
/*      */       case 253: 
/* 4001 */         addr += ebx;
/* 4002 */         break;
/*      */       
/*      */ 
/*      */       case 250: 
/* 4006 */         addr += 2 * eax;
/* 4007 */         break;
/*      */       
/*      */ 
/*      */       case 251: 
/* 4011 */         addr += 4 * eax;
/* 4012 */         break;
/*      */       
/*      */ 
/*      */       case 248: 
/* 4016 */         addr += uops[(pos++)];
/* 4017 */         break;
/*      */       
/*      */ 
/*      */       case 249: 
/* 4021 */         addr += eax;
/* 4022 */         break;
/*      */       
/*      */ 
/*      */       case 246: 
/* 4026 */         ebp = esp;
/* 4027 */         break;
/*      */       
/*      */ 
/*      */       case 247: 
/* 4031 */         cpu.gs = new Segment((short)eax, memory.getQuadWord(cpu.gdt.getBase() + 2 * (eax & 0xFFF8)));
/* 4032 */         break;
/*      */       
/*      */ 
/*      */       case 244: 
/* 4036 */         edi = memory.getDoubleWord(esp);
/* 4037 */         esp += 4;
/* 4038 */         break;
/*      */       
/*      */ 
/*      */       case 245: 
/* 4042 */         ebp = memory.getDoubleWord(esp);
/* 4043 */         esp += 4;
/* 4044 */         break;
/*      */       
/*      */ 
/*      */       case 242: 
/* 4048 */         edx = memory.getDoubleWord(esp);
/* 4049 */         esp += 4;
/* 4050 */         break;
/*      */       
/*      */ 
/*      */       case 243: 
/* 4054 */         esi = memory.getDoubleWord(esp);
/* 4055 */         esp += 4;
/* 4056 */         break;
/*      */       
/*      */ 
/*      */       case 240: 
/* 4060 */         ebx = memory.getDoubleWord(esp);
/* 4061 */         esp += 4;
/* 4062 */         break;
/*      */       
/*      */ 
/*      */       case 241: 
/* 4066 */         ecx = memory.getDoubleWord(esp);
/* 4067 */         esp += 4;
/* 4068 */         break;
/*      */       
/*      */ 
/*      */       case 275: 
/* 4072 */         addr += 2 * ebp;
/* 4073 */         break;
/*      */       
/*      */ 
/*      */       case 274: 
/* 4077 */         addr += ebp;
/* 4078 */         break;
/*      */       
/*      */ 
/*      */       case 273: 
/* 4082 */         addr += esp;
/* 4083 */         break;
/*      */       
/*      */ 
/*      */       case 272: 
/* 4087 */         addr += 8 * edi;
/* 4088 */         break;
/*      */       
/*      */ 
/*      */       case 279: 
/* 4092 */         addr = 0;
/* 4093 */         break;
/*      */       
/*      */ 
/*      */       case 278: 
/* 4097 */         if (cpu.gs != null) {
/* 4098 */           addr += cpu.gs.getBase();
/*      */         }
/*      */         
/*      */         break;
/*      */       case 277: 
/* 4103 */         addr += 8 * ebp;
/* 4104 */         break;
/*      */       
/*      */ 
/*      */       case 276: 
/* 4108 */         addr += 4 * ebp;
/* 4109 */         break;
/*      */       
/*      */ 
/*      */       case 283: 
/* 4113 */         reg0 = ecx;
/* 4114 */         break;
/*      */       
/*      */ 
/*      */       case 282: 
/* 4118 */         reg0 = ebx;
/* 4119 */         break;
/*      */       
/*      */ 
/*      */       case 281: 
/* 4123 */         reg0 = eax;
/* 4124 */         break;
/*      */       
/*      */ 
/*      */       case 280: 
/* 4128 */         reg0 = uops[(pos++)];
/* 4129 */         break;
/*      */       
/*      */ 
/*      */       case 287: 
/* 4133 */         reg0 = esp;
/* 4134 */         break;
/*      */       
/*      */ 
/*      */       case 286: 
/* 4138 */         reg0 = edi;
/* 4139 */         break;
/*      */       
/*      */ 
/*      */       case 285: 
/* 4143 */         reg0 = esi;
/* 4144 */         break;
/*      */       
/*      */ 
/*      */       case 284: 
/* 4148 */         reg0 = edx;
/* 4149 */         break;
/*      */       
/*      */ 
/*      */       case 258: 
/* 4153 */         addr += 2 * ecx;
/* 4154 */         break;
/*      */       
/*      */ 
/*      */       case 259: 
/* 4158 */         addr += 4 * ecx;
/* 4159 */         break;
/*      */       
/*      */ 
/*      */       case 256: 
/* 4163 */         addr += 8 * ebx;
/* 4164 */         break;
/*      */       
/*      */ 
/*      */       case 257: 
/* 4168 */         addr += ecx;
/* 4169 */         break;
/*      */       
/*      */ 
/*      */       case 262: 
/* 4173 */         addr += 2 * edx;
/* 4174 */         break;
/*      */       
/*      */ 
/*      */       case 263: 
/* 4178 */         addr += 4 * edx;
/* 4179 */         break;
/*      */       
/*      */ 
/*      */       case 260: 
/* 4183 */         addr += 8 * ecx;
/* 4184 */         break;
/*      */       
/*      */ 
/*      */       case 261: 
/* 4188 */         addr += edx;
/* 4189 */         break;
/*      */       
/*      */ 
/*      */       case 266: 
/* 4193 */         addr += 2 * esi;
/* 4194 */         break;
/*      */       
/*      */ 
/*      */       case 267: 
/* 4198 */         addr += 4 * esi;
/* 4199 */         break;
/*      */       
/*      */ 
/*      */       case 264: 
/* 4203 */         addr += 8 * edx;
/* 4204 */         break;
/*      */       
/*      */ 
/*      */       case 265: 
/* 4208 */         addr += esi;
/* 4209 */         break;
/*      */       
/*      */ 
/*      */       case 270: 
/* 4213 */         addr += 2 * edi;
/* 4214 */         break;
/*      */       
/*      */ 
/*      */       case 271: 
/* 4218 */         addr += 4 * edi;
/* 4219 */         break;
/*      */       
/*      */ 
/*      */       case 268: 
/* 4223 */         addr += 8 * esi;
/* 4224 */         break;
/*      */       
/*      */ 
/*      */       case 269: 
/* 4228 */         addr += edi;
/* 4229 */         break;
/*      */       
/*      */ 
/*      */       case 305: 
/* 4233 */         reg0 = memory.getWord(addr);
/* 4234 */         break;
/*      */       
/*      */ 
/*      */       case 304: 
/* 4238 */         reg0 = memory.getByte(addr);
/* 4239 */         break;
/*      */       
/*      */ 
/*      */       case 307: 
/* 4243 */         regl0 = memory.getWord(addr);
/* 4244 */         break;
/*      */       
/*      */ 
/*      */       case 306: 
/* 4248 */         reg0 = memory.getDoubleWord(addr);
/* 4249 */         break;
/*      */       
/*      */ 
/*      */       case 309: 
/* 4253 */         regl0 = memory.getQuadWord(addr);
/* 4254 */         break;
/*      */       
/*      */ 
/*      */       case 308: 
/* 4258 */         regl0 = memory.getDoubleWord(addr);
/* 4259 */         break;
/*      */       
/*      */ 
/*      */       case 311: 
/* 4263 */         reg1 = eax;
/* 4264 */         break;
/*      */       
/*      */ 
/*      */       case 310: 
/* 4268 */         reg1 = uops[(pos++)];
/* 4269 */         break;
/*      */       
/*      */ 
/*      */       case 313: 
/* 4273 */         reg1 = ecx;
/* 4274 */         break;
/*      */       
/*      */ 
/*      */       case 312: 
/* 4278 */         reg1 = ebx;
/* 4279 */         break;
/*      */       
/*      */ 
/*      */       case 315: 
/* 4283 */         reg1 = esi;
/* 4284 */         break;
/*      */       
/*      */ 
/*      */       case 314: 
/* 4288 */         reg1 = edx;
/* 4289 */         break;
/*      */       
/*      */ 
/*      */       case 317: 
/* 4293 */         reg1 = esp;
/* 4294 */         break;
/*      */       
/*      */ 
/*      */       case 316: 
/* 4298 */         reg1 = edi;
/* 4299 */         break;
/*      */       
/*      */ 
/*      */       case 319: 
/* 4303 */         reg1 = eax & 0xFFFF;
/* 4304 */         break;
/*      */       
/*      */ 
/*      */       case 318: 
/* 4308 */         reg1 = ebp;
/* 4309 */         break;
/*      */       
/*      */ 
/*      */       case 288: 
/* 4313 */         reg0 = ebp;
/* 4314 */         break;
/*      */       
/*      */ 
/*      */       case 289: 
/* 4318 */         reg0 = (byte)eax;
/* 4319 */         break;
/*      */       
/*      */ 
/*      */       case 290: 
/* 4323 */         reg0 = (byte)(eax >> 8);
/* 4324 */         break;
/*      */       
/*      */ 
/*      */       case 291: 
/* 4328 */         reg0 = eax & 0xFFFF;
/* 4329 */         break;
/*      */       
/*      */ 
/*      */       case 292: 
/* 4333 */         reg0 = ebx & 0xFF;
/* 4334 */         break;
/*      */       
/*      */ 
/*      */       case 293: 
/* 4338 */         reg0 = (byte)(ebx >> 8);
/* 4339 */         break;
/*      */       
/*      */ 
/*      */       case 294: 
/* 4343 */         reg0 = ebx & 0xFFFF;
/* 4344 */         break;
/*      */       
/*      */ 
/*      */       case 295: 
/* 4348 */         reg0 = ecx & 0xFF;
/* 4349 */         break;
/*      */       
/*      */ 
/*      */       case 296: 
/* 4353 */         reg0 = (ecx & 0xFF00) >> 8;
/* 4354 */         break;
/*      */       
/*      */ 
/*      */       case 297: 
/* 4358 */         reg0 = ecx & 0xFFFF;
/* 4359 */         break;
/*      */       
/*      */ 
/*      */       case 298: 
/* 4363 */         reg0 = edx & 0xFF;
/* 4364 */         break;
/*      */       
/*      */ 
/*      */       case 299: 
/* 4368 */         reg0 = (edx & 0xFF00) >> 8;
/* 4369 */         break;
/*      */       
/*      */ 
/*      */       case 300: 
/* 4373 */         reg0 = edx & 0xFFFF;
/* 4374 */         break;
/*      */       
/*      */ 
/*      */       case 301: 
/* 4378 */         reg0 = esi & 0xFFFF;
/* 4379 */         break;
/*      */       
/*      */ 
/*      */       case 302: 
/* 4383 */         reg0 = edi & 0xFFFF;
/* 4384 */         break;
/*      */       
/*      */ 
/*      */       case 303: 
/* 4388 */         reg0 = ebp & 0xFFFF;
/* 4389 */         break;
/*      */       
/*      */ 
/*      */       case 343: 
/* 4393 */         reg2 = edi;
/* 4394 */         break;
/*      */       
/*      */ 
/*      */       case 342: 
/* 4398 */         reg2 = esi;
/* 4399 */         break;
/*      */       
/*      */ 
/*      */       case 341: 
/* 4403 */         reg2 = edx;
/* 4404 */         break;
/*      */       
/*      */ 
/*      */       case 340: 
/* 4408 */         reg2 = ecx;
/* 4409 */         break;
/*      */       
/*      */ 
/*      */       case 339: 
/* 4413 */         reg2 = ebx;
/* 4414 */         break;
/*      */       
/*      */ 
/*      */       case 338: 
/* 4418 */         reg2 = eax;
/* 4419 */         break;
/*      */       
/*      */ 
/*      */       case 337: 
/* 4423 */         reg2 = uops[(pos++)];
/* 4424 */         break;
/*      */       
/*      */ 
/*      */       case 336: 
/* 4428 */         reg1 = 1;
/* 4429 */         break;
/*      */       
/*      */ 
/*      */       case 351: 
/* 4433 */         reg2 = ecx & 0xFFFF;
/* 4434 */         break;
/*      */       
/*      */ 
/*      */       case 350: 
/* 4438 */         reg2 = ebx & 0xFF;
/* 4439 */         break;
/*      */       
/*      */ 
/*      */       case 349: 
/* 4443 */         reg2 = ebx & 0xFFFF;
/* 4444 */         break;
/*      */       
/*      */ 
/*      */       case 348: 
/* 4448 */         reg2 = eax & 0xFF;
/* 4449 */         break;
/*      */       
/*      */ 
/*      */       case 347: 
/* 4453 */         reg2 = (eax & 0xFF00) >> 8;
/* 4454 */         break;
/*      */       
/*      */ 
/*      */       case 346: 
/* 4458 */         reg2 = eax & 0xFFFF;
/* 4459 */         break;
/*      */       
/*      */ 
/*      */       case 345: 
/* 4463 */         reg2 = ebp;
/* 4464 */         break;
/*      */       
/*      */ 
/*      */       case 344: 
/* 4468 */         reg2 = esp;
/* 4469 */         break;
/*      */       
/*      */ 
/*      */       case 326: 
/* 4473 */         reg1 = (ecx & 0xFF00) >> 8;
/* 4474 */         break;
/*      */       
/*      */ 
/*      */       case 327: 
/* 4478 */         reg1 = edx & 0xFFFF;
/* 4479 */         break;
/*      */       
/*      */ 
/*      */       case 324: 
/* 4483 */         reg1 = ecx & 0xFFFF;
/* 4484 */         break;
/*      */       
/*      */ 
/*      */       case 325: 
/* 4488 */         reg1 = ecx & 0xFF;
/* 4489 */         break;
/*      */       
/*      */ 
/*      */       case 322: 
/* 4493 */         reg1 = ebx & 0xFFFF;
/* 4494 */         break;
/*      */       
/*      */ 
/*      */       case 323: 
/* 4498 */         reg1 = ebx & 0xFF;
/* 4499 */         break;
/*      */       
/*      */ 
/*      */       case 320: 
/* 4503 */         reg1 = (eax & 0xFF00) >> 8;
/* 4504 */         break;
/*      */       
/*      */ 
/*      */       case 321: 
/* 4508 */         reg1 = eax & 0xFF;
/* 4509 */         break;
/*      */       
/*      */ 
/*      */       case 334: 
/* 4513 */         reg1 = memory.getWord(addr);
/* 4514 */         break;
/*      */       
/*      */ 
/*      */       case 335: 
/* 4518 */         reg1 = memory.getByte(addr);
/* 4519 */         break;
/*      */       
/*      */ 
/*      */       case 332: 
/* 4523 */         reg1 = ebp & 0xFFFF;
/* 4524 */         break;
/*      */       
/*      */ 
/*      */       case 333: 
/* 4528 */         reg1 = memory.getDoubleWord(addr);
/* 4529 */         break;
/*      */       
/*      */ 
/*      */       case 330: 
/* 4533 */         reg1 = esi & 0xFFFF;
/* 4534 */         break;
/*      */       
/*      */ 
/*      */       case 331: 
/* 4538 */         reg1 = edi & 0xFFFF;
/* 4539 */         break;
/*      */       
/*      */ 
/*      */       case 328: 
/* 4543 */         reg1 = edx & 0xFF;
/* 4544 */         break;
/*      */       
/*      */ 
/*      */       case 329: 
/* 4548 */         reg1 = (edx & 0xFF00) >> 8;
/* 4549 */         break;
/*      */       
/*      */ 
/*      */       case 373: 
/* 4553 */         eax = eax & 0xFFFF0000 | reg0 & 0xFFFF;
/* 4554 */         break;
/*      */       
/*      */ 
/*      */       case 372: 
/* 4558 */         edi = reg1;
/* 4559 */         break;
/*      */       
/*      */ 
/*      */       case 375: 
/* 4563 */         ebx = reg0;
/* 4564 */         break;
/*      */       
/*      */ 
/*      */       case 374: 
/* 4568 */         eax = reg0;
/* 4569 */         break;
/*      */       
/*      */ 
/*      */       case 369: 
/* 4573 */         edx = reg1;
/* 4574 */         break;
/*      */       
/*      */ 
/*      */       case 368: 
/* 4578 */         ebx = reg1;
/* 4579 */         break;
/*      */       
/*      */ 
/*      */       case 371: 
/* 4583 */         eax = eax & 0xFFFF0000 | reg1 & 0xFFFF;
/* 4584 */         break;
/*      */       
/*      */ 
/*      */       case 370: 
/* 4588 */         ecx = reg1;
/* 4589 */         break;
/*      */       
/*      */ 
/*      */       case 381: 
/* 4593 */         memory.setDoubleWord(addr, reg0);
/* 4594 */         break;
/*      */       
/*      */ 
/*      */       case 380: 
/* 4598 */         ebp = reg0;
/* 4599 */         break;
/*      */       
/*      */ 
/*      */       case 383: 
/* 4603 */         eax = reg2;
/* 4604 */         break;
/*      */       
/*      */ 
/*      */       case 382: 
/* 4608 */         memory.setQuadWord(addr, regl0);
/* 4609 */         break;
/*      */       
/*      */ 
/*      */       case 377: 
/* 4613 */         edx = reg0;
/* 4614 */         break;
/*      */       
/*      */ 
/*      */       case 376: 
/* 4618 */         ecx = reg0;
/* 4619 */         break;
/*      */       
/*      */ 
/*      */       case 379: 
/* 4623 */         esi = reg0;
/* 4624 */         break;
/*      */       
/*      */ 
/*      */       case 378: 
/* 4628 */         edi = reg0;
/* 4629 */         break;
/*      */       
/*      */ 
/*      */       case 356: 
/* 4633 */         reg2 = edx & 0xFF;
/* 4634 */         break;
/*      */       
/*      */ 
/*      */       case 357: 
/* 4638 */         reg2 = esi & 0xFFFF;
/* 4639 */         break;
/*      */       
/*      */ 
/*      */       case 358: 
/* 4643 */         reg2 = edi & 0xFFFF;
/* 4644 */         break;
/*      */       
/*      */ 
/*      */       case 359: 
/* 4648 */         reg2 = ebp & 0xFFFF;
/* 4649 */         break;
/*      */       
/*      */ 
/*      */       case 352: 
/* 4653 */         reg2 = (ecx & 0xFF00) >> 8;
/* 4654 */         break;
/*      */       
/*      */ 
/*      */       case 353: 
/* 4658 */         reg2 = ecx & 0xFF;
/* 4659 */         break;
/*      */       
/*      */ 
/*      */       case 354: 
/* 4663 */         reg2 = edx & 0xFFFF;
/* 4664 */         break;
/*      */       
/*      */ 
/*      */       case 355: 
/* 4668 */         reg2 = (edx & 0xFF00) >> 8;
/* 4669 */         break;
/*      */       
/*      */ 
/*      */       case 364: 
/* 4673 */         reg3 = uops[(pos++)];
/* 4674 */         break;
/*      */       
/*      */ 
/*      */       case 365: 
/* 4678 */         eax = reg1;
/* 4679 */         break;
/*      */       
/*      */ 
/*      */       case 366: 
/* 4683 */         esi = reg1;
/* 4684 */         break;
/*      */       
/*      */ 
/*      */       case 367: 
/* 4688 */         edi = reg1;
/* 4689 */         break;
/*      */       
/*      */ 
/*      */       case 360: 
/* 4693 */         reg2 = memory.getDoubleWord(addr);
/* 4694 */         break;
/*      */       
/*      */ 
/*      */       case 361: 
/* 4698 */         reg2 = memory.getWord(addr);
/* 4699 */         break;
/*      */       
/*      */ 
/*      */       case 362: 
/* 4703 */         reg2 = memory.getByte(addr);
/* 4704 */         break;
/*      */       
/*      */ 
/*      */       case 363: 
/* 4708 */         reg3 = ecx & 0xFF;
/* 4709 */         break;
/*      */       
/*      */ 
/*      */       case 410: 
/* 4713 */         edx = edx & 0xFFFF0000 | reg3 & 0xFFFF;
/* 4714 */         break;
/*      */       
/*      */ 
/*      */       case 411: 
/* 4718 */         edx = reg3;
/* 4719 */         break;
/*      */       
/*      */ 
/*      */       case 408: 
/* 4723 */         memory.setByte(addr, (byte)reg2);
/* 4724 */         break;
/*      */       
/*      */ 
/*      */       case 409: 
/* 4728 */         eax = eax & 0xFFFF00FF | (reg3 & 0xFF) << 8;
/* 4729 */         break;
/*      */       
/*      */ 
/*      */ 
/*      */       case 414: 
/* 4734 */         memory.store(addr, new byte[10]);
/* 4735 */         memory.setF64(addr, freg0);
/* 4736 */         break;
/*      */       
/*      */ 
/*      */       case 415: 
/* 4740 */         pushFPU(freg0, fpuData, ftop);
/* 4741 */         break;
/*      */       
/*      */ 
/*      */       case 412: 
/* 4745 */         memory.setF32(addr, (float)freg0);
/* 4746 */         break;
/*      */       
/*      */ 
/*      */       case 413: 
/* 4750 */         memory.setF64(addr, freg0);
/* 4751 */         break;
/*      */       
/*      */ 
/*      */       case 402: 
/* 4755 */         edi = edi & 0xFFFF0000 | reg2 & 0xFFFF;
/* 4756 */         break;
/*      */       
/*      */ 
/*      */       case 403: 
/* 4760 */         esp = reg2;
/* 4761 */         break;
/*      */       
/*      */ 
/*      */       case 400: 
/* 4765 */         esi = esi & 0xFFFF0000 | reg2 & 0xFFFF;
/* 4766 */         break;
/*      */       
/*      */ 
/*      */       case 401: 
/* 4770 */         edi = reg2;
/* 4771 */         break;
/*      */       
/*      */ 
/*      */       case 406: 
/* 4775 */         memory.setDoubleWord(addr, reg2);
/* 4776 */         break;
/*      */       
/*      */ 
/*      */       case 407: 
/* 4780 */         memory.setWord(addr, (short)reg2);
/* 4781 */         break;
/*      */       
/*      */ 
/*      */       case 404: 
/* 4785 */         ebp = reg2;
/* 4786 */         break;
/*      */       
/*      */ 
/*      */       case 405: 
/* 4790 */         ebp = ebp & 0xFFFF0000 | reg2 & 0xFFFF;
/* 4791 */         break;
/*      */       
/*      */ 
/*      */       case 395: 
/* 4795 */         edx = edx & 0xFFFF0000 | reg2 & 0xFFFF;
/* 4796 */         break;
/*      */       
/*      */ 
/*      */       case 394: 
/* 4800 */         edx = edx & 0xFFFF00FF | (reg2 & 0xFF) << 8;
/* 4801 */         break;
/*      */       
/*      */ 
/*      */       case 393: 
/* 4805 */         edx = edx & 0xFF00 | reg2 & 0xFF;
/* 4806 */         break;
/*      */       
/*      */ 
/*      */       case 392: 
/* 4810 */         ecx = ecx & 0xFFFF00FF | (reg2 & 0xFF) << 8;
/* 4811 */         break;
/*      */       
/*      */ 
/*      */       case 399: 
/* 4815 */         esi = reg2;
/* 4816 */         break;
/*      */       
/*      */ 
/*      */       case 398: 
/* 4820 */         edx = reg2;
/* 4821 */         break;
/*      */       
/*      */ 
/*      */       case 397: 
/* 4825 */         ecx = reg2;
/* 4826 */         break;
/*      */       
/*      */ 
/*      */       case 396: 
/* 4830 */         ebx = reg2;
/* 4831 */         break;
/*      */       
/*      */ 
/*      */       case 387: 
/* 4835 */         ebx = ebx & 0xFFFF0000 | reg2 & 0xFFFF;
/* 4836 */         break;
/*      */       
/*      */ 
/*      */       case 386: 
/* 4840 */         eax = eax & 0xFF00 | reg2 & 0xFF;
/* 4841 */         break;
/*      */       
/*      */ 
/*      */       case 385: 
/* 4845 */         eax = eax & 0xFFFF00FF | (reg2 & 0xFF) << 8;
/* 4846 */         break;
/*      */       
/*      */ 
/*      */       case 384: 
/* 4850 */         eax = eax & 0xFFFF0000 | reg2 & 0xFFFF;
/* 4851 */         break;
/*      */       
/*      */ 
/*      */       case 391: 
/* 4855 */         ecx = ecx & 0xFF00 | reg2 & 0xFF;
/* 4856 */         break;
/*      */       
/*      */ 
/*      */       case 390: 
/* 4860 */         ecx = ecx & 0xFFFF0000 | reg2 & 0xFFFF;
/* 4861 */         break;
/*      */       
/*      */ 
/*      */       case 389: 
/* 4865 */         ebx = ebx & 0xFF00 | reg2 & 0xFF;
/* 4866 */         break;
/*      */       
/*      */ 
/*      */       case 388: 
/* 4870 */         ebx = ebx & 0xFFFF00FF | (reg2 & 0xFF) << 8;
/* 4871 */         break;
/*      */       
/*      */ 
/*      */       case 440: 
/* 4875 */         freg0 = fpuData[(ftop + 1 & 0x7)];
/* 4876 */         break;
/*      */       
/*      */ 
/*      */       case 441: 
/* 4880 */         freg0 = fpuData[(ftop + 2 & 0x7)];
/* 4881 */         break;
/*      */       
/*      */ 
/*      */       case 442: 
/* 4885 */         freg0 = fpuData[(ftop + 3 & 0x7)];
/* 4886 */         break;
/*      */       
/*      */ 
/*      */       case 443: 
/* 4890 */         freg0 = fpuData[(ftop + 4 & 0x7)];
/* 4891 */         break;
/*      */       
/*      */ 
/*      */       case 444: 
/* 4895 */         freg0 = fpuData[(ftop + 5 & 0x7)];
/* 4896 */         break;
/*      */       
/*      */ 
/*      */       case 445: 
/* 4900 */         freg0 = fpuData[(ftop + 6 & 0x7)];
/* 4901 */         break;
/*      */       
/*      */ 
/*      */       case 446: 
/* 4905 */         freg0 = fpuData[(ftop + 7 & 0x7)];
/* 4906 */         break;
/*      */       
/*      */ 
/*      */       case 447: 
/* 4910 */         freg1 = fpuData[ftop];
/* 4911 */         break;
/*      */       
/*      */ 
/*      */       case 432: 
/* 4915 */         fpuData[(ftop + 7 & 0x7)] = freg2;
/* 4916 */         break;
/*      */       
/*      */ 
/*      */       case 433: 
/* 4920 */         freg0 = memory.getF32(addr);
/* 4921 */         break;
/*      */       
/*      */ 
/*      */       case 434: 
/* 4925 */         freg0 = memory.getF64(addr);
/* 4926 */         break;
/*      */       
/*      */ 
/*      */ 
/*      */       case 435: 
/* 4931 */         freg0 = memory.getF64(addr);
/* 4932 */         break;
/*      */       
/*      */ 
/*      */       case 436: 
/* 4936 */         freg1 = memory.getF32(addr);
/* 4937 */         break;
/*      */       
/*      */ 
/*      */       case 437: 
/* 4941 */         freg1 = memory.getF64(addr);
/* 4942 */         break;
/*      */       
/*      */ 
/*      */       case 438: 
/* 4946 */         freg0 = fpuData[ftop];
/* 4947 */         break;
/*      */       
/*      */ 
/*      */       case 439: 
/* 4951 */         freg0 = fpuData[ftop];
/* 4952 */         break;
/*      */       
/*      */ 
/*      */       case 425: 
/* 4956 */         pushFPU(freg2, fpuData, ftop);
/* 4957 */         break;
/*      */       
/*      */ 
/*      */       case 424: 
/* 4961 */         pushFPU(freg2, fpuData, ftop);
/* 4962 */         break;
/*      */       
/*      */ 
/*      */       case 427: 
/* 4966 */         fpuData[(ftop + 2 & 0x7)] = freg2;
/* 4967 */         break;
/*      */       
/*      */ 
/*      */       case 426: 
/* 4971 */         fpuData[(ftop + 1 & 0x7)] = freg2;
/* 4972 */         break;
/*      */       
/*      */ 
/*      */       case 429: 
/* 4976 */         fpuData[(ftop + 4 & 0x7)] = freg2;
/* 4977 */         break;
/*      */       
/*      */ 
/*      */       case 428: 
/* 4981 */         fpuData[(ftop + 3 & 0x7)] = freg2;
/* 4982 */         break;
/*      */       
/*      */ 
/*      */       case 431: 
/* 4986 */         fpuData[(ftop + 6 & 0x7)] = freg2;
/* 4987 */         break;
/*      */       
/*      */ 
/*      */       case 430: 
/* 4991 */         fpuData[(ftop + 5 & 0x7)] = freg2;
/* 4992 */         break;
/*      */       
/*      */ 
/*      */       case 417: 
/* 4996 */         fpuData[(ftop + 1 & 0x7)] = freg0;
/* 4997 */         break;
/*      */       
/*      */ 
/*      */       case 416: 
/* 5001 */         pushFPU(freg0, fpuData, ftop);
/* 5002 */         break;
/*      */       
/*      */ 
/*      */       case 419: 
/* 5006 */         fpuData[(ftop + 3 & 0x7)] = freg0;
/* 5007 */         break;
/*      */       
/*      */ 
/*      */       case 418: 
/* 5011 */         fpuData[(ftop + 2 & 0x7)] = freg0;
/* 5012 */         break;
/*      */       
/*      */ 
/*      */       case 421: 
/* 5016 */         fpuData[(ftop + 5 & 0x7)] = freg0;
/* 5017 */         break;
/*      */       
/*      */ 
/*      */       case 420: 
/* 5021 */         fpuData[(ftop + 4 & 0x7)] = freg0;
/* 5022 */         break;
/*      */       
/*      */ 
/*      */       case 423: 
/* 5026 */         fpuData[(ftop + 7 & 0x7)] = freg0;
/* 5027 */         break;
/*      */       
/*      */ 
/*      */       case 422: 
/* 5031 */         fpuData[(ftop + 6 & 0x7)] = freg0;
/* 5032 */         break;
/*      */       
/*      */ 
/*      */       case 460: 
/* 5036 */         throw new IllegalStateException("Unresolved immediate/displacement in ucode stream");
/*      */       
/*      */ 
/*      */       case 459: 
/* 5040 */         throw new IllegalStateException("Unresolved immediate/displacement in ucode stream");
/*      */       
/*      */ 
/*      */       case 458: 
/* 5044 */         flagOp1 = cpu.flagOp1;flagOp2 = cpu.flagOp2;flagResult = cpu.flagResult;flagIns = cpu.flagIns;
/* 5045 */         flagStatus = cpu.flagStatus;
/* 5046 */         eax = cpu.eax;
/* 5047 */         ebx = cpu.ebx;
/* 5048 */         edx = cpu.edx;
/* 5049 */         ecx = cpu.ecx;
/* 5050 */         esi = cpu.esi;
/* 5051 */         edi = cpu.edi;
/* 5052 */         esp = cpu.esp;
/* 5053 */         ebp = cpu.ebp;
/* 5054 */         eip = cpu.eip;
/* 5055 */         of = cpu.of;
/* 5056 */         sf = cpu.sf;
/* 5057 */         zf = cpu.zf;
/* 5058 */         af = cpu.af;
/* 5059 */         pf = cpu.pf;
/* 5060 */         cf = cpu.cf;
/* 5061 */         ftop = cpu.ftop;
/* 5062 */         break;
/*      */       
/*      */ 
/*      */       case 457: 
/* 5066 */         throw new ProgramEndException();
/*      */       
/*      */ 
/*      */       case 456: 
/* 5070 */         reg2 = addr;
/* 5071 */         addr = 0;
/* 5072 */         break;
/*      */       
/*      */ 
/*      */       case 455: 
/* 5076 */         freg1 = fpuData[(ftop + 7 & 0x7)];
/* 5077 */         break;
/*      */       
/*      */ 
/*      */       case 454: 
/* 5081 */         freg1 = fpuData[(ftop + 6 & 0x7)];
/* 5082 */         break;
/*      */       
/*      */ 
/*      */       case 453: 
/* 5086 */         freg1 = fpuData[(ftop + 5 & 0x7)];
/* 5087 */         break;
/*      */       
/*      */ 
/*      */       case 452: 
/* 5091 */         freg1 = fpuData[(ftop + 4 & 0x7)];
/* 5092 */         break;
/*      */       
/*      */ 
/*      */       case 451: 
/* 5096 */         freg1 = fpuData[(ftop + 3 & 0x7)];
/* 5097 */         break;
/*      */       
/*      */ 
/*      */       case 450: 
/* 5101 */         freg1 = fpuData[(ftop + 2 & 0x7)];
/* 5102 */         break;
/*      */       
/*      */ 
/*      */       case 449: 
/* 5106 */         freg1 = fpuData[(ftop + 1 & 0x7)];
/* 5107 */         break;
/*      */       
/*      */ 
/*      */       case 448: 
/* 5111 */         freg1 = fpuData[ftop];
/* 5112 */         break;
/*      */       
/*      */ 
/*      */       case 485: 
/* 5116 */         eax = (short)eax;
/* 5117 */         break;
/*      */       
/*      */ 
/*      */       case 484: 
/* 5121 */         reg0 = Integer.reverseBytes(reg0);
/* 5122 */         break;
/*      */       
/*      */ 
/*      */       case 483: 
/* 5126 */         context.unlock(0);
/* 5127 */         break;
/*      */       
/*      */ 
/*      */       case 482: 
/* 5131 */         context.lock(0);
/* 5132 */         break;
/*      */       case 140: case 141: case 238: case 461: case 462: case 463: case 464: case 465: case 466: 
/*      */       case 467: case 468: case 469: case 470: case 471: case 472: case 473: case 474: 
/*      */       case 475: case 476: case 477: case 478: case 479: case 480: case 481: default: 
/* 5136 */         System.out.println("UCode " + uop + " UNKNOWN (X86=" + UCodes.getName(uop) + ")");
/*      */       }
/*      */       
/*      */     }
/* 5140 */     synchronize(context, eax, ebx, ecx, edx, esi, edi, esp, ebp, eip, of, sf, zf, af, pf, cf, flagOp1, flagOp2, flagResult, flagStatus, flagIns, ftop);
/* 5141 */     return false;
/*      */   }
/*      */   
/*      */   public static void synchronize(ThreadContext context, int eax, int ebx, int ecx, int edx, int esi, int edi, int esp, int ebp, int eip, boolean of, boolean sf, boolean zf, boolean af, boolean pf, boolean cf, int flagOp1, int flagOp2, int flagResult, int flagStatus, int flagIns, int ftop) {
/* 5145 */     Processor cpu = context.cpu;
/* 5146 */     cpu.eax = eax;
/* 5147 */     cpu.ebx = ebx;
/* 5148 */     cpu.ecx = ecx;
/* 5149 */     cpu.edx = edx;
/* 5150 */     cpu.esi = esi;
/* 5151 */     cpu.edi = edi;
/* 5152 */     cpu.esp = esp;
/* 5153 */     cpu.ebp = ebp;
/* 5154 */     cpu.eip = eip;
/*      */     
/* 5156 */     cpu.of = of;
/* 5157 */     cpu.sf = sf;
/* 5158 */     cpu.zf = zf;
/* 5159 */     cpu.af = af;
/* 5160 */     cpu.pf = pf;
/* 5161 */     cpu.cf = cf;
/* 5162 */     cpu.flagOp1 = flagOp1;
/* 5163 */     cpu.flagOp2 = flagOp2;
/* 5164 */     cpu.flagResult = flagResult;
/* 5165 */     cpu.flagIns = flagIns;
/* 5166 */     cpu.flagStatus = flagStatus;
/*      */     
/* 5168 */     cpu.ftop = ftop;
/*      */   }
/*      */   
/*      */ 
/*      */   public static int decFtop(int ftop)
/*      */   {
/* 5174 */     return ftop - 1 & 0x7;
/*      */   }
/*      */   
/*      */   public static void pushFPU(double d, double[] stack, int ftop)
/*      */   {
/* 5179 */     if (ftop >= 0)
/*      */     {
/* 5181 */       stack[ftop] = d;
/* 5182 */       return;
/*      */     }
/* 5184 */     stack[7] = d;
/*      */   }
/*      */   
/*      */   public static double popFPU(double[] stack, int ftop)
/*      */   {
/* 5189 */     double d = stack[ftop];
/* 5190 */     return d;
/*      */   }
/*      */ }


/* Location:              C:\Users\Joey\Downloads\codejar.jar!\com\emt\proteus\runtime32\Interpreter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */