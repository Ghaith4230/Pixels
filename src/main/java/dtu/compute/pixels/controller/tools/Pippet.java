package dtu.compute.pixels.controller.tools;

import dtu.compute.pixels.controller.Controller;
import dtu.compute.pixels.model.Point;
import dtu.compute.pixels.model.Color;

public class Pippet implements Tool {
    private boolean pressed = false; 
    private Color pippetColor;

    @Override
    public void press(Controller ctrl, Point point) {
        pressed = true; 
        pippetColor = ctrl.getColor(point); 
        ctrl.setPrimarySecondary(pippetColor); 
    }

    @Override
    public void update(Controller ctrl, Point point) {
        if (!pressed) {
            ctrl.resetScratch(false);
        } 
        ctrl.setPrimarySecondary(pippetColor); 
    }

    @Override
    public void release(Controller ctrl, Point point) {
        if (pressed) {
            pressed = false; 
        }
    }

    @Override
    public void abandon(Controller ctrl) {
        pressed = false; 
    }
}
