package raytracer;

/**
 * RGBfCol
 * 
 * @author Stefan Frenz
 * @version 0.000.03.SF
 */

public class RGBfCol {
  private final static int RED = 2, GREEN = 1, BLUE = 0; // order in long

  private final static int bpc = 21; // 21 bits per color (maximum resolution:
                                      // 21 bits, care sign!)

  private final static float f_mul = 2097152.0f; // 2**21

  private final static int i_max = 2097151; // 2**21-1 == 0x1FFFFF (used as
                                            // mask)

  public static long create(float rI, float gI, float bI) {
    int r, g, b;

    r = (int) (rI * f_mul);
    g = (int) (gI * f_mul);
    b = (int) (bI * f_mul);
    if (r < 0)
      r = 0;
    else if (r > i_max)
      r = i_max;
    if (g < 0)
      g = 0;
    else if (g > i_max)
      g = i_max;
    if (b < 0)
      b = 0;
    else if (b > i_max)
      b = i_max;

    return (((long) r) << (bpc * 2)) | (((long) g) << bpc) | ((long) b);
  }

  private static int getIntCol(long col, int index) {
    int shift, val;

    shift = index * bpc;
    val = ((int) (col >>> shift)) & i_max;
    return val;
  }

  public static float fGetRed(long col) {
    float v;
    v = (float) getIntCol(col, RED);
    return v / f_mul;
  }

  public static float fGetGreen(long col) {
    float v;
    v = (float) getIntCol(col, GREEN);
    return v / f_mul;
  }

  public static float fGetBlue(long col) {
    float v;
    v = (float) getIntCol(col, BLUE);
    return v / f_mul;
  }

  public static int toRGB(long col, boolean bgr) {
    int rv, gv, bv;

    rv = getIntCol(col, RED) >> (bpc - 8);
    gv = getIntCol(col, GREEN) >> (bpc - 8);
    bv = getIntCol(col, BLUE) >> (bpc - 8);

    return bgr ? (bv << 16) | (gv << 8) | rv : (rv << 16) | (gv << 8) | bv;
  }
}