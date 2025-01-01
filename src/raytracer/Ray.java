package raytracer;

/**
 * The Ray class is a standard geometric ray; it contains an origin point and a
 * direction. It also has support for object intersection and tracing. It can be
 * treated as a parametric line, where 0 is the origin and positive t moves in
 * the direction of the ray.
 */

public class Ray {
  /**
   * MAX_T is the farthest distance the ray can travel.
   */
  public float MAX_T = 1e38f; // should be Float.MAX_VALUE

  /** origin point of the ray */
  public Vector3f origin;

  /** normalized direction of the ray */
  public Vector3f direction;

  /** parametric t value */
  public float t;

  /**
   * This is the sole constructor; it normalizes the direction vector.
   * 
   * @param Vector3f
   *          eyepoint
   * @param Vector3f
   *          direction
   */
  public Ray(Vector3f eye, Vector3f dir) {
    origin = new Vector3f();
    direction = new Vector3f();
    init(eye, dir);
  }

  public void init(Vector3f eye, Vector3f dir) {
    origin.init(eye.x, eye.y, eye.z);
    direction.normalize(dir);
  }

  /**
   * This function casts the ray from the origin to infinity, stopping when it
   * hits any object.
   * 
   * @param Vector
   *          of objects to test for intersection
   * @return boolean whether any object was hit
   */
  public boolean cast(ObjList objList) {
    // It's really only useful for shadow casting.

    float tval = 0.0f;
    Renderable objTemp;

    t = MAX_T;
    // object = null;
    while (objList != null) {
      objTemp = (Renderable) objList.elem;
      objList = objList.next;
      tval = objTemp.intersect(this);

      // if returns a t > 0, we have a hit
      if (tval > 0.0f)
        return true;
    }

    // if we get here, we never hit any object
    return false;
  }

  /**
   * This function traces the ray from the origin to infinity, checking every
   * object to determine which is closest.
   * 
   * @param Vector
   *          of objects to test for intersection
   * @return boolean whether any object was hit
   */
  public Renderable trace(ObjList objList) {
    Renderable objTemp, objRet;
    float tval = 0.0f;
    t = MAX_T;

    objRet = null;
    while (objList != null) {
      objTemp = (Renderable) objList.elem;
      objList = objList.next;
      tval = objTemp.intersect(this);

      // V1.1: Changed the condition so that it will
      // test every single object in the world, not just
      // the first.

      // if returns a t > 0, we have a hit
      if (tval > 0.0f && tval < t) {
        t = tval;
        objRet = objTemp;
      }
    }

    return objRet;
  }
}
