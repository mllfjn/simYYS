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
    public StateShengTian(Character belongTo, Character comeFrom) {
        super(belongTo, comeFrom, StateType.SPECIAL, StateForm.SPECIAL);
    }

    @Override
    public boolean runnable(Trigger trigger) {
        return trigger == Trigger.BEFORE_ROUND && increase ;
    }

    @Override
    public void run(Trigger trigger, BattlePane bp) {
        comeFrom.increaseLocation(comeFrom, 30);
        increase = false;
    }
}
