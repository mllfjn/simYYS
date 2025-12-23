package com.mllfjn.simyys.character.list.sp.kongmian;

import com.mllfjn.simyys.character.CharacterShiShenBase;

public class KongMian extends CharacterShiShenBase {
    public static final String CharacterName = "空相面灵气";

    @Override
    protected boolean useSkillAuto() {
        return tryUseSkill(3);
    }

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
        return "3162";
    }

    @Override
    protected void addOwnSkills() {
        addSkill(new Skill1(this, skill1Level));
        addSkill(new Skill2(this, skill2Level));
        addSkill(new Skill3(this, skill3Level));
    }
}
