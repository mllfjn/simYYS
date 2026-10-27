package com.mllfjn.simyys.character.skill;

import com.mllfjn.simyys.BattlePane;
import com.mllfjn.simyys.character.Character;

import java.util.Optional;

public abstract class PassiveSkill extends Skill {
    private boolean enabled = false;

    public PassiveSkill(Character belongTo, String name, int level, int skillID) {
        super(belongTo, level, name, 0, 0, skillID);
    }

    @Deprecated
    public PassiveSkill(Character belongTo, int level, int skillID) {
        super(belongTo, level, 0, 0, skillID);
    }

    @Override
    public final Optional<Character> usePrivate(BattlePane bp) {
        return Optional.empty();
    }

    public void enable() {
        enabled = true;
    }

    public void disable() {
        enabled = false;
    }

    public boolean isActive() {
        return enabled;
    }
}