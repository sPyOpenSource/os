package kernel;


public class DebugScreen extends ExcHandler {
  public void handle(int no, boolean withError, int ec) {
    MAGIC.wMem32(0xB8000, -1);
    kernel.Kernel.directPrintInt(no, 16, 2, 2, 0, 0x1C, true);
    MAGIC.wMem32(0xB8008, -1);
    while (true);
  }
}
