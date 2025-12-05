package com.mllfjn.simyys.character.status.instance;

import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.status.Status;
import com.mllfjn.simyys.character.status.StatusForm;
import com.mllfjn.simyys.character.status.StatusType;
import com.mllfjn.simyys.character.status.determinant.RejectAllStatuses;

public class RejectAllStatusesInstance extends Status implements RejectAllStatuses {
    public RejectAllStatusesInstance(Character character) {
        super(character, character, StatusType.SPECIAL, StatusForm.SPECIAL);
    }
}
