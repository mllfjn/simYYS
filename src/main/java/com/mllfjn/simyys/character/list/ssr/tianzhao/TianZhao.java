package com.mllfjn.simyys.character.list.ssr.tianzhao;

import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.CharacterShiShenBase;

public class TianZhao extends CharacterShiShenBase {
    public static final String CharacterName = "天照";

    Character copyTarget;

    @Override
    public double getZengShang() {
        if (copyTarget != null) {
            return copyTarget.getZengShang();
        } else {
            return super.getZengShang();
        }
    }

    void setCopyTarget(Character copyTarget) {
        this.copyTarget = copyTarget;
    }

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
        return "3886";
    }

    @Override
    protected void addOwnSkills() {
        addSkill(new Skill1(this, skill1Level));
        Skill2 skill2 = new Skill2(this, skill2Level);
        addSkill(skill2);
        addSkill(new Skill3(this, skill3Level, skill2));
    }
}
