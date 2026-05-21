package com.mllfjn.simyys;

import com.mllfjn.simyys.battleevent.*;
import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.CharacterFactory;
import com.mllfjn.simyys.character.PropertyKey;
import com.mllfjn.simyys.collections.SafeList;
import com.mllfjn.simyys.customnode.CustomTextField;
import com.mllfjn.simyys.customnode.TextFlowLog;
import com.mllfjn.simyys.guihuo.MobGuiHuo;
import com.mllfjn.simyys.guihuo.SubstituteProvider;
import com.mllfjn.simyys.interactive.Interactive;
import com.mllfjn.simyys.ratecontroller.RateCalc;
import com.mllfjn.simyys.character.propertygetter.PropertiesHolder;
import com.mllfjn.simyys.collections.SerializableObservableList;
import com.mllfjn.simyys.starter.CharacterNameAndTeam;
import com.mllfjn.simyys.starter.Prediction;
import com.mllfjn.simyys.utils.SerializableConsumer;
import com.mllfjn.simyys.utils.Utils;
import javafx.beans.binding.DoubleBinding;
import javafx.collections.ObservableMap;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.io.*;
import java.util.*;
import java.util.function.Consumer;

public class BattlePane {
    // 所有需要在返回上一步时恢复的内容封装在这里
    public SerializableItems situation = new SerializableItems();
    // 初始化属性列表
    private final SerializableObservableList<PropertiesHolder> list;
    // 概率控制模式
    public final RateCalc calc = new RateCalc();
    // 交互
    public final Interactive interactive = new Interactive(this);
    // 日志
    public final TextFlowLog log = new TextFlowLog();
    // 左边显示信息用的容器，包括log
    private final VBox info = new VBox(log);
    // 战斗记录,用于撤销
    private final Stack<byte[]> recorder = new Stack<>();
    // 主界面
    private final BorderPane root = new BorderPane();
    // 行动条显示模式,初始为顺位
    private ActionBarType actionBarType = ActionBarType.SHUN_WEI;
    private final AnchorPane actionBar = new AnchorPane();
    // teamPane容器
    private final StackPane teamPaneContainer1 = new StackPane();
    private final StackPane teamPaneContainer0 = new StackPane();
    // 战斗开始时事件列表，用于先机
    private List<Runnable> battleStartEventList = new ArrayList<>();

    // 行动顺序记录
    private final SerializableObservableList<CharacterNameAndTeam> actionOrder = new SerializableObservableList<>();


    public BattlePane(Scene scene, Pane stageRoot, Runnable back,
                      SerializableObservableList<PropertiesHolder> list, Prediction prediction) {
        this.list = list;
        stageRoot.getChildren().set(0, root);

        setupUI(back, scene, prediction);
        init();
        repaint();
    }

    // 该构造方法用于预测模式
    public BattlePane(SerializableObservableList<PropertiesHolder> list) {
        this.list = list;

        init();
    }

    public void predictionShow(Stage stage, Runnable back, Prediction prediction) {
        Scene scene = new Scene(root);
        stage.setScene(scene);
        setupUI(back, scene, prediction);
        repaint();
    }

    private void setupUI(Runnable back, Scene scene, Prediction prediction) {
        // 右边是主操作区
        // 主体偏上是行动条，点击切换模式
        // 右下角是控制区

        // 队伍区
        ScrollPane scrollPane1 = new ScrollPane(teamPaneContainer1);
        ScrollPane scrollPane0 = new ScrollPane(teamPaneContainer0);

        scrollPane1.setFitToWidth(true);
        scrollPane0.setFitToWidth(true);

        VBox pane = new VBox(scrollPane1, scrollPane0);

        DoubleBinding childHeight = pane.heightProperty().divide(2);
        scrollPane1.prefHeightProperty().bind(childHeight);
        scrollPane0.prefHeightProperty().bind(childHeight);

        root.setCenter(pane);
        // 控制区
        BorderPane right = new BorderPane();
        right.setTop(actionBar);
        right.setBottom(configureControlPane(back, scene, prediction));
        root.setRight(right);
        configureActionBar();
        reloadTeamPane();

        // 日志区
        VBox.setVgrow(log, Priority.ALWAYS);
        info.setPrefWidth(400);
        root.setLeft(info);
    }

    private void reloadTeamPane() {
        Pane pane1 = situation.teamPane[1].getPane();
        Pane pane0 = situation.teamPane[0].getPane();

        if (!teamPaneContainer1.getChildren().isEmpty()) {
            teamPaneContainer1.getChildren().clear();
        }

        if (!teamPaneContainer0.getChildren().isEmpty()) {
            teamPaneContainer0.getChildren().clear();
        }

        teamPaneContainer1.getChildren().add(pane1);
        teamPaneContainer0.getChildren().add(pane0);
    }

