package com.mllfjn.simyys.character.list.mob.multiplayer.jifengmo.shenqilou;

import com.mllfjn.simyys.character.Attribute;
import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.status.*;

class StatusReduceCritRate extends Status {
    private static final String StatusName = "降暴";

    public StatusReduceCritRate(Character from, Character belongTo, double num) {
        super(StatusName, from, belongTo);
        type(StatusType.DEBUFF, StatusForm.ZHUANG_TAI);
        duration(StatusDurationType.CHI_XU, 2);
        displayNameAndDuration();
        attribute(Attribute.CRIT_RATE, _ -> -num);
    }
}
