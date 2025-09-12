package com.mllfjn.simyys.starter.propertygetter;

import com.mllfjn.simyys.customnode.StringGroup;
import javafx.collections.FXCollections;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.TilePane;
import javafx.stage.Stage;

import java.io.Serializable;
import java.util.List;

public class PropertySelectSingle  extends PropertyRequire implements Serializable {
    private final StringGroup[] options;
    private String value;
    public PropertySelectSingle(String desc, StringGroup[] options) {
        super(desc);
        this.options = options;
    }

    @Override
    public boolean cover(PropertyRequire pr) {
        if (pr instanceof PropertySelectSingle ps) {
            String newValue = ps.value;
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
    public Node getNode() {
        if (options.length == 1 && options[0].values().length <= 5) {
            return getSimpleComboBox();
        }
        return getSelectWindow();
    }

    private Node getSimpleComboBox() {
        ComboBox<String> comboBox = new ComboBox<>();
        comboBox.getItems().addAll(options[0].values());
        comboBox.getSelectionModel().select(0);
        HBox node = new HBox(new Label(desc), comboBox);
        node.setSpacing(10);
        return node;
    }

    private Node getSelectWindow() {
        HBox node = new HBox();
        node.setSpacing(10);
        Label descLbl = new Label(desc);
        Label valueLbl = new Label(value);

        Button confirmBtn = new Button("点击选择");
        confirmBtn.setOnAction(e -> {
            Stage stage = new Stage();
            GridPane gp = new GridPane();
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
            stage.showAndWait();
        });

        node.getChildren().addAll(descLbl, valueLbl, confirmBtn);
        return node;
    }

    @Override
    public void toString(StringBuilder sb) {
        super.toString(sb);
        sb.append(desc).append(":").append(value);
    }
}
