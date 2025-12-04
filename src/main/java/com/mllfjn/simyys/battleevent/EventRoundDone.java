package com.mllfjn.simyys.battleevent;

import com.mllfjn.simyys.character.Character;

public class EventRoundDone extends BattleEvent {
    private final Character character;
    public EventRoundDone(Character character) {
        this.character = character;
    }

    public Character getCharacter() {
        return character;
    }
}
