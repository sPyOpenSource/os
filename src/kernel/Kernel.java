package kernel;

import devices.DeviceList;
import kernel.memory.BasicMemory;
import kernel.memory.MemIntf;

public class Kernel {
  public static MemIntf mem;
  public static Interrupts ints;
  
  public static void main() {
    MAGIC.inline(0x9B, 0xDB, 0xE3); //FINIT
    directPrintChar('N', 1, 0, 0x07); BasicMemory.init();
    directPrintChar('I', 1, 0, 0x07); Interrupts.init();
    directPrintChar('D', 1, 0, 0x07); DeviceList.initDevices();
    directPrintChar('E', 1, 0, 0x07); ints.sti();
    directPrintChar('S', 1, 0, 0x07); Scheduler.run(); 
  }
  
  public static void directPrintInt(int val, int base, int len,
      int x, int y, int col, boolean leadingZero) {
    int i, addr, dignum, digchr;
    long uval, ubase;

    if (len<1 || base<2 || base>16 || x<0 || x+len>80 || y<0 || y>24) return; //invalid parameter
    addr=0xB8000+((y*80+x)<<1); //address of first character
    col=(col&0xFF)<<8; //color is upper byte -> mask and shift
    uval=((long)val)&0xFFFFFFFFl;
    ubase=(long)base;
    for (i=0; i<len; i++) {
      dignum=(int)(uval%ubase);
      uval=uval/ubase;
      if (dignum==0) {
        if (leadingZero || i==0 || uval!=0l) digchr=48;
        else digchr=32;
      }
      else if (dignum<10) digchr=dignum+48;
      else digchr=dignum+55;
      if (i==len-1 && uval!=0l) digchr=0x3E; //">" to show "overflow"
      MAGIC.wMem16(addr+((len-i-1)<<1), (short)(col|digchr));
    }
  }
  
  public static void directPrintChar(char c, int x, int y, int col) {
    MAGIC.wMem16(0xB8000+((y*80+x)<<1), (short)((((int)c)&0xFF)|(col<<8)));
  }

  public static void setMem32(int addr, int cnt, int val) {
    MAGIC.inline(0x57); //push e/rdi
    MAGIC.inline(0xFC); //cld
    MAGIC.inline(0x8B, 0x7D); MAGIC.inlineOffset(1, addr); //mov edi,[e/rbp+16/32]
    MAGIC.inline(0x8B, 0x45); MAGIC.inlineOffset(1, val); //mov eax,[e/rbp+8/16]
    MAGIC.inline(0x8B, 0x4D); MAGIC.inlineOffset(1, cnt); //mov ecx,[e/rbp+12+24]
    MAGIC.inline(0xF3, 0xAB); //rep stosd
    MAGIC.inline(0x5F); //pop e/rdi
  }
}
