package com.mllfjn.simyys.trigger.battleevent;

import com.mllfjn.simyys.character.Character;

public class EventCharacterDie extends TriggerEvent {
    private final Character character;
    public EventCharacterDie(Character character) {
        this.character = character;
    }

    public Character getCharacter() {
        return character;
    }
}
