/*     */ package com.emt.proteus.runtime32.io;
/*     */ 
/*     */ import com.emt.proteus.runtime32.api.FileProxy;
/*     */ import com.emt.proteus.runtime32.api.FileProxy.Memory;
/*     */ import java.io.IOException;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ 
/*     */ 
/*     */ 
/*     */ public class InfoProvider
/*     */   implements Provider
/*     */ {
/*  14 */   private static final Provider instance = new InfoProvider();
/*     */   private static final String MEM_INFO = "MemTotal:        3096820 kB\nMemFree:          937944 kB\nBuffers:          164196 kB\nCached:          1245552 kB\nSwapCached:            0 kB\nActive:          1105208 kB\nInactive:         897248 kB\nActive(anon):     593496 kB\nInactive(anon):     9812 kB\nActive(file):     511712 kB\nInactive(file):   887436 kB\nUnevictable:          16 kB\nMlocked:              16 kB\nHighTotal:       2234736 kB\nHighFree:         372628 kB\nLowTotal:         862084 kB\nLowFree:          565316 kB\nSwapTotal:       8388604 kB\nSwapFree:        8388604 kB\nDirty:               112 kB\nWriteback:             0 kB\nAnonPages:        592720 kB\nMapped:           162416 kB\nShmem:             10608 kB\nSlab:              80660 kB\nSReclaimable:      64748 kB\nSUnreclaim:        15912 kB\nKernelStack:        2912 kB\nPageTables:         6556 kB\nNFS_Unstable:          0 kB\nBounce:                0 kB\nWritebackTmp:          0 kB\nCommitLimit:     9937012 kB\nCommitted_AS:    2064692 kB\nVmallocTotal:     122880 kB\nVmallocUsed:       80392 kB\nVmallocChunk:      27132 kB\nHardwareCorrupted:     0 kB\nAnonHugePages:         0 kB\nHugePages_Total:       0\nHugePages_Free:        0\nHugePages_Rsvd:        0\nHugePages_Surp:        0\nHugepagesize:       4096 kB\nDirectMap4k:       73720 kB\nDirectMap4M:      835584 kB\n";
/*     */   
/*  17 */   public static Provider getInstance() { return instance; }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private static final String CPU_INFO = "processor\t: 0\nvendor_id\t: GenuineIntel\ncpu family\t: 6\nmodel\t\t: 23\nmodel name\t: Intel(R) Core(TM)2 Quad  CPU   Q9000  @ 2.00GHz\nstepping\t: 10\ncpu MHz\t\t: 1600.000\ncache size\t: 3072 KB\nphysical id\t: 0\nsiblings\t: 4\ncore id\t\t: 0\ncpu cores\t: 4\napicid\t\t: 0\ninitial apicid\t: 0\nfdiv_bug\t: no\nhlt_bug\t\t: no\nf00f_bug\t: no\ncoma_bug\t: no\nfpu\t\t: yes\nfpu_exception\t: yes\ncpuid level\t: 13\nwp\t\t: yes\nflags\t\t: fpu vme de pse tsc msr pae mce cx8 apic mtrr pge mca cmov pat pse36 clflush dts acpi mmx fxsr sse sse2 ss ht tm pbe nx lm constant_tsc arch_perfmon pebs bts aperfmperf pni dtes64 monitor ds_cpl vmx smx est tm2 ssse3 cx16 xtpr pdcm sse4_1 xsave lahf_lm ida dts tpr_shadow vnmi flexpriority\nbogomips\t: 3989.11\nclflush size\t: 64\ncache_alignment\t: 64\naddress sizes\t: 36 bits physical, 48 bits virtual\npower management:\n\nprocessor\t: 1\nvendor_id\t: GenuineIntel\ncpu family\t: 6\nmodel\t\t: 23\nmodel name\t: Intel(R) Core(TM)2 Quad  CPU   Q9000  @ 2.00GHz\nstepping\t: 10\ncpu MHz\t\t: 1600.000\ncache size\t: 3072 KB\nphysical id\t: 0\nsiblings\t: 4\ncore id\t\t: 1\ncpu cores\t: 4\napicid\t\t: 1\ninitial apicid\t: 1\nfdiv_bug\t: no\nhlt_bug\t\t: no\nf00f_bug\t: no\ncoma_bug\t: no\nfpu\t\t: yes\nfpu_exception\t: yes\ncpuid level\t: 13\nwp\t\t: yes\nflags\t\t: fpu vme de pse tsc msr pae mce cx8 apic mtrr pge mca cmov pat pse36 clflush dts acpi mmx fxsr sse sse2 ss ht tm pbe nx lm constant_tsc arch_perfmon pebs bts aperfmperf pni dtes64 monitor ds_cpl vmx smx est tm2 ssse3 cx16 xtpr pdcm sse4_1 xsave lahf_lm ida dts tpr_shadow vnmi flexpriority\nbogomips\t: 3989.95\nclflush size\t: 64\ncache_alignment\t: 64\naddress sizes\t: 36 bits physical, 48 bits virtual\npower management:\n\nprocessor\t: 2\nvendor_id\t: GenuineIntel\ncpu family\t: 6\nmodel\t\t: 23\nmodel name\t: Intel(R) Core(TM)2 Quad  CPU   Q9000  @ 2.00GHz\nstepping\t: 10\ncpu MHz\t\t: 1600.000\ncache size\t: 3072 KB\nphysical id\t: 0\nsiblings\t: 4\ncore id\t\t: 2\ncpu cores\t: 4\napicid\t\t: 2\ninitial apicid\t: 2\nfdiv_bug\t: no\nhlt_bug\t\t: no\nf00f_bug\t: no\ncoma_bug\t: no\nfpu\t\t: yes\nfpu_exception\t: yes\ncpuid level\t: 13\nwp\t\t: yes\nflags\t\t: fpu vme de pse tsc msr pae mce cx8 apic mtrr pge mca cmov pat pse36 clflush dts acpi mmx fxsr sse sse2 ss ht tm pbe nx lm constant_tsc arch_perfmon pebs bts aperfmperf pni dtes64 monitor ds_cpl vmx smx est tm2 ssse3 cx16 xtpr pdcm sse4_1 xsave lahf_lm ida dts tpr_shadow vnmi flexpriority\nbogomips\t: 3989.65\nclflush size\t: 64\ncache_alignment\t: 64\naddress sizes\t: 36 bits physical, 48 bits virtual\npower management:\n\nprocessor\t: 3\nvendor_id\t: GenuineIntel\ncpu family\t: 6\nmodel\t\t: 23\nmodel name\t: Intel(R) Core(TM)2 Quad  CPU   Q9000  @ 2.00GHz\nstepping\t: 10\ncpu MHz\t\t: 1600.000\ncache size\t: 3072 KB\nphysical id\t: 0\nsiblings\t: 4\ncore id\t\t: 3\ncpu cores\t: 4\napicid\t\t: 3\ninitial apicid\t: 3\nfdiv_bug\t: no\nhlt_bug\t\t: no\nf00f_bug\t: no\ncoma_bug\t: no\nfpu\t\t: yes\nfpu_exception\t: yes\ncpuid level\t: 13\nwp\t\t: yes\nflags\t\t: fpu vme de pse tsc msr pae mce cx8 apic mtrr pge mca cmov pat pse36 clflush dts acpi mmx fxsr sse sse2 ss ht tm pbe nx lm constant_tsc arch_perfmon pebs bts aperfmperf pni dtes64 monitor ds_cpl vmx smx est tm2 ssse3 cx16 xtpr pdcm sse4_1 xsave lahf_lm ida dts tpr_shadow vnmi flexpriority\nbogomips\t: 3990.00\nclflush size\t: 64\ncache_alignment\t: 64\naddress sizes\t: 36 bits physical, 48 bits virtual\npower management:\n\n";
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static final String PAGETYPEINFO = "Page block order: 10\nPages per block:  1024\n\nFree pages count per migrate type at order       0      1      2      3      4      5      6      7      8      9     10 \nNode    0, zone      DMA, type    Unmovable      0      1      1      1      1      1      1      1      1      1      0 \nNode    0, zone      DMA, type  Reclaimable      0      0      0      0      0      0      0      0      0      0      0 \nNode    0, zone      DMA, type      Movable      0      1      0      1      2      1      1      0      1      1      1 \nNode    0, zone      DMA, type      Reserve      0      0      0      0      0      0      0      0      0      0      1 \nNode    0, zone      DMA, type      Isolate      0      0      0      0      0      0      0      0      0      0      0 \nNode    0, zone   Normal, type    Unmovable    208     17     26      5      1      1      0      1      0      1      0 \nNode    0, zone   Normal, type  Reclaimable      1      1      0      1      1      0      0      1      0      0      0 \nNode    0, zone   Normal, type      Movable      0      0      0      0      0      0      0      1      1      1    130 \nNode    0, zone   Normal, type      Reserve      0      0      0      0      0      0      0      0      0      0      2 \nNode    0, zone   Normal, type      Isolate      0      0      0      0      0      0      0      0      0      0      0 \nNode    0, zone  HighMem, type    Unmovable     28      5      7      1      0      0      0      1      2      3      1 \nNode    0, zone  HighMem, type  Reclaimable      0      0      0      0      0      0      0      0      0      0      0 \nNode    0, zone  HighMem, type      Movable      5      0      2      2      0      0      0      1      0      1     87 \nNode    0, zone  HighMem, type      Reserve      0      1      0      0      0      0      0      0      0      0      0 \nNode    0, zone  HighMem, type      Isolate      0      0      0      0      0      0      0      0      0      0      0 \n\nNumber of blocks type     Unmovable  Reclaimable      Movable      Reserve      Isolate \nNode 0, zone      DMA            1            0            2            1            0 \nNode 0, zone   Normal           29           16          171            2            0 \nNode 0, zone  HighMem            9            0          536            1            0 \n";
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 215 */   private Map<String, String> files = new HashMap();
/*     */   
/*     */   public InfoProvider() {
/* 218 */     this.files.put("/proc/meminfo", "MemTotal:        3096820 kB\nMemFree:          937944 kB\nBuffers:          164196 kB\nCached:          1245552 kB\nSwapCached:            0 kB\nActive:          1105208 kB\nInactive:         897248 kB\nActive(anon):     593496 kB\nInactive(anon):     9812 kB\nActive(file):     511712 kB\nInactive(file):   887436 kB\nUnevictable:          16 kB\nMlocked:              16 kB\nHighTotal:       2234736 kB\nHighFree:         372628 kB\nLowTotal:         862084 kB\nLowFree:          565316 kB\nSwapTotal:       8388604 kB\nSwapFree:        8388604 kB\nDirty:               112 kB\nWriteback:             0 kB\nAnonPages:        592720 kB\nMapped:           162416 kB\nShmem:             10608 kB\nSlab:              80660 kB\nSReclaimable:      64748 kB\nSUnreclaim:        15912 kB\nKernelStack:        2912 kB\nPageTables:         6556 kB\nNFS_Unstable:          0 kB\nBounce:                0 kB\nWritebackTmp:          0 kB\nCommitLimit:     9937012 kB\nCommitted_AS:    2064692 kB\nVmallocTotal:     122880 kB\nVmallocUsed:       80392 kB\nVmallocChunk:      27132 kB\nHardwareCorrupted:     0 kB\nAnonHugePages:         0 kB\nHugePages_Total:       0\nHugePages_Free:        0\nHugePages_Rsvd:        0\nHugePages_Surp:        0\nHugepagesize:       4096 kB\nDirectMap4k:       73720 kB\nDirectMap4M:      835584 kB\n");
/* 219 */     this.files.put("/proc/cpuinfo", "processor\t: 0\nvendor_id\t: GenuineIntel\ncpu family\t: 6\nmodel\t\t: 23\nmodel name\t: Intel(R) Core(TM)2 Quad  CPU   Q9000  @ 2.00GHz\nstepping\t: 10\ncpu MHz\t\t: 1600.000\ncache size\t: 3072 KB\nphysical id\t: 0\nsiblings\t: 4\ncore id\t\t: 0\ncpu cores\t: 4\napicid\t\t: 0\ninitial apicid\t: 0\nfdiv_bug\t: no\nhlt_bug\t\t: no\nf00f_bug\t: no\ncoma_bug\t: no\nfpu\t\t: yes\nfpu_exception\t: yes\ncpuid level\t: 13\nwp\t\t: yes\nflags\t\t: fpu vme de pse tsc msr pae mce cx8 apic mtrr pge mca cmov pat pse36 clflush dts acpi mmx fxsr sse sse2 ss ht tm pbe nx lm constant_tsc arch_perfmon pebs bts aperfmperf pni dtes64 monitor ds_cpl vmx smx est tm2 ssse3 cx16 xtpr pdcm sse4_1 xsave lahf_lm ida dts tpr_shadow vnmi flexpriority\nbogomips\t: 3989.11\nclflush size\t: 64\ncache_alignment\t: 64\naddress sizes\t: 36 bits physical, 48 bits virtual\npower management:\n\nprocessor\t: 1\nvendor_id\t: GenuineIntel\ncpu family\t: 6\nmodel\t\t: 23\nmodel name\t: Intel(R) Core(TM)2 Quad  CPU   Q9000  @ 2.00GHz\nstepping\t: 10\ncpu MHz\t\t: 1600.000\ncache size\t: 3072 KB\nphysical id\t: 0\nsiblings\t: 4\ncore id\t\t: 1\ncpu cores\t: 4\napicid\t\t: 1\ninitial apicid\t: 1\nfdiv_bug\t: no\nhlt_bug\t\t: no\nf00f_bug\t: no\ncoma_bug\t: no\nfpu\t\t: yes\nfpu_exception\t: yes\ncpuid level\t: 13\nwp\t\t: yes\nflags\t\t: fpu vme de pse tsc msr pae mce cx8 apic mtrr pge mca cmov pat pse36 clflush dts acpi mmx fxsr sse sse2 ss ht tm pbe nx lm constant_tsc arch_perfmon pebs bts aperfmperf pni dtes64 monitor ds_cpl vmx smx est tm2 ssse3 cx16 xtpr pdcm sse4_1 xsave lahf_lm ida dts tpr_shadow vnmi flexpriority\nbogomips\t: 3989.95\nclflush size\t: 64\ncache_alignment\t: 64\naddress sizes\t: 36 bits physical, 48 bits virtual\npower management:\n\nprocessor\t: 2\nvendor_id\t: GenuineIntel\ncpu family\t: 6\nmodel\t\t: 23\nmodel name\t: Intel(R) Core(TM)2 Quad  CPU   Q9000  @ 2.00GHz\nstepping\t: 10\ncpu MHz\t\t: 1600.000\ncache size\t: 3072 KB\nphysical id\t: 0\nsiblings\t: 4\ncore id\t\t: 2\ncpu cores\t: 4\napicid\t\t: 2\ninitial apicid\t: 2\nfdiv_bug\t: no\nhlt_bug\t\t: no\nf00f_bug\t: no\ncoma_bug\t: no\nfpu\t\t: yes\nfpu_exception\t: yes\ncpuid level\t: 13\nwp\t\t: yes\nflags\t\t: fpu vme de pse tsc msr pae mce cx8 apic mtrr pge mca cmov pat pse36 clflush dts acpi mmx fxsr sse sse2 ss ht tm pbe nx lm constant_tsc arch_perfmon pebs bts aperfmperf pni dtes64 monitor ds_cpl vmx smx est tm2 ssse3 cx16 xtpr pdcm sse4_1 xsave lahf_lm ida dts tpr_shadow vnmi flexpriority\nbogomips\t: 3989.65\nclflush size\t: 64\ncache_alignment\t: 64\naddress sizes\t: 36 bits physical, 48 bits virtual\npower management:\n\nprocessor\t: 3\nvendor_id\t: GenuineIntel\ncpu family\t: 6\nmodel\t\t: 23\nmodel name\t: Intel(R) Core(TM)2 Quad  CPU   Q9000  @ 2.00GHz\nstepping\t: 10\ncpu MHz\t\t: 1600.000\ncache size\t: 3072 KB\nphysical id\t: 0\nsiblings\t: 4\ncore id\t\t: 3\ncpu cores\t: 4\napicid\t\t: 3\ninitial apicid\t: 3\nfdiv_bug\t: no\nhlt_bug\t\t: no\nf00f_bug\t: no\ncoma_bug\t: no\nfpu\t\t: yes\nfpu_exception\t: yes\ncpuid level\t: 13\nwp\t\t: yes\nflags\t\t: fpu vme de pse tsc msr pae mce cx8 apic mtrr pge mca cmov pat pse36 clflush dts acpi mmx fxsr sse sse2 ss ht tm pbe nx lm constant_tsc arch_perfmon pebs bts aperfmperf pni dtes64 monitor ds_cpl vmx smx est tm2 ssse3 cx16 xtpr pdcm sse4_1 xsave lahf_lm ida dts tpr_shadow vnmi flexpriority\nbogomips\t: 3990.00\nclflush size\t: 64\ncache_alignment\t: 64\naddress sizes\t: 36 bits physical, 48 bits virtual\npower management:\n\n");
/* 220 */     this.files.put("/proc/pagetypeing", "Page block order: 10\nPages per block:  1024\n\nFree pages count per migrate type at order       0      1      2      3      4      5      6      7      8      9     10 \nNode    0, zone      DMA, type    Unmovable      0      1      1      1      1      1      1      1      1      1      0 \nNode    0, zone      DMA, type  Reclaimable      0      0      0      0      0      0      0      0      0      0      0 \nNode    0, zone      DMA, type      Movable      0      1      0      1      2      1      1      0      1      1      1 \nNode    0, zone      DMA, type      Reserve      0      0      0      0      0      0      0      0      0      0      1 \nNode    0, zone      DMA, type      Isolate      0      0      0      0      0      0      0      0      0      0      0 \nNode    0, zone   Normal, type    Unmovable    208     17     26      5      1      1      0      1      0      1      0 \nNode    0, zone   Normal, type  Reclaimable      1      1      0      1      1      0      0      1      0      0      0 \nNode    0, zone   Normal, type      Movable      0      0      0      0      0      0      0      1      1      1    130 \nNode    0, zone   Normal, type      Reserve      0      0      0      0      0      0      0      0      0      0      2 \nNode    0, zone   Normal, type      Isolate      0      0      0      0      0      0      0      0      0      0      0 \nNode    0, zone  HighMem, type    Unmovable     28      5      7      1      0      0      0      1      2      3      1 \nNode    0, zone  HighMem, type  Reclaimable      0      0      0      0      0      0      0      0      0      0      0 \nNode    0, zone  HighMem, type      Movable      5      0      2      2      0      0      0      1      0      1     87 \nNode    0, zone  HighMem, type      Reserve      0      1      0      0      0      0      0      0      0      0      0 \nNode    0, zone  HighMem, type      Isolate      0      0      0      0      0      0      0      0      0      0      0 \n\nNumber of blocks type     Unmovable  Reclaimable      Movable      Reserve      Isolate \nNode 0, zone      DMA            1            0            2            1            0 \nNode 0, zone   Normal           29           16          171            2            0 \nNode 0, zone  HighMem            9            0          536            1            0 \n");
/*     */   }
/*     */   
/*     */   public boolean provides(String fileName)
/*     */   {
/* 225 */     return this.files.containsKey(fileName);
/*     */   }
/*     */   
/*     */   public FileProxy create(String fileName, boolean create) throws IOException {
/* 229 */     String s = (String)this.files.get(fileName);
/* 230 */     return new FileProxy.Memory(fileName, s);
/*     */   }
/*     */ }


/* Location:              C:\Users\Joey\Downloads\codejar.jar!\com\emt\proteus\runtime32\io\InfoProvider.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */