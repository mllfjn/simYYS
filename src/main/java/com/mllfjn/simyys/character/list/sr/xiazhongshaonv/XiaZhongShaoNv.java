package com.mllfjn.simyys.character.list.sr.xiazhongshaonv;

import com.mllfjn.simyys.character.CharacterShiShenBase;

public class XiaZhongShaoNv extends CharacterShiShenBase {
    public static final String CharacterName = "匣中少女";

    @Override
    protected String getDefaultSkillLevel() {
        return "515";
    }

    @Override
    protected boolean canAwakening() {
        return true;
    }

    @Override
    protected String getDefaultBaseAttack() {
        return "2439";
    }

    @Override
    protected void addOwnSkills() {
        addSkill(new Skill1(this, skill1Level));
        addSkill(new Skill2(this, awakening));
    }
}
