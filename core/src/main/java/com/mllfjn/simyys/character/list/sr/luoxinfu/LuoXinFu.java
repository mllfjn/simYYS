package com.mllfjn.simyys.character.list.sr.luoxinfu;

import com.mllfjn.simyys.character.CharacterShiShenBase;

public class LuoXinFu extends CharacterShiShenBase {
    public static final String CharacterName = "络新妇";

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
        return "3216";
    }

    @Override
    protected void addOwnSkills() {
        Skill2 skill2 = new Skill2(this);
        addSkill(new Skill1(this, skill1Level, skill2));
        addSkill(skill2);
        addSkill(new Skill3(this, skill3Level, skill2));
    }
}
