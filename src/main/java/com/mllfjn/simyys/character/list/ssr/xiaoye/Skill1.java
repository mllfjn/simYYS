package com.mllfjn.simyys.character.list.ssr.xiaoye;

import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.skill.Skill1PuGongBase;
import com.mllfjn.simyys.interactive.AttackType;
import com.mllfjn.simyys.interactive.Interactive;

class Skill1 extends Skill1PuGongBase {
    private static final String SkillName = "瞬影";

    private static final int[] Multiplier = new int[]{0, 50, 52, 54, 56, 62};

    public Skill1(Character belongTo, int level) {
        super(belongTo, level);
    }

    @Override
    public void usePrivate(Interactive interactive, Character target) {
        interactive.attackTypical(target, Multiplier[getLevel()], AttackType.DAN_TI);
    }

    @Override
    public String getName() {
        return SkillName;
    }
}
