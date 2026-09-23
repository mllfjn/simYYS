package com.mllfjn.simyys.character.list.r.shouwu;

import com.mllfjn.simyys.character.CharacterShiShenBase;

public class ShouWu extends CharacterShiShenBase {
    public static final String CharacterName = "首无";

    @Override
    protected boolean useSkillAuto() {
        return tryUseSkill(3);
    }

    @Override
    protected String getDefaultSkillLevel() {
        return "515";
    }

    @Override
    protected boolean canAwakening() {
        return false;
    }

    @Override
    protected String getDefaultBaseAttack() {
        return "2894";
    }

    @Override
    protected void addOwnSkills() {
        Skill2 skill2 = new Skill2(this);
        addSkill(new Skill1(this, skill1Level, skill2));
        addSkill(skill2);
        addSkill(new Skill3(this, skill3Level, skill2));
    }
}
