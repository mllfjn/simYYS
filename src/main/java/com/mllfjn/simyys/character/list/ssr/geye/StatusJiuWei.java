package com.mllfjn.simyys.character.list.ssr.geye;

import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.status.Displayable;
import com.mllfjn.simyys.character.status.Status;
import com.mllfjn.simyys.character.status.StatusForm;
import com.mllfjn.simyys.character.status.StatusType;

import java.util.Optional;

class StatusJiuWei extends Status implements Displayable {
    private static final String StatusName = "九尾";
    private int stack = 1;

    private StatusJiuWei(Character character) {
        super(character, character, StatusType.GENERAL, StatusForm.YIN_JI);
    }

    int getStack() {
        return stack;
    }

    static boolean addStack(Character character) {
        Optional<StatusJiuWei> oStatus = character.getStatus(StatusJiuWei.class);
        if (oStatus.isPresent()) {
            StatusJiuWei status = oStatus.get();
            status.stack++;
            return status.stack == 3;
        } else {
            character.addStatus(new StatusJiuWei(character));
            return false;
        }
    }

    @Override
    public String getDisplayText() {
        return StatusName + stack;
    }
}
