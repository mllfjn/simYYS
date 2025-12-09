package com.mllfjn.simyys.character.status.triggerParam;

import com.mllfjn.simyys.character.status.Status;
import com.mllfjn.simyys.interactive.EffectInfo;

public class ParamAddCrowdControl extends TriggerParam {
    private final EffectInfo effectInfo;

    public ParamAddCrowdControl(EffectInfo effectInfo) {
        this.effectInfo = effectInfo;
    }

    public EffectInfo getEffectInfo() {
        return effectInfo;
    }
}
