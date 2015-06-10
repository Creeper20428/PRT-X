/*    */ package com.emt.proteus.xserver.client;
/*    */ 
/*    */ import com.emt.proteus.xserver.display.Resource;
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
/*    */ public class Clients
/*    */   extends Resource
/*    */ {
/*    */   public Clients next;
/*    */   public int resource;
/*    */   
/*    */   public Clients(int id)
/*    */   {
/* 29 */     super(id, 536870920);
/* 30 */     this.id = id;
/* 31 */     this.resource = id;
/*    */   }
/*    */   
/* 34 */   public final boolean sameClient(XClient c) { XClient cc = getClient();
/* 35 */     if (cc == null) return false;
/* 36 */     return cc.index == c.index;
/*    */   }
/*    */   
/* 39 */   public final XClient getClient() { return XClient.X_CLIENTs[((this.resource & 0x1FC00000) >> 22)]; }
/*    */ }


/* Location:              C:\Users\Joey\Downloads\launch.jar!\com\emt\proteus\xserver\client\Clients.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */