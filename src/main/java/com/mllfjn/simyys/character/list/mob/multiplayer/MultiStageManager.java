package com.mllfjn.simyys.character.list.mob.multiplayer;

import com.mllfjn.simyys.battleevent.EventActionDone;
import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.utils.SerializableRunnable;
import com.mllfjn.simyys.utils.SerializableSupplier;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.input.MouseEvent;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.Queue;

public class MultiStageManager implements Serializable {
    private final Queue<SerializableRunnable> stageQueue = new LinkedList<>();
    private final SerializableSupplier<Boolean> canChangeStage;
    private final Character character;

    private boolean prepareChangeStage = false;

    private transient ContextMenu contextMenu;

    public MultiStageManager(Character character, SerializableSupplier<Boolean> canChangeStage) {
        this.canChangeStage = canChangeStage;
        this.character = character;
    }

    public EventHandler<MouseEvent> getEventHandler() {
        return event -> {
            if (!prepareChangeStage && !stageQueue.isEmpty() && canChangeStage.get()) {
                getContextMenu().show((Node) event.getSource(), event.getScreenX(), event.getScreenY());
            }
        };
    }

    private ContextMenu getContextMenu() {
        if (contextMenu == null) {
            MenuItem itemSkip = new MenuItem("跳过当前回合切换阶段");
            MenuItem itemAfter = new MenuItem("该回合行动后切换阶段");

            itemSkip.setOnAction(event -> {
                character.bp.skipCharacterAct();
                changeStage();
            });

            itemAfter.setOnAction(event ->
                    character.bp.addActionListener(character, e -> {
                        prepareChangeStage = true;
                        if (e instanceof EventActionDone) {
                            changeStage();
                            prepareChangeStage = false;
                            return true;
                        }
                        return false;
                    })
            );

            contextMenu = new ContextMenu(itemSkip, itemAfter);
        }
        return contextMenu;
    }

    public void changeStage() {
        if (!stageQueue.isEmpty()) {
            stageQueue.poll().run();
        }
    }

    public void addStage(SerializableRunnable stage) {
        stageQueue.add(stage);
    }


}