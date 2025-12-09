package com.mllfjn.simyys.character.list.yys.yuanlaiguang;

import com.mllfjn.simyys.BattlePane;
import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.skill.CharacterFinder;
import com.mllfjn.simyys.character.skill.Skill1PuGongBase;
import com.mllfjn.simyys.interactive.AttackType;

import java.util.Optional;

class Skill1 extends Skill1PuGongBase {
    public static final String SkillName = "天剑";
    private static final int[] multiplier = new int[]{0, 100, 105, 110, 115, 125};

    private final int shuYin;

    public Skill1(Character belongTo, int level, int shuYin) {
        super(belongTo, level);
        this.shuYin = shuYin;
    }

    @Override
    public String getName() {
        return SkillName;
    }

    @Override
    public Optional<Character> usePrivate(BattlePane bp) {
        // 对敌方目标造成攻击(系数)伤害
        Character belongTo = getBelongTo();
        Character target = new CharacterFinder(belongTo)
                .setTargetTeam(CharacterFinder.TargetTeam.ENEMY)
                .getRandom();
        belongTo.getInteractive().attackTypical(this, target,
                multiplier[getLevel()] + shuYin * 20, AttackType.DAN_TI);
        return Optional.of(target);
    }
}
