package java.lang;

import rte.SClassDesc;

@SJC.IgnoreUnit
public class MAGIC {
  public static int ptrSize;
  public static boolean movable;
  public static boolean indirScalars;
  public static boolean streamline;
  public static boolean assignCall;
  public static boolean assignHeapCall;
  public static boolean runtimeBoundException;
  public static boolean runtimeNullException;
  public static int imageBase;
  public static int compressedImageBase;
  public static boolean embedded;
  public static boolean embConstRAM;
  public static int relocation;
  public static int comprRelocation;
  public static void inline(int p1) {}
  public static void inline(int p1, int p2) {}
  public static void inline(int p1, int p2, int p3) {}
  public static void inline(int p1, int p2, int p3, int p4) {}
  public static void inline(int p1, int p2, int p3, int p4, int p5) {}
  public static void inline(int p1, int p2, int p3, int p4, int p5, int p6) {}
  public static void inline16(int p1) {}
  public static void inline16(int p1, int p2) {}
  public static void inline16(int p1, int p2, int p3) {}
  public static void inline16(int p1, int p2, int p3, int p4) {}
  public static void inline16(int p1, int p2, int p3, int p4, int p5) {}
  public static void inline16(int p1, int p2, int p3, int p4, int p5, int p6) {}
  public static void inline32(int p1) {}
  public static void inline32(int p1, int p2) {}
  public static void inline32(int p1, int p2, int p3) {}
  public static void inline32(int p1, int p2, int p3, int p4) {}
  public static void inline32(int p1, int p2, int p3, int p4, int p5) {}
  public static void inline32(int p1, int p2, int p3, int p4, int p5, int p6) {}
  public static void inlineOffset(int p1, double p2) {}
  public static void inlineOffset(int p1, double p2, int p3) {}
  public static void inlineOffset(int p1, Object p2) {}
  public static void inlineOffset(int p1, Object p2, int p3) {}
  public static void inlineBlock(String blockName) {}
  public static void wMem64(int addr, long v) {}
  public static long rMem64(int addr) { return 0l; }
  public static void wMem32(int addr, int v) {}
  public static int rMem32(int addr) { return 0; }
  public static void wMem16(int addr, short v) {}
  public static short rMem16(int addr) { return (short)0; }
  public static void wMem8(int addr, byte v) {}
  public static byte rMem8(int addr) { return (byte)0; }
  public static void wIOs64(int addr, long v) {}
  public static long rIOs64(int addr) { return 0l; }
  public static void wIOs32(int addr, int v) {}
  public static int rIOs32(int addr) { return 0; }
  public static void wIOs16(int addr, short v) {}
  public static short rIOs16(int addr) { return (short)0; }
  public static void wIOs8(int addr, byte v) {}
  public static byte rIOs8(int addr) { return (byte)0; }
  public static int cast2Ref(Object o) { return 0; }
  public static Object cast2Obj(int addr) { return null; }
  public static int addr(double i) { return 0; }
  public static int addr(Object o) { return 0; }
  public static SClassDesc clssDesc(String clssName) { return null; }
  public static int mthdOff(String clssName, String mthdName) { return 0; }
  public static int getCodeOff() { return 0; }
  public static int getInstScalarSize(String clssName) { return 0; }
  public static int getInstRelocEntries(String clssName) { return 0; }
  public static void bitMem8(int addr, byte v, boolean set) {}
  public static void bitMem16(int addr, short v, boolean set) {}
  public static void bitMem32(int addr, int v, boolean set) {}
  public static void bitMem64(int addr, long v, boolean set) {}
  public static void bitIOs8(int addr, byte v, boolean set) {}
  public static void bitIOs16(int addr, short v, boolean set) {}
  public static void bitIOs32(int addr, int v, boolean set) {}
  public static void bitIOs64(int addr, long v, boolean set) {}
  public static Object cast2Struct(int addr) { return null; }
  public static int getRamAddr() { return 0; }
  public static int getRamSize() { return 0; }
  public static int getRamInitAddr() { return 0; }
  public static void useAsThis(Object newThis) {}
  public static int getConstMemorySize() { return 0; }
  public static void doStaticInit() {}
  public static byte[] toByteArray(String s) { return null; }
  public static char[] toCharArray(String s) { return null; }
  public static String getNamedString(String s) { return null; }
  public static void ignore(int dummy) {}
  public static void ignore(long dummy) {}
  public static void ignore(Object o) {}
  public static void stopBlockCoding() {}
  public static void assign(int dest, int value) {}
  public static void assign(long dest, long value) {}
  public static void assign(Object dest, Object value) {}

    public static byte rIOs8(short s, int i) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
}
