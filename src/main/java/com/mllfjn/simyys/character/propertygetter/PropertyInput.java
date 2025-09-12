package com.mllfjn.simyys.character.propertygetter;

import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;

import java.io.Serializable;

public class PropertyInput extends PropertyRequire implements Serializable {
    private String value = "";
    public PropertyInput setValue(String value) {
        this.value = value;
        return this;
    }
    public String getValue() {
        return value;
    }

    @Override
    public boolean cover(PropertyRequire pr) {
        if (pr instanceof PropertyInput pi) {
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
        try {
            return Double.parseDouble(value);
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    @Override
    public int getInt() {
        return Integer.parseInt(value);
    }

    @Override
    public void toString(StringBuilder sb) {
        sb.append(value == null || value.isEmpty() ? "无" : value);
    }
}
