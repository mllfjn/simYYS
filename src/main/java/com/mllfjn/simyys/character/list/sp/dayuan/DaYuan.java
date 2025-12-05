package com.mllfjn.simyys.character.list.sp.dayuan;

import com.mllfjn.simyys.character.CharacterShiShenBase;

public class DaYuan extends CharacterShiShenBase {
    public static final String CharacterName = "纺愿缘结神";
    public DaYuan() {

    }

    @Override
    protected boolean useSkillAuto() {
        return isHaveStatus(StatusCombined.class) ? tryUseSkill(3) : tryUseSkill(5);
    }

    @Override
    protected String getDefaultBaseAttack() {
        return "2224";
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
    public void addOwnSkills() {
        addSkill(new Skill1(this, skill1Level));
        Skill2.addStatus(this);
        addSkill(new Skill3(this, skill3Level));
        addSkill(new Skill5(this));
        addSkill(new Skill6(this));
    }
}

