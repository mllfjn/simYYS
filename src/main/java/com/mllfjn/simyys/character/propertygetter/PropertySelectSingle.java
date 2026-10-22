package com.mllfjn.simyys.character.propertygetter;

import com.mllfjn.simyys.starter.Initializer;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Button;
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
import java.util.concurrent.atomic.AtomicInteger;

public class PropertySelectSingle extends PropertySelect implements Serializable {
    @Serial
    private static final long serialVersionUID = 3858806336321004506L;

    public PropertySelectSingle(String optionKey) {
        super(optionKey);
    }

    @Override
    public boolean cover(PropertyRequire pr) {
        if (pr instanceof PropertySelectSingle ps) {
            String newValue = ps.value;
            if (newValue == null) {
                return true;
            }

            if (contains(newValue)) {
                this.value = newValue;
                return true;
            }
        }
        return false;
    }

    @Override
    public PropertyRequire setValue(String value) {
        if (contains(value)) {
            this.value = value;
        }
        return this;
    }

    @Override
    public Node getNode(String desc, Window owner) {
        return getSelectWindow(desc, owner);
    }

    private Node getSelectWindow(String desc, Window owner) {
        HBox node = new HBox();
        node.setSpacing(10);
        Label descLbl = new Label(desc);
        Label valueLbl = new Label(value);

        Button confirmBtn = new Button("点击选择");
        confirmBtn.setOnAction(_ -> {
            Stage stage = new Stage();
            GridPane gp = new GridPane();
            gp.setVgap(20);
            gp.setHgap(20);
            gp.setPadding(new Insets(20));
            Set<Map.Entry<String, List<String>>> optionEntries = getOptions().entrySet();
            AtomicInteger i = new AtomicInteger();
            optionEntries.forEach(entry -> {
                TilePane tp = new TilePane();
                for (String s : entry.getValue()) {
                    Button button = new Button(s);
                    button.setOnAction(_ -> {
                        value = s;
                        valueLbl.setText(s);
                        stage.close();
                    });
                    tp.getChildren().add(button);
                }

                Label label = new Label(entry.getKey());
                // 小了契灵会变成"..."???
                label.setPrefWidth(70);
                gp.add(label, 0, i.get());
                gp.add(tp, 1, i.get());
                i.getAndIncrement();
            });
            Initializer.installScale(stage, gp, 300, 500);
            stage.initOwner(owner);
            stage.initModality(Modality.WINDOW_MODAL);

            stage.showAndWait();
        });

        node.getChildren().addAll(descLbl, valueLbl, confirmBtn);
        return node;
    }
}
