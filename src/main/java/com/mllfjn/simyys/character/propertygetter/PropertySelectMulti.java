package com.mllfjn.simyys.character.propertygetter;

import com.mllfjn.simyys.collections.StringGroup;
import com.mllfjn.simyys.starter.Initializer;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.TilePane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;
import java.util.StringJoiner;

public class PropertySelectMulti  extends PropertyRequire implements Serializable {
    @Serial
    private static final long serialVersionUID = -9067275274183631003L;
    private static final String SPLIT_CHAR = ",";

    private String value;
    private final StringGroup[] options;

    public PropertySelectMulti(StringGroup[] options) {
        this.options = options;
    }

    @Override
    public boolean cover(PropertyRequire pr) {
        if (pr instanceof PropertySelectMulti pm) {
            if (pm.value == null || pm.value.isEmpty()) {
                return true;
            }
            String[] values = pm.value.split(SPLIT_CHAR);
            StringJoiner sj = new StringJoiner(SPLIT_CHAR);
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

    @Override
    public String getString() {
        return value;
    }

    @Override
    public PropertyRequire setValue(String s) {
        value = s;
        return this;
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
    public Node getNode(String desc, Window owner) {
        HBox node = new HBox();
        node.setSpacing(10);

        Label descLbl = new Label(desc);
        Label valueLbl = new Label(value);
        Button selectBtn = new Button("点击选择");
        selectBtn.setOnAction(e -> openSelectDialog(valueLbl, owner));

        node.getChildren().addAll(descLbl, valueLbl, selectBtn);
        return node;
    }

    private void openSelectDialog(Label valueLbl, Window owner) {
        Stage stage = new Stage();
        GridPane gp = new GridPane();
        gp.setHgap(10);

        List<String> currentValues = null;
        if (value != null && !value.isEmpty()) {
            currentValues = List.of(value.split(SPLIT_CHAR));
        }

        CheckBox[][] cbs = new CheckBox[options.length][];
        for (int i = 0; i < options.length; i++) {
            String[] values = options[i].values();
            cbs[i] = new CheckBox[values.length];
            TilePane tp = new TilePane();
            for (int j = 0; j < values.length; j++) {
                CheckBox cb = new CheckBox(values[j]);
                cb.setPrefWidth(75);
                cbs[i][j] = cb;

                if (currentValues != null && currentValues.contains(values[j])) {
                    cb.setSelected(true);
                }

                tp.getChildren().add(cb);
            }
            gp.add(new Label(options[i].label()), 0, i);
            gp.add(tp, 1, i);
        }

        Button confirmBtn = new Button("确定");
        confirmBtn.setOnAction(e -> {
            StringJoiner sj = new StringJoiner(SPLIT_CHAR);
            for (int i = 0; i < cbs.length; i++) {
                for (int j = 0; j < cbs[i].length; j++) {
                    if (cbs[i][j].isSelected()) {
                        sj.add(options[i].values()[j]);
                    }
                }
            }
            value = sj.toString();
            valueLbl.setText(value);
            stage.close();
        });

        gp.add(confirmBtn, 1, options.length);
        gp.setPadding(new Insets(20));
        gp.setVgap(20);

        Initializer.installScale(stage, gp, 600, 500);

        stage.initModality(Modality.WINDOW_MODAL);
        stage.initOwner(owner);
        stage.showAndWait();
    }
}
