package com.mllfjn.simyys.character.list.sp.sphongye;

import com.mllfjn.simyys.character.CharacterShiShenBase;

public class SPHongYe extends CharacterShiShenBase {
    public static final String CharacterName = "心狩鬼女红叶";

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
        return "3136";
    }

    @Override
    protected void addOwnSkills() {
        addSkill(new Skill1(this, skill1Level));
        Skill2 skill2 = new Skill2(this, skill2Level);
        addSkill(skill2);
        addSkill(new Skill3(this, skill3Level, skill2));
    }
}
