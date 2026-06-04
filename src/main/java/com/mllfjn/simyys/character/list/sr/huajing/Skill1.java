package com.mllfjn.simyys.character.list.sr.huajing;

import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.skill.Skill1PuGongBase;
import com.mllfjn.simyys.interactive.AttackType;
import com.mllfjn.simyys.interactive.Interactive;

class Skill1 extends Skill1PuGongBase {
    private static final String SkillName = "水袭";
    private static final int[] multiplier = new int[]{0, 80, 85, 90, 95, 105};

    public Skill1(Character belongTo, int level) {
        super(belongTo, level);
    }

    @Override
    public void usePrivate(Interactive interactive, Character target) {
        interactive.attackTypical(this, target, multiplier[getLevel()], AttackType.DAN_TI);
    }

    @Override
    public String getName() {
        return SkillName;
    }
}
