package com.mllfjn.simyys.starter.propertygetter;

import com.mllfjn.simyys.customnode.StringGroup;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.TilePane;
import javafx.stage.Stage;

import java.io.Serializable;
import java.util.List;
import java.util.StringJoiner;

public class PropertySelectMulti  extends PropertyRequire implements Serializable {
    private String value;
    private final StringGroup[] options;
    public PropertySelectMulti(String desc, StringGroup[] options) {
        super(desc);
        this.options = options;
    }

    @Override
    public boolean cover(PropertyRequire pr) {
        if (pr instanceof PropertySelectMulti pm) {
            String[] values = pm.value.split(",");
            StringJoiner sj = new StringJoiner(",");
            boolean match = true;
            for (String newValue : values) {
                if (contains(newValue)) {
                    sj.add(newValue);
                } else {
                    match = false;
                }
            }
            this.value = sj.toString();
            return match;
        }
        return false;
    }

    private boolean contains(String newValue) {
        for (StringGroup option : options) {
            for (String s : option.values()) {
                if (newValue.equals(s)) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public Node getNode() {
        HBox node = new HBox();
        node.setSpacing(10);

        Label descLbl = new Label(desc);
        Label valueLbl = new Label();
        Button selectBtn = new Button("点击选择");
        selectBtn.setOnAction(e -> openSelectDialog());

        node.getChildren().addAll(descLbl, valueLbl, selectBtn);
        return node;
    }

    private void openSelectDialog() {
        Stage stage = new Stage();
        GridPane gp = new GridPane();
        gp.setHgap(10);

        List<String> currentValues = List.of(value.split(","));

        CheckBox[][] cbs = new CheckBox[options.length][];
        for (int i = 0; i < options.length; i++) {
            String[] values = options[i].values();
            cbs[i] = new CheckBox[values.length];
            TilePane tp = new TilePane();
            for (int j = 0; j < values.length; j++) {
                CheckBox cb = new CheckBox(values[j]);
                cbs[i][j] = cb;

                if (currentValues.contains(values[j])) {
                    cb.setSelected(true);
                }

                tp.getChildren().add(cb);
            }
            gp.add(new Label(options[i].label()), 0, i);
            gp.add(tp, 1, i);
        }

        Button confirmBtn = new Button("确定");
        confirmBtn.setOnAction(e -> {
            StringJoiner sj = new StringJoiner(",");
            for (int i = 0; i < cbs.length; i++) {
                for (int j = 0; j < cbs[i].length; j++) {
                    if (cbs[i][j].isSelected()) {
                        sj.add(options[i].values()[j]);
                    }
                }
            }
            value = sj.toString();
        });

        stage.showAndWait();
    }

    @Override
    public void toString(StringBuilder sb) {
        super.toString(sb);
        sb.append(desc).append(":").append(value);
    }
}
