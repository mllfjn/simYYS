package com.mllfjn.simyys.character.list.sp.spyue;

import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.skill.Skill1PuGongBase;
import com.mllfjn.simyys.interactive.AttackInfo;
import com.mllfjn.simyys.interactive.AttackType;
import com.mllfjn.simyys.interactive.Interactive;

class Skill1 extends Skill1PuGongBase {
    private static final String SkillName = "巍峨";

    private static final int[] Multiplier = new int[]{0, 300, 340, 380, 420, 480};

    public Skill1(Character belongTo, int level) {
        super(belongTo, level);
    }

    @Override
    public void usePrivate(Interactive interactive, Character target) {
        interactive.attack(new AttackInfo(getBelongTo(), this, target, AttackType.DAN_TI,
                getBelongTo().getDefence() * Multiplier[getLevel()] / 100)
        );
    }

    @Override
    public String getName() {
        return SkillName;
    }
}
