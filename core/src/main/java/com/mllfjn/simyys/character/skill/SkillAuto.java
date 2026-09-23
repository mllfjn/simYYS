package com.mllfjn.simyys.character.skill;

import com.mllfjn.simyys.BattlePane;
import com.mllfjn.simyys.character.Character;

import java.util.Optional;

public final class SkillAuto extends Skill {
    public static final SkillAuto INSTANCE = new SkillAuto();
    private SkillAuto() {
        super(null, 0, 0, 0, 0);
    }

    @Override
    public String getName() {
        return "妖术";
    }

    @Override
    public Optional<Character> usePrivate(BattlePane bp) {
        return Optional.empty();
    }
}
