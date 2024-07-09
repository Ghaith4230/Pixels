package dtu.compute.pixels.tools;

import dtu.compute.pixels.controller.Controller;
import dtu.compute.pixels.controller.tools.RotateTool;
import dtu.compute.pixels.controller.tools.SelectTool;
import dtu.compute.pixels.controller.tools.TriangleTool;
import dtu.compute.pixels.model.Color;
import dtu.compute.pixels.model.Image;
import dtu.compute.pixels.model.Point;
import dtu.compute.pixels.model.Rect;
import dtu.compute.pixels.util.TestUtils;
import java.io.IOException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
public class RotateAndSelectToolTest{
   
    @Test
    @DisplayName("Spray on selected pixel")
    void canSprayIndividualPixels() throws IOException {
    
        Image img = new Image(new Rect(200, 200));
        img.reset(Color.WHITE);
    
        Controller ctrl = new Controller().setTool(new TriangleTool()).setImage(img);
    
        ctrl.setColor(Color.BLACK).press(new Point(100, 40)).press(new Point(30, 100)).press((new Point(100, 100)))
        .setTool(new SelectTool()).press(new Point(120,10)).update(new Point(0,110)).release(new Point(0,110))
        .setTool(new RotateTool()).press(new Point(100,15)).update(new Point(120,50)).release(new Point(120,50))
        .setTool(new TriangleTool()).setColor(Color.RED).press(new Point(100, 40)).press(new Point(30, 100)).press((new Point(100, 100)));
        
    
      
           
    
        TestUtils.goldenTest("RotateAndSelectTool", img);
    }
        
    }

