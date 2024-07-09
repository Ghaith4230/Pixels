package dtu.compute.pixels.util;

import dtu.compute.pixels.model.Image;
import dtu.compute.pixels.model.Rect;
import java.io.IOException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ImageUtilsTest {

  @Test
  @DisplayName("writing and reading is the same")
  void writingAndReadingIsTheSame() throws IOException {
    Image image = Image.pretty(new Rect(32,32));

    TestUtils.goldenTest("pretty", image);
  }

}