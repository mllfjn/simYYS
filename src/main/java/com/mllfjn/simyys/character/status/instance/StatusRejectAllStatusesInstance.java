package com.mllfjn.simyys.character.status.instance;

import com.mllfjn.simyys.BattlePane;
import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.status.*;
import com.mllfjn.simyys.character.status.determinant.RejectAllStatuses;
import com.mllfjn.simyys.character.status.triggerParam.ParamAddCrowdControl;
import com.mllfjn.simyys.character.status.triggerParam.TriggerParam;

public class StatusRejectAllStatusesInstance extends Status implements RejectAllStatuses, StatusRunnable {
    public StatusRejectAllStatusesInstance(Character character) {
        super(character, character, StatusType.SPECIAL, StatusForm.SPECIAL);
    }

    @Override
    public boolean runnable(Trigger trigger) {
        return trigger == Trigger.ADDING_CROWD_CONTROL;
    }

    @Override
    public boolean run(Trigger trigger, BattlePane bp, TriggerParam param) {
        if (param instanceof ParamAddCrowdControl pac) {
            pac.getEffectInfo().setCancel(true);
        }
        return false;
    }
}
