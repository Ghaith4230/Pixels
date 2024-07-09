package dtu.compute.pixels.view;

import dtu.compute.pixels.controller.Controller;
import dtu.compute.pixels.controller.tools.Pen;
import dtu.compute.pixels.controller.tools.PentagonTool;
import dtu.compute.pixels.controller.tools.RotateTool;
import dtu.compute.pixels.controller.tools.SprayTool;
import dtu.compute.pixels.controller.tools.SquareTool;
import dtu.compute.pixels.controller.tools.StarTool;
import dtu.compute.pixels.controller.tools.TriangleTool;
import dtu.compute.pixels.controller.tools.Eraser;
import dtu.compute.pixels.controller.tools.GradientTool;
import dtu.compute.pixels.controller.tools.HexagonTool;
import dtu.compute.pixels.controller.tools.MoveTool;
import dtu.compute.pixels.controller.tools.OctagramTool;
import dtu.compute.pixels.controller.tools.OvalTool;
import dtu.compute.pixels.controller.tools.SelectTool;
import dtu.compute.pixels.controller.tools.LineTool;
import dtu.compute.pixels.controller.tools.ColorFill;
import dtu.compute.pixels.controller.tools.DottedTool;
import dtu.compute.pixels.controller.tools.Pippet;
import dtu.compute.pixels.util.ColorUtils;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.geometry.Insets;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

import java.util.ArrayList;


public class ToolBarFactory {
    ToolBar tb;
    Controller ctrl;
    private HBox colors;
    private Button primaryColorButton ;
    private Button secondaryColorButton;
    private static Rectangle chosenColor = new Rectangle();
    private static Rectangle selectedTool = new Rectangle();
    private static Timeline timeline;

