package com.mllfjn.simyys.battleevent;

import com.mllfjn.simyys.interactive.InteractiveInfo;

public class EventAttack extends BattleEvent {
    private final InteractiveInfo interactiveInfo;

    public EventAttack(InteractiveInfo interactiveInfo) {
        this.interactiveInfo = interactiveInfo;
    }

    public InteractiveInfo getAttackInfo() {
        return interactiveInfo;
    }
}
