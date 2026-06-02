package com.mllfjn.simyys.character.list.sphudie;

import com.mllfjn.simyys.BattlePane;
import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.skill.Skill;

import java.util.Optional;

class Skill2 extends Skill {
    private static final String SkillName = "灵梦";

    public Skill2(Character belongTo, int level) {
        super(belongTo, level, 1, 0, 2);
    }

    @Override
    public String getName() {
        return SkillName;
    }

    @Override
    public Optional<Character> usePrivate(BattlePane bp) {
        return Optional.empty();
    }
}
