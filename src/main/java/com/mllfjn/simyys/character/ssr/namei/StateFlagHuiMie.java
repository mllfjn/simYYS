package com.mllfjn.simyys.character.ssr.namei;

import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.state.State;
import com.mllfjn.simyys.state.StateForm;
import com.mllfjn.simyys.state.StateType;

public class StateFlagHuiMie extends State {
    public static final String privateName = "Flag-毁灭";

    public StateFlagHuiMie(Character from, Character to) {
        super(from, to, StateType.SPECIAL, StateForm.SPECIAL);
    }

    @Override
    public void setName() {
        this.name = privateName;
    }
}
