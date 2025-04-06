package com.mllfjn.simyys.starter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mllfjn.simyys.BattlePane;
import com.mllfjn.simyys.starter.info.CharacterInfo;
import com.mllfjn.simyys.starter.info.FlagChangeInfo;
import com.mllfjn.simyys.starter.info.SkillChangeInfo;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.*;
import java.nio.charset.StandardCharsets;

public class Initializer {
    CharacterPane characterPane;
    SkillChangePane skillChangePane;
    FlagChangePane flagChangePane;
    public Initializer(Stage stage) {
        final int width = 1600;
        final int height = 900;
        final int skillChangePaneWidth = 350;
        final int skillChangePaneHeight = 410;
        final int flagChangePaneWidth = 375;
        final int flagChangePaneHeight = 425;

        BorderPane root = new BorderPane();
        Scene scene = new Scene(root);

        HBox controlPane = new HBox(50);
        controlPane.setAlignment(Pos.CENTER);

        Button saveButton = new Button("保存队伍预设");
        Button loadButton = new Button("读取队伍预设");
        Button startButton = new Button("开始");

        saveButton.setOnAction(event -> saveDate(stage));
        loadButton.setOnAction(event -> loadData(stage));
        startButton.setOnAction(event -> {
            BattlePane battlePane = new BattlePane(stage, characterPane.getInfo(), skillChangePane.getInfo(), flagChangePane.getInfo());
        });

        controlPane.getChildren().addAll(saveButton, loadButton, startButton);
        root.setTop(controlPane);


        this.characterPane = new CharacterPane();
        this.skillChangePane = new SkillChangePane();
        this.flagChangePane = new FlagChangePane();
        ScrollPane scrollPane1 = new ScrollPane(characterPane);
        ScrollPane scrollPane2 = new ScrollPane(skillChangePane);
        ScrollPane scrollPane3 = new ScrollPane(flagChangePane);

        scrollPane2.setPrefSize(skillChangePaneWidth, skillChangePaneHeight);
        scrollPane3.setPrefSize(flagChangePaneWidth, flagChangePaneHeight);

        scrollPane3.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);

        BorderPane paneRight = new BorderPane();
        paneRight.setTop(scrollPane2);
        paneRight.setBottom(scrollPane3);
        root.setRight(paneRight);
        root.setCenter(scrollPane1);

        stage.setScene(scene);
        stage.setWidth(width);
        stage.setHeight(height);
        stage.setTitle("配置式神");
        stage.show();
    }

    private void loadData(Stage stage) {
        FileChooser fileChooser = new FileChooser();
        File directory = new File("save");
        if (!directory.exists()) {
            directory.mkdir();
        }
        fileChooser.setInitialDirectory(directory);
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Json File", "*.json"));

        File file = fileChooser.showOpenDialog(stage);
        if (file != null) {
            try (
                    FileInputStream fis = new FileInputStream(file);
                    InputStreamReader isr = new InputStreamReader(fis, StandardCharsets.UTF_8);
                    BufferedReader reader = new BufferedReader(isr)){

                Gson gson = new Gson();
                JsonSaver jsonSaver = gson.fromJson(reader, new TypeToken<JsonSaver>(){}.getType());

                for (CharacterInfo info : jsonSaver.characterInfo) {
                    characterPane.addNewLine(info);
                }

                for (SkillChangeInfo info : jsonSaver.skillChangeInfo) {
                    skillChangePane.addNewLine(info);
                }

                for (FlagChangeInfo info : jsonSaver.flagChangeInfo) {
                    flagChangePane.addNewLine(info);
                }
            } catch (IOException ignored) {

            }
        }
    }
    private void saveDate(Stage stage) {
        JsonSaver jsonSaver = new JsonSaver(characterPane.getInfo(), skillChangePane.getInfo(), flagChangePane.getInfo());
        Gson gson = new Gson();
        String json = gson.toJson(jsonSaver);

        FileChooser fileChooser = new FileChooser();
        File directory = new File("save");
        if (!directory.exists()) {
            directory.mkdir();
        }
        fileChooser.setInitialDirectory(directory);
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Json File", "*.json"));

        File file = fileChooser.showSaveDialog(stage);

        if (file != null) {
            try (   // 使用 FileOutputStream 打开文件
                    FileOutputStream fos = new FileOutputStream(file);
                    // 创建 OutputStreamWriter，并指定 UTF-8 编码
                    OutputStreamWriter osw = new OutputStreamWriter(fos, StandardCharsets.UTF_8);
                    // 创建 BufferedWriter 来写入文本
                    BufferedWriter writer = new BufferedWriter(osw)) {
                writer.write(json);
            } catch (IOException ignored) {

            }
        }

    }
}
