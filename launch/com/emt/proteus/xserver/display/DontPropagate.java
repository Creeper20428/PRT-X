/*    */ package com.emt.proteus.xserver.display;
/*    */ 
/*    */ 
/*    */ 
/*    */ class DontPropagate
/*    */ {
/*  7 */   public static int[] masks = new int[8];
/*  8 */   public static int[] refc = new int[8];
/*    */   
/* 10 */   static { for (int i = 0; i < 8; i++) {
/* 11 */       masks[i] = (refc[i] = 0);
/*    */     }
/*    */   }
/*    */   
/*    */   static int store(int mask) {
/* 16 */     int i = 0;
/* 17 */     int free = 0;
/* 18 */     i = 8;free = 0; for (;;) { i--; if (i > 0)
/* 19 */         if (refc[i] == 0) { free = i;
/* 20 */         } else if (mask == masks[i])
/*    */           break;
/*    */     }
/* 23 */     if ((i == 0) && (free != 0)) {
/* 24 */       i = free;
/* 25 */       masks[i] = mask;
/*    */     }
/* 27 */     return i;
/*    */   }
/*    */ }


/* Location:              C:\Users\Joey\Downloads\launch.jar!\com\emt\proteus\xserver\display\DontPropagate.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */