package raytracer;

/**
 * An object must implement a Renderable interface in order to be ray traced. It
 * should be straightforward to add new objects by implementing Renderable.
 */
public abstract class Renderable {
  /**
   * Checks whether the given ray ever intersects this object.
   * 
   * @param Ray
   *          to check against the object
   * @return float the closest t value at intersection, or -1 if there is no
   *         intersection.
   */
  public abstract float intersect(Ray r);

  /**
   * Shade a surface, given the world description and the ray.
   * 
   * @param r
   *          the ray to trace
   * @param scene
   *          lights, objects, background
   * @param three
   *          temporary vectors (are changed in method!)
   * @return RGBfCol shading color
   */
  public abstract long Shade(Ray r, Scene scene, Vector3f t1, Vector3f t2,
      Vector3f t3);

  /**
   * Calculate an onPoint to be a small step away from the surface
   */
  public float onPoint(float t) {
    return t - (t / 10000.0f);
  }
}
