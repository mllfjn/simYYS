package com.mllfjn.simyys.character.list.mob.multiplayer;

import com.mllfjn.simyys.BattlePane;
import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.status.*;
import com.mllfjn.simyys.character.status.triggerParam.ParamAttackInfo;
import com.mllfjn.simyys.character.status.triggerParam.TriggerParam;

public abstract class StatusRecordDamage extends Status implements StatusRunnable {

    public StatusRecordDamage(Character character) {
        super(character, character, StatusType.SPECIAL, StatusForm.SPECIAL);
    }

    protected abstract void addDamage(double damage);

    @Override
    public boolean runnable(Trigger trigger) {
        return trigger == Trigger.AFTER_ATTACK;
    }

    @Override
    public boolean run(Trigger trigger, BattlePane bp, TriggerParam param) {
        if (trigger == Trigger.AFTER_ATTACK) {
            addDamage(((ParamAttackInfo) param).getAttackInfo().getTraceableNumber().getNumber());
        }
        return false;
    }
}
