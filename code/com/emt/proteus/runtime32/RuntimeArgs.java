/*     */ package com.emt.proteus.runtime32;
/*     */ 
/*     */ import java.util.Collection;
/*     */ import java.util.Iterator;
/*     */ import java.util.LinkedHashMap;
/*     */ import java.util.Map;
/*     */ import java.util.Map.Entry;
/*     */ import java.util.Set;
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
/*     */ public class RuntimeArgs
/*     */ {
/*     */   public static final int AT_NULL = 0;
/*     */   public static final int AT_IGNORE = 1;
/*     */   public static final int AT_EXECFD = 2;
/*     */   public static final int AT_PHDR = 3;
/*     */   public static final int AT_PHENT = 4;
/*     */   public static final int AT_PHNUM = 5;
/*     */   public static final int AT_PAGESZ = 6;
/*     */   public static final int AT_BASE = 7;
/*     */   public static final int AT_FLAGS = 8;
/*     */   public static final int AT_ENTRY = 9;
/*     */   public static final int AT_NOTELF = 10;
/*     */   public static final int AT_UID = 11;
/*     */   public static final int AT_EUID = 12;
/*     */   public static final int AT_GID = 13;
/*     */   public static final int AT_EGID = 14;
/*     */   public static final int AT_CLKTCK = 17;
/*     */   public static final int AT_PLATFORM = 15;
/*     */   public static final int AT_HWCAP = 16;
/*     */   public static final int AT_FPUCW = 18;
/*     */   public static final int AT_DCACHEBSIZE = 19;
/*     */   public static final int AT_ICACHEBSIZE = 20;
/*     */   public static final int AT_UCACHEBSIZE = 21;
/*     */   public static final int AT_IGNOREPPC = 22;
/*     */   public static final int AT_SECURE = 23;
/*     */   public static final int AT_BASE_PLATFORM = 24;
/*     */   public static final int AT_RANDOM = 25;
/*     */   public static final int AT_EXECFN = 31;
/*     */   public static final int AT_SYSINFO = 32;
/*     */   public static final int AT_SYSINFO_EHDR = 33;
/*     */   public static final int AT_L1I_CACHESHAPE = 34;
/*     */   public static final int AT_L1D_CACHESHAPE = 35;
/*     */   public static final int AT_L2_CACHESHAPE = 36;
/*     */   public static final int AT_L3_CACHESHAPE = 37;
/* 100 */   private static Aux[] DEFAULT_AUXV = { new Aux(17, 100), new Aux(16, 0), new Aux(14, 1000), new Aux(13, 1000), new Aux(11, 1000), new Aux(6, 4096), new Aux(8, 0), new Aux(23, 0), new Aux(32, -143104992), new Aux(33, 0), new Aux(4, 32), new Aux(15, "i386") };
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
/* 115 */   public static final String[][] ENV = { { "SSH_AGENT_PID", "1980" }, { "GPG_AGENT_INFO", "/tmp/keyring-e1vQ7s/gpg:0:1" }, { "TERM", "xterm" }, { "SHELL", "/bin/bash" }, { "LD_BIND_NOW", "1" }, { "XDG_SESSION_COOKIE", "50bf3dd4c06518e79f3f63e200000008-1338446273.283276-1818653475" }, { "WINDOWID", "71303174" }, { "OLDPWD", "/home/emt" }, { "GNOME_KEYRING_CONTROL", "/tmp/keyring-e1vQ7s" }, { "GTK_MODULES", "canberra-gtk-module:canberra-gtk-module" }, { "USER", "emt" }, { "LS_COLORS", "rs=0:di=01;34:ln=01;36:mh=00:pi=40;33:so=01;35:do=01;35:bd=40;33;01:cd=40;33;01:or=40;31;01:su=37;41:sg=30;43:ca=30;41:tw=30;42:ow=34;42:st=37;44:ex=01;32:*.tar=01;31:*.tgz=01;31:*.arj=01;31:*.taz=01;31:*.lzh=01;31:*.lzma=01;31:*.tlz=01;31:*.txz=01;31:*.zip=01;31:*.z=01;31:*.Z=01;31:*.dz=01;31:*.gz=01;31:*.lz=01;31:*.xz=01;31:*.bz2=01;31:*.bz=01;31:*.tbz=01;31:*.tbz2=01;31:*.tz=01;31:*.deb=01;31:*.rpm=01;31:*.jar=01;31:*.rar=01;31:*.ace=01;31:*.zoo=01;31:*.cpio=01;31:*.7z=01;31:*.rz=01;31:*.jpg=01;35:*.jpeg=01;35:*.gif=01;35:*.bmp=01;35:*.pbm=01;35:*.pgm=01;35:*.ppm=01;35:*.tga=01;35:*.xbm=01;35:*.xpm=01;35:*.tif=01;35:*.tiff=01;35:*.png=01;35:*.svg=01;35:*.svgz=01;35:*.mng=01;35:*.pcx=01;35:*.mov=01;35:*.mpg=01;35:*.mpeg=01;35:*.m2v=01;35:*.mkv=01;35:*.ogm=01;35:*.mp4=01;35:*.m4v=01;35:*.mp4v=01;35:*.vob=01;35:*.qt=01;35:*.nuv=01;35:*.wmv=01;35:*.asf=01;35:*.rm=01;35:*.rmvb=01;35:*.flc=01;35:*.avi=01;35:*.fli=01;35:*.flv=01;35:*.gl=01;35:*.dl=01;35:*.xcf=01;35:*.xwd=01;35:*.yuv=01;35:*.cgm=01;35:*.emf=01;35:*.axv=01;35:*.anx=01;35:*.ogv=01;35:*.ogx=01;35:*.aac=00;36:*.au=00;36:*.flac=00;36:*.mid=00;36:*.midi=00;36:*.mka=00;36:*.mp3=00;36:*.mpc=00;36:*.ogg=00;36:*.ra=00;36:*.wav=00;36:*.axa=00;36:*.oga=00;36:*.spx=00;36:*.xspf=00;36:" }, { "XDG_SESSION_PATH", "/org/freedesktop/DisplayManager/Session0" }, { "XDG_SEAT_PATH", "/org/freedesktop/DisplayManager/Seat0" }, { "SSH_AUTH_SOCK", "/tmp/keyring-e1vQ7s/ssh" }, { "SESSION_MANAGER", "local/EMT04U:@/tmp/.ICE-unix/1947,unix/EMT04U:/tmp/.ICE-unix/1947" }, { "USERNAME", "emt" }, { "DEFAULTS_PATH", "/usr/share/gconf/ubuntu.default.path" }, { "XDG_CONFIG_DIRS", "/etc/xdg/xdg-ubuntu:/etc/xdg" }, { "PATH", "/usr/lib/lightdm/lightdm:/usr/local/sbin:/usr/local/bin:/usr/sbin:/usr/bin:/sbin:/bin:/usr/games" }, { "DESKTOP_SESSION", "ubuntu" }, { "LC_MESSAGES", "en_GB.UTF-8" }, { "LC_COLLATE", "en_GB.UTF-8" }, { "PWD", "/home/emt/dev/emtbase/proteus2/proteus" }, { "JAVA_HOME", "/opt/java/64/jdk1.6.0_30" }, { "GNOME_KEYRING_PID", "1937" }, { "LANG", "en_GB.UTF-8" }, { "MANDATORY_PATH", "/usr/share/gconf/ubuntu.mandatory.path" }, { "UBUNTU_MENUPROXY", "libappmenu.so" }, { "COMPIZ_CONFIG_PROFILE", "ubuntu" }, { "GDMSESSION", "ubuntu" }, { "SHLVL", "1" }, { "HOME", "/home/emt" }, { "LANGUAGE", "en_GB:en" }, { "GNOME_DESKTOP_SESSION_ID", "this-is-deprecated" }, { "IBUS_ENABLE_SYNC_MODE", "1" }, { "LOGNAME", "emt" }, { "XDG_DATA_DIRS", "/usr/share/ubuntu:/usr/share/gnome:/usr/local/share/:/usr/share/" }, { "DBUS_SESSION_BUS_ADDRESS", "unix:abstract=/tmp/dbus-dUWmz6vrCB,guid=018e9f9876ee1ee8d8545c6500000908" }, { "LC_CTYPE", "en_GB.UTF-8" }, { "LESSOPEN", "| /usr/bin/lesspipe %s" }, { "DISPLAY", "127.0.0.1:2" }, { "XDG_CURRENT_DESKTOP", "Unity" }, { "LESSCLOSE", "/usr/bin/lesspipe %s %s" }, { "COLORTERM", "gnome-terminal" }, { "XAUTHORITY", "/home/emt/.Xauthority" }, { "_", "/usr/bin/env" } };
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
/* 168 */   private Map<String, String> env = new LinkedHashMap();
/* 169 */   private LinkedHashMap<Integer, Aux> aux = new LinkedHashMap();
/*     */   private String[] args;
/*     */   
/*     */   public RuntimeArgs()
/*     */   {
/* 174 */     this.args = new String[0];
/*     */   }
/*     */   
/*     */   public RuntimeArgs(String[] args, RuntimeArgs template) {
/* 178 */     this(null, args, template);
/*     */   }
/*     */   
/* 181 */   public RuntimeArgs(String[] args, Map<String, String> env) { this(null, args, env, null); }
/*     */   
/*     */   public RuntimeArgs(String program, String[] args, RuntimeArgs template)
/*     */   {
/* 185 */     this(program, args, template.env, template.aux);
/*     */   }
/*     */   
/*     */ 
/* 189 */   public RuntimeArgs(String program, String[] args, Map<String, String> env) { this(program, args, env, null); }
/*     */   
/*     */   public RuntimeArgs(String program, String[] args, Map<String, String> env, Map<Integer, Aux> auxv) {
/* 192 */     if (program != null) {
/* 193 */       String[] tmp = new String[args.length + 1];
/* 194 */       System.arraycopy(args, 0, tmp, 1, args.length);
/* 195 */       tmp[0] = program;
/* 196 */       args = tmp;
/*     */     }
/* 198 */     this.args = args;
/* 199 */     if (env != null) {
/* 200 */       this.env.putAll(env);
/*     */     } else
/* 202 */       for (int i = 0; i < ENV.length; i++) {
/* 203 */         String[] strings = ENV[i];
/* 204 */         this.env.put(strings[0], strings[1]);
/*     */       }
/*     */     Iterator<Aux> iterator;
/* 207 */     if (auxv != null) {
/* 208 */       for (iterator = auxv.values().iterator(); iterator.hasNext();) {
/* 209 */         Aux next = (Aux)iterator.next();
/* 210 */         auxv(next);
/*     */       }
/*     */     } else {
/* 213 */       auxv(32, -143104992);
/* 214 */       auxv(33, -143104992);
/* 215 */       auxv(16, 0);
/* 216 */       auxv(6, 4096);
/* 217 */       auxv(17, 100);
/* 218 */       auxv(8, 0);
/* 219 */       auxv(4, 32);
/* 220 */       auxv(11, 1000);
/* 221 */       auxv(12, 1000);
/* 222 */       auxv(13, 1000);
/* 223 */       auxv(14, 1000);
/* 224 */       auxv(23, 0);
/* 225 */       auxv(25, 32);
/* 226 */       auxv(15, "i386");
/*     */     }
/*     */   }
/*     */   
/*     */   public RuntimeArgs env(String name, String value) {
/* 231 */     this.env.put(name, value);
/* 232 */     return this;
/*     */   }
/*     */   
/*     */   public RuntimeArgs auxv(Aux aux) {
/* 236 */     if (aux.getText() != null) {
/* 237 */       aux = new Aux(aux.key, aux.text);
/*     */     }
/* 239 */     return add(aux);
/*     */   }
/*     */   
/* 242 */   public RuntimeArgs auxv(int key, int value) { return add(new Aux(key, value)); }
/*     */   
/*     */   public RuntimeArgs auxv(int key, String value) {
/* 245 */     return add(new Aux(key, value));
/*     */   }
/*     */   
/*     */   private RuntimeArgs add(Aux aux) {
/* 249 */     this.aux.put(Integer.valueOf(aux.getKey()), aux);
/* 250 */     return this;
/*     */   }
/*     */   
/*     */   public String[] getArgs() {
/* 254 */     return this.args;
/*     */   }
/*     */   
/*     */   public String[] getEnv() {
/* 258 */     String[] env = new String[this.env.size()];
/* 259 */     Set<Map.Entry<String, String>> entries = this.env.entrySet();
/* 260 */     int index = 0;
/* 261 */     for (Iterator<Map.Entry<String, String>> iterator = entries.iterator(); iterator.hasNext();) {
/* 262 */       Map.Entry<String, String> next = (Map.Entry)iterator.next();
/* 263 */       env[(index++)] = ((String)next.getKey() + "=" + (String)next.getValue());
/*     */     }
/* 265 */     return env;
/*     */   }
/*     */   
/*     */   public Aux[] getAuxv()
/*     */   {
/* 270 */     return (Aux[])this.aux.values().toArray(new Aux[this.aux.size()]);
/*     */   }
/*     */   
/*     */   public String getProgram() {
/* 274 */     return this.args[0];
/*     */   }
/*     */   
/*     */   public static class Aux
/*     */   {
/*     */     private final int key;
/*     */     private final String text;
/*     */     private int value;
/*     */     
/*     */     public Aux(int key, int value) {
/* 284 */       this.key = key;
/* 285 */       this.value = value;
/* 286 */       this.text = null;
/*     */     }
/*     */     
/* 289 */     public Aux(int key, String text) { this.key = key;
/* 290 */       this.value = 0;
/* 291 */       this.text = text;
/*     */     }
/*     */     
/*     */     public int hashCode() {
/* 295 */       return this.key;
/*     */     }
/*     */     
/*     */     public int getKey() {
/* 299 */       return this.key;
/*     */     }
/*     */     
/*     */     public int getValue() {
/* 303 */       return this.value;
/*     */     }
/*     */     
/*     */     public String getText() {
/* 307 */       return this.text;
/*     */     }
/*     */     
/*     */     public void setValue(int value) {
/* 311 */       if (this.text == null) throw new IllegalStateException("Only text keys have a mutable Aux int field");
/* 312 */       this.value = value;
/*     */     }
/*     */     
/*     */     public boolean equals(Object o) {
/* 316 */       return ((Aux)o).key == this.key;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Joey\Downloads\codejar.jar!\com\emt\proteus\runtime32\RuntimeArgs.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */