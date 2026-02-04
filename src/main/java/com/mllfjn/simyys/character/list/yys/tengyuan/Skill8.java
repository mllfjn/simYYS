package com.mllfjn.simyys.character.list.yys.tengyuan;

import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.skill.PassiveSkill;

class Skill8 extends PassiveSkill {
    public static final String SkillName = "余音入梦";

    public Skill8(Character belongTo, int level) {
        super(belongTo, level, 8);
    }

    @Override
    public void enable() {

    }

    @Override
    protected void disable() {

    }

    @Override
    public String getName() {
        return "";
    }
}
