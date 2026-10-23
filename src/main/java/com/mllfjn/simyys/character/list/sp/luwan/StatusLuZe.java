package com.mllfjn.simyys.character.list.sp.luwan;

import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.status.Status;
import com.mllfjn.simyys.character.status.StatusForm;
import com.mllfjn.simyys.character.status.StatusType;

class StatusLuZe extends Status {
    private static final String StatusName = "麓泽";

    private StatusLuZe(Character character) {
        super(StatusName, character);
        type(StatusType.BUFF, StatusForm.YIN_JI);
        enableStackAndAddStack(4);
        displayNameAndStack();
    }

    static void addStack(Character character) {
        character.getStatus(StatusLuZe.class)
                .ifPresentOrElse(
                        Status::addStack,
                        () -> character.addStatus(new StatusLuZe(character))
                );
    }
}
