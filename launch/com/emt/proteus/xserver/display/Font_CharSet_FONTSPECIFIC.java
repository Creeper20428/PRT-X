/*     */ package com.emt.proteus.xserver.display;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ class Font_CharSet_FONTSPECIFIC
/*     */   implements Font_CharSet
/*     */ {
/*  26 */   private static char[] map = { '\000', '\001', '\002', '\003', '\004', '\005', '\006', '\007', '\b', '\t', '\n', '\013', '\f', '\r', '\016', '\017', '\020', '\021', '\022', '\023', '\024', '\025', '\026', '\027', '\030', '\031', '\032', '\033', '\034', '\035', '\036', '\037', ' ', '!', '∀', '#', '∃', '%', '&', '∍', '(', ')', '*', '+', ',', '-', '.', '/', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', ':', ';', '<', '=', '>', '?', '≅', 'Α', 'Β', 'Χ', 'Δ', 'Ε', 'Φ', 'Γ', 'Η', 'Ι', 'ϑ', 'Κ', 'Λ', 'Μ', 'Ν', 'Ο', 'Π', 'Θ', 'Ρ', 'Σ', 'Τ', 'Υ', 'ς', 'Ω', 'Ξ', 'Ψ', 'Ζ', '[', '∴', ']', '⊥', '_', ' ', 'α', 'β', 'χ', 'δ', 'ε', 'ϕ', 'γ', 'η', 'ι', 'φ', 'κ', 'λ', 'μ', 'ν', 'ο', 'π', 'θ', 'ρ', 'σ', 'τ', 'υ', 'ϖ', 'ω', 'ξ', 'ψ', 'ζ', '{', '|', '}', '~', '', '', '', '', '', '', '', '', '', '', '', '', '', '', '', '', '', '', '', '', '', '', '', '', '', '', '', '', '', '', '', '', '', ' ', 'ϒ', 'ʹ', '≤', '∕', '∞', '∱', '♣', '♦', '♥', '♠', '↔', '←', '↑', '→', '↓', '∘', '±', '"', '≥', '×', '∝', '∂', '∙', '÷', '≠', '≡', '≈', '⋯', '∣', '−', '↲', 'ℵ', 'ℑ', 'ℜ', '℘', '⊗', '⊕', '∅', '⋂', '⋃', 52355, '⊇', '⊄', '⊂', '⊆', '∈', '∉', '∠', '⊽', '®', '©', '™', '∏', '√', '⋅', '¬', '∧', '∨', '⇔', '⇐', '⇑', '⇒', '⇓', '≬', '〈', '®', '©', '™', '∑', ' ', ' ', ' ', ' ', ' ', '⌊', ' ', ' ', ' ', ' ', ' ', '⌨', '∫', '⌈', ' ', ' ', ' ', ' ', ' ', ' ', ' ', '⌋', ' ', ' ', ' ', ' ' };
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  62 */   static int min_byte1 = 0;
/*  63 */   static int max_byte1 = 0;
/*  64 */   static int min_char_or_byte2 = 32;
/*  65 */   static int max_char_or_byte2 = 255;
/*  66 */   static int default_char = 32;
/*  67 */   static String encoding = "unknown";
/*  68 */   static String charset = "fontspecific";
/*     */   
/*  70 */   public int getMinByte1() { return min_byte1; }
/*  71 */   public int getMaxByte1() { return max_byte1; }
/*  72 */   public int getMinCharOrByte2() { return min_char_or_byte2; }
/*  73 */   public int getMaxCharOrByte2() { return max_char_or_byte2; }
/*  74 */   public int getDefaultChar() { return default_char; }
/*  75 */   public String getEncoding() { return encoding; }
/*  76 */   public String getCharset() { return charset; }
/*     */   
/*  78 */   private static String[] _flist = { "-misc-nil-medium-r-normal--2-20-75-75-c-10-misc-fontspecific" };
/*     */   
/*     */ 
/*     */ 
/*  82 */   private static String[] _flist_scalable = { "-misc-nil-medium-r-normal--0-0-75-75-c-0-misc-fontspecific" };
/*     */   
/*     */ 
/*     */ 
/*  86 */   private static String[] _flist_scalable_proportional = { "-adobe-symbol-medium-r-normal--0-0-0-0-p-0-adobe-fontspecific" };
/*     */   
/*     */ 
/*     */ 
/*  90 */   private static String[] _aliases = new String[0];
/*     */   
/*     */   public void init()
/*     */   {
/*  94 */     if (_flist == null) return;
/*  95 */     XFont.addFont(_flist);
/*  96 */     _flist = null;
/*  97 */     XFont.addFont(_flist_scalable);
/*  98 */     _flist_scalable = null;
/*  99 */     XFont.addFont(_flist_scalable_proportional);
/* 100 */     _flist_scalable_proportional = null;
/* 101 */     XFont.addAlias(_aliases);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public int encode(byte[] bbuffer, int start, int len, char[] cbuffer)
/*     */   {
/* 109 */     int i = 0;
/* 110 */     for (int j = 0; 
/* 111 */         i < len; 
/*     */         
/*     */ 
/* 114 */         j++)
/*     */     {
/* 112 */       if (bbuffer[(start + i)] == 0) i++;
/* 113 */       cbuffer[j] = ((char)(bbuffer[(start + i)] & 0xFF));
/* 114 */       i++;
/*     */     }
/* 116 */     len = j;
/* 117 */     for (i = 0; i < len; i++) {
/* 118 */       cbuffer[i] = map[(cbuffer[i] & 0xFFFF)];
/*     */     }
/* 120 */     return len;
/*     */   }
/*     */ }


/* Location:              C:\Users\Joey\Downloads\launch.jar!\com\emt\proteus\xserver\display\Font_CharSet_FONTSPECIFIC.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */