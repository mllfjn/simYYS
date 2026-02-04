package com.mllfjn.simyys.character.list.ssr.xuzuo;

import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.skill.PassiveSkill;

class Skill2 extends PassiveSkill {
    private static final String SkillName = "神之领域";

    public Skill2(Character belongTo, int level) {
        super(belongTo, level, 2);
    }

    @Override
    public void enable() {

    }

    @Override
    protected void disable() {

    }

    @Override
    public String getName() {
        return SkillName;
    }
}
