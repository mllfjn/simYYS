package com.mllfjn.simyys.character.status.triggerParam;

import com.mllfjn.simyys.interactive.AttackInfo;

public class ParamAttackInfo extends TriggerParam {
    private final AttackInfo attackInfo;

    public ParamAttackInfo(AttackInfo attackInfo) {
        this.attackInfo = attackInfo;
    }

    public AttackInfo getAttackInfo() {
        return attackInfo;
    }
}