    public ToolBarFactory(Controller ctrl) {
        tb = new ToolBar();
        this.ctrl = ctrl;
        
        ArrayList<Color> colorsList = makeColorsList();

        ColorPicker picker = createColorPicker(ctrl);
         primaryColorButton = createColorButton(ctrl,"primary");
         secondaryColorButton = createColorButton(ctrl,"secondary");
        Button undo = createUndoButton(ctrl);
        Button redo = createRedoButton(ctrl);
        Button move = new Button("move");
        move.setOnAction(e -> {
            ctrl.setTool(new MoveTool());
            MenuBarFactory.getLineToolItem().setSelected(false);
        });

        Rectangle colorFill = makeButton("bucket","colorFill");

        Label zoomLabel = new Label("Zoom: 1x ");
        zoomLabel.getStyleClass().add("label");
        Slider zoomSlider = new Slider(1, 11, 1);
        zoomSlider.setPrefWidth(150);
        zoomSlider.setShowTickMarks(true);
        zoomSlider.setMajorTickUnit(5);
        zoomSlider.setMinorTickCount(3);
        zoomSlider.setBlockIncrement(1);
        zoomSlider.valueProperty().addListener((obs, oldVal, newVal) -> {
            ctrl.setZoomLevel(newVal.doubleValue());
            zoomLabel.setText("Zoom level: " + (int) zoomSlider.getValue() + "x ");

        });

        Button pen = new Button("", createIcon("pen"));
        pen.setOnAction(e -> {
            ctrl.setTool(new Pen());

        });

       Rectangle pipette = makeButton("pipette","pipette");



        Button sprayButton = new Button("", createIcon("spray"));
        sprayButton.setOnAction(e -> ctrl.setTool(new SprayTool()));
        Button dottedButton = new Button("", createIcon("dot"));
        dottedButton.setOnAction(e -> ctrl.setTool(new DottedTool()));
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        Rectangle eraser = makeButton("eraser","eraser");

        ComboBox<Integer> brushSizeSelector = new ComboBox<>();
        brushSizeSelector.getItems().addAll(1, 3, 4);
        for (int i = 6; i <= 30; i += 3) {
            brushSizeSelector.getItems().add(i);
        }

        brushSizeSelector.setValue(4);
        brushSizeSelector.setOnAction(e -> {
            int selectedSize = brushSizeSelector.getValue();
            ctrl.setBrushSize(selectedSize);

        });

        Slider spraySizeSlider = new Slider(1, 10, 5);
        spraySizeSlider.setShowTickLabels(true);
        spraySizeSlider.setShowTickMarks(true);
        spraySizeSlider.setMajorTickUnit(5);
        spraySizeSlider.setBlockIncrement(1);
        spraySizeSlider.valueProperty().addListener((obs, oldVal, newVal) -> {
            if (ctrl.getTool() instanceof SprayTool) {
                ((SprayTool) ctrl.getTool()).setRadius(newVal.intValue());
            }

        });

        Label spraySizeLabel = new Label("    Adjust the spray here");
        spraySizeLabel.getStyleClass().add("label");
        VBox spraySizeContainer = new VBox(spraySizeLabel, spraySizeSlider);
        spraySizeContainer.setSpacing(5);

        MenuButton LayerTools = new MenuButton("Layer tools", createIcon("layer"));

        MenuItem mover = new MenuItem("Move", createIcon("move"));
        mover.setOnAction(e -> {
            ctrl.setTool(new MoveTool());

        });

        MenuItem Rotater = new MenuItem("Rotate", createIcon("rotate"));
        Rotater.setOnAction(e -> {
            ctrl.setTool(new RotateTool());

        });

        MenuItem Selecter = new MenuItem("Select", createIcon("select"));
        Selecter.setOnAction(e -> {
            ctrl.setTool(new SelectTool());
        });

        MenuItem addLayerer = new MenuItem("Add layer", createIcon("addlayer"));
        addLayerer.setOnAction(e -> {
            MenuBarFactory.addLayerMenuItem(new MenuBarFactory.MenuController(ctrl));
            ctrl.images.add(ctrl.createImage());
            ctrl.setImage(ctrl.images.get(ctrl.images.size() - 1));
        });

        LayerTools.getItems().addAll(Selecter, Rotater, mover, addLayerer);

        MenuButton menuToolsButton = new MenuButton("Tools", createIcon("tools_icon"));

        MenuItem penItem = new MenuItem("Pen", createIcon("pen"));
        penItem.setOnAction(e -> {
            ctrl.setTool(new Pen());

        });

        menuToolsButton.getItems().add(penItem);
        MenuItem dottedButtonItem = new MenuItem("Dotted", createIcon("dot"));
        dottedButtonItem.setOnAction(e -> {
            ctrl.setTool(new DottedTool());

        });

        menuToolsButton.getItems().add(dottedButtonItem);
        MenuItem sprayButtonItem = new MenuItem("Spray", createIcon("spray"));
        sprayButtonItem.setOnAction(e -> {
            ctrl.setTool(new SprayTool());

        });

        menuToolsButton.getItems().add(sprayButtonItem);



        MenuButton shapesButton = new MenuButton("Shapes", createIcon("shapes_icon"));

        MenuItem lineToolItem = new MenuItem("Line", createIcon("line_tool"));
        lineToolItem.setOnAction(e -> ctrl.setTool(new LineTool()));
        shapesButton.getItems().add(lineToolItem);

        MenuItem squareToolItem = new MenuItem("Square", createIcon("square_tool"));
        squareToolItem.setOnAction(e -> {
            ctrl.setTool(new SquareTool());

        });

        shapesButton.getItems().add(squareToolItem);
        MenuItem ovalToolItem = new MenuItem("Oval", createIcon("oval_tool"));
        ovalToolItem.setOnAction(e -> ctrl.setTool(new OvalTool()));
        shapesButton.getItems().add(ovalToolItem);

        MenuItem triangleToolItem = new MenuItem("Triangle", createIcon("triangle_tool"));
        triangleToolItem.setOnAction(e -> ctrl.setTool(new TriangleTool()));
        shapesButton.getItems().add(triangleToolItem);


        MenuItem starToolItem = new MenuItem("Star", createIcon("star_tool"));
        starToolItem.setOnAction(e -> ctrl.setTool(new StarTool()));
        shapesButton.getItems().add(starToolItem);


        MenuItem pentagonToolItem = new MenuItem("Pentagon", createIcon("pentagon_tool"));
        pentagonToolItem.setOnAction(e -> ctrl.setTool(new PentagonTool()));
        shapesButton.getItems().add(pentagonToolItem);


        MenuItem hexagonToolItem = new MenuItem("Hexagon", createIcon("hexagon_tool"));
        hexagonToolItem.setOnAction(e -> ctrl.setTool(new HexagonTool()));
        shapesButton.getItems().add(hexagonToolItem);


        MenuItem octagramToolItem = new MenuItem("Octagram", createIcon("octagram_tool"));
        octagramToolItem.setOnAction(e -> ctrl.setTool(new OctagramTool()));
        shapesButton.getItems().add(octagramToolItem);

        MenuButton gradientButton = new MenuButton("Gradients", createIcon("gradient_menu"));

        MenuItem gradientToolItem = new MenuItem("Gradient", createIcon("gradient"));
        gradientToolItem.setOnAction(e -> {
            Dialog<Color[]> colorDialog = createGradientColorPickerDialog(ctrl);
            colorDialog.showAndWait().ifPresent(colors -> {
                ctrl.setPrimaryColor(ColorUtils.toColor(colors[0]));
                ctrl.setSecondaryColor(ColorUtils.toColor(colors[1]));
                ctrl.setTool(new GradientTool());
            });
        });

        MenuItem gradientToolItem_2 = new MenuItem("Pretty", createIcon("pretty"));
        gradientToolItem_2.setOnAction(e -> {
            dtu.compute.pixels.model.Image image = dtu.compute.pixels.model.Image.pretty(ctrl.getImage().getSize());
            dtu.compute.pixels.model.Image temp = ctrl.getImage();
            temp.override(image);
            ctrl.setImage(temp);
            ctrl.notifyChange();
        });

        gradientButton.getItems().addAll(gradientToolItem, gradientToolItem_2);

        //this HBOX represents the box with all the colors and buttons related to it
        colors = new HBox(5);

        //the box represents the secondary and primary color buttons
        VBox colorButtons = new VBox(5);
        colorButtons.setPadding(new Insets(0,5,0,5));


        VBox colorPaleteAndUtilities = new VBox(15);




       //made the circle graphic which represents the current selected color for both of the buttons
        Circle primaryGraphic = new Circle(15,Color.BLACK);
        Circle secondaryGraphic = new Circle(15,Color.BLACK);
        primaryColorButton.setGraphic(primaryGraphic);
        primaryColorButton.setPadding(new Insets(10,10,10,10));
        secondaryColorButton.setPadding(new Insets(10,10,10,10));
        secondaryColorButton.setGraphic(secondaryGraphic);
        colors.setStyle("-fx-background-color: #f0f0f0; -fx-border-color: #c0c0c0; -fx-border-width: 1px; -fx-padding: 10px;");


        // this is the color squares
        GridPane colorPallete = createPallete(colorsList);
        colorButtons.getChildren().addAll(primaryColorButton, secondaryColorButton);


        //HBox for all the utility buttons
        HBox utilities = new HBox(15);
        utilities.getChildren().addAll(picker,colorFill,pipette, eraser);

        colorPallete.setPadding(new Insets(3,5,0,5));
        colorPaleteAndUtilities.getChildren().addAll(colorPallete,utilities);
        colors.getChildren().addAll(colorPaleteAndUtilities, colorButtons);



        colors.setSpacing(20);
        tb.getItems().addAll(menuToolsButton, brushSizeSelector, shapesButton, gradientButton, LayerTools,colors, spraySizeContainer, spacer, undo, redo);

        updateViewTimeLine();

    }

