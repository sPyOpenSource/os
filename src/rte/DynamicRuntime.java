package rte;

public class DynamicRuntime {
  public static Object newInstance(int scalarSize, int relocEntries, SClassDesc type) {
    return null;
  }
  
  public static SArray newArray(int length, int arrDim, int entrySize, int stdType, SClassDesc unitType) { //change to Object if interface arrays are used
    return null;
  }
  
  public static void newMultArray(SArray[] parent, int curLevel, int destLevel,
      int length, int arrDim, int entrySize, int stdType, SClassDesc unitType) {
      //change unitType to Object if interface arrays are used
  }
  
  public static boolean isInstance(Object o, SClassDesc dest, boolean asCast) {
    return false;
  }
  
  public static SIntfMap isImplementation(Object o, SIntfDesc dest, boolean asCast) {
    return null;
  }
  
  public static boolean isArray(SArray o, int stdType, SClassDesc unitType,
      int arrDim, boolean asCast) { //change unitType to Object if interface arrays are used
    return false;
  }
}
