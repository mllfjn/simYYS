package com.mllfjn.simyys.character.list.sp.laotou;

import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.skill.Skill1PuGongBase;
import com.mllfjn.simyys.interactive.AttackType;
import com.mllfjn.simyys.interactive.Interactive;

class Skill1 extends Skill1PuGongBase {
    private static final String SkillName = "礼教";

    public Skill1(Character belongTo, int level) {
        super(belongTo, level);
    }

    @Override
    public void usePrivate(Interactive interactive, Character target) {
        // 造成攻击（系数）伤害
        interactive.attackTypical(this, target, multiplierGeneral[getLevel()], AttackType.DAN_TI);
    }

    @Override
    public String getName() {
        return SkillName;
    }
}
