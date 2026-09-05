package com.mllfjn.simyys.character.list.sp.laotou;

import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.yuhun.Equip;
import com.mllfjn.simyys.character.status.Status;

public class StatusYuHunBeingTransfer extends Status {
    public final StatusYuHunTransfer statusYuHunTransfer;
    private final Equip removed;

    public StatusYuHunBeingTransfer(Character from, LaoTou belongTo, Equip equip, StatusYuHunTransfer statusYuHunTransfer) {
        super("御魂被转移", from, belongTo);
        this.statusYuHunTransfer = statusYuHunTransfer;
        removed = belongTo.removeYuHun(equip.getClass());
        displayName();
        beforeDelete(() -> belongTo.addYuHun(removed));
    }
}
