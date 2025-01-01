package raytracer;

public class Vector3f {
  public float x, y, z;

  public Vector3f() {
    init(0.0f, 0.0f, 0.0f);
  }

  public Vector3f(Vector3f v1) {
    init(v1.x, v1.y, v1.z);
  }

  public Vector3f(float xI, float yI, float zI) {
    init(xI, yI, zI);
  }

  public void init(float xI, float yI, float zI) {
    x = xI;
    y = yI;
    z = zI;
  }

  public void normalize() {
    normalize(this);
  }

  public void normalize(Vector3f v1) {
    float len = (float) Math.sqrt((double) v1.lenSq());

    x = v1.x / len;
    y = v1.y / len;
    z = v1.z / len;
  }

  public float lenSq() {
    return x * x + y * y + z * z;
  }

  public void scale(float factor) {
    x *= factor;
    y *= factor;
    z *= factor;
  }

  public void add(Vector3f v1) {
    x += v1.x;
    y += v1.y;
    z += v1.z;
  }

  public void add(float vx, float vy, float vz) {
    x += vx;
    y += vy;
    z += vz;
  }

  public void addScaled(Vector3f v1, float factor) {
    x += factor * v1.x;
    y += factor * v1.y;
    z += factor * v1.z;
  }

  public void sub(Vector3f v1) {
    x -= v1.x;
    y -= v1.y;
    z -= v1.z;
  }

  public float dot(Vector3f v1) {
    return dot(v1.x, v1.y, v1.z);
  }

  public float dot(float vx, float vy, float vz) {
    return x * vx + y * vy + z * vz;
  }

  public void cross(Vector3f v1, Vector3f v2) {
    x = v1.y * v2.z - v1.z * v2.y;
    y = v1.z * v2.x - v1.x * v2.z;
    z = v1.x * v2.y - v1.y * v2.x;
  }

  // functions to avoid Vector3f-objects

  public static float lenSq(float x, float y, float z) {
    return x * x + y * y + z * z;
  }
}