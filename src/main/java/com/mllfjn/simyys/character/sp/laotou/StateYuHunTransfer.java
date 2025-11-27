package com.mllfjn.simyys.character.sp.laotou;

import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.yuhun.YuHun;
import com.mllfjn.simyys.character.yuhun.YuHunFactory;
import com.mllfjn.simyys.state.*;

public class StateYuHunTransfer extends State implements Displayable {
    private final Class<? extends YuHun> yClass;
    private boolean transfer = true;

    private StateYuHunBeingTransfer beingTransfer;

    public StateYuHunTransfer(Character from, Character belongTo, Class<? extends YuHun> yClass) {
        super(from, belongTo, StateType.SPECIAL, StateForm.SPECIAL);
        this.yClass = yClass;
        setSettleType(StateSettleType.WEI_CHI, 1);

        for (YuHun yuHun : belongTo.getYuHunSet()) {
            if (yuHun.getClass() == yClass) {
                transfer = false;
                break;
            }
        }

        if (transfer) {
            belongTo.addYuHun(YuHunFactory.getYuHun(yClass, belongTo).orElseThrow());
            beingTransfer = new StateYuHunBeingTransfer(belongTo, ((LaoTou) from), yClass, this);
            from.addState(beingTransfer);
        }
    }

    @Override
    public void beforeDelete() {
        if (transfer) {
            belongTo.removeYuHun(yClass);
            beingTransfer.delete();
        }
    }

    @Override
    public String getText() {
        return "转移御魂";
    }
}
