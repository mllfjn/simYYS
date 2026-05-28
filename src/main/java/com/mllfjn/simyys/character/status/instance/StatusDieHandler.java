package com.mllfjn.simyys.character.status.instance;

import com.mllfjn.simyys.BattlePane;
import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.status.*;
import com.mllfjn.simyys.character.status.triggerParam.TriggerParam;
import com.mllfjn.simyys.utils.SerializableRunnable;

public class StatusDieHandler extends Status implements StatusRunnable {
    private final SerializableRunnable action;

    public StatusDieHandler(Character character, SerializableRunnable action) {
        super(character, character, StatusType.SPECIAL, StatusForm.SPECIAL);
        this.action = action;
    }

    @Override
    public boolean runnable(Trigger trigger) {
        return trigger == Trigger.DIE;
    }

    @Override
    public boolean run(Trigger trigger, BattlePane bp, TriggerParam param) {
        action.run();
        return false;
    }
}
