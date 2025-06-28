package com.mllfjn.simyys.character.sp.dayuan;

import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.state.State;
import com.mllfjn.simyys.state.StateForm;
import com.mllfjn.simyys.state.StateType;

class StateFlag extends State {
    public static final String privateName = "DaYuanCombined";

    public StateFlag(Character belongTo) {
        super(belongTo, belongTo, StateType.SPECIAL, StateForm.SPECIAL);
    }

    @Override
    public void setName() {
        name = privateName;
    }
}
