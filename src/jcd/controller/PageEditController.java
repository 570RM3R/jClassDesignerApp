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
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.Cursor;
import javafx.scene.SnapshotParameters;
import javafx.scene.control.TableColumn;
import javafx.scene.image.WritableImage;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javax.imageio.ImageIO;
import jcd.Connector;
import org.apache.commons.io.FileUtils;
import jcd.Diagram;
import jcd.Grid;
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
    boolean snapEnabled;
    jClassDesigner app;
    int index = -1;
    double xStartingPosition, yStartingPosition, xEndingPosition, yEndingPosition;
    Diagram diagram;
    Connector connector;
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
    
    public boolean getSnapEnabled() {
        return snapEnabled;
    }
    
    public void handleAddClassRequest() {
        Workspace workspace = (Workspace) app.getWorkspaceComponent();
        workspace.reloadWorkspace(index);
        Pane pane = workspace.getLeftPane();
        pane.setCursor(Cursor.CROSSHAIR);
        pane.setOnMousePressed((MouseEvent event) -> {
            diagram = new Diagram(-1, event.getX(), event.getY(), "\t\t\t\t", "", false, false, false, FXCollections.observableArrayList(),
                    FXCollections.observableArrayList(), new ArrayList(), new ArrayList(), new ArrayList(), new ArrayList());
            if(snapEnabled)
                diagram.setPosition((int)(diagram.getNameSection().getX()/10)*10, (int)(diagram.getNameSection().getY()/10)*10);
            finishAddingDiagram(diagram);
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
                        writer.println("\t" + method.exportString() + (method.isAbstract().equals("true") ? "" : " {"));
                        
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
                        writer.println(method.isAbstract().equals("true") ? ";" :"\t}");
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
            diagram = new Diagram(-1, event.getX(), event.getY(), "\t\t\t\t", "", true, false, false, FXCollections.observableArrayList(),
                    FXCollections.observableArrayList(), new ArrayList(), new ArrayList(), new ArrayList(), new ArrayList());
            if(snapEnabled)
                diagram.setPosition((int)(diagram.getNameSection().getX()/10)*10, (int)(diagram.getNameSection().getY()/10)*10);
            finishAddingDiagram(diagram);
        } ) ;
        workspace.reloadWorkspace(index);
        
    }
    
    public void handleRemoveRequest() {
        Workspace workspace = (Workspace) app.getWorkspaceComponent();
        Pane pane = workspace.getLeftPane();
        if (index != -1 && pane.getChildren().get(index) instanceof Diagram){
            diagram = (Diagram)pane.getChildren().get(index);
            removeDiagramDependencies();
            pane.getChildren().remove(index);
            index = -1;
        }
        if (index != -1 && pane.getChildren().get(index) instanceof Connector){
            connector = (Connector)pane.getChildren().get(index);
            removeConnectorDependencies();
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
        if (index != -1 && pane.getChildren().get(index) instanceof Diagram) {
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
        if (index != -1 && pane.getChildren().get(index) instanceof Diagram) {
            diagram = (Diagram)pane.getChildren().get(index);
            diagram.setStroke(Color.BLUE);
        }
        workspace.reloadWorkspace(index);
    }

    public void handleAddVariablesRequest() {
        Workspace workspace = (Workspace) app.getWorkspaceComponent();
        Pane pane = workspace.getLeftPane();
        if (index != -1 && pane.getChildren().get(index) instanceof Diagram){
            diagram = (Diagram)pane.getChildren().get(index);
            diagram.addVariable(new Variable("", "", "false", ""));
            diagram.updateVariableText();
            diagram.dynamicResize();
            diagram.dynamicPosition();
            updateConnectors(diagram);
        }
        workspace.reloadWorkspace(index);
    }

    public void handleRemoveVariablesRequest(Variable selectedVariable) {
        Workspace workspace = (Workspace) app.getWorkspaceComponent();
        Pane pane = workspace.getLeftPane();
        if (index != -1 && pane.getChildren().get(index) instanceof Diagram){
            diagram = (Diagram)pane.getChildren().get(index);
            diagram.removeVariable(selectedVariable);
            diagram.updateVariableText();
            diagram.dynamicResize();
            diagram.dynamicPosition();
            updateConnectors(diagram);
        }
    }

    public void handleNameUpdateRequest(String name) {
        Workspace workspace = (Workspace) app.getWorkspaceComponent();
        Pane pane = workspace.getLeftPane();
       if (index != -1 && pane.getChildren().get(index) instanceof Diagram){
            diagram = (Diagram)pane.getChildren().get(index);
            diagram.getNameText().setText(name);
            diagram.dynamicResize();
            updateConnectors(diagram);
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
                if (pane.getChildren().get(index) instanceof Diagram) {
                    diagram = (Diagram)pane.getChildren().get(index);
                    diagram.setStroke(Color.BLACK);
                }
                else if(pane.getChildren().get(index) instanceof Connector) {
                    connector = (Connector)pane.getChildren().get(index);
                    connector.setStroke(Color.BLACK);
                    connector.setDragEnabled(false);
                }
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
                    if(pane.getChildren().get(i) instanceof Diagram || pane.getChildren().get(i) instanceof Connector){
                        index = i;
                        //app.getGUI().updateToolbarControls(false);
                        break;
                    }
                }
            }
            
            if (index != -1){
                if(pane.getChildren().get(index) instanceof Diagram) {
                    diagram = (Diagram)pane.getChildren().get(index);
                    diagram.setStroke(Color.BLUE);
                    workspace.getNameTextField().setText(diagram.getNameText().getText());
                    workspace.getPackageTextField().setText(diagram.getPackageName());
                    int lastParentId = diagram.getInheritanceData().isEmpty() ? -1 : diagram.getInheritanceData().get(diagram.getInheritanceData().size()-1);
                    Diagram parent = findDiagram(lastParentId);
                    workspace.getParentComboBox().setValue(parent == null ? "" : parent.getNameText().getText());
                }
                else if(pane.getChildren().get(index) instanceof Connector){
                    connector = (Connector)pane.getChildren().get(index);
                    connector.setStroke(Color.BLUE);
                    connector.setDragEnabled(true);
                    workspace.getNameTextField().setText("");
                    workspace.getPackageTextField().setText("");
                    workspace.getParentComboBox().setValue("");
                }
//                workspace.getremoveButton().setDisable(false);
//                workspace.getDownButton().setDisable(false);
//                workspace.getUpButton().setDisable(false);                  
            }
        } ) ;
        
        pane.setOnMouseDragged((MouseEvent event) -> {
            if (index != -1 && pane.getChildren().get(index) instanceof Diagram){
                xEndingPosition = event.getX();
                yEndingPosition = event.getY();
                diagram = (Diagram)pane.getChildren().get(index);
                diagram.setLayout(xEndingPosition - xStartingPosition, yEndingPosition - yStartingPosition);
                dragged = true;
            }
        } ) ;

        pane.setOnMouseReleased((MouseEvent event) -> {
            if (dragged && pane.getChildren().get(index) instanceof Diagram) {
                diagram = (Diagram)pane.getChildren().get(index);               
                diagram.setStroke(Color.BLACK); 
                if(snapEnabled)
                    diagram.setPosition((int)((diagram.getNameSection().getX() + diagram.getNameSection().getLayoutX())/10)*10,
                        (int)((diagram.getNameSection().getY() + diagram.getNameSection().getLayoutY())/10)*10);
                else
                    diagram.setPosition(diagram.getNameSection().getX() + diagram.getNameSection().getLayoutX(),
                        diagram.getNameSection().getY() + diagram.getNameSection().getLayoutY());
                diagram.dynamicPosition();
                diagram.setLayout(0, 0);
                updateConnectors(diagram);
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
        if (index != -1 && pane.getChildren().get(index) instanceof Diagram){
            diagram = (Diagram)pane.getChildren().get(index);
            if(!diagram.getVariableText().getText().isEmpty() || !diagram.getMethodText().getText().isEmpty()) {
                diagram.abridgeDiagram();
                diagram.setAbriged(true);
            }
            else {
                diagram.updateVariableText();
                diagram.updateMethodText();
                diagram.setAbriged(false);
            }
            diagram.dynamicResize();
            updateConnectors(diagram);
        }
    }

    public void handlePackageNameUpdateRequest(String packageName) {
        Workspace workspace = (Workspace) app.getWorkspaceComponent();
        Pane pane = workspace.getLeftPane();
        if (index != -1 && pane.getChildren().get(index) instanceof Diagram){
            diagram = (Diagram)pane.getChildren().get(index);
            diagram.setPackageName(packageName);
        }
    }

    public void handleAddMethodsRequest() {
        Workspace workspace = (Workspace) app.getWorkspaceComponent();
        Pane pane = workspace.getLeftPane();
        if (index != -1 && pane.getChildren().get(index) instanceof Diagram){
            diagram = (Diagram)pane.getChildren().get(index);
            diagram.addMethod(new Method("", "" , "false" , "false", "" , "", "", ""));
            diagram.updateMethodText();
            diagram.dynamicResize();
            diagram.dynamicPosition();
            updateConnectors(diagram);
        }
        workspace.reloadWorkspace(index);
    }
    
    public void handleGridRequest(boolean snapEnabled) {
        this.snapEnabled = snapEnabled;
    }

    public void handleRemoveMethodsRequest(Method selectedMethod) {
        Workspace workspace = (Workspace) app.getWorkspaceComponent();
        Pane pane = workspace.getLeftPane();
        if (index != -1 && pane.getChildren().get(index) instanceof Diagram){
            diagram = (Diagram)pane.getChildren().get(index);
            diagram.removeMethod(selectedMethod);
            diagram.updateMethodText();
            diagram.dynamicResize();
            diagram.dynamicPosition();
            updateConnectors(diagram);
        }
    }

    public void handleUndoRequest() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public void handleRedoRequest() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public void handleZoomInRequest() {

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
        if (index != -1 && pane.getChildren().get(index) instanceof Diagram){
            diagram = (Diagram)pane.getChildren().get(index);
            Diagram parent = findDiagram(parentName);
                // Create new parent diagram if it doesn't exist in the workspace
                if(parent == null && !isPrimitive(parentName)) {
                    parent = new Diagram(-1, diagram.getNameSection().getX() + 62.5, diagram.getNameSection().getY() + diagram.heightProperty().doubleValue() + 180,
                            parentName, diagram.getPackageName(), false, false, false, FXCollections.observableArrayList(),
                            FXCollections.observableArrayList(), new ArrayList(), new ArrayList(), new ArrayList(), new ArrayList());
                    finishAddingDiagram(parent);
                }
                // Set proper data, and connect the child with the parent
                if(parent != null && diagram.getDiagramId() != parent.getDiagramId()) {  
                    if (diagram.getInheritanceData().isEmpty() || (!diagram.getInheritanceData().isEmpty() && !diagram.getInheritanceData().contains(parent.getDiagramId()))) {
                        diagram.getInheritanceData().add(parent.getDiagramId());
                        finishAddingConnector(diagram, parent, 1);
                    }
                }
        }
    }
    
    // Find a connector from its id
    public Connector findConnector(int connectorId) {
        Workspace workspace = (Workspace) app.getWorkspaceComponent();
        Pane pane = workspace.getLeftPane();
        if(connectorId == -1)
            return null;
        for(int i = 0; i < pane.getChildren().size(); i++) {
            if (pane.getChildren().get(i) instanceof Connector) {
                Connector tempConnector = (Connector)pane.getChildren().get(i);
                if(tempConnector.getConnectorId() == connectorId)
                    return tempConnector;
            }
        }
        return null;
    }
    
    // Find a diagram from its id
    public Diagram findDiagram(int diagramId) {
        Workspace workspace = (Workspace) app.getWorkspaceComponent();
        Pane pane = workspace.getLeftPane();
        if(diagramId == -1)
            return null;
        for(int i = 0; i < pane.getChildren().size(); i++) {
            if (pane.getChildren().get(i) instanceof Diagram) {
                Diagram tempDiagram = (Diagram)pane.getChildren().get(i);
                if(tempDiagram.getDiagramId() == diagramId)
                    return tempDiagram;
            }
        }
        return null;
    }
    
    // Find a diagram from its name
    public Diagram findDiagram(String diagramName) {
        Workspace workspace = (Workspace) app.getWorkspaceComponent();
        Pane pane = workspace.getLeftPane();
        for(int i = 0; i < pane.getChildren().size(); i++) {
            if (pane.getChildren().get(i) instanceof Diagram) {
                Diagram tempDiagram = (Diagram)pane.getChildren().get(i);
                if(tempDiagram.getNameText().getText().equals(diagramName))
                    return tempDiagram;
            }
        }
        return null;
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
    
    // Retruns the coordinates of shortest path between source and diagram
    public ObservableList<Double> findLinePath(Diagram source, Diagram destination) {        
        double[] sourceCoordinates = {
                // Source top
                source.getNameSection().getX() + (source.widthProperty().doubleValue()/2),
                source.getNameSection().getY(),
                // Source bottom
                source.getNameSection().getX() + (source.widthProperty().doubleValue()/2),
                source.getNameSection().getY() + (source.heightProperty().doubleValue()),
                // Source right
                source.getNameSection().getX() + (source.widthProperty().doubleValue()),
                source.getNameSection().getY() + (source.heightProperty().doubleValue()/2),
                // Source left
                source.getNameSection().getX(),
                source.getNameSection().getY() + (source.heightProperty().doubleValue()/2),
                };
        double[] destinationCoordinates = {
                // Destination top
                destination.getNameSection().getX() + (destination.widthProperty().doubleValue()/2),
                destination.getNameSection().getY(),
                // Destination bottom
                destination.getNameSection().getX() + (destination.widthProperty().doubleValue()/2),
                destination.getNameSection().getY() + (destination.heightProperty().doubleValue()),
                // Destination right
                destination.getNameSection().getX() + (destination.widthProperty().doubleValue()),
                destination.getNameSection().getY() + (destination.heightProperty().doubleValue()/2),
                // Destination left
                destination.getNameSection().getX(),
                destination.getNameSection().getY() + (destination.heightProperty().doubleValue()/2),
                };
        
        double[] minData = new double[2];
        minData[0] = 0;
        
        for(int i = 0; i < sourceCoordinates.length; i += 2){
            for(int j = 0; j < destinationCoordinates.length; j += 2) {
                double distance = Math.sqrt((destinationCoordinates[j]-sourceCoordinates[i]) * (destinationCoordinates[j]-sourceCoordinates[i]) +
                        ((destinationCoordinates[j+1]-sourceCoordinates[i+1])) * (destinationCoordinates[j+1]-sourceCoordinates[i+1]));
                if(i == 0)
                    minData[1] = distance;
                else if(distance < minData[1]) {
                    minData[0] = i * 10 + j;
                    minData[1] = distance;
                }
            }
        }
        
        ObservableList<Double> linePath = FXCollections.observableArrayList();
        linePath.addAll(sourceCoordinates[(int)minData[0]/10], sourceCoordinates[((int)minData[0]/10) + 1], 
            destinationCoordinates[(int)minData[0]%10], destinationCoordinates[((int)minData[0]%10) + 1]);
        return linePath;
    }
    // Update connector paths of a diagram when it's moved
    private void updateConnectors(Diagram tempDiagram) {
//        for(int i = 0; i < tempDiagram.getConnectorData().size(); i++) {
//                Connector connector = findConnector(tempDiagram.getConnectorData().get(i));
//                if(connector != null)
//                    connector.updateConnectorPath(findLinePath(findDiagram(connector.getSourceId()), findDiagram(connector.getDestinationId())));
//        }
    }
    
    // Handle variable table updates
    public void handleVariableTableUpdateRequest(TableColumn.CellEditEvent<Variable, String> tableColumn, int option) {
        Workspace workspace = (Workspace) app.getWorkspaceComponent();
        Pane pane = workspace.getLeftPane();
        if (index != -1 && pane.getChildren().get(index) instanceof Diagram){
            switch(option) {
                case 1:
                    ((Variable)tableColumn.getTableView().getItems().get(tableColumn.getTablePosition().getRow())).setVariableName(tableColumn.getNewValue());
                    break;
                case 2:
                    ((Variable)tableColumn.getTableView().getItems().get(tableColumn.getTablePosition().getRow())).setTypeName(tableColumn.getNewValue());
                    Diagram tempDiagram = findDiagram(tableColumn.getNewValue());
                    if(tempDiagram == null && !isPrimitive(tableColumn.getNewValue())) {
                        tempDiagram = new Diagram(-1, diagram.getNameSection().getX() + 62.5, diagram.getNameSection().getY() + diagram.heightProperty().doubleValue() + 180,
                                tableColumn.getNewValue(), diagram.getPackageName(), false, false, false, FXCollections.observableArrayList(),
                                FXCollections.observableArrayList(), new ArrayList(), new ArrayList(), new ArrayList(), new ArrayList());
                        finishAddingDiagram(tempDiagram);
                        finishAddingConnector(diagram, tempDiagram, 1);
                    }
                    break;
                case 3:
                    ((Variable)tableColumn.getTableView().getItems().get(tableColumn.getTablePosition().getRow())).setStatic(tableColumn.getNewValue());
                    break;
                case 4:
                    ((Variable)tableColumn.getTableView().getItems().get(tableColumn.getTablePosition().getRow())).setAccessType(tableColumn.getNewValue());
                    break;
            }
            diagram = (Diagram)pane.getChildren().get(index);
            diagram.updateVariableText();
        }
        workspace.reloadWorkspace(index);
    }
    
    // Handle method table updates
    public void handleMethodTableUpdateRequest(TableColumn.CellEditEvent<Method, String> tableColumn, int option) {
        Workspace workspace = (Workspace) app.getWorkspaceComponent();
        Pane pane = workspace.getLeftPane();
        if (index != -1 && pane.getChildren().get(index) instanceof Diagram){
            switch(option) {
                case 1:
                    ((Method)tableColumn.getTableView().getItems().get(tableColumn.getTablePosition().getRow())).setMethodName(tableColumn.getNewValue());
                    break;
                case 2:
                    ((Method)tableColumn.getTableView().getItems().get(tableColumn.getTablePosition().getRow())).setReturnType(tableColumn.getNewValue());
                    Diagram tempDiagram = findDiagram(tableColumn.getNewValue());
                    if(tempDiagram == null && !isPrimitive(tableColumn.getNewValue())) {
                        tempDiagram = new Diagram(-1, diagram.getNameSection().getX() + 62.5, diagram.getNameSection().getY() + diagram.heightProperty().doubleValue() + 180,
                                tableColumn.getNewValue(), diagram.getPackageName(), false, false, false, FXCollections.observableArrayList(),
                                FXCollections.observableArrayList(), new ArrayList(), new ArrayList(), new ArrayList(), new ArrayList());
                        finishAddingDiagram(tempDiagram);
                        finishAddingConnector(diagram, tempDiagram, 1);
                    }
                    break;
                case 3:
                    ((Method)tableColumn.getTableView().getItems().get(tableColumn.getTablePosition().getRow())).setStatic(tableColumn.getNewValue());
                    break;
                case 4:
                    ((Method)tableColumn.getTableView().getItems().get(tableColumn.getTablePosition().getRow())).setAbstract(tableColumn.getNewValue());
                    break;
                case 5:
                    ((Method)tableColumn.getTableView().getItems().get(tableColumn.getTablePosition().getRow())).setAccessType(tableColumn.getNewValue());
                    break;
                case 6:
                    ((Method)tableColumn.getTableView().getItems().get(tableColumn.getTablePosition().getRow())).setArgumentOne(tableColumn.getNewValue());
                    tempDiagram = findDiagram(tableColumn.getNewValue().split(" ")[0]);
                    if(tempDiagram == null && !isPrimitive(tableColumn.getNewValue().split(" ")[0])) {
                        tempDiagram = new Diagram(-1, diagram.getNameSection().getX() + 62.5, diagram.getNameSection().getY() + diagram.heightProperty().doubleValue() + 180,
                                tableColumn.getNewValue().split(" ")[0], diagram.getPackageName(), false, false, false, FXCollections.observableArrayList(),
                                FXCollections.observableArrayList(), new ArrayList(), new ArrayList(), new ArrayList(), new ArrayList());
                        finishAddingDiagram(tempDiagram);
                        finishAddingConnector(diagram, tempDiagram, 1);
                    }
                    break;
                case 7:
                    ((Method)tableColumn.getTableView().getItems().get(tableColumn.getTablePosition().getRow())).setArgumentTwo(tableColumn.getNewValue());
                    tempDiagram = findDiagram(tableColumn.getNewValue().split(" ")[0]);
                    if(tempDiagram == null && !isPrimitive(tableColumn.getNewValue().split(" ")[0])) {
                        tempDiagram = new Diagram(-1, diagram.getNameSection().getX() + 62.5, diagram.getNameSection().getY() + diagram.heightProperty().doubleValue() + 180,
                                tableColumn.getNewValue().split(" ")[0], diagram.getPackageName(), false, false, false, FXCollections.observableArrayList(),
                                FXCollections.observableArrayList(), new ArrayList(), new ArrayList(), new ArrayList(), new ArrayList());
                        finishAddingDiagram(tempDiagram);
                        finishAddingConnector(diagram, tempDiagram, 1);
                    }
                    break;
                case 8:
                    ((Method)tableColumn.getTableView().getItems().get(tableColumn.getTablePosition().getRow())).setArgumentThree(tableColumn.getNewValue());
                    tempDiagram = findDiagram(tableColumn.getNewValue().split(" ")[0]);
                    if(tempDiagram == null && !isPrimitive(tableColumn.getNewValue().split(" ")[0])) {
                        tempDiagram = new Diagram(-1, diagram.getNameSection().getX() + 62.5, diagram.getNameSection().getY() + diagram.heightProperty().doubleValue() + 180,
                                tableColumn.getNewValue().split(" ")[0], diagram.getPackageName(), false, false, false, FXCollections.observableArrayList(),
                                FXCollections.observableArrayList(), new ArrayList(), new ArrayList(), new ArrayList(), new ArrayList());
                        finishAddingDiagram(tempDiagram);
                        finishAddingConnector(diagram, tempDiagram, 1);
                    }
                    break;
            }
            diagram = (Diagram)pane.getChildren().get(index);
            diagram.updateMethodText();
        }
        workspace.reloadWorkspace(index);
    }
    
    // Remove dependencies of a diagram when it is removed
    public void removeDiagramDependencies() {
        Workspace workspace = (Workspace) app.getWorkspaceComponent();
        Pane pane = workspace.getLeftPane();
        for(int i = 0; i < diagram.getConnectorData().size(); i++) {
            for(int j = 0; j < pane.getChildren().size(); j++) {
                if(pane.getChildren().get(j) instanceof Connector) {
                    Connector tempConnector = (Connector)pane.getChildren().get(j);
                    if(tempConnector.getConnectorId() == diagram.getConnectorData().get(i))
                        pane.getChildren().remove(j);
                }
            }
        }
    }
    // Remove dependencies of a connector when it is removed
    public void removeConnectorDependencies() {
        Workspace workspace = (Workspace) app.getWorkspaceComponent();
        Pane pane = workspace.getLeftPane();
        for(int i = 0; i < pane.getChildren().size(); i++) {
            if(pane.getChildren().get(i) instanceof Diagram) {
                Diagram tempDiagram = (Diagram)pane.getChildren().get(i);
                if(tempDiagram.getDiagramId() == connector.getSourceId()) {
                    tempDiagram.removeInheritanceData(connector.getDestinationId());
                    break;
                }
            }
        }
    }
    // Finish adding a diagram
    private void finishAddingDiagram(Diagram newDiagram) {
        Workspace workspace = (Workspace) app.getWorkspaceComponent();
        Pane pane = workspace.getLeftPane();
        newDiagram.setStroke(Color.BLACK);
        newDiagram.setFill(Color.web("#e0eae1"));
        newDiagram.dynamicResize();
        newDiagram.dynamicPosition();
        pane.getChildren().add(newDiagram);
    }
    
    private void finishAddingConnector(Diagram sourceDiagram, Diagram destinationDiagram, int option) {
        Workspace workspace = (Workspace) app.getWorkspaceComponent();
        Pane pane = workspace.getLeftPane();
        connector = new Connector(-1, 10, sourceDiagram.getDiagramId(), destinationDiagram.getDiagramId(), 
                sourceDiagram.getConnectionX(1).doubleValue(), sourceDiagram.getConnectionY(1).doubleValue(), 
                destinationDiagram.getConnectionX(3).doubleValue(), destinationDiagram.getConnectionY(3).doubleValue());
        //connector.bindProperties(sourceDiagram.getConnectionX(1), sourceDiagram.getConnectionY(1),
                //destinationDiagram.getConnectionX(3), destinationDiagram.getConnectionY(3));
        //connector.updateConnectorPath(linePath);
        sourceDiagram.getConnectorData().add(connector.getConnectorId());
        destinationDiagram.getConnectorData().add(connector.getConnectorId());
        pane.getChildren().add(connector);
    }

    public void handleGridRequest(boolean isSelected, double width, double height) {
        Workspace workspace = (Workspace) app.getWorkspaceComponent();
        Pane pane = workspace.getLeftPane();
        if(isSelected) {
            pane.getChildren().add(new Grid(width, height));
            pane.getChildren().get(pane.getChildren().size()-1).toBack();
            if(index != -1)
                index++;
        }
        else {
            pane.getChildren().remove(0);
            if(index != -1)
                index--;
        }
    }
    // Bind the connectors respective diagram
    public void bindConnectors(){
        
    }
    
}
