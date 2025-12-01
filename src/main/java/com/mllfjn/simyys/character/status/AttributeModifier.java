package com.mllfjn.simyys.character.status;

import com.mllfjn.simyys.character.Attribute;

public interface AttributeModifier {
    boolean isAffectAttribute(Attribute attribute);
    double getInfluence(Attribute attribute);
}
