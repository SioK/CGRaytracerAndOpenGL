/*
 * $RCSfile: Quat4d.java,v $
 *
 * Copyright (c) 2005 Sun Microsystems, Inc. All rights reserved.
 *
 * Use is subject to license terms.
 *
 * $Revision: 1.2 $
 * $Date: 2006/03/22 09:43:56 $
 * $State: Exp $
 */

package cg2.vecmath;

import java.lang.Math;

/**
 * A 4-element quaternion represented by float precision floating point x,y,z,w
 * coordinates. The quaternion is always normalized.
 * 
 */
public final class Quaternion {

  public final float x, y, z, w;

  final static float EPS = 0.000001f;
  final static float EPS2 = 1.0e-30f;
  final static float PIO2 = 1.57079632679f;

  /**
   * Constructs and initializes a quaternion from the specified xyzw
   * coordinates.
   * 
   * @param x
   *          the x coordinate
   * @param y
   *          the y coordinate
   * @param z
   *          the z coordinate
   * @param w
   *          the w scalar component
   */
  public Quaternion(float x, float y, float z, float w) {
    float mag;
    mag = 1.0f / (float) Math.sqrt(x * x + y * y + z * z + w * w);
    this.x = x * mag;
    this.y = y * mag;
    this.z = z * mag;
    this.w = w * mag;

  }

  /**
   * Constructs and initializes a quaternion from the array of length 4.
   * 
   * @param q
   *          the array of length 4 containing xyzw in order
   */
  public Quaternion(float[] q) {
    float mag;
    mag = 1.0f / (float) Math.sqrt(q[0] * q[0] + q[1] * q[1] + q[2] * q[2]
        + q[3] * q[3]);
    x = q[0] * mag;
    y = q[1] * mag;
    z = q[2] * mag;
    w = q[3] * mag;

  }

  /**
   * Constructs and initializes a quaternion to (0,0,0,0).
   */
  public Quaternion() {
    x = 0.0f;
    y = 0.0f;
    z = 0.0f;
    w = 0.0f;
  }

  /**
   * Sets the value of this quaternion to the rotational component of the passed
   * matrix.
   * 
   * @param m
   *          The matrix.
   */
  public Quaternion(Matrix m) {
    float ww = 0.25f * (m.get(0, 0) + m.get(1, 1) + m.get(2, 2) + m.get(3, 3));

    if (ww >= 0) {
      if (ww >= EPS2) {
        w = (float) Math.sqrt(ww);
        ww = 0.25f / w;
        x = ((m.get(2, 1) - m.get(1, 2)) * ww);
        y = ((m.get(0, 2) - m.get(2, 0)) * ww);
        z = ((m.get(1, 0) - m.get(0, 1)) * ww);
        return;
      }
    } else {
      w = 0;
      x = 0;
      y = 0;
      z = 1;
      return;
    }

    this.w = 0;
    ww = -0.5f * (m.get(1, 1) + m.get(2, 2));
    if (ww >= 0) {
      if (ww >= EPS2) {
        x = (float) Math.sqrt(ww);
        ww = 1.0f / (2.0f * x);
        y = (m.get(1, 0) * ww);
        z = (m.get(2, 0) * ww);
        return;
      }
    } else {
      x = 0;
      y = 0;
      z = 1;
      return;
    }

    this.x = 0;
    ww = 0.5f * (1.0f - m.get(2, 2));
    if (ww >= EPS2) {
      y = (float) Math.sqrt(ww);
      z = (m.get(2, 1)) / (2.0f * y);
      return;
    }

    y = 0;
    z = 1;
  }

  /**
   * Construct a quaternion from an axis and an angle.
   * 
   * @param axis
   *          The rotation axis.
   * @param angle
   *          The rotation angle.
   * @return The newly constructed quaternion.
   */
  public static Quaternion rotate(Vector axis, float angle) {
    return new Quaternion(Matrix.rotate(axis, angle));
  }

  /**
   * Negate the quaternion.
   * 
   * @return The negated quaternion.
   */
  public final Quaternion negate() {
    return new Quaternion(-x, -y, -z, -w);
  }

  /**
   * Conjugate the quaternion.
   * 
   * @return The conjugated quaternion.
   */
  public final Quaternion conjugate() {
    return new Quaternion(-x, -y, -z, w);
  }

  /**
   * Calculate the product of two quaternions.
   * 
   * @param q
   *          The other quaternion.
   * @return The resulting quaternion.
   */
  public final Quaternion mul(Quaternion q) {
    return new Quaternion(w * q.x + q.w * x + y * q.z - z * q.y, w * q.y + q.w
        * y - x * q.z + z * q.x, w * q.z + q.w * z + x * q.y - y * q.x, w * q.w
        - x * q.x - y * q.y - z * q.z);
  }

  /**
   * Invert the quaternion.
   * 
   * @return The inverted quaternion.
   */
  public final Quaternion inverse() {
    float norm = 1.0f / (w * w + x * x + y * y + z * z);
    return new Quaternion(norm * w, -norm * x, -norm * y, -norm * z);
  }

  /**
   * Normalizes the quaternion.
   * 
   * @return The normalized quaternion.
   */
  public final Quaternion normalize() {
    float norm = w * w + x * x + y * y + z * z;

    if (norm > 0.0f) {
      norm = 1.0f / (float) Math.sqrt(norm);
      return new Quaternion(x * norm, y * norm, z * norm, w * norm);
    } else {
      return new Quaternion();
    }
  }

  /**
   * Perform a great circle (spherical linear) interpolation between this
   * quaternion and the quaternion parameter.
   * 
   * @param q
   *          The other quaternion.
   * @param alpha
   *          The interpolation parameter from the interval [0, 1].
   * @return The interpolated quaternion.
   */
  public final Quaternion interpolate(Quaternion q, float alpha) {
    // From "Advanced Animation and Rendering Techniques"
    // by Watt and Watt pg. 364, function as implemented appeared to be
    // incorrect. Fails to choose the same quaternion for the float
    // covering. Resulting in change of direction for rotations.
    // Fixed function to negate the first quaternion in the case that the
    // dot product of q and this is negative. Second case was not needed.
    float dot, s1, s2, om, sinom;

    dot = x * q.x + y * q.y + z * q.z + w * q.w;

    if (dot < 0) {
      q = q.negate();
      dot = -dot;
    }

    if ((1.0 - dot) > EPS) {
      om = (float) Math.acos(dot);
      sinom = (float) Math.sin(om);
      s1 = (float) Math.sin((1.0 - alpha) * om) / sinom;
      s2 = (float) Math.sin(alpha * om) / sinom;
    } else {
      s1 = 1.0f - alpha;
      s2 = alpha;
    }

    return new Quaternion(s1 * w + s2 * q.w, s1 * x + s2 * q.x, s1 * y + s2
        * q.y, s1 * z + s2 * q.z);
  }
}