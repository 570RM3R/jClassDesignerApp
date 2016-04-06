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
import javafx.scene.effect.Bloom;
import javafx.scene.image.WritableImage;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Ellipse;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
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
    double xStartingPosition, yStartingPosition, xEndingPosition, yEndingPosition;
    Rectangle rectangle;
    Ellipse ellipse;
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

    public void handleSelectionButton() {
        Workspace workspace = (Workspace) app.getWorkspaceComponent();
        workspace.reloadWorkspace();
        // TAKE CARE OF THE BUTTONS
        Pane pane = workspace.getRightPane();
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
                    // TAKE CARE OF THE BUTTONS
                }
                else {
                    object.setEffect(new Bloom());
                    // TAKE CARE OF THE BUTTONS
                }
                
                // TAKE CARE OF THE BUTTONS
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
                else if (object instanceof Ellipse) {
                    ellipse = (Ellipse) object;
                    ellipse.setCenterX(ellipse.getCenterX() + ellipse.getLayoutX());
                    ellipse.setCenterY(ellipse.getCenterY() + ellipse.getLayoutY());
                    ellipse.setLayoutX(0);
                    ellipse.setLayoutY(0);
                }
                // TAKE CARE OF THE BUTTONS
                index = -1;
                dragged = false;
            }
            workspace.reloadWorkspace();
        } ) ;
    }

    public void handleRemoveButton() {
        Workspace workspace = (Workspace) app.getWorkspaceComponent();
        Pane pane = workspace.getRightPane();
        pane.setCursor(Cursor.HAND);
        if (index != -1) {
            pane.getChildren().remove(index);
            app.getGUI().updateToolbarControls(false);
            // TAKE CARE OF THE BUTTONS
            index = -1;
            workspace.reloadWorkspace();
        }
    }

    public void handleRectangleButton() {
        Workspace workspace = (Workspace) app.getWorkspaceComponent();
        workspace.reloadWorkspace();
        // TAKE CARE OF THE BUTTONS
        Pane pane = workspace.getRightPane();
        pane.setCursor(Cursor.CROSSHAIR);
        if (index != -1) {
            Shape object = (Shape)pane.getChildren().get(index);
            object.setEffect(null);
        }
        pane.setOnMousePressed((MouseEvent event) -> {
            if (drawn == false) {
                xStartingPosition = event.getX();
                yStartingPosition = event.getY();
                rectangle = new Rectangle();
                // TAKE CARE OF THE BUTTONS
                pane.getChildren().add(rectangle);
                workspace.reloadWorkspace();
                drawn = true ;
                app.getGUI().updateToolbarControls(false);
            }
        } ) ;
        
        pane.setOnMouseDragged((MouseEvent event) -> {
            if (drawn == true) {
                xEndingPosition = event.getX();
                yEndingPosition = event.getY();
                rectangle.setX(xStartingPosition) ;
                rectangle.setY(yStartingPosition) ;
                rectangle.setWidth(xEndingPosition - xStartingPosition) ;
                rectangle.setHeight(yEndingPosition - yStartingPosition);
            }
        } ) ;

        pane.setOnMouseReleased((MouseEvent event) -> {
            if (drawn == true) {
                rectangle = null ;
                drawn = false ;
            }
        } ) ;
    }

    public void handleEllipseButton() {
        Workspace workspace = (Workspace) app.getWorkspaceComponent();
        workspace.reloadWorkspace();
        // TAKE CARE OF THE BUTTONS
        Pane pane = workspace.getRightPane();
        pane.setCursor(Cursor.CROSSHAIR);
        if (index != -1) {
            Shape object = (Shape)pane.getChildren().get(index);
            object.setEffect(null);
        }
        pane.setOnMousePressed((MouseEvent event) -> {
            if (drawn == false) {
                xStartingPosition = event.getX();
                yStartingPosition = event.getY();
                ellipse = new Ellipse();
                // TAKE CARE OF THE BUTTONS
                pane.getChildren().add(ellipse);
                workspace.reloadWorkspace();
                drawn = true ;
                app.getGUI().updateToolbarControls(false);
            }
        } ) ;
        
        pane.setOnMouseDragged((MouseEvent event) -> {
            if (drawn == true) {
                xEndingPosition = event.getX();
                yEndingPosition = event.getY();
                ellipse.setCenterX(xStartingPosition);
                ellipse.setCenterY(yStartingPosition);
                ellipse.setRadiusX(xEndingPosition - xStartingPosition);
                ellipse.setRadiusY(yEndingPosition - yStartingPosition);
            }
        } ) ;

        pane.setOnMouseReleased((MouseEvent event) -> {
            if (drawn == true) {
                ellipse = null ;
                drawn = false ;
            }
        } ) ;
    }

    public void handleDownButton() {
        Workspace workspace = (Workspace) app.getWorkspaceComponent();
        workspace.reloadWorkspace();
        Pane pane = workspace.getRightPane();
        if (index != -1) {
            pane.getChildren().get(index).toBack();
            app.getGUI().updateToolbarControls(false);
            // REQUEST FOCUS??
            index = 0;
            app.getGUI().updateToolbarControls(false);
        }
        workspace.reloadWorkspace();
    }

    public void handleUpButton() {
        Workspace workspace = (Workspace) app.getWorkspaceComponent();
        workspace.reloadWorkspace();
        Pane pane = workspace.getRightPane();
        pane.setCursor(Cursor.HAND);
        if (index != -1) {
            pane.getChildren().get(index).toFront();
            app.getGUI().updateToolbarControls(false);
            // REQUEST FOCUS
            index = pane.getChildren().size()-1;
            app.getGUI().updateToolbarControls(false);
        }
        workspace.reloadWorkspace();
    }

    public void handleBackgroundColorPicker(Color color) {
        Workspace workspace = (Workspace) app.getWorkspaceComponent();
        workspace.reloadWorkspace();
        Pane pane = workspace.getRightPane();
        String colorString = "-fx-background-color: #" + color.toString().substring(2) + ";";
        pane.setStyle(colorString);
        workspace.reloadWorkspace();
        app.getGUI().updateToolbarControls(false);
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

    public void handleFillColorPicker(Color color) {
        Workspace workspace = (Workspace) app.getWorkspaceComponent();
        workspace.reloadWorkspace();
        Pane pane = workspace.getRightPane();
        if (index != -1) {
            Shape shape = (Shape)pane.getChildren().get(index);
            shape.setFill(color);
            app.getGUI().updateToolbarControls(false);
            // REQUEST FOCUS
            app.getGUI().updateToolbarControls(false);
        }
        workspace.reloadWorkspace();
    }

    public void handleOutlineColorPicker(Color color) {
        Workspace workspace = (Workspace) app.getWorkspaceComponent();
        workspace.reloadWorkspace();
        Pane pane = workspace.getRightPane();
        if (index != -1) {
            Shape shape = (Shape)pane.getChildren().get(index);
            shape.setStroke(color);
            app.getGUI().updateToolbarControls(false);
            // REQUEST FOCUS
            app.getGUI().updateToolbarControls(false);
        }
        workspace.reloadWorkspace();
    }

    public void handleOutlineThiknessSlider(double strokeWidth) {
        Workspace workspace = (Workspace) app.getWorkspaceComponent();
        workspace.reloadWorkspace();
        Pane pane = workspace.getRightPane();
        if (index != -1) {
            Shape shape = (Shape)pane.getChildren().get(index);
            shape.setStrokeWidth(strokeWidth);
            app.getGUI().updateToolbarControls(false);
            // REQUEST FOCUS
            app.getGUI().updateToolbarControls(false);
        }
        workspace.reloadWorkspace();
    }
}
