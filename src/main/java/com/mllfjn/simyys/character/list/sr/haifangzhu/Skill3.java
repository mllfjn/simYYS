package com.mllfjn.simyys.character.list.sr.haifangzhu;

import com.mllfjn.simyys.BattlePane;
import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.skill.CharacterFinder;
import com.mllfjn.simyys.character.skill.Skill;
import com.mllfjn.simyys.interactive.AttackType;
import com.mllfjn.simyys.interactive.Interactive;

import java.util.List;
import java.util.Optional;

class Skill3 extends Skill {
    public static final String SkillName = "巨浪";

    private final int multiplier;

    public Skill3(Character belongTo, int level) {
        super(belongTo, level, 3, 0, 3);
        multiplier = 30 + level * 3;
    }

    @Override
    public String getName() {
        return SkillName;
    }

    @Override
    public Optional<Character> usePrivate(BattlePane bp) {
        Character belongTo = getBelongTo();
        Interactive interactive = belongTo.getInteractive();
        List<Character> list = new CharacterFinder(belongTo)
                .filterEnemy()
                .getList();
        for (int i = 0; i < 3; i++) {
            interactive.attackTypical(this, list, multiplier, AttackType.QUN_TI);
        }
        return Optional.empty();
    }
}
