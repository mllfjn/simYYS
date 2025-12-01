package com.mllfjn.simyys.character.ssr.qianji;

import com.mllfjn.simyys.BattlePane;
import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.status.*;
import com.mllfjn.simyys.character.status.Runnable;
import com.mllfjn.simyys.trigger.Trigger;

class StatusXiMeng extends Status implements Runnable, Displayable {
    public StatusXiMeng(Character from, Character belongTo) {
        super(from, belongTo, StatusType.DEBUFF, StatusForm.YIN_JI);
    }

    @Override
    public String getText() {
        return "汐梦";
    }

    @Override
    public boolean runnable(Trigger trigger) {
        // 行动后消耗
        return trigger == Trigger.AFTER_ROUND;
    }

    @Override
    public boolean run(Trigger trigger, BattlePane bp) {
        // TODO 扣除3点鬼火,对怪物不生效
        return true;
    }
}
