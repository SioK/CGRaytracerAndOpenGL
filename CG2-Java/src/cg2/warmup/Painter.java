package cg2.warmup;

import cg2.vecmath.Color;

/**
 * Subclass <code>Painter</code> to specify how an image should be painted.
 */
public interface Painter {

  /**
   * Override to determine the pixel color for one pixel of the image.
   * 
   * @param x
   *          The X-coordinate of the pixel.
   * @param y
   *          The Y-Coordinate of the pixel.
   * @param width
   *          The image width in pixels.
   * @param height
   *          The image height in pixels.
   * @return The pixel color
   */
  public Color pixelColorAt(int x, int y, int width, int height);
}
