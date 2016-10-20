package jcd.gui;

import java.io.IOException;
import javafx.geometry.Insets;
import static javafx.geometry.Orientation.VERTICAL;
import javafx.geometry.Rectangle2D;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Separator;
import javafx.scene.control.Slider;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.util.Callback;
import jcd.Diagram;
import jcd.Method;
import jcd.MethodCellCheckBox;
import jcd.MethodCellComboBox;
import jcd.MethodCellText;
import jcd.Variable;
import jcd.VariableCellComboBox;
import jcd.VariableCellText;
import jcd.VariableCellCheckBox;
import jcd.ZoomingPane;
import jcd.controller.PageEditController;
import jcd.data.DataManager;
import jcd.file.FileManager;
import jcd.jClassDesigner;
import properties_manager.PropertiesManager;
import paf.ui.AppGUI;
import paf.AppTemplate;
import paf.components.AppWorkspaceComponent;
import static paf.settings.AppPropertyType.*;
import paf.ui.AppMessageDialogSingleton;
import paf.ui.AppYesNoCancelDialogSingleton;

/**
 * This class serves as the workspace component for this application, providing
 * the user interface controls for editing work.
 *
 * @author Richard McKenna
 * @author Saeid
 * @version 1.0
 */
public class Workspace extends AppWorkspaceComponent {
    
    // THESE CONSTANTS ARE FOR TYING THE PRESENTATION STYLE OF
    // THIS Workspace'S COMPONENTS TO A STYLE SHEET THAT IT USES
    

    // HERE'S THE APP
    AppTemplate app;

    // IT KNOWS THE GUI IT IS PLACED INSIDE
    AppGUI gui;
    
    // THIS HANDLES INTERACTIONS WITH PAGE EDITING CONTROLS
    PageEditController pageEditController;
    
    // WE'LL PUT THE WORKSPACE INSIDE A SPLIT PANE
    SplitPane workspaceSplitPane;
    
    // The pane for editing options
    Pane leftPane;
    ZoomingPane zoomingPane;
    ScrollPane leftScrollPane;
    VBox rightPane;
    GridPane infoGridPaneOne;
    GridPane infoGridPaneTwo;
    
    
    // THESE ARE HEADINGS
    Label nameLabel;
    Label packageLabel;
    Label parentLabel;
    Label variablesLabel;
    Label methodsLabel;
    
    
    TextField nameTextField;
    TextField packageTextField;
    ComboBox parentComboBox;
    
    Button saveAsCodeButton;
    Button addClassButton;
    Button selectButton;
    Button resizeButton;
    Button addInterfaceButton;
    Button addVariableButton;
    Button removeVariableButton;
    Button addMethodButton;
    Button removeMethodButton;
    Button saveAsPhotoButton;
    Button exitButton;
    Button removeDiagramButton;
    Button undoButton;
    Button redoButton;
    Slider zoomSlider;
    CheckBox gridCheckBox;
    CheckBox snapCheckBox;  
    Button helpButton;
    Button infoButton;

    TableView<Variable> variableTableView;
    TableColumn<Variable, String> variableNameColumn;
    TableColumn<Variable, String> variableTypeColumn;
    TableColumn<Variable, String> variableIsStaticColumn;
    TableColumn<Variable, String> variableAccessColumn;
    
    TableView<Method> methodTableView;
    TableColumn<Method, String> methodNameColumn;
    TableColumn<Method, String> methodReturnTypeColumn;
    TableColumn<Method, String> methodIsStaticColumn;
    TableColumn<Method, String> methodIsAbstractColumn;
    TableColumn<Method, String> methodAccessTypeColumn;
    TableColumn<Method, String> methodArgOneColumn;
    TableColumn<Method, String> methodArgTwoColumn;
    TableColumn<Method, String> methodArgThreeColumn;
    
    
    // HERE ARE OUR DIALOGS
    AppMessageDialogSingleton messageDialog;
    AppYesNoCancelDialogSingleton yesNoCancelDialog;

    /**
     * Constructor for initializing the workspace, note that this constructor
     * will fully setup the workspace user interface for use.
     *
     * @param initApp The application this workspace is part of.
     *
     * @throws IOException Thrown should there be an error loading application
     * data for setting up the user interface.
     */
    public Workspace(AppTemplate initApp) throws IOException {
        // KEEP THIS FOR LATER
	app = initApp;

	// KEEP THE GUI FOR LATER
	gui = app.getGUI();
        Stage primaryStage = gui.getWindow();
        SplashScreen splashStage = new SplashScreen();
        splashStage.start(primaryStage, "./images/SplashScreen.png");

        // THIS WILL PROVIDE US WITH OUR CUSTOM UI SETTINGS AND TEXT
	PropertiesManager propsSingleton = PropertiesManager.getPropertiesManager();
        
        // LOAD ALL THE HTML TAG TYPES
	FileManager fileManager = (FileManager) app.getFileComponent();
	DataManager dataManager = (DataManager) app.getDataComponent();
        
        // GET THE SCREEN SIZE
        Screen screen = Screen.getPrimary();
        Rectangle2D bounds = screen.getVisualBounds();
        
        
        // WE'LL PUT THE WORKSPACE INSIDE A SPLIT PANE
        workspaceSplitPane = new SplitPane();
        
        // WE'LL ORGANIZE OUR WORKSPACE COMPONENTS USING A BORDER PANE
	workspace = new Pane();
        
        // THESE ARE THE MAIN TWO PANES OF THE APPLICATION
        leftPane = new Pane();
        leftPane.setMinSize(bounds.getWidth() - 376, bounds.getHeight());
        zoomingPane = new ZoomingPane(leftPane);
        leftScrollPane = new ScrollPane(zoomingPane);
        leftScrollPane.setMinSize(bounds.getWidth() - 376, bounds.getHeight() - 78);
        leftScrollPane.setMaxSize(bounds.getWidth() - 376, bounds.getHeight() - 78);
        rightPane = new VBox(20);
        rightPane.setPadding(new Insets(8, 12, 8, 12));
        rightPane.setMaxWidth(366);
        
        // THIS WILL MANAGE ALL EDITING EVENTS
	pageEditController = new PageEditController((jClassDesigner) app);
        
        saveAsPhotoButton = gui.initChildButton(gui.getToolbarPane(), SAVE_AS_PHOTO_ICON.toString(), SAVE_AS_PHOTO_TOOLTIP.toString(), false);
        saveAsCodeButton = gui.initChildButton(gui.getToolbarPane(), SAVE_AS_CODE_ICON.toString(), SAVE_AS_CODE_TOOLTIP.toString(), false);
        exitButton = gui.initChildButton(gui.getToolbarPane(), EXIT_ICON.toString(), EXIT_TOOLTIP.toString(), false);
        gui.getToolbarPane().getChildren().add(new Separator(VERTICAL));
        selectButton = gui.initChildButton(gui.getToolbarPane(), SELECT_ICON.toString(), SELECT_TOOLTIP.toString(), false);
        resizeButton = gui.initChildButton(gui.getToolbarPane(), RESIZE_ICON.toString(), RESIZE_TOOLTIP.toString(), false);
        addClassButton = gui.initChildButton(gui.getToolbarPane(), ADD_CLASS_ICON.toString(), ADD_CLASS_TOOLTIP.toString(), false);
        addInterfaceButton = gui.initChildButton(gui.getToolbarPane(), ADD_INTERFACE_ICON.toString(), ADD_INTERFACE_TOOLTIP.toString(), false);
        removeDiagramButton = gui.initChildButton(gui.getToolbarPane(), REMOVE_ICON.toString(), REMOVE_DIAGRAM_TOOLTIP.toString(), false);
        undoButton = gui.initChildButton(gui.getToolbarPane(), UNDO_ICON.toString(), UNDO_TOOLTIP.toString(), false);
        redoButton = gui.initChildButton(gui.getToolbarPane(), REDO_ICON.toString(), REDO_TOOLTIP.toString(), false);
        gui.getToolbarPane().getChildren().add(new Separator(VERTICAL));
        zoomSlider = new Slider(0.5,2,1);
        zoomingPane.getZoomFactorProperty().bind(zoomSlider.valueProperty());
        //leftPane.prefHeightProperty().bind(zoomSlider.valueProperty());
        //leftPane.prefWidthProperty().bind(zoomSlider.valueProperty());
        gridCheckBox = new CheckBox("Grid");
        snapCheckBox = new CheckBox("Snap");
        gui.getToolbarPane().getChildren().addAll(zoomSlider, gridCheckBox, snapCheckBox, new Separator(VERTICAL));
        helpButton = gui.initChildButton(gui.getToolbarPane(), HELP_ICON.toString(), HELP_TOOLTIP.toString(), false);
        infoButton = gui.initChildButton(gui.getToolbarPane(), INFO_ICON.toString(), INFO_TOOLTIP.toString(), false);
        infoGridPaneOne = new GridPane();
        infoGridPaneOne.setVgap(20);
        infoGridPaneTwo = new GridPane();
        infoGridPaneTwo.setVgap(20);
        
        //FINALIZE THE HEADINGS
        nameLabel = new Label ("Name: ");
        packageLabel = new Label ("Package: ");
        parentLabel = new Label ("Parent:");
        variablesLabel = new Label ("Variables:         ");
        methodsLabel = new Label ("Methods:         ");
        
        nameTextField = new TextField();
        nameTextField.setPromptText("Class/Interface Name");
        packageTextField = new TextField();
        packageTextField.setPromptText("Package Name");
        parentComboBox = new ComboBox();
        parentComboBox.setPromptText("Parent Name");
        parentComboBox.setEditable(true);
        
        variableTableView = new TableView();
        variableTableView.setEditable(true);
        
        variableNameColumn = new TableColumn("Name");
        Callback<TableColumn<Variable, String>, TableCell<Variable, String>> variableNameCellFactory = (TableColumn<Variable, String> param) -> new VariableCellText();
        variableNameColumn.setCellValueFactory(cellData -> cellData.getValue().getVariableNameProperty());
        variableNameColumn.setCellFactory(variableNameCellFactory);
        
        variableTypeColumn = new TableColumn("Type");
        Callback<TableColumn<Variable, String>, TableCell<Variable, String>> variableTypeCellFactory = (TableColumn<Variable, String> param) -> new VariableCellText();
        variableTypeColumn.setCellValueFactory(cellData -> cellData.getValue().getVariableTypeProperty());
        variableTypeColumn.setCellFactory(variableTypeCellFactory);
        
        variableIsStaticColumn = new TableColumn("Static");
        Callback<TableColumn<Variable, String>, TableCell<Variable, String>> variableIsStaticCellFactory = (TableColumn<Variable, String> param) -> new VariableCellCheckBox();
        variableIsStaticColumn.setCellValueFactory(cellData -> cellData.getValue().isStaticProperty());
        variableIsStaticColumn.setCellFactory(variableIsStaticCellFactory);
        
        variableAccessColumn = new TableColumn("Access");
        Callback<TableColumn<Variable, String>, TableCell<Variable, String>> variableAccessCellFactory = (TableColumn<Variable, String> param) -> new VariableCellComboBox();
        variableAccessColumn.setCellValueFactory(cellData -> cellData.getValue().getAccessTypeProperty());
        variableAccessColumn.setCellFactory(variableAccessCellFactory);
        
        variableTableView.getColumns().addAll(variableNameColumn, variableTypeColumn, variableIsStaticColumn, variableAccessColumn);
        
        methodTableView = new TableView();
        methodTableView.setEditable(true);
        
        methodNameColumn = new TableColumn("Name");
        Callback<TableColumn<Method, String>, TableCell<Method, String>> methodNameCellFactory = (TableColumn<Method, String> param) -> new MethodCellText();
        methodNameColumn.setCellValueFactory(cellData -> cellData.getValue().getMethodNameProperty());
        methodNameColumn.setCellFactory(methodNameCellFactory);
        
        methodReturnTypeColumn = new TableColumn("Return");
        Callback<TableColumn<Method, String>, TableCell<Method, String>> methodRetrunTypeCellFactory = (TableColumn<Method, String> param) -> new MethodCellText();
        methodReturnTypeColumn.setCellValueFactory(cellData -> cellData.getValue().getReturnTypeProperty());
        methodReturnTypeColumn.setCellFactory(methodRetrunTypeCellFactory);
        
        methodIsStaticColumn = new TableColumn("Static");
        Callback<TableColumn<Method, String>, TableCell<Method, String>> methodIsStaticCellFactory = (TableColumn<Method, String> param) -> new MethodCellCheckBox();
        methodIsStaticColumn.setCellValueFactory(cellData -> cellData.getValue().isStaticProperty());
        methodIsStaticColumn.setCellFactory(methodIsStaticCellFactory);
        
        methodIsAbstractColumn = new TableColumn("Abstract");
        Callback<TableColumn<Method, String>, TableCell<Method, String>> methodIsAbstractCellFactory = (TableColumn<Method, String> param) -> new MethodCellCheckBox();
        methodIsAbstractColumn.setCellValueFactory(cellData -> cellData.getValue().isAbstractProperty());
        methodIsAbstractColumn.setCellFactory(methodIsAbstractCellFactory);
        
        methodAccessTypeColumn = new TableColumn("Access");
        Callback<TableColumn<Method, String>, TableCell<Method, String>> methodAccessTypeCellFactory = (TableColumn<Method, String> param) -> new MethodCellComboBox();
        methodAccessTypeColumn.setCellValueFactory(cellData -> cellData.getValue().getAccessTypeProperty());
        methodAccessTypeColumn.setCellFactory(methodAccessTypeCellFactory);
        
        methodArgOneColumn = new TableColumn("Arg 1");
        Callback<TableColumn<Method, String>, TableCell<Method, String>> methodArgOneCellFactory = (TableColumn<Method, String> param) -> new MethodCellText();
        methodArgOneColumn.setCellValueFactory(cellData -> cellData.getValue().getArgumentOneProperty());
        methodArgOneColumn.setCellFactory(methodArgOneCellFactory);
        
        methodArgTwoColumn = new TableColumn("Arg 2");
        Callback<TableColumn<Method, String>, TableCell<Method, String>> methodArgTwoCellFactory = (TableColumn<Method, String> param) -> new MethodCellText();
        methodArgTwoColumn.setCellValueFactory(cellData -> cellData.getValue().getArgumentTwoProperty());
        methodArgTwoColumn.setCellFactory(methodArgTwoCellFactory);
        
        methodArgThreeColumn = new TableColumn("Arg 3");
        Callback<TableColumn<Method, String>, TableCell<Method, String>> methodArgThreeCellFactory = (TableColumn<Method, String> param) -> new MethodCellText();
        methodArgThreeColumn.setCellValueFactory(cellData -> cellData.getValue().getArgumentThreeProperty());
        methodArgThreeColumn.setCellFactory(methodArgThreeCellFactory);
        
        
        methodTableView.getColumns().addAll(methodNameColumn, methodReturnTypeColumn, methodIsStaticColumn, methodIsAbstractColumn, 
                methodAccessTypeColumn, methodArgOneColumn, methodArgTwoColumn, methodArgThreeColumn);
        
        infoGridPaneOne.add(nameLabel, 0, 0);
        infoGridPaneOne.add(nameTextField, 1, 0);
        infoGridPaneOne.add(packageLabel, 0, 1);
        infoGridPaneOne.add(packageTextField, 1, 1);
        infoGridPaneOne.add(parentLabel, 0, 2);
        infoGridPaneOne.add(parentComboBox, 1, 2);
        infoGridPaneOne.add(variablesLabel, 0, 3);
        HBox tempHBoxOne = new HBox(15);
        addVariableButton = gui.initChildButton(tempHBoxOne, PLUS_ICON.toString(), ADD_VARIABLE_TOOLTIP.toString(), false);
        removeVariableButton = gui.initChildButton(tempHBoxOne, MINUS_ICON.toString(), REMOVE_VARIABLE_TOOLTIP.toString(), false);
        
        infoGridPaneOne.add(tempHBoxOne, 1, 3);
        ScrollPane tempScrollPaneOne = new ScrollPane(variableTableView);
        tempScrollPaneOne.setMaxSize(360, 210);
        
        rightPane.getChildren().addAll(infoGridPaneOne, tempScrollPaneOne);
        
        HBox tempHBoxTwo = new HBox(15);
        infoGridPaneTwo.add(methodsLabel, 0, 0);
        addMethodButton = gui.initChildButton(tempHBoxTwo, PLUS_ICON.toString(), ADD_METHOD_TOOLTIP.toString(), false);
        removeMethodButton = gui.initChildButton(tempHBoxTwo, MINUS_ICON.toString(), REMOVE_METHOD_TOOLTIP.toString(), false);
        infoGridPaneTwo.add(tempHBoxTwo, 1, 0);
        ScrollPane tempScrollPaneTwo = new ScrollPane(methodTableView);
        tempScrollPaneTwo.setMaxSize(360, 210);
        
        rightPane.getChildren().addAll(infoGridPaneTwo, tempScrollPaneTwo);
                
        saveAsPhotoButton.setOnAction(e -> {
            pageEditController.handleSaveAsPhotoRequest();
        });
        saveAsCodeButton.setOnAction(e -> {
            pageEditController.handleSaveAsCodeRequest();
        });
        exitButton.setOnAction(e -> {
            gui.getFileController().handleExitRequest();
        });
        selectButton.setOnAction(e -> {
            pageEditController.handleSelectRequest();
        });
        resizeButton.setOnAction(e -> {
            pageEditController.handleResizeRequest();
        });
        addClassButton.setOnAction(e -> {
            pageEditController.handleAddClassRequest();
            gui.getFileController().markAsEdited(gui);
        });
        addInterfaceButton.setOnAction(e -> {
            pageEditController.handleAddInterfaceRequest();
        });
        removeDiagramButton.setOnAction(e -> {
            pageEditController.handleRemoveRequest();
        });
        undoButton.setOnAction(e -> {
            pageEditController.handleUndoRequest();
        });
        redoButton.setOnAction(e -> {
            pageEditController.handleRedoRequest();
        });
        helpButton.setOnAction(e -> {
            pageEditController.handleHelpRequest();
        });
        infoButton.setOnAction(e -> {
            pageEditController.handleInfoRequest();
        });
        gridCheckBox.setOnAction(e -> {
            pageEditController.handleGridRequest(gridCheckBox.isSelected(), leftPane.getWidth(), leftPane.getHeight());
        });
        snapCheckBox.setOnAction(e -> {
            if(pageEditController.getSnapEnabled())
                pageEditController.handleGridRequest(false);
            else
                pageEditController.handleGridRequest(true);
        });
        nameTextField.setOnKeyReleased(e -> {
            pageEditController.handleNameUpdateRequest(nameTextField.getText());
        });
        packageTextField.setOnKeyReleased(e -> {
            pageEditController.handlePackageNameUpdateRequest(packageTextField.getText());
        });
        parentComboBox.valueProperty().addListener(e -> {
            if(parentComboBox.getValue() != null && !parentComboBox.getValue().equals(""))
                pageEditController.handleParentComboBoxUpdateRequest(parentComboBox.getValue().toString());
        });
        addVariableButton.setOnAction(e -> {
            pageEditController.handleAddVariablesRequest();
        });
        variableNameColumn.setOnEditCommit((TableColumn.CellEditEvent<Variable, String> tableColumn) -> {
            if(!tableColumn.getNewValue().equals(""))
                pageEditController.handleVariableTableUpdateRequest(tableColumn, 1);
        });
        variableTypeColumn.setOnEditCommit((TableColumn.CellEditEvent<Variable, String> tableColumn) -> {
            if(!tableColumn.getNewValue().equals(""))
                pageEditController.handleVariableTableUpdateRequest(tableColumn, 2);
        });
        variableIsStaticColumn.setOnEditCommit((TableColumn.CellEditEvent<Variable, String> tableColumn) -> {
                pageEditController.handleVariableTableUpdateRequest(tableColumn, 3);
        });
        variableAccessColumn.setOnEditCommit((TableColumn.CellEditEvent<Variable, String> tableColumn) -> {
                pageEditController.handleVariableTableUpdateRequest(tableColumn, 4);
        });
        removeVariableButton.setOnAction(e -> {
            pageEditController.handleRemoveVariablesRequest(variableTableView.getSelectionModel().getSelectedItem());
        });
        addMethodButton.setOnAction(e -> {
            pageEditController.handleAddMethodsRequest();
        });
        methodNameColumn.setOnEditCommit((TableColumn.CellEditEvent<Method, String> tableColumn) -> {
            if(!tableColumn.getNewValue().equals(""))
                pageEditController.handleMethodTableUpdateRequest(tableColumn, 1);
        });
        methodReturnTypeColumn.setOnEditCommit((TableColumn.CellEditEvent<Method, String> tableColumn) -> {
            if(!tableColumn.getNewValue().equals(""))
                pageEditController.handleMethodTableUpdateRequest(tableColumn, 2);
        });
        methodIsStaticColumn.setOnEditCommit((TableColumn.CellEditEvent<Method, String> tableColumn) -> {
                pageEditController.handleMethodTableUpdateRequest(tableColumn, 3);
        });
        methodIsAbstractColumn.setOnEditCommit((TableColumn.CellEditEvent<Method, String> tableColumn) -> {
                pageEditController.handleMethodTableUpdateRequest(tableColumn, 4);
        });
        methodAccessTypeColumn.setOnEditCommit((TableColumn.CellEditEvent<Method, String> tableColumn) -> {
                pageEditController.handleMethodTableUpdateRequest(tableColumn, 5);
        });
        methodArgOneColumn.setOnEditCommit((TableColumn.CellEditEvent<Method, String> tableColumn) -> {
            if(!tableColumn.getNewValue().equals(""))
                pageEditController.handleMethodTableUpdateRequest(tableColumn, 6);
        });
        methodArgTwoColumn.setOnEditCommit((TableColumn.CellEditEvent<Method, String> tableColumn) -> {
            if(!tableColumn.getNewValue().equals(""))
                pageEditController.handleMethodTableUpdateRequest(tableColumn, 7);
        });
        methodArgThreeColumn.setOnEditCommit((TableColumn.CellEditEvent<Method, String> tableColumn) -> {
            if(!tableColumn.getNewValue().equals(""))
                pageEditController.handleMethodTableUpdateRequest(tableColumn, 8);
        });
        removeMethodButton.setOnAction(e -> {
            pageEditController.handleRemoveMethodsRequest(methodTableView.getSelectionModel().getSelectedItem());
        });
        
        // NOTE THAT WE HAVE NOT PUT THE WORKSPACE INTO THE WINDOW,
	// THAT WILL BE DONE WHEN THE USER EITHER CREATES A NEW
	// COURSE OR LOADS AN EXISTING ONE FOR EDITING
	workspaceActivated = false;
        
        //SET THE RIGHT PANE OF DATA MANAGER
        dataManager.setLeftPane(leftPane);
        workspaceSplitPane.getItems().addAll(leftScrollPane, rightPane);
        workspace.getChildren().add(workspaceSplitPane);
        app.getGUI().updateToolbarControls("1100001.0000000");
        reloadWorkspace(-1);
        
    }
    
