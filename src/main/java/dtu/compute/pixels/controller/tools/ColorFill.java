package dtu.compute.pixels.controller.tools;

import dtu.compute.pixels.controller.Controller;
import dtu.compute.pixels.model.Color;
import dtu.compute.pixels.model.Point;
import java.util.Stack;

public class ColorFill implements Tool {
    
    private boolean pressed = false;

    
    @Override
    public void press(Controller ctrl, Point point) {
        pressed = true; 
        update(ctrl, point); 
    }

    
    @Override
    public void update(Controller ctrl, Point point) {
        if (pressed) {
            // Perform flood fill
            floodFill(ctrl, point);
        }
    }

    
    @Override
    public void release(Controller ctrl, Point point) {
        
        pressed = false;
       
        ctrl.commitScratch();
    }

    
    @Override
    public void abandon(Controller ctrl) {
       
        pressed = false;
        
        ctrl.resetScratch(true);
    }

    
    
    private void floodFill(Controller ctrl, Point startPoint) {
        int width = ctrl.getImage().getSize().width();
        int height = ctrl.getImage().getSize().height();
        Stack<Point> points = new Stack<>();
        points.push(startPoint);

        while (!points.isEmpty()) {
            startPoint = points.pop();

            
            if (startPoint.x() < 0 || startPoint.x() >= width || startPoint.y() < 0 || startPoint.y() >= height) {
                continue;
            }

            
            Color targetColor = ctrl.getImage().getPixel(startPoint);

            
            Color currentColor = ctrl.getScratch().getPixel(startPoint);

            
            if (!currentColor.equals(targetColor)) {
                continue;
            }

           
            ctrl.setScratchPixel(startPoint, ctrl.getColor());

           
            points.push(new Point(startPoint.x() + 1, startPoint.y()));
            points.push(new Point(startPoint.x() - 1, startPoint.y()));
            points.push(new Point(startPoint.x(), startPoint.y() - 1));
            points.push(new Point(startPoint.x(), startPoint.y() + 1));

        }

    }

}
