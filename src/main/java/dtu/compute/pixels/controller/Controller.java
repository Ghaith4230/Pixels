package dtu.compute.pixels.controller;

import dtu.compute.pixels.controller.tools.Tool;
import dtu.compute.pixels.model.Color;
import dtu.compute.pixels.model.Image;
import dtu.compute.pixels.model.Point;
import dtu.compute.pixels.controller.tools.ColorFill;
import java.util.ArrayList;
import java.util.List;

public class Controller {

  private final List<Observer> observers;
  private Image image; 
  private Image scratch; 
  private Image selectedSquare = null;
  private Tool tool; 
  private Color color; 
  public Color backgroundColor = Color.WHITE; 
  private Color secondaryColor = Color.BLACK;
  private Color primaryColor = Color.BLACK;
  public List<Image> changes;
  private double zoomLevel = 1.0;
  public boolean primary = true;
  public List<Image> redoChanges = new ArrayList<>();
  public List<Image> images = new ArrayList<>();
  public List<Color> lastColor = new ArrayList<>();
  public List<Color> nextColor = new ArrayList<>();
  private int brushSize = 4;
  private  List<Point> CurrentCoordinate = new ArrayList<>();
  
  // Adding grids and guidelines
  private boolean showGrid = false;
  private boolean showGuidelines = false;
  private Double Angle = 0.0;
  public boolean zoomAllowed = false;


  public Color getSecondaryOrPrimary(){
    if(primary){
      return primaryColor;
    } else {
      return secondaryColor;
    }
  }


  public void toggleGridVisibility(boolean show) {
    showGrid = show;
    notifyChange();
  }

  public Image createImage(){
    Image image0 = new Image(this.getImage().getSize());
    return image0;
  }

  public void toggleGuideVisibility(boolean show) {
      showGuidelines = show;
      notifyChange();
  }

  public boolean isShowGrid() {
      return showGrid;
  }

  public boolean isShowGuidelines() {
      return showGuidelines;
  }

  public void setSelectedRotation(Double angle){
    this.Angle = angle;
  }
  public Double getSelectedRotation(){
    return this.Angle;
  }

  // Bresenham's line algorithm to draw a straight line 
  public void drawStraightLine(Point start, Point end, Color color) {
    int x0 = start.x();
    int y0 = start.y();
    int x1 = end.x();
    int y1 = end.y();

    int dx = Math.abs(x1 - x0);
    int dy = -Math.abs(y1 - y0);
    int sx = x0 < x1 ? 1 : -1;
    int sy = y0 < y1 ? 1 : -1;
    int err = dx + dy;

    while (true) {
        setScratchPixel(new Point(x0, y0), color);

        if (x0 == x1 && y0 == y1) {
            break;
        }

        int e2 = 2 * err;
        if (e2 >= dy) {
            err += dy;
            x0 += sx;
        }
        if (e2 <= dx) {
            err += dx;
            y0 += sy;
        }
    }

    notifyChange(); 
  }

  // Same here; using Bresenham's line algorithm
  public void drawTemporaryLine(Point start, Point end, Color color) {
    int x0 = start.x();
    int y0 = start.y();
    int x1 = end.x();
    int y1 = end.y();

    int dx = Math.abs(x1 - x0);
    int dy = -Math.abs(y1 - y0);
    int sx = x0 < x1 ? 1 : -1;
    int sy = y0 < y1 ? 1 : -1;
    int err = dx + dy, e2; 

    while (true) {
        setScratchPixel(new Point(x0, y0), color);

        if (x0 == x1 && y0 == y1) break;
        e2 = 2 * err;
        if (e2 >= dy) { err += dy; x0 += sx; }
        if (e2 <= dx) { err += dx; y0 += sy; }
    }

    notifyChange();
  }

  public void clearTemporaryLine(Point start, Point end) {

  }

  // Making this method public so it can be used by the SquareTool 
  public Point adjustPointToCanvas(Point point) {
    int x = Math.min(Math.max(point.x(), 0), image.getSize().width() - 1);
    int y = Math.min(Math.max(point.y(), 0), image.getSize().height() - 1);
    return new Point(x, y);
  }

  // Adding SquareTool
  public void drawTemporarySquare(Point start, Point end, Color color) {
    resetScratch(true);

    Point adjustedStart = adjustPointToCanvas(start);
    Point adjustedEnd = adjustPointToCanvas(end);

    int xMin = Math.min(adjustedStart.x(), adjustedEnd.x());
    int yMin = Math.min(adjustedStart.y(), adjustedEnd.y());
    int xMax = Math.max(adjustedStart.x(), adjustedEnd.x());
    int yMax = Math.max(adjustedStart.y(), adjustedEnd.y());

    for (int x = xMin; x <= xMax; x++) {
        setScratchPixel(new Point(x, yMin), color);
        setScratchPixel(new Point(x, yMax), color);
    }
    for (int y = yMin; y <= yMax; y++) {
        setScratchPixel(new Point(xMin, y), color);
        setScratchPixel(new Point(xMax, y), color);
    }

    notifyChange();
  }

