package com.mllfjn.simyys.character.list.ssr.xunxiangxing;

import com.mllfjn.simyys.character.CharacterShiShenBase;

public class XunXiangXing extends CharacterShiShenBase {
    public static final String CharacterName = "寻香行";

    private Skill2 skill2;

    double getPercent() {
        return skill2Level >= 3 ? 0.1 : 0.05;
    }

    Skill2 getSkill2() {
        return skill2;
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
        return "3511";
    }

    @Override
    protected void addOwnSkills() {
        addSkill(new Skill1(this, skill1Level));
        skill2 = new Skill2(this, skill2Level);
        addSkill(skill2);
        addSkill(new Skill3(this, skill3Level));
    }
}
