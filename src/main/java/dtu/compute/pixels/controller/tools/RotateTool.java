package dtu.compute.pixels.controller.tools;

import dtu.compute.pixels.controller.Controller;
import dtu.compute.pixels.model.Color;
import dtu.compute.pixels.model.Point;
import dtu.compute.pixels.model.Image;
import dtu.compute.pixels.model.Rect;

import java.util.List;

public class RotateTool implements Tool {
    private boolean pressed = false;
    private Point initialPressPoint;
    private double initialAngle;
    private List<Point> selectedCoordinates;

    @Override
    public void press(Controller ctrl, Point point) {
        pressed = true;
        initialPressPoint = point;
        initialAngle = ctrl.getSelectedRotation(); 
        selectedCoordinates = ctrl.getCurrentCoordinate();
    }

    public void update(Controller ctrl, Point point) {
        if (pressed) {
            int deltaX = point.x() - initialPressPoint.x();
            int deltaY = point.y() - initialPressPoint.y();
            double newAngle = initialAngle + calculateRotationAngle(deltaX, deltaY);
    
            ctrl.setSelectedRotation(newAngle);
            
            rotateSelectedCoordinates(selectedCoordinates, newAngle, ctrl);
    
            ctrl.notifyChange();
        }
    }

    @Override
    public void release(Controller ctrl, Point point) {
        if (pressed) {
            clearSelectedCoordinates(selectedCoordinates,ctrl);
            ctrl.getImage().paintOverWith(ctrl.getScratch());
            ctrl.notifyChange();
            pressed = false;
        }
    }

    @Override
    public void abandon(Controller ctrl) {
        pressed = false;
    }

    // Helper method to calculate the rotation angle based on mouse movement
    private double calculateRotationAngle(int deltaX, int deltaY) {
        double radians = Math.atan2(deltaY, deltaX);
        double degrees = Math.toDegrees(radians);
        if (degrees < 0) {
            degrees += 360;
        }

        return degrees;
    }

    // Helper method to rotate selected coordinates around a center point and update the scratch image
    private void rotateSelectedCoordinates(List<Point> coordinates, double angle, Controller ctrl) {
        Point center = calculateCenterPoint(coordinates);
        int width = ctrl.getScratch().getSize().width();
        int height = ctrl.getScratch().getSize().height();
        Image rotatedSelection = new Image(new Rect(width, height));
    
        for (Point coordinate : coordinates) {
            double translatedX = coordinate.x() - center.x();
            double translatedY = coordinate.y() - center.y();
            double rotatedX = translatedX * Math.cos(Math.toRadians(angle)) - translatedY * Math.sin(Math.toRadians(angle));
            double rotatedY = translatedX * Math.sin(Math.toRadians(angle)) + translatedY * Math.cos(Math.toRadians(angle));
            double finalX = rotatedX + center.x();
            double finalY = rotatedY + center.y();
            finalX = Math.max(0, Math.min(finalX, width - 1));
            finalY = Math.max(0, Math.min(finalY, height - 1));
            Color pixelValue = ctrl.getImage().getPixel(new Point((int) finalX, (int) finalY));
            rotatedSelection.setPixel(coordinate, pixelValue);
        }

        for (Point coordinate : coordinates) {
            ctrl.getScratch().setPixel(coordinate, rotatedSelection.getPixel(coordinate));
        }

    }
    
    private Point calculateCenterPoint(List<Point> coordinates) {
        int totalX = 0;
        int totalY = 0;

        for (Point coordinate : coordinates) {
            totalX += coordinate.x();
            totalY += coordinate.y();
        }

        int centerX = totalX / coordinates.size();
        int centerY = totalY / coordinates.size();

        return new Point(centerX, centerY);
    }
    public void clearSelectedCoordinates(List<Point> coordinates,Controller ctrl) {
        for (Point coordinate : coordinates) {
            ctrl.getImage().setPixel(coordinate, Color.TRANSPARENT);
        }
    }
}