/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jcd;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 *
 * @author Saeid
 */
public class Method {
    String methodName;
    final ObservableList<String> typeOption = FXCollections.observableArrayList("int", "double", "String");
    boolean isStatic;
    final ObservableList<String> accessOption = FXCollections.observableArrayList("private", "public");
    
}
