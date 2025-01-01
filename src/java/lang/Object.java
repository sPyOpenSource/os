package java.lang;

import rte.*;

public class Object {
  //all fields are set in DynamicRuntime.newInstance
  public final int _r_relocEntries=0, _r_scalarSize=0; //fixed by compiler: scalar-position 0 / 1
  public final SClassDesc _r_type=null; //fixed by compiler: reloc-position 0
  public final Object _r_next=null; //fixed by compiler: reloc-position 1
}
