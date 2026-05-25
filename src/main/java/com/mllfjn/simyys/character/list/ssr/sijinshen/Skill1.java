package com.mllfjn.simyys.character.list.ssr.sijinshen;

import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.skill.Skill1PuGongBase;
import com.mllfjn.simyys.interactive.AttackType;
import com.mllfjn.simyys.interactive.Interactive;

class Skill1 extends Skill1PuGongBase {
    private static final String SkillName = "焰途";
    private static final int[] multiplier = new int[]{0, 100, 105, 110, 115, 120};

    public Skill1(Character belongTo, int level) {
        super(belongTo, level);
    }

    @Override
    public void usePrivate(Interactive interactive, Character target) {
        interactive.attackTypical(this, target, multiplier[getLevel()], AttackType.DAN_TI);
        StatusXinYang.addStack();
    }

    @Override
    public String getName() {
        return SkillName;
    }
}
