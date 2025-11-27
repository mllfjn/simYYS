package com.mllfjn.simyys.character.sp.laotou;

import com.mllfjn.simyys.character.CharacterShiShenBase;

public class LaoTou extends CharacterShiShenBase {
    public static final String CharacterName = "晨晖惠比寿";

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
        skills.add(new Skill1(this, skill1Level));
        new Skill2(this, skill2Level);
        skills.add(new Skill3(this, skill3Level));
    }

    @Override
    protected void dieHandle() {
        getState(StateYuHunBeingTransfer.class).ifPresent(stateYuHunBeingTransfer -> {
            stateYuHunBeingTransfer.stateYuHunTransfer.delete();
        });
    }
}
