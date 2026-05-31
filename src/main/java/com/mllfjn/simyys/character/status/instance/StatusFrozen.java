package com.mllfjn.simyys.character.status.instance;

import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.status.*;
import com.mllfjn.simyys.interactive.StatusSupplier;

import java.util.Optional;

public class StatusFrozen extends Status implements CrowdControl, Displayable {
    public static final String StatusName = "冰冻";

    private StatusFrozen(Character from, Character belongTo, int duration) {
        super(from, belongTo, StatusType.DEBUFF, StatusForm.ZHUANG_TAI);
        setDurationType(StatusDurationType.CHI_XU, duration);
    }

    public static void install(Character from, Character belongTo, int duration) {
        Optional<StatusFrozen> oStatus = belongTo.getStatus(StatusFrozen.class);
        if (oStatus.isPresent()) {
            StatusFrozen status = oStatus.get();
            if (status.getDuration() >= duration) {
                return;
            } else {
                status.delete();
            }
        }
        belongTo.addStatus(new StatusFrozen(from, belongTo, duration));
    }

    public static StatusSupplier getSupplier(int duration) {
        return new StatusSupplier(StatusName, StatusFrozen.class,
                (from, to) -> install(from, to, duration)
        );
    }

    @Override
    public String getDisplayText() {
        return StatusName + getDuration();
    }
}
