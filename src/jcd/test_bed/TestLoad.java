/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jcd.test_bed;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.ObservableList;
import jcd.Diagram;
import jcd.file.FileManager;

/**
 *
 * @author Saeid
 */
public class TestLoad {
    static ObservableList<Diagram> diagramData;
    static FileManager fileManager;
    
    public static String loadDesignOne(String path) {
        String designData = "\n\nDesign One\n\n";
        fileManager = new FileManager();
        try {
            diagramData = fileManager.loadDataTest(path);
        } catch (IOException ex) {
            Logger.getLogger(TestLoad.class.getName()).log(Level.SEVERE, null, ex);
        }   
        for(Diagram diagram: diagramData){
            designData += diagram.toString();
        }
        return designData;
    }
    
    public static String loadDesignTwo(String path) {
        String designData = "\n\nDesign Two\n\n";
        fileManager = new FileManager();
        try {
            diagramData = fileManager.loadDataTest(path);
        } catch (IOException ex) {
            Logger.getLogger(TestLoad.class.getName()).log(Level.SEVERE, null, ex);
        }
        for(Diagram diagram: diagramData){
            designData += diagram.toString();
        }
        return designData;
    }
    
    public static String loadDesignThree(String path) {
        String designData = "\n\nDesign Three\n\n";
        fileManager = new FileManager();
        try {
            diagramData = fileManager.loadDataTest(path);
        } catch (IOException ex) {
            Logger.getLogger(TestLoad.class.getName()).log(Level.SEVERE, null, ex);
        }
       for(Diagram diagram: diagramData){
            designData += diagram.toString();
        }
        return designData;
    }
    
    public static void main(String[] args){
        System.out.println(loadDesignOne("work/DesignSaveTestOne.json"));
        System.out.println(loadDesignTwo("work/DesignSaveTestTwo.json"));
        System.out.println(loadDesignThree("work/DesignSaveTestThree.json"));
    }
}
