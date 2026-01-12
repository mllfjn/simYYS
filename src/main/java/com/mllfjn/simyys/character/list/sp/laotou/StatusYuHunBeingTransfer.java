package com.mllfjn.simyys.character.list.sp.laotou;

import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.yuhun.YuHun;
import com.mllfjn.simyys.character.yuhun.YuHunFactory;
import com.mllfjn.simyys.character.status.Displayable;
import com.mllfjn.simyys.character.status.Status;
import com.mllfjn.simyys.character.status.StatusForm;
import com.mllfjn.simyys.character.status.StatusType;

public class StatusYuHunBeingTransfer extends Status implements Displayable {
    public final StatusYuHunTransfer statusYuHunTransfer;
    private final YuHun removed;

    public StatusYuHunBeingTransfer(Character from, LaoTou belongTo, Class<? extends YuHun> yClass, StatusYuHunTransfer statusYuHunTransfer) {
        super(from, belongTo, StatusType.SPECIAL, StatusForm.SPECIAL);
        this.statusYuHunTransfer = statusYuHunTransfer;
        removed = belongTo.removeYuHun(yClass);
    }

    @Override
    public String getDisplayText() {
        return "御魂被转移";
    }

    @Override
    public void beforeDelete() {
        belongTo.addYuHun(removed);
    }
}
