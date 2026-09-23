package com.mllfjn.simyys.character.list.ssr.shiling;

import com.mllfjn.simyys.character.CharacterShiShenBase;

public class ShiLing extends CharacterShiShenBase {
    public static final String CharacterName = "食灵";

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
        return "3028";
    }

    @Override
    protected boolean useSkillAuto() {
        return tryUseSkill(3);
    }

    @Override
    protected void addOwnSkills() {
        addSkill(new Skill1(this, skill1Level));
        addSkill(new Skill2(this, skill2Level));
        addSkill(new Skill3(this, skill3Level));
    }
}
