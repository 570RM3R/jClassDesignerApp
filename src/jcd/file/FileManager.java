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
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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
import jcd.Method;
import jcd.Variable;
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
    public static final String JSON_METHOD_DATA = "method_data";
    public static final String JSON_PARENT_ID = "parent_id";
    public static final String JSON_VARIABLE_NAME = "variable_name";
    public static final String JSON_TYPE_NAME = "type_name";
    public static final String JSON_IS_STATIC = "is_static";
    public static final String JSON_IS_ABSTRACT = "is_abstract";
    public static final String JSON_ACCESS_TYPE = "access_type";
    public static final String JSON_METHOD_NAME = "method_name";
    public static final String JSON_RETURN_TYPE = "return_type";
    public static final String JSON_ARGUMENT_ONE = "argument_one";
    public static final String JSON_ARGUMENT_TWO = "argument_two";
    public static final String JSON_ARGUMENT_THREE = "argument_three";

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
    
    // HELPER METHOD FOR SAVING DATA TO A JSON FORMAT
    private JsonObject makeDiagramJsonObject(Diagram diagram) {
	JsonObject jsonDiagram = Json.createObjectBuilder()
		.add(JSON_DIAGRAM_ID, diagram.getDiagramId())
		.add(JSON_X_COORDINATE, diagram.getNameSection().getX() + 62.5)
		.add(JSON_Y_COORDINATE, diagram.getNameSection().getY() + 15)
		.add(JSON_NAME_STRING, diagram.getNameText().getText())
                .add(JSON_PACKAGE_NAME, diagram.getPackageName())
		.add(JSON_IS_INTERFACE, diagram.isInterface() ? 1 : 0)
                .add(JSON_VARIABLE_DATA, buildVariableJsonArray(diagram.getVariableData()))
		.add(JSON_METHOD_DATA, buildMethodJsonArray(diagram.getMethodData()))
                .add(JSON_PARENT_ID, diagram.getParentId())
		.build();
	return jsonDiagram;
    }
    
    // HELPER METHOD FOR SAVING DATA TO A JSON FORMAT
    private JsonArray buildVariableJsonArray(ObservableList<Variable> variableData) {
        JsonArrayBuilder variableArray = Json.createArrayBuilder();
        for (Variable variable : variableData) {
            JsonObject jsonVariable = Json.createObjectBuilder()
		.add(JSON_VARIABLE_NAME, variable.getVariableName())
		.add(JSON_TYPE_NAME, variable.getTypeName())
		.add(JSON_IS_STATIC, variable.isStatic() ? 1 : 0)
		.add(JSON_ACCESS_TYPE, variable.getAccessType())
		.build();
           variableArray.add(jsonVariable);
        }
        return variableArray.build();
    }
    
    // HELPER METHOD FOR SAVING DATA TO A JSON FORMAT
    private JsonArray buildMethodJsonArray(ObservableList<Method> methodData) {
        JsonArrayBuilder methodArray = Json.createArrayBuilder();
        for (Method method : methodData) {
            JsonObject jsonVariable = Json.createObjectBuilder()
		.add(JSON_METHOD_NAME, method.getMethodName())
		.add(JSON_RETURN_TYPE, method.getReturnType())
		.add(JSON_IS_STATIC, method.isStatic() ? 1 : 0)
                .add(JSON_IS_ABSTRACT, method.isAbstract() ? 1 : 0)
		.add(JSON_ACCESS_TYPE, method.getAccessType())
                .add(JSON_ARGUMENT_ONE, method.getArgumentOne())
                .add(JSON_ARGUMENT_TWO, method.getArgumentTwo())
                .add(JSON_ARGUMENT_THREE, method.getArgumentThree())
		.build();
           methodArray.add(jsonVariable);
        }
        return methodArray.build();
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
        
        Diagram.setIdCounter(json.getInt(JSON_ID_COUNTER));
        
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
                buildVariableList(jsonObject.getJsonArray(JSON_VARIABLE_DATA)),
                buildMethodList(jsonObject.getJsonArray(JSON_METHOD_DATA)),
                jsonObject.getInt(JSON_PARENT_ID)
            );
        diagram.updateVariableText();
        diagram.updateMethodText();
        diagram.setStroke(Color.BLACK);
        diagram.setFill(Color.web("#e0eae1"));
        diagram.dynamicResize();
        diagram.dynamicPosition();
        return diagram;
    }
    
    private ObservableList<Variable> buildVariableList(JsonArray variableArray) {
        ObservableList<Variable> variableList = FXCollections.observableArrayList();
        for (int i = 0; i < variableArray.size(); i++) {
	    JsonObject jsonObject = variableArray.getJsonObject(i);
            variableList.add(new Variable(
            jsonObject.getString(JSON_VARIABLE_NAME),
            jsonObject.getString(JSON_TYPE_NAME),
            jsonObject.getInt(JSON_IS_STATIC) == 1,
            jsonObject.getString(JSON_ACCESS_TYPE)
            ));
	}
        return variableList;
    }
    
    private ObservableList<Method> buildMethodList(JsonArray methodArray) {
        ObservableList<Method> methodList = FXCollections.observableArrayList();
        for (int i = 0; i < methodArray.size(); i++) {
	    JsonObject jsonObject = methodArray.getJsonObject(i);
            methodList.add(new Method(
            jsonObject.getString(JSON_METHOD_NAME),
            jsonObject.getString(JSON_RETURN_TYPE),
            jsonObject.getInt(JSON_IS_STATIC) == 1,
            jsonObject.getInt(JSON_IS_ABSTRACT) == 1,
            jsonObject.getString(JSON_ACCESS_TYPE),
            jsonObject.getString(JSON_ARGUMENT_ONE),
            jsonObject.getString(JSON_ARGUMENT_TWO),
            jsonObject.getString(JSON_ARGUMENT_THREE)
            ));
	}
        return methodList;
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
