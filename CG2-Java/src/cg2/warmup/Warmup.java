package cg2.warmup;

public class Warmup {

  public static void main(String[] args) {

    // get the user's home directory - should work on all operating systems
    String path = System.getProperty("user.home");

    // ************ test painting a checkerboard ************
    {
      String filename = path + "/" + "checkerboard.png";
      new ImageGenerator(new Checkerboard(), 650, 750, filename, "png");
      ImageGenerator.showImage(filename);
    }

    // ************ test painting a disk ************
    {
      String filename = path + "/" + "disk.png";
      new ImageGenerator(new Disk(), 750, 500, filename, "png");
      ImageGenerator.showImage(filename);
    }

  }

}
