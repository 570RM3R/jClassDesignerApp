/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jcd.data;

import javafx.scene.layout.Pane;
import paf.components.AppDataComponent;
import paf.AppTemplate;

/**
 * This class serves as the data management component for this application.
 *
 * @author Richard McKenna
 * @author Saeid
 * @version 1.0
 */
public class DataManager implements AppDataComponent {
    Pane leftPane;
    // THIS IS A SHARED REFERENCE TO THE APPLICATION
    AppTemplate app;

    /**
     * THis constructor creates the data manager and sets up the
     *
     *
     * @param initApp The application within which this data manager is serving.
     */
    public DataManager(AppTemplate initApp) throws Exception {
	// KEEP THE APP FOR LATER
	app = initApp;
    }

    /**
     * This function clears out the HTML tree and reloads it with the minimal
     * tags, like html, head, and body such that the user can begin editing a
     * page.
     */
    @Override
    public void reset() {
        leftPane.getChildren().clear();
        leftPane.setStyle("-fx-background-color:#FFFFFF;");
    }
    
    public void setLeftPane(Pane pane) {
        leftPane = pane;
    }
    
    public Pane getRightPane() {
        return leftPane;
    }
}