    private ArrayList<Color> makeColorsList() {

        ArrayList<Color> colors = new ArrayList<>();

        colors.add(Color.LIME);
        colors.add(Color.LIGHTYELLOW);
        colors.add(Color.GOLD);
        colors.add(Color.PINK);
        colors.add(Color.BROWN);
        colors.add(Color.GREY);
        colors.add(Color.WHITE);
        colors.add(Color.BLUE);
        colors.add(Color.LIGHTBLUE);
        colors.add(Color.INDIGO);
        colors.add(Color.GREEN);
        colors.add(Color.YELLOW);
        colors.add(Color.ORANGE);
        colors.add(Color.RED);
        colors.add(Color.DARKRED);
        colors.add(Color.BLACK);
        return colors;

    }

    public ToolBar returnToolBar(){
        return tb;
    }
  private static Dialog<Color[]> createGradientColorPickerDialog(Controller ctrl) {

    Dialog<Color[]> dialog = new Dialog<>();
    dialog.setTitle("Select Gradient Colors");
    dialog.setHeaderText("Select first and second gradient color!");


    ButtonType confirmButtonType = new ButtonType("OK", ButtonBar.ButtonData.OK_DONE);
    dialog.getDialogPane().getButtonTypes().addAll(confirmButtonType, ButtonType.CANCEL);


    ColorPicker startColorPicker = new ColorPicker();
    ColorPicker endColorPicker = new ColorPicker();


    GridPane grid = new GridPane();
    grid.setHgap(10);
    grid.setVgap(10);
    grid.add(new Label("First color:"), 0, 0);
    grid.add(startColorPicker, 1, 0);
    grid.add(new Label("Second color:"), 0, 1);
    grid.add(endColorPicker, 1, 1);

    dialog.getDialogPane().setContent(grid);


    dialog.setResultConverter(dialogButton -> {
        if (dialogButton == confirmButtonType) {
            return new Color[] {
                startColorPicker.getValue(),
                endColorPicker.getValue()
            };
        }
        return null;
    });

    return dialog;
  }

