package com.mllfjn.simyys.character.propertygetter;

import javafx.scene.Node;
import javafx.scene.control.CheckBox;

import java.io.Serializable;

public class PropertyCheck extends PropertyRequire implements Serializable {
    private boolean value;

    @Override
    public boolean cover(PropertyRequire pr) {
        if (pr instanceof PropertyCheck pc) {
            this.value = pc.value;
            return true;
        }
        return false;
    }

    public void setValue(boolean value) {
        this.value = value;
    }

    public boolean getValue() {
        return value;
    }

    @Override
    public Node getNode(String desc) {
        CheckBox node = new CheckBox(desc);
        node.setMnemonicParsing(false);
        if (value) {
            node.setSelected(true);
        }
        node.selectedProperty().addListener((obs, old, val) -> value = val);

        return node;
    }

    @Override
    public void toString(StringBuilder sb) {
        if (value) {
            sb.append("是");
        } else {
            sb.append("否");
        }
    }
}
