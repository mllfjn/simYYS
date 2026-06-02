package com.mllfjn.simyys.character.list.ssr.bujianyue;

import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.skill.PassiveSkill;

public class Skill2 extends PassiveSkill {
    private static final String SkillName = "古山之神";

    public Skill2(Character belongTo, int level) {
        super(belongTo, level, 2);
    }

    @Override
    public void enable() {

    }

    @Override
    public void disable() {

    }

    @Override
    public String getName() {
        return SkillName;
    }
}
