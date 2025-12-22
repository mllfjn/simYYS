package com.mllfjn.simyys.character.list.sr.haifangzhu;

import com.mllfjn.simyys.character.CharacterShiShenBase;

public class HaiFangZhu extends CharacterShiShenBase {
    public static final String CharacterName = "海坊主";

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
        return "3055";
    }

    @Override
    protected void addOwnSkills() {
        addSkill(new Skill1(this, skill1Level));
    }
}
