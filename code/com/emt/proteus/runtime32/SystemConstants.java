/*    */ package com.emt.proteus.runtime32;
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
/*    */ public final class SystemConstants
/*    */ {
/*    */   public static final String STATIC_CONSTRUCTORS = "_static_constructors_";
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
/* 34 */   public static String UNAME_SYSNAME = "Linux";
/* 35 */   public static String UNAME_NODENAME = "emt-EX58-UD4P";
/* 36 */   public static String UNAME_RELEASE = "3.0.0-12-generic-pae";
/*    */   
/* 38 */   public static String UNAME_VERSION = "#20-Ubuntu SMP Fri Oct 7 16:37:17 UTC 2011";
/* 39 */   public static String UNAME_MACHINE = "i686";
/*    */   public static final int _ERROR_NO_ADDRESS = 4;
/*    */   public static final int OPT_FINISHED = 12;
/*    */   public static final int OPT_IND = 16;
/*    */   public static final int OPT_ERR = 20;
/*    */   public static final int OPT_ARG = 24;
/*    */   public static final int OPT_OPT = 28;
/*    */   public static final int CTYPE_UPPER_LOC = 32;
/*    */   public static final int CTYPE_B_LOC = 36;
/*    */   public static final int TIME_STRUCT = 64;
/*    */   public static final int LCONV_STRUCT = 128;
/*    */   public static final int LCONV_STRUCT_DECIMAL = 128;
/*    */   public static final int LCONV_STRUCT_THOUSAND = 132;
/*    */   public static final int LCONV_STRUCT_GROUP = 136;
/*    */   public static final int LCONV_STRUCT_CURRENCY = 140;
/*    */   public static final int LCONV_STRS = 192;
/*    */   public static final int NULL_STR = 192;
/*    */   public static final int LCONV_DP_STR = 194;
/*    */   public static final int LCONV_MON = 196;
/*    */   public static final int LAST_STRING = 256;
/*    */   public static final int TIME_STRING = 256;
/*    */   public static final int STDOUTIN_ADDR = 1792;
/*    */   public static final int STDIN_ADDR = 1792;
/*    */   public static final int STDOUT_ADDR = 1792;
/*    */   public static final int STDERR_ADDR = 1800;
/*    */   public static final int COUTIN_ADDR = 1808;
/*    */   public static final int CIN_ADDR = 1808;
/*    */   public static final int COUT_ADDR = 1808;
/*    */   public static final int CERR_ADDR = 2048;
/*    */   public static final int IO_VTABLE_START = 2304;
/*    */   public static final int IO_VTABLE_END = 4096;
/*    */   public static final int CTYPE_UPPER_START = 8192;
/*    */   public static final int CTYPE_UPPER_BASE = 8704;
/*    */   public static final int CTYPE_B_START = 9728;
/*    */   public static final int CTYPE_B_BASE = 9984;
/*    */   public static final int STRING_MEM_START = 10752;
/*    */   public static final int STRING_MEM_END = 12288;
/*    */   public static final int SYSTEM_MEMORY_LIMIT = 12288;
/*    */   public static final int RAND_MAX = 32767;
/*    */   public static final int FN_ID_BASE = Integer.MIN_VALUE;
/*    */   public static final int MATH_FN_ID_BASE = -2130706432;
/*    */   public static final int STR_FN_ID_BASE = -2113929216;
/*    */   public static final int MEMSET = -2147483647;
/*    */   public static final int _NEW = -2147483646;
/*    */   public static final int _NEW_ARRAY = -2147483645;
/*    */   public static final int MALLOC = -2147483644;
/*    */   public static final int FREE = -2147483643;
/*    */   public static final int VARGS_END = -2147483640;
/*    */   public static final int VARGS_START = -2147483639;
/*    */   public static final int LONGJMP = -2147483638;
/*    */   public static final int SPRINTF = -2147483635;
/*    */   public static final int FPRINTF = -2147483634;
/*    */   public static final int PRINTF = -2147483633;
/*    */   public static final int FSCANF = -2147483632;
/*    */   public static final int _DELETE = -2147483630;
/*    */   public static final int _DELETE_ARRAY = -2147483629;
/*    */   public static final int FCLOSE = -2147483627;
/*    */   public static final int FEOF = -2147483625;
/*    */   public static final int FFLUSH = -2147483624;
/*    */   public static final int FGETS = -2147483623;
/*    */   public static final int FOPEN = -2147483622;
/*    */   public static final int FPUTC = -2147483621;
/*    */   public static final int FPUTS = -2147483620;
/*    */   public static final int FREAD = -2147483619;
/*    */   public static final int OPEN = -2147483618;
/*    */   public static final int PUTCHAR = -2147483617;
/*    */   public static final int PUTS = -2147483616;
/*    */   public static final int LSEEK = -2147483615;
/*    */   public static final int READ = -2147483614;
/*    */   public static final int FSEEK = -2147483613;
/*    */   public static final int FSTAT = -2147483612;
/*    */   public static final int SNPRINTF = -2147483611;
/*    */   public static final int REALLOC = -2147483610;
/*    */   public static final int DO_THROWABLE = -2147483609;
/*    */   public static final int CHECK_THROWABLE = -2147483608;
/*    */   public static final int MEMCPY_LONG_ADDR = -2147483607;
/*    */   public static final int MEMSET_LONG_ADDR = -2147483606;
/*    */   public static final int __ADD_SHUTDOWN_HOOK = -2147483605;
/*    */   public static final int ACQUIRE_GUARD = -2147483604;
/*    */   public static final int RELEASE_GUARD = -2147483603;
/*    */   public static final int __ERRNO_LOCATION = -2147483602;
/*    */   public static final int _SETJMP = -2147483601;
/*    */   public static final int VFPRINTF = -2147483600;
/*    */   public static final int VSNPRINTF = -2147483599;
/*    */   public static final int VSPRINTF = -2147483598;
/*    */   public static final int MEMCPY = -2147483596;
/*    */   public static final int BSWAP_INT = -2147483595;
/*    */   public static final int BSWAP_SHORT = -2147483594;
/*    */   public static final int SSCANF = -2147483593;
/*    */   public static final int MEMCMP = -2147483578;
/*    */   public static final int MEMCHR = -2147483577;
/*    */   public static final int FCNTL = -2147483570;
/*    */   public static final int EXIT = -2147483569;
/*    */   public static final int CLOSE = -2147483568;
/*    */   public static final int STAT = -2147483567;
/*    */   public static final int FWRITE = -2147483566;
/*    */   public static final int GETCWD = -2147483565;
/*    */   public static final int GETENV = -2147483564;
/*    */   public static final int GETHOSTBYNAME = -2147483563;
/*    */   public static final int GETPAGESIZE = -2147483562;
/*    */   public static final int GETPID = -2147483561;
/*    */   public static final int GETTIMEOFDAY = -2147483560;
/*    */   public static final int CTIME = -2147483559;
/*    */   public static final int GMTIME = -2147483558;
/*    */   public static final int HTONS = -2147483557;
/*    */   public static final int INET_ADDR = -2147483556;
/*    */   public static final int IOCTL = -2147483555;
/*    */   public static final int LOCALTIME = -2147483552;
/*    */   public static final int ASCTIME = -2147483550;
/*    */   public static final int MKSTEMP = -2147483549;
/*    */   public static final int MKTIME = -2147483548;
/*    */   public static final int PERROR = -2147483547;
/*    */   public static final int SETLOCALE = -2147483544;
/*    */   public static final int SIGNAL = -2147483543;
/*    */   public static final int TIME = -2147483542;
/*    */   public static final int UNLINK = -2147483539;
/*    */   public static final int WRITE = -2147483538;
/*    */   public static final int USLEEP = -2147483536;
/*    */   public static final int FGETC = -2147483533;
/*    */   public static final int UNGETC = -2147483532;
/*    */   public static final int CLEARERR = -2147483531;
/*    */   public static final int MEMMOVE = -2147483530;
/*    */   public static final int MEMMOVE_LONG_ADDR = -2147483529;
/*    */   public static final int CALLOC = -2147483528;
/*    */   public static final int LSEEK64 = -2147483499;
/*    */   public static final int FSTAT64 = -2147483498;
/*    */   public static final int STAT64 = -2147483497;
/*    */   public static final int UTIME = -2147483496;
/*    */   public static final int SCANF = -2147483495;
/*    */   public static final int NTOHS = -2147483494;
/*    */   public static final int _UNWIND = -2147483491;
/*    */   public static final int ABORT = -2147483490;
/*    */   public static final int _NO_IMPLEMENTATION = -2147483489;
/*    */   public static final int __ASSERT_FAILURE = -2147483488;
/*    */   public static final int __GET_CLASS = -2147483487;
/*    */   public static final int ACCEPT = -2147483486;
/*    */   public static final int ACCESS = -2147483485;
/*    */   public static final int LISTEN = -2147483484;
/*    */   public static final int GETOPT = -2147483492;
/*    */   public static final int MATH_ABS = -2130706431;
/*    */   public static final int MATH_ACOS = -2130706430;
/*    */   public static final int MATH_ASIN = -2130706429;
/*    */   public static final int MATH_ATAN = -2130706428;
/*    */   public static final int MATH_ATAN2 = -2130706426;
/*    */   public static final int MATH_CEIL = -2130706425;
/*    */   public static final int MATH_CEILF = -2130706424;
/*    */   public static final int MATH_COS = -2130706423;
/*    */   public static final int MATH_COSH = -2130706422;
/*    */   public static final int MATH_ERFC = -2130706421;
/*    */   public static final int MATH_EXP = -2130706420;
/*    */   public static final int MATH_FABS = -2130706419;
/*    */   public static final int MATH_FABSF = -2130706418;
/*    */   public static final int MATH_FLOOR = -2130706417;
/*    */   public static final int MATH_FLOORF = -2130706416;
/*    */   public static final int MATH_FMOD = -2130706415;
/*    */   public static final int MATH_HYPOT = -2130706414;
/*    */   public static final int MATH_LDEXP = -2130706413;
/*    */   public static final int MATH_LOG = -2130706412;
/*    */   public static final int MATH_LOG10 = -2130706410;
/*    */   public static final int MATH_POW = -2130706409;
/*    */   public static final int MATH_RAND = -2130706408;
/*    */   public static final int MATH_SIN = -2130706407;
/*    */   public static final int MATH_SINCOS = -2130706406;
/*    */   public static final int MATH_SINH = -2130706405;
/*    */   public static final int MATH_SQRT = -2130706404;
/*    */   public static final int MATH_SQRTF = -2130706403;
/*    */   public static final int MATH_SRAND = -2130706402;
/*    */   public static final int MATH_TAN = -2130706401;
/*    */   public static final int MATH_TANH = -2130706400;
/*    */   public static final int ATOF = -2113929215;
/*    */   public static final int ATOI = -2113929214;
/*    */   public static final int ATOL = -2113929213;
/*    */   public static final int ATOLL = -2113929212;
/*    */   public static final int ISALPHA = -2113929211;
/*    */   public static final int ISPRINT = -2113929210;
/*    */   public static final int ISSPACE = -2113929209;
/*    */   public static final int STRCASECMP = -2113929208;
/*    */   public static final int STRCAT = -2113929207;
/*    */   public static final int STRCHR = -2113929206;
/*    */   public static final int STRCMP = -2113929205;
/*    */   public static final int STRCPY = -2113929204;
/*    */   public static final int STRCSPN = -2113929203;
/*    */   public static final int STRDUP = -2113929202;
/*    */   public static final int STRERROR = -2113929201;
/*    */   public static final int STRLEN = -2113929200;
/*    */   public static final int STRNCASECMP = -2113929199;
/*    */   public static final int STRNCAT = -2113929198;
/*    */   public static final int STRNCMP = -2113929197;
/*    */   public static final int STRNCPY = -2113929196;
/*    */   public static final int STRRCHR = -2113929195;
/*    */   public static final int STRSTR = -2113929194;
/*    */   public static final int STRTOD = -2113929193;
/*    */   public static final int STRTOK = -2113929192;
/*    */   public static final int STRTOL = -2113929191;
/*    */   public static final int STRTOLL = -2113929190;
/*    */   public static final int STRTOUL = -2113929189;
/*    */   public static final int STRTOULL = -2113929188;
/*    */   public static final int TOLOWER = -2113929187;
/*    */   public static final int TOUPPER = -2113929186;
/*    */ }


/* Location:              C:\Users\Joey\Downloads\codejar.jar!\com\emt\proteus\runtime32\SystemConstants.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */