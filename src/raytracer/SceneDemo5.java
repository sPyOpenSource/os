package raytracer;

public class SceneDemo5 extends Scene {
  private void setUpEye() {
    eye = new Vector3f(0.0f, -8.0f, 3.0f);
    up = new Vector3f(0.0f, 0.0f, 1.0f);
    lookat = new Vector3f(0.0f, -2.0f, 0.0f);
    fov = 75.0f;
  }

  private void setUpEnvironment() {
    Surface s;

    background = RGBfCol.create(0.078f, 0.361f, 0.753f);
    // background = RGBfCol.create(0.0f, 0.0f, 0.0f);
    s = new Surface(0.1f, 0.6f, 0.3f, 0.15f, 1.0f, 0.0f, 1.0f, 0.0f, 0.0f, 1.0f);
    objects.addElement(new Sphere(s, new Vector3f(0.0f, 0.0f, -2000.0f),
        2000.0f));
  }

  private void createWalls() {
    Surface s;
    Vector3f b1, b2, b3, b4, t1, t2, t3, t4;
    Vector3f mb1, mb2, mt1, mt2;

    b1 = new Vector3f(-2.0f, -2.0f, 0.0f);
    t1 = new Vector3f(-2.0f, -2.0f, 2.0f);
    b2 = new Vector3f(-2.0f, 2.0f, 0.0f);
    t2 = new Vector3f(-2.0f, 2.0f, 2.0f);
    b3 = new Vector3f(2.0f, 2.0f, 0.0f);
    t3 = new Vector3f(2.0f, 2.0f, 2.0f);
    b4 = new Vector3f(2.0f, -2.0f, 0.0f);
    t4 = new Vector3f(2.0f, -2.0f, 2.0f);
    mb1 = new Vector3f(-8.0f, 4.0f, 0.0f);
    mt1 = new Vector3f(-8.5f, 3.75f, 3.0f);
    mb2 = new Vector3f(-4.0f, 6.0f, 0.0f);
    mt2 = new Vector3f(-4.5f, 5.75f, 3.0f);

    s = new Surface(0.9f, 0.9f, 0.9f, 0.2f, 1.0f, 0.0f, 0.8f, 0.0f, 0.0f, 1.0f);
    objects.addElement(new Triangle(s, b1, b2, t1));
    objects.addElement(new Triangle(s, t1, t2, b2));
    objects.addElement(new Triangle(s, b3, b4, t3));
    objects.addElement(new Triangle(s, t3, t4, b4));

    s = new Surface(0.5f, 0.45f, 0.35f, 0.07f, 1.0f, 0.8f, 3.0f, 0.5f, 0.0f,
        1.0f);
    objects.addElement(new Triangle(s, b2, b3, t3));
    objects.addElement(new Triangle(s, t2, t3, b2));

    s = new Surface(0.0f, 0.0f, 0.0f, 0.07f, 1.0f, 0.8f, 3.0f, 0.5f, 0.0f, 1.0f);
    objects.addElement(new Triangle(s, mb1, mb2, mt1));
    objects.addElement(new Triangle(s, mt1, mt2, mb2));
  }

  private void createString() {
    Surface s;

    s = new Surface(0.5f, 0.45f, 0.35f, 0.07f, 1.0f, 0.8f, 3.0f, 0.5f, 0.0f,
        1.0f);
    objects.addElement(new Sphere(s, new Vector3f(-2.0f, 2.0f, 2.0f), 0.5f));
    objects.addElement(new Sphere(s, new Vector3f(-1.0f, 2.0f, 2.0f), 0.5f));
    objects.addElement(new Sphere(s, new Vector3f(0.0f, 2.0f, 2.0f), 0.5f));
    objects.addElement(new Sphere(s, new Vector3f(1.0f, 2.0f, 2.0f), 0.5f));
    objects.addElement(new Sphere(s, new Vector3f(2.0f, 2.0f, 2.0f), 0.5f));
  }

  private void createGroup(Surface cS, float x, float y, boolean upper) {
    objects.addElement(new Sphere(cS, new Vector3f(x + 0.0f, y + 0.0f, 0.5f),
        0.5f));
    objects.addElement(new Sphere(cS, new Vector3f(x + 0.643951f,
        y + 0.172546f, 0.5f), 0.166667f));
    objects.addElement(new Sphere(cS, new Vector3f(x + 0.172546f,
        y + 0.643951f, 0.5f), 0.166667f));
    objects.addElement(new Sphere(cS, new Vector3f(x - 0.471405f,
        y + 0.471405f, 0.5f), 0.166667f));
    objects.addElement(new Sphere(cS, new Vector3f(x - 0.643951f,
        y - 0.172546f, 0.5f), 0.166667f));
    objects.addElement(new Sphere(cS, new Vector3f(x - 0.172546f,
        y - 0.643951f, 0.5f), 0.166667f));
    objects.addElement(new Sphere(cS, new Vector3f(x + 0.471405f,
        y - 0.471405f, 0.5f), 0.166667f));
    if (upper) {
      objects.addElement(new Sphere(cS, new Vector3f(x + 0.272166f,
          y + 0.272166f, 1.044331f), 0.166667f));
      objects.addElement(new Sphere(cS, new Vector3f(x - 0.371785f,
          y + 0.0996195f, 1.044331f), 0.166667f));
      objects.addElement(new Sphere(cS, new Vector3f(x - 0.0996195f,
          y - 0.371785f, 1.044331f), 0.166667f));
    }
  }

  private void createThings() {
    Surface s;

    s = new Surface(0.0f, 0.0f, 1.0f, 0.07f, 1.0f, 0.8f, 3.0f, 0.5f, 0.0f, 1.0f);
    objects.addElement(new Sphere(s, new Vector3f(-1.5f, -1.5f, 0.5f), 0.5f));

    s = new Surface(1.0f, 0.0f, 0.0f, 0.07f, 1.0f, 0.8f, 3.0f, 0.5f, 0.0f, 1.0f);
    objects.addElement(new Sphere(s, new Vector3f(-1.0f, 0.0f, 0.5f), 0.5f));

    s = new Surface(0.5f, 0.45f, 0.35f, 0.07f, 1.0f, 0.8f, 3.0f, 0.5f, 0.0f,
        1.0f);
    objects.addElement(new Sphere(s, new Vector3f(0.0f, 1.0f, 0.5f), 0.5f));

    s = new Surface(1.0f, 1.0f, 0.0f, 0.07f, 1.0f, 0.8f, 3.0f, 0.5f, 0.0f, 1.0f);
    objects.addElement(new Sphere(s, new Vector3f(1.0f, 0.0f, 0.5f), 0.5f));

    s = new Surface(0.0f, 1.0f, 1.0f, 0.07f, 1.0f, 0.8f, 3.0f, 0.5f, 0.0f, 1.0f);
    objects.addElement(new Sphere(s, new Vector3f(1.5f, -1.5f, 0.5f), 0.5f));

    s = new Surface(1.0f, 1.0f, 1.0f, 0.07f, 1.0f, 0.8f, 5.0f, 0.6f, 0.0f, 1.0f);
    objects.addElement(new Cylinder(s, new Vector3f(3.0f, -1.8f, 1.2f), 0.35f,
        new Vector3f(-3.0f, 1.6f, 0.3f), false));

    s = new Surface(0.5f, 0.45f, 0.35f, 0.07f, 1.0f, 0.8f, 3.0f, 0.5f, 0.0f,
        1.0f);
    createGroup(s, 4.5f, 2.0f, false);
    createGroup(s, -4.5f, 2.0f, false);
    createGroup(s, 6.0f, 0.5f, true);
    createGroup(s, -6.0f, 0.5f, true);
    createGroup(s, 4.5f, -1.0f, false);
    createGroup(s, -4.5f, -1.0f, false);
    createGroup(s, 3.0f, -2.5f, true);
    createGroup(s, -3.0f, -2.5f, true);
    createGroup(s, 1.5f, -4.0f, false);
    createGroup(s, -1.5f, -4.0f, false);
    createGroup(s, 0.0f, -5.5f, true);
  }

  private void createLights() {
    lights.addElement(new Light(Light.AMBIENT, null, 0.5f, 0.5f, 0.5f));
    lights.addElement(new Light(Light.POINT, new Vector3f(10.0f, -3.0f, 10.0f),
        0.8f, 0.8f, 0.8f));
    lights.addElement(new Light(Light.POINT, new Vector3f(0.0f, 0.0f, 2.0f),
        0.3f, 0.3f, 0.3f));
  }

  public void createWorld() {
    setUpEye();
    setUpEnvironment();
    createWalls();
    createString();
    createThings();
    createLights();
  }
}