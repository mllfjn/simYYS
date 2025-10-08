package com.mllfjn.simyys.character.propertygetter;

import com.mllfjn.simyys.utils.Utils;
import com.mllfjn.simyys.character.PropertyKey;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.DoubleBinding;
import javafx.beans.property.SimpleStringProperty;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.Serializable;
import java.util.Map;

public class PropertiesHolder implements Serializable {
    public final String name;
    public final PropertiesMap map;
    public final Map<Integer, Integer> lockSKill;
    private transient SimpleStringProperty property;

    public transient DoubleBinding totalAttackProperty;
    public PropertiesHolder(String name, PropertiesMap map, Map<Integer, Integer> lockSKill) {
        this.name = name;
        this.map = map;
        this.lockSKill = lockSKill;
    }
    public void show(Stage owner) {
        Stage stage = new Stage();

        TabPane tabPane = new TabPane();
        Tab tabProperty = new Tab("角色属性", getPropertyPane());
        Tab tabSkill = new Tab("切换技能", getLockSkillPane());
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
    public SimpleStringProperty getNameProperty() {
        if (property == null) {
            property = new SimpleStringProperty(name);
        }
        return property;
    }
    public DoubleBinding getTotalAttack() {
        if (totalAttackProperty == null) {
            SimpleStringProperty baseAttack = ((PropertyInput)map.get(PropertyKey.GENERAL_BASE_ATTACK_KEY)).getProperty();
            SimpleStringProperty addAttack = ((PropertyInput)map.get(PropertyKey.GENERAL_YU_HUN_ATTACK_KEY)).getProperty();

            totalAttackProperty = Bindings.createDoubleBinding(() -> Utils.parseDoubleOrDefault(
                    baseAttack.getValue(), 0.0) + Utils.parseDoubleOrDefault(addAttack.getValue(), 0.0)
                    , baseAttack, addAttack);
        }
        return totalAttackProperty;
    }
    private Node getPropertyPane() {
        VBox vb = new VBox();
        vb.setPadding(new Insets(10, 20, 10, 20));
        vb.setSpacing(10);

        for (Map.Entry<String, PropertyRequire> entry : map.entrySet()) {
            vb.getChildren().add(entry.getValue().getNode(entry.getKey()));
        }

        return vb;
    }
    private Node getLockSkillPane() {
//        VBox vb = new VBox();
//        vb.setPadding(new Insets(10, 20, 10, 20));
//        vb.setSpacing(10);
//
//        for (Map.Entry<String, PropertyRequire> entry : map.entrySet()) {
//            vb.getChildren().add(entry.getValue().getNode(entry.getKey()));
//        }
//
//        return vb;


        BorderPane bp = new BorderPane();
//        bp.setCenter(tableSkill);
        return bp;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("名称:").append(name);

        for (Map.Entry<String, PropertyRequire> entry : map.entrySet()) {
            sb.append(entry.getKey()).append(":");
            entry.getValue().toString(sb);
            sb.append("\t");
        }

        return sb.toString();
    }
}