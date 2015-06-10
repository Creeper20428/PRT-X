/*    */ package com.emt.proteus.xserver.display;
/*    */ 
/*    */ import com.emt.proteus.xserver.io.ComChannel;
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
/*    */ public final class Depth
/*    */ {
/*    */   public int depth;
/*    */   public Visual[] visual;
/*    */   
/* 27 */   public int getDepth() { return this.depth; }
/* 28 */   public void setDepth(int d) { this.depth = d; }
/* 29 */   public Visual[] getVisual() { return this.visual; }
/* 30 */   public void setVisual(Visual[] v) { this.visual = v; }
/* 31 */   public int visuals() { return this.visual.length; }
/*    */   
/* 33 */   public Depth(int depth, Visual[] visual) { this.depth = depth;
/* 34 */     this.visual = visual;
/* 35 */     for (int i = 0; i < visual.length; i++) {
/* 36 */       visual[i].setDepth(this);
/*    */     }
/*    */   }
/*    */   
/*    */   public void writeByte(ComChannel out) throws IOException {
/* 41 */     out.writeByte(this.depth);
/* 42 */     out.writePad(1);
/* 43 */     if (this.visual != null) out.writeShort(this.visual.length); else
/* 44 */       out.writeShort(0);
/* 45 */     out.writePad(4);
/* 46 */     if (this.visual != null) {
/* 47 */       for (int i = 0; i < this.visual.length; i++)
/* 48 */         this.visual[i].writeByte(out);
/*    */     }
/*    */   }
/*    */   
/*    */   public int getLength() {
/* 53 */     return 2 + (this.visual != null ? 6 * this.visual.length : 0);
/*    */   }
/*    */ }


/* Location:              C:\Users\Joey\Downloads\launch.jar!\com\emt\proteus\xserver\display\Depth.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */