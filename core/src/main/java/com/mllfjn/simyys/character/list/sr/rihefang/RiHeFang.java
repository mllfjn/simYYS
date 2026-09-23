package com.mllfjn.simyys.character.list.sr.rihefang;

import com.mllfjn.simyys.character.CharacterShiShenBase;

public class RiHeFang extends CharacterShiShenBase {
    public static final String CharacterName = "日和坊";

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
        return "2358";
    }

    @Override
    protected void addOwnSkills() {
        addSkill(new Skill1(this, skill1Level));
        Skill2 skill2 = new Skill2(this);
        Skill3 skill3 = new Skill3(this, skill3Level, skill2);
        skill2.setSkill3(skill3);
        addSkill(skill2);
        addSkill(skill3);
    }
}
