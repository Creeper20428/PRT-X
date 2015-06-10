/*     */ package com.emt.proteus.decoder;
/*     */ 
/*     */ import java.io.InputStream;
/*     */ 
/*     */ public abstract class Disassembler implements java.io.Serializable
/*     */ {
/*     */   private static Disassembler defaultDisassembler;
/*     */   public abstract X86Opcode disassemble(InputStream paramInputStream) throws java.io.IOException;
/*     */   
/*     */   public static class DisassembleBranchNode extends Disassembler
/*     */   {
/*     */     public Disassembler[] branches;
/*     */     
/*     */     public DisassembleBranchNode() {
/*  15 */       this.branches = new Disassembler['Ā'];
/*     */     }
/*     */     
/*     */     public X86Opcode disassemble(InputStream opcodeStream) throws java.io.IOException
/*     */     {
/*  20 */       int index = 0xFF & opcodeStream.read();
/*     */       
/*  22 */       Disassembler d = this.branches[index];
/*  23 */       if (d == null)
/*     */       {
/*  25 */         System.out.println(Integer.toHexString(index));
/*  26 */         throw new NullPointerException("Un-decodable opcode in stream: 0x" + Integer.toHexString(index));
/*     */       }
/*     */       
/*     */       try
/*     */       {
/*  31 */         return d.disassemble(opcodeStream);
/*     */       }
/*     */       catch (NullPointerException e) {
/*  34 */         e.printStackTrace();
/*  35 */         System.out.println(Integer.toHexString(index));
/*     */         
/*  37 */         String m = e.getMessage();
/*  38 */         String start = m.substring(0, m.indexOf("0x") + 2);
/*  39 */         String end = m.substring(m.indexOf("0x") + 2);
/*  40 */         throw new NullPointerException(start + Integer.toHexString(index) + " " + end); } } }
/*     */   
/*     */   public static class DisassembleLeafNode extends Disassembler { private String name;
/*     */     private String opName;
/*     */     private String opcodePattern;
/*     */     private int x86Length;
/*     */     private int displacement;
/*     */     private int immediate;
/*     */     private int displacementSize;
/*     */     private int immediateSize;
/*     */     private int prefices;
/*     */     private int operandCount;
/*     */     public transient Object annotation;
/*     */     
/*  54 */     public DisassembleLeafNode(int prefices, String name, String pattern, int x86Length, int dispSize, int disp, int immSize, int imm, Object note) { this.prefices = prefices;
/*  55 */       this.name = name;
/*  56 */       this.x86Length = x86Length;
/*  57 */       this.opcodePattern = pattern.trim();
/*     */       
/*  59 */       this.displacementSize = dispSize;
/*  60 */       this.displacement = disp;
/*     */       
/*  62 */       this.immediateSize = immSize;
/*  63 */       this.immediate = imm;
/*     */       
/*  65 */       this.annotation = note;
/*  66 */       this.opName = name;
/*     */       
/*  68 */       this.operandCount = 0;
/*  69 */       int idx = this.opcodePattern.indexOf(name);
/*  70 */       String operandPattern = this.opcodePattern.substring(idx + name.length());
/*  71 */       if (operandPattern.length() > 0)
/*     */       {
/*     */         for (;;)
/*     */         {
/*  75 */           this.operandCount += 1;
/*  76 */           idx = operandPattern.indexOf(",");
/*  77 */           if (idx < 0)
/*     */             break;
/*  79 */           operandPattern = operandPattern.substring(idx + 1);
/*     */         }
/*     */       }
/*     */     }
/*     */     
/*     */     public X86Opcode disassemble(InputStream opcodeStream) throws java.io.IOException
/*     */     {
/*  86 */       int immediate = 0;
/*  87 */       int displacement = 0;
/*     */       
/*  89 */       if (this.displacementSize > 0)
/*     */       {
/*  91 */         if (this.displacementSize == 1) {
/*  92 */           displacement = (byte)opcodeStream.read();
/*  93 */         } else if (this.displacementSize == 2)
/*     */         {
/*  95 */           int b1 = 0xFF & opcodeStream.read();
/*  96 */           int b2 = 0xFF & opcodeStream.read();
/*     */           
/*  98 */           displacement = (short)(b1 | b2 << 8);
/*     */         }
/*     */         else
/*     */         {
/* 102 */           int b1 = 0xFF & opcodeStream.read();
/* 103 */           int b2 = 0xFF & opcodeStream.read();
/* 104 */           int b3 = 0xFF & opcodeStream.read();
/* 105 */           int b4 = 0xFF & opcodeStream.read();
/*     */           
/* 107 */           displacement = b1 | b2 << 8 | b3 << 16 | b4 << 24;
/*     */         }
/*     */       }
/*     */       
/* 111 */       if (this.immediateSize > 0)
/*     */       {
/* 113 */         if (this.immediateSize == 1) {
/* 114 */           immediate = (byte)opcodeStream.read();
/* 115 */         } else if (this.immediateSize == 2)
/*     */         {
/* 117 */           int b1 = 0xFF & opcodeStream.read();
/* 118 */           int b2 = 0xFF & opcodeStream.read();
/*     */           
/* 120 */           immediate = (short)(b1 | b2 << 8);
/*     */         }
/*     */         else
/*     */         {
/* 124 */           int b1 = 0xFF & opcodeStream.read();
/* 125 */           int b2 = 0xFF & opcodeStream.read();
/* 126 */           int b3 = 0xFF & opcodeStream.read();
/* 127 */           int b4 = 0xFF & opcodeStream.read();
/*     */           
/* 129 */           immediate = b1 | b2 << 8 | b3 << 16 | b4 << 24;
/*     */         }
/*     */       }
/*     */       
/* 133 */       return new X86Opcode(this.prefices, this.opName, this.opcodePattern, this.operandCount, this.x86Length, this.displacementSize, displacement, this.immediateSize, immediate);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   public static synchronized void init(String decoderResourceName)
/*     */     throws Exception
/*     */   {
/* 141 */     InputStream in = Disassembler.class.getResourceAsStream(decoderResourceName);
/* 142 */     java.io.ObjectInputStream objIn = new java.io.ObjectInputStream(in);
/* 143 */     defaultDisassembler = (Disassembler)objIn.readObject();
/* 144 */     in.close();
/*     */   }
/*     */   
/*     */   public static synchronized void init() throws Exception
/*     */   {
/* 149 */     init("/resources/disassembler.bin");
/*     */   }
/*     */   
/*     */   public static synchronized Disassembler getDisassembler()
/*     */   {
/* 154 */     return defaultDisassembler;
/*     */   }
/*     */ }


/* Location:              C:\Users\Joey\Downloads\codejar.jar!\com\emt\proteus\decoder\Disassembler.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */