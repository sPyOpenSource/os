package java.util;
public class BitSet {
   public void set(int arg0) { throw new Error("NOT IMPLEMENTED"); }
   public boolean get(int arg0) { throw new Error("NOT IMPLEMENTED"); }
   public void BitSet(int arg0) { throw new Error("NOT IMPLEMENTED"); }
   public BitSet(int nbits) {
        // nbits can't be negative; size 0 is OK
        if (nbits < 0)
            throw new NegativeArraySizeException("nbits < 0: " + nbits);

        //initWords(nbits);
        //sizeIsSticky = true;
    }

    public int nextClearBit(int i) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
