package com.mllfjn.simyys.character.list.sp.luwan;

import com.mllfjn.simyys.BattlePane;
import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.skill.CharacterFinder;
import com.mllfjn.simyys.character.skill.Skill;
import com.mllfjn.simyys.interactive.AttackType;
import com.mllfjn.simyys.interactive.Interactive;

import java.util.Optional;

class Skill2Special extends Skill {
    private static final String SkillName = "逆魂尽断";

    public Skill2Special(Character belongTo) {
        super(belongTo, -1, 0, 0, 2);
    }

    @Override
    public String getName() {
        return SkillName;
    }

    @Override
    public Optional<Character> usePrivate(BattlePane bp) {
        Character belongTo = getBelongTo();
        Interactive interactive = belongTo.getInteractive();
        for (int i = 0; i < 5; i++) {
            Character target = new CharacterFinder(belongTo)
                    .filterEnemy()
                    .getRandom();
            interactive.attackTypical(this, target, 240, AttackType.DAN_TI);
        }
        return Optional.empty();
    }
}
