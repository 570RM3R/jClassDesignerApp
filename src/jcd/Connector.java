/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jcd;

import java.util.ArrayList;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Polyline;
import javafx.scene.shape.StrokeType;

/**
 *
 * @author Saeid
 */
public class Connector extends Group{
    static int idCounter = 0;
    int connectorId;
    int type;
    int sourceId;
    int destinationId;
    double startX;
    double startY;
    double endX;
    double endY;
    ArrayList<Double> anchorData;
    Anchor base;
    Head head;
    double headLayoutX;
    double headLayoutY;
    
    public Connector(int connectorId, int type, int sourceId, int destionationId, double startX, double startY, 
            double endX, double endY, double headLayoutX, double headLayoutY, ArrayList<Double> anchorData) {
        this.connectorId = connectorId == -1 ? Connector.idCounter : connectorId;
        this.type = type;
        this.sourceId = sourceId;
        this.destinationId = destionationId;
        this.startX = startX;
        this.startY = startY;
        this.endX = endX;
        this.endY = endY;
        this.anchorData = anchorData;
        double horizontalDistance = Math.abs(startX - endX);
        double verticalDistance = Math.abs(startY - endY);
        switch(type / 100) {
            // Destination is straight top
            case 1:
                // Six lines straight top
                createLines(true, (verticalDistance - 10) / 7, (type / 10) % 10 == 1, 7, 1, true, (type % 100) % 10);
            break;
            // Destination is right top
            case 2:
                // Two lines straight right
                createLines(true, (horizontalDistance - 10) / 4, (type / 10) % 10 == 1, 2, 2, false, (type % 100) % 10);
                // Three lines straight top
                createLines(false, verticalDistance / 3, (type / 10) % 10 == 1, 3, 1, false, (type % 100) % 10);
                // Two lines straight right
                createLines(false, (horizontalDistance - 10) / 4, (type / 10) % 10 == 1, 2, 2, true, (type % 100) % 10);
            break;
            // Destination is straight right
            case 3:
                // Six lines straight right
                createLines(true, (horizontalDistance - 10) / 7, (type / 10) % 10 == 1, 7, 2, true, (type % 100) % 10);
            break;
            // Destination is right bottom
            case 4:
                // Two lines straight right
                createLines(true, (horizontalDistance - 10) / 4, (type / 10) % 10 == 1, 2, 2, false, (type % 100) % 10);
                // Three lines straight bottom
                createLines(false, verticalDistance / 3, (type / 10) % 10 == 1, 3, 3, false, (type % 100) % 10);
                // Two lines straight right
                createLines(false, (horizontalDistance - 10) / 4, (type / 10) % 10 == 1, 2, 2, true, (type % 100) % 10);
            break;
            // Destination is straight bottom
            case 5:
                // Six lines straight bottom
                createLines(true, (verticalDistance - 10) / 7, (type / 10) % 10 == 1, 7, 3, true, (type % 100) % 10);
            break;
            // Destination is bottom left
            case 6:
                // Two lines straight left
                createLines(true, (horizontalDistance - 10) / 4, (type / 10) % 10 == 1, 2, 4, false, (type % 100) % 10);
                // Three lines straight bottom
                createLines(false, verticalDistance / 3, (type / 10) % 10 == 1, 3, 3, false, (type % 100) % 10);
                // Two lines straight left
                createLines(false, (horizontalDistance - 10) / 4, (type / 10) % 10 == 1, 2, 4, true, (type % 100) % 10);
            break;
            // Destination is straight left
            case 7:
                // Six lines straight left
                createLines(true, (horizontalDistance - 10) / 7, (type / 10) % 10 == 1, 7, 4, true, (type % 100) % 10);
            break;
            // Destination is top left
            case 8:
                // Two lines straight left
                createLines(true, (horizontalDistance - 10) / 4, (type / 10) % 10 == 1, 2, 4, false, (type % 100) % 10);
                // Three lines straight top
                createLines(false, verticalDistance / 3, (type / 10) % 10 == 1, 3, 1, false, (type % 100) % 10);
                // Two lines straight left
                createLines(false, (horizontalDistance - 10) / 4, (type / 10) % 10 == 1, 2, 4, true, (type % 100) % 10);
            break;
        }
        this.headLayoutX = headLayoutX == -1 ? getLayoutX() : headLayoutX;
        this.headLayoutY  = headLayoutY == -1 ? getLayoutY() : headLayoutY;
        idCounter++;
    }
    
