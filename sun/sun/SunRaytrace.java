package sun;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import raytracer.*;

/**
 * The following application demonstrates a simple ray tracer.
 */
public class SunRaytrace extends Frame {
  private static final long serialVersionUID = 1L;

  public final static int XRES=800, YRES=600;
  
  /** the buffered image instance for drawing the rendered picture */ 
  private BufferedImage screen;

	public SunRaytrace() {
    super("SunRaytrace");
    setSize(XRES, YRES);
    screen = new BufferedImage(XRES, YRES, BufferedImage.TYPE_INT_RGB);
    Graphics gc = screen.getGraphics();
    gc.setColor(getBackground());
    gc.fillRect(0, 0, XRES, YRES);
    setVisible(true);

    // adds a window listener so that hitting the close button works 
    addWindowListener(new java.awt.event.WindowAdapter() {
      public void windowClosing(java.awt.event.WindowEvent e) {
        System.exit(0);
      }
    });
	}

  public void paint(Graphics g) {
    g.drawImage(screen, 0, 0, this);
  }

  public static void main(String[] args){
    int y, image[][], line[];
    
    if (args.length!=0) {
        System.err.println("Improper arguments.");
        System.exit(0);
    }
    SunRaytrace me=new SunRaytrace();
    image=new int[YRES][XRES];
    RayTrace rt = new RayTrace(new SceneDemo5(), false);
    rt.init(XRES, YRES);
    CalcTAinfo cTAi = new CalcTAinfo();
    for (y=0; y<YRES; y++) {
      rt.renderLine(line=image[cTAi.y=y], cTAi);
      me.screen.setRGB(0, y, XRES, 1, line, 0, XRES);
      me.paint(me.getGraphics());
    }
  }
}
