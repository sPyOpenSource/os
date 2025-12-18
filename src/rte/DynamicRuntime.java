package rte;

public class DynamicRuntime {
  public static Object newInstance(int scalarSize, int relocEntries, SClassDesc type) {
    return kernel.Kernel.mem.allocate(scalarSize, relocEntries, type);
  }
  
  public static SArray newArray(int length, int arrDim, int entrySize, int stdType,
      SClassDesc unitType) { //unitType is not for sure of type SClassDesc
    int scS, rlE;
    SArray me;
    
    if (stdType == 0 && unitType._r_type != MAGIC.clssDesc("SClassDesc"))
      MAGIC.inline(0xCC); //check type of unitType, we don't support interface arrays
    scS = MAGIC.getInstScalarSize("SArray");
    rlE = MAGIC.getInstRelocEntries("SArray");
    if (arrDim > 1 || entrySize < 0) rlE += length;
    else scS += length * entrySize;
    me = (SArray)newInstance(scS, rlE, MAGIC.clssDesc("SArray"));
    MAGIC.assign(me.length, length);
    MAGIC.assign(me._r_dim, arrDim);
    MAGIC.assign(me._r_stdType, stdType);
    MAGIC.assign(me._r_unitType, unitType);
    return me;
  }
  
  public static void newMultArray(SArray[] parent, int curLevel, int destLevel,
      int length, int arrDim, int entrySize, int stdType, SClassDesc clssType) {
    int i;
    
    if (curLevel + 1 < destLevel) { //step down one level
      curLevel++;
      for (i = 0; i < parent.length; i++) {
        newMultArray((SArray[])((Object)parent[i]), curLevel, destLevel,
            length, arrDim, entrySize, stdType, clssType);
      }
    } else { //create the new entries
      destLevel = arrDim - curLevel;
      for (i = 0; i < parent.length; i++) {
        parent[i] = newArray(length, destLevel, entrySize, stdType, clssType);
      }
    }
  }
  
  public static boolean isInstance(Object o, SClassDesc dest, boolean asCast) {    
    if (o == null) {
        //null matches all
        //null is not an instance
      return asCast; 
    }
    SClassDesc check = o._r_type;
    while (check != null) {
      if (check == dest) return true;
      check = check.parent;
    }
    if (asCast) MAGIC.inline(0xCC);
    return false;
  }
  
  public static SIntfMap isImplementation(Object o, SIntfDesc dest, boolean asCast) {
    SIntfMap check;
    
    if (o == null) return null;
    check = o._r_type.implementations;
    while (check != null) {
      if (check.owner == dest) return check;
      check = check.next;
    }
    if (asCast) MAGIC.inline(0xCC);
    return null;
  }
  
  public static boolean isArray(SArray o, int stdType, SClassDesc clssType, int arrDim, boolean asCast) {
    SClassDesc clss;
    
    //in fact o is of type "Object", _r_type has to be checked below - but this check is faster than "instanceof" and conversion
    if (o == null) {
        //null matches all
        //null is not an instance
      return asCast; 
    }
    if (o._r_type != MAGIC.clssDesc("SArray")) { //will never match independently of arrDim
      if (asCast) MAGIC.inline(0xCC);
      return false;
    }
    if (clssType == MAGIC.clssDesc("SArray")) { //special test for arrays
      if (o._r_unitType == MAGIC.clssDesc("SArray")) arrDim--; //an array of SArrays, make next test to ">=" instead of ">"
      if (o._r_dim > arrDim) return true; //at least one level has to be left to have an object of type SArray
      if (asCast) MAGIC.inline(0xCC);
      return false;
    }
    //no specials, check arrDim and check for standard type
    if (o._r_stdType != stdType || o._r_dim < arrDim) { //check standard types and array dimension
      if (asCast) MAGIC.inline(0xCC);
      return false;
    }
    if (stdType != 0) {
      if (o._r_dim == arrDim) return true; //array of standard-type matching
      if (asCast) MAGIC.inline(0xCC);
      return false;
    }
    //array of objects, make deep-check for class type (PicOS does not support interface arrays)
    if (o._r_unitType._r_type != MAGIC.clssDesc("SClassDesc")) MAGIC.inline(0xCC);
    clss=o._r_unitType;
    while (clss != null) {
      if (clss == clssType) return true;
      clss = clss.parent;
    }
    if (asCast) MAGIC.inline(0xCC);
    return false;
  }
  
  public static void checkArrayStore(SArray dest, SArray newEntry) {
    if (dest._r_dim > 1) isArray(newEntry, dest._r_stdType, dest._r_unitType, dest._r_dim - 1, true);
    else if (dest._r_unitType == null) MAGIC.inline(0xCC);
    else isInstance(newEntry, dest._r_unitType, true);
  }
}