    // Deep copy constructor
    public Connector(Connector connector) {
        connectorId = connector.getConnectorId();
        type = connector.getType();
        sourceId = connector.getSourceId();
        destinationId = connector.getDestinationId();
        startX = connector.getStartX();
        startY = connector.getStartY();
        endX = connector.getEndX();
        endY = connector.getEndY();
        anchorData = (ArrayList<Double>)connector.getAnchorData().clone();
        double horizontalDistance = Math.abs(startX - endX);
        double verticalDistance = Math.abs(startY - endY);
        switch(type / 100) {
            // Destination is straight top
            case 1:
                // Six lines straight top
                createLines(true, (verticalDistance - 10) / 7, (type / 10) % 10 == 1, 7, 1, true, (type % 100) % 10);
            break;
            // Destination is right top
            case 2:
                // Two lines straight right
                createLines(true, (horizontalDistance - 10) / 4, (type / 10) % 10 == 1, 2, 2, false, (type % 100) % 10);
                // Three lines straight top
                createLines(false, verticalDistance / 3, (type / 10) % 10 == 1, 3, 1, false, (type % 100) % 10);
                // Two lines straight right
                createLines(false, (horizontalDistance - 10) / 4, (type / 10) % 10 == 1, 2, 2, true, (type % 100) % 10);
            break;
            // Destination is straight right
            case 3:
                // Six lines straight right
                createLines(true, (horizontalDistance - 10) / 7, (type / 10) % 10 == 1, 7, 2, true, (type % 100) % 10);
            break;
            // Destination is right bottom
            case 4:
                // Two lines straight right
                createLines(true, (horizontalDistance - 10) / 4, (type / 10) % 10 == 1, 2, 2, false, (type % 100) % 10);
                // Three lines straight bottom
                createLines(false, verticalDistance / 3, (type / 10) % 10 == 1, 3, 3, false, (type % 100) % 10);
                // Two lines straight right
                createLines(false, (horizontalDistance - 10) / 4, (type / 10) % 10 == 1, 2, 2, true, (type % 100) % 10);
            break;
            // Destination is straight bottom
            case 5:
                // Six lines straight bottom
                createLines(true, (verticalDistance - 10) / 7, (type / 10) % 10 == 1, 7, 3, true, (type % 100) % 10);
            break;
            // Destination is bottom left
            case 6:
                // Two lines straight left
                createLines(true, (horizontalDistance - 10) / 4, (type / 10) % 10 == 1, 2, 4, false, (type % 100) % 10);
                // Three lines straight bottom
                createLines(false, verticalDistance / 3, (type / 10) % 10 == 1, 3, 3, false, (type % 100) % 10);
                // Two lines straight left
                createLines(false, (horizontalDistance - 10) / 4, (type / 10) % 10 == 1, 2, 4, true, (type % 100) % 10);
            break;
            // Destination is straight left
            case 7:
                // Six lines straight left
                createLines(true, (horizontalDistance - 10) / 7, (type / 10) % 10 == 1, 7, 4, true, (type % 100) % 10);
            break;
            // Destination is top left
            case 8:
                // Two lines straight left
                createLines(true, (horizontalDistance - 10) / 4, (type / 10) % 10 == 1, 2, 4, false, (type % 100) % 10);
                // Three lines straight top
                createLines(false, verticalDistance / 3, (type / 10) % 10 == 1, 3, 1, false, (type % 100) % 10);
                // Two lines straight left
                createLines(false, (horizontalDistance - 10) / 4, (type / 10) % 10 == 1, 2, 4, true, (type % 100) % 10);
            break;
        }
        headLayoutX = connector.getHeadLayoutX();
        headLayoutY  = connector.getHeadLayoutX();
        fixAnchorPosition();
    }
    
