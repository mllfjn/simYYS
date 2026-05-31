package com.mllfjn.simyys.character.skill;

import com.mllfjn.simyys.BattlePane;
import com.mllfjn.simyys.character.Character;

import java.util.Optional;

public abstract class PassiveSkill extends Skill {

    public PassiveSkill(Character belongTo, int level, int skillID) {
        super(belongTo, level, 0, 0, skillID);
    }

    @Override
    public final Optional<Character> usePrivate(BattlePane bp) {
        return Optional.empty();
    }

    public abstract void enable();

    public abstract void disable();
}