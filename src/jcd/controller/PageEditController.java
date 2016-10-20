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
import javafx.geometry.Insets;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.SnapshotParameters;
import javafx.scene.control.TableColumn;
import javafx.scene.image.WritableImage;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;
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
    int version;
    boolean snapEnabled;
    boolean drawn;
    boolean dragged;
    boolean undone;
    int index;
    double xStart;
    double yStart;
    double xEnd;
    double yEnd;
    Diagram diagram;
    Connector connector;
    jClassDesigner app;
    ArrayList<ArrayList<Node>>versionData;

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
        version = -1;
        snapEnabled= false;
        drawn = false;
        dragged = false;
        undone = false;
        index = -1;
        xStart = 0;
        yStart = 0;
        xEnd = 0;
        yEnd = 0;
        app = initApp;
        versionData = new ArrayList();
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
            finishAddingDiagram(event.getX(), event.getY(), "\t\t\t\t", "", false);
            workspace.reloadWorkspace(index);
        } ) ;
    }
        
    public void handleSaveAsCodeRequest() {
        Workspace workspace = (Workspace) app.getWorkspaceComponent();
        workspace.reloadWorkspace(index);
        Pane pane = workspace.getLeftPane();
        String folderPath = "work" + File.separator + "ExportedProject" + File.separator + "src" + File.separator + "default_package";
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
                if(!tempDiagram.isGeneric()) {
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
                        writer.println();
                        // Write the variable data
                        for(Variable variable : tempDiagram.getVariableData()) {
                            writer.println("\t" + variable.exportString() + ";");
                        }
                        writer.println();
                        // Write the method data
                        for(Method method : tempDiagram.getMethodData()) {
                            writer.println("\t" + method.exportString() + (method.isAbstract().equals("true") ? "" : " {"));
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
                                    createJavaSourceCode(tempDiagram, packagePath + File.separator + method.getReturnType() + ".java", method.getReturnType(), true);
                                }
                            }
                            writer.println(method.isAbstract().equals("true") ? ";" :"\t}");
                        }
                        writer.print("}");
                    }
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
            finishAddingDiagram(event.getX(), event.getY(), "\t\t\t\t", "", true);
            workspace.reloadWorkspace(index);
        } ) ;
        
    }
    
    public void handleRemoveRequest() {
        Workspace workspace = (Workspace) app.getWorkspaceComponent();
        Pane pane = workspace.getLeftPane();
        if (index != -1 && pane.getChildren().get(index) instanceof Diagram){
            diagram = (Diagram)pane.getChildren().get(index);
            removeDiagramDependencies(diagram);
            pane.getChildren().remove(index);
            index = -1;
        }
        if (index != -1 && pane.getChildren().get(index) instanceof Connector){
            connector = (Connector)pane.getChildren().get(index);
            removeConnectorDependencies(connector);
            pane.getChildren().remove(index);
            index = -1;
        }
        workspace.getNameTextField().setText("");
        workspace.getPackageTextField().setText("");
        workspace.getParentComboBox().setValue("");
        createVersion();
        workspace.reloadWorkspace(index);
    }
    
    public void handleSaveAsPhotoRequest() {
        Workspace workspace = (Workspace) app.getWorkspaceComponent();
        PropertiesManager props = PropertiesManager.getPropertiesManager();
        double zoomIndex = workspace.getZoomSlider().getValue();
        workspace.getZoomSlider().setValue(2);
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
        workspace.getZoomSlider().setValue(zoomIndex);
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
            createVersion();
            workspace.reloadWorkspace(index);
        }
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
            createVersion();
            workspace.reloadWorkspace(index);
        }
    }

    public void handleNameUpdateRequest(String name) {
        Workspace workspace = (Workspace) app.getWorkspaceComponent();
        Pane pane = workspace.getLeftPane();
       if (index != -1 && pane.getChildren().get(index) instanceof Diagram){
            diagram = (Diagram)pane.getChildren().get(index);
            diagram.getNameText().setText(name);
            diagram.dynamicResize();
            createVersion();
            workspace.reloadWorkspace(index);
        }
    }
    
    public void handleSelectRequest() {
        app.getGUI().updateToolbarControls("1111111.1011011");
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
                    connector.setStroke(Color.BLACK, false);
                    connector.setDragEnabled(false);
                }
                workspace.getNameTextField().setText("");
                workspace.getPackageTextField().setText("");
                workspace.getParentComboBox().setValue("");
                workspace.getVariableTableView().setItems(null);
                workspace.getMethodTableView().setItems(null);
                index = -1;
            }
            // Search whether any item lies in the clicked region
            xStart = event.getX();
            yStart = event.getY();
            for (int i = pane.getChildren().size()-1; i >= 0; i --) {
                if (pane.getChildren().get(i).contains(xStart, yStart)) {
                    if(pane.getChildren().get(i) instanceof Diagram){
                        index = i;
                        app.getGUI().updateToolbarControls("1111111.1111111");
                        break;
                    }
                    if(pane.getChildren().get(i) instanceof Connector){
                        index = i;
                        app.getGUI().updateToolbarControls("1111111.1011111");
                        break;
                    }
                }
                else {
                    app.getGUI().updateToolbarControls("1111111.1011011");
                }
            }
            
            if (index != -1){
                if(pane.getChildren().get(index) instanceof Diagram) {
                    diagram = (Diagram)pane.getChildren().get(index);
                    diagram.setStroke(Color.BLUE);
                    workspace.getNameTextField().setText(diagram.getNameText().getText());
                    workspace.getPackageTextField().setText(diagram.getPackageName());
                    workspace.reloadWorkspace(index);
                    int lastParentId = diagram.getExtensionData().isEmpty() ? -1 : diagram.getExtensionData().get(diagram.getExtensionData().size()-1);
                    Diagram parent = findDiagram(lastParentId);
                    workspace.getParentComboBox().setValue(parent == null ? "" : parent.getNameText().getText());
                }
                else if(pane.getChildren().get(index) instanceof Connector){
                    connector = (Connector)pane.getChildren().get(index);
                    connector.setStroke(Color.BLUE, true);
                    connector.setDragEnabled(true);
                    workspace.getNameTextField().setText("");
                    workspace.getPackageTextField().setText("");
                    workspace.getParentComboBox().setValue("");
                }               
            }
        } ) ;
        
        pane.setOnMouseDragged((MouseEvent event) -> {
            if (index != -1 && pane.getChildren().get(index) instanceof Diagram){
                xEnd = event.getX();
                yEnd = event.getY();
                diagram = (Diagram)pane.getChildren().get(index);
                diagram.setLayout(xEnd - xStart, yEnd - yStart);
                setConnectorLayout( xEnd - xStart, yEnd - yStart);
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
                setConnectorPosition();
                app.getGUI().updateToolbarControls("1111111.1011011");
                createVersion();
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
            createVersion();
        }
    }

    public void handlePackageNameUpdateRequest(String packageName) {
        Workspace workspace = (Workspace) app.getWorkspaceComponent();
        Pane pane = workspace.getLeftPane();
        if (index != -1 && pane.getChildren().get(index) instanceof Diagram){
            diagram = (Diagram)pane.getChildren().get(index);
            diagram.setPackageName(packageName);
            diagram.updateVariableText();
            diagram.updateMethodText();
            diagram.dynamicResize();
            diagram.dynamicPosition();
            createVersion();
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
            createVersion();
            workspace.reloadWorkspace(index);
        }
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
            createVersion();
            workspace.reloadWorkspace(index);
        }
    }

    public void handleUndoRequest() {
        Workspace workspace = (Workspace) app.getWorkspaceComponent();
        Pane pane = workspace.getLeftPane();
        pane.getChildren().clear();
        if(version > 0) {
            version--;
            for(Node node: versionData.get(version)) {
                if(node instanceof Diagram) {
                    Diagram tempDiagram = (Diagram)node;
                    pane.getChildren().add(new Diagram(tempDiagram));
                }
                else if(node instanceof Connector) {
                    Connector tempConnector = (Connector)node;
                    pane.getChildren().add(new Connector(tempConnector));
                }
            }
            undone = true;
            workspace.reloadWorkspace(-1);
        }
    }

    public void handleRedoRequest() {
        Workspace workspace = (Workspace) app.getWorkspaceComponent();
        Pane pane = workspace.getLeftPane();
        if(version < versionData.size()) {
            pane.getChildren().clear();
            for(Node node: versionData.get(version)) {
                if(node instanceof Diagram) {
                    Diagram tempDiagram = (Diagram)node;
                    pane.getChildren().add(new Diagram(tempDiagram));
                }
                else if(node instanceof Connector) {
                    Connector tempConnector = (Connector)node;
                    pane.getChildren().add(new Connector(tempConnector));
                }
            }
            version++;
            workspace.reloadWorkspace(-1);
        }
    }
    
    public void handleHelpRequest() {
        Stage helpStage = new Stage();
        FlowPane helpPane = new FlowPane();
        helpPane.setPadding(new Insets(100, 100, 100, 90));
        helpPane.getChildren().add(new Text("Mouse over a button for cues.\n\n\n\t  For more help, visit https://the-jclass-designer.firebaseapp.com/"));
        Scene helpScene = new Scene(helpPane, 700, 300, Color.WHITE);
        helpStage.setScene(helpScene);
        helpStage.show();
    }

    public void handleInfoRequest() {
        Stage infoStage = new Stage();
        FlowPane infoPane = new FlowPane();
        infoPane.setPadding(new Insets(100, 100, 100, 90));
        infoPane.getChildren().add(new Text("jClass Designer (alpha)\n\n Developed by Richard McKenna and Saeid Abu\n\n Dedicated to our Moms ❤"));
        Scene infoScene = new Scene(infoPane, 620, 300, Color.WHITE);
        infoStage.setScene(infoScene);
        infoStage.show();
    }

    public void handleParentComboBoxUpdateRequest(String parentName) {
        Workspace workspace = (Workspace) app.getWorkspaceComponent();
        Pane pane = workspace.getLeftPane();
        if (index != -1 && pane.getChildren().get(index) instanceof Diagram){
            diagram = (Diagram)pane.getChildren().get(index);
            Diagram parent = findDiagram(parentName);
            // Parent doesn't exist in the workspace
            if(parent == null) {
                // Delete previous extension data and connector
                if(!diagram.getExtensionData().isEmpty()) {
                    // For Java only, where multiinheritence isn't allowed
                    removeDiagramDependencies(findDiagram(diagram.getExtensionData().get(0)));
                    diagram.getExtensionData().clear();
                }
                parent = finishAddingDiagram(diagram.getNameSection().getX() + 62.5, diagram.getNameSection().getY() - 180, parentName, diagram.getPackageName(), false);
                diagram.addExtensionData(parent.getDiagramId());
                finishAddingConnector(diagram, parent, 11);
            }
            // Parent exists in the workspace, and it's not the class itself
            else if(parent != null && diagram.getDiagramId() != parent.getDiagramId()) {
                // Parent is class and it doesn't exist in the extension data of the diagram
                if(!parent.isInterface() && !diagram.getExtensionData().contains(parent.getDiagramId())) {
                    // Delete previous extension data and connector
                    if(!diagram.getExtensionData().isEmpty()) {
                        // For Java only, where multiinheritence isn't allowed
                        removeDiagramDependencies(findDiagram(diagram.getExtensionData().get(0)));
                        diagram.getExtensionData().clear();
                    }
                    diagram.addExtensionData(parent.getDiagramId());
                    finishAddingConnector(diagram, parent, 11);
                }
                // Parent is an interface and it doesn't exist in the implementation data of the diagram
                else if(parent.isInterface() && !diagram.getImplementationData().contains(parent.getDiagramId())) {
                    diagram.addImplementationData(parent.getDiagramId());
                    finishAddingConnector(diagram, parent, 01);
                }
            }
            createVersion();
        }
    }
    
    // Find a diagram from its id
    private Diagram findDiagram(int diagramId) {
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
    private Diagram findDiagram(String diagramName) {
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
    private boolean isPrimitive(String className) {
        String[] primitiveClass = {"byte", "short", "int", "long", "float", "double", "char", "boolean", "String"};
        for(int i = 0; i < primitiveClass.length; i++) {
            if(className.contains(primitiveClass[i]))
                return true;
        }
        return false;
    }
    
    // Crete a java source code file
    private PrintWriter createJavaSourceCode(Diagram tempDiagram, String filePath, String name, boolean isComplete) {
        PrintWriter writer = null;
        try {
            File file = new File(filePath);
            file.getParentFile().mkdirs();
            writer = new PrintWriter(filePath, "UTF-8");
            writer.println((tempDiagram.getPackageName().isEmpty() ? "package default_package;\n\n" : "package default_package." + tempDiagram.getPackageName() + ";\n\n") + 
                    getImportString(tempDiagram) + "public " + (isComplete ? "class " : (tempDiagram.isAbstract() ? "abstract " : "") + (tempDiagram.isInterface() ? 
                    "interface " : "class ")) + name + getInheritanceString(tempDiagram) + " {" + (isComplete ? "\n\tpublic " + name + "(){}"+  "\n}" : ""));
        } catch (FileNotFoundException | UnsupportedEncodingException ex) {
            Logger.getLogger(PageEditController.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if(isComplete)
                writer.close();
        }
        return writer;
    }
    
    private String getInheritanceString(Diagram diagram) {
        String inheritanceString = "";
        boolean flag = false;
        // For extension data
        for(int i = 0; i < diagram.getExtensionData().size(); i++) {
            Diagram parent = findDiagram(diagram.getExtensionData().get(i));
            if(parent != null) {
                if(!flag) {
                    inheritanceString += " extends " + parent.getNameText().getText();
                    flag = true;
                }
                else 
                    inheritanceString += ", " + parent.getNameText().getText();
            }
        }
        flag = false;
        // For implementation data
        for(int i = 0; i < diagram.getImplementationData().size(); i++) {
            Diagram parent = findDiagram(diagram.getImplementationData().get(i));
            if(parent != null) {
                if(!flag) {
                    inheritanceString += " implements " + parent.getNameText().getText();
                    flag = true;
                }
                else 
                    inheritanceString += ", " + parent.getNameText().getText();
            }
        }
        return inheritanceString;
    }
    
    private String getImportString(Diagram diagram) {
        String importString = "";
        // Import statements for extension data 
        for(int i = 0; i < diagram.getExtensionData().size(); i++) {
            Diagram parent = findDiagram(diagram.getExtensionData().get(i));
            if(parent != null) {
                if(parent.getPackageName().isEmpty())
                    importString += "import default_package." + parent.getNameText().getText() + ";\n";
                else
                    importString += "import " + parent.getPackageName() + ";\n";
            }
        }
        // Import statements for implementation data 
        for(int i = 0; i < diagram.getImplementationData().size(); i++) {
            Diagram parent = findDiagram(diagram.getImplementationData().get(i));
            if(parent != null) {
                if(parent.getPackageName().isEmpty())
                    importString += "import default_package." + parent.getNameText().getText() + ";\n";
                else
                    importString += "import " + parent.getPackageName() + ";\n";
            }
        }
        // Import statements for aggregation data 
        for(int i = 0; i < diagram.getAggregationData().size(); i++) {
            Diagram aggregatedDiagram = findDiagram(diagram.getAggregationData().get(i));
            if(aggregatedDiagram != null) {
                if(aggregatedDiagram.getPackageName().isEmpty())
                    importString += "import default_package." + aggregatedDiagram.getNameText().getText() + ";\n";
                else
                    importString += "import " + aggregatedDiagram.getPackageName() + ";\n";
            }
        }
        // Import statements for relationship data 
        for(int i = 0; i < diagram.getRelationshipData().size(); i++) {
            Diagram relatedDiagram = findDiagram(diagram.getRelationshipData().get(i));
            if(relatedDiagram != null) {
                if(relatedDiagram.getPackageName().isEmpty())
                    importString += "import default_package." + relatedDiagram.getNameText().getText() + ";\n";
                else
                    importString += "import " + relatedDiagram.getPackageName() + ";\n";
            }
        }
        importString += "\n";
        return importString;
    }
    
    // Handle variable table updates
    public void handleVariableTableUpdateRequest(TableColumn.CellEditEvent<Variable, String> tableColumn, int option) {
        Workspace workspace = (Workspace) app.getWorkspaceComponent();
        Pane pane = workspace.getLeftPane();
        if (index != -1 && pane.getChildren().get(index) instanceof Diagram){
            switch(option) {
                // Variable name is updated
                case 1:
                    ((Variable)tableColumn.getTableView().getItems().get(tableColumn.getTablePosition().getRow())).setVariableName(tableColumn.getNewValue());
                    break;
                // Variable type is updated
                case 2:
                    ((Variable)tableColumn.getTableView().getItems().get(tableColumn.getTablePosition().getRow())).setTypeName(tableColumn.getNewValue());
                    Diagram tempDiagram = findDiagram(tableColumn.getNewValue());
                    if(tempDiagram == null && !isPrimitive(tableColumn.getNewValue())) {
                        tempDiagram = finishAddingDiagram(diagram.getNameSection().getX() - 280, diagram.getNameSection().getY(), tableColumn.getNewValue(), "", false);
                        finishAddingConnector(tempDiagram, diagram, 12);
                        diagram.addAggregationData(tempDiagram.getDiagramId());
                    }
                    break;
                // Variable staticity is updated
                case 3:
                    ((Variable)tableColumn.getTableView().getItems().get(tableColumn.getTablePosition().getRow())).setStatic(tableColumn.getNewValue());
                    break;
                // Variable access type is updated
                case 4:
                    ((Variable)tableColumn.getTableView().getItems().get(tableColumn.getTablePosition().getRow())).setAccessType(tableColumn.getNewValue());
                    break;
            }
            diagram = (Diagram)pane.getChildren().get(index);
            diagram.updateVariableText();
            createVersion();
            workspace.reloadWorkspace(index);
        }
    }
    
    // Handle method table updates
    public void handleMethodTableUpdateRequest(TableColumn.CellEditEvent<Method, String> tableColumn, int option) {
        Workspace workspace = (Workspace) app.getWorkspaceComponent();
        Pane pane = workspace.getLeftPane();
        if (index != -1 && pane.getChildren().get(index) instanceof Diagram){
            switch(option) {
                // Method name is updated
                case 1:
                    ((Method)tableColumn.getTableView().getItems().get(tableColumn.getTablePosition().getRow())).setMethodName(tableColumn.getNewValue());
                    break;
                // Method return type is updated
                case 2:
                    ((Method)tableColumn.getTableView().getItems().get(tableColumn.getTablePosition().getRow())).setReturnType(tableColumn.getNewValue());
                    Diagram tempDiagram = findDiagram(tableColumn.getNewValue());
                    if(tempDiagram == null && !isPrimitive(tableColumn.getNewValue()) && !tableColumn.getNewValue().equals("void")) {
                        tempDiagram = finishAddingDiagram(diagram.getNameSection().getX() - 280, diagram.getNameSection().getY(), tableColumn.getNewValue(), "", false);
                        finishAddingConnector(diagram, tempDiagram, 13);
                        diagram.addRelationshipData(tempDiagram.getDiagramId());
                    }
                    break;
                // Method staticity is updated
                case 3:
                    ((Method)tableColumn.getTableView().getItems().get(tableColumn.getTablePosition().getRow())).setStatic(tableColumn.getNewValue());
                    break;
                // Method abstractivity is updated
                case 4:
                    ((Method)tableColumn.getTableView().getItems().get(tableColumn.getTablePosition().getRow())).setAbstract(tableColumn.getNewValue());
                    diagram.setAbstract(true);
                    diagram.updateHeadingText();
                    diagram.dynamicPosition();
                    break;
                // Method access type is updated
                case 5:
                    ((Method)tableColumn.getTableView().getItems().get(tableColumn.getTablePosition().getRow())).setAccessType(tableColumn.getNewValue());
                    break;
                // Method argument 1 is updated
                case 6:
                    ((Method)tableColumn.getTableView().getItems().get(tableColumn.getTablePosition().getRow())).setArgumentOne(tableColumn.getNewValue());
                    tempDiagram = findDiagram(tableColumn.getNewValue().split(" ")[0]);
                    if(tempDiagram == null && !isPrimitive(tableColumn.getNewValue().split(" ")[0])) {
                        tempDiagram = finishAddingDiagram(diagram.getNameSection().getX() - 280, diagram.getNameSection().getY(), tableColumn.getNewValue().split(" ")[0], diagram.getPackageName(), false);
                        finishAddingConnector(diagram, tempDiagram, 13);
                        diagram.addRelationshipData(tempDiagram.getDiagramId());
                    }
                    break;
                // Method argument 2 is updated
                case 7:
                    ((Method)tableColumn.getTableView().getItems().get(tableColumn.getTablePosition().getRow())).setArgumentTwo(tableColumn.getNewValue());
                    tempDiagram = findDiagram(tableColumn.getNewValue().split(" ")[0]);
                    if(tempDiagram == null && !isPrimitive(tableColumn.getNewValue().split(" ")[0])) {
                        tempDiagram = finishAddingDiagram(diagram.getNameSection().getX() - 280, diagram.getNameSection().getY(), tableColumn.getNewValue().split(" ")[0], diagram.getPackageName(), false);
                        finishAddingConnector(diagram, tempDiagram, 13);
                        diagram.addRelationshipData(tempDiagram.getDiagramId());
                    }
                    break;
                // Method argument 3 is updated
                case 8:
                    ((Method)tableColumn.getTableView().getItems().get(tableColumn.getTablePosition().getRow())).setArgumentThree(tableColumn.getNewValue());
                    tempDiagram = findDiagram(tableColumn.getNewValue().split(" ")[0]);
                    if(tempDiagram == null && !isPrimitive(tableColumn.getNewValue().split(" ")[0])) {
                        tempDiagram = finishAddingDiagram(diagram.getNameSection().getX() - 280, diagram.getNameSection().getY(), tableColumn.getNewValue().split(" ")[0], diagram.getPackageName(), false);
                        finishAddingConnector(diagram, tempDiagram, 13);
                        diagram.addRelationshipData(tempDiagram.getDiagramId());
                    }
                    break;
            }
            diagram = (Diagram)pane.getChildren().get(index);
            diagram.updateMethodText();
            createVersion();
            workspace.reloadWorkspace(index);
        }
    }
    
    // Remove dependencies of a diagram when it is removed
    private void removeDiagramDependencies(Diagram diagram) {
        Workspace workspace = (Workspace) app.getWorkspaceComponent();
        Pane pane = workspace.getLeftPane();
        for(int i = 0; i < diagram.getConnectorData().size(); i++) {
            for(int j = 0; j < pane.getChildren().size(); j++) {
                if(pane.getChildren().get(j) instanceof Connector) {
                    Connector tempConnector = (Connector)pane.getChildren().get(j);
                    if(tempConnector.getConnectorId() == diagram.getConnectorData().get(i)) {
                        removeConnectorDependencies(tempConnector);
                        pane.getChildren().remove(j);
                        break;
                    }
                }
            }
        }
    }
    // Remove dependencies of a connector when it is removed
    private void removeConnectorDependencies(Connector connector) {
        Workspace workspace = (Workspace) app.getWorkspaceComponent();
        Pane pane = workspace.getLeftPane();
        for(int i = 0; i < pane.getChildren().size(); i++) {
            if(pane.getChildren().get(i) instanceof Diagram) {
                Diagram tempDiagram = (Diagram)pane.getChildren().get(i);
                if(tempDiagram.getDiagramId() == connector.getSourceId() || tempDiagram.getDiagramId() == connector.getDestinationId()) {
                    if((connector.getType() % 100) % 10 == 1) {
                        if (!tempDiagram.removeExtensionData(connector.getDestinationId())) {
                            tempDiagram.removeImplementationData(connector.getDestinationId());
                        }
                    }
                    else if((connector.getType() % 100) % 10 == 2)
                        tempDiagram.removeAggregationData(connector.getDestinationId());
                    else if((connector.getType() % 100) % 10 == 3)
                        tempDiagram.removeRelationshipData(connector.getDestinationId());
                    tempDiagram.removeConnectorData(connector.getConnectorId());
                }
            }
        }
    }
    
    private Diagram finishAddingDiagram(double x, double y, String name, String packageName, boolean isInterface) {
        Workspace workspace = (Workspace) app.getWorkspaceComponent();
        Pane pane = workspace.getLeftPane();
        Diagram newDiagram = new Diagram(-1, x, y, name, packageName, false, isInterface, false, false, FXCollections.observableArrayList(),
                FXCollections.observableArrayList(), new ArrayList(), new ArrayList(), new ArrayList(), new ArrayList(), new ArrayList());
        if(snapEnabled)
                newDiagram.setPosition((int)(newDiagram.getNameSection().getX()/10)*10, (int)(newDiagram.getNameSection().getY()/10)*10);
        pane.getChildren().add(newDiagram);
        createVersion();
        return newDiagram;
    }
    
    // Create a connector by calculating the best path between source and diagram
    private Connector finishAddingConnector(Diagram source, Diagram destination, int type) {
        Workspace workspace = (Workspace) app.getWorkspaceComponent();
        Pane pane = workspace.getLeftPane();
        //
        int[] connectorData = new int[3];
        // Destination is straight top
        if(Math.abs(source.getConnectionX(1) - destination.getConnectionX(1)) < 10 && source.getConnectionY(1) > destination.getConnectionY(1)) {
            connectorData[0] = 1;
            connectorData[1] = 1;
            connectorData[2] = 3;
        }
        // Destination is top right
        else if(source.getConnectionX(1) < destination.getConnectionX(1) && source.getConnectionY(1) > destination.getConnectionY(1)) {
            connectorData[0] = 2;
            connectorData[1] = 2;
            connectorData[2] = 4;
        }
        // Destination is straight right
        else if(source.getConnectionX(1) < destination.getConnectionX(1) && Math.abs(source.getConnectionY(1) - destination.getConnectionY(1)) < 10) {
            connectorData[0] = 3;
            connectorData[1] = 2;
            connectorData[2] = 4;
        }
        // Destination is bottom right
        else if(source.getConnectionX(1) < destination.getConnectionX(1) && source.getConnectionY(1) < destination.getConnectionY(1)) {
            connectorData[0] = 4;
            connectorData[1] = 2;
            connectorData[2] = 4;
        }
        // Destination is straight bottom
        else if(Math.abs(source.getConnectionX(1) - destination.getConnectionX(1)) < 10 && source.getConnectionY(1) < destination.getConnectionY(1)) {
            connectorData[0] = 5;
            connectorData[1] = 3;
            connectorData[2] = 1;
        }
        // Destination is left bottom
        else if(source.getConnectionX(1) > destination.getConnectionX(1) && source.getConnectionY(1) < destination.getConnectionY(1)) {
            connectorData[0] = 6;
            connectorData[1] = 4;
            connectorData[2] = 2;
        }
        // Destination is straight left
        else if(source.getConnectionX(1) > destination.getConnectionX(1) && Math.abs(source.getConnectionY(1) - destination.getConnectionY(1)) < 10) {
            connectorData[0] = 7;
            connectorData[1] = 4;
            connectorData[2] = 2;
        }
        // Destination is top left
        else if(source.getConnectionX(1) > destination.getConnectionX(1) && source.getConnectionY(1) > destination.getConnectionY(1)) {
            connectorData[0] = 8;
            connectorData[1] = 4;
            connectorData[2] = 2;
        }
        Connector newConnector =  new Connector(-1, connectorData[0] * 100 + type, source.getDiagramId(), destination.getDiagramId(), source.getConnectionX(connectorData[1]),
                source.getConnectionY(connectorData[1]), destination.getConnectionX(connectorData[2]), destination.getConnectionY(connectorData[2]), -1, -1, new ArrayList());
        
        source.getConnectorData().add(newConnector.getConnectorId());
        destination.getConnectorData().add(newConnector.getConnectorId());
        pane.getChildren().add(newConnector);
        return newConnector;
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
    private void setConnectorLayout(double deltaX, double deltaY){
        Workspace workspace = (Workspace) app.getWorkspaceComponent();
        Pane pane = workspace.getLeftPane();
        for(int i = 0; i < diagram.getConnectorData().size(); i++) {
           for(int j = 0; j < pane.getChildren().size(); j++) {
               if(pane.getChildren().get(j) instanceof Connector) {
                    connector = (Connector)pane.getChildren().get(j);
                    if(diagram.getConnectorData().get(i) == connector.getConnectorId())
                        connector.setLayout(deltaX, deltaY, diagram.getDiagramId());
               }
           } 
        }
    }
    
    private void setConnectorPosition(){
        Workspace workspace = (Workspace) app.getWorkspaceComponent();
        Pane pane = workspace.getLeftPane();
        for(int i = 0; i < diagram.getConnectorData().size(); i++) {
           for(int j = 0; j < pane.getChildren().size(); j++) {
               if(pane.getChildren().get(j) instanceof Connector) {
                    connector = (Connector)pane.getChildren().get(j);
                    if(diagram.getConnectorData().get(i) == connector.getConnectorId())
                        connector.setPosition(diagram.getDiagramId());
               }
           } 
        }
    }
    
    public void createVersion() {
        Workspace workspace = (Workspace) app.getWorkspaceComponent();
        Pane pane = workspace.getLeftPane();
        // Clear the obsolete entries
        if(undone) {
            for(int i = version; i < versionData.size();)
                versionData.remove(i);
            undone = false;
        }
        versionData.add(copyPaneData(pane));
        version++;
    }
    
    private ArrayList<Node> copyPaneData(Pane pane) {
        ArrayList<Node> version = new ArrayList();
        for(Node node: pane.getChildren()) {
            if(node instanceof Diagram) {
                Diagram tempDiagram = (Diagram)node;
                version.add(new Diagram(tempDiagram));
            }
            else if(node instanceof Connector) {
                Connector tempConnector = (Connector)node;
                version.add(new Connector(tempConnector));
            }
        }
        return version;
    }
}
