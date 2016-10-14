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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.layout.Pane;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.json.JsonWriter;
import javax.json.JsonWriterFactory;
import javax.json.stream.JsonGenerator;
import jcd.Connector;
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
    
    public FileManager(){}
        
    public static final String JSON_FILE = "data.json";
    public static final String PATH_TEMP = "./temp/";
    public static final String TEMP_PAGE = PATH_TEMP + JSON_FILE;
    public static final String JSON_JCLASS_DESIGNER_DATA = "jClass Designer Data";
    public static final String JSON_DIAGRAM_ID_COUNTER = "diagram_id_counter";
    public static final String JSON_CONNECTOR_ID_COUNTER = "connector_id_counter";
    public static final String JSON_ID = "id";
    public static final String JSON_TYPE = "type";
    public static final String JSON_X = "x";
    public static final String JSON_Y = "y";
    public static final String JSON_NAME = "name";
    public static final String JSON_IS_INTERFACE = "is_interface";
    public static final String JSON_IS_GENERIC = "is_generic";
    public static final String JSON_PACKAGE_NAME = "package_name";
    public static final String JSON_VARIABLE_DATA = "variable_data";
    public static final String JSON_METHOD_DATA = "method_data";
    public static final String JSON_EXTENSION_DATA = "extension_data";
    public static final String JSON_IMPLEMENTATION_DATA = "implementaion_data";
    public static final String JSON_AGGREGATION_DATA = "aggregation_data";
    public static final String JSON_RELATIONSHIP_DATA = "relationship_data";
    public static final String JSON_CONNECTOR_DATA = "connector_data";
    public static final String JSON_VARIABLE_NAME = "variable_name";
    public static final String JSON_TYPE_NAME = "type_name";
    public static final String JSON_IS_STATIC = "is_static";
    public static final String JSON_IS_ABSTRACT = "is_abstract";
    public static final String JSON_IS_ABRIGED = "is_abriged";
    public static final String JSON_ACCESS_TYPE = "access_type";
    public static final String JSON_METHOD_NAME = "method_name";
    public static final String JSON_RETURN_TYPE = "return_type";
    public static final String JSON_ARGUMENT_ONE = "argument_one";
    public static final String JSON_ARGUMENT_TWO = "argument_two";
    public static final String JSON_ARGUMENT_THREE = "argument_three";
    public static final String JSON_SOURCE_ID = "source_id";
    public static final String JSON_DESTINATION_ID = "destination_id";
    public static final String JSON_START_X = "start_x";
    public static final String JSON_START_Y = "start_y";
    public static final String JSON_END_X = "end_x";
    public static final String JSON_END_Y = "end_y";
    public static final String JSON_HEAD_LAYOUTX = "head_layoutX";
    public static final String JSON_HEAD_LAYOUTY = "head_layoutY";
    public static final String JSON_ANCHOR_DATA = "anchor_data";

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
            if (pane.getChildren().get(i) instanceof Diagram) {
                JsonObject diagram = makeDiagramJsonObject((Diagram)pane.getChildren().get(i));
                arrayBuilder.add(diagram);
            }
            else if (pane.getChildren().get(i) instanceof Connector) {
                JsonObject connector = makeConnectorJsonObject((Connector)pane.getChildren().get(i));
                arrayBuilder.add(connector);
            }
        }
        
	JsonArray nodesArray = arrayBuilder.build();
	
	// THEN PUT IT ALL TOGETHER IN A JsonObject
	JsonObject dataManagerJSO = Json.createObjectBuilder()
		.add(JSON_JCLASS_DESIGNER_DATA, nodesArray)
                .add(JSON_DIAGRAM_ID_COUNTER, Diagram.getIdCounter())
                .add(JSON_CONNECTOR_ID_COUNTER, Connector.getIdCounter())
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
		.add(JSON_ID, 'D' + Integer.toString(diagram.getDiagramId()))
		.add(JSON_X, diagram.getNameSection().getX() + 66.5)
		.add(JSON_Y, diagram.getNameSection().getY() + 15)
		.add(JSON_NAME, diagram.getNameText().getText())
                .add(JSON_PACKAGE_NAME, diagram.getPackageName())
                .add(JSON_IS_GENERIC, diagram.isGeneric() ? 1 : 0)
		.add(JSON_IS_INTERFACE, diagram.isInterface() ? 1 : 0)
                .add(JSON_IS_ABSTRACT, diagram.isAbstract()? 1 : 0)
                .add(JSON_IS_ABRIGED, diagram.isAbriged()? 1 : 0)
                .add(JSON_VARIABLE_DATA, buildVariableJsonArray(diagram.getVariableData()))
		.add(JSON_METHOD_DATA, buildMethodJsonArray(diagram.getMethodData()))
                .add(JSON_EXTENSION_DATA, buildDataString(diagram.getExtensionData()))
                .add(JSON_IMPLEMENTATION_DATA, buildDataString(diagram.getImplementationData()))
                .add(JSON_AGGREGATION_DATA, buildDataString(diagram.getAggregationData()))
                .add(JSON_RELATIONSHIP_DATA, buildDataString(diagram.getRelationshipData()))
                .add(JSON_CONNECTOR_DATA, buildDataString(diagram.getConnectorData()))
		.build();
	return jsonDiagram;
    }
    
    // HELPER METHOD FOR SAVING DATA TO A JSON FORMAT
    private JsonObject makeConnectorJsonObject(Connector connector) {
	JsonObject jsonConnector = Json.createObjectBuilder()
		.add(JSON_ID, 'C' + Integer.toString(connector.getConnectorId()))
                .add(JSON_TYPE, connector.getType())
		.add(JSON_SOURCE_ID, connector.getSourceId())
		.add(JSON_DESTINATION_ID, connector.getDestinationId())
		.add(JSON_START_X, connector.getStartX())
                .add(JSON_START_Y, connector.getStartY())
		.add(JSON_END_X, connector.getEndX())
                .add(JSON_END_Y, connector.getEndY())
                .add(JSON_HEAD_LAYOUTX, connector.getHeadLayoutX())
                .add(JSON_HEAD_LAYOUTY, connector.getHeadLayoutY())
                .add(JSON_ANCHOR_DATA, buildAnchorDataString(connector.getAnchorData()))
		.build();
	return jsonConnector;
    }
    
    // HELPER METHOD FOR SAVING DATA TO A JSON FORMAT
    private JsonArray buildVariableJsonArray(ObservableList<Variable> variableData) {
        JsonArrayBuilder variableArray = Json.createArrayBuilder();
        for (Variable variable : variableData) {
            JsonObject jsonVariable = Json.createObjectBuilder()
		.add(JSON_VARIABLE_NAME, variable.getVariableName())
		.add(JSON_TYPE_NAME, variable.getTypeName())
		.add(JSON_IS_STATIC, variable.isStatic())
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
            JsonObject jsonMethod = Json.createObjectBuilder()
		.add(JSON_METHOD_NAME, method.getMethodName())
		.add(JSON_RETURN_TYPE, method.getReturnType())
		.add(JSON_IS_STATIC, method.isStatic())
                .add(JSON_IS_ABSTRACT, method.isAbstract())
		.add(JSON_ACCESS_TYPE, method.getAccessType())
                .add(JSON_ARGUMENT_ONE, method.getArgumentOne())
                .add(JSON_ARGUMENT_TWO, method.getArgumentTwo())
                .add(JSON_ARGUMENT_THREE, method.getArgumentThree())
		.build();
           methodArray.add(jsonMethod);
        }
        return methodArray.build();
    }
    private String buildAnchorDataString(ArrayList<Double> anchorData) {
        String anchorDataString = Double.toString(anchorData.get(0));
        for (int i = 1; i < anchorData.size(); i++) {
            anchorDataString += ", " + anchorData.get(i);
        }
        return anchorDataString;
    }
    
    private String buildDataString(ArrayList<Integer> data) {
        String dataString = "";
        if(!data.isEmpty()) {
            dataString = Integer.toString(data.get(0));
        }
        for (int i = 1; i < data.size(); i++) {
            dataString += ", ";
            dataString += data.get(i);
        }
        return dataString;
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
        
        Diagram.setIdCounter(json.getInt(JSON_DIAGRAM_ID_COUNTER));
        Connector.setIdCounter(json.getInt(JSON_CONNECTOR_ID_COUNTER));
        
	// LOAD THE TAG TREE
	JsonArray jsonClassArray = json.getJsonArray(JSON_JCLASS_DESIGNER_DATA);
	loadClassObjects(jsonClassArray, dataManager);
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
    private void loadClassObjects(JsonArray jsonTagsArray, DataManager dataManager) {
        Pane pane = dataManager.getLeftPane();
	// AND NOW JUST GO THROUGH THE REST OF THE ARRAY
	for (int i = 0; i < jsonTagsArray.size(); i++) {
	    JsonObject jsonObject = jsonTagsArray.getJsonObject(i);
            if(jsonObject.getString(JSON_ID).charAt(0) == 'D')
                pane.getChildren().add(loadDiagram(jsonObject)); 
            else if(jsonObject.getString(JSON_ID).charAt(0) == 'C')
                pane.getChildren().add(loadConnector(jsonObject));
	}
    }
    
    // HELPER METHOD FOR LOADING DATA FROM A JSON FORMAT
    private Diagram loadDiagram(JsonObject jsonObject) {
        Diagram diagram = new Diagram(
                Integer.parseInt(jsonObject.getString(JSON_ID).substring(1)),
                jsonObject.getInt(JSON_X),
                jsonObject.getInt(JSON_Y),
                jsonObject.getString(JSON_NAME),
                jsonObject.getString(JSON_PACKAGE_NAME),
                jsonObject.getInt(JSON_IS_GENERIC) == 1,
                jsonObject.getInt(JSON_IS_INTERFACE) == 1,
                jsonObject.getInt(JSON_IS_ABSTRACT) == 1,
                jsonObject.getInt(JSON_IS_ABRIGED) == 1,
                buildVariableList(jsonObject.getJsonArray(JSON_VARIABLE_DATA)),
                buildMethodList(jsonObject.getJsonArray(JSON_METHOD_DATA)),
                buildDataArray(jsonObject.getString(JSON_EXTENSION_DATA)),
                buildDataArray(jsonObject.getString(JSON_IMPLEMENTATION_DATA)),
                buildDataArray(jsonObject.getString(JSON_AGGREGATION_DATA)),
                buildDataArray(jsonObject.getString(JSON_RELATIONSHIP_DATA)),
                buildDataArray(jsonObject.getString(JSON_CONNECTOR_DATA))
            );
        return diagram;
    }
    
    // HELPER METHOD FOR LOADING DATA FROM A JSON FORMAT
    private Connector loadConnector(JsonObject jsonObject) {
        Connector connector = new Connector(
                Integer.parseInt(jsonObject.getString(JSON_ID).substring(1)),
                jsonObject.getInt(JSON_TYPE),
                jsonObject.getInt(JSON_SOURCE_ID),
                jsonObject.getInt(JSON_DESTINATION_ID),
                jsonObject.getInt(JSON_START_X),
                jsonObject.getInt(JSON_START_Y),
                jsonObject.getInt(JSON_END_X),
                jsonObject.getInt(JSON_END_Y),
                jsonObject.getInt(JSON_HEAD_LAYOUTX),
                jsonObject.getInt(JSON_HEAD_LAYOUTY),
                buildAnchorData(jsonObject.getString(JSON_ANCHOR_DATA))
            );
        connector.fixAnchorPosition();
        return connector;
    }
    
    private ObservableList<Variable> buildVariableList(JsonArray variableArray) {
        ObservableList<Variable> variableList = FXCollections.observableArrayList();
        for (int i = 0; i < variableArray.size(); i++) {
	    JsonObject jsonObject = variableArray.getJsonObject(i);
            variableList.add(new Variable(
            jsonObject.getString(JSON_VARIABLE_NAME),
            jsonObject.getString(JSON_TYPE_NAME),
            jsonObject.getString(JSON_IS_STATIC),
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
            jsonObject.getString(JSON_IS_STATIC),
            jsonObject.getString(JSON_IS_ABSTRACT),
            jsonObject.getString(JSON_ACCESS_TYPE),
            jsonObject.getString(JSON_ARGUMENT_ONE),
            jsonObject.getString(JSON_ARGUMENT_TWO),
            jsonObject.getString(JSON_ARGUMENT_THREE)
            ));
	}
        return methodList;
    }
    
    private ArrayList<Double> buildAnchorData(String anchorString) {
        ArrayList<Double> anchorData = new ArrayList<>();
        if(!anchorString.isEmpty()) {
            String[] anchorStringArray = anchorString.split(", ");
            for(String anchorToken : anchorStringArray) {
                anchorData.add(Double.parseDouble(anchorToken));
            }
        }
       return anchorData;
    }
    
    private ArrayList<Integer> buildDataArray(String dataString) {
        ArrayList<Integer> data = new ArrayList<>();
        if(!dataString.isEmpty()) {
            String[] dataStringArray = dataString.split(", ");
            for(String dataToken : dataStringArray) {
                data.add(Integer.parseInt(dataToken));
            }
        }
       return data;
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
