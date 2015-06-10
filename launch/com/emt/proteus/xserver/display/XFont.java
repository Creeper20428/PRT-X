/*      */ package com.emt.proteus.xserver.display;
/*      */ 
/*      */ import com.emt.proteus.xserver.client.XClient;
/*      */ import com.emt.proteus.xserver.io.ComChannel;
/*      */ import java.awt.Font;
/*      */ import java.awt.FontMetrics;
/*      */ import java.io.IOException;
/*      */ import java.io.PrintStream;
/*      */ import java.util.Vector;
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
/*      */ public final class XFont
/*      */   extends Resource
/*      */ {
/*      */   public static XFont dflt;
/*   30 */   public static Vector charSets = new Vector();
/*      */   
/*      */   public byte[] name;
/*      */   public byte[] lfname;
/*   34 */   public String encoding = null;
/*      */   
/*      */   public DDXFont font;
/*      */   
/*      */   public int ascent;
/*      */   public int descent;
/*   40 */   private static String[] _flist = { "-misc-fixed-medium-r-semicondensed--13-120-75-75-c-60-iso8859-1", "-misc-fixed-bold-r-normal--0-0-100-100-c-0-iso8859-1", "-misc-fixed-bold-r-normal--0-0-75-75-c-0-iso8859-1", "-misc-fixed-bold-r-normal--13-100-100-100-c-70-iso8859-1", "-misc-fixed-bold-r-normal--13-100-100-100-c-80-iso8859-1", "-misc-fixed-bold-r-normal--13-120-75-75-c-70-iso8859-1", "-misc-fixed-bold-r-normal--13-120-75-75-c-80-iso8859-1", "-misc-fixed-bold-r-normal--14-130-75-75-c-70-iso8859-1", "-misc-fixed-bold-r-normal--15-120-100-100-c-90-iso8859-1", "-misc-fixed-bold-r-normal--15-140-75-75-c-90-iso8859-1", "-misc-fixed-bold-r-semicondensed--13-100-100-100-c-60-iso8859-1", "-misc-fixed-bold-r-semicondensed--13-120-75-75-c-60-iso8859-1", "-misc-fixed-medium-r-normal--0-0-100-100-c-0-iso8859-1", "-misc-fixed-medium-r-normal--0-0-75-75-c-0-iso8859-1", "-misc-fixed-medium-r-normal--10-100-75-75-c-60-iso8859-1", "-misc-fixed-medium-r-normal--10-70-100-100-c-60-iso8859-1", "-misc-fixed-medium-r-normal--13-100-100-100-c-70-iso8859-1", "-misc-fixed-medium-r-normal--13-100-100-100-c-80-iso8859-1", "-misc-fixed-medium-r-normal--13-120-75-75-c-70-iso8859-1", "-misc-fixed-medium-r-normal--13-120-75-75-c-80-iso8859-1", "-misc-fixed-medium-r-normal--14-110-100-100-c-70-iso8859-1", "-misc-fixed-medium-r-normal--14-130-75-75-c-70-iso8859-1", "-misc-fixed-medium-r-normal--15-120-100-100-c-90-iso8859-1", "-misc-fixed-medium-r-normal--15-140-75-75-c-90-iso8859-1", "-misc-fixed-medium-r-normal--16-150-75-75-c-80-iso8859-1", "-misc-fixed-medium-r-normal--20-140-100-100-c-100-iso8859-1", "-misc-fixed-medium-r-normal--20-200-75-75-c-100-iso8859-1", "-misc-fixed-medium-r-normal--7-50-100-100-c-50-iso8859-1", "-misc-fixed-medium-r-normal--7-70-75-75-c-50-iso8859-1", "-misc-fixed-medium-r-normal--8-60-100-100-c-50-iso8859-1", "-misc-fixed-medium-r-normal--8-80-75-75-c-50-iso8859-1", "-misc-fixed-medium-r-normal--9-80-100-100-c-60-iso8859-1", "-misc-fixed-medium-r-normal--9-90-75-75-c-60-iso8859-1", "-misc-fixed-medium-r-semicondensed--12-110-75-75-c-60-iso8859-1", "-misc-fixed-medium-r-semicondensed--12-90-100-100-c-60-iso8859-1", "-misc-fixed-medium-r-semicondensed--13-100-100-100-c-60-iso8859-1", "-sony-fixed-medium-r-normal--16-120-100-100-c-80-iso8859-1", "-sony-fixed-medium-r-normal--16-150-75-75-c-80-iso8859-1", "-sony-fixed-medium-r-normal--24-170-100-100-c-120-iso8859-1", "-sony-fixed-medium-r-normal--24-230-75-75-c-120-iso8859-1", "-adobe-courier-bold-o-normal--0-0-100-100-m-0-iso8859-1", "-adobe-courier-bold-r-normal--0-0-100-100-m-0-iso8859-1", "-adobe-courier-medium-o-normal--0-0-100-100-m-0-iso8859-1", "-adobe-courier-medium-r-normal--0-0-100-100-m-0-iso8859-1", "-adobe-courier-bold-o-normal--0-0-75-75-m-0-iso8859-1", "-adobe-courier-bold-r-normal--0-0-75-75-m-0-iso8859-1", "-adobe-courier-medium-o-normal--0-0-75-75-m-0-iso8859-1", "-adobe-courier-medium-r-normal--0-0-75-75-m-0-iso8859-1", "-adobe-helvetica-bold-o-normal--0-0-0-0-p-0-iso8859-1", "-adobe-helvetica-bold-r-normal--0-0-0-0-p-0-iso8859-1", "-adobe-helvetica-medium-o-normal--0-0-0-0-p-0-iso8859-1", "-adobe-helvetica-medium-r-normal--0-0-0-0-p-0-iso8859-1", "-adobe-new century schoolbook-bold-i-normal--0-0-0-0-p-0-iso8859-1", "-adobe-new century schoolbook-bold-r-normal--0-0-0-0-p-0-iso8859-1", "-adobe-new century schoolbook-medium-i-normal--0-0-0-0-p-0-iso8859-1", "-adobe-new century schoolbook-medium-r-normal--0-0-0-0-p-0-iso8859-1", "-adobe-times-bold-i-normal--0-0-0-0-p-0-iso8859-1", "-adobe-times-bold-r-normal--0-0-0-0-p-0-iso8859-1", "-adobe-times-medium-i-normal--0-0-0-0-p-0-iso8859-1", "-adobe-times-medium-r-normal--0-0-0-0-p-0-iso8859-1" };
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
/*  135 */   static DDXFont[] flist = null;
/*      */   
/*  137 */   private static String[] _aliases = { "fixed", "-misc-fixed-medium-r-semicondensed--13-120-75-75-c-60-iso8859-1", "7x14", "-misc-fixed-bold-r-normal--14-130-75-75-c-70-iso8859-1", "7x13", "-misc-fixed-medium-r-normal--13-120-75-75-c-70-iso8859-1", "7x13bold", "-misc-fixed-bold-r-normal--13-120-75-75-c-70-iso8859-1", "8x16", "-misc-fixed-medium-r-normal--16-150-75-75-c-80-iso8859-1", "12x24", "-sony-fixed-medium-r-normal--24-170-100-100-c-120-iso8859-1", "a14", "-misc-fixed-medium-r-normal--14-130-75-75-c-70-iso8859-1" };
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  147 */   private static Alias[] aliases = null;
/*      */   
/*      */   static void addFont(String _name) {
/*  150 */     byte[] name = _name.getBytes();
/*  151 */     DDXFont f = null;
/*  152 */     try { f = new DDXFont();f.init(name); } catch (Exception e) {}
/*  153 */     if (f == null) return;
/*  154 */     DDXFont[] tmp = new DDXFont[flist.length + 1];
/*  155 */     System.arraycopy(flist, 0, tmp, 1, flist.length);
/*  156 */     flist = tmp;
/*  157 */     flist[0] = f;
/*      */   }
/*      */   
/*      */   static void addFont(String[] name) {
/*  161 */     Vector v = new Vector();
/*  162 */     for (int i = 0; i < name.length; i++) {
/*      */       try {
/*  164 */         DDXFont f = new DDXFont();
/*  165 */         f.init(name[i].getBytes());
/*  166 */         v.addElement(f);
/*      */       }
/*      */       catch (Exception e) {}
/*      */     }
/*  170 */     if (v.size() == 0) { return;
/*      */     }
/*  172 */     DDXFont[] tmp = null;
/*  173 */     int offset = 0;
/*  174 */     if (flist == null) { tmp = new DDXFont[v.size()];
/*      */     } else {
/*  176 */       tmp = new DDXFont[flist.length + v.size()];
/*  177 */       System.arraycopy(flist, 0, tmp, 0, flist.length);
/*  178 */       offset = flist.length;
/*      */     }
/*  180 */     for (int i = 0; i < v.size(); i++) tmp[(i + offset)] = ((DDXFont)v.elementAt(i));
/*  181 */     flist = tmp;
/*      */   }
/*      */   
/*      */   static void addAlias(String[] name) {
/*  185 */     Vector v = new Vector();
/*  186 */     for (int i = 0; i < name.length; i += 2) {
/*  187 */       byte[] b = name[(i + 1)].getBytes();
/*  188 */       DDXFont fl = null;
/*  189 */       for (int j = 0; j < flist.length; j++) {
/*  190 */         if (match_aux(flist[j].lfname, 0, b, 0)) {
/*  191 */           fl = flist[j];
/*  192 */           break;
/*      */         }
/*      */       }
/*  195 */       if (fl != null)
/*  196 */         v.addElement(new Alias(name[i].getBytes(), fl));
/*      */     }
/*  198 */     if (v.size() == 0) { return;
/*      */     }
/*  200 */     Alias[] tmp = null;
/*  201 */     int offset = 0;
/*  202 */     if (aliases == null) { tmp = new Alias[v.size()];
/*      */     } else {
/*  204 */       tmp = new Alias[aliases.length + v.size()];
/*  205 */       System.arraycopy(aliases, 0, tmp, 0, aliases.length);
/*  206 */       offset = aliases.length;
/*      */     }
/*  208 */     for (int i = 0; i < v.size(); i++) {
/*  209 */       tmp[(i + offset)] = ((Alias)v.elementAt(i));
/*      */     }
/*  211 */     aliases = tmp;
/*      */   }
/*      */   
/*      */   static void addAlias(String name, String font) {
/*  215 */     byte[] b = font.getBytes();
/*  216 */     for (int i = 0; i < flist.length; i++) {
/*  217 */       if (match_aux(flist[i].lfname, 0, b, 0)) {
/*  218 */         if (aliases == null) {
/*  219 */           aliases = new Alias[1];
/*      */         }
/*      */         else {
/*  222 */           Alias[] foo = new Alias[aliases.length + 1];
/*  223 */           System.arraycopy(aliases, 0, foo, 1, aliases.length);
/*  224 */           aliases = foo;
/*      */         }
/*  226 */         aliases[0] = new Alias(name.getBytes(), flist[i]);
/*  227 */         return;
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*  232 */   public static byte[][] fpath = { "/tmp".getBytes() };
/*      */   
/*      */ 
/*      */   public static void init(int id, String charset)
/*      */   {
/*  237 */     if (_flist != null) {
/*  238 */       addFont(_flist);
/*  239 */       _flist = null;
/*      */     }
/*  241 */     if (_aliases != null) {
/*  242 */       addAlias(_aliases);
/*  243 */       _aliases = null;
/*      */     }
/*  245 */     dflt = new XFont(id, "fixed");
/*      */     
/*  247 */     if (charset != null) {
/*  248 */       int start = 0;
/*      */       int end;
/*  250 */       while ((end = charset.indexOf(',', start)) > 0) {
/*  251 */         loadCharSet(charset.substring(start, end));
/*  252 */         start = end + 1;
/*      */       }
/*      */       
/*  255 */       if (start < charset.length()) {
/*  256 */         loadCharSet(charset.substring(start));
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*  261 */     loadCharSet("FONTSPECIFIC");
/*      */   }
/*      */   
/*      */   public static void loadCharSet(String name) {
/*      */     try {
/*  266 */       Class c = Class.forName("com.emt.proteus.xserver.display.Font_CharSet_" + name);
/*      */       
/*  268 */       Object o = c.newInstance();
/*  269 */       charSets.addElement(o);
/*  270 */       ((Font_CharSet)o).init();
/*      */     } catch (Exception e) {}
/*      */   }
/*      */   
/*      */   public Font getFont() {
/*  275 */     return this.font.getFont();
/*      */   }
/*      */   
/*  278 */   public int bytesWidth(byte[] b, int i, int j) { return this.font.metric.bytesWidth(b, i, j); }
/*      */   
/*      */   public int charsWidth(char[] b, int i, int j)
/*      */   {
/*  282 */     return this.font.metric.charsWidth(b, i, j);
/*      */   }
/*      */   
/*      */   private static boolean match_aux(byte[] b1, int i1, byte[] b2, int i2) {
/*  286 */     while ((i1 < b1.length) && (i2 < b2.length) && 
/*  287 */       (b1[i1] == b2[i2])) {
/*  288 */       i1++;i2++;
/*      */     }
/*  290 */     if (i2 == b2.length) return true;
/*  291 */     if (b2[i2] == 42) {
/*  292 */       i2++;
/*  293 */       if (i2 == b2.length) return true;
/*  294 */       if (i1 == b1.length) return false;
/*  295 */       for (int i = i1; i < b1.length; i++) {
/*  296 */         if (b1[i] == b2[i2]) {
/*  297 */           boolean res = match_aux(b1, i, b2, i2);
/*  298 */           if (res) return res;
/*      */         }
/*      */       }
/*  301 */       return false;
/*      */     }
/*  303 */     if (b2[i2] == 63) {
/*  304 */       if (i1 == b1.length) return false;
/*  305 */       i2++;i1++;
/*  306 */       return match_aux(b1, i1, b2, i2);
/*      */     }
/*  308 */     return false;
/*      */   }
/*      */   
/*      */   private static boolean match_scalable(byte[] b1, byte[] b2) {
/*  312 */     int i1 = 0;
/*  313 */     int i2 = 0;
/*  314 */     int i = 0;
/*      */     
/*  316 */     for (int j = 0; j < b2.length; j++) {
/*  317 */       if (b2[j] == 45) i++;
/*      */     }
/*  319 */     if (i != 14) { return false;
/*      */     }
/*  321 */     i = 0;
/*  322 */     for (; (i1 < b1.length) && (i2 < b2.length); 
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
/*      */ 
/*  357 */         i2++)
/*      */     {
/*  323 */       if (b1[i1] == 45) { i++;
/*      */       }
/*      */       
/*  326 */       if (b1[i1] != b2[i2]) {
/*  327 */         if ((b2[i2] == 42) || (b2[i2] == 48)) {
/*  328 */           if (i2 + 1 == b2.length) return true;
/*  329 */           if (b1[i1] == 45) { i--;i1--;
/*      */           } else {
/*  331 */             for (; i1 < b1.length; i1++)
/*  332 */               if (b1[i1] == 45) { i1--; break;
/*      */               }
/*  334 */             if (i1 == b1.length) return false;
/*      */           }
/*      */         }
/*  337 */         else if (b2[i2] == 63) {
/*  338 */           if (i2 + 1 == b2.length) return true;
/*  339 */           if (b1[i1] == 45) return false;
/*  340 */           if ((i1 + 1 >= b1.length) || (b1[(i1 + 1)] != 45)) {
/*  341 */             return false;
/*      */           }
/*      */         }
/*  344 */         else if ((b1[i1] == 48) && ((i == 7) || (i == 8) || (i == 9) || (i == 10) || (i == 11) || (i == 12)))
/*      */         {
/*      */ 
/*  347 */           if ((b2[i2] < 48) || (57 < b2[i2])) return false;
/*  348 */           for (; i2 < b2.length; i2++)
/*  349 */             if (b2[i2] == 45) { i2--; break;
/*      */             }
/*  351 */           if (i2 == b2.length) return false;
/*      */         }
/*      */         else {
/*  354 */           return false;
/*      */         }
/*      */       }
/*  357 */       i1++;
/*      */     }
/*  359 */     return true;
/*      */   }
/*      */   
/*      */   static byte[] genScaleName(byte[] lfname, byte[] name) {
/*  363 */     return genScaleName(lfname, name, 0);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   static byte[] genScaleName(byte[] lfname, byte[] name, int size)
/*      */   {
/*  370 */     if (size == 0) {
/*  371 */       size = getScalableSize(name);
/*  372 */       if (size == 0)
/*      */       {
/*  374 */         size = 14;
/*      */       }
/*      */     }
/*      */     
/*  378 */     StringBuffer tmp = new StringBuffer();
/*  379 */     int i = 0;
/*  380 */     int skip = 7;
/*  381 */     while (i < lfname.length) {
/*  382 */       if (lfname[i] == 45) skip--;
/*  383 */       tmp.append((char)lfname[i]);
/*  384 */       i++;
/*  385 */       if (skip == 0) break;
/*      */     }
/*  387 */     tmp.append(size + "-" + size * 10 + "-");
/*  388 */     skip = 2;
/*  389 */     while (i < lfname.length) {
/*  390 */       if (lfname[i] == 45) skip--;
/*  391 */       i++;
/*  392 */       if (skip == 0) break;
/*      */     }
/*  394 */     skip = 3;
/*  395 */     while (i < lfname.length) {
/*  396 */       if (lfname[i] == 45) skip--;
/*  397 */       tmp.append((char)lfname[i]);
/*  398 */       i++;
/*  399 */       if (skip == 0) break;
/*      */     }
/*  401 */     tmp.append(size + "-");
/*  402 */     skip = 1;
/*  403 */     while (i < lfname.length) {
/*  404 */       if (lfname[i] == 45) skip--;
/*  405 */       i++;
/*  406 */       if (skip == 0) break;
/*      */     }
/*  408 */     while (i < lfname.length) {
/*  409 */       tmp.append((char)lfname[i]);
/*  410 */       i++;
/*      */     }
/*      */     
/*  413 */     return tmp.toString().getBytes();
/*      */   }
/*      */   
/*      */   static int getScalableSize(byte[] name) {
/*  417 */     int size = 0;
/*  418 */     int i = 0;
/*  419 */     int skip = 7;
/*  420 */     while (i < name.length) {
/*  421 */       if (name[i] == 45) skip--;
/*  422 */       i++;
/*  423 */       if (skip == 0) break;
/*      */     }
/*  425 */     if (i >= name.length) return 0;
/*  426 */     if ((48 <= name[i]) && (name[i] <= 57)) {
/*  427 */       while ((i < name.length) && 
/*  428 */         (name[i] != 45)) {
/*  429 */         size = size * 10 + name[i] - 48;
/*  430 */         i++;
/*      */       }
/*      */     }
/*  433 */     if (size == 0) {
/*  434 */       i++;
/*  435 */       skip = 1;
/*  436 */       while (i < name.length) {
/*  437 */         if (name[i] == 45) skip--;
/*  438 */         i++;
/*  439 */         if (skip == 0) break;
/*      */       }
/*  441 */       if (i >= name.length) return 0;
/*  442 */       if ((48 <= name[i]) && (name[i] <= 57)) {
/*  443 */         while ((i < name.length) && 
/*  444 */           (name[i] != 45)) {
/*  445 */           size = size * 10 + name[i] - 48;
/*  446 */           i++;
/*      */         }
/*      */       }
/*  449 */       if (size > 0) size /= 10;
/*      */     }
/*  451 */     if (size == 0) {
/*  452 */       i++;
/*  453 */       skip = 3;
/*  454 */       while (i < name.length) {
/*  455 */         if (name[i] == 45) skip--;
/*  456 */         i++;
/*  457 */         if (skip == 0) break;
/*      */       }
/*  459 */       if (i >= name.length) return 0;
/*  460 */       if ((48 <= name[i]) && (name[i] <= 57)) {
/*  461 */         while ((i < name.length) && 
/*  462 */           (name[i] != 45)) {
/*  463 */           size = size * 10 + name[i] - 48;
/*  464 */           i++;
/*      */         }
/*      */       }
/*      */     }
/*  468 */     return size;
/*      */   }
/*      */   
/*      */   XFont(int id, String name) {
/*  472 */     super(id, 4);
/*  473 */     this.name = name.getBytes();
/*  474 */     if ((name.equals("fixed")) || (name.equals("cursor"))) {
/*  475 */       this.font = flist[0];
/*  476 */       this.lfname = this.font.lfname;
/*      */     }
/*      */     else {
/*  479 */       for (int i = 0; i < aliases.length; i++) {
/*  480 */         if (match_aux(aliases[i].name, 0, this.name, 0)) {
/*  481 */           this.font = aliases[i].font;
/*  482 */           this.lfname = this.font.lfname;
/*      */         }
/*      */       }
/*  485 */       if (this.lfname == null) {
/*  486 */         for (int i = 0; i < flist.length; i++) {
/*  487 */           if (match_aux(flist[i].lfname, 0, this.name, 0)) {
/*  488 */             this.font = flist[i];
/*  489 */             this.lfname = this.font.lfname;
/*  490 */             break;
/*      */           }
/*      */         }
/*      */       }
/*  494 */       if (this.lfname == null) {
/*  495 */         int size = getScalableSize(this.name);
/*  496 */         if (size > 0) {
/*  497 */           for (int i = 0; i < flist.length; i++) {
/*  498 */             if ((flist[i].getScalable()) && 
/*  499 */               (match_scalable(flist[i].lfname, this.name))) {
/*  500 */               this.font = flist[i].getScalableFont(this.name);
/*  501 */               this.lfname = this.font.lfname;
/*  502 */               break;
/*      */             }
/*      */           }
/*      */         }
/*      */       }
/*      */       
/*  508 */       if (this.lfname == null) {
/*  509 */         this.font = flist[0];
/*  510 */         this.lfname = this.font.lfname;
/*      */       }
/*      */     }
/*  513 */     this.font.getFont();
/*  514 */     this.ascent = this.font.getAscent();
/*  515 */     this.descent = this.font.getDescent();
/*  516 */     this.encoding = this.font.encoding;
/*      */   }
/*      */   
/*      */   public static void reqQueryTextExtents(XClient c) throws IOException
/*      */   {
/*  521 */     boolean odd = false;
/*  522 */     ComChannel comChannel = c.channel;
/*  523 */     int foo = c.data;
/*  524 */     if (foo != 0) odd = true;
/*  525 */     int n = c.length;
/*  526 */     foo = comChannel.readInt();
/*  527 */     c.length -= 2;
/*  528 */     XFont f = (XFont)lookupIDByType(foo, 4);
/*  529 */     if (f == null) {
/*  530 */       c.errorValue = foo;
/*  531 */       c.errorReason = 7;
/*  532 */       return;
/*      */     }
/*  534 */     n -= 2;
/*  535 */     n *= 4;
/*  536 */     comChannel.readByte(c.bbuffer, 0, n);
/*      */     
/*  538 */     if (odd) { n -= 2;
/*      */     }
/*  540 */     synchronized (comChannel) {
/*  541 */       comChannel.writeByte(1);
/*  542 */       if (f.encoding != null) {
/*  543 */         for (int i = 0; i < n; i++) {
/*  544 */           int tmp128_126 = i; byte[] tmp128_123 = c.bbuffer;tmp128_123[tmp128_126] = ((byte)(tmp128_123[tmp128_126] | 0x80));
/*      */         }
/*      */         try {
/*  547 */           String s = new String(c.bbuffer, 0, n, f.encoding);
/*  548 */           n = s.length();
/*  549 */           s.getChars(0, n, c.cbuffer, 0);
/*      */         }
/*      */         catch (Exception e) {
/*  552 */           System.out.println(e);
/*  553 */           return;
/*      */         }
/*      */       }
/*      */       else {
/*  557 */         for (int i = 0; i < n; i++) {
/*  558 */           c.cbuffer[i] = ((char)c.bbuffer[i]);
/*      */         }
/*      */       }
/*      */       
/*  562 */       foo = f.charsWidth(c.cbuffer, 0, n);
/*      */       
/*  564 */       comChannel.writeByte((byte)0);
/*  565 */       comChannel.writeShort(c.getSequence());
/*  566 */       comChannel.writeInt(0);
/*  567 */       comChannel.writeShort(f.font.getMaxAscent());
/*  568 */       comChannel.writeShort(f.font.getMaxDescent());
/*  569 */       comChannel.writeShort(f.font.getMaxAscent());
/*  570 */       comChannel.writeShort(f.font.getMaxDescent());
/*  571 */       comChannel.writeInt(foo);
/*  572 */       comChannel.writeInt(0);
/*  573 */       comChannel.writeInt(foo);
/*  574 */       comChannel.writePad(4);
/*  575 */       comChannel.flush();
/*      */     }
/*      */   }
/*      */   
/*      */   public static void reqListFontsWithInfo(XClient c) throws IOException
/*      */   {
/*  581 */     ComChannel comChannel = c.channel;
/*  582 */     int n = c.length;
/*  583 */     int maxname = comChannel.readShort();
/*  584 */     int foo = comChannel.readShort();
/*  585 */     byte[] qqqq = null;
/*      */     
/*      */ 
/*  588 */     qqqq = c.bbuffer;
/*  589 */     comChannel.readByte(qqqq, 0, foo);
/*  590 */     comChannel.readPad(-foo & 0x3);
/*  591 */     String s = new String(qqqq, 0, foo);
/*  592 */     qqqq = s.getBytes();
/*      */     
/*  594 */     byte[] pattern = qqqq;
/*  595 */     byte[] bb = new byte[12];
/*  596 */     int count = 0;
/*  597 */     for (int i = 0; i < aliases.length; i++) {
/*  598 */       if (match_aux(aliases[i].name, 0, pattern, 0)) {
/*  599 */         count++;
/*  600 */         if (maxname < count)
/*      */           break;
/*      */       }
/*      */     }
/*  604 */     if (maxname >= count) {
/*  605 */       for (int i = 0; i < flist.length; i++)
/*  606 */         if (match_aux(flist[i].lfname, 0, pattern, 0)) {
/*  607 */           count++;
/*  608 */           if (maxname < count)
/*      */             break;
/*      */         }
/*      */     }
/*  612 */     if ((maxname >= count) && (count == 0)) {
/*  613 */       for (int i = 0; i < flist.length; i++) {
/*  614 */         if (match_scalable(flist[i].lfname, pattern)) {
/*  615 */           count++;
/*  616 */           if (maxname < count)
/*      */             break;
/*      */         }
/*      */       }
/*      */     }
/*  621 */     synchronized (comChannel) {
/*  622 */       if (count > 0) {
/*  623 */         for (int i = 0; i < aliases.length; i++) {
/*  624 */           if (match_aux(aliases[i].name, 0, pattern, 0)) {
/*  625 */             count--;
/*  626 */             DDXFont font = aliases[i].font;
/*  627 */             int[] prop = font.getProp();
/*      */             
/*  629 */             comChannel.writeByte(1);
/*      */             
/*  631 */             comChannel.writeByte(aliases[i].name.length);
/*  632 */             comChannel.writeShort(c.getSequence());
/*  633 */             comChannel.writeInt(7 + (prop != null ? prop.length / 2 : 0) * 2 + (aliases[i].name.length + 3) / 4);
/*      */             
/*      */ 
/*  636 */             comChannel.writeShort(0);
/*  637 */             comChannel.writeShort(0);
/*  638 */             comChannel.writeShort(font.min_width);
/*  639 */             comChannel.writeShort(0);
/*  640 */             comChannel.writeShort(0);
/*  641 */             comChannel.writeShort(0);
/*  642 */             comChannel.writePad(4);
/*      */             
/*      */ 
/*  645 */             comChannel.writeShort(0);
/*  646 */             comChannel.writeShort(font.max_width);
/*  647 */             comChannel.writeShort(font.max_width);
/*  648 */             comChannel.writeShort(font.getMaxAscent());
/*  649 */             comChannel.writeShort(font.getMaxDescent());
/*  650 */             comChannel.writeShort(0);
/*  651 */             comChannel.writePad(4);
/*      */             
/*  653 */             comChannel.writeShort(font.min_char_or_byte2);
/*  654 */             comChannel.writeShort(font.max_char_or_byte2);
/*      */             
/*  656 */             comChannel.writeShort(font.default_char);
/*      */             
/*  658 */             comChannel.writeShort(prop != null ? prop.length / 2 : 0);
/*      */             
/*  660 */             comChannel.writeByte(0);
/*  661 */             comChannel.writeByte(font.min_byte1);
/*  662 */             comChannel.writeByte(font.max_byte1);
/*  663 */             comChannel.writeByte(0);
/*      */             
/*  665 */             comChannel.writeShort(font.getMaxAscent());
/*  666 */             comChannel.writeShort(font.getMaxDescent());
/*  667 */             comChannel.writeInt(count);
/*      */             
/*      */ 
/*  670 */             if (prop != null) {
/*  671 */               for (int j = 0; j < prop.length; j++) {
/*  672 */                 comChannel.writeInt(prop[j]);
/*      */               }
/*      */             }
/*      */             
/*  676 */             comChannel.writeByte(aliases[i].name);
/*  677 */             comChannel.writePad(-aliases[i].name.length & 0x3);
/*      */             
/*  679 */             if (count == 0)
/*      */               break;
/*      */           }
/*      */         }
/*  683 */         if (count > 0) {
/*  684 */           for (int i = 0; i < flist.length; i++) {
/*  685 */             if (match_aux(flist[i].lfname, 0, pattern, 0)) {
/*  686 */               count--;
/*      */               
/*  688 */               DDXFont font = flist[i];
/*  689 */               int[] prop = font.getProp();
/*      */               
/*  691 */               comChannel.writeByte(1);
/*      */               
/*  693 */               comChannel.writeByte(font.lfname.length);
/*  694 */               comChannel.writeShort(c.getSequence());
/*  695 */               comChannel.writeInt(7 + (prop != null ? prop.length / 2 : 0) * 2 + (font.lfname.length + 3) / 4);
/*      */               
/*      */ 
/*  698 */               comChannel.writeShort(0);
/*  699 */               comChannel.writeShort(0);
/*  700 */               comChannel.writeShort(font.min_width);
/*  701 */               comChannel.writeShort(0);
/*  702 */               comChannel.writeShort(0);
/*  703 */               comChannel.writeShort(0);
/*  704 */               comChannel.writePad(4);
/*      */               
/*      */ 
/*  707 */               comChannel.writeShort(0);
/*  708 */               comChannel.writeShort(font.max_width);
/*  709 */               comChannel.writeShort(font.max_width);
/*  710 */               comChannel.writeShort(font.getMaxAscent());
/*  711 */               comChannel.writeShort(font.getMaxDescent());
/*  712 */               comChannel.writeShort(0);
/*  713 */               comChannel.writePad(4);
/*      */               
/*  715 */               comChannel.writeShort(font.min_char_or_byte2);
/*  716 */               comChannel.writeShort(font.max_char_or_byte2);
/*      */               
/*  718 */               comChannel.writeShort(font.default_char);
/*  719 */               comChannel.writeShort(prop != null ? prop.length / 2 : 0);
/*  720 */               comChannel.writeByte(0);
/*  721 */               comChannel.writeByte(font.min_byte1);
/*  722 */               comChannel.writeByte(font.max_byte1);
/*  723 */               comChannel.writeByte(0);
/*  724 */               comChannel.writeShort(font.getMaxAscent());
/*  725 */               comChannel.writeShort(font.getMaxDescent());
/*  726 */               comChannel.writeInt(count);
/*      */               
/*  728 */               if (prop != null) {
/*  729 */                 for (int j = 0; j < prop.length; j++) {
/*  730 */                   comChannel.writeInt(prop[j]);
/*      */                 }
/*      */               }
/*      */               
/*  734 */               comChannel.writeByte(font.lfname);
/*  735 */               comChannel.writePad(-font.lfname.length & 0x3);
/*      */               
/*  737 */               if (count == 0)
/*      */                 break;
/*      */             }
/*      */           }
/*      */         }
/*  742 */         if (count > 0) {
/*  743 */           for (int i = 0; i < flist.length; i++) {
/*  744 */             if (match_scalable(flist[i].lfname, pattern)) {
/*  745 */               count--;
/*      */               
/*  747 */               DDXFont font = flist[i].getScalableFont(pattern);
/*  748 */               int[] prop = font.getProp();
/*      */               
/*  750 */               comChannel.writeByte(1);
/*      */               
/*      */ 
/*  753 */               comChannel.writeByte(font.lfname.length);
/*  754 */               comChannel.writeShort(c.getSequence());
/*  755 */               comChannel.writeInt(7 + (prop != null ? prop.length / 2 : 0) * 2 + (font.lfname.length + 3) / 4);
/*      */               
/*      */ 
/*      */ 
/*  759 */               comChannel.writeShort(0);
/*  760 */               comChannel.writeShort(0);
/*  761 */               comChannel.writeShort(font.min_width);
/*  762 */               comChannel.writeShort(0);
/*  763 */               comChannel.writeShort(0);
/*  764 */               comChannel.writeShort(0);
/*  765 */               comChannel.writePad(4);
/*      */               
/*      */ 
/*  768 */               comChannel.writeShort(0);
/*  769 */               comChannel.writeShort(font.max_width);
/*  770 */               comChannel.writeShort(font.max_width);
/*  771 */               comChannel.writeShort(font.getMaxAscent());
/*  772 */               comChannel.writeShort(font.getMaxDescent());
/*  773 */               comChannel.writeShort(0);
/*  774 */               comChannel.writePad(4);
/*      */               
/*  776 */               comChannel.writeShort(font.min_char_or_byte2);
/*  777 */               comChannel.writeShort(font.max_char_or_byte2);
/*  778 */               comChannel.writeShort(font.default_char);
/*      */               
/*  780 */               comChannel.writeShort(prop != null ? prop.length / 2 : 0);
/*      */               
/*  782 */               comChannel.writeByte(0);
/*  783 */               comChannel.writeByte(font.min_byte1);
/*  784 */               comChannel.writeByte(font.max_byte1);
/*  785 */               comChannel.writeByte(0);
/*      */               
/*  787 */               comChannel.writeShort(font.getMaxAscent());
/*  788 */               comChannel.writeShort(font.getMaxDescent());
/*  789 */               comChannel.writeInt(count);
/*      */               
/*      */ 
/*  792 */               if (prop != null) {
/*  793 */                 for (int j = 0; j < prop.length; j++) {
/*  794 */                   comChannel.writeInt(prop[j]);
/*      */                 }
/*      */               }
/*      */               
/*  798 */               comChannel.writeByte(font.lfname);
/*  799 */               comChannel.writePad(-font.lfname.length & 0x3);
/*      */               
/*  801 */               if (count == 0)
/*      */                 break;
/*      */             }
/*      */           }
/*      */         }
/*      */       }
/*  807 */       comChannel.writeByte(1);
/*  808 */       comChannel.writeByte(0);
/*  809 */       comChannel.writeShort(c.getSequence());
/*  810 */       comChannel.writeInt(7);
/*  811 */       comChannel.writePad(52);
/*  812 */       comChannel.flush();
/*      */     }
/*      */   }
/*      */   
/*      */   public static void reqSetFontPath(XClient c) throws IOException
/*      */   {
/*  818 */     ComChannel comChannel = c.channel;
/*      */     
/*  820 */     int n = c.length;
/*  821 */     int foo = comChannel.readShort();
/*  822 */     comChannel.readPad(2);
/*      */     
/*  824 */     byte[][] path = new byte[foo][];
/*  825 */     int count = 0;
/*  826 */     for (int i = 0; i < path.length; i++) {
/*  827 */       foo = comChannel.readByte();
/*  828 */       count += 1 + foo;
/*  829 */       path[i] = new byte[foo];
/*  830 */       comChannel.readByte(path[i], 0, foo);
/*      */     }
/*  832 */     comChannel.readPad(-count & 0x3);
/*  833 */     synchronized (XFont.class) {
/*  834 */       fpath = path;
/*      */     }
/*      */   }
/*      */   
/*      */   public static void reqGetFontPath(XClient c) throws IOException
/*      */   {
/*  840 */     ComChannel comChannel = c.channel;
/*  841 */     int foo = c.length;
/*      */     
/*  843 */     synchronized (comChannel) {
/*  844 */       comChannel.writeByte(1);
/*  845 */       int length = 0;
/*  846 */       for (int i = 0; i < fpath.length; i++) {
/*  847 */         length += 1 + fpath[i].length;
/*      */       }
/*  849 */       comChannel.writePad(1);
/*  850 */       comChannel.writeShort(c.getSequence());
/*  851 */       comChannel.writeInt((length + 3) / 4);
/*  852 */       comChannel.writeShort(fpath.length);
/*  853 */       comChannel.writePad(22);
/*      */       
/*  855 */       for (int i = 0; i < fpath.length; i++) {
/*  856 */         comChannel.writeByte(fpath[i].length);
/*  857 */         comChannel.writeByte(fpath[i]);
/*      */       }
/*  859 */       comChannel.writePad(-length & 0x3);
/*  860 */       comChannel.flush();
/*      */     }
/*      */   }
/*      */   
/*      */   public static void reqListFonts(XClient c)
/*      */     throws IOException
/*      */   {
/*  867 */     ComChannel comChannel = c.channel;
/*  868 */     int n = c.length;
/*  869 */     int maxname = foo = comChannel.readShort();
/*  870 */     int foo = comChannel.readShort();
/*  871 */     byte[] pattern = null;
/*      */     
/*  873 */     comChannel.readByte(c.bbuffer, 0, foo);
/*  874 */     comChannel.readPad(-foo & 0x3);
/*  875 */     for (int i = 0; i < foo; i++) {
/*  876 */       if ((65 <= c.bbuffer[i]) && (c.bbuffer[i] <= 90))
/*  877 */         c.bbuffer[i] = ((byte)(97 + c.bbuffer[i] - 65));
/*      */     }
/*  879 */     String s = new String(c.bbuffer, 0, foo);
/*  880 */     pattern = s.getBytes();
/*      */     
/*      */ 
/*  883 */     synchronized (comChannel) {
/*  884 */       comChannel.writeByte(1);
/*      */       
/*  886 */       int count = 0;
/*  887 */       int length = 0;
/*      */       
/*  889 */       for (int i = 0; i < aliases.length; i++)
/*  890 */         if (match_aux(aliases[i].name, 0, pattern, 0)) {
/*  891 */           count++;
/*  892 */           length += 1 + aliases[i].name.length;
/*  893 */           if (maxname < count)
/*      */             break;
/*      */         }
/*  896 */       if (maxname >= count) {
/*  897 */         for (int i = 0; i < flist.length; i++)
/*  898 */           if (match_aux(flist[i].lfname, 0, pattern, 0)) {
/*  899 */             count++;
/*  900 */             length += 1 + flist[i].lfname.length;
/*  901 */             if (maxname < count)
/*      */               break;
/*      */           }
/*      */       }
/*  905 */       if ((maxname >= count) && (count == 0)) {
/*  906 */         for (int i = 0; i < flist.length; i++) {
/*  907 */           if (match_scalable(flist[i].lfname, pattern)) {
/*  908 */             count++;
/*  909 */             byte[] tmp = genScaleName(flist[i].lfname, pattern);
/*  910 */             length += 1 + tmp.length;
/*  911 */             if (maxname < count)
/*      */               break;
/*      */           }
/*      */         }
/*      */       }
/*  916 */       comChannel.writePad(1);
/*  917 */       comChannel.writeShort(c.getSequence());
/*  918 */       comChannel.writeInt((length + 3) / 4);
/*  919 */       comChannel.writeShort(count);
/*  920 */       comChannel.writePad(22);
/*      */       
/*  922 */       if (count > 0) {
/*  923 */         for (int i = 0; i < aliases.length; i++)
/*  924 */           if (match_aux(aliases[i].name, 0, pattern, 0)) {
/*  925 */             count--;
/*  926 */             comChannel.writeByte(aliases[i].name.length);
/*  927 */             comChannel.writeByte(aliases[i].name);
/*  928 */             if (count == 0)
/*      */               break;
/*      */           }
/*      */       }
/*  932 */       if (count > 0) {
/*  933 */         for (int i = 0; i < flist.length; i++)
/*  934 */           if (match_aux(flist[i].lfname, 0, pattern, 0)) {
/*  935 */             count--;
/*  936 */             comChannel.writeByte(flist[i].lfname.length);
/*  937 */             comChannel.writeByte(flist[i].lfname);
/*  938 */             if (count == 0)
/*      */               break;
/*      */           }
/*      */       }
/*  942 */       if (count > 0) {
/*  943 */         for (int i = 0; i < flist.length; i++)
/*  944 */           if (match_scalable(flist[i].lfname, pattern)) {
/*  945 */             count--;
/*  946 */             byte[] tmp = genScaleName(flist[i].lfname, pattern);
/*  947 */             comChannel.writeByte(tmp.length);
/*  948 */             comChannel.writeByte(tmp);
/*  949 */             if (count == 0)
/*      */               break;
/*      */           }
/*      */       }
/*  953 */       comChannel.writePad(-length & 0x3);
/*  954 */       comChannel.flush();
/*      */     }
/*      */   }
/*      */   
/*      */   public static void reqCloseFont(XClient c) throws IOException
/*      */   {
/*  960 */     ComChannel comChannel = c.channel;
/*  961 */     int foo = c.length;
/*  962 */     foo = comChannel.readInt();
/*  963 */     freeResource(foo, 0);
/*      */   }
/*      */   
/*  966 */   public void delete() throws IOException { this.name = null;
/*  967 */     this.lfname = null;
/*  968 */     this.encoding = null;
/*  969 */     if (this.font != null) this.font.delete();
/*  970 */     this.font = null;
/*      */   }
/*      */   
/*      */   public static void reqOpenFont(XClient c)
/*      */     throws IOException
/*      */   {
/*  976 */     ComChannel comChannel = c.channel;
/*  977 */     int n = c.length;
/*  978 */     int foo = comChannel.readInt();int fid = foo;
/*  979 */     foo = comChannel.readShort();
/*  980 */     comChannel.readPad(2);
/*  981 */     byte[] qqqq = null;
/*      */     
/*      */ 
/*  984 */     qqqq = c.bbuffer;
/*  985 */     comChannel.readByte(qqqq, 0, foo);
/*  986 */     comChannel.readPad(-foo & 0x3);
/*  987 */     for (int i = 0; i < foo; i++) {
/*  988 */       if ((65 <= qqqq[i]) && (qqqq[i] <= 90))
/*  989 */         qqqq[i] = ((byte)(97 + qqqq[i] - 65));
/*      */     }
/*  991 */     String s = new String(qqqq, 0, foo);
/*      */     
/*      */     try
/*      */     {
/*  995 */       XFont f = new XFont(fid, s);
/*  996 */       add(f);
/*      */     }
/*      */     catch (Exception e) {}
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public static void reqQueryFont(XClient c)
/*      */     throws IOException
/*      */   {
/* 1006 */     ComChannel comChannel = c.channel;
/* 1007 */     int foo = comChannel.readInt();
/* 1008 */     c.length -= 2;
/* 1009 */     XFont f = (XFont)lookupIDByType(foo, 4);
/* 1010 */     if (f == null) {
/* 1011 */       c.errorValue = foo;
/* 1012 */       c.errorReason = 7;
/* 1013 */       return;
/*      */     }
/*      */     
/*      */ 
/* 1017 */     int[] prop = f.getProp();
/* 1018 */     boolean hascinfo = f.hasCharInfo();
/*      */     
/* 1020 */     synchronized (comChannel) {
/* 1021 */       comChannel.writeByte(1);
/* 1022 */       comChannel.writePad(1);
/* 1023 */       comChannel.writeShort(c.getSequence());
/* 1024 */       comChannel.writeInt(7 + (prop != null ? prop.length / 2 : 0) * 2 + (hascinfo ? (f.font.max_char_or_byte2 - f.font.min_char_or_byte2 + 1) * 3 : 0));
/*      */       
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1031 */       comChannel.writeShort(0);
/* 1032 */       comChannel.writeShort(0);
/* 1033 */       comChannel.writeShort(f.font.min_width);
/* 1034 */       comChannel.writeShort(0);
/* 1035 */       comChannel.writeShort(0);
/* 1036 */       comChannel.writeShort(0);
/* 1037 */       comChannel.writePad(4);
/*      */       
/*      */ 
/* 1040 */       comChannel.writeShort(0);
/* 1041 */       comChannel.writeShort(f.font.max_width);
/* 1042 */       comChannel.writeShort(f.font.max_width);
/* 1043 */       comChannel.writeShort(f.font.getMaxAscent());
/* 1044 */       comChannel.writeShort(f.font.getMaxDescent());
/* 1045 */       comChannel.writeShort(0);
/* 1046 */       comChannel.writePad(4);
/*      */       
/* 1048 */       comChannel.writeShort(f.font.min_char_or_byte2);
/* 1049 */       comChannel.writeShort(f.font.max_char_or_byte2);
/*      */       
/* 1051 */       comChannel.writeShort(f.font.default_char);
/* 1052 */       comChannel.writeShort(prop != null ? prop.length / 2 : 0);
/*      */       
/* 1054 */       comChannel.writeByte(0);
/* 1055 */       comChannel.writeByte(f.font.min_byte1);
/* 1056 */       comChannel.writeByte(f.font.max_byte1);
/* 1057 */       comChannel.writeByte(0);
/*      */       
/* 1059 */       comChannel.writeShort(f.font.getMaxAscent());
/* 1060 */       comChannel.writeShort(f.font.getMaxDescent());
/* 1061 */       if (hascinfo) {
/* 1062 */         comChannel.writeInt(f.font.max_char_or_byte2 - f.font.min_char_or_byte2 + 1);
/*      */       }
/*      */       else {
/* 1065 */         comChannel.writeInt(0);
/*      */       }
/*      */       
/* 1068 */       if (prop != null) {
/* 1069 */         for (int i = 0; i < prop.length; i++) {
/* 1070 */           comChannel.writeInt(prop[i]);
/*      */         }
/*      */       }
/*      */       
/* 1074 */       if (hascinfo) {
/* 1075 */         f.dumpCharInfo(c);
/*      */       }
/*      */       
/* 1078 */       comChannel.flush();
/*      */     }
/*      */   }
/*      */   
/*      */   public int[] getProp() {
/* 1083 */     return this.font.getProp();
/*      */   }
/*      */   
/*      */   public boolean hasCharInfo() {
/* 1087 */     return (this.font.min_width != this.font.max_width) || (this.font.min_byte1 == 0);
/*      */   }
/*      */   
/*      */   public void dumpCharInfo(XClient c) throws IOException {
/* 1091 */     ComChannel comChannel = c.channel;
/* 1092 */     char w = '\000';
/* 1093 */     int ma = this.font.getMaxAscent();
/* 1094 */     int md = this.font.getMaxDescent();
/* 1095 */     if (this.font.min_width == this.font.max_width) {
/* 1096 */       w = (char)this.font.min_width;
/* 1097 */       for (int i = this.font.min_char_or_byte2; i <= this.font.max_char_or_byte2; i++) {
/* 1098 */         comChannel.writeShort(0);
/* 1099 */         comChannel.writeShort(w);
/* 1100 */         comChannel.writeShort(w);
/* 1101 */         comChannel.writeShort(ma);
/* 1102 */         comChannel.writeShort(md);
/* 1103 */         comChannel.writeShort(0);
/*      */       }
/* 1105 */       return;
/*      */     }
/* 1107 */     if (this.font.encoding == null) {
/* 1108 */       int[] widths = this.font.getWidths();
/* 1109 */       for (int i = this.font.min_char_or_byte2; i <= this.font.max_char_or_byte2; i++) {
/* 1110 */         w = (char)widths[i];
/* 1111 */         comChannel.writeShort(0);
/* 1112 */         comChannel.writeShort(w);
/* 1113 */         comChannel.writeShort(w);
/* 1114 */         comChannel.writeShort(ma);
/* 1115 */         comChannel.writeShort(md);
/* 1116 */         comChannel.writeShort(0);
/*      */       }
/* 1118 */       return;
/*      */     }
/* 1120 */     if (this.font.max_char_or_byte2 <= 255) {
/* 1121 */       byte[] src = new byte[1];
/* 1122 */       char[] dst = new char[1];
/*      */       
/* 1124 */       for (int i = this.font.min_char_or_byte2; i <= this.font.max_char_or_byte2; i++) {
/* 1125 */         src[0] = ((byte)i);
/* 1126 */         encode(src, 0, 1, dst);
/* 1127 */         w = (char)this.font.charWidth(dst[0]);
/* 1128 */         comChannel.writeShort(0);
/* 1129 */         comChannel.writeShort(w);
/* 1130 */         comChannel.writeShort(w);
/* 1131 */         comChannel.writeShort(ma);
/* 1132 */         comChannel.writeShort(md);
/* 1133 */         comChannel.writeShort(0);
/*      */       }
/* 1135 */       return;
/*      */     }
/*      */     
/* 1138 */     byte[] src = new byte[2];
/* 1139 */     char[] dst = new char[1];
/*      */     
/* 1141 */     for (int i = this.font.min_char_or_byte2; i <= this.font.max_char_or_byte2; i++) {
/* 1142 */       src[0] = ((byte)(i >> 8));
/* 1143 */       src[1] = ((byte)i);
/* 1144 */       encode(src, 0, 2, dst);
/* 1145 */       w = (char)this.font.charWidth(dst[0]);
/* 1146 */       comChannel.writeShort(0);
/* 1147 */       comChannel.writeShort(w);
/* 1148 */       comChannel.writeShort(w);
/* 1149 */       comChannel.writeShort(ma);
/* 1150 */       comChannel.writeShort(md);
/* 1151 */       comChannel.writeShort(0);
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */   public int encode(byte[] bbuffer, int start, int len, char[] cbuffer)
/*      */   {
/* 1158 */     if (this.font.charset == null) {
/* 1159 */       return 0;
/*      */     }
/* 1161 */     return this.font.charset.encode(bbuffer, start, len, cbuffer);
/*      */   }
/*      */ }


/* Location:              C:\Users\Joey\Downloads\launch.jar!\com\emt\proteus\xserver\display\XFont.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */