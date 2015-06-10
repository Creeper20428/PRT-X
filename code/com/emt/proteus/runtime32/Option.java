/*     */ package com.emt.proteus.runtime32;
/*     */ 
/*     */ import java.io.PrintStream;
/*     */ import java.lang.reflect.Constructor;
/*     */ import java.lang.reflect.Method;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Hashtable;
/*     */ import java.util.Iterator;
/*     */ 
/*     */ public abstract class Option
/*     */ {
/*  12 */   private static final Hashtable<String, Option> names2options = new Hashtable();
/*     */   
/*  14 */   public static final Switch interp = createSwitch("int");
/*  15 */   public static final Switch dynamic = createSwitch("dynamic");
/*  16 */   public static final Switch strace = createSwitch("strace");
/*  17 */   public static final Switch ltrace = createSwitch("ltrace");
/*  18 */   public static final Switch trace = createSwitch("trace");
/*  19 */   public static final Switch decode = createSwitch("log-decode");
/*  20 */   public static final Switch stackWatch = createSwitch("stack-watch");
/*  21 */   public static final Opt copylib = opt("copylib");
/*  22 */   public static final Opt watch = opt("mem-watch");
/*  23 */   public static final Switch futex = createSwitch("log-futex");
/*  24 */   public static final Opt libpath = opt("libpath");
/*     */   
/*  26 */   public static final Opt watch_len = opt("mem-watch-len");
/*  27 */   public static final Opt watch_function = opt("watch-function");
/*  28 */   public static final Opt suspend_function = opt("suspend-function");
/*  29 */   public static final Opt dumpAddress = opt("dump-address");
/*  30 */   public static final Switch log_relocation = createSwitch("log-relocation");
/*  31 */   public static final Switch compareIntToCompByFunction = createSwitch("compareIntToCompByFunction");
/*  32 */   public static final Switch compareIntToCompByBlock = createSwitch("compareIntToCompByBlock");
/*  33 */   public static final Opt log = opt("log");
/*  34 */   public static final Opt compare = opt("compare");
/*  35 */   public static final Opt recompare = opt("recompare");
/*  36 */   public static final Switch loopcompare = createSwitch("loopcompare");
/*  37 */   public static final Opt IO_FILES = opt("files");
/*  38 */   public static final Opt ss = opt("ss");
/*  39 */   public static final Opt stackbase = opt("stackbase");
/*  40 */   public static final Opt postskip = opt("postskip");
/*  41 */   public static final Switch run1 = createSwitch("run1");
/*  42 */   public static final Opt benchmark = opt("benchmark");
/*  43 */   public static final Switch time = createSwitch("time");
/*     */   private final String name;
/*     */   
/*  46 */   public static String[] parse(String[] source) { ArrayList<String> tmp = new ArrayList();
/*  47 */     for (Iterator<Option> iterator = names2options.values().iterator(); iterator.hasNext();) {
/*  48 */       Option next = (Option)iterator.next();
/*  49 */       next.set = false;
/*     */     }
/*  51 */     for (int index = 0; 
/*  52 */         index < source.length; index++) {
/*  53 */       String arg = source[index];
/*  54 */       if (arg.startsWith("-")) {
/*  55 */         arg = arg.substring(1);
/*     */       }
/*  57 */       Option opt = (Option)names2options.get(arg);
/*  58 */       if (opt == null) {
/*  59 */         tmp.add(source[index]);
/*     */       } else {
/*  61 */         opt.set = true;
/*  62 */         index = opt.update(source, index);
/*     */       }
/*     */     }
/*  65 */     if (tmp.size() == source.length) {
/*  66 */       return source;
/*     */     }
/*  68 */     return (String[])tmp.toArray(new String[tmp.size()]);
/*     */   }
/*     */   
/*     */   public static Switch createSwitch(String name)
/*     */   {
/*  73 */     Switch sw = (Switch)names2options.get(name);
/*  74 */     if (sw == null) {
/*  75 */       sw = new Switch(name);
/*     */     }
/*  77 */     return sw;
/*     */   }
/*     */   
/*     */   public static Opt opt(String name) {
/*  81 */     Opt opt = (Opt)names2options.get(name);
/*  82 */     if (opt == null) {
/*  83 */       opt = new Opt(name);
/*     */     }
/*  85 */     return opt;
/*     */   }
/*     */   
/*     */ 
/*     */   private boolean set;
/*     */   
/*     */   protected Option(String name)
/*     */   {
/*  93 */     this.name = name;
/*  94 */     names2options.put(name, this);
/*     */   }
/*     */   
/*     */   public String getName() {
/*  98 */     return this.name;
/*     */   }
/*     */   
/*     */   public boolean isSet() {
/* 102 */     return this.set;
/*     */   }
/*     */   
/*     */   public abstract Object getValue();
/*     */   
/*     */   protected abstract int update(String[] paramArrayOfString, int paramInt);
/*     */   
/*     */   public static class Opt extends Option
/*     */   {
/*     */     private String value;
/*     */     
/*     */     public Opt(String name)
/*     */     {
/* 115 */       super();
/*     */     }
/*     */     
/*     */     public int update(String[] args, int index)
/*     */     {
/* 120 */       this.value = args[(++index)];
/* 121 */       return index;
/*     */     }
/*     */     
/*     */ 
/*     */     public Object getValue()
/*     */     {
/* 127 */       return this.value;
/*     */     }
/*     */     
/*     */     public int intValue(int defaultValue) {
/* 131 */       if (this.value != null) {
/* 132 */         return Integer.parseInt(this.value.trim());
/*     */       }
/* 134 */       return defaultValue;
/*     */     }
/*     */     
/*     */     public int intValue(int defaultValue, int radix) {
/* 138 */       if (this.value != null) {
/* 139 */         return (int)Long.parseLong(this.value.trim(), radix);
/*     */       }
/* 141 */       return defaultValue;
/*     */     }
/*     */     
/*     */ 
/*     */     public long longValue(long defaultValue, int radix)
/*     */     {
/* 147 */       if (this.value != null) {
/* 148 */         return Long.parseLong(this.value.trim(), radix);
/*     */       }
/* 150 */       return defaultValue;
/*     */     }
/*     */     
/*     */     public String[] arrayValue(String[] defaultValue) {
/* 154 */       if (this.value != null) {
/* 155 */         return this.value.split(":");
/*     */       }
/* 157 */       return defaultValue;
/*     */     }
/*     */     
/*     */ 
/*     */     public String value(String defaultValue)
/*     */     {
/* 163 */       if (this.value != null) {
/* 164 */         return this.value;
/*     */       }
/* 166 */       return defaultValue;
/*     */     }
/*     */     
/*     */     public String value()
/*     */     {
/* 171 */       return this.value;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     public Object valueOf(Class type, Object defaultValue)
/*     */     {
/* 180 */       if (this.value == null) return defaultValue;
/* 181 */       Throwable t = null;
/*     */       try
/*     */       {
/* 184 */         Method valueOf = type.getMethod("valueOf", new Class[] { String.class });
/* 185 */         return valueOf.invoke(type, new Object[] { this.value });
/*     */       } catch (NoSuchMethodException e) {
/* 187 */         System.err.println(type + " :No suitable method");
/*     */         try
/*     */         {
/* 190 */           Constructor constructor = type.getConstructor(new Class[] { String.class });
/* 191 */           return constructor.newInstance(new Object[] { this.value });
/*     */         } catch (NoSuchMethodException e) {
/* 193 */           System.err.println(type + " :No suitable Constructor");
/*     */         }
/*     */       } catch (Exception e) {
/* 196 */         throw new FatalException("Nested Exception" + type, e);
/*     */       }
/* 198 */       throw new FatalException("Unable obtain value of " + type);
/*     */     }
/*     */     
/*     */     public Object instance(String defaultClassName)
/*     */     {
/* 203 */       if (this.value == null) {
/* 204 */         this.value = defaultClassName;
/*     */       }
/*     */       try {
/* 207 */         return Class.forName(this.value.trim()).newInstance();
/*     */       } catch (InstantiationException e) {
/* 209 */         throw new FatalException("Nested Exception", e);
/*     */       }
/*     */       catch (IllegalAccessException e) {
/* 212 */         throw new FatalException("Nested Exception", e);
/*     */       } catch (ClassNotFoundException e) {
/* 214 */         throw new FatalException("Nested Exception", e);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public static class Switch extends Option { private boolean value;
/*     */     
/* 221 */     public Switch(String name) { super(); }
/*     */     
/*     */     public boolean value() {
/* 224 */       return this.value;
/*     */     }
/*     */     
/*     */     public Object getValue()
/*     */     {
/* 229 */       return Boolean.valueOf(this.value);
/*     */     }
/*     */     
/*     */     public int update(String[] args, int index)
/*     */     {
/* 234 */       this.value = true;
/* 235 */       return index;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Joey\Downloads\codejar.jar!\com\emt\proteus\runtime32\Option.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */