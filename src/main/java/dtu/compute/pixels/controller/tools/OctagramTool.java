package dtu.compute.pixels.controller.tools;

import dtu.compute.pixels.controller.Controller;
import dtu.compute.pixels.model.Color;
import dtu.compute.pixels.model.Point;

public class OctagramTool implements Tool {
    private Point centerPoint;
    private boolean pressed = false;

    @Override
    public void press(Controller ctrl, Point point) {
        pressed = true;
        centerPoint = point;
    }

    @Override
    public void update(Controller ctrl, Point point) {
        if (pressed) {
            ctrl.resetScratch(false);
            drawOctagram(ctrl, centerPoint, point, ctrl.getColor());
        }
    }

    @Override
    public void release(Controller ctrl, Point point) {
        if (pressed) {
            drawOctagram(ctrl, centerPoint, point, ctrl.getColor());
            ctrl.commitScratch();
            pressed = false;
        }
    }

    @Override
    public void abandon(Controller ctrl) {
        pressed = false;
        ctrl.resetScratch(true);
    }

    private void drawOctagram(Controller ctrl, Point center, Point edge, Color color) {
        double radius = Math.sqrt(Math.pow(center.x() - edge.x(), 2) + Math.pow(center.y() - edge.y(), 2));
        
        for (int i = 0; i < 8; i++) {
            double angle1 = 2 * Math.PI / 8 * i;
            double angle2 = 2 * Math.PI / 8 * (i + 2); // Skips a point to create the star shape
            Point p1 = adjustPoint(ctrl, new Point(
                (int) (center.x() + radius * Math.cos(angle1)),
                (int) (center.y() + radius * Math.sin(angle1))
            ));
            Point p2 = adjustPoint(ctrl, new Point(
                (int) (center.x() + radius * Math.cos(angle2)),
                (int) (center.y() + radius * Math.sin(angle2))
            ));

            ctrl.drawStraightLine(p1, p2, color);
        }
    }

    private Point adjustPoint(Controller ctrl, Point p) {
        int x = Math.min(Math.max(p.x(), 0), ctrl.getImage().getSize().width() - 1);
        int y = Math.min(Math.max(p.y(), 0), ctrl.getImage().getSize().height() - 1);
        return new Point(x, y);
    }
}
