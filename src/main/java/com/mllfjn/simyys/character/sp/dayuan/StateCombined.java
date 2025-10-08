package com.mllfjn.simyys.character.sp.dayuan;

import com.mllfjn.simyys.BattlePane;
import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.state.Runnable;
import com.mllfjn.simyys.state.State;
import com.mllfjn.simyys.state.StateForm;
import com.mllfjn.simyys.state.StateType;
import com.mllfjn.simyys.trigger.Trigger;

class StateCombined extends State implements Runnable {
    public static final String privateName = "DaYuanCombined";

    public boolean increase = true;
    private final StateShengTian shengTian;

    public StateCombined(Character from, Character belongTo, StateShengTian shengTian) {
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
        from.increaseLocation(bp, belongTo, 30);
        increase = false;
        shengTian.active();
    }
}
