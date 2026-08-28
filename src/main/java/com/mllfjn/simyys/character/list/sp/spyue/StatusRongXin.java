package com.mllfjn.simyys.character.list.sp.spyue;

import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.status.Status;
import com.mllfjn.simyys.character.status.StatusForm;
import com.mllfjn.simyys.character.status.StatusType;

class StatusRongXin extends Status {
    public StatusRongXin(Character character) {
        super(character, character, StatusType.GENERAL, StatusForm.YIN_JI);
    }
}
