package dtu.compute.pixels.controller.tools;

import dtu.compute.pixels.controller.Controller;
import dtu.compute.pixels.model.Point;

public class DottedTool implements Tool {
    private boolean pressed = false;
    private int dotSpacing = 5; // Spacing between dots
    private Point lastPoint = null;

    @Override
    public void press(Controller ctrl, Point point) {
        pressed = true;
        lastPoint = point;
        drawDot(ctrl, point);
    }

    @Override
    public void update(Controller ctrl, Point point) {
        if (pressed && shouldDrawDot(point)) {
            drawDot(ctrl, point);
            lastPoint = point;
        }
    }

    private boolean shouldDrawDot(Point currentPoint) {
        if (lastPoint == null) {
            return true;
        }
        int dx = currentPoint.x() - lastPoint.x();
        int dy = currentPoint.y() - lastPoint.y();
        return Math.sqrt(dx * dx + dy * dy) >= dotSpacing;
    }

    private void drawDot(Controller ctrl, Point point) {
        // Drawing a simple dot. 
        ctrl.setScratchPixel(point, ctrl.getColor());
        ctrl.notifyChange();
    }

    @Override
    public void release(Controller ctrl, Point point) {
        pressed = false;
        ctrl.commitScratch();
        lastPoint = null;
    }

    @Override
    public void abandon(Controller ctrl) {
        pressed = false;
        lastPoint = null;
        ctrl.resetScratch(true);
    }

    public void setDotSpacing(int spacing) {
        this.dotSpacing = spacing;
    }
}
