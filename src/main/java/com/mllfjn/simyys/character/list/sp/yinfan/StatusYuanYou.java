package com.mllfjn.simyys.character.list.sp.yinfan;

import com.mllfjn.simyys.character.Attribute;
import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.status.*;

class StatusYuanYou extends Status implements AttributeModifier, Displayable {
    private static final String StatusName = "愿佑";

    private StatusYuanYou(Character from, Character belongTo) {
        super(from, belongTo, StatusType.BUFF, StatusForm.YIN_JI);
        duration(StatusDurationType.CHI_XU, 1);
    }

    public static void install(Character from, Character belongTo) {
        belongTo.getStatus(StatusYuanYou.class)
                .ifPresentOrElse(
                        status -> status.duration(1),
                        () -> belongTo.addStatus(new StatusYuanYou(from, belongTo)));
    }

    @Override
    public boolean isAffectAttribute(Attribute attribute) {
        return attribute == Attribute.CRIT_POWER || attribute == Attribute.DEFENCE;
    }

    @Override
    public double getInfluence(Attribute attribute, StatusModifyParam param) {
        if (attribute == Attribute.CRIT_POWER) {
            return Math.min(120, from.getCritPower() * 0.3);
        } else {
            return Math.min(200, from.getDefence() * 0.3);
        }
    }

    @Override
    public String getDisplayText() {
        return StatusName;
    }
}
