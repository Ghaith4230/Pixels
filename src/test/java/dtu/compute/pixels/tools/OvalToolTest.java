package dtu.compute.pixels.tools;

import dtu.compute.pixels.controller.Controller;
import dtu.compute.pixels.controller.tools.OvalTool;
import dtu.compute.pixels.model.Color;
import dtu.compute.pixels.model.Image;
import dtu.compute.pixels.model.Point;
import dtu.compute.pixels.model.Rect;
import dtu.compute.pixels.util.TestUtils;
import java.io.IOException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
public class OvalToolTest{
   
    @Test
    @DisplayName("Spray on selected pixel")
    void canSprayIndividualPixels() throws IOException {
    
        Image img = new Image(new Rect(200, 200));
        img.reset(Color.WHITE);
    
        Controller ctrl = new Controller().setTool(new OvalTool()).setImage(img);
    
        // Ensuring that the coordinates are within the bounds of the image
        
    
        ctrl.setColor(Color.BLACK).press(new Point(100, 0)).update(new Point(0, 100)).release((new Point(0, 100)));
           
            // Adjusted y-coordinate
    
        TestUtils.goldenTest("OvalTool", img);
    }
        
    }
