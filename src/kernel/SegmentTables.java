package kernel;


public class SegmentTables {
  public static void rebuildGDT() {
    //null descriptor
    MAGIC.wMem32(KernelConst.KM_GDTADDR, 0x00000000);
    MAGIC.wMem32(KernelConst.KM_GDTADDR+4, 0x00000000);
    //code descriptor
    MAGIC.wMem32(KernelConst.KM_GDTADDR+8, 0x0000FFFF);
    if (MAGIC.ptrSize==4) MAGIC.wMem32(KernelConst.KM_GDTADDR+12, 0x00CF9A00); //32 bit code
    else MAGIC.wMem32(KernelConst.KM_GDTADDR+12, 0x002F9800); //64 bit code
    //data descriptor
    MAGIC.wMem32(KernelConst.KM_GDTADDR+16, 0x0000FFFF);
    MAGIC.wMem32(KernelConst.KM_GDTADDR+20, 0x00CF9200);
    //descriptor for 16 bit code protected mode code (used in BIOS call)
    MAGIC.wMem32(KernelConst.KM_GDTADDR+24, 0x0000FFFF|(KernelConst.BIOS_MEMORY<<16));
    MAGIC.wMem32(KernelConst.KM_GDTADDR+28, 0x008F9A00|(KernelConst.BIOS_MEMORY>>>16));
    
    //load new gdt
    lgdt(4); //see also KernelConst
    //do a far-jump coded as retf to load the new cs
    MAGIC.inline(0x6A, 0x08);                       //push byte 0x08
    MAGIC.inline(0xE8, 0x00, 0x00, 0x00, 0x00);     //call rel 0
    if (MAGIC.ptrSize==4) {
      MAGIC.inline(0x83, 0x04, 0x24, 0x05);         //add dword [esp],byte 0x05
      MAGIC.inline(0xCB);                           //retf
    } else {
      MAGIC.inline(0x83, 0x04, 0x24, 0x06);         //add dword [rsp],byte 0x06
      MAGIC.inline(0x48, 0xCB);                     //retf
    }
    //reload the new data-selector
    MAGIC.inline(0x66, 0xB8, 0x10, 0x00); //mov ax,0x10
    MAGIC.inline(0x8E, 0xD8);             //mov ds,ax  
    MAGIC.inline(0x8E, 0xC0);             //mov es,ax  
    MAGIC.inline(0x8E, 0xE0);             //mov fs,ax  
    MAGIC.inline(0x8E, 0xE8);             //mov gs,ax  
    MAGIC.inline(0x8E, 0xD0);             //mov ss,ax  
  }
  
  public static void lgdt(int cnt) {
    long amd0=0l, dummy;
    
    MAGIC.ignore(amd0);
    dummy=(((long)KernelConst.KM_GDTADDR)<<16)|((long)((cnt<<3)-1));
    MAGIC.ignore(dummy);
    MAGIC.inline(0x0F, 0x01, 0x55, 0xF0); // lgdt [e/rbp-0x10]
  }
  
  public static void lidt() {
    long amd0=0l, dummy;
    
    MAGIC.ignore(amd0);
    dummy=(((long)KernelConst.KM_IDTADDR)<<16)|((long)(KernelConst.KC_INTCOUNT*2*MAGIC.ptrSize-1));
    MAGIC.ignore(dummy);
    MAGIC.inline(0x0F, 0x01, 0x5D, 0xF0); // lidt [e/rbp-0x10]
  }
  
  public static void lidtRM() {
    long amd0=0l, dummy=1023l;
    
    MAGIC.ignore(amd0);
    MAGIC.ignore(dummy);
    MAGIC.inline(0x0F, 0x01, 0x5D, 0xF0); // lidt [e/rbp-0x10]
  }
}
