/*     */ package com.emt.proteus.demo.common;
/*     */ 
/*     */ import com.emt.proteus.runtime32.api.Connection;
/*     */ import com.emt.proteus.runtime32.api.FileProxy;
/*     */ import com.emt.proteus.runtime32.api.FileProxy.Memory;
/*     */ import com.emt.proteus.xserver.client.XClient;
/*     */ import com.emt.proteus.xserver.display.XDisplay;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.OutputStream;
/*     */ import java.io.PipedInputStream;
/*     */ import java.io.PipedOutputStream;
/*     */ import java.io.PrintStream;
/*     */ import java.net.MalformedURLException;
/*     */ import java.net.URL;
/*     */ import java.util.concurrent.Callable;
/*     */ import java.util.concurrent.ExecutorService;
/*     */ 
/*     */ public class BasicIoSystem implements com.emt.proteus.runtime32.api.IoSystem
/*     */ {
/*     */   private boolean useClassPath;
/*  22 */   private PrintStream stdOut = System.out;
/*  23 */   private InputStream stdIn = System.in;
/*  24 */   private PrintStream stdErr = System.err;
/*     */   private XDisplay display;
/*     */   private ExecutorService connections;
/*     */   private ClassLoader loader;
/*     */   private Connector currentConnector;
/*     */   
/*     */   public BasicIoSystem(ExecutorService connections) {
/*  31 */     this.connections = connections;
/*     */   }
/*     */   
/*     */   public XDisplay getDisplay()
/*     */   {
/*  36 */     return this.display;
/*     */   }
/*     */   
/*     */   public void setDisplay(XDisplay display) {
/*  40 */     this.display = display;
/*     */   }
/*     */   
/*     */   public PrintStream getStdOut() {
/*  44 */     return this.stdOut;
/*     */   }
/*     */   
/*     */   public void setStdOut(PrintStream stdOut) {
/*  48 */     this.stdOut = stdOut;
/*     */   }
/*     */   
/*     */   public InputStream getStdIn() {
/*  52 */     return this.stdIn;
/*     */   }
/*     */   
/*     */   public void setStdIn(InputStream stdIn) {
/*  56 */     this.stdIn = stdIn;
/*     */   }
/*     */   
/*     */   public PrintStream getStdErr() {
/*  60 */     return this.stdErr;
/*     */   }
/*     */   
/*     */   public void setStdErr(PrintStream stdErr) {
/*  64 */     this.stdErr = stdErr;
/*     */   }
/*     */   
/*     */   public Connection connect(byte[] ip, int port) throws IOException {
/*  68 */     if (Connection.isXServer(ip, port)) {
/*  69 */       PipedOutputStream xout = new PipedOutputStream();
/*  70 */       PipedOutputStream pout = new PipedOutputStream();
/*  71 */       PipedInputStream xin = new PipedInputStream(pout, 8192);
/*  72 */       PipedInputStream pin = new PipedInputStream(xout, 8192);
/*  73 */       if (this.currentConnector != null) this.currentConnector.dispose();
/*  74 */       this.currentConnector = new Connector(this.display, xin, xout);
/*  75 */       this.connections.submit(this.currentConnector);
/*  76 */       return new com.emt.proteus.runtime32.api.Connection.Stream("xserver", pin, pout);
/*     */     }
/*  78 */     return null;
/*     */   }
/*     */   
/*  81 */   public InputStream open(URL url) throws IOException { return url.openStream(); }
/*     */   
/*     */   public InputStream createInputstream(String uri) throws IOException {
/*     */     try {
/*     */       InputStream input;
/*  86 */       if (uri.startsWith("classpath://")) {
/*  87 */         String substring = uri.substring("classpath://".length());
/*  88 */         URL resource = getResource(substring);
/*  89 */         input = open(resource);
/*     */       }
/*  91 */       return getFile(uri, false).getInputStream();
/*     */     }
/*     */     catch (Exception fne) {}
/*     */     
/*  95 */     return null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/* 100 */   public URL getUrl(String relativeName) throws MalformedURLException { return getResource(relativeName); }
/*     */   
/*     */   public URL getResource(String relativeName) throws MalformedURLException {
/* 103 */     ClassLoader loader = this.loader;
/* 104 */     if (loader == null) loader = getClass().getClassLoader();
/* 105 */     return loader.getResource(relativeName);
/*     */   }
/*     */   
/*     */   public ClassLoader getLoader() {
/* 109 */     return this.loader;
/*     */   }
/*     */   
/*     */   public void setLoader(ClassLoader loader) {
/* 113 */     this.loader = loader;
/*     */   }
/*     */   
/*     */   public FileProxy getFile(String relativeName, boolean create)
/*     */     throws IOException
/*     */   {
/* 119 */     FileProxy result = null;
/*     */     try {
/* 121 */       URL url = getResource(relativeName);
/* 122 */       result = url != null ? new FileProxy.Memory(relativeName, url) : null;
/* 123 */       localFileProxy1 = result;return localFileProxy1;
/* 124 */     } catch (Exception e) { e = e;
/*     */       
/* 126 */       FileProxy localFileProxy1 = null;return localFileProxy1;
/*     */     }
/*     */     finally {}
/*     */   }
/*     */   
/*     */   public boolean exists(String fileName)
/*     */     throws IOException
/*     */   {
/* 134 */     return false;
/*     */   }
/*     */   
/*     */   public void programExit()
/*     */   {
/* 139 */     if (this.currentConnector != null) this.currentConnector.dispose();
/*     */   }
/*     */   
/*     */   public class Connector implements Callable
/*     */   {
/*     */     private XDisplay display;
/*     */     private InputStream xin;
/*     */     private OutputStream xout;
/*     */     
/*     */     public Connector(XDisplay display, InputStream xin, OutputStream xout) {
/* 149 */       this.display = display;
/* 150 */       this.xin = xin;
/* 151 */       this.xout = xout;
/*     */     }
/*     */     
/*     */     public Object call() throws Exception
/*     */     {
/*     */       try {
/* 157 */         XClient client = XClient.connect(this.display, this.xin, this.xout);
/* 158 */         client.run();
/* 159 */         return Boolean.valueOf(true);
/*     */       } finally {
/* 161 */         dispose();
/* 162 */         if (BasicIoSystem.this.currentConnector == this) {
/* 163 */           BasicIoSystem.this.currentConnector = null;
/*     */         }
/*     */       }
/*     */     }
/*     */     
/*     */     public void dispose() {
/*     */       try {
/* 170 */         this.xin.close();
/*     */       }
/*     */       catch (Exception e) {}
/*     */       try {
/* 174 */         this.xout.close();
/*     */       }
/*     */       catch (Exception e) {}
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Joey\Downloads\launch.jar!\com\emt\proteus\demo\common\BasicIoSystem.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */