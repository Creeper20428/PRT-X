/*    */ package com.emt.proteus.elf;
/*    */ 
/*    */ import java.util.Hashtable;
/*    */ import java.util.Vector;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class StringSection
/*    */ {
/*    */   private Hashtable index;
/*    */   private String[] stringList;
/*    */   private Integer[] indexList;
/*    */   private byte[] data;
/*    */   
/*    */   public StringSection()
/*    */   {
/* 18 */     this.index = new Hashtable();
/*    */   }
/*    */   
/*    */   public int nameCount()
/*    */   {
/* 23 */     return this.index.size();
/*    */   }
/*    */   
/*    */   public Integer getIndexAt(int row)
/*    */   {
/* 28 */     return this.indexList[row];
/*    */   }
/*    */   
/*    */   public String getStringAt(int row)
/*    */   {
/* 33 */     return this.stringList[row];
/*    */   }
/*    */   
/*    */   public String getNameAtIndex(int idx)
/*    */   {
/* 38 */     return str(idx);
/*    */   }
/*    */   
/*    */   public void read(byte[] raw)
/*    */   {
/* 43 */     this.data = raw;
/* 44 */     this.index.clear();
/*    */     
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/* 52 */     Vector buffer = new Vector();
/* 53 */     int startIndex = 1;
/* 54 */     for (int i = 1; i < raw.length; i++)
/*    */     {
/* 56 */       if (raw[i] == 0)
/*    */       {
/* 58 */         String val = new String(raw, startIndex, i - startIndex);
/* 59 */         Integer idx = Integer.valueOf(startIndex);
/* 60 */         this.index.put(idx, val);
/* 61 */         buffer.add(idx);
/* 62 */         buffer.add(val);
/*    */         
/* 64 */         startIndex = i + 1;
/*    */       }
/*    */     }
/*    */     
/* 68 */     this.stringList = new String[buffer.size() / 2];
/* 69 */     this.indexList = new Integer[this.stringList.length];
/* 70 */     int i = 0; for (int j = 0; i < this.stringList.length; i++)
/*    */     {
/* 72 */       this.indexList[i] = ((Integer)buffer.elementAt(j++));
/* 73 */       this.stringList[i] = ((String)buffer.elementAt(j++));
/*    */     }
/*    */   }
/*    */   
/*    */   public String str(int offset) {
/* 78 */     StringBuilder scratch = new StringBuilder();
/*    */     for (;;) {
/* 80 */       int c = this.data[(offset++)] & 0xFF;
/* 81 */       if (c == 0) return scratch.toString();
/* 82 */       scratch.append((char)c);
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\Joey\Downloads\codejar.jar!\com\emt\proteus\elf\StringSection.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */