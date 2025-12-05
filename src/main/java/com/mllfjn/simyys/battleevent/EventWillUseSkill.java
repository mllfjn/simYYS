package com.mllfjn.simyys.battleevent;

import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.skill.Skill;

public class EventWillUseSkill extends BattleEvent {
    private final Character character;
    private final Skill skill;

    public EventWillUseSkill(Character character, Skill skill) {
        this.character = character;
        this.skill = skill;
    }

    public Character getCharacter() {
        return character;
    }

    public Skill getSkill() {
        return skill;
    }
}
