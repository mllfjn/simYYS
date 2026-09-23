package com.mllfjn.simyys.character.list.sr.luoxinfu;

import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.skill.Skill1PuGongBase;
import com.mllfjn.simyys.interactive.Interactive;

class Skill1 extends Skill1PuGongBase {
    private static final String SkillName = "毒针";

    private final Skill2 skill2;

    public Skill1(Character belongTo, int level, Skill2 skill2) {
        super(belongTo, level);
        this.skill2 = skill2;
    }

    @Override
    public void usePrivate(Interactive interactive, Character target) {
        super.usePrivate(interactive, target);
        skill2.madeAttack(target);
    }

    @Override
    public String getName() {
        return SkillName;
    }
}
