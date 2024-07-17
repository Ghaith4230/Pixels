package dtu.compute.pixels.view;

import dtu.compute.pixels.controller.Controller;
import dtu.compute.pixels.controller.Observer;
import dtu.compute.pixels.controller.tools.Pen;
import dtu.compute.pixels.controller.tools.Resize;
import dtu.compute.pixels.controller.tools.Shortcuts;
import dtu.compute.pixels.controller.tools.Text;
import dtu.compute.pixels.model.Color;
import dtu.compute.pixels.model.Image;
import dtu.compute.pixels.model.Point;
import dtu.compute.pixels.model.Rect;
import dtu.compute.pixels.util.ImageUtils;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Point2D;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.input.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Paint;
import javafx.stage.Stage;


public class Pixels extends Application implements Observer {

  // Constants for the initial and canvas size
  private final static Rect START_SIZE = new Rect(1000, 700);
  private static Rect CANVAS_SIZE = new Rect(600, 600);
  public final Controller ctrl;
  private Canvas canvas;
  private double currentMouseX;
  private double currentMouseY;

  Point2D canvasXY;

  Pane root;
  Label currentElement = new Label("");
  HBox textBox = new HBox();



  public Pixels() {
    ctrl = new Controller()
            .setColor(Color.fromARGB(0xff000000))
            .setTool(new Pen());
  }

  public static void main(String[] args) {
    launch(args);
  }

  @Override
  public void start(Stage stage) {

    root = new StackPane();
    final BorderPane layout = new BorderPane();
    ctrl.setImage(new Image(new Rect(200, 200)));
    ctrl.images.add(ctrl.getImage());
    layout.setTop(new MenuBarFactory(stage, ctrl));
    MenuBarFactory.addLayerMenuItem(new MenuBarFactory.MenuController(ctrl));

    // Create canvas and set it in the center of the layout
    canvas = createCanvas();

    root.getChildren().addAll(canvas);
    layout.setCenter(root);

    // Set up the toolbar and place it at the bottom
    layout.setBottom(new ToolBarFactory(ctrl));


    Scene scene = new Scene(layout, START_SIZE.width(), START_SIZE.height(),
            javafx.scene.paint.Color.GRAY);
    Shortcuts.addShortcuts(scene, ctrl);

    scene.setOnKeyPressed(this::sceneHandleKeyboard);

    stage.setTitle("Pixels");
    stage.setScene(scene);
    stage.show();

    // Set the stage to "maximized" mode (normal full screen)
    stage.setMaximized(true);

    ctrl.addObserver(this);
    redraw();
  }

  private void sceneHandleKeyboard(KeyEvent e) {
    if (e.getCode() != KeyCode.ENTER) {
      String newLabel;
      if (e.getCode() == KeyCode.SPACE) {
        newLabel = currentElement.getText() + ' ';
      } else {
        newLabel = currentElement.getText() + e.getText();
      }
      currentElement.setText(newLabel);
      textBox.setMaxSize(textBox.getWidth() + 8, textBox.getHeight());
    } else {
      currentElement = new Label("");
      textBox.setStyle("");
    }

  }

  // Create canvas and set up its behavior for mouse events
  private Canvas createCanvas() {
    Canvas view = new Canvas(CANVAS_SIZE.width(),CANVAS_SIZE.height());
    final var context = view.getGraphicsContext2D();
    context.setImageSmoothing(false);
    view.setOnMousePressed(e -> {
      currentMouseX = e.getX();
      currentMouseY = e.getY();

       canvasXY = canvas.localToScene(0,0);

      view.setStyle(
              "-fx-border-color: black;" +
                      "-fx-border-width: 2;" +
                      "-fx-border-style: dashed;"

      );
      if (e.getButton() == MouseButton.PRIMARY) {
        if (ctrl.getTool() instanceof Text) {
          handleText(e);
        } else {
          ctrl.setColor(ctrl.getPrimaryColor());
          ctrl.press(getPointFromEvent(CANVAS_SIZE, e));
        }
      } else if (e.getButton() == MouseButton.SECONDARY) {
        ctrl.setColor(ctrl.getSecondaryColor());
        ctrl.press(getPointFromEvent(CANVAS_SIZE, e));
      } else {
        ctrl.abandon();
      }
    });

    view.setOnMouseReleased(e -> {
      if (e.isPrimaryButtonDown() || e.isSecondaryButtonDown()) {
        return;
      }
      ctrl.release(getPointFromEvent(CANVAS_SIZE, e));
      CANVAS_SIZE = new Rect((int)canvas.getWidth(),(int)canvas.getHeight());
    });

    // Handle mouse dragging for drawing
    view.setOnMouseDragged(e -> {
      if (e.getButton() == MouseButton.PRIMARY || e.getButton() == MouseButton.SECONDARY) {
        if (ctrl.getTool() instanceof Resize) {
          handleResize(e);
        }
        ctrl.update(getPointFromEvent(CANVAS_SIZE, e));
      }
    });

    view.setOnMouseMoved(e -> {
      if (ctrl.getTool() instanceof Resize) {
        view.setCursor(Cursor.E_RESIZE);
      } else {
        view.setCursor(Cursor.DEFAULT);
      }
      ctrl.update(getPointFromEvent(CANVAS_SIZE, e));

    });


    return view;
  }

