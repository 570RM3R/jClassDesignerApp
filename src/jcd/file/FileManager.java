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
import javafx.scene.shape.Ellipse;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.json.JsonWriter;
import javax.json.JsonWriterFactory;
import javax.json.stream.JsonGenerator;
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
    public static final String JSON_POSE_OBJECT = "pose_object";
    public static final String JSON_BACKGROUND_STYLE = "background_style";
    public static final String JSON_TYPE_NAME = "type";
    public static final String JSON_X_COORDINATE = "x_coordinate";
    public static final String JSON_Y_COORDINATE = "y_coordinate";
    public static final String JSON_X_LENGTH = "x_length";
    public static final String JSON_Y_LENGTH = "y_length";
    public static final String JSON_STROKE_WIDTH = "stroke_width";
    public static final String JSON_STROKE_COLOR = "stroke_color";
    public static final String JSON_FILL_COLOR = "fill_color";


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
        Pane pane = dataManager.getRightPane();
        
        StringWriter sw = new StringWriter();

	// THEN THE TREE
	JsonArrayBuilder arrayBuilder = Json.createArrayBuilder();
        for (int i = 0; i < pane.getChildren().size(); i++) {
            JsonObject poseObject = makePoseJsonObject((Shape)pane.getChildren().get(i));
            arrayBuilder.add(poseObject);
        }
        
	JsonArray nodesArray = arrayBuilder.build();
	
	// THEN PUT IT ALL TOGETHER IN A JsonObject
	JsonObject dataManagerJSO = Json.createObjectBuilder()
		.add(JSON_POSE_OBJECT, nodesArray)
                .add(JSON_BACKGROUND_STYLE, pane.getStyle())
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
        Pane pane = dataManager.getRightPane();
        pane.getChildren().clear();
	
	// LOAD THE JSON FILE WITH ALL THE DATA
	JsonObject json = loadJSONFile(filePath);
        pane.setStyle(json.getString(JSON_BACKGROUND_STYLE));
        String backgroundColor = json.getString(JSON_BACKGROUND_STYLE).equals("") ? "ffef84" : json.getString(JSON_BACKGROUND_STYLE).substring(22, json.getString(JSON_BACKGROUND_STYLE).length()-1);
	
	// LOAD THE TAG TREE
	JsonArray jsonPoseArray = json.getJsonArray(JSON_POSE_OBJECT);
	loadPoseObjects(jsonPoseArray, dataManager);
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
    private void loadPoseObjects(JsonArray jsonTagsArray, DataManager dataManager) {
        Pane pane = dataManager.getRightPane();
	// AND NOW JUST GO THROUGH THE REST OF THE ARRAY
	for (int i = 0; i < jsonTagsArray.size(); i++) {
	    JsonObject jsonObject = jsonTagsArray.getJsonObject(i);
	    Shape shpae = loadShape(jsonObject);
            pane.getChildren().add(shpae); 
	}
    }
    
    // HELPER METHOD FOR LOADING DATA FROM A JSON FORMAT
    private Shape loadShape(JsonObject jsonObject) {
        Rectangle rectangle = new Rectangle();
        Ellipse ellipse = new Ellipse();
        if(jsonObject.getString(JSON_TYPE_NAME).equals("rectangle")) {
            rectangle.setX(jsonObject.getInt(JSON_X_COORDINATE));
            rectangle.setY(jsonObject.getInt(JSON_Y_COORDINATE));
            rectangle.setWidth(jsonObject.getInt(JSON_X_LENGTH));
            rectangle.setHeight(jsonObject.getInt(JSON_Y_LENGTH));
            rectangle.setStrokeWidth(jsonObject.getInt(JSON_STROKE_WIDTH));
            rectangle.setStroke(Color.web(jsonObject.getString(JSON_STROKE_COLOR)));
            rectangle.setFill(Color.web(jsonObject.getString(JSON_FILL_COLOR)));
            return (Shape)rectangle;
        }
        else {
            ellipse.setCenterX(jsonObject.getInt(JSON_X_COORDINATE));
            ellipse.setCenterY(jsonObject.getInt(JSON_Y_COORDINATE));
            ellipse.setRadiusX(jsonObject.getInt(JSON_X_LENGTH));
            ellipse.setRadiusY(jsonObject.getInt(JSON_Y_LENGTH));
            ellipse.setStrokeWidth(jsonObject.getInt(JSON_STROKE_WIDTH));
            ellipse.setStroke(Color.web(jsonObject.getString(JSON_STROKE_COLOR)));
            ellipse.setFill(Color.web(jsonObject.getString(JSON_FILL_COLOR)));
            return (Shape)ellipse;
        }
        
    }
    
    // HELPER METHOD FOR SAVING DATA TO A JSON FORMAT
    private JsonObject makePoseJsonObject(Shape shape) {
        Rectangle rectangle = new Rectangle();
        Ellipse ellipse = new Ellipse();
	if (shape instanceof Rectangle) {
            rectangle = (Rectangle) shape;
        }
        else if (shape instanceof Ellipse) {
            ellipse = (Ellipse) shape;
        }
	JsonObject jso = Json.createObjectBuilder()
		.add(JSON_TYPE_NAME, shape instanceof Rectangle ? "rectangle" : "ellipse")
		.add(JSON_X_COORDINATE, shape instanceof Rectangle ? rectangle.getX() : ellipse.getCenterX())
		.add(JSON_Y_COORDINATE, shape instanceof Rectangle ? rectangle.getY() : ellipse.getCenterY())
		.add(JSON_X_LENGTH, shape instanceof Rectangle ? rectangle.getWidth() : ellipse.getRadiusX())
		.add(JSON_Y_LENGTH, shape instanceof Rectangle ? rectangle.getHeight() : ellipse.getRadiusY())
                .add(JSON_STROKE_WIDTH, shape.getStrokeWidth())
                .add(JSON_STROKE_COLOR, shape.getStroke().toString().substring(2))
		.add(JSON_FILL_COLOR, shape.getFill().toString().substring(2))
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
