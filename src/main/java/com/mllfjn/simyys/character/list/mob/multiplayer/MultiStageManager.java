package com.mllfjn.simyys.character.list.mob.multiplayer;

import com.mllfjn.simyys.battleevent.BattleActionListener;
import com.mllfjn.simyys.battleevent.BattleEvent;
import com.mllfjn.simyys.battleevent.EventActionDone;
import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.EventHandlerContainer;
import com.mllfjn.simyys.character.status.instance.StatusDieHandler;
import com.mllfjn.simyys.utils.SerializableConsumer;
import com.mllfjn.simyys.utils.SerializableRunnable;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.input.MouseEvent;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class MultiStageManager implements Serializable, EventHandlerContainer {
    private final Queue<SerializableRunnable> stageQueue = new LinkedList<>();
    private final List<Character> summonList = new ArrayList<>(5);
    private final Character character;

    // 是否允许转阶段
    private boolean canChangeStage = true;

    // 标记已经选择了该回合后转阶段，此时不应继续显示转阶段按钮
    private boolean prepareChangeStage = false;

    // 是否在召唤的小怪死后立刻进入下一阶段
    private boolean autoChangeStage = false;

    // 召唤的小怪死亡后回调
    private SerializableConsumer<Character> summonDieCallback;

    // 死亡后是否需要调用处理方法,用于清除所有召唤物
    private boolean needHandle = true;

    // 点击菜单
    private transient ContextMenu contextMenu;

    public MultiStageManager(Character character) {
        this.character = character;
    }

    public void changeStage() {
        if (!stageQueue.isEmpty()) {
            stageQueue.poll().run();
        }
    }

    public void addStage(SerializableRunnable stage) {
        stageQueue.add(stage);
    }

    public void addSummon(Character character) {
        summonList.add(character);
        character.bp.addCharacter(character);
        character.addStatus(new StatusDieHandler(character, () -> summonDie(character)));
    }

    public void setCanChangeStage(boolean canChangeStage) {
        this.canChangeStage = canChangeStage;
    }

    public void setAutoChangeStage(boolean autoChangeStage) {
        this.autoChangeStage = autoChangeStage;
    }

    public void setSummonDieCallback(SerializableConsumer<Character> summonDieCallback) {
        this.summonDieCallback = summonDieCallback;
    }

    public List<Character> getSummonList() {
        return summonList;
    }

    private void summonDie(Character character) {
        if (needHandle) {
            if (summonList.contains(character)) {
                summonList.remove(character);
                if (summonDieCallback != null) {
                    summonDieCallback.accept(character);
                }

                if (summonList.isEmpty()) {
                    summonDieCallback = null;
                    if (autoChangeStage) {
                        changeStage();
                        autoChangeStage = false;
                    }
                }
            }
        }
    }

    public void clearSummon() {
        summonDieCallback = null;
        autoChangeStage = false;
        needHandle = false;
        summonList.forEach(Character::die);
        summonList.clear();
        needHandle = true;
    }

    private ContextMenu getContextMenu() {
        if (contextMenu == null) {
            MenuItem itemSkip = new MenuItem("跳过当前回合切换阶段");
            MenuItem itemAfter = new MenuItem("该回合行动后切换阶段");

            itemSkip.setOnAction(event -> {
                changeStage();
                character.bp.skipCharacterAct();
            });

            itemAfter.setOnAction(event -> {
                        prepareChangeStage = true;
                character.bp.addActionListener(new BattleActionListener(character) {
                    @Override
                    public boolean onBattleAction(BattleEvent event) {
                        if (event instanceof EventActionDone) {
                            changeStage();
                            prepareChangeStage = false;
                            return true;
                        }
                        return false;
                    }
                        });
                    }

            );

            contextMenu = new ContextMenu(itemSkip, itemAfter);
        }
        return contextMenu;
    }

    @Override
    public void handle(MouseEvent event) {
        if (!prepareChangeStage && !stageQueue.isEmpty() && canChangeStage) {
            getContextMenu().show((Node) event.getSource(), event.getScreenX(), event.getScreenY());
        }
    }
}