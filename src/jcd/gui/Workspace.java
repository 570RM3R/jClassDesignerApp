/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jcd.gui;

import java.io.IOException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import static javafx.geometry.Orientation.VERTICAL;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Separator;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import static javafx.scene.control.TableView.CONSTRAINED_RESIZE_POLICY;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import jcd.Diagram;
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
    Button addVariablesButton;
    Button removeVariablesButton;
    Button addMethodsButton;
    Button removeMethodsButton;
    Button saveAsPhotoButton;
    Button exitButton;
    Button removeDiagramButton;
    Button undoButton;
    Button redoButton;
    Button zoomInButton;
    Button zoomOutButton;
    CheckBox gridCheckBox;
    CheckBox snapCheckBox;
    Button helpButton;
    Button infoButton;
    
    TableView variablesTableView;
    TableView methodsTableView;
    
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
        
        // THIS WILL PROVIDE US WITH OUR CUSTOM UI SETTINGS AND TEXT
	PropertiesManager propsSingleton = PropertiesManager.getPropertiesManager();
        
        // LOAD ALL THE HTML TAG TYPES
	FileManager fileManager = (FileManager) app.getFileComponent();
	DataManager dataManager = (DataManager) app.getDataComponent();
        
        // WE'LL PUT THE WORKSPACE INSIDE A SPLIT PANE
        workspaceSplitPane = new SplitPane();
        
        // WE'LL ORGANIZE OUR WORKSPACE COMPONENTS USING A BORDER PANE
	workspace = new Pane();
        
        // THESE ARE THE MAIN TWO PANES OF THE APPLICATION
        leftPane = new Pane();
        leftPane.setMinSize(1050, 800);
        leftPane.setMaxSize(1050, 800);
        rightPane = new VBox(20);
        rightPane.setPadding(new Insets(8, 12, 8, 12));
        rightPane.setMaxWidth(370);
        rightPane.setMinHeight(800);
        
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
        zoomInButton = gui.initChildButton(gui.getToolbarPane(), ZOOM_IN_ICON.toString(), ZOOM_IN_TOOLTIP.toString(), false);
        zoomOutButton = gui.initChildButton(gui.getToolbarPane(), ZOOM_OUT_ICON.toString(), ZOOM_OUT_TOOLTIP.toString(), false);
        gridCheckBox = new CheckBox("Grid");
        snapCheckBox = new CheckBox("Snap");
        gui.getToolbarPane().getChildren().addAll(gridCheckBox, snapCheckBox, new Separator(VERTICAL));
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
        packageTextField = new TextField();
        parentComboBox = new ComboBox();
        
        variablesTableView = new TableView();
        variablesTableView.setEditable(true);
        variablesTableView.getColumns().addAll(new TableColumn("Name"), new TableColumn("Type"), new TableColumn("Static"), new TableColumn("Access"));
        variablesTableView.setColumnResizePolicy(CONSTRAINED_RESIZE_POLICY);
        
        methodsTableView = new TableView();
        methodsTableView.setEditable(true);
        methodsTableView.getColumns().addAll(new TableColumn("Name"), new TableColumn("Return"), new TableColumn("Static"), new TableColumn("Abstract"), 
                new TableColumn("Access"), new TableColumn("Arg 1"), new TableColumn("Arg 2"), new TableColumn("Arg 3"));
        methodsTableView.setColumnResizePolicy(CONSTRAINED_RESIZE_POLICY);
        
        infoGridPaneOne.add(nameLabel, 0, 0);
        infoGridPaneOne.add(nameTextField, 1, 0);
        infoGridPaneOne.add(packageLabel, 0, 1);
        infoGridPaneOne.add(packageTextField, 1, 1);
        infoGridPaneOne.add(parentLabel, 0, 2);
        infoGridPaneOne.add(parentComboBox, 1, 2);
        infoGridPaneOne.add(variablesLabel, 0, 3);
        HBox tempHBoxOne = new HBox(15);
        addVariablesButton = gui.initChildButton(tempHBoxOne, PLUS_ICON.toString(), ADD_VARIABLE_TOOLTIP.toString(), false);
        removeVariablesButton = gui.initChildButton(tempHBoxOne, MINUS_ICON.toString(), REMOVE_VARIABLE_TOOLTIP.toString(), false);
        
        infoGridPaneOne.add(tempHBoxOne, 1, 3);
        ScrollPane tempScrollPaneOne = new ScrollPane(variablesTableView);
        tempScrollPaneOne.setMaxSize(340, 210);
        
        rightPane.getChildren().addAll(infoGridPaneOne, tempScrollPaneOne);
        
        HBox tempHBoxTwo = new HBox(15);
        infoGridPaneTwo.add(methodsLabel, 0, 0);
        addMethodsButton = gui.initChildButton(tempHBoxTwo, PLUS_ICON.toString(), ADD_METHOD_TOOLTIP.toString(), false);
        removeMethodsButton = gui.initChildButton(tempHBoxTwo, MINUS_ICON.toString(), REMOVE_METHOD_TOOLTIP.toString(), false);
        infoGridPaneTwo.add(tempHBoxTwo, 1, 0);
        ScrollPane tempScrollPaneTwo = new ScrollPane(methodsTableView);
        tempScrollPaneTwo.setMaxSize(340, 210);
        
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
        });
        addInterfaceButton.setOnAction(e -> {
            pageEditController.handleAddInterfaceRequest();
        });
        removeDiagramButton.setOnAction(e -> {
            pageEditController.handleRemoveDiagramRequest();
        });
        undoButton.setOnAction(e -> {
            pageEditController.handleUndoRequest();
        });
        redoButton.setOnAction(e -> {
            pageEditController.handleRedoRequest();
        });
        zoomInButton.setOnAction(e -> {
            pageEditController.handleZoomInRequest();
        });
        zoomOutButton.setOnAction(e -> {
            pageEditController.handleZoomOutRequest();
        });
        helpButton.setOnAction(e -> {
            pageEditController.handleHelpRequest();
        });
        infoButton.setOnAction(e -> {
            pageEditController.handleInfoRequest();
        });
        nameTextField.setOnKeyReleased(e -> {
            pageEditController.handleNameUpdateRequest(nameTextField.getText());
        });
        packageTextField.setOnKeyReleased(e -> {
            pageEditController.handlePackageNameUpdateRequest(packageTextField.getText());
        });
        parentComboBox.setOnHiding(e -> {
            if(parentComboBox.getValue() != null)
                pageEditController.handleParentComboBoxUpdateRequest(parentComboBox.getValue().toString());
        });
        addVariablesButton.setOnAction(e -> {
            pageEditController.handleAddVariablesRequest();
        });
        removeVariablesButton.setOnAction(e -> {
            pageEditController.handleRemoveVariablesRequest();
        });
        addMethodsButton.setOnAction(e -> {
            pageEditController.handleAddMethodsRequest();
        });
        removeMethodsButton.setOnAction(e -> {
            pageEditController.handleRemoveMethodsRequest();
        });
        
        // NOTE THAT WE HAVE NOT PUT THE WORKSPACE INTO THE WINDOW,
	// THAT WILL BE DONE WHEN THE USER EITHER CREATES A NEW
	// COURSE OR LOADS AN EXISTING ONE FOR EDITING
	workspaceActivated = false;
        
        //SET THE RIGHT PANE OF DATA MANAGER
        dataManager.setLeftPane(leftPane);
        workspaceSplitPane.getItems().addAll(leftPane, rightPane);
        workspace.getChildren().add(workspaceSplitPane);
        reloadWorkspace();
        
    }
    
    public Pane getLeftPane() {
        return leftPane;
    }
    
    public TextField getNameTextField() {
        return nameTextField;
    }
    
    public TextField getPackageTextField() {
        return packageTextField;
    }
    
    public ComboBox getParentComboBox() {
        return parentComboBox;
    }
    
    public TableView getVariablesTableView() {
        return variablesTableView;
    }
    
    public TableView getMethodsTableView() {
        return methodsTableView;
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
        packageLabel.getStyleClass().add(CLASS_SUB_HEADING_TEXT);
        parentLabel.getStyleClass().add(CLASS_SUB_HEADING_TEXT);
        variablesLabel.getStyleClass().add(CLASS_SUB_HEADING_TEXT);
        methodsLabel.getStyleClass().add(CLASS_SUB_HEADING_TEXT);
        
    }

    /**
     * This function reloads all the controls for editing tag attributes into
     * the workspace.
     */
    @Override
    public void reloadWorkspace() {
        if(!leftPane.getChildren().isEmpty()) {
            parentComboBox.getItems().clear();
            for(int i = 0; i < leftPane.getChildren().size(); i++) {
                Diagram diagram = (Diagram)leftPane.getChildren().get(i);
                parentComboBox.getItems().add(diagram.getNameText().getText());
            }
        }
    }
}
