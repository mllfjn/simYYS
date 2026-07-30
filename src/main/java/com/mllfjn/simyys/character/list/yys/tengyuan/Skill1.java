package com.mllfjn.simyys.character.list.yys.tengyuan;

import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.skill.Skill1PuGongBase;
import com.mllfjn.simyys.interactive.AttackType;
import com.mllfjn.simyys.interactive.Interactive;

class Skill1 extends Skill1PuGongBase {
    static final String SkillName = "训诫之音";

    private final int multiplier;

    public Skill1(Character belongTo, int level, int shuYin) {
        super(belongTo, level);
        multiplier = multiplierGeneral[level] + shuYin * 20;
    }

    @Override
    public void usePrivate(Interactive interactive, Character target) {
        interactive.attackTypical(this, target, multiplier, AttackType.DAN_TI);
    }

    @Override
    public String getName() {
        return SkillName;
    }
}
