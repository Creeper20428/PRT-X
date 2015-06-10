/*     */ package com.emt.proteus.xserver.display.input;
/*     */ 
/*     */ import java.awt.event.KeyEvent;
/*     */ import java.io.Serializable;
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
/*     */ public class Keymap
/*     */   implements Serializable
/*     */ {
/*  26 */   public static Keymap km = null;
/*  27 */   protected byte state = 0;
/*     */   protected int start;
/*     */   protected int width;
/*     */   protected int count;
/*     */   protected int[] map;
/*     */   
/*     */   public Keymap() {}
/*     */   
/*     */   public Keymap(int start, int width, int count) {
/*  36 */     this.start = start;
/*  37 */     this.width = width;
/*  38 */     this.count = count;
/*  39 */     this.map = new int[count * width];
/*     */   }
/*     */   
/*     */   public int getCode(KeyEvent e) {
/*  43 */     if (e.isShiftDown()) this.state = ((byte)(this.state | 0x1));
/*  44 */     if (e.isControlDown()) this.state = ((byte)(this.state | 0x4));
/*  45 */     if (e.isAltDown()) this.state = ((byte)(this.state | 0x8));
/*  46 */     int key = e.getKeyCode();
/*  47 */     if (key != 0) {
/*  48 */       switch (key) {
/*     */       case 65: 
/*     */       case 66: 
/*     */       case 67: 
/*     */       case 68: 
/*     */       case 69: 
/*     */       case 70: 
/*     */       case 71: 
/*     */       case 72: 
/*     */       case 73: 
/*     */       case 74: 
/*     */       case 75: 
/*     */       case 76: 
/*     */       case 77: 
/*     */       case 78: 
/*     */       case 79: 
/*     */       case 80: 
/*     */       case 81: 
/*     */       case 82: 
/*     */       case 83: 
/*     */       case 84: 
/*     */       case 85: 
/*     */       case 86: 
/*     */       case 87: 
/*     */       case 88: 
/*     */       case 89: 
/*     */       case 90: 
/*  75 */         key += 32;
/*  76 */         break;
/*     */       case 48: 
/*     */       case 49: 
/*     */       case 50: 
/*     */       case 51: 
/*     */       case 52: 
/*     */       case 53: 
/*     */       case 54: 
/*     */       case 55: 
/*     */       case 56: 
/*     */       case 57: 
/*     */         break;
/*     */       case 10: 
/*  89 */         key = 65293; break;
/*     */       case 8: 
/*  91 */         key = 65288; break;
/*     */       case 9: 
/*  93 */         key = 65289; break;
/*     */       
/*     */ 
/*     */ 
/*     */       case 32: 
/*     */       case 44: 
/*     */       case 46: 
/*     */       case 47: 
/*     */       case 59: 
/*     */       case 61: 
/*     */       case 91: 
/*     */       case 92: 
/*     */       case 93: 
/*     */         break;
/*     */       
/*     */ 
/*     */ 
/*     */       case 192: 
/* 111 */         key = 96; break;
/*     */       case 222: 
/* 113 */         key = 39; break;
/*     */       case 16: 
/* 115 */         key = 65505; break;
/*     */       case 17: 
/* 117 */         key = 65507; break;
/*     */       case 18: 
/* 119 */         key = 65513; break;
/*     */       case 19: 
/* 121 */         key = 65299; break;
/*     */       case 20: 
/* 123 */         key = 65509; break;
/*     */       case 27: 
/* 125 */         key = 65307; break;
/*     */       case 33: 
/* 127 */         key = 65365; break;
/*     */       case 34: 
/* 129 */         key = 65366; break;
/*     */       case 35: 
/* 131 */         key = 65367; break;
/*     */       case 36: 
/* 133 */         key = 65360; break;
/*     */       case 37: 
/* 135 */         key = 65361; break;
/*     */       case 38: 
/* 137 */         key = 65362; break;
/*     */       case 39: 
/* 139 */         key = 65363; break;
/*     */       case 40: 
/* 141 */         key = 65364; break;
/*     */       case 96: 
/* 143 */         key = 65456; break;
/*     */       case 97: 
/* 145 */         key = 65457; break;
/*     */       case 98: 
/* 147 */         key = 65458; break;
/*     */       case 99: 
/* 149 */         key = 65459; break;
/*     */       case 100: 
/* 151 */         key = 65460; break;
/*     */       case 101: 
/* 153 */         key = 65461; break;
/*     */       case 102: 
/* 155 */         key = 65462; break;
/*     */       case 103: 
/* 157 */         key = 65463; break;
/*     */       case 104: 
/* 159 */         key = 65464; break;
/*     */       case 105: 
/* 161 */         key = 65465; break;
/*     */       case 106: 
/* 163 */         key = 65450; break;
/*     */       case 107: 
/* 165 */         key = 65451; break;
/*     */       case 108: 
/* 167 */         key = 65452; break;
/*     */       case 109: 
/* 169 */         key = 65453; break;
/*     */       case 110: 
/* 171 */         key = 65454; break;
/*     */       case 111: 
/* 173 */         key = 65455; break;
/*     */       case 112: 
/* 175 */         key = 65470; break;
/*     */       case 113: 
/* 177 */         key = 65471; break;
/*     */       case 114: 
/* 179 */         key = 65472; break;
/*     */       case 115: 
/* 181 */         key = 65473; break;
/*     */       case 116: 
/* 183 */         key = 65474; break;
/*     */       case 117: 
/* 185 */         key = 65475; break;
/*     */       case 118: 
/* 187 */         key = 65476; break;
/*     */       case 119: 
/* 189 */         key = 65477; break;
/*     */       case 120: 
/* 191 */         key = 65478; break;
/*     */       case 121: 
/* 193 */         key = 65479; break;
/*     */       case 122: 
/* 195 */         key = 65480; break;
/*     */       case 123: 
/* 197 */         key = 65481; break;
/*     */       case 127: 
/* 199 */         key = 65535; break;
/*     */       case 144: 
/* 201 */         key = 65407; break;
/*     */       case 145: 
/* 203 */         key = 65300; break;
/*     */       case 154: 
/* 205 */         key = 65377; break;
/*     */       case 155: 
/* 207 */         key = 65379; break;
/*     */       case 156: 
/* 209 */         key = 65386; break;
/*     */       case 157: 
/* 211 */         key = 65511; break;
/*     */       case 11: case 12: case 13: case 14: case 15: case 21: case 22: case 23: case 24: case 25: case 26: case 28: case 29: 
/*     */       case 30: case 31: case 41: case 42: case 43: case 45: case 58: case 60: case 62: case 63: case 64: case 94: case 95: 
/*     */       case 124: case 125: case 126: case 128: case 129: case 130: case 131: case 132: case 133: case 134: case 135: case 136: case 137: 
/*     */       case 138: case 139: case 140: case 141: case 142: case 143: case 146: case 147: case 148: case 149: case 150: case 151: case 152: 
/*     */       case 153: case 158: case 159: case 160: case 161: case 162: case 163: case 164: case 165: case 166: case 167: case 168: case 169: 
/*     */       case 170: case 171: case 172: case 173: case 174: case 175: case 176: case 177: case 178: case 179: case 180: case 181: case 182: 
/*     */       case 183: case 184: case 185: case 186: case 187: case 188: case 189: case 190: case 191: case 193: case 194: case 195: case 196: 
/*     */       case 197: case 198: case 199: case 200: case 201: case 202: case 203: case 204: case 205: case 206: case 207: case 208: case 209: 
/*     */       case 210: case 211: case 212: case 213: case 214: case 215: case 216: case 217: case 218: case 219: case 220: case 221: default: 
/* 221 */         key = e.getKeyChar();break;
/*     */       }
/*     */       
/*     */     } else {
/* 225 */       key = e.getKeyChar();
/*     */     }
/*     */     
/* 228 */     int s = 10;
/* 229 */     if (km != null) {
/* 230 */       int i = 0;
/* 231 */       int j = 0;
/* 232 */       s = km.start;
/* 233 */       while ((i < km.count * km.width) && 
/* 234 */         (km.map[i] != key)) {
/* 235 */         i++;
/* 236 */         j++;
/* 237 */         if (j == km.width) {
/* 238 */           j = 0;
/* 239 */           s++;
/*     */         }
/*     */       }
/*     */     }
/* 243 */     return s;
/*     */   }
/*     */   
/*     */   public byte getState() {
/* 247 */     return this.state;
/*     */   }
/*     */   
/*     */   public int getStart() {
/* 251 */     return this.start;
/*     */   }
/*     */   
/*     */   public int getLast() {
/* 255 */     return this.count + this.start - 1;
/*     */   }
/*     */   
/*     */   public int getWidth() {
/* 259 */     return this.width;
/*     */   }
/*     */   
/*     */   public int getCount() {
/* 263 */     return this.count;
/*     */   }
/*     */   
/*     */   public int[] getMap() {
/* 267 */     return this.map;
/*     */   }
/*     */ }


/* Location:              C:\Users\Joey\Downloads\launch.jar!\com\emt\proteus\xserver\display\input\Keymap.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */