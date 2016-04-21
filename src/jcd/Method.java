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
public class Method {
    String methodName;
    String returnType;
    boolean isStatic;
    boolean isAbstract;
    String accessType;
    String argumentOne;
    String argumentTwo;
    String argumentThree;
    
    public Method(String methodName, String returnType, boolean isStatic, boolean isAbstract, String accessType, String argumentOne, String argumentTwo, String argumentThree) {
        this.methodName = methodName;
        this.returnType = returnType;
        this.isStatic = isStatic;
        this.isAbstract = isAbstract;
        this.accessType = accessType;
        this.argumentOne = argumentOne;
        this.argumentTwo = argumentTwo;
        this.argumentThree = argumentThree;
    }
    
    public String getMethodName() {
        return methodName;
    }
    
    public String getReturnType() {
        return returnType;
    }
    
    public boolean getStatic() {
        return isStatic;
    }
    
    public boolean getAbstract() {
        return isAbstract;
    }
    
    public String getAccessType() {
        return accessType;
    }
    
    public String getArgumentOne() {
        return argumentOne;
    }
    
    public String getArgumentTwo() {
        return argumentTwo;
    }
    
    public String getArgumentThree() {
        return argumentThree;
    }
    
    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }
    
    public void setReturnType(String returnType) {
        this.returnType = returnType;
    }
    
    public void setStatic(boolean isStatic) {
        this.isStatic = isStatic;
    }
    
    public void setAbstract(boolean isAbstract) {
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
    
    public void setArgumentThree(String methodName) {
        this.argumentThree = argumentThree;
    }
    
    @Override
    public String toString() {
        return accessType + (isStatic ? " static " : (" " + (isAbstract ? " abstract " : " "))) + returnType + " " + methodName + "(" + argumentOne + (argumentTwo.equals("") ? "" : ", " + argumentTwo) + (argumentThree.equals("") ? "" : ", " + argumentThree + ")");
    }
}
