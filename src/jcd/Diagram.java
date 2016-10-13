/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jcd;
import java.util.ArrayList;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Parent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

/**
 *
 * @author Saeid
 */
public class Diagram extends Parent{
    static int idCounter = 0;
    int diagramId;
    Rectangle nameSection;
    Rectangle variableSection;
    Rectangle methodSection;
    Text nameText;
    Text variableText;
    Text methodText;
    String packageName;
    boolean isGeneric;
    boolean isInterface;
    boolean isAbstract;
    boolean isAbriged;
    Text headingText;
    ObservableList<Variable> variableData;
    ObservableList<Method> methodData;
    ArrayList<Integer> inheritanceData;
    ArrayList<Integer> aggregationData;
    ArrayList<Integer> relationshipData;
    ArrayList<Integer> connectorData;
    
    public Diagram(int diagramId, double x, double y, String nameString, String packageName, boolean isGeneric, boolean isInterface,
            boolean isAbstract, boolean isAbriged, ObservableList<Variable> variableData, ObservableList<Method> methodData,
            ArrayList<Integer> inheritanceData, ArrayList<Integer> aggregationData, ArrayList<Integer> relationshipData, ArrayList<Integer> connectorData) {
        this.diagramId = diagramId == -1 ? Diagram.idCounter : diagramId;
        nameSection = new Rectangle(125, 30);
        nameSection.setX(x - 66.5);
        nameSection.setY(y - 15);
        nameSection.setStrokeWidth(1.3);
        nameSection.setFill(Color.web("#e5e5cd"));
        nameText = new Text(nameString);
        nameText.setFont(Font.font("Verdana", FontWeight.BOLD, 12));
        headingText = new Text();
        this.packageName = packageName;
        this.isGeneric = isGeneric;
        this.isAbstract = isAbstract;
        this.isInterface = isInterface;
        this.isAbriged = isAbriged;
        variableSection = new Rectangle();
        variableSection.setStrokeWidth(1.3);
        variableSection.setFill(Color.web("#f6f6ee"));
        variableText = new Text();
        methodSection = new Rectangle();
        methodSection.setStrokeWidth(1.3);
        methodSection.setFill(Color.web("#f6f6ee"));
        methodText = new Text();
        this.variableData = variableData;
        this.methodData = methodData;
        getChildren().addAll(nameSection, headingText, nameText, variableSection, variableText, methodSection, methodText);
        this.inheritanceData = inheritanceData;
        this.aggregationData = aggregationData;
        this.relationshipData = relationshipData;
        this.connectorData = connectorData;
        setStroke(Color.BLACK);
        if(this.isAbstract || this.isInterface)
            updateHeadingText();
        if(!this.isAbriged) {
            updateVariableText();
            updateMethodText();
        }
        dynamicResize();
        dynamicPosition();
        idCounter++;
    }
    
    // Deep copy constructor
    public Diagram(Diagram diagram) {
        diagramId = diagram.getDiagramId();
        nameSection = new Rectangle(125, 30);
        nameSection.setX(diagram.getNameSection().getX());
        nameSection.setY(diagram.getNameSection().getY());
        nameSection.setStrokeWidth(1.3);
        nameSection.setFill(Color.web("#e5e5cd"));
        nameText = new Text(diagram.getNameText().getText());  
        nameText.setFont(Font.font("Verdana", FontWeight.BOLD, 12));
        headingText = new Text();
        packageName = diagram.getPackageName();
        isGeneric = diagram.isGeneric();
        isAbstract = diagram.isAbstract();
        isInterface = diagram.isInterface();
        isAbriged = diagram.isAbriged();
        variableSection = new Rectangle();
        variableSection.setStrokeWidth(1.3);
        variableSection.setFill(Color.web("#f6f6ee"));
        variableText = new Text();
        methodSection = new Rectangle();
        methodSection.setStrokeWidth(1.3);
        methodSection.setFill(Color.web("#f6f6ee"));
        methodText = new Text();
        variableData = diagram.getClonedVariableData();
        methodData = diagram.getClonedMethodData();
        getChildren().addAll(nameSection, headingText, nameText, variableSection, variableText, methodSection, methodText);
        inheritanceData = (ArrayList<Integer>)diagram.getInheritanceData().clone();
        aggregationData = (ArrayList<Integer>)diagram.getAggregationData().clone();
        relationshipData = (ArrayList<Integer>)diagram.getRelationshipData().clone();
        connectorData = (ArrayList<Integer>)diagram.getConnectorData().clone();
        setStroke(Color.BLACK);
        if(this.isAbstract || this.isInterface)
            updateHeadingText();
        if(!this.isAbriged) {
            updateVariableText();
            updateMethodText();
        }
        dynamicResize();
        dynamicPosition();
    }
    
    public int getDiagramId(){
        return diagramId;
    }
    // Top, right, bottom, left
    public double getConnectionX(int side) {
        switch(side) {
            case 1:
                return nameSection.getX() + getWidth() / 2;
            case 2:
                return nameSection.getX() + getWidth();
            case 3:
                return nameSection.getX() + getWidth() / 2;
            default:
                return nameSection.getX();
        }     
    }
    // Top, right, bottom, left
    public double getConnectionY(int side) {
        switch(side) {
            case 1:
                return nameSection.getY();
            case 2:
                return nameSection.getY() + getHeight() / 2;
            case 3:
                return nameSection.getY() + getHeight();
            default:
                return nameSection.getY() + getHeight() / 2;
        }     
    }
    
    public double getWidth() {
        return nameSection.getWidth();
    }
    
    public double getHeight() {
        return nameSection.getHeight() + variableSection.getHeight() + methodSection.getHeight();
    }
    
    public Rectangle getNameSection() {
        return nameSection;
    }
    
    public Rectangle getVariableSection() {
        return variableSection;
    }
    
    public Rectangle getMethodSection() {
        return methodSection;
    }
    
    public Text getNameText() {
        return nameText;
    }
    
    public Text getVariableText() {
        return variableText;
    }
    
    public Text getMethodText() {
        return methodText;
    }
    
    public String getPackageName() {
        return packageName;
    }
    
    public boolean isGeneric() {
        return isGeneric;
    }
    
    public boolean isInterface() {
        return isInterface;
    }
    
    public boolean isAbstract() {
        return isAbstract;
    }
    
    public boolean isAbriged() {
        return isAbriged;
    }
    
    public Text getHeadingText() {
        return headingText;
    }
    
    public static int getIdCounter() {
        return idCounter;
    }
    
    public ObservableList<Variable> getVariableData() {
        return variableData;
    }
    
    public ObservableList<Variable> getClonedVariableData() {
        ObservableList<Variable> clonedVariableData = FXCollections.observableArrayList();
        for(Variable variable: variableData) {
            clonedVariableData.add(new Variable(variable));
        }
        return clonedVariableData;
    }
    
    public ObservableList<Method> getMethodData() {
        return methodData;
    }
    
    public ObservableList<Method> getClonedMethodData() {
        ObservableList<Method> clonedMethodData = FXCollections.observableArrayList();
        for(Method method: methodData) {
            clonedMethodData.add(new Method(method));
        }
        return clonedMethodData;
    }
    
    public ArrayList<Integer> getInheritanceData(){
        return inheritanceData;
    }
    
    public ArrayList<Integer> getAggregationData(){
        return aggregationData;
    }
    
    public ArrayList<Integer> getRelationshipData(){
        return relationshipData;
    }
    
    public ArrayList<Integer> getConnectorData(){
        return connectorData;
    }
    
    public void setPackageName(String packageName) {
        this.packageName = packageName;
        if(packageName.contains("java.") || packageName.contains("javax.") || packageName.contains("javaf.") || packageName.contains("javafx.")) {
            isGeneric = true;
            nameSection.setFill(Color.web("#d1d1e0"));
            variableSection.setFill(Color.web("#f0f0f5"));
            methodSection.setFill(Color.web("#f0f0f5"));
        }
        else {
            isGeneric = false;
            nameSection.setFill(Color.web("#e5e5cd"));
            variableSection.setFill(Color.web("#f6f6ee"));
            methodSection.setFill(Color.web("#f6f6ee"));
        }
    }
    
    public static void setIdCounter(int idCounter) {
        Diagram.idCounter = idCounter;
    }
    
    public void setAbstract(boolean isAbstract) {
        this.isAbstract = isAbstract;
    }
    
    public void setAbriged(boolean isAbriged) {
        this.isAbriged = isAbriged;
    }
    
    public void addVariable(Variable newVariable) {
        if(!isInterface && isValidVariable(newVariable))
            variableData.add(newVariable);
    }
    
    public void removeVariable(Variable selectedVariable) {
        if(!isInterface)
            variableData.remove(selectedVariable);
    }
    
    public void addMethod(Method newMethod) {
        if(isValidMethod(newMethod))
            methodData.add(newMethod);
    }
    
    public void removeMethod(Method selectedMethod) {
        if(!isInterface)
            methodData.remove(selectedMethod);
    }
    
    public final void dynamicPosition() {
        nameText.setX(nameSection.getX() + 6);
        nameText.setY(nameSection.getY() + 19);
        if(isInterface || isAbstract) {
            headingText.setX(nameSection.getX() + 6);
            headingText.setY(nameSection.getY() + 18);
            nameText.setX(nameSection.getX() + 6);
            nameText.setY(nameSection.getY() + 36.4);
        }
        variableSection.setX(nameSection.getX());
        variableSection.setY(nameSection.getY() + nameSection.getHeight());
        variableText.setX(variableSection.getX() + 6);
        variableText.setY(variableSection.getY() + 6);
        methodSection.setX(nameSection.getX());
        methodSection.setY(variableSection.getY() + variableSection.getHeight());
        methodText.setX(methodSection.getX() + 6);
        methodText.setY(methodSection.getY() + 6);
    }
    
    public boolean addInheritanceData(int id) {
        if(!inheritanceData.contains(id)) {
            return inheritanceData.add(id);
        }
        return false;
    }
    
    public void removeInheritanceData(int id) {
        for(int i = 0; i< inheritanceData.size(); i++) {
            if(inheritanceData.get(i) == id) {
                inheritanceData.remove(i);
                break;
            }
        }
    }
    
    public boolean addAggregationData(int id) {
        if(!aggregationData.contains(id)) {
            return aggregationData.add(id);
        }
        return false;
    }
    
    public void removeAggregationData(int id) {
        for(int i = 0; i< aggregationData.size(); i++) {
            if(aggregationData.get(i) == id) {
                aggregationData.remove(i);
                break;
            }
        }
    }
    
    public void removeConnectorData(int id) {
        for(int i = 0; i< connectorData.size(); i++) {
            if(connectorData.get(i) == id) {
                connectorData.remove(i);
                break;
            }
        }
    }
    
    public boolean addRelationshipData(int id) {
        if(!relationshipData.contains(id)) {
            return relationshipData.add(id);
        }
        return false;
    }
    
    public void removeRelationshipData(int id) {
        for(int i = 0; i< relationshipData.size(); i++) {
            if(relationshipData.get(i) == id) {
                relationshipData.remove(i);
                break;
            }
        }
    }
    
    public final void dynamicResize() {
        // Fix the width and height dynamically when there is no variable
        if(variableText.getText().isEmpty()) {
            variableSection.setWidth(0);
            variableSection.setHeight(0);      
        }
        // Fix the width and height dynamically when there is no method
        if(methodText.getText().isEmpty()) {
            methodSection.setWidth(0);
            methodSection.setHeight(0);      
        }
        
        // Calculate the max lodical width of a diagram
        double maxWidth = Math.max(Math.max(Math.max(nameText.getLayoutBounds().getWidth(), headingText.getLayoutBounds().getWidth()), variableText.getLayoutBounds().getWidth()), methodText.getLayoutBounds().getWidth());      
        
        nameSection.setWidth(maxWidth + 13);
        
        // Fix the width and height dynamically when there are some variables
        if(!variableText.getText().isEmpty()) {
            variableSection.setWidth(maxWidth + 13);
            variableSection.setHeight(variableText.getLayoutBounds().getHeight() + 6);
            if(!methodText.getText().isEmpty()) {
                methodSection.setY(variableSection.getY() + variableSection.getHeight());
                methodText.setY(methodSection.getY() + 5);
                methodSection.setHeight(methodText.getLayoutBounds().getHeight() + 6);
            }
        }
        
        // Fix the width and height dynamically when there are some methods
        if(!methodText.getText().isEmpty()) {
            methodSection.setWidth(maxWidth + 13);
            methodSection.setHeight(methodText.getLayoutBounds().getHeight() + 6);
            if(!variableText.getText().isEmpty()) {
                variableSection.setHeight(variableText.getLayoutBounds().getHeight() + 6);
                methodSection.setY(variableSection.getY() + variableSection.getHeight());
                methodText.setY(methodSection.getY() + 6);
            }
        }
    }
    
    public final void updateHeadingText() {
        headingText.setText("");
        if(isInterface) {
            headingText.setText("<<Interface>>");
            nameSection.setHeight(45.4);
        }
        else if(isAbstract){
            headingText.setText("{abstract}");
            nameSection.setHeight(45.4);
        }
    }
    
    public final void updateVariableText() {
        variableText.setText("");
        for(int i = 0; i < variableData.size(); i++) {
            variableText.setText(variableText.getText() + "\n" + variableData.get(i).toString());
        }
    }
    
    public final void updateMethodText() {
        methodText.setText("");
        for(int i = 0; i < methodData.size(); i++) {
            methodText.setText(methodText.getText() + "\n" + methodData.get(i).toString());
        }
    }
    
    public final void setStroke(Color color) {
        nameSection.setStroke(color);
        variableSection.setStroke(color);
        methodSection.setStroke(color);
    }
    
    public void setLayout(double deltaX, double deltaY) {
        nameSection.setLayoutX(deltaX);
        nameSection.setLayoutY(deltaY);
        nameText.setLayoutX(deltaX);
        nameText.setLayoutY(deltaY);
        if(isInterface || isAbstract) {
            headingText.setLayoutX(deltaX);
            headingText.setLayoutY(deltaY);
        }
        variableSection.setLayoutX(deltaX);
        variableSection.setLayoutY(deltaY);
        variableText.setLayoutX(deltaX);
        variableText.setLayoutY(deltaY);
        methodSection.setLayoutX(deltaX);
        methodSection.setLayoutY(deltaY);
        methodText.setLayoutX(deltaX);
        methodText.setLayoutY(deltaY);
    }
    
    public void setPosition(double PositionX, double PositionY) {
        nameSection.setX(PositionX);
        nameSection.setY(PositionY);
    }
    
    public final void setFill(Color color) {
        nameSection.setFill(color);
        variableSection.setFill(color);
        methodSection.setFill(color);
    }
    
    public void abridgeDiagram() {
        variableText.setText(null);
        methodText.setText(null);
    }
    
    public boolean isValidVariable(Variable newVariable) {
        for(Variable variable: variableData) {
            if(variable.getVariableName().equals(newVariable.getVariableName())) {
                return false;
            }
        }
        return true;
    }
    
    public boolean isValidMethod(Method newMethod) {
        return true;
    }
    
    @Override
    public String toString() {
        String diagramString = "";
        diagramString += "Diagram ID: " + diagramId + "\nDiagram Position: (" + nameSection.getX() + ", " + nameSection.getY() + 
                ")\nName: " + nameText.getText() + "\nPackage Name: " + packageName + "\nIs Interface: " + 
                (isInterface ? "Yes" : "No") + "\nIs Abstract: " + (isAbstract ? "Yes\n\nVariables:\n" : "No\n\nVariables:\n");
        
        if(variableData.isEmpty()) {
            diagramString += "No variables\n";
        }
        else {
            for(Variable variable: variableData){
                diagramString += variable.exportString() + "\n";
            }
        }
        
        diagramString += "\nMethods:\n";
        
        if(methodData.isEmpty()) {
            diagramString += "No methods\n";
        }
        else {
            for(Method method: methodData){
                diagramString += method.exportString() + "\n";
            }
        }
        
        diagramString += "\nPoints:\n";
        if(inheritanceData.isEmpty() && aggregationData.isEmpty() && relationshipData.isEmpty()) {
            diagramString += "No connectors\n";
        }
        else {
            diagramString += "Connector Data";
        }
        diagramString += "\n\n";
        return diagramString;
    }
}
