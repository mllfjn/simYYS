package com.mllfjn.simyys.character.list.ssr.beimihu;

import com.mllfjn.simyys.character.CharacterShiShenBase;

import java.util.Optional;

public class BeiMiHu extends CharacterShiShenBase {
    public static final String CharacterName = "卑弥呼";

    private StatusShiZhiHui shiZhiHuiCarrier;

    public void setShiZhiHuiCarrier(StatusShiZhiHui status) {
        if (shiZhiHuiCarrier != null) {
            shiZhiHuiCarrier.belongTo.removeStatus(shiZhiHuiCarrier);
        }

        this.shiZhiHuiCarrier = status;
    }

    public Optional<StatusShiZhiHui> getShiZhiHuiCarrier() {
        return Optional.ofNullable(shiZhiHuiCarrier);
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
        return "3243";
    }

    @Override
    protected boolean useSkillAuto() {
        return tryUseSkill(3);
    }

    @Override
    protected void addOwnSkills() {
        addSkill(new Skill1(this, skill1Level));
        addSkill(new Skill2(this, skill2Level));
        addSkill(new Skill3(this, skill3Level));
    }
}
