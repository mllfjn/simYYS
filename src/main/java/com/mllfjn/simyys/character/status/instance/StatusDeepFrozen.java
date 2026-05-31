package com.mllfjn.simyys.character.status.instance;

import com.mllfjn.simyys.character.Attribute;
import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.status.*;

public class StatusDeepFrozen extends Status implements CrowdControl, Displayable, AttributeModifier {
    private StatusDeepFrozen(Character from, Character belongTo) {
        super(from, belongTo, StatusType.DEBUFF, StatusForm.YIN_JI);
        setDurationType(StatusDurationType.CHI_XU, 1);
    }

    public static void install(Character from, Character belongTo) {
        if (!belongTo.isHaveStatus(StatusDeepFrozen.class)) {
            belongTo.addStatus(new StatusDeepFrozen(from, belongTo));
        }
    }

    @Override
    public String getDisplayText() {
        return "深度冰冻";
    }

    @Override
    public boolean isAffectAttribute(Attribute attribute) {
        return attribute == Attribute.SPEED;
    }

    @Override
    public double getInfluence(Attribute attribute, StatusModifyParam param) {
        return -20;
    }
}
