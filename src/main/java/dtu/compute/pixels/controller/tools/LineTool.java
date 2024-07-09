package dtu.compute.pixels.controller.tools;

import dtu.compute.pixels.controller.Controller;
import dtu.compute.pixels.model.Point;

public class LineTool implements Tool {

    private Point startPoint;
    private boolean pressed = false;

    @Override
    public void press(Controller ctrl, Point point) {
        pressed = true;
        startPoint = point;
        // Draw a temporary line from the starting point to the current point
        ctrl.drawTemporaryLine(startPoint, point, ctrl.getColor());
    }

    @Override
    public void update(Controller ctrl, Point point) {
        if (pressed) {
            ctrl.resetScratch(false);
            ctrl.drawTemporaryLine(startPoint, point, ctrl.getColor());
        }
    }

    @Override
    public void release(Controller ctrl, Point point) {
        if (pressed) {
            ctrl.drawStraightLine(startPoint, point, ctrl.getColor());
            ctrl.commitScratch(); 
            pressed = false;
        }
    }

    @Override
    public void abandon(Controller ctrl) {
        pressed = false;
        ctrl.resetScratch(true);
    }
}