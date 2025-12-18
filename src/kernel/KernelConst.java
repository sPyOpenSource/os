package kernel;

public class KernelConst {
  //--- ### --- constants used at several places --- ### ---
  public final static int KC_FIRSTINTINGDT = 4; //reserve 4 entries in gdt (see SegmentTables)
  public final static int KC_INTCOUNT = 48; //create table for 32 system exceptions and 16 legacy hardware interrupts
  
  //--- ### --- constants used to address magic memory locations --- ### --- 
  
  //constants to specify complete blocks
  public final static int KM_MEMSTART = 0x08000; //start of kernel-memory, size 512 bytes
  public final static int BIOS_MEMORY = 0x09000; //start of BIOS-memory
  public final static int BIOS_STKEND = BIOS_MEMORY + 0x1000; //allocate one page
  public final static int KM_GDTADDR  = 0x10000; //position of gdt, size 4K
  public final static int KM_IDTADDR  = 0x11000; //position of idt, size 4K
  public final static int KM_PAGEDIR  = 0x12000; //position of top-level page directory
  public final static int KM_SCRATCH  = 0x9F000; //one page for scratch buffer
  
  //constants used in kernel (see K_MEMSTART), max 512 bytes 
  public final static int KM_INTDESC = /*int*/ KM_MEMSTART; //address of class of interrupt-handler
  
  //layout of image (see K_IMAGEPOS), size differs
  public final static int KM_IMAGESIZEREL = /*int*/ 4;
}
