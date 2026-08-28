package com.mllfjn.simyys.character.list.sp.spyue;

import com.mllfjn.simyys.BattlePane;
import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.skill.Skill;

import java.util.Optional;

class Skill3 extends Skill {
    private static final String SkillName = "归山之路";

    public Skill3(Character belongTo, int level) {
        super(belongTo, level, 2, 0, 3);
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
