package dtu.compute.pixels.model;

public record Point(int x, int y) {

    // Creates a new Point with the specified x-coordinate while keeping the same y-coordinate
    public Point withX(int x) {

        return new Point(x, this.y);
    }

    // Creates a new Point with the specified y-coordinate while keeping the same x-coordinate
    public Point withY(int y) {

        return new Point(this.x, y);
    }
}

