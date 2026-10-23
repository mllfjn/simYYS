package com.mllfjn.simyys.character.list.sp.guitun;

import com.mllfjn.simyys.character.CharacterShiShenBase;

public class GuiTun extends CharacterShiShenBase {
    public static final String CharacterName = "鬼王酒吞童子";

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
        return false;
    }

    @Override
    protected String getDefaultBaseAttack() {
        return "3189";
    }

    @Override
    protected void addOwnSkills() {
        Skill1 skill1 = new Skill1(this, skill1Level);
        addSkill(skill1);
        addSkill(new Skill2(this, skill2Level));
        addSkill(new Skill3(this, skill3Level, skill1));
    }
}
