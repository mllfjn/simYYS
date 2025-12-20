package com.mllfjn.simyys.character.status.triggerParam;

import com.mllfjn.simyys.interactive.AttackInfo;

public class ParamCauseAttack extends TriggerParam {
    public final AttackInfo attackInfo;

    public ParamCauseAttack(AttackInfo attackInfo) {
        this.attackInfo = attackInfo;
    }
}
