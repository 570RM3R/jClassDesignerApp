/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jcd;

import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;

/**
 *
 * @author Saeid
 */
public class Grid extends Group{
    public Grid(double width, double height) {
        for (int i = 0; i < width; i+=10) {
            Line verticalLine = new Line(i, 0, i, height);
            verticalLine.setStroke(Color.LIGHTGRAY);
            getChildren().add(verticalLine);
        }
        for (int i = 0; i < height; i+=10) {
            Line horizontalLine = new Line(0, i, width, i);
            horizontalLine.setStroke(Color.LIGHTGRAY);
            getChildren().add(horizontalLine);
        }
    }
}
