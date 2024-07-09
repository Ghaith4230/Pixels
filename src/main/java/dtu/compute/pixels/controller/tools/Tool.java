package dtu.compute.pixels.controller.tools;

import dtu.compute.pixels.controller.Controller;
import dtu.compute.pixels.model.Point;

/**
 * A Tool is something that can modify the state of the controller.
 */

public interface Tool {
  // Method to handle the action when a tool is pressed at a point
  void press(Controller ctrl, Point point);

  void update(Controller ctrl, Point point);

  void release(Controller ctrl, Point point);

  void abandon(Controller ctrl);
}
