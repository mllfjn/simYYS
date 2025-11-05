package com.mllfjn.simyys.character.propertygetter;

import com.mllfjn.simyys.utils.DecimalFormatUtil;
import com.mllfjn.simyys.utils.Utils;
import com.mllfjn.simyys.character.PropertyKey;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.DoubleBinding;
import javafx.beans.binding.StringBinding;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.io.Serializable;
import java.util.AbstractMap;
import java.util.Map;
import java.util.concurrent.Callable;

public class PropertiesHolder implements Serializable {
    public final String name;
    public final PropertiesMap map;
    public final Map<Integer, Integer> lockSKill;

    private transient SimpleStringProperty nameProperty;
    private transient StringBinding totalAttackProperty;

    public PropertiesHolder(String name, PropertiesMap map, Map<Integer, Integer> lockSKill) {
        this.name = name;
        this.map = map;
        this.lockSKill = lockSKill;
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
            SimpleStringProperty baseAttack = ((PropertyInput) map.get(PropertyKey.GENERAL_BASE_ATTACK_KEY)).getProperty();
            SimpleStringProperty addAttack = ((PropertyInput) map.get(PropertyKey.GENERAL_YU_HUN_ATTACK_KEY)).getProperty();

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

        for (Map.Entry<String, PropertyRequire> entry : map.entrySet()) {
            vb.getChildren().add(entry.getValue().getNode(entry.getKey(), owner));
        }

        return vb;
    }

    private Node getLockSkillPane(Window owner) {

        TableView<Map.Entry<Integer, Integer>> tableView = new TableView<>();
        tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        TableColumn<Map.Entry<Integer, Integer>, String> keyColumn = new TableColumn<>("行动回合");
        TableColumn<Map.Entry<Integer, Integer>, String> valueColumn = new TableColumn<>("锁定技能");

        keyColumn.setCellValueFactory(data -> new ReadOnlyStringWrapper(String.valueOf(data.getValue().getKey())));
        valueColumn.setCellValueFactory(data -> new ReadOnlyStringWrapper(String.valueOf(data.getValue().getValue())));

        tableView.getColumns().add(keyColumn);
        tableView.getColumns().add(valueColumn);

        tableView.setItems(FXCollections.observableArrayList(lockSKill.entrySet()));


        TilePane controller = new TilePane();
        controller.setVgap(20);

        Button btnAdd = new Button("添加");
        Button btnDelete = new Button("删除");

        btnAdd.setOnAction(e -> {
            TextField tfKey = new TextField();
            TextField tfValue = new TextField();
            Button btnConfirm = new Button("确定");

            btnConfirm.setOnAction(e1 -> {
                int key = Utils.parseIntOrDefault(tfKey.getText(), 0);
                int value = Utils.parseIntOrDefault(tfValue.getText(), 0);
                if (!lockSKill.containsKey(key)) {
                    lockSKill.put(key, value);
                    tableView.getItems().add(new AbstractMap.SimpleEntry<>(key, value));
                    tfKey.clear();
                    tfValue.clear();
//                    addRow(skillGrid, key, value);
                } else {
                    Utils.information("添加失败：该行动回合已设置锁定技能");
                }

                tfKey.requestFocus();
            });

            tfKey.setOnKeyPressed(e1 -> {
                if (e1.getCode() == KeyCode.ENTER) {
                    tfValue.requestFocus();
                }
            });

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
                lockSKill.remove(selectedItem.getKey());
            }
        });

        controller.getChildren().addAll(btnAdd, btnDelete);

        BorderPane border = new BorderPane();
        border.setCenter(tableView);
        border.setRight(controller);

        return border;
    }

    private Node getFlagPane(Window owner) {
        GridPane gp = new GridPane();
        final int COL_NUM = 4;
        for (int i = 0; i < COL_NUM; i++) {
            ColumnConstraints columnConstraints = new ColumnConstraints();
            columnConstraints.setPercentWidth(100.0 / COL_NUM);
            gp.getColumnConstraints().add(columnConstraints);
        }

        gp.addRow(0, new Text("操作"), new Text("行动回合"), new Text("标记类型"), new Text("标记目标"));
        return gp;
    }

    private record FlagInfo(int round, FlagType type, String target) implements Serializable {

        public void getNodes(GridPane gp) {
            TextField tfRound = new TextField();
            ComboBox<FlagType> cbType = new ComboBox<>();
            cbType.setItems(FXCollections.observableArrayList(FlagType.values()));
            cbType.getSelectionModel().select(0);
            cbType.itemsProperty().addListener((obs, old, val) -> {

            });
            Label lblChoose = new Label("请选择");

//            stackPane.getChildren().add();
        }

        public enum FlagType {
            FLAG_GREEN,
            FLAG_RED,
            FLAG_CANCEL
        }
    }
}