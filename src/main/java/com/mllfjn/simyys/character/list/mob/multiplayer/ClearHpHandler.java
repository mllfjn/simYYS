package com.mllfjn.simyys.character.list.mob.multiplayer;

import com.mllfjn.simyys.character.Character;
import javafx.event.EventHandler;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.input.MouseEvent;

import java.io.Serializable;

public class ClearHpHandler implements Serializable {
    private final Character character;

    private transient ContextMenu contextMenu;

    public ClearHpHandler(Character character) {
        this.character = character;
    }

    public EventHandler<MouseEvent> getEventHandler() {
        if (contextMenu == null) {
            MenuItem item = new MenuItem("承受下次攻击后死亡");
            item.setOnAction(event -> {
                character.setHpWithoutTrigger(0.01);
                character.getCharacterIcon().update();
            });
            contextMenu = new ContextMenu(item);
        }
        return event -> {
            if (character.getHp() > 0.01) {
                contextMenu.show(character.getCharacterIcon().getCenter(), event.getScreenX(), event.getScreenY());
            }
        };
    }
}
