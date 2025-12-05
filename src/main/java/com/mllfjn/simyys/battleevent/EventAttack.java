package com.mllfjn.simyys.battleevent;

import com.mllfjn.simyys.interactive.AttackInfo;

public class EventAttack extends BattleEvent {
    private final AttackInfo attackInfo;

    public EventAttack(AttackInfo attackInfo) {
        this.attackInfo = attackInfo;
    }

    public AttackInfo getAttackInfo() {
        return attackInfo;
    }
}
