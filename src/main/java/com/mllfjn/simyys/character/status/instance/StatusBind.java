package com.mllfjn.simyys.character.status.instance;

import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.status.*;
import com.mllfjn.simyys.interactive.StatusSupplier;

public class StatusBind extends Status implements CrowdControl, Displayable {
    private static final String StatusName = "束缚";

    public StatusBind(Character from, Character belongTo, int duration) {
        super(from, belongTo, StatusType.DEBUFF, StatusForm.ZHUANG_TAI);
        duration(StatusDurationType.CHI_XU, duration);
    }

    public static StatusSupplier getSupplier(int duration) {
        return new StatusSupplier(StatusName, StatusBind.class, (from, to) ->
                to.getStatus(StatusBind.class).ifPresentOrElse(
                        status -> {
                            if (status.getDuration() < duration) {
                                status.duration(duration);
                            }
                        },
                        () -> to.addStatus(new StatusBind(from, to, duration))
                )
        );
    }

    public void doBind() {
        belongTo.getPuGong().ifPresent(skill -> skill.log(null));
    }

    @Override
    public String getDisplayText() {
        return StatusName;
    }
}
