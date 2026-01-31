package com.mllfjn.simyys.character.list.sr.xienv;

import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.CharacterShiShenBase;

public class XieNv extends CharacterShiShenBase {
    public static final String CharacterName = "蝎女";

    private Skill3.StatusXieDu status;

    Skill3.StatusXieDu getXieDu() {
        return status;
    }

    Character getXieDuCarrier() {
        return status.belongTo;
    }

    void setStatus(Skill3.StatusXieDu status) {
        this.status = status;
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
        addSkill(new Skill2(this, skill2Level));
        addSkill(new Skill3(this, skill3Level));
    }
}
