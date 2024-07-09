package dtu.compute.pixels.tools;

import dtu.compute.pixels.controller.Controller;
import dtu.compute.pixels.controller.tools.DottedTool;
import dtu.compute.pixels.model.Color;
import dtu.compute.pixels.model.Image;
import dtu.compute.pixels.model.Point;
import dtu.compute.pixels.model.Rect;
import dtu.compute.pixels.util.TestUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class DottedToolTest {
    @Test
    @DisplayName("Test DottedTool")
    void testDottedTool() throws IOException {
        // Create an image with a white background
        Image img = new Image(new Rect(10, 10));
        img.reset(Color.WHITE);

        // Create a Controller and set the DottedTool
        Controller ctrl = new Controller().setTool(new DottedTool()).setImage(img);

        // Set the dot spacing to 3 for testing
        DottedTool dottedTool = (DottedTool) ctrl.getTool();
        dottedTool.setDotSpacing(3);

        // Simulate using the DottedTool
        ctrl.setColor(Color.RED);

        // Create a pattern of dots by pressing and releasing the tool at specific points
        ctrl.press(new Point(2, 2));
        ctrl.release(new Point(2, 2));

        ctrl.press(new Point(5, 5));
        ctrl.release(new Point(5, 5));

        ctrl.press(new Point(8, 8));
        ctrl.release(new Point(8, 8));

        // Expected result: Dots at (2, 2), (5, 5), and (8, 8)

        TestUtils.goldenTest("DottedToolTest", img);

        // Verify the colors at specific points
        assertEquals(Color.RED, img.getPixel(new Point(2, 2)));
        assertEquals(Color.RED, img.getPixel(new Point(5, 5)));
        assertEquals(Color.RED, img.getPixel(new Point(8, 8)));
    }
}
