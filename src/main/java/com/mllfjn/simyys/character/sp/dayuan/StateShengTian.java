package com.mllfjn.simyys.character.sp.dayuan;

import com.mllfjn.simyys.BattlePane;
import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.state.*;
import com.mllfjn.simyys.state.Runnable;
import com.mllfjn.simyys.state.determinant.IgnoreActionDecrease;
import com.mllfjn.simyys.trigger.Trigger;

// 该状态在结缘目标身上,belongTo是结缘目标,from是大缘
abstract class StateShengTian extends State implements IgnoreActionDecrease, Runnable {
    // 在对方下回合开始前至多触发一次(即被拉一次才能拉下一次)
    public boolean increase = true;
    private final StateCombined combined;
    public StateShengTian(DaYuan from, Character belongTo) {
        super(from, belongTo, StateType.SPECIAL, StateForm.SPECIAL);
        this.combined = new StateCombined(belongTo, from, this);
        from.addState(combined);
    }

    public void active() {
        increase = true;
    }

    @Override
    public boolean runnable(Trigger trigger) {
        return trigger == Trigger.AFTER_ROUND && increase ;
    }

    @Override
    public void run(Trigger trigger, BattlePane bp) {
        // (自身与)处于胜天之缘的目标回合结束后,均会提升对方30%行动条
        // 这里拉条来源必须要是from-大缘自己,因为大缘免疫其他目标拉条
        from.increaseLocation(bp, from, 30);
        increase = false;
        combined.active();
    }
}


class StateSTChi extends StateShengTian implements Displayable {
    public static final String privateName = "胜天之缘·赤";

    public StateSTChi(DaYuan from, Character belongTo) {
        super(from, belongTo);
    }

    @Override
    public void setName() {
        name = privateName;
    }

    @Override
    public String getText() {
        return "缘·赤";
    }
}

class StateSTQing extends StateShengTian implements Displayable {
    public static final String privateName = "胜天之缘·青";

    public StateSTQing(DaYuan from, Character belongTo) {
        super(from, belongTo);
    }

    @Override
    public void setName() {
        name = privateName;
    }

    @Override
    public String getText() {
        return "缘·青";
    }
}


// 该状态在大缘身上,belongTo是大缘,from是结缘目标
class StateCombined extends State implements Runnable {
    public static final String privateName = "DaYuanCombined";

    // 在对方下回合开始前至多触发一次(即被拉一次才能拉下一次)
    public boolean increase = true;
    private final StateShengTian shengTian;

    public StateCombined(Character from, DaYuan belongTo, StateShengTian shengTian) {
        super(from, belongTo, StateType.SPECIAL, StateForm.SPECIAL);
        this.shengTian = shengTian;
    }

    public void active() {
        increase = true;
    }

    @Override
    public void setName() {
        name = privateName;
    }

    @Override
    public boolean runnable(Trigger trigger) {
        return trigger == Trigger.AFTER_ROUND && increase;
    }

    @Override
    public void run(Trigger trigger, BattlePane bp) {
        // 自身(与处于)胜天之缘的目标回合结束后,均会提升对方30%行动条
        from.increaseLocation(bp, belongTo, 30);
        increase = false;
        shengTian.active();
    }
}
