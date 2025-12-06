package com.mllfjn.simyys.character.list.mob.jifengmo.shenqilou;

import com.mllfjn.simyys.BattlePane;
import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.skill.CharacterFinder;
import com.mllfjn.simyys.character.skill.Skill;
import com.mllfjn.simyys.interactive.AttackType;

import java.util.Optional;

class Skill1 extends Skill {
    private static final String SkillName = "极-钳击";

    public Skill1(Character belongTo) {
        super(belongTo, 0, 0, 0, 1);
    }

    @Override
    public String getName() {
        return SkillName;
    }

    @Override
    public Optional<Character> usePrivate(BattlePane bp) {
        ShenQiLou belongTo = (ShenQiLou) getBelongTo();
        // 对单体敌人造成攻击100%的伤害
        Character target = new CharacterFinder(belongTo)
                .setTargetTeam(CharacterFinder.TargetTeam.ENEMY)
                .getRandom();
        belongTo.getInteractive().attack(this, target, 120, AttackType.DAN_TI);
        // 并降低敌人30%的暴击
        target.addStatus(new StatusReduceCritRate(belongTo, target, 30));
        return Optional.of(target);
    }
}
