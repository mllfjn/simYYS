package com.mllfjn.simyys.character.skill;

import com.mllfjn.simyys.character.Character;

public abstract class PassiveSkillCanNotSeal extends PassiveSkill {
    public PassiveSkillCanNotSeal(Character belongTo, int level, int skillID) {
        super(belongTo, level, skillID);
    }

    @Override
    public void disable() {

    }

    @Override
    public void enable() {

    }
}
