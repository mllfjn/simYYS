package com.mllfjn.simyys.character.list.ssr.xunxiangxing;

import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.status.Status;
import com.mllfjn.simyys.character.status.StatusForm;
import com.mllfjn.simyys.character.status.StatusType;

public class StatusShiShen extends Status {
    private StatusShiShen(Character from, Character belongTo) {
        super("失神", from, belongTo, StatusType.DEBUFF, StatusForm.YIN_JI);
        displayName();
    }

    static void install(Character from, Character belongTo) {
        if (belongTo.getStatus(StatusShiShen.class).isEmpty()) {
            belongTo.addStatus(new StatusShiShen(from, belongTo));
        }
    }
}
