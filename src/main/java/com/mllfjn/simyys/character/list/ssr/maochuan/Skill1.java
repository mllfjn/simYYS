package com.mllfjn.simyys.character.list.ssr.maochuan;

import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.skill.Skill1PuGongBase;
import com.mllfjn.simyys.interactive.AttackType;
import com.mllfjn.simyys.interactive.Interactive;

class Skill1 extends Skill1PuGongBase {
    private static final String SkillName = "探温";
    private static final int[] multipliers = new int[]{0, 42, 47, 52, 57, 62};

    public Skill1(Character belongTo, int level) {
        super(belongTo, level);
    }

    @Override
    public void usePrivate(Interactive interactive, Character target) {
        int multiplier = multipliers[getLevel()];
        if (((MaoChuan) getBelongTo()).isBieGuanSiTangExist()) {
            multiplier *= 2;
        }
        for (int i = 0; i < 2; i++) {
            interactive.attackTypical(this, target, multiplier, AttackType.DAN_TI);
        }
    }

    @Override
    public String getName() {
        return SkillName;
    }
}
