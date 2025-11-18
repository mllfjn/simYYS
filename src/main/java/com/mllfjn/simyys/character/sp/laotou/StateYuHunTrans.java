package com.mllfjn.simyys.character.sp.laotou;

import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.yuhun.YuHun;
import com.mllfjn.simyys.character.yuhun.YuHunFactory;
import com.mllfjn.simyys.state.*;

public class StateYuHunTrans extends State implements Displayable {
    private final Class<? extends YuHun> yClass;
    private boolean willTrans = true;

    public StateYuHunTrans(Character from, Character belongTo, Class<? extends YuHun> yClass) {
        super(from, belongTo, StateType.SPECIAL, StateForm.SPECIAL);
        this.yClass = yClass;
        setSettleType(StateSettleType.WEI_CHI, 1);

        for (YuHun yuHun : belongTo.getYuHunSet()) {
            if (yuHun.getClass() == yClass) {
                willTrans = false;
                break;
            }
        }

        if (willTrans) {
            belongTo.addYuHun(YuHunFactory.getYuHun(yClass, belongTo));
        }
    }

    @Override
    public void beforeDelete() {
        if (willTrans) {
            belongTo.removeYuHun(yClass);
        }
    }

    @Override
    public String getText() {
        return "转移御魂";
    }
}
