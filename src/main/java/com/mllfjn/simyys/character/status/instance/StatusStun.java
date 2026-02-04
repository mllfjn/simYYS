package com.mllfjn.simyys.character.status.instance;

import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.status.*;
import com.mllfjn.simyys.interactive.StatusSupplier;

public class StatusStun extends Status implements CrowdControl, Displayable {
    private static final String StatusName = "眩晕";

    public StatusStun(Character from, Character belongTo, int duration) {
        super(from, belongTo, StatusType.DEBUFF, StatusForm.ZHUANG_TAI);

        setDurationType(StatusDurationType.CHI_XU, duration);
    }

    public static StatusSupplier getSupplier(int duration) {
        return new StatusSupplier(StatusName, StatusStun.class, (from, to) ->
                to.getStatus(StatusStun.class).ifPresentOrElse(
                        status -> {
                            if (status.getDuration() < duration) {
                                status.setDuration(duration);
                            }
                        },
                        () -> to.addStatus(new StatusStun(from, to, duration))
                )
        );
    }

    @Override
    public String getDisplayText() {
        return StatusName + getDuration();
    }
}
