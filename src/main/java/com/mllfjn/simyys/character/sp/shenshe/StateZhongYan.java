package com.mllfjn.simyys.character.sp.shenshe;

import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.state.State;
import com.mllfjn.simyys.state.StateForm;
import com.mllfjn.simyys.state.StateType;
import com.mllfjn.simyys.state.determinant.IgnoreDebuff;

public class StateZhongYan extends State implements IgnoreDebuff {
    public StateZhongYan(Character character) {
        super(character, character, StateType.SPECIAL, StateForm.SPECIAL);
    }

    @Override
    public void setName() {
        name = "终焉审判幻境";
    }
}
