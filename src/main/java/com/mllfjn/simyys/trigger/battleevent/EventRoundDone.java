package com.mllfjn.simyys.trigger.battleevent;

import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.trigger.TriggerEvent;

public class EventRoundDone extends TriggerEvent {
    private final Character character;
    public EventRoundDone(Character character) {
        this.character = character;
    }

    public Character getCharacter() {
        return character;
    }
}
