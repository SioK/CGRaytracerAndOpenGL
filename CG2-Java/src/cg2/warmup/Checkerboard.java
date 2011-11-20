package cg2.warmup;

import cg2.vecmath.Color;

public class Checkerboard implements Painter {



  public Color pixelColorAt(int i, int j, int width, int height) {

    float f1 = width / 8.0f;
    float f2 = height / 8.0f;
    int w = (int) (i / f1);
    int h = (int) (j / f2);

    // this is just a little hint -
    // but not yet the solution to paint an 8x8 checkerboard for any given
    // resolution
    if (w % 2 + h % 2 == 1) {
      return new Color(0, 0, 0);
    } else {
      return new Color(1, 1, 1);
    }

  }

}
