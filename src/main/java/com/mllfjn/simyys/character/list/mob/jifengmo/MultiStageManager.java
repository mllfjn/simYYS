package com.mllfjn.simyys.character.list.mob.jifengmo;

import com.mllfjn.simyys.battleevent.EventActionDone;
import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.utils.SerializableRunnable;
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
    private final Character character;

    private boolean prepareChangeStage = false;

    private transient ContextMenu contextMenu;

    public MultiStageManager(Character character) {
        this.character = character;
    }

    public EventHandler<MouseEvent> getEventHandler() {
        return event -> {
            if (!prepareChangeStage && !stageQueue.isEmpty()) {
                getContextMenu().show((Node) event.getSource(), event.getScreenX(), event.getScreenY());
            }
        };
    }

    private ContextMenu getContextMenu() {
        if (contextMenu == null) {
            MenuItem item1 = new MenuItem("跳过当前回合切换阶段");
            MenuItem item2 = new MenuItem("该回合行动后切换阶段");

            item1.setOnAction(event -> {
                stageQueue.poll().run();
                character.bp.skipCharacterAct();
            });

            item2.setOnAction(event -> {
                character.bp.addActionListener(character, e -> {
                    prepareChangeStage = true;
                    if (e instanceof EventActionDone) {
                        stageQueue.poll().run();
                        prepareChangeStage = false;
                        return true;
                    }
                    return false;
                });

            });

            contextMenu = new ContextMenu(item1, item2);
        }
        return contextMenu;
    }

    public void addStage(SerializableRunnable stage) {
        stageQueue.add(stage);
    }


}