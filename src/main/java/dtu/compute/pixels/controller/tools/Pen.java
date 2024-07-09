package dtu.compute.pixels.controller.tools;

import dtu.compute.pixels.controller.Controller;
import dtu.compute.pixels.model.Point;

public class Pen implements Tool {

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
    }  
    else {
      draw(ctrl, point); 
    }
  }
  
  @Override
  public void release(Controller ctrl, Point point) {
     if (pressed) {
      draw(ctrl, point); 
      pressed = false; 
      ctrl.commitScratch(); 
    }
  }

  // Abandons the current drawing action
  @Override
  public void abandon(Controller ctrl) {
    pressed = false; 
    ctrl.resetScratch(true); 
  }
  private void draw(Controller ctrl, Point point) {
    for (int dx = -brushSize / 2; dx <= brushSize / 2; dx++) {
        for (int dy = -brushSize / 2; dy <= brushSize / 2; dy++) {
            Point p = new Point(point.x() + dx, point.y() + dy);
            ctrl.setScratchPixel(p, ctrl.getColor());
        }
    }
    ctrl.notifyChange(); 
  }
}