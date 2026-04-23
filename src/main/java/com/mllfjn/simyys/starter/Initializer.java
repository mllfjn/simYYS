package com.mllfjn.simyys.starter;

import com.mllfjn.simyys.BattlePane;
import com.mllfjn.simyys.character.propertygetter.FlagChangeInfo;
import com.mllfjn.simyys.collections.SerializableObservableList;
import com.mllfjn.simyys.customnode.NodeWithController;
import com.mllfjn.simyys.utils.Utils;
import com.mllfjn.simyys.character.CharacterFactory;
import com.mllfjn.simyys.character.propertygetter.PropertiesHolder;
import com.mllfjn.simyys.character.propertygetter.PropertiesMap;
import com.mllfjn.simyys.character.propertygetter.PropertyRequire;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.*;
import javafx.stage.*;

import java.io.*;
import java.util.*;

public class Initializer extends Application {
    private final SerializableObservableList<PropertiesHolder> items = new SerializableObservableList<>();
    private final Prediction prediction = new Prediction();

    private SerializableObservableList<ExtraFlag> extraFlags = new SerializableObservableList<>();
    private SerializableObservableList<ExtraLockSkill> extraLockSkills = new SerializableObservableList<>();

    private final static double BORDER_WIDTH = 16;
    private final static double BORDER_HEIGHT = 39;

    private static double scaleX = 1;
    private static double scaleY = 1;

    @Override
    public void start(Stage stage) {
        CustomTableView customTableView = new CustomTableView();
        customTableView.setItems(items.getObservableList());

        NodeWithController borderPane = new NodeWithController();
        borderPane.setNode(customTableView);

        StackPane stageRoot = new StackPane(borderPane);
        stageRoot.setMinSize(1784, 861);
        stageRoot.setMaxSize(1784, 861);

        Scene scene = new Scene(new StackPane(stageRoot));
        getController(stage, scene, stageRoot, borderPane);

        borderPane.addControlButton("添加角色", e -> addCharacter(stage), scene, KeyCode.A);
        borderPane.addControlButton("删除角色", e -> {
            int index = customTableView.getSelectionModel().getSelectedIndex();
            if (index >= 0 && index < items.size()) {
                items.remove(index);
                if (index < items.size()) {
                    customTableView.getSelectionModel().select(index);
                }
            }
        }, scene, KeyCode.D);
        borderPane.addControlButton("修改角色", e -> {
            PropertiesHolder item = customTableView.getSelectionModel().getSelectedItem();
            if (item != null) {
                item.show(scene);
                customTableView.refresh();
            }
        }, scene, KeyCode.E);
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
        borderPane.addControlButton("清空", e -> {
            items.clear();
            prediction.predictionOrder.clear();
        });

        borderPane.addControlButton("设置预计顺序", e -> prediction.showPrediction(scene, items));
        borderPane.addControlButton("检查是否符合", e -> prediction.check(items
                , stage, () -> stage.setScene(scene)), scene, KeyCode.C);

        stage.setScene(scene);
        stage.setTitle("配置式神");
        stage.show();

        stage.widthProperty().addListener((obs, old, val) -> {
            double height = val.doubleValue() / 2;
            if (stage.getHeight() != height) {
                stage.setHeight(height);
                setScale(stage, stageRoot);
            }
        });
        stage.heightProperty().addListener((obs, old, val) -> {
            double width = val.doubleValue() * 2;
            if (stage.getWidth() != width) {
                stage.setWidth(width);
                setScale(stage, stageRoot);
            }
        });

        Rectangle2D bounds = Screen.getPrimary().getVisualBounds();
        if (bounds.getWidth() < 1800 || bounds.getHeight() < 900) {
            stage.setWidth(bounds.getWidth() * 0.9);
            stage.setX(bounds.getWidth() * 0.05);
            stage.setY((bounds.getHeight() - stage.getHeight()) / 2);
//            stage.setMaximized(true);
        }
    }

    private void setScale(Stage stage, Pane root) {
        scaleX = (stage.getWidth() - BORDER_WIDTH) / 1784;
        scaleY = (stage.getHeight() - BORDER_HEIGHT) / 861;
        root.setScaleX(scaleX);
        root.setScaleY(scaleY);
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
                    propertiesHolder.show(stageSelect.getScene());
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
        Initializer.installScale(stageSelect, gp, 500, 500);
        stageSelect.showAndWait();
    }

    private void getController(Stage stage, Scene scene, Pane stageRoot, BorderPane borderPane) {
        HBox controlPane = new HBox(50);
        controlPane.setPadding(new Insets(20, 0, 20, 0));
        controlPane.setAlignment(Pos.CENTER);

        Button saveButton = new Button("保存队伍预设");
        Button loadButton = new Button("读取队伍预设");
        Button startButton = new Button("开始");

        saveButton.setOnAction(event -> saveDate(stage));
        loadButton.setOnAction(event -> loadData(stage));
        startButton.setOnAction(event -> {
            new BattlePane(
                    scene,
                    stageRoot,
                    () -> {
                        stageRoot.getChildren().set(0, borderPane);
                        stage.setTitle("配置式神");
                    },
                    items,
                    prediction
            );
            stage.setTitle("战斗中");
        });

        controlPane.getChildren().addAll(saveButton, loadButton, startButton);

        borderPane.setTop(controlPane);
    }

    private void loadData(Stage stage) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setInitialDirectory(getDirectory());
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
        fileChooser.setInitialDirectory(getDirectory());
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

    private File getDirectory() {
        File directory = new File("save");
        if (!directory.exists() && !directory.mkdir()) {
            Utils.error("创建目录失败");
        }
        return directory;
    }

    public static void installScale(Stage stage, Region root, double expectedWidth, double expectedHeight) {
        root.setMaxSize(expectedWidth, expectedHeight);
        root.setMinSize(expectedWidth, expectedHeight);

        root.setScaleX(Initializer.scaleX);
        root.setScaleY(Initializer.scaleY);

        stage.setScene(new Scene(new StackPane(root)));
        stage.setWidth(expectedWidth * Initializer.scaleX + Initializer.BORDER_WIDTH);
        stage.setHeight(expectedHeight * Initializer.scaleY + Initializer.BORDER_HEIGHT);
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
