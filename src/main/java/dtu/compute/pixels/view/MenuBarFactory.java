package dtu.compute.pixels.view;

import dtu.compute.pixels.controller.Controller;
import dtu.compute.pixels.model.Image;
import dtu.compute.pixels.model.Rect;
import dtu.compute.pixels.util.ImageUtils;
import javafx.event.ActionEvent;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.GridPane;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.FileImageOutputStream;
import java.awt.image.BufferedImage;

public class MenuBarFactory {
    private static CheckMenuItem lineToolItem;
    private static Menu layers;
    private static int number = 0;
    private static HashMap<String, Image> imageHashMap = new HashMap<>();
   
    public static MenuBar create(Stage stage, Controller ctrl) {
        final MenuBar bar = new MenuBar();
        MenuController mctrl = new MenuController(ctrl);
        Menu fileMenu = createFileMenu(stage, mctrl);
        MenuItem saveGif = createSaveGif(stage,mctrl);
        fileMenu.getItems().add(saveGif);
        Menu help = new Menu("Help");

        // Adding 'Layout' menu
        Menu layoutMenu = new Menu("Layout");
        CheckMenuItem gridItem = new CheckMenuItem("Show Grid");
        gridItem.setOnAction(event -> ctrl.toggleGridVisibility(gridItem.isSelected()));
        CheckMenuItem guideItem = new CheckMenuItem("Show Guidelines");
        guideItem.setOnAction(event -> ctrl.toggleGuideVisibility(guideItem.isSelected()));
        layoutMenu.getItems().addAll(gridItem, guideItem);

        // Adding 'layers' menu
        layers = new Menu("layers");        
        Menu LayerOptions = new Menu("Layer options");
        CheckMenuItem merge = new CheckMenuItem("Merge Layers");
        merge.setOnAction(event -> mctrl.handleMergeLayersSelection(event, merge));
        CheckMenuItem duplicateLayer = new CheckMenuItem("Duplicate layer");
        duplicateLayer.setOnAction(event -> mctrl.handleDuplicatingLayer(event,duplicateLayer));
        LayerOptions.getItems().addAll(merge,duplicateLayer);

        // Add the "File", "Help", "layoutMenu" og "layers" menus to the menu bar
        bar.getMenus().addAll(fileMenu, help, layoutMenu, layers,LayerOptions);
        bar.setUseSystemMenuBar(true);

        return bar;
    }

    public static int returnIndex(){
        return number;
    }

    public static void addLayerMenuItem(MenuController mctrl) {
        CheckMenuItem layerItem = new CheckMenuItem("Layer" + number);
        layerItem.setSelected(true);
        layers.getItems().add(layerItem);
        layerItem.setOnAction(event -> mctrl.handleLayerSelection(event, layerItem));
        number += 1;
    }
   


    public static CheckMenuItem getLineToolItem() {
        return lineToolItem;
    }
    private static Menu createFileMenu(Stage stage, MenuController mctrl) {
        Menu file = new Menu("File");
        file.getItems().setAll(
                createNewImageMenuItem(mctrl),
                createLoadImageMenuItem(stage, mctrl),
                createSaveImageMenuItem(mctrl));

        return file;
    }

    private static MenuItem createLoadImageMenuItem(Stage stage, MenuController mctrl) {
        MenuItem menuItem = new MenuItem("Load Image");
        menuItem.setOnAction(event -> mctrl.onLoadImage(stage, event));
        return menuItem;
    }

    private static MenuItem createNewImageMenuItem(MenuController mctrl) {
        MenuItem newImage = new MenuItem("New Image");
        newImage.setOnAction(event -> mctrl.onNewImage(event));
        return newImage;
    }
    private static MenuItem createSaveImageMenuItem(MenuController mctrl) {
        MenuItem saveImage = new MenuItem("Save Image");
        saveImage.setOnAction(event -> mctrl.onSaveImage(event));
        return saveImage;
    }
    private static MenuItem createSaveGif(Stage stage,MenuController mctrl) {
        MenuItem saveGif = new MenuItem("Save Gif");
        saveGif.setOnAction(event -> mctrl.onSaveGif(stage,event));
        return saveGif;
    }
    
