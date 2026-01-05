package com.mllfjn.simyys.character.list.sr.xienv;

import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.skill.Skill1PuGongBase;
import com.mllfjn.simyys.interactive.AttackType;
import com.mllfjn.simyys.interactive.Interactive;

class Skill1 extends Skill1PuGongBase {
    public static final String SkillName = "蝎刺";
    private static final int[] multiplier = new int[]{0, 50, 52, 54, 56, 60};

    public Skill1(Character belongTo, int level) {
        super(belongTo, level);
    }

    @Override
    public void usePrivate(Interactive interactive, Character target) {
        for (int i = 0; i < 2; i++) {
            interactive.attackTypical(this, target, multiplier[getLevel()], AttackType.DAN_TI);
        }
    }

    @Override
    public String getName() {
        return SkillName;
    }
}
