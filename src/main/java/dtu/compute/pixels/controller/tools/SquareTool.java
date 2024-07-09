package dtu.compute.pixels.controller.tools;

import dtu.compute.pixels.controller.Controller;
import dtu.compute.pixels.model.Point;

public class SquareTool implements Tool {
    private Point startPoint;
    private boolean pressed = false;

    @Override
    public void press(Controller ctrl, Point point) {
        pressed = true;
        startPoint = point;
        ctrl.drawTemporarySquare(startPoint, ctrl.adjustPointToCanvas(point), ctrl.getColor());
    }

    @Override
    public void update(Controller ctrl, Point point) {
        if (pressed) {
            ctrl.resetScratch(false);
            ctrl.drawTemporarySquare(startPoint, ctrl.adjustPointToCanvas(point), ctrl.getColor());
        }
    }

    @Override
    public void release(Controller ctrl, Point point) {
        if (pressed) {
            ctrl.drawSquare(startPoint, ctrl.adjustPointToCanvas(point), ctrl.getColor());
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