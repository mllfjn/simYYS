package com.mllfjn.simyys;

import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.CharacterFactory;
import com.mllfjn.simyys.character.PropertyKey;
import com.mllfjn.simyys.customnode.CustomTextField;
import com.mllfjn.simyys.customnode.TextFlowLog;
import com.mllfjn.simyys.guihuo.GuiHuo;
import com.mllfjn.simyys.ratecontroller.TotalRateCalc;
import com.mllfjn.simyys.starter.Initializer;
import com.mllfjn.simyys.character.propertygetter.PropertiesHolder;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class BattlePane {
    private final Initializer.Back back;
    private final List<PropertiesHolder> list;
    public List<Character> characters;
    public Character[] autoTo = new Character[2];
    private ActionBarType actionBarType = ActionBarType.SHUN_WEI;
    private final BorderPane root = new BorderPane();
    private final AnchorPane actionBar = new AnchorPane();
    public final TextFlowLog log = new TextFlowLog();
    public final TotalRateCalc calc = new TotalRateCalc();
    private final HBox[] teamPane = new HBox[2];
    private Character characterActing;
    private final Stack<byte[]> recorder = new Stack<>();
    public boolean isControlRate = false;
    private GuiHuo[] guiHuo = new GuiHuo[2];
    public BattlePane(Stage stage, Initializer.Back back, List<PropertiesHolder> list) {
        this.back = back;
        this.list = list;
        this.characters = new ArrayList<>();
        stage.setScene(new Scene(root));

        setupUI();
        init();
        repaintActionBar();

        guiHuo[0] = new GuiHuo(4);
        guiHuo[1] = new GuiHuo(4);
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

//        teamPane[0].setMaxWidth(Double.MAX_VALUE);
//        teamPane[1].setMaxWidth(Double.MAX_VALUE);

        teamPane[0].setSpacing(5);
        teamPane[1].setSpacing(5);
        teamPane[0].setPadding(new Insets(5));

//        teamPane[0].setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, null, new BorderWidths(3))));
//        teamPane[1].setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, null, new BorderWidths(3))));

        VBox container = new VBox(teamPane[1], teamPane[0]);
        //container.setSpacing(5);
        container.setPadding(new Insets(3));
        container.setPrefWidth(1000);
//        container.setMaxWidth(Double.MAX_VALUE);
        teamPane[0].minHeightProperty().bind(root.heightProperty().divide(2).subtract(5));
        teamPane[1].minHeightProperty().bind(root.heightProperty().divide(2).subtract(5));

        ScrollPane scrollPane = new ScrollPane(container);
        scrollPane.setMaxWidth(1100);
        root.setCenter(scrollPane);
//        reloadCharacterIcon();
    }

    private void reloadCharacterIcon() {
        teamPane[0].getChildren().clear();
        teamPane[1].getChildren().clear();
        for (Character character : characters) {
            teamPane[character.team].getChildren().add(character.getCharacterIcon(this::setAutoTo));
        }
    }
    public boolean canUseGuiHuo(Character character, int num) {
        if (character.isMob()) {
            return GuiHuo.mobCanUseGuiHuo(character, num);
        } else {
            return guiHuo[character.team].canUseGuiHuo(num);
        }
    }

    public void useGuiHuo(Character character, int num) {
        if (character.isMob()) {
            GuiHuo.mobUseGuiHuo(character, num);
        } else {
            guiHuo[character.team].useGuiHuo(num);
        }
    }

    private void setAutoTo(Character characterSelected) {
        // 如果没标任何人，给选择目标设置标
        // 如果已有标，且和选择的目标不是一个，换到新目标
        // 如果已有标，且和选择的目标是一个，取消标

        if (autoTo[characterSelected.team] == null) {
            autoTo[characterSelected.team] = characterSelected;
            characterSelected.characterIcon.setIsAuto(true);
        } else {
            autoTo[characterSelected.team].characterIcon.setIsAuto(false);
            if (autoTo[characterSelected.team] != characterSelected) {
                characterSelected.characterIcon.setIsAuto(true);
                autoTo[characterSelected.team] = characterSelected;
            } else {
                autoTo[characterSelected.team] = null;
            }

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
        rateControl.selectedProperty().addListener((obs, old, val) -> this.isControlRate = val);

        Button prevBtn = new Button("上一个");
        Button nextBtn = new Button("下一个");
        Button backBtn = new Button("返回");
        prevBtn.setOnAction(event -> prev());
        nextBtn.setOnAction(event -> next());
        backBtn.setOnAction(event -> back.back());
        prevBtn.setPrefSize(100, 25);
        nextBtn.setPrefSize(100, 25);
        backBtn.setPrefSize(100, 25);

        CustomTextField round = new CustomTextField();
        Button skip = new Button("跳过回合");
        skip.setOnAction(event -> skip(Integer.parseInt(round.getText())));

        round.setPrefSize(100, 25);
        skip.setPrefSize(100, 25);


        GridPane controller = new GridPane();
        controller.setPadding(new Insets(0, 0, 50, 0)); // 底部留空

        controller.add(rateControl, 0, 0);
        controller.add(calc, 1, 0);
        controller.add(prevBtn, 0, 1);
        controller.add(nextBtn, 1, 1);
        controller.add(round, 0, 2);
        controller.add(skip, 1, 2);
        controller.add(backBtn, 0, 3);

        return controller;
    }

    private void configureActionBar() {
        actionBar.setPrefWidth(200);
        actionBar.setOnMouseClicked(event -> {
            if (actionBarType == ActionBarType.SHUN_WEI) {
                actionBarType = ActionBarType.JIN_DU;
            } else {
                actionBarType = ActionBarType.SHUN_WEI;
            }
            repaintActionBar();
        });
//        actionBar.setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, null, new BorderWidths(3))));
    }

    private void init() {
        for (PropertiesHolder holder : list) {
            int team = holder.map().get(PropertyKey.GENERAL_TEAM_KEY).getInt();
            if (team == 0 || team == 1) {
                Character character = CharacterFactory.getCharacter(holder);
                if (character != null) {
                    addCharacter(character);
                }
            }
        }

        getNextActor();

        // 先机
        for (Character character : characters) {
            character.useFrontSkill(this);
        }

        log.characterAct(characterActing);
    }

    private void addCharacter(Character character) {
        characters.add(character);
        teamPane[character.team].getChildren().add(character.getCharacterIcon(this::setAutoTo));
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

    private List<Character> getCharactersByActionSort() {
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
        for (int i = 1; i < 9; i++) {
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

        if (actionBarType == ActionBarType.SHUN_WEI) {

            List<Character> list = getCharactersByActionSort();
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

        } else if (actionBarType == ActionBarType.JIN_DU) {
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

            CharacterStack prev = null;
            try (ByteArrayInputStream bis = new ByteArrayInputStream(recorder.pop());
                 ObjectInputStream ois = new ObjectInputStream(bis)
            ){
                prev = (CharacterStack) ois.readObject();
            } catch (IOException | ClassNotFoundException e) {
                Utils.throwException("恢复时出错", e);
            }
            
            if (prev == null) {
                return;
            }

            characters = prev.characters();
            characterActing = prev.characterActing();
            this.autoTo = prev.autoTo();
            this.guiHuo = prev.guiHuos();
            this.calc.setRate(prev.totalRate());

            repaintActionBar();
            reloadCharacterIcon();

            if (autoTo[0] != null) {
                autoTo[0].characterIcon.setIsAuto(true);
            }

            if (autoTo[1] != null) {
                autoTo[1].characterIcon.setIsAuto(true);
            }

            log.prev();
        }

    }
    private void next() {
        try (ByteArrayOutputStream bos = new ByteArrayOutputStream();
             ObjectOutputStream oos = new ObjectOutputStream(bos)
        ){
            oos.writeObject(new CharacterStack(characters, characterActing, autoTo, guiHuo, calc.getRate()));
            recorder.push(bos.toByteArray());
        } catch (IOException e) {
            Utils.throwException("保存时出错", e);
        }

        characterActing.round(this);
        getNextActor();
        log.characterAct(characterActing);
        repaintActionBar();

        for (Character character : characters) {
            character.characterIcon.update();
        }
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
        characterActing = next;

        next.setLocation(0);
        next.timesToAct++;
        next.beforeRound(this);
    }

    private void skip(int round) {
        for (int i = 0; i < round; i++) {
            next();
        }
    }

    public void removeCharacter(Character character) {
        characters.remove(character);
        teamPane[character.team].getChildren().remove(character.characterIcon);
    }

    public boolean canSummon(int team) {
        for (Character character : characters) {
            if (character.team == team && character.isSummon()) {
                return false;
            }
        }
        return true;
    }

    public void addSummon(int team, Character character) {

    }

    private enum ActionBarType {
        JIN_DU,
        SHUN_WEI
    }

}
