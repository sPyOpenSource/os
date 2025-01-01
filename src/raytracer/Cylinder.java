package raytracer;

public class Cylinder extends Renderable {
  public Vector3f origin;
  public Vector3f direction; // normed vector
  public float radiusSq;
  public Surface surface;

  public Cylinder(Surface is, Vector3f io, float ir, Vector3f id, boolean idCopy) {
    surface = is;
    origin = io;
    radiusSq = ir * ir;
    if (idCopy) {
      direction = new Vector3f();
      direction.normalize(id);
    }
    else {
      direction = id;
      direction.normalize();
    }
  }

  public float intersect(Ray ray) {
    float a, b, c, sc;
    float t, det, sqrtDet, tMin, tMax;
    float ax, ay, az, bx, by, bz; // this should be alpha, beta - but we don't
                                  // want "new"

    ax = ray.origin.x - origin.x;
    ay = ray.origin.y - origin.y;
    az = ray.origin.z - origin.z;
    sc = ax * direction.x + ay * direction.y + az * direction.z;
    ax -= direction.x * sc;
    ay -= direction.y * sc;
    az -= direction.z * sc;

    bx = ray.direction.x;
    by = ray.direction.y;
    bz = ray.direction.z;
    sc = bx * direction.x + by * direction.y + bz * direction.z;
    bx -= direction.x * sc;
    by -= direction.y * sc;
    bz -= direction.z * sc;

    a = Vector3f.lenSq(bx, by, bz);
    b = 2.0f * (ax * bx + ay * by + az * bz);
    c = Vector3f.lenSq(ax, ay, az) - radiusSq;

    if (a == 0.0f) {
      if (b != 0.0f) {
        t = (-c) / b;
        if (t > 0.0f)
          return onPoint(t);
      }
      return -1.0f;
    }

    det = b * b - 4.0f * a * c;
    if (det <= 0.0f)
      return -1.0f;

    if ((sqrtDet = (float) Math.sqrt((double) det)) < 0.0f) {
      tMin = (-b + sqrtDet) / (2.0f * a);
      tMax = (-b - sqrtDet) / (2.0f * a);
    }
    else {
      tMin = (-b - sqrtDet) / (2.0f * a);
      tMax = (-b + sqrtDet) / (2.0f * a);
    }
    if (tMax < 0.0f)
      return -1.0f;
    if (tMin > 0.0f)
      return onPoint(tMin);
    return onPoint(tMax);
  }

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
    float sc;
    long col;

    t1.init(px, py, pz);
    t2.init(px - origin.x, py - origin.y, pz - origin.z);
    sc = t2.dot(direction);
    t2.addScaled(direction, -sc); // calculate the projection with our direction
    t2.normalize();
    t3.init(-ray.direction.x, -ray.direction.y, -ray.direction.z);

    // The illumination model is applied
    // by the surface's Shade() method
    col = surface.Shade(t1, t2, t3, scene, ray);

    return col;
  }
}
