/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jcd.controller;

import java.io.File;
import java.io.IOException;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.SnapshotParameters;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.effect.Bloom;
import javafx.scene.image.WritableImage;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Path;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.scene.text.Text;
import javax.imageio.ImageIO;
import jcd.gui.Workspace;
import jcd.jClassDesigner;
import properties_manager.PropertiesManager;
import static paf.settings.AppPropertyType.SAVE_ERROR_MESSAGE;
import static paf.settings.AppPropertyType.SAVE_ERROR_TITLE;
import paf.ui.AppMessageDialogSingleton;

/**
 *
 * @author Saeid
 */
public class PageEditController {
    
    // HERE'S THE FULL APP, WHICH GIVES US ACCESS TO OTHER STUFF
    jClassDesigner app;
    int index = -1;
    double xPosition, yPosition, xStartingPosition, yStartingPosition, xEndingPosition, yEndingPosition;
    Rectangle rectangle;
    Rectangle nameSection, variableSection, methodSection;
    Path path;
    boolean drawn = false, dragged = false;

    // WE USE THIS TO MAKE SURE OUR PROGRAMMED UPDATES OF UI
    // VALUES DON'T THEMSELVES TRIGGER EVENTS
    private boolean enabled;

    /**
     * Constructor for initializing this object, it will keep the app for later.
     *
     * @param initApp The JavaFX application this controller is associated with.
     */
    public PageEditController(jClassDesigner initApp) {
	// KEEP IT FOR LATER
	app = initApp;
    }
    
    /**
     * This mutator method lets us enable or disable this controller.
     *
     * @param enableSetting If false, this controller will not respond to
     * workspace editing. If true, it will.
     */
    public void enable(boolean enableSetting) {
	enabled = enableSetting;
    }
    
    public void handleAddClassRequest() {
        Workspace workspace = (Workspace) app.getWorkspaceComponent();
        workspace.reloadWorkspace();
        Pane pane = app.getGUI().getCenterPane();
        pane.setCursor(Cursor.CROSSHAIR);
        pane.setOnMousePressed((MouseEvent event) -> {
            nameSection = new Rectangle(200, 40);
            nameSection.setStroke(Color.BLACK);
            nameSection.setFill(Color.web("#e0eae1"));
            nameSection.setX(event.getX() - 100);
            nameSection.setY(event.getY() - 20);
            pane.getChildren().add(nameSection);
        } ) ;
        workspace.reloadWorkspace();
    }
        
    public void handleSaveAsCodeRequest() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public void handleAddInterfaceRequest() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public void handleSnapshotButton() {
        Workspace workspace = (Workspace) app.getWorkspaceComponent();
        workspace.reloadWorkspace();
        Pane pane = workspace.getRightPane();
        if (index != -1) {
            Shape shape = (Shape)pane.getChildren().get(index);
            shape.setEffect(null);
        }
        PropertiesManager props = PropertiesManager.getPropertiesManager();
        WritableImage image = pane.snapshot(new SnapshotParameters(), null);
        File file = new File("Pose.png");
        try {
            ImageIO.write(SwingFXUtils.fromFXImage(image, null), "png", file);
        } catch (IOException e) {
            AppMessageDialogSingleton dialog = AppMessageDialogSingleton.getSingleton();
	    dialog.show(props.getProperty(SAVE_ERROR_TITLE), props.getProperty(SAVE_ERROR_MESSAGE));
        }
        if (index != -1) {
            Shape shape = (Shape)pane.getChildren().get(index);
            shape.setEffect(new Bloom());
        }
        workspace.reloadWorkspace();
    }

    public void handleAddVariablesRequest() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public void handleRemoveVariablesRequest() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public void handleClassNameUpdateRequest(String name) {
        Workspace workspace = (Workspace) app.getWorkspaceComponent();
        TextField x = workspace.getClassNameTextField();
        
        Pane pane = app.getGUI().getCenterPane();
        
        if (index != -1){
            Rectangle object = (Rectangle) pane.getChildren().get(index);
            Text className = new Text(name);
            className.setX(object.getX() + 5);
            className.setY(object.getY() + 22);
            //className.xProperty().bind(object.layoutXProperty());
            //className.yProperty().bind(object.layoutYProperty());
            pane.getChildren().add(className);

        }
    }
    
    public void handleSelectRequest() {
//        Workspace workspace = (Workspace) app.getWorkspaceComponent();
//        workspace.reloadWorkspace();
//        workspace.getSelectionButton().setDisable(false);
//        workspace.getRectangleButton().setDisable(false);
//        workspace.getEllipseButton().setDisable(false);
//        workspace.getremoveButton().setDisable(true);
//        workspace.getDownButton().setDisable(true);
//        workspace.getUpButton().setDisable(true);
        Workspace workspace = (Workspace) app.getWorkspaceComponent();
        workspace.reloadWorkspace();
        Pane pane = app.getGUI().getCenterPane();
        pane.setCursor(Cursor.HAND);
        
        pane.setOnMousePressed((MouseEvent event) -> {
            boolean isFound = false;
            if (index != -1) {
                Shape object = (Shape)pane.getChildren().get(index);
                object.setEffect(null);
            }
            xStartingPosition = event.getX();
            yStartingPosition = event.getY();
            for (int i = pane.getChildren().size()-1; i >= 0; i --) {
                if (pane.getChildren().get(i).contains(xStartingPosition, yStartingPosition)) {
                    index = i;
                    isFound = true;
                    app.getGUI().updateToolbarControls(false);
                    break;
                }
            }
            
            if (index != -1) {
                Shape object = (Shape)pane.getChildren().get(index);
                if (!isFound) {
                    object.setEffect(null);
                    index = -1;
//                    workspace.getremoveButton().setDisable(true);
//                    workspace.getDownButton().setDisable(true);
//                    workspace.getUpButton().setDisable(true);
                }
                else {
                    object.setEffect(new Bloom());
//                    workspace.getremoveButton().setDisable(false);
//                    workspace.getDownButton().setDisable(false);
//                    workspace.getUpButton().setDisable(false);
                }
                
//                workspace.getFillColorPicker().setValue((Color)object.getFill());
//                workspace.getOutlineColorPicker().setValue((Color)object.getStroke());
//                workspace.getOutlineThiknessSlider().setValue(object.getStrokeWidth());
            }
        } ) ;
        
        pane.setOnMouseDragged((MouseEvent event) -> {
            if (index != -1) {
                xEndingPosition = event.getX();
                yEndingPosition = event.getY();
                Node object = pane.getChildren().get(index);
                object.setLayoutX(xEndingPosition - xStartingPosition);
                object.setLayoutY(yEndingPosition - yStartingPosition);
                dragged = true;
            }
        } ) ;

        pane.setOnMouseReleased((MouseEvent event) -> {
            if (dragged) {
                Node object = pane.getChildren().get(index);
                object.setEffect(null);
                if (object instanceof Rectangle) {
                    rectangle = (Rectangle) object;
                    rectangle.setX(rectangle.getX() + rectangle.getLayoutX());
                    rectangle.setY(rectangle.getY() + rectangle.getLayoutY());
                    rectangle.setLayoutX(0);
                    rectangle.setLayoutY(0);
                }
//                workspace.getremoveButton().setDisable(true);
//                workspace.getDownButton().setDisable(true);
//                workspace.getUpButton().setDisable(true);
                index = -1;
                dragged = false;
            }
//            workspace.reloadWorkspace();
        } ) ;
    }

    public void handleResizeRequest() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
