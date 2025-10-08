package com.mllfjn.simyys.character.propertygetter;

import com.mllfjn.simyys.utils.Utils;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;

import java.io.Serializable;

public class PropertyInput extends PropertyRequire implements Serializable {
    private String value = "";
    private transient SimpleStringProperty property;
    public PropertyInput setValue(String value) {
        this.value = value;
        return this;
    }
    public String getValue() {
        return value;
    }
    public SimpleStringProperty getProperty() {
        if (property == null) {
            property = new SimpleStringProperty(value);
            property.addListener((obs, old, val) -> value = val);
        }
        return property;
    }

    @Override
    public boolean cover(PropertyRequire pr) {
        if (pr instanceof PropertyInput pi) {
            if (property != null) {
                property.set(pi.value);
            }
            this.value = pi.value;
            return true;
        }
        return false;
    }

    @Override
    public Node getNode(String desc) {
        HBox node = new HBox();
        node.setSpacing(10);
        Label label = new Label(desc);

        TextField tf = new TextField(value);
        tf.textProperty().addListener((obs, old, val) -> value = val);

        node.getChildren().addAll(label, tf);
        return node;
    }

    @Override
    public double getDouble() {
        return Utils.parseDoubleOrDefault(value, 0.0);
    }

    @Override
    public int getInt() {
        return Utils.parseIntOrDefault(value, 0);
    }

    @Override
    public void toString(StringBuilder sb) {
        sb.append(value == null || value.isEmpty() ? "无" : value);
    }
}
