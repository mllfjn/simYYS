package com.mllfjn.simyys.character.status.triggerParam;

import com.mllfjn.simyys.character.Character;

public class ParamKilledCharacter extends TriggerParam {
    private final Character character;
    private final double excessDamage;

    public ParamKilledCharacter(Character character, double excessDamage) {
        this.character = character;
        this.excessDamage = excessDamage;
    }

    public Character getCharacter() {
        return character;
    }

    public double getExcessDamage() {
        return excessDamage;
    }
}
