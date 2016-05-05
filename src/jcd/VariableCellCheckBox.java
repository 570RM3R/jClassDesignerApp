/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jcd;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.TableCell;

/**
 *
 * @author Saeid
 */
public class VariableCellCheckBox extends TableCell<Variable, String> {    
    CheckBox checkBox;

    public VariableCellCheckBox() {
        checkBox = new CheckBox();
        checkBox.setDisable(true);
        checkBox.selectedProperty().addListener(new ChangeListener<Boolean> () {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if(isEditing())
                    commitEdit(checkBox.isSelected() ? "true" : "false");
            }
        });
        this.setGraphic(checkBox);
        this.setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
        this.setEditable(true);
    }

    @Override
    public void startEdit() {
        super.startEdit();
        if (isEmpty()) {
            return;
        }
        checkBox.setDisable(false);
        checkBox.requestFocus();
    }

    @Override
        public void cancelEdit() {
            super.cancelEdit();
            checkBox.setDisable(true);
        }
    @Override
    public void commitEdit(String value) {
        super.commitEdit(value);
        checkBox.setDisable(true);
    }
    @Override
    public void updateItem(String item, boolean empty) {
        super.updateItem(item, empty);
        if (!isEmpty()) {
            checkBox.setSelected(item.equals("true"));
        }
    }
}
