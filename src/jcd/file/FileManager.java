/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jcd.file;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.json.JsonWriter;
import javax.json.JsonWriterFactory;
import javax.json.stream.JsonGenerator;
import jcd.Diagram;
import jcd.data.DataManager;
import paf.components.AppDataComponent;
import paf.components.AppFileComponent;

/**
 * This class serves as the file management component for this application,
 * providing all I/O services.
 *
 * @author Richard McKenna
 * @author Saeid
 * @version 1.0
 */
public class FileManager implements AppFileComponent {
        
    public static final String JSON_FILE = "data.json";
    public static final String PATH_TEMP = "./temp/";
    public static final String TEMP_PAGE = PATH_TEMP + JSON_FILE;
    public static final String JSON_DIAGRAM_COLLECTION = "diagram_collections";
    public static final String JSON_ID_COUNTER = "id_counter";
    public static final String JSON_DIAGRAM_ID = "diagram_id";
    public static final String JSON_X_COORDINATE = "x_coordinate";
    public static final String JSON_Y_COORDINATE = "y_coordinate";
    public static final String JSON_NAME_STRING = "name_string";
    public static final String JSON_IS_INTERFACE = "is_interface";
    public static final String JSON_PACKAGE_NAME = "package_name";
    public static final String JSON_VARIABLE_DATA = "variable_data";
    public static final String JSON_VARIABLE_STRING = "variable_string";
    public static final String JSON_METHOD_DATA = "method_data";
    public static final String JSON_METHOD_STRING = "method_string";
    public static final String JSON_PARENT_NAME = "parent_name";
    public static final String JSON_PARENT_ID = "parent_id";


    /**
     * This method is for saving user work, which in the case of this
     * application means the data that constitutes the page DOM.
     * 
     * @param data The data management component for this application.
     * 
     * @param filePath Path (including file name/extension) to where
     * to save the data to.
     * 
     * @throws IOException Thrown should there be an error writing 
     * out data to the file.
     */
    @Override
    public void saveData(AppDataComponent data, String filePath) throws IOException {
        DataManager dataManager = (DataManager)data;
        Pane pane = dataManager.getLeftPane();
        
        StringWriter sw = new StringWriter();

	// THEN THE TREE
	JsonArrayBuilder arrayBuilder = Json.createArrayBuilder();
        for (int i = 0; i < pane.getChildren().size(); i++) {
            JsonObject diagram = makeDiagramJsonObject((Diagram)pane.getChildren().get(i));
            arrayBuilder.add(diagram);
        }
        
	JsonArray nodesArray = arrayBuilder.build();
	
	// THEN PUT IT ALL TOGETHER IN A JsonObject
	JsonObject dataManagerJSO = Json.createObjectBuilder()
		.add(JSON_DIAGRAM_COLLECTION, nodesArray)
                .add(JSON_ID_COUNTER, Diagram.getIdCounter())
		.build();
	
	// AND NOW OUTPUT IT TO A JSON FILE WITH PRETTY PRINTING
	Map<String, Object> properties = new HashMap<>(1);
	properties.put(JsonGenerator.PRETTY_PRINTING, true);
	JsonWriterFactory writerFactory = Json.createWriterFactory(properties);
	JsonWriter jsonWriter = writerFactory.createWriter(sw);
	jsonWriter.writeObject(dataManagerJSO);
	jsonWriter.close();

	// INIT THE WRITER
	OutputStream os = new FileOutputStream(filePath);
	JsonWriter jsonFileWriter = Json.createWriter(os);
	jsonFileWriter.writeObject(dataManagerJSO);
	String prettyPrinted = sw.toString();
	PrintWriter pw = new PrintWriter(filePath);
	pw.write(prettyPrinted);
	pw.close();

    }
      
    /**
     * This method loads data from a JSON formatted file into the data 
     * management component and then forces the updating of the workspace
     * such that the user may edit the data.
     * 
     * @param data Data management component where we'll load the file into.
     * 
     * @param filePath Path (including file name/extension) to where
     * to load the data from.
     * 
     * @throws IOException Thrown should there be an error reading
     * in data from the file.
     */
    @Override
    public void loadData(AppDataComponent data, String filePath) throws IOException {
        // CLEAR THE OLD DATA OUT
	DataManager dataManager = (DataManager)data;
	dataManager.reset();
        Pane pane = dataManager.getLeftPane();
        pane.getChildren().clear();
	
	// LOAD THE JSON FILE WITH ALL THE DATA
	JsonObject json = loadJSONFile(filePath);
        
	// LOAD THE TAG TREE
	JsonArray jsonPoseArray = json.getJsonArray(JSON_DIAGRAM_COLLECTION);
	loadDiagramObjects(jsonPoseArray, dataManager);
    }

