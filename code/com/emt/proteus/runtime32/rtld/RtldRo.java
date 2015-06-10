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
/*     */ public final class RtldRo
/*     */   extends CStruct
/*     */ {
/*     */   public static final int SIZEOF = 1024;
/* 174 */   public static final CStruct.CField _dl_debug_mask = _integer(0, "_dl_debug_mask");
/* 175 */   public static final CStruct.CField _dl_osversion = _integer(_dl_debug_mask, "_dl_osversion");
/* 176 */   public static final CStruct.CField _dl_platform = _pointer(_dl_osversion, "_dl_platform");
/* 177 */   public static final CStruct.CField _dl_platform_len = _integer(_dl_platform, "_dl_platform_len");
/* 178 */   public static final CStruct.CField _dl_pagesize = _integer(_dl_platform_len, "_dl_pagesize");
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
/* 199 */   public static final CStruct.CField r_search_path_elem = _integer(376, "r_search_path_elem");
/* 200 */   public static final CStruct.CField _dl_trace_prelink_map = _pointer(380, "_dl_trace_prelink_map");
/* 201 */   public static final CStruct.CField _dl_init_all_dirs = _pointer(384, "_dl_init_all_dirs");
/* 202 */   public static final CStruct.CField hp_timing_t = _long(384, "hp_timing_t");
/* 203 */   public static final CStruct.CField _dl_sysinfo = _integer(392, "_dl_sysinfo");
/* 204 */   public static final CStruct.CField _dl_sysinfo_dso = _integer(396, "_dl_sysinfo_dso");
/* 205 */   public static final CStruct.CField _dl_sysinfo_map = _pointer(_dl_sysinfo_dso, "_dl_sysinfo_map");
/* 206 */   public static final CStruct.CField _dl_debug_printf = _pointer(_dl_sysinfo_map, "_dl_debug_printf");
/* 207 */   public static final CStruct.CField _dl_catch_error = _pointer(_dl_debug_printf, "_dl_catch_error");
/* 208 */   public static final CStruct.CField _dl_signal_error = _pointer(_dl_catch_error, "_dl_signal_error");
/* 209 */   public static final CStruct.CField _dl_mcount = _pointer(_dl_signal_error, "_dl_mcount");
/* 210 */   public static final CStruct.CField _dl_lookup_symbol_x = _pointer(_dl_mcount, "_dl_lookup_symbol_x");
/* 211 */   public static final CStruct.CField _dl_check_caller = _pointer(_dl_lookup_symbol_x, "_dl_check_caller");
/* 212 */   public static final CStruct.CField _dl_open = _pointer(_dl_check_caller, "_dl_open");
/* 213 */   public static final CStruct.CField _dl_close = _pointer(_dl_open, "_dl_close");
/* 214 */   public static final CStruct.CField _dl_tls_get_addr_soft = _pointer(436, "_dl_tls_get_addr_soft");
/* 215 */   public static final CStruct.CField _dl_discover_osversion = _pointer(440, "_dl_discover_osversion");
/* 216 */   public static final CStruct.CField _dl_audit = _pointer(_dl_discover_osversion, "_dl_audit");
/* 217 */   public static final CStruct.CField _dl_naudit = _integer(_dl_audit, "_dl_naudit");
/* 218 */   public static final CStruct.CField _dl_pointer_guard = _integer(_dl_naudit, "_dl_pointer_guard");
/*     */   
/*     */   public RtldRo() {
/* 221 */     super(1024);
/* 222 */     setAlign(1024);
/*     */   }
/*     */   
/*     */   public void assign(int address, Data data)
/*     */   {
/* 227 */     super.assign(address, data);
/*     */   }
/*     */ }


/* Location:              C:\Users\Joey\Downloads\codejar.jar!\com\emt\proteus\runtime32\rtld\RtldRo.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */