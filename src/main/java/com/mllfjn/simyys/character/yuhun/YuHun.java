package com.mllfjn.simyys.character.yuhun;

import com.mllfjn.simyys.character.Character;

import java.io.Serializable;

public abstract class YuHun implements Serializable {
    protected Character character;
    private boolean isInit;

    public abstract String getName();

    public void init(Character character, boolean isInit) {
        this.character = character;

        this.isInit = isInit;
    }

    public boolean isInit() {
        return isInit;
    }

    protected Character getBelongTo() {
        return character;
    }

    protected void yuHunEffect() {
        character.bp.interactive.addYuHunEffectLog(character, getName());
    }
}
