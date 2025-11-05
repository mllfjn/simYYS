package com.mllfjn.simyys.trigger;

import com.mllfjn.simyys.character.Character;

public class EventRoundDone extends TriggerEvent {
    private final Character character;
    public EventRoundDone(Character character) {
        this.character = character;
    }

    public Character getCharacter() {
        return character;
    }
}
