package raytracer;

/**
 * The following application demonstrates a simple ray tracer.
 */
public class RayTrace {
  /** horizontal direction of screen in worldspace */
  public Vector3f Du = new Vector3f();

  /** vertical direction of screen in worldspace */
  public Vector3f Dv = new Vector3f();

  /** vector from eye to topleft corner of screen in worldspace */
  public Vector3f Vp = new Vector3f();

  /** the world description, with objects, lights, and camera info */
  public Scene scene;

  /** the screen size */
  public int width, height;
  
  /** bgr instead of rgb color model */
  public boolean bgr;

  /**
   * Initializes an instance of the raytracer with the window size and the world
   * description.
   * 
   * @param int
   *          image width
   * @param int
   *          image height
   * @param Scene
   *          world description
   */
  public RayTrace(Scene s, boolean useBGR) {
    scene = s;
    bgr = useBGR;
  }

  public void init(int w, int h) {
    float fl;
    Vector3f look, crossp;

    width = w;
    height = h;
    // Compute viewing matrix that maps a
    // screen coordinate to a ray direction
    look = new Vector3f(scene.lookat.x - scene.eye.x, scene.lookat.y
        - scene.eye.y, scene.lookat.z - scene.eye.z);
    crossp = new Vector3f();
    crossp.cross(look, scene.up);
    Du.normalize(crossp);
    crossp.cross(look, Du);
    Dv.normalize(crossp);
    // fl = (width / (2.0f*FPU.tan((0.5f*scene.fov)*FPU.PI/180.0f)));
    fl = ((float) width)
        / (2.0f * (float) Math.tan((double) ((0.5f * scene.fov)
            * (float) Math.PI / 180.0f)));
    // fl=(0.5f*scene.fov)*FloatUtil.PI()/180.0f;
    // fl=((float)width) / (2.0f*FPU.sin(fl)/FPU.cos(fl));
    Vp.normalize(look);
    Vp.x = Vp.x * fl - 0.5f * ((float) width * Du.x + (float) height * Dv.x);
    Vp.y = Vp.y * fl - 0.5f * ((float) width * Du.y + (float) height * Dv.y);
    Vp.z = Vp.z * fl - 0.5f * ((float) width * Du.z + (float) height * Dv.z);
    
    //prepare scene
    // Compute viewing matrix that maps a
    // screen coordinate to a ray direction
    look = new Vector3f(scene.lookat.x - scene.eye.x, scene.lookat.y
        - scene.eye.y, scene.lookat.z - scene.eye.z);
    crossp = new Vector3f();
    crossp.cross(look, scene.up);
    Du.normalize(crossp);
    crossp.cross(look, Du);
    Dv.normalize(crossp);
    fl = (float) width
        / (2.0f * (float) Math.tan((double) ((0.5f * scene.fov)
            * (float) Math.PI / 180.0f)));
    Vp.normalize(look);
    Vp.x = Vp.x * fl - 0.5f * ((float) width * Du.x + (float) height * Dv.x);
    Vp.y = Vp.y * fl - 0.5f * ((float) width * Du.y + (float) height * Dv.y);
    Vp.z = Vp.z * fl - 0.5f * ((float) width * Du.z + (float) height * Dv.z);
  }
  
  /**
   * Render a line of pixel, by creating ray through the top-left corner of the
   * requested pixel, tracing the ray through the scene, and shading
   * accordingly.
   */
  public void renderLine(int[] dest, CalcTAinfo cTAi) {
    Vector3f dir;
    Ray ray;
    Renderable rayObject;
    Vector3f t1, t2, t3;
    int x;
    
    if (cTAi.var1 != null) dir = (Vector3f) cTAi.var1;
    else cTAi.var1 = dir = new Vector3f();
    if (cTAi.var2 != null) ray = (Ray) cTAi.var2;
    else cTAi.var2 = ray = new Ray(scene.eye, dir);
    if (cTAi.var3 != null) t1 = (Vector3f) cTAi.var3;
    else cTAi.var3 = t1 = new Vector3f();
    if (cTAi.var4 != null) t2 = (Vector3f) cTAi.var4;
    else cTAi.var4 = t2 = new Vector3f();
    if (cTAi.var5 != null) t3 = (Vector3f) cTAi.var5;
    else cTAi.var5 = t3 = new Vector3f();
    
    for (x=0; x<width; x++) {
      cTAi.x=x;
      dir.init((float) cTAi.x * Du.x + (float) cTAi.y * Dv.x + Vp.x,
          (float) cTAi.x * Du.y + (float) cTAi.y * Dv.y + Vp.y, (float) cTAi.x
              * Du.z + (float) cTAi.y * Dv.z + Vp.z);
      ray.init(scene.eye, dir);
      rayObject = ray.trace(scene.objects);
      dest[x] = RGBfCol.toRGB(rayObject != null ? rayObject.Shade(ray, scene, t1, t2, t3) : scene.background, bgr);
    }
  }
}
