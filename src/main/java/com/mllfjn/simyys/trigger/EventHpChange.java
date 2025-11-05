package com.mllfjn.simyys.trigger;

import com.mllfjn.simyys.character.Character;

public class EventHpChange extends TriggerEvent{
    private final Character character;
    public EventHpChange(Character character) {
        this.character = character;
    }

    public Character getCharacter() {
        return character;
    }
}
