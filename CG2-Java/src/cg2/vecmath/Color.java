package cg2.vecmath;

import java.nio.FloatBuffer;

import org.lwjgl.BufferUtils;

/**
 * A simple three component color vector. Color vectors are non-mutable and can
 * be passed around by value.
 * 
 */
public final class Color implements Comparable<Color> {

  public final float r, g, b;

  /**
   * Construct a new color vector and initialize the components.
   * 
   * @param r
   *          The red component.
   * @param g
   *          The green component.
   * @param b
   *          The blue component.
   */
  public Color(float r, float g, float b) {
    this.r = r;
    this.g = g;
    this.b = b;
  }

  public Color(Vector v) {
    r = v.x;
    g = v.y;
    b = v.z;
  }

  /**
   * Construct a new color vector and initialize the components.
   * 
   * @param r
   *          The red component.
   * @param g
   *          The green component.
   * @param b
   *          The blue component.
   */
  public Color(float r, float g, float b, float a) {
    this.r = r;
    this.g = g;
    this.b = b;
  }

  /**
   * Test this color for blackness.
   * 
   * @return True if black, else false.
   */
  public boolean isBlack() {
    return r == 0.0 && g == 0.0 && b == 0.0;
  }

  /**
   * Calculate the sum of two colors.
   * 
   * @param c
   *          The second color.
   * @return The sum.
   */
  public Color add(Color c) {
    return new Color(r + c.r, g + c.g, b + c.b);
  }

  /**
   * Calculate the product of this color an a scalar.
   * 
   * @param s
   *          The scalar.
   * @return The product.
   */
  public Color modulate(float s) {
    return new Color(r * s, g * s, b * s);
  }

  /**
   * Perform the component wise multiplication of two colors. This is not a dot
   * product!
   * 
   * @param c
   *          The second color.
   * @return The result of the multiplication.
   */
  public Color modulate(Color c) {
    return new Color(r * c.r, g * c.g, b * c.b);
  }

  /**
   * Clip the color components to the interval [0.0, 1.0].
   * 
   * @return The clipped color.
   */
  public Color clip() {
    Color c = new Color(Math.min(r, 1), Math.min(g, 1), Math.min(b, 1));
    return new Color(Math.max(c.r, 0), Math.max(c.g, 0), Math.max(c.b, 0));
  }
  
  public Vector asVector() {
	  return new Vector(r,g,b);
  }

  public float[] asArray() {
    float[] v = { r, g, b };
    return v;
  }

  public FloatBuffer asBuffer() {
    FloatBuffer buf = BufferUtils.createFloatBuffer(Color.size());
    buf.put(r);
    buf.put(g);
    buf.put(b);
    buf.rewind();
    return buf;
  }

  public void fillBuffer(FloatBuffer buf) {
    buf.put(r);
    buf.put(g);
    buf.put(b);
  }
  
  /**
   * Return this color in a packed pixel format suitable for use with AWT.
   * 
   * @return The color as a packed pixel integer value.
   */
  public int toAwtColor() {
    Color c = clip();
    return (toInt(1.0f) << 24) | (toInt(c.r) << 16) | (toInt(c.g) << 8)
        | toInt(c.b);
  }

  /**
   * Convert a floating point component from the interval [0.0, 1.0] to an
   * integral component in the interval [0, 255]
   * 
   * @param c
   *          The float component.
   * @return The integer component.
   */
  private static int toInt(float c) {
    return Math.round(c * 255.0f);
  }

  /*
   * @see java.lang.Object#toString()
   */
  @Override
public String toString() {
    return "(" + r + ", " + g + ", " + b + ")";
  }

  @Override
  public boolean equals(final Object o) {
    if (!(o instanceof Color))
      return false;
    final Color c = (Color) o;
    return r == c.r && g == c.g && b == c.b;
  }

  public int compareTo(Color o) {
    if (r != o.r)
      return (r < o.r ? -1 : 1);
    if (g != o.g)
      return (g < o.g ? -1 : 1);
    if (b != o.b)
      return (b < o.b ? -1 : 1);
    return 0;
  }

  public static int size() {
    return 3;
  }

}
