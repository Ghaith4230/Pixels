package dtu.compute.pixels.util;

import dtu.compute.pixels.model.Color;

public class ColorUtils {
  // Converts a Color object to JavaFX color
  public static javafx.scene.paint.Color fromColor(Color color) {
    double[] fractions = color.toFractions();
    return new javafx.scene.paint.Color(
        fractions[1],
        fractions[2],
        fractions[3],
        fractions[0]
    );
  }

  public static Color toColor(javafx.scene.paint.Color color) {
    return dtu.compute.pixels.model.Color.fromFractions(
        color.getOpacity(),
        color.getRed(), color.getGreen(),
        color.getBlue());
  }

}
