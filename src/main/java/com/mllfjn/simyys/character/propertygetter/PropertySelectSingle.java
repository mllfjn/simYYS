package com.mllfjn.simyys.character.propertygetter;

import com.mllfjn.simyys.collections.StringGroup;
import com.mllfjn.simyys.starter.Initializer;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.TilePane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.io.Serial;
import java.io.Serializable;

public class PropertySelectSingle  extends PropertyRequire implements Serializable {
    @Serial
    private static final long serialVersionUID = 3858806336321004506L;

    private String value;
    private final StringGroup[] options;

    public PropertySelectSingle(StringGroup[] options) {
        this.options = options;
    }

    @Override
    public String getString() {
        return value;
    }

    public void setValue(int i) {
        if (options.length == 1) {
            value = options[0].values()[i];
        }

    }

    @Override
    public boolean cover(PropertyRequire pr) {
        if (pr instanceof PropertySelectSingle ps) {
            String newValue = ps.value;
            if (newValue == null) {
                return true;
            }
            for (StringGroup group : options) {
                for (String s : group.values()) {
                    if (newValue.equals(s)) {
                        this.value = newValue;
                        return true;
                    }
                }
            }
        }
        return false;
    }

    @Override
    public Node getNode(String desc, Window owner) {
        if (options.length == 1 && options[0].values().length <= 5) {
            return getSimpleComboBox(desc);
        }
        return getSelectWindow(desc, owner);
    }

    private Node getSimpleComboBox(String desc) {
        ComboBox<String> comboBox = new ComboBox<>();
        comboBox.getItems().addAll(options[0].values());
        comboBox.getSelectionModel().selectedItemProperty().addListener((obs, old, val) -> value = val);
        if (value != null && !value.isEmpty()) {
            comboBox.getSelectionModel().select(value);
        }
        HBox node = new HBox(new Label(desc), comboBox);
        node.setSpacing(10);
        return node;
    }

    private Node getSelectWindow(String desc, Window owner) {
        HBox node = new HBox();
        node.setSpacing(10);
        Label descLbl = new Label(desc);
        Label valueLbl = new Label(value);

        Button confirmBtn = new Button("点击选择");
        confirmBtn.setOnAction(e -> {
            Stage stage = new Stage();
            GridPane gp = new GridPane();
            gp.setVgap(20);
            gp.setHgap(20);
            gp.setPadding(new Insets(20));
            for (int i = 0; i < options.length; i++) {
                TilePane tp = new TilePane();
                for (String s : options[i].values()) {
                    Button button = new Button(s);
                    button.setOnAction(event -> {
                        value = s;
                        valueLbl.setText(s);
                        stage.close();
                    });
                    tp.getChildren().add(button);
                }

                gp.add(new Label(options[i].label()), 0, i);
                gp.add(tp, 1, i);
            }
            Initializer.installScale(stage, gp, 300, 500);
            stage.initOwner(owner);
            stage.initModality(Modality.WINDOW_MODAL);

            stage.showAndWait();
        });

        node.getChildren().addAll(descLbl, valueLbl, confirmBtn);
        return node;
    }
}
