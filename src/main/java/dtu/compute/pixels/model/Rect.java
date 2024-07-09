package dtu.compute.pixels.model;

public record Rect(int width, int height) {

    // Calculates the area of the rectangle
    public int area() {
        return width * height;
    }

    // Translates a point from one rectangle's coordinate system to another
    public Point into(Rect from, Point p) {

        int px = Math.round(p.x() / from.width * width);
        int py = Math.round(p.y() / from.height * height);

        return new Point(px, py);
    }

    // Creates a new Rect with the specified width, keeping the same height
    public Rect withWidth(int width) {

        return new Rect(width, height);
    }

    public Rect withHeight(int height) {

        return new Rect(width, height);
    }
}