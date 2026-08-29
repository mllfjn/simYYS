package com.mllfjn.simyys.character.list.sr.xuenv;

import com.mllfjn.simyys.character.Attribute;
import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.status.*;
import com.mllfjn.simyys.interactive.StatusSupplier;

class StatusReduceSpeed extends Status implements Displayable, AttributeModifier {
    private static final String StatusName = "减速";

    private StatusReduceSpeed(Character from, Character belongTo) {
        super(from, belongTo, StatusType.DEBUFF, StatusForm.ZHUANG_TAI);
        duration(StatusDurationType.CHI_XU, 2);
    }

    static StatusSupplier getSupplier() {
        return new StatusSupplier(StatusName, StatusReduceSpeed.class, (from, to) -> {
            to.getStatus(StatusReduceSpeed.class)
                    .ifPresentOrElse(
                            status -> status.duration(2),
                            () -> to.addStatus(new StatusReduceSpeed(from, to))
                    );
        });
    }

    @Override
    public boolean isAffectAttribute(Attribute attribute) {
        return attribute == Attribute.SPEED;
    }

    @Override
    public double getInfluence(Attribute attribute, StatusModifyParam param) {
        return -10;
    }

    @Override
    public String getDisplayText() {
        return StatusName;
    }
}
