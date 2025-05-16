package com.mllfjn.simyys.character.ssr.namei;

import com.mllfjn.simyys.character.Attribute;
import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.state.State;
import com.mllfjn.simyys.state.StateForm;
import com.mllfjn.simyys.state.StateType;

public class DiaoLing extends State {
    public static final String privateName = "凋零";

    public DiaoLing(Character belongTo, Character comeFrom) {
        super(belongTo, comeFrom, StateType.GENERAL, StateForm.YIN_JI);
    }

    @Override
    public void setName() {
        name = privateName;
    }

    @Override
    public boolean isAffectAttribute(Attribute attribute) {
        return attribute == Attribute.DEFENCE;
    }

    @Override
    public double getInfluence(Attribute attribute) {
        return -100;
    }

    @Override
    public boolean isDisplayable() {
        return true;
    }

    @Override
    public String getDisplayText() {
        return "凋零";
    }
}
