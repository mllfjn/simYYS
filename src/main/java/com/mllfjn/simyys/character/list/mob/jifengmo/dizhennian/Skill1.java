package com.mllfjn.simyys.character.list.mob.jifengmo.dizhennian;

import com.mllfjn.simyys.BattlePane;
import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.skill.CharacterFinder;
import com.mllfjn.simyys.character.skill.Skill;
import com.mllfjn.simyys.interactive.AttackType;

import java.util.List;
import java.util.Optional;

class Skill1 extends Skill {
    public static final String SkillName = "尾鳍攻击";

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
                .setTargetTeam(CharacterFinder.TargetTeam.ENEMY)
                .getList();

        belongTo.getInteractive().attackTypical(this, targets, 100, AttackType.QUN_TI);
        return Optional.empty();
    }
}
