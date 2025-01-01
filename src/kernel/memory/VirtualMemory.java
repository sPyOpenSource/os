package kernel.memory;

import kernel.Kernel;
import kernel.KernelConst;
import rte.SClassDesc;
import devices.DeviceList;

public class VirtualMemory extends MemIntf {
  private static int pagetables;
  
  public static void init() { //BasicMemory is installed already, new is working
    int i;
    
    if (!(Kernel.mem instanceof BasicMemory)) {
      DeviceList.out.println("BasicMemory already replaced");
      return;
    }
    DeviceList.out.print("switching to VirtualMemory...");
    Kernel.ints.cli();
    if (MAGIC.ptrSize==4) { //protected mode
      //allocate space for pagetables
      pagetables=BasicMemory.allocateKernelBlock(4*1024*1024, 0xFFF);
      //create top-level page directory
      for (i=0; i<1024; i++) MAGIC.wMem32((i<<2)+KernelConst.KM_PAGEDIR, ((i<<12)+pagetables)|3);
      //create pagetables as 1:1-mapping for 0-4 GB (complete 32 bit address space) except last page (for tests)
      for (i=0; i<1024*1024-1; i++) MAGIC.wMem32((i<<2)+pagetables, (i<<12)|0x3);
      MAGIC.wMem32(1024*1024-1+pagetables, 0);
      //set pagedirectory register cr3
      MAGIC.inline(0xB8); MAGIC.inline32(KernelConst.KM_PAGEDIR); //mov eax,KernelConst.KM_PAGEDIR
      MAGIC.inline(0x0F, 0x22, 0xD8); //mov cr3,eax
      //enable paging via cr0
      MAGIC.inline(0x0F, 0x20, 0xC0); //mov eax,cr0
      MAGIC.inline(0x0D, 0x00, 0x00, 0x01, 0x80); //or eax,0x80010000
      MAGIC.inline(0x0F, 0x22, 0xC0); //mov cr0,eax
    }
    else { //long mode
      //allocate space for pagetables
      pagetables=BasicMemory.allocateKernelBlock(4*8*512, 0xFFF);
      //clear upper two levels of page directories
      Kernel.setMem32(KernelConst.KM_PAGEDIR, 5*4096/4, 0);
      //initialize level one and two
      MAGIC.wMem32(KernelConst.KM_PAGEDIR, (KernelConst.KM_PAGEDIR+4096)|3);
      for (i=0; i<4; i++) MAGIC.wMem32(KernelConst.KM_PAGEDIR+4096+(i<<3), ((i<<12)+pagetables)|3);
      //create level three page directory as 1:1-mapping for 0-4 GB except last page (for tests)
      for (i=0; i<4*512-1; i++) MAGIC.wMem32((i<<3)+pagetables, (i<<21)|0x83);
      //set pagedirectory register cr3
      MAGIC.inline(0xB8); MAGIC.inline32(KernelConst.KM_PAGEDIR); //mov eax,KernelConst.KM_PAGEDIR
      MAGIC.inline(0x0F, 0x22, 0xD8); //mov cr3,rax
      //paging already enabled in long mode, do not change cr0
    }
    //everything done, replace allocation-strategy
    Kernel.mem=new VirtualMemory();
    Kernel.ints.replaceExcHandler(0x0E, new PageFault());
    Kernel.ints.sti();
    DeviceList.out.println(" done");
  }

  public Object allocate(int scalarSize, int relocEntries, SClassDesc type) {
    return BasicMemory.basicAllocate(scalarSize, relocEntries, type);
  }
}
