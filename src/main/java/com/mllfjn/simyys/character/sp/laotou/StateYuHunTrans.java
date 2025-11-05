package com.mllfjn.simyys.character.sp.laotou;

import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.yuhun.YuHun;
import com.mllfjn.simyys.character.yuhun.YuHunFactory;
import com.mllfjn.simyys.character.yuhun.YuHunSealResponse;
import com.mllfjn.simyys.state.State;
import com.mllfjn.simyys.state.StateForm;
import com.mllfjn.simyys.state.StateSettleType;
import com.mllfjn.simyys.state.StateType;

public class StateYuHunTrans extends State {
    public final Class<? extends YuHun> yClass;

    public StateYuHunTrans(Character from, Character belongTo, Class<? extends YuHun> yClass) {
        super(from, belongTo, StateType.SPECIAL, StateForm.SPECIAL);
        this.yClass = yClass;
        belongTo.addYuHun(YuHunFactory.getYuHun(yClass, belongTo), false);
        setSettleType(StateSettleType.WEI_CHI, 1);
    }

    @Override
    public void delete() {
        super.delete();
        belongTo.removeYuHun(YuHun.class);
    }
}
