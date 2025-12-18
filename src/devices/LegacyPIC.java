package devices;

import kernel.IRQController;

public class LegacyPIC extends IRQController {
  private final static int MASTER = 0x20;
  private final static int SLAVE  = 0xA0;
  
  @Override
  public void init() {
    programChip(MASTER, 0x20, 0x04); //master
    programChip(SLAVE,  0x28, 0x02); //slave
  }
  
  @Override
  public void ackIRQ(int no) {
    MAGIC.wIOs8(MASTER, (byte)0x20);
    if (no>=8) MAGIC.wIOs8(SLAVE, (byte)0x20);
  }
  
  private void programChip(int port, int offset, int icw3) {
    MAGIC.wIOs8(port++, (byte)0x11);   //ICW1
    MAGIC.wIOs8(port,   (byte)offset); //ICW2
    MAGIC.wIOs8(port,   (byte)icw3);   //ICW3
    MAGIC.wIOs8(port,   (byte)0x01);   //ICW4
  }
}
