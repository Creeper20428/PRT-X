/*      */ package com.emt.proteus.decoder;
/*      */ 
/*      */ import java.io.PrintStream;
/*      */ import java.lang.reflect.Field;
/*      */ import java.lang.reflect.Modifier;
/*      */ import java.util.Arrays;
/*      */ import java.util.Collection;
/*      */ import java.util.HashMap;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public class UCodes
/*      */ {
/*      */   public static final int NOOP = 0;
/*      */   public static final int EIP_UPDATE = 1;
/*      */   public static final int AND = 2;
/*      */   public static final int OR = 3;
/*      */   public static final int NOT = 4;
/*      */   public static final int XOR = 5;
/*      */   public static final int MASK2_8 = 6;
/*      */   public static final int MASK2_16 = 7;
/*      */   public static final int SIGN_EXTEND8 = 8;
/*      */   public static final int SIGN_EXTEND16 = 9;
/*      */   public static final int CMPXCHG = 10;
/*      */   public static final int XCHG = 11;
/*      */   public static final int XADD = 12;
/*      */   public static final int MOVSB = 13;
/*      */   public static final int MOVSW = 14;
/*      */   public static final int CMOVNE = 15;
/*      */   public static final int CMOVS = 16;
/*      */   public static final int CMOVNS = 17;
/*      */   public static final int CMOVE = 18;
/*      */   public static final int CMOVGE = 19;
/*      */   public static final int CMOVLE = 20;
/*      */   public static final int CMOVL = 21;
/*      */   public static final int CMOVB = 22;
/*      */   public static final int CMOVG = 23;
/*      */   public static final int CMOVBE = 24;
/*      */   public static final int CMOVA = 25;
/*      */   public static final int CMOVAE = 26;
/*      */   public static final int CMOVO = 27;
/*      */   public static final int CMOVNO = 28;
/*      */   public static final int CMOVP = 29;
/*      */   public static final int CMOVNP = 30;
/*      */   public static final int REP_MOVS_DWORD = 31;
/*      */   public static final int REP_MOVS_BYTE = 32;
/*      */   public static final int REPZ_CMPS_BYTE = 33;
/*      */   public static final int REPNE_SCAS_BYTE = 34;
/*      */   public static final int REP_STOS_DWORD = 35;
/*      */   public static final int REP_STOS_BYTE = 36;
/*      */   public static final int STOS_BYTE = 37;
/*      */   public static final int BT16 = 38;
/*      */   public static final int BT32 = 39;
/*      */   public static final int BTC16 = 40;
/*      */   public static final int BTC32 = 41;
/*      */   public static final int BTR16 = 42;
/*      */   public static final int BTR32 = 43;
/*      */   public static final int BSR = 44;
/*      */   public static final int BSF = 45;
/*      */   public static final int CPUID = 46;
/*      */   public static final int RDTSC = 47;
/*      */   public static final int POPF = 48;
/*      */   public static final int PUSHF = 49;
/*      */   public static final int FPOP = 50;
/*      */   public static final int FSTSW = 51;
/*      */   public static final int FSTCW = 52;
/*      */   public static final int FLDCW = 53;
/*      */   public static final int FCOM = 54;
/*      */   public static final int FXAM = 55;
/*      */   public static final int FLDZ = 56;
/*      */   public static final int FST = 57;
/*      */   public static final int FSTP = 58;
/*      */   public static final int FLD = 59;
/*      */   public static final int FLD1 = 60;
/*      */   public static final int FLDPI = 61;
/*      */   public static final int FLDLN2 = 62;
/*      */   public static final int FLDL2E = 63;
/*      */   public static final int FLDL2T = 64;
/*      */   public static final int FLDLG2 = 65;
/*      */   public static final int FXCH1 = 66;
/*      */   public static final int FXCH2 = 67;
/*      */   public static final int FXCH3 = 68;
/*      */   public static final int FXCH4 = 69;
/*      */   public static final int FXCH5 = 70;
/*      */   public static final int FXCH6 = 71;
/*      */   public static final int FUCOMI = 72;
/*      */   public static final int L2D = 73;
/*      */   public static final int FMUL = 74;
/*      */   public static final int FADD = 75;
/*      */   public static final int FSUB = 76;
/*      */   public static final int FABS = 77;
/*      */   public static final int FCHS = 78;
/*      */   public static final int FSQRT = 79;
/*      */   public static final int FSCALE = 80;
/*      */   public static final int F2XM1 = 81;
/*      */   public static final int FRNDINT = 82;
/*      */   public static final int FSINCOS = 83;
/*      */   public static final int FSIN = 84;
/*      */   public static final int FCOS = 85;
/*      */   public static final int FTAN = 86;
/*      */   public static final int FATAN = 87;
/*      */   public static final int FPREM = 88;
/*      */   public static final int FDIV = 89;
/*      */   public static final int FDIVR = 90;
/*      */   public static final int FYL2XP1 = 91;
/*      */   public static final int FYL2X = 92;
/*      */   public static final int FCMOVE = 93;
/*      */   public static final int FCMOVNE = 94;
/*      */   public static final int FCMOVB = 95;
/*      */   public static final int FCMOVNB = 96;
/*      */   public static final int FCMOVBE = 97;
/*      */   public static final int FCMOVNBE = 98;
/*      */   public static final int FCMOVU = 99;
/*      */   public static final int FCMOVNU = 100;
/*      */   public static final int FSTENV = 101;
/*      */   public static final int FLDENV = 102;
/*      */   public static final int FSAVE = 103;
/*      */   public static final int FRSTOR = 104;
/*      */   public static final int D2I16 = 105;
/*      */   public static final int D2I32 = 106;
/*      */   public static final int D2I64 = 107;
/*      */   public static final int D2BCD = 108;
/*      */   public static final int BCD2D = 109;
/*      */   public static final int FINIT = 110;
/*      */   public static final int STMXCSR = 111;
/*      */   public static final int LDMXCSR = 112;
/*      */   public static final int ROL8 = 113;
/*      */   public static final int ROL16 = 114;
/*      */   public static final int ROL32 = 115;
/*      */   public static final int ROR8 = 116;
/*      */   public static final int ROR16 = 117;
/*      */   public static final int ROR32 = 118;
/*      */   public static final int RCR8 = 119;
/*      */   public static final int RCR16 = 120;
/*      */   public static final int RCR32 = 121;
/*      */   public static final int RCL8 = 122;
/*      */   public static final int RCL16 = 123;
/*      */   public static final int RCL32 = 124;
/*      */   public static final int SHR = 125;
/*      */   public static final int SHRD16 = 126;
/*      */   public static final int SHRD32 = 127;
/*      */   public static final int ADD8 = 128;
/*      */   public static final int ADD16 = 129;
/*      */   public static final int ADD32 = 130;
/*      */   public static final int ADC8 = 131;
/*      */   public static final int ADC16 = 132;
/*      */   public static final int ADC32 = 133;
/*      */   public static final int SUB8 = 134;
/*      */   public static final int SUB16 = 135;
/*      */   public static final int SUB32 = 136;
/*      */   public static final int SBB8 = 137;
/*      */   public static final int SBB16 = 138;
/*      */   public static final int SBB32 = 139;
/*      */   public static final int INC = 140;
/*      */   public static final int DEC = 141;
/*      */   public static final int NEG8 = 142;
/*      */   public static final int NEG16 = 143;
/*      */   public static final int NEG32 = 144;
/*      */   public static final int SAR8 = 145;
/*      */   public static final int SAR16 = 146;
/*      */   public static final int SAR32 = 147;
/*      */   public static final int SHL8 = 148;
/*      */   public static final int SHL16 = 149;
/*      */   public static final int SHL32 = 150;
/*      */   public static final int SHLD16 = 151;
/*      */   public static final int SHLD32 = 152;
/*      */   public static final int IMUL8 = 153;
/*      */   public static final int IMUL16 = 154;
/*      */   public static final int IMUL32 = 155;
/*      */   public static final int MUL8 = 156;
/*      */   public static final int MUL16 = 157;
/*      */   public static final int MUL32 = 158;
/*      */   public static final int DIV8 = 159;
/*      */   public static final int DIV16 = 160;
/*      */   public static final int DIV32 = 161;
/*      */   public static final int IDIV8 = 162;
/*      */   public static final int IDIV16 = 163;
/*      */   public static final int IDIV32 = 164;
/*      */   public static final int SETNE = 165;
/*      */   public static final int SETE = 166;
/*      */   public static final int SETGE = 167;
/*      */   public static final int SETG = 168;
/*      */   public static final int SETLE = 169;
/*      */   public static final int SETL = 170;
/*      */   public static final int SETA = 171;
/*      */   public static final int SETAE = 172;
/*      */   public static final int SETB = 173;
/*      */   public static final int SETBE = 174;
/*      */   public static final int SETO = 175;
/*      */   public static final int SETNO = 176;
/*      */   public static final int SETNP = 177;
/*      */   public static final int SETP = 178;
/*      */   public static final int SETS = 179;
/*      */   public static final int SETNS = 180;
/*      */   public static final int CLD = 181;
/*      */   public static final int STD = 182;
/*      */   public static final int SAHF = 183;
/*      */   public static final int FLAGS = 184;
/*      */   public static final int FLAGS_IMUL32 = 185;
/*      */   public static final int FLAGS_SHIFT = 186;
/*      */   public static final int FLAGS_DOUBLE_SHIFT = 187;
/*      */   public static final int FLAGS_OSZAP = 188;
/*      */   public static final int FLAGS_SZP = 189;
/*      */   public static final int BITWISE_FLAGS32 = 190;
/*      */   public static final int BITWISE_FLAGS16 = 191;
/*      */   public static final int BITWISE_FLAGS8 = 192;
/*      */   public static final int LEAVE = 193;
/*      */   public static final int INT = 194;
/*      */   public static final int CALL_REL = 195;
/*      */   public static final int CALL_ECX = 196;
/*      */   public static final int CALL_EDX = 197;
/*      */   public static final int CALL_ESI = 198;
/*      */   public static final int CALL_EDI = 199;
/*      */   public static final int CALL_EAX = 200;
/*      */   public static final int CALL_EBX = 201;
/*      */   public static final int CALL_EBP = 202;
/*      */   public static final int CALL_ABS = 203;
/*      */   public static final int RET = 204;
/*      */   public static final int RET_II = 205;
/*      */   public static final int JMP = 206;
/*      */   public static final int JMP_ABS = 207;
/*      */   public static final int JS = 208;
/*      */   public static final int JNS = 209;
/*      */   public static final int JA = 210;
/*      */   public static final int JAE = 211;
/*      */   public static final int JB = 212;
/*      */   public static final int JBE = 213;
/*      */   public static final int JCXZ = 214;
/*      */   public static final int JE = 215;
/*      */   public static final int JNE = 216;
/*      */   public static final int JL = 217;
/*      */   public static final int JLE = 218;
/*      */   public static final int JO = 219;
/*      */   public static final int JNO = 220;
/*      */   public static final int JP = 221;
/*      */   public static final int JNP = 222;
/*      */   public static final int JG = 223;
/*      */   public static final int JGE = 224;
/*      */   public static final int JECXZ = 225;
/*      */   public static final int LOOP = 226;
/*      */   public static final int LOOPE = 227;
/*      */   public static final int LOOPNE = 228;
/*      */   public static final int PUSH = 229;
/*      */   public static final int PUSH_EAX = 230;
/*      */   public static final int PUSH_EBX = 231;
/*      */   public static final int PUSH_ECX = 232;
/*      */   public static final int PUSH_EDX = 233;
/*      */   public static final int PUSH_ESI = 234;
/*      */   public static final int PUSH_EDI = 235;
/*      */   public static final int PUSH_ESP = 236;
/*      */   public static final int PUSH_EBP = 237;
/*      */   public static final int POP = 238;
/*      */   public static final int POP_EAX = 239;
/*      */   public static final int POP_EBX = 240;
/*      */   public static final int POP_ECX = 241;
/*      */   public static final int POP_EDX = 242;
/*      */   public static final int POP_ESI = 243;
/*      */   public static final int POP_EDI = 244;
/*      */   public static final int POP_EBP = 245;
/*      */   public static final int MOV_EBP_ESP = 246;
/*      */   public static final int MOV_GS_EAX = 247;
/*      */   public static final int ADDR_$D = 248;
/*      */   public static final int ADDR_EAX = 249;
/*      */   public static final int ADDR_2EAX = 250;
/*      */   public static final int ADDR_4EAX = 251;
/*      */   public static final int ADDR_8EAX = 252;
/*      */   public static final int ADDR_EBX = 253;
/*      */   public static final int ADDR_2EBX = 254;
/*      */   public static final int ADDR_4EBX = 255;
/*      */   public static final int ADDR_8EBX = 256;
/*      */   public static final int ADDR_ECX = 257;
/*      */   public static final int ADDR_2ECX = 258;
/*      */   public static final int ADDR_4ECX = 259;
/*      */   public static final int ADDR_8ECX = 260;
/*      */   public static final int ADDR_EDX = 261;
/*      */   public static final int ADDR_2EDX = 262;
/*      */   public static final int ADDR_4EDX = 263;
/*      */   public static final int ADDR_8EDX = 264;
/*      */   public static final int ADDR_ESI = 265;
/*      */   public static final int ADDR_2ESI = 266;
/*      */   public static final int ADDR_4ESI = 267;
/*      */   public static final int ADDR_8ESI = 268;
/*      */   public static final int ADDR_EDI = 269;
/*      */   public static final int ADDR_2EDI = 270;
/*      */   public static final int ADDR_4EDI = 271;
/*      */   public static final int ADDR_8EDI = 272;
/*      */   public static final int ADDR_ESP = 273;
/*      */   public static final int ADDR_EBP = 274;
/*      */   public static final int ADDR_2EBP = 275;
/*      */   public static final int ADDR_4EBP = 276;
/*      */   public static final int ADDR_8EBP = 277;
/*      */   public static final int ADDR_GS = 278;
/*      */   public static final int ZERO_ADDR = 279;
/*      */   public static final int LOAD0_$I = 280;
/*      */   public static final int LOAD0_EAX = 281;
/*      */   public static final int LOAD0_EBX = 282;
/*      */   public static final int LOAD0_ECX = 283;
/*      */   public static final int LOAD0_EDX = 284;
/*      */   public static final int LOAD0_ESI = 285;
/*      */   public static final int LOAD0_EDI = 286;
/*      */   public static final int LOAD0_ESP = 287;
/*      */   public static final int LOAD0_EBP = 288;
/*      */   public static final int LOAD0_AL = 289;
/*      */   public static final int LOAD0_AH = 290;
/*      */   public static final int LOAD0_AX = 291;
/*      */   public static final int LOAD0_BL = 292;
/*      */   public static final int LOAD0_BH = 293;
/*      */   public static final int LOAD0_BX = 294;
/*      */   public static final int LOAD0_CL = 295;
/*      */   public static final int LOAD0_CH = 296;
/*      */   public static final int LOAD0_CX = 297;
/*      */   public static final int LOAD0_DL = 298;
/*      */   public static final int LOAD0_DH = 299;
/*      */   public static final int LOAD0_DX = 300;
/*      */   public static final int LOAD0_SI = 301;
/*      */   public static final int LOAD0_DI = 302;
/*      */   public static final int LOAD0_BP = 303;
/*      */   public static final int LOAD0_MEM_BYTE = 304;
/*      */   public static final int LOAD0_MEM_WORD = 305;
/*      */   public static final int LOAD0_MEM_DWORD = 306;
/*      */   public static final int LOADL0_MEM_WORD = 307;
/*      */   public static final int LOADL0_MEM_DWORD = 308;
/*      */   public static final int LOADL0_MEM_QWORD = 309;
/*      */   public static final int LOAD1_$I = 310;
/*      */   public static final int LOAD1_EAX = 311;
/*      */   public static final int LOAD1_EBX = 312;
/*      */   public static final int LOAD1_ECX = 313;
/*      */   public static final int LOAD1_EDX = 314;
/*      */   public static final int LOAD1_ESI = 315;
/*      */   public static final int LOAD1_EDI = 316;
/*      */   public static final int LOAD1_ESP = 317;
/*      */   public static final int LOAD1_EBP = 318;
/*      */   public static final int LOAD1_AX = 319;
/*      */   public static final int LOAD1_AH = 320;
/*      */   public static final int LOAD1_AL = 321;
/*      */   public static final int LOAD1_BX = 322;
/*      */   public static final int LOAD1_BL = 323;
/*      */   public static final int LOAD1_CX = 324;
/*      */   public static final int LOAD1_CL = 325;
/*      */   public static final int LOAD1_CH = 326;
/*      */   public static final int LOAD1_DX = 327;
/*      */   public static final int LOAD1_DL = 328;
/*      */   public static final int LOAD1_DH = 329;
/*      */   public static final int LOAD1_SI = 330;
/*      */   public static final int LOAD1_DI = 331;
/*      */   public static final int LOAD1_BP = 332;
/*      */   public static final int LOAD1_MEM_DWORD = 333;
/*      */   public static final int LOAD1_MEM_WORD = 334;
/*      */   public static final int LOAD1_MEM_BYTE = 335;
/*      */   public static final int LOAD1_1 = 336;
/*      */   public static final int LOAD2_$I = 337;
/*      */   public static final int LOAD2_EAX = 338;
/*      */   public static final int LOAD2_EBX = 339;
/*      */   public static final int LOAD2_ECX = 340;
/*      */   public static final int LOAD2_EDX = 341;
/*      */   public static final int LOAD2_ESI = 342;
/*      */   public static final int LOAD2_EDI = 343;
/*      */   public static final int LOAD2_ESP = 344;
/*      */   public static final int LOAD2_EBP = 345;
/*      */   public static final int LOAD2_AX = 346;
/*      */   public static final int LOAD2_AH = 347;
/*      */   public static final int LOAD2_AL = 348;
/*      */   public static final int LOAD2_BX = 349;
/*      */   public static final int LOAD2_BL = 350;
/*      */   public static final int LOAD2_CX = 351;
/*      */   public static final int LOAD2_CH = 352;
/*      */   public static final int LOAD2_CL = 353;
/*      */   public static final int LOAD2_DX = 354;
/*      */   public static final int LOAD2_DH = 355;
/*      */   public static final int LOAD2_DL = 356;
/*      */   public static final int LOAD2_SI = 357;
/*      */   public static final int LOAD2_DI = 358;
/*      */   public static final int LOAD2_BP = 359;
/*      */   public static final int LOAD2_MEM_DWORD = 360;
/*      */   public static final int LOAD2_MEM_WORD = 361;
/*      */   public static final int LOAD2_MEM_BYTE = 362;
/*      */   public static final int LOAD3_CL = 363;
/*      */   public static final int LOAD3_$I = 364;
/*      */   public static final int STORE1_EAX = 365;
/*      */   public static final int STORE1_ESI = 366;
/*      */   public static final int STORE1_EDI = 367;
/*      */   public static final int STORE1_EBX = 368;
/*      */   public static final int STORE1_EDX = 369;
/*      */   public static final int STORE1_ECX = 370;
/*      */   public static final int STORE1_AX = 371;
/*      */   public static final int STORE1_MEM_DWORD = 372;
/*      */   public static final int STORE0_AX = 373;
/*      */   public static final int STORE0_EAX = 374;
/*      */   public static final int STORE0_EBX = 375;
/*      */   public static final int STORE0_ECX = 376;
/*      */   public static final int STORE0_EDX = 377;
/*      */   public static final int STORE0_EDI = 378;
/*      */   public static final int STORE0_ESI = 379;
/*      */   public static final int STORE0_EBP = 380;
/*      */   public static final int STORE0_MEM_DWORD = 381;
/*      */   public static final int STOREL0_MEM_QWORD = 382;
/*      */   public static final int STORE2_EAX = 383;
/*      */   public static final int STORE2_AX = 384;
/*      */   public static final int STORE2_AH = 385;
/*      */   public static final int STORE2_AL = 386;
/*      */   public static final int STORE2_BX = 387;
/*      */   public static final int STORE2_BH = 388;
/*      */   public static final int STORE2_BL = 389;
/*      */   public static final int STORE2_CX = 390;
/*      */   public static final int STORE2_CL = 391;
/*      */   public static final int STORE2_CH = 392;
/*      */   public static final int STORE2_DL = 393;
/*      */   public static final int STORE2_DH = 394;
/*      */   public static final int STORE2_DX = 395;
/*      */   public static final int STORE2_EBX = 396;
/*      */   public static final int STORE2_ECX = 397;
/*      */   public static final int STORE2_EDX = 398;
/*      */   public static final int STORE2_ESI = 399;
/*      */   public static final int STORE2_SI = 400;
/*      */   public static final int STORE2_EDI = 401;
/*      */   public static final int STORE2_DI = 402;
/*      */   public static final int STORE2_ESP = 403;
/*      */   public static final int STORE2_EBP = 404;
/*      */   public static final int STORE2_BP = 405;
/*      */   public static final int STORE2_MEM_DWORD = 406;
/*      */   public static final int STORE2_MEM_WORD = 407;
/*      */   public static final int STORE2_MEM_BYTE = 408;
/*      */   public static final int STORE3_AH = 409;
/*      */   public static final int STORE3_DX = 410;
/*      */   public static final int STORE3_EDX = 411;
/*      */   public static final int STOREF0_MEM_DWORD = 412;
/*      */   public static final int STOREF0_MEM_QWORD = 413;
/*      */   public static final int STOREF0_MEM_TBYTE = 414;
/*      */   public static final int STOREF0_ST = 415;
/*      */   public static final int STOREF0_ST0 = 416;
/*      */   public static final int STOREF0_ST1 = 417;
/*      */   public static final int STOREF0_ST2 = 418;
/*      */   public static final int STOREF0_ST3 = 419;
/*      */   public static final int STOREF0_ST4 = 420;
/*      */   public static final int STOREF0_ST5 = 421;
/*      */   public static final int STOREF0_ST6 = 422;
/*      */   public static final int STOREF0_ST7 = 423;
/*      */   public static final int STOREF2_ST = 424;
/*      */   public static final int STOREF2_ST0 = 425;
/*      */   public static final int STOREF2_ST1 = 426;
/*      */   public static final int STOREF2_ST2 = 427;
/*      */   public static final int STOREF2_ST3 = 428;
/*      */   public static final int STOREF2_ST4 = 429;
/*      */   public static final int STOREF2_ST5 = 430;
/*      */   public static final int STOREF2_ST6 = 431;
/*      */   public static final int STOREF2_ST7 = 432;
/*      */   public static final int LOADF0_MEM_DWORD = 433;
/*      */   public static final int LOADF0_MEM_QWORD = 434;
/*      */   public static final int LOADF0_MEM_TBYTE = 435;
/*      */   public static final int LOADF1_MEM_DWORD = 436;
/*      */   public static final int LOADF1_MEM_QWORD = 437;
/*      */   public static final int LOADF0_ST = 438;
/*      */   public static final int LOADF0_ST0 = 439;
/*      */   public static final int LOADF0_ST1 = 440;
/*      */   public static final int LOADF0_ST2 = 441;
/*      */   public static final int LOADF0_ST3 = 442;
/*      */   public static final int LOADF0_ST4 = 443;
/*      */   public static final int LOADF0_ST5 = 444;
/*      */   public static final int LOADF0_ST6 = 445;
/*      */   public static final int LOADF0_ST7 = 446;
/*      */   public static final int LOADF1_ST = 447;
/*      */   public static final int LOADF1_ST0 = 448;
/*      */   public static final int LOADF1_ST1 = 449;
/*      */   public static final int LOADF1_ST2 = 450;
/*      */   public static final int LOADF1_ST3 = 451;
/*      */   public static final int LOADF1_ST4 = 452;
/*      */   public static final int LOADF1_ST5 = 453;
/*      */   public static final int LOADF1_ST6 = 454;
/*      */   public static final int LOADF1_ST7 = 455;
/*      */   public static final int ADDR2 = 456;
/*      */   public static final int HLT = 457;
/*      */   public static final int RESTORE_STATE = 458;
/*      */   public static final int IMMEDIATE = 459;
/*      */   public static final int DISPLACEMENT = 460;
/*      */   public static final int LOAD0 = 461;
/*      */   public static final int LOADL0 = 462;
/*      */   public static final int STOREL0 = 463;
/*      */   public static final int LOAD0_2 = 464;
/*      */   public static final int LOAD1 = 465;
/*      */   public static final int LOAD1_0 = 466;
/*      */   public static final int LOAD2 = 467;
/*      */   public static final int LOAD3 = 468;
/*      */   public static final int ADDR2_0 = 469;
/*      */   public static final int STORE0 = 470;
/*      */   public static final int STORE0_1 = 471;
/*      */   public static final int STORE1_0 = 472;
/*      */   public static final int STORE1_1 = 473;
/*      */   public static final int STORE2 = 474;
/*      */   public static final int LOADF0 = 475;
/*      */   public static final int LOADF0_1 = 476;
/*      */   public static final int LOADF1 = 477;
/*      */   public static final int LOADF1_0 = 478;
/*      */   public static final int STOREF0 = 479;
/*      */   public static final int STOREF0_1 = 480;
/*      */   public static final int STOREF2 = 481;
/*      */   public static final int LOCK = 482;
/*      */   public static final int UNLOCK = 483;
/*      */   public static final int BSWAP = 484;
/*      */   public static final int CWDE = 485;
/*      */   public static final int UNKNOWN_OPCODE = 486;
/*      */   public static final int UNKNOWN_OPERAND = 487;
/*  532 */   private static HashMap ucodeCache = new HashMap();
/*      */   
/*  534 */   private static final HashMap index = new HashMap();
/*  535 */   private static final HashMap reverseIndex = new HashMap();
/*      */   private static final HashMap snippetIndex;
/*      */   
/*      */   public static Collection<Integer> getUCodeSet() {
/*  539 */     return index.values();
/*      */   }
/*      */   
/*      */   static
/*      */   {
/*  544 */     Class cls = UCodes.class;
/*  545 */     Field[] flds = cls.getDeclaredFields();
/*      */     
/*  547 */     for (int i = 0; i < flds.length; i++)
/*      */     {
/*      */       try
/*      */       {
/*  551 */         if (flds[i].getType() == Integer.TYPE)
/*      */         {
/*  553 */           int mods = flds[i].getModifiers();
/*  554 */           if ((!Modifier.isStatic(mods)) || (Modifier.isFinal(mods)))
/*      */           {
/*      */ 
/*  557 */             String name = flds[i].getName();
/*  558 */             int value = flds[i].getInt(null);
/*  559 */             Integer val = Integer.valueOf(value);
/*      */             
/*  561 */             index.put(name, val);
/*  562 */             reverseIndex.put(val, name);
/*      */           }
/*      */         }
/*      */       } catch (Exception e) {
/*  566 */         e.printStackTrace();
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*  571 */     snippetIndex = new HashMap();
/*      */     
/*      */ 
/*  574 */     snippetIndex.put("MOV", new int[] { 467, 474 });
/*  575 */     snippetIndex.put("MOVS_BYTE_PTR_ES:[EDI]_BYTE_PTR_DS:[ESI]", new int[] { 13 });
/*  576 */     snippetIndex.put("MOVS_WORD_PTR_ES:[EDI]_WORD_PTR_DS:[ESI]", new int[] { 14 });
/*  577 */     snippetIndex.put("MOVZX32_8", new int[] { 467, 6, 474 });
/*  578 */     snippetIndex.put("MOVZX32_16", new int[] { 467, 7, 474 });
/*      */     
/*  580 */     snippetIndex.put("MOVZX_EDX_WORD_PTR_[EAX]", new int[] { 467, 7, 474 });
/*  581 */     snippetIndex.put("MOVZX_EAX_WORD_PTR_DS$DDDD", new int[] { 467, 7, 474 });
/*  582 */     snippetIndex.put("MOVZX_EDX_WORD_PTR_[EBP$D]", new int[] { 467, 7, 474 });
/*  583 */     snippetIndex.put("MOVZX_EDX_WORD_PTR_[ESI]", new int[] { 467, 7, 474 });
/*      */     
/*  585 */     snippetIndex.put("MOVSX32_8", new int[] { 467, 8, 474 });
/*  586 */     snippetIndex.put("MOVSX32_16", new int[] { 467, 9, 474 });
/*  587 */     snippetIndex.put("CMOVNE", new int[] { 461, 465, 15, 474 });
/*  588 */     snippetIndex.put("CMOVAE", new int[] { 461, 465, 26, 474 });
/*  589 */     snippetIndex.put("CMOVA", new int[] { 461, 465, 25, 474 });
/*  590 */     snippetIndex.put("CMOVE", new int[] { 461, 465, 18, 474 });
/*  591 */     snippetIndex.put("CMOVGE", new int[] { 461, 465, 19, 474 });
/*  592 */     snippetIndex.put("CMOVLE", new int[] { 461, 465, 20, 474 });
/*  593 */     snippetIndex.put("CMOVL", new int[] { 461, 465, 21, 474 });
/*  594 */     snippetIndex.put("CMOVS", new int[] { 461, 465, 16, 474 });
/*  595 */     snippetIndex.put("CMOVNS", new int[] { 461, 465, 17, 474 });
/*  596 */     snippetIndex.put("CMOVB", new int[] { 461, 465, 22, 474 });
/*  597 */     snippetIndex.put("CMOVBE", new int[] { 461, 465, 24, 474 });
/*  598 */     snippetIndex.put("CMOVG", new int[] { 461, 465, 23, 474 });
/*  599 */     snippetIndex.put("CMOVO", new int[] { 461, 465, 27, 474 });
/*  600 */     snippetIndex.put("CMOVNO", new int[] { 461, 465, 28, 474 });
/*  601 */     snippetIndex.put("CMOVP", new int[] { 461, 465, 29, 474 });
/*  602 */     snippetIndex.put("CMOVNP", new int[] { 461, 465, 30, 474 });
/*      */     
/*  604 */     snippetIndex.put("XCHG", new int[] { 461, 465, 11, 470, 473 });
/*  605 */     snippetIndex.put("XADD", new int[] { 461, 465, 12, 470, 473 });
/*      */     
/*  607 */     snippetIndex.put("BSF", new int[] { 461, 465, 45, 474 });
/*  608 */     snippetIndex.put("BSR", new int[] { 461, 465, 44, 474 });
/*  609 */     snippetIndex.put("BT16", new int[] { 461, 465, 38 });
/*  610 */     snippetIndex.put("BT32", new int[] { 461, 465, 39 });
/*  611 */     snippetIndex.put("BTS16", new int[] { 461, 465, 38, 474 });
/*  612 */     snippetIndex.put("BTS32", new int[] { 461, 465, 39, 474 });
/*  613 */     snippetIndex.put("BTC16", new int[] { 461, 465, 40, 474 });
/*  614 */     snippetIndex.put("BTC32", new int[] { 461, 465, 41, 474 });
/*  615 */     snippetIndex.put("BTR16", new int[] { 461, 465, 42, 474 });
/*  616 */     snippetIndex.put("BTR32", new int[] { 461, 465, 43, 474 });
/*  617 */     snippetIndex.put("PUSH", new int[] { 461, 229 });
/*  618 */     snippetIndex.put("NEG8", new int[] { 461, 142, 474, 184, 142 });
/*  619 */     snippetIndex.put("NEG16", new int[] { 461, 143, 474, 184, 143 });
/*  620 */     snippetIndex.put("NEG32", new int[] { 461, 144, 474, 184, 144 });
/*  621 */     snippetIndex.put("ADD8", new int[] { 461, 465, 128, 474, 184, 128 });
/*  622 */     snippetIndex.put("ADD16", new int[] { 461, 465, 129, 474, 184, 129 });
/*  623 */     snippetIndex.put("ADD32", new int[] { 461, 465, 130, 474, 184, 130 });
/*  624 */     snippetIndex.put("ADC8", new int[] { 461, 465, 131, 474, 184, 131 });
/*  625 */     snippetIndex.put("ADC16", new int[] { 461, 465, 132, 474, 184, 132 });
/*  626 */     snippetIndex.put("ADC32", new int[] { 461, 465, 133, 474, 184, 133 });
/*  627 */     snippetIndex.put("INC8", new int[] { 461, 310, 1, 128, 474, 188, 128 });
/*  628 */     snippetIndex.put("INC16", new int[] { 461, 310, 1, 129, 474, 188, 129 });
/*  629 */     snippetIndex.put("INC32", new int[] { 461, 310, 1, 130, 474, 188, 130 });
/*  630 */     snippetIndex.put("DEC8", new int[] { 461, 310, 1, 134, 474, 188, 134 });
/*  631 */     snippetIndex.put("DEC16", new int[] { 461, 310, 1, 135, 474, 188, 135 });
/*  632 */     snippetIndex.put("DEC32", new int[] { 461, 310, 1, 136, 474, 188, 136 });
/*  633 */     snippetIndex.put("SUB8", new int[] { 461, 465, 134, 474, 184, 134 });
/*  634 */     snippetIndex.put("SUB16", new int[] { 461, 465, 135, 474, 184, 135 });
/*  635 */     snippetIndex.put("SUB32", new int[] { 461, 465, 136, 474, 184, 136 });
/*  636 */     snippetIndex.put("SBB8", new int[] { 461, 465, 137, 474, 184, 137 });
/*  637 */     snippetIndex.put("SBB16", new int[] { 461, 465, 138, 474, 184, 138 });
/*  638 */     snippetIndex.put("SBB32", new int[] { 461, 465, 139, 474, 184, 139 });
/*  639 */     snippetIndex.put("CMP8", new int[] { 461, 465, 134, 184, 134 });
/*  640 */     snippetIndex.put("CMP16", new int[] { 461, 465, 135, 184, 135 });
/*  641 */     snippetIndex.put("CMP32", new int[] { 461, 465, 136, 184, 136 });
/*  642 */     snippetIndex.put("CMPXCHG", new int[] { 281, 466, 136, 184, 136, 467, 10, 471, 474 });
/*  643 */     snippetIndex.put("SHR", new int[] { 461, 465, 125, 474, 186, 125 });
/*  644 */     snippetIndex.put("SHRD16", new int[] { 461, 465, 468, 126, 474, 187, 126 });
/*  645 */     snippetIndex.put("SHRD32", new int[] { 461, 465, 468, 127, 474, 187, 127 });
/*  646 */     snippetIndex.put("ROL8", new int[] { 461, 465, 113, 474 });
/*  647 */     snippetIndex.put("ROL16", new int[] { 461, 465, 114, 474 });
/*  648 */     snippetIndex.put("ROL32", new int[] { 461, 465, 115, 474 });
/*  649 */     snippetIndex.put("ROR8", new int[] { 461, 465, 116, 474 });
/*  650 */     snippetIndex.put("ROR16", new int[] { 461, 465, 117, 474 });
/*  651 */     snippetIndex.put("ROR32", new int[] { 461, 465, 118, 474 });
/*  652 */     snippetIndex.put("RCR8", new int[] { 461, 465, 119, 474 });
/*  653 */     snippetIndex.put("RCR16", new int[] { 461, 465, 120, 474 });
/*  654 */     snippetIndex.put("RCR32", new int[] { 461, 465, 121, 474 });
/*  655 */     snippetIndex.put("RCL8", new int[] { 461, 465, 122, 474 });
/*  656 */     snippetIndex.put("RCL16", new int[] { 461, 465, 123, 474 });
/*  657 */     snippetIndex.put("RCL32", new int[] { 461, 465, 124, 474 });
/*      */     
/*      */ 
/*  660 */     snippetIndex.put("AND32", new int[] { 461, 465, 2, 474, 190 });
/*  661 */     snippetIndex.put("TEST32", new int[] { 461, 465, 2, 190 });
/*  662 */     snippetIndex.put("XOR32", new int[] { 461, 465, 5, 474, 190 });
/*  663 */     snippetIndex.put("OR32", new int[] { 461, 465, 3, 474, 190 });
/*  664 */     snippetIndex.put("AND16", new int[] { 461, 465, 2, 474, 191 });
/*  665 */     snippetIndex.put("TEST16", new int[] { 461, 465, 2, 191 });
/*  666 */     snippetIndex.put("XOR16", new int[] { 461, 465, 5, 474, 191 });
/*  667 */     snippetIndex.put("OR16", new int[] { 461, 465, 3, 474, 191 });
/*  668 */     snippetIndex.put("AND8", new int[] { 461, 465, 2, 474, 192 });
/*  669 */     snippetIndex.put("TEST8", new int[] { 461, 465, 2, 192 });
/*  670 */     snippetIndex.put("XOR8", new int[] { 461, 465, 5, 474, 192 });
/*  671 */     snippetIndex.put("OR8", new int[] { 461, 465, 3, 474, 192 });
/*      */     
/*  673 */     snippetIndex.put("NOT", new int[] { 461, 4, 474 });
/*  674 */     snippetIndex.put("SAR8", new int[] { 461, 465, 145, 474, 186, 145 });
/*  675 */     snippetIndex.put("SAR16", new int[] { 461, 465, 146, 474, 186, 146 });
/*  676 */     snippetIndex.put("SAR32", new int[] { 461, 465, 147, 474, 186, 147 });
/*  677 */     snippetIndex.put("SHL8", new int[] { 461, 465, 148, 474, 186, 148 });
/*  678 */     snippetIndex.put("SHL16", new int[] { 461, 465, 149, 474, 186, 149 });
/*  679 */     snippetIndex.put("SHL32", new int[] { 461, 465, 150, 474, 186, 150 });
/*  680 */     snippetIndex.put("SHLD16", new int[] { 461, 465, 468, 151, 474, 187, 151 });
/*  681 */     snippetIndex.put("SHLD32", new int[] { 461, 465, 468, 152, 474, 187, 152 });
/*  682 */     snippetIndex.put("IMUL16[3]", new int[] { 465, 464, 154, 474, 184, 154 });
/*  683 */     snippetIndex.put("IMUL32[3]", new int[] { 465, 464, 155, 474, 185 });
/*  684 */     snippetIndex.put("IMUL16[2]", new int[] { 461, 465, 154, 474, 184, 154 });
/*  685 */     snippetIndex.put("IMUL32[2]", new int[] { 461, 465, 155, 474, 185 });
/*  686 */     snippetIndex.put("IMUL8[1]", new int[] { 321, 461, 153, 384, 184, 153 });
/*  687 */     snippetIndex.put("IMUL16[1]", new int[] { 319, 461, 154, 384, 410, 184, 154 });
/*  688 */     snippetIndex.put("IMUL32[1]", new int[] { 311, 461, 155, 383, 411, 185 });
/*  689 */     snippetIndex.put("MUL8", new int[] { 461, 156, 189 });
/*  690 */     snippetIndex.put("MUL16", new int[] { 461, 157, 189 });
/*  691 */     snippetIndex.put("MUL32", new int[] { 461, 158, 189 });
/*  692 */     snippetIndex.put("DIV8", new int[] { 461, 159, 386, 409 });
/*  693 */     snippetIndex.put("DIV16", new int[] { 461, 160, 384, 410 });
/*  694 */     snippetIndex.put("DIV32", new int[] { 461, 161, 383, 411 });
/*  695 */     snippetIndex.put("IDIV8", new int[] { 461, 162, 386, 409 });
/*  696 */     snippetIndex.put("IDIV16", new int[] { 461, 163, 384, 410 });
/*  697 */     snippetIndex.put("IDIV32", new int[] { 461, 164, 383, 411 });
/*  698 */     snippetIndex.put("LEA", new int[] { 456, 474, 279 });
/*  699 */     snippetIndex.put("SETNE", new int[] { 165, 474 });
/*  700 */     snippetIndex.put("SETE", new int[] { 166, 474 });
/*  701 */     snippetIndex.put("SETGE", new int[] { 167, 474 });
/*  702 */     snippetIndex.put("SETG", new int[] { 168, 474 });
/*  703 */     snippetIndex.put("SETLE", new int[] { 169, 474 });
/*  704 */     snippetIndex.put("SETL", new int[] { 170, 474 });
/*  705 */     snippetIndex.put("SETA", new int[] { 171, 474 });
/*  706 */     snippetIndex.put("SETAE", new int[] { 172, 474 });
/*  707 */     snippetIndex.put("SETB", new int[] { 173, 474 });
/*  708 */     snippetIndex.put("SETBE", new int[] { 174, 474 });
/*  709 */     snippetIndex.put("SETO", new int[] { 175, 474 });
/*  710 */     snippetIndex.put("SETNO", new int[] { 176, 474 });
/*  711 */     snippetIndex.put("SETNP", new int[] { 177, 474 });
/*  712 */     snippetIndex.put("SETP", new int[] { 178, 474 });
/*  713 */     snippetIndex.put("SETS", new int[] { 179, 474 });
/*  714 */     snippetIndex.put("SETNS", new int[] { 180, 474 });
/*  715 */     snippetIndex.put("SAHF", new int[] { 183 });
/*  716 */     snippetIndex.put("RDTSC", new int[] { 47, 398, 365 });
/*  717 */     snippetIndex.put("POPF", new int[] { 48 });
/*  718 */     snippetIndex.put("PUSHF", new int[] { 49 });
/*      */     
/*      */ 
/*  721 */     snippetIndex.put("FLDENV", new int[] { 461, 102 });
/*  722 */     snippetIndex.put("FLDENVW", new int[] { 461, 102 });
/*  723 */     snippetIndex.put("FSTENV", new int[] { 461, 101 });
/*  724 */     snippetIndex.put("FNSTENV", new int[] { 461, 101 });
/*  725 */     snippetIndex.put("FNSTENVW", new int[] { 461, 101 });
/*  726 */     snippetIndex.put("FNSAVEW", new int[] { 461, 103 });
/*  727 */     snippetIndex.put("FNSAVE", new int[] { 461, 103 });
/*  728 */     snippetIndex.put("FRSTORW", new int[] { 461, 104 });
/*  729 */     snippetIndex.put("FRSTOR", new int[] { 461, 104 });
/*  730 */     snippetIndex.put("FFREE", new int[0]);
/*  731 */     snippetIndex.put("FINIT", new int[] { 110 });
/*  732 */     snippetIndex.put("FSTCW", new int[] { 52, 474 });
/*  733 */     snippetIndex.put("FSTSW", new int[] { 51, 474 });
/*  734 */     snippetIndex.put("FLDCW", new int[] { 461, 53 });
/*  735 */     snippetIndex.put("FXAM", new int[] { 55 });
/*  736 */     snippetIndex.put("FABS", new int[] { 77 });
/*  737 */     snippetIndex.put("FCHS", new int[] { 78 });
/*  738 */     snippetIndex.put("FSQRT", new int[] { 79 });
/*  739 */     snippetIndex.put("FSCALE", new int[] { 80 });
/*  740 */     snippetIndex.put("F2XM1", new int[] { 81 });
/*  741 */     snippetIndex.put("FRNDINT", new int[] { 82 });
/*  742 */     snippetIndex.put("FSINCOS", new int[] { 83 });
/*  743 */     snippetIndex.put("FCOS", new int[] { 85 });
/*  744 */     snippetIndex.put("FPTAN", new int[] { 86 });
/*  745 */     snippetIndex.put("FPATAN", new int[] { 87 });
/*  746 */     snippetIndex.put("FPREM", new int[] { 88 });
/*  747 */     snippetIndex.put("FPREM1", new int[] { 88 });
/*  748 */     snippetIndex.put("FYL2XP1", new int[] { 91, 417, 50 });
/*  749 */     snippetIndex.put("FYL2X", new int[] { 92, 417, 50 });
/*      */     
/*  751 */     snippetIndex.put("FCMOVE", new int[] { 475, 477, 93, 481 });
/*  752 */     snippetIndex.put("FCMOVNE", new int[] { 475, 477, 94, 481 });
/*  753 */     snippetIndex.put("FCMOVB", new int[] { 475, 477, 95, 481 });
/*  754 */     snippetIndex.put("FCMOVNB", new int[] { 475, 477, 96, 481 });
/*  755 */     snippetIndex.put("FCMOVBE", new int[] { 475, 477, 97, 481 });
/*  756 */     snippetIndex.put("FCMOVNBE", new int[] { 475, 477, 98, 481 });
/*  757 */     snippetIndex.put("FCMOVU", new int[] { 475, 477, 99, 481 });
/*  758 */     snippetIndex.put("FCMOVNU", new int[] { 475, 477, 100, 481 });
/*  759 */     snippetIndex.put("FST", new int[] { 57, 479 });
/*  760 */     snippetIndex.put("FSTP", new int[] { 438, 479, 58 });
/*  761 */     snippetIndex.put("FSTP_ST(0)", new int[] { 438, 479, 58 });
/*  762 */     snippetIndex.put("FIST16", new int[] { 439, 105, 474 });
/*  763 */     snippetIndex.put("FIST32", new int[] { 439, 106, 474 });
/*  764 */     snippetIndex.put("FISTP32", new int[] { 439, 106, 474, 58 });
/*  765 */     snippetIndex.put("FIST64", new int[] { 439, 107, 463 });
/*  766 */     snippetIndex.put("FISTP64", new int[] { 439, 107, 463, 58 });
/*  767 */     snippetIndex.put("FBSTP", new int[] { 439, 469, 108, 58, 279 });
/*  768 */     snippetIndex.put("FBLD", new int[] { 469, 109, 279 });
/*  769 */     snippetIndex.put("FLD", new int[] { 475, 59 });
/*  770 */     snippetIndex.put("FILD", new int[] { 462, 73, 59 });
/*  771 */     snippetIndex.put("FDIV", new int[] { 475, 477, 89, 481 });
/*  772 */     snippetIndex.put("FDIVP", new int[] { 475, 477, 89, 481, 50 });
/*  773 */     snippetIndex.put("FDIV_MEM", new int[] { 439, 478, 89, 425 });
/*      */     
/*  775 */     snippetIndex.put("FDIVR", new int[] { 475, 477, 90, 481 });
/*  776 */     snippetIndex.put("FDIVR_MEM", new int[] { 439, 478, 90, 425 });
/*  777 */     snippetIndex.put("FDIVRP", new int[] { 475, 477, 90, 481, 50 });
/*      */     
/*  779 */     snippetIndex.put("FMUL_MEM", new int[] { 438, 478, 74, 425 });
/*  780 */     snippetIndex.put("FMULP_MEM", new int[] { 475, 477, 74, 481, 50 });
/*  781 */     snippetIndex.put("FMULP", new int[] { 475, 477, 74, 481, 50 });
/*  782 */     snippetIndex.put("FMUL", new int[] { 475, 477, 74, 481 });
/*      */     
/*  784 */     snippetIndex.put("FADDP", new int[] { 475, 477, 75, 481, 50 });
/*  785 */     snippetIndex.put("FADD", new int[] { 475, 477, 75, 481 });
/*  786 */     snippetIndex.put("FADD_MEM", new int[] { 475, 448, 75, 425 });
/*      */     
/*  788 */     snippetIndex.put("FSUB", new int[] { 475, 477, 76, 481 });
/*  789 */     snippetIndex.put("FSUB_MEM", new int[] { 439, 478, 76, 425 });
/*  790 */     snippetIndex.put("FSUBP", new int[] { 475, 477, 76, 481, 50 });
/*  791 */     snippetIndex.put("FSUBR_MEM", new int[] { 475, 448, 76, 425 });
/*  792 */     snippetIndex.put("FSUBR", new int[] { 476, 478, 76, 481 });
/*  793 */     snippetIndex.put("FSUBRP", new int[] { 476, 478, 76, 481, 50 });
/*      */     
/*  795 */     snippetIndex.put("FCOM", new int[] { 439, 478, 54 });
/*  796 */     snippetIndex.put("FCOMI", new int[] { 475, 477, 54, 72 });
/*  797 */     snippetIndex.put("FCOMP", new int[] { 439, 478, 54, 50 });
/*  798 */     snippetIndex.put("FUCOM", new int[] { 439, 478, 54 });
/*  799 */     snippetIndex.put("FUCOMP", new int[] { 439, 449, 54, 50 });
/*  800 */     snippetIndex.put("FUCOMPP", new int[] { 439, 449, 54, 50, 50 });
/*  801 */     snippetIndex.put("FUCOMI", new int[] { 475, 477, 72 });
/*  802 */     snippetIndex.put("FUCOMIP", new int[] { 475, 477, 72, 50 });
/*  803 */     snippetIndex.put("FLD1", new int[] { 60 });
/*  804 */     snippetIndex.put("FLDPI", new int[] { 61 });
/*  805 */     snippetIndex.put("FLDLN2", new int[] { 62 });
/*  806 */     snippetIndex.put("FLDL2E", new int[] { 63 });
/*  807 */     snippetIndex.put("FLDL2T", new int[] { 64 });
/*  808 */     snippetIndex.put("FLDLG2", new int[] { 65 });
/*  809 */     snippetIndex.put("FXCH_ST(1)", new int[] { 66 });
/*  810 */     snippetIndex.put("FXCH_ST(2)", new int[] { 67 });
/*  811 */     snippetIndex.put("FXCH_ST(3)", new int[] { 68 });
/*  812 */     snippetIndex.put("FXCH_ST(4)", new int[] { 69 });
/*  813 */     snippetIndex.put("FXCH_ST(5)", new int[] { 70 });
/*  814 */     snippetIndex.put("FXCH_ST(6)", new int[] { 71 });
/*  815 */     snippetIndex.put("FWAIT", new int[0]);
/*      */     
/*      */ 
/*  818 */     snippetIndex.put("REP_MOVS_DWORD_PTR_ES:[EDI]_DWORD_PTR_DS:[ESI]", new int[] { 31 });
/*  819 */     snippetIndex.put("REP_MOVS_BYTE_PTR_ES:[EDI]_BYTE_PTR_DS:[ESI]", new int[] { 32 });
/*  820 */     snippetIndex.put("REPZ_CMPS_BYTE_PTR_DS:[ESI]_BYTE_PTR_ES:[EDI]", new int[] { 33, 184, 134 });
/*  821 */     snippetIndex.put("REP_STOS_DWORD_PTR_ES:[EDI]_EAX", new int[] { 465, 35 });
/*  822 */     snippetIndex.put("REP_STOS_BYTE_PTR_ES:[EDI]_AL", new int[] { 465, 36 });
/*  823 */     snippetIndex.put("STOS_BYTE_PTR_ES:[EDI]_AL", new int[] { 465, 37 });
/*  824 */     snippetIndex.put("REPNZ_SCAS_AL_BYTE_PTR_ES:[EDI]", new int[] { 461, 34, 184, 134 });
/*      */     
/*  826 */     snippetIndex.put("RET_$II", new int[] { 205, 459 });
/*  827 */     snippetIndex.put("REP_RET", new int[] { 204 });
/*  828 */     snippetIndex.put("INT", new int[] { 194, 459 });
/*  829 */     snippetIndex.put("CALL_$IIII", new int[] { 195, 459, 458 });
/*  830 */     snippetIndex.put("CALL_MEM", new int[] { 461, 203, 458 });
/*  831 */     snippetIndex.put("CALL_EAX", new int[] { 200, 458 });
/*  832 */     snippetIndex.put("CALL_EBX", new int[] { 201, 458 });
/*  833 */     snippetIndex.put("CALL_ECX", new int[] { 196, 458 });
/*  834 */     snippetIndex.put("CALL_EDX", new int[] { 197, 458 });
/*  835 */     snippetIndex.put("CALL_ESI", new int[] { 198, 458 });
/*  836 */     snippetIndex.put("CALL_EDI", new int[] { 199, 458 });
/*  837 */     snippetIndex.put("CALL_EBP", new int[] { 202, 458 });
/*      */     
/*      */ 
/*  840 */     snippetIndex.put("JMP_$IIII", new int[] { 206, 459 });
/*  841 */     snippetIndex.put("JMP_$I", new int[] { 206, 459 });
/*  842 */     snippetIndex.put("JMP_MEM", new int[] { 461, 207 });
/*      */     
/*  844 */     snippetIndex.put("JMP_EAX", new int[] { 461, 207 });
/*  845 */     snippetIndex.put("JMP_EDX", new int[] { 461, 207 });
/*  846 */     snippetIndex.put("JMP_ECX", new int[] { 461, 207 });
/*  847 */     snippetIndex.put("JMP_ESI", new int[] { 461, 207 });
/*  848 */     snippetIndex.put("JMP_EBX", new int[] { 461, 207 });
/*  849 */     snippetIndex.put("JMP_EBP", new int[] { 461, 207 });
/*  850 */     snippetIndex.put("JBE", new int[] { 213, 459 });
/*  851 */     snippetIndex.put("JB", new int[] { 212, 459 });
/*  852 */     snippetIndex.put("JAE", new int[] { 211, 459 });
/*  853 */     snippetIndex.put("JA", new int[] { 210, 459 });
/*  854 */     snippetIndex.put("JCXZ", new int[] { 214, 459 });
/*  855 */     snippetIndex.put("JNS", new int[] { 209, 459 });
/*  856 */     snippetIndex.put("JS", new int[] { 208, 459 });
/*  857 */     snippetIndex.put("JNE", new int[] { 216, 459 });
/*  858 */     snippetIndex.put("JE", new int[] { 215, 459 });
/*  859 */     snippetIndex.put("JL", new int[] { 217, 459 });
/*  860 */     snippetIndex.put("JO", new int[] { 219, 459 });
/*  861 */     snippetIndex.put("JNO", new int[] { 220, 459 });
/*  862 */     snippetIndex.put("JP", new int[] { 221, 459 });
/*  863 */     snippetIndex.put("JNP", new int[] { 222, 459 });
/*  864 */     snippetIndex.put("JG", new int[] { 223, 459 });
/*  865 */     snippetIndex.put("JGE", new int[] { 224, 459 });
/*  866 */     snippetIndex.put("JLE", new int[] { 218, 459 });
/*  867 */     snippetIndex.put("JECXZ", new int[] { 225, 459 });
/*  868 */     snippetIndex.put("ADDR16_LOOP_$I", new int[] { 297, 310, 1, 135, 390, 226, 459 });
/*  869 */     snippetIndex.put("LOOP_$I", new int[] { 283, 310, 1, 136, 397, 226, 459 });
/*  870 */     snippetIndex.put("ADDR16_LOOPE_$I", new int[] { 297, 310, 1, 135, 390, 227, 459 });
/*  871 */     snippetIndex.put("LOOPE_$I", new int[] { 283, 310, 1, 136, 397, 227, 459 });
/*  872 */     snippetIndex.put("ADDR16_LOOPNE_$I", new int[] { 297, 310, 1, 135, 390, 228, 459 });
/*  873 */     snippetIndex.put("LOOPNE_$I", new int[] { 283, 310, 1, 136, 397, 228, 459 });
/*  874 */     snippetIndex.put("NOP", new int[0]);
/*      */     
/*  876 */     snippetIndex.put("STMXCSR", new int[] { 111 });
/*  877 */     snippetIndex.put("LDMXCSR", new int[] { 112 });
/*  878 */     snippetIndex.put("BSWAP", new int[] { 461, 484, 470 });
/*      */   }
/*      */   
/*      */   private static void compileUCodesMayBeLock(X86Opcode op)
/*      */   {
/*  883 */     if (op.isLock()) {
/*  884 */       ucodeBuffer[(position++)] = 482;
/*      */     }
/*  886 */     compileUCodes(op);
/*  887 */     if (op.isLock()) {
/*  888 */       ucodeBuffer[(position++)] = 483;
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */   private static void compileUCodes(X86Opcode op)
/*      */   {
/*  895 */     String pattern = op.getPattern();
/*      */     
/*  897 */     String customPattern = pattern.toUpperCase().replace(" ", "_").replace(",", "_");
/*  898 */     Integer customUCode = (Integer)index.get(customPattern);
/*  899 */     int[] snippet = (int[])snippetIndex.get(op.getGeneralizedPattern().toUpperCase().replace(" ", "_").replace(",", "_"));
/*  900 */     if ((snippet == null) && 
/*  901 */       (customUCode != null))
/*      */     {
/*      */ 
/*  904 */       if (snippetIndex.get(customPattern) == null)
/*      */       {
/*  906 */         ucodeBuffer[(position++)] = customUCode.intValue();
/*  907 */         return;
/*      */       }
/*      */     }
/*      */     
/*  911 */     if (snippet == null)
/*  912 */       snippet = (int[])snippetIndex.get(customPattern);
/*  913 */     if (snippet == null)
/*  914 */       snippet = (int[])snippetIndex.get(op.opName().toUpperCase());
/*  915 */     if (snippet == null)
/*  916 */       snippet = (int[])snippetIndex.get(op.opName().toUpperCase() + op.getOperandSize());
/*  917 */     if (snippet == null)
/*  918 */       snippet = (int[])snippetIndex.get(op.opName().toUpperCase() + op.getOperand1Size() + "_" + op.getOperand2Size());
/*  919 */     if (snippet == null)
/*  920 */       snippet = (int[])snippetIndex.get(op.opName().toUpperCase() + "[" + op.getOperandCount() + "]");
/*  921 */     if (snippet == null)
/*  922 */       snippet = (int[])snippetIndex.get(op.opName().toUpperCase() + op.getOperand1Size() + "[" + op.getOperandCount() + "]");
/*  923 */     if (snippet == null)
/*      */     {
/*  925 */       System.err.println("Unknown opcode: " + op.opName().toUpperCase() + " or " + op.opName().toUpperCase() + op.getOperandSize() + "[" + op.getOperandCount() + "]");
/*  926 */       ucodeBuffer[(position++)] = 486;
/*  927 */       throw new IllegalStateException("UNKNOWN OPCODE, pattern: " + pattern + " or " + op.opName().toUpperCase() + op.getOperandSize() + "[" + op.getOperandCount() + "]" + ", general pattern: " + op.getGeneralizedPattern().toUpperCase().replace(" ", "_").replace(",", "_"));
/*      */     }
/*      */     
/*  930 */     String[] operands = new String[0];
/*  931 */     int namePos = pattern.indexOf(op.opName() + " ");
/*  932 */     if (namePos >= 0) {
/*  933 */       operands = pattern.substring(namePos + op.opName().length() + 1).split(",");
/*      */     }
/*      */     try {
/*  936 */       for (int i = 0; i < snippet.length; i++)
/*      */       {
/*  938 */         if (snippet[i] == 461) {
/*  939 */           compileRegOp("LOAD0_", operands[0]);
/*  940 */         } else if (snippet[i] == 465) {
/*  941 */           compileRegOp("LOAD1_", operands[1]);
/*  942 */         } else if (snippet[i] == 466) {
/*  943 */           compileRegOp("LOAD1_", operands[0]);
/*  944 */         } else if (snippet[i] == 467) {
/*  945 */           compileRegOp("LOAD2_", operands[1]);
/*  946 */         } else if (snippet[i] == 470) {
/*  947 */           compileRegOp("STORE0_", operands[0]);
/*  948 */         } else if (snippet[i] == 471) {
/*  949 */           compileRegOp("STORE0_", operands[1]);
/*  950 */         } else if (snippet[i] == 472) {
/*  951 */           compileRegOp("STORE1_", operands[0]);
/*  952 */         } else if (snippet[i] == 473) {
/*  953 */           compileRegOp("STORE1_", operands[1]);
/*  954 */         } else if (snippet[i] == 474) {
/*  955 */           compileRegOp("STORE2_", operands[0]);
/*  956 */         } else if (snippet[i] == 456) {
/*  957 */           compileRegOp("ADDR2", operands[1]);
/*  958 */         } else if (snippet[i] == 469) {
/*  959 */           compileRegOp("ADDR2", operands[0]);
/*  960 */         } else if (snippet[i] == 479) {
/*  961 */           compileRegOp("STOREF0_", operands[0]);
/*  962 */         } else if (snippet[i] == 480) {
/*  963 */           compileRegOp("STOREF0_", operands[1]);
/*  964 */         } else if (snippet[i] == 481) {
/*  965 */           compileRegOp("STOREF2_", operands[0]);
/*  966 */         } else if (snippet[i] == 475) {
/*  967 */           compileRegOp("LOADF0_", operands[0]);
/*  968 */         } else if (snippet[i] == 476) {
/*  969 */           compileRegOp("LOADF0_", operands[1]);
/*  970 */         } else if (snippet[i] == 477) {
/*  971 */           compileRegOp("LOADF1_", operands[1]);
/*  972 */         } else if (snippet[i] == 478) {
/*  973 */           compileRegOp("LOADF1_", operands[0]);
/*  974 */         } else if (snippet[i] == 468) {
/*  975 */           compileRegOp("LOAD3_", operands[2]);
/*  976 */         } else if (snippet[i] == 464) {
/*  977 */           compileRegOp("LOAD0_", operands[2]);
/*  978 */         } else if (snippet[i] == 462) {
/*  979 */           compileRegOp("LOADL0_", operands[0]);
/*  980 */         } else if (snippet[i] == 463) {
/*  981 */           compileRegOp("STOREL0_", operands[0]);
/*      */         } else
/*  983 */           ucodeBuffer[(position++)] = snippet[i];
/*      */       }
/*      */     } catch (RuntimeException e) {
/*  986 */       System.err.println("Failed pattern: " + customPattern);
/*  987 */       throw e;
/*      */     }
/*      */   }
/*      */   
/*  991 */   private static int[] ucodeBuffer = new int[100];
/*  992 */   private static int position = 0;
/*      */   
/*      */   private static String[] getParts(String pattern)
/*      */   {
/*  996 */     int end = pattern.length();
/*  997 */     boolean disp = false;
/*      */     
/*  999 */     if (pattern.contains("$"))
/*      */     {
/* 1001 */       end = pattern.indexOf("$");
/* 1002 */       if (end == 0)
/* 1003 */         return new String[] { "$D" };
/* 1004 */       disp = true;
/*      */     }
/* 1006 */     String regs = pattern.substring(0, end);
/* 1007 */     String[] regSplit = regs.split("\\+");
/* 1008 */     if (disp)
/*      */     {
/* 1010 */       String[] parts = new String[regSplit.length + 1];
/* 1011 */       System.arraycopy(regSplit, 0, parts, 0, regSplit.length);
/* 1012 */       parts[(parts.length - 1)] = "$D";
/* 1013 */       return parts;
/*      */     }
/* 1015 */     return regSplit;
/*      */   }
/*      */   
/*      */   private static void compileRegOp(String prefix, String operand)
/*      */   {
/* 1020 */     String ucodeName = prefix + operand.toUpperCase();
/* 1021 */     operand = operand.toUpperCase();
/* 1022 */     boolean accessesMemory = false;
/* 1023 */     if ((operand.contains("PTR")) || (operand.contains("DS")) || (operand.contains("GS")) || (operand.contains("[")))
/*      */     {
/* 1025 */       accessesMemory = true;
/* 1026 */       String end = prefix + "MEM_DWORD";
/* 1027 */       if (operand.startsWith("DWORD PTR "))
/*      */       {
/* 1029 */         operand = operand.substring(10, operand.length());
/* 1030 */       } else if (operand.startsWith("BYTE PTR "))
/*      */       {
/* 1032 */         operand = operand.substring(9, operand.length());
/* 1033 */         end = prefix + "MEM_BYTE";
/* 1034 */       } else if (operand.startsWith("WORD PTR "))
/*      */       {
/* 1036 */         operand = operand.substring(9, operand.length());
/* 1037 */         end = prefix + "MEM_WORD";
/* 1038 */       } else if (operand.startsWith("QWORD PTR "))
/*      */       {
/* 1040 */         operand = operand.substring(10, operand.length());
/* 1041 */         end = prefix + "MEM_QWORD";
/* 1042 */       } else if (operand.startsWith("TBYTE PTR "))
/*      */       {
/* 1044 */         operand = operand.substring(10, operand.length());
/* 1045 */         end = prefix + "MEM_TBYTE";
/*      */       }
/*      */       String addressPattern;
/* 1048 */       if (operand.startsWith("["))
/*      */       {
/* 1050 */         addressPattern = operand.substring(1, operand.length() - 1);
/* 1051 */       } else if ((operand.startsWith("DS")) || (operand.startsWith("ES")) || (operand.startsWith("GS")))
/*      */       {
/* 1053 */         String addressPattern = operand.substring(2, operand.length());
/* 1054 */         if (addressPattern.startsWith(":"))
/* 1055 */           addressPattern = addressPattern.substring(1);
/* 1056 */         if (addressPattern.startsWith("["))
/* 1057 */           addressPattern = addressPattern.substring(1, addressPattern.length() - 1);
/* 1058 */         if (operand.startsWith("GS"))
/* 1059 */           addressPattern = "GS+" + addressPattern;
/*      */       } else {
/* 1061 */         throw new IllegalStateException("unknown memory operand: " + operand);
/*      */       }
/*      */       String addressPattern;
/* 1064 */       String[] parts = getParts(addressPattern);
/* 1065 */       label625: for (String part : parts)
/*      */       {
/* 1067 */         if (part.contains("*"))
/*      */         {
/* 1069 */           if (part.charAt(part.length() - 1) == '1') {
/* 1070 */             part = part.substring(0, 3);
/*      */           } else {
/* 1072 */             part = part.charAt(part.length() - 1) + part.substring(0, 3);
/*      */           }
/*      */         }
/*      */         try {
/* 1076 */           ucodeBuffer[position] = ((Integer)index.get("ADDR_" + part)).intValue();
/* 1077 */           position += 1;
/*      */         }
/*      */         catch (NullPointerException e) {
/* 1080 */           if (!part.endsWith("EIZ")) break label625; }
/* 1081 */         continue;
/* 1082 */         System.out.println("ERROR: pattern: " + addressPattern + ", couldn't find address microcode ADDR_" + part);
/* 1083 */         System.out.print("Parts[" + parts.length + "]: ");
/* 1084 */         for (String s : parts)
/* 1085 */           System.out.print(s + "\n");
/* 1086 */         throw new IllegalStateException("ERROR: couldn't find address microcode ADDR_" + part);
/*      */         
/* 1088 */         if (part.contains("$")) {
/* 1089 */           ucodeBuffer[(position++)] = 460;
/*      */         }
/*      */       }
/*      */       try
/*      */       {
/* 1094 */         ucodeBuffer[position] = ((Integer)index.get(end)).intValue();
/* 1095 */         position += 1;
/*      */       }
/*      */       catch (NullPointerException e) {
/* 1098 */         if (prefix.equals("ADDR2"))
/*      */         {
/* 1100 */           ucodeBuffer[(position++)] = 456;
/* 1101 */           return;
/*      */         }
/* 1103 */         System.out.println("ERROR: couldn't find microcode " + end);
/* 1104 */         throw new IllegalStateException("ERROR: couldn't find microcode " + end);
/*      */       }
/*      */     }
/*      */     else {
/* 1108 */       if (ucodeName.endsWith("IIII"))
/* 1109 */         ucodeName = ucodeName.substring(0, ucodeName.length() - 3);
/* 1110 */       if (ucodeName.endsWith("$II"))
/* 1111 */         ucodeName = ucodeName.substring(0, ucodeName.length() - 1);
/* 1112 */       if (ucodeName.endsWith("DDDD"))
/* 1113 */         ucodeName = ucodeName.substring(0, ucodeName.length() - 3);
/* 1114 */       if (ucodeName.endsWith("DD")) {
/* 1115 */         ucodeName = ucodeName.substring(0, ucodeName.length() - 1);
/*      */       }
/* 1117 */       ucodeName = ucodeName.replace("(", "").replace(")", "");
/* 1118 */       Integer result = (Integer)index.get(ucodeName);
/* 1119 */       if (result == null)
/*      */       {
/* 1121 */         System.err.println("Unknown operand: " + ucodeName);
/* 1122 */         ucodeBuffer[(position++)] = 487;
/* 1123 */         throw new IllegalStateException("unknown memory operand: " + operand);
/*      */       }
/* 1125 */       ucodeBuffer[(position++)] = result.intValue();
/*      */       
/* 1127 */       if (operand.indexOf("$D") >= 0)
/* 1128 */         ucodeBuffer[(position++)] = 460;
/* 1129 */       if (operand.indexOf("$I") >= 0)
/* 1130 */         ucodeBuffer[(position++)] = 459;
/*      */     }
/* 1132 */     if (accessesMemory) {
/* 1133 */       ucodeBuffer[(position++)] = 279;
/*      */     }
/*      */   }
/*      */   
/*      */   public static int[] decode(X86Opcode op) {
/* 1138 */     String pattern = op.getPattern() + "{" + op.x86Length() + "}";
/*      */     
/* 1140 */     int[] result = (int[])ucodeCache.get(pattern);
/* 1141 */     if (result == null)
/*      */     {
/* 1143 */       position = 0;
/* 1144 */       if (op.isBranch())
/*      */       {
/* 1146 */         ucodeBuffer[(position++)] = 1;
/* 1147 */         ucodeBuffer[(position++)] = op.x86Length();
/* 1148 */         compileUCodesMayBeLock(op);
/*      */       }
/*      */       else {
/* 1151 */         compileUCodesMayBeLock(op);
/* 1152 */         ucodeBuffer[(position++)] = 1;
/* 1153 */         ucodeBuffer[(position++)] = op.x86Length();
/*      */       }
/*      */       
/* 1156 */       result = new int[position];
/* 1157 */       System.arraycopy(ucodeBuffer, 0, result, 0, result.length);
/* 1158 */       ucodeCache.put(pattern, result);
/*      */     }
/*      */     
/* 1161 */     result = Arrays.copyOf(result, result.length);
/*      */     
/* 1163 */     for (int i = 0; i < result.length; i++)
/*      */     {
/* 1165 */       if (result[i] == 460) {
/* 1166 */         result[i] = op.getDisplacement();
/* 1167 */       } else if (result[i] == 459)
/* 1168 */         result[i] = op.getImmediate();
/*      */     }
/* 1170 */     return result;
/*      */   }
/*      */   
/*      */   public static String[] getUCodeStrings(int[] ucodes)
/*      */   {
/* 1175 */     String[] result = new String[ucodes.length];
/* 1176 */     for (int i = 0; i < result.length; i++)
/*      */     {
/* 1178 */       if (i > 0)
/*      */       {
/* 1180 */         if (result[(i - 1)].indexOf("$D") >= 0)
/*      */         {
/* 1182 */           result[i] = Integer.toHexString(ucodes[i]);
/* 1183 */           continue;
/*      */         }
/* 1185 */         if (result[(i - 1)].indexOf("$I") >= 0)
/*      */         {
/* 1187 */           result[i] = Integer.toHexString(ucodes[i]);
/* 1188 */           continue;
/*      */         }
/* 1190 */         if ((ucodes[(i - 1)] == 1) || (ucodes[(i - 1)] == 195) || (result[(i - 1)].startsWith("J")))
/*      */         {
/* 1192 */           result[i] = Integer.toHexString(ucodes[i]);
/* 1193 */           continue;
/*      */         }
/*      */       }
/*      */       
/* 1197 */       String name = (String)reverseIndex.get(Integer.valueOf(ucodes[i]));
/* 1198 */       if (name != null) {
/* 1199 */         result[i] = name;
/*      */       } else {
/* 1201 */         result[i] = ("?" + ucodes[i] + "?");
/*      */       }
/*      */     }
/* 1204 */     return result;
/*      */   }
/*      */   
/*      */   public static boolean isJcc(int ucode)
/*      */   {
/* 1209 */     String name = getName(ucode);
/* 1210 */     if ((name.startsWith("J")) && (!name.startsWith("JMP")))
/* 1211 */       return true;
/* 1212 */     return false;
/*      */   }
/*      */   
/*      */   public static boolean isBranch(int ucode)
/*      */   {
/* 1217 */     String name = getName(ucode);
/* 1218 */     if ((name.startsWith("J")) || (name.startsWith("RET")) || (name.startsWith("CALL")))
/* 1219 */       return true;
/* 1220 */     return false;
/*      */   }
/*      */   
/*      */   public static String getUCodeString(int[] ucodes)
/*      */   {
/* 1225 */     String[] names = getUCodeStrings(ucodes);
/* 1226 */     String result = "";
/* 1227 */     for (int i = 0; i < names.length; i++)
/* 1228 */       result = result + names[i] + " ";
/* 1229 */     return result;
/*      */   }
/*      */   
/*      */   public static Integer getUCode(String ucode)
/*      */   {
/* 1234 */     return (Integer)index.get(ucode);
/*      */   }
/*      */   
/*      */   public static String getName(int ucode)
/*      */   {
/* 1239 */     return (String)reverseIndex.get(Integer.valueOf(ucode));
/*      */   }
/*      */ }


/* Location:              C:\Users\Joey\Downloads\codejar.jar!\com\emt\proteus\decoder\UCodes.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */