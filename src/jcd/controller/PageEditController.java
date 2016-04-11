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
import javafx.scene.SnapshotParameters;
import javafx.scene.image.WritableImage;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javax.imageio.ImageIO;
import jcd.Diagram;
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
    Diagram diagram;
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
            diagram = new Diagram(event.getX(), event.getY(), false);
            pane.getChildren().add(diagram);
        } ) ;
        workspace.reloadWorkspace();
    }
        
    public void handleSaveAsCodeRequest() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public void handleAddInterfaceRequest() {
        Workspace workspace = (Workspace) app.getWorkspaceComponent();
        workspace.reloadWorkspace();
        Pane pane = app.getGUI().getCenterPane();
        pane.setCursor(Cursor.CROSSHAIR);
        pane.setOnMousePressed((MouseEvent event) -> {
            diagram = new Diagram(event.getX(), event.getY(), true);
            pane.getChildren().add(diagram);
        } ) ;
        workspace.reloadWorkspace();
        
    }

    public void handleSnapshotButton() {
        Workspace workspace = (Workspace) app.getWorkspaceComponent();
        PropertiesManager props = PropertiesManager.getPropertiesManager();
        workspace.reloadWorkspace();
        Pane pane = workspace.getRightPane();
        if (index != -1) {
            diagram = (Diagram)pane.getChildren().get(index);
            diagram.getMethodSection().setStroke(Color.BLACK);
        }
        WritableImage image = pane.snapshot(new SnapshotParameters(), null);
        File file = new File("ClassDiagram.png");
        try {
            ImageIO.write(SwingFXUtils.fromFXImage(image, null), "png", file);
        } catch (IOException e) {
            AppMessageDialogSingleton dialog = AppMessageDialogSingleton.getSingleton();
	    dialog.show(props.getProperty(SAVE_ERROR_TITLE), props.getProperty(SAVE_ERROR_MESSAGE));
        }
        if (index != -1) {
            diagram = (Diagram)pane.getChildren().get(index);
            diagram.getMethodSection().setStroke(Color.BLUE);
        }
        workspace.reloadWorkspace();
    }

    public void handleAddVariablesRequest() {
        Pane pane = app.getGUI().getCenterPane();
        if (index != -1){
            diagram = (Diagram)pane.getChildren().get(index);
            diagram.addVariable();
        }
    }

    public void handleRemoveVariablesRequest() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public void handleClassNameUpdateRequest(String name) {
        Pane pane = app.getGUI().getCenterPane();
        if (index != -1){
            diagram = (Diagram)pane.getChildren().get(index);
            diagram.getNameText().setText(name);
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
                diagram = (Diagram)pane.getChildren().get(index);
                diagram.getNameSection().setStroke(Color.BLACK);
                if(diagram.getVariableSection() != null)
                    diagram.getVariableSection().setStroke(Color.BLACK);
                if(diagram.getMethodSection() != null)
                    diagram.getMethodSection().setStroke(Color.BLACK);
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
                diagram = (Diagram)pane.getChildren().get(index);
                if (!isFound) {
                    diagram.getNameSection().setStroke(Color.BLACK);
                    if(diagram.getVariableSection() != null)
                        diagram.getVariableSection().setStroke(Color.BLACK);
                    if(diagram.getMethodSection() != null)
                        diagram.getMethodSection().setStroke(Color.BLACK);
                    index = -1;
//                    workspace.getremoveButton().setDisable(true);
//                    workspace.getDownButton().setDisable(true);
//                    workspace.getUpButton().setDisable(true);
                }
                else {
                    diagram.getNameSection().setStroke(Color.BLUE);
                    if(diagram.getVariableSection() != null)
                        diagram.getVariableSection().setStroke(Color.BLUE);
                    if(diagram.getMethodSection() != null)
                        diagram.getMethodSection().setStroke(Color.BLUE);
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
                diagram = (Diagram)pane.getChildren().get(index);
                diagram.getNameSection().setLayoutX(xEndingPosition - xStartingPosition);
                diagram.getNameSection().setLayoutY(yEndingPosition - yStartingPosition);
                diagram.getNameText().setLayoutX(xEndingPosition - xStartingPosition);
                diagram.getNameText().setLayoutY(yEndingPosition - yStartingPosition);
                if(diagram.getVariableSection() != null) {
                    diagram.getVariableSection().setLayoutX(xEndingPosition - xStartingPosition);
                    diagram.getVariableSection().setLayoutY(yEndingPosition - yStartingPosition);
                    diagram.getVariableText().setLayoutX(xEndingPosition - xStartingPosition);
                    diagram.getVariableText().setLayoutY(yEndingPosition - yStartingPosition);
                }
                if(diagram.getMethodSection() != null) {
                    diagram.getMethodSection().setLayoutX(xEndingPosition - xStartingPosition);
                    diagram.getMethodSection().setLayoutY(yEndingPosition - yStartingPosition);
                    diagram.getMethodText().setLayoutX(xEndingPosition - xStartingPosition);
                    diagram.getMethodText().setLayoutY(yEndingPosition - yStartingPosition);
                }
                dragged = true;
            }
        } ) ;

        pane.setOnMouseReleased((MouseEvent event) -> {
            if (dragged) {
                diagram = (Diagram)pane.getChildren().get(index);
                diagram.getNameSection().setStroke(Color.BLACK);
                if(diagram.getVariableSection() != null)
                    diagram.getVariableSection().setStroke(Color.BLACK);
                if(diagram.getMethodSection() != null)
                    diagram.getMethodSection().setStroke(Color.BLACK);
                diagram.getNameSection().setX(diagram.getNameSection().getX() + diagram.getNameSection().getLayoutX());
                diagram.getNameSection().setY(diagram.getNameSection().getY() + diagram.getNameSection().getLayoutY());
                diagram.getNameText().setX(diagram.getNameText().getX() + diagram.getNameText().getLayoutX());
                diagram.getNameText().setY(diagram.getNameText().getY() + diagram.getNameText().getLayoutY());
                diagram.getNameSection().setLayoutX(0);
                diagram.getNameSection().setLayoutY(0);
                diagram.getNameText().setLayoutX(0);
                diagram.getNameText().setLayoutY(0);
                if(diagram.getVariableSection() != null) {
                    diagram.getVariableSection().setX(diagram.getVariableSection().getX() + diagram.getVariableSection().getLayoutX());
                    diagram.getVariableSection().setY(diagram.getVariableSection().getY() + diagram.getVariableSection().getLayoutY());
                    diagram.getVariableText().setX(diagram.getVariableText().getX() + diagram.getVariableText().getLayoutX());
                    diagram.getVariableText().setY(diagram.getVariableText().getY() + diagram.getVariableText().getLayoutY());
                    diagram.getVariableSection().setLayoutX(0);
                    diagram.getVariableSection().setLayoutY(0);
                    diagram.getVariableText().setLayoutX(0);
                    diagram.getVariableText().setLayoutY(0);
                }
                if(diagram.getMethodSection() != null) {
                    diagram.getMethodSection().setX(diagram.getMethodSection().getX() + diagram.getMethodSection().getLayoutX());
                    diagram.getMethodSection().setY(diagram.getMethodSection().getY() + diagram.getMethodSection().getLayoutY());
                    diagram.getMethodText().setX(diagram.getMethodText().getX() + diagram.getMethodText().getLayoutX());
                    diagram.getMethodText().setY(diagram.getMethodText().getY() + diagram.getMethodText().getLayoutY());
                    diagram.getMethodSection().setLayoutX(0);
                    diagram.getMethodSection().setLayoutY(0);
                    diagram.getMethodText().setLayoutX(0);
                    diagram.getMethodText().setLayoutY(0);
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

    public void handlePackageNameUpdateRequest(String packageName) {        
        Pane pane = app.getGUI().getCenterPane();
        if (index != -1){
            diagram = (Diagram)pane.getChildren().get(index);
            diagram.setPackageName(packageName);
        }
    }

    public void handleAddMethodsRequest() {
        Pane pane = app.getGUI().getCenterPane();
        if (index != -1){
            diagram = (Diagram)pane.getChildren().get(index);
            diagram.addMethod();
        }
    }

    public void handleRemoveMethodsRequest() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
