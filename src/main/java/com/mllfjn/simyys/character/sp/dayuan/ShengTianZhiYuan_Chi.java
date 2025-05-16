package com.mllfjn.simyys.character.sp.dayuan;

import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.state.State;
import com.mllfjn.simyys.state.StateForm;
import com.mllfjn.simyys.state.StateType;

public class ShengTianZhiYuan_Chi extends State {
    public static final String privateName = "胜天之缘·赤";
    public ShengTianZhiYuan_Chi(Character belongTo, Character comeFrom) {
        super(belongTo, comeFrom, StateType.SPECIAL, StateForm.SPECIAL);
    }

    @Override
    public void setName() {
        this.name = privateName;
    }
}
