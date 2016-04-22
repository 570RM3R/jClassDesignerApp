/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jcd;

import javafx.scene.Parent;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Polyline;

/**
 *
 * @author Saeid
 */
public class Connector extends Parent{
    Polyline line;
    Polygon head;
    
    public Connector(int type, double startX, double startY, double endX, double endY) {
        line = new Polyline(0.0, 0.0, 20.0, 10.0, 10.0, 20.0);
        if(type == 1) {
            head = new Polygon(0.0, 0.0, 20.0, 10.0, 10.0, 20.0);
            head.setFill(null);
        }
        else if(type == 2) {
            head = new Polygon(0.0, 0.0, 20.0, 10.0, 10.0, 20.0);
            head.setFill(null);
        }
        else if(type == 3) {
            head = new Polygon(0.0, 0.0, 20.0, 10.0, 10.0, 20.0);
            head.setFill(null);
        }
        getChildren().addAll(line, head);
    }
    
}
