package com.mllfjn.simyys.character.list.mob.multiplayer;

import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.status.*;
import com.mllfjn.simyys.character.status.triggerParam.ParamAttackInfo;

public abstract class StatusRecordDamage extends Status {

    public StatusRecordDamage(Character character) {
        super("特殊-记录伤害", character);
        runOn(Trigger.AFTER_ATTACK, triggerParam ->
                addDamage(((ParamAttackInfo) triggerParam).getAttackInfo().getTraceableNumber().getNumber())
        );
    }

    protected abstract void addDamage(double damage);
}
