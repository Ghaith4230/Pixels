package dtu.compute.pixels.tools;

import dtu.compute.pixels.controller.Controller;
import dtu.compute.pixels.controller.tools.Pen;
import dtu.compute.pixels.controller.tools.Eraser;
import dtu.compute.pixels.model.Color;
import dtu.compute.pixels.model.Image;
import dtu.compute.pixels.model.Point;
import dtu.compute.pixels.model.Rect;
import dtu.compute.pixels.util.TestUtils;
import java.io.IOException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class EraserTest {
    @Test
    @DisplayName("delete selected pixels")
    void canEraseIndividualPixels() throws IOException {
  
      Image img = new Image(new Rect(30, 30));
  
      Controller ctrl = new Controller().setTool(new Pen()).setImage(img);
  
      ctrl.setColor(Color.BLACK).click(new Point(10, 5))
      .setColor(Color.BLUE).click(new Point(10, 10))
      .setColor(Color.RED).click(new Point(10, 15))
      .setColor(Color.GREEN).click(new Point(10, 20))
      .setColor(Color.WHITE).click(new Point(10, 25));

          ctrl.setTool(new Eraser())
          .click(new Point(10, 5))
          .click(new Point(10, 10))
          .click(new Point(10, 15))
          .click(new Point(10, 20))
          .click(new Point(10, 25));
  
      TestUtils.goldenTest("Eraser", img);
    }
    
}
