package com.mllfjn.simyys.character.list.ssr.xueyuqian;

import com.mllfjn.simyys.character.CharacterShiShenBase;
import com.mllfjn.simyys.interactive.AttackInfo;

public class XueYuQian extends CharacterShiShenBase {
    public static final String CharacterName = "雪御前";

    boolean isLDLLExist = false;

    @Override
    protected boolean useSkillAuto() {
        if (isLDLLExist) {
            return tryUseSkill(3);
        } else {
            return tryUseSkill(2);
        }
    }

    @Override
    public void beHurt(AttackInfo attackInfo) {
        if (isLDLLExist) {
            attackInfo.setCancel(true);
        } else {
            super.beHurt(attackInfo);
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
        return "3913";
    }

    @Override
    protected void addOwnSkills() {
        Skill2 skill2 = new Skill2(this, skill2Level);
        addSkill(new Skill1(this, skill1Level, skill2.getStatusWLXR()));
        addSkill(skill2);
        addSkill(new Skill3(this, skill3Level, skill2.getStatusWLXR()));
    }
}
