package com.mllfjn.simyys.character.list.sp.laotou;

import com.mllfjn.simyys.character.CharacterShiShenBase;

public class LaoTou extends CharacterShiShenBase {
    public static final String CharacterName = "晨晖惠比寿";

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
        return "2278";
    }

    @Override
    public void addOwnSkills() {
        addSkill(new Skill1(this, skill1Level));
        addSkill(new Skill2(this, skill2Level));
        addSkill(new Skill3(this, skill3Level));
    }

    @Override
    protected void dieHandle() {
        getStatus(StatusYuHunBeingTransfer.class).ifPresent(status ->
                status.statusYuHunTransfer.delete());
    }
}
