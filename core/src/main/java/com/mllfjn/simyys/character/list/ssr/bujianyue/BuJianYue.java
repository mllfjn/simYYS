package com.mllfjn.simyys.character.list.ssr.bujianyue;

import com.mllfjn.simyys.character.CharacterShiShenBase;

public class BuJianYue extends CharacterShiShenBase {
    public static final String CharacterName = "不见岳";

    private Skill3 skill3;

    @Override
    protected boolean useSkillAuto() {
        if (skill3.status == null) {
            return tryUseSkill(3);
        } else {
            return false;
        }
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
        return "2492";
    }

    @Override
    protected void addOwnSkills() {
        addSkill(new Skill1(this, skill1Level));
        Skill2 skill2 = new Skill2(this, skill2Level);
        addSkill(skill2);
        skill3 = new Skill3(this, skill3Level, skill2);
        addSkill(skill3);
    }
}