  public void drawSquare(Point start, Point end, Color color) {
    resetScratch(true); 

    Point adjustedStart = adjustPointToCanvas(start);
    Point adjustedEnd = adjustPointToCanvas(end);

    int xMin = Math.min(adjustedStart.x(), adjustedEnd.x());
    int yMin = Math.min(adjustedStart.y(), adjustedEnd.y());
    int xMax = Math.max(adjustedStart.x(), adjustedEnd.x());
    int yMax = Math.max(adjustedStart.y(), adjustedEnd.y());

    for (int x = xMin; x <= xMax; x++) {
        setScratchPixel(new Point(x, yMin), color);
        setScratchPixel(new Point(x, yMax), color);
    }
    for (int y = yMin; y <= yMax; y++) {
        setScratchPixel(new Point(xMin, y), color);
        setScratchPixel(new Point(xMax, y), color);
    }

    commitScratch(); 
  }

  // Adding Ovaltool
  public void drawTemporaryOval(Point start, Point end, Color color) {
    resetScratch(true);
    Point adjustedStart = adjustPointToCanvas(start);
    Point adjustedEnd = adjustPointToCanvas(end);

    int xMin = Math.min(adjustedStart.x(), adjustedEnd.x());
    int yMin = Math.min(adjustedStart.y(), adjustedEnd.y());
    int xMax = Math.max(adjustedStart.x(), adjustedEnd.x());
    int yMax = Math.max(adjustedStart.y(), adjustedEnd.y());

    int width = xMax - xMin;
    int height = yMax - yMin;

    drawOvalBorder(xMin + width / 2, yMin + height / 2, width, height, color);

    notifyChange();
  }

  private void drawOvalBorder(int x0, int y0, int width, int height, Color color) {
    int a = width / 2;
    int b = height / 2;

    for (int x = -a; x <= a; x++) {
        int y = (int) (b * Math.sqrt(1 - (x * x) / (double) (a * a)));
        safeSetScratchPixel(new Point(x0 + x, y0 + y), color);
        safeSetScratchPixel(new Point(x0 + x, y0 - y), color);
    }

    for (int y = -b; y <= b; y++) {
        int x = (int) (a * Math.sqrt(1 - (y * y) / (double) (b * b)));
        safeSetScratchPixel(new Point(x0 + x, y0 + y), color);
        safeSetScratchPixel(new Point(x0 - x, y0 + y), color);
    }
  }

  private void safeSetScratchPixel(Point point, Color color) {
    if (point.x() >= 0 && point.x() < scratch.getSize().width() && 
        point.y() >= 0 && point.y() < scratch.getSize().height()) {
        setScratchPixel(point, color);
    }
  }

  public void drawOval(Point start, Point end, Color color) {
    resetScratch(true);
    Point adjustedStart = adjustPointToCanvas(start);
    Point adjustedEnd = adjustPointToCanvas(end);

    int xMin = Math.min(adjustedStart.x(), adjustedEnd.x());
    int yMin = Math.min(adjustedStart.y(), adjustedEnd.y());
    int xMax = Math.max(adjustedStart.x(), adjustedEnd.x());
    int yMax = Math.max(adjustedStart.y(), adjustedEnd.y());

    int width = xMax - xMin;
    int height = yMax - yMin;
    int centerX = xMin + width / 2;
    int centerY = yMin + height / 2;

    drawOvalBorder(centerX, centerY, width, height, color);

    commitScratch();
  }

  // Adding TriangleTool
  public void drawTemporaryTriangle(Point p1, Point p2, Point p3, Color color) {
    resetScratch(true);

    // Draws temporary lines between the dots
    drawTemporaryLine(p1, p2, color);
    drawTemporaryLine(p2, p3, color);
    drawTemporaryLine(p3, p1, color);

    notifyChange();
  }

  public void drawTriangle(Point p1, Point p2, Point p3, Color color) {
    resetScratch(true); 

    drawStraightLine(p1, p2, color);
    drawStraightLine(p2, p3, color);
    drawStraightLine(p3, p1, color);

    commitScratch(); 
  }

   // Adding StarTool, located under Startool.java :)**
   // Adding PentagonTool, located under PentagonTool.java :)**
   // Adding HexagonTool, located under HexagonTool.java :)**
   // Adding OctagramTool, located under OctagramTool.java :)**

  // Constructor initializes observers list
  public Controller() {
    observers = new ArrayList<>();
    changes = new ArrayList<>();
    
  }

  public List<Point> getCurrentCoordinate() {
      return CurrentCoordinate;
  }

  public void setCurrentCoordinate(List<Point> currentCoordinate) {
      CurrentCoordinate = currentCoordinate;
  }

  public Image getImage() {
    return image;
  }

  public void undo() {
    if (!changes.isEmpty()) {
        Image lastChange = changes.remove(changes.size() - 1);
        Color lastBgColor = lastColor.remove(lastColor.size() - 1);
        redoChanges.add(this.image.clone());
        nextColor.add(this.backgroundColor);

        this.setBackgroundColor(lastBgColor);
        this.image.override(lastChange);

        notifyChange();
    }
  }

