/*    */ package com.emt.proteus.xserver.display;
/*    */ 
/*    */ import java.awt.Toolkit;
/*    */ import java.awt.datatransfer.Clipboard;
/*    */ import java.awt.datatransfer.ClipboardOwner;
/*    */ import java.awt.datatransfer.DataFlavor;
/*    */ import java.awt.datatransfer.StringSelection;
/*    */ import java.awt.datatransfer.Transferable;
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
/*    */ public final class CopyPaste
/*    */   implements ClipboardOwner
/*    */ {
/* 27 */   private static Clipboard clipboard = null;
/* 28 */   private static CopyPaste copypaste = null;
/* 29 */   private static boolean isOwner = true;
/*    */   
/* 31 */   public static void init() { if (clipboard == null) {
/* 32 */       try { clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
/*    */       }
/*    */       catch (Exception e) {
/* 35 */         clipboard = null;
/*    */       }
/*    */     }
/* 38 */     if ((clipboard != null) && (copypaste == null)) {
/* 39 */       copypaste = new CopyPaste();
/* 40 */       setString(" ");
/*    */     }
/*    */   }
/*    */   
/*    */   public static void setString(String str) {
/* 45 */     if (clipboard == null) return;
/* 46 */     StringSelection contents = new StringSelection(str);
/* 47 */     isOwner = true;
/* 48 */     clipboard.setContents(contents, copypaste);
/*    */   }
/*    */   
/*    */   public static String getString() {
/* 52 */     if (clipboard == null) return null;
/* 53 */     Transferable content = clipboard.getContents(copypaste);
/* 54 */     if (content != null) {
/* 55 */       try { return (String)content.getTransferData(DataFlavor.stringFlavor);
/*    */       }
/*    */       catch (Exception e) {}
/*    */     }
/*    */     
/* 60 */     return null;
/*    */   }
/*    */   
/*    */   public void lostOwnership(Clipboard clipboard, Transferable contents) {
/* 64 */     isOwner = false;
/*    */   }
/*    */   
/* 67 */   public static boolean isOwner() { return isOwner; }
/*    */ }


/* Location:              C:\Users\Joey\Downloads\launch.jar!\com\emt\proteus\xserver\display\CopyPaste.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */