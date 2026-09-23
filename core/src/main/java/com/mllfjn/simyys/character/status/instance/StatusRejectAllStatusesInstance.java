package com.mllfjn.simyys.character.status.instance;

import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.status.*;
import com.mllfjn.simyys.character.status.determinant.RejectAllStatuses;
import com.mllfjn.simyys.character.status.triggerParam.ParamAddCrowdControl;

public class StatusRejectAllStatusesInstance extends Status implements RejectAllStatuses {
    public StatusRejectAllStatusesInstance(Character character) {
        super("无法添加状态", character);
        runOn(Trigger.ADDING_CROWD_CONTROL, param ->
                ((ParamAddCrowdControl) param).getEffectInfo().setCancel(true)
        );
    }
}
