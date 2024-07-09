package dtu.compute.pixels.tools;

import dtu.compute.pixels.controller.Controller;
import dtu.compute.pixels.controller.tools.LineTool;
import dtu.compute.pixels.model.Color;
import dtu.compute.pixels.model.Image;
import dtu.compute.pixels.model.Point;
import dtu.compute.pixels.model.Rect;
import dtu.compute.pixels.util.TestUtils;
import java.io.IOException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class LineToolTest {
    @Test
    @DisplayName("make a line")
    void canFillSquare() throws IOException {

        // Create an image with a white background
        Image img = new Image(new Rect(5, 5));
        img.reset(Color.WHITE);

        // Create a Controller and set the ColorFill tool
        Controller ctrl = new Controller().setTool(new LineTool()).setImage(img);

        // Click on a point to initiate the ColorFill operation
        ctrl.setColor(Color.BLACK).press(new Point(0,0)).release(new Point(4,4));

        TestUtils.goldenTest("LineTool", img);
    }
}