    public void repaint() {
        repaintActionBar();

        for (Character character : situation.characters) {
            character.getCharacterIcon().update();
        }
    }

    public void setSubstituteProvider(int team, SubstituteProvider substituteProvider) {
        situation.teamPane[team].setSubstituteProvider(substituteProvider);
    }

    public Label requestInfoDisplayLabel() {
        Label label = new Label();
        label.setFont(new Font(30));
        info.getChildren().add(0, label);
        return label;
    }

    public boolean canUseGuiHuo(Character character, int num) {
        if (character.isMob()) {
            return MobGuiHuo.mobCanUseGuiHuo(character, num);
        } else {
            return situation.teamPane[character.team].canUseGuiHuo(num);
        }
    }

    public void useGuiHuo(Character character, int num) {
        if (character.isMob()) {
            MobGuiHuo.mobUseGuiHuo(character, num);
        } else {
            situation.teamPane[character.team].useGuiHuo(this, character, num);
        }
    }

    public void gainGuiHuo(Character character, int num) {
        if (character.isMob()) {
            MobGuiHuo.mobGainGuiHuo(character, num);
        } else {
            situation.teamPane[character.team].gainGuiHuo(num);
            interactive.guiHuo(character, num, "鬼火");
        }
    }

    public int getGuiHuoCount(Character character) {
        return situation.teamPane[character.team].getGuiHuoCount();
    }

    public void addGuiHuoProgress(int team) {
        situation.addProgress(team);
    }

    private GridPane configureControlPane(Runnable back, Scene scene, Prediction prediction) {
        /*控制区
        控制区的内容
        1、概率控制模式的开关
        2、流程控制
        上一个，下一个
        跳过指定回合数*/

        Button prevBtn = new Button("上一个(Q)");
        Button nextBtn = new Button("下一个(E)");
        Button backBtn = new Button("返回(B)");
        Button savePredictionBtn = new Button("保存预测顺序");
        prevBtn.setOnAction(event -> prev());
        nextBtn.setOnAction(event -> {
            next(false);
            repaint();
        });
        savePredictionBtn.setOnAction(event -> {
            prediction.predictionOrder = actionOrder;
            prediction.copyToClipboard();
        });

        prevBtn.setPrefSize(100, 25);
        nextBtn.setPrefSize(100, 25);
        backBtn.setPrefSize(100, 25);
        savePredictionBtn.setPrefSize(100, 25);

        CustomTextField round = new CustomTextField();
        Button skip = new Button("跳过回合(S)");
        skip.setOnAction(event -> {
            int skipRound = Utils.parseIntOrDefault(round.getText(), 0);
            if (skipRound > 0) {
                skip(skipRound);
                repaint();
            }
        });

        KeyCombination kcq = new KeyCodeCombination(KeyCode.Q);
        KeyCombination kce = new KeyCodeCombination(KeyCode.E);
        KeyCombination kcs = new KeyCodeCombination(KeyCode.S);
        KeyCombination kcb = new KeyCodeCombination(KeyCode.B);

        ObservableMap<KeyCombination, Runnable> accelerators = scene.getAccelerators();

        // next和edit快捷键冲突
        Runnable temp = accelerators.get(kce);
        backBtn.setOnAction(event -> {
            back.run();
            accelerators.put(kce, temp);
        });

        accelerators.put(kcq, prevBtn::fire);
        accelerators.put(kce, nextBtn::fire);
        accelerators.put(kcb, backBtn::fire);
        accelerators.put(kcs, skip::fire);

        round.setPrefSize(100, 25);
        skip.setPrefSize(100, 25);


        GridPane controller = new GridPane();
        controller.setPadding(new Insets(0, 0, 50, 0)); // 底部留空

        controller.add(calc.getControl(), 0, 0);
        controller.add(calc.getLabel(), 1, 0);
        controller.add(prevBtn, 0, 1);
        controller.add(nextBtn, 1, 1);
        controller.add(round, 0, 2);
        controller.add(skip, 1, 2);
        controller.add(backBtn, 0, 3);
        controller.add(savePredictionBtn, 1, 3);

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
            int team = holder.propertiesMap.get(PropertyKey.GENERAL_TEAM_KEY).getInt();
            if (team == 0 || team == 1) {
                CharacterFactory.getCharacter(holder, this).ifPresent(this::addCharacter);
            }
        }


        Character characterActing = getNextOrder();
        situation.characterActing = characterActing;
        characterActing.timesToAct++;
        characterActing.forceSetLocation(0);
        actionOrder.add(new CharacterNameAndTeam(characterActing.name, characterActing.team));

        // 战斗开始
        for (Runnable runnable : battleStartEventList) {
            runnable.run();
        }
        battleStartEventList = null;
        // 火灵
        situation.teamPane[0].calHuoLing();
        situation.teamPane[1].calHuoLing();

        characterActing.beforeRound();
        characterActing.setLockSkillAndAuto();

        interactive.display();
        situation.teamPane[characterActing.team].totalActTimes++;
        log.characterAct(characterActing, 1);
        log.next();
    }

    public void addCharacter(Character character) {
        situation.addCharacter(character);
        onTrigger(new EventAddCharacter(character));
    }

    /**
     * 仅向角色列表中添加角色，不进行其余操作
     */
    public void addToList(Character character) {
        situation.addToList(character);
    }

    /**
     * 仅从角色列表中移除角色，不进行其余操作
     */
    public void removeFromList(Character character) {
        situation.removeFromList(character);
    }

    private List<Character> getCharactersByLocation() {
        return situation.characters.stream().sorted((o1, o2) -> {
            if (o1.getLocation() > o2.getLocation()) {
                return -1;
            } else if (o1.getLocation() < o2.getLocation()) {
                return 1;
            } else return Double.compare(o1.getSpeed(), o2.getSpeed());
        }).toList();
    }

    private List<Character> getCharactersByActionSort() {
        List<Character> rt = new ArrayList<>();
        List<Character> list = situation.characters;
        int size = list.size();
        double[] distance = new double[size];
        double[] speed = new double[size];
        for (int i = 0; i < size; i++) {
            distance[i] = 100.0 - list.get(i).getLocation();
            speed[i] = list.get(i).getSpeed();
        }

        rt.add(situation.characterActing);
        for (int i = 0; i < 8; i++) {
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

    private void repaintActionBar() {
        double yOffset = 20;
        double layoutXBig = 40;
        double layoutXSmall = 62.5;
        Color color = Color.ORANGE;
        double strokeWidth = 3;

        actionBar.getChildren().clear();

        if (actionBarType == ActionBarType.SHUN_WEI) {

            List<Character> list = getCharactersByActionSort();
            Node imageBig = CharacterFactory.getImageWithStroke(list.get(0).name, CharacterFactory.ImageSize.BIG, color, strokeWidth);
            imageBig.setLayoutY(yOffset + 8 * CharacterFactory.ImageSize.SMALL.size);
            imageBig.setLayoutX(layoutXBig);
            actionBar.getChildren().add(imageBig);
            for (int i = 1; i < 9; i++) {
                Node imageSmall = CharacterFactory.getImageWithStroke(list.get(i).name, CharacterFactory.ImageSize.SMALL, color, strokeWidth);
                imageSmall.setLayoutX(layoutXSmall);
                imageSmall.setLayoutY(yOffset + (8 - i) * CharacterFactory.ImageSize.SMALL.size);
                actionBar.getChildren().add(imageSmall);
            }

        } else if (actionBarType == ActionBarType.JIN_DU) {
            List<Character> list = getCharactersByLocation();
            for (int i = list.size() - 2; i >= 0; i--) {
                Node imageSmall = CharacterFactory.getImageWithStroke(list.get(i).name, CharacterFactory.ImageSize.SMALL, color, strokeWidth);
                // location在0的时候，y在0
                // location在100的时候，y在small.size * 7.5
                // y = location * small.size * 7.5 / 100
                imageSmall.setLayoutY(yOffset + list.get(i).getLocation() * CharacterFactory.ImageSize.SMALL.size * 7.5 / 100);
                imageSmall.setLayoutX(layoutXSmall);
                actionBar.getChildren().add(imageSmall);
            }
            Node imageBig = CharacterFactory.getImageWithStroke(list.get(list.size() - 1).name, CharacterFactory.ImageSize.BIG, color, strokeWidth);
            imageBig.setLayoutY(yOffset + 8 * CharacterFactory.ImageSize.SMALL.size);
            imageBig.setLayoutX(layoutXBig);
            actionBar.getChildren().add(imageBig);
        }
    }

    private void prev() {
        if (!recorder.isEmpty()) {

            actionOrder.subList(actionOrder.size() - situation.roundInLastStep, actionOrder.size()).clear();

            try (ByteArrayInputStream bis = new ByteArrayInputStream(recorder.pop());
                 ObjectInputStream ois = new ObjectInputStream(bis)
            ) {
                situation = (SerializableItems) ois.readObject();
            } catch (Exception e) {
                Utils.throwException("恢复时出错", e);
                return;
            }
            info.getChildren().remove(0, info.getChildren().size() - 1);
            calc.setCurrentRate(situation.getCurrentRate());
            situation.reset(this);
            reloadTeamPane();
            repaint();
            log.prev();
        }
    }

    private void skip(int round) {
        for (int i = 0; i < round; i++) {
            next(false);
        }
    }

    public void next(boolean skip) {
        try (ByteArrayOutputStream bos = new ByteArrayOutputStream();
             ObjectOutputStream oos = new ObjectOutputStream(bos)
        ) {
            situation.setCurrentRate(calc.getCurrentRate());
            oos.writeObject(situation);
            recorder.push(bos.toByteArray());
            situation.roundInLastStep = 0;
        } catch (IOException e) {
            Utils.throwException("保存时出错", e);
        }


        Character characterActing = situation.characterActing;
        do {
            // 如果skip为真,当前行动角色跳过
            // 但是后续的其他角色不跳过
            if (!skip) {
                characterActing.round();
            }
            skip = false;

            characterActing.afterRound();
            // 这俩职责重复，将来合并
            // 行动结束事件
            onTrigger(new EventActionDone(characterActing));
            // 回合结束事件
            onTrigger(new EventRoundDone(characterActing));
            getNextActor();
            characterActing = situation.characterActing;
            interactive.display();
            situation.teamPane[characterActing.team].totalActTimes++;
            situation.roundInLastStep++;
            log.characterAct(characterActing, situation.teamPane[characterActing.team].totalActTimes);
        } while (characterActing.isUncontrollable());

        log.next();
    }

    public void skipCharacterAct() {
        next(true);
        repaint();
    }

    private void getNextActor() {
        // 如果有时之隙新回合,先时之隙,否则看一般获得新回合,最后跑条
        Character characterActing = situation.sZXNewRoundCharacter().orElseGet(() -> {
            Character newRoundCharacter = situation.newRoundCharacter().orElseGet(this::getNextOrder);
            // 这部分不在时之隙新回合生效
            newRoundCharacter.forceSetLocation(0);
            newRoundCharacter.beforeRound();
            return newRoundCharacter;
        });
        situation.characterActing = characterActing;
        characterActing.timesToAct++;
        characterActing.refreshSkills();
        characterActing.setLockSkillAndAuto();

        actionOrder.add(new CharacterNameAndTeam(characterActing.name, characterActing.team));
    }

    private Character getNextOrder() {
        List<Character> characters = situation.characters;
        int indexMin = -1;
        double ttaMin = Double.MAX_VALUE;
        double[] speeds = new double[characters.size()];
        for (int i = 0; i < characters.size(); i++) {
            speeds[i] = characters.get(i).getSpeed();
            double ttaNext = Character.getTTA(100 - characters.get(i).getLocationWhenGettingOrder(), speeds[i]);
            if (ttaNext < ttaMin) {
                ttaMin = ttaNext;
                indexMin = i;
            }
        }

        if (ttaMin > 0) {
            for (int i = 0; i < characters.size(); i++) {
                characters.get(i).setLocation(characters.get(i).getLocation() + speeds[i] * ttaMin);
            }
        }

        return characters.get(indexMin);
    }

    public Character getCharacterActing() {
        return situation.characterActing;
    }

    public Interactive getInteractive(Character character) {
        interactive.setOwner(character);
        return interactive;
    }

    public void doInteractive(Character character, Consumer<Interactive> action) {
        interactive.doInterActive(character, action);
    }

    public boolean canSummon(int team) {
        return situation.teamPane[team].canSummon();
    }

    public void removeCharacter(Character character) {
        situation.removeCharacter(character);
        onTrigger(new EventCharacterDie(character));
    }

    public void removeCharacterWithoutTrigger(Character character) {
        situation.characters.remove(character);
        situation.teamPane[character.team].removeCharacter(character);
    }

    public void addActionListener(Character character, BattleActionListener listener) {
        situation.listenerMap.computeIfAbsent(character, k -> new SafeList<>()).add(listener);
    }

    public void removeActionListener(Character character, BattleActionListener listener) {
        situation.listenerMap.get(character).safeRemove(listener);
    }

    public void onTrigger(BattleEvent event) {
        for (SafeList<BattleActionListener> list : situation.listenerMap.values()) {
            for (BattleActionListener battleActionListener : list) {
                if (battleActionListener.onBattleAction(event)) {
                    list.safeRemove(battleActionListener);
                }
            }
            list.endIterator();
//            list.removeIf(listener -> listener.onBattleAction(event));
        }
    }

    public void atBattleStart(Runnable runnable) {
        battleStartEventList.add(runnable);
    }

    public boolean isMobBattle(Character character) {
        return situation.teamPane[1 - character.team].isMobTeam();
    }

    public BattleActionListener forEveryone(Character owner, SerializableConsumer<Character> action) {
        for (Character character : situation.characters) {
            action.accept(character);
        }

        BattleActionListener listener = event -> {
            if (event instanceof EventAddCharacter eac) {
                action.accept(eac.getCharacter());
            }
            return false;
        };
        addActionListener(owner, listener);
        return listener;
    }

    private enum ActionBarType {
        JIN_DU,
        SHUN_WEI
    }
}
