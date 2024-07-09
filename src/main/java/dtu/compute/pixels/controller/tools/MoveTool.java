package dtu.compute.pixels.controller.tools;

import dtu.compute.pixels.controller.Controller;
import dtu.compute.pixels.model.Color;
import dtu.compute.pixels.model.Point;
import dtu.compute.pixels.model.Image;

public class MoveTool implements Tool {
    private boolean pressed = false;
    private Point initialPressPoint;

    @Override
    public void press(Controller ctrl, Point point) {
        pressed = true;
        initialPressPoint = point;
    }

    public void update(Controller ctrl, Point point) {
        if (pressed) {
            // Calculate the difference between the initial press point and the current point
            int deltaX = point.x() - initialPressPoint.x();
            int deltaY = point.y() - initialPressPoint.y();
    
            // Move the selected square to the new location
            Image selectedSquare = ctrl.getSelectedSquare();
    
            // Check for null values to avoid NullPointerException
            if (selectedSquare != null && ctrl.getScratch() != null) {
                // Iterate through the current coordinates and update their locations
                for (Point coordinate : ctrl.getCurrentCoordinate()) {
                    int newX = coordinate.x() + deltaX;
                    int newY = coordinate.y() + deltaY;
    
                    // Ensure the new location is within the image boundaries
                    if (newX >= 0 && newX < ctrl.getImage().getSize().width()
                            && newY >= 0 && newY < ctrl.getImage().getSize().height()) {
    
                        Point newLocation = new Point(newX, newY);
    
                        // Set the pixel in the scratch image to the corresponding pixel from the selected square
                        ctrl.getScratch().setPixel(newLocation, selectedSquare.getPixel(coordinate));
    
                        // Set the corresponding pixel in the main image to transparent
                        ctrl.getImage().setPixel(coordinate, Color.TRANSPARENT);
                    }
                }
                ctrl.notifyChange();
            }
        }
    }
  
    public void release(Controller ctrl, Point point) {
        if (pressed) {
            // Clear the first-ever selected square from the old location in the scratch imag
            
            ctrl.commitScratch();
            ctrl.setSelectedSquare(null);
            pressed = false;
        }
    }

    @Override
    public void abandon(Controller ctrl) {
        pressed = false;
    }
    
    
}
