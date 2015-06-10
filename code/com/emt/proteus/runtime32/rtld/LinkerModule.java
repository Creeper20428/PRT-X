/*     */ package com.emt.proteus.runtime32.rtld;
/*     */ 
/*     */ import com.emt.proteus.runtime32.CallArgs;
/*     */ import com.emt.proteus.runtime32.ExecutionEnvironment;
/*     */ import com.emt.proteus.runtime32.LongJumpException;
/*     */ import com.emt.proteus.runtime32.Option;
/*     */ import com.emt.proteus.runtime32.Option.Switch;
/*     */ import com.emt.proteus.runtime32.ThreadContext;
/*     */ import com.emt.proteus.runtime32.memory.MainMemory;
/*     */ import com.emt.proteus.utils.CStruct;
/*     */ import com.emt.proteus.utils.CStruct.CField;
/*     */ import com.emt.proteus.utils.Data;
/*     */ import com.emt.proteus.utils.ILog;
/*     */ import java.io.IOException;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class LinkerModule
/*     */   extends JavaModule
/*     */ {
/*  72 */   private static final boolean LTRACE = Option.ltrace.value();
/*     */   
/*     */   private static final int SIZE = 4096;
/*     */   
/*     */   public static final int LINK_MAP_ENTRIES = 262144;
/*     */   
/*     */   private final Symbols.Symbol rtldState;
/*     */   
/*     */   private final Symbols.Symbol global_tsd;
/*     */   
/*     */   public final Symbols.Symbol pltInteceptor;
/*     */   
/*     */   public final Symbols.Symbol __libc_enable_secure;
/*     */   
/*     */   public final Symbols.Symbol _rtld_global_ro;
/*     */   public final Symbols.Symbol _rtld_global;
/*     */   public final Symbols.Symbol _dl_argv;
/*     */   public final Symbols.Symbol __libc_stack_end;
/*     */   public final Symbols.Symbol _r_debug;
/*     */   public final Symbols.Symbol _dl_rtld_di_serinfo;
/*     */   public final Symbols.Symbol _dl_get_tls_static_info;
/*     */   public final Symbols.Symbol _dl_allocate_tls_init;
/*     */   public final Symbols.Symbol _dl_make_stack_executable;
/*     */   public final Symbols.Symbol __tls_get_addr;
/*     */   public final Symbols.Symbol ___tls_get_addr;
/*     */   public final Symbols.Symbol _dl_debug_state;
/*     */   public final Symbols.Symbol _dl_tls_setup;
/*     */   public final Symbols.Symbol _dl_mcount;
/*     */   public final Symbols.Symbol _dl_deallocate_tls;
/*     */   public final Symbols.Symbol _dl_allocate_tls;
/*     */   private final Symbols.Symbol _dl_initial_error_catch_tsd;
/*     */   private final Symbols.Symbol _dl_open;
/*     */   private final Symbols.Symbol _dl_close;
/*     */   private final Symbols.Symbol _dl_lookup_symbol_x;
/*     */   private final Symbols.Symbol _dl_debug_printf;
/*     */   private final Symbols.Symbol _dl_tls_get_addr_soft;
/*     */   private final Symbols.Symbol _dl_catch_error;
/*     */   private final Symbols.Symbol _dl_rtld_lock_recursive;
/*     */   private final Symbols.Symbol _dl_rtld_unlock_recursive;
/*     */   private final Symbols.Symbol _dl_make_stack_executable_hook;
/*     */   private final Symbols.Symbol _unknown;
/*     */   private RtLdState state;
/*     */   
/*     */   public LinkerModule(RtLdState state)
/*     */   {
/* 117 */     super("ld-linux.so.2", "/lib/ld-linux.so.2");
/*     */     
/* 119 */     this.state = state;
/* 120 */     this.pltInteceptor = function("pltInterceptor", new Function() {
/* 121 */       public void executeImpl(ThreadContext ctx, ExecutionEnvironment env) { LinkerModule.this.pltInteceptor(ctx); } });
/* 122 */     this._dl_allocate_tls = function("_dl_allocate_tls", new Function() {
/* 123 */       public void executeImpl(ThreadContext ctx, ExecutionEnvironment env) { LinkerModule.this._dl_allocate_tls(ctx);
/*     */       }
/* 125 */     });
/* 126 */     this._dl_deallocate_tls = function("_dl_deallocate_tls", new Function() {
/*     */       public void executeImpl(ThreadContext ctx, ExecutionEnvironment env) {
/* 128 */         LinkerModule.this._dl_deallocate_tls(ctx);
/*     */       }
/* 130 */     });
/* 131 */     this._dl_get_tls_static_info = function("_dl_get_tls_static_info", new Function() {
/*     */       public void executeImpl(ThreadContext ctx, ExecutionEnvironment env) {
/* 133 */         LinkerModule.this._dl_get_tls_static_info(ctx);
/*     */       }
/* 135 */     });
/* 136 */     this._dl_mcount = function("_dl_mcount", new Function() {
/* 137 */       public void executeImpl(ThreadContext ctx, ExecutionEnvironment env) { LinkerModule.this._dl_mcount(ctx); }
/* 138 */     });
/* 139 */     this._dl_tls_setup = function("_dl_tls_setup", new Function() {
/* 140 */       public void executeImpl(ThreadContext ctx, ExecutionEnvironment env) { LinkerModule.this._dl_tls_setup(ctx); }
/* 141 */     });
/* 142 */     this._dl_debug_state = function("_dl_debug_state", new Function() {
/* 143 */       public void executeImpl(ThreadContext ctx, ExecutionEnvironment env) { LinkerModule.this._dl_debug_state(ctx); }
/* 144 */     });
/* 145 */     this.___tls_get_addr = function("___tls_get_addr", new Function() {
/* 146 */       public void executeImpl(ThreadContext ctx, ExecutionEnvironment env) { LinkerModule.this.___tls_get_addr(ctx); }
/* 147 */     });
/* 148 */     this.__tls_get_addr = function("__tls_get_addr", new Function() {
/* 149 */       public void executeImpl(ThreadContext ctx, ExecutionEnvironment env) { LinkerModule.this.__tls_get_addr(ctx); }
/* 150 */     });
/* 151 */     this._dl_make_stack_executable = function("_dl_make_stack_executable", new Function() {
/* 152 */       public void executeImpl(ThreadContext ctx, ExecutionEnvironment env) { LinkerModule.this._dl_make_stack_executable(ctx); }
/* 153 */     });
/* 154 */     this._dl_allocate_tls_init = function("_dl_allocate_tls_init", new Function()
/*     */     {
/* 156 */       public void executeImpl(ThreadContext ctx, ExecutionEnvironment env) { LinkerModule.this._dl_allocate_tls_init(ctx); }
/* 157 */     });
/* 158 */     this._dl_rtld_di_serinfo = function("_dl_rtld_di_serinfo", new Function()
/*     */     {
/* 160 */       public void executeImpl(ThreadContext ctx, ExecutionEnvironment env) { LinkerModule.this._dl_rtld_di_serinfo(ctx); }
/* 161 */     });
/* 162 */     this._dl_initial_error_catch_tsd = function("_dl_initial_error_catch_tsd", new Function() {
/* 163 */       public void executeImpl(ThreadContext ctx, ExecutionEnvironment env) { LinkerModule.this._dl_initial_error_catch_tsd(ctx); }
/* 164 */     });
/* 165 */     this._dl_open = function("_dl_open", new Function() {
/* 166 */       public void executeImpl(ThreadContext ctx, ExecutionEnvironment env) { LinkerModule.this._dl_open(ctx); }
/* 167 */     });
/* 168 */     this._dl_lookup_symbol_x = function("_dl_lookup_symbol_x", new Function() {
/* 169 */       public void executeImpl(ThreadContext ctx, ExecutionEnvironment env) { LinkerModule.this._dl_lookup_symbol_x(ctx); }
/* 170 */     });
/* 171 */     this._dl_close = function("_dl_close", new Function() {
/* 172 */       public void executeImpl(ThreadContext ctx, ExecutionEnvironment env) { LinkerModule.this._dl_close(ctx); }
/* 173 */     });
/* 174 */     this._dl_debug_printf = function("_dl_debug_printf", new Function() {
/* 175 */       public void executeImpl(ThreadContext ctx, ExecutionEnvironment env) { LinkerModule.this._dl_debug_printf(ctx); }
/* 176 */     });
/* 177 */     this._dl_tls_get_addr_soft = function("_dl_tls_get_addr_soft", new Function() {
/* 178 */       public void executeImpl(ThreadContext ctx, ExecutionEnvironment env) { LinkerModule.this._dl_tls_get_addr_soft(ctx); }
/* 179 */     });
/* 180 */     this._dl_catch_error = function("_dl_catch_error", new Function() {
/* 181 */       public void executeImpl(ThreadContext ctx, ExecutionEnvironment env) { LinkerModule.this._dl_catch_error(ctx); }
/* 182 */     });
/* 183 */     this._dl_rtld_lock_recursive = function("_dl_rtld_lock_recursive", new Function() {
/* 184 */       public void executeImpl(ThreadContext ctx, ExecutionEnvironment env) { LinkerModule.this._dl_rtld_lock_recursive(ctx); }
/* 185 */     });
/* 186 */     this._dl_rtld_unlock_recursive = function("_dl_rtld_unlock_recursive", new Function() {
/* 187 */       public void executeImpl(ThreadContext ctx, ExecutionEnvironment env) { LinkerModule.this._dl_rtld_unlock_recursive(ctx); }
/* 188 */     });
/* 189 */     this._dl_make_stack_executable_hook = function("_dl_make_stack_executable_hook", new Function() {
/* 190 */       public void executeImpl(ThreadContext ctx, ExecutionEnvironment env) { LinkerModule.this._dl_make_stack_executable_hook(ctx); }
/* 191 */     });
/* 192 */     this._unknown = function("unknown", new Function() {
/* 193 */       public void executeImpl(ThreadContext ctx, ExecutionEnvironment env) { LinkerModule.this.__unknown(ctx);
/*     */       }
/*     */ 
/* 196 */     });
/* 197 */     this._r_debug = object("_r_debug", 20);
/* 198 */     this.__libc_stack_end = object("__libc_stack_end", 4);
/* 199 */     this._dl_argv = object("_dl_argv", 4);
/* 200 */     this.__libc_enable_secure = object("__libc_enable_secure", 4);
/* 201 */     this.global_tsd = object("__global_tsd", 16);
/* 202 */     align(4096);
/* 203 */     this.rtldState = object("RTLD", state);
/* 204 */     this._rtld_global = aliasObject("_rtld_global", this.rtldState, 0, state.getRtld());
/* 205 */     this._rtld_global_ro = aliasObject("_rtld_global_ro", this.rtldState, 4096, state.getRtldRo());
/*     */   }
/*     */   
/*     */   private void _dl_make_stack_executable_hook(ThreadContext ctx) {
/* 209 */     ctx.ret();
/*     */   }
/*     */   
/*     */   private void _dl_rtld_unlock_recursive(ThreadContext ctx) {
/* 213 */     getLinker().unlock();
/* 214 */     ctx.ret();
/*     */   }
/*     */   
/*     */   private void _dl_rtld_lock_recursive(ThreadContext ctx) {
/* 218 */     getLinker().lock();
/* 219 */     ctx.ret();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   protected int loadImpl(Data memory, int baseAddress, CLinkEntry map)
/*     */     throws IOException
/*     */   {
/* 227 */     int result = super.loadImpl(memory, baseAddress, map);
/*     */     
/* 229 */     Rtld rtld = this.state.getRtld();
/* 230 */     RtldRo rtldRo = this.state.getRtldRo();
/* 231 */     LinkerMap mainMap = this.state.getMap();
/* 232 */     rtld.set(Rtld._dl_error_catch_tsd, this._dl_initial_error_catch_tsd.getRelocatedAddress()).set(Rtld._dl_rtld_lock_recursive, this._dl_rtld_lock_recursive.getRelocatedAddress()).set(Rtld._dl_rtld_unlock_recursive, this._dl_rtld_unlock_recursive.getRelocatedAddress()).set(Rtld._dl_make_stack_executable_hook, this._dl_make_stack_executable_hook.getRelocatedAddress());
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 238 */     rtld.getNameSpace(0).set(Rtld.LinkNameSpace.NS_LOADED, map.addressOf()).set(Rtld.LinkNameSpace.NS_NLOADED, 1L);
/*     */     
/*     */ 
/* 241 */     rtldRo.set(RtldRo._dl_debug_mask, 0L).set(RtldRo._dl_pagesize, 4096L).set(RtldRo._dl_sysinfo_dso, 0L).set(RtldRo._dl_open, this._dl_open.getRelocatedAddress()).set(RtldRo._dl_lookup_symbol_x, this._dl_lookup_symbol_x.getRelocatedAddress()).set(RtldRo._dl_close, this._dl_close.getRelocatedAddress()).set(RtldRo._dl_debug_printf, this._dl_debug_printf.getRelocatedAddress()).set(RtldRo._dl_tls_get_addr_soft, this._dl_tls_get_addr_soft.getRelocatedAddress()).set(RtldRo._dl_catch_error, this._dl_catch_error.getRelocatedAddress());
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 253 */     return result;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void pltInteceptor(ThreadContext ctx)
/*     */   {
/* 263 */     int moduleKey = ctx.pop();
/* 264 */     int functionKey = ctx.pop();
/* 265 */     LinkerMap.Entry module = getLinker().findModule(moduleKey);
/* 266 */     ctx.log.info(getName(), "Intercepting PLT %10s %04X", new Object[] { module.getName(), Integer.valueOf(functionKey) });
/* 267 */     System.exit(0);
/*     */   }
/*     */   
/* 270 */   public void _dl_mcount(ThreadContext ctx) { throw new UnsupportedOperationException("_dl_mcount"); }
/*     */   
/*     */   public void _dl_tls_setup(ThreadContext ctx)
/*     */   {
/* 274 */     throw new UnsupportedOperationException("_dl_tls_setup");
/*     */   }
/*     */   
/* 277 */   public void _dl_debug_state(ThreadContext ctx) { throw new UnsupportedOperationException("_dl_debug_state"); }
/*     */   
/*     */   public void ___tls_get_addr(ThreadContext ctx) {
/* 280 */     int arg = CallArgs.internal(ctx, 0);
/* 281 */     ctx.eax(ctx.getTlsAddress(arg));
/* 282 */     ctx.ret();
/*     */   }
/*     */   
/*     */   public void __tls_get_addr(ThreadContext ctx) {
/* 286 */     ctx.getCpu().eax = CallArgs.cdecl(ctx, 0);
/* 287 */     ___tls_get_addr(ctx);
/*     */   }
/*     */   
/* 290 */   public void _dl_make_stack_executable(ThreadContext ctx) { throw new UnsupportedOperationException("_dl_make_stack_executable"); }
/*     */   
/*     */   public int _dl_allocate_tls(ThreadContext ctx)
/*     */   {
/* 294 */     int newThreadPointer = CallArgs.internal(ctx, 0);
/* 295 */     ctx.log.info("THREAD", "Allocating at threadpointer %08X", new Object[] { Integer.valueOf(newThreadPointer) });
/*     */     
/* 297 */     if (newThreadPointer == 0)
/*     */     {
/* 299 */       throw new UnsupportedOperationException("_dl_allocate_storage");
/*     */     }
/*     */     
/* 302 */     int dtv$ = getLinker().allocateDtv(newThreadPointer);
/*     */     
/* 304 */     newThreadPointer = getLinker().allocateTlsInit(newThreadPointer, dtv$);
/* 305 */     ctx.eax(newThreadPointer);
/* 306 */     ctx.ret();
/* 307 */     return newThreadPointer;
/*     */   }
/*     */   
/* 310 */   private void _dl_tls_get_addr_soft(ThreadContext ctx) { int map = CallArgs.cdecl(ctx, 0);
/* 311 */     int index = (int)CLinkEntry.TLS_MOD_ID.get(map, ctx.memory);
/* 312 */     int tlsAddress = ctx.getTlsAddress(index, 0);
/* 313 */     ctx.eax(tlsAddress);
/* 314 */     ctx.ret();
/*     */   }
/*     */   
/*     */   public void _dl_deallocate_tls(ThreadContext ctx) {
/* 318 */     ctx.log.warn("THREAD", "Unimplemented call %s", new Object[] { "_dl_deallocate_tls" });
/* 319 */     ctx.ret();
/*     */   }
/*     */   
/*     */   public void _dl_allocate_tls_init(ThreadContext ctx) {
/* 323 */     throw new UnsupportedOperationException("_dl_allocate_tls_init");
/*     */   }
/*     */   
/* 326 */   public void _dl_rtld_di_serinfo(ThreadContext ctx) { throw new UnsupportedOperationException("_dl_rtld_di_serinfo"); }
/*     */   
/*     */   public void _dl_initial_error_catch_tsd(ThreadContext ctx) {
/* 329 */     ctx.eax(this.global_tsd.getRelocatedAddress());
/* 330 */     ctx.ret();
/*     */   }
/*     */   
/* 333 */   public void _dl_get_tls_static_info(ThreadContext ctx) { int $tls_static_size = CallArgs.internal(ctx, 0);
/* 334 */     int $tls_static_align = CallArgs.internal(ctx, 1);
/* 335 */     ctx.getMemory().setDoubleWord($tls_static_size, getLinker().getStaticTlsSize());
/* 336 */     ctx.getMemory().setDoubleWord($tls_static_align, getLinker().getStaticTlsAlign());
/* 337 */     ctx.ret();
/*     */   }
/*     */   
/* 340 */   private void _dl_close(ThreadContext ctx) { throw new UnsupportedOperationException("_dl_close"); }
/*     */   
/*     */   private void _dl_open(ThreadContext ctx)
/*     */   {
/* 344 */     int file$ = CallArgs.cdecl(ctx, 0);
/* 345 */     int mode = CallArgs.cdecl(ctx, 1);
/* 346 */     String fileName = ctx.getMemory().string(file$);
/*     */     
/* 348 */     ctx.eax(getLinker().dlopen(ctx, fileName, mode));
/*     */     
/* 350 */     ctx.ret();
/*     */   }
/*     */   
/* 353 */   private void _dl_lookup_symbol_x(ThreadContext ctx) { MainMemory memory = ctx.getMemory();
/* 354 */     String symbolName = memory.string(CallArgs.internal(ctx, 0));
/* 355 */     int moduleAddress = CallArgs.internal(ctx, 1);
/* 356 */     int symbol$$ = CallArgs.internal(ctx, 2);
/* 357 */     Symbols.Symbol symbol = getLinker().dlSymbol(moduleAddress, symbolName);
/*     */     
/* 359 */     int result = 0;
/* 360 */     int location = 0;
/* 361 */     if (symbol != null) {
/* 362 */       location = symbol.getLocation();
/* 363 */       result = moduleAddress;
/*     */     }
/* 365 */     memory.setDoubleWord(symbol$$, location);
/* 366 */     ctx.eax(result);
/* 367 */     ctx.ret(5);
/*     */   }
/*     */   
/* 370 */   private void _dl_debug_printf(ThreadContext ctx) { ctx.warn(this.rtldState.toString(), new Object[0]);
/* 371 */     ctx.warn(this._rtld_global.toString(), new Object[0]);
/* 372 */     ctx.warn(this._rtld_global_ro.toString(), new Object[0]);
/* 373 */     throw new UnsupportedOperationException(this._dl_debug_printf.toString());
/*     */   }
/*     */   
/* 376 */   private void __unknown(ThreadContext ctx) { throw new UnsupportedOperationException("unknown"); }
/*     */   
/*     */ 
/*     */   private void _dl_catch_error(ThreadContext ctx)
/*     */   {
/* 381 */     MainMemory memory = ctx.getMemory();
/* 382 */     int objname$$ = CallArgs.internal(ctx, 0);
/* 383 */     int errstring$$ = CallArgs.internal(ctx, 1);
/* 384 */     int malloced$b = CallArgs.internal(ctx, 2);
/*     */     
/* 386 */     int operate$ = CallArgs.internal(ctx, 3);
/* 387 */     int arg$ = CallArgs.internal(ctx, 4);
/* 388 */     int file$ = memory.getDoubleWord(arg$);
/* 389 */     int mode = memory.getDoubleWord(arg$ + 4);
/*     */     
/*     */     try
/*     */     {
/* 393 */       ctx.push(arg$);
/*     */       
/* 395 */       ctx.call(operate$, "dl_catch_error");
/* 396 */       memory.setDoubleWord(objname$$, 0);
/* 397 */       memory.setDoubleWord(errstring$$, 0);
/* 398 */       memory.setByte(malloced$b, (byte)0);
/* 399 */       ctx.pop();
/* 400 */       ctx.eax(0);
/*     */       
/* 402 */       ctx.ret(2);
/*     */     } catch (IllegalStateException ise) {
/* 404 */       ctx.trace("_dl_catch_error Exception (args:%08X) function=%08X file [%08X]=%s mode=%X", new Object[] { Integer.valueOf(arg$), Integer.valueOf(operate$), Integer.valueOf(file$), memory.string(file$), Integer.valueOf(mode) });
/* 405 */       ctx.dump();
/* 406 */       System.exit(0);
/*     */ 
/*     */ 
/*     */     }
/*     */     catch (LongJumpException nse)
/*     */     {
/*     */ 
/* 413 */       int errorCode = nse.getStatus();
/* 414 */       ctx.eax(errorCode == -1 ? 0 : errorCode);
/* 415 */       ctx.ret();
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Joey\Downloads\codejar.jar!\com\emt\proteus\runtime32\rtld\LinkerModule.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */