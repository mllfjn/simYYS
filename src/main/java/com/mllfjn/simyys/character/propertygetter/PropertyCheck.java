package com.mllfjn.simyys.character.propertygetter;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.scene.Node;
import javafx.scene.control.CheckBox;
import javafx.stage.Window;

import java.io.Serial;
import java.io.Serializable;

public class PropertyCheck extends PropertyRequire implements Serializable {
    @Serial
    private static final long serialVersionUID = -4955169577216666265L;

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

    @Override
    public int getInt() {
        return value ? 1 : 0;
    }

    public SimpleBooleanProperty getProperty() {
        if (property == null) {
            property = new SimpleBooleanProperty(value);
            property.addListener((_, _, val) -> value = val);
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
        node.selectedProperty().addListener((_, _, val) -> {
            value = val;
            if (property != null) {
                property.set(val);
            }
        });

        return node;
    }
}
