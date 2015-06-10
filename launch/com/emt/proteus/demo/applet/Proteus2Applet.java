/*     */ package com.emt.proteus.demo.applet;
/*     */ 
/*     */ import com.emt.proteus.demo.common.ConsoleStream;
/*     */ import com.emt.proteus.demo.common.ControlPanel;
/*     */ import com.emt.proteus.demo.common.JConsolePanel;
/*     */ import com.emt.proteus.demo.common.ProteusHandle;
/*     */ import java.io.UnsupportedEncodingException;
/*     */ import java.net.URL;
/*     */ import java.util.Timer;
/*     */ import java.util.TimerTask;
/*     */ import javax.swing.JApplet;
/*     */ import javax.swing.UIManager;
/*     */ import javax.swing.UIManager.LookAndFeelInfo;
/*     */ 
/*     */ 
/*     */ public class Proteus2Applet
/*     */   extends JApplet
/*     */ {
/*     */   private TimerTask task;
/*     */   private Timer timer;
/*     */   private URL[] urls;
/*     */   private ControlPanel controlPanel;
/*     */   private ConsoleStream cps;
/*     */   private ProteusHandle handle;
/*     */   
/*     */   public Proteus2Applet() {}
/*     */   
/*     */   public Proteus2Applet(URL[] urls)
/*     */   {
/*  30 */     this.urls = urls;
/*     */   }
/*     */   
/*     */   public void init() {
/*     */     try {
/*  35 */       if (this.urls == null)
/*     */       {
/*  37 */         this.urls = new URL[] { new URL(getDocumentBase(), "./codejar.jar") };
/*     */       }
/*  39 */       setLookAndFeel();
/*  40 */       int width = getInt("xwidth", 430);
/*  41 */       int height = getInt("xheight", 340);
/*     */       
/*  43 */       ClassLoader classLoader = getClass().getClassLoader();
/*  44 */       this.handle = new ProteusHandle("com.emt.proteus.runtime32.demo.ProcessImpl", classLoader, this.urls);
/*     */       
/*     */ 
/*     */ 
/*  48 */       this.controlPanel = new ControlPanel(this.handle, width, height);
/*     */       try {
/*  50 */         this.cps = new ConsoleStream();
/*     */       } catch (UnsupportedEncodingException e) {
/*  52 */         e.printStackTrace();
/*     */       }
/*  54 */       this.handle.setStdOut(this.cps);
/*  55 */       this.handle.setStdErr(this.cps);
/*  56 */       String program = getParameter("program");
/*  57 */       if ((program != null) && (!program.isEmpty())) {
/*  58 */         this.handle.setProgram(program);
/*     */       }
/*  60 */       this.timer = new Timer();
/*  61 */       this.task = new TimerTask()
/*     */       {
/*     */         public void run() {
/*  64 */           String consoleOutput = Proteus2Applet.this.cps.getConsoleOutput();
/*  65 */           Proteus2Applet.this.controlPanel.getConsolePanel().updateText(consoleOutput);
/*     */         }
/*     */         
/*  68 */       };
/*  69 */       add(this.controlPanel);
/*     */     } catch (Exception e) {
/*  71 */       e.printStackTrace();
/*     */     }
/*     */   }
/*     */   
/*     */   private void setLookAndFeel()
/*     */   {
/*     */     try
/*     */     {
/*  79 */       UIManager.LookAndFeelInfo named = null;
/*  80 */       UIManager.LookAndFeelInfo[] installedLookAndFeels = UIManager.getInstalledLookAndFeels();
/*  81 */       for (int i = 0; i < installedLookAndFeels.length; i++) {
/*  82 */         UIManager.LookAndFeelInfo installedLookAndFeel = installedLookAndFeels[i];
/*  83 */         if (installedLookAndFeel.getName().equalsIgnoreCase("nimbus")) {
/*  84 */           named = installedLookAndFeel;
/*  85 */           break;
/*     */         }
/*     */       }
/*  88 */       if (named == null) return;
/*  89 */       String sysLandF = named.getClassName();
/*  90 */       UIManager.setLookAndFeel(sysLandF);
/*     */     }
/*     */     catch (Exception e) {
/*  93 */       e.printStackTrace();
/*     */     }
/*     */   }
/*     */   
/*     */   public void start()
/*     */   {
/*  99 */     this.timer.schedule(this.task, 1000L, 200L);
/* 100 */     this.handle.run();
/*     */   }
/*     */   
/*     */   public void stop() {
/* 104 */     this.timer.cancel();
/* 105 */     this.handle.stop();
/*     */   }
/*     */   
/*     */   public void destroy() {
/* 109 */     this.handle.dispose();
/*     */   }
/*     */   
/*     */   public void setURLs(URL[] URLs) {
/* 113 */     this.urls = URLs;
/*     */   }
/*     */   
/*     */   private int getInt(String key, int defaultv) {
/* 117 */     String dim = getParameter(key);
/* 118 */     if ((dim != null) && (!dim.isEmpty())) {
/*     */       try {
/* 120 */         return Integer.parseInt(dim.trim());
/*     */       } catch (Exception e) {}
/*     */     }
/* 123 */     return defaultv;
/*     */   }
/*     */ }


/* Location:              C:\Users\Joey\Downloads\launch.jar!\com\emt\proteus\demo\applet\Proteus2Applet.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */