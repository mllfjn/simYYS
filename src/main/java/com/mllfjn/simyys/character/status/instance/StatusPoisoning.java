package com.mllfjn.simyys.character.status.instance;

import com.mllfjn.simyys.character.Attribute;
import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.status.*;

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

    public double getDefenseForJianJieShangHai() {
        return -10 * stack;
    }

    @Override
    public boolean isAffectAttribute(Attribute attribute) {
        return attribute == Attribute.SPEED;
    }

    @Override
    public double getInfluence(Attribute attribute) {
        return -0.1 * belongTo.getInitSpeed();
    }

    @Override
    public String getDisplayText() {
        return StatusName + getDuration();
    }
}
