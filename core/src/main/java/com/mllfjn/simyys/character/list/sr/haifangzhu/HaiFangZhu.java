package com.mllfjn.simyys.character.list.sr.haifangzhu;

import com.mllfjn.simyys.character.CharacterShiShenBase;

public class HaiFangZhu extends CharacterShiShenBase {
    public static final String CharacterName = "海坊主";

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
        return "3055";
    }

    @Override
    protected boolean useSkillAuto() {
        return tryUseSkill(3);
    }

    @Override
    protected void addOwnSkills() {
        addSkill(new Skill1(this, skill1Level));
        addSkill(new Skill2(this));
        addSkill(new Skill3(this, skill3Level));
    }
}
