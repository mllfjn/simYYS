package com.mllfjn.simyys;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.CharacterFactory;
import com.mllfjn.simyys.character.CharacterIcon;
import com.mllfjn.simyys.customnode.CustomTextField;
import com.mllfjn.simyys.customnode.CustomTextFlow;
import com.mllfjn.simyys.starter.info.CharacterInfo;
import com.mllfjn.simyys.starter.info.FlagChangeInfo;
import com.mllfjn.simyys.starter.info.SkillChangeInfo;
import com.mllfjn.simyys.utils.RuntimeTypeAdapterFactory;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;

public class BattlePane {
    CharacterInfo[] characterInfo;
    SkillChangeInfo[] skillChangeInfo;
    FlagChangeInfo[] flagChangeInfo;
    List<Character> characters;
    ActionBarType actionBarType = ActionBarType.SHUNWEI;
    BorderPane root = new BorderPane();
    AnchorPane actionBar = new AnchorPane();
    CustomTextFlow log = new CustomTextFlow();
    HBox[] teamPane = new HBox[2];
    Character characterActing;
    List<String> recorder = new ArrayList<>();
    boolean isControlRate = false;
    Gson gson;
    public BattlePane(Stage stage, CharacterInfo[] characterInfo, SkillChangeInfo[] skillChangeInfo, FlagChangeInfo[] flagChangeInfo) {
        this.characterInfo = characterInfo;
        this.skillChangeInfo = skillChangeInfo;
        this.flagChangeInfo = flagChangeInfo;
        this.characters = new ArrayList<>();
        stage.setScene(new Scene(root));

        init();
        setupUI();

//        gson = new GsonBuilder().registerTypeAdapterFactory(RuntimeTypeAdapterFactory.of())
    }

    private void setupUI() {
/*
          右边是主操作区
          主体偏上是行动条，点击切换模式
          右下角是控制区
         */

        BorderPane right = new BorderPane();
        right.setTop(actionBar);
        right.setBottom(configureControlPane());
        root.setRight(right);
        configureActionBar();
        configureTeamPane();

        log.setPrefWidth(400);
        root.setLeft(log);

    }

