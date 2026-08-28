package com.mllfjn.simyys.character.list.sp.spyue;

import com.mllfjn.simyys.character.CharacterShiShenBase;

public class SpYue extends CharacterShiShenBase {
    public static final String CharacterName = "云间不见岳";

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
        return "2412";
    }

    @Override
    protected void addOwnSkills() {
        addSkill(new Skill1(this, skill1Level));
        addSkill(new Skill2(this, skill2Level));
    }
}
