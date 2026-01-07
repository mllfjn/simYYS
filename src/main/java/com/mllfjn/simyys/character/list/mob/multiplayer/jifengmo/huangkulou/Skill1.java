package com.mllfjn.simyys.character.list.mob.multiplayer.jifengmo.huangkulou;

import com.mllfjn.simyys.BattlePane;
import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.skill.CharacterFinder;
import com.mllfjn.simyys.character.skill.Skill;
import com.mllfjn.simyys.interactive.AttackType;

import java.util.List;
import java.util.Optional;

// 这个技能虽然从冷却和消耗上来看像是普攻,但是普攻似乎只有攻击单体目标这一种形式,所以不算作普攻
class Skill1 extends Skill {
    public static final String SkillName = "刀锋突刺";

    public Skill1(Character belongTo) {
        super(belongTo, 0, 0, 0, 1);
    }

    @Override
    public String getName() {
        return SkillName;
    }

    @Override
    public Optional<Character> usePrivate(BattlePane bp) {
        Character belongTo = getBelongTo();
        List<Character> targets = new CharacterFinder(belongTo)
                .filterEnemy()
                .getList();

        // 攻击敌方全体造成1段伤害
        belongTo.getInteractive().attackTypical(this, targets, 100, AttackType.QUN_TI);
        return Optional.empty();
    }
}
