package com.mllfjn.simyys.character.list.ssr.bujianyue;

import com.mllfjn.simyys.character.CharacterShiShenBase;

public class BuJianYue extends CharacterShiShenBase {
    public static final String CharacterName = "不见岳";

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
        return "2492";
    }

    @Override
    protected void addOwnSkills() {
        addSkill(new Skill1(this, skill1Level));
    }
}
