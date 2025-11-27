package com.mllfjn.simyys.character.sp.laotou;

import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.state.State;
import com.mllfjn.simyys.state.StateForm;
import com.mllfjn.simyys.state.StateSettleType;
import com.mllfjn.simyys.state.StateType;

class StateUse3Flag extends State {
    public StateUse3Flag(LaoTou laoTou) {
        super(laoTou, laoTou, StateType.SPECIAL, StateForm.SPECIAL);
        setSettleType(StateSettleType.WEI_CHI, 1);
    }
}