   public static class MenuController {
        final Controller ctrl;

        public MenuController(Controller ctrl) {
            this.ctrl = ctrl;
        }

        private void handleLayerSelection(ActionEvent event, CheckMenuItem layerItem) {
            if (layerItem.isSelected()) {
                System.out.println(layerItem.getText() + " is selected");
                ctrl.images.set(Character.getNumericValue(layerItem.getText().charAt(5)),imageHashMap.get(layerItem.getText()));
                ctrl.setImage(ctrl.images.get(Character.getNumericValue(layerItem.getText().charAt(5))));
                ctrl.notifyChange();
            } else {
                System.out.println(layerItem.getText() + " is deselected");
                imageHashMap.put(layerItem.getText(),ctrl.images.get(Character.getNumericValue(layerItem.getText().charAt(5))));
                ctrl.images.set(Character.getNumericValue(layerItem.getText().charAt(5)),null);
                if(Character.getNumericValue(layerItem.getText().charAt(5)) < ctrl.images.size() - 1){
                    if (ctrl.images.get(Character.getNumericValue(layerItem.getText().charAt(5)) + 1) != null){
                    ctrl.setImage(ctrl.images.get(Character.getNumericValue(layerItem.getText().charAt(5)) + 1));
                    }
                } else if (Character.getNumericValue(layerItem.getText().charAt(5)) > 0){
                    if(ctrl.images.get(Character.getNumericValue(layerItem.getText().charAt(5)) - 1) != null){
                    ctrl.setImage(ctrl.images.get(Character.getNumericValue(layerItem.getText().charAt(5)) - 1));
                    }
                }
                ctrl.notifyChange();
            }
        }
        private void handleMergeLayersSelection(ActionEvent event, CheckMenuItem mergeItem) {
            if (mergeItem.isSelected()) {
                if (ctrl.images.size() > 1) {
                    Image mergedImage = ctrl.compressLayers();
                    ctrl.images.clear();
                    ctrl.images.add(mergedImage);
                    MenuBarFactory.number = 0;
                    MenuBarFactory.layers.getItems().clear();
                    MenuBarFactory.addLayerMenuItem(new MenuBarFactory.MenuController(ctrl));
                    ctrl.setImage(mergedImage);
                    ctrl.notifyChange();
                } else {
                    mergeItem.setSelected(false);
                }
            }
            }

            private void handleDuplicatingLayer(ActionEvent event, CheckMenuItem DuplicateItem) {
                if (DuplicateItem.isSelected()) {
                    Image mostRecentImage = ctrl.getImage();
                    ctrl.images.add(mostRecentImage);
                    MenuBarFactory.addLayerMenuItem(new MenuBarFactory.MenuController(ctrl));
                   
                }  else {
                    DuplicateItem.setSelected(false);
                }
                }
        
        public void onSaveImage(ActionEvent event) {
            Image currentImage = ctrl.compressLayers();
            if (currentImage == null) {
                error("No image to save.");
                return;
            }

            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Save Image");
            fileChooser.getExtensionFilters().addAll(
                    new ExtensionFilter("PNG Files", "*.png"));
            File selectedFile = fileChooser.showSaveDialog(null);
        
            if (selectedFile != null) {
                try {
                    ImageUtils.writePNG(selectedFile,currentImage);
                } catch (IOException e) {
                    e.printStackTrace();
                    error("Error saving image.");
                }
            }
        }    
        
