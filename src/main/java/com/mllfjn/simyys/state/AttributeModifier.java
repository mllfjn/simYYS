package com.mllfjn.simyys.state;

import com.mllfjn.simyys.character.Attribute;

public interface AttributeModifier {
    boolean isAffectAttribute(Attribute attribute);
    double getInfluence(Attribute attribute);
}
