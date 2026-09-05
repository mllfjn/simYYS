package com.mllfjn.simyys.character.list.ssr.shijiamei;

import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.status.*;

class StatusXieZhi extends Status {
    private static final String StatusName = "邪执";

    public StatusXieZhi(Character from, Character belongTo) {
        super(StatusName, from, belongTo, StatusType.GENERAL, StatusForm.YIN_JI);
        duration(StatusDurationType.CHI_XU, 1);
        displayName();
        runOn(Trigger.WILL_USE_SKILL, _ -> StatusXuWangMiZhang.install(from, belongTo));
        runOn(Trigger.AFTER_ACTION, _ -> delete());
    }
}
