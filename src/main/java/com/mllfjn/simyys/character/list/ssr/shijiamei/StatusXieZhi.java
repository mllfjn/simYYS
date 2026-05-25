package com.mllfjn.simyys.character.list.ssr.shijiamei;

import com.mllfjn.simyys.BattlePane;
import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.status.*;
import com.mllfjn.simyys.character.status.triggerParam.TriggerParam;

class StatusXieZhi extends Status implements Displayable, StatusRunnable {
    private static final String StatusName = "邪执";

    public StatusXieZhi(Character from, Character belongTo) {
        super(from, belongTo, StatusType.GENERAL, StatusForm.YIN_JI);
        setDurationType(StatusDurationType.CHI_XU, 1);
    }

    @Override
    public String getDisplayText() {
        return StatusName;
    }

    @Override
    public boolean runnable(Trigger trigger) {
        return trigger == Trigger.WILL_USE_SKILL || trigger == Trigger.AFTER_ACTION;
    }

    @Override
    public boolean run(Trigger trigger, BattlePane bp, TriggerParam param) {
        if (trigger == Trigger.WILL_USE_SKILL) {
            StatusXuWangMiZhang.install(from, belongTo);
            return false;
        } else {
            return true;
        }
    }
}
