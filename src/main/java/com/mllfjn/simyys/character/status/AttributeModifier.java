package com.mllfjn.simyys.character.status;

import com.mllfjn.simyys.character.Attribute;
import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.interactive.AttackType;

public interface AttributeModifier {
    boolean isAffectAttribute(Attribute attribute);

    double getInfluence(Attribute attribute, StatusModifyParam param);

    record StatusModifyParam(Character target, AttackType attackType) {
    }
}
