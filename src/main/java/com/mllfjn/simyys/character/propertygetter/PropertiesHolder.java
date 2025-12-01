package com.mllfjn.simyys.character.propertygetter;

import com.mllfjn.simyys.utils.DecimalFormatUtil;
import com.mllfjn.simyys.utils.Utils;
import com.mllfjn.simyys.character.PropertyKey;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.StringBinding;
import javafx.beans.property.ReadOnlyIntegerWrapper;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.io.Serializable;
import java.util.AbstractMap;
import java.util.Map;

public class PropertiesHolder implements Serializable {
    public final String name;
    public final PropertiesMap propertiesMap;
    public final Map<Integer, Integer> lockSkillMap;
    public final Map<Integer, FlagChangeInfo> flagChangeMap;

    private transient SimpleStringProperty nameProperty;
    private transient StringBinding totalAttackProperty;

    public PropertiesHolder(String name, PropertiesMap propertiesMap, Map<Integer, Integer> lockSkillMap, Map<Integer, FlagChangeInfo> flagChangeMap) {
        this.name = name;
        this.propertiesMap = propertiesMap;
        this.lockSkillMap = lockSkillMap;
        this.flagChangeMap = flagChangeMap;
    }

    public void show(Window owner) {
        Stage stage = new Stage();

        TabPane tabPane = new TabPane();
        Tab tabProperty = new Tab("角色属性", getPropertyPane(stage));
        Tab tabSkill = new Tab("切换技能", getLockSkillPane(stage));
        Tab tabFlag = new Tab("红绿标", getFlagPane(stage));

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
        if (nameProperty == null) {
            nameProperty = new SimpleStringProperty(name);
        }
        return nameProperty;
    }

    public StringBinding getTotalAttack() {
        if (totalAttackProperty == null) {
            SimpleStringProperty baseAttack = ((PropertyInput) propertiesMap.get(PropertyKey.GENERAL_BASE_ATTACK_KEY))
                    .getProperty();
            SimpleStringProperty addAttack = ((PropertyInput) propertiesMap.get(PropertyKey.GENERAL_YU_HUN_ATTACK_KEY))
                    .getProperty();

            totalAttackProperty = Bindings.createStringBinding(() -> DecimalFormatUtil.df_0_2.format(
                            Utils.parseDoubleOrDefault(baseAttack.getValue(), 0)
                                    + Utils.parseDoubleOrDefault(addAttack.getValue(), 0))
                    , baseAttack, addAttack);
        }
        return totalAttackProperty;
    }

    private Node getPropertyPane(Window owner) {
        VBox vb = new VBox();
        vb.setPadding(new Insets(10, 20, 10, 20));
        vb.setSpacing(10);

        for (Map.Entry<String, PropertyRequire> entry : propertiesMap.entrySet()) {
            vb.getChildren().add(entry.getValue().getNode(entry.getKey(), owner));
        }

        return vb;
    }

