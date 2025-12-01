package com.mllfjn.simyys.character.propertygetter;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.scene.Node;
import javafx.scene.control.CheckBox;
import javafx.stage.Window;

import java.io.Serializable;

public class PropertyCheck extends PropertyRequire implements Serializable {
    private boolean value;
    private transient SimpleBooleanProperty property;

    @Override
    public boolean cover(PropertyRequire pr) {
        if (pr instanceof PropertyCheck pc) {
            this.value = pc.value;
            return true;
        }
        return false;
    }

    public PropertyCheck setValue(boolean value) {
        this.value = value;
        return this;
    }

    public boolean getValue() {
        return value;
    }

    public SimpleBooleanProperty getProperty() {
        if (property == null) {
            property = new SimpleBooleanProperty(value);
            property.addListener((obs, old, val) -> value = val);
        }
        return property;
    }

    @Override
    public boolean getBoolean() {
        return value;
    }

    @Override
    public Node getNode(String desc, Window owner) {
        CheckBox node = new CheckBox(desc);
        node.setMnemonicParsing(false);
        if (value) {
            node.setSelected(true);
        }
        node.selectedProperty().addListener((obs, old, val) -> {
            value = val;
            if (property != null) {
                property.set(val);
            }
        });

        return node;
    }
}
