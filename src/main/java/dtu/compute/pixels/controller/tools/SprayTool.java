package dtu.compute.pixels.controller.tools;

import dtu.compute.pixels.controller.Controller;
import dtu.compute.pixels.model.Point;
import java.util.Random;

public class SprayTool implements Tool {
    private boolean pressed = false;
    private int radius = 3; // Further reduced radius for a finer spray effect
    private Random random = new Random();

    @Override
    public void press(Controller ctrl, Point point) {
        pressed = true;
        spray(ctrl, point);
    }

    @Override
    public void update(Controller ctrl, Point point) {
        if (pressed) {
            spray(ctrl, point);
        } else{
             
                ctrl.resetScratch(false);
                
        }
    }

    private void spray(Controller ctrl, Point point) {
        // Further reduced number of dots and tighter spray distribution
        for (int i = 0; i < 15; i++) {
            int dx = random.nextInt(radius * 2) - radius;
            int dy = random.nextInt(radius * 2) - radius;
            if (dx * dx + dy * dy <= radius * radius) {
                ctrl.setScratchPixel(new Point(point.x() + dx, point.y() + dy), ctrl.getColor());
            }
            ctrl.notifyChange();
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

    // Method to set the radius dynamically
    public void setRadius(int radius) {
        this.radius = Math.max(radius, 1); 
    }
}