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
public class Method {
    String methodName;
    String returnType;
    String isStatic;
    String isAbstract;
    String accessType;
    String argumentOne;
    String argumentTwo;
    String argumentThree;
    
    public Method(String methodName, String returnType, String isStatic, String isAbstract,
            String accessType, String argumentOne, String argumentTwo, String argumentThree) {
        this.methodName = methodName;
        this.returnType = returnType;
        this.isStatic = isStatic;
        this.isAbstract = isAbstract;
        this.accessType = accessType;
        this.argumentOne = argumentOne;
        this.argumentTwo = argumentTwo;
        this.argumentThree = argumentThree;
    }
    
    // Copy constructor
    public Method(Method method) {
        methodName = method.getMethodName();
        returnType = method.getReturnType();
        isStatic = method.isStatic();
        isAbstract = method.isAbstract();
        accessType = method.getAccessType();
        argumentOne = method.getArgumentOne();
        argumentTwo = method.getArgumentTwo();
        argumentThree = method.getArgumentThree();
    }
    
    public String getMethodName() {
        return methodName;
    }
    
    public SimpleStringProperty getMethodNameProperty() {
        return new SimpleStringProperty(methodName);
    }
    
    public String getReturnType() {
        return returnType;
    }
    
    public SimpleStringProperty getReturnTypeProperty() {
        return new SimpleStringProperty(returnType);
    }
    
    public String isStatic() {
        return isStatic;
    }
    
    public SimpleStringProperty isStaticProperty() {
        return new SimpleStringProperty(isStatic);
    }
    
    public String isAbstract() {
        return isAbstract;
    }
    
    public SimpleStringProperty isAbstractProperty() {
        return new SimpleStringProperty(isAbstract);
    }
    
    public String getAccessType() {
        return accessType;
    }
    
    public SimpleStringProperty getAccessTypeProperty() {
        return new SimpleStringProperty(accessType);
    }
    
    public String getArgumentOne() {
        return argumentOne;
    }
    
    public SimpleStringProperty getArgumentOneProperty() {
        return new SimpleStringProperty(argumentOne);
    }
    
    public String getArgumentTwo() {
        return argumentTwo;
    }
    
    public SimpleStringProperty getArgumentTwoProperty() {
        return new SimpleStringProperty(argumentTwo);
    }
    
    public String getArgumentThree() {
        return argumentThree;
    }
    
    public SimpleStringProperty getArgumentThreeProperty() {
        return new SimpleStringProperty(argumentThree);
    }
    
    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }
    
    public void setReturnType(String returnType) {
        this.returnType = returnType;
    }
    
    public void setStatic(String isStatic) {
        this.isStatic = isStatic;
    }
    
    public void setAbstract(String isAbstract) {
        this.isAbstract = isAbstract;
    }
    
    public void setAccessType(String accessType) {
        this.accessType = accessType;
    }
    
    public void setArgumentOne(String argumentOne) {
        this.argumentOne = argumentOne;
    }
    
    public void setArgumentTwo(String argumentTwo) {
        this.argumentTwo = argumentTwo;
    }
    
    public void setArgumentThree(String argumentThree) {
        this.argumentThree = argumentThree;
    }
    
    public String exportString() {
        return accessType + (isStatic.equals("true") ? " static " : ("" + (isAbstract.equals("true") ? " abstract " : ""))) + " " + returnType + " " + 
                methodName + "(" + argumentOne + (argumentTwo.equals("") ? "" : ", " + argumentTwo) + (argumentThree.equals("") ? "" : ", " + argumentThree) + ")" ;
    }
    
    
    @Override
    public String toString() {
        String methodString = "";
        switch(accessType) {
            case "public":
                methodString += "+ ";
                break;
            case "private":
                methodString += "― ";
                break;
            case "protected":
                methodString += "# ";
                break;
        }
        if(isStatic.equals("true")) {
            methodString += "$ ";
        }
        else if(isAbstract.equals("true")) {
            methodString += "{abstract} ";
        }
        methodString += methodName + " (";
        if(!argumentOne.isEmpty()) {
            String argumentArray[] = argumentOne.split(" ");
            if(argumentArray.length == 2)
                methodString += argumentArray[1] + " : ";
            methodString += argumentArray[0];
        }
        if(!argumentTwo.isEmpty()) {
            methodString += ", ";
            String argumentArray[] = argumentTwo.split(" ");
            if(argumentArray.length == 2)
                methodString += argumentArray[1] + " : ";
            methodString += argumentArray[0];
        }
        if(!argumentThree.isEmpty()) {
            methodString += ", ";
            String argumentArray[] = argumentThree.split(" ");
            if(argumentArray.length == 2)
                methodString += argumentArray[1] + " : ";
            methodString += argumentArray[0];
        }
        methodString += ")";
        if(!returnType.isEmpty()) {
            methodString +=  " : " + returnType;
        }
        
        return methodString;
    }
}
