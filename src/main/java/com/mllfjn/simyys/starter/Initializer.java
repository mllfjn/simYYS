package com.mllfjn.simyys.starter;

import com.mllfjn.simyys.BattlePane;
import com.mllfjn.simyys.collections.SerializableObservableList;
import com.mllfjn.simyys.utils.Utils;
import com.mllfjn.simyys.character.CharacterFactory;
import com.mllfjn.simyys.character.propertygetter.PropertiesHolder;
import com.mllfjn.simyys.character.propertygetter.PropertiesMap;
import com.mllfjn.simyys.character.propertygetter.PropertyRequire;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.*;
import java.util.*;

public class Initializer extends Application {
    private final SerializableObservableList<PropertiesHolder> items = new SerializableObservableList<>();

    @Override
    public void start(Stage stage) {
        BorderPane borderPane = new BorderPane();
        Scene scene = new Scene(borderPane);

        getController(stage, scene, borderPane);

        CustomTableView customTableView = new CustomTableView();
        customTableView.setItems(items.getObservableList());

        Button btnAdd = new Button("添加角色");
        Button btnDelete = new Button("删除角色");
        Button btnModify = new Button("修改角色");
        Button btnMoveUp = new Button("上移");
        Button btnMoveDown = new Button("下移");
        Button btnClear = new Button("清空");

        btnAdd.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        btnDelete.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        btnMoveUp.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        btnMoveDown.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        btnClear.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);

        btnAdd.setOnAction(e -> addCharacter(stage));
        btnDelete.setOnAction(e -> {
            int index = customTableView.getSelectionModel().getSelectedIndex();
            if (index >= 0 && index < items.size()) {
                items.remove(index);
                if (index < items.size()) {
                    customTableView.getSelectionModel().select(index);
                }
            }
        });
        btnModify.setOnAction(e -> {
            PropertiesHolder item = customTableView.getSelectionModel().getSelectedItem();
            if (item != null) {
                item.show(stage);
                customTableView.refresh();
            }
        });
        btnMoveUp.setOnAction(e -> {
            int index = customTableView.getSelectionModel().getSelectedIndex();
            if (index > 0 && index < items.size()) {
                items.add(index - 1, items.remove(index));
                customTableView.getSelectionModel().select(index - 1);
            }
        });
        btnMoveDown.setOnAction(e -> {
            int index = customTableView.getSelectionModel().getSelectedIndex();
            if (index < items.size() - 1 && index >= 0) {
                items.add(index + 1, items.remove(index));
                customTableView.getSelectionModel().select(index + 1);
            }
        });
        btnClear.setOnAction(e -> items.clear());

        TilePane tp = new TilePane(btnAdd, btnDelete, btnModify, btnMoveUp, btnMoveDown, btnClear);
        tp.setVgap(20);
        tp.setPadding(new Insets(20, 10, 20, 10));

        borderPane.setCenter(customTableView);
        borderPane.setRight(tp);

