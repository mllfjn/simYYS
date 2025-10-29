package com.mllfjn.simyys.character.sp.dayuan;

import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.state.*;

class StateSTQingTODO extends StateShengTian implements Displayable {
    public static final String privateName = "胜天之缘·青";

    public StateSTQingTODO(Character from, Character belongTo) {
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
