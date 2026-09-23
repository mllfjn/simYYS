package com.mllfjn.simyys.character.status.instance;

import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.status.*;
import com.mllfjn.simyys.character.status.determinant.IgnoreDebuff;
import com.mllfjn.simyys.character.status.triggerParam.ParamAddCrowdControl;
import com.mllfjn.simyys.character.status.triggerParam.ParamLocationChange;

public class StatusBoss extends Status implements IgnoreDebuff {
    public StatusBoss(Character character) {
        super("BOSS", character);
        // 免控
        runOn(Trigger.ADDING_CROWD_CONTROL, triggerParam -> {
            if (triggerParam instanceof ParamAddCrowdControl pac) {
                pac.getEffectInfo().setCancel(true);
            }
        });
        // 免疫击退
        runOn(Trigger.LOCATION_WILL_CHANGE, triggerParam -> {
            ParamLocationChange param = (ParamLocationChange) triggerParam;
            if (param.isFromDecrease) {
                param.cancel();
            }
        });
    }
}
