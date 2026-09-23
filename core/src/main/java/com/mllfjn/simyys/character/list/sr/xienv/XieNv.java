package com.mllfjn.simyys.character.list.sr.xienv;

import com.mllfjn.simyys.character.CharacterShiShenBase;

public class XieNv extends CharacterShiShenBase {
    public static final String CharacterName = "蝎女";

    private StatusXieDu status;

    StatusXieDu getXieDu() {
        return status;
    }

    void setStatus(StatusXieDu status) {
        this.status = status;
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
        return true;
    }

    @Override
    protected String getDefaultBaseAttack() {
        return "2975";
    }

    @Override
    protected void addOwnSkills() {
        addSkill(new Skill1(this, skill1Level));
        Skill2 skill2 = new Skill2(this, skill2Level);
        addSkill(skill2);
        addSkill(new Skill3(this, skill3Level, skill2));
    }
}
