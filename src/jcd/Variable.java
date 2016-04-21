/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jcd;

/**
 *
 * @author Saeid
 */
public class Variable {
    String variableName;
    String typeName;
    boolean isStatic;
    String accessType;
    
    public Variable(String variableName, String typeName, boolean isStatic, String accessType) {
        this.variableName = variableName;
        this.typeName = typeName;
        this.isStatic = isStatic;
        this.accessType = accessType;
    }
    
    public String getVariableName() {
        return variableName;
    }
    
    public String getTypeName() {
        return typeName;
    }
    
    public boolean getStatic() {
        return isStatic;
    }
    
    public String getAccessType() {
        return accessType;
    }
    
    public void setVariableName(String variableName) {
        this.variableName = variableName;
    }
    
    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }
    
    public void setStatic(boolean isStatic) {
        this.isStatic = isStatic;
    }
    
    public void setAccessType(String accessType) {
        this.accessType = accessType;
    }
    
    @Override
    public String toString() {
        return accessType + (isStatic ? " static " : " ") + typeName + " " + variableName;
    }
}
