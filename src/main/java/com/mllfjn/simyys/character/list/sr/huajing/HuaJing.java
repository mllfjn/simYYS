package com.mllfjn.simyys.character.list.sr.huajing;

import com.mllfjn.simyys.character.CharacterShiShenBase;

public class HuaJing extends CharacterShiShenBase {
    public static final String CharacterName = "化鲸";

    StatusChiJia statusChiJia;
    StatusTiJia statusTiJia;

    boolean tiJiaIncrease() {
        return statusChiJia == null || statusChiJia.belongTo != statusTiJia.belongTo;
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
        return "2841";
    }

    @Override
    protected void addOwnSkills() {
        addSkill(new Skill1(this, skill1Level));
        Skill3 skill3 = new Skill3(this, skill3Level);
        addSkill(new Skill2(this, skill2Level, skill3));
        addSkill(skill3);
    }
}