  private static GridPane createPallete(ArrayList<Color> colors){
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(15);
        int yCordinate = 0;
        int xCordinate = 0;

      DropShadow dropShadow = new DropShadow();
      dropShadow.setColor(Color.LIGHTBLUE); // Set the glow color
      dropShadow.setRadius(15); // Set the radius of the blur
      dropShadow.setSpread(0.5);

        for (int i = 0; i < colors.size(); i++) {
            Rectangle rectangle = new Rectangle(25,25);
            rectangle.setFill(colors.get(colors.size() - (1 + i)));
            rectangle.setStroke(Color.BLACK);
            if(i%colors.size()== (colors.size() / 2 ) -1){
                yCordinate += 1;
            }

           rectangle.setOnMouseEntered(e -> {
               rectangle.setEffect(dropShadow);
           });
            rectangle.setOnMouseExited(e -> {
                rectangle.setEffect(null);
            });

            rectangle.setOnMouseClicked(e ->  {
                chosenColor.setStroke(Color.BLACK);
                chosenColor = rectangle;
                rectangle.setStroke(Color.CORNFLOWERBLUE);
            } );


            grid.add(rectangle,xCordinate, yCordinate);

            System.out.println("x coordinate" + xCordinate + " y " + yCordinate);
            xCordinate += 1;
            if(xCordinate == (colors.size() / 2) - 1){
                xCordinate = 0;
            }

        }

        return grid;

  }

  private void updateViewTimeLine(){
       Timeline timeline1;

        timeline1 = new Timeline(new KeyFrame(Duration.seconds(0.5), event -> {
            updateView();
      }));
      timeline1.setCycleCount(Timeline.INDEFINITE);
      timeline1.play();
  }





  private static Button createUndoButton(Controller ctrl) {
    Button undo = new Button("",createIcon("undo"));
    undo.setOnAction(e -> ctrl.undo());
    return undo;
  }


