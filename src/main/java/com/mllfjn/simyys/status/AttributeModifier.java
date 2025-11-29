package com.mllfjn.simyys.status;

import com.mllfjn.simyys.character.Attribute;

public interface AttributeModifier {
    boolean isAffectAttribute(Attribute attribute);
    double getInfluence(Attribute attribute);
}
