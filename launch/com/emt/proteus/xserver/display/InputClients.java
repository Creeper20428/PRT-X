/*    */ package com.emt.proteus.xserver.display;
/*    */ 
/*    */ import com.emt.proteus.xserver.client.Clients;
/*    */ 
/*    */ class InputClients extends Clients
/*    */ {
/*    */   int[] mask;
/*    */   
/*    */   InputClients(int id)
/*    */   {
/* 11 */     super(id);
/* 12 */     this.mask = new int[9];
/*    */   }
/*    */ }


/* Location:              C:\Users\Joey\Downloads\launch.jar!\com\emt\proteus\xserver\display\InputClients.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */