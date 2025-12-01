package com.mllfjn.simyys.character.sp.laotou;

import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.yuhun.YuHun;
import com.mllfjn.simyys.character.yuhun.YuHunFactory;
import com.mllfjn.simyys.character.status.Displayable;
import com.mllfjn.simyys.character.status.Status;
import com.mllfjn.simyys.character.status.StatusForm;
import com.mllfjn.simyys.character.status.StatusType;

public class StatusYuHunBeingTransfer extends Status implements Displayable {
    private final Class<? extends YuHun> yClass;
    public final StatusYuHunTransfer statusYuHunTransfer;
    public StatusYuHunBeingTransfer(Character from, LaoTou belongTo, Class<? extends YuHun> yClass, StatusYuHunTransfer statusYuHunTransfer) {
        super(from, belongTo, StatusType.SPECIAL, StatusForm.SPECIAL);
        this. yClass = yClass;
        this.statusYuHunTransfer = statusYuHunTransfer;
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
