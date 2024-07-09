package dtu.compute.pixels.tools;

import dtu.compute.pixels.controller.Controller;
import dtu.compute.pixels.controller.tools.MoveTool;
import dtu.compute.pixels.controller.tools.SelectTool;
import dtu.compute.pixels.controller.tools.SquareTool;
import dtu.compute.pixels.model.Color;
import dtu.compute.pixels.model.Image;
import dtu.compute.pixels.model.Point;
import dtu.compute.pixels.model.Rect;
import dtu.compute.pixels.util.TestUtils;
import java.io.IOException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
public class SelectAndMoveToolTest{
   
    @Test
    @DisplayName("Spray on selected pixel")
    void canSprayIndividualPixels() throws IOException {
    
        Image img = new Image(new Rect(200, 200));
        img.reset(Color.WHITE);
    
        Controller ctrl = new Controller().setTool(new SquareTool()).setImage(img);
    
        
        
    
        ctrl.setColor(Color.BLACK).press(new Point(50, 50)).update(new Point(70, 70)).release((new Point(70, 70)))
        .setTool(new SelectTool()).press(new Point(49, 49)).update(new Point(71, 71)).release((new Point(71, 71)))
        .setTool(new MoveTool()).press(new Point(51, 51)).update(new Point(100, 100)).release((new Point(100, 100)))
        .setColor(Color.RED).setTool(new SquareTool()).press(new Point(50, 50)).update(new Point(70, 70)).release((new Point(70, 70)));
           
    
        TestUtils.goldenTest("MoveAndSelectTool", img);
    }
        
    }

