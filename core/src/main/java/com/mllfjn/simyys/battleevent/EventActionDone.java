package com.mllfjn.simyys.battleevent;

import com.mllfjn.simyys.character.Character;

public class EventActionDone extends BattleEvent {
    private final Character character;
    public EventActionDone(Character character) {
        this.character = character;
    }

    public Character getCharacter() {
        return character;
    }
}
