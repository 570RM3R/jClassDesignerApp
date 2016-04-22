/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jcd.controller;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.Cursor;
import javafx.scene.SnapshotParameters;
import javafx.scene.image.WritableImage;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javax.imageio.ImageIO;
import jcd.Diagram;
import jcd.Method;
import jcd.Variable;
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
        workspace.reloadWorkspace(index);
        Pane pane = workspace.getLeftPane();
        pane.setCursor(Cursor.CROSSHAIR);
        pane.setOnMousePressed((MouseEvent event) -> {
            diagram = new Diagram(-1, event.getX(), event.getY(), "", "", false, FXCollections.observableArrayList(), FXCollections.observableArrayList(), -1);
            diagram.setStroke(Color.BLACK);
            diagram.setFill(Color.web("#e0eae1"));
            diagram.dynamicPosition();
            Diagram.setIdCounter(Diagram.getIdCounter() + 1);
            pane.getChildren().add(diagram);
        } ) ;
        workspace.reloadWorkspace(index);
    }
        
    public void handleSaveAsCodeRequest() {
        Workspace workspace = (Workspace) app.getWorkspaceComponent();
        workspace.reloadWorkspace(index);
        Pane pane = workspace.getLeftPane();
        for(int i = 0; i < pane.getChildren().size(); i++) {
            if(pane.getChildren().get(i) instanceof Diagram) {
                Diagram diagram = (Diagram) pane.getChildren().get(i);
                String path = "work" + File.separator + "ExportedProject" + File.separator + "src"
                        + (diagram.getPackageName().isEmpty() ? "" : File.separator + diagram.getPackageName())
                        + File.separator + diagram.getNameText().getText() + ".java";
                File file = new File(path);
                file.getParentFile().mkdirs(); 
                try {
                    file.createNewFile();
                } catch (IOException ex) {
                    Logger.getLogger(PageEditController.class.getName()).log(Level.SEVERE, null, ex);
                }
                PrintWriter writer;
                try {
                    writer = new PrintWriter(path, "UTF-8");
                    writer.println("public " + (diagram.isInterface() ? "interface " : "class ") + diagram.getNameText().getText() + 
                            (diagram.getParentId() == -1 ? "{" : " extends " + findDiagramName(diagram.getParentId()) + "{"));
                    for(Variable variable : diagram.getVariableData()) {
                        writer.println("\t" + variable.toString() + ";");
                    }
                    for(Method method : diagram.getMethodData()) {
                        writer.println("\t" + method.toString() + "{\n\t" +
                                (method.getReturnType().isEmpty() ? "" : "return ") + "\n\t}");
                    }
                    writer.print("}");
                    writer.close();
                } catch (FileNotFoundException | UnsupportedEncodingException ex) {
                    Logger.getLogger(PageEditController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }

    public void handleAddInterfaceRequest() {
        Workspace workspace = (Workspace) app.getWorkspaceComponent();
        workspace.reloadWorkspace(index);
        Pane pane = workspace.getLeftPane();
        pane.setCursor(Cursor.CROSSHAIR);
        pane.setOnMousePressed((MouseEvent event) -> {
            diagram = new Diagram(-1, event.getX(), event.getY(), "", "", true, FXCollections.observableArrayList(), FXCollections.observableArrayList(), -1);
            diagram.setStroke(Color.BLACK);
            diagram.setFill(Color.web("#e0eae1"));
            diagram.dynamicPosition();
            Diagram.setIdCounter(Diagram.getIdCounter() + 1);
            pane.getChildren().add(diagram);
        } ) ;
        workspace.reloadWorkspace(index);
        
    }
    
    public void handleRemoveDiagramRequest() {
        Workspace workspace = (Workspace) app.getWorkspaceComponent();
        Pane pane = workspace.getLeftPane();
        if(index != -1) {
            pane.getChildren().remove(index);
            index = -1;
        }
        workspace.getNameTextField().setText("");
        workspace.getPackageTextField().setText("");
        workspace.getParentComboBox().setValue("");
        workspace.reloadWorkspace(index);
    }
    
    public void handleSaveAsPhotoRequest() {
        Workspace workspace = (Workspace) app.getWorkspaceComponent();
        PropertiesManager props = PropertiesManager.getPropertiesManager();
        workspace.reloadWorkspace(index);
        Pane pane = workspace.getLeftPane();
        if (index != -1) {
            diagram = (Diagram)pane.getChildren().get(index);
            diagram.setStroke(Color.BLACK);
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
            diagram.setStroke(Color.BLUE);
        }
        workspace.reloadWorkspace(index);
    }

    public void handleAddVariablesRequest() {
        Workspace workspace = (Workspace) app.getWorkspaceComponent();
        Pane pane = workspace.getLeftPane();
        if (index != -1){
            diagram = (Diagram)pane.getChildren().get(index);
            diagram.addVariable(new Variable("newRandomVariableLaLaLa", "int", true, "public"));
        }
    }

    public void handleRemoveVariablesRequest() {
        
    }

    public void handleNameUpdateRequest(String name) {
        Workspace workspace = (Workspace) app.getWorkspaceComponent();
        Pane pane = workspace.getLeftPane();
        if(index != -1){
            diagram = (Diagram)pane.getChildren().get(index);
            diagram.getNameText().setText(name);
            diagram.dynamicResize();
            workspace.reloadWorkspace(index);
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
        workspace.reloadWorkspace(index);
        Pane pane = workspace.getLeftPane();
        pane.setCursor(Cursor.HAND);
        
        pane.setOnMousePressed((MouseEvent event) -> {
            // Unselect previously selected item and set the index -1
            if (index != -1) {
                diagram = (Diagram)pane.getChildren().get(index);
                diagram.setStroke(Color.BLACK);
                workspace.getNameTextField().setText("");
                workspace.getPackageTextField().setText("");
                workspace.getParentComboBox().setValue("");
                index = -1;
            }
            // Search whether any item lies in the clicked region
            xStartingPosition = event.getX();
            yStartingPosition = event.getY();
            for (int i = pane.getChildren().size()-1; i >= 0; i --) {
                if (pane.getChildren().get(i).contains(xStartingPosition, yStartingPosition)) {
                    index = i;
                    //app.getGUI().updateToolbarControls(false);
                    break;
                }
            }
            
            if (index != -1) {
                diagram = (Diagram)pane.getChildren().get(index);
                diagram.setStroke(Color.BLUE);
                workspace.getNameTextField().setText(diagram.getNameText().getText());
                workspace.getPackageTextField().setText(diagram.getPackageName());
                workspace.getParentComboBox().setValue(findDiagramName(diagram.getParentId()));
//                workspace.getremoveButton().setDisable(false);
//                workspace.getDownButton().setDisable(false);
//                workspace.getUpButton().setDisable(false);                  
            }
        } ) ;
        
        pane.setOnMouseDragged((MouseEvent event) -> {
            if (index != -1) {
                xEndingPosition = event.getX();
                yEndingPosition = event.getY();
                diagram = (Diagram)pane.getChildren().get(index);
                diagram.setLayout(xEndingPosition - xStartingPosition, yEndingPosition - yStartingPosition);
                dragged = true;
            }
        } ) ;

        pane.setOnMouseReleased((MouseEvent event) -> {
            if (dragged) {
                diagram = (Diagram)pane.getChildren().get(index);               
                diagram.setStroke(Color.BLACK); 
                diagram.setPosition(diagram.getNameSection().getX() + diagram.getNameSection().getLayoutX(),
                        diagram.getNameSection().getY() + diagram.getNameSection().getLayoutY());
                diagram.setLayout(0, 0);
//                workspace.getremoveButton().setDisable(true);
//                workspace.getDownButton().setDisable(true);
//                workspace.getUpButton().setDisable(true);
                index = -1;
                dragged = false;
            }
        } ) ;
    }

    public void handleResizeRequest() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public void handlePackageNameUpdateRequest(String packageName) {
        Workspace workspace = (Workspace) app.getWorkspaceComponent();
        Pane pane = workspace.getLeftPane();
        if (index != -1){
            diagram = (Diagram)pane.getChildren().get(index);
            diagram.setPackageName(packageName);
        }
    }

    public void handleAddMethodsRequest() {
        Workspace workspace = (Workspace) app.getWorkspaceComponent();
        Pane pane = workspace.getLeftPane();
        if (index != -1){
            diagram = (Diagram)pane.getChildren().get(index);
            diagram.addMethod(new Method("newMethodLaLaLa", "int" , true , true, "private" , "int x", "int y", "int z"));
        }
    }

    public void handleRemoveMethodsRequest() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public void handleUndoRequest() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public void handleRedoRequest() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public void handleZoomInRequest() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public void handleZoomOutRequest() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public void handleHelpRequest() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public void handleInfoRequest() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public void handleParentComboBoxUpdateRequest(String parentName) {
        Workspace workspace = (Workspace) app.getWorkspaceComponent();
        Pane pane = workspace.getLeftPane();
        if (index != -1){
            diagram = (Diagram)pane.getChildren().get(index);
            diagram.setParentId(findDiagramId(parentName));
        }

    }
    
    public String findDiagramName(int diagramId) {
        Workspace workspace = (Workspace) app.getWorkspaceComponent();
        Pane pane = workspace.getLeftPane();
        for(int i = 0; i < pane.getChildren().size(); i++) {
            Diagram diagram = (Diagram)pane.getChildren().get(i);
            if(diagram.getDiagramId() == diagramId) {
                return diagram.getNameText().getText();
            }
        }
        return "";
    }
    
    public int findDiagramId(String diagramName) {
        Workspace workspace = (Workspace) app.getWorkspaceComponent();
        Pane pane = workspace.getLeftPane();
        for(int i = 0; i < pane.getChildren().size(); i++) {
            Diagram diagram = (Diagram)pane.getChildren().get(i);
            if(diagram.getNameText().getText().equals(diagramName)) {
                return i;
            }
        }
        return -1;
    }
}
