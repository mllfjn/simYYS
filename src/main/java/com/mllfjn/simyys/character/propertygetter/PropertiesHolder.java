package com.mllfjn.simyys.character.propertygetter;

import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.Serializable;
import java.util.Map;

public record PropertiesHolder(String name, PropertiesMap map) implements Serializable {
    public void show(Stage owner) {
        Stage stage = new Stage();

        TabPane tabPane = new TabPane();
        Tab tabProperty = new Tab("角色属性", getPropertyPane());
        Tab tabSkill = new Tab("切换技能");
        Tab tabFlag = new Tab("红绿标");

        tabProperty.setClosable(false);
        tabSkill.setClosable(false);
        tabFlag.setClosable(false);

        tabPane.getTabs().addAll(tabProperty, tabSkill, tabFlag);

        stage.setTitle(name);
        stage.initOwner(owner);
        stage.initModality(Modality.WINDOW_MODAL);
        stage.setWidth(600);
        stage.setHeight(800);
        stage.setScene(new Scene(tabPane));
        stage.showAndWait();
    }

    private Node getPropertyPane() {
        VBox vb = new VBox();
        vb.setPadding(new Insets(10, 20, 10, 20));
        vb.setSpacing(10);

        for (Map.Entry<String, PropertyRequire> entry : map.entrySet()) {
            vb.getChildren().add(entry.getValue().getNode(entry.getKey()));
        }
        /*for (PropertyRequire pr : map().values()) {
            vb.getChildren().add(pr.getNode());
        }*/

        return vb;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("名称:").append(name);

        for (Map.Entry<String, PropertyRequire> entry : map().entrySet()) {
            sb.append(entry.getKey()).append(":");
            entry.getValue().toString(sb);
            sb.append("\t");
        }

        return sb.toString();
    }
}