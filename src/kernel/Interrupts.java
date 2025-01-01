package kernel;

import devices.Device;
import devices.DeviceList;

public class Interrupts {
  private static ExcHandler[] handler;
  private static Device[] devList;
  private int cliLevel;
  
  public static void init() {
    //create our new gdt
    SegmentTables.rebuildGDT();
    Interrupts.createTable();
    SegmentTables.lidt();
    //load gdt
    SegmentTables.lgdt(KernelConst.KC_FIRSTINTINGDT+KernelConst.KC_INTCOUNT);
    //instantiate Interrupts, insert Timer and fire
    Kernel.ints=new Interrupts();
  }
  
  private Interrupts() {
    MAGIC.inline(0xFA); //cli
    cliLevel=1;
  }
  
  public void sti() {
    if (cliLevel==0) return;
    if (--cliLevel==0) MAGIC.inline(0xFB);
  }
  
  public void cli() {
    MAGIC.inline(0xFA);
    cliLevel++;
  }
  
  public void replaceIRQHandler(int irqNr, Device dev) {
    devList[irqNr]=dev;
  }
  
  public void replaceExcHandler(int excNr, ExcHandler hnd) {
    handler[excNr]=hnd;
  }
  
  //should only be called with cleared interrupts of course
  public static void createTable() {
    int i=0, cls, destWOE, destWEC;
    ExcHandler[] newHandler;
    ExcHandler debug;
    Device[] newDevices;
    
    //save address of class descriptor and get destinations
    cls=(int)MAGIC.cast2Ref(MAGIC.clssDesc("Interrupts"));
    MAGIC.wMem32(KernelConst.KM_INTDESC, cls);
    destWOE=MAGIC.rMem32(cls+MAGIC.mthdOff("Interrupts", "firstLevelHandlerWOE"))
      +MAGIC.getCodeOff();
    destWEC=MAGIC.rMem32(cls+MAGIC.mthdOff("Interrupts", "firstLevelHandlerWEC"))
      +MAGIC.getCodeOff();
    //create entries for exceptions
    while (i<=0x07) createEntry(i++, destWOE);
    while (i<=0x0E) createEntry(i++, destWEC);
    while (i<=0x10) createEntry(i++, destWOE);
    createEntry(i++, destWEC);
    while (i<KernelConst.KC_INTCOUNT) createEntry(i++, destWOE); //also create entries for hardware interrupts
    //create new exc-table and copy old entries if existing
    newHandler=new ExcHandler[0x20];
    if (handler!=null) for (i=0; i<0x20; i++) newHandler[i]=handler[i];
    handler=newHandler;
    //enter DebugScreen as default
    debug=new DebugScreen();
    //create new irq-table and copy old entries is existing
    for (i=0; i<0x20; i++) if (handler[i]==null) handler[i]=debug;
    newDevices=new Device[KernelConst.KC_INTCOUNT-0x20];
    if (devList!=null) for (i=0; i<KernelConst.KC_INTCOUNT-0x20 && i<devList.length; i++)
      newDevices[i]=devList[i];
    devList=newDevices;
  }
  
  private static void createEntry(int no, int dest) {
    int addr, dstSel=KernelConst.KC_FIRSTINTINGDT+no;
    
    if (MAGIC.ptrSize==4) { //ia32
      //write entry in idt
      addr=KernelConst.KM_IDTADDR+(no<<3);
      MAGIC.wMem32(addr, (dest&0x0000FFFF)|(dstSel<<19));
      MAGIC.wMem32(addr+4, (dest&0xFFFF0000)|0x00008E00);
    }
    else { //amd64
      //write entry in idt
      addr=KernelConst.KM_IDTADDR+(no<<4);
      MAGIC.wMem32(addr, (dest&0x0000FFFF)|(dstSel<<19));
      MAGIC.wMem32(addr+4, (dest&0xFFFF0000)|0x00008E00);
      MAGIC.wMem32(addr+8, 0); //upper bits of address
      MAGIC.wMem32(addr+12, 0); //reserved
    }
    //copy source-selctor in gdt for this idt-entry
    MAGIC.wMem64(KernelConst.KM_GDTADDR+(dstSel<<3),
        MAGIC.rMem64(KernelConst.KM_GDTADDR+(1<<3)));
  }
  
  @SJC.Interrupt
  protected static void firstLevelHandlerWOE() {
    int cs=0, no;
    
    if (MAGIC.ptrSize==4) { //ia32
      MAGIC.inline(0x8C, 0x4D); MAGIC.inlineOffset(1, cs); //mov [ebp-0x04],cs
      MAGIC.inline(0x8B, 0x3D); MAGIC.inline32(KernelConst.KM_INTDESC); //mov edi,[KernelConst.KM_INTDESC]
    }
    else { //amd64
      MAGIC.inline(0x8C, 0x4D); MAGIC.inlineOffset(1, cs); //mov [ebp-0x08],cs
      MAGIC.inline(0x8B, 0x3C, 0x25); MAGIC.inline32(KernelConst.KM_INTDESC); //mov edi,[KernelConst.KM_INTDESC]
    }
    no=(cs>>>3)-KernelConst.KC_FIRSTINTINGDT;
    if (no>=handler.length) {
      no-=handler.length;
      if (devList[no]!=null) devList[no].handleIRQ(no);
      DeviceList.irq.ackIRQ(no);
    }
    else if (handler[no]!=null) handler[no].handle(no, false, 0);
  }
  
  @SJC.Interrupt
  protected static void firstLevelHandlerWEC(int ec) {
    int cs=0, no;
    
    if (MAGIC.ptrSize==4) { //ia32
      MAGIC.inline(0x8C, 0x4D); MAGIC.inlineOffset(1, cs); //mov [ebp-0x04],cs
      MAGIC.inline(0x8B, 0x3D); MAGIC.inline32(KernelConst.KM_INTDESC); //mov edi,[KernelConst.KM_INTDESC]
   }
    else { //amd64
      MAGIC.inline(0x8C, 0x4D); MAGIC.inlineOffset(1, cs); //mov [ebp-0x08],cs
      MAGIC.inline(0x8B, 0x3C, 0x25); MAGIC.inline32(KernelConst.KM_INTDESC); //mov edi,[KernelConst.KM_INTDESC]
    }
    no=(cs>>>3)-KernelConst.KC_FIRSTINTINGDT;
    if (handler[no]!=null) handler[no].handle(no, true, ec);
  }
}
