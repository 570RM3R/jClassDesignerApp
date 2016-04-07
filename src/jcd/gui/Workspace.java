/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jcd.gui;

import java.io.IOException;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import jcd.controller.PageEditController;
import jcd.data.DataManager;
import jcd.file.FileManager;
import properties_manager.PropertiesManager;
import paf.ui.AppGUI;
import paf.AppTemplate;
import paf.components.AppWorkspaceComponent;
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
    HBox classPane;
    HBox packagePane;
    HBox parentPane;
    
    // THESE ARE HEADINGS
    Label classNameText;
    Label packageText;
    Label parentText;
    Label variablesText;
    Label methodsText;
    
    
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
        classPane = new HBox(5);
        packagePane = new HBox(5);
        parentPane = new HBox(5);
        
        //FINALIZE THE HEADINGS
        classNameText = new Label ("Class Name: ");
        packageText = new Label ("Package: ");
        parentText = new Label ("Parent:");
        variablesText = new Label ("Variables:");
        methodsText = new Label ("Methods:");
        
        classPane.getChildren().addAll(classNameText, new TextField());
        packagePane.getChildren().addAll(packageText, new TextField());
        parentPane.getChildren().addAll(parentText, new ComboBox());
        
        
        rightPane.getChildren().addAll(classPane, packagePane, parentPane);
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
        classNameText.getStyleClass().add(CLASS_HEADING_TEXT);
        packageText.getStyleClass().add(CLASS_SUB_HEADING_TEXT);
        parentText.getStyleClass().add(CLASS_SUB_HEADING_TEXT);
        
    }

    /**
     * This function reloads all the controls for editing tag attributes into
     * the workspace.
     */
    @Override
    public void reloadWorkspace() {
       
    }
}
