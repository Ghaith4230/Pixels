package dtu.compute.pixels.controller.tools;

import dtu.compute.pixels.controller.Controller;
import dtu.compute.pixels.model.Point;

public class OvalTool implements Tool {
    private Point startPoint;
    private boolean pressed = false;

    @Override
    public void press(Controller ctrl, Point point) {
        pressed = true;
        startPoint = point;
    }

    @Override
    public void update(Controller ctrl, Point point) {
        if (pressed) {
            ctrl.drawTemporaryOval(startPoint, point, ctrl.getColor());
        }
    }

    @Override
    public void release(Controller ctrl, Point point) {
        if (pressed) {
            ctrl.drawOval(startPoint, point, ctrl.getColor());
            pressed = false;
        }
    }

    @Override
    public void abandon(Controller ctrl) {
        pressed = false;
    }
}
