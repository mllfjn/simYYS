package com.mllfjn.simyys.character.status.instance;

import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.status.*;

public class StatusForceChangeCost extends Status implements ForceChangeCost {
    private final int reduce;

    public StatusForceChangeCost(Character character, int reduce) {
        super(character, character, StatusType.SPECIAL, StatusForm.SPECIAL);
        this.reduce = reduce;

        duration(StatusDurationType.CHI_XU, 1);
    }

    @Override
    public int getChange() {
        return -reduce;
    }
}
