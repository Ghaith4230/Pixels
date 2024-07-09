package dtu.compute.pixels.controller.tools;

import dtu.compute.pixels.controller.Controller;
import dtu.compute.pixels.model.Color;
import dtu.compute.pixels.model.Point;
import dtu.compute.pixels.model.Image;
import dtu.compute.pixels.model.Rect;

import java.util.ArrayList;
import java.util.List;

public class SelectTool implements Tool {
    private boolean pressed = false; 
    private List<Point> drawnPoints = new ArrayList<>();
    private Point initialPressPoint; 
    private Point startPoint;

    
    @Override
    public void press(Controller ctrl, Point point) {
        pressed = true; 
        initialPressPoint = point; 
        startPoint = point;
    }

    @Override
    public void update(Controller ctrl, Point point) {
        if (pressed) {
            ctrl.resetScratch(false);
            draw(ctrl, initialPressPoint, point); 
        }
    }

    public void release(Controller ctrl, Point point) {
        if (pressed) {
            Point endPoint = point;
            int minX = Math.min(startPoint.x(), endPoint.x());
            int minY = Math.min(startPoint.y(), endPoint.y());
            int maxX = Math.max(startPoint.x(), endPoint.x());
            int maxY = Math.max(startPoint.y(), endPoint.y());
    
            Rect mainImageRect = new Rect(ctrl.getImage().getSize().width(), ctrl.getImage().getSize().height());
    
            Image selectedSquare = new Image(mainImageRect);
    
            List<Point> currentCoordinate = ctrl.getCurrentCoordinate();
    
            for (int x = 0; x < mainImageRect.width(); x++) {
                for (int y = 0; y < mainImageRect.height(); y++) {
                    if (x >= minX && x <= maxX && y >= minY && y <= maxY) {
                        Point p = new Point(x, y);
                        currentCoordinate.add(p); 
                        selectedSquare.setPixel(new Point(x, y), ctrl.getImage().getPixel(currentCoordinate.get(currentCoordinate.size() - 1 )));
                    } else {
                        selectedSquare.setPixel(new Point(x, y), Color.TRANSPARENT);
                    }
                }
            }
    
            ctrl.setSelectedSquare(selectedSquare);
    
    
            System.out.println(ctrl.getSelectedSquare());
            ctrl.resetScratch(true);
           
            pressed = false;

            ctrl.notifyChange();
        }
    }

    @Override
    public void abandon(Controller ctrl) {
        pressed = false; 
        ctrl.resetScratch(true); 
        drawnPoints.clear(); 
    }

    private void draw(Controller ctrl, Point startPoint, Point endPoint) {
        drawnPoints.clear(); 
        int minX = Math.min(startPoint.x(), endPoint.x());
        int minY = Math.min(startPoint.y(), endPoint.y());
        int maxX = Math.max(startPoint.x(), endPoint.x());
        int maxY = Math.max(startPoint.y(), endPoint.y());

        for (int x = minX; x <= maxX; x++) {
            Point p1 = new Point(x, minY);
            Point p2 = new Point(x, maxY);
            ctrl.setScratchPixel(p1, ctrl.getColor());
            ctrl.setScratchPixel(p2, ctrl.getColor());
            drawnPoints.add(p1);
            drawnPoints.add(p2);
        }

        // Draw vertical lines
        for (int y = minY; y <= maxY; y++) {
            Point p1 = new Point(minX, y);
            Point p2 = new Point(maxX, y);
            ctrl.setScratchPixel(p1, ctrl.getColor());
            ctrl.setScratchPixel(p2, ctrl.getColor());
            drawnPoints.add(p1);
            drawnPoints.add(p2);
        }

        ctrl.notifyChange(); 
    }
}
