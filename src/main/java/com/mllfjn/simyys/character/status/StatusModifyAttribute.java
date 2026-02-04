package com.mllfjn.simyys.character.status;

import com.mllfjn.simyys.character.Character;

public abstract class StatusModifyAttribute extends Status implements AttributeModifier {
    public StatusModifyAttribute(Character from, Character belongTo, StatusType statusType, StatusForm statusForm) {
        super(from, belongTo, statusType, statusForm);
    }
}
