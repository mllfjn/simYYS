package com.mllfjn.simyys.battleevent;

import com.mllfjn.simyys.interactive.AttackInfo;
import com.mllfjn.simyys.interactive.EffectInfo;

public class EventUsedSkill extends BattleEvent {
    private final Character character;
    private final AttackInfo attackInfo;
    private final EffectInfo effectInfo;

    public EventUsedSkill(Character character, AttackInfo attackInfo, EffectInfo effectInfo) {
        this.character = character;
        this.attackInfo = attackInfo;
        this.effectInfo = effectInfo;
    }

    public Character getCharacter() {
        return character;
    }

    public AttackInfo getAttackInfo() {
        return attackInfo;
    }

    public EffectInfo getEffectInfo() {
        return effectInfo;
    }
}
