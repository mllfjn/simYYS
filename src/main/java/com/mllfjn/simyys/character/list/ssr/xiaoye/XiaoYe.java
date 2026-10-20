package com.mllfjn.simyys.character.list.ssr.xiaoye;

import com.mllfjn.simyys.character.CharacterShiShenBase;

public class XiaoYe extends CharacterShiShenBase {
    public static final String CharacterName = "咲耶";

    StatusYiSui statusYiSui;

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
        return true;
    }

    @Override
    protected String getDefaultBaseAttack() {
        return "2948";
    }

    @Override
    protected void addOwnSkills() {
        addSkill(new Skill1(this, skill1Level));
        addSkill(new Skill2(this, skill2Level));
        addSkill(new Skill3(this, skill3Level));
    }
}
