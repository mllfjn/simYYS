package com.mllfjn.simyys.character.status.instance;

import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.status.*;
import com.mllfjn.simyys.character.status.determinant.IgnoreActionDecrease;
import com.mllfjn.simyys.character.status.determinant.IgnoreDebuff;
import com.mllfjn.simyys.character.status.triggerParam.ParamAddCrowdControl;
import com.mllfjn.simyys.character.status.triggerParam.ParamLocationChange;

public class StatusBoss extends Status implements IgnoreDebuff, IgnoreActionDecrease {
    public StatusBoss(Character character) {
        super("BOSS", character);
        runOn(Trigger.ADDING_CROWD_CONTROL, triggerParam -> {
            if (triggerParam instanceof ParamAddCrowdControl pac) {
                pac.getEffectInfo().setCancel(true);
            }
        });
        runOn(Trigger.LOCATION_CHANGE, triggerParam -> {
            ParamLocationChange param = (ParamLocationChange) triggerParam;
            if (param.isFromDecrease) {
                param.cancel();
            }
        });
    }
}
