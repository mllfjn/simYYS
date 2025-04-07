package com.mllfjn.simyys.character.SP.dayuan;

import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.state.State;

public class ShengTianZhiYuan_Chi extends State {
    public ShengTianZhiYuan_Chi(Character belongTo, Character comeFrom) {
        super(belongTo, comeFrom, StateType.SPECIAL, StateForm.SPECIAL);
    }
}
