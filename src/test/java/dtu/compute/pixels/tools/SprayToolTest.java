package dtu.compute.pixels.tools;

import dtu.compute.pixels.controller.Controller;
import dtu.compute.pixels.controller.tools.SprayTool;
import dtu.compute.pixels.model.Color;
import dtu.compute.pixels.model.Image;
import dtu.compute.pixels.model.Point;
import dtu.compute.pixels.model.Rect;
import dtu.compute.pixels.util.TestUtils;
import java.io.IOException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class SprayToolTest {
@Test
@DisplayName("Spray on selected pixel")
void canSprayIndividualPixels() throws IOException {

    Image img = new Image(new Rect(50, 50));
    img.reset(Color.WHITE);

    Controller ctrl = new Controller().setTool(new SprayTool()).setImage(img);

    // Ensuring that the coordinates are within the bounds of the image
    

    ctrl.setColor(Color.BLACK).click(new Point(10, 10))
        .setColor(Color.BLUE).click(new Point(20, 10))
        .setColor(Color.RED).click(new Point(30, 10))
        .setColor(Color.GREEN).click(new Point(40, 10));
        // Adjusted y-coordinate

    TestUtils.goldenTest("SprayTool", img);
}
    
}
