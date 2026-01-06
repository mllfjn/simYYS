package com.mllfjn.simyys.character.list.sp.laotou;

import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.status.*;
import com.mllfjn.simyys.character.yuhun.YuHun;
import com.mllfjn.simyys.character.yuhun.YuHunFactory;

public class StatusYuHunTransfer extends Status implements Displayable {
    private final Class<? extends YuHun> yClass;
    private boolean transfer = true;

    private StatusYuHunBeingTransfer beingTransfer;

    public StatusYuHunTransfer(Character from, Character belongTo, Class<? extends YuHun> yClass) {
        super(from, belongTo, StatusType.SPECIAL, StatusForm.SPECIAL);
        this.yClass = yClass;
        setDurationType(StatusDurationType.WEI_CHI, 1);

        for (YuHun yuHun : belongTo.getYuHunSet()) {
            if (yuHun.getClass() == yClass) {
                transfer = false;
                break;
            }
        }

        if (transfer) {
            belongTo.addYuHun(YuHunFactory.getYuHun(yClass, belongTo).orElseThrow());
            beingTransfer = new StatusYuHunBeingTransfer(belongTo, ((LaoTou) from), yClass, this);
            from.addStatus(beingTransfer);
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
    public String getDisplayText() {
        return "转移御魂";
    }
}