    // create line for the connector
    final void createLines(boolean isFirst, double length, boolean isContinuous, int number, int direction, boolean isLast, int headType) {
        Line previousLine;
        for (int i = 0; i < number; i++) {
            Line line = new Line();
            Anchor anchor;
            Color transparent = new Color(1, 1, 1, 0);
            if (isFirst && i == 0) {
                base = new Anchor(transparent, new SimpleDoubleProperty(startX), new SimpleDoubleProperty(startY));
                anchor = base;
            }
            else {
                previousLine = (Line)getChildren().get(getChildren().size()-1);
                anchor = new Anchor(transparent, previousLine.endXProperty(), previousLine.endYProperty());
            }
            getChildren().add(anchor);
            line.startXProperty().bind(anchor.centerXProperty());
            line.startYProperty().bind(anchor.centerYProperty());
            switch(direction) {
                // Direction is up
                case 1:
                    line.endXProperty().bind(anchor.centerXProperty());
                    line.endYProperty().bind(anchor.centerYProperty().subtract(length));
                    break;
                // Direction is right
                case 2:
                    line.endXProperty().bind(anchor.centerXProperty().add(length));
                    line.endYProperty().bind(anchor.centerYProperty());
                    break;
                // Direction is down
                case 3:
                    line.endXProperty().bind(anchor.centerXProperty());
                    line.endYProperty().bind(anchor.centerYProperty().add(length));
                    break;
                // Direction is left
                case 4:
                    line.endXProperty().bind(anchor.centerXProperty().subtract(length));
                    line.endYProperty().bind(anchor.centerYProperty());
                    break;
            }
            if(!isContinuous)
                line.getStrokeDashArray().setAll(5.0, 5.0);
            line.setMouseTransparent(true);
            getChildren().add(line);
        }
        if(isLast) {
            previousLine = (Line)getChildren().get(getChildren().size()-1);
            head = new Head(headType * 10 + direction, previousLine.endXProperty(), previousLine.endYProperty());
            getChildren().add(head);
        }
    }
    
    // Fix the position of the anchors
    public final void fixAnchorPosition() {
        for(int i = 0; i < getChildren().size() - 1; i+=2) {
            Anchor anchor = (Anchor) getChildren().get(i);
            anchor.setCenterX(anchorData.get(i));
            anchor.setCenterY(anchorData.get(i+1));
        }
        head.setLayoutX(headLayoutX);
        head.setLayoutY(headLayoutY);
    }
    public static int getIdCounter() {
        return idCounter;
    }
    
    public int getConnectorId() {
        return connectorId;
    }
    
    public int getType() {
        return type;
    }
    
    public int getSourceId() {
        return sourceId;
    }
    
    public int getDestinationId() {
        return destinationId;
    }
    
    public double getStartX() {
        return startX;
    }
    
    public double getStartY() {
        return startY;
    }
    
    public double getEndX() {
        return endX;
    }
    
    public double getEndY() {
        return endY;
    }
    
    public double getHeadLayoutX() {
        return headLayoutX;
    }
    
    public double getHeadLayoutY() {
        return headLayoutY;
    }
    
    public ArrayList<Double> getAnchorData(){
        anchorData.clear();
        for (int i = 0; i < getChildren().size() -1; i+=2) {
            Anchor anchor = (Anchor)getChildren().get(i);
            anchorData.add(anchor.getCenterX());
            anchorData.add(anchor.getCenterY());
        }
        return anchorData;
    }
    
    public static void setIdCounter(int idCounter) {
        Connector.idCounter = idCounter;
    }
    
    public void setStroke(Color color, boolean isSelected) {
        for(int i = 0; i < getChildren().size(); i++) {
            if(getChildren().get(i) instanceof Line) {
                Line line = (Line)getChildren().get(i);
                line.setStroke(color);
            }
            else if(getChildren().get(i) instanceof Anchor) {
               Anchor anchor = (Anchor)getChildren().get(i);
               if(isSelected)
                   anchor.setFillAndStroke(color);
               else
                   anchor.setFillAndStroke(new Color(1, 1, 1, 0));
            }
            else {
                Head head = (Head)getChildren().get(i);
                head.setFillAndStroke(color);
            }
        }
    }
    public void setDragEnabled(boolean isDragEnabled) {
        for(int i = 0; i < getChildren().size(); i++) {
            if(getChildren().get(i) instanceof Anchor) {
                Anchor anchor = (Anchor)getChildren().get(i);
                anchor.setDragEnabled(isDragEnabled);
            }
            else if(getChildren().get(i) instanceof Head) {
                Head anchor = (Head)getChildren().get(i);
                anchor.setDragEnabled(isDragEnabled);
            }
        }
    }
    
    // Set the layout of the connector
    public void setLayout(double deltaX, double deltaY, int diagramID) {
        if(diagramID == sourceId) {
            double newX = startX + deltaX;
            if (newX > 0 && newX < getScene().getWidth()) {
                base.setCenterX(newX);
            }
            double newY = startY + deltaY;
            if (newY > 0 && newY < getScene().getHeight()) {
                base.setCenterY(newY);
            }
        }
        else {
            head.setLayoutX(headLayoutX + deltaX);
            head.setLayoutY(headLayoutY + deltaY);
        }
    }
    
    // Set the position of the connector
    public void setPosition(int diagramID) {
        if(diagramID == sourceId) {
            startX = base.getCenterX();
            startY = base.getCenterY();
        }
        else {
            headLayoutX = head.getLayoutX();
            headLayoutY = head.getLayoutY();
        }
    }
    
    public class Anchor extends Circle {
        boolean isDragEnabled;
        Anchor(Color color, DoubleProperty x, DoubleProperty y) {
            super(x.get(), y.get(), 1.5);
            setFillAndStroke(color);
            setStrokeWidth(1);
            setStrokeType(StrokeType.OUTSIDE);
            x.bind(centerXProperty());
            y.bind(centerYProperty());
            isDragEnabled = false;
            enableDrag();
        }
        // make a node movable by dragging it around with the mouse.
        private void enableDrag() {
            final Delta dragDelta = new Delta();
            setOnMousePressed((MouseEvent mouseEvent) -> {
                // record a delta distance for the drag and drop operation.
                if(isDragEnabled) {
                    dragDelta.x = getCenterX() - mouseEvent.getX();
                    dragDelta.y = getCenterY() - mouseEvent.getY();
                }
            });
            setOnMouseDragged((MouseEvent mouseEvent) -> {
                if(isDragEnabled) {
                    double newX = mouseEvent.getX() + dragDelta.x;
                    if (newX > 0 && newX < getScene().getWidth()) {
                        setCenterX(newX);
                        if(this == base)
                            startX = getCenterX();
                    }
                    double newY = mouseEvent.getY() + dragDelta.y;
                    if (newY > 0 && newY < getScene().getHeight()) {
                        setCenterY(newY);
                        if(this == base)
                            startY = getCenterY();
                    }
                }
            });
        }
        
        public void setDragEnabled(boolean isDragEnabled) {
            this.isDragEnabled = isDragEnabled;
        }
        
        private void setFillAndStroke(Color color) {
            setFill(color.deriveColor(1, 1, 1, 1));
            setStroke(color);
        }
        
        // records relative x and y co-ordinates.
        private class Delta {
            double x, y;
        }
    }
    
    public class Head extends Polyline{
        int type;
        boolean isDragEnabled;
        public Head(int type, DoubleProperty x, DoubleProperty y) {
            this.type = type;
            isDragEnabled = false;
            ObservableList<Double> points = FXCollections.observableArrayList();
            switch(type) {
                // Triangle up
                case 11:
                    points.addAll(x.get(), y.get(), x.get()+5, y.get(), x.get(), y.get()-10, x.get()-5, y.get(), x.get(), y.get());
                    getPoints().addAll(points);
                    setFill(Color.BLACK);
                    break;
                // Triangle right
                case 12:
                    points.addAll(x.get(), y.get(), x.get(), y.get()-5, x.get()+10, y.get(), x.get(), y.get()+5, x.get(), y.get());
                    getPoints().addAll(points);
                    setFill(Color.BLACK);
                    break;
                // Triangle down
                case 13:
                    points.addAll(x.get(), y.get(), x.get()+5, y.get(), x.get(), y.get()+10, x.get()-5, y.get(), x.get(), y.get());
                    getPoints().addAll(points);
                    setFill(Color.BLACK);
                    break;
                // Triangle left
                case 14:
                    points.addAll(x.get(), y.get(), x.get(), y.get()-5, x.get()-10, y.get(), x.get(), y.get()+5, x.get(), y.get());
                    getPoints().addAll(points);
                    setFill(Color.BLACK);
                    break;
                // Diamond up
                case 21:
                    points.addAll(x.get(), y.get(), x.get()+3.5, y.get()-5, x.get(), y.get()-10, x.get()-3.5, y.get()-5, x.get(), y.get());
                    getPoints().addAll(points);
                    setFill(Color.BLACK);
                    break;
                // Diamond right
                case 22:
                    points.addAll(x.get(), y.get(), x.get()+5, y.get()-3.5, x.get()+10, y.get(), x.get()+5, y.get()+3.5, x.get(), y.get());
                    getPoints().addAll(points);
                    setFill(Color.BLACK);
                    break;
                // Diamond down
                case 23:
                    points.addAll(x.get(), y.get(), x.get()+3.5, y.get()+5, x.get(), y.get()+10, x.get()-3.5, y.get()+5, x.get(), y.get());
                    getPoints().addAll(points);
                    setFill(Color.BLACK);
                    break;
                // Diamond left
                case 24:
                    points.addAll(x.get(), y.get(), x.get()-5, y.get()-3.5, x.get()-10, y.get(), x.get()-5, y.get()+3.5, x.get(), y.get());
                    getPoints().addAll(points);
                    setFill(Color.BLACK);
                    break;
                // Arrow Up
                case 31:
                    points.addAll(x.get(), y.get(), x.get(), y.get()-10, x.get()-5, y.get(), x.get(), y.get()-10, x.get()+5, y.get(), x.get(), y.get()-10);
                    getPoints().addAll(points);
                    break;
                // Arrow right
                case 32:
                    points.addAll(x.get(), y.get(), x.get()+10, y.get(), x.get(), y.get()-5, x.get()+10, y.get(), x.get(), y.get()+5, x.get()+10, y.get());
                    getPoints().addAll(points);
                    break;
                // Arrow down
                case 33:
                    points.addAll(x.get(), y.get(), x.get(), y.get()+10, x.get()-5, y.get(), x.get(), y.get()+10, x.get()+5, y.get(), x.get(), y.get()+10);
                    getPoints().addAll(points);
                    break;
                // Arrow left
                default:
                    points.addAll(x.get(), y.get(), x.get()-10, y.get(), x.get(), y.get()-5, x.get()-10, y.get(), x.get(), y.get()+5, x.get()-10, y.get());
                    getPoints().addAll(points);
                    break;
            }
            x.bind(layoutXProperty().add(x.get()));
            y.bind(layoutYProperty().add(y.get()));
            enableDrag();
        }
        private void enableDrag() {
            final ObjectProperty<Point2D> mousePosition = new SimpleObjectProperty<>();
            setOnMousePressed((MouseEvent mouseEvent) -> {
                if(isDragEnabled) {
                    mousePosition.set(new Point2D(mouseEvent.getSceneX(), mouseEvent.getSceneY()));
                }
            });
            setOnMouseDragged((MouseEvent mouseEvent) -> {
                if(isDragEnabled && mousePosition.get() != null) {
                    double deltaX = mouseEvent.getSceneX() - (mousePosition.get() != null ? mousePosition.get().getX() : 0);
                    double deltaY = mouseEvent.getSceneY() - (mousePosition.get() != null ? mousePosition.get().getY() : 0);
                    setLayoutX(getLayoutX() + deltaX);
                    setLayoutY(getLayoutY() + deltaY);
                    mousePosition.set(new Point2D(mouseEvent.getSceneX(), mouseEvent.getSceneY()));
                    headLayoutX = head.getLayoutX();
                    headLayoutY = head.getLayoutY();
                }
            });
        }
        public void setDragEnabled(boolean isDragEnabled) {
            this.isDragEnabled = isDragEnabled;
        }
        private void setFillAndStroke(Color color) {
            if(type != 31 && type != 32 && type != 33 && type != 34)
                setFill(color.deriveColor(1, 1, 1, 1));
            setStroke(color);
        }
    } 
}
