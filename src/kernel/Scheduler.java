package kernel;

import tools.KEvT;
import devices.DeviceList;
import raytracer.CalcTAinfo;
import raytracer.RayTrace;
import raytracer.SceneDemo5;

public class Scheduler {
  private final static int XRES=800, YRES=600;
  
  public static void run() {
    DeviceList.out.cls();
    DeviceList.out.print("Welcome to smfPicOS running in ");
    DeviceList.out.print(MAGIC.ptrSize << 3);
    DeviceList.out.println(" bit mode");
    printHelp();
    while(true) {
      printInput(); //print incoming keys
      printTime(72, 0, 0x17); //print the rtc
    }
  }
  
  private static void printHelp() {
    DeviceList.out.println("With F1 switch to virtual memory (check F2)");
    DeviceList.out.println("With F2 access memory at location 0xFFFFFFFC");
    DeviceList.out.println("With F5 switch to high resolution mode and show colors");
    DeviceList.out.println("With F6 switch to high resolution mode and raytrace picture");
    DeviceList.out.println("With F7 switch to low resolution mode and calc Mandelbrot picture");
    DeviceList.out.println("With F9 re-initialize display or switch back to normal textual mode");
    DeviceList.out.println("With Ctrl+Shift+Esc you can fire a sys-req resulting in a break point");
  }
  
  private static boolean initVesa() {
    if (DeviceList.vesa!=null) {
      if (!DeviceList.vesa.setMode(XRES, YRES, 24, true))
        DeviceList.out.println("installed VESA-card does not support requested mode");
      else return true;                 
    }
    else DeviceList.out.println("VESA not supported");
    return false;
  }
  
  private static void printInput() {
    long event;
    short modifier;
    int code, x, y;
    char c;
    RayTrace rt;
    CalcTAinfo cTAi;
    int image[][] = null, line[];
    
    while (DeviceList.in.eventAvail()) {
      event=DeviceList.in.fetchEvent();
      modifier=DeviceList.in.extractCode(event);
      code=DeviceList.in.extractCodeExt(event);
      c=DeviceList.kevt.getChar(modifier, code);
      if ((modifier&(KEvT.MD_ANY_CTRL|KEvT.MD_ANY_ALT|KEvT.MD_ANY_SHIFT))==(short)0) { //system key
        switch (DeviceList.kevt.getKey((short)0, code)) {
          case KEvT.KC_F1: //switch to virtual memory
            kernel.memory.VirtualMemory.init();
            break;
          case KEvT.KC_F2: //access int at position 0xFFFFFFFC to test page fault
            Kernel.directPrintInt(MAGIC.rMem32(0xFFFFFFFC), 16, 8, 0, 0, 0x0C, true);
            break;
          case KEvT.KC_F5: //switch to VESA mode and show colors
            if (initVesa()) for (y=0; y<YRES; y++) for (x=0; x<XRES; x++)
              DeviceList.vesa.setPixel(x, y, linInterPol2D(x, y, XRES, YRES, 0x0000FF, 0x00FF00, 0xFF0000, 0xFFFF00));
          	break;
          case KEvT.KC_F6: //switch to VESA mode and calc raytracer picture
            if (initVesa()) {
              if (image==null) image=new int[YRES][XRES];
              rt=new RayTrace(new SceneDemo5(), false);
              rt.init(XRES, YRES);
              cTAi=new CalcTAinfo();
              for (y=0; y<YRES; y++) {
                rt.renderLine(line=image[cTAi.y=y], cTAi);
                DeviceList.vesa.drawLine(y, line);
              }
            }
            break;
          case KEvT.KC_F7: //switch to low res graphic mode and calc Mandelbrot picture
            MAGIC.wMem32(BIOS.EAX, 0x13);
            BIOS.rint(0x10);
            for (y=0; y<200; y++) for (x=0; x<320; x++)
              MAGIC.wMem8(0xA0000+y*320+x, calcMandelPixel((float)(x-200)*0.0125f, (float)(y-100)*0.0125f));
            break;
          case KEvT.KC_F9: //reset textmode
            DeviceList.vga.setTextMode();
            printHelp();
            break;
        }
      }
      else { //normal key
        if (c=='\u001B') DeviceList.out.cls();
        else if (c!=(char)0) DeviceList.out.print(c);
      }
    }
  }
  
  private static void printTime(int x, int y, int col) {
    MAGIC.wIOs8(0x70, (byte)4);
    Kernel.directPrintInt((int)MAGIC.rIOs8(0x71)&0xFF, 16, 2, x, y, col, false);
    Kernel.directPrintChar(':', x+2, y, col);
    MAGIC.wIOs8(0x70, (byte)2);
    Kernel.directPrintInt((int)MAGIC.rIOs8(0x71)&0xFF, 16, 2, x+3, y, col, true);
    Kernel.directPrintChar(':', x+5, y, col);
    MAGIC.wIOs8(0x70, (byte)0);
    Kernel.directPrintInt((int)MAGIC.rIOs8(0x71)&0xFF, 16, 2, x+6, y, col, true);
  }
  
  private static int linInterPol2D(int x, int y, int xRes, int yRes, int lt, int rt, int lb, int rb) {
    return linInterPol1D(y, yRes, linInterPol1D(x, xRes, lt, rt), linInterPol1D(x, xRes, lb, rb));
  }
  
  private static int linInterPol1D(int x, int xRes, int l, int r) {
    return ((((l>>>16)*(xRes-x)+(r>>>16)*x)/xRes)<<16)
      |(((((l>>>8)&0xFF)*(xRes-x)+((r>>>8)&0xFF)*x)/xRes)<<8)
      |(((l&0xFF)*(xRes-x)+(r&0xFF)*x)/xRes);
  }
  
  private static byte calcMandelPixel(float xc, float yc) {
    float x, y, xn, yn;
    int iter;
    
    x=xc;
    y=yc;
    for (iter=0; iter<60; iter++) {
      xn=x*x-y*y+xc;
      yn=2.0f*x*y+yc;
      x=xn;
      y=yn;
      if (x*x+y*y>60.0f) return (byte)iter;
    }
    return (byte)0;
  }
}
