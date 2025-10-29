package com.mllfjn.simyys.character.sp.dayuan;

import com.mllfjn.simyys.character.Attribute;
import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.state.*;

class StateCritPower extends State implements AttributeModifier {
    public static final String privateName = "大缘加爆伤";
    // 防止爆伤来源为大缘自身时递归计算
    private boolean counting;

    public StateCritPower(Character from, Character belongTo) {
        super(from, belongTo, StateType.SPECIAL, StateForm.SPECIAL);
        setSettleType(StateSettleType.WEI_CHI, 1);
    }

    @Override
    public boolean isAffectAttribute(Attribute attribute) {
        return attribute == Attribute.CRIT_POWER && !counting;
    }

    @Override
    public double getInfluence(Attribute attribute) {
        counting = true;
        double rt = from.getCritPower() * 0.4;
        counting = false;
        return Math.min(rt, 110);
    }

    @Override
    public void setName() {
        this.name = privateName;
    }
}
