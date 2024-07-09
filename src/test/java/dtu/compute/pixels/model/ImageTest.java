package dtu.compute.pixels.model;

import dtu.compute.pixels.util.TestUtils;
import java.io.IOException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class ImageTest {

  @Test
  @DisplayName("can paint a specific pixel")
  void canPaintASpecificPixel() throws IOException {
    Image image = new Image(new Rect(3, 3));

    image.reset(Color.WHITE);

    // set the middle pixel red.
    image.setPixel(new Point(1, 1), Color.RED);

    TestUtils.goldenTest("single-pixel", image);

  }

}
