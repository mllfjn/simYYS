package com.mllfjn.simyys.character.yuhun;

import com.mllfjn.simyys.character.Character;

import java.io.Serializable;

public abstract class YuHun implements Serializable {
    protected Character character;
    public abstract String getName();
    public void init(Character character) {
        this.character = character;
    }

    protected Character getBelongTo() {
        return character;
    }

    protected void yuHunEffect() {
        character.bp.interactive.addYuHunEffectLog(character, getName());
    }
}
