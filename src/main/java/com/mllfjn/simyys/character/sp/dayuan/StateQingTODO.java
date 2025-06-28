package com.mllfjn.simyys.character.sp.dayuan;

import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.state.State;
import com.mllfjn.simyys.state.StateForm;
import com.mllfjn.simyys.state.StateType;

class StateQingTODO extends State {
    public static final String privateName = "尘缘·青";

    public StateQingTODO(Character belongTo, Character comeFrom) {
        super(belongTo, comeFrom, StateType.BUFF, StateForm.YIN_JI);
    }

    @Override
    public void setName() {
        name = privateName;
    }
}
