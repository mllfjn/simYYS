package com.mllfjn.simyys.character.ssr.namei;

import com.mllfjn.simyys.character.CharacterShiShenBase;

public class NaMei extends CharacterShiShenBase {
    public static final String CharacterName = "伊邪那美";

    public NaMei() {

    }

    @Override
    protected String getDefaultBaseAttack() {
        return "3618";
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
    public void addOwnSkills() {
        addSkill(new Skill1(this, skill1Level));
        addSkill(new Skill2(this, awakening, skill2Level));
        addSkill(new Skill3(this, skill3Level));
    }

    @Override
    protected boolean useSkillAuto() {
        // 如果毁灭已经给出去了,放三,否则二
        return isHaveStatus(StatusNaMeiFlag.class) ? tryUseSkill(3) : tryUseSkill(2);
    }
}