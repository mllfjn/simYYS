package com.mllfjn.simyys.character.list.sp.fuji;

import com.mllfjn.simyys.character.CharacterShiShenBase;

public class FuJi extends CharacterShiShenBase {
    public static final String CharacterName = "缚骨清姬";

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
        return "3028";
    }

    @Override
    protected void addOwnSkills() {
        int defensePerStack = skill1Level >= 5 ? 50 : 20;
        addSkill(new Skill1(this, skill1Level, defensePerStack));
//        addSkill(new Skill2(this, skill2Level));
        addSkill(new Skill3(this, skill3Level, defensePerStack));
    }
}
