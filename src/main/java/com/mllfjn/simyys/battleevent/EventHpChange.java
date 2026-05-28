package com.mllfjn.simyys.battleevent;

import com.mllfjn.simyys.character.Character;

@Deprecated
public class EventHpChange extends BattleEvent {
    private final Character character;
    public EventHpChange(Character character) {
        this.character = character;
    }

    public Character getCharacter() {
        return character;
    }
}
