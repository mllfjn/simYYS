package com.mllfjn.simyys.character.sp.laotou;

import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.yuhun.YuHun;
import com.mllfjn.simyys.character.yuhun.YuHunFactory;
import com.mllfjn.simyys.state.Displayable;
import com.mllfjn.simyys.state.State;
import com.mllfjn.simyys.state.StateForm;
import com.mllfjn.simyys.state.StateType;

public class StateYuHunBeingTransfer extends State implements Displayable {
    private final Class<? extends YuHun> yClass;
    public final StateYuHunTransfer stateYuHunTransfer;
    public StateYuHunBeingTransfer(Character from, LaoTou belongTo, Class<? extends YuHun> yClass, StateYuHunTransfer stateYuHunTransfer) {
        super(from, belongTo, StateType.SPECIAL, StateForm.SPECIAL);
        this. yClass = yClass;
        this.stateYuHunTransfer = stateYuHunTransfer;
        belongTo.removeYuHun(yClass);
    }

    @Override
    public String getText() {
        return "御魂被转移";
    }

    @Override
    public void beforeDelete() {
        belongTo.addYuHun(YuHunFactory.getYuHun(yClass, belongTo).orElseThrow());
    }
}
