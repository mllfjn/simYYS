package com.mllfjn.simyys.character.list.ssr.maochuan;

import com.mllfjn.simyys.character.CharacterShiShenBase;

public class MaoChuan extends CharacterShiShenBase {
    public static final String CharacterName = "猫川";

    @Override
    protected String getDefaultSkillLevel() {
        return "555";
    }

    @Override
    protected boolean canAwakening() {
        return true;
    }

    @Override
    protected String getDefaultBaseAttack() {
        return "3377";
    }

    @Override
    protected void addOwnSkills() {

    }
}
