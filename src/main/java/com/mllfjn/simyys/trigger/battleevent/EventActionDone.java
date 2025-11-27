package com.mllfjn.simyys.trigger.battleevent;

import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.trigger.TriggerEvent;

public class EventActionDone extends TriggerEvent {
    private final Character character;
    public EventActionDone(Character character) {
        this.character = character;
    }

    public Character getCharacter() {
        return character;
    }
}
