package com.mllfjn.simyys.character.list.sp.spjin;

import com.mllfjn.simyys.character.CharacterShiShenBase;

public class SpJin extends CharacterShiShenBase {
    public static final String CharacterName = "瑶音紧那罗";

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
        return "3296";
    }

    @Override
    protected void addOwnSkills() {
        addSkill(new Skill1(this, skill1Level));
        addSkill(new Skill2(this, skill2Level));
        addSkill(new Skill3(this, skill3Level));
    }
}
