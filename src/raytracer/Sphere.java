package raytracer;

/**
 * Sphere is an example "Renderable" object. Spheres are about as easy as it
 * gets when it comes to intersection.
 */
public class Sphere extends Renderable {

  /** The Sphere's surface description */
  public Surface surface;

  /** The center point of the sphere */
  public Vector3f center;

  /** The radius of the sphere */
  public float radius;

  /** Precomputed square of the radius */
  public float radSqr;

  /**
   * Sole constructor for a sphere
   * 
   * @param Surface
   *          the sphere's surface
   * @param Vector3f
   *          the center of the sphere
   * @param float
   *          the radius of the sphere
   */
  public Sphere(Surface s, Vector3f c, float r) {
    surface = s;
    center = c;
    radius = r;
    radSqr = r * r;
  }

  /**
   * Check whether this object intersects with the given ray.
   * 
   * @param Ray
   *          ray to intersect
   * @return float -1 if no intersection, otherwise returns value of parametric
   *         value t at the intersection point
   */
  public float intersect(Ray ray) {
    float dx = center.x - ray.origin.x;
    float dy = center.y - ray.origin.y;
    float dz = center.z - ray.origin.z;
    float v = ray.direction.dot(dx, dy, dz);
    float t;

    // Do the following quick check to see if there is even a chance
    // that an intersection here might be closer than a previous one
    if (v - radius > ray.t)
      return -1.0f;

    // Test if the ray actually intersects the sphere
    t = radSqr + v * v - dx * dx - dy * dy - dz * dz;
    if (t < 0.0f)
      return -1.0f;

    // Test if the intersection is in the positive
    // ray direction and it is the closest so far
    t = v - ((float) Math.sqrt((double) t));
    // t = v - (FPU.sqrt(t)); //buggy
    if ((t > ray.t) || (t < 0.0f))
      return -1.0f;

    return t;
  }

  /**
   * The shade function takes the description of the world and feeds data to a
   * surface shader. This function determines the point of intersection, surface
   * normal, and ray origin and passes them to the surface shader.
   * 
   * @param Ray
   *          the intersecting ray
   * @param Scene
   *          lights, objects, background
   * @return RGBfCol the shaded color
   */
  public long Shade(Ray ray, Scene scene, Vector3f t1, Vector3f t2, Vector3f t3) {
    // An object shader doesn't really do too much other than
    // supply a few critical pieces of geometric information
    // for a surface shader. It must must compute
    // 1. the point of intersection (p)
    // 2. a unit-length surface normal (n)
    // 3. a unit-length vector towards the ray's origin (v)
    float px = ray.origin.x + ray.t * ray.direction.x;
    float py = ray.origin.y + ray.t * ray.direction.y;
    float pz = ray.origin.z + ray.t * ray.direction.z;
    long col;

    t1.init(px, py, pz);
    t2.init(px - center.x, py - center.y, pz - center.z);
    t2.normalize();
    t3.init(-ray.direction.x, -ray.direction.y, -ray.direction.z);

    // The illumination model is applied
    // by the surface's Shade() method
    col = surface.Shade(t1, t2, t3, scene, ray);

    return col;
  }
}
