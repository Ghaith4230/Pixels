package dtu.compute.pixels.tools;

import dtu.compute.pixels.controller.Controller;
import dtu.compute.pixels.controller.tools.ColorFill;
import dtu.compute.pixels.controller.tools.SquareTool;
import dtu.compute.pixels.model.Color;
import dtu.compute.pixels.model.Image;
import dtu.compute.pixels.model.Point;
import dtu.compute.pixels.model.Rect;
import dtu.compute.pixels.util.TestUtils;
import java.io.IOException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class ColorFillAndSquareTest {
    @Test
    @DisplayName("colorfill a square")
    void canFillSquare() throws IOException {

        // Create an image with a white background
        Image img = new Image(new Rect(20, 20));
    

        // Create a Controller and set the ColorFill tool
        Controller ctrl = new Controller().setTool(new SquareTool()).setImage(img);

        // Click on a point to initiate the ColorFill operation
        ctrl.setColor(Color.BLACK).press(new Point(2,2)).update(new Point(10,10)).release(new Point(10,10))
        .setTool(new ColorFill()).setColor(Color.RED).press(new Point(4,4)).release(new Point(4,4));

        TestUtils.goldenTest("ColorFill", img);
    }
}
