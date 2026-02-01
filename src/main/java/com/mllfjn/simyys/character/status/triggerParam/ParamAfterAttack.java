package com.mllfjn.simyys.character.status.triggerParam;

import com.mllfjn.simyys.interactive.AttackInfo;

public class ParamAfterAttack extends TriggerParam {
    public final AttackInfo attackInfo;

    public ParamAfterAttack(AttackInfo attackInfo) {
        this.attackInfo = attackInfo;
    }
}
