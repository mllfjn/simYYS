package com.mllfjn.simyys;

import com.mllfjn.simyys.battleevent.*;
import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.CharacterFactory;
import com.mllfjn.simyys.character.PropertyKey;
import com.mllfjn.simyys.character.list.ssr.xunxiangxing.StatusShiShen;
import com.mllfjn.simyys.character.propertygetter.FlagChangeInfo;
import com.mllfjn.simyys.character.skill.CharacterFinder;
import com.mllfjn.simyys.character.skill.Skill;
import com.mllfjn.simyys.character.status.Status;
import com.mllfjn.simyys.character.status.determinant.RetainAfterChangeWave;
import com.mllfjn.simyys.customnode.CustomTextField;
import com.mllfjn.simyys.customnode.TextFlowLog;
import com.mllfjn.simyys.guihuo.GuiHuo;
import com.mllfjn.simyys.guihuo.MobGuiHuo;
import com.mllfjn.simyys.interactive.Interactive;
import com.mllfjn.simyys.ratecontroller.RateCalc;
import com.mllfjn.simyys.character.propertygetter.PropertiesHolder;
import com.mllfjn.simyys.collections.SerializableObservableList;
import com.mllfjn.simyys.starter.Initializer;
import com.mllfjn.simyys.starter.LockSkillAndFlag;
import com.mllfjn.simyys.starter.CharacterNameAndTeam;
import com.mllfjn.simyys.utils.SerializableConsumer;
import com.mllfjn.simyys.utils.SerializableRunnable;
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
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.io.*;
import java.util.*;
import java.util.function.Consumer;

public class BattlePane {
    // 所有需要在返回上一步时恢复的内容封装在这里
    public SerializableItems situation = new SerializableItems();
    // 初始化属性列表
    private final SerializableObservableList<PropertiesHolder> propertiesHolderList;
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
    // 回合外使用技能
    // TODO 换成优先队列
    private final List<Runnable> outRoundSkillList = new ArrayList<>();
    // 主界面
    private final BorderPane root = new BorderPane();
    // 行动条显示模式,初始为顺位
    private ActionBarType actionBarType = ActionBarType.SHUN_WEI;
    private final AnchorPane actionBar = new AnchorPane();
    // teamPane容器
    private final StackPane teamPaneContainer1 = new StackPane();
    private final StackPane teamPaneContainer0 = new StackPane();

    // 行动顺序记录
    private final SerializableObservableList<CharacterNameAndTeam> actionOrder = new SerializableObservableList<>();
    // 锁定技能和红绿标情况
    private final List<LockSkillAndFlag> lockSkillAndFlagList = new ArrayList<>();
    private LockSkillAndFlag currentLockSkillAndFlag;


    public BattlePane(Scene scene, Pane stageRoot, Runnable back,
                      SerializableObservableList<PropertiesHolder> PropertiesHolderList, Initializer initializer) {
        this.propertiesHolderList = PropertiesHolderList;
        stageRoot.getChildren().set(0, root);

        setupUI(back, scene, initializer);
        init();
        repaint();
    }

    // 该构造方法用于预测模式
    public BattlePane(SerializableObservableList<PropertiesHolder> PropertiesHolderList) {
        this.propertiesHolderList = PropertiesHolderList;

        init();
    }

    public void predictionShow(Stage stage, Runnable back, Initializer initializer) {
        Scene scene = new Scene(root);
        stage.setScene(scene);
        setupUI(back, scene, initializer);
        repaint();
    }

