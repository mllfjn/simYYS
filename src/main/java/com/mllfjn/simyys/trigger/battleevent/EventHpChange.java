package com.mllfjn.simyys.trigger.battleevent;

import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.trigger.TriggerEvent;

public class EventHpChange extends TriggerEvent {
    private final Character character;
    public EventHpChange(Character character) {
        this.character = character;
    }

    public Character getCharacter() {
        return character;
    }
}
