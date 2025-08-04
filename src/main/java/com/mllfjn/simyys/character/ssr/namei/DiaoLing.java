package com.mllfjn.simyys.character.ssr.namei;

import com.mllfjn.simyys.character.Attribute;
import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.state.*;

public class DiaoLing extends State implements Displayable, AttributeModifier {
    public static final String privateName = "凋零";

    public DiaoLing(Character belongTo, Character from) {
        super(from, belongTo, StateType.GENERAL, StateForm.YIN_JI);
        setSettleType(StateSettleType.CHI_XU, 1);
    }

    @Override
    public void setName() {
        name = privateName;
    }

    @Override
    public String getText() {
        return "凋零" + getDuration();
    }

    @Override
    public boolean isAffectAttribute(Attribute attribute) {
        return attribute == Attribute.DEFENCE;
    }

    @Override
    public double getInfluence(Attribute attribute) {
        return attribute == Attribute.DEFENCE ? -100 : 0;
    }
}
