package dtu.compute.pixels.controller.tools;

import dtu.compute.pixels.controller.Controller;
import dtu.compute.pixels.model.Point;

public class Eraser implements Tool {

  private boolean pressed = false; 
  private int brushSize; 

  // Handles the action when the pen is pressed
  @Override
  public void press(Controller ctrl, Point point) {
    brushSize = ctrl.getBrushSize(); 
    pressed = true; 
    draw(ctrl, point); 
} 

  // Updates the drawing as the pen moves
  @Override
  public void update(Controller ctrl, Point point) {
    if (!pressed) {
    ctrl.resetScratch(false);
    }  // Reset scratch if not pressed
    else {
      draw(ctrl, point); // Continue drawing
    // Apply changes from scratch to the main image
  }
   }
  

  // Handles the action when the pen is released
  @Override
  public void release(Controller ctrl, Point point) {
     if (pressed) {
      draw(ctrl, point); // Finalize drawing
      pressed = false; // Reset pressed state
      ctrl.commitScratch(); // Apply changes from scratch to the main image
  }
  }

  // Abandons the current drawing action
  @Override
  public void abandon(Controller ctrl) {
    pressed = false; // Reset pressed state
    ctrl.resetScratch(true); // Clear the scratch image
  }
  private void draw(Controller ctrl, Point point) {
    for (int dx = -brushSize / 2; dx <= brushSize / 2; dx++) {
        for (int dy = -brushSize / 2; dy <= brushSize / 2; dy++) {
            Point p = new Point(point.x() + dx, point.y() + dy);
            ctrl.Erase(p);
        }
    }
    ctrl.notifyChange();
}
}
