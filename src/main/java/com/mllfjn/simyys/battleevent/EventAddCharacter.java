package com.mllfjn.simyys.battleevent;

import com.mllfjn.simyys.character.Character;

public class EventAddCharacter extends BattleEvent {
    private final Character character;

    public EventAddCharacter(Character character) {
        this.character = character;
    }

    public Character getCharacter() {
        return character;
    }
}
