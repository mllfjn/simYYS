package com.mllfjn.simyys.battleevent;

import com.mllfjn.simyys.character.Character;

public class EventUsePuGong extends BattleEvent {
    private final Character attacker;
    private final Character target;

    public EventUsePuGong(Character attacker, Character target) {
        this.attacker = attacker;
        this.target = target;
    }

    public Character getAttacker() {
        return attacker;
    }

    public Character getTarget() {
        return target;
    }
}
