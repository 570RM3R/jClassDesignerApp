/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jcd;

import javafx.beans.property.SimpleStringProperty;

/**
 *
 * @author Saeid
 */
public class Variable {
    String variableName;
    String typeName;
    String isStatic;
    String accessType;
    
    public Variable(String variableName, String typeName, String isStatic, String accessType) {
        this.variableName = variableName;
        this.typeName = typeName;
        this.isStatic = isStatic;
        this.accessType = accessType;
    }
    // Copy constructor
    public Variable(Variable variable) {
        variableName = variable.getVariableName();
        typeName = variable.getTypeName();
        isStatic = variable.isStatic();
        accessType = variable.getAccessType();
    }
    
    public String getVariableName() {
        return variableName;
    }
    public SimpleStringProperty getVariableNameProperty() {
        return new SimpleStringProperty(variableName);
    }
    
    public String getTypeName() {
        return typeName;
    }
    
    public SimpleStringProperty getVariableTypeProperty() {
        return new SimpleStringProperty(typeName);
    }
    
    public String isStatic() {
        return isStatic;
    }
    public SimpleStringProperty isStaticProperty() {
        return new SimpleStringProperty(isStatic);
    }
    
    public String getAccessType() {
        return accessType;
    }
    
    public SimpleStringProperty getAccessTypeProperty() {
        return new SimpleStringProperty(accessType);
    }
    
    public void setVariableName(String variableName) {
        this.variableName = variableName;
    }
    
    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }
    
    public void setStatic(String isStatic) {
        this.isStatic = isStatic;
    }
    
    public void setAccessType(String accessType) {
        this.accessType = accessType;
    }
    
    public String exportString() {
        return accessType + (isStatic.equals("ture") ? " static " : " ") + typeName + " " + variableName;
    }
    
    @Override
    public String toString() {
        String variableString = "";
        switch (accessType) {
            case "public":
                variableString += "+ ";
                break;
            case "private":
                variableString += "― ";
                break;
            case "protected":
                variableString += "# ";
                break;
        }
        if(isStatic.equals("true")) {
            variableString += "$ ";
        }
        variableString += variableName + " : " + typeName;
        return variableString;
    }
}
