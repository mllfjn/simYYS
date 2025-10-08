package com.mllfjn.simyys.character.sp.dayuan;

import com.mllfjn.simyys.BattlePane;
import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.determinant.ForbidDecrease;
import com.mllfjn.simyys.state.Runnable;
import com.mllfjn.simyys.state.State;
import com.mllfjn.simyys.state.StateForm;
import com.mllfjn.simyys.state.StateType;
import com.mllfjn.simyys.trigger.Trigger;

abstract class StateShengTian extends State implements ForbidDecrease, Runnable {
    public boolean increase = true;
    private final StateCombined combined;
    public StateShengTian(Character from, Character belongTo) {
        super(from, belongTo, StateType.SPECIAL, StateForm.SPECIAL);
        combined = new StateCombined(belongTo, from, this);
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
        from.increaseLocation(bp, belongTo, 30);
        increase = false;
        combined.active();
    }
}
