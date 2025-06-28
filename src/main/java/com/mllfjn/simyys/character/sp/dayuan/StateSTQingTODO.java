package com.mllfjn.simyys.character.sp.dayuan;

import com.mllfjn.simyys.BattlePane;
import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.determinant.ForbidDecrease;
import com.mllfjn.simyys.state.Runnable;
import com.mllfjn.simyys.state.State;
import com.mllfjn.simyys.state.StateForm;
import com.mllfjn.simyys.state.StateType;
import com.mllfjn.simyys.trigger.Trigger;

class StateSTQingTODO extends StateShengTian {
    public static final String privateName = "胜天之缘·青";

    public StateSTQingTODO(Character belongTo, Character comeFrom) {
        super(belongTo, comeFrom);
    }

    @Override
    public void setName() {
        name = privateName;
    }

}