  private void handleResize(MouseEvent e) {
    double dx = e.getX() - currentMouseX;
    double dy = e.getY() - currentMouseY;

    // Calculate new size, constrained by max width and height
    double newWidth = canvas.getWidth() + dx / 3;
    double newHeight = canvas.getHeight() + dy / 3;

    // Apply new size to canvas
    canvas.setWidth(newWidth);
    canvas.setHeight(newHeight);

    // Calculate the difference in scene coordinates
    Point2D newScenePosition = canvas.localToScene(0, 0);
    double canvasDx = newScenePosition.getX() - canvasXY.getX();
    double canvasDy = newScenePosition.getY() - canvasXY.getY();

    // Adjust canvas translation to keep top-left corner in place

    System.out.println(canvas.getTranslateX());
    canvas.setTranslateX(canvas.getTranslateX() - canvasDx);
    canvas.setTranslateY(canvas.getTranslateY() - canvasDy);

    // Redraw canvas content
    redraw();

    // Update current mouse position
    currentMouseX = e.getX();
    currentMouseY = e.getY();
  }

  private void handleText(MouseEvent e) {
    Label text = new Label();

    textBox = new HBox();

    text.setPadding(new Insets(4,4,4,4));
    currentElement = text;
    textBox.setStyle(
            "-fx-border-color: black;" +
                    "-fx-border-width: 2;" +
                    "-fx-border-style: dashed;"

    );
    textBox.setMaxSize(text.getWidth() + 4,text.getHeight() + 4);
    textBox.getChildren().add(text);
    root.getChildren().add(textBox);
    textBox.setTranslateX(e.getX() - 300);
    textBox.setTranslateY(e.getY() - 300);
  }


  private Point getPointFromEvent(Rect size, MouseEvent e) {
    Rect bufferSize = ctrl.getImage().getSize();
    double x = e.getX();
    double y = e.getY();
    int px = (int) Math.max(0, Math.min(bufferSize.width() - 1, Math.floor(x / size.width() * bufferSize.width())));
    int py = (int) Math.max(0, Math.min(bufferSize.height() - 1, Math.floor(y / size.height() * bufferSize.height())));

    return new Point(px, py);
  }

  public void redraw() {
    GraphicsContext ctx = canvas.getGraphicsContext2D();
    ctx.setFill(Paint.valueOf("white"));
    ctx.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());


    for (Image layer : ctrl.images) {
      if(layer != null){
      javafx.scene.image.Image img = ImageUtils.asJavaFXImage(layer);
      ctx.drawImage(img,
              0, 0, img.getWidth(), img.getHeight(),
              0, 0, canvas.getWidth(), canvas.getHeight());
      }
    }

    javafx.scene.image.Image himg = ImageUtils.asJavaFXImage(ctrl.getScratch());
    ctx.drawImage(himg,
            0, 0, himg.getWidth(), himg.getHeight(),
            0, 0, canvas.getWidth(), canvas.getHeight());

    //  grid
    if (ctrl.isShowGrid()) {
      drawGrid(ctx);
    }

    // Guidelines
    if (ctrl.isShowGuidelines()) {
      drawGuidelines(ctx);
    }
  }

  private void drawGrid(GraphicsContext ctx) {
    int cellSize = 50;
    for (int x = 0; x < canvas.getWidth(); x += cellSize) {
        for (int y = 0; y < canvas.getHeight(); y += cellSize) {
            ctx.strokeLine(x, 0, x, canvas.getHeight());
            ctx.strokeLine(0, y, canvas.getWidth(), y);
        }
    }
  }

  private void drawGuidelines(GraphicsContext ctx) {
    ctx.strokeLine(0, canvas.getHeight() / 2, canvas.getWidth(), canvas.getHeight() / 2);
    ctx.strokeLine(canvas.getWidth() / 2, 0, canvas.getWidth() / 2, canvas.getHeight());
  }





  @Override
  public void onChange() {
    redraw();
    if(ctrl.zoomAllowed){
      updateZoom();
      }

  }

    private void updateZoom() {
      double zoomFactor = 1.2;
      double zoomLevel = ctrl.getZoomLevel();

      double mouseX = currentMouseX;
      double mouseY = currentMouseY;

      double canvasWidth = canvas.getWidth();
      double canvasHeight = canvas.getHeight();

      double offsetX = (mouseX - canvasWidth / 2) / canvasWidth;
      double offsetY = (mouseY - canvasHeight / 2) / canvasHeight;

      canvas.setScaleX(zoomLevel * zoomFactor);
      canvas.setScaleY(zoomLevel * zoomFactor);

      canvas.setTranslateX(canvas.getTranslateX() + offsetX * canvasWidth * (1 - zoomFactor));
      canvas.setTranslateY(canvas.getTranslateY() + offsetY * canvasHeight * (1 - zoomFactor));
  }

}