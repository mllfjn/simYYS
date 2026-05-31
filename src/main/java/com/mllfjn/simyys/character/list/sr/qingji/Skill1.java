package com.mllfjn.simyys.character.list.sr.qingji;

import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.skill.Skill1PuGongBase;
import com.mllfjn.simyys.interactive.AttackType;
import com.mllfjn.simyys.interactive.Interactive;

class Skill1 extends Skill1PuGongBase {
    private static final String SkillName = "蛇行击";
    private static final int[] multiplier = new int[]{0, 86, 90, 94, 98, 102, 102};

    public Skill1(Character belongTo, int level) {
        super(belongTo, level);
    }

    @Override
    public void usePrivate(Interactive interactive, Character target) {
        interactive.attackTypical(this, target, multiplier[getLevel()], AttackType.DAN_TI);
        interactive.
    }

    @Override
    public String getName() {
        return SkillName;
    }
}
