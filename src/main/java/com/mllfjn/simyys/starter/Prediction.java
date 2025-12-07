package com.mllfjn.simyys.starter;

import com.mllfjn.simyys.character.propertygetter.PropertiesHolder;
import com.mllfjn.simyys.collections.SerializableObservableList;
import com.mllfjn.simyys.utils.Utils;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.Serializable;
import java.util.LinkedHashSet;
import java.util.Set;

public class Prediction implements Serializable {
    private SerializableObservableList<String> predictionOrder = new SerializableObservableList<>();
    private Set<String> candidateCharacterName = getUsedCharacterName();

    public void showPrediction(Stage owner, SerializableObservableList<PropertiesHolder> items) {
        Stage stage = new Stage();
        // center ListView
        ListView<String> listView = new ListView<>();
        listView.setItems(predictionOrder.getObservableList());

        listView.setCellFactory(param -> new ListCell<>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (item == null || empty) {
                    setText(null);
                } else {
                    setText("\t" + (getIndex() + 1) + "\t" + item);
                }
            }
        });

        // right controller
        Button btnAdd = new Button("添加");
        Button btnDelete = new Button("删除");
        Button btnMoveUp = new Button("上移");
        Button btnMoveDown = new Button("下移");
        Button btnClear = new Button("清空");

        VBox controller = new VBox(btnAdd, btnDelete, btnMoveUp, btnMoveDown, btnClear);

        btnAdd.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        btnDelete.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        btnMoveUp.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        btnMoveDown.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        btnClear.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);

        btnAdd.setOnAction(e -> {
            Set<String> set = getUsedCharacterName();
            if (set == null) return;

            TilePane tp = new TilePane();
            for (String s : set) {
                Button btn = new Button(s);
                btn.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
                btn.setOnAction(e1 -> predictionOrder.add(s));
                tp.getChildren().add(btn);
            }

            Stage addStage = new Stage();
            addStage.setScene(new Scene(tp));
            addStage.initModality(Modality.APPLICATION_MODAL);
            addStage.initOwner(stage);

            addStage.showAndWait();
        });

        btnDelete.setOnAction(e -> {
            int index = listView.getSelectionModel().getSelectedIndex();
            if (index >= 0 && index < list.size()) {
                list.remove(index);
                if (index < list.size()) {
                    listView.getSelectionModel().select(index);
                }
            }
        });

        btnMoveUp.setOnAction(e -> {
            int index = listView.getSelectionModel().getSelectedIndex();
            if (index > 0 && index < list.size()) {
                list.add(index - 1, list.remove(index));
                listView.getSelectionModel().select(index - 1);
            }
        });
        btnMoveDown.setOnAction(e -> {
            int index = listView.getSelectionModel().getSelectedIndex();
            if (index < list.size() - 1 && index >= 0) {
                list.add(index + 1, list.remove(index));
                listView.getSelectionModel().select(index + 1);
            }
        });
        btnClear.setOnAction(e -> list.clear());

        BorderPane borderPane = new BorderPane();
        borderPane.setCenter(listView);
        borderPane.setTop(tf);
        borderPane.setRight(controller);

        stage.initModality(Modality.APPLICATION_MODAL);
        stage.initOwner(owner);
        stage.setScene(new Scene(borderPane));
        stage.showAndWait();
    }

    private Set<String> getUsedCharacterName() {
        Set<String> set = new LinkedHashSet<>();
        for (PropertiesHolder item : items) {
            set.add(item.name);
        }
        if (set.isEmpty()) {
            Utils.information("请先添加角色");
            return null;
        }
        return set;
    }
}
