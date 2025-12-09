package com.mllfjn.simyys.character.list.sp.dayuan;

import com.mllfjn.simyys.BattlePane;
import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.status.*;
import com.mllfjn.simyys.character.status.StatusRunnable;
import com.mllfjn.simyys.character.status.determinant.IgnoreActionDecrease;
import com.mllfjn.simyys.character.status.Trigger;
import com.mllfjn.simyys.character.status.triggerParam.TriggerParam;

// 该状态在结缘目标身上,belongTo是结缘目标,from是大缘
abstract class StatusShengTian extends Status implements IgnoreActionDecrease, StatusRunnable {
    // 在对方下回合开始前至多触发一次(即被拉一次才能拉下一次)
    public boolean increase = true;
    private final StatusCombined combined;
    public StatusShengTian(DaYuan from, Character belongTo) {
        super(from, belongTo, StatusType.SPECIAL, StatusForm.SPECIAL);
        this.combined = new StatusCombined(belongTo, from, this);
        from.addStatus(combined);
    }

    public void active() {
        increase = true;
    }

    @Override
    public boolean runnable(Trigger trigger) {
        return trigger == Trigger.AFTER_ROUND && increase ;
    }

    @Override
    public boolean run(Trigger trigger, BattlePane bp, TriggerParam param) {
        // (自身与)处于胜天之缘的目标回合结束后,均会提升对方30%行动条
        // 这里拉条来源必须要是from-大缘自己,因为大缘免疫其他目标拉条
        from.doInteractive(interactive -> interactive.increaseLocation(from, 30));
        increase = false;
        combined.active();

        return false;
    }
}


class StatusSTChi extends StatusShengTian implements Displayable {
    public StatusSTChi(DaYuan from, Character belongTo) {
        super(from, belongTo);
    }

    @Override
    public String getText() {
        return "缘·赤";
    }
}

class StatusSTQing extends StatusShengTian implements Displayable {
    public StatusSTQing(DaYuan from, Character belongTo) {
        super(from, belongTo);
    }

    @Override
    public String getText() {
        return "缘·青";
    }
}


// 该状态在大缘身上,belongTo是大缘,from是结缘目标
class StatusCombined extends Status implements StatusRunnable {
    // 在对方下回合开始前至多触发一次(即被拉一次才能拉下一次)
    public boolean increase = true;
    private final StatusShengTian shengTian;

    public StatusCombined(Character from, DaYuan belongTo, StatusShengTian shengTian) {
        super(from, belongTo, StatusType.SPECIAL, StatusForm.SPECIAL);
        this.shengTian = shengTian;
    }

    public void active() {
        increase = true;
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
        shengTian.active();

        return false;
    }
}
