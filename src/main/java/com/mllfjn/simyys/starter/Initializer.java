package com.mllfjn.simyys.starter;

import com.mllfjn.simyys.BattlePane;
import com.mllfjn.simyys.Utils;
import com.mllfjn.simyys.character.CharacterFactory;
import com.mllfjn.simyys.character.propertygetter.PropertiesHolder;
import com.mllfjn.simyys.character.propertygetter.PropertiesMap;
import com.mllfjn.simyys.character.propertygetter.PropertyRequire;
import com.mllfjn.simyys.character.propertygetter.SerializableHolder;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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

public class Initializer {
    private final ObservableList<PropertiesHolder> items = FXCollections.observableArrayList();
    public Initializer(Stage stage) {
        final int width = 1600;
        final int height = 900;
        BorderPane borderPane = new BorderPane();
        Scene scene = new Scene(borderPane);

        getController(stage, scene, borderPane);

        ListView<PropertiesHolder> lvList = new ListView<>();
        lvList.setItems(items);

        Button btnAdd = new Button("添加角色");
        Button btnDelete = new Button("删除角色");
        Button btnModify = new Button("修改角色");
        Button btnMoveUp = new Button("上移");
        Button btnMoveDown = new Button("下移");

        btnAdd.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        btnDelete.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        btnMoveUp.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        btnMoveDown.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);

        btnAdd.setOnAction(e -> addCharacter(stage, items));
        btnDelete.setOnAction(e -> {
            int index = lvList.getSelectionModel().getSelectedIndex();
            if (index >= 0 && index < items.size()) {
                items.remove(index);
                if (index < items.size()) {
                    lvList.getSelectionModel().select(index);
                }
            }
        });
        btnModify.setOnAction(e -> {
            PropertiesHolder item = lvList.getSelectionModel().getSelectedItem();
            if (item != null) {
                item.show(stage);
                lvList.refresh();
            }
        });
        btnMoveUp.setOnAction(e -> {
            int index = lvList.getSelectionModel().getSelectedIndex();
            if (index > 0 && index < items.size()) {
                items.add(index - 1, items.remove(index));
                lvList.getSelectionModel().select(index - 1);
            }
        });
        btnMoveDown.setOnAction(e -> {
            int index = lvList.getSelectionModel().getSelectedIndex();
            if (index < items.size() - 1 && index >= 0) {
                items.add(index + 1, items.remove(index));
                lvList.getSelectionModel().select(index + 1);
            }
        });

        TilePane tp = new TilePane(btnAdd, btnDelete, btnModify, btnMoveUp, btnMoveDown);
        tp.setVgap(20);
        tp.setPadding(new Insets(20, 10, 20, 10));

        borderPane.setCenter(lvList);
        borderPane.setRight(tp);

        stage.setScene(scene);
        stage.setWidth(width);
        stage.setHeight(height);
        stage.setTitle("配置式神");
        stage.show();
    }

    private void addCharacter(Stage owner, ObservableList<PropertiesHolder> items) {
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
                btn.setOnAction(event-> {
                    PropertiesHolder propertiesHolder = new PropertiesHolder(name, CharacterFactory.getProperties(name));
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

    private void loadData(Stage stage) {
        FileChooser fileChooser = new FileChooser();
        File directory = new File("save");
        if (!directory.exists()) {
            directory.mkdir();
        }
        fileChooser.setInitialDirectory(directory);
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("队伍预设", "*.team"));
//        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("队伍预设", "*.json"));

        File file = fileChooser.showOpenDialog(stage);
        if (file != null) {
            List<PropertiesHolder> readItems = null;
            try (FileInputStream fis = new FileInputStream(file);
                 ObjectInputStream ois = new ObjectInputStream(fis)
            ) {
                readItems = ((SerializableHolder)ois.readObject()).list;
            } catch (Exception e) {
                Utils.throwException("读取时出错", e);
            }

            if (readItems == null) {
                return;
            }

            StringJoiner sj = new StringJoiner("\n");
            for (PropertiesHolder item : readItems) {
                PropertiesMap currentProperties = CharacterFactory.getProperties(item.name());

                if (currentProperties == null) {
                    sj.add(item.name() + "角色不存在");
                    continue;
                }

                for (Map.Entry<String, PropertyRequire> entry : currentProperties.entrySet()) {
                    String key = entry.getKey();
                    PropertyRequire require = item.map().remove(key);
                    if (require == null) {
                        sj.add(item.name() + "新增属性：" + entry.getKey());
                        continue;
                    }

                    if (!entry.getValue().cover(require)) {
                        sj.add(item.name() + "属性：" + entry.getKey() + "发生变更");
                    }

                }
                if (!item.map().isEmpty()) {
                    sj.add(item.name() + "的预设中含有当前不存在的属性");
                }
                items.add(new PropertiesHolder(item.name(), currentProperties));
            }

            String message = sj.toString();
            if (!message.isEmpty()) {
                Utils.information("部分式神或属性发生变更，请检查");
            }

            /*try (
                    FileInputStream fis = new FileInputStream(file);
                    InputStreamReader isr = new InputStreamReader(fis, StandardCharsets.UTF_8);
                    BufferedReader reader = new BufferedReader(isr)){

                Gson gson = new Gson();
                Saver saver = gson.fromJson(reader, new TypeToken<Saver>(){}.getType());

                for (CharacterInfo info : saver.characterInfo) {
                    characterPane.addNewLine(info);
                }

                for (SkillChangeInfo info : saver.skillChangeInfo) {
                    skillChangePane.addNewLine(info);
                }

                for (FlagChangeInfo info : saver.flagChangeInfo) {
                    flagChangePane.addNewLine(info);
                }
            } catch (IOException ignored) {

            }*/
        }
    }
    private void saveDate(Stage stage) {

        FileChooser fileChooser = new FileChooser();
        File directory = new File("save");
        if (!directory.exists()) {
            directory.mkdir();
        }
        fileChooser.setInitialDirectory(directory);
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("队伍预设", "*.team"));

        File file = fileChooser.showSaveDialog(stage);

        if (file != null) {
            try (
                    FileOutputStream fos = new FileOutputStream(file);
                    ObjectOutputStream oos = new ObjectOutputStream(fos)
            ) {
                oos.writeObject(new SerializableHolder(items));
            } catch (Exception e) {
                Utils.throwException("保存时出错", e);
            }

        }

        /*if (file != null) {
            try (   // 使用 FileOutputStream 打开文件
                    FileOutputStream fos = new FileOutputStream(file);
                    // 创建 OutputStreamWriter，并指定 UTF-8 编码
                    OutputStreamWriter osw = new OutputStreamWriter(fos, StandardCharsets.UTF_8);
                    // 创建 BufferedWriter 来写入文本
                    BufferedWriter writer = new BufferedWriter(osw)) {
                writer.write(json);
            } catch (IOException ignored) {

            }
        }*/

    }

    public interface Back {
        void back();
    }
}
