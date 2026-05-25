package com.mllfjn.simyys.character.list.ssr.sijinshen;

import com.mllfjn.simyys.character.CharacterShiShenBase;

public class SiJinShen extends CharacterShiShenBase {
    public static final String CharacterName = "思金神";

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
        return "";
    }

    @Override
    protected void addOwnSkills() {

    }
}
