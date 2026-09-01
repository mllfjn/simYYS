package com.mllfjn.simyys.character.list.sp.luwan;

import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.status.Status;
import com.mllfjn.simyys.character.status.StatusForm;
import com.mllfjn.simyys.character.status.StatusType;

class StatusLuZe extends Status {
    private static final String StatusName = "麓泽";

    private int stack = 1;

    private StatusLuZe(Character character) {
        super(StatusName, character);
        type(StatusType.BUFF, StatusForm.YIN_JI);
        display(() -> StatusName + stack);
    }

    static void addStack(Character character) {
        character.getStatus(StatusLuZe.class)
                .ifPresentOrElse(
                        status -> {
                            if (status.stack < 4) {
                                status.stack++;
                            }
                        },
                        () -> character.addStatus(new StatusLuZe(character))
                );
    }

    public int getStack() {
        return stack;
    }
}
