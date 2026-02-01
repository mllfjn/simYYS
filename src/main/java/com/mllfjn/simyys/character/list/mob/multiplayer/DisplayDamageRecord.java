package com.mllfjn.simyys.character.list.mob.multiplayer;

import com.mllfjn.simyys.BattlePane;
import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.status.*;
import com.mllfjn.simyys.character.status.triggerParam.ParamAfterAttack;
import com.mllfjn.simyys.character.status.triggerParam.TriggerParam;

public class DisplayDamageRecord extends Status implements StatusRunnable, InfoDisplay {
    private double totalDamage;

    public DisplayDamageRecord(Character character) {
        super(character, character, StatusType.SPECIAL, StatusForm.SPECIAL);
    }

    public void addDamage(double damage) {
        this.totalDamage += damage;
    }

    @Override
    public boolean runnable(Trigger trigger) {
        return trigger == Trigger.AFTER_ATTACK;
    }

    @Override
    public boolean run(Trigger trigger, BattlePane bp, TriggerParam param) {
        if (param instanceof ParamAfterAttack paa) {
            addDamage(paa.attackInfo.getTraceableNumber().getNumber());
        }
        return false;
    }

    @Override
    public String getInfo() {
        StringBuilder sb = new StringBuilder("我的伤害:");
        if (totalDamage > 10000) {
            sb.append(String.format("%.1f", totalDamage / 10000)).append("万");
        } else {
            sb.append("<1万");
        }
        return sb.toString();
    }
}
