package com.mllfjn.simyys.starter;

import com.mllfjn.simyys.BattlePane;
import com.mllfjn.simyys.character.propertygetter.FlagChangeInfo;
import com.mllfjn.simyys.collections.SerializableObservableList;
import com.mllfjn.simyys.customnode.ListViewWithBasicController;
import com.mllfjn.simyys.customnode.NodeWithController;
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
    private final Prediction prediction = new Prediction();

    private SerializableObservableList<ExtraFlag> extraFlags = new SerializableObservableList<>();
    private SerializableObservableList<ExtraLockSkill> extraLockSkills = new SerializableObservableList<>();

    @Override
    public void start(Stage stage) {
        CustomTableView customTableView = new CustomTableView();
        customTableView.setItems(items.getObservableList());

        NodeWithController borderPane = new NodeWithController();
        borderPane.setNode(customTableView);

        Scene scene = new Scene(borderPane);
        getController(stage, scene, borderPane);

        borderPane.addControlButton("添加角色", e -> addCharacter(stage));
        borderPane.addControlButton("删除角色", e -> {
            int index = customTableView.getSelectionModel().getSelectedIndex();
            if (index >= 0 && index < items.size()) {
                items.remove(index);
                if (index < items.size()) {
                    customTableView.getSelectionModel().select(index);
                }
            }
        });
        borderPane.addControlButton("修改角色", e -> {
            PropertiesHolder item = customTableView.getSelectionModel().getSelectedItem();
            if (item != null) {
                item.show(stage);
                customTableView.refresh();
            }
        });
        borderPane.addControlButton("上移", e -> {
            int index = customTableView.getSelectionModel().getSelectedIndex();
            if (index > 0 && index < items.size()) {
                items.add(index - 1, items.remove(index));
                customTableView.getSelectionModel().select(index - 1);
            }
        });
        borderPane.addControlButton("下移", e -> {
            int index = customTableView.getSelectionModel().getSelectedIndex();
            if (index < items.size() - 1 && index >= 0) {
                items.add(index + 1, items.remove(index));
                customTableView.getSelectionModel().select(index + 1);
            }
        });
        borderPane.addControlButton("清空", e -> items.clear());
        borderPane.addControlButton("额外红绿标", e -> {
            // TODO
            ListViewWithBasicController<ExtraFlag> listViewPane = new ListViewWithBasicController<>(extraFlags);
            /*listViewPane.setDefaultControlButtons(e -> {
                // String int int combobox int
                TextField tfName = new TextField();
                TextField tfTeam = new TextField();
                TextField tfRound = new TextField();
            });*/
        });
        borderPane.addControlButton("额外锁技能", e -> {


        });

        borderPane.addControlButton("设置行动顺序", e -> prediction.showPrediction(stage, items));
        borderPane.addControlButton("检查是否符合", e -> prediction.check(items));

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
            SerializableRecord readRecord = null;
            try (FileInputStream fis = new FileInputStream(file);
                 ObjectInputStream ois = new ObjectInputStream(fis)
            ) {
                readRecord = ((SerializableRecord) ois.readObject());
            } catch (Exception e) {
                Utils.throwException("读取文件时出错", e);
            }

            if (readRecord == null || readRecord.items == null) {
                return;
            }

            if (prediction.predictionOrder.isEmpty()) {
                prediction.predictionOrder = readRecord.prediction.predictionOrder;
            }

            if (extraFlags.isEmpty()) {
                extraFlags = readRecord.extraFlags;
            }

            if (extraLockSkills.isEmpty()) {
                extraLockSkills = readRecord.extraLockSkills;
            }

            SerializableObservableList<PropertiesHolder> readItems = readRecord.items;

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
                oos.writeObject(new SerializableRecord(items, extraFlags, extraLockSkills, prediction));
            } catch (Exception e) {
                Utils.throwException("保存时出错", e);
            }

        }
    }

    public interface Back {
        void back();
    }

    record ExtraFlag(String name, int team, int timesToAct, FlagChangeInfo flagChangeInfo) implements Serializable {
    }

    record ExtraLockSkill(String name, int team, int timesToAct, int skillId) implements Serializable {
    }

    record SerializableRecord(
            SerializableObservableList<PropertiesHolder> items
            , SerializableObservableList<ExtraFlag> extraFlags
            , SerializableObservableList<ExtraLockSkill> extraLockSkills,
            Prediction prediction) implements Serializable {
    }
}
