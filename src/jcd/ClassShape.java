/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jcd;

import javafx.scene.Parent;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;

/**
 *
 * @author Saeid
 */
public class ClassShape extends Parent {
    Rectangle nameSection;
    Rectangle variableSection;
    Rectangle methodSection;
    
    public ClassShape() {
        nameSection = new Rectangle(10, 10);
        variableSection = new Rectangle(10, 10);
        methodSection = new Rectangle(10, 10);
    }
    
    public com.sun.javafx.geom.Shape impl_configShape() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
