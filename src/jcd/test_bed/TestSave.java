/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jcd.test_bed;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import jcd.Diagram;
import jcd.Method;
import jcd.Variable;
import jcd.file.FileManager;

/**
 *
 * @author Saeid
 */
public class TestSave {
    static ObservableList<Diagram> diagramData;
    static FileManager fileManager;
    
    public static void saveDesignOne(){
        fileManager = new FileManager();
        diagramData = FXCollections.observableArrayList();
        for (int i = 0; i < 5; i++) {
            if (i == 0) {
                ObservableList<Variable> variableData = FXCollections.observableArrayList();
                ObservableList<Method> methodData = FXCollections.observableArrayList();
                ArrayList<List<Double>> pointData = new ArrayList<>();
                variableData.addAll(
                        new Variable("windows", "Stage", false, "private"),
                        new Variable("appPane", "BorderPane", false, "private"),
                        new Variable("topPane", "FlowPane", false, "private"),
                        new Variable("startButton", "Button", false, "private"),
                        new Variable("textArea", "TextArea", false, "private"));
                methodData.addAll(
                        new Method("start", "void", false, false, "public", "Stage primaryStage", "", ""),
                        new Method("startWork", "void", false, false, "public", "", "", ""),
                        new Method("pauseWork", "void", false, false, "public", "", "", ""),
                        new Method("doWork", "boolean", false, false, "public", "", "", ""),
                        new Method("appendText", "void", false, false, "public", "String textToAppend", "", ""));
                pointData.add(Arrays.asList(1.0, 2.0, 3.0, 4.0));
                pointData.add(Arrays.asList(1.0, 2.0, 3.0, 4.0));
                diagramData.add(new Diagram(1, 529.0, 237.0, -1, -1, "ThreadExample", "", false, false, variableData, methodData, 2, pointData));
                
                variableData = FXCollections.observableArrayList();
                methodData = FXCollections.observableArrayList();
                pointData = new ArrayList<>();
                methodData.addAll(
                        new Method("start", "void", false, true, "", "Stage primaryStage", "", ""));
                diagramData.add(new Diagram(2, 516.0, 75.0, -1, -1, "Application", "", false, true, variableData, methodData, -1, pointData));
                
                variableData = FXCollections.observableArrayList();
                methodData = FXCollections.observableArrayList();
                pointData = new ArrayList<>();
                diagramData.add(new Diagram(3, 279.0, 699.0, -1, -1, "Stage", "", false, false, variableData, methodData, -1, pointData));
                
                variableData = FXCollections.observableArrayList();
                methodData = FXCollections.observableArrayList();
                pointData = new ArrayList<>();
                diagramData.add(new Diagram(4, 415.0, 699.0, -1, -1, "BorderPane", "", false, false, variableData, methodData, -1, pointData));
                
                variableData = FXCollections.observableArrayList();
                methodData = FXCollections.observableArrayList();
                pointData = new ArrayList<>();
                diagramData.add(new Diagram(5, 552.0, 698.0, -1, -1, "FlowPane", "", false, false, variableData, methodData, -1, pointData));
                
                variableData = FXCollections.observableArrayList();
                methodData = FXCollections.observableArrayList();
                pointData = new ArrayList<>();
                diagramData.add(new Diagram(6, 693.0, 700.0, -1, -1, "Button", "", false, false, variableData, methodData, -1, pointData));
                
                variableData = FXCollections.observableArrayList();
                methodData = FXCollections.observableArrayList();
                pointData = new ArrayList<>();
                diagramData.add(new Diagram(7, 847.0, 693.0, -1, -1, "TextArea", "", false, false, variableData, methodData, -1, pointData));
            }
            else if (i == 1) {
                ObservableList<Variable> variableData = FXCollections.observableArrayList();
                ObservableList<Method> methodData = FXCollections.observableArrayList();
                ArrayList<List<Double>> pointData = new ArrayList<>();
                variableData.addAll(
                        new Variable("app", "ThreadExample", false, "private"),
                        new Variable("counter", "int", false, "private"));
                methodData.addAll(
                        new Method("CounterTask", "", false, false, "public", "ThreadExample initApp", "", ""),
                        new Method("call", "void", true, false, "protected", "", "", ""));
                diagramData.add(new Diagram(8, 199.0, 225.0, -1, -1, "CounterTask", "", false, false, variableData, methodData, 9, pointData));
                
                variableData = FXCollections.observableArrayList();
                methodData = FXCollections.observableArrayList();
                pointData = new ArrayList<>();
                diagramData.add(new Diagram(9, 65.0, 267.0, -1, -1, "Task", "", false, false, variableData, methodData, -1, pointData));
            }               
            else if (i == 2) {
                ObservableList<Variable> variableData = FXCollections.observableArrayList();
                ObservableList<Method> methodData = FXCollections.observableArrayList();
                ArrayList<List<Double>> pointData = new ArrayList<>();
                variableData.addAll(
                        new Variable("app", "ThreadExample", false, "private"),
                        new Variable("now", "int", false, "private"));
                methodData.addAll(
                        new Method("DateTask", "", false, false, "public", "ThreadExample initApp", "", ""),
                        new Method("call", "void", true, false, "protected", "", "", ""));
                diagramData.add(new Diagram(10, 208.0, 375.0, -1, -1, "DateTask", "", false, false, variableData, methodData, 9, pointData));
                
                variableData = FXCollections.observableArrayList();
                methodData = FXCollections.observableArrayList();
                pointData = new ArrayList<>();
                diagramData.add(new Diagram(11, 68.0, 393.0, -1, -1, "Date", "", false, false, variableData, methodData, -1, pointData));
            }
            else if (i == 3) {
                ObservableList<Variable> variableData = FXCollections.observableArrayList();
                ObservableList<Method> methodData = FXCollections.observableArrayList();
                ArrayList<List<Double>> pointData = new ArrayList<>();
                variableData.addAll(
                        new Variable("app", "ThreadExample", false, "private"));
                methodData.addAll(
                        new Method("PauseHandler", "", false, false, "public", "ThreadExample initApp", "", ""),
                        new Method("handle", "void", true, false, "public", "Event event", "", ""));
                diagramData.add(new Diagram(12, 829.0, 247.0, -1, -1, "PauseHandler", "", false, false, variableData, methodData, -1, pointData));
            }
            
            else if (i == 4) {
                ObservableList<Variable> variableData = FXCollections.observableArrayList();
                ObservableList<Method> methodData = FXCollections.observableArrayList();
                ArrayList<List<Double>> pointData = new ArrayList<>();
                variableData.addAll(
                        new Variable("app", "ThreadExample", false, "private"));
                methodData.addAll(
                        new Method("StartHandler", "", false, false, "public", "ThreadExample initApp", "", ""),
                        new Method("handle", "void", true, false, "public", "Event event", "", ""));
                diagramData.add(new Diagram(13, 826.0, 396.0, -1, -1, "StartHandler", "", false, false, variableData, methodData, -1, pointData));
            }      
        }
    }
    
