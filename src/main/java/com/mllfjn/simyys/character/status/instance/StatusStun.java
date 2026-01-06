package com.mllfjn.simyys.character.status.instance;

import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.status.*;

public class StatusStun extends Status implements CrowdControl, Displayable {
    public static final String StatusName = "眩晕";
    public StatusStun(Character from, Character belongTo, int duration) {
        super(from, belongTo, StatusType.DEBUFF, StatusForm.ZHUANG_TAI);

        setDurationType(StatusDurationType.CHI_XU, duration);
    }

    @Override
    public String getDisplayText() {
        return StatusName + getDuration();
    }
}
