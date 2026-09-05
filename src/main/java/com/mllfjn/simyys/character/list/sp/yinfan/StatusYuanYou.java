package com.mllfjn.simyys.character.list.sp.yinfan;

import com.mllfjn.simyys.character.Attribute;
import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.status.*;

class StatusYuanYou extends Status {
    private static final String StatusName = "愿佑";

    private StatusYuanYou(Character from, Character belongTo) {
        super(StatusName, from, belongTo);
        type(StatusType.BUFF, StatusForm.YIN_JI);
        duration(StatusDurationType.CHI_XU, 1);
        attribute(Attribute.CRIT_POWER, _ -> Math.min(120, from.getCritPower() * 0.3));
        attribute(Attribute.DEFENCE, _ -> Math.min(200, from.getDefence() * 0.3));
        displayName();
    }

    public static void install(Character from, Character belongTo) {
        belongTo.getStatus(StatusYuanYou.class)
                .ifPresentOrElse(
                        status -> status.duration(1),
                        () -> belongTo.addStatus(new StatusYuanYou(from, belongTo)));
    }
}
