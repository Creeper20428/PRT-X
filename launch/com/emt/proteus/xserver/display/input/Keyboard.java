/*     */ package com.emt.proteus.xserver.display.input;
/*     */ 
/*     */ import com.emt.proteus.xserver.client.XClient;
/*     */ import com.emt.proteus.xserver.io.ComChannel;
/*     */ import java.io.IOException;
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
/*     */ public final class Keyboard
/*     */ {
/*  28 */   public static Keyboard keyboard = null;
/*     */   public int minKeyCode;
/*     */   public int maxKeyCode;
/*     */   int keysyms_per_keycode;
/*     */   int[] keysym;
/*     */   static final int noSymbol = 0;
/*     */   
/*     */   public int keysym(int key, int n)
/*     */   {
/*  37 */     if (n < this.keysyms_per_keycode)
/*  38 */       return this.keysym[((key - this.minKeyCode) * this.keysyms_per_keycode + n)];
/*  39 */     return 0;
/*     */   }
/*     */   
/*     */   public int group1(int key, int n) {
/*  43 */     return keysym(key, n);
/*     */   }
/*     */   
/*     */   public int group2(int key, int n) {
/*  47 */     if (this.keysyms_per_keycode < 3)
/*  48 */       return keysym(key, n);
/*  49 */     return keysym(key, n + 2);
/*     */   }
/*     */   
/*     */   public Keyboard(int minKeyCode, int maxKeyCode) {
/*  53 */     this.minKeyCode = minKeyCode;
/*  54 */     this.maxKeyCode = maxKeyCode;
/*     */   }
/*     */   
/*     */   void writeByte(ComChannel out) throws IOException {
/*  58 */     out.writeByte(this.minKeyCode);
/*  59 */     out.writeByte(this.maxKeyCode);
/*     */   }
/*     */   
/*     */   public void getMap() {
/*  63 */     int length = this.maxKeyCode - this.minKeyCode + 1;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   void led(int number, int value) {}
/*     */   
/*     */ 
/*     */ 
/*     */   void bell(int volume) {}
/*     */   
/*     */ 
/*     */   public static void reqGrabKeyboard(XClient c)
/*     */     throws IOException
/*     */   {
/*  78 */     ComChannel comChannel = c.getChannel();
/*     */     
/*  80 */     int foo = c.data;
/*  81 */     foo = c.length;
/*  82 */     foo = comChannel.readInt();
/*  83 */     foo = comChannel.readInt();
/*  84 */     foo = comChannel.readByte();
/*  85 */     foo = comChannel.readByte();
/*  86 */     comChannel.readPad(2);
/*     */     
/*  88 */     synchronized (comChannel) {
/*  89 */       comChannel.writeByte(1);
/*  90 */       comChannel.writeByte(0);
/*  91 */       comChannel.writeShort(c.getSequence());
/*  92 */       comChannel.writeInt(0);
/*  93 */       comChannel.writePad(24);
/*  94 */       comChannel.flush();
/*     */     }
/*     */   }
/*     */   
/*     */   public static void reqGetKeyboardControl(XClient c) throws IOException {
/*  99 */     ComChannel comChannel = c.getChannel();
/* 100 */     int foo = c.length;
/*     */     
/* 102 */     synchronized (comChannel) {
/* 103 */       comChannel.writeByte(1);
/* 104 */       comChannel.writeByte((byte)1);
/* 105 */       comChannel.writeShort(c.getSequence());
/* 106 */       comChannel.writeInt(5);
/* 107 */       comChannel.writeInt(0);
/* 108 */       comChannel.writeByte((byte)0);
/* 109 */       comChannel.writeByte((byte)50);
/* 110 */       comChannel.writeShort(400);
/* 111 */       comChannel.writeShort(100);
/* 112 */       comChannel.writePad(2);
/* 113 */       for (int i = 0; i < 8; i++) {
/* 114 */         comChannel.writeInt(0);
/*     */       }
/* 116 */       comChannel.flush();
/*     */     }
/*     */   }
/*     */   
/*     */   public static void reqQueryKeymap(XClient c) throws IOException
/*     */   {
/* 122 */     ComChannel comChannel = c.getChannel();
/*     */     
/*     */ 
/* 125 */     synchronized (comChannel) {
/* 126 */       comChannel.writeByte(1);
/* 127 */       comChannel.writePad(1);
/* 128 */       comChannel.writeShort(c.getSequence());
/* 129 */       comChannel.writeInt(2);
/* 130 */       for (int i = 0; i < 8; i++) {
/* 131 */         comChannel.writeInt(0);
/*     */       }
/* 133 */       comChannel.flush();
/*     */     }
/*     */   }
/*     */   
/*     */   public static void reqChangeKeyboardMapping(XClient c) throws IOException {
/* 138 */     ComChannel comChannel = c.getChannel();
/* 139 */     int n = c.length;
/* 140 */     int first = comChannel.readByte();
/* 141 */     int kpk = comChannel.readByte();
/* 142 */     comChannel.readPad(2);
/* 143 */     n -= 2;
/* 144 */     int i = (first - Keymap.km.start) * Keymap.km.width;
/* 145 */     while (n != 0) {
/* 146 */       int foo = comChannel.readInt();
/* 147 */       if (kpk == 1) {
/* 148 */         Keymap.km.map[i] = foo;
/*     */       }
/* 150 */       n--;
/*     */     }
/*     */   }
/*     */   
/*     */   public static void reqGetKeyboardMapping(XClient c)
/*     */     throws IOException
/*     */   {
/* 157 */     ComChannel comChannel = c.getChannel();
/*     */     
/* 159 */     int i = comChannel.readByte();
/* 160 */     int foo = comChannel.readByte();
/* 161 */     comChannel.readPad(2);
/*     */     
/* 163 */     synchronized (comChannel) {
/* 164 */       comChannel.writeByte(1);
/* 165 */       comChannel.writeByte(Keymap.km.width);
/* 166 */       comChannel.writeShort(c.getSequence());
/* 167 */       comChannel.writeInt(foo * Keymap.km.width);
/* 168 */       comChannel.writePad(24);
/*     */       
/* 170 */       int ii = (i - Keymap.km.start) * Keymap.km.width;
/* 171 */       int n = foo * Keymap.km.width;
/* 172 */       while (n != 0) {
/* 173 */         comChannel.writeInt(Keymap.km.map[ii]);
/* 174 */         n--;
/* 175 */         ii++;
/*     */       }
/* 177 */       comChannel.flush();
/*     */     }
/*     */   }
/*     */   
/*     */   public static void reqSetModifierMapping(XClient c) throws IOException
/*     */   {
/* 183 */     ComChannel comChannel = c.getChannel();
/* 184 */     int kpm = c.data;
/* 185 */     int n = c.length;
/*     */     
/* 187 */     n -= 1;
/* 188 */     int i = 0;
/*     */     
/* 190 */     while (n != 0) {
/* 191 */       int foo = comChannel.readInt();
/* 192 */       n--;
/*     */     }
/*     */     
/* 195 */     synchronized (comChannel) {
/* 196 */       comChannel.writeByte(1);
/* 197 */       comChannel.writeByte(0);
/* 198 */       comChannel.writeShort(c.getSequence());
/* 199 */       comChannel.writeInt(0);
/* 200 */       comChannel.writePad(24);
/* 201 */       comChannel.flush();
/*     */     }
/*     */   }
/*     */   
/*     */   public static void reqGetModifierMapping(XClient c)
/*     */     throws IOException
/*     */   {
/* 208 */     ComChannel comChannel = c.getChannel();
/* 209 */     Keymodifier kmod = null;
/*     */     
/* 211 */     synchronized (comChannel) {
/* 212 */       comChannel.writeByte(1);
/* 213 */       comChannel.writeByte(Keymodifier.kmod.width);
/* 214 */       comChannel.writeShort(c.getSequence());
/* 215 */       comChannel.writeInt(Keymodifier.kmod.width * 2);
/* 216 */       comChannel.writePad(24);
/*     */       
/* 218 */       int n = Keymodifier.kmod.width * 8;
/*     */       
/* 220 */       int ii = 0;
/* 221 */       while (n != 0) {
/* 222 */         comChannel.writeByte(Keymodifier.kmod.keys[ii]);
/* 223 */         ii++;
/* 224 */         n--;
/*     */       }
/* 226 */       comChannel.flush();
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Joey\Downloads\launch.jar!\com\emt\proteus\xserver\display\input\Keyboard.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */