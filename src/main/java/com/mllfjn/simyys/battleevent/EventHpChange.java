package com.mllfjn.simyys.battleevent;

import com.mllfjn.simyys.character.Character;

public class EventHpChange extends BattleEvent {
    private final Character character;
    public EventHpChange(Character character) {
        this.character = character;
    }

    public Character getCharacter() {
        return character;
    }
}
