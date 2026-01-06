package com.mllfjn.simyys.character.status.instance;

import com.mllfjn.simyys.BattlePane;
import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.status.*;
import com.mllfjn.simyys.character.status.StatusRunnable;
import com.mllfjn.simyys.character.status.triggerParam.ParamAfterAttack;
import com.mllfjn.simyys.character.status.triggerParam.TriggerParam;

// TODO 无法动作和受到伤害时自动移除效果没有写
public class StatusSleep extends Status implements Displayable, CrowdControl, StatusRunnable {

    public StatusSleep(Character from, Character belongTo) {
        super(from, belongTo, StatusType.DEBUFF, StatusForm.ZHUANG_TAI);
    }

    public static void removeSleep(Character character) {
        character.removeStatus(StatusSleep.class);
    }

    @Override
    public String getDisplayText() {
        return "沉睡" + getDuration();
    }

    @Override
    public boolean runnable(Trigger trigger) {
        return trigger == Trigger.AFTER_ATTACK;
    }

    @Override
    public boolean run(Trigger trigger, BattlePane bp, TriggerParam param) {
        if (param instanceof ParamAfterAttack pa) {
            return pa.interactiveInfo.getTraceableNumber().getNumber() > 0;
        }
        return false;
    }
}