  private static Button createColorButton(Controller ctrl,String handler) {
      Button button = new Button();
      button.setStyle("-fx-background-color: transparent; " +
              "-fx-text-fill: #ff4500; " +
              "-fx-font-size: 12px; " +
              "-fx-font-weight: bold; " +
              "-fx-border-color: #fff; " +
              "-fx-border-width: 2px; " +
              "-fx-border-radius: 50%; " +
              "-fx-background-radius: 50%; " +
              "-fx-pref-width: 30px; " +
              "-fx-pref-height: 30px; " +
              "-fx-padding: 0px; " +
              "-fx-alignment: center; ");



      // Hover effect - change border color only
      button.setOnMouseEntered(e -> {
       Circle circle = (Circle) button.getGraphic();
       circle.setStroke(Color.LIGHTBLUE);
      });
      button.setOnMouseExited(e -> {
          Circle circle = (Circle) button.getGraphic();
          circle.setStroke(circle.getFill());
      });

      button.setOnMouseClicked(e -> {

          // i start by making the border of the recently selected color black again
          chosenColor.setStroke(Color.BLACK);

          // i then make a new object of rectangle and make it the new chosenColor for placeholding purposes
          chosenColor = null;
          chosenColor = new Rectangle();

          // i set its color to null so that i can use it in the timeline to detect if the user have chosen a color or not
          chosenColor.setFill(null);

           timeline = new Timeline(new KeyFrame(Duration.seconds(0.2), event -> {
              if (chosenColor.getFill() != null) {
                  Color color = (Color)chosenColor.getFill();
                  Circle circle = (Circle) button.getGraphic();
                  circle.setFill(chosenColor.getFill());
                  ctrl.setPrimarySecondary(ColorUtils.toColor(color));
                  timeline.stop();
              } else {
                  System.out.println("Object is still null, continuing check...");
              }
          }));
          timeline.setCycleCount(Timeline.INDEFINITE);
          timeline.play();
      });
      // Add hover effect to the button
      button.setOnAction(e -> {
              switch(handler){
                  case "primary":
                      ctrl.primary = true;
                      break;
                  case "secondary":
                      ctrl.primary = false;
              }
  });
      return button;
  }



    private static Button createRedoButton(Controller ctrl) {
    Button redo = new Button("",createIcon("redo"));
    redo.setOnAction(e -> ctrl.redo());
    return redo;
  }

  private static ImageView createIcon(String iconName) {
    Image iconImage;
    try {
        iconImage = new Image(ToolBarFactory.class.getResourceAsStream("/icons/" + iconName + ".png"));
    } catch (Exception e) {
        System.err.println("Error loading icon: " + e.getMessage());
        iconImage = new Image("default_icon.png");
    }
    ImageView iconImageView = new ImageView(iconImage);
    iconImageView.setFitWidth(16);
    iconImageView.setFitHeight(16);
    return iconImageView;
  }

  public Rectangle makeButton(String path, String type){

      Rectangle button = new Rectangle(25,25);

      Image bucket = new Image(getClass().getResourceAsStream("/icons/" + path + ".png"));
      ImagePattern imagePattern = new ImagePattern(bucket);
      button.setFill(imagePattern);

      button.setOnMouseClicked(e -> {
          selectedTool.setStroke(null);
          switch(type){
              case "pipette":
                  ctrl.setTool(new Pippet());
                  break;
              case "eraser":
                  ctrl.setTool(new Eraser());
                  break;
              case "colorFill":
                  ctrl.setTool(new ColorFill());
                  break;
          }
          selectedTool =button;
          button.setStroke(Color.LIGHTBLUE);
      });

      button.setOnMouseEntered(e -> {
          button.setStroke(Color.LIGHTBLUE);
      });

      button.setOnMouseExited(e -> {
          button.setStroke(null);
      });
        return button;
  }

  private ColorPicker createColorPicker(Controller ctrl) {
    ColorPicker picker = new ColorPicker();
    picker.getStyleClass().add("button");
    picker.setValue(ColorUtils.fromColor(ctrl.getColor()));
    picker.setOnAction(e -> {
      ctrl.setColor(ColorUtils.toColor(picker.getValue()));

        ctrl.setPrimarySecondary(ColorUtils.toColor(picker.getValue()));

        updateView();

    });

    return picker;
  }

  public  void updateView(){
        Circle primaryGraphic = (Circle)primaryColorButton.getGraphic();
        Circle secondaryGraphic = (Circle)secondaryColorButton.getGraphic();

        Color color = ColorUtils.fromColor(ctrl.getSecondaryOrPrimary());

      if(ctrl.primary){
          primaryGraphic.setFill(color);
      } else {
          secondaryGraphic.setFill(color);
      }

  }
  }

