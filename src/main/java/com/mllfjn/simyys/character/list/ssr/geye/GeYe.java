package com.mllfjn.simyys.character.list.ssr.geye;

import com.mllfjn.simyys.character.CharacterShiShenBase;

public class GeYe extends CharacterShiShenBase {
    public static final String CharacterName = "葛叶";

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
        return "3966";
    }

    @Override
    protected void addOwnSkills() {
        addSkill(new Skill1(this, skill1Level));


        addSkill(new Skill4(this, skill3Level));
    }
}