        stage.setScene(scene);
        stage.setWidth(1800);
        stage.setHeight(900);
        stage.setTitle("配置式神");
        stage.show();
    }

    private void addCharacter(Stage owner) {
        Stage stageSelect = new Stage();
        GridPane gp = new GridPane();
        gp.setPadding(new Insets(20));
        gp.setHgap(10);
        gp.setVgap(10);
        Set<String> labels = CharacterFactory.characterMap.keySet();

        int i = 0;
        for (String label : labels) {
            FlowPane tp = new FlowPane();
            for (String name : CharacterFactory.characterMap.get(label).keySet()) {
                Button btn = new Button(name);
                btn.setPrefWidth(100);
                btn.setOnAction(event -> {
                    PropertiesHolder propertiesHolder = new PropertiesHolder(name, CharacterFactory.getProperties(name).orElseThrow(), new LinkedHashMap<>(), new LinkedHashMap<>());
                    propertiesHolder.show(stageSelect);
                    items.add(propertiesHolder);
                });
                tp.getChildren().add(btn);
            }
            gp.add(new Label(label), 0, i);
            gp.add(tp, 1, i);
            i++;
        }

        stageSelect.setTitle("添加角色");
        stageSelect.initOwner(owner);
        stageSelect.initModality(Modality.WINDOW_MODAL);
        stageSelect.setScene(new Scene(gp));
        stageSelect.showAndWait();
    }

    private void getController(Stage stage, Scene scene, BorderPane borderPane) {
        HBox controlPane = new HBox(50);
        controlPane.setPadding(new Insets(20, 0, 20, 0));
        controlPane.setAlignment(Pos.CENTER);

        Button saveButton = new Button("保存队伍预设");
        Button loadButton = new Button("读取队伍预设");
        Button startButton = new Button("开始");

        saveButton.setOnAction(event -> saveDate(stage));
        loadButton.setOnAction(event -> loadData(stage));
        startButton.setOnAction(event -> new BattlePane(stage, () -> stage.setScene(scene), items));

        controlPane.getChildren().addAll(saveButton, loadButton, startButton);

        borderPane.setTop(controlPane);
    }

    @SuppressWarnings("unchecked")
    private void loadData(Stage stage) {
        FileChooser fileChooser = new FileChooser();
        File directory = new File("save");
        if (!directory.exists() && !directory.mkdir()) {
            Utils.information("创建目录失败");
        }
        fileChooser.setInitialDirectory(directory);
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("队伍预设", "*.team"));

        File file = fileChooser.showOpenDialog(stage);
        if (file != null) {
            SerializableObservableList<PropertiesHolder> readItems = null;
            try (FileInputStream fis = new FileInputStream(file);
                 ObjectInputStream ois = new ObjectInputStream(fis)
            ) {
                readItems = (SerializableObservableList<PropertiesHolder>) ois.readObject();
            } catch (Exception e) {
                Utils.throwException("读取文件时出错", e);
            }

            if (readItems == null) {
                return;
            }

            StringJoiner sj = new StringJoiner("\n");
            for (PropertiesHolder item : readItems) {
                Optional<PropertiesMap> op = CharacterFactory.getProperties(item.name);
                if (op.isEmpty()) {
                    sj.add(item.name + "角色不存在");
                    continue;
                }
                PropertiesMap currentProperties = op.get();

                for (Map.Entry<String, PropertyRequire> entry : currentProperties.entrySet()) {
                    String key = entry.getKey();
                    PropertyRequire require = item.propertiesMap.remove(key);
                    if (require == null) {
                        sj.add(item.name + "新增属性：" + entry.getKey());
                        continue;
                    }

                    if (!entry.getValue().cover(require)) {
                        sj.add(item.name + "属性：" + entry.getKey() + "发生变更");
                    }

                }
                if (!item.propertiesMap.isEmpty()) {
                    sj.add(item.name + "的预设中含有当前不存在的属性");
                }
                items.add(new PropertiesHolder(item.name, currentProperties, item.lockSkillMap, item.flagChangeMap));
            }

            String message = sj.toString();
            if (!message.isEmpty()) {
                Utils.information("部分式神或属性发生变更，请检查：\n" + message);
            }
        }
    }

    private void saveDate(Stage stage) {

        FileChooser fileChooser = new FileChooser();
        File directory = new File("save");
        if (!directory.exists() && !directory.mkdir()) {
            Utils.information("创建目录失败");
        }
        fileChooser.setInitialDirectory(directory);
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("队伍预设", "*.team"));

        File file = fileChooser.showSaveDialog(stage);

        if (file != null) {
            try (
                    FileOutputStream fos = new FileOutputStream(file);
                    ObjectOutputStream oos = new ObjectOutputStream(fos)
            ) {
                oos.writeObject(items);
            } catch (Exception e) {
                Utils.throwException("保存时出错", e);
            }

        }
    }

    public interface Back {
        void back();
    }
}
