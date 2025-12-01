package com.mllfjn.simyys.character.propertygetter;

import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.stage.Window;

public class PropertyMultiInput extends PropertyRequire {
    private final String[] values;
    private final String[] desc;

    public PropertyMultiInput(String[] desc) {
        this.values = new String[desc.length];
        this.desc = desc;
    }

    public PropertyMultiInput setValue(int index, String value) {
        values[index] = value;
        return this;
    }

    public String[] getValues() {
        return values;
    }

    @Override
    public Node getNode(String desc, Window owner) {
        Label label = new Label(desc);

        HBox node = new HBox(label);
        node.setSpacing(10);

        for (int i = 0; i < values.length; i++) {
            int ii = i;
            TextField tf = new TextField(values[i]);
            // 当输入框内容为空时显示信息
            tf.setPromptText(this.desc[i]);
            tf.textProperty().addListener((obs, old, val) -> values[ii] = val);
            node.getChildren().add(tf);
        }

        return node;
    }

    @Override
    public boolean cover(PropertyRequire pr) {
        if (pr instanceof PropertyMultiInput pmi && pmi.values.length == values.length) {
            System.arraycopy(pmi.values, 0, values, 0, values.length);
            return true;
        }
        return false;
    }
}
