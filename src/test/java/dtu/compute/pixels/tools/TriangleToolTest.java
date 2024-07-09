package dtu.compute.pixels.tools;

import dtu.compute.pixels.controller.Controller;
import dtu.compute.pixels.controller.tools.TriangleTool;
import dtu.compute.pixels.model.Color;
import dtu.compute.pixels.model.Image;
import dtu.compute.pixels.model.Point;
import dtu.compute.pixels.model.Rect;
import dtu.compute.pixels.util.TestUtils;
import java.io.IOException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
public class TriangleToolTest{
   
    @Test
    @DisplayName("Spray on selected pixel")
    void canSprayIndividualPixels() throws IOException {
    
        Image img = new Image(new Rect(200, 200));
        img.reset(Color.WHITE);
    
        Controller ctrl = new Controller().setTool(new TriangleTool()).setImage(img);
    
        // Ensuring that the coordinates are within the bounds of the image
        
    
        ctrl.setColor(Color.BLACK).press(new Point(100, 0)).press(new Point(0, 100)).press((new Point(100, 100)));
        ctrl.setColor(Color.RED).press(new Point(100, 0)).press(new Point(100, 100)).press((new Point(200, 100)));    
            // Adjusted y-coordinate
    
        TestUtils.goldenTest("TriangleTool", img);
    }
        
    }
