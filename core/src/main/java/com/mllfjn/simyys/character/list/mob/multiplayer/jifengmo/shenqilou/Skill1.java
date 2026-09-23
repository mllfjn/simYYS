package com.mllfjn.simyys.character.list.mob.multiplayer.jifengmo.shenqilou;

import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.skill.CharacterFinder;
import com.mllfjn.simyys.character.skill.Skill1PuGongBase;
import com.mllfjn.simyys.interactive.AttackType;
import com.mllfjn.simyys.interactive.Interactive;

class Skill1 extends Skill1PuGongBase {
    private static final String SkillName = "极-钳击";

    public Skill1(Character belongTo) {
        super(belongTo, 0);
    }

    @Override
    public String getName() {
        return SkillName;
    }

    @Override
    public Character getTarget() {
        return new CharacterFinder(getBelongTo())
                .filterEnemy()
                .getAutoOrElseRandom();
    }

    @Override
    public void usePrivate(Interactive interactive, Character target) {
        // 对单体敌人造成攻击100%的伤害（这里好像实际是120的数值）
        interactive.attackTypical(this, target, 120, AttackType.DAN_TI);
        // 并降低敌人30%的暴击
        target.addStatus(new StatusReduceCritRate(getBelongTo(), target, 30));
    }
}
