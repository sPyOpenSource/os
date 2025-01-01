package kernel.memory;

import kernel.Kernel;
import kernel.ExcHandler;

public class PageFault extends ExcHandler {
  public void handle(int no, boolean withError, int ec) {
    int cr2=0;

    Kernel.directPrintInt(ec, 16, 8, 0, 0, 0x1E, true);
    MAGIC.inline(0x0F, 0x20, 0xD0); //mov e/rax,cr2
    MAGIC.inline(0x89, 0x45); MAGIC.inlineOffset(1, cr2); //mov [e/rbp-4/8],eax (skip upper 32 bit in 64 bit mode)
    Kernel.directPrintInt(cr2, 16, 8, 0, 1, 0x1F, true);
    while(true);
  }
}
