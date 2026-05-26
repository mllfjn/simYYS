package com.mllfjn.simyys.character.list.ssr.xunxiangxing;

import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.status.Displayable;
import com.mllfjn.simyys.character.status.Status;
import com.mllfjn.simyys.character.status.StatusForm;
import com.mllfjn.simyys.character.status.StatusType;

public class StatusShiShen extends Status implements Displayable {
    private StatusShiShen(Character from, Character belongTo) {
        super(from, belongTo, StatusType.DEBUFF, StatusForm.YIN_JI);
    }

    static void install(Character from, Character belongTo) {
        if (belongTo.getStatus(StatusShiShen.class).isEmpty()) {
            belongTo.addStatus(new StatusShiShen(from, belongTo));
        }
    }

    @Override
    public String getDisplayText() {
        return "失神";
    }
}
