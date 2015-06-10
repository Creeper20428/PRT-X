/*     */ package com.emt.proteus.demo.common;
/*     */ 
/*     */ import com.emt.proteus.runtime32.api.IoSystem;
/*     */ import com.emt.proteus.runtime32.api.ProteusProcess;
/*     */ import java.io.PrintStream;
/*     */ import java.net.URL;
/*     */ import java.net.URLClassLoader;
/*     */ import java.util.concurrent.ExecutorService;
/*     */ import java.util.concurrent.Executors;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ProteusHandle
/*     */ {
/*  17 */   private ExecutorService async = Executors.newSingleThreadExecutor();
/*  18 */   private ExecutorService clients = Executors.newSingleThreadExecutor();
/*     */   
/*     */   private String className;
/*     */   private ClassLoader parentClassLoader;
/*     */   private URL[] childClassPath;
/*     */   private BasicIoSystem ioSystem;
/*     */   private ProteusProcess process;
/*  25 */   private String program = "xeyes";
/*     */   
/*     */   public ProteusHandle(String className, ClassLoader parentClassLoader, URL... childClassPath)
/*     */   {
/*  29 */     this.className = className;
/*  30 */     this.parentClassLoader = parentClassLoader;
/*  31 */     this.childClassPath = childClassPath;
/*  32 */     this.ioSystem = new BasicIoSystem(this.clients);
/*     */   }
/*     */   
/*     */   public String getClassName() {
/*  36 */     return this.className;
/*     */   }
/*     */   
/*     */   public void setClassName(String className) {
/*  40 */     this.className = className;
/*     */   }
/*     */   
/*     */   public ClassLoader getParentClassLoader() {
/*  44 */     return this.parentClassLoader;
/*     */   }
/*     */   
/*     */   public void setParentClassLoader(ClassLoader parentClassLoader) {
/*  48 */     this.parentClassLoader = parentClassLoader;
/*     */   }
/*     */   
/*     */   public URL[] getChildClassPath() {
/*  52 */     return this.childClassPath;
/*     */   }
/*     */   
/*     */   public void setChildClassPath(URL[] childClassPath) {
/*  56 */     this.childClassPath = childClassPath;
/*     */   }
/*     */   
/*     */   public IoSystem getIoSystem() {
/*  60 */     return this.ioSystem;
/*     */   }
/*     */   
/*     */   public void setIoSystem(BasicIoSystem ioSystem) {
/*  64 */     this.ioSystem = ioSystem;
/*     */   }
/*     */   
/*     */   public String getProgram() {
/*  68 */     return this.program;
/*     */   }
/*     */   
/*     */   public void setProgram(String program) {
/*  72 */     if (!program.startsWith("tests/")) program = "tests/" + program;
/*  73 */     this.program = program;
/*     */   }
/*     */   
/*     */   public boolean create() {
/*  77 */     disposeProcess();
/*  78 */     ClassLoader classLoader = URLClassLoader.newInstance(this.childClassPath, ProteusHandle.class.getClassLoader());
/*     */     try {
/*  80 */       this.process = ((ProteusProcess)classLoader.loadClass(this.className).newInstance());
/*  81 */       this.process.init(this.ioSystem);
/*  82 */       this.ioSystem.setLoader(classLoader);
/*  83 */       return true;
/*     */     } catch (Exception e) {
/*  85 */       e.printStackTrace();
/*     */     }
/*  87 */     return false;
/*     */   }
/*     */   
/*     */   private void disposeProcess() {
/*  91 */     if (this.process != null) {
/*  92 */       this.ioSystem.setLoader(null);
/*  93 */       this.process.dispose();
/*     */     }
/*     */     
/*  96 */     this.process = null;
/*  97 */     System.gc();
/*  98 */     System.gc();
/*  99 */     System.gc();
/*     */   }
/*     */   
/*     */   public void dispose() {
/* 103 */     disposeProcess();
/* 104 */     this.async.shutdown();
/* 105 */     this.clients.shutdown();
/*     */   }
/*     */   
/*     */   public void reset() {
/* 109 */     disposeProcess();
/*     */     try {
/* 111 */       ConsoleStream err = (ConsoleStream)this.ioSystem.getStdErr();
/* 112 */       err.clear();
/*     */     }
/*     */     catch (ClassCastException cce) {}
/*     */   }
/*     */   
/*     */   private void runImpl()
/*     */   {
/*     */     try {
/* 120 */       if (this.process == null) create();
/* 121 */       String[] arguments = { this.program };
/* 122 */       this.process.exec(arguments);
/*     */     } catch (Exception e) {
/* 124 */       e.printStackTrace();
/*     */     }
/*     */   }
/*     */   
/*     */   public void run()
/*     */   {
/* 130 */     this.async.submit(new Runnable()
/*     */     {
/*     */       public void run() {
/* 133 */         ProteusHandle.this.runImpl();
/*     */       }
/*     */     });
/*     */   }
/*     */   
/*     */   public void setStdErr(PrintStream ps) {
/* 139 */     this.ioSystem.setStdErr(ps);
/*     */   }
/*     */   
/*     */   public void setStdOut(PrintStream ps) {
/* 143 */     this.ioSystem.setStdOut(ps);
/*     */   }
/*     */   
/*     */   public void stop() {}
/*     */ }


/* Location:              C:\Users\Joey\Downloads\launch.jar!\com\emt\proteus\demo\common\ProteusHandle.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */