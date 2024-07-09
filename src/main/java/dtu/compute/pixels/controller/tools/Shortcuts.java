
package dtu.compute.pixels.controller.tools;

import dtu.compute.pixels.controller.Controller;
import javafx.scene.Scene;
import javafx.scene.input.KeyEvent;

public class Shortcuts {

    public static void addShortcuts(Scene scene, Controller ctrl) {

        // Add an event filter to listen for key presses
        scene.addEventFilter(KeyEvent.KEY_PRESSED, event -> {
            if (event.isControlDown()) {
                switch (event.getCode()) {
                    case Z:
                        // If Ctrl+Z is pressed, perform undo action
                        ctrl.undo();
                        break;
                    case Y:
                        // If Ctrl+Y is pressed, perform redo action
                        ctrl.redo();
                        break;
                    case PLUS:
                    case ADD:
                        // If Ctrl+Plus is pressed, increase zoom level by 20%
                        ctrl.setZoomLevel(ctrl.getZoomLevel() * 1.2);
                        break;
                    case MINUS:
                    case SUBTRACT:
                        // If Ctrl+Minus is pressed, decrease zoom level by 20%
                        ctrl.setZoomLevel(ctrl.getZoomLevel() / 1.2);
                        break;
                    
                    default:
                        // Log unhandled key presses or perform some default action
                        System.out.println("Unhandled key press: " + event.getCode());
                        break;
                }
            }
        });
    }
}