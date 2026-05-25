package com.mllfjn.simyys.character.list.sp.shenshe;

import com.mllfjn.simyys.BattlePane;
import com.mllfjn.simyys.character.Attribute;
import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.status.*;
import com.mllfjn.simyys.character.status.StatusRunnable;
import com.mllfjn.simyys.character.status.instance.StatusSleep;
import com.mllfjn.simyys.character.status.Trigger;
import com.mllfjn.simyys.character.status.triggerParam.TriggerParam;

public class StatusDuoHua extends Status implements Displayable, StatusRunnable, AttributeModifier {
    private boolean enable = true;

    public StatusDuoHua(Character from, Character belongTo) {
        super(from, belongTo, StatusType.GENERAL, StatusForm.YIN_JI);
    }

    @Override
    public boolean isAffectAttribute(Attribute attribute) {
        return attribute == Attribute.SPEED;
    }

    @Override
    public double getInfluence(Attribute attribute, StatusModifyParam param) {
        return belongTo.getInitSpeed() * 0.4;
    }

    @Override
    public String getDisplayText() {
        return "堕化";
    }

    @Override
    public boolean runnable(Trigger trigger) {
        return trigger == Trigger.BEFORE_ROUND || trigger == Trigger.DIE;
    }

    @Override
    public boolean run(Trigger trigger, BattlePane bp, TriggerParam param) {
        if (trigger == Trigger.BEFORE_ROUND) {
            // 回合开始时腐蚀自身当前24%生命,视为失去生命,且可解除睡眠状态
            belongTo.lostHP(belongTo.getHp() * 0.24);
            StatusSleep.removeSleep(belongTo);
        } else {
            if (enable) {
                // 携带者阵亡时,神堕八岐大蛇提升40%行动条
                belongTo.doInteractive(interactive -> interactive.increaseLocation(from, 40));
                // 并将携带者献祭成1把堕落之剑
                new DuoLuoZhiJian((ShenShe) from, belongTo, false);
                // 立即破除1把天羽羽斩封印
                ((ShenShe) from).poChuZhenYa();
            }
        }

        return false;
    }

    public void disable() {
        enable = false;
    }
}
