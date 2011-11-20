package cg2.vecmath;

import java.nio.FloatBuffer;

import org.lwjgl.BufferUtils;

/**
 * Simple 3 component vector class. Vectors are non mutable and can be passed
 * around by value.
 * 
 * @author henrik
 */
public final class Vector implements Comparable<Vector> {

  public static Vector X = new Vector(1, 0, 0);
  public static Vector Y = new Vector(0, 1, 0);
  public static Vector Z = new Vector(0, 0, 1);

  public final float x, y, z;

  /**
   * Construct a new vector and initialize the components.
   * 
   * @param x
   *          The X component.
   * @param y
   *          The Y component.
   * @param z
   *          The Z component.
   */
  public Vector(float x, float y, float z) {
    this.x = x;
    this.y = y;
    this.z = z;
  }

  /**
   * Component-wise addition of two vectors.
   * 
   * @param v
   *          The second vector.
   * @return The sum.
   */
  public Vector add(Vector v) {
    return new Vector(x + v.x, y + v.y, z + v.z);
  }

  /**
   * Subtract a vector from this vector.
   * 
   * @param v
   *          The second vector.
   * @return The difference.
   */
  public Vector sub(Vector v) {
    return new Vector(x - v.x, y - v.y, z - v.z);
  }

  /**
   * Multiply this vector by a scalar.
   * 
   * @param s
   *          The scalar.
   * @return The scaled vector.
   */
  public Vector mult(float s) {
    return new Vector(x * s, y * s, z * s);
  }

  /**
   * Componentwise multiplication of two vectors. This is not the dot product!
   * 
   * @param v
   *          The second vector.
   * @return The product.
   */
  public Vector mult(Vector v) {
    return new Vector(x * v.x, y * v.y, z * v.z);
  }

  /**
   * Compute the length of this vector.
   * 
   * @return The lenght of this vector.
   */
  public float length() {
    return (float) Math.sqrt(x * x + y * y + z * z);
  }

  /**
   * Normalize this vector.
   * 
   * @return The normalized vector.
   */
  public Vector normalize() {
    return mult(1.0f / length());
  }

  /**
   * Calculate the dot product of two vectors.
   * 
   * @param v
   *          The second vector.
   * @return The dot product.
   */
  public float dot(Vector v) {
    return x * v.x + y * v.y + z * v.z;
  }

  /**
   * Calculate the cross product of two vectors.
   * 
   * @param v
   *          The second vector.
   * @return The cross product.
   */
  public Vector cross(Vector v) {
    return new Vector(y * v.z - z * v.y, -x * v.z + z * v.x, x * v.y - y * v.x);
  }

  public float[] asArray() {
    float[] v = { x, y, z };
    return v;
  }

  public FloatBuffer asBuffer() {
    FloatBuffer b = BufferUtils.createFloatBuffer(Vector.size());
    b.put(x);
    b.put(y);
    b.put(z);
    b.rewind();
    return b;
  }
  
  public void fillBuffer(FloatBuffer buf) {
    buf.put(x);
    buf.put(y);
    buf.put(z);
  }


  /*
   * @see java.lang.Object#toString()
   */
  @Override
public String toString() {
    return "(" + x + ", " + y + ", " + z + ")";
  }

  @Override
  public boolean equals(final Object o) {
    if (!(o instanceof Vector))
      return false;
    final Vector v = (Vector) o;
    return x == v.x && y == v.y && z == v.z;
  }

  public int compareTo(Vector o) {
    if (x != o.x)
      return (x < o.x ? -1 : 1);
    if (y != o.y)
      return (y < o.y ? -1 : 1);
    if (z != o.z)
      return (z < o.z ? -1 : 1);
    return 0;
  }

  public static int size() {
    return 3;
  }
}