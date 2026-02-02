package com.mllfjn.simyys.character.list.ssr.shenwuyue;

import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.CharacterShiShenBase;

public class ShenWuYue extends CharacterShiShenBase {
    public static final String CharacterName = "神无月";

    private Skill2.StatusMengShen statusMengShen;
    private StatusRuMeng ruMeng;

    Character getRuMengCarrier() {
        if (ruMeng == null) {
            return null;
        } else {
            return ruMeng.belongTo;
        }
    }

    void setRuMeng(StatusRuMeng newRuMeng, boolean needDelete) {
        if (needDelete && this.ruMeng != null) {
            this.ruMeng.deleteAndRemoveMaintained();
        }
        this.ruMeng = newRuMeng;
    }

    Skill2.StatusMengShen addStack() {
        statusMengShen.addStack(1);
        return statusMengShen;
    }

    void setStatusMengShen(Skill2.StatusMengShen statusMengShen) {
        this.statusMengShen = statusMengShen;
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
        return "2010";
    }

    @Override
    protected void addOwnSkills() {
        addSkill(new Skill1(this, skill1Level));
        addSkill(new Skill2(this, skill2Level));
        addSkill(new Skill3(this, skill3Level));
    }

    @Override
    protected boolean useSkillAuto() {
        return tryUseSkill(3);
    }
}
