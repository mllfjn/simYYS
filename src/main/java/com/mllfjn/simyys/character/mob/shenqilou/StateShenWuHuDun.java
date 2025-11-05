package com.mllfjn.simyys.character.mob.shenqilou;

import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.state.StateShield;

public class StateShenWuHuDun extends StateShield {
    public static final String StateName = "蜃雾护盾";

    public StateShenWuHuDun(Character from, Character belongTo) {
        super(from, belongTo, belongTo.getMaxHp());
    }

    @Override
    public String getText() {
        return StateName;
    }
}
