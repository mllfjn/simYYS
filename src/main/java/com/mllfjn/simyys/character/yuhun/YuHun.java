package com.mllfjn.simyys.character.yuhun;

import com.mllfjn.simyys.character.Character;

import java.io.Serializable;

public abstract class YuHun implements Serializable {
    private Character character;
    public abstract String getName();
    public void init(Character character) {
        this.character = character;
    }

    protected Character getBelongTo() {
        return character;
    }
}
