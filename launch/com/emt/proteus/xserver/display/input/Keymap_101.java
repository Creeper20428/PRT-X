/*     */ package com.emt.proteus.xserver.display.input;
/*     */ 
/*     */ import java.awt.event.KeyEvent;
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
/*     */ public final class Keymap_101
/*     */   extends Keymap
/*     */ {
/*  25 */   private int[] _map = { 65307, 0, 49, 33, 50, 64, 51, 35, 52, 36, 53, 37, 54, 94, 55, 38, 56, 42, 57, 40, 48, 41, 45, 95, 61, 43, 65288, 0, 65289, 65056, 113, 81, 119, 87, 101, 69, 114, 82, 116, 84, 121, 89, 117, 85, 105, 73, 111, 79, 112, 80, 91, 123, 93, 125, 65293, 0, 65507, 0, 97, 65, 115, 83, 100, 68, 102, 70, 103, 71, 104, 72, 106, 74, 107, 75, 108, 76, 59, 58, 39, 34, 96, 126, 65505, 0, 92, 124, 122, 90, 120, 88, 99, 67, 118, 86, 98, 66, 110, 78, 109, 77, 44, 60, 46, 62, 47, 63, 65506, 0, 65450, 0, 65513, 65511, 32, 0, 65509, 0, 65470, 0, 65471, 0, 65472, 0, 65473, 0, 65474, 0, 65475, 0, 65476, 0, 65477, 0, 65478, 0, 65479, 0, 65407, 65273, 65300, 0, 65429, 65463, 65431, 65464, 65434, 65465, 65453, 0, 65430, 65460, 65437, 65461, 65432, 65462, 65451, 0, 65436, 65457, 65433, 65458, 65435, 65459, 65438, 65456, 65439, 65454, 0, 0, 0, 0, 0, 0, 65480, 0, 65481, 0, 65360, 0, 65362, 0, 65365, 0, 65361, 0, 0, 0, 65363, 0, 65367, 0, 65364, 0, 65366, 0, 65379, 0, 65535, 0, 65421, 0, 65508, 0, 65299, 65387, 65377, 65378, 65455, 0, 65514, 65512, 0, 0, 0, 0, 0, 0, 0, 0 };
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
/*     */   public Keymap_101()
/*     */   {
/* 137 */     this.start = 9;
/* 138 */     this.width = 2;
/* 139 */     this.count = 109;
/* 140 */     this.map = this._map;
/*     */   }
/*     */   
/*     */   public final int getCode(KeyEvent e) {
/* 144 */     if (e.isShiftDown()) this.state = ((byte)(this.state | 0x1));
/* 145 */     if (e.isControlDown()) this.state = ((byte)(this.state | 0x4));
/* 146 */     if (e.isAltDown()) this.state = ((byte)(this.state | 0x8));
/* 147 */     int key = e.getKeyCode();
/* 148 */     if (key != 0) {
/* 149 */       switch (key) {
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
/* 176 */         key += 32;
/* 177 */         break;
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
/* 190 */         key = 65293; break;
/*     */       case 8: 
/* 192 */         key = 65288; break;
/*     */       case 9: 
/* 194 */         key = 65289; break;
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
/* 212 */         key = 96; break;
/*     */       case 222: 
/* 214 */         key = 39; break;
/*     */       case 16: 
/* 216 */         key = 65505; break;
/*     */       case 17: 
/* 218 */         key = 65507; break;
/*     */       case 18: 
/* 220 */         key = 65513; break;
/*     */       case 19: 
/* 222 */         key = 65299; break;
/*     */       case 20: 
/* 224 */         key = 65509; break;
/*     */       case 27: 
/* 226 */         key = 65307; break;
/*     */       case 33: 
/* 228 */         key = 65365; break;
/*     */       case 34: 
/* 230 */         key = 65366; break;
/*     */       case 35: 
/* 232 */         key = 65367; break;
/*     */       case 36: 
/* 234 */         key = 65360; break;
/*     */       case 37: 
/* 236 */         key = 65361; break;
/*     */       case 38: 
/* 238 */         key = 65362; break;
/*     */       case 39: 
/* 240 */         key = 65363; break;
/*     */       case 40: 
/* 242 */         key = 65364; break;
/*     */       case 96: 
/* 244 */         key = 65456; break;
/*     */       case 97: 
/* 246 */         key = 65457; break;
/*     */       case 98: 
/* 248 */         key = 65458; break;
/*     */       case 99: 
/* 250 */         key = 65459; break;
/*     */       case 100: 
/* 252 */         key = 65460; break;
/*     */       case 101: 
/* 254 */         key = 65461; break;
/*     */       case 102: 
/* 256 */         key = 65462; break;
/*     */       case 103: 
/* 258 */         key = 65463; break;
/*     */       case 104: 
/* 260 */         key = 65464; break;
/*     */       case 105: 
/* 262 */         key = 65465; break;
/*     */       case 106: 
/* 264 */         key = 65450; break;
/*     */       case 107: 
/* 266 */         key = 65451; break;
/*     */       case 108: 
/* 268 */         key = 65452; break;
/*     */       case 109: 
/* 270 */         if (e.getKeyChar() == '_') key = 95; else
/* 271 */           key = 65453;
/*     */         break;
/* 273 */       case 110:  key = 65454; break;
/*     */       case 111: 
/* 275 */         key = 65455; break;
/*     */       case 112: 
/* 277 */         key = 65470; break;
/*     */       case 113: 
/* 279 */         key = 65471; break;
/*     */       case 114: 
/* 281 */         key = 65472; break;
/*     */       case 115: 
/* 283 */         key = 65473; break;
/*     */       case 116: 
/* 285 */         key = 65474; break;
/*     */       case 117: 
/* 287 */         key = 65475; break;
/*     */       case 118: 
/* 289 */         key = 65476; break;
/*     */       case 119: 
/* 291 */         key = 65477; break;
/*     */       case 120: 
/* 293 */         key = 65478; break;
/*     */       case 121: 
/* 295 */         key = 65479; break;
/*     */       case 122: 
/* 297 */         key = 65480; break;
/*     */       case 123: 
/* 299 */         key = 65481; break;
/*     */       case 127: 
/* 301 */         key = 65535; break;
/*     */       case 144: 
/* 303 */         key = 65407; break;
/*     */       case 145: 
/* 305 */         key = 65300; break;
/*     */       case 154: 
/* 307 */         key = 65377; break;
/*     */       case 155: 
/* 309 */         key = 65379; break;
/*     */       case 156: 
/* 311 */         key = 65386; break;
/*     */       case 157: 
/* 313 */         key = 65511; break;
/*     */       case 11: case 12: case 13: 
/*     */       case 14: case 15: case 21: 
/*     */       case 22: case 23: case 24: 
/*     */       case 25: case 26: 
/*     */       case 28: case 29: 
/*     */       case 30: case 31: 
/*     */       case 41: case 42: 
/*     */       case 43: case 45: 
/*     */       case 58: case 60: 
/*     */       case 62: case 63: 
/*     */       case 64: case 94: 
/*     */       case 95: case 124: 
/*     */       case 125: case 126: 
/*     */       case 128: case 129: 
/*     */       case 130: case 131: 
/*     */       case 132: case 133: 
/*     */       case 134: case 135: 
/*     */       case 136: case 137: 
/*     */       case 138: case 139: 
/*     */       case 140: case 141: 
/*     */       case 142: case 143: 
/*     */       case 146: case 147: 
/*     */       case 148: case 149: 
/*     */       case 150: case 151: 
/*     */       case 152: case 153: 
/*     */       case 158: case 159: 
/*     */       case 160: case 161: 
/*     */       case 162: case 163: 
/*     */       case 164: case 165: 
/*     */       case 166: case 167: 
/*     */       case 168: case 169: 
/*     */       case 170: case 171: 
/*     */       case 172: case 173: 
/*     */       case 174: case 175: 
/*     */       case 176: case 177: 
/*     */       case 178: case 179: 
/*     */       case 180: case 181: 
/*     */       case 182: case 183: 
/*     */       case 184: case 185: 
/*     */       case 186: case 187: 
/*     */       case 188: case 189: 
/*     */       case 190: case 191: 
/*     */       case 193: case 194: 
/*     */       case 195: case 196: 
/*     */       case 197: case 198: 
/*     */       case 199: case 200: 
/*     */       case 201: case 202: 
/*     */       case 203: case 204: 
/*     */       case 205: case 206: 
/*     */       case 207: case 208: 
/*     */       case 209: case 210: 
/*     */       case 211: case 212: 
/*     */       case 213: case 214: 
/*     */       case 215: case 216: 
/*     */       case 217: case 218: 
/*     */       case 219: case 220: 
/*     */       case 221: default: 
/* 371 */         key = e.getKeyChar();break;
/*     */       }
/*     */       
/*     */     } else {
/* 375 */       key = e.getKeyChar();
/*     */     }
/* 377 */     int s = 10;
/* 378 */     if (km != null) {
/* 379 */       int i = 0;
/* 380 */       int j = 0;
/* 381 */       s = km.start;
/* 382 */       while ((i < km.count * km.width) && 
/* 383 */         (km.map[i] != key)) {
/* 384 */         i++;
/* 385 */         j++;
/* 386 */         if (j == km.width) {
/* 387 */           j = 0;
/* 388 */           s++;
/*     */         }
/*     */       }
/*     */     }
/* 392 */     return s;
/*     */   }
/*     */ }


/* Location:              C:\Users\Joey\Downloads\launch.jar!\com\emt\proteus\xserver\display\input\Keymap_101.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */