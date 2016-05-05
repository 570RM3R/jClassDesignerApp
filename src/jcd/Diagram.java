/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jcd;
import java.util.ArrayList;
import javafx.beans.binding.DoubleBinding;
import javafx.collections.ObservableList;
import javafx.scene.Parent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
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
    
    public Diagram(int diagramId, double x, double y, String nameString, String packageName, boolean isInterface, boolean isAbstract,
            boolean isAbriged, ObservableList<Variable> variableData, ObservableList<Method> methodData, ArrayList<Integer> inheritanceData,
            ArrayList<Integer> aggregationData, ArrayList<Integer> relationshipData, ArrayList<Integer> connectorData) {
        this.diagramId = diagramId == -1 ? Diagram.idCounter : diagramId;
        nameSection = new Rectangle(125, 30);
        nameSection.setX(x - 66.5);
        nameSection.setY(y - 15);
        nameText = new Text(nameString);       
        headingText = new Text();
        if(isInterface) {
            headingText.setText("<<Interface>>");
            nameSection.setHeight(45.4);
        }
        else if(isAbstract){
            headingText.setText("{abstract}");
            nameSection.setHeight(45.4);
        }
        this.variableData = variableData;
        this.packageName = packageName;
        this.isAbstract = isAbstract;
        this.isInterface = isInterface;
        this.isAbriged = isAbriged;
        variableSection = new Rectangle();
        variableText = new Text();
        methodSection = new Rectangle();
        methodText = new Text();
        this.methodData = methodData;
        getChildren().addAll(nameSection, headingText, nameText, variableSection, variableText, methodSection, methodText);
        this.inheritanceData = inheritanceData;
        this.aggregationData = aggregationData;
        this.relationshipData = relationshipData;
        this.connectorData = connectorData;        
        idCounter++;
    }
    
    public int getDiagramId(){
        return diagramId;
    }
    // Top, right, bottom, left
    public DoubleBinding getConnectionX(int side) {
        switch(side) {
            case 1:
                return nameSection.xProperty().add(widthProperty().divide(2));
            case 2:
                return nameSection.xProperty().add(widthProperty());
            case 3:
                return nameSection.xProperty().add(widthProperty().divide(2));
            default:
                return nameSection.xProperty().add(0);
        }     
    }
    // Top, right, bottom, left
    public DoubleBinding getConnectionY(int side) {
        switch(side) {
            case 1:
                return nameSection.yProperty().add(0);
            case 2:
                return nameSection.yProperty().add(heightProperty().divide(2));
            case 3:
                return nameSection.yProperty().add(heightProperty());
            default:
                return nameSection.yProperty().add(heightProperty().divide(2));
        }     
    }
    
    public DoubleBinding widthProperty() {
        return nameSection.widthProperty().add(0);
    }
    
    public DoubleBinding heightProperty() {
        return nameSection.heightProperty().add(variableSection.heightProperty().add(methodSection.heightProperty()));
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
    
    public ObservableList<Method> getMethodData() {
        return methodData;
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
    }
    
    public static void setIdCounter(int idCounter) {
        Diagram.idCounter = idCounter;
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
    
    public void dynamicPosition() {
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
    
    public void removeInheritanceData(int id) {
        for(int i = 0; i< inheritanceData.size(); i++) {
            if(inheritanceData.get(i) == id) {
                inheritanceData.remove(i);
                break;
            }
        }
    }    
    
    public void dynamicResize() {
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
        double maxWidth = Math.max(Math.max(nameText.getLayoutBounds().getWidth(), variableText.getLayoutBounds().getWidth()), methodText.getLayoutBounds().getWidth());      
        
        nameSection.setWidth(maxWidth + 13);
        
        // Fix the width and height dynamically when there are some variables
        if(!variableText.getText().isEmpty()) {
            variableSection.setWidth(maxWidth + 13);
            variableSection.setHeight(variableText.getLayoutBounds().getHeight() + 5);
            if(!methodText.getText().isEmpty()) {
                methodSection.setY(variableSection.getY() + variableSection.getHeight());
                methodText.setY(methodSection.getY() + 5);
                methodSection.setHeight(methodText.getLayoutBounds().getHeight() + 5);
            }
        }
        
        // Fix the width and height dynamically when there are some methods
        if(!methodText.getText().isEmpty()) {
            methodSection.setWidth(maxWidth + 13);
            methodSection.setHeight(methodText.getLayoutBounds().getHeight() + 5);
            if(!variableText.getText().isEmpty()) {
                variableSection.setHeight(variableText.getLayoutBounds().getHeight() + 5);
                methodSection.setY(variableSection.getY() + variableSection.getHeight());
                methodText.setY(methodSection.getY() + 5);
            }
        }
    }
    
    public void updateVariableText() {
        variableText.setText("");
        for(int i = 0; i < variableData.size(); i++) {
            variableText.setText(variableText.getText() + "\n" + variableData.get(i).toString());
        }
    }
    
    public void updateMethodText() {
        methodText.setText("");
        for(int i = 0; i < methodData.size(); i++) {
            methodText.setText(methodText.getText() + "\n" + methodData.get(i).toString());
        }
    }
    
    public void setStroke(Color color) {
        nameSection.setStroke(color);
        variableSection.setStroke(color);
        methodSection.setStroke(color);
    }
    
    public void setLayout(double LayoutX, double LayoutY) {
        nameSection.setLayoutX(LayoutX);
        nameSection.setLayoutY(LayoutY);
        nameText.setLayoutX(LayoutX);
        nameText.setLayoutY(LayoutY);
        if(isInterface || isAbstract) {
            headingText.setLayoutX(LayoutX);
            headingText.setLayoutY(LayoutY);
        }
        variableSection.setLayoutX(LayoutX);
        variableSection.setLayoutY(LayoutY);
        variableText.setLayoutX(LayoutX);
        variableText.setLayoutY(LayoutY);
        methodSection.setLayoutX(LayoutX);
        methodSection.setLayoutY(LayoutY);
        methodText.setLayoutX(LayoutX);
        methodText.setLayoutY(LayoutY);
    }
    
    public void setPosition(double PositionX, double PositionY) {
        nameSection.setX(PositionX);
        nameSection.setY(PositionY);
    }
    
    public void setFill(Color color) {
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
