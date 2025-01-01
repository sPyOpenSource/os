package raytracer;

/**
 * Surface qualities associated with a Renderable object. All objects have a
 * single homogeneous surface.
 */
public class Surface {
  /** surface's intrinsic color */
  public float ir, ig, ib;

  /** phong model: ambient coefficent of surface */
  public float ka;

  /** phong model: diffuse coefficent of surface */
  public float kd;

  /** phong model: specular coefficient of surface */
  public float ks;

  /** phong model: shininess of surface */
  public float ns;

  /**
   * fraction of the transmitted ray's intensity that is reflected to the eye
   */
  public float kt;

  /**
   * Reflective constant of surface. Shininess only affects reflection of
   * lights, whereas this value affects reflection of the sight ray.
   */
  public float kr;

  /**
   * ratio of the surrounding material's index of refraction to the object's
   * index of refraction. Nt is Ni over Nr. Ni is the index of refraction for
   * air, that is, 1. Nr is the index of refraction for the object. Water, for
   * example, has an index of 1.33. Nt would be 1/1.33.
   */
  public float nt;

  /** handy small number */
  private static final float TINY = 0.00001f;

  /**
   * Sole constructor, which builds the Surface instance by being given all
   * values. Notable parameter is the reflection value, which is divided by 255
   * automatically.
   * 
   * @param float
   *          rval red
   * @param float
   *          gval green
   * @param float
   *          bval blue
   * @param float
   *          a ambient coefficient
   * @param float
   *          d diffuse coefficient
   * @param float
   *          s specular coefficient
   * @param float
   *          n shininess
   * @param float
   *          r reflectivity
   * @param float
   *          t transparency
   * @param float
   *          nt refractive index
   */

  public Surface(float rval, float gval, float bval, float a, float d, float s,
      float n, float r, float t, float index) {
    ir = rval;
    ig = gval;
    ib = bval;
    ka = a;
    kd = d;
    ks = s;
    ns = n;
    kr = r;
    kt = t;
    nt = index;
  }

  /**
   * This is the true shading function where work gets done. This function
   * determines the color value of a ray hitting this surface by calculating the
   * phong illumination model and spawning the appropriate secondary rays.
   * 
   * @return RGBfCol the color of the surface at the intersection pt
   * @param Vector3f
   *          p the point of intersection
   * @param Vector3f
   *          n the unit-length surface normal
   * @param Vector3f
   *          v the unit-length vector towards the ray origin
   * @param Scene
   *          lights, objects, color
   */
  public long Shade(Vector3f t1, Vector3f t2, Vector3f t3, Scene scene,
      Ray tempRay) {
    // start with shadow vectors to each light source
    ObjList lightSources;

    float r = 0.0f;
    float g = 0.0f;
    float b = 0.0f;

    // block-locals
    float lambert;
    float diffuse;
    float spec;
    float t;
    long rcolor;
    Light light;
    Renderable reflectedObject;

    // for optimization: copy values from p, n, v and use them as tempvars
    float px, py, pz, nx, ny, nz, vx, vy, vz;

    px = t1.x;
    py = t1.y;
    pz = t1.z;
    nx = t2.x;
    ny = t2.y;
    nz = t2.z;
    vx = t3.x;
    vy = t3.y;
    vz = t3.z;

    lightSources = scene.lights;
    while (lightSources != null) {
      light = (Light) lightSources.elem;
      lightSources = lightSources.next;
      if (light.lightType == Light.AMBIENT) {
        r += ka * ir * light.ir;
        g += ka * ig * light.ig;
        b += ka * ib * light.ib;
      }
      else {
        if (light.lightType == Light.POINT) {
          t1.init(light.lvec.x - px, light.lvec.y - py, light.lvec.z - pz);
          t1.normalize();
        }
        else
          t1.init(-light.lvec.x, -light.lvec.y, -light.lvec.z);

        // Check if the surface point is in shadow
        t2.init(px + TINY * t1.x, py + TINY * t1.y, pz + TINY * t1.z);
        tempRay.init(t2, t1);

        if (tempRay.cast(scene.objects) == false) { // direct ray to light
          lambert = t1.dot(nx, ny, nz);
          if (lambert > 0.0f) {
            if (kd > 0.0f) {
              diffuse = kd * lambert;
              r += diffuse * ir * light.ir;
              g += diffuse * ig * light.ig;
              b += diffuse * ib * light.ib;
            }
            if (ks > 0.0f) {
              lambert *= 2.0f;
              t3.init(lambert * nx - t1.x, lambert * ny - t1.y, lambert * nz
                  - t1.z);
              spec = t3.dot(vx, vy, vz);
              if (spec > 0.0f) {
                spec = ks * (float) Math.pow((double) spec, (double) ns);
                r += spec * light.ir;
                g += spec * light.ig;
                b += spec * light.ib;
              }
            }
          }
        }
      }
    }

    // Compute illumination due to reflection
    if (kr > 0.0f) {
      t1.init(vx, vy, vz);
      t = t1.dot(nx, ny, nz);
      if (t > 0.0f) {
        t *= 2.0f;
        t1.init(t * nx - vx, t * ny - vy, t * nz - vz);
        t2.init(px + TINY * t1.x, py + TINY * t1.y, pz + TINY * t1.z);
        tempRay.init(t2, t1);
        reflectedObject = tempRay.trace(scene.objects);
        if (reflectedObject != null) {
          t1.init(px, py, pz);
          t2.init(nx, ny, nz);
          t3.init(vx, vy, vz);
          rcolor = reflectedObject.Shade(tempRay, scene, t1, t2, t3);
          r += kr * RGBfCol.fGetRed(rcolor);
          g += kr * RGBfCol.fGetGreen(rcolor);
          b += kr * RGBfCol.fGetBlue(rcolor);
        }
        else {
          r += kr * RGBfCol.fGetRed(scene.background);
          g += kr * RGBfCol.fGetGreen(scene.background);
          b += kr * RGBfCol.fGetBlue(scene.background);
        }
      }
    }

    // Add code for refraction here!

    // Clamp RGB values
    /*
     * r = (r > 1f) ? 1f : r; g = (g > 1f) ? 1f : g; b = (b > 1f) ? 1f : b;
     */
    if (r > 1.0f)
      r = 1.0f;
    if (g > 1.0f)
      g = 1.0f;
    if (b > 1.0f)
      b = 1.0f;

    return RGBfCol.create(r, g, b);
  }
}
