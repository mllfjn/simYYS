package com.mllfjn.simyys.character.list.mob.jifengmo.dizhennian;

import com.mllfjn.simyys.BattlePane;
import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.skill.CharacterFinder;
import com.mllfjn.simyys.character.skill.Skill;
import com.mllfjn.simyys.interactive.AttackType;
import com.mllfjn.simyys.interactive.Interactive;

import java.util.List;
import java.util.Optional;

class Skill2 extends Skill {
    public static final String SkillName = "波浪翻涌";

    public Skill2(Character belongTo) {
        super(belongTo, 0, 0, 2, 2);
    }

    @Override
    public String getName() {
        return SkillName;
    }

    @Override
    public Optional<Character> usePrivate(BattlePane bp) {
        Character belongTo = getBelongTo();
        Interactive interactive = belongTo.getInteractive();

        List<Character> targets = new CharacterFinder(belongTo)
                .setTargetTeam(CharacterFinder.TargetTeam.ENEMY)
                .getList();

        interactive.attackTypical(this, targets, 80, AttackType.QUN_TI);
        interactive.attackTypical(this, targets, 80, AttackType.QUN_TI);


        return Optional.empty();
    }
}
