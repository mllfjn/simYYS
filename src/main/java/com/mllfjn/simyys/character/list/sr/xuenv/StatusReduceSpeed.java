package com.mllfjn.simyys.character.list.sr.xuenv;

import com.mllfjn.simyys.character.Attribute;
import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.status.*;
import com.mllfjn.simyys.interactive.StatusSupplier;

class StatusReduceSpeed extends Status {
    private static final String StatusName = "减速";

    private StatusReduceSpeed(Character from, Character belongTo) {
        super(XueNv.CharacterName + StatusName, from, belongTo, StatusType.DEBUFF, StatusForm.ZHUANG_TAI);
        duration(StatusDurationType.CHI_XU, 2);
        attribute(Attribute.SPEED, -10.0);
        displayName();
    }

    static StatusSupplier getSupplier() {
        return new StatusSupplier(StatusName, StatusReduceSpeed.class, (from, to) ->
                to.getStatus(StatusReduceSpeed.class)
                        .ifPresentOrElse(
                                status -> status.duration(2),
                                () -> to.addStatus(new StatusReduceSpeed(from, to))
                        )
        );
    }
}
