/*     */ package com.emt.proteus.runtime32.io;
/*     */ 
/*     */ import com.emt.proteus.runtime32.api.Connection;
/*     */ import com.emt.proteus.runtime32.api.Connection.NIO;
/*     */ import com.emt.proteus.runtime32.api.FileProxy;
/*     */ import com.emt.proteus.runtime32.api.IoSystem;
/*     */ import com.emt.proteus.utils.Utils;
/*     */ import java.io.File;
/*     */ import java.io.FileInputStream;
/*     */ import java.io.FileNotFoundException;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.PrintStream;
/*     */ import java.io.RandomAccessFile;
/*     */ import java.net.InetAddress;
/*     */ import java.net.InetSocketAddress;
/*     */ import java.net.MalformedURLException;
/*     */ import java.net.Socket;
/*     */ import java.net.URI;
/*     */ import java.net.URL;
/*     */ import java.nio.channels.SocketChannel;
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
/*     */ public class DefaultIoSystem
/*     */   implements IoSystem
/*     */ {
/*     */   public PrintStream getStdOut()
/*     */   {
/*  52 */     return System.out;
/*     */   }
/*     */   
/*     */   public InputStream getStdIn()
/*     */   {
/*  57 */     return System.in;
/*     */   }
/*     */   
/*     */   public PrintStream getStdErr()
/*     */   {
/*  62 */     return System.err;
/*     */   }
/*     */   
/*     */   public InputStream open(URL url) throws IOException {
/*  66 */     return url.openStream();
/*     */   }
/*     */   
/*     */ 
/*  70 */   public InputStream open(File file) throws IOException { return new FileInputStream(file); }
/*     */   
/*     */   public InputStream createInputstream(String uri) throws IOException {
/*     */     try {
/*     */       InputStream input;
/*  75 */       if (uri.startsWith("classpath://")) {
/*  76 */         String substring = uri.substring("classpath://".length());
/*  77 */         URL resource = Utils.getResourceUrl(substring);
/*  78 */         input = open(resource);
/*     */       }
/*  80 */       return open(new File(uri));
/*     */     }
/*     */     catch (FileNotFoundException fne) {}
/*     */     
/*  84 */     return null;
/*     */   }
/*     */   
/*     */   public URL getUrl(String relativeName) throws MalformedURLException
/*     */   {
/*  89 */     if (relativeName == null) return null;
/*  90 */     if (relativeName.startsWith("classpath://")) {
/*  91 */       String substring = relativeName.substring("classpath://".length());
/*  92 */       return Utils.getResourceUrl(substring);
/*     */     }
/*  94 */     return new File(relativeName).toURI().toURL();
/*     */   }
/*     */   
/*     */   public URL getResource(String relativeName)
/*     */     throws MalformedURLException
/*     */   {
/* 100 */     return Utils.getResourceUrl(relativeName);
/*     */   }
/*     */   
/*     */   public FileProxy getFile(String relativeName, boolean writeable) throws IOException {
/* 104 */     if ((new File(relativeName).exists()) || (writeable)) {
/* 105 */       return new LocalFile(relativeName, new RandomAccessFile(relativeName, writeable ? "rw" : "r"));
/*     */     }
/* 107 */     return null;
/*     */   }
/*     */   
/*     */   public boolean exists(String fileName) throws IOException
/*     */   {
/* 112 */     return new File(fileName).exists();
/*     */   }
/*     */   
/*     */   public Connection connect(byte[] ip, int port)
/*     */     throws IOException
/*     */   {
/* 118 */     if (Connection.isXServer(ip, port)) {
/* 119 */       InetSocketAddress inetSocketAddress = new InetSocketAddress(InetAddress.getByAddress(ip), port);
/* 120 */       SocketChannel open = SocketChannel.open();
/* 121 */       Socket realSocket = open.socket();
/* 122 */       realSocket.setTcpNoDelay(true);
/* 123 */       realSocket.setKeepAlive(true);
/* 124 */       realSocket.connect(inetSocketAddress);
/* 125 */       open.configureBlocking(false);
/* 126 */       return new Connection.NIO("XSERVER", open);
/*     */     }
/* 128 */     return null;
/*     */   }
/*     */   
/*     */   public void programExit() {}
/*     */ }


/* Location:              C:\Users\Joey\Downloads\codejar.jar!\com\emt\proteus\runtime32\io\DefaultIoSystem.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */