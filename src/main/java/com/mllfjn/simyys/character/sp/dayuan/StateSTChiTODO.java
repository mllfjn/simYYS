package com.mllfjn.simyys.character.sp.dayuan;

import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.determinant.ForbidDecrease;
import com.mllfjn.simyys.state.State;
import com.mllfjn.simyys.state.StateForm;
import com.mllfjn.simyys.state.StateType;

class StateSTChiTODO extends StateShengTian{
    public static final String privateName = "胜天之缘·赤";

    public StateSTChiTODO(Character belongTo, Character comeFrom) {
        super(belongTo, comeFrom);
    }

    @Override
    public void setName() {
        name = privateName;
    }
}
