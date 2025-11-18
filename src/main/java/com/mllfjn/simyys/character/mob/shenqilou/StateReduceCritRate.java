package com.mllfjn.simyys.character.mob.shenqilou;

import com.mllfjn.simyys.character.Attribute;
import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.state.*;

class StateReduceCritRate extends State implements AttributeModifier, Displayable {
    public static final String StateName = "降暴";
    private final double num;

    public StateReduceCritRate(ShenQiLou from, Character belongTo, double num) {
        super(from, belongTo, StateType.DEBUFF, StateForm.ZHUANG_TAI);
        this.num = num;

        setSettleType(StateSettleType.CHI_XU, 2);
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
        return StateName + getDuration();
    }
}
