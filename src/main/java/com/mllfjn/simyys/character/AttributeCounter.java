package com.mllfjn.simyys.character;

import com.mllfjn.simyys.character.status.AttributeModifier;
import com.mllfjn.simyys.character.status.Status;

import java.util.List;

public class AttributeCounter {
    public static double getGeneralAttribute(Attribute attribute, double base, List<Status> statuses) {
        for (Status status : statuses) {
            if (status instanceof AttributeModifier a && a.isAffectAttribute(attribute)) {
                base += a.getInfluence(attribute);
            }
        }
        return base < 0 ? 0 : base;
    }
}