    public static void saveDesignTwo(){
        fileManager = new FileManager();
        diagramData = FXCollections.observableArrayList();
        for (int i = 0; i < 5; i++) {
            if (i == 0) {
                ObservableList<Variable> variableData = FXCollections.observableArrayList();
                ObservableList<Method> methodData = FXCollections.observableArrayList();
                ArrayList<List<Double>> pointData = new ArrayList<>();
                variableData.addAll(
                        new Variable("windows", "Stage", false, "private"),
                        new Variable("appPane", "BorderPane", false, "private"),
                        new Variable("topPane", "FlowPane", false, "private"),
                        new Variable("startButton", "Button", false, "private"),
                        new Variable("textArea", "TextArea", false, "private"));
                methodData.addAll(
                        new Method("start", "void", false, false, "public", "Stage primaryStage", "", ""),
                        new Method("startWork", "void", false, false, "public", "", "", ""),
                        new Method("pauseWork", "void", false, false, "public", "", "", ""),
                        new Method("doWork", "boolean", false, false, "public", "", "", ""),
                        new Method("appendText", "void", false, false, "public", "String textToAppend", "", ""));
                pointData.add(Arrays.asList(1.0, 2.0, 3.0, 4.0));
                pointData.add(Arrays.asList(1.0, 2.0, 3.0, 4.0));
                diagramData.add(new Diagram(1, 529.0, 237.0, -1, -1, "ThreadExample", "", false, false, variableData, methodData, 2, pointData));
                
                variableData = FXCollections.observableArrayList();
                methodData = FXCollections.observableArrayList();
                pointData = new ArrayList<>();
                methodData.addAll(
                        new Method("start", "void", false, true, "", "Stage primaryStage", "", ""));
                diagramData.add(new Diagram(2, 516.0, 75.0, -1, -1, "Application", "", false, true, variableData, methodData, -1, pointData));
                
                variableData = FXCollections.observableArrayList();
                methodData = FXCollections.observableArrayList();
                pointData = new ArrayList<>();
                diagramData.add(new Diagram(3, 279.0, 699.0, -1, -1, "Stage", "", false, false, variableData, methodData, -1, pointData));
                
                variableData = FXCollections.observableArrayList();
                methodData = FXCollections.observableArrayList();
                pointData = new ArrayList<>();
                diagramData.add(new Diagram(4, 415.0, 699.0, -1, -1, "BorderPane", "", false, false, variableData, methodData, -1, pointData));
                
                variableData = FXCollections.observableArrayList();
                methodData = FXCollections.observableArrayList();
                pointData = new ArrayList<>();
                diagramData.add(new Diagram(5, 552.0, 698.0, -1, -1, "FlowPane", "", false, false, variableData, methodData, -1, pointData));
                
                variableData = FXCollections.observableArrayList();
                methodData = FXCollections.observableArrayList();
                pointData = new ArrayList<>();
                diagramData.add(new Diagram(6, 693.0, 700.0, -1, -1, "Button", "", false, false, variableData, methodData, -1, pointData));
                
                variableData = FXCollections.observableArrayList();
                methodData = FXCollections.observableArrayList();
                pointData = new ArrayList<>();
                diagramData.add(new Diagram(7, 847.0, 693.0, -1, -1, "TextArea", "", false, false, variableData, methodData, -1, pointData));
            }
            else if (i == 1) {
                ObservableList<Variable> variableData = FXCollections.observableArrayList();
                ObservableList<Method> methodData = FXCollections.observableArrayList();
                ArrayList<List<Double>> pointData = new ArrayList<>();
                variableData.addAll(
                        new Variable("app", "ThreadExample", false, "private"),
                        new Variable("counter", "int", false, "private"));
                methodData.addAll(
                        new Method("CounterTask", "", false, false, "public", "ThreadExample initApp", "", ""),
                        new Method("call", "void", true, false, "protected", "", "", ""));
                diagramData.add(new Diagram(8, 199.0, 225.0, -1, -1, "CounterTask", "", false, false, variableData, methodData, 9, pointData));
                
                variableData = FXCollections.observableArrayList();
                methodData = FXCollections.observableArrayList();
                pointData = new ArrayList<>();
                diagramData.add(new Diagram(9, 65.0, 267.0, -1, -1, "Task", "", false, false, variableData, methodData, -1, pointData));
            }               
            else if (i == 2) {
                ObservableList<Variable> variableData = FXCollections.observableArrayList();
                ObservableList<Method> methodData = FXCollections.observableArrayList();
                ArrayList<List<Double>> pointData = new ArrayList<>();
                variableData.addAll(
                        new Variable("app", "ThreadExample", false, "private"),
                        new Variable("now", "int", false, "private"));
                methodData.addAll(
                        new Method("DateTask", "", false, false, "public", "ThreadExample initApp", "", ""),
                        new Method("call", "void", true, false, "protected", "", "", ""));
                diagramData.add(new Diagram(10, 208.0, 375.0, -1, -1, "DateTask", "", false, false, variableData, methodData, 9, pointData));
                
                variableData = FXCollections.observableArrayList();
                methodData = FXCollections.observableArrayList();
                pointData = new ArrayList<>();
                diagramData.add(new Diagram(11, 68.0, 393.0, -1, -1, "Date", "", false, false, variableData, methodData, -1, pointData));
            }
            else if (i == 3) {
                ObservableList<Variable> variableData = FXCollections.observableArrayList();
                ObservableList<Method> methodData = FXCollections.observableArrayList();
                ArrayList<List<Double>> pointData = new ArrayList<>();
                variableData.addAll(
                        new Variable("app", "ThreadExample", false, "private"));
                methodData.addAll(
                        new Method("PauseHandler", "", false, false, "public", "ThreadExample initApp", "", ""),
                        new Method("handle", "void", true, false, "public", "Event event", "", ""));
                diagramData.add(new Diagram(12, 829.0, 247.0, -1, -1, "PauseHandler", "", false, false, variableData, methodData, -1, pointData));
            }
            
            else if (i == 4) {
                ObservableList<Variable> variableData = FXCollections.observableArrayList();
                ObservableList<Method> methodData = FXCollections.observableArrayList();
                ArrayList<List<Double>> pointData = new ArrayList<>();
                variableData.addAll(
                        new Variable("app", "ThreadExample", false, "private"));
                methodData.addAll(
                        new Method("StartHandler", "", false, false, "public", "ThreadExample initApp", "", ""),
                        new Method("handle", "void", true, false, "public", "Event event", "", ""));
                diagramData.add(new Diagram(13, 826.0, 396.0, -1, -1, "StartHandler", "", false, false, variableData, methodData, -1, pointData));
                
                variableData = FXCollections.observableArrayList();
                methodData = FXCollections.observableArrayList();
                pointData = new ArrayList<>();
                diagramData.add(new Diagram(14, 144.0, 63.0, -1, -1, "newAbstractClass", "", false, true, variableData, methodData, -1, pointData));  
            }      
        }
    }
    
