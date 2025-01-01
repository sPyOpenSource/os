package raytracer;

/**
 * The scene class encapsulates camera and world data.
 */

public abstract class Scene {
  /** eye position */
  public Vector3f eye;

  /** direction of view */
  public Vector3f lookat;

  /** up vector from eye position */
  public Vector3f up;

  /** field of view */
  public float fov;

  /** list of objects in the world */
  public ObjList objects;

  /** list of lights in the world */
  public ObjList lights;

  /** the RGBfCol coded color of the scene background */
  public long background;

  /**
   * Scene constructor reads all camera and world details from a file.
   */
  public Scene() {
    // Initialize various lists
    objects = new ObjList();
    lights = new ObjList();

    // Initialize defaults in case they aren't specified
    fov = 30.0f;
    if (eye == null)
      eye = new Vector3f(0.0f, 0.0f, 10.0f);
    if (lookat == null)
      lookat = new Vector3f(0.0f, 0.0f, 0.0f);
    if (up == null)
      up = new Vector3f(0.0f, 1.0f, 0.0f);
    background = RGBfCol.create(0.0f, 0.0f, 0.0f);
    createWorld();
  }

  /**
   * This is a pass-through function
   */

  public abstract void createWorld();
}