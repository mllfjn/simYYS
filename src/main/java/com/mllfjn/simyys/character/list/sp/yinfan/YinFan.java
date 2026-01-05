package com.mllfjn.simyys.character.list.sp.yinfan;

import com.mllfjn.simyys.character.CharacterShiShenBase;

public class YinFan extends CharacterShiShenBase {
    public static final String CharacterName = "因幡辉夜姬";

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
        return "2949";
    }

    @Override
    protected void addOwnSkills() {
        addSkill(new Skill1(this, skill1Level));
        addSkill(new Skill2(this, skill2Level, skill3Level));
        addSkill(new Skill3(this, skill3Level));
    }

    @Override
    protected boolean useSkillAuto() {
        // 如果已经开了幻境，尝试开三，否则开幻境
        if (isHaveStatus(Skill2.StatusHuanJing.class)) {
            return tryUseSkill(3);
        } else {
            return tryUseSkill(2);
        }
    }
}
