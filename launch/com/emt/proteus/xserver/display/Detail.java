/*    */ package com.emt.proteus.xserver.display;
/*    */ 
/*    */ 
/*    */ class Detail
/*    */ {
/*    */   int exact;
/*    */   
/*  8 */   byte[] pMask = null;
/*    */   
/*    */   boolean isInGrabMask(Detail secondDetail, int exception) {
/* 11 */     if (this.exact == exception) {
/* 12 */       if (this.pMask == null) return true;
/* 13 */       if (secondDetail.exact == exception) return false;
/* 14 */       if ((this.pMask[(secondDetail.exact >> 5)] & secondDetail.exact << 31) != 0)
/* 15 */         return true;
/*    */     }
/* 17 */     return false;
/*    */   }
/*    */   
/*    */ 
/*    */   static boolean identicalExactDetails(int firstExact, int secondExact, int exception)
/*    */   {
/* 23 */     if ((firstExact == exception) || (secondExact == exception)) return false;
/* 24 */     if (firstExact == secondExact) return true;
/* 25 */     return false;
/*    */   }
/*    */   
/* 28 */   boolean detailSupersedesSecond(Detail secondDetail, int exception) { if (isInGrabMask(secondDetail, exception)) return true;
/* 29 */     if (identicalExactDetails(this.exact, secondDetail.exact, exception))
/* 30 */       return true;
/* 31 */     return false;
/*    */   }
/*    */ }


/* Location:              C:\Users\Joey\Downloads\launch.jar!\com\emt\proteus\xserver\display\Detail.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */