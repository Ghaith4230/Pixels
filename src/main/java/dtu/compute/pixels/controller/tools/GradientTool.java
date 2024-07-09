package dtu.compute.pixels.controller.tools;

import dtu.compute.pixels.controller.Controller;
import dtu.compute.pixels.model.Color;
import dtu.compute.pixels.model.Point;

public class GradientTool implements Tool {
    private Point start;
    private Point end;
    private boolean pressed = false;

    @Override
    public void press(Controller ctrl, Point point) {
        start = point;
        pressed = true;
    }

    @Override
    public void update(Controller ctrl, Point point) {
        if (pressed) {
            end = point;
            drawGradient(ctrl, start, end);
        }
    }

    @Override
    public void release(Controller ctrl, Point end) {
        this.end = end;
        if (pressed) {
            applyGradientToEntireCanvas(ctrl);
            ctrl.commitScratch();
            ctrl.setPrimaryColor(Color.BLACK); // Updates the starting color
            ctrl.setSecondaryColor(Color.BLACK);
            pressed = false;
        }
    }

    @Override
    public void abandon(Controller ctrl) {
        pressed = false;
        ctrl.resetScratch(true);
    }

    private void drawGradient(Controller ctrl, Point start, Point end) {
        ctrl.resetScratch(true);
        Color startColor = ctrl.getColor(); // first color of the gradient
        Color endColor = ctrl.getSecondaryColor(); // second color of the gradient

        int dx = end.x() - start.x();
        int dy = end.y() - start.y();
        double distance = Math.sqrt(dx * dx + dy * dy);

        for (double t = 0; t <= 1; t += 1 / distance) {
            int x = (int) (start.x() + t * dx);
            int y = (int) (start.y() + t * dy);
            if (x >= 0 && x < ctrl.getImage().getSize().width() && y >= 0 && y < ctrl.getImage().getSize().height()) {
                Color gradientColor = interpolateColor(startColor, endColor, t);
                ctrl.setScratchPixel(new Point(x, y), gradientColor);
            }
        }

        ctrl.notifyChange();
    }

    private void applyGradientToEntireCanvas(Controller ctrl) {
        Color startColor = ctrl.getPrimaryColor();
        Color endColor = ctrl.getSecondaryColor();
        double dx = end.x() - start.x();
        double dy = end.y() - start.y();
        double length = Math.sqrt(dx * dx + dy * dy);
        for (int y = 0; y < ctrl.getImage().getSize().height(); y++) {
            for (int x = 0; x < ctrl.getImage().getSize().width(); x++) {
                double t = ((x - start.x()) * dx + (y - start.y()) * dy) / (length * length);
                t = Math.min(Math.max(t, 0), 1); 
                Color gradientColor = interpolateColor(startColor, endColor, t);
                ctrl.setScratchPixel(new Point(x, y), gradientColor);
            }
        }
    }
    
    private Color interpolateColor(Color startColor, Color endColor, double t) {
        // Uses an "ease-in-ease-out" approach
        t = t * t * (3 - 2 * t);
        double[] startFractions = startColor.toFractions();
        double[] endFractions = endColor.toFractions();

        double red = startFractions[1] + t * (endFractions[1] - startFractions[1]);
        double green = startFractions[2] + t * (endFractions[2] - startFractions[2]);
        double blue = startFractions[3] + t * (endFractions[3] - startFractions[3]);
        double alpha = startFractions[0] + t * (endFractions[0] - startFractions[0]);
        return Color.fromFractions(alpha, red, green, blue);
    }
}