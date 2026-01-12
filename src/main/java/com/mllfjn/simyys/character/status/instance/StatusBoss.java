package com.mllfjn.simyys.character.status.instance;

import com.mllfjn.simyys.BattlePane;
import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.status.*;
import com.mllfjn.simyys.character.status.determinant.IgnoreActionDecrease;
import com.mllfjn.simyys.character.status.determinant.IgnoreDebuff;
import com.mllfjn.simyys.character.status.triggerParam.ParamAddCrowdControl;
import com.mllfjn.simyys.character.status.triggerParam.TriggerParam;

public class StatusBoss extends Status implements IgnoreDebuff, IgnoreActionDecrease, StatusRunnable {
    public StatusBoss(Character character) {
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
