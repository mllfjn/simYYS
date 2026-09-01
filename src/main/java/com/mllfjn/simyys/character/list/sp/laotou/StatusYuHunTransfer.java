package com.mllfjn.simyys.character.list.sp.laotou;

import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.status.*;
import com.mllfjn.simyys.character.yuhun.YuHun;
import com.mllfjn.simyys.character.yuhun.YuHunFactory;

public class StatusYuHunTransfer extends Status {
    private final YuHun addedYuHun;

    private boolean transfer = true;

    private StatusYuHunBeingTransfer beingTransfer;

    public StatusYuHunTransfer(Character from, Character belongTo, Class<? extends YuHun> yClass) {
        super("转移御魂", from, belongTo);
        duration(StatusDurationType.WEI_CHI, 1);

        for (YuHun yuHun : belongTo.getYuHunSet()) {
            if (yuHun.getClass() == yClass) {
                transfer = false;
                break;
            }
        }

        if (transfer) {
            addedYuHun = YuHunFactory.getYuHun(yClass, belongTo, false).orElseThrow();
            belongTo.addYuHun(addedYuHun);
            beingTransfer = new StatusYuHunBeingTransfer(belongTo, ((LaoTou) from), yClass, this);
            from.addStatus(beingTransfer);
        } else {
            addedYuHun = null;
        }

        beforeDelete(() -> {
            if (transfer && addedYuHun != null) {
                belongTo.removeYuHun(addedYuHun);
                beingTransfer.delete();
            }
        });
        displayName();
    }
}
