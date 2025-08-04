package com.mllfjn.simyys.character.sp.dayuan;

import com.mllfjn.simyys.BattlePane;
import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.state.Runnable;
import com.mllfjn.simyys.state.State;
import com.mllfjn.simyys.state.StateForm;
import com.mllfjn.simyys.state.StateType;
import com.mllfjn.simyys.trigger.Trigger;

class StateFlagCombined extends State implements Runnable {
    public static final String privateName = "DaYuanCombined";

    public boolean increase = true;

    public StateFlagCombined(Character from, Character belongTo) {
        super(from, belongTo, StateType.SPECIAL, StateForm.SPECIAL);
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
    }
}
