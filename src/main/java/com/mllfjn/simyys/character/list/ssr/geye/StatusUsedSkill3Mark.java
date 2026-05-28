package com.mllfjn.simyys.character.list.ssr.geye;

import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.status.Status;
import com.mllfjn.simyys.character.status.StatusDurationType;
import com.mllfjn.simyys.character.status.StatusForm;
import com.mllfjn.simyys.character.status.StatusType;

class StatusUsedSkill3Mark extends Status {
    public StatusUsedSkill3Mark(Character character) {
        super(character, character, StatusType.SPECIAL, StatusForm.SPECIAL);
        setDurationType(StatusDurationType.CHI_XU, 2);
    }
}
