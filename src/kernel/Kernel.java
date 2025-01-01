package kernel;

//small SJC-example assuming a LED at PA2

public class Kernel {
  @SJC(enterCodeAddr=AVR.STARTMETHOD)
  public static void main() {
    boolean state=false;
    
    //initialize default levels
    MAGIC.bitMem8(AVR.PORTA, (byte)0x04, state);

    //initialize port directions
    MAGIC.wMem8(AVR.DDRA, (byte)(0x01));
    
    //initialize gobals
    initRAM();
    
    //main loop
    while (true) {
      //toggle LED
      state=!state;
      MAGIC.bitMem8(AVR.PORTA, (byte)0x04, state);
      //wait a bit
      for (int i=0; i<10000; i++) { /*wait*/ }
    }
  }
  
  @SJC.Inline
  private static void initRAM() { //initialize RAM
    short m, src, dst;
    
    //get start of RAM-init-area in flash
    src=(short)MAGIC.getRamInitAddr();
    
    //get maximum of RAM-init-area in flash
    m=(short)((dst=(short)MAGIC.getRamAddr())+(short)MAGIC.getRamSize()+(short)MAGIC.getConstMemorySize());
    
    //copy each byte from flash to RAM
    while (dst<m) MAGIC.wMem8(dst++, MAGIC.rIOs8(src++, 1));
    //old version using rFlash8 below: while (dst<m) MAGIC.wMem8(dst++, rFlash8(src++));
  }
}