  public int getBrushSize(){
    return this.brushSize;
  }
  public void setBrushSize(int size){
    this.brushSize = size;
  }

  public void redo() {
    if (!redoChanges.isEmpty()) {
        Image lastRedoChange = redoChanges.remove(redoChanges.size() - 1);
        Color nextBgColor = nextColor.remove(nextColor.size() - 1);
        changes.add(this.image.clone());
        lastColor.add(this.backgroundColor);

        this.backgroundColor = nextBgColor;
        this.image.override(lastRedoChange);

        notifyChange();
    }
  }

  public void setZoomLevel(double newZoomLevel) {
    zoomAllowed = true;
    this.zoomLevel = newZoomLevel;
    notifyChange();
    zoomAllowed = false;
  }

  public double getZoomLevel() {
    return this.zoomLevel;
  }

  public Image getSelectedSquare() {
    return selectedSquare;
  }

  public void setSelectedSquare(Image selectedSquare) {
    this.selectedSquare = selectedSquare;
    notifyChange();
  }

  public void clearSelectedSquare() {
    selectedSquare = null;
    notifyChange();
  }

  public Controller setImage(Image image) {
    this.image = image;
    scratch = new Image(image.getSize());
    
    
    return this;
  }

  private void addChange() {
    Image clonedImage = this.image.clone();
    Color currentBGcolor = this.getbackgroundColor();
    lastColor.add(currentBGcolor);
    changes.add(clonedImage);
    
  }

  public Color getColor() {
    return color;
  }

  public Controller setColor(Color color) {
    this.color = color;
    notifyChange();
    return this;
  }
  public Color getbackgroundColor() {
    return backgroundColor;
  }

  public Controller setBackgroundColor(Color backgroundColor) {
    this.backgroundColor = backgroundColor;
    notifyChange();
    return this;
  }
  public Controller setPrimarySecondary(Color color) {
    if(primary){
      this.primaryColor = color;
    } else{
      this.secondaryColor = color;
    }
    return this;
  }
   public Color getPrimaryColor() {
    return primaryColor;
  }
   public Color getSecondaryColor() {
    return secondaryColor;
  }
  
  public void setPrimaryColor(Color color) {
    this.primaryColor = color;
  }
   public void setSecondaryColor(Color color) {
    this.secondaryColor = color;
  }

  public Controller press(Point point) {
    tool.press(this, point);
    return this;
  }

  public Controller abandon() {
    tool.abandon(this);
    return this;
  }

  public Controller release(Point point) {
    tool.release(this, point);
    return this;
  }

  public Controller update(Point point) {
    tool.update(this, point);
    return this;
  }

  public Controller click(Point point) {
    tool.press(this, point);
    tool.release(this, point);
    return this;
  }
  
  /**
   * Paint the scratch over the current image.
   */

  public void commitScratch() {
    addChange();
    this.image.paintOverWith(this.scratch);
    this.resetScratch(true); 
    if(this.getTool() instanceof ColorFill){
    this.setBackgroundColor(this.getColor());
    }
  }
  
  /**
   * Set the scratch image all transparent.
   *
   * @param notify notify observers that stuff has changed.
   */

  public void resetScratch(boolean notify) {
    this.scratch.reset(Color.TRANSPARENT); 
    if (notify) {
      this.notifyChange(); 
    }
  }

  public void changeScratchColor(Color newColor) {
    for (int x = 0; x < scratch.getSize().width(); x++) {
        for (int y = 0; y < scratch.getSize().height(); y++) {
            scratch.setPixel(new Point(x, y), newColor);
        }
    }
    notifyChange();
}


  /**
   * Sets a pixel on the scratch image.
   *
   * @param point
   * @param color
   */

  public void setScratchPixel(Point point, Color color) {
    this.scratch.setPixel(point, color);
  }

  public void Erase(Point point) {
    this.image.Erase(point);
  }

  /**
   * Part of the observer pattern Adds an observer from the list of observers.
   *
   * @param observer, the observer to be notified on change.
   */

  public Controller addObserver(Observer observer) {
    observers.add(observer); 
    return this;
  }

  /**
   * Part of the observer pattern. Removes an observer from the list of observers.
   *
   * @param observer, the observer to be no longer notified on change.
   * @return a boolean indicating if the observer was in the list.
   */

  public boolean removeObserver(Observer observer) {
    return observers.remove(observer); 
  }

  /**
   * Part of the observer pattern. Notify all observers that the state was changed.
   */
  
  public void notifyChange() {
    for (Observer o : observers) {
      o.onChange(); // Informs each observer about the change
    }
  }

  public Image getScratch() {
    return scratch;
  }

  public Color getColor(Point point) {
    return this.image.getPixel(point);
  }

  public Tool getTool() {
    return tool;
  }

  public Controller setTool(Tool t) {
    this.tool = t;
    notifyChange();
    return this;
  }
  public Image compressLayers(){
    Image compressedImage = new Image(this.getImage().getSize());
  for(int i = 0; i < images.size(); i++){
    
    if (images.get(i) != null){
      compressedImage.paintOverWith(images.get(i));
    }
  }
  return compressedImage;
  
  }
}