    private Node getLockSkillPane(Window owner) {

        TableView<Map.Entry<Integer, Integer>> tableView = new TableView<>();
        tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        TableColumn<Map.Entry<Integer, Integer>, Integer> keyColumn = new TableColumn<>("行动回合");
        TableColumn<Map.Entry<Integer, Integer>, Integer> valueColumn = new TableColumn<>("锁定技能");

        keyColumn.setCellValueFactory(data ->
                new ReadOnlyIntegerWrapper(data.getValue().getKey()).getReadOnlyProperty().asObject());
        valueColumn.setCellValueFactory(data ->
                new ReadOnlyIntegerWrapper(data.getValue().getValue()).getReadOnlyProperty().asObject());

        tableView.getColumns().add(keyColumn);
        tableView.getColumns().add(valueColumn);

        tableView.setItems(FXCollections.observableArrayList(lockSkillMap.entrySet()));

        // 按照行动回合排序
        keyColumn.setSortType(TableColumn.SortType.ASCENDING);
        tableView.getSortOrder().add(keyColumn);
        valueColumn.setSortable(false);

        Button btnAdd = new Button("添加");
        Button btnDelete = new Button("删除");

        btnAdd.setOnAction(e -> {
            TextField tfKey = new TextField();
            TextField tfValue = new TextField();
            Button btnConfirm = new Button("确定");

            btnConfirm.setOnAction(e1 -> {
                int key = Utils.parseIntOrDefault(tfKey.getText(), 0);
                int value = Utils.parseIntOrDefault(tfValue.getText(), 0);
                if (!lockSkillMap.containsKey(key)) {
                    lockSkillMap.put(key, value);
                    tableView.getItems().add(new AbstractMap.SimpleEntry<>(key, value));
                    tableView.sort();
                    tfKey.clear();
                    tfValue.clear();
                } else {
                    Utils.information("添加失败：该行动回合已设置锁定技能");
                }

                tfKey.requestFocus();
            });
            // 行动回合输入框回车跳到锁定技能输入框
            tfKey.setOnKeyPressed(e1 -> {
                if (e1.getCode() == KeyCode.ENTER) {
                    tfValue.requestFocus();
                }
            });
            // 技能输入框回车确定
            tfValue.setOnKeyPressed(e1 -> {
                if (e1.getCode() == KeyCode.ENTER) {
                    btnConfirm.fire();
                }
            });

            HBox hBox = new HBox(tfKey, tfValue, btnConfirm);
            Stage stage = new Stage();
            stage.initOwner(owner);
            stage.initModality(Modality.WINDOW_MODAL);
            stage.setScene(new Scene(hBox));
            stage.showAndWait();
        });
        btnDelete.setOnAction(e -> {
            Map.Entry<Integer, Integer> selectedItem = tableView.getSelectionModel().getSelectedItem();
            if (selectedItem != null) {
                tableView.getItems().remove(selectedItem);
                lockSkillMap.remove(selectedItem.getKey());
            }
        });

        TilePane controller = new TilePane(btnAdd, btnDelete);
        controller.setVgap(20);

        BorderPane border = new BorderPane();
        border.setCenter(tableView);
        border.setRight(controller);

        return border;
    }

