package com.mllfjn.simyys.character.sp.dayuan;

import com.mllfjn.simyys.BattlePane;
import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.determinant.ForbidDecrease;
import com.mllfjn.simyys.state.*;
import com.mllfjn.simyys.state.Runnable;
import com.mllfjn.simyys.trigger.Trigger;

class StateSTQingTODO extends StateShengTian implements Displayable {
    public static final String privateName = "胜天之缘·青";

    public StateSTQingTODO(Character belongTo, Character from) {
        super(belongTo, from);
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