        private void onNewImage(ActionEvent event) {
            var dialog = new Dialog<>();
            dialog.setTitle("New Image");
            dialog.setContentText("Create a new image.");
            dialog.getDialogPane()
                .getButtonTypes()
                .add(ButtonType.APPLY);
            dialog.getDialogPane()
                .getButtonTypes()
                .add(ButtonType.CANCEL);

            GridPane gridPane = new GridPane();
            gridPane.add(new Label("Width:"), 0, 0);
            TextField widthInput = new TextField("200");
            gridPane.add(widthInput, 1, 0);

            gridPane.add(new Label("Height:"), 0, 1);
            TextField heightInput = new TextField("200");
            gridPane.add(heightInput, 1, 1);
            dialog.getDialogPane()
                .setContent(gridPane);

            dialog.showAndWait()
                .ifPresent(response -> {
                    if (response instanceof ButtonType
                        && ((ButtonType) response).getButtonData() == ButtonBar.ButtonData.APPLY) {
                        try {
                            int width = Integer.parseInt(widthInput.getText());
                            int height = Integer.parseInt(heightInput.getText());
                            ctrl.setImage(new Image(new Rect(width, height)));
                            ctrl.images.add(ctrl.getImage());
    
                             MenuBarFactory.addLayerMenuItem(new MenuBarFactory.MenuController(ctrl));
                        } catch (NumberFormatException e) {
                            error("Width and Height can only be integers");
                        }
                    }
                });
        }
        public void onSaveGif(Stage stage, ActionEvent event) {
            if (ctrl.images.isEmpty()) {
                error("No images to save.");
                return;
            }

            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Save GIF");
            fileChooser.getExtensionFilters().addAll(
                    new FileChooser.ExtensionFilter("GIF Files", "*.gif"));

                File selectedFile = fileChooser.showSaveDialog(stage);
                if (selectedFile != null) {
                    try {
                        List<BufferedImage> bufferedImages = new ArrayList<>();

                        for (Image image : ctrl.images) {
                            bufferedImages.add(ImageUtils.asBufferedImage(image));
                        }

                        ImageWriter writer = ImageIO.getImageWritersByFormatName("gif").next();

                        FileImageOutputStream output = new FileImageOutputStream(selectedFile);
                        writer.setOutput(output);

                        ImageWriteParam writeParam = writer.getDefaultWriteParam();
                        writeParam.setCompressionMode(ImageWriteParam.MODE_DEFAULT);

                        writer.prepareWriteSequence(null);

                        for (BufferedImage bufferedImage : bufferedImages) {
                            IIOImage ioImage = new IIOImage(bufferedImage, null, null);
                            writer.writeToSequence(ioImage, null);
                        }

                        writer.endWriteSequence();
                        writer.dispose();

                        output.close();

                        Alert alert = new Alert(AlertType.INFORMATION);
                        alert.setContentText("GIF saved successfully.");
                        alert.showAndWait();
                    } catch (IOException e) {
                        e.printStackTrace();
                        error("Error saving GIF.");
                    }
                }
        }
    
        public void onLoadImage(Stage stage, ActionEvent event) {
            // Set up a file chooser to load an image
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Load Image");
            fileChooser.getExtensionFilters().addAll(
                    new ExtensionFilter("Image Files", "*.png"));

            // Show the file chooser and get the selected file
            File selectedFile = fileChooser.showOpenDialog(stage);

            // Handle the selected file
            if (selectedFile != null) {
                try {
                    // Read the image from the selected file and set it in the controller
                    Image loadedImage = ImageUtils.readPNG(selectedFile);
                    ctrl.setImage(loadedImage);
                    ctrl.images.add(ctrl.getImage());
                    MenuBarFactory.addLayerMenuItem(new MenuBarFactory.MenuController(ctrl));
                    ctrl.notifyChange();
                } catch (IOException e) {
                    e.printStackTrace();
                    // Display an error message if the image loading fails
                    error("Error loading image.");
                }
            }
        }
    }

    // Display an error message in an alert dialog
    private static void error(String s) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setContentText(s);
        alert.showAndWait();
    }
}