    // HELPER METHOD FOR LOADING DATA FROM A JSON FORMAT
    private JsonObject loadJSONFile(String jsonFilePath) throws IOException {
	InputStream is = new FileInputStream(jsonFilePath);
	JsonReader jsonReader = Json.createReader(is);
	JsonObject json = jsonReader.readObject();
	jsonReader.close();
	is.close();
	return json;

    }
    
    // HELPER METHOD FOR LOADING DATA FROM A JSON FORMAT
    private void loadDiagramObjects(JsonArray jsonTagsArray, DataManager dataManager) {
        Pane pane = dataManager.getLeftPane();
	// AND NOW JUST GO THROUGH THE REST OF THE ARRAY
	for (int i = 0; i < jsonTagsArray.size(); i++) {
	    JsonObject jsonObject = jsonTagsArray.getJsonObject(i);
            pane.getChildren().add(loadDiagram(jsonObject)); 
	}
    }
    
    // HELPER METHOD FOR LOADING DATA FROM A JSON FORMAT
    private Diagram loadDiagram(JsonObject jsonObject) {
        Diagram diagram = new Diagram(
                jsonObject.getInt(JSON_DIAGRAM_ID),
                jsonObject.getInt(JSON_X_COORDINATE),
                jsonObject.getInt(JSON_Y_COORDINATE),
                jsonObject.getString(JSON_NAME_STRING),
                jsonObject.getString(JSON_PACKAGE_NAME),
                jsonObject.getInt(JSON_IS_INTERFACE) == 1,
                jsonObject.getString(JSON_VARIABLE_STRING),
                jsonObject.getString(JSON_METHOD_STRING),
                jsonObject.getString(JSON_PARENT_NAME),
                jsonObject.getInt(JSON_PARENT_ID)
            );
        diagram.setFill(Color.web("#e0eae1"));
        diagram.dynamicResize();
        diagram.dynamicPosition();
        diagram.setStroke(Color.BLACK);
        return diagram;
    }
    
    // HELPER METHOD FOR SAVING DATA TO A JSON FORMAT
    private JsonObject makeDiagramJsonObject(Diagram diagram) {
	JsonObject jso = Json.createObjectBuilder()
		.add(JSON_DIAGRAM_ID, diagram.getDiagramId())
		.add(JSON_X_COORDINATE, diagram.getNameSection().getX() + 62.5)
		.add(JSON_Y_COORDINATE, diagram.getNameSection().getY() + 15)
		.add(JSON_NAME_STRING, diagram.getNameText().getText())
                .add(JSON_PACKAGE_NAME, diagram.getParentName())
		.add(JSON_IS_INTERFACE, diagram.isInterface() ? 1 : 0)
                .add(JSON_VARIABLE_DATA, "")
                .add(JSON_VARIABLE_STRING, diagram.getVariableText().getText())
		.add(JSON_METHOD_DATA, "")
                .add(JSON_METHOD_STRING, diagram.getMethodText().getText())
                .add(JSON_PARENT_NAME, diagram.getParentName())
                .add(JSON_PARENT_ID, diagram.getParentId())
		.build();
	return jso;
    }
    
    /**
     * This method exports the contents of the data manager to a 
     * Web page including the html page, needed directories, and
     * the CSS file.
     * 
     * @param data The data management component.
     * 
     * @param filePath Path (including file name/extension) to where
     * to export the page to.
     * 
     * @throws IOException Thrown should there be an error writing
     * out data to the file.
     */
    @Override
    public void exportData(AppDataComponent data, String filePath) throws IOException {
        
    }
    
    /**
     * This method is provided to satisfy the compiler, but it
     * is not used by this application.
     */
    @Override
    public void importData(AppDataComponent data, String filePath) throws IOException {
	// NOTE THAT THE Web Page Maker APPLICATION MAKES
	// NO USE OF THIS METHOD SINCE IT NEVER IMPORTS
	// EXPORTED WEB PAGES
    }
    
        /**
     * This function clears the contents of the file argument.
     * 
     * @param filePath The file to clear.
     * 
     * @throws IOException Thrown should there be an issue writing
     * to the file to clear.
     */
    public void clearFile(String filePath) throws IOException {
	PrintWriter out = new PrintWriter(filePath);
	out.print("");
	out.close();
    }
}
