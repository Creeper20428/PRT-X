/*     */ package com.emt.proteus.runtime32.rtld;
/*     */ 
/*     */ import com.emt.proteus.utils.CStruct;
/*     */ import com.emt.proteus.utils.CStruct.CField;
/*     */ import com.emt.proteus.utils.Data;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class Rtld
/*     */   extends CStruct
/*     */ {
/*     */   public static final int SIZEOF = 4096;
/*     */   public static final int NMAX_NAMESPACE = 16;
/*     */   
/*     */   public static class LinkNameSpace
/*     */     extends CStruct
/*     */   {
/* 165 */     public static final CStruct.CField NS_LOADED = _pointer(0, "_ns_loaded");
/* 166 */     public static final CStruct.CField NS_NLOADED = _integer(NS_LOADED, "_ns_nloaded");
/* 167 */     public static final CStruct.CField MAIN_SRC_LIST = _pointer(NS_NLOADED, "_ns_main_searchlist");
/* 168 */     public static final CStruct.CField GLOBAL_SCOPE_ALLOC = _integer(MAIN_SRC_LIST, "_ns_global_scope_alloc");
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 187 */     public static final CStruct.CField NS_UNIQUE_SYM_TABLE = _blob(GLOBAL_SCOPE_ALLOC, " _ns_unique_sym_table", 20);
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 211 */     public static final CStruct.CField NS_DEBUG = _blob(NS_UNIQUE_SYM_TABLE, " _ns_debug", 20);
/* 212 */     public static final CStruct.CField LAST = _blob(NS_DEBUG, " _ns_debug", 20);
/*     */     
/* 214 */     public static final int SIZEOF = LAST.nextOffset();
/*     */     
/*     */     private LinkNameSpace() {
/* 217 */       super();
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 225 */   public static final CStruct.CField DL_NS = structArray(0, "_ns_loaded", LinkNameSpace.SIZEOF, 16);
/* 226 */   public static final CStruct.CField DL_NNS = _integer(DL_NS, "_dl_nns");
/* 227 */   public static final CStruct.CField LD_LOAD_LOCK = _blob(DL_NNS, "ld_load_lock", 24);
/* 228 */   public static final CStruct.CField LD_LOAD_WRITE_LOCK = _blob(LD_LOAD_LOCK, "ld_load_write_lock", 24);
/* 229 */   public static final CStruct.CField DL_LOAD_ADDS = _long(LD_LOAD_WRITE_LOCK, "_dl_load_adds");
/* 230 */   public static final CStruct.CField _dl_initfirst = _integer(1276, "_dl_initfirst");
/* 231 */   public static final CStruct.CField _dl_cpuclock_offset = _long(1280, "_dl_cpuclock_offset");
/* 232 */   public static final CStruct.CField _dl_profile_map = _pointer(1288, "_dl_profile_map");
/* 233 */   public static final CStruct.CField _dl_num_relocations = _integer(1292, "_dl_num_relocations");
/* 234 */   public static final CStruct.CField _dl_num_cache_relocations = _integer(1296, "_dl_num_cache_relocations");
/* 235 */   public static final CStruct.CField r_search_path_elem = _pointer(1300, "r_search_path_elem");
/* 236 */   public static final CStruct.CField _dl_error_catch_tsd = _pointer(1304, "_dl_error_catch_tsd");
/*     */   
/*     */ 
/* 239 */   public static final CStruct.CField _dl_rtld_lock_recursive = _pointer(2036, "_dl_rtld_lock_recursive");
/* 240 */   public static final CStruct.CField _dl_rtld_unlock_recursive = _pointer(2040, "_dl_rtld_unlock_recursive");
/* 241 */   public static final CStruct.CField _dl_make_stack_executable_hook = _pointer(2044, "_dl_make_stack_executable_hook");
/*     */   
/* 243 */   private LinkNameSpace[] nameSpaces = new LinkNameSpace[16];
/*     */   
/*     */   public Rtld() {
/* 246 */     super(4096);
/* 247 */     for (int i = 0; i < this.nameSpaces.length; i++) {
/* 248 */       this.nameSpaces[i] = new LinkNameSpace(null);
/*     */     }
/* 250 */     setAlign(1024);
/*     */   }
/*     */   
/*     */   public LinkNameSpace getNameSpace(int index) {
/* 254 */     return this.nameSpaces[index];
/*     */   }
/*     */   
/*     */   public void assign(int address, Data data)
/*     */   {
/* 259 */     super.assign(address, data);
/* 260 */     for (int i = 0; i < this.nameSpaces.length; i++) {
/* 261 */       this.nameSpaces[i].assign(address + i * LinkNameSpace.SIZEOF, data);
/*     */     }
/*     */     
/* 264 */     DL_NNS.set(this, 1L);
/*     */   }
/*     */ }


/* Location:              C:\Users\Joey\Downloads\codejar.jar!\com\emt\proteus\runtime32\rtld\Rtld.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */