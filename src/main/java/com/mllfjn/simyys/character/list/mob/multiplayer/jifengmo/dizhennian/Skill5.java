package com.mllfjn.simyys.character.list.mob.multiplayer.jifengmo.dizhennian;

import com.mllfjn.simyys.BattlePane;
import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.skill.CharacterFinder;
import com.mllfjn.simyys.character.skill.Skill;
import com.mllfjn.simyys.interactive.AttackType;
import com.mllfjn.simyys.interactive.Interactive;

import java.util.List;
import java.util.Optional;

class Skill5 extends Skill {
    private static final String SkillName = "波浪翻涌";

    public Skill5(Character belongTo) {
        super(belongTo, 0, 0, 2, 5);
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
                .filterEnemy()
                .getList();

        interactive.attackTypical(this, targets, 80, AttackType.QUN_TI);
        interactive.attackTypical(this, targets, 80, AttackType.QUN_TI);


        return Optional.empty();
    }
}
