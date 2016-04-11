/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jcd;
import javafx.scene.Parent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

/**
 *
 * @author Saeid
 */
public class Diagram extends Parent{
    Rectangle nameSection;
    Rectangle variableSection;
    Rectangle methodSection;
    Text nameText;
    Text variableText;
    Text methodText;
    String packageName;
    boolean isInterface;

    public Diagram(double x, double y, boolean isInterface) {
        nameSection = new Rectangle(125, 30);
        nameSection.setStroke(Color.BLACK);
        nameSection.setFill(Color.web("#e0eae1"));
        nameSection.setX(x - 100);
        nameSection.setY(y - 20);
        nameText = new Text();
        nameText.setX(nameSection.getX() + 6);
        nameText.setY(nameSection.getY() + 15);
        if(isInterface)
            nameText.setText("<<Interface>>");
        getChildren().addAll(nameSection, nameText);
        this.isInterface = isInterface;
    }
    public Rectangle getNameSection() {
        return nameSection;
    }
    public Rectangle getVariableSection() {
        return variableSection;
    }
    public Rectangle getMethodSection() {
        return methodSection;
    }
    
    public Text getNameText() {
        return nameText;
    }
    
    public Text getVariableText() {
        return variableText;
    }
    
    public Text getMethodText() {
        return methodText;
    }
    public String getPackageText() {
        return packageName;
    }
    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }
    
    public void addVariable() {
        if(variableSection == null){
            variableSection = new Rectangle(125, 30);
            variableSection.setStroke(Color.BLUE);
            variableSection.setFill(Color.web("#e0eae1"));
            variableSection.setX(nameSection.getX());
            variableSection.setY(nameSection.getY() + 30);
            if(methodSection != null) {
                methodSection.setY(variableSection.getY() + variableSection.getHeight());
                methodText.setY(methodSection.getY() + 5);
            }
            variableText = new Text();
            variableText.setX(variableSection.getX() + 6);
            variableText.setY(variableSection.getY() + 5);
            getChildren().addAll(variableSection, variableText);
        }
        else {
            if(!variableText.getText().equals("")) {
                variableSection.setHeight(variableSection.getHeight() + 15.4);
                if(methodSection != null) {
                    methodSection.setY(methodSection.getY() + 15.4);
                    methodText.setY(methodSection.getY() + 5);
                }
            }
            variableText.setText(variableText.getText() + "\nnew variable");
        }
    }
    
    public void addMethod() {
        if(methodSection == null){
            methodSection = new Rectangle(125, 30);
            methodSection.setStroke(Color.BLUE);
            methodSection.setFill(Color.web("#e0eae1"));
            methodSection.setX(nameSection.getX());
            if(variableSection == null)
                methodSection.setY(nameSection.getY() + nameSection.getHeight());
            else
                methodSection.setY(nameSection.getY() + nameSection.getHeight() + variableSection.getHeight());
            methodText = new Text();
            methodText.setX(methodSection.getX() + 6);
            methodText.setY(methodSection.getY() + 5);
            getChildren().addAll(methodSection, methodText);
        }
        else {
            if(!methodText.getText().equals(""))
                methodSection.setHeight(methodSection.getHeight() + 15.4);
            methodText.setText(methodText.getText() + "\nnew method");
        }
    }
    
}
