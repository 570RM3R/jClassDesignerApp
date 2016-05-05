/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jcd;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListCell;
import javafx.scene.control.TableCell;

/**
 *
 * @author Saeid
 */
public class VariableCellComboBox extends TableCell<Variable, String> {    
    final ObservableList<String> accessOption;
    ComboBox<String> comboBox;

    public VariableCellComboBox() {
        accessOption = FXCollections.observableArrayList("public", "protected", "<no modifier>", "private");
    }

    @Override
    public void startEdit() {
        if (!isEmpty()) {
            super.startEdit();
            createComboBox();
            setText(null);
            setGraphic(comboBox);
        }
    }

    @Override
    public void cancelEdit() {
        super.cancelEdit();
        setText(getString());
        setGraphic(null);
    }

    @Override
    public void updateItem(String item, boolean empty) {
        super.updateItem(item, empty);
        if (empty) {
            setText(null);
            setGraphic(null);
        } 
        else {
            if (isEditing()) {
                if (comboBox != null)
                    comboBox.setValue(item);
                setText(getString());
                setGraphic(comboBox);
            }
            else {
                setText(getString());
                setGraphic(null);
            }
        }
    }

    private void createComboBox() {
        comboBox = new ComboBox<>(accessOption);
        comboBoxConverter(comboBox);
        comboBox.valueProperty().set(getString());
        comboBox.setMinWidth(this.getWidth() - this.getGraphicTextGap() * 2);
        comboBox.setOnAction((e) -> {
            commitEdit(comboBox.getSelectionModel().getSelectedItem());
        });
    }

    private void comboBoxConverter(ComboBox<String> comboBox) {comboBox.setCellFactory((c) -> {
        return new ListCell<String>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (item == null || empty) {
                    setText(null);
                }
                else {
                    setText(item);
                }
            }
        };
    });}
    
    private String getString() {
        return getItem() == null ? "" : getItem();
    }
}
