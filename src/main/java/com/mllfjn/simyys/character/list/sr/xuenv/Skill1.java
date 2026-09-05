package com.mllfjn.simyys.character.list.sr.xuenv;

import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.skill.Skill1PuGongBase;
import com.mllfjn.simyys.interactive.AttackType;
import com.mllfjn.simyys.interactive.Interactive;

class Skill1 extends Skill1PuGongBase {
    private static final String SkillName = "雪球";
    private static final int[] multiplier = new int[]{0, 100, 110, 110, 120, 120};

    public Skill1(Character belongTo, int level) {
        super(belongTo, level);
    }

    @Override
    public void usePrivate(Interactive interactive, Character target) {
        interactive.attackTypical(this, target, multiplier[getLevel()], AttackType.DAN_TI);
        if (getLevel() >= 3) {
            interactive.effect(this, target, getLevel() >= 5 ? 50 : 25, true,
                    StatusReduceSpeed.getSupplier()
            );
        }
    }

    @Override
    public String getName() {
        return SkillName;
    }

}
