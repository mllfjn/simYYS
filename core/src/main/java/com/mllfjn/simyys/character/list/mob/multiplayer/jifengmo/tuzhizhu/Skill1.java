package com.mllfjn.simyys.character.list.mob.multiplayer.jifengmo.tuzhizhu;

import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.skill.Skill1PuGongBase;
import com.mllfjn.simyys.interactive.AttackType;
import com.mllfjn.simyys.interactive.Interactive;

class Skill1 extends Skill1PuGongBase {
    private static final String SkillName = "毒丝";

    public Skill1(Character belongTo) {
        super(belongTo, -1);
    }

    @Override
    public String getSkillDesc() {
        return """
                √\t对目标造成攻击力100%的伤害
                √\t并使该目标中毒5回合,每回合造成土蜘蛛攻击10%的伤害,最多叠加3层
                """;
    }

    @Override
    public void usePrivate(Interactive interactive, Character target) {
        interactive.attackTypical(this, target, 100, AttackType.DAN_TI);
        StatusTZZPoisoning.addTZZPoisoning(getBelongTo(), target, 5);
    }

    @Override
    public String getName() {
        return SkillName;
    }
}
