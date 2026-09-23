package com.mllfjn.simyys.character.list.sr.yaoqin;

import com.mllfjn.simyys.character.CharacterShiShenBase;

public class YaoQin extends CharacterShiShenBase {
    public static final String CharacterName = "妖琴师";

    @Override
    protected String getDefaultBaseAttack() {
        return "2573";
    }

    @Override
    protected String getDefaultSkillLevel() {
        return "555";
    }

    @Override
    protected boolean canAwakening() {
        return true;
    }

    @Override
    protected boolean useSkillAuto() {
        return tryUseSkill(3) || tryUseSkill(2);
    }

    @Override
    protected void addOwnSkills() {
        addSkill(new Skill1(this, skill1Level));
        addSkill(new Skill2(this, skill2Level));
    }
}
