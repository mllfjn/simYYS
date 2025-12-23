package com.mllfjn.simyys.character.list.sp.kongmian;

import com.mllfjn.simyys.BattlePane;
import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.skill.Skill;

import java.util.Optional;

class Skill3Special extends Skill {
    public static final String SkillName = "轮回一息";

    public Skill3Special(Character belongTo) {
        super(belongTo, 0, 3, 0, 3);
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
