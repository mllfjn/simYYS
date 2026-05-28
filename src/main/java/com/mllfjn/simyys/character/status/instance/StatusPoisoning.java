package com.mllfjn.simyys.character.status.instance;

import com.mllfjn.simyys.character.Attribute;
import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.status.*;
import com.mllfjn.simyys.interactive.AttackType;

public class StatusPoisoning extends Status implements Displayable, AttributeModifier {
    private static final String StatusName = "中毒";

    private int stack;

    private StatusPoisoning(Character from, Character belongTo, int stack, int duration) {
        super(from, belongTo, StatusType.DEBUFF, StatusForm.ZHUANG_TAI);
        this.stack = stack;

        setDurationType(StatusDurationType.CHI_XU, duration);
    }

    public static void add(Character from, Character belongTo, int stack, int duration) {
        for (Status belongToStatus : belongTo.getStatuses()) {
            if (belongToStatus instanceof StatusPoisoning bsp
                    && bsp.getDuration() == duration
            ) {
                bsp.stack += stack;
                return;
            }
        }
        belongTo.addStatus(new StatusPoisoning(from, belongTo, stack, duration));
    }

    @Override
    public boolean isAffectAttribute(Attribute attribute) {
        return attribute == Attribute.SPEED || attribute == Attribute.DEFENCE;
    }

    @Override
    public double getInfluence(Attribute attribute, StatusModifyParam param) {
        if (attribute == Attribute.SPEED) {
            return -0.1 * belongTo.getInitSpeed();
        } else {
            if (param == null) {
                return 0;
            } else if (param.attackType() == AttackType.JIAN_JIE) {
                return -10 * stack;
            }
        }

        return 0;
    }

    @Override
    public String getDisplayText() {
        return StatusName + stack + "-" + getDuration();
    }
}
