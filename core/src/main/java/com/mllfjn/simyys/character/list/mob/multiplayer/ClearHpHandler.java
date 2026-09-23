package com.mllfjn.simyys.character.list.mob.multiplayer;

import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.EventHandlerContainer;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.input.MouseEvent;

public class ClearHpHandler implements EventHandlerContainer {
    private final Character character;

    private transient ContextMenu contextMenu;

    public ClearHpHandler(Character character) {
        this.character = character;
    }

    @Override
    public void handle(MouseEvent event) {
        if (character.getHp() > 0.01) {
            getContextMenu().show(character.getCharacterIcon().getCenter(), event.getScreenX(), event.getScreenY());
        }
    }

    private ContextMenu getContextMenu() {
        if (contextMenu == null) {
            MenuItem item = new MenuItem("承受下次攻击后死亡");
            item.setOnAction(event -> {
                character.setHpWithoutTrigger(0.01);
                character.getCharacterIcon().update();
            });
            contextMenu = new ContextMenu(item);
        }
        return contextMenu;
    }
}
