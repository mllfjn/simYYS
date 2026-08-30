package com.mllfjn.simyys.character.list.sp.laotou;

import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.yuhun.YuHun;
import com.mllfjn.simyys.character.status.Status;

public class StatusYuHunBeingTransfer extends Status {
    public final StatusYuHunTransfer statusYuHunTransfer;
    private final YuHun removed;

    public StatusYuHunBeingTransfer(Character from, LaoTou belongTo, Class<? extends YuHun> yClass, StatusYuHunTransfer statusYuHunTransfer) {
        super("御魂被转移", from, belongTo);
        this.statusYuHunTransfer = statusYuHunTransfer;
        removed = belongTo.removeYuHun(yClass);
        displayName();
        beforeDelete(() -> belongTo.addYuHun(removed));
    }
}
