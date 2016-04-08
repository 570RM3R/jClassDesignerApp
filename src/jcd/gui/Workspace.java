/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jcd.gui;

import java.io.IOException;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import static javafx.scene.control.TableView.CONSTRAINED_RESIZE_POLICY;
import javafx.scene.control.TextField;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import jcd.controller.PageEditController;
import jcd.data.DataManager;
import jcd.file.FileManager;
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
    
    // The pane for editing options
    VBox rightPane;
    GridPane infoGridPaneOne;
    GridPane infoGridPaneTwo;
    
    
    // THESE ARE HEADINGS
    Label classNameLabel;
    Label packageLabel;
    Label parentLabel;
    Label variablesLabel;
    Label methodsLabel;
    
    
    TextField classNameTextField;
    TextField packageTextField;
    ComboBox parentComboBox;
    
    Button addVariablesButton;
    Button removeVariablesButton;
    Button addMethodsButton;
    Button removeMethodsButton;
    
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
        
        rightPane = gui.getRightPane();
        infoGridPaneOne = new GridPane();
        infoGridPaneOne.setVgap(20);
        infoGridPaneTwo = new GridPane();
        infoGridPaneTwo.setVgap(20);
        
        //FINALIZE THE HEADINGS
        classNameLabel = new Label ("Class Name: ");
        packageLabel = new Label ("Package: ");
        parentLabel = new Label ("Parent:");
        variablesLabel = new Label ("Variables:");
        methodsLabel = new Label ("Methods:         ");
        
        classNameTextField = new TextField();
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
        
        infoGridPaneOne.add(classNameLabel, 0, 0);
        infoGridPaneOne.add(classNameTextField, 1, 0);
        infoGridPaneOne.add(packageLabel, 0, 1);
        infoGridPaneOne.add(packageTextField, 1, 1);
        infoGridPaneOne.add(parentLabel, 0, 2);
        infoGridPaneOne.add(parentComboBox, 1, 2);
        infoGridPaneOne.add(variablesLabel, 0, 3);
        HBox tempHBoxOne = new HBox(15);
        addVariablesButton = gui.initChildButton(tempHBoxOne, PLUS_ICON.toString(), PLUS_TOOLTIP.toString(), true, false);
        removeVariablesButton = gui.initChildButton(tempHBoxOne, MINUS_ICON.toString(), MINUS_TOOLTIP.toString(), true, false);
        infoGridPaneOne.add(tempHBoxOne, 1, 3);
        ScrollPane tempScrollPaneOne = new ScrollPane(variablesTableView);
        tempScrollPaneOne.setMaxSize(340, 210);
        
        rightPane.getChildren().addAll(infoGridPaneOne, tempScrollPaneOne);
        
        HBox tempHBoxTwo = new HBox(15);
        infoGridPaneTwo.add(methodsLabel, 0, 0);
        addMethodsButton = gui.initChildButton(tempHBoxTwo, PLUS_ICON.toString(), PLUS_TOOLTIP.toString(), true, false);
        removeMethodsButton = gui.initChildButton(tempHBoxTwo, MINUS_ICON.toString(), MINUS_TOOLTIP.toString(), true, false);
        infoGridPaneTwo.add(tempHBoxTwo, 1, 0);
        ScrollPane tempScrollPaneTwo = new ScrollPane(methodsTableView);
        tempScrollPaneTwo.setMaxSize(340, 210);
        
        rightPane.getChildren().addAll(infoGridPaneTwo, tempScrollPaneTwo);
        
        // THIS WILL PROVIDE US WITH OUR CUSTOM UI SETTINGS AND TEXT
	PropertiesManager propsSingleton = PropertiesManager.getPropertiesManager();
        
        // LOAD ALL THE HTML TAG TYPES
	FileManager fileManager = (FileManager) app.getFileComponent();
	DataManager dataManager = (DataManager) app.getDataComponent();
        
        // NOTE THAT WE HAVE NOT PUT THE WORKSPACE INTO THE WINDOW,
	// THAT WILL BE DONE WHEN THE USER EITHER CREATES A NEW
	// COURSE OR LOADS AN EXISTING ONE FOR EDITING
	workspaceActivated = false;
        
        
        //SET THE RIGHT PANE OF DATA MANAGER
        dataManager.setRigthPane(rightPane);
        reloadWorkspace();
    }
    
    public Pane getRightPane() {
        return rightPane;
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
        classNameLabel.getStyleClass().add(CLASS_HEADING_TEXT);
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
       
    }
}
