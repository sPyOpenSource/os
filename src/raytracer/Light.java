package raytracer;

/**
 * The Light class represents a light emitter in the scene. Lights can be of
 * multiple types (ambient, directional, point). Light has no methods except a
 * constructor; it is essentially a data structure.
 * 
 * There are loads of public variables so that lights and surfaces can get
 * along; these should probably be abstracted.
 */

public class Light {
  /** denotes ambient light */
  public static final int AMBIENT = 0;

  /** denotes directional light (not a spotlight) */
  public static final int DIRECTIONAL = 1;

  /** denotes a hypothetical but ever-present point light */
  public static final int POINT = 2;

  /** stores the type of light, as per enumerations above */
  public int lightType;

  /**
   * This stores the position of a point light, or, the direction of a
   * directional light.
   */
  public Vector3f lvec;

  /**
   * These hold the intensity of the light source in red, green, blue
   */
  public float ir, ig, ib;

  /**
   * Sole constructor; only operation is to normalize a directional light's
   * direction and clamp intensities.
   * 
   * @param int
   *          the type of light
   * @param Vector3f
   *          the position or direction
   * @param float
   *          red light color, from 0 to 1
   * @param float
   *          green light color, from 0 to 1
   * @param float
   *          blue light color, from 0 to 1
   */
  public Light(int type, Vector3f v, float r, float g, float b) {
    lightType = type;
    ir = r;
    ig = g;
    ib = b;

    // clamp intensity values
    if (ir < 0f)
      ir = 0f;
    if (ig < 0f)
      ig = 0f;
    if (ib < 0f)
      ib = 0f;
    if (ir > 1f)
      ir = 1f;
    if (ig > 1f)
      ig = 1f;
    if (ib > 1f)
      ib = 1f;

    if (type != AMBIENT) {
      lvec = v;
      if (type == DIRECTIONAL)
        lvec.normalize();
    }
  }
}
