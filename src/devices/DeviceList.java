package devices;

import tools.EventBuffer;
import tools.KEvT;
import kernel.IRQController;
import kernel.Kernel;

public class DeviceList {
  public static Timer timer;
  public static Keyboard keyb;
  public static EventBuffer in;
  public static KEvT kevt;
  public static VGAText vga;
  public static VESAGraphics vesa;
  public static Viewer out;
  public static IRQController irq;
  
  public static void initDevices() {
    //program legacy hardware interrupt controller
    (irq = new LegacyPIC()).init();
    //initialize output and helpers
    if ((vga = vesa = VESAGraphics.detectDevice()) == null) vga = new VGAText();
    out = new Viewer(vga);
    //initialize input and helpers
    in = new EventBuffer(512);
    (kevt = new KEvT()).useTable(49);
    //initialize devices and insert handlers
    Kernel.ints.replaceIRQHandler(0, timer = new Timer());
    Kernel.ints.replaceIRQHandler(1, keyb = new Keyboard());
  }
}
