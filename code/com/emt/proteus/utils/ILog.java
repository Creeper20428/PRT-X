/*     */ package com.emt.proteus.utils;
/*     */ 
/*     */ import com.emt.proteus.runtime32.FatalException;
/*     */ import java.io.PrintStream;
/*     */ import java.io.PrintWriter;
/*     */ import java.util.logging.Level;
/*     */ import java.util.logging.Logger;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract interface ILog
/*     */ {
/*     */   public static final String D_FMT = "[%12s] [%6s] %s%n";
/*  42 */   public static final ILog NULL = new ILog()
/*     */   {
/*     */     public void debug(String source, String fmt, Object... args) {}
/*     */     
/*     */ 
/*     */     public void info(String source, String fmt, Object... args) {}
/*     */     
/*     */ 
/*     */     public void warn(String source, String fmt, Object... args) {}
/*     */     
/*     */ 
/*     */     public void error(String source, String fmt, Object... args) {}
/*     */     
/*     */ 
/*     */     public void fatal(String source, String fmt, Object... args) {}
/*     */     
/*     */ 
/*     */     public void enter(String source, int depth, int fn, Object label) {}
/*     */     
/*     */ 
/*     */     public void exit(String source, int depth, int fn, int ret, Object label) {}
/*     */     
/*     */ 
/*     */     public void out(String source, String prefix, String fmt, Object... args) {}
/*     */   };
/*     */   
/*     */ 
/*     */   public abstract void debug(String paramString1, String paramString2, Object... paramVarArgs);
/*     */   
/*     */ 
/*     */   public abstract void info(String paramString1, String paramString2, Object... paramVarArgs);
/*     */   
/*     */ 
/*     */   public abstract void warn(String paramString1, String paramString2, Object... paramVarArgs);
/*     */   
/*     */ 
/*     */   public abstract void error(String paramString1, String paramString2, Object... paramVarArgs);
/*     */   
/*     */ 
/*     */   public abstract void fatal(String paramString1, String paramString2, Object... paramVarArgs);
/*     */   
/*     */   public abstract void enter(String paramString, int paramInt1, int paramInt2, Object paramObject);
/*     */   
/*     */   public abstract void exit(String paramString, int paramInt1, int paramInt2, int paramInt3, Object paramObject);
/*     */   
/*     */   public abstract void out(String paramString1, String paramString2, String paramString3, Object... paramVarArgs);
/*     */   
/*     */   public static abstract class Base
/*     */     implements ILog
/*     */   {
/*     */     private int level;
/*     */     
/*     */     public static String fmtExit(int depth, int fn, int ret, Object label, StringBuilder scratch)
/*     */     {
/*  96 */       String fmt = "";
/*  97 */       synchronized (scratch) {
/*  98 */         scratch.setLength(0);
/*  99 */         Utils.padding(depth * 3, scratch);
/* 100 */         Utils.appendInt(fn, scratch);
/* 101 */         scratch.append("->");
/* 102 */         Utils.appendInt(ret, scratch);
/* 103 */         if (label != null) scratch.append(' ').append(label);
/* 104 */         fmt = scratch.toString();
/*     */       }
/* 106 */       return fmt;
/*     */     }
/*     */     
/*     */     public static String fmtEnter(int depth, int fn, Object label, StringBuilder scratch) {
/*     */       String fmt;
/* 111 */       synchronized (scratch) {
/* 112 */         scratch.setLength(0);
/* 113 */         Utils.padding(depth * 3, scratch);
/* 114 */         Utils.appendInt(fn, scratch);
/* 115 */         if (label != null) scratch.append(' ').append(label);
/* 116 */         fmt = scratch.toString();
/*     */       }
/* 118 */       return fmt;
/*     */     }
/*     */     
/*     */ 
/* 122 */     private StringBuilder scratch = new StringBuilder();
/*     */     
/*     */     protected Base() {
/* 125 */       this.level = Level.WARNING.intValue();
/*     */     }
/*     */     
/* 128 */     public void setLevel(Level level) { this.level = level.intValue(); }
/*     */     
/*     */ 
/* 131 */     public void setLevel(int level) { this.level = level; }
/*     */     
/*     */     public void debug(String source, String fmt, Object... args) {
/* 134 */       if (this.level <= Level.FINE.intValue()) out(source, "DEBUG", fmt, args);
/*     */     }
/*     */     
/* 137 */     public void info(String source, String fmt, Object... args) { if (this.level <= Level.INFO.intValue()) out(source, "INFO", fmt, args);
/*     */     }
/*     */     
/* 140 */     public void out(String source, String fmt, Object... args) { out(source, "FATAL", fmt, args); }
/*     */     
/*     */     public void warn(String source, String fmt, Object... args) {
/* 143 */       if (this.level <= Level.WARNING.intValue()) out(source, "WARN", fmt, args);
/*     */     }
/*     */     
/* 146 */     public void error(String source, String fmt, Object... args) { out(source, "ERROR", fmt, args); }
/*     */     
/*     */     public void fatal(String source, String fmt, Object... args) {
/* 149 */       out(source, "FATAL", fmt, args);
/* 150 */       throw new FatalException(String.format(fmt, args));
/*     */     }
/*     */     
/* 153 */     public void enter(String source, int depth, int fn, Object label) { String fmt = "";
/* 154 */       StringBuilder scratch = this.scratch;
/* 155 */       fmt = fmtEnter(depth, fn, label, scratch);
/* 156 */       out(source, "ENTER", fmt, new Object[0]);
/*     */     }
/*     */     
/* 159 */     public void exit(String source, int depth, int fn, int ret, Object label) { StringBuilder scratch = this.scratch;
/* 160 */       String fmt = fmtExit(depth, fn, ret, label, scratch);
/* 161 */       out(source, "EXIT", fmt, new Object[0]);
/*     */     }
/*     */     
/*     */     public int getLevel()
/*     */     {
/* 166 */       return this.level;
/*     */     }
/*     */   }
/*     */   
/*     */   public static class Stream extends ILog.Base
/*     */   {
/*     */     private final PrintStream stream;
/*     */     
/*     */     public Stream(PrintStream stream)
/*     */     {
/* 176 */       this.stream = stream;
/*     */     }
/*     */     
/*     */     public void out(String source, String prefix, String fmt, Object... args) {
/* 180 */       this.stream.printf("[%12s] [%6s] %s%n", new Object[] { source, prefix, String.format(fmt, args) });
/*     */     }
/*     */   }
/*     */   
/*     */   public static class Writer extends ILog.Base {
/*     */     private final PrintWriter writer;
/*     */     
/* 187 */     public Writer(PrintWriter writer) { this.writer = writer; }
/*     */     
/*     */     public void out(String source, String prefix, String fmt, Object... args)
/*     */     {
/* 191 */       this.writer.printf("[%12s] [%6s] %s%n", new Object[] { source, prefix, String.format(fmt, args) });
/*     */     }
/*     */   }
/*     */   
/*     */   public static class Err extends ILog.Base
/*     */   {
/*     */     public void out(String source, String prefix, String fmt, Object... args)
/*     */     {
/* 199 */       System.out.flush();
/* 200 */       System.err.printf("[%12s] [%6s] %s%n", new Object[] { source, prefix, String.format(fmt, args) });
/*     */     }
/*     */   }
/*     */   
/*     */   public static class Log implements ILog {
/* 205 */     public void debug(String source, String fmt, Object... args) { Logger.getLogger(source).fine(String.format(fmt, args)); }
/*     */     
/*     */     public void info(String source, String fmt, Object... args) {
/* 208 */       Logger.getLogger(source).info(String.format(fmt, args));
/*     */     }
/*     */     
/* 211 */     public void warn(String source, String fmt, Object... args) { Logger.getLogger(source).warning(String.format(fmt, args)); }
/*     */     
/*     */     public void error(String source, String fmt, Object... args)
/*     */     {
/* 215 */       Logger.getLogger(source).severe(String.format(fmt, args));
/*     */     }
/*     */     
/*     */ 
/* 219 */     public void out(String source, String prefix, String fmt, Object... args) { Logger.getLogger(source).log(Level.SEVERE, String.format(fmt, args)); }
/*     */     
/*     */     public void fatal(String source, String fmt, Object... args) {
/* 222 */       Logger.getLogger(source).severe(String.format(fmt, args));
/* 223 */       System.exit(-1);
/*     */     }
/*     */     
/*     */     public void enter(String source, int depth, int fn, Object label) {
/* 227 */       Logger.getLogger(source).severe(ILog.Base.fmtEnter(depth, fn, label, new StringBuilder()));
/*     */     }
/*     */     
/*     */     public void exit(String source, int depth, int fn, int ret, Object label) {
/* 231 */       Logger.getLogger(source).severe(ILog.Base.fmtExit(depth, fn, ret, label, new StringBuilder()));
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Joey\Downloads\codejar.jar!\com\emt\proteus\utils\ILog.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */