package com.mllfjn.simyys.battleevent;

import com.mllfjn.simyys.character.Character;

public class EventCharacterDie extends BattleEvent {
    private final Character character;
    public EventCharacterDie(Character character) {
        this.character = character;
    }

    public Character getCharacter() {
        return character;
    }
}
