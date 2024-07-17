package dtu.compute.pixels.model;

import javafx.scene.paint.Paint;

public record Color(byte alpha, byte red, byte green, byte blue) {

  // Predefined color constants
  public static final Color BLACK = Color.fromARGB(0xff000000);
  public static final Color WHITE = Color.fromARGB(0xffffffff);
  public static final Color RED = Color.fromARGB(0xffff0000);
  public static final Color GREEN = Color.fromARGB(0xff00ff00);
  public static final Color BLUE = Color.fromARGB(0xff0000ff);
  public static final Color TRANSPARENT = Color.fromARGB(0);


    public static Color fromARGB(int hex) {
    return Color.fromInts(hex >> 24, hex >> 16, hex >> 8, hex);
  }

  // Create Color from integer RGBA values
  public static Color fromInts(int alpha, int red, int green, int blue) {
    return new Color((byte) (alpha & 0xff), (byte) (red & 0xff), (byte) (green & 0xff),
        (byte) (blue & 0xff));
  }

  // Create Color from fractional RGBA values
  public static Color fromFractions(double alpha, double red, double green, double blue) {
    return new Color(
        (byte) (Math.round(alpha * 255)),
        (byte) (Math.round(red * 255)),
        (byte) (Math.round(green * 255)),
        (byte) (Math.round(blue * 255)));
  }

  // Convert Color to ARGB hex value
  public int toARGB() {
    return (((((Byte.toUnsignedInt(alpha) << 8) | Byte.toUnsignedInt(red)) << 8) | Byte.toUnsignedInt(green)) << 8) | Byte.toUnsignedInt(blue);
  }

  // Convert Color to fractional RGBA values
  public double[] toFractions() {
    return new double[]{
        Byte.toUnsignedInt(alpha) / 255.0,
        Byte.toUnsignedInt(red) / 255.0,
        Byte.toUnsignedInt(green) / 255.0,
        Byte.toUnsignedInt(blue) / 255.0
    };
  }
}
