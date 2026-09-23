package com.mllfjn.simyys.character.list.ssr.datiangou;

import com.mllfjn.simyys.character.CharacterShiShenBase;

public class DaTianGou extends CharacterShiShenBase {
    public static final String CharacterName = "大天狗";

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
        Skill3 skill3 = new Skill3(this, skill3Level, skill2);

        skill2.setSkill3(skill3);
        addSkill(skill2);
        addSkill(skill3);
    }
}
