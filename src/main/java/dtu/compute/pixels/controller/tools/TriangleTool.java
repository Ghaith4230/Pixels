package dtu.compute.pixels.controller.tools;

import dtu.compute.pixels.controller.Controller;
import dtu.compute.pixels.model.Point;

public class TriangleTool implements Tool {
    private Point firstPoint;
    private Point secondPoint;
    private boolean firstPointSet = false;
    private boolean secondPointSet = false;

    @Override
    public void press(Controller ctrl, Point point) {
        if (!firstPointSet) {
            firstPoint = point;
            firstPointSet = true;
        } else if (!secondPointSet) {
            secondPoint = point;
            secondPointSet = true;
        } else {
            ctrl.drawTriangle(firstPoint, secondPoint, point, ctrl.getColor());
            resetPoints();
        }
    }

    @Override
    public void update(Controller ctrl, Point point) {
        if (firstPointSet) {
            ctrl.resetScratch(false);
            if (secondPointSet) {
                ctrl.drawTemporaryTriangle(firstPoint, secondPoint, point, ctrl.getColor());
            } else {
                ctrl.drawTemporaryLine(firstPoint, point, ctrl.getColor());
            }
        }
    }

    @Override
    public void release(Controller ctrl, Point point) {
    }

    @Override
    public void abandon(Controller ctrl) {
        resetPoints();
        ctrl.resetScratch(true);
    }

    private void resetPoints() {
        firstPointSet = false;
        secondPointSet = false;
    }
}