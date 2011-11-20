package cg2.warmup;

import cg2.vecmath.Color;

public class Disk implements Painter {

  public Color pixelColorAt(int x, int y, int width, int height) {

    int size = width < height ? width : height;
    float radius = size / 10.0f * 9.0f / 2.0f;
    float centerX = width / 2.0f;
    float centerY = height / 2.0f;
    float floatX = x;
    float floatY = y;

    if ((floatX - centerX) * (floatX - centerX) + (floatY - centerY) * (floatY - centerY) - radius * radius > 0) {
      return new Color(0.5f, 0.5f, 0.5f);
    } else {
      return new Color(1, 0, 0);
    }

  }
}
