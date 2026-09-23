package com.mllfjn.simyys.character.list.ssr.shijiamei;

import com.mllfjn.simyys.character.CharacterShiShenBase;

public class ShiJiaMei extends CharacterShiShenBase {
    public static final String CharacterName = "市加美";

    private boolean isUnlockDuanZui = false;

    @Override
    protected boolean useSkillAuto() {
        return tryUseSkill(3) || tryUseSkill(2);
    }

    void unlockDuanZui() {
        isUnlockDuanZui = true;
        getStatus(Skill2.StatusRenBuff.class)
                .ifPresent(Skill2.StatusRenBuff::unlock);
        getSkill(3).ifPresent(skill -> ((Skill3) skill).unlock());
    }

    boolean isUnlockDuanZui() {
        return isUnlockDuanZui;
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
        return "3216";
    }

    @Override
    protected void addOwnSkills() {
        addSkill(new Skill1(this, skill1Level));
        addSkill(new Skill2(this, skill2Level));
        addSkill(new Skill3(this, skill3Level));
    }
}
