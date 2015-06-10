/*     */ package com.emt.proteus.xserver.api;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.PrintStream;
/*     */ import java.net.URL;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import java.util.Properties;
/*     */ 
/*     */ 
/*     */ 
/*     */ public class Context
/*     */ {
/*     */   public static final String EVENT_ROOT = "EVENT_ROOT";
/*     */   public static final String CONTAINER = "CONTAINER";
/*     */   public static final String SERVER = "server";
/*     */   private Properties configuration;
/*  19 */   private Map<String, Object> attributes = new HashMap();
/*     */   
/*     */ 
/*  22 */   public Context(Properties configuration) { this.configuration = configuration; }
/*     */   
/*     */   public Context() {
/*  25 */     this(new Properties());
/*  26 */     if ((!load("xserver.properties")) && (!load("/xserver.properties"))) {
/*  27 */       System.err.println("Unable to load defaults");
/*     */     }
/*     */   }
/*     */   
/*     */   public boolean load(String resourceName) {
/*     */     try {
/*  33 */       URL resource = Context.class.getClassLoader().getResource(resourceName);
/*  34 */       load(resource);
/*  35 */       return true;
/*     */     } catch (IOException e) {}
/*  37 */     return false;
/*     */   }
/*     */   
/*     */   public void load(URL resource) throws IOException
/*     */   {
/*  42 */     load(resource.openStream());
/*     */   }
/*     */   
/*  45 */   public void load(InputStream resource) throws IOException { this.configuration.load(resource); }
/*     */   
/*     */   public Object getInstance(String property, String defaultClass)
/*     */   {
/*  49 */     Object instance = null;
/*  50 */     String classname = this.configuration.getProperty(property, defaultClass);
/*     */     try {
/*  52 */       instance = Class.forName(classname).newInstance();
/*     */     } catch (InstantiationException e) {
/*  54 */       e.printStackTrace();
/*     */     } catch (IllegalAccessException e) {
/*  56 */       e.printStackTrace();
/*     */     } catch (ClassNotFoundException e) {
/*  58 */       e.printStackTrace();
/*     */     }
/*  60 */     if ((instance instanceof Configurable)) {
/*  61 */       ((Configurable)instance).initialize(this);
/*     */     }
/*  63 */     return instance;
/*     */   }
/*     */   
/*     */   public void set(String attr, Object value) {
/*  67 */     this.attributes.put(attr, value);
/*     */   }
/*     */   
/*     */   public Object get(String attr) {
/*  71 */     return this.attributes.get(attr);
/*     */   }
/*     */   
/*     */   public String stringValue(String key, String def) {
/*  75 */     return this.configuration.getProperty(key, def);
/*     */   }
/*     */   
/*     */   public boolean booleanValue(String s) {
/*  79 */     return Boolean.parseBoolean(stringValue(s, "false"));
/*     */   }
/*     */   
/*     */   public short shortValue(String key, int def) {
/*  83 */     String s = stringValue(key, null);
/*  84 */     if (s != null) {
/*     */       try {
/*  86 */         return Short.parseShort(s.trim());
/*     */       }
/*     */       catch (Exception e) {}
/*     */     }
/*  90 */     return (short)def;
/*     */   }
/*     */   
/*  93 */   public int intValue(String key, int def) { String s = stringValue(key, null);
/*  94 */     if (s != null) {
/*     */       try {
/*  96 */         return Integer.parseInt(s.trim());
/*     */       }
/*     */       catch (Exception e) {}
/*     */     }
/* 100 */     return def;
/*     */   }
/*     */ }


/* Location:              C:\Users\Joey\Downloads\launch.jar!\com\emt\proteus\xserver\api\Context.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */