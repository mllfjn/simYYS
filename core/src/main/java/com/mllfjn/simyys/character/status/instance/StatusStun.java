package com.mllfjn.simyys.character.status.instance;

import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.status.*;
import com.mllfjn.simyys.interactive.StatusSupplier;

public class StatusStun extends Status implements CrowdControl {
    private static final String StatusName = "眩晕";

    public StatusStun(Character from, Character belongTo, int duration) {
        super(StatusName, from, belongTo, StatusType.DEBUFF, StatusForm.YIN_JI);

        duration(StatusDurationType.CHI_XU, duration);
        displayNameAndDuration();
    }

    public static StatusSupplier getSupplier(int duration) {
        return new StatusSupplier(StatusName, StatusStun.class, (from, to) ->
                to.getStatus(StatusStun.class).ifPresentOrElse(
                        status -> {
                            if (status.getDuration() < duration) {
                                status.duration(duration);
                            }
                        },
                        () -> to.addStatus(new StatusStun(from, to, duration))
                )
        );
    }
}