    public Pane getLeftPane() {
        return leftPane;
    }
    
    public ZoomingPane getZoomingPane() {
        return zoomingPane;
    }
    
    public TextField getNameTextField() {
        return nameTextField;
    }
    
    public Slider getZoomSlider() {
        return zoomSlider;
    }
    
    public TextField getPackageTextField() {
        return packageTextField;
    }
    
    public ComboBox getParentComboBox() {
        return parentComboBox;
    }
    
    public TableView<Variable> getVariableTableView() {
        return variableTableView;
    }
    
    public TableView<Method> getMethodTableView() {
        return methodTableView;
    }
 
    /**
     * This function specifies the CSS style classes for all the UI components
     * known at the time the workspace is initially constructed. Note that the
     * tag editor controls are added and removed dynamically as the application
     * runs so they will have their style setup separately.
     */
    @Override
    public void initStyle() {
        parentComboBox.getStyleClass().add(CLASS_COMBO_BOX);
        nameLabel.getStyleClass().add(CLASS_HEADING_TEXT);
        packageLabel.getStyleClass().add(CLASS_HEADING_TEXT);
        parentLabel.getStyleClass().add(CLASS_HEADING_TEXT);
        variablesLabel.getStyleClass().add(CLASS_HEADING_TEXT);
        methodsLabel.getStyleClass().add(CLASS_HEADING_TEXT);
        rightPane.getStyleClass().add(CLASS_BORDER_PANE);
        
    }

    /**
     * This function reloads all the controls for editing tag attributes into
     * the workspace.
     * @param index
     */
    @Override
    public void reloadWorkspace(int index) {
        // Update the parent combobox
        if(!leftPane.getChildren().isEmpty()) {
            parentComboBox.getItems().clear();
            for(int i = 0; i < leftPane.getChildren().size(); i++) {
                if (leftPane.getChildren().get(i) instanceof Diagram) {
                    Diagram diagram = (Diagram)leftPane.getChildren().get(i);
                    parentComboBox.getItems().add(diagram.getNameText().getText());
                }
            }
        }
        // Update variable and method tables
        if (index != -1 && leftPane.getChildren().get(index) instanceof Diagram){
            Diagram diagram = (Diagram)leftPane.getChildren().get(index);
            diagram.dynamicResize();
            variableTableView.setItems(diagram.getVariableData());
            methodTableView.setItems(diagram.getMethodData());
        }
        else {
            variableTableView.setItems(null);
            methodTableView.setItems(null);
        }
    }
}
