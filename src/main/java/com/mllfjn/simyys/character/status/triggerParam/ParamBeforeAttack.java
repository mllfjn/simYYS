package com.mllfjn.simyys.character.status.triggerParam;

import com.mllfjn.simyys.interactive.AttackInfo;

public class ParamBeforeAttack extends TriggerParam {
    private final AttackInfo attackInfo;

    public ParamBeforeAttack(AttackInfo attackInfo) {
        this.attackInfo = attackInfo;
    }

    public AttackInfo getAttackInfo() {
        return attackInfo;
    }
}
