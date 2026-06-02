package com.mllfjn.simyys.character.list.sphudie;

import com.mllfjn.simyys.character.CharacterShiShenBase;

public class SpHuDie extends CharacterShiShenBase {
    public static final String CharacterName = "梦引蝴蝶精";

    @Override
    protected String getDefaultSkillLevel() {
        return "555";
    }

    @Override
    protected boolean canAwakening() {
        return false;
    }

    @Override
    protected String getDefaultBaseAttack() {
        return "";
    }

    @Override
    protected void addOwnSkills() {

    }
}