    public static void saveDesignThree(){
        fileManager = new FileManager();
        diagramData = FXCollections.observableArrayList();
        for (int i = 0; i < 5; i++) {
            if (i == 0) {
                ObservableList<Variable> variableData = FXCollections.observableArrayList();
                ObservableList<Method> methodData = FXCollections.observableArrayList();
                ArrayList<List<Double>> pointData = new ArrayList<>();
                variableData.addAll(
                        new Variable("windows", "Stage", false, "private"),
                        new Variable("appPane", "BorderPane", false, "private"),
                        new Variable("topPane", "FlowPane", false, "private"),
                        new Variable("startButton", "Button", false, "private"),
                        new Variable("textArea", "TextArea", false, "private"));
                methodData.addAll(
                        new Method("start", "void", false, false, "public", "Stage primaryStage", "", ""),
                        new Method("startWork", "void", false, false, "public", "", "", ""),
                        new Method("pauseWork", "void", false, false, "public", "", "", ""),
                        new Method("doWork", "boolean", false, false, "public", "", "", ""),
                        new Method("appendText", "void", false, false, "public", "String textToAppend", "", ""));
                pointData.add(Arrays.asList(1.0, 2.0, 3.0, 4.0));
                pointData.add(Arrays.asList(1.0, 2.0, 3.0, 4.0));
                diagramData.add(new Diagram(1, 529.0, 237.0, -1, -1, "ThreadExample", "", false, false, variableData, methodData, 2, pointData));
                
                variableData = FXCollections.observableArrayList();
                methodData = FXCollections.observableArrayList();
                pointData = new ArrayList<>();
                methodData.addAll(
                        new Method("start", "void", false, true, "", "Stage primaryStage", "", ""));
                diagramData.add(new Diagram(2, 516.0, 75.0, -1, -1, "Application", "", false, true, variableData, methodData, -1, pointData));
                
                variableData = FXCollections.observableArrayList();
                methodData = FXCollections.observableArrayList();
                pointData = new ArrayList<>();
                diagramData.add(new Diagram(3, 279.0, 699.0, -1, -1, "Stage", "", false, false, variableData, methodData, -1, pointData));
                
                variableData = FXCollections.observableArrayList();
                methodData = FXCollections.observableArrayList();
                pointData = new ArrayList<>();
                diagramData.add(new Diagram(4, 415.0, 699.0, -1, -1, "BorderPane", "", false, false, variableData, methodData, -1, pointData));
                
                variableData = FXCollections.observableArrayList();
                methodData = FXCollections.observableArrayList();
                pointData = new ArrayList<>();
                diagramData.add(new Diagram(5, 552.0, 698.0, -1, -1, "FlowPane", "", false, false, variableData, methodData, -1, pointData));
                
                variableData = FXCollections.observableArrayList();
                methodData = FXCollections.observableArrayList();
                pointData = new ArrayList<>();
                diagramData.add(new Diagram(6, 693.0, 700.0, -1, -1, "Button", "", false, false, variableData, methodData, -1, pointData));
                
                variableData = FXCollections.observableArrayList();
                methodData = FXCollections.observableArrayList();
                pointData = new ArrayList<>();
                diagramData.add(new Diagram(7, 847.0, 693.0, -1, -1, "TextArea", "", false, false, variableData, methodData, -1, pointData));
            }
            else if (i == 1) {
                ObservableList<Variable> variableData = FXCollections.observableArrayList();
                ObservableList<Method> methodData = FXCollections.observableArrayList();
                ArrayList<List<Double>> pointData = new ArrayList<>();
                variableData.addAll(
                        new Variable("app", "ThreadExample", false, "private"),
                        new Variable("counter", "int", false, "private"));
                methodData.addAll(
                        new Method("CounterTask", "", false, false, "public", "ThreadExample initApp", "", ""),
                        new Method("call", "void", true, false, "protected", "", "", ""));
                diagramData.add(new Diagram(8, 199.0, 225.0, -1, -1, "CounterTask", "", false, false, variableData, methodData, 9, pointData));
                
                variableData = FXCollections.observableArrayList();
                methodData = FXCollections.observableArrayList();
                pointData = new ArrayList<>();
                diagramData.add(new Diagram(9, 65.0, 267.0, -1, -1, "Task", "", false, false, variableData, methodData, -1, pointData));
            }               
            else if (i == 2) {
                ObservableList<Variable> variableData = FXCollections.observableArrayList();
                ObservableList<Method> methodData = FXCollections.observableArrayList();
                ArrayList<List<Double>> pointData = new ArrayList<>();
                variableData.addAll(
                        new Variable("app", "ThreadExample", false, "private"),
                        new Variable("now", "int", false, "private"));
                methodData.addAll(
                        new Method("DateTask", "", false, false, "public", "ThreadExample initApp", "", ""),
                        new Method("call", "void", true, false, "protected", "", "", ""));
                diagramData.add(new Diagram(10, 208.0, 375.0, -1, -1, "DateTask", "", false, false, variableData, methodData, 9, pointData));
                
                variableData = FXCollections.observableArrayList();
                methodData = FXCollections.observableArrayList();
                pointData = new ArrayList<>();
                diagramData.add(new Diagram(11, 68.0, 393.0, -1, -1, "Date", "", false, false, variableData, methodData, -1, pointData));
            }
            else if (i == 3) {
                ObservableList<Variable> variableData = FXCollections.observableArrayList();
                ObservableList<Method> methodData = FXCollections.observableArrayList();
                ArrayList<List<Double>> pointData = new ArrayList<>();
                variableData.addAll(
                        new Variable("app", "ThreadExample", false, "private"));
                methodData.addAll(
                        new Method("PauseHandler", "", false, false, "public", "ThreadExample initApp", "", ""),
                        new Method("handle", "void", true, false, "public", "Event event", "", ""));
                diagramData.add(new Diagram(12, 829.0, 247.0, -1, -1, "PauseHandler", "", false, false, variableData, methodData, -1, pointData));
            }
            
            else if (i == 4) {
                ObservableList<Variable> variableData = FXCollections.observableArrayList();
                ObservableList<Method> methodData = FXCollections.observableArrayList();
                ArrayList<List<Double>> pointData = new ArrayList<>();
                variableData.addAll(
                        new Variable("app", "ThreadExample", false, "private"));
                methodData.addAll(
                        new Method("StartHandler", "", false, false, "public", "ThreadExample initApp", "", ""),
                        new Method("handle", "void", true, false, "public", "Event event", "", ""));
                diagramData.add(new Diagram(13, 826.0, 396.0, -1, -1, "StartHandler", "", false, false, variableData, methodData, -1, pointData));
                variableData = FXCollections.observableArrayList();
                methodData = FXCollections.observableArrayList();
                pointData = new ArrayList<>();
                diagramData.add(new Diagram(14, 144.0, 63.0, -1, -1, "NewInterface", "", true, false, variableData, methodData, -1, pointData));
            }      
        }
    }
    
    public static void main(String[] args){
        saveDesignOne();
        try {
            fileManager.saveDataTest(diagramData, "work/DesignSaveTestOne.json");
        } catch (IOException ex) {
            Logger.getLogger(TestSave.class.getName()).log(Level.SEVERE, null, ex);
        }
        saveDesignTwo();
        try {
            fileManager.saveDataTest(diagramData, "work/DesignSaveTestTwo.json");
        } catch (IOException ex) {
            Logger.getLogger(TestSave.class.getName()).log(Level.SEVERE, null, ex);
        }
        saveDesignThree();
        try {
            fileManager.saveDataTest(diagramData, "work/DesignSaveTestThree.json");
        } catch (IOException ex) {
            Logger.getLogger(TestSave.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
