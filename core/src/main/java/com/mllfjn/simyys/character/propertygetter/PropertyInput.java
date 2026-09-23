package com.mllfjn.simyys.character.propertygetter;

import com.mllfjn.simyys.utils.Utils;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.stage.Window;

import java.io.Serial;
import java.io.Serializable;

public class PropertyInput extends PropertyRequire implements Serializable {
    @Serial
    private static final long serialVersionUID = -629409598346660239L;

    private String value = "";
    private transient SimpleStringProperty property;

    @Override
    public PropertyRequire setValue(String value) {
        if (property != null) {
            property.setValue(value);
        } else {
            this.value = value;
        }
        return this;
    }

    @Override
    public PropertyRequire setValue(double value) {
        String s = String.valueOf(value);
        if (property != null) {
            property.setValue(s);
        } else {
            this.value = s;
        }
        return this;
    }

    public SimpleStringProperty getProperty() {
        if (property == null) {
            property = new SimpleStringProperty(value);
            property.addListener((_, _, val) -> value = val);
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
    public Node getNode(String desc, Window owner) {
        HBox node = new HBox();
        node.setSpacing(10);
        Label label = new Label(desc);

        TextField tf = new TextField(value);
        tf.textProperty().bindBidirectional(getProperty());

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
}
