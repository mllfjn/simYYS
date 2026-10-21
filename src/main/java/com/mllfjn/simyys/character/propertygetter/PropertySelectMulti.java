package com.mllfjn.simyys.character.propertygetter;

import com.mllfjn.simyys.starter.Initializer;
import javafx.beans.property.SimpleStringProperty;
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
import java.util.Map;
import java.util.Set;
import java.util.StringJoiner;
import java.util.concurrent.atomic.AtomicInteger;

public class PropertySelectMulti extends PropertySelect implements Serializable {
    @Serial
    private static final long serialVersionUID = -9067275274183631003L;
    private static final String SPLIT_CHAR = ",";

    private transient SimpleStringProperty property;

    public PropertySelectMulti(String optionKey) {
        super(optionKey);
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
            setValue(sj.toString());
            return match;
        }
        return false;
    }

    @Override
    public PropertyRequire setValue(String s) {
        if (property == null) {
            value = s;
        } else {
            property.setValue(s);
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
    public Node getNode(String desc, Window owner) {
        HBox node = new HBox();
        node.setSpacing(10);

        Label descLbl = new Label(desc);
        Label valueLbl = new Label();
        valueLbl.textProperty().bindBidirectional(getProperty());
        Button selectBtn = new Button("点击选择");
        selectBtn.setOnAction(_ -> openSelectDialog(valueLbl, owner));

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

        Set<Map.Entry<String, List<String>>> entries = getOptions().entrySet();
        CheckBox[][] cbs = new CheckBox[entries.size()][];
        AtomicInteger i = new AtomicInteger(0);
        for (Map.Entry<String, List<String>> entry : entries) {
            List<String> values = entry.getValue();
            cbs[i.get()] = new CheckBox[values.size()];
            TilePane tp = new TilePane();

            for (int j = 0; j < values.size(); j++) {
                CheckBox cb = new CheckBox(values.get(j));
                cb.setPrefWidth(75);
                cbs[i.get()][j] = cb;

                if (currentValues != null && currentValues.contains(values.get(j))) {
                    cb.setSelected(true);
                }

                tp.getChildren().add(cb);
            }

            gp.add(new Label(entry.getKey()), 0, i.get());
            gp.add(tp, 1, i.get());

            i.getAndIncrement();
        }

        Button confirmBtn = new Button("确定");
        confirmBtn.setOnAction(_ -> {
            StringJoiner sj = new StringJoiner(SPLIT_CHAR);
            for (CheckBox[] cb : cbs) {
                for (CheckBox checkBox : cb) {
                    if (checkBox.isSelected()) {
                        sj.add(checkBox.getText());
                    }
                }
            }
            setValue(sj.toString());
            valueLbl.setText(value);
            stage.close();
        });

        gp.add(confirmBtn, 1, entries.size());
        gp.setPadding(new Insets(20));
        gp.setVgap(20);

        Initializer.installScale(stage, gp, 600, 500);

        stage.initModality(Modality.WINDOW_MODAL);
        stage.initOwner(owner);
        stage.showAndWait();
    }
}
