package com.mllfjn.simyys.character.status.instance;

import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.status.*;

public class StatusReduceCost extends Status implements ReduceCost {
    private final int reduce;

    public StatusReduceCost(Character character, int reduce) {
        super(character, character, StatusType.SPECIAL, StatusForm.SPECIAL);
        this.reduce = reduce;

        setDurationType(StatusDurationType.CHI_XU, 1);
    }

    @Override
    public int getReduce() {
        return reduce;
    }
}
