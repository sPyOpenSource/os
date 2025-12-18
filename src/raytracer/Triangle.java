package raytracer;

/**
 * Triangle is an "Renderable" object.
 */
public class Triangle extends Renderable {
  /** epsilon */
  public final static float EPSILON = 0.0001f;

  /** The Triangle's surface description */
  public Surface surface;

  /** The Triangle's parameters */
  public Vector3f point1, point2, point3;

  public Triangle(Surface s, Vector3f v1, Vector3f v2, Vector3f v3) {
    surface = s;
    point1 = v1;
    point2 = v2;
    point3 = v3;
  }

  /**
   * Check whether this object intersects with the given ray.
   * 
   * @param ray to intersect
   * @return float -1 if no intersection, otherwise returns value of parametric
   *         value t at the intersection point
   */
  @Override
  public float intersect(Ray ray) {
    float t;
    float det, u, v;
    // following is to avoid "new" for new Vector3f
    float edge1x, edge1y, edge1z, edge2x, edge2y, edge2z;
    float px, py, pz, qx, qy, qz, tx, ty, tz;

    // edge1 = point2-point1
    edge1x = point2.x - point1.x;
    edge1y = point2.y - point1.y;
    edge1z = point2.z - point1.z;
    // edge2 = point3-point1
    edge2x = point3.x - point1.x;
    edge2y = point3.y - point1.y;
    edge2z = point3.z - point1.z;

    // pvec = cross(ray.direction, edge2)
    px = ray.direction.y * edge2z - ray.direction.z * edge2y;
    py = ray.direction.z * edge2x - ray.direction.x * edge2z;
    pz = ray.direction.x * edge2y - ray.direction.y * edge2x;

    // det = edge1.dot(pvec)
    det = edge1x * px + edge1y * py + edge1z * pz;
    if ((float) Math.abs((double) det) < EPSILON)
      return -1.0f;

    // calculate distance from point0 to ray origin
    // tvec = ray.origin-point1
    tx = ray.origin.x - point1.x;
    ty = ray.origin.y - point1.y;
    tz = ray.origin.z - point1.z;

    // calculate u parameter and test bounds
    // u = t.dot(p)
    u = (tx * px + ty * py + tz * pz) / det;
    if (u < 0.0f || u > 1.0f)
      return -1.0f;

    // prepare to test v parameter
    // qvec = t.cross(edge1)
    qx = ty * edge1z - tz * edge1y;
    qy = tz * edge1x - tx * edge1z;
    qz = tx * edge1y - ty * edge1x;

    // calculate v parameter and test bounds
    // v = ray.direction.dot(qvec)
    v = ray.direction.dot(qx, qy, qz) / det;
    if (v < 0.0f || u + v > 1.0f)
      return -1.0f;

    // calculate t parameter
    // t = edge2.dot(qvec)/det;
    t = (edge2x * qx + edge2y * qy + edge2z * qz) / det;

    if (ray.t < t)
      return -1.0f;
    return t;
  }

  /**
   * The shade function takes the description of the world and feeds data to a
   * surface shader. This function determines the point of intersection, surface
   * normal, and ray origin and passes them to the surface shader.
   * 
   * @param ray
   *          the intersecting ray
   * @param scene
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
    float t = onPoint(ray.t);
    float px = ray.origin.x + t * ray.direction.x;
    float py = ray.origin.y + t * ray.direction.y;
    float pz = ray.origin.z + t * ray.direction.z;
    float edge1x, edge1y, edge1z, edge2x, edge2y, edge2z;
    float nx, ny, nz, l1, l2;
    long col;

    // edge1 = point2-point1
    edge1x = point2.x - point1.x;
    edge1y = point2.y - point1.y;
    edge1z = point2.z - point1.z;
    // edge2 = point3-point1
    edge2x = point3.x - point1.x;
    edge2y = point3.y - point1.y;
    edge2z = point3.z - point1.z;

    nx = edge1y * edge2z - edge1z * edge2y;
    ny = edge1z * edge2x - edge1x * edge2z;
    nz = edge1x * edge2y - edge1y * edge2x;
    l1 = (ray.origin.x - px - nx) * (ray.origin.x - px - nx)
        + (ray.origin.y - py - ny) * (ray.origin.y - py - ny)
        + (ray.origin.z - pz - nz) * (ray.origin.z - pz - nz);
    l2 = (ray.origin.x - px + nx) * (ray.origin.x - px + nx)
        + (ray.origin.y - py + ny) * (ray.origin.y - py + ny)
        + (ray.origin.z - pz + nz) * (ray.origin.z - pz + nz);
    if (l2 < l1) {
      nx = -nx;
      ny = -ny;
      nz = -nz;
    }

    t1.init(px, py, pz);
    t2.init(nx, ny, nz);
    t2.normalize();
    t3.init(-ray.direction.x, -ray.direction.y, -ray.direction.z);

    col = surface.Shade(t1, t2, t3, scene, ray);

    return col;
  }
}
