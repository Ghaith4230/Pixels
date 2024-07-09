package dtu.compute.pixels.controller.tools;

import dtu.compute.pixels.controller.Controller;
import dtu.compute.pixels.model.Point;
import dtu.compute.pixels.model.Color;

public class StarTool implements Tool {
    private Point startPoint;
    private boolean pressed = false;
    private final int numPoints = 5; // The number of points on the star
    private final int innerRadiusFactor = 2; // The ratio of the inner and outer radius of the star

    @Override
    public void press(Controller ctrl, Point point) {
        pressed = true;
        startPoint = point;
        drawStar(ctrl, startPoint, point, ctrl.getColor(), false);
    }

    @Override
    public void update(Controller ctrl, Point point) {
        if (pressed) {
            ctrl.resetScratch(false);
            drawStar(ctrl, startPoint, point, ctrl.getColor(), false);
        }
    }

    @Override
    public void release(Controller ctrl, Point point) {
        if (pressed) {
            drawStar(ctrl, startPoint, point, ctrl.getColor(), true);
            ctrl.commitScratch();
            pressed = false;
        }
    }

    @Override
    public void abandon(Controller ctrl) {
        pressed = false;
        ctrl.resetScratch(true);
    }

    private void drawStar(Controller ctrl, Point center, Point outerPoint, Color color, boolean permanent) {
        double radius = Math.sqrt(Math.pow(center.x() - outerPoint.x(), 2) + Math.pow(center.y() - outerPoint.y(), 2));
        double innerRadius = radius / innerRadiusFactor;

        Point[] outerPoints = calculateStarPoints(center, radius, numPoints, 0);
        Point[] innerPoints = calculateStarPoints(center, innerRadius, numPoints, Math.PI / numPoints);

        for (int i = 0; i < numPoints; i++) {
            if (permanent) {
                ctrl.drawStraightLine(outerPoints[i], innerPoints[i], color);
                ctrl.drawStraightLine(innerPoints[i], outerPoints[(i + 1) % numPoints], color);
            } else {
                ctrl.drawTemporaryLine(outerPoints[i], innerPoints[i], color);
                ctrl.drawTemporaryLine(innerPoints[i], outerPoints[(i + 1) % numPoints], color);
            }
        }
    }

    private Point[] calculateStarPoints(Point center, double radius, int numPoints, double offsetAngle) {
        Point[] points = new Point[numPoints];
        double angleStep = Math.PI * 2 / numPoints;

        for (int i = 0; i < numPoints; i++) {
            double angle = angleStep * i + offsetAngle;
            int x = (int) (center.x() + radius * Math.cos(angle));
            int y = (int) (center.y() + radius * Math.sin(angle));
            points[i] = new Point(x, y);
        }
        return points;
    }
}