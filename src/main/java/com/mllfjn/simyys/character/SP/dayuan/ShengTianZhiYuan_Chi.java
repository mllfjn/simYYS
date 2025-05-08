package com.mllfjn.simyys.character.SP.dayuan;

import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.state.State;

public class ShengTianZhiYuan_Chi extends State {
    public static final String privateName = "胜天之缘·赤";
    public ShengTianZhiYuan_Chi(Character belongTo, Character comeFrom) {
        super(belongTo, comeFrom, StateType.SPECIAL, StateForm.SPECIAL);
    }


    @Override
    protected void setName() {
        name = privateName;
    }
/*
    @Override
    void setName() {
        this.name = privateName;
    }*/
}
