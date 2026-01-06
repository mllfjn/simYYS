package com.mllfjn.simyys.character.list.sp.dayuan;

import com.mllfjn.simyys.BattlePane;
import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.status.*;
import com.mllfjn.simyys.character.status.triggerParam.TriggerParam;

// 该状态在大缘身上,belongTo是大缘,from是结缘目标
public class StatusCombined extends Status implements StatusRunnable {
    // 在对方下回合开始前至多触发一次
    public boolean increase = true;
    private final StatusShengTian shengTian;

    public StatusCombined(Character from, DaYuan belongTo, StatusShengTian shengTian) {
        super(from, belongTo, StatusType.SPECIAL, StatusForm.SPECIAL);
        this.shengTian = shengTian;
    }

    public void active() {
        shengTian.increase = true;
    }

    @Override
    public boolean runnable(Trigger trigger) {
        return trigger == Trigger.AFTER_ROUND && increase;
    }

    @Override
    public boolean run(Trigger trigger, BattlePane bp, TriggerParam param) {
        // 自身(与处于)胜天之缘的目标回合结束后,均会提升对方30%行动条

        belongTo.doInteractive(interactive -> interactive.increaseLocation(from, 30));
        increase = false;

        return false;
    }
}
