package com.mllfjn.simyys.character.list.mob.jifengmo.shenqilou;

import com.mllfjn.simyys.character.Attribute;
import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.status.*;

class StatusReduceCritRate extends Status implements AttributeModifier, Displayable {
    public static final String StatusName = "降暴";
    private final double num;

    public StatusReduceCritRate(ShenQiLou from, Character belongTo, double num) {
        super(from, belongTo, StatusType.DEBUFF, StatusForm.ZHUANG_TAI);
        this.num = num;

        setDurationType(StatusDurationType.CHI_XU, 2);
    }

    @Override
    public boolean isAffectAttribute(Attribute attribute) {
        return attribute == Attribute.CRIT_RATE;
    }

    @Override
    public double getInfluence(Attribute attribute) {
        return -num;
    }

    @Override
    public String getText() {
        return StatusName + getDuration();
    }
}
