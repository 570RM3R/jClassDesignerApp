/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jcd;

import javafx.beans.binding.DoubleBinding;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.event.EventHandler;
import javafx.scene.Cursor;
import javafx.scene.Group;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Polygon;
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
        double horizontalDistance = Math.abs(startX - endX) - 15;
        double verticalDistance = Math.abs(startY - endY) -15;
        double length;
        switch(type / 10) {
            // Destination is straight top
            case 1:
                // Six lines straight top
                length = verticalDistance / 7;
                for (int i = 0; i < 7; i++) {
                    Line line = new Line();
                    if (i == 0) {
                        line.startXProperty().bind(new SimpleDoubleProperty(startX));
                        line.startYProperty().bind(new SimpleDoubleProperty(startY));
                    }
                    else {
                        Line previousLine = (Line)getChildren().get(getChildren().size()-2);
                        line.startXProperty().bind(previousLine.endXProperty());
                        line.startYProperty().bind(previousLine.endYProperty());
                    }
                    line.endXProperty().bind(new SimpleDoubleProperty(startX));
                    line.endYProperty().bind(new SimpleDoubleProperty(startY - ((i+1) * length)));
                    if(type % 10 == 1)
                        line.getStrokeDashArray().setAll(5.0, 5.0);
                    line.setMouseTransparent(true);
                    getChildren().add(line);
                    if (i != 6) {
                        Anchor anchor = new Anchor(Color.BLACK, line.endXProperty(), line.endYProperty());
                        getChildren().add(anchor);
                    }
                }
            break;
            // Destination is right top
            case 2:
                // Two lines straight right
                length = horizontalDistance / 4;
                for (int i = 0; i < 2; i++) {
                    Line line = new Line();
                    if (i == 0) {
                        line.startXProperty().bind(new SimpleDoubleProperty(startX));
                        line.startYProperty().bind(new SimpleDoubleProperty(startY));
                    }
                    else {
                        Line previousLine = (Line)getChildren().get(getChildren().size()-2);
                        line.startXProperty().bind(previousLine.endXProperty());
                        line.startYProperty().bind(previousLine.endYProperty());
                    }
                    line.endXProperty().bind(new SimpleDoubleProperty(startX + (i + 1) * length));
                    line.endYProperty().bind(new SimpleDoubleProperty(startY));
                    if(type % 10 == 1)
                        line.getStrokeDashArray().setAll(5.0, 5.0);
                    line.setMouseTransparent(true);
                    getChildren().add(line);
                    Anchor anchor = new Anchor(Color.BLACK, line.endXProperty(), line.endYProperty());
                    getChildren().add(anchor);
                }
                // Three lines straight top
                length = verticalDistance / 3;
                for (int i = 0; i < 3; i++) {
                    Line previousLine = (Line)getChildren().get(getChildren().size()-2);
                    Line line = new Line();
                    line.startXProperty().bind(previousLine.endXProperty());
                    line.startYProperty().bind(previousLine.endYProperty());
                    line.endXProperty().bind(new SimpleDoubleProperty(previousLine.getEndX()));
                    line.endYProperty().bind(new SimpleDoubleProperty(previousLine.getEndY() - length));
                    if(type % 10 == 1)
                        line.getStrokeDashArray().setAll(5.0, 5.0);
                    line.setMouseTransparent(true);
                    getChildren().add(line);
                    Anchor anchor = new Anchor(Color.BLACK, line.endXProperty(), line.endYProperty());
                    getChildren().add(anchor);
                }
                // Two lines straight right
                length = horizontalDistance / 4;
                for (int i = 0; i < 2; i++) {
                    Line line = new Line();
                    Line previousLine = (Line)getChildren().get(getChildren().size()-2);
                    line.startXProperty().bind(previousLine.endXProperty());
                    line.startYProperty().bind(previousLine.endYProperty());
                    line.endXProperty().bind(new SimpleDoubleProperty(previousLine.getEndX() + length));
                    line.endYProperty().bind(new SimpleDoubleProperty(previousLine.getEndY()));
                    if(type % 10 == 1)
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
                    Line line = new Line();
                    if (i == 0) {
                        line.startXProperty().bind(new SimpleDoubleProperty(startX));
                        line.startYProperty().bind(new SimpleDoubleProperty(startY));
                    }
                    else {
                        Line previousLine = (Line)getChildren().get(getChildren().size()-2);
                        line.startXProperty().bind(previousLine.endXProperty());
                        line.startYProperty().bind(previousLine.endYProperty());
                    }
                    line.endXProperty().bind(new SimpleDoubleProperty(startX + ((i+1) * length)));
                    line.endYProperty().bind(new SimpleDoubleProperty(startY));
                    if(type % 10 == 1)
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
                    Line line = new Line();
                    if (i == 0) {
                        line.startXProperty().bind(new SimpleDoubleProperty(startX));
                        line.startYProperty().bind(new SimpleDoubleProperty(startY));
                    }
                    else {
                        Line previousLine = (Line)getChildren().get(getChildren().size()-2);
                        line.startXProperty().bind(previousLine.endXProperty());
                        line.startYProperty().bind(previousLine.endYProperty());
                    }
                    line.endXProperty().bind(new SimpleDoubleProperty(startX + (i + 1) * length));
                    line.endYProperty().bind(new SimpleDoubleProperty(startY));
                    if(type % 10 == 1)
                        line.getStrokeDashArray().setAll(5.0, 5.0);
                    line.setMouseTransparent(true);
                    getChildren().add(line);
                    Anchor anchor = new Anchor(Color.BLACK, line.endXProperty(), line.endYProperty());
                    getChildren().add(anchor);
                }
                // Three lines straight bottom
                length = verticalDistance / 3;
                for (int i = 0; i < 3; i++) {
                    Line previousLine = (Line)getChildren().get(getChildren().size()-2);
                    Line line = new Line();
                    line.startXProperty().bind(previousLine.endXProperty());
                    line.startYProperty().bind(previousLine.endYProperty());
                    line.endXProperty().bind(new SimpleDoubleProperty(previousLine.getEndX()));
                    line.endYProperty().bind(new SimpleDoubleProperty(previousLine.getEndY() + length));
                    if(type % 10 == 1)
                        line.getStrokeDashArray().setAll(5.0, 5.0);
                    line.setMouseTransparent(true);
                    getChildren().add(line);
                    Anchor anchor = new Anchor(Color.BLACK, line.endXProperty(), line.endYProperty());
                    getChildren().add(anchor);
                }
                // Two lines straight right
                length = horizontalDistance / 4;
                for (int i = 0; i < 2; i++) {
                    Line line = new Line();
                    Line previousLine = (Line)getChildren().get(getChildren().size()-2);
                    line.startXProperty().bind(previousLine.endXProperty());
                    line.startYProperty().bind(previousLine.endYProperty());
                    line.endXProperty().bind(new SimpleDoubleProperty(previousLine.getEndX() + length));
                    line.endYProperty().bind(new SimpleDoubleProperty(previousLine.getEndY()));
                    if(type % 10 == 1)
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
                    Line line = new Line();
                    if (i == 0) {
                        line.startXProperty().bind(new SimpleDoubleProperty(startX));
                        line.startYProperty().bind(new SimpleDoubleProperty(startY));
                    }
                    else {
                        Line previousLine = (Line)getChildren().get(getChildren().size()-2);
                        line.startXProperty().bind(previousLine.endXProperty());
                        line.startYProperty().bind(previousLine.endYProperty());
                    }
                    line.endXProperty().bind(new SimpleDoubleProperty(startX));
                    line.endYProperty().bind(new SimpleDoubleProperty(startY + ((i+1) * length)));
                    if(type % 10 == 1)
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
                    Line line = new Line();
                    if (i == 0) {
                        line.startXProperty().bind(new SimpleDoubleProperty(startX));
                        line.startYProperty().bind(new SimpleDoubleProperty(startY));
                    }
                    else {
                        Line previousLine = (Line)getChildren().get(getChildren().size()-2);
                        line.startXProperty().bind(previousLine.endXProperty());
                        line.startYProperty().bind(previousLine.endYProperty());
                    }
                    line.endXProperty().bind(new SimpleDoubleProperty(startX - (i + 1) * length));
                    line.endYProperty().bind(new SimpleDoubleProperty(startY));
                    if(type % 10 == 1)
                        line.getStrokeDashArray().setAll(5.0, 5.0);
                    line.setMouseTransparent(true);
                    getChildren().add(line);
                    Anchor anchor = new Anchor(Color.BLACK, line.endXProperty(), line.endYProperty());
                    getChildren().add(anchor);
                }
                // Three lines straight bottom
                length = verticalDistance / 3;
                for (int i = 0; i < 3; i++) {
                    Line previousLine = (Line)getChildren().get(getChildren().size()-2);
                    Line line = new Line();
                    line.startXProperty().bind(previousLine.endXProperty());
                    line.startYProperty().bind(previousLine.endYProperty());
                    line.endXProperty().bind(new SimpleDoubleProperty(previousLine.getEndX()));
                    line.endYProperty().bind(new SimpleDoubleProperty(previousLine.getEndY() + length));
                    if(type % 10 == 1)
                        line.getStrokeDashArray().setAll(5.0, 5.0);
                    line.setMouseTransparent(true);
                    getChildren().add(line);
                    Anchor anchor = new Anchor(Color.BLACK, line.endXProperty(), line.endYProperty());
                    getChildren().add(anchor);
                }
                // Two lines straight left
                length = horizontalDistance / 4;
                for (int i = 0; i < 2; i++) {
                    Line line = new Line();
                    Line previousLine = (Line)getChildren().get(getChildren().size()-2);
                    line.startXProperty().bind(previousLine.endXProperty());
                    line.startYProperty().bind(previousLine.endYProperty());
                    line.endXProperty().bind(new SimpleDoubleProperty(previousLine.getEndX() - length));
                    line.endYProperty().bind(new SimpleDoubleProperty(previousLine.getEndY()));
                    if(type % 10 == 1)
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
                    Line line = new Line();
                    if (i == 0) {
                        line.startXProperty().bind(new SimpleDoubleProperty(startX));
                        line.startYProperty().bind(new SimpleDoubleProperty(startY));
                    }
                    else {
                        Line previousLine = (Line)getChildren().get(getChildren().size()-2);
                        line.startXProperty().bind(previousLine.endXProperty());
                        line.startYProperty().bind(previousLine.endYProperty());
                    }
                    line.endXProperty().bind(new SimpleDoubleProperty(startX - ((i+1) * length)));
                    line.endYProperty().bind(new SimpleDoubleProperty(startY));
                    if(type % 10 == 1)
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
                    Line line = new Line();
                    if (i == 0) {
                        line.startXProperty().bind(new SimpleDoubleProperty(startX));
                        line.startYProperty().bind(new SimpleDoubleProperty(startY));
                    }
                    else {
                        Line previousLine = (Line)getChildren().get(getChildren().size()-2);
                        line.startXProperty().bind(previousLine.endXProperty());
                        line.startYProperty().bind(previousLine.endYProperty());
                    }
                    line.endXProperty().bind(new SimpleDoubleProperty(startX - (i + 1) * length));
                    line.endYProperty().bind(new SimpleDoubleProperty(startY));
                    if(type % 10 == 1)
                        line.getStrokeDashArray().setAll(5.0, 5.0);
                    line.setMouseTransparent(true);
                    getChildren().add(line);
                    Anchor anchor = new Anchor(Color.BLACK, line.endXProperty(), line.endYProperty());
                    getChildren().add(anchor);
                }
                // Three lines straight top
                length = verticalDistance / 3;
                for (int i = 0; i < 3; i++) {
                    Line previousLine = (Line)getChildren().get(getChildren().size()-2);
                    Line line = new Line();
                    line.startXProperty().bind(previousLine.endXProperty());
                    line.startYProperty().bind(previousLine.endYProperty());
                    line.endXProperty().bind(new SimpleDoubleProperty(previousLine.getEndX()));
                    line.endYProperty().bind(new SimpleDoubleProperty(previousLine.getEndY() - length));
                    if(type % 10 == 1)
                        line.getStrokeDashArray().setAll(5.0, 5.0);
                    line.setMouseTransparent(true);
                    getChildren().add(line);
                    Anchor anchor = new Anchor(Color.BLACK, line.endXProperty(), line.endYProperty());
                    getChildren().add(anchor);
                }
                // Two lines straight left
                length = horizontalDistance / 4;
                for (int i = 0; i < 2; i++) {
                    Line line = new Line();
                    Line previousLine = (Line)getChildren().get(getChildren().size()-2);
                    line.startXProperty().bind(previousLine.endXProperty());
                    line.startYProperty().bind(previousLine.endYProperty());
                    line.endXProperty().bind(new SimpleDoubleProperty(previousLine.getEndX() - length));
                    line.endYProperty().bind(new SimpleDoubleProperty(previousLine.getEndY()));
                    if(type % 10 == 1)
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
            else {
               Anchor anchor = (Anchor)getChildren().get(i);
               anchor.setFillAndStroke(color);
            }
        }
    }
    public void setAnchorDragEnabled(boolean isDragEnabled) {
        for(int i = 0; i < getChildren().size(); i++) {
            if(getChildren().get(i) instanceof Anchor) {
                Anchor anchor = (Anchor)getChildren().get(i);
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
            setStrokeWidth(2);
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
                        getScene().setCursor(Cursor.MOVE);
                    }
                }
            });
            setOnMouseReleased(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent mouseEvent) {
                    if(isDragEnabled) {
                        getScene().setCursor(Cursor.HAND);
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
            setOnMouseEntered(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent mouseEvent) {
                    if (!mouseEvent.isPrimaryButtonDown() && isDragEnabled) {
                        getScene().setCursor(Cursor.HAND);
                    }
                }
            });
            setOnMouseExited(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent mouseEvent) {
                    if (!mouseEvent.isPrimaryButtonDown() && isDragEnabled) {
                        getScene().setCursor(Cursor.DEFAULT);
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
    
    class Head extends Polygon{
        public Head(int type) {
            switch(type) {
                // Triangle up
                case 1:
                    getPoints().addAll(new Double[]{0.0, 0.0, 20.0, 10.0, 10.0, 20.0 });
                    break;
                // Triangle right
                case 2:
                    getPoints().addAll(new Double[]{0.0, 0.0, 20.0, 10.0, 10.0, 20.0 });
                    break;
                // Triangle down
                case 3:
                    getPoints().addAll(new Double[]{0.0, 0.0, 20.0, 10.0, 10.0, 20.0 });
                    break;
                // Triangle left
                case 4:
                    getPoints().addAll(new Double[]{0.0, 0.0, 20.0, 10.0, 10.0, 20.0 });
                    break;
                // Diamond up
                case 5:
                    getPoints().addAll(new Double[]{0.0, 0.0, 20.0, 10.0, 10.0, 20.0 });
                    break;
                // Diamond right
                case 6:
                    getPoints().addAll(new Double[]{0.0, 0.0, 20.0, 10.0, 10.0, 20.0 });
                    break;
                // Diamond down
                case 7:
                    getPoints().addAll(new Double[]{0.0, 0.0, 20.0, 10.0, 10.0, 20.0 });
                    break;
                // Diamond left
                case 8:
                    getPoints().addAll(new Double[]{0.0, 0.0, 20.0, 10.0, 10.0, 20.0 });
                    break;
                // Arrow Up
                case 9:
                    getPoints().addAll(new Double[]{0.0, 0.0, 20.0, 10.0, 10.0, 20.0 });
                    break;
                // Arrow right
                case 10:
                    getPoints().addAll(new Double[]{0.0, 0.0, 20.0, 10.0, 10.0, 20.0 });
                    break;
                // Arrow down
                case 11:
                    getPoints().addAll(new Double[]{0.0, 0.0, 20.0, 10.0, 10.0, 20.0 });
                    break;
                // Arrow left
                case 12:
                    getPoints().addAll(new Double[]{0.0, 0.0, 20.0, 10.0, 10.0, 20.0 });
                    break;
            }
        }
    }
    
}
