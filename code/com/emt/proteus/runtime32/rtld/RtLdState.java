/*    */ package com.emt.proteus.runtime32.rtld;
/*    */ 
/*    */ import com.emt.proteus.utils.CStruct;
/*    */ import com.emt.proteus.utils.CStruct.CField;
/*    */ import com.emt.proteus.utils.Data;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class RtLdState
/*    */   extends CStruct
/*    */ {
/*    */   public static final int RTLD_OFFSET = 0;
/*    */   public static final int RTLD_RO_OFFSET = 4096;
/*    */   public static final int LINK_MAP_OFFSET = 5120;
/*    */   private Rtld rtld;
/*    */   private RtldRo rtldRo;
/*    */   private LinkerMap map;
/*    */   
/*    */   public RtLdState()
/*    */   {
/* 22 */     super(5120 + LinkerMap.sizeof(256));
/* 23 */     this.rtld = new Rtld();
/* 24 */     this.rtldRo = new RtldRo();
/* 25 */     this.map = new LinkerMap(256);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */   public int getRtldOffset()
/*    */   {
/* 32 */     return 0;
/*    */   }
/*    */   
/* 35 */   public int getRtldRoOffset() { return 4096; }
/*    */   
/*    */   public int getSize()
/*    */   {
/* 39 */     return 5120 + this.map.getSize();
/*    */   }
/*    */   
/*    */   public Rtld getRtld() {
/* 43 */     return this.rtld;
/*    */   }
/*    */   
/*    */   public RtldRo getRtldRo() {
/* 47 */     return this.rtldRo;
/*    */   }
/*    */   
/*    */   public LinkerMap getMap() {
/* 51 */     return this.map;
/*    */   }
/*    */   
/*    */   public void assign(int address, Data memory) {
/* 55 */     this.rtld.assign(address + 0, memory);
/* 56 */     this.rtldRo.assign(address + 4096, memory);
/* 57 */     this.map.assign(address + 5120, memory);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public void syncMap()
/*    */   {
/* 69 */     Rtld.LinkNameSpace nameSpace = this.rtld.getNameSpace(0);
/* 70 */     CLinkEntry entry = new CLinkEntry();
/* 71 */     entry.assign(this.map.getRoot());
/* 72 */     Rtld.LinkNameSpace.NS_LOADED.set(nameSpace, entry.addressOf());
/* 73 */     int count = 0;
/* 74 */     while (entry.addressOf() != 0) {
/* 75 */       count++;
/* 76 */       entry.assign(entry.getNext());
/*    */     }
/* 78 */     Rtld.LinkNameSpace.NS_NLOADED.set(nameSpace, count);
/* 79 */     Rtld.DL_LOAD_ADDS.set(this.rtld, 2L);
/*    */   }
/*    */   
/*    */   public LinkerMap.Entry newEntry(LinkerMap.Entry appendTo, Module dep) {
/* 83 */     LinkerMap.Entry entry = getMap().entry(appendTo, dep);
/* 84 */     Rtld.LinkNameSpace nameSpace = this.rtld.getNameSpace(0);
/* 85 */     int count = Rtld.LinkNameSpace.NS_NLOADED.intValue(nameSpace);
/* 86 */     Rtld.LinkNameSpace.NS_NLOADED.set(nameSpace, ++count);
/* 87 */     long l = Rtld.DL_LOAD_ADDS.get(this.rtld);
/* 88 */     Rtld.DL_LOAD_ADDS.set(this.rtld, 1L + l);
/* 89 */     return entry;
/*    */   }
/*    */ }


/* Location:              C:\Users\Joey\Downloads\codejar.jar!\com\emt\proteus\runtime32\rtld\RtLdState.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */