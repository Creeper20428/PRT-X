/*    */ package com.emt.proteus.xserver.client;
/*    */ 
/*    */ import com.emt.proteus.xserver.display.WindowOpt;
/*    */ import com.emt.proteus.xserver.display.XWindow;
/*    */ import java.io.IOException;
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
/*    */ public final class OtherClients
/*    */   extends Clients
/*    */ {
/*    */   public int mask;
/*    */   public XWindow XWindow;
/*    */   
/* 30 */   public OtherClients(int id) { super(id); }
/*    */   
/*    */   public void delete() throws IOException {
/* 33 */     if (this.XWindow == null) return;
/* 34 */     XClient c = getClient();
/* 35 */     OtherClients prev = null;
/* 36 */     for (OtherClients other = this.XWindow.optional == null ? null : this.XWindow.optional.otherClients; 
/* 37 */         other != null; other = (OtherClients)other.next) {
/* 38 */       if (other.getClient() == c) {
/* 39 */         if (prev != null) prev.next = other.next; else
/* 40 */           this.XWindow.optional.otherClients = ((OtherClients)other.next);
/* 41 */         this.XWindow.recalculateDeliverableEvents();
/*    */       }
/* 43 */       prev = other;
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\Joey\Downloads\launch.jar!\com\emt\proteus\xserver\client\OtherClients.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */