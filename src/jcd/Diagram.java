/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jcd;
import java.util.ArrayList;
import java.util.List;
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
    double width;
    double height;
    Rectangle nameSection;
    Rectangle variableSection;
    Rectangle methodSection;
    Text nameText;
    Text variableText;
    Text methodText;
    String packageName;
    boolean isInterface;
    boolean isAbstract;
    Text headingText;
    ObservableList<Variable> variableData;
    ObservableList<Method> methodData;
    int parentId;
    int aggregateId;
    int relationshipId;
    ArrayList<List<Double>> pointData;
    
    public Diagram(int diagramId, double x, double y, double width, double height, String nameString, String packageName,boolean isInterface, 
            boolean isAbstract, ObservableList<Variable> variableData, ObservableList<Method> methodData, int parentId, ArrayList<List<Double>> pointData) {
        this.diagramId = diagramId == -1 ? Diagram.idCounter : diagramId;
        nameSection = new Rectangle(125, 30);
        nameSection.setX(x - 62.5);
        nameSection.setY(y - 15);
        this.width = width;
        this.height = height;
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
        variableSection = new Rectangle();
        variableText = new Text();
        methodSection = new Rectangle();
        methodText = new Text();
        this.methodData = methodData;
        getChildren().addAll(nameSection, headingText, nameText, variableSection, variableText, methodSection, methodText);
        this.parentId = parentId;
        this.pointData = pointData;
    }
    
    public int getDiagramId(){
        return diagramId;
    }
    
    public double getDiagramWidth() {
        return nameSection.getWidth() + variableSection.getWidth() + methodSection.getWidth();
    }
    
    public double getDiagramHeight() {
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
    
    public boolean isInterface() {
        return isInterface;
    }
    
    public boolean isAbstract() {
        return isAbstract;
    }
    
    public Text getHeadingText() {
        return headingText;
    }
    
    public int getParentId(){
        return parentId;
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
    
    public ArrayList<List<Double>> getPointData() {
        return pointData;
    }
    
    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }
    
    public void setParentId(int parentId) {
        this.parentId = parentId;
    }
    
    public static void setIdCounter(int idCounter) {
        Diagram.idCounter = idCounter;
    }
    
    public void addVariable(Variable newVariable) {
        if(!isInterface && isValidVariable(newVariable)) {
            variableData.add(newVariable);
            variableText.setText(variableText.getText() + "\n" + newVariable.toString());
            dynamicResize();
            dynamicPosition();
        }
    }
    
    public void addMethod(Method newMethod) {
        if(isValidMethod(newMethod)) {
            methodData.add(newMethod);
            methodText.setText(methodText.getText() + "\n" + newMethod.toString());
            dynamicResize();
            dynamicPosition();
        }
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
    
    
    public void dynamicResize() {      
        double maxWidth = Math.max(Math.max(nameText.getLayoutBounds().getWidth(), variableText.getLayoutBounds().getWidth()), methodText.getLayoutBounds().getWidth());      
        if(nameSection.getWidth() - 12 <= maxWidth) {
            nameSection.setWidth(maxWidth + 12);
            if(!variableText.getText().isEmpty())
                variableSection.setWidth(maxWidth + 12);
            if(!methodText.getText().isEmpty())
                methodSection.setWidth(maxWidth + 12);
        }
        if(!variableText.getText().isEmpty()) {
            variableSection.setHeight(variableText.getLayoutBounds().getHeight() + 5);
            if(!methodText.getText().isEmpty()) {
                methodSection.setY(variableSection.getY() + variableSection.getHeight());
                methodText.setY(methodSection.getY() + 5);
                methodSection.setHeight(methodText.getLayoutBounds().getHeight() + 5);
            }
        }       
        if(!methodText.getText().isEmpty()) {
            methodSection.setHeight(methodText.getLayoutBounds().getHeight() + 5);
            if(!variableText.getText().isEmpty()) {
                variableSection.setHeight(variableText.getLayoutBounds().getHeight() + 5);
                methodSection.setY(variableSection.getY() + variableSection.getHeight());
                methodText.setY(methodSection.getY() + 5);
            }
        }
    }
    
    public void updateVariableText() {
        for(int i = 0; i < variableData.size(); i++) {
            variableText.setText(variableText.getText() + "\n" + variableData.get(i).toString());
        }
        dynamicResize();
    }
    
    public void updateMethodText() {
        for(int i = 0; i < methodData.size(); i++) {
            methodText.setText(methodText.getText() + "\n" + methodData.get(i).toString());
        }
        dynamicResize();
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
        dynamicPosition();
    }
    
    public void setFill(Color color) {
        nameSection.setFill(color);
        variableSection.setFill(color);
        methodSection.setFill(color);
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
        if(pointData.isEmpty()) {
            diagramString += "No points\n";
        }
        else {
            diagramString += "[" + pointData.get(0);
            for(int i = 1; i < pointData.size(); i++){
                diagramString += "{" + pointData.get(i).get(0);
                for(int j = 1; j < pointData.get(i).size(); j++) {
                    diagramString += ", " + pointData.get(i).get(j);
                }
                diagramString += "}";
            }
            diagramString += "]";
        }
        diagramString += "\n\n";
        return diagramString;
    }
}