    private Node getFlagPane(Window owner) {
        /*ObservableList<Map.Entry<Integer, FlagChangeInfo>> items = FXCollections.observableArrayList(flagChangeMap.entrySet());
        ListView<Map.Entry<Integer, FlagChangeInfo>> listView = new ListView<>(items);
        listView.setCellFactory(lv -> new ListCell<>() {
            @Override
            protected void updateItem(Map.Entry<Integer, FlagChangeInfo> entry, boolean empty) {
                super.updateItem(entry, empty);
                if (entry == null || empty) {
                    setText(null);
                } else {
                    FlagChangeInfo value = entry.getValue();
                    setText(entry.getKey() + "\t" + value.flagType + "\t" + value.target);
                }
            }
        });*/
        // 创建表格
        ObservableList<Map.Entry<Integer, FlagChangeInfo>> items = FXCollections.observableArrayList(flagChangeMap.entrySet());
        TableView<Map.Entry<Integer, FlagChangeInfo>> tableView = new TableView<>(items);
        tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        TableColumn<Map.Entry<Integer, FlagChangeInfo>, Integer> keyColumn = new TableColumn<>("行动回合");
        TableColumn<Map.Entry<Integer, FlagChangeInfo>, String> flagColumn = new TableColumn<>("标记类型");
        TableColumn<Map.Entry<Integer, FlagChangeInfo>, String> roundColumn = new TableColumn<>("标记目标");

        keyColumn.setCellValueFactory(data ->
                new ReadOnlyIntegerWrapper(data.getValue().getKey()).getReadOnlyProperty().asObject());

        flagColumn.setCellValueFactory(data ->
                new ReadOnlyStringWrapper(data.getValue().getValue().flagType.type));

        roundColumn.setCellValueFactory(data ->
                new ReadOnlyStringWrapper(String.valueOf(data.getValue().getValue().target)));

        tableView.getColumns().add(keyColumn);
        tableView.getColumns().add(flagColumn);
        tableView.getColumns().add(roundColumn);

        // 排序
        tableView.getSortOrder().add(keyColumn);
        keyColumn.setSortType(TableColumn.SortType.ASCENDING);
        flagColumn.setSortable(false);
        roundColumn.setSortable(false);


        Button btnAdd = new Button("添加");
        Button btnDelete = new Button("删除");
        Button btnModify = new Button("批量修改");

        btnAdd.setOnAction(event -> {
            TextField tfKey = new TextField();
            ComboBox<FlagChangeInfo.FlagType> cbFlagType = new ComboBox<>();
            TextField tfTarget = new TextField();

            cbFlagType.getItems().addAll(FlagChangeInfo.FlagType.values());
            cbFlagType.getSelectionModel().select(0);

            Button btnConfirm = new Button("确定");

            btnConfirm.setOnAction(e1 -> {
                int key = Utils.parseIntOrDefault(tfKey.getText(), 0);

                if (!flagChangeMap.containsKey(key)) {
                    int target = Utils.parseIntOrDefault(tfTarget.getText(), 0);
                    FlagChangeInfo value = new FlagChangeInfo(cbFlagType.getValue(), target);

                    flagChangeMap.put(key,value);
                    tableView.getItems().add(new AbstractMap.SimpleEntry<>(key, value));
                    tableView.sort();
//                    items.add(new AbstractMap.SimpleEntry<>(key, value));
                    tfKey.clear();
                    tfTarget.clear();
                    cbFlagType.getSelectionModel().select(0);
                } else {
                    Utils.information("添加失败：该行动回合已设置红绿标");
                }

                tfKey.requestFocus();
            });
            // 行动回合输入框回车跳到锁定技能输入框
            tfKey.setOnKeyPressed(e1 -> {
                if (e1.getCode() == KeyCode.ENTER) {
                    tfTarget.requestFocus();
                }
            });
            // 技能输入框回车确定
            tfTarget.setOnKeyPressed(e1 -> {
                if (e1.getCode() == KeyCode.ENTER) {
                    btnConfirm.fire();
                }
            });

            HBox hBox = new HBox(tfKey, cbFlagType, tfTarget, btnConfirm);
            Stage stage = new Stage();
            stage.initOwner(owner);
            stage.initModality(Modality.WINDOW_MODAL);
            stage.setScene(new Scene(hBox));
            stage.showAndWait();
        });

        btnDelete.setOnAction(event -> {
            /*Map.Entry<Integer, FlagChangeInfo> selectedItem = listView.getSelectionModel().getSelectedItem();
            if (selectedItem != null) {
                items.remove(selectedItem);
                flagChangeMap.remove(selectedItem.getKey());
            }*/
            Map.Entry<Integer, FlagChangeInfo> selectedItem = tableView.getSelectionModel().getSelectedItem();
            if (selectedItem != null) {
                tableView.getItems().remove(selectedItem);
                flagChangeMap.remove(selectedItem.getKey());
            }
        });

        btnModify.setOnAction(event -> {
            Stage stage = new Stage();

            TextField tfTargetOld = new TextField();
            ComboBox<FlagChangeInfo.FlagType> cbFlagType = new ComboBox<>();
            TextField tfTargetNew = new TextField();
            Button btnConfirm = new Button("确定");

            cbFlagType.getItems().addAll(FlagChangeInfo.FlagType.values());
            cbFlagType.getSelectionModel().select(0);

            btnConfirm.setOnAction(e1 -> {
                int targetOld = Utils.parseIntOrDefault(tfTargetOld.getText(), 0);
                int targetNew = Utils.parseIntOrDefault(tfTargetNew.getText(), 0);
                for (Map.Entry<Integer, FlagChangeInfo> item : items) {
                    FlagChangeInfo value = item.getValue();
                    if (value.target == targetOld && value.flagType == cbFlagType.getValue()) {
                        value.target = targetNew;
                    }
                }
//                listView.refresh();
                tableView.refresh();
                stage.close();
            });

            HBox hBox = new HBox(tfTargetOld, cbFlagType, tfTargetNew, btnConfirm);
            stage.setScene(new Scene(hBox));
            stage.initOwner(owner);
            stage.initModality(Modality.WINDOW_MODAL);
            stage.show();
        });


        TilePane controller = new TilePane(btnAdd, btnDelete, btnModify);
        controller.setVgap(20);

        BorderPane borderPane = new BorderPane();
        borderPane.setCenter(tableView);
//        borderPane.setCenter(listView);
        borderPane.setRight(controller);
        return borderPane;
    }

}