    private void configureTeamPane() {
        teamPane[0] = new HBox();
        teamPane[1] = new HBox();
        teamPane[0].setAlignment(Pos.CENTER);
        teamPane[1].setAlignment(Pos.CENTER);

        teamPane[0].setSpacing(5);
        teamPane[1].setSpacing(5);
        teamPane[0].setPadding(new Insets(5));

        teamPane[0].setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, null, new BorderWidths(3))));
        teamPane[1].setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, null, new BorderWidths(3))));

        VBox container = new VBox(teamPane[1], teamPane[0]);
        container.setSpacing(10);
        container.setPadding(new Insets(5));
        // 高度是一半-5
        teamPane[0].minHeightProperty().bind(root.heightProperty().divide(2).subtract(10));
        teamPane[1].minHeightProperty().bind(root.heightProperty().divide(2).subtract(10));

        ScrollPane scrollPane = new ScrollPane(container);
        scrollPane.setPrefWidth(1100);
        root.setCenter(scrollPane);

        for (Character character : characters) {
            teamPane[character.team].getChildren().add(new CharacterIcon(character, this::setAutoTo));
        }
    }

    private void setAutoTo(Character characterSelected) {
        // 相同队伍的其他角色取消标，该角色切换标
        for (Character character : characters.stream().filter(character -> character.team == characterSelected.team).toList()) {

        }
    }

    private GridPane configureControlPane() {
        /*控制区
        控制区的内容
        1、概率控制模式的开关
        2、流程控制
        上一个，下一个
        跳过指定回合数*/

        CheckBox rateControl = new CheckBox("概率控制模式");
        rateControl.selectedProperty().addListener((obs, old, val) -> {
            this.isControlRate = val;
        });

        Button prev = new Button("上一个");
        Button next = new Button("下一个");
        prev.setOnAction(event -> prev());
        next.setOnAction(event -> next());
        prev.setPrefSize(100, 25);
        next.setPrefSize(100, 25);

        CustomTextField round = new CustomTextField();
        Button skip = new Button("跳过回合");
        skip.setOnAction(event -> skip(Integer.parseInt(round.getText())));

        round.setPrefSize(100, 25);
        skip.setPrefSize(100, 25);

        GridPane controller = new GridPane();
        controller.setPadding(new Insets(0, 0, 50, 0)); // 底部留空

        controller.add(rateControl, 0, 0);
        controller.add(prev, 0, 1);
        controller.add(next, 1, 1);
        controller.add(round, 0, 2);
        controller.add(skip, 1, 2);

        return controller;
    }

    private void configureActionBar() {
        actionBar.setPrefWidth(200);
        actionBar.setOnMouseClicked(event -> {
            if (actionBarType == ActionBarType.SHUNWEI) {
                actionBarType = ActionBarType.JINDU;
            } else {
                actionBarType = ActionBarType.SHUNWEI;
            }
            repaintActionBar();
        });
//        actionBar.setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, null, new BorderWidths(3))));
        repaintActionBar();
    }

    private void init() {
        for (CharacterInfo info : characterInfo) {
            if (Integer.parseInt(info.team) == 0 || Integer.parseInt(info.team) == 1) {
                characters.add(CharacterFactory.createCharacter(info));
            }
        }

        getNextActor();
    }

    private List<Character> getCharactersByLocation() {
        return getCharactersAlive().stream().sorted((o1, o2) -> {
            if (o1.getLocation() > o2.getLocation()) {
                return -1;
            } else if (o1.getLocation() < o2.getLocation()) {
                return 1;
            } else return Double.compare(o1.getSpeed(), o2.getSpeed());
        }).toList();
    }

    private List<Character> getCharactersByActionSort(int num) {
        List<Character> rt = new ArrayList<>();
        List<Character> list = getCharactersAlive();
        int size = list.size();
        double[] distance = new double[size];
        double[] speed = new double[size];
        for (int i = 0; i < size; i++) {
            distance[i] = 100.0 - list.get(i).getLocation();
            speed[i] = list.get(i).getSpeed();
        }

        rt.add(characterActing);
        for (int i = 1; i < num ; i++) {
            int min = 0;
            for (int j = 1; j < size; j++) {
                if (Character.before(distance[j], speed[j], distance[min], speed[min])) {
                    min = j;
                }
            }
            rt.add(list.get(min));
            distance[min] += 100;
        }

        return rt;
    }

    private List<Character> getCharactersAlive() {
        return characters.stream().filter(character -> character.alive).toList();
    }

    private void repaintActionBar() {
        double yOffset = 20;
        double layoutXBig = 40;
        double layoutXSmall = 62.5;
        Color color = Color.ORANGE;
        double strokeWidth = 3;

        actionBar.getChildren().clear();

        if (actionBarType == ActionBarType.SHUNWEI) {

            List<Character> list = getCharactersByActionSort(9);
            Pane imageBig = CharacterFactory.getImageByName(list.get(0).name, CharacterFactory.ImageSize.BIG, color, strokeWidth);
            imageBig.setLayoutY( yOffset + 8 * CharacterFactory.ImageSize.SMALL.size );
            imageBig.setLayoutX(layoutXBig);
            actionBar.getChildren().add(imageBig);
            for (int i = 1 ; i < 9; i++) {
                Pane imageSmall = CharacterFactory.getImageByName(list.get(i).name, CharacterFactory.ImageSize.SMALL, color, strokeWidth);
                imageSmall.setLayoutX( layoutXSmall );
                imageSmall.setLayoutY( yOffset + (8 - i) * CharacterFactory.ImageSize.SMALL.size);
                actionBar.getChildren().add(imageSmall);
            }

        } else if (actionBarType == ActionBarType.JINDU) {
            List<Character> list = getCharactersByLocation();
            for (int i = list.size() - 2; i >= 0; i--) {
                Pane imageSmall = CharacterFactory.getImageByName(list.get(i).name, CharacterFactory.ImageSize.SMALL, color, strokeWidth);
                // location在0的时候，y在0
                // location在100的时候，y在small.size * 7.5
                // y = location * small.size * 7.5 / 100
                imageSmall.setLayoutY( yOffset + list.get(i).getLocation() * CharacterFactory.ImageSize.SMALL.size * 7.5 / 100);
                imageSmall.setLayoutX(layoutXSmall);
                actionBar.getChildren().add(imageSmall);
            }
            Pane imageBig = CharacterFactory.getImageByName(list.get(list.size() - 1).name, CharacterFactory.ImageSize.BIG, color, strokeWidth);
            imageBig.setLayoutY( yOffset + 8 * CharacterFactory.ImageSize.SMALL.size );
            imageBig.setLayoutX(layoutXBig);
            actionBar.getChildren().add(imageBig);
        }
    }

    private void prev() {
        if (!recorder.isEmpty()) {
            Recorder prev = new Gson().fromJson(recorder.get(recorder.size() - 1), new TypeToken<Recorder>(){}.getType());
            characters = prev.characters;
            characterActing = prev.characterActing;
            repaintActionBar();
            recorder.remove(recorder.size() - 1);
            System.out.println(characters);
        }
    }
    private void next() {
        recorder.add(new Gson().toJson(new Recorder(characters, characterActing)));

        characterActing.act();
        getNextActor();
        repaintActionBar();
    }

    private void getNextActor() {

        Character next = characters.get(0);
        for (Character character : characters) {
            if (character.before(next)) {
                next = character;
            }
        }

        for (Character character : characters) {
            if (character != next) {
                character.setLocation(character.getLocation() + character.getSpeed() * next.getTTA());
            }
        }
        next.setLocation(0);

        characterActing = next;
    }

    private void skip(int round) {
        for (int i = 0; i < round; i++) {
            next();
        }
    }

    private enum ActionBarType {
        JINDU,
        SHUNWEI;
    }

    private class Recorder {
        List<Character> characters;
        Character characterActing;
        public Recorder(List<Character> characters, Character characterActing) {
            this.characters = characters;
            this.characterActing = characterActing;
        }
    }
}
