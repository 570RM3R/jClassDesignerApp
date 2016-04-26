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
import java.util.ArrayList;
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
import org.apache.commons.io.FileUtils;
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
            diagram = new Diagram(-1, event.getX(), event.getY(), -1, -1, "", "", false, false,
                    FXCollections.observableArrayList(), FXCollections.observableArrayList(), -1, new ArrayList<>());
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
        String folderPath = "work" + File.separator + "ExportedProject" + File.separator + "src";
        // Clear the existing files of the folder
        try {
            FileUtils.cleanDirectory(new File(folderPath));
        } catch (IOException ex) {
            Logger.getLogger(PageEditController.class.getName()).log(Level.SEVERE, null, ex);
        }
        for(int i = 0; i < pane.getChildren().size(); i++) {
            String packagePath = folderPath;
            if(pane.getChildren().get(i) instanceof Diagram) {
                Diagram tempDiagram = (Diagram) pane.getChildren().get(i);
                // Create appropriate directory for the java source code
                if(!tempDiagram.getPackageName().isEmpty()) {
                    String packageToken[] = tempDiagram.getPackageName().split("\\.");
                    for(String token : packageToken) {
                        packagePath += File.separator + token;
                    }
                }
                // Create java source code file
                String filePath = packagePath + File.separator + tempDiagram.getNameText().getText() + ".java";
                try (PrintWriter writer = createJavaSourceCode(tempDiagram, filePath, tempDiagram.getNameText().getText(), false)) {
                    for(Variable variable : tempDiagram.getVariableData()) {
                        writer.println("\t" + variable.exportString() + ";");
                        if(!isPrimitive(variable.getTypeName()))
                            createJavaSourceCode(tempDiagram, packagePath + File.separator +
                                    variable.getTypeName()+ ".java", variable.getTypeName(), true);
                    }
                    writer.println();
                    for(Method method : tempDiagram.getMethodData()) {
                        writer.println("\t" + method.exportString() + (method.isAbstract() ? "" : " {"));
                        
                        String[] argumentOneArray = method.getArgumentOne().split(" ");
                        String[] argumentTwoArray = method.getArgumentTwo().split(" ");
                        String[] argumentThreeArray = method.getArgumentThree().split(" ");
                        
                        if(!isPrimitive(argumentOneArray[0]))
                            createJavaSourceCode(tempDiagram, packagePath + File.separator +
                                    argumentOneArray[0] + ".java", argumentOneArray[0], true);
                        
                        if(!isPrimitive(method.getArgumentTwo().split(" ")[0]))
                            createJavaSourceCode(tempDiagram, packagePath + File.separator +
                                    argumentTwoArray[0] + ".java", argumentTwoArray[0], true);
                        
                        if(!isPrimitive(method.getArgumentThree().split(" ")[0]))
                            createJavaSourceCode(tempDiagram, packagePath + File.separator +
                                    argumentThreeArray[0] + ".java", argumentThreeArray[0], true);
                        
                        if(!method.getReturnType().isEmpty()) {
                            if(method.getReturnType().equals("char")) {
                                writer.println("\t\treturn Character.UNASSIGNED;");
                            }
                            else if(method.getReturnType().equals("boolean")) {
                                writer.println("\t\treturn true;");
                            }
                            else if(method.getReturnType().equals("String")) {
                                writer.println("\t\treturn \"\";");
                            }
                            else if(isPrimitive(method.getReturnType())) {
                                writer.println("\t\t" + method.getReturnType() + " returnedObject = 0;\n\t\treturn returnedObject;");
                            }
                            else if(!method.getReturnType().equals("void")) {
                                writer.println("\t\treturn new " + method.getReturnType() + "();");
                                createJavaSourceCode(tempDiagram, packagePath + File.separator +
                                        method.getReturnType() + ".java", method.getReturnType(), true);
                            }
                        }
                        writer.println(method.isAbstract() ? ";" :"\t}");
                    }
                    writer.print("}");
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
            diagram = new Diagram(-1, event.getX(), event.getY(), -1, -1, "", "", true, false,
                    FXCollections.observableArrayList(), FXCollections.observableArrayList(), -1, new ArrayList<>());
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
            diagram.dynamicResize();
            diagram.dynamicPosition();
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
        Workspace workspace = (Workspace) app.getWorkspaceComponent();
        Pane pane = workspace.getLeftPane();
        if (index != -1){
            diagram = (Diagram)pane.getChildren().get(index);
            if(!diagram.getVariableText().getText().isEmpty() || !diagram.getMethodText().getText().isEmpty())
                diagram.abridgeDiagram();
            else {
                diagram.updateVariableText();
                diagram.updateMethodText();
            }
            diagram.dynamicResize();
        }
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
            diagram.addMethod(new Method("newMethodLaLaLa", "Zinga" , false , true, "public" , "int x", "int y", "int z"));
            diagram.dynamicResize();
            diagram.dynamicPosition();
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
    // Find the id of a diagram
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
    // Check whether the class name is primitive type or not
    public boolean isPrimitive(String className) {
        String[] primitiveClass = {"byte", "short", "int", "long", "float", "double", "char", "boolean", "String"};
        for(int i = 0; i < primitiveClass.length; i++) {
            if(className.equals(primitiveClass[i]))
                return true;
        }
        return false;
    }
    
    // Crete a java source code file
    public PrintWriter createJavaSourceCode(Diagram tempDiagram, String filePath, String name, boolean isComplete) {
        PrintWriter writer = null;
        try {
            File file = new File(filePath);
            file.getParentFile().mkdirs();
            writer = new PrintWriter(filePath, "UTF-8");
            writer.println((tempDiagram.getPackageName().isEmpty() ? "" : "package " + tempDiagram.getPackageName() + ";\n\n") + 
                    "public " + (isComplete ? "class " : (tempDiagram.isAbstract() ? "abstract " : "") + (tempDiagram.isInterface() ? 
                    "interface " : "class ")) + name + " extends Object{" + (isComplete ? "\n\tpublic " + name + "(){}"+  "\n}" : ""));
        } catch (FileNotFoundException | UnsupportedEncodingException ex) {
            Logger.getLogger(PageEditController.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if(isComplete)
                writer.close();
        }
        return writer;
    }

}
