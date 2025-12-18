package kernel.memory;

import kernel.Kernel;
import kernel.KernelConst;
import rte.SClassDesc;
import rte.DynamicRuntime;
import rte.SArray;

public class BasicMemory extends MemIntf {
  private static int nextFreeAddress;
  private static int lastObjectAddress;
  
  public static void init() {
    //get size of code-image and prepare BasicMemory to start allocation at the next page
    nextFreeAddress = (MAGIC.imageBase
        + (MAGIC.rMem32(MAGIC.imageBase + KernelConst.KM_IMAGESIZEREL) + 0xFFF)) & ~0xFFF;
    //get an instance of BasicMemory allocated with BasicMemory :)
    Kernel.mem = (MemIntf)basicAllocate(
        MAGIC.getInstScalarSize("BasicMemory"),
        MAGIC.getInstRelocEntries("BasicMemory"),
        MAGIC.clssDesc("BasicMemory"));
  }
  
  @Override
  public Object allocate(int scalarSize, int relocEntries, SClassDesc type) {
    return basicAllocate(scalarSize, relocEntries, type);
  }
  
  //allocate normal object
  public static Object basicAllocate(int scalarSize, int relocEntries, SClassDesc type) {
    int rs, size, objPtr;
    Object me;
    
    //prepare 
    rs=relocEntries*MAGIC.ptrSize;
    scalarSize=(scalarSize+(MAGIC.ptrSize-1))&~(MAGIC.ptrSize-1);
    size=rs+scalarSize;
    //prepare object
    Kernel.setMem32(nextFreeAddress, size>>2, 0); //clear memory
    objPtr=nextFreeAddress+rs; //pointer to header
    me=MAGIC.cast2Obj(objPtr);
    MAGIC.assign(me._r_type, type); //place object and set type
    MAGIC.assign(me._r_relocEntries, relocEntries); //set amount of relocs
    MAGIC.assign(me._r_scalarSize, scalarSize); //set adjusted size in scalars
    //update heap-info-structure
    if (lastObjectAddress!=0) MAGIC.assign(MAGIC.cast2Obj(lastObjectAddress)._r_next, me); //enter next-object in last object
    //remember the work
    lastObjectAddress=objPtr; //remember this object as last object
    nextFreeAddress+=size; //set next free address behind this object
    return me;
  }
  
  //allocate space for kernel, wrapped in a byte-array
  public static int allocateKernelBlock(int size, int maskBits) {
    SArray dest;
    int safety=MAGIC.getInstScalarSize("SArray")+maskBits;
    dest=DynamicRuntime.newArray(safety+size+1, 1, 1, 1, null);
    return ((int)MAGIC.cast2Ref(dest)+safety)&~maskBits;
  }
}