    private void setupUI(Runnable back, Scene scene, Initializer initializer) {
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
        right.setBottom(configureControlPane(back, scene, initializer));
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

    public GuiHuo getGuiHuoInstance(int team) {
        return situation.teamPane[team].getGuiHuoInstance();
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

    private GridPane configureControlPane(Runnable back, Scene scene, Initializer initializer) {
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
        Button saveLockSkillAndFlagBtn = new Button("技能和红绿标");
        prevBtn.setOnAction(event -> prev());
        nextBtn.setOnAction(event -> {
            next(false);
            repaint();
        });
        savePredictionBtn.setOnAction(event -> {
            initializer.prediction.predictionOrder = actionOrder;
            initializer.prediction.copyToClipboard();
        });
        saveLockSkillAndFlagBtn.setOnAction(event -> {
            for (LockSkillAndFlag lockSkillAndFlag : lockSkillAndFlagList) {
                int lockSkill = lockSkillAndFlag.getLockSkill();
                int flagTarget = lockSkillAndFlag.getFlagTarget();
                if (lockSkill == -1 && flagTarget == -1) {
                    continue;
                }
                String name = lockSkillAndFlag.getCharacterName();
                int team = lockSkillAndFlag.getTeam();
                for (PropertiesHolder item : initializer.items) {
                    if (item.name.equals(name)) {
                        if (team == item.propertiesMap.get(PropertyKey.GENERAL_TEAM_KEY).getInt()) {
                            int timesToAct = lockSkillAndFlag.getTimesToAct();
                            if (lockSkill != -1) {
                                item.lockSkillMap.put(timesToAct, lockSkill);
                            }

                            if (flagTarget != -1) {
                                item.flagChangeMap.put(
                                        timesToAct,
                                        new FlagChangeInfo(lockSkillAndFlag.getFlagType(), flagTarget + 1)
                                );
                            }
                            break;
                        }
                    }
                }
            }
        });

        prevBtn.setPrefSize(100, 25);
        nextBtn.setPrefSize(100, 25);
        backBtn.setPrefSize(100, 25);
        savePredictionBtn.setPrefSize(100, 25);
        saveLockSkillAndFlagBtn.setPrefSize(100, 25);

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
        controller.add(savePredictionBtn, 0, 3);
        controller.add(saveLockSkillAndFlagBtn, 1, 3);

        controller.add(backBtn, 0, 4);

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
        int wave0 = situation.getWave(0);
        int wave1 = situation.getWave(1);
        for (PropertiesHolder holder : propertiesHolderList) {
            int wave = holder.propertiesMap.get(PropertyKey.GENERAL_WAVE_KEY).getInt();
            if (wave == wave0 || wave == wave1) {
                CharacterFactory.getCharacter(holder, this).ifPresent(this::addCharacter);
            }
        }


        Character characterActing = getNextOrder();
        situation.characterActing = characterActing;
        characterActing.timesToAct++;
        characterActing.forceSetLocation(0);
        actionOrder.add(new CharacterNameAndTeam(characterActing.name, characterActing.team));
        currentLockSkillAndFlag = new LockSkillAndFlag(characterActing);

        // 战斗开始
        // 先机
        situation.priorityMove();
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

    private List<Character> getCharactersByLocation() {
        return situation.getCharactersChangeLocation().stream().sorted((o1, o2) -> {
            if (o1.getLocation() > o2.getLocation()) {
                return -1;
            } else if (o1.getLocation() < o2.getLocation()) {
                return 1;
            } else return Double.compare(o1.getSpeed(), o2.getSpeed());
        }).toList();
    }

    private List<Character> getCharactersByActionSort() {
        List<Character> rt = new ArrayList<>();
        List<Character> list = situation.getCharactersChangeLocation();
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

        actionBar.getChildren().clear();

        if (actionBarType == ActionBarType.SHUN_WEI) {

            List<Character> list = getCharactersByActionSort();
            Node imageBig = CharacterFactory.getImageWithStroke(list.get(0), CharacterFactory.ImageSize.BIG, 3);
            imageBig.setLayoutY(yOffset + 8 * CharacterFactory.ImageSize.SMALL.size);
            imageBig.setLayoutX(layoutXBig);
            actionBar.getChildren().add(imageBig);
            for (int i = 1; i < 9; i++) {
                Node imageSmall = CharacterFactory.getImageWithStroke(list.get(i), CharacterFactory.ImageSize.SMALL, 3);
                imageSmall.setLayoutX(layoutXSmall);
                imageSmall.setLayoutY(yOffset + (8 - i) * CharacterFactory.ImageSize.SMALL.size);
                actionBar.getChildren().add(imageSmall);
            }

        } else if (actionBarType == ActionBarType.JIN_DU) {
            List<Character> list = getCharactersByLocation();
            for (int i = list.size() - 2; i >= 0; i--) {
                Node imageSmall = CharacterFactory.getImageWithStroke(list.get(i), CharacterFactory.ImageSize.SMALL, 3);
                // location在0的时候，y在0
                // location在100的时候，y在small.size * 7.5
                // y = location * small.size * 7.5 / 100
                imageSmall.setLayoutY(yOffset + list.get(i).getLocation() * CharacterFactory.ImageSize.SMALL.size * 7.5 / 100);
                imageSmall.setLayoutX(layoutXSmall);
                actionBar.getChildren().add(imageSmall);
            }
            Node imageBig = CharacterFactory.getImageWithStroke(list.get(list.size() - 1), CharacterFactory.ImageSize.BIG, 3);
            imageBig.setLayoutY(yOffset + 8 * CharacterFactory.ImageSize.SMALL.size);
            imageBig.setLayoutX(layoutXBig);
            actionBar.getChildren().add(imageBig);
        }
    }

    private void prev() {
        if (!recorder.isEmpty()) {

            actionOrder.subList(actionOrder.size() - situation.roundInLastStep, actionOrder.size()).clear();
            currentLockSkillAndFlag = lockSkillAndFlagList.remove(lockSkillAndFlagList.size() - 1);

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
            // 行动结束事件
            onTrigger(new EventActionDone(characterActing));
            Iterator<Runnable> iterator = outRoundSkillList.iterator();
            while (iterator.hasNext()) {
                iterator.next().run();
                iterator.remove();
            }
            getNextActor();
            characterActing = situation.characterActing;
            interactive.display();
            situation.teamPane[characterActing.team].totalActTimes++;
            situation.roundInLastStep++;
            log.characterAct(characterActing, situation.teamPane[characterActing.team].totalActTimes);
        } while (characterActing.isUncontrollable());

        lockSkillAndFlagList.add(currentLockSkillAndFlag);
        currentLockSkillAndFlag = new LockSkillAndFlag(situation.characterActing);

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
        List<Character> characters = situation.getCharactersChangeLocation();
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
                characters.get(i).setLocation(characters.get(i).getLocation() + speeds[i] * ttaMin,
                        false
                );
            }
        }

        Character character = characters.get(indexMin);
        for (Status status : character.getStatuses()) {
            if (status instanceof StatusShiShen) {
                status.delete();
                character.forceSetLocation(0);
                return getNextOrder();
            }
        }

        return character;
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

    public void removeCharacter(Character tobeRemovedCharacter) {
        situation.removeCharacter(tobeRemovedCharacter);
        onTrigger(new EventCharacterDie(tobeRemovedCharacter));

        int team = tobeRemovedCharacter.team;
        if (situation.teamPane[team].characters.isEmpty()) {
            situation.addWave(team);
            int wave = situation.getWave(team);
            for (PropertiesHolder propertiesHolder : propertiesHolderList) {
                if (wave == propertiesHolder.propertiesMap.get(PropertyKey.GENERAL_WAVE_KEY).getInt()) {
                    CharacterFactory.getCharacter(propertiesHolder, this).ifPresent(this::addCharacter);
                }
            }

            if (situation.teamPane[team].characters.isEmpty()) {
                Utils.information(team == 0 ? "失败" : "胜利");
            } else {
                // 如果游戏没结束,那么进入新的回目,阵亡方不可以再复活
                situation.deadCharacters.removeIf(character -> character.team == team);
                // 让对方的人清一遍状态
                int enemyTeam = CharacterFinder.getEnemyTeam(team);
                for (Character character : situation.teamPane[enemyTeam].characters) {
                    Iterator<Status> iterator = character.getStatuses().iterator();
                    while (iterator.hasNext()) {
                        Status status = iterator.next();
                        if (!(status instanceof RetainAfterChangeWave)) {
                            status.beforeDelete();
                            iterator.remove();
                        }
                    }
                }
            }
        }
    }

    public void removeCharacterWithoutTrigger(Character character) {
        situation.removeCharacter(character);
    }

    public void addActionListener(BattleActionListener listener) {
        if (listener.fromCharacter.alive) {
            situation.listeners.add(listener);
        }
    }

    public void removeActionListener(BattleActionListener listener) {
        situation.listeners.remove(listener);
    }

    public void onTrigger(BattleEvent event) {
        for (BattleActionListener listener : situation.listeners) {
            if (listener.onBattleAction(event)) {
                situation.listeners.remove(listener);
            }
        }
        situation.listeners.endIterator();
    }

    public void addPriorityMove(Character character, SerializableRunnable runnable) {
        situation.addPriorityMove(character, runnable);
    }

    public void characterSetLockSkill(Character character, int lockSkill) {
        if (character == situation.characterActing) {
            currentLockSkillAndFlag.setLockSkill(lockSkill);
        }
    }

    public void characterSetFlag(FlagChangeInfo.FlagType flagType, Character flagTarget) {
        currentLockSkillAndFlag.setFlag(flagType, situation.teamPane[flagTarget.team].characters.indexOf(flagTarget));
    }

    public boolean isMobBattle(Character character) {
        return situation.teamPane[1 - character.team].isMobTeam();
    }

    public void addOutRoundSkill(Skill skill, Runnable runnable) {
        // 将来skill.done要统一调用
        outRoundSkillList.add(runnable);
    }

    public BattleActionListener forEveryone(Character owner, SerializableConsumer<Character> action) {
        for (Character character : situation.characters) {
            action.accept(character);
        }

        BattleActionListener listener = new BattleActionListenerWrapper(owner, action);
        addActionListener(listener);
        return listener;
    }

    private enum ActionBarType {
        JIN_DU,
        SHUN_WEI
    }
}
