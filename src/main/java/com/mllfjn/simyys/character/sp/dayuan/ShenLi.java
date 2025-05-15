package com.mllfjn.simyys.character.sp.dayuan;

import com.mllfjn.simyys.character.Attribute;
import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.state.State;

public class ShenLi extends State {
    public static final String privateName = "神力";
    private int ceng;

    public ShenLi(Character belongTo, Character comeFrom) {
        super(belongTo, comeFrom, StateType.BUFF, StateForm.YIN_JI);
    }

    @Override
    public void setName() {
        this.name = privateName;
    }

    public void addCeng(int add) {
        this.ceng += add;
        if (ceng > 5) {
            ceng = 5;
        }
    }

    @Override
    public boolean isAffectAttribute(Attribute attribute) {
        return attribute == Attribute.EFFECT_RESIST_RATE;
    }

    @Override
    public double getInfluence(Attribute attribute) {
        return ceng * 20;
    }
}
