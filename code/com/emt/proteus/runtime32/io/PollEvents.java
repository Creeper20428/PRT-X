/*    */ package com.emt.proteus.runtime32.io;
/*    */ 
/*    */ import com.emt.proteus.utils.Utils;
/*    */ import java.util.Map;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class PollEvents
/*    */ {
/*    */   public static final int POLLIN = 1;
/*    */   public static final int POLLPRI = 2;
/*    */   public static final int POLLOUT = 4;
/*    */   public static final int POLLRDNORM = 64;
/*    */   public static final int POLLRDBAND = 128;
/*    */   public static final int POLLWRNORM = 256;
/*    */   public static final int POLLWRBAND = 512;
/*    */   public static final int POLLMSG = 1024;
/*    */   public static final int POLLREMOVE = 4096;
/*    */   public static final int POLLRDHUP = 8192;
/*    */   public static final int POLLERR = 8;
/*    */   public static final int POLLHUP = 16;
/*    */   public static final int POLLNVAL = 32;
/* 49 */   private static Map<Integer, String> _constants = Utils.createConstantMap(PollEvents.class);
/*    */   
/*    */   public static String toString(int value) {
/* 52 */     return Utils.toOrBitString(_constants, value);
/*    */   }
/*    */ }


/* Location:              C:\Users\Joey\Downloads\codejar.jar!\com\emt\proteus\runtime32\io\PollEvents.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */