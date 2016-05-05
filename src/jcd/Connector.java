/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jcd;

import javafx.beans.binding.DoubleBinding;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
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
    int sourceId;
    int destinationId;
    double startX;
    double startY;
    double endX;
    double endY;
    
    public Connector(int connectorId, int type, int sourceId, int destionationId, double startX, double startY, double endX, double endY) {
        this.connectorId = connectorId == -1 ? Connector.idCounter : connectorId;
        this.sourceId = sourceId;
        this.startX = startX;
        this.startY = startY;
        this.endX = endX;
        this.endY = endY;
        this.destinationId = destionationId;
        double horizontalDistance = Math.abs(startX - endX) - 10;
        double verticalDistance = Math.abs(startY - endY) -10;
        double length;
        Line line;
        Line previousLine;
        switch(type / 100) {
            // Destination is straight top
            case 1:
                // Six lines straight top
                length = verticalDistance / 7;
                for (int i = 0; i < 7; i++) {
                    line = new Line();
                    Anchor anchor;
                    if (i == 0)
                        anchor = new Anchor(Color.BLACK, new SimpleDoubleProperty(startX), new SimpleDoubleProperty(startY));
                    else {
                        previousLine = (Line)getChildren().get(getChildren().size()-1);
                        anchor = new Anchor(Color.BLACK, previousLine.endXProperty(), previousLine.endYProperty());
                    }
                    line.startXProperty().bind(anchor.centerXProperty());
                    line.startYProperty().bind(anchor.centerYProperty());
                    line.endXProperty().bind(new SimpleDoubleProperty(startX));
                    line.endYProperty().bind(new SimpleDoubleProperty(startY - ((i+1) * length)));
                    if((type/10) % 10 == 1)
                        line.getStrokeDashArray().setAll(5.0, 5.0);
                    line.setMouseTransparent(true);
                    getChildren().addAll(anchor, line);
                }
                previousLine = (Line)getChildren().get(getChildren().size()-1);
                Head head = new Head(11, previousLine.endXProperty(), previousLine.endYProperty());
                getChildren().add(head);
            break;
            // Destination is right top
            case 2:
                // Two lines straight right
                length = horizontalDistance / 4;
                for (int i = 0; i < 2; i++) {
                    line = new Line();
                    if (i == 0) {
                        line.startXProperty().bind(new SimpleDoubleProperty(startX));
                        line.startYProperty().bind(new SimpleDoubleProperty(startY));
                    }
                    else {
                        previousLine = (Line)getChildren().get(getChildren().size()-2);
                        line.startXProperty().bind(previousLine.endXProperty());
                        line.startYProperty().bind(previousLine.endYProperty());
                    }
                    line.endXProperty().bind(new SimpleDoubleProperty(startX + (i + 1) * length));
                    line.endYProperty().bind(new SimpleDoubleProperty(startY));
                    if((type/10) % 10 == 1)
                        line.getStrokeDashArray().setAll(5.0, 5.0);
                    line.setMouseTransparent(true);
                    getChildren().add(line);
                    Anchor anchor = new Anchor(Color.BLACK, line.endXProperty(), line.endYProperty());
                    getChildren().add(anchor);
                }
                // Three lines straight top
                length = verticalDistance / 3;
                for (int i = 0; i < 3; i++) {
                    previousLine = (Line)getChildren().get(getChildren().size()-2);
                    line = new Line();
                    line.startXProperty().bind(previousLine.endXProperty());
                    line.startYProperty().bind(previousLine.endYProperty());
                    line.endXProperty().bind(new SimpleDoubleProperty(previousLine.getEndX()));
                    line.endYProperty().bind(new SimpleDoubleProperty(previousLine.getEndY() - length));
                    if((type/10) % 10 == 1)
                        line.getStrokeDashArray().setAll(5.0, 5.0);
                    line.setMouseTransparent(true);
                    getChildren().add(line);
                    Anchor anchor = new Anchor(Color.BLACK, line.endXProperty(), line.endYProperty());
                    getChildren().add(anchor);
                }
                // Two lines straight right
                length = horizontalDistance / 4;
                for (int i = 0; i < 2; i++) {
                    line = new Line();
                    previousLine = (Line)getChildren().get(getChildren().size()-2);
                    line.startXProperty().bind(previousLine.endXProperty());
                    line.startYProperty().bind(previousLine.endYProperty());
                    line.endXProperty().bind(new SimpleDoubleProperty(previousLine.getEndX() + length));
                    line.endYProperty().bind(new SimpleDoubleProperty(previousLine.getEndY()));
                    if((type/10) % 10 == 1)
                        line.getStrokeDashArray().setAll(5.0, 5.0);
                    line.setMouseTransparent(true);
                    getChildren().add(line);
                    if (i != 1) {
                        Anchor anchor = new Anchor(Color.BLACK, line.endXProperty(), line.endYProperty());
                        getChildren().add(anchor);
                    }
                }
            break;
            // Destination is straight right
            case 3:
                // Six lines straight right
                length = horizontalDistance / 7;
                for (int i = 0; i < 7; i++) {
                    line = new Line();
                    if (i == 0) {
                        line.startXProperty().bind(new SimpleDoubleProperty(startX));
                        line.startYProperty().bind(new SimpleDoubleProperty(startY));
                    }
                    else {
                        previousLine = (Line)getChildren().get(getChildren().size()-2);
                        line.startXProperty().bind(previousLine.endXProperty());
                        line.startYProperty().bind(previousLine.endYProperty());
                    }
                    line.endXProperty().bind(new SimpleDoubleProperty(startX + ((i+1) * length)));
                    line.endYProperty().bind(new SimpleDoubleProperty(startY));
                    if((type/10) % 10 == 1)
                        line.getStrokeDashArray().setAll(5.0, 5.0);
                    line.setMouseTransparent(true);
                    getChildren().add(line);
                    if (i != 6) {
                        Anchor anchor = new Anchor(Color.BLACK, line.endXProperty(), line.endYProperty());
                        getChildren().add(anchor);
                    }
                }
            break;
            // Destination is right bottom
            case 4:
                // Two lines straight right
                length = horizontalDistance / 4;
                for (int i = 0; i < 2; i++) {
                    line = new Line();
                    if (i == 0) {
                        line.startXProperty().bind(new SimpleDoubleProperty(startX));
                        line.startYProperty().bind(new SimpleDoubleProperty(startY));
                    }
                    else {
                        previousLine = (Line)getChildren().get(getChildren().size()-2);
                        line.startXProperty().bind(previousLine.endXProperty());
                        line.startYProperty().bind(previousLine.endYProperty());
                    }
                    line.endXProperty().bind(new SimpleDoubleProperty(startX + (i + 1) * length));
                    line.endYProperty().bind(new SimpleDoubleProperty(startY));
                    if((type/10) % 10 == 1)
                        line.getStrokeDashArray().setAll(5.0, 5.0);
                    line.setMouseTransparent(true);
                    getChildren().add(line);
                    Anchor anchor = new Anchor(Color.BLACK, line.endXProperty(), line.endYProperty());
                    getChildren().add(anchor);
                }
                // Three lines straight bottom
                length = verticalDistance / 3;
                for (int i = 0; i < 3; i++) {
                    previousLine = (Line)getChildren().get(getChildren().size()-2);
                    line = new Line();
                    line.startXProperty().bind(previousLine.endXProperty());
                    line.startYProperty().bind(previousLine.endYProperty());
                    line.endXProperty().bind(new SimpleDoubleProperty(previousLine.getEndX()));
                    line.endYProperty().bind(new SimpleDoubleProperty(previousLine.getEndY() + length));
                    if((type/10) % 10 == 1)
                        line.getStrokeDashArray().setAll(5.0, 5.0);
                    line.setMouseTransparent(true);
                    getChildren().add(line);
                    Anchor anchor = new Anchor(Color.BLACK, line.endXProperty(), line.endYProperty());
                    getChildren().add(anchor);
                }
                // Two lines straight right
                length = horizontalDistance / 4;
                for (int i = 0; i < 2; i++) {
                    line = new Line();
                    previousLine = (Line)getChildren().get(getChildren().size()-2);
                    line.startXProperty().bind(previousLine.endXProperty());
                    line.startYProperty().bind(previousLine.endYProperty());
                    line.endXProperty().bind(new SimpleDoubleProperty(previousLine.getEndX() + length));
                    line.endYProperty().bind(new SimpleDoubleProperty(previousLine.getEndY()));
                    if((type/10) % 10 == 1)
                        line.getStrokeDashArray().setAll(5.0, 5.0);
                    line.setMouseTransparent(true);
                    getChildren().add(line);
                    if (i != 1) {
                        Anchor anchor = new Anchor(Color.BLACK, line.endXProperty(), line.endYProperty());
                        getChildren().add(anchor);
                    }
                }
            break;
            // Destination is straight bottom
            case 5:
                // Six lines straight bottom
                length = verticalDistance / 7;
                for (int i = 0; i < 7; i++) {
                    line = new Line();
                    if (i == 0) {
                        line.startXProperty().bind(new SimpleDoubleProperty(startX));
                        line.startYProperty().bind(new SimpleDoubleProperty(startY));
                    }
                    else {
                        previousLine = (Line)getChildren().get(getChildren().size()-2);
                        line.startXProperty().bind(previousLine.endXProperty());
                        line.startYProperty().bind(previousLine.endYProperty());
                    }
                    line.endXProperty().bind(new SimpleDoubleProperty(startX));
                    line.endYProperty().bind(new SimpleDoubleProperty(startY + ((i+1) * length)));
                    if((type/10) % 10 == 1)
                        line.getStrokeDashArray().setAll(5.0, 5.0);
                    line.setMouseTransparent(true);
                    getChildren().add(line);
                    if (i != 6) {
                        Anchor anchor = new Anchor(Color.BLACK, line.endXProperty(), line.endYProperty());
                        getChildren().add(anchor);
                    }
                }
            break;
            // Destination is bottom left
            case 6:
                // Two lines straight left
                length = horizontalDistance / 4;
                for (int i = 0; i < 2; i++) {
                    line = new Line();
                    if (i == 0) {
                        line.startXProperty().bind(new SimpleDoubleProperty(startX));
                        line.startYProperty().bind(new SimpleDoubleProperty(startY));
                    }
                    else {
                        previousLine = (Line)getChildren().get(getChildren().size()-2);
                        line.startXProperty().bind(previousLine.endXProperty());
                        line.startYProperty().bind(previousLine.endYProperty());
                    }
                    line.endXProperty().bind(new SimpleDoubleProperty(startX - (i + 1) * length));
                    line.endYProperty().bind(new SimpleDoubleProperty(startY));
                    if((type/10) % 10 == 1)
                        line.getStrokeDashArray().setAll(5.0, 5.0);
                    line.setMouseTransparent(true);
                    getChildren().add(line);
                    Anchor anchor = new Anchor(Color.BLACK, line.endXProperty(), line.endYProperty());
                    getChildren().add(anchor);
                }
                // Three lines straight bottom
                length = verticalDistance / 3;
                for (int i = 0; i < 3; i++) {
                    previousLine = (Line)getChildren().get(getChildren().size()-2);
                    line = new Line();
                    line.startXProperty().bind(previousLine.endXProperty());
                    line.startYProperty().bind(previousLine.endYProperty());
                    line.endXProperty().bind(new SimpleDoubleProperty(previousLine.getEndX()));
                    line.endYProperty().bind(new SimpleDoubleProperty(previousLine.getEndY() + length));
                    if((type/10) % 10 == 1)
                        line.getStrokeDashArray().setAll(5.0, 5.0);
                    line.setMouseTransparent(true);
                    getChildren().add(line);
                    Anchor anchor = new Anchor(Color.BLACK, line.endXProperty(), line.endYProperty());
                    getChildren().add(anchor);
                }
                // Two lines straight left
                length = horizontalDistance / 4;
                for (int i = 0; i < 2; i++) {
                    line = new Line();
                    previousLine = (Line)getChildren().get(getChildren().size()-2);
                    line.startXProperty().bind(previousLine.endXProperty());
                    line.startYProperty().bind(previousLine.endYProperty());
                    line.endXProperty().bind(new SimpleDoubleProperty(previousLine.getEndX() - length));
                    line.endYProperty().bind(new SimpleDoubleProperty(previousLine.getEndY()));
                    if((type/10) % 10 == 1)
                        line.getStrokeDashArray().setAll(5.0, 5.0);
                    line.setMouseTransparent(true);
                    getChildren().add(line);
                    if (i != 1) {
                        Anchor anchor = new Anchor(Color.BLACK, line.endXProperty(), line.endYProperty());
                        getChildren().add(anchor);
                    }
                }
            break;
            // Destination is straight left
            case 7:
                // Six lines straight right
                length = horizontalDistance / 7;
                for (int i = 0; i < 7; i++) {
                    line = new Line();
                    if (i == 0) {
                        line.startXProperty().bind(new SimpleDoubleProperty(startX));
                        line.startYProperty().bind(new SimpleDoubleProperty(startY));
                    }
                    else {
                        previousLine = (Line)getChildren().get(getChildren().size()-2);
                        line.startXProperty().bind(previousLine.endXProperty());
                        line.startYProperty().bind(previousLine.endYProperty());
                    }
                    line.endXProperty().bind(new SimpleDoubleProperty(startX - ((i+1) * length)));
                    line.endYProperty().bind(new SimpleDoubleProperty(startY));
                    if((type/10) % 10 == 1)
                        line.getStrokeDashArray().setAll(5.0, 5.0);
                    line.setMouseTransparent(true);
                    getChildren().add(line);
                    if (i != 6) {
                        Anchor anchor = new Anchor(Color.BLACK, line.endXProperty(), line.endYProperty());
                        getChildren().add(anchor);
                    }
                }
            break;
            // Destination is top left
            default:
                // Two lines straight left
                length = horizontalDistance / 4;
                for (int i = 0; i < 2; i++) {
                    line = new Line();
                    if (i == 0) {
                        line.startXProperty().bind(new SimpleDoubleProperty(startX));
                        line.startYProperty().bind(new SimpleDoubleProperty(startY));
                    }
                    else {
                        previousLine = (Line)getChildren().get(getChildren().size()-2);
                        line.startXProperty().bind(previousLine.endXProperty());
                        line.startYProperty().bind(previousLine.endYProperty());
                    }
                    line.endXProperty().bind(new SimpleDoubleProperty(startX - (i + 1) * length));
                    line.endYProperty().bind(new SimpleDoubleProperty(startY));
                    if((type/10) % 10 == 1)
                        line.getStrokeDashArray().setAll(5.0, 5.0);
                    line.setMouseTransparent(true);
                    getChildren().add(line);
                    Anchor anchor = new Anchor(Color.BLACK, line.endXProperty(), line.endYProperty());
                    getChildren().add(anchor);
                }
                // Three lines straight top
                length = verticalDistance / 3;
                for (int i = 0; i < 3; i++) {
                    previousLine = (Line)getChildren().get(getChildren().size()-2);
                    line = new Line();
                    line.startXProperty().bind(previousLine.endXProperty());
                    line.startYProperty().bind(previousLine.endYProperty());
                    line.endXProperty().bind(new SimpleDoubleProperty(previousLine.getEndX()));
                    line.endYProperty().bind(new SimpleDoubleProperty(previousLine.getEndY() - length));
                    if((type/10) % 10 == 1)
                        line.getStrokeDashArray().setAll(5.0, 5.0);
                    line.setMouseTransparent(true);
                    getChildren().add(line);
                    Anchor anchor = new Anchor(Color.BLACK, line.endXProperty(), line.endYProperty());
                    getChildren().add(anchor);
                }
                // Two lines straight left
                length = horizontalDistance / 4;
                for (int i = 0; i < 2; i++) {
                    line = new Line();
                    previousLine = (Line)getChildren().get(getChildren().size()-2);
                    line.startXProperty().bind(previousLine.endXProperty());
                    line.startYProperty().bind(previousLine.endYProperty());
                    line.endXProperty().bind(new SimpleDoubleProperty(previousLine.getEndX() - length));
                    line.endYProperty().bind(new SimpleDoubleProperty(previousLine.getEndY()));
                    if((type/10) % 10 == 1)
                        line.getStrokeDashArray().setAll(5.0, 5.0);
                    line.setMouseTransparent(true);
                    getChildren().add(line);
                    if (i != 1) {
                        Anchor anchor = new Anchor(Color.BLACK, line.endXProperty(), line.endYProperty());
                        getChildren().add(anchor);
                    }
                }
            break;
        }
        idCounter++;
    }
    public int getConnectorId() {
        return connectorId;
    }
    public int getSourceId() {
        return sourceId;
    }
    public int getDestinationId() {
        return destinationId;
    }
    
    public void updateConnectorPath(int diagramID, double x, double y) {
        if(sourceId == diagramID) {
            Line line = (Line)getChildren().get(0);
            line.startXProperty().bind(new SimpleDoubleProperty(x));
            line.startYProperty().bind(new SimpleDoubleProperty(y));
        }
        else {
            Line line = (Line)getChildren().get(getChildren().size()-1);
            line.startXProperty().bind(new SimpleDoubleProperty(x));
            line.startYProperty().bind(new SimpleDoubleProperty(y));
        }
    }
    
    public void setStroke(Color color) {
        for(int i = 0; i < getChildren().size(); i++) {
            if(getChildren().get(i) instanceof Line) {
                Line line = (Line)getChildren().get(i);
                line.setStroke(color);
            }
            else if(getChildren().get(i) instanceof Anchor) {
               Anchor anchor = (Anchor)getChildren().get(i);
               anchor.setFillAndStroke(color);
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
    // Bind connector with source and destination diagram 
    public void bindProperties(DoubleBinding startXProperty, DoubleBinding startYProperty, DoubleBinding endXProperty, DoubleBinding endYProperty) {
        Line startLine = (Line)getChildren().get(0);
        startLine.startXProperty().bind(startXProperty);
        startLine.startYProperty().bind(startYProperty);
        Line endLine = (Line)getChildren().get(getChildren().size()-1);
        endLine.endXProperty().bind(endXProperty);
        endLine.endYProperty().bind(endYProperty);
    }
    
    class Anchor extends Circle {
        boolean isDragEnabled;
        Anchor(Color color, DoubleProperty x, DoubleProperty y) {
            super(x.get(), y.get(), 0.5);
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
            setOnMousePressed(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent mouseEvent) {
                    // record a delta distance for the drag and drop operation.
                    if(isDragEnabled) {
                        dragDelta.x = getCenterX() - mouseEvent.getX();
                        dragDelta.y = getCenterY() - mouseEvent.getY();
                    }
                }
            });
            setOnMouseDragged(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent mouseEvent) {
                    if(isDragEnabled) {
                        double newX = mouseEvent.getX() + dragDelta.x;
                        if (newX > 0 && newX < getScene().getWidth()) {
                            setCenterX(newX);
                        }
                        double newY = mouseEvent.getY() + dragDelta.y;
                        if (newY > 0 && newY < getScene().getHeight()) {
                            setCenterY(newY);
                        }
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
    
    class Head extends Polyline{
        int type;
        boolean isDragEnabled;
        public Head(int type, DoubleProperty x, DoubleProperty y) {
            this.type = type;
            isDragEnabled = false;
            ObservableList<Double> points = FXCollections.observableArrayList();
            switch(type) {
                // Triangle up
                case 1:
                    points.addAll(x.get(), y.get(), x.get()+5, y.get(), x.get(), y.get()-10, x.get()-5, y.get(), x.get(), y.get());
                    getPoints().addAll(points);
                    setFill(Color.BLACK);
                    break;
                // Triangle right
                case 2:
                    points.addAll(x.get(), y.get(), x.get(), y.get()-5, x.get()+10, y.get(), x.get(), y.get()+5, x.get(), y.get());
                    getPoints().addAll(points);
                    setFill(Color.BLACK);
                    break;
                // Triangle down
                case 3:
                    points.addAll(x.get(), y.get(), x.get()+5, y.get(), x.get(), y.get()+10, x.get()-5, y.get(), x.get(), y.get());
                    getPoints().addAll(points);
                    setFill(Color.BLACK);
                    break;
                // Triangle left
                case 4:
                    points.addAll(x.get(), y.get(), x.get(), y.get()-5, x.get()-10, y.get(), x.get(), y.get()+5, x.get(), y.get());
                    getPoints().addAll(points);
                    setFill(Color.BLACK);
                    break;
                // Diamond up
                case 5:
                    points.addAll(x.get(), y.get(), x.get()+2.5, y.get()-5, x.get(), y.get()-10, x.get()-2.5, y.get()-5, x.get(), y.get());
                    getPoints().addAll(points);
                    setFill(Color.BLACK);
                    break;
                // Diamond right
                case 6:
                    points.addAll(x.get(), y.get(), x.get()+5, y.get()-2.5, x.get()+10, y.get(), x.get()+5, y.get()+2.5, x.get(), y.get());
                    getPoints().addAll(points);
                    setFill(Color.BLACK);
                    break;
                // Diamond down
                case 7:
                    points.addAll(x.get(), y.get(), x.get()+2.5, y.get()+5, x.get(), y.get()+10, x.get()-2.5, y.get()+5, x.get(), y.get());
                    getPoints().addAll(points);
                    setFill(Color.BLACK);
                    break;
                // Diamond left
                case 8:
                    points.addAll(x.get(), y.get(), x.get()-5, y.get()-2.5, x.get()-10, y.get(), x.get()-5, y.get()+2.5, x.get(), y.get());
                    getPoints().addAll(points);
                    setFill(Color.BLACK);
                    break;
                // Arrow Up
                case 9:
                    points.addAll(x.get(), y.get(), x.get(), y.get()-10, x.get()-5, y.get(), x.get(), y.get()-10, x.get()+5, y.get(), x.get(), y.get()-10);
                    getPoints().addAll(points);
                    break;
                // Arrow right
                case 10:
                    points.addAll(x.get(), y.get(), x.get()+10, y.get(), x.get(), y.get()-5, x.get()+10, y.get(), x.get(), y.get()+5, x.get()+10, y.get());
                    getPoints().addAll(points);
                    break;
                // Arrow down
                case 11:
                    points.addAll(x.get(), y.get(), x.get(), y.get()+10, x.get()-5, y.get(), x.get(), y.get()+10, x.get()+5, y.get(), x.get(), y.get()+10);
                    getPoints().addAll(points);
                    break;
                // Arrow left
                case 12:
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
            setOnMousePressed(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent mouseEvent) {
                    if(isDragEnabled) {
                        mousePosition.set(new Point2D(mouseEvent.getSceneX(), mouseEvent.getSceneY()));
                    }
                }
            });
            setOnMouseDragged(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent mouseEvent) {
                    if(isDragEnabled) {
                        double deltaX = mouseEvent.getSceneX() - mousePosition.get().getX();
                        double deltaY = mouseEvent.getSceneY() - mousePosition.get().getY();
                        setLayoutX(getLayoutX()+deltaX);
                        setLayoutY(getLayoutY()+deltaY);
                        mousePosition.set(new Point2D(mouseEvent.getSceneX(), mouseEvent.getSceneY()));
                    }
                }
            });
        }
        public void setDragEnabled(boolean isDragEnabled) {
            this.isDragEnabled = isDragEnabled;
        }
        private void setFillAndStroke(Color color) {
            if(type != 9 && type != 10 && type != 11 && type != 12)
                setFill(color.deriveColor(1, 1, 1, 1));
            setStroke(color);
        }
    }
    
}
