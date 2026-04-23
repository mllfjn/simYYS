package com.mllfjn.simyys.character.list.ssr.geye;

import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.status.Displayable;
import com.mllfjn.simyys.character.status.Status;
import com.mllfjn.simyys.character.status.StatusForm;
import com.mllfjn.simyys.character.status.StatusType;

class StatusJiuWei extends Status implements Displayable {
    private static final String StatusName = "九尾";
    private int stack;

    StatusJiuWei(Character character) {
        super(character, character, StatusType.GENERAL, StatusForm.YIN_JI);
    }

    @Override
    public String getDisplayText() {
        return StatusName + stack;
    }